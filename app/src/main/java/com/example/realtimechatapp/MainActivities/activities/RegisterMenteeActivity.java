package com.example.realtimechatapp.MainActivities.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.realtimechatapp.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterMenteeActivity extends AppCompatActivity {

    private MaterialEditText fullname,email,password,confirmpassword;
    private Button btn_register;
    private ImageView profile_image;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Uri image_uri;
    static int PreqCode =1;
    static int REQUESCODE = 1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mentee);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");


        profile_image = findViewById(R.id.profile_image);

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        confirmpassword = findViewById(R.id.confirmpassword);
        progressBar = findViewById(R.id.progressing);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermission();
                }
                else {
                    openGallery();
                }
            }
        });


        auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                String txt_fullname = fullname.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmpass = confirmpassword.getText().toString();


                if(TextUtils.isEmpty(txt_fullname) || TextUtils.isEmpty(txt_email) ||
                        TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirmpass)){
                    Toast.makeText(RegisterMenteeActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
                        btn_register.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                }else if (txt_password.length() < 6){
                    Toast.makeText(RegisterMenteeActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                }else if(!txt_password.equals(txt_confirmpass)) {
                    Toast.makeText(RegisterMenteeActivity.this, "Password not matched!", Toast.LENGTH_SHORT).show();
                } else {
                    register(txt_fullname,txt_email,txt_password);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE &&
        data != null){

            image_uri = data.getData();
            profile_image.setImageURI(image_uri);
        }
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterMenteeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterMenteeActivity.this
            ,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterMenteeActivity.this, "Please accept for " +
                        "for required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(RegisterMenteeActivity.this
                ,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PreqCode);
            }
        }
        else {
            openGallery();
        }

    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }


    private void  register (final String fullname, final String email, final String password){


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//
                            updateUserInfo(email,fullname ,image_uri,auth.getCurrentUser());

                        }else{
                            Toast.makeText(RegisterMenteeActivity.this, "You cant register wiht this email and password" , Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void updateUserInfo(final String email, final String fullname, final Uri image_uri, final FirebaseUser currentUser) {

        StorageReference mStorage = FirebaseStorage.getInstance().getReference()
                .child("users_photo");
        final StorageReference imageFilePath = mStorage.child(image_uri.getLastPathSegment());
        imageFilePath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullname)
                                .setPhotoUri(uri)
                                .build();



                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            FirebaseUser firebaseUser = auth.getCurrentUser();
                                                assert firebaseUser != null;
                                                String userid = firebaseUser.getUid();

                                                reference = FirebaseDatabase.getInstance().getReference("UserMentee").child(userid);
                                                FirebaseAuth auth = FirebaseAuth.getInstance();

                                                HashMap<String,String> hashMap = new HashMap<>();
                                                hashMap.put("id",userid);
                                                hashMap.put("fullname",fullname);
                                                hashMap.put("email",email);
                                                hashMap.put("status","offline");

                                            progressBar.setVisibility(View.VISIBLE);
                                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(RegisterMenteeActivity.this,
                                                                    "Registration Successful" , Toast.LENGTH_SHORT).show();
                                                           Intent intent = new Intent(RegisterMenteeActivity.this, MenteeMainActivity.class);
                                                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           startActivity(intent);
                                                           finish();
                                                        }
                                                    }
                                                });

                                        }
                                    }
                                });

                    }
                });
            }
        });
    }
}
