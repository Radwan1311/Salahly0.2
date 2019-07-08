package com.radwan1311.salahly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private Button ResetButton ;
    private TextView ResetEmail ;
    private FirebaseAuth firebaseAuth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ResetButton = (Button) findViewById(R.id.PasswordResetButton) ;
        ResetEmail = (TextView) findViewById(R.id.PasswordResetEmail);
        firebaseAuth = FirebaseAuth.getInstance() ;

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = ResetEmail.getText().toString().trim();

                if (userEmail.isEmpty() || userEmail.equals("")){
                    Toast.makeText(ForgetPassword.this, "Please Enter A Registered / Valid Email ", Toast.LENGTH_SHORT).show();

                }else {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgetPassword.this, "Password Reset Email Has Been Send ", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetPassword.this , LoginActivity.class));
                            }else {
                                Toast.makeText(ForgetPassword.this, "Password Reset Failed , Please check Your Email And Try Again  ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }


}
