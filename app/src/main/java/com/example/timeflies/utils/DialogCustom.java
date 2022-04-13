package com.example.timeflies.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.timeflies.R;

/**
 * 参考
 * https://blog.csdn.net/sakurakider/article/details/80735400
 * https://www.cnblogs.com/holyday/p/7284394.html
 * https://blog.csdn.net/xuyin1204/article/details/109351800
 * https://blog.csdn.net/weixin_40481076/article/details/106262508?spm=1001.2014.3001.5501
 * https://blog.csdn.net/weixin_43607099/article/details/105822321?spm=1001.2014.3001.5501
 * 
 * https://blog.csdn.net/weixin_44002043/article/details/121858407
 */
public class DialogCustom extends Dialog{

    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private TextView mLeave, mStay, mClear, mCancel, mConfirm;
    private TextView mTitle,tTitle, tConfirm;
    private EditText mEdit;
    private String title, edit, teacherTitle;


    public DialogCustom setTitle(String title) {
        this.title = title;
        mTitle.setText(title);
        return this;
    }

    public DialogCustom setEdit(String edit) {
        this.edit = edit;
        mEdit.setText(edit);
        return this;
    }

    public void setTeacherTitle(String teacherTitle) {
        this.teacherTitle = teacherTitle;
        tTitle.setText(teacherTitle);
    }

    public DialogCustom(@NonNull Context context, int layoutId, Double width) {
        this(context, R.style.dialog, layoutId,width);
    }

    private DialogCustom(@NonNull Context context, int themeResId, int layoutId, Double width) {
        super(context, themeResId);
        setContentView(layoutId);

        initDialog(context, layoutId, width);
    }

    private void initDialog(Context context, int layoutId, Double width) {
        initView();
        initSize(width);
    }

    /**
     * 初始化控件
     *
     */
    private void initView() {
        mLeave = findViewById(R.id.leave);
        mStay = findViewById(R.id.stay);
        mClear = findViewById(R.id.credit_clear);
        mCancel = findViewById(R.id.credit_cancel);
        mConfirm = findViewById(R.id.credit_confirm);
        mTitle = findViewById(R.id.title);
        mEdit = findViewById(R.id.edit);
        tTitle = findViewById(R.id.teacher_title);
        tConfirm =findViewById(R.id.teacher_confirm);       
    }

    /**
     * 设置dialog的宽度
     *
     * @param wid
     */
    public void initSize(Double wid){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * wid);
        getWindow().setAttributes(p);
        //点击外部Dialog消失
        setCanceledOnTouchOutside(true);
    }


    /**
     * 设置一些点击事件
     *
     * @param listener
     */
    public void setCancelListener(View.OnClickListener listener){
        mLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    public void setConfirmListener(View.OnClickListener listener){
        mStay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    public void setClearListener(View.OnClickListener listener){
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
    public void setCreditCancelListener(View.OnClickListener listener){
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    public void setCreditConfirmListener(View.OnClickListener listener){
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }


    
    public void setTeacherConfirmListener(View.OnClickListener listener){
        tConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

}
