package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.utils.ToastCustom;
import com.example.timeflies.utils.DialogCustom;

import java.util.ArrayList;
import java.util.List;

public class AddCourse extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private List<String> listSize = new ArrayList<>();
    private ImageView ivback;
    private ImageView ivsave;
    private View vAddCredit;
    private View vAddRemark;
    private View addItem;
    private DialogCustom dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        initView();
        setListenet();
        initContent();
    }

    /**
     * 初始化recycle，展示时间段的内容
     *
     */
    public void initContent(){
        listSize.add("1");
        recyclerView = findViewById(R.id.rv_content);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddCourse.this);
        recyclerView.setLayoutManager(layoutManager);
        contentAdapter = new ContentAdapter(AddCourse.this,listSize);
        recyclerView.setAdapter(contentAdapter);
        //添加动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //setHasFixedSize(true)方法使得RecyclerView能够固定自身size不受adapter变化的影响；
        //而setNestedScrollingeEnabled(false)方法则是进一步调用了RecyclerView内部NestedScrollingChildHelper对象的setNestedScrollingeEnabled(false)方法
        //进而，NestedScrollingChildHelper对象通过该方法关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 实例化控件
     *
     */
    public void initView(){
        ivback = findViewById(R.id.ivBack);
        ivsave = findViewById(R.id.ivSave);
        vAddCredit = findViewById(R.id.addCredit);
        vAddRemark = findViewById(R.id.addRemark);
        addItem = findViewById(R.id.addItem);
    }

    /**
     * 设置控件的监听
     */
    public void setListenet(){
        ivback.setOnClickListener(this);
        ivsave.setOnClickListener(this);
        vAddCredit.setOnClickListener(this);
        vAddRemark.setOnClickListener(this);
        addItem.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回按钮
            case R.id.ivBack:
                BtnBack();
                break;

            // 保存按钮
            case R.id.ivSave:
                BtnSave();
                break;

            //添加学分
            case R.id.addCredit:
                BtnCredit();
                break;

            //添加备注
            case R.id.addRemark:
                BtnRemark();
                break;

            //添加时间段按钮
            case R.id.addItem:
                contentAdapter.addItem(listSize.size());
                break;
        }
    }


    /**
     * 返回按钮
     *
     */
    public void BtnBack(){
        DialogCustom dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_back, 0.8);
        dialog.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCourse.this,MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 保存按钮
     *
     */
    public void BtnSave(){
        //获取课程名称
        EditText et1 = findViewById(R.id.course_name);
        String course_name = et1.getText().toString();
        //如果课程名称为空
        if(course_name.equals("")){
            ToastCustom.showMsgFalse(AddCourse.this, "请填写课程名称！");
        }else{
            ToastCustom.showMsgTrue(AddCourse.this, "保存成功");
            Intent intent = new Intent(AddCourse.this,MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 学分按钮
     *
     */
    public void BtnCredit(){
        dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_credit, 0.8);
        dialog.setTitle("学分").setEdit("0.0");
        dialog.setClearListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastCustom.showMsgTrue(AddCourse.this, "学分按钮的清除");
                dialog.setEdit("");
                dialog.dismiss();
            }
        });
        dialog.setCreditCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ToastCustom.showMsgTrue(AddCourse.this, "学分按钮的取消");
            }
        });
        dialog.setCreditConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ToastCustom.showMsgTrue(AddCourse.this, "学分按钮的确定");
            }
        });
        dialog.show();
    }

    /**
     * 备注按钮
     *
     */
    public void BtnRemark(){
        dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_credit, 0.8);
        dialog.setTitle("备注");
        dialog.setClearListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ToastCustom.showMsgTrue(AddCourse.this, "备注按钮的清除");
            }
        });
        dialog.setCreditCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastCustom.showMsgTrue(AddCourse.this, "备注按钮的取消");
                dialog.dismiss();
            }
        });
        dialog.setCreditConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastCustom.showMsgTrue(AddCourse.this, "备注按钮的确定");
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}