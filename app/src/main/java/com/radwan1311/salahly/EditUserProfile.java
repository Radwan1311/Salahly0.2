package com.radwan1311.salahly;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditUserProfile extends AppCompatActivity {

    private EditText editProfileName, editProfilePhoneNumber, editProfileJobDiscription, editProfileCity, editProfileAddress;
    private ImageView editProfileImage, editProfileWorkImage3, editProfileWorkImage2, editProfileWorkImage1;
    private TextView ResetPassword;
    private Button editProfileSaveButton, editProfileBackButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private Uri imageUrl;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null && data != null) {
            imageUrl = data.getData();
            Picasso.get().load(imageUrl).fit().centerCrop().into(editProfileImage);
            if (requestCode == 2 && resultCode == RESULT_OK && data.getData() != null && data != null) {
                imageUrl = data.getData();
                Picasso.get().load(imageUrl).into(editProfileWorkImage1);
            }
            if (requestCode == 3 && resultCode == RESULT_OK && data.getData() != null && data != null) {
                imageUrl = data.getData();
                Picasso.get().load(imageUrl).into(editProfileWorkImage2);
            }
            if (requestCode == 4 && resultCode == RESULT_OK && data.getData() != null && data != null) {
                imageUrl = data.getData();
                Picasso.get().load(imageUrl).into(editProfileWorkImage3);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_user_profile);


        editProfileAddress = findViewById(R.id.EditProfileAddress);
        editProfileCity = findViewById(R.id.EditProfileCity);
        editProfileName = findViewById(R.id.EditProfileName);
        editProfileJobDiscription = findViewById(R.id.EditProfileJobDiscription);
        editProfilePhoneNumber = findViewById(R.id.EditProfilePhoneNumber);

        ResetPassword = findViewById(R.id.resetPas);

        editProfileImage = findViewById(R.id.EditProfileImage);

        editProfileSaveButton = findViewById(R.id.EditProfileSaveButton);
        editProfileBackButton = findViewById(R.id.EditProfileBackButton);


        editProfileWorkImage1 = findViewById(R.id.editProfileWorkImage1);
        editProfileWorkImage2 = findViewById(R.id.editProfileWorkImage2);
        editProfileWorkImage3 = findViewById(R.id.editProfileWorkImage3);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        storageReference = firebaseStorage.getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseData databaseData = dataSnapshot.getValue(DatabaseData.class);
                editProfileAddress.setText(databaseData.getAddress());
                editProfileCity.setText(databaseData.getCity());
                editProfileName.setText(databaseData.getName());
                editProfileJobDiscription.setText(databaseData.getJob());
                editProfilePhoneNumber.setText(databaseData.getPhone());
                Picasso.get().load(databaseData.getImageUrl())
                        .fit().centerCrop().into(editProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditUserProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });

        editProfileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editProfileName.getText().toString();
                final String address = editProfileAddress.getText().toString();
                final String city = editProfileCity.getText().toString();
                final String job = editProfileJobDiscription.getText().toString();
                final String phone = editProfilePhoneNumber.getText().toString();


                final StorageReference fileReference = storageReference.child(firebaseAuth.getUid());

                final UploadTask uploadTask = fileReference.putFile(imageUrl);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            DatabaseData databaseData = new DatabaseData();//name, city, phone, address, job, downloadUri.toString()
                            databaseReference.setValue(databaseData);
                            Picasso.get().load(databaseData.getImageUrl())
                                    .fit().centerCrop().into(editProfileImage);
                            Toast.makeText(EditUserProfile.this, "Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditUserProfile.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                Toast.makeText(EditUserProfile.this, "Profile / Card Edited Successfully ", Toast.LENGTH_SHORT).show();
                finish();

            }
        });


        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 1);

            }
        });

        editProfileWorkImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 2);

            }
        });

        editProfileWorkImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 3);

            }
        });
        editProfileWorkImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent, "Select An Image "), 4);

            }
        });


        editProfileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditUserProfile.this, ProfileActivity.class));
                finish();
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditUserProfile.this, ForgetPassword.class));
                finish();
            }
        });

    }
}

