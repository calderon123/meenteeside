package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.activities.MenteeMainActivity;
import com.example.realtimechatapp.MainActivities.activities.MentorProfile;
import com.example.realtimechatapp.MainActivities.activities.StartActivity;
import com.example.realtimechatapp.MainActivities.models.Counselors;
import com.example.realtimechatapp.MainActivities.activities.MessageActivity;
import com.example.realtimechatapp.MainActivities.models.UserMentor;
import com.example.realtimechatapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMentorList extends RecyclerView.Adapter<UserMentorList.ViewHolder> {

    private UserMentor userMentor;
    private Context mContext;
    private List<Counselors> mUsers;
    private boolean ischat;



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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Counselors counselors = mUsers.get(i);
        viewHolder.fullname.setText(counselors.getFullname());
        viewHolder.rate.setText(counselors.getRate());
        viewHolder.expertise.setText(counselors.getExpertise());

//        if (userMentor.getImage().equals("default")){
            viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
//        }else{
//            Glide.with(mContext).load(userMentor.getImage()).into(viewHolder.profile_image);
//        }
//        if (ischat){
//            if (userMentor.getStatus().equals("online")){
//                viewHolder.img_on.setVisibility(View.VISIBLE);
//                viewHolder.img_off.setVisibility(View.GONE);
//            }else {
//                viewHolder.img_on.setVisibility(View.GONE);
//                viewHolder.img_off.setVisibility(View.VISIBLE);
//            }
//        }else {
//            viewHolder.img_on.setVisibility(View.GONE);
//            viewHolder.img_off.setVisibility(View.GONE);
//        }

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

        public TextView fullname,expertise,rate;
        public CircleImageView profile_image;
        private CircleImageView img_off;
        private CircleImageView img_on;
        private Button rate_star;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            fullname = itemView.findViewById(R.id.fullname);
            expertise = itemView.findViewById(R.id.expertise);
            rate = itemView.findViewById(R.id.rate);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_off = itemView.findViewById(R.id.img_off);
            img_on= itemView.findViewById(R.id.img_on);
            rate_star = itemView.findViewById(R.id.rate_star);
        }

    }

}
