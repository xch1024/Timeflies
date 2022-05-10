package com.example.timeflies.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.TimeTableData;
import com.example.timeflies.sqlite.ScheduleSqlite;

import java.util.List;


public class TableChoiceAdapter extends RecyclerView.Adapter<TableChoiceAdapter.TableHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<TimeTableData> mList;//数据源
    private Context mContext;//上下文
    public TableChoiceAdapter(Context context, List<TimeTableData> list){
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvtime_manage,parent,false);
        return new TableChoiceAdapter.TableHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull TableChoiceAdapter.TableHolder holder, int position) {
        TimeTableData data = mList.get(position);

        //默认时间表删除键不可见
        if(position == 0){
            holder.ivDel.setVisibility(View.GONE);
        }
        holder.tvName.setText(data.getTableName());

        holder.itemView.setTag(position);
        holder.tvName.setTag(position);
        holder.ivDel.setTag(position);
        holder.ivUpdate.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class TableHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivDel, ivUpdate;

        public TableHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivDel = itemView.findViewById(R.id.ivDelete);
            ivUpdate = itemView.findViewById(R.id.ivEdit);

            tvName.setOnClickListener(TableChoiceAdapter.this);
            ivDel.setOnClickListener(TableChoiceAdapter.this);
            ivUpdate.setOnClickListener(TableChoiceAdapter.this);

            ivDel.setOnLongClickListener(TableChoiceAdapter.this);

        }
    }

//=====================================================================
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    public interface OnItemClickListener{
        void onItemClick(View v, ContentAdapter.ViewName viewName, int position);
        void onItemLongClick(View v, int position);
    }

    //声明自定义的接口
    private TableChoiceAdapter.OnItemClickListener mOnItemClickListener;
    //定义方法并传给外面的使用者
    public void setOnItemClickListener(TableChoiceAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }


    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.clockManage:
                    mOnItemClickListener.onItemClick(view, ContentAdapter.ViewName.PRACTISE, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(view, ContentAdapter.ViewName.ITEM, position);
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
                    mOnItemClickListener.onItemLongClick(view, position);
                    break;
            }
        }
        return false;
    }

//=================================================================

    /**
     * 新建时间表
     */
    public void insertTable(Context context, String target){
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put("tableName", target);
            db.insert("tableNames", null, values);
        }
        db.close();
        mList.add(new TimeTableData(0, target));
        notifyDataSetChanged();
    }

    /**
     * 删除时间表
     */
    public void delTable(Context context, int id){
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()){
           db.delete("tableNames", "_id= ?", new String[]{String.valueOf(id)});
        }
        db.close();
        notifyDataSetChanged();
    }



}
