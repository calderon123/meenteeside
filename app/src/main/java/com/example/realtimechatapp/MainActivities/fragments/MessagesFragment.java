package com.example.realtimechatapp.MainActivities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.realtimechatapp.MainActivities.models.Chat;
import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.adapters.UserMentorList;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {


    private RecyclerView recyclerView;
    private Button rate_star;
    private UserMentorList userMentorList;
    private List<String> mUserslist;


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private List<Counselors> mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_messages, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserslist = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserslist.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat =snapshot.getValue(Chat.class);

                    if (chat.getSender().equals(firebaseUser.getUid())){
                        mUserslist.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid())){
                        mUserslist.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return  view;
    }

    private void readChats(){

        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Add")
        .child(firebaseUser.getUid()).child("counselor");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Counselors counselors = snapshot.getValue(Counselors.class);
                    for (String id: mUserslist ){
                        if (counselors.getId().equals(id)){
                            if (mUsers.size() != 0){
                                for (Counselors counselors1 : mUsers){
                                    if (!counselors.getId().equals(counselors1.getId())){
                                        mUsers.add(counselors);
                                    }
                                }
                            }else {
                                mUsers.add(counselors);
                            }
                        }

                    }
                }
                userMentorList = new UserMentorList(getContext(),mUsers,true);
                recyclerView.setAdapter(userMentorList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
