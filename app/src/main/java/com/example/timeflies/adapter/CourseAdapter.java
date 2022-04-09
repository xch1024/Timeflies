package com.example.timeflies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvcourse,parent,false);
        CourseAdapter.CourseHolder holder = new CourseAdapter.CourseHolder(inflater);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class CourseHolder extends RecyclerView.ViewHolder {
        public CourseHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
