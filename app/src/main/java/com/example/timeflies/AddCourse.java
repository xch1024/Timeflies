package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private DialogCustom dialogBack, dialogCredit;
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
                dialogBack = new DialogCustom(AddCourse.this, R.layout.layout_dialog_back, 0.8);
                dialogBack.show();
                break;

            // 保存按钮
            case R.id.ivSave:
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
                break;

            //添加学分
            case R.id.addCredit:
                dialogCredit = new DialogCustom(AddCourse.this, R.layout.layout_dialog_credit, 0.8);
                dialogCredit.show();
                break;

            //添加备注
            case R.id.addRemark:

                break;

            //添加时间段按钮
            case R.id.addItem:
                contentAdapter.addItem(listSize.size());
                //默认滑到最底部
//                recyclerView.scrollToPosition(contentAdapter.getItemCount()-1);
                break;
        }
    }
}