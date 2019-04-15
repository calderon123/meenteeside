package com.example.realtimechatapp.MainActivities.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realtimechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

@SuppressWarnings("ALL")
public class StartActivity extends AppCompatActivity {

    private Button login, register;

    private FirebaseUser firebaseUser;
    private MaterialEditText fullname,email,password,confirmpassword;
    private Button btn_register;
    private ImageView profile_image;
    private TextView forgot_password;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Uri image_uri;
    static int PreqCode =1;
    static int REQUESCODE = 1;
    private ProgressBar progressBar;
    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MenteeMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        FirebaseApp.initializeApp(getApplicationContext());

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder  =new AlertDialog.Builder(StartActivity.this);
                View view = getLayoutInflater().inflate(R.layout.activity_login_mentee,null);

                final MaterialEditText email = view.findViewById(R.id.email);
                final MaterialEditText password =view.findViewById(R.id.password);
                Button btn_login = view.findViewById(R.id.btn_login);
                final ProgressBar progressBar = view.findViewById(R.id.progressBar);
                final FirebaseAuth auth = FirebaseAuth.getInstance();

                forgot_password = view.findViewById(R.id.forgot_password);


                forgot_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(StartActivity.this, ResetPassword.class));
                    }
                });

                btn_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        String txt_email = email.getText().toString();
                        String txt_password = password.getText().toString();



                        if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                            Toast.makeText(StartActivity.this,"All fields required!", Toast.LENGTH_SHORT).show();
                        }else {

                            progressBar.setVisibility(View.VISIBLE);
                            auth.signInWithEmailAndPassword(txt_email, txt_password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            progressBar.setVisibility(view.GONE);
                                            if (task.isSuccessful()){
                                                Intent intent = new Intent(StartActivity.this, MenteeMainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();

                                            }else{
                                                Toast.makeText(StartActivity.this, "Authentication failed!" , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
                builder.setView(view);
                AlertDialog  dialog = builder.create();
                dialog.show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(StartActivity.this, RegisterMenteeActivity.class));
            }
        });

    }
}
