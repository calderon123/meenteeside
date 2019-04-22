package com.example.realtimechatapp.MainActivities.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.example.realtimechatapp.MainActivities.models.Counselors;

import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class MentorProfile extends AppCompatActivity {


    public  static Context context;
    private CircleImageView profile_image;
    private TextView fullname,expertise,emmail,feedback_count,feedbacks,mentees_count;
    FirebaseUser firebaseUser;
    Button back;
    Toolbar toolbar;
    private String id;



    public static  Counselors counselor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_profile);
        context = this;

        profile_image = (CircleImageView)findViewById(R.id.profile_image);

        fullname = findViewById(R.id.fullname);
        expertise = findViewById(R.id.expertise);
        emmail = findViewById(R.id.email);
        mentees_count = findViewById(R.id.mentees_count);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        startActivity(new Intent(MentorProfile.this, MenteeMainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
        feedback_count = findViewById(R.id.feedback_count);
        feedbacks =findViewById(R.id.feedbacks);
        RelativeLayout view_feedbacks =findViewById(R.id.view_feedbacks);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        final String userid = getIntent().getStringExtra("id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        view_feedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MentorProfile.this, ViewFeedbacks.class);
                intent.putExtra("id", userid);
                startActivity(intent );
            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Add").child(firebaseUser.getUid())
                .child("counselor").child(userid);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Counselors counselors = dataSnapshot.getValue(Counselors.class);

                FirebaseDatabase.getInstance().getReference("UserMentor").
                        child(counselors.getId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserMentor userMentor = dataSnapshot.getValue(UserMentor.class);

                                emmail.setText(userMentor.getEmail());
                                fullname.setText(userMentor.getFullname());
                                expertise.setText(userMentor.getExpertise());
                                if (userMentor.getImageUrl().equals("default")){
                                    profile_image.setImageResource(R.drawable.ic_account_circle_black_24dp);
                                }else {
                                    Glide.with(getApplicationContext()).load(userMentor.getImageUrl()).into(profile_image);
                                }
                                 }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("RateDetails")
                .child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   int countFeedbacks = (int) dataSnapshot.getChildrenCount();
                    if (countFeedbacks >= 1){
                        feedback_count.setText(Integer.toString(countFeedbacks));
                        feedbacks.setText("FEEDBACKS");

                    }else if(countFeedbacks < 1) {
                        feedback_count.setText(Integer.toString(countFeedbacks));

                    }

                }else {
                    feedback_count.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference  databaseReference2 = FirebaseDatabase.getInstance().getReference("Add")
                .child(userid).child("mentees");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int countMentees = (int) dataSnapshot.getChildrenCount();
                    if (countMentees >= 1){
                        mentees_count.setText(Integer.toString(countMentees));

                    }else if(countMentees < 1) {
                        mentees_count.setText(Integer.toString(countMentees));

                    }

                }else {
                    feedback_count.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}