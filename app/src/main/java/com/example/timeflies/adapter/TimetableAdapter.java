package com.example.timeflies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.ConfigData;

import java.util.List;

public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableHolder> implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "xch";
    private List<ConfigData> list;
    private Context context;

    public TimetableAdapter(List<ConfigData> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public TimetableAdapter.TimetableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_many_table, null);
        return new TimetableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableAdapter.TimetableHolder holder, int position) {
        ConfigData data = list.get(position);
        if(position == 0){
            holder.ivDel.setVisibility(View.GONE);
        }
        holder.tvName.setText(data.getClassName());

        holder.itemView.setTag(position);
        holder.ivDel.setTag(position);
        holder.ivEdit.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TimetableHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivEdit, ivDel;
        public TimetableHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.many_name);
            ivEdit = itemView.findViewById(R.id.many_edit);
            ivDel = itemView.findViewById(R.id.many_delete);

            //需要编辑和删除功能再打开
            ivEdit.setVisibility(View.GONE);
            ivDel.setVisibility(View.GONE);

            ivEdit.setOnClickListener(TimetableAdapter.this);
            ivDel.setOnClickListener(TimetableAdapter.this);

            ivEdit.setOnClickListener(TimetableAdapter.this);
            ivDel.setOnClickListener(TimetableAdapter.this);

            itemView.setOnClickListener(TimetableAdapter.this);
            itemView.setOnLongClickListener(TimetableAdapter.this);
        }
    }
//=============================================================
//=======================以下为item中的button控件点击事件处理===================================

/**
 * item里面有多个控件可以点击（item+item内部控件）
 * https://blog.csdn.net/lin_dianwei/article/details/78687487   item里面有多个控件可以点击
 * https://blog.csdn.net/weixin_37577039/   Item内部控件的点击事件
 * https://blog.csdn.net/qq_38225558/article/details/80627273   RecyclerView适配器实现多布局item+item内部控件点击事件
 * https://blog.csdn.net/qq_38225558/article/details/80608527   RecyclerView适配器实现item+item内部控件点击事件
 */
//item里面有多个控件可以点击（item+item内部控件）
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //声明自定义的接口
    private TimetableAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View v, ViewName viewName,int position);
        void onItemLongClick(View v, ViewName viewName,int position);
    }

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(TimetableAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.rv_many_table:
                    mOnItemClickListener.onItemClick(view, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(view, ViewName.ITEM, position);
                    break;
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (view.getId()){
                default:
                    mOnItemClickListener.onItemLongClick(view, ViewName.ITEM, position);
                    break;
            }
        }
        return true;
    }


}
