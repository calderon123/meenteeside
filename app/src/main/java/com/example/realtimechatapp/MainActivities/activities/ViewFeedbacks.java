package com.example.realtimechatapp.MainActivities.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.realtimechatapp.MainActivities.adapters.FeedBackAdapter;
import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.models.RateDetails;
import com.example.realtimechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("ALL")
public class ViewFeedbacks extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    public  static Context context;
    private String id;
    EditText comments;
    DatabaseReference rateDetailRef;
    DatabaseReference userMentorInfo;
    DatabaseReference databaseReference;
    ImageButton btn_send,back;
    RecyclerView recyclerView;
    FeedBackAdapter feedBackAdapter;
    List<RateDetails> rateDetailsList;
    Toolbar toolbar;

    public static  Counselors counselor = null;
    double ratingStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedbacks);
        context = this;

        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedbacks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ViewFeedbacks.this, MentorProfile.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                });
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        rateDetailsList = new ArrayList<>();

        readUsers();
    }
    private void readUsers() {
        final String userid = getIntent().getStringExtra("id");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RateDetails")
                .child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RateDetails rateDetails = snapshot.getValue(RateDetails.class);
                    rateDetailsList.add(rateDetails);
                }
                feedBackAdapter = new FeedBackAdapter(getApplicationContext(), rateDetailsList);
                recyclerView.setAdapter(feedBackAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button btn_feedback = findViewById(R.id.btn_feedback);

        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewFeedbacks.this);

                View view = getLayoutInflater().inflate(R.layout.button_feedback, null);
                final SmileRating smileRating = view.findViewById(R.id.ratingView);
                comments = view.findViewById(R.id.comments);
                btn_send = view.findViewById(R.id.btn_send);
                smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                    @Override
                    public void onSmileySelected(int smiley, boolean reselected) {
                        switch (smiley) {
                            case SmileRating.BAD:
                                Toast.makeText(ViewFeedbacks.this, "BAD", Toast.LENGTH_SHORT).show();
                                break;
                            case SmileRating.GOOD:
                                Toast.makeText(ViewFeedbacks.this, "GOOD", Toast.LENGTH_SHORT).show();
                                break;
                            case SmileRating.GREAT:
                                Toast.makeText(ViewFeedbacks.this, "GREAT", Toast.LENGTH_SHORT).show();
                                ;
                                break;
                            case SmileRating.OKAY:
                                Toast.makeText(ViewFeedbacks.this, "OKAY", Toast.LENGTH_SHORT).show();
                                break;
                            case SmileRating.TERRIBLE:
                                Toast.makeText(ViewFeedbacks.this, "TERRIBLE", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
                    @Override
                    public void onRatingSelected(int level, boolean reselected) {

                        ratingStars = level;

                    }
                });
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String comments_ = comments.getText().toString();
                        if (!comments_.equals("")) {
                            submitRateDetails(firebaseUser.getUid(),comments_);
                        } else {
                            Toast.makeText(ViewFeedbacks.this, "Please give some feedback", Toast.LENGTH_SHORT).show();
                        }
                        comments.setText("");
                    }
                });

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        userMentorInfo = FirebaseDatabase.getInstance().getReference("UserMentor");
        FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        String pushid = FirebaseDatabase.getInstance().getReference("UserMentor").push().getKey();
        rateDetailRef = FirebaseDatabase.getInstance().getReference("RateDetails")
                .child(userid).child(pushid);


    }
    private void submitRateDetails(final String userMentorId,final String comments) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Map<String,Object> hashMap = new HashMap<>();

        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH).format(new Date());
        hashMap.put("comments", comments);
        hashMap.put("date", date);
        hashMap.put("rate",String.valueOf(ratingStars));
        hashMap.put("id", userMentorId);
        rateDetailRef.setValue(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ViewFeedbacks.this , "Thanks for rating",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewFeedbacks.this, "Rate failed!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
