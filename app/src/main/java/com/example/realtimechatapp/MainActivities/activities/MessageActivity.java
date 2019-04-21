package com.example.realtimechatapp.MainActivities.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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
import com.example.realtimechatapp.MainActivities.fragments.APIService;
import com.example.realtimechatapp.MainActivities.models.Chat;
import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.models.Mentees;
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

    Button btn_set_sched;
    EditText text_send;
    ImageButton btn_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

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


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, MenteeMainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

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
        final Button schedule = findViewById(R.id.schedule);



        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);

                View view = getLayoutInflater().inflate(R.layout.schedule_btn, null);
                final String userid = getIntent().getStringExtra("id");
                btn_calendar = view.findViewById(R.id.btn_calendar);
                date_schedule = view.findViewById(R.id.date_schedule);
                 set_dscrpt = view.findViewById(R.id.set_dscrpt);
                btn_set_sched = view.findViewById(R.id.btn_set_sched);


                btn_calendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int day  = calendar.get(Calendar.DAY_OF_MONTH);
                        int month  = calendar.get(Calendar.MONTH);
                        int year  = calendar.get(Calendar.YEAR);

                        DatePickerDialog dialog = new DatePickerDialog(
                                MessageActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                onDateSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                    }
                });
                onDateSetListener  = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                        month = month +1;
                        year = year ;

                        String date = dayOfMonth+ "/"+month +"/"+year ;

                        date_schedule.setText(date);
                    }
                };

                btn_set_sched.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date_schedule_ = date_schedule.getText().toString();
                        String set_dscrpt_ = set_dscrpt.getText().toString();

                        String msg = date_schedule_ + set_dscrpt_;

                        if (!msg.equals("")) {
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                            sendMessage(firebaseUser.getUid(), userid, msg);
                        } else {
                            Toast.makeText(MessageActivity.this, "Can't set empty fields", Toast.LENGTH_SHORT).show();
                        }
                        text_send.setText("");
                    }
                });

                builder.setView(view);
                AlertDialog  dialog = builder.create();
                dialog.show();
            }
        });


        final String userid = getIntent().getStringExtra("id");




        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

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

                fullname.setText(counselors.getFullname());
                expertise.setText(counselors.getExpertise());
                Glide.with(getApplicationContext()).load(counselors.getImageURL()).into(profile_image);

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
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private  void sendMessage(String sender, final String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("time_sent",time_sent());
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
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
                UserMentor userMentor= dataSnapshot.getValue(UserMentor.class);
                if (notify) {
                    sendNotification(receiver, userMentor.getEmail(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String time_sent() {
            String delegate = "hh:mm aaa";
            return (String) DateFormat.format(delegate,Calendar.getInstance().getTime());
    }

    private void sendNotification(String receiver, final String email, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        final String userid = getIntent().getStringExtra("id");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);

                    Data data = new Data(firebaseUser.getUid(), R.mipmap.ic_launcher, email+": "+message,
                            "New Message",userid);

                 Sender sender = new Sender(data ,token.getToken());
                        apiService.sendNotification(sender)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.code() == 200 ){
                                            if (response.body().success == 1){
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

    private void status(String status){
        UserMentor userMentor = null;
        databaseReference  = FirebaseDatabase.getInstance().getReference("UserMentee").
                child(firebaseUser.getUid());

        HashMap<String,Object> hashMap= new HashMap<>();
        hashMap.put("status",status);

        databaseReference.updateChildren(hashMap);

    }
    @Override
    protected void onResume(){
        super.onResume();
        status("online");
    }
    @Override
    protected  void onPause(){
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        status("offline");
    }


}
