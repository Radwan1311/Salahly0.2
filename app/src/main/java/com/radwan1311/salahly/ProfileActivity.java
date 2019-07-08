package com.radwan1311.salahly;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profilePic , ProfileWorkImage1 ,ProfileWorkImage2 , ProfileWorkImage3;
    private TextView profilename, profilejob, profileaddress, profilecity, profilephone, EditUserProfile , NoCardView , WorkImage , view5 , view8 , view10;
    private FirebaseAuth firebaseAuth;
    private Button NoCardButton ;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        profileaddress = (TextView) findViewById(R.id.profileAddress);
        profilecity = (TextView) findViewById(R.id.profileCity);
        profilejob = (TextView) findViewById(R.id.Profilejob);
        profilename = (TextView) findViewById(R.id.profileName);
        profilephone = (TextView) findViewById(R.id.profilePhone);
        profilePic = (ImageView) findViewById(R.id.profileImage);
        EditUserProfile = (TextView) findViewById(R.id.EditProfile);
        NoCardView = (TextView) findViewById(R.id.NoCardView);
        WorkImage = (TextView)findViewById(R.id.workImagesText);
        view5 = (TextView)findViewById(R.id.textView5);
        view8 = (TextView)findViewById(R.id.textView8);
        view10 = (TextView)findViewById(R.id.textView10);




        ProfileWorkImage1 = findViewById(R.id.ProfileWorkImage1);
        ProfileWorkImage2= findViewById(R.id.editProfileWorkImage2) ;
        ProfileWorkImage3 = findViewById(R.id.editProfileWorkImage3);


        NoCardButton = (Button) findViewById(R.id.NoCardbutton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        databaseReference = firebaseDatabase.getReference("Users").child(firebaseAuth.getUid());
        storageReference = firebaseStorage.getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    DatabaseData databaseData = dataSnapshot.getValue(DatabaseData.class);
                    profileaddress.setText(""+ databaseData.getAddress());
                    profilecity.setText("" + databaseData.getCity());
                    profilename.setText(databaseData.getName());
                    profilejob.setText(databaseData.getJob());
                    profilephone.setText("" + databaseData.getPhone());

                    NoCardButton.setEnabled(false);
                    NoCardButton.setVisibility(View.INVISIBLE);

                    NoCardView.setEnabled(false);
                    NoCardView.setVisibility(View.INVISIBLE);


                    Picasso.get().load(databaseData.getImageUrl())
                            .fit().centerCrop().into(profilePic);
                    Picasso.get().load(databaseData.getImageUrl2())
                            .fit().centerCrop().into(ProfileWorkImage1);
                    Picasso.get().load(databaseData.getImageUrl3())
                            .fit().centerCrop().into(ProfileWorkImage2);
                    Picasso.get().load(databaseData.getImageUrl4())
                            .fit().centerCrop().into(ProfileWorkImage3);


                }else {
                    profileaddress.setVisibility(View.GONE);
                    profilecity.setVisibility(View.GONE);
                    profilename.setVisibility(View.GONE);
                    profilejob.setVisibility(View.GONE);
                    profilephone.setVisibility(View.GONE);
                    profilePic.setVisibility(View.GONE);
                    ProfileWorkImage1.setVisibility(View.GONE);
                    ProfileWorkImage2.setVisibility(View.GONE);
                    ProfileWorkImage3.setVisibility(View.GONE);
                    WorkImage.setVisibility(View.GONE);
                    EditUserProfile.setVisibility(View.GONE);
                    view5.setVisibility(View.GONE);
                    view8.setVisibility(View.GONE);
                    view10.setVisibility(View.GONE);


                    NoCardButton.setEnabled(true);
                    NoCardButton.setVisibility(View.VISIBLE);

                    NoCardView.setEnabled(true);
                    NoCardView.setVisibility(View.VISIBLE);

                    NoCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ProfileActivity.this , JobCard.class));
                            finish();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        EditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditUserProfile.class));
                finish();
            }
        });

    }

}

