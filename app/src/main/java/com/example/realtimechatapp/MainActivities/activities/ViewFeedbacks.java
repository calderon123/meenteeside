package com.example.realtimechatapp.MainActivities.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.realtimechatapp.MainActivities.models.Rate;
import com.example.realtimechatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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
    List<Rate> rateList;
    Toolbar toolbar;

    public static  final  String rate_detal_1= "RateDetails";
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
        rateList = new ArrayList<>();

        readUsers();
    }
    private void readUsers() {
        final String userid = getIntent().getStringExtra("id");
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RateDetails")
                .child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Rate rate= snapshot.getValue(Rate.class);

                    rateList.add(rate);
                }
                feedBackAdapter = new FeedBackAdapter(getApplicationContext(),rateList);
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
                SmileRating smileRating = view.findViewById(R.id.ratingView);
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
                                Toast.makeText(ViewFeedbacks.this, "GREAT", Toast.LENGTH_SHORT).show();;
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
                        submitRateDetails(id);

                    }
                });

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        userMentorInfo = FirebaseDatabase.getInstance().getReference("UserMentor");
        rateDetailRef = FirebaseDatabase.getInstance().getReference(rate_detal_1);


    }

    private void submitRateDetails(final String userMentorId) {

        final Rate rate = new Rate(context, "");
        rate.setRates(String.valueOf(ratingStars));
        rate.setComments(comments.getText().toString());
        final String push_id = FirebaseDatabase.getInstance().getReference("RateDetails").push().getKey();

        rateDetailRef.child(userMentorId)
                .push()
                .setValue(rate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        final String id = userMentorId;

                        rateDetailRef.child(userMentorId)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        double averageStars = 0.0;
                                        int count = 0;
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                        {
                                            Rate rate = postSnapshot.getValue(Rate.class);
                                            assert rate != null;
                                            averageStars+=Double.parseDouble(rate.getRates().trim());

                                            count++;
                                        }
                                        double finalAverage = averageStars/count;
                                        DecimalFormat df = new DecimalFormat("#.#");
                                        String valueUpdate = df.format(finalAverage);

                                        FirebaseUser firebaseUser =
                                                FirebaseAuth.getInstance().getCurrentUser();
                                        Map<String,Object> userMentorUpdateRate = new HashMap<>();
                                        userMentorUpdateRate.put("rates", valueUpdate);
                                        userMentorUpdateRate.put("id", firebaseUser.getUid());
                                        rateDetailRef.child(counselor.getId())
                                                .child(push_id)
                                                .updateChildren(userMentorUpdateRate);
                                        comments.setText("");

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
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
