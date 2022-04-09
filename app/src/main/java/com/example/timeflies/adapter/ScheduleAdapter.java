package com.example.timeflies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {
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

    }

    @Override
    public int getItemCount() {
        return 13;
    }

    public class ScheduleHolder extends RecyclerView.ViewHolder {
        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
