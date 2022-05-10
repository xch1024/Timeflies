package com.example.timeflies.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

//layout_dialog_menu ===================================================================================
    private TextView mNo, mSure;

//layout_dialog_back ===================================================================================
    private TextView mLeave, mStay;
    private TextView back_content;
    private String content;
    private String leave, stay;


    public DialogCustom setContent(String content) {
        this.content = content;
        back_content.setText(content);
        return this;
    }

    public DialogCustom setLeave(String leave) {
        this.leave = leave;
        mLeave.setText(leave);
        return this;
    }

    public DialogCustom setStay(String stay) {
        this.stay = stay;
        mStay.setText(stay);
        return this;
    }

//layout_dialog_credit ===================================================================================
    private TextView mTitle, mClear, mCancel, mConfirm;
    private EditText mEdit;
    private String title;
    private String edit;

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

    public String getEdit() {
        edit = mEdit.getText().toString();
        return edit;
    }

//layout_dialog_add_lesson ===================================================================================
    private RadioGroup radioGroup;
    private EditText week_start, week_end, week_day, step_start, step_end, teacher_name, class_room;
    private String weekStart, weekEnd, weekDay, stepStart, stepEnd, teacherName, classRoom;
    private int radio;
    private TextView add_cancel, add_confirm;

    public String getWeekStart() {
        weekStart = week_start.getText().toString();
        return weekStart;
    }

    public DialogCustom setWeekStart(String weekStart) {
        this.weekStart = weekStart;
        week_start.setText(weekStart);
        return this;
    }

    public String getWeekEnd() {
        weekEnd  = week_end.getText().toString();
        return weekEnd;
    }

    public DialogCustom setWeekEnd(String weekEnd) {
        this.weekEnd = weekEnd;
        week_end.setText(weekEnd);
        return this;
    }

    public String getWeekDay() {
        weekDay = week_day.getText().toString();
        return weekDay;
    }

    public DialogCustom setWeekDay(String weekDay) {
        this.weekDay = weekDay;
        week_day.setText(weekDay);
        return this;
    }

    public String getStepStart() {
        stepStart = step_start.getText().toString();
        return stepStart;
    }

    public DialogCustom setStepStart(String stepStart) {
        this.stepStart = stepStart;
        step_start.setText(stepStart);
        return this;
    }

    public String getStepEnd() {
        stepEnd = step_end.getText().toString();
        return stepEnd;
    }

    public DialogCustom setStepEnd(String stepEnd) {
        this.stepEnd = stepEnd;
        step_end.setText(stepEnd);
        return this;
    }

    public String getTeacherName() {
        teacherName = teacher_name.getText().toString();
        return teacherName;
    }

    public DialogCustom setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        teacher_name.setText(teacherName);
        return this;
    }

    public String getClassRoom() {
        classRoom = class_room.getText().toString();
        return classRoom;
    }

    public DialogCustom setClassRoom(String classRoom) {
        this.classRoom = classRoom;
        class_room.setText(classRoom);
        return this;
    }

    public int getRadio() {
        radio = radioGroup.getCheckedRadioButtonId();
        return radio;
    }

    public DialogCustom setRadio(int radio) {
        this.radio = radio;
        radioGroup.check(radio);
        return this;
    }

//layout_dialog_schedule ===================================================================================
    private TextView schedule_title, schedule_edit, schedule_max, schedule_cancel, schedule_confirm;
    private String scheduleTitle, scheduleEdit, scheduleMax;

    public DialogCustom setScheduleTitle(String scheduleTitle) {
        this.scheduleTitle = scheduleTitle;
        schedule_title.setText(scheduleTitle);
        return this;
    }

    public String getScheduleEdit() {
        scheduleEdit = schedule_edit.getText().toString();
        return scheduleEdit;
    }

    public DialogCustom setScheduleEdit(String scheduleEdit) {
        this.scheduleEdit = scheduleEdit;
        schedule_edit.setText(scheduleEdit);
        return this;
    }

    public DialogCustom setScheduleMax(String scheduleMax) {
        this.scheduleMax = scheduleMax;
        schedule_max.setText(scheduleMax);
        return this;
    }



