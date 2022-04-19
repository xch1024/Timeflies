package com.example.timeflies.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
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

    //popWindow菜单   是否反馈对话框
    private TextView mNo, mSure;

    //添加课程页面    返回按钮对话框
    private TextView mLeave, mStay;

    //添加课程页面    学分、备注按钮对话框
    private TextView mClear, mCancel, mConfirm;

    //添加课程页面    授课老师、上课地点对话框
    private TextView mTitle,tTitle, tConfirm;
    private EditText tEdit;

    //课表数据页面    当前周、一天课程节数、学期周数对话框
    private TextView sTitle, sEdit, sMax, sCancel, sConfirm;
    private EditText mEdit;
    private String title;

    private String edit;
    private String teacherTitle;
    private String scheduleTitle, scheduleEdit, scheduleMax;


    //课表数据页面    课表名称对话框
    private TextView tableCancel, tableConfirm;
    private EditText tableEdit;



    private String nEdit;

//《===================================》
    //设置学分、备注标题
        public DialogCustom setTitle(String title) {
            this.title = title;
            mTitle.setText(title);
            return this;
        }

    //设置学分、备注输入框文字
        public DialogCustom setEdit(String edit) {
            this.edit = edit;
            mEdit.setText(edit);
            return this;
        }
        public String getEdit() {
            return edit;
        }

    //设置授课老师、上课地点标题文字
        public DialogCustom setTeacherTitle(String teacherTitle) {
        this.teacherTitle = teacherTitle;
        tTitle.setText(teacherTitle);
        return this;
    }

    //设置当前周、一天课程节数、学期周数的标题
        public DialogCustom setScheduleTitle(String scheduleTitle) {
        this.scheduleTitle = scheduleTitle;
        sTitle.setText(scheduleTitle);
        return this;
    }


    //设置当前周、一天课程节数、学期周数的输入框
        public String getScheduleEdit() {
        scheduleEdit = sEdit.getText().toString();
        return scheduleEdit;
    }
        public DialogCustom setScheduleEdit(String scheduleEdit) {
        this.scheduleEdit = scheduleEdit;
        sEdit.setText(scheduleEdit);
        return this;
    }

    //设置当前周、一天课程节数、学期周数的范围
        public DialogCustom setScheduleMax(String scheduleMax) {
        this.scheduleMax = scheduleMax;
        sMax.setText(scheduleMax);
        return this;
    }

    //设置课程名称输入框
        public String getnEdit() {
            nEdit = tableEdit.getText().toString();
            return nEdit;
        }
        public void setnEdit(String nEdit) {
            this.nEdit = nEdit;
            tableEdit.setText(nEdit);
        }

//    《=============================》
    public DialogCustom(@NonNull Context context, int layoutId, Double width) {
        this(context, R.style.dialog, layoutId, width);
    }

    private DialogCustom(@NonNull Context context, int themeResId, int layoutId, Double width) {
        super(context, themeResId);
        setContentView(layoutId);
        initDialog(width);
    }

    private void initDialog(Double width) {
        initView();
        initSize(width);
    }

    /**
     * 初始化控件
     *
     */
    private void initView() {
        //返回
        mLeave = findViewById(R.id.leave);
        mStay = findViewById(R.id.stay);
        //学分、备注
        mTitle = findViewById(R.id.title);
        mEdit = findViewById(R.id.edit);
        mClear = findViewById(R.id.credit_clear);
        mCancel = findViewById(R.id.credit_cancel);
        mConfirm = findViewById(R.id.credit_confirm);
        //授课老师、上课地点
        tTitle = findViewById(R.id.teacher_title);
        tEdit = findViewById(R.id.teacher_edit);
        tConfirm =findViewById(R.id.teacher_confirm);
        //pop菜单
        mNo = findViewById(R.id.no);
        mSure = findViewById(R.id.sure);
        //当前周、一天课程节数、学期周数
        sTitle = findViewById(R.id.schedule_title);
        sEdit = findViewById(R.id.schedule_edit);
        sMax = findViewById(R.id.schedule_max);
        sCancel = findViewById(R.id.schedule_cancel);
        sConfirm = findViewById(R.id.schedule_confirm);
        //课程名称
        tableEdit = findViewById(R.id.table_edit);
        tableCancel = findViewById(R.id.table_cancel);
        tableConfirm = findViewById(R.id.table_confirm);
    }

//  <=================================================================>
    /**
     * 设置dialog的宽度
     *
     * @param width
     */
    public void initSize(Double width){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * width);
        getWindow().setAttributes(p);
        //点击外部Dialog消失
        setCanceledOnTouchOutside(true);
    }

    //《================给对话框里的按钮设置监听=====================》
    /**
     * 菜单的联系按钮对话框
     *
     * @param listener
     */
        public void setMenuConfirmListener(View.OnClickListener listener){
            mNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }
        public void setMenuCancelListener(View.OnClickListener listener){
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    /**
     * 返回对话框监听
     * 只有取消和确定按钮
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

    /**
     * 学分、备注按钮
     * 清除、取消、确定 三个按钮
     * 有一个输入框
     * @param listener
     */
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

    /**
     * 授课老师、上课地点
     * 一个按钮的对话框
     *
     * @param listener
     */
        public void setTeacherConfirmListener(View.OnClickListener listener){
            tConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }

    /**
     * 课表数据当前周、一天课程节数、学期周数
     *
     */
        public void setScheduleCancelListener(View.OnClickListener listener){
        sCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
        public void setScheduleConfirmListener(View.OnClickListener listener){
        sConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    /**
     * 课表数据课程名称
     */
        public void setTableNameCancelListener(View.OnClickListener listener){
            tableCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }
        public void setTableNameConfirmListener(View.OnClickListener listener){
            tableConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }
}
