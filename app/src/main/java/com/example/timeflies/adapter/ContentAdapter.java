package com.example.timeflies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.AddCourse;
import com.example.timeflies.R;
import com.example.timeflies.dailog.DialogCustom;
import com.example.timeflies.listener.CallBackListener;
import com.example.timeflies.model.contentItem;
import com.example.timeflies.utils.ToastUtil;

import java.util.List;


public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentHolder> {

    private final Context context;
    private final List<contentItem> contentList;
    private final CallBackListener CallBackListener;//点击加减时回调
    private contentItem contentItem;

    public ContentAdapter(Context context, List<contentItem> contentList, com.example.timeflies.listener.CallBackListener callBackListener){
        this.context = context;
        this.contentList = contentList;
        this.CallBackListener = callBackListener;

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
//        contentItem = contentList.get(position);
//        if(contentItem.isAdded){
//
//        }else{
//
//        }
        holder.bindData();
    }

    /**
     * 返回Item总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return 2;
    }


    //内部类，绑定控件
    public class ContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View rv_delete;
        private final View rv_week;
        private final View rv_time;
        private final View rv_teacher;
        private final View rv_location;
        private final CheckBox rv_custom;
        public ContentHolder(@NonNull View itemView) {
            super(itemView);

            rv_delete = itemView.findViewById(R.id.rv_delete);
            rv_week = itemView.findViewById(R.id.rv_week);
            rv_time = itemView.findViewById(R.id.rv_time);
            rv_teacher = itemView.findViewById(R.id.rv_teacher);
            rv_location = itemView.findViewById(R.id.rv_location);
            rv_custom = itemView.findViewById(R.id.rv_custom);

            rv_week.setOnClickListener(this);
            rv_time.setOnClickListener(this);
            rv_teacher.setOnClickListener(this);
            rv_location.setOnClickListener(this);
            rv_delete.setOnClickListener(this);
            rv_custom.setOnClickListener(this);
        }

        public void bindData() {

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rv_delete:
                    Log.i("夏成昊", "onClick: 删除");
                    break;
                case R.id.rv_week:
                    Log.i("夏成昊", "onClick: 第几周");
                    break;
                case R.id.rv_time:
                    Log.i("夏成昊", "onClick: 第几节");
                    break;
                case R.id.rv_custom:
                    Log.i("夏成昊", "onClick: 自定义时间");
                    break;
                case R.id.rv_teacher:
                    Log.i("夏成昊", "onClick: 授课老师");
//                    DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.layout_dialog_schedule,
//                            new int[]{R.id.credit_confirm},
//                            getString(R.string.course_teacher),getString(R.string.dialog_schedule_hint));
//                    dialogCustom.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);
//                    dialogCustom.setTitle(getString(R.string.course_teacher));
//                    dialogCustom.setHint(getString(R.string.dialog_schedule_hint));
//                    //显示
//                    dialogCustom.show();
                    break;
                case R.id.rv_location:
                    Log.i("夏成昊", "onClick: 上课地点");
//                    DialogCustom dialogCustom1 = new DialogCustom(AddCourse.this, R.layout.layout_dialog_schedule,
//                            new int[]{R.id.credit_confirm},
//                            getString(R.string.course_location),getString(R.string.dialog_schedule_hint));
//                    dialogCustom1.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);
//
//                    dialogCustom1.setTitle(getString(R.string.course_location));
//                    dialogCustom1.setHint(getString(R.string.dialog_schedule_hint));
//                    //显示
//                    dialogCustom1.show();
                    break;
            }
        }
    }

}