//layout_dialog_tableName ===================================================================================
    private TextView table_name, table_cancel, table_confirm;
    private EditText table_edit;
    private String tableTitle;
    private String tableEdit;
    private String tableEditHint;
    private int inputType;

    public DialogCustom setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
        table_name.setText(tableTitle);
        return this;
    }

    public String getTableEdit() {
        tableEdit = table_edit.getText().toString();
        return tableEdit;
    }

    public DialogCustom setTableEdit(String nEdit) {
        this.tableEdit = nEdit;
        table_edit.setText(nEdit);
        return this;
    }

    public DialogCustom setTableEditHint(String tableEditHint) {
        this.tableEditHint = tableEditHint;
        table_edit.setHint(tableEditHint);
        return this;
    }

    public DialogCustom setInputType(int inputType) {
        this.inputType = inputType;
        table_edit.setInputType(inputType);
        return this;
    }

    // 《===============================================================================================================》
    public DialogCustom(Context context, int themeResId) {
        super(context, themeResId);
    }

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
        //layout_dialog_menu
        mNo = findViewById(R.id.no);
        mSure = findViewById(R.id.sure);

        //layout_dialog_back
        back_content = findViewById(R.id.back_content);
        mLeave = findViewById(R.id.leave);
        mStay = findViewById(R.id.stay);

        //layout_dialog_credit
        mTitle = findViewById(R.id.title);
        mEdit = findViewById(R.id.edit);
        mClear = findViewById(R.id.credit_clear);
        mCancel = findViewById(R.id.credit_cancel);
        mConfirm = findViewById(R.id.credit_confirm);

        //layout_dialog_add_lesson
        radioGroup = findViewById(R.id.radio_group);
        week_start = findViewById(R.id.week_start);
        week_end = findViewById(R.id.week_end);
        week_day = findViewById(R.id.week_day);
        step_start = findViewById(R.id.step_start);
        step_end = findViewById(R.id.step_end);
        teacher_name = findViewById(R.id.teacher_name);
        class_room = findViewById(R.id.class_room);
        add_cancel = findViewById(R.id.add_cancel);
        add_confirm = findViewById(R.id.add_confirm);

        //layout_dialog_schedule
        schedule_title = findViewById(R.id.schedule_title);
        schedule_edit = findViewById(R.id.schedule_edit);
        schedule_max = findViewById(R.id.schedule_max);
        schedule_cancel = findViewById(R.id.schedule_cancel);
        schedule_confirm = findViewById(R.id.schedule_confirm);

        //layout_dialog_tableName
        table_name = findViewById(R.id.table_name);
        table_edit = findViewById(R.id.table_edit);
        table_cancel = findViewById(R.id.table_cancel);
        table_confirm = findViewById(R.id.table_confirm);

    }

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

//《================给对话框里的按钮设置监听================================================================================================
    /**
     * 菜单的联系按钮对话框
     * layout_dialog_menu
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
     * layout_dialog_back
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
     * 学分、备注对话框
     * layout_dialog_credit
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
     * 课表数据当前周、一天课程节数、学期周数
     * layout_dialog_schedule
     */
    public void setScheduleCancelListener(View.OnClickListener listener){
    schedule_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onClick(view);
        }
    });
    }
    public void setScheduleConfirmListener(View.OnClickListener listener){
    schedule_confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onClick(view);
        }
    });
    }

    /**
     * 课表数据课程名称
     * layout_dialog_tablename
     */
    public void setTableNameCancelListener(View.OnClickListener listener){
        table_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
    public void setTableNameConfirmListener(View.OnClickListener listener){
        table_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    /**
     * 添加时间段
     * layout_dialog_add_lesson
     */
    public void setAddCourseCancelListener(View.OnClickListener listener){
        add_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
    public void setAddCourseConfirmListener(View.OnClickListener listener){
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }

    /**
     * 修改周次
     * dialog_update_week
     */
    public void setUpdateWeekCancelListener(View.OnClickListener listener){
        add_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
    public void setUpdateWeekConfirmListener(View.OnClickListener listener){
        add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(view);
            }
        });
    }
}
