package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.timeflies.R;
import com.example.timeflies.model.ContentData;
import com.example.timeflies.utils.ToastCustom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> implements View.OnClickListener{

    private List<ContentData> list;//数据源
    private Context mContext;//上下文
    public ContentAdapter(Context Context, List<ContentData> list) {
        this.mContext = Context;
        this.list = list;
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvcontent,parent,false);
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
//        holder.bindData(list,position);

        ContentData data = list.get(position);
        holder.tvTime.setText(data.getTime());
        holder.tvWeek.setText(data.getWeek());
        holder.tvUsername.setText(data.getUsername());
        holder.tvLocation.setText(data.getLocation());

        holder.itemView.setTag(position);
        holder.delItem.setTag(position);
        holder.rv_week.setTag(position);
        holder.rv_time.setTag(position);
        holder.rv_checkbox.setTag(position);
        holder.rv_teacher.setTag(position);
        holder.rv_location.setTag(position);

        //添加动画效果
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.recycle_anim);
        holder.itemView.startAnimation(animation);
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
        private TextView tvTime, tvWeek, tvUsername, tvLocation;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);

            initView();
            setListener();
        }

        public void bindData(List<ContentData> list, int position) {
            ContentData data = list.get(position);
        }

        /**
         * 初始化item内部控件
         *
         */
        private void initView(){
            delItem = itemView.findViewById(R.id.delItem);
            rv_week = itemView.findViewById(R.id.rv_week);
            rv_time = itemView.findViewById(R.id.rv_time);
            rv_teacher = itemView.findViewById(R.id.rv_teacher);
            rv_location = itemView.findViewById(R.id.rv_location);
            rv_checkbox = itemView.findViewById(R.id.rv_checkbox);

            tvTime = itemView.findViewById(R.id.time);
            tvWeek = itemView.findViewById(R.id.week);
            tvUsername = itemView.findViewById(R.id.username);
            tvLocation = itemView.findViewById(R.id.location);
        }

        /**
         * 设置点击
         *
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

        /**
         * 一些按钮
         *
         */
        public void BtnDel(){
            if(list.size()== 1){
                ToastCustom.showMsgFalse(mContext.getApplicationContext(), "至少要保留一个时间段！");
            }else{
                delItem(getAdapterPosition());
            }
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
        void onItemLongClick(View v);
    }

    //声明自定义的接口
    private OnItemClickListener mOnItemClickListener;
    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();      //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.rv_content:
                    mOnItemClickListener.onItemClick(v, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }

    /**
     * 添加item方法,供外部使用
     *
     * @param position
     */
    public void addItem(int position){
        //在list中添加数据，并通知条目加入一条
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(new ContentData("第几周", "周一 第几节", "授课老师(可不填)","上课地点(可不填)"));
        notifyItemInserted(position);
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
