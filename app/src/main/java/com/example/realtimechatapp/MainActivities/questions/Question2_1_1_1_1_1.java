package com.example.realtimechatapp.MainActivities.questions;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.activities.MenteeMainActivity;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.MainActivities.adapters.UserMentorAdapter;
import com.example.realtimechatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Question2_1_1_1_1_1 extends Activity{


    private RecyclerView recyclerView;
    private ImageButton home,back;
    private UserMentorAdapter userMentorAdapter;
    private List<UserMentor> mUsermentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2_1_1_1_1_1);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



        home = findViewById(R.id.home);
        back = findViewById(R.id.back);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2_1_1_1_1_1.this, MenteeMainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
               finish();
            }
        });

        home = findViewById(R.id.home);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Question2_1_1_1_1_1.this, Question2_1_1_1_1.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mUsermentor = new ArrayList<>();

        readUsers();

    }

    private void readUsers() {

        final String expertise= "Mental Health Counselor";
        final String rating ="500-1000/hour";
        final String availability ="Once a week";
       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserMentor");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserMentor userMentor = snapshot.getValue(UserMentor.class);

                    if (userMentor.getExpertise().equals(expertise)){
                        mUsermentor.add(userMentor);
                    }else if (userMentor.getRate().equals(rating)){
                        mUsermentor.add(userMentor);
                    }else if (userMentor.getAvailability().equals(availability)){
                        mUsermentor.add(userMentor);
                    }
                }
                userMentorAdapter = new UserMentorAdapter(getApplicationContext(),mUsermentor);
                recyclerView.setAdapter(userMentorAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        databaseReference.orderByChild("rating").equalTo(rating).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    UserMentor userMentor = snapshot.getValue(UserMentor.class);
//
//                    mUsermentor.add(userMentor);
//                }
//                userMentorAdapter = new UserMentorAdapter(getApplicationContext(),mUsermentor);
//                recyclerView.setAdapter(userMentorAdapter);
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        databaseReference.orderByChild("availability").equalTo(availability).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    UserMentor userMentor = snapshot.getValue(UserMentor.class);
//
//                    mUsermentor.add(userMentor);
//                }
//                userMentorAdapter = new UserMentorAdapter(getApplicationContext(),mUsermentor);
//                recyclerView.setAdapter(userMentorAdapter);
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }





}