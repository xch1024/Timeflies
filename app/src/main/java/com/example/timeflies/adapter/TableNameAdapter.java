package com.example.timeflies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.ConfigData;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class TableNameAdapter extends RecyclerView.Adapter<TableNameAdapter.TableNameHolder> implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "xch";
    //记录当前点击位置
    //标记当前选择的选项
    private int index = -1;
    private List<ConfigData> list;
    private Context context;

    public TableNameAdapter(List<ConfigData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TableNameAdapter.TableNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_table_name, null);
        return new TableNameAdapter.TableNameHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableNameAdapter.TableNameHolder holder, int position) {
        setContView(holder, position);

    }

    /**
     * RecyclerView实现单选的三种⽅式 https://wenku.baidu.com/view/c665832f5b1b6bd97f192279168884868762b8e7?bfetype=new
     */
    private void setContView(TableNameHolder holder, int position) {
        ConfigData data = list.get(position);
        holder.table_name.setText(data.getClassName());

        if(data.isChecked()){
            holder.image_table.setImageResource(R.drawable.photo_radio_select);
            holder.table_name.setTextColor(Color.parseColor("#FF41964B"));
        }else{
            holder.image_table.setImageResource(R.drawable.photo_radio_none_select);
            holder.table_name.setTextColor(Color.parseColor("#FFF8F8FF"));
        }
        holder.image_table.setOnClickListener(TableNameAdapter.this);
        holder.image_table.setOnLongClickListener(TableNameAdapter.this);
        //初始化选择的课表
//        if(index == position){
//            TableNameHolder vhNew = (TableNameHolder) myRecycle.findViewHolderForAdapterPosition(index);
//            vhNew.image_table.setImageResource(R.drawable.photo_radio_none_select);
//            index = -1;
////            Log.d(TAG, "index == position:=== "+index);
//        }else if(index != -1){
//            TableNameHolder couponVh = (TableNameHolder) myRecycle.findViewHolderForAdapterPosition(index);
//            if(couponVh !=null){
//                couponVh.image_table.setImageResource(R.drawable.photo_radio_none_select);
//            }else{
//                notifyItemChanged(position);
//            }
//            index = position;
//            TableNameHolder vhNew = (TableNameHolder) myRecycle.findViewHolderForAdapterPosition(index);
//            vhNew.image_table.setImageResource(R.drawable.photo_radio_select);
//            Log.d(TAG, "index != position && index != -1:=== "+index);
//        }else if(index ==-1){
//            TableNameHolder couponVh = (TableNameHolder) myRecycle.findViewHolderForAdapterPosition(index);
//            index =position;
//            couponVh.image_table.setImageResource(R.drawable.photo_radio_select);
//            Log.d(TAG, "index ==-1:=== "+index);
//        }

        holder.image_table.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(view, position);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int position = (int) view.getTag();
        if(onItemClickListener != null){
            onItemClickListener.onItemLongClick(view, position);
        }
        return true;
    }


    public class TableNameHolder extends RecyclerView.ViewHolder {

        private TextView  table_name;
        private ImageView image_table;

        public TableNameHolder(@NonNull View itemView) {
            super(itemView);
            initView();
        }

        private void initView() {
            table_name = itemView.findViewById(R.id.table_name);
            image_table = itemView.findViewById(R.id.image_table);

            image_table.setOnClickListener(TableNameAdapter.this);
            image_table.setOnLongClickListener(TableNameAdapter.this);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
