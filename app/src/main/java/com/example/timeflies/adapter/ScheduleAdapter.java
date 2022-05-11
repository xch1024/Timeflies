package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.TimeData;

import java.util.List;

/**
 * 主页左侧作息时间表
 * Recycleview之打造通用的Adapter https://blog.csdn.net/weixin_43607099/article/details/106842200?spm=1001.2014.3001.5501
 *
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private String TAG = "xch";

    private int tableLineWidth = 1;
    private int cellHeight = 75;

    private int ItemTotal;
    private List<TimeData> list;//数据源
    private Context context;//上下文

    public ScheduleAdapter(List<TimeData> list, Context context) {
        this.list = list;
        this.context = context;
        preprocessorParam();
    }



    public void setItemTotal(int itemTotal) {
        ItemTotal = itemTotal;
    }

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_times, parent,false);
        return new ScheduleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleHolder holder, int position) {
        setContent(holder, position);
    }

    private void setContent(ScheduleHolder holder, int position) {
        TimeData data = list.get(position);
        holder.tvNum.setText(String.valueOf(position+1));
        holder.tvStart.setText(data.getStartTime());
        holder.tvEnd.setText(data.getEndTime());
    }

    /**
     * 数据预处理
     */
    private void preprocessorParam() {
        tableLineWidth = dip2px(tableLineWidth);
        cellHeight = dip2px(cellHeight);
    }

    private int dip2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    //item的显示个数
    @Override
    public int getItemCount() {
        return ItemTotal;
    }

    public class ScheduleHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tvNum,tvStart,tvEnd;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);
            initView();

        }

        private void initView() {
            tvNum = itemView.findViewById(R.id.time_num);
            tvStart = itemView.findViewById(R.id.time_start);
            tvEnd = itemView.findViewById(R.id.time_end);

            view = itemView.findViewById(R.id.view);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,cellHeight);
            layoutParams.setMargins(tableLineWidth,tableLineWidth,tableLineWidth,tableLineWidth);
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * 重新设置时间表的时间
     * @param courseArray
     * @return
     */
    private String arrayToString(String[] courseArray){
        StringBuffer sb = new StringBuffer();
        for(int i = 0, len = courseArray.length; i < len; i++){
            sb.append(courseArray[i]);
            if(i != len - 1)sb.append(";");
        }
        String time = sb.toString();
        return time;
    }
}
