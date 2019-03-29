package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.activities.MentorListFragment;
import com.example.realtimechatapp.MainActivities.activities.UserMentor;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserMentorAdapter extends RecyclerView.Adapter<UserMentorAdapter.ViewHolder> {

    private Context mContext;
    private List<UserMentor> mUsermentor;
    private View view;


    public UserMentorAdapter (Context mContext,List<UserMentor> mUsermentor){
        this.mUsermentor = mUsermentor;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


         view = LayoutInflater.from(mContext).inflate(R.layout.mentorlist500_1000_item, parent, false);
        return new UserMentorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final DatabaseReference userid = FirebaseDatabase.getInstance().getReference("UserMentor").child("id");

        final UserMentor userMentor = mUsermentor.get(position);


        viewHolder.btn_add.setVisibility(View.VISIBLE);
        viewHolder.fullname.setText(userMentor.getFullname());
        viewHolder.expertise.setText(userMentor.getExpertise());
        viewHolder.rate.setText(userMentor.getRate());
        viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        isAdding(userMentor.getId().toString(), viewHolder.btn_add);

        if (userMentor.getId().equals(userid)){
            viewHolder.btn_add.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", userMentor.getId().toString());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MentorListFragment()).commit();
            }
        });

        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            final DatabaseReference userid = FirebaseDatabase.getInstance().getReference("UserMentor").child("id");
            @Override
            public void onClick(View v) {
                if (viewHolder.btn_add.getText().toString().equals("Add")){
                    FirebaseDatabase.getInstance().getReference().child("Add").child(userMentor.getId().toString()).child("added")
                            .child(userMentor.getId().toString()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Add").child(userMentor.getId().toString()).child("Mentors")
                            .child(userMentor.getId().toString()).setValue(true);
                }else {
                    FirebaseDatabase.getInstance().getReference().child("Add").child(userMentor.getId().toString()).child("added")
                            .child(userMentor.getId().toString()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Add").child(userMentor.getId().toString()).child("Mentors")
                            .child(userMentor.getId().toString()).setValue(true);
                }
            }
        });

    }

    private void isAdding(String id) {
    }


    @Override
    public int getItemCount() {
        return mUsermentor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fullname,expertise,rate;
        public ImageView profile_image;
        public Button btn_add;

        public ViewHolder(View itemView){
            super(itemView);

            btn_add = itemView.findViewById(R.id.btn_add);
            fullname = itemView.findViewById(R.id.fullname);
            expertise = itemView.findViewById(R.id.expertise);
            profile_image = itemView.findViewById(R.id.profile_image);
            rate = itemView.findViewById(R.id.rate);
        }
    }

    private void isAdding(final String userid, final Button button){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Add").child(userid).child("added");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()){
                    button.setText("added");
                } else {
                    button.setText("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
