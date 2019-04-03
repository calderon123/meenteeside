package com.example.realtimechatapp.MainActivities.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.realtimechatapp.MainActivities.adapters.MessageAdapter;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView fullname, expertise;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    EditText text_send;
    ImageButton btn_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image = findViewById(R.id.profile_image);
        fullname = findViewById(R.id.fullname);
        expertise = findViewById(R.id.expertise);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        final String userid = getIntent().getStringExtra("id");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();

                if (!msg.equals("")){
                    sendMessage(firebaseUser.getUid(), userid ,msg);
                }else{
                    Toast.makeText(MessageActivity.this, "You cant send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });




        databaseReference = FirebaseDatabase.getInstance().getReference("Add").child(firebaseUser.getUid())
        .child("counselor").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Counselors counselors = dataSnapshot.getValue(Counselors.class);

                fullname.setText(counselors.getFullname());
                expertise.setText(counselors.getExpertise());
                    profile_image.setImageResource(R.mipmap.ic_launcher);

                readMessage(firebaseUser.getUid(),userid ,counselors.getImageurl() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private  void sendMessage(String sender,String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
    }
    private void readMessage(final String myid, final String userid, final String imageUrl){
        mChat = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                    chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
