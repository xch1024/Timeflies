package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.utils.ToastCustom;
import com.example.timeflies.utils.DialogCustom;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.jianshu.com/p/971396467a62   RecyclerView系列之三：处理item的点击事件
 * https://www.jianshu.com/p/15d2ddc1eba7   如何正确的给RecycleView添加点击事件
 * https://blog.csdn.net/jk_szl/article/details/120970030   recycleView页面点击跳转
 *
 */
public class AddCourse extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private static LinearLayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private List<String> listSize = new ArrayList<>();
    private ImageView ivBack,ivSave, ivColor;
    private TextView tvColor;
    private LinearLayout vColor, vAddCredit, vAddRemark, addItem;
    private DialogCustom dialog;
    private LinearLayout rv_teacher, rv_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        initView();
        setListener();
        initContent();
    }

    /**
     * 初始化recycle，展示时间段的内容
     * https://blog.csdn.net/weixin_42229694/article/details/103513003?spm=1001.2014.3001.5501 最简单的RecyclerView Item动画全解析
     * https://www.jianshu.com/p/3acc395ae933 RecycleView4种定位滚动方式演示
     *
     */
    public void initContent(){
        listSize.add("1");
        recyclerView = findViewById(R.id.rv_content);
        layoutManager = new LinearLayoutManager(AddCourse.this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        contentAdapter = new ContentAdapter(AddCourse.this,listSize);
        recyclerView.setAdapter(contentAdapter);
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
        vColor = findViewById(R.id.color);
        ivColor = findViewById(R.id.colorMap);
        ivBack = findViewById(R.id.ivBack);
        ivSave = findViewById(R.id.ivSave);
        vAddCredit = findViewById(R.id.addCredit);
        vAddRemark = findViewById(R.id.addRemark);
        addItem = findViewById(R.id.addItem);
        tvColor = findViewById(R.id.colorText);
        View view = LayoutInflater.from(AddCourse.this).inflate(R.layout.layout_rvcontent, null);
        rv_teacher = view.findViewById(R.id.rv_teacher);
        rv_location = view.findViewById(R.id.rv_location);
    }

    /**
     * 设置控件的监听
     */
    public void setListener(){
        ivBack.setOnClickListener(this);
        ivSave.setOnClickListener(this);
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
            case R.id.color:
                ivColor.setColorFilter(Color.parseColor("#FFcc0000"));
                tvColor.setTextColor(Color.parseColor("#FFcc0000"));
                ToastCustom.showMsgTrue(this, "更改颜色");
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
                ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(contentAdapter.getItemCount() - 1, 0);
                scrollToEnd();
                break;
            case R.id.rv_teacher:
                BtnTeacher();
                break;
        }
    }

    /**
     * scrollTo滚动到指定位置
     * https://blog.csdn.net/neabea2016/article/details/101209787 NestScrollView滚动到指定位置
     *
     */
    private void scrollToEnd(){
        nestedScrollView = findViewById(R.id.nsv);
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
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
                dialog.setEdit("0.0");
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

    /**
     * 添加老师按钮
     *
     */
    private void BtnTeacher(){
        dialog = new DialogCustom(AddCourse.this,R.layout.layout_dialog_teacher,0.8);
        dialog.setTeacherTitle("授课老师");
        dialog.setTeacherConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastCustom.showMsgFalse(getApplicationContext(), "授课老师的确定按钮");
            }
        });
        dialog.show();
    }
}