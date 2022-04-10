package com.example.timeflies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private final List<String> timenum,timestart,timeend;

    public ScheduleAdapter() {

        timenum = new ArrayList<>() ;
        for(int i = 1; i < 12; i++){
            timenum.add(String.valueOf(i));
        }

        timestart = new ArrayList<>();
            timestart.add("8:00");
            timestart.add("9:00");
            timestart.add("10:10");
            timestart.add("11:00");
            timestart.add("15:00");
            timestart.add("16:00");
            timestart.add("17:00");
            timestart.add("18:00");
            timestart.add("19:30");
            timestart.add("20:30");
            timestart.add("21:30");

        timeend = new ArrayList<>();
            timeend.add("8:00");
            timeend.add("9:00");
            timeend.add("10:10");
            timeend.add("11:00");
            timeend.add("15:00");
            timeend.add("16:00");
            timeend.add("17:00");
            timeend.add("18:00");
            timeend.add("19:30");
            timeend.add("20:30");
            timeend.add("21:30");
    }

    @NonNull
    @Override
    public ScheduleAdapter.ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvschedule,parent,false);
        ScheduleAdapter.ScheduleHolder holder = new ScheduleAdapter.ScheduleHolder(inflater);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleHolder holder, int position) {

        holder.bindData(timenum,timestart,timeend,position);
    }

    @Override
    public int getItemCount() {
        return timenum.size();
    }

    public class ScheduleHolder extends RecyclerView.ViewHolder {

        private final TextView tvnum,tvStart,tvEnd;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            tvnum = itemView.findViewById(R.id.time_num);
            tvStart = itemView.findViewById(R.id.time_start);
            tvEnd = itemView.findViewById(R.id.time_end);
        }

        public void bindData(List<String> num, List<String> start,List<String> end, int position) {
            String n = num.get(position);
            String s = start.get(position);
            String e = end.get(position);
            tvnum.setText(n);
            tvStart.setText(s);
            tvEnd.setText(e);
        }


    }
}
