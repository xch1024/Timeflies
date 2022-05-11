package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.timeflies.R;
import com.example.timeflies.model.CourseData;

import java.util.List;

/**
 * 添加课程页面
 *
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements View.OnClickListener, View.OnLongClickListener{

    private List<CourseData> list;//数据源
    private Context mContext;//上下文
    public ContentAdapter(Context Context, List<CourseData> list) {
        this.mContext = Context;
        this.list = list;
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvcontent,parent,false);
        return new ContentHolder(inflater);
    }

    /**
     * https://blog.csdn.net/plokmju88/article/details/110944418 RecyclerView 的 Item 酷炫动画
     * https://blog.csdn.net/fangchao3652/article/details/44016651 给RecycleView item 加动画时注意的问题
     * position下标从0开始
     *
     */
    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder, int position) {

        setContent(holder, position);

        //添加动画效果
//        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycle_anim);
//        holder.itemView.startAnimation(animation);
    }

    private void setContent(ContentHolder holder, int position){
        CourseData data = list.get(position);
        holder.tvWeek.setText("第"+data.getStartWeek()+" - "+ data.getEndWeek()+"周");
        holder.tvWeekType.setText(data.getWeekType());
        holder.tvDay.setText(tranDay(data));
        holder.tvStep.setText("第"+ data.getSectionStart() + " - "+ data.getSectionEnd() + "节");
        if(!data.getTeacherName().equals("null")){
            holder.tvTeacher.setText(data.getTeacherName());
        }
        if(!data.getClassroom().equals("null")){
            holder.tvClassroom.setText(data.getClassroom());
        }

        holder.itemView.setTag(position);
        holder.delItem.setTag(position);
        holder.rv_week.setTag(position);
        holder.rv_time.setTag(position);
        holder.rv_checkbox.setTag(position);
        holder.rv_teacher.setTag(position);
        holder.rv_location.setTag(position);

    }


    private String tranDay(CourseData data){
        int day = data.getDay();
        String weekDay = "";
        if(day == 1){
            weekDay= "周一";
        }else if(day == 2){
            weekDay = "周二";
        }else if(day == 3){
            weekDay = "周三";
        }else if(day == 4){
            weekDay = "周四";
        }else if(day == 5){
            weekDay = "周五";
        }else if(day == 6){
            weekDay = "周六";
        }else if(day == 7){
            weekDay = "周日";
        }
        return weekDay;
    }

    /**
     * 返回Item总条数
     *
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContentHolder extends RecyclerView.ViewHolder{
        private View delItem, rv_week, rv_time, rv_teacher, rv_location;
        private CheckBox rv_checkbox;
        private TextView tvWeek, tvWeekType, tvDay, tvStep;
        private EditText tvTeacher, tvClassroom;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);

            initView();
            setListener();
        }


        /**
         * 初始化item内部控件
         */
        private void initView(){
            delItem = itemView.findViewById(R.id.delItem);
            rv_week = itemView.findViewById(R.id.rv_week);
            rv_time = itemView.findViewById(R.id.rv_time);
            rv_teacher = itemView.findViewById(R.id.rv_teacher);
            rv_location = itemView.findViewById(R.id.rv_location);
            rv_checkbox = itemView.findViewById(R.id.rv_checkbox);
            rv_checkbox.setVisibility(View.GONE);

            tvWeek = itemView.findViewById(R.id.week);
            tvWeekType = itemView.findViewById(R.id.week_type);
            tvDay = itemView.findViewById(R.id.week_day);
            tvStep = itemView.findViewById(R.id.step);
            tvTeacher = itemView.findViewById(R.id.teacher);
            tvClassroom = itemView.findViewById(R.id.classroom);
        }

        /**
         * 设置点击
         */
        private void setListener(){
            // 为ItemView添加点击事件
            itemView.setOnClickListener(ContentAdapter.this);
            delItem.setOnClickListener(ContentAdapter.this);
            rv_week.setOnClickListener(ContentAdapter.this);
            rv_time.setOnClickListener(ContentAdapter.this);
            rv_teacher.setOnClickListener(ContentAdapter.this);
            rv_location.setOnClickListener(ContentAdapter.this);
            rv_checkbox.setOnClickListener(ContentAdapter.this);
        }
    }

//=======================以下为item中的button控件点击事件处理===================================

    /**
     * item里面有多个控件可以点击（item+item内部控件）
     * https://blog.csdn.net/lin_dianwei/article/details/78687487   item里面有多个控件可以点击
     * https://blog.csdn.net/weixin_37577039/   Item内部控件的点击事件
     * https://blog.csdn.net/qq_38225558/article/details/80627273   RecyclerView适配器实现多布局item+item内部控件点击事件
     * https://blog.csdn.net/qq_38225558/article/details/80608527   RecyclerView适配器实现item+item内部控件点击事件
     */
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    /**
     * item内部的监听接口
     * 自定义一个回调接口来实现Click和LongClick事件
     */
    public interface OnItemClickListener{
        void onItemClick(View v, ViewName viewName, int position);
        void onItemLongClick(View v, ViewName viewName,int position);
    }

    //声明自定义的接口
    private OnItemClickListener mOnItemClickListener;
    //定义方法并传给外面的使用者
    public void setOnItemClickListener(ContentAdapter.OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (view.getId()){
                case R.id.rv_content:
                    mOnItemClickListener.onItemClick(view, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(view, ViewName.ITEM, position);
                    break;
            }
        }
    }

    public boolean onLongClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.rv_content:
                    mOnItemClickListener.onItemClick(view, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(view, ViewName.ITEM, position);
                    break;
            }
        }
        return true;
    }



    /**
     * 删除item方法，供外部使用
     * @param position
     */
    public void delItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


}
