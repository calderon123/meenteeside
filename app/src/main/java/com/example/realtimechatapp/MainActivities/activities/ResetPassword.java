package com.example.realtimechatapp.MainActivities.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realtimechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        final EditText send_email= findViewById(R.id.send_email);
        Button btn_reset = findViewById(R.id.btn_reset);


        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = send_email.getText().toString();

                if (email.equals("")){
                    Toast.makeText(ResetPassword.this ,"All fields required", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPassword.this, "Please check your Email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPassword.this, StartActivity.class));
                        }else {
                            String error  = task.getException().getMessage();
                            Toast.makeText(ResetPassword.this, error, Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }
            }
        });

    }
}
