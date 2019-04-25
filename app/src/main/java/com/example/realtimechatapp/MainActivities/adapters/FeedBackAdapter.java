package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realtimechatapp.MainActivities.models.RateDetails;
import com.example.realtimechatapp.MainActivities.models.UserMentee;
import com.example.realtimechatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.SmileRating;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class FeedBackAdapter extends RecyclerView.Adapter <FeedBackAdapter.FeedBackViewHolder> {


    private Context mContext;
    private List<RateDetails> rateDetailsList;
    public FeedBackAdapter(Context mContext,List<RateDetails> rateDetailsList){
        this.rateDetailsList = rateDetailsList;
        this.mContext = mContext;
    }

    @Override
    public FeedBackAdapter.FeedBackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feedbacks, viewGroup, false );
        return new FeedBackAdapter.FeedBackViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final FeedBackViewHolder feedBackViewHolder, int i) {
        RateDetails rateDetails = rateDetailsList.get(i);

        feedBackViewHolder.comments_retrieve.setText("'"+ rateDetails.getComments()+"'");
        feedBackViewHolder.date.setText(rateDetails.getDate());
        FirebaseDatabase.getInstance().getReference("UserMentee").child(rateDetails.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UserMentee userMentee =dataSnapshot.getValue(UserMentee.class);

                        if (userMentee.getImageURL().equals("default")){
                            feedBackViewHolder.profile_image.setImageResource(R.drawable.ic_account_circle_black_24dp);
                        }else {
                            Glide.with(mContext).load(userMentee.getImageURL()).into(feedBackViewHolder.profile_image);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return rateDetailsList.size();
    }

    public class FeedBackViewHolder extends RecyclerView.ViewHolder{

        SmileRating rating_retrieve;
        TextView comments_retrieve,date;
        CircleImageView profile_image;

        public FeedBackViewHolder(View itemView){
            super(itemView);

            date = itemView.findViewById(R.id.date);
            profile_image = itemView.findViewById(R.id.profile_image);
            comments_retrieve = itemView.findViewById(R.id.comments_retrieve);
        }
    }
}
