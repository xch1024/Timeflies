package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.listener.CallBackListener;
import com.example.timeflies.model.contentItem;
import com.example.timeflies.utils.ToastUtil;
import com.example.timeflies.dailog.DialogCustom;
import java.util.List;

public class AddCourse extends AppCompatActivity implements View.OnClickListener,DialogCustom.OnCenterItemClickListener, CallBackListener {

    private RecyclerView recyclerView;
    List<contentItem> contentList;
    private ContentAdapter contentAdapter;

    private ImageView ivback;
    private ImageView ivsave;
    private View vAddCredit;
    private View vAddRemark;
    private View rv_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        ivback = findViewById(R.id.ivBack);
        ivsave = findViewById(R.id.ivSave);
        vAddCredit = findViewById(R.id.addCredit);
        vAddRemark = findViewById(R.id.addRemark);
        rv_add = findViewById(R.id.rv_add);

        ivback.setOnClickListener(this);
        ivsave.setOnClickListener(this);
        vAddCredit.setOnClickListener(this);
        vAddRemark.setOnClickListener(this);
        rv_add.setOnClickListener(this);

        contentShow();


    }

    /**
     * 初始化recycle，展示时间段的内容
     *
     */
    public void contentShow(){
        recyclerView = findViewById(R.id.rv_content);
        //1.1
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddCourse.this);
        //1.setLayoutManager
        recyclerView.setLayoutManager(layoutManager);
        //2.1
        contentAdapter = new ContentAdapter(AddCourse.this, contentList, this);
        //2.setAdapter适配器
        recyclerView.setAdapter(contentAdapter);

        //setHasFixedSize(true)方法使得RecyclerView能够固定自身size不受adapter变化的影响；
        //而setNestedScrollingeEnabled(false)方法则是进一步调用了RecyclerView内部NestedScrollingChildHelper对象的setNestedScrollingeEnabled(false)方法
        //进而，NestedScrollingChildHelper对象通过该方法关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);
    }


    /**
     * 对话框里的按钮监听
     * @param dialog
     * @param view
     */
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


    /**
     * 回滚，没懂是啥
     * 博客地址 https://blog.csdn.net/yezhuAndroid/article/details/79363857
     * @param code
     * @param object
     */
    @Override
    public void CallBack(int code, Object object) {
        switch (code){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回按钮
            case R.id.ivBack:
                DialogCustom dialogCustomBack = new DialogCustom(AddCourse.this, R.layout.layout_dialog_back, new int[]{R.id.leave,R.id.stay});
                dialogCustomBack.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);
                dialogCustomBack.show();

                break;
            // 保存按钮
            case R.id.ivSave:
                //获取课程名称
                EditText et1 = findViewById(R.id.course_name);
                String course_name = et1.getText().toString();
                //如果课程名称为空
                if(course_name.equals("")){
                    String toast_text = getString(R.string.toast_save);
                    ToastUtil.showMsg(AddCourse.this, toast_text);
                }else{
                    Intent intent = new Intent(AddCourse.this,MainActivity.class);
                    startActivity(intent);
                }

                break;
            //添加学分
            case R.id.addCredit:
                DialogCustom dialogCustomCredit = new DialogCustom(AddCourse.this, R.layout.layout_dialog_course,
                        new int[]{R.id.course_credit,R.id.credit_clear,R.id.credit_cancel,R.id.credit_confirm},
                        getString(R.string.dialog_credit),getString(R.string.dialog_credit_hint));
                /**
                 * 这里没写好
                 *
                 */
                dialogCustomCredit.setOnCenterItemClickListener((DialogCustom.OnCenterItemClickListener)this);

                dialogCustomCredit.setTitle(getString(R.string.dialog_credit));
                dialogCustomCredit.setHint(getString(R.string.dialog_credit_hint));
                //显示
                dialogCustomCredit.show();

                break;
            //添加备注
            case R.id.addRemark:
                DialogCustom dialogCustomRemark = new DialogCustom(AddCourse.this, R.layout.layout_dialog_course,
                        new int[]{R.id.course_credit,R.id.credit_clear,R.id.credit_cancel,R.id.credit_confirm},
                        getString(R.string.dialog_editor),null);
                dialogCustomRemark.setTitle(getString(R.string.dialog_editor));
                dialogCustomRemark.show();

                break;
            //添加时间段按钮
            case R.id.rv_add:
                Log.i("夏成昊", "onClick: 添加时间段");
                ToastUtil.showMsg(AddCourse.this,"添加时间段");
                break;
        }
    }
}