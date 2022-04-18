package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.ScheduleData;
import com.example.timeflies.utils.ToastCustom;
import java.util.List;

/**
 * Recycleview之打造通用的Adapter https://blog.csdn.net/weixin_43607099/article/details/106842200?spm=1001.2014.3001.5501
 *
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {


    private List<ScheduleData> list;//数据源
    private Context context;//上下文

    public ScheduleAdapter(List<ScheduleData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvschedule,parent,false);
        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleHolder holder, int position) {
        ScheduleData data = list.get(position);

        holder.tvNum.setText(String.valueOf(data.getId()));
        holder.tvStart.setText(data.getStartTime());
        holder.tvEnd.setText(data.getEndTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastCustom.showMsgTrue(context, "跳转到选择时间段");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ScheduleHolder extends RecyclerView.ViewHolder {

        private TextView tvNum,tvStart,tvEnd;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.time_num);
            tvStart = itemView.findViewById(R.id.time_start);
            tvEnd = itemView.findViewById(R.id.time_end);
        }

    }
}
