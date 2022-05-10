package com.example.timeflies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;

/**
 * 主页课程显示
 * RecycleView实现合并拆分单元格 https://blog.csdn.net/weixin_43607099/article/details/106299021?spm=1001.2014.3001.5501
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvcourse,parent,false);
        return new CourseHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public static class CourseHolder extends RecyclerView.ViewHolder {
        public CourseHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
