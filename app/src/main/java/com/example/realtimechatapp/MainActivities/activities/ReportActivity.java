package com.example.realtimechatapp.MainActivities.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.realtimechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    private EditText report_dscrpt,report_type;
    private Button btn_send_report,upload_photo;
    private ImageView image;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Uri image_uri;
    static int PreqCode =1;
    private  static int REQUESCODE = 1;
    private ProgressBar progressBar;
    private StorageTask uploadTask;
    ProgressDialog pd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        image = findViewById(R.id.image_uploaded);
        upload_photo = findViewById(R.id.btn_upload);
        btn_send_report = findViewById(R.id.btn_send_report);
        report_dscrpt = findViewById(R.id.report_dscrpt);
        report_type = findViewById(R.id.report_type);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Reports")
                .child(firebaseUser.getUid());

        btn_send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_report_dscrpt = report_dscrpt.getText().toString();
                String report_type_ = report_type.getText().toString();


                if(TextUtils.isEmpty(txt_report_dscrpt) || TextUtils.isEmpty(report_type_)){
                    Toast.makeText(ReportActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
                    btn_send_report.setVisibility(View.VISIBLE);

                }else {
                    sendReport(txt_report_dscrpt,report_type_);
                }
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("users_photo");
        upload_photo.setOnClickListener(new View.OnClickListener() {
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
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(ReportActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ReportActivity.this
                    ,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(ReportActivity.this, "Please accept for " +
                        "for required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(ReportActivity.this
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE &&
                data != null){

            image_uri = data.getData();
            image.setImageURI(image_uri);
            if(image.getDrawable() == null){
                Toast.makeText(ReportActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
            };
        }
    }
    private void sendReport(String report_dscrpt, String report_type) {


        updateUserInfo(report_dscrpt,report_type ,image_uri);
    }

    private void updateUserInfo(final String report_dscrpt, final String report_type, final Uri image_uri){
        StorageReference mStorage = FirebaseStorage.getInstance().getReference()
                .child("report_photos");
        final StorageReference imageFilePath = mStorage.child(image_uri.getLastPathSegment());
        imageFilePath.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();
                        Date date = new Date();
                        Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
                        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                        String stringdate = dt.format(newDate);

                        reference = FirebaseDatabase.getInstance().getReference("Reports");
                        String pushid = FirebaseDatabase.getInstance().getReference("Reports")
                                .push().getKey();
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("id",userid);
                        hashMap.put("date_reported",stringdate);
                        hashMap.put("imageURL", String.valueOf(uri));
                        hashMap.put("report_type",report_type);
                        hashMap.put("report_dscrpt",report_dscrpt);

                        reference.child(pushid).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ReportActivity.this,
                                            "Report Sent" , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ReportActivity.this, ReportActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                });


            }
        });
    }

}
