package com.example.realtimechatapp.MainActivities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.adapters.UserMentorList;
import com.example.realtimechatapp.MainActivities.adapters.MentorlistAdapter;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.MainActivities.questions.Question1;
import com.example.realtimechatapp.MainActivities.questions.Question2;
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


public class MentorListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    private EditText search;
    private RecyclerView add;
    private  MentorlistAdapter mentorlistAdapter;
    private List<Counselors> mUsers;
    private Button find_counselor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_mentor_list, container, false);

        search = view.findViewById(R.id.search);
        recyclerView = view.findViewById(R.id.fragment_cont);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        find_counselor = view.findViewById(R.id.find_counselor);

        find_counselor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Question1.class);
                startActivity(intent);
            }
        });


        mUsers = new ArrayList<>();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searhUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        readUsers();
        return view;


    }

    private void searhUsers(final String s) {
        final  FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Add")
               .child(firebaseUser.getUid()).child("counselor").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Counselors counselors= snapshot.getValue(Counselors.class);
                        if (!counselors.getId().equals(firebaseUser.getUid()) && mUsers != null) {
                            find_counselor.setVisibility(View.GONE);
                            mUsers.add(counselors);
                        }else {
                            find_counselor.setVisibility(View.VISIBLE);
                        }
                    }
                    mentorlistAdapter = new MentorlistAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(mentorlistAdapter);
                }




            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Add").child(firebaseUser.getUid()).child("counselor");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Counselors counselors = snapshot.getValue(Counselors.class);

                        mUsers.add(counselors);

                    }
                    mentorlistAdapter = new MentorlistAdapter(getContext(), mUsers, true);
                    recyclerView.setAdapter(mentorlistAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
