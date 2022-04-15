package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;

import java.util.List;
import java.util.PrimitiveIterator;

/**
 * @author:halo
 * @projectName:com.example.timeflies.adapter
 * @date:2022-04-15
 * @time:11:09
 * @description:
 */
public class CourseNameAdapter extends RecyclerView.Adapter<CourseNameAdapter.CourseNameHolder> implements View.OnClickListener{

    private Context mContext;
    private List<String> list;

    public CourseNameAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public CourseNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvcourse_name,null);
        return new CourseNameHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseNameAdapter.CourseNameHolder holder, int position) {
        String data = list.get(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseNameHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        public CourseNameHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.className);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
