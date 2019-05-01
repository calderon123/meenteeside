package com.example.realtimechatapp.MainActivities.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import com.example.realtimechatapp.MainActivities.models.Counselors;

import com.example.realtimechatapp.MainActivities.models.RateDetails;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;


import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class MentorProfile extends AppCompatActivity {

    private Toolbar toolbar;
    public  static Context context;
    private CircleImageView profile_image;
    private TextView fullname,expertise,emmail,feedback_count,feedbacks,mentees,mentees_count,ratingAve,rate_ave;
    FirebaseUser firebaseUser;
    Button back;
    private String id;
    RatingBar rating_bar;



    public static  Counselors counselor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_profile);
        context = this;

        profile_image = (CircleImageView)findViewById(R.id.profile_image);
        mentees = findViewById(R.id.mentees);
        feedbacks = findViewById(R.id.feedbacks);
        rate_ave = findViewById(R.id.rate_ave);
        fullname = findViewById(R.id.fullname);
        expertise = findViewById(R.id.expertise);
        emmail = findViewById(R.id.email);
        mentees_count = findViewById(R.id.mentees_count);

        rating_bar = findViewById(R.id.rating_bar);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        feedback_count = findViewById(R.id.feedback_count);
        RelativeLayout view_feedbacks =findViewById(R.id.view_feedbacks);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        final String userid = getIntent().getStringExtra("id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("RateDetails").child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        double total = 0.0;
                        double count = 0.0;
                        int average = 0;
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            RateDetails rateDetails = ds.getValue(RateDetails.class);

                            double rating = Double.parseDouble(rateDetails.getRate());
                            total = total + rating;
                            count = count + 1;
                            average = (int) (total / count);


                            final String ave = Integer.toString(average);

                            String ave_ave = Double.toString(Double.parseDouble(ave));
                            rate_ave.setText(ave_ave);
                            rating_bar.setRating(new Float(ave_ave));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
                                if (userMentor.getImageURL().equals("default")){
                                    profile_image.setImageResource(R.drawable.ic_account_circle_black_24dp);
                                }else {
                                    Glide.with(getApplicationContext()).load(userMentor.getImageURL()).into(profile_image);
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
                        feedbacks.setText("Feedbacks");

                    }else if(countFeedbacks < 1) {
                        feedback_count.setText(Integer.toString(countFeedbacks));
                        feedbacks.setText("Feedback");
                    }

                }else {
                    feedback_count.setText("No Feedbacks");
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
                        mentees.setText("Mentees");
                    }else if(countMentees < 1) {
                        mentees_count.setText(Integer.toString(countMentees));
                        mentees.setText("Mentee");
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