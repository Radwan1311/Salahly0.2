package com.radwan1311.salahly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Users_Profiles_Preview extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public LoginButton facelogin ;

    DatabaseReference myRef ;
    RecyclerView recyclerView ;
    ArrayList<DatabaseData>listfinal ;
    CardAdapter cardAdapter;
    private long backPressedTime;
    private Toast backToast ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Users_Profiles_Preview.this , JobCard.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listfinal = new ArrayList<DatabaseData>();


        myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    DatabaseData list =dataSnapshot.getValue(DatabaseData.class);
                    listfinal.add(list);
                }

                cardAdapter = new CardAdapter(Users_Profiles_Preview.this , listfinal);
                recyclerView.setAdapter(cardAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Users_Profiles_Preview.this , databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });








    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                super.onBackPressed();
                return;

            } else {
                backToast = Toast.makeText(Users_Profiles_Preview.this , " Press Back Again To Exit ", Toast.LENGTH_SHORT);
                backToast.show();
            }

            backPressedTime = System.currentTimeMillis();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //startActivity(new Intent(Users_Profiles_Preview.this , SettingsActivity.class));
        }else if (id == R.id.action_Logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Users_Profiles_Preview.this , "Logged Out Successfully " , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Users_Profiles_Preview.this , LoginActivity.class));
            LoginManager.getInstance().logOut();
            FacebookSdk.sdkInitialize(getApplicationContext());
            finish();

        }else if (id==R.id.action_My_Profile){
            startActivity(new Intent(Users_Profiles_Preview.this , ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
    public void logOut() {
        facelogin.performClick();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(Users_Profiles_Preview.this , Users_Profiles_Preview.class));
        } else if (id == R.id.nav_MyProfile) {
            startActivity(new Intent(Users_Profiles_Preview.this , ProfileActivity.class));

        } else if (id == R.id.nav_Settings) {
            //startActivity(new Intent(Users_Profiles_Preview.this , SettingsActivity.class));

        } else if (id == R.id.nav_Category) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id == R.id.nav_Logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(Users_Profiles_Preview.this , "Logged Out Successfully " , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Users_Profiles_Preview.this , LoginActivity.class));
            LoginManager.getInstance().logOut();
            FacebookSdk.sdkInitialize(getApplicationContext());
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
