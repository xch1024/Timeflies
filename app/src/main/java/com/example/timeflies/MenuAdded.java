package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.adapter.CourseNameAdapter;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class MenuAdded extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTitle;
    private ImageView ivRemove, ivBack;
    private RecyclerView rvCourseName;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_added);
        initView();
        setListener();
        initCourseNameView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.tvTitle);
        ivRemove = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        tvTitle.setText(R.string.menu_added);
        ivRemove.setImageResource(R.drawable.remove);
    }

    private void initCourseNameView(){
        list.add("数据结构"); list.add("数据结构");   list.add("数据结构");    list.add("数据结构");   list.add("数据结构");   list.add("数据结构");        list.add("数据结构");
        list.add("数据结构"); list.add("数据结构");
        rvCourseName = findViewById(R.id.rv_className);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MenuAdded.this,2);
        rvCourseName.setLayoutManager(gridLayoutManager);
        CourseNameAdapter nameAdapter = new CourseNameAdapter(MenuAdded.this,list);
        rvCourseName.setAdapter(nameAdapter);
        rvCourseName.setNestedScrollingEnabled(false);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivRemove.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MainActivity.class);
                break;
            case R.id.ivSave:
                ToastCustom.showMsgTrue(this, "清空");
                break;
        }
    }

    /**
     * 页面跳转
     *
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MenuAdded.this, cls);
        startActivity(intent);
    }
}