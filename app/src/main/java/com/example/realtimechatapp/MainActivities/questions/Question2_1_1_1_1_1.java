package  com.example.realtimechatapp.MainActivities.questions;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realtimechatapp.MainActivities.activities.UserMentor;
import com.example.realtimechatapp.MainActivities.adapters.UserMentorAdapter;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Question2_1_1_1_1_1 extends Activity{


    private RecyclerView recyclerView;

    private UserMentorAdapter userMentorAdapter;
    private List<UserMentor> mUsermentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2_1_1_1_1_1);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mUsermentor = new ArrayList<>();

        readUsers();

    }

    private void readUsers() {

        String rate = "500-1000/hour";


       final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserMentor");

        databaseReference.orderByChild("rate").equalTo(rate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserMentor userMentor = snapshot.getValue(UserMentor.class);

                    mUsermentor.add(userMentor);
                }
                userMentorAdapter = new UserMentorAdapter(getApplicationContext(),mUsermentor);
                recyclerView.setAdapter(userMentorAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}