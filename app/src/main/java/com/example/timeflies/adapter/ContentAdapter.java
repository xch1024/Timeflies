package com.example.timeflies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;



public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {


    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvcontent,parent,false);
        ContentHolder holder = new ContentHolder(inflater);
        return holder;
    }

    //position下标从0开始
    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        //返回Item总条数
        return 10;
    }


    //内部类，绑定控件
    public class ContentHolder extends RecyclerView.ViewHolder {
        public ContentHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void bindData() {

        }

    }

    public void addItem(int position){
        notifyItemInserted(position);
    }

    public void deleteItem(int position){
        notifyItemRemoved(position);
    }
}
