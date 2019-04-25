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
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.Notification.Token;
import com.example.realtimechatapp.MainActivities.activities.MenteeMainActivity;
import com.example.realtimechatapp.MainActivities.models.Chat;
import com.example.realtimechatapp.MainActivities.models.ChatList;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {


    private RecyclerView recyclerView;
    private Button rate_star;
    private  UserMentorList userMentorList;
    private List<ChatList> mUserslist;


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

        reference  = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserslist.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren() ){
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    mUserslist.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return  view;
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }
    private void chatList() {
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Add")
                .child(firebaseUser.getUid())
                .child("counselor");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Counselors counselors = snapshot.getValue(Counselors.class);
                    for (ChatList chatList :mUserslist){
                        if(counselors.getId().equals(chatList.getId())) {
                            mUsers.add(counselors);
                        }
                    }
                }

                userMentorList = new UserMentorList(getContext(), mUsers ,true);
                recyclerView.setAdapter(userMentorList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
