package com.example.timeflies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.AddCourse;
import com.example.timeflies.R;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.List;

/**
 *
 *
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {


    private Context mcontext;
    private List<String> listSize;
    private DialogCustom dialog;



    public ContentAdapter(Context mcontext, List<String> listSize) {
        this.mcontext = mcontext;
        this.listSize = listSize;
    }

    @NonNull
    @Override
    public ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rvcontent,parent,false);
        ContentHolder holder = new ContentHolder(inflater);
        return holder;
    }

    //position下标从0开始
    @Override
    public void onBindViewHolder(@NonNull ContentHolder holder,int position) {
        holder.bindData(listSize,position);
    }

    /**
     * 返回Item总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return listSize.size();
    }

    /**
     * 添加item
     *
     * @param position
     */
    public void addItem(int position){
        //      在list中添加数据，并通知条目加入一条
        listSize.add(position, "添加" + position);
        //添加动画
        notifyItemInserted(position);
    }

    /**
     * 删除item
     * @param position
     */
    public  void delItem(int position){
        listSize.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }



    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View delItem;
        private final View rv_week;
        private final View rv_time;
        private final View rv_teacher;
        private final View rv_location;
        private final CheckBox rv_custom;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            delItem = itemView.findViewById(R.id.delItem);
            rv_week = itemView.findViewById(R.id.rv_week);
            rv_time = itemView.findViewById(R.id.rv_time);
            rv_teacher = itemView.findViewById(R.id.rv_teacher);
            rv_location = itemView.findViewById(R.id.rv_location);
            rv_custom = itemView.findViewById(R.id.rv_custom);

            rv_week.setOnClickListener(this);
            rv_time.setOnClickListener(this);
            rv_teacher.setOnClickListener(this);
            rv_location.setOnClickListener(this);
            delItem.setOnClickListener(this);
            rv_custom.setOnClickListener(this);
        }

        public void bindData(List<String> mlist, int position) {
            mlist.get(position);
        }

        /**
         * 一些按钮
         *
         */
        public void BtnDel(){
            if(listSize.size()== 1){
                ToastCustom.showMsgFalse(mcontext.getApplicationContext(), "至少要保留一个时间段！");
            }else{
                delItem(getAdapterPosition());
            }
        }

        public void BtnTeacher(){
            dialog = new DialogCustom(mcontext.getApplicationContext(),R.layout.layout_dialog_teacher,0.8);
//            dialog.setTeacherTitle("授课老师");
            dialog.setTeacherConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastCustom.showMsgFalse(mcontext.getApplicationContext(), "授课老师的确定按钮");
                }
            });
            dialog.show();
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.delItem:
                    BtnDel();
                    break;
                case R.id.rv_week:

                    break;
                case R.id.rv_time:

                    break;
                case R.id.rv_custom:

                    break;
                case R.id.rv_teacher:
                    BtnTeacher();
                    break;
                case R.id.rv_location:
//
                    break;
            }
        }
    }


}
