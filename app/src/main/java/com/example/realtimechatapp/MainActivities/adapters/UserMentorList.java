package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realtimechatapp.MainActivities.activities.MentorProfile;
import com.example.realtimechatapp.MainActivities.activities.MessageActivity;
import com.example.realtimechatapp.MainActivities.models.Chat;
import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMentorList extends RecyclerView.Adapter<UserMentorList.ViewHolder> {

    private UserMentor userMentor;
    private Context mContext;
    private List<Counselors> mUsers;
    private boolean ischat;
     String theLastMessage;


    public UserMentorList(Context mcontext, List<Counselors> userMentorList,boolean ischat){
        this.mContext = mcontext;
        this.mUsers = userMentorList;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.mentorlist500_1000_item, parent, false);
        return new UserMentorList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Counselors counselors = mUsers.get(i);
        viewHolder.fullname.setText(counselors.getFullname());

        viewHolder.expertise.setText(counselors.getExpertise());

        if (counselors.getImageURL().equals("default")){
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(counselors.getImageURL()).into(viewHolder.profile_image);
        }

        if (ischat){
            lastMessage(counselors.getId() ,viewHolder.last_msg);
        }else {
            viewHolder.last_msg.setVisibility(View.GONE);
        }
        FirebaseDatabase.getInstance().getReference("UserMentor").child(counselors.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren();

                        UserMentor user = dataSnapshot.getValue(UserMentor.class);
                        if (counselors.getId().equals(user.getId())) {
                          viewHolder.bobo(user.getStatus());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("id", counselors.getId());
                mContext.startActivity(intent );
            }
        });
        viewHolder.rate_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MentorProfile.class);
                intent.putExtra("id", counselors.getId());
                mContext.startActivity(intent );
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView fullname,expertise,rate,last_msg;
        public CircleImageView profile_image;
        private CircleImageView img_off;
        private CircleImageView img_on;
        private Button rate_star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            last_msg = itemView.findViewById(R.id.last_message);
            fullname = itemView.findViewById(R.id.fullname);
            expertise = itemView.findViewById(R.id.expertise);
            rate = itemView.findViewById(R.id.rate);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_off = itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);
            rate_star = itemView.findViewById(R.id.rate_star);
        }
        private void bobo(String id){

            if (ischat){
                if (id.equals("online")){
                    img_on.setVisibility(View.VISIBLE);
                    img_off.setVisibility(View.GONE);
                }else {
                    img_on.setVisibility(View.GONE);
                    img_off.setVisibility(View.VISIBLE);
                }
            }else {
                img_on.setVisibility(View.GONE);
                img_off.setVisibility(View.GONE);

            }


    }


    }
    private void lastMessage(final String userid, final TextView last_message){
         theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                            theLastMessage = chat.getMessage();
                    }

                }
                switch (theLastMessage){
                    case "default":
                    last_message.setText("NoMessage");
                    break;


                    default:
                        last_message.setText(theLastMessage);
                        break;
                }
                theLastMessage= "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
