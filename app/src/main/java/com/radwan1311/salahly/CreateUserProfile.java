package com.radwan1311.salahly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateUserProfile extends AppCompatActivity {
    private Button saveButton ;
    private EditText emailEditText ;
    private EditText PasswordEditText ;
    private EditText PhoneEditText ;
    private Button back ;
    private FirebaseAuth fireBaseAuth ;






    public void back (View view){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_user_profile);
        setUpUi();

        fireBaseAuth = FirebaseAuth.getInstance() ;


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (validate()) {
                   // register to data base ...
                   String user_email = emailEditText.getText().toString().trim() ;
                   String user_Password = PasswordEditText.getText().toString().trim() ;

                    fireBaseAuth.createUserWithEmailAndPassword(user_email,user_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();

                            }else {
                                Toast.makeText(CreateUserProfile.this, "Registration failed , Please check your Information", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



               }

            }

        });

    }
    private boolean validate(){
        Boolean result = false ;

        String email = emailEditText.getText().toString();
        String password = PasswordEditText.getText().toString();
        String phone = PhoneEditText.getText().toString();

        if (email.isEmpty() && password.isEmpty() && phone.isEmpty()){
            Toast.makeText(this,"Please enter the required Data" ,Toast.LENGTH_SHORT).show();
        }else {
            result = true ;
        }
        return result ;

    }

    private void sendEmailVerification(){
        final FirebaseUser firebaseUser = fireBaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful()){
                     Toast.makeText(CreateUserProfile.this , "Registered successfully , Verification Email Has Been Send , lease check Your Email" , Toast.LENGTH_SHORT).show(); ;
                     fireBaseAuth.getInstance().signOut();
                     finish();
                     Intent intent = new Intent(CreateUserProfile.this, LoginActivity.class);
                     startActivity(intent);
                 }else {
                     Toast.makeText(CreateUserProfile.this , "Could not Send Verification Email , Please Try Again Later " , Toast.LENGTH_SHORT).show(); ;

                 }
                }
            });
        }
    }



    private void setUpUi (){
          saveButton = (Button) findViewById(R.id.saveButton) ;
          emailEditText = (EditText) findViewById(R.id.emailEditText);
          PasswordEditText = (EditText) findViewById(R.id.PasswordEditText);
          PhoneEditText = (EditText) findViewById(R.id.PhoneEditText);
    }
}
