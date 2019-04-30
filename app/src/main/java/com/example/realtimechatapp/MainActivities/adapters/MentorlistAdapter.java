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
import com.example.realtimechatapp.MainActivities.fragments.MentorListFragment;
import com.example.realtimechatapp.MainActivities.fragments.MessagesFragment;
import com.example.realtimechatapp.MainActivities.fragments.ProfileFragments;
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

public class MentorlistAdapter extends RecyclerView.Adapter<MentorlistAdapter.ViewHolder> {

    private UserMentor userMentor;
    private Context mContext;
    private List<Counselors> mUsers;
    private boolean ischat;
    String theLastMessage;


    public MentorlistAdapter(Context mcontext, List<Counselors> userMentorList,boolean ischat){
        this.mContext = mcontext;
        this.mUsers = userMentorList;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_mentorlist_adapter, parent, false);
        return new MentorlistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Counselors counselors = mUsers.get(i);

        FirebaseDatabase.getInstance().getReference("UserMentor")
                .child(counselors.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserMentor userMentor = dataSnapshot.getValue(UserMentor.class);
                viewHolder.fullname.setText(userMentor.getFullname());

                viewHolder.expertise.setText(userMentor.getExpertise());

                if (userMentor.getImageURL().equals("default")){
                    viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(mContext).load(userMentor.getImageURL()).into(viewHolder.profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("UserMentor").child(counselors.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getChildren();

                        UserMentor user = dataSnapshot.getValue(UserMentor.class);
                        if (counselors.getId().equals(user.getId())) {
                            viewHolder.status(user.getStatus());
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

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView fullname,expertise,rate;
        public CircleImageView profile_image;
        private CircleImageView img_off;
        private CircleImageView img_on;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.fullname);
            expertise = itemView.findViewById(R.id.expertise);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_off = itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);

        }
        private void status(String id){

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
