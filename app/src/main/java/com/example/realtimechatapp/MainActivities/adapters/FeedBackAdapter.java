package com.example.realtimechatapp.MainActivities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.models.Rate;
import com.example.realtimechatapp.R;
import com.hsalf.smilerating.SmileRating;

import java.util.List;

@SuppressWarnings("ALL")
public class FeedBackAdapter extends RecyclerView.Adapter <FeedBackAdapter.FeedBackViewHolder> {


    private Context mContext;
    private List<Rate> rateList;
    public FeedBackAdapter(Context mContext,List<Rate> rateList){
        this.rateList = rateList;
        this.mContext = mContext;
    }

    @Override
    public FeedBackAdapter.FeedBackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.feedbacks, viewGroup, false );
        return new FeedBackAdapter.FeedBackViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FeedBackViewHolder feedBackViewHolder, int i) {
        Rate rate = rateList.get(i);

        feedBackViewHolder.comments_retrieve.setText("'"+rate.getComments()+"'");
    }

    @Override
    public int getItemCount() {
        return rateList.size();
    }

    public class FeedBackViewHolder extends RecyclerView.ViewHolder{

        SmileRating rating_retrieve;
        TextView comments_retrieve;

        public FeedBackViewHolder(View itemView){
            super(itemView);

            comments_retrieve = itemView.findViewById(R.id.comments_retrieve);
        }
    }
}
