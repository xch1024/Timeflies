package com.example.timeflies.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.TimeData;
import com.example.timeflies.utils.ToastCustom;

import java.util.List;

/**
 * 修改作息时间表
 */
public class ClockManageAdapter extends RecyclerView.Adapter<ClockManageAdapter.ManageHolder> implements View.OnClickListener {

    private List<TimeData> list;//数据源
    private Context context;//上下文

    public ClockManageAdapter(List<TimeData> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ManageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //RecyclerView中的item的match_parent属性失效问题解决方案。：https://blog.csdn.net/OverseasAndroid/article/details/51840819
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_times_section, parent, false);
        return new ManageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClockManageAdapter.ManageHolder holder, @SuppressLint("RecyclerView") int position) {
        TimeData data = list.get(position);

        //为每个item设置显示的起始时间
        holder.tvSection.setText("第 "+(position+1)+" 节");
        holder.tvStart.setText(data.getStartTime());
        holder.tvEnd.setText(data.getEndTime());

        holder.rv_update.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ManageHolder extends RecyclerView.ViewHolder {

        private View rv_update;
        private TextView tvSection, tvStart, tvEnd;

        public ManageHolder(@NonNull View itemView) {
            super(itemView);

            rv_update = itemView.findViewById(R.id.update_time);

            tvSection = itemView.findViewById(R.id.section);
            tvStart = itemView.findViewById(R.id.start);
            tvEnd = itemView.findViewById(R.id.end);

            rv_update.setOnClickListener(ClockManageAdapter.this);

        }
    }

    /**
     * item内部的监听接口
     * 自定义一个回调接口来实现Click和LongClick事件
     */
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    //声明自定义的接口
    private ClockManageAdapter.OnItemClickListener mOnItemClickListener;
    //定义方法并传给外面的使用者
    public void setOnItemClickListener(ClockManageAdapter.OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.update_time:
                    mOnItemClickListener.onItemClick(view, pos);
                    break;
            }
        }
    }


}
