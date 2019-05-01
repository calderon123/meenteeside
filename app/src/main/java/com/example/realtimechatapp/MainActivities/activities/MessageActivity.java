package com.example.realtimechatapp.MainActivities.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.realtimechatapp.MainActivities.Notification.Client;
import com.example.realtimechatapp.MainActivities.Notification.Data;
import com.example.realtimechatapp.MainActivities.Notification.MyResponse;
import com.example.realtimechatapp.MainActivities.Notification.Sender;
import com.example.realtimechatapp.MainActivities.Notification.Token;
import com.example.realtimechatapp.MainActivities.adapters.MessageAdapter;
import com.example.realtimechatapp.MainActivities.adapters.ScheduleAdapter;
import com.example.realtimechatapp.MainActivities.fragments.APIService;
import com.example.realtimechatapp.MainActivities.models.Chat;
import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.models.Mentees;
import com.example.realtimechatapp.MainActivities.models.Schedules;
import com.example.realtimechatapp.MainActivities.models.UserMentee;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView fullname, expertise,date_schedule;
    Button rate,btn_calendar;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private CalendarView calendarView;
    private Toolbar toolbar;
    ScheduleAdapter scheduleAdapter;
    List<Schedules> mSchedules;

    EditText text_send;
    ImageButton btn_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView1;
    APIService apiService;
    boolean notify = false;
    EditText set_dscrpt;
    RecyclerView recyclerView;
    ValueEventListener seenListener;
    Intent intent;
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView1.setLayoutManager(linearLayoutManager1);


        profile_image = findViewById(R.id.profile_image);
        fullname = findViewById(R.id.fullname);
        expertise = findViewById(R.id.expertise);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        toolbar = findViewById(R.id.toolbar);

        final String userid = getIntent().getStringExtra("id");


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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mSchedules = new ArrayList<>();
        DatabaseReference adding_schedules =   FirebaseDatabase.getInstance().getReference("Schedules")
                .child(userid).child(firebaseUser.getUid());
        adding_schedules.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mSchedules.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Schedules schedules = snapshot.getValue(Schedules.class);
                    if (!(mSchedules ==null) && schedules.getMentee_sched_id().equals(firebaseUser.getUid())
                            && schedules.getCounselor_sched_id().equals(userid)) {

                        mSchedules.add(schedules);
                    }else {

                    }
                }
                scheduleAdapter = new ScheduleAdapter(MessageActivity.this,mSchedules);
                recyclerView1.setAdapter(scheduleAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
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


                FirebaseDatabase.getInstance().getReference("UserMentor").child(counselors.getId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserMentor userMentor = dataSnapshot.getValue(UserMentor.class);
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

                readMessage(firebaseUser.getUid(),userid ,counselors.getImageURL() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            seenMessage(userid);
    }

    private void seenMessage(final String userid){
        databaseReference= FirebaseDatabase.getInstance().getReference("Chats");
        seenListener= databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("isseen", true );
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private String delegate() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    private  void sendMessage(String sender, final String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message + "| time sent:"+delegate());
        hashMap.put("isseen", false);

        databaseReference.child("Chats").push().setValue(hashMap);
        final String userid = getIntent().getStringExtra("id");
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatRef.child("id").setValue(userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg = message;

        databaseReference = FirebaseDatabase.getInstance().getReference("Add").child(userid)
                .child("mentees").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mentees mentees= dataSnapshot.getValue(Mentees.class);

                FirebaseDatabase.getInstance().getReference("UserMentee").child(mentees.getId()).child(mentees.getId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserMentee userMentee = dataSnapshot.getValue(UserMentee.class);
                                       if (notify) {
                                            sendNotification(receiver, userMentee.getFullname(), msg);
                                        }
                                notify = false;

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
    }

    private void sendNotification(String receiver, final String fullname, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Notifications");
        final String userid = getIntent().getStringExtra("id");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, fullname+": "+message,
                            "New Message",userid);

                    Sender sender = new Sender(data ,token.getToken());

                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200 ){
                                            if (response.body().success != 1){
                                                Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {

                                    }
                                });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();

        editor.putString("currentuser",userid);
        editor.apply();

    }

    private void status(String status){
        UserMentor userMentor = null;
        databaseReference  = FirebaseDatabase.getInstance().getReference("UserMentee").
                child(firebaseUser.getUid()).child(firebaseUser.getUid());

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);

        databaseReference.updateChildren(hashMap);

    }
    @Override
    protected void onResume(){
        final String userid = getIntent().getStringExtra("id");
        super.onResume();
        status("online");
        currentUser(userid);
    }
    @Override
    protected  void onPause(){
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }


}
