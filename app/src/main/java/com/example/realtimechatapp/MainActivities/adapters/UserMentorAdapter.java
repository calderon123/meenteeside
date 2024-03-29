package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realtimechatapp.MainActivities.activities.MessageActivity;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.example.realtimechatapp.MainActivities.fragments.MentorListFragment;
//import com.example.realtimechatapp.MainActivities.fragments.MentorListFragment;

public class UserMentorAdapter extends RecyclerView.Adapter<UserMentorAdapter.ViewHolder> {

    private Context mContext;
    private List<UserMentor> mUsermentor;
    private View view;
    FirebaseUser firebaseUser;
    private Button btn_message;
    public UserMentorAdapter(Context mContext, List<UserMentor> mUsermentor){
        this.mUsermentor = mUsermentor;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


         view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);


         btn_message = view.findViewById(R.id.btn_message);
        return new UserMentorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final UserMentor userMentor = mUsermentor.get(position);


        viewHolder.btn_add.setVisibility(View.VISIBLE);
        viewHolder.fullname.setText(userMentor.getFullname());
        viewHolder.expertise.setText(userMentor.getExpertise());
        viewHolder.rate.setText(userMentor.getRate());
        if (userMentor.getImageURL().equals("default")){
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
            viewHolder.card_background.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(userMentor.getImageURL()).into(viewHolder.card_background);
            Glide.with(mContext).load(userMentor.getImageURL()).into(viewHolder.profile_image);
        }
        viewHolder.btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", userMentor.getId());
                v.getContext().startActivity(intent);
            }
        });
        isAdding(userMentor.getId(), viewHolder.btn_add);

        if (userMentor.getId().equals(firebaseUser.getUid())){
            viewHolder.btn_add.setVisibility(View.GONE);
        }


        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.btn_add.getText().toString().equals("add")){
                    viewHolder.btn_message.setVisibility(View.VISIBLE);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("id",userMentor.getId());
                    String userid =  userMentor.getId();
                    hashMap.put("expertise",userMentor.getExpertise().toLowerCase());
                    hashMap.put("search",userMentor.getFullname().toLowerCase());
                    FirebaseDatabase.getInstance().getReference().child("Add").child(firebaseUser.getUid()).child("counselor")
                            .child(userid).setValue(hashMap);


                    HashMap<String,String> hashMap1 = new HashMap<>();
                    hashMap1.put("id",firebaseUser.getUid());
                    hashMap1.put("search" ,firebaseUser.getEmail().toLowerCase());
                    FirebaseDatabase.getInstance().getReference().child("Add").child(userMentor.getId().toString()).child("mentees")
                            .child(firebaseUser.getUid()).setValue(hashMap1);

                }else {
                    viewHolder.btn_message.setVisibility(View.GONE);

                    String userid =  userMentor.getId();
                    FirebaseDatabase.getInstance().getReference().child("Add").child(firebaseUser.getUid()).child("counselor")
                            .child(userid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Add").child(userMentor.getId().toString()).child("mentees")
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return mUsermentor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fullname,expertise,rate;
        public ImageView card_background;
        public CircleImageView profile_image;
        public Button btn_add,btn_message;
        public ImageButton home,back;

        public ViewHolder(View itemView){
            super(itemView);

            btn_message = itemView.findViewById(R.id.btn_message);
            back = itemView.findViewById(R.id.back);
            home = itemView.findViewById(R.id.home);
            btn_add = itemView.findViewById(R.id.btn_add);
            rate = itemView.findViewById(R.id.rate);
            fullname = itemView.findViewById(R.id.fullname);
            expertise = itemView.findViewById(R.id.expertise);
            profile_image = itemView.findViewById(R.id.profile_image);
            card_background = itemView.findViewById(R.id.card_background);
        }
    }

    private void isAdding(final String userid,final Button button){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Add").child(firebaseUser.getUid()).child("counselor");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()){
                    button.setText("unfriend");
                    btn_message.setVisibility(View.VISIBLE);
                } else {
                    button.setText("add");
                    btn_message.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
