package com.example.realtimechatapp.MainActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.realtimechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterMenteeActivity extends AppCompatActivity {

    MaterialEditText fullname,email,password,confirmpassword;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mentee);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);
        confirmpassword = findViewById(R.id.confirmpassword);

        auth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_fullname = fullname.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_confirmpass = confirmpassword.getText().toString();


                if(TextUtils.isEmpty(txt_fullname) || TextUtils.isEmpty(txt_email) ||
                        TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_confirmpass)){
                    Toast.makeText(RegisterMenteeActivity.this, "All fields required!", Toast.LENGTH_SHORT).show();
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
    private void  register (final String fullname, final String email, final String password){


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("UserMentee").child(userid);
                            FirebaseAuth auth = FirebaseAuth.getInstance();

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("fullname",fullname);
                            hashMap.put("email",email);
                            hashMap.put("password",password);




                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                       Intent intent = new Intent(RegisterMenteeActivity.this, MenteeMainActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(intent);
                                       finish();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterMenteeActivity.this, "You cant register wiht this email and password" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
