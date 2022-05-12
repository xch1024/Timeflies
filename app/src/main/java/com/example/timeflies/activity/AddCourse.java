package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.model.CourseData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.ColorPickerDialog;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.Locale;

public class AddCourse extends AppCompatActivity implements View.OnClickListener{

    private View view_Name, view_Color, View_Credit, View_Remark;
    private TextView course_color, credit, course_cancel, course_confirm;
    private EditText course_name, course_credit, course_remark;
    private DialogCustom dialogCustom;
    private ScheduleSqlite sqlite = new ScheduleSqlite(AddCourse.this);

    private int mColor;
    private boolean mHexValueEnable = true;
    private ImageView colorMap;
    private SharedPreferences sp;
    private String termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //接收传过来的当前学期id
        Intent intent = getIntent();
        termId = intent.getStringExtra("termId");
        sp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
        setListener();
    }

    //初始化控件
    private void initView(){
        view_Name = findViewById(R.id.view_Name);
        view_Color = findViewById(R.id.view_Color);
        View_Credit = findViewById(R.id.View_Credit);
        View_Remark = findViewById(R.id.View_Remark);
        course_name = findViewById(R.id.course_name);
        course_color = findViewById(R.id.course_color);
        colorMap = findViewById(R.id.colorMap);
        colorMap.setColorFilter(getResources().getColor(R.color.text_normal));
        course_color.setTextColor(getResources().getColor(R.color.text_normal));
        course_credit = findViewById(R.id.course_credit);
        credit = findViewById(R.id.credit);
        course_remark = findViewById(R.id.course_remark);
        course_cancel = findViewById(R.id.course_cancel);
        course_confirm = findViewById(R.id.course_confirm);

        //不可粘贴，长按不会弹出粘贴框
        course_name.setKeyListener(null);
        course_credit.setKeyListener(null);
        course_remark.setKeyListener(null);
    }

    private void setListener(){
        view_Name.setOnClickListener(this);
        view_Color.setOnClickListener(this);
        View_Credit.setOnClickListener(this);
        View_Remark.setOnClickListener(this);
        course_cancel.setOnClickListener(this);
        course_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_Name:
                viewName();
                break;
            case R.id.view_Color:
                viewColor();
                break;
            case R.id.View_Credit:
                viewCredit();
                break;
            case R.id.View_Remark:
                viewRemark();
                break;
            case R.id.course_cancel:
                this.finish();
                break;
            case R.id.course_confirm:
                tvSave();
                break;
        }
    }

    //课程名称
    private void viewName(){
        dialogCustom = new DialogCustom(AddCourse.this,R.layout.dialog_tablename, 0.8);
        if(!TextUtils.isEmpty(course_name.getText().toString())){
            dialogCustom.setTableEdit(course_name.getText().toString());
        }
        dialogCustom.setTableTitle("课程名称");
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseName = dialogCustom.getTableEdit();
                if(!TextUtils.isEmpty(courseName)){
                    course_name.setText(courseName);
                    dialogCustom.dismiss();
                }else{
                    ToastCustom.showMsgWarning(AddCourse.this,"课表名称不能为空哦");
                }

            }
        });
        dialogCustom.show();
    }

    //更改颜色
    private void viewColor(){
        new ColorPickerDialog.Builder(AddCourse.this, mColor)
                .setHexValueEnabled(mHexValueEnable)
                .setOnColorPickedListener(mListener)
                .build()
                .show();
        ToastCustom.showMsgWarning(AddCourse.this, "课程颜色");
    }

    private ColorPickerDialog.OnColorPickedListener mListener = new ColorPickerDialog.OnColorPickedListener() {
        @Override
        public void onColorPicked(int color) {
            mColor = color;
            colorMap.setColorFilter(mColor);
            course_color.setTextColor(mColor);
            course_color.setText("#"+com.example.timeflies.operater.ColorUtils.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
    };

    //更改学分
    private void viewCredit(){
        dialogCustom = new DialogCustom(this, R.layout.dialog_tablename, 0.8);
        if(!TextUtils.isEmpty(course_credit.getText().toString())){
            dialogCustom.setTableEdit(course_credit.getText().toString());
        }
        dialogCustom.setTableTitle("学分").setTableEdit("0.0").setInputType(InputType.TYPE_CLASS_PHONE);
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCredit = dialogCustom.getTableEdit();
                if(courseCredit.equals("0")|| courseCredit.equals("0.0")){
                }else{
                    course_credit.setText(courseCredit);
                    credit.setVisibility(View.VISIBLE);
                }
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }

    //更改备注
    public void viewRemark(){
        dialogCustom = new DialogCustom(AddCourse.this,R.layout.dialog_tablename, 0.8);
        if(!TextUtils.isEmpty(course_remark.getText())){
            dialogCustom.setTableEdit(course_remark.getText().toString());
        }
        dialogCustom.setTableTitle("备注");
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseRemark = dialogCustom.getTableEdit();
                    course_remark.setText(courseRemark);
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }

    //保存
    private void tvSave(){
        String courseName = course_name.getText().toString();
        if(TextUtils.isEmpty(courseName)){
            ToastCustom.showMsgWarning(AddCourse.this,"课程名称不能为空哦");
        }else{
            addCourse();
            this.finish();
            Intent intent = new Intent(AddCourse.this, MenuAdded.class);
            intent.putExtra("termId",termId);
            startActivity(intent);
        }
    }

    //添加课程到数据库
    private void addCourse() {

        String courseName = course_name.getText().toString();
        String courseColor = course_color.getText().toString();
        String courseCredit = course_credit.getText().toString();
        String courseRemark = course_remark.getText().toString();

        Log.d("xch", "courseTermId: "+ termId);
        CourseData courseData = new CourseData();
        courseData.setCourseName(courseName);
        if(courseColor.equals("点击更改颜色")){
            courseData.setCourseColor("#919191");
        }else{
            courseData.setCourseColor(courseColor);
        }
        courseData.setCourseCredit(courseCredit);
        courseData.setCourseRemark(courseRemark);
        courseData.setTermId(termId);
        long insert = sqlite.insert(courseData);
        if (insert != -1) {
            ToastCustom.showMsgTrue(AddCourse.this, "添加课程成功");
        }else{
            ToastCustom.showMsgFalse(AddCourse.this, "添加失败");
        }
    }

    /**
     * 页面跳转
     *
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(AddCourse.this, cls);
        startActivity(intent);
    }

}