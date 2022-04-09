package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.util.ToastUtil;
import com.example.timeflies.dailog.DialogCustom;
import java.util.List;

public class AddCourse extends AppCompatActivity implements View.OnClickListener,DialogCustom.OnCenterItemClickListener{

    private Context context;
    private List<String> list;
    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        RecyclerView recyclerView = findViewById(R.id.rv_content);
        //1.1
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddCourse.this);
        //1.setLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        //2.1
        ContentAdapter contentAdapter = new ContentAdapter();
        //2.setAdapter适配器
        recyclerView.setAdapter(contentAdapter);

        //setHasFixedSize(true)方法使得RecyclerView能够固定自身size不受adapter变化的影响；
        //而setNestedScrollingeEnabled(false)方法则是进一步调用了RecyclerView内部NestedScrollingChildHelper对象的setNestedScrollingeEnabled(false)方法
        //进而，NestedScrollingChildHelper对象通过该方法关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void OnCenterItemClick(DialogCustom dialog, View view) {
        switch (view.getId()){
            case R.id.leave:
                Intent intent = new Intent(AddCourse.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.stay:
                ToastUtil.showMsg(AddCourse.this, "留下");
                break;
            case R.id.credit_clear:
                ToastUtil.showMsg(AddCourse.this, "清除");
                break;
            case R.id.credit_cancel:
                ToastUtil.showMsg(AddCourse.this, "取消");
                break;
            case R.id.credit_confirm:
                ToastUtil.showMsg(AddCourse.this, "确定");
                break;
        }

    }



//    返回按钮监听
    public void back(View view) {
//        自定义dialog
        DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.layout_dialog_back, new int[]{R.id.leave,R.id.stay});
        dialogCustom.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);
        //显示
        dialogCustom.show();
    }

//    保存按钮
    public void save(View view) {
        //     获取课程名称
        EditText et1 = findViewById(R.id.course_name);
        String course_name = et1.getText().toString();
        //      如果课程名称为空
        if(course_name.equals("")){
            String toast_text = getString(R.string.toast_save);
            ToastUtil.showMsg(AddCourse.this, toast_text);

        }else{
            Intent intent = new Intent(AddCourse.this,MainActivity.class);
            startActivity(intent);
        }
    }

//    设置学分按钮
    public void course_credit(View view) {
        //自定义学分dialog
        DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.layout_dialog_course,
                new int[]{R.id.course_credit,R.id.credit_clear,R.id.credit_cancel,R.id.credit_confirm},
                getString(R.string.dialog_credit),getString(R.string.dialog_credit_hint));
        dialogCustom.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);

        dialogCustom.setTitle(getString(R.string.dialog_credit));
        dialogCustom.setHint(getString(R.string.dialog_credit_hint));
        //显示
        dialogCustom.show();
    }

//    设置备注按钮
    public void course_editor(View view) {
        //自定义备注dialog
        DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.layout_dialog_course,
                new int[]{R.id.course_credit,R.id.credit_clear,R.id.credit_cancel,R.id.credit_confirm},
                getString(R.string.dialog_editor),null);
        dialogCustom.setTitle(getString(R.string.dialog_editor));
        dialogCustom.show();
    }

//    删除时间段按钮
    public void schedule_delete(View view) {
        ToastUtil.showMsg(AddCourse.this,"删除时间段");

    }

//    添加时间段按钮
    public void schedule_add(View view) {
        ToastUtil.showMsg(AddCourse.this,"添加时间段");

    }

//    添加授课老师
    public void add_teacher(View view) {

        //自定义授课老师dialog
        DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.layout_dialog_schedule,
                new int[]{R.id.credit_confirm},
                getString(R.string.course_teacher),getString(R.string.dialog_schedule_hint));
        dialogCustom.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);
        dialogCustom.setTitle(getString(R.string.course_teacher));
        dialogCustom.setHint(getString(R.string.dialog_schedule_hint));
        //显示
        dialogCustom.show();
    }
//    添加上课地点
    public void add_location(View view) {
        //自定义上课地点dialog
        DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.layout_dialog_schedule,
                new int[]{R.id.credit_confirm},
                getString(R.string.course_location),getString(R.string.dialog_schedule_hint));
        dialogCustom.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);

        dialogCustom.setTitle(getString(R.string.course_location));
        dialogCustom.setHint(getString(R.string.dialog_schedule_hint));
        //显示
        dialogCustom.show();
    }



}