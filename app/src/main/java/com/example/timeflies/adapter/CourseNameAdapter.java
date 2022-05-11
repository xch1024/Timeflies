package com.example.timeflies.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timeflies.R;
import com.example.timeflies.model.CourseData;

import java.util.List;

/**
 * 课程管理页面、已添加课程显示
 */
public class CourseNameAdapter extends RecyclerView.Adapter<CourseNameAdapter.CourseNameHolder> implements View.OnClickListener, View.OnLongClickListener{

    private Context mContext;
    private List<CourseData> list;

    public CourseNameAdapter(Context mContext, List<CourseData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public CourseNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_course_name,null);
        return new CourseNameHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseNameAdapter.CourseNameHolder holder, int position) {
        CourseData data = list.get(position);
        holder.tvName.setText(data.getCourseName());
        holder.color.setColorFilter(Color.parseColor(data.getCourseColor()));

        holder.cName.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseNameHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private View cName;
        private ImageView color;

        public CourseNameHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.className);
            cName = itemView.findViewById(R.id.cName);
            color = itemView.findViewById(R.id.color);

            cName.setOnClickListener(CourseNameAdapter.this);
            cName.setOnLongClickListener(CourseNameAdapter.this);
        }

    }

//=============================================================
    public interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    //声明自定义的接口
    private CourseNameAdapter.OnItemClickListener mOnItemClickListener;
    //定义方法并传给外面的使用者
    public void setOnItemClickListener(CourseNameAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.cName:
                    mOnItemClickListener.onItemClick(view, position);
                    break;
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int position = (int) view.getTag();      //getTag()获取数据
        if(mOnItemClickListener != null){
            switch (view.getId()){
                case R.id.cName:
                    mOnItemClickListener.onItemLongClick(view, position);
                    break;
            }
        }
        return true;
    }
}
