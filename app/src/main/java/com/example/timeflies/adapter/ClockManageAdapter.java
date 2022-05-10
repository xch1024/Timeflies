package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.TimeTableData;
import com.example.timeflies.utils.ToastCustom;

import java.util.List;

/**
 * 修改作息时间表
 */
public class ClockManageAdapter extends RecyclerView.Adapter<ClockManageAdapter.ManageHolder>{

    private List<TimeTableData> list;//数据源
    private Context context;//上下文

    public ClockManageAdapter(List<TimeTableData> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ManageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //RecyclerView中的item的match_parent属性失效问题解决方案。：https://blog.csdn.net/OverseasAndroid/article/details/51840819
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvclock_manage, parent, false);
        return new ManageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClockManageAdapter.ManageHolder holder, int position) {
        TimeTableData data = list.get(position);

        //为每个item设置显示的起始时间
        holder.tvSection.setText("第 "+(position+1)+" 节");
        holder.tvStart.setText(data.getStartTime());
        holder.tvEnd.setText(data.getEndTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastCustom.showMsgWarning(context, "现在设置第"+ (position+1)+"节上课时间");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ManageHolder extends RecyclerView.ViewHolder {

        private TextView tvSection, tvStart, tvEnd;

        public ManageHolder(@NonNull View itemView) {
            super(itemView);

            tvSection = itemView.findViewById(R.id.section);
            tvStart = itemView.findViewById(R.id.start);
            tvEnd = itemView.findViewById(R.id.end);

        }
    }


}
