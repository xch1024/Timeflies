package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeflies.R;
import com.example.timeflies.adapter.CourseNameAdapter;
import com.example.timeflies.model.CourseData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class MenuAdded extends AppCompatActivity implements View.OnClickListener{

    //标题-返回按钮-保存按钮-提示信息-没课时的背景-添加按钮
    private TextView tvTitle;
    private ImageView ivRemove, ivBack;
    private TextView hint;
    private View bg;
    private View vAdd;

    private ScheduleSqlite sqlite = new ScheduleSqlite(this);
    private RecyclerView rvCourseName;
    private List<CourseData> list = new ArrayList<>();

    private String TAG = "xch";

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initCourseNameView();
        setListener();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_added);
        Log.d(TAG, "onCreate: ");
    }

    private void initView(){
        tvTitle = findViewById(R.id.tvTitle);
        ivRemove = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        hint = findViewById(R.id.hint);
        bg = findViewById(R.id.bg_none);
        vAdd = findViewById(R.id.addItem);
        tvTitle.setText(R.string.menu_added_view);
        ivRemove.setImageResource(R.drawable.remove);
        bg.setVisibility(View.GONE);
    }

    /**
     * 所有课程
     */
    private void initCourseNameView(){
        list.clear();
        list = sqlite.listAll();
        int listSize = list.size();
        rvCourseName = findViewById(R.id.rv_className);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MenuAdded.this,2);
        rvCourseName.setLayoutManager(gridLayoutManager);
        CourseNameAdapter courseNameAdapter = new CourseNameAdapter(MenuAdded.this, list);
        courseNameAdapter.setOnItemClickListener(MyItemClickListener);
        if(listSize > 0 ){
            hint.setVisibility(View.VISIBLE);
            String[] courseNames = new String[listSize];
            for (int i = 0; i < listSize; i++) courseNames[i] = list.get(i).getCourseName();
            rvCourseName.setAdapter(courseNameAdapter);
            rvCourseName.setNestedScrollingEnabled(false);
        }else{
            bg.setVisibility(View.VISIBLE);
            hint.setVisibility(View.GONE);
        }
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivRemove.setOnClickListener(this);
        vAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ivSave:
                BtnClear();
                break;
            case R.id.addItem:
                intentActivity(CourseActivity.class);
                break;
        }
    }

    /**
     * 清空全部课程对话框
     */
    private void BtnClear() {
        DialogCustom dialogCustom = new DialogCustom(MenuAdded.this, R.layout.layout_dialog_back, 0.8);
        dialogCustom.setContent("真的要清空课表吗？这将无法恢复。");
        dialogCustom.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlite.clear();
                initCourseNameView();
                ToastCustom.showMsgTrue(MenuAdded.this, "清空成功");
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }

    /**
     * 点击和长按事件
     * 修改课程-删除课程
     */
    private CourseNameAdapter.OnItemClickListener MyItemClickListener = new CourseNameAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            ToastCustom.showMsgWarning(MenuAdded.this,"修改"+position);

            CourseData courseData = list.get(position);
            Intent intent = new Intent(MenuAdded.this, AddCourse.class);
            intent.putExtra("course", courseData);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View v, int position) {
            BtnDelete(position);
        }
    };

    /**
     * 删除单个课程对话框
     * @param position
     */
    private void BtnDelete(int position){
        DialogCustom dialogCustom = new DialogCustom(MenuAdded.this,R.layout.layout_dialog_back,0.8);
        dialogCustom.setContent("确定要删除该课程吗？它的所有时间段都将会被删除");
        dialogCustom.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(position);
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }

    /**
     * 删除
     */
    private void delete(int position) {
        CourseData courseData = list.get(position);
        int delete = sqlite.delete(courseData.getId());
        if(delete > 0 ){
            ToastCustom.showMsgTrue(MenuAdded.this,"删除成功");
            initCourseNameView();
        }else{
            ToastCustom.showMsgTrue(MenuAdded.this,"删除失败");
        }
    }

    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MenuAdded.this, cls);
        startActivity(intent);
    }
}