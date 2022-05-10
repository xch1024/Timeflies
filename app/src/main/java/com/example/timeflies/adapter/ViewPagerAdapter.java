package com.example.timeflies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.TimeTableData;

import java.util.List;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {

    private ScheduleAdapter scheduleAdapter;
    private List<TimeTableData> list;//作息时间数据源
    private LinearLayoutManager linearLayoutManager;
    private int ItemTotal;

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewPagerViewHolder，返回每一个页面的布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager, parent,false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewPagerViewHolder holder, int position) {
        initSchedule(holder, position);

    }

    /**
     * 作息时间表展示
     * @param holder
     * @param position
     */
    private void initSchedule(ViewPagerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public void setItemTotal(int itemTotal) {
        ItemTotal = itemTotal;
    }

    public class ViewPagerViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout view_Linear;
        private TextView week_month;

        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            view_Linear = itemView.findViewById(R.id.view_Linear);
            week_month = itemView.findViewById(R.id.week_month);

        }
    }


}
