package com.example.realtimechatapp.MainActivities.adapters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.realtimechatapp.MainActivities.models.Schedules;
import com.example.realtimechatapp.R;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter <ScheduleAdapter.ScheduleViewHolder> {

    private Dialog dialog;
    private Context mContext;
    private List<Schedules> schedulesList;


    public ScheduleAdapter(Context mContext, List<Schedules> schedulesList){
        this.schedulesList = schedulesList;
        this.mContext = mContext;
    }

    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.appointment_schedules, viewGroup, false );
        return new ScheduleAdapter.ScheduleViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder scheduleViewHolder, int i) {
        final Schedules schedules = schedulesList.get(i);

        scheduleViewHolder.date.setText("  Date: " + schedules.getDate_sched());
        scheduleViewHolder.where.setText(schedules.getSet_dscrpt());
        scheduleViewHolder.date_schedule.setText(schedules.getDate_sched());
        scheduleViewHolder.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.line1.setVisibility(View.GONE);
            }
        });

        scheduleViewHolder.view_schedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.line1.setVisibility(View.VISIBLE);
                scheduleViewHolder.btn_view_sched.setVisibility(View.VISIBLE);
            }
        });
        scheduleViewHolder.btn_view_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.title1.setVisibility(View.VISIBLE);
            }
        });

        scheduleViewHolder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleViewHolder.title1.setVisibility(View.GONE);
            }
        });



    }

    @Override
    public int getItemCount() {
        return schedulesList.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{

        Button btn_calendar;
        TextView date,btn_view_sched,date_schedule,where,close;
        ImageButton hide,view_schedules,edit;
        LinearLayout line1,title1;

        public ScheduleViewHolder(View itemView){
            super(itemView);

            btn_calendar = itemView.findViewById(R.id.btn_calendar);

            date_schedule = itemView.findViewById(R.id.date_schedule);
            where = itemView.findViewById(R.id.where);
            close = itemView.findViewById(R.id.close);
            title1 = itemView.findViewById(R.id.title1);
            view_schedules = itemView.findViewById(R.id.view_schedules);
            btn_view_sched = itemView.findViewById(R.id.btn_view_sched);
            hide = itemView.findViewById(R.id.hide);
            date = itemView.findViewById(R.id.date);
            line1 = itemView.findViewById(R.id.line1);
        }
    }
}
