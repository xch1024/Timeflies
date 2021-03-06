package com.example.timeflies.adapter;

import android.content.Context;
import android.util.Log;
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

        holder.tvName.setText(data.getClassName());
        if(data.isChecked()){
            Log.d(TAG, "data.isChecked: "+data.getClassName());
            holder.ivDel.setVisibility(View.GONE);
        }else{
            holder.ivDel.setVisibility(View.VISIBLE);
        }

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



            //????????????????????????????????????
            ivEdit.setVisibility(View.GONE);
//            ivDel.setVisibility(View.GONE);

            ivEdit.setOnClickListener(TimetableAdapter.this);
            ivDel.setOnClickListener(TimetableAdapter.this);

            ivEdit.setOnLongClickListener(TimetableAdapter.this);
            ivDel.setOnLongClickListener(TimetableAdapter.this);

            itemView.setOnClickListener(TimetableAdapter.this);
            itemView.setOnLongClickListener(TimetableAdapter.this);
        }
    }
//=============================================================
//=======================?????????item??????button????????????????????????===================================

/**
 * item????????????????????????????????????item+item???????????????
 * https://blog.csdn.net/lin_dianwei/article/details/78687487   item?????????????????????????????????
 * https://blog.csdn.net/weixin_37577039/   Item???????????????????????????
 * https://blog.csdn.net/qq_38225558/article/details/80627273   RecyclerView????????????????????????item+item????????????????????????
 * https://blog.csdn.net/qq_38225558/article/details/80608527   RecyclerView???????????????item+item????????????????????????
 */
//item????????????????????????????????????item+item???????????????
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    //????????????????????????
    private TimetableAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View v, ViewName viewName,int position);
        void onItemLongClick(View v, ViewName viewName,int position);
    }

    //???????????????????????????????????????
    public void setOnItemClickListener(TimetableAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();      //getTag()????????????
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
        int position = (int) view.getTag();      //getTag()????????????
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.rv_many_table:
                    mOnItemClickListener.onItemLongClick(view, ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemLongClick(view, ViewName.ITEM, position);
                    break;
            }
        }
        return true;
    }


}
