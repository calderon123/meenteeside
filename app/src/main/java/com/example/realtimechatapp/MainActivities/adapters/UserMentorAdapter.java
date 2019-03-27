package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.activities.UserMentor;
import com.example.realtimechatapp.R;

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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        UserMentor userMentor = mUsermentor.get(position);
        viewHolder.fullname.setText(userMentor.getFullname());
        viewHolder.expertise.setText(userMentor.getExpertise());
        viewHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return mUsermentor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fullname,expertise;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);

            fullname = itemView.findViewById(R.id.fullname);
            expertise = itemView.findViewById(R.id.expertise);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }


}
