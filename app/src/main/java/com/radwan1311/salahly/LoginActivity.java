package com.radwan1311.salahly;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.radwan1311.salahly.Helper.LocaleHelper;

import java.util.List;

import io.paperdb.Paper;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private EditText loginEmail ;
    private EditText Password ;
    private FirebaseAuth firebaseAuth ;
    private FirebaseUser firebaseUser ;
    private ProgressDialog progressDialog ;
    private Button Login ;
    private int counter = 5 ;
    private int counter2 = 10 ;
    private ImageView googlelogin ;
    public LoginButton facelogin ;
    private GoogleSignInClient mGoogleSignInClient ;
    private CallbackManager callbackManager ;
    String[] perms ;
    private TextView englishLanguage ,arabicLanguage ;
    boolean state = false ;
    boolean mark ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        Password = (EditText) findViewById(R.id.Password);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        Login = (Button) findViewById(R.id.normalLoginButton);
        googlelogin = (ImageView) findViewById(R.id.googleLoginButton);
        facelogin = (LoginButton) findViewById(R.id.faceBookLoginButton);
        arabicLanguage =(TextView) findViewById(R.id.arabicLanguage);
        englishLanguage =(TextView) findViewById(R.id.englishLanguage);


        Paper.init(this);

        String language = Paper.book().read("language");

        if (language == null)
            Paper.book().write("language" , "en") ;
        updateview((String)Paper.book().read("language"));



            englishLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write("language", "en");
                    updateview((String) Paper.book().read("language"));
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    englishLanguage.setEnabled(false);
                    arabicLanguage.setEnabled(true);
                    finish();

                }
            });

            arabicLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Paper.book().write("language", "ar");
                    updateview((String) Paper.book().read("language"));
                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    arabicLanguage.setEnabled(false);
                    englishLanguage.setEnabled(true);
                    finish();

                }
            });


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser() ;
        progressDialog = new ProgressDialog(this);

        if (user != null){
            finish();
            startActivity(new Intent(LoginActivity.this , Users_Profiles_Preview.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getpermissions();

                if ( loginEmail.getText().toString().isEmpty() && Password.getText().toString().isEmpty() ) {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Email And Password ", Toast.LENGTH_SHORT).show();
                    counter2--;
                    if (counter2 == 0) {
                        Login.setEnabled(false);
                        Toast.makeText(LoginActivity.this, "Please Restart The App To Be Able To Login Again  ", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    validate(loginEmail.getText().toString(), Password.getText().toString());
                }
        }

        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this,gso);

        if (user == null) {
            googlelogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getpermissions();
                    progressDialog.setMessage("Logging in , Please Wait this May take a Second ");
                    progressDialog.show();
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, 11);
                }


            });
            progressDialog.dismiss();

        }

     if (user == null) {
         FacebookSdk.sdkInitialize(getApplicationContext());
         callbackManager = CallbackManager.Factory.create();

         facelogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getpermissions();
                 LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                     @Override
                     public void onSuccess(LoginResult loginResult) {
                         progressDialog.setMessage("Logging in , Please Wait this May take a Second ");
                         progressDialog.show();
                       handelFaceBookToken(loginResult.getAccessToken());

                     }

                     @Override
                     public void onCancel() {

                     }

                     @Override
                     public void onError(FacebookException error) {
                         Toast.makeText(LoginActivity.this , error.getMessage() , Toast.LENGTH_SHORT).show();
                         progressDialog.dismiss();

                     }
                 });
             }
         });
     }

    }

    private void updateview(String lang) {
        Context context = LocaleHelper.setLocale(this , lang);
        Resources resources = context.getResources();
    }

    private void handelFaceBookToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()) {
                          Toast.makeText(LoginActivity.this, "Logged In Successfully ", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(LoginActivity.this , Users_Profiles_Preview.class));
                          firebaseUser = firebaseAuth.getCurrentUser();

                      }else {
                          Toast.makeText(LoginActivity.this, "Logged In Failed , Could not Register , Please Try Again ", Toast.LENGTH_SHORT).show();
                          progressDialog.dismiss();

                      }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 11) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this , Users_Profiles_Preview.class));

                            Toast.makeText(LoginActivity.this , "Logged In Successfulliy " , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this , "Login Failed Try Again " , Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    public void create(View view){
        Intent intent = new Intent(LoginActivity.this ,CreateUserProfile.class ) ;
        startActivity(intent);
    }

    private void validate(final String LoginEmail , final String LoginPassword){
        progressDialog.setMessage("Logging in , Please Wait this May take a Second ");
        progressDialog.show();
        getpermissions();
        firebaseAuth.signInWithEmailAndPassword(LoginEmail,LoginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    checkEmailVerification();
                } else{
                    Toast.makeText(LoginActivity.this," Login Failed , Please check Your Email Or Password  " ,Toast.LENGTH_SHORT).show();
                    counter--;
                    progressDialog.dismiss();

                    if (counter == 0){
                        Login.setEnabled(false);
                        Toast.makeText(LoginActivity.this , "Please wait 5 Seconds and try again " , Toast.LENGTH_SHORT).show();

                        CountDownTimer countDownTimer = new CountDownTimer(6000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                Login.setEnabled(true);
                                counter = 5 ;
                            }
                        }.start();
                    }
                }
            }
        });
    }

    @AfterPermissionGranted(1)
    private void getpermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            state = true ;
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this and that",
                    1, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(PackageManager.PERMISSION_GRANTED)&&Manifest.permission.ACCESS_FINE_LOCATION.equals( PackageManager.PERMISSION_GRANTED) && Manifest.permission.READ_EXTERNAL_STORAGE.equals(PackageManager.PERMISSION_GRANTED)) {

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();


        }
    }



    public void resetPassword(View view){
        Intent intent = new Intent(LoginActivity.this , ForgetPassword.class);
        startActivity(intent);
    }


    private void checkEmailVerification(){
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        boolean emailFlag = firebaseUser.isEmailVerified();

        if (emailFlag){
            finish();
            startActivity(new Intent(LoginActivity.this , Users_Profiles_Preview.class));
            Toast.makeText(LoginActivity.this, " Logged in successfully  ", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(LoginActivity.this , "Please check your Email and find the sent Verification Email to verify your Email " , Toast.LENGTH_LONG).show();
            firebaseAuth.getInstance().signOut();
        }
    }


    @Override
    protected void attachBaseContext (Context newbase){
        super.attachBaseContext(LocaleHelper.onAttach(newbase , "en"));

    }

}
