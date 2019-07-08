package com.radwan1311.salahly;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


public class JobCard extends AppCompatActivity {

    private Button SaveButton;
    private EditText name, city, phone, address, job;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private ImageView profileImage, WorkImage1, WorkImage2, WorkImage3;
    private String Username, UserPhone, UserCity, Userjob, Useraddress;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Uri imageUrl, imageUrl2, imageUrl3, imageUrl4;
    private FirebaseStorage firebaseStorage;
    private ArrayList<Uri> images = new ArrayList<>();
    final ArrayList<String> strDownloadUr = new ArrayList<>();
    private UploadTask uploadTask ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_job_card);
        SaveButton = findViewById(R.id.EditProfileSaveButton);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");


        profileImage = (ImageView) findViewById(R.id.EditProfileImage);
        WorkImage1 = (ImageView) findViewById(R.id.WorkImage1);
        WorkImage2 = (ImageView) findViewById(R.id.WorkImage2);
        WorkImage3 = (ImageView) findViewById(R.id.WorkImage3);


        name = (EditText) findViewById(R.id.EditProfileName);
        city = (EditText) findViewById(R.id.EditProfileCity);
        phone = (EditText) findViewById(R.id.EditProfilePhoneNumber);
        address = (EditText) findViewById(R.id.EditProfileAddress);
        job = (EditText) findViewById(R.id.EditProfileJobDiscription);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 1);

            }
        });

        WorkImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 2);

            }
        });

        WorkImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 3);

            }
        });
        WorkImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 4);

            }
        });


        SaveButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                SaveButton.setEnabled(false);
                Toast.makeText(JobCard.this , "Creating Your Card ,This May take A Second" , Toast.LENGTH_LONG).show();
                setup();
                sendToDataBase();

            }

        });
    }


    private String getExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void cardbackbutton(View view) {
        startActivity(new Intent(JobCard.this,Users_Profiles_Preview.class));
        finish();
    }

    private void setup() {

        Username = name.getText().toString();
        Useraddress = address.getText().toString();
        UserCity = city.getText().toString();
        Userjob = job.getText().toString();
        UserPhone = phone.getText().toString();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null && data != null) {
            imageUrl = data.getData();
            Picasso.get().load(imageUrl).into(profileImage);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data.getData() != null && data != null) {
            images.add(data.getData());
            imageUrl2 = data.getData();
            Picasso.get().load(imageUrl2).into(WorkImage1);
        } else if (requestCode == 3 && resultCode == RESULT_OK && data.getData() != null && data != null) {
            imageUrl3 = data.getData();
            images.add(data.getData());
            Picasso.get().load(imageUrl3).into(WorkImage2);
        } else if (requestCode == 4 && resultCode == RESULT_OK && data.getData() != null && data != null) {
            imageUrl4 = data.getData();
            images.add(data.getData());
            Picasso.get().load(imageUrl4).into(WorkImage3);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void sendToDataBase() {
        setup();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(firebaseAuth.getUid());

        if (Username.isEmpty() || UserCity.isEmpty() || Userjob.isEmpty() || Useraddress.isEmpty() || UserPhone.isEmpty()) {
            Toast.makeText(JobCard.this, " Please complete all the Required Data ", Toast.LENGTH_LONG).show();

        } else {
            upload();

        }
    }

    private void upload() {
        int i;
        for (i = 0; i < images.size(); i++) {
            final StorageReference fileReference = storageReference.child(firebaseAuth.getUid()).child(System.currentTimeMillis() + "." + getExtension(images.get(i)));
           if (i == 0) {
               final UploadTask uploadTaskImages = fileReference.putFile(imageUrl2);
               Task<Uri> urlTask = uploadTaskImages.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                   @Override
                   public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       return fileReference.getDownloadUrl();
                   }
               }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                   @Override
                   public void onComplete(@NonNull Task<Uri> task) {
                       if (task.isSuccessful()) {
                           Uri downloadUriImages = task.getResult();
                           strDownloadUr.add(downloadUriImages.toString());

                       }
                   }
               });

           }if (i == 1) {
                final UploadTask uploadTaskImages = fileReference.putFile(imageUrl3);
                Task<Uri> urlTask = uploadTaskImages.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUriImages = task.getResult();
                            strDownloadUr.add(downloadUriImages.toString());

                        }
                    }
                });

            }if (i == 2) {
                final UploadTask uploadTaskImages = fileReference.putFile(imageUrl4);
                Task<Uri> urlTask = uploadTaskImages.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUriImages = task.getResult();
                            strDownloadUr.add(downloadUriImages.toString());

                        }
                    }
                });

            }
        }

        if (i<3){
            switch(i) {
                case 2:
                    strDownloadUr.add("");
                    break;
                case 1:
                    strDownloadUr.add("");
                    strDownloadUr.add("");
                    break;
                case 0:
                    strDownloadUr.add("");
                    strDownloadUr.add("");
                    strDownloadUr.add("");
                    break;
            }
}



        final StorageReference fileReference = storageReference.child(firebaseAuth.getUid()).child(System.currentTimeMillis() + "." + getExtension(imageUrl));

        final UploadTask uploadTask = fileReference.putFile(imageUrl);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseData databaseData = new DatabaseData(Username, UserCity, UserPhone, Useraddress, Userjob, downloadUri.toString()
                            , strDownloadUr.get(0).toString(), strDownloadUr.get(1).toString(), strDownloadUr.get(2).toString());

                    myRef.setValue(databaseData);

                    Toast.makeText(JobCard.this , "Success" , Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(JobCard.this, "Failed To Create , Please Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
