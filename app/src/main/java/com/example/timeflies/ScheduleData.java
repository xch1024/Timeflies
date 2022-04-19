package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

public class ScheduleData extends AppCompatActivity implements View.OnClickListener {

    private DialogCustom dialogCustomName, dialogCustomWeek, dialogCustomClass, dialogCustomTerm;
    private TextView tvTitle;
    private ImageView ivDonate, ivBack;
    private View table_name, class_time, current_week, class_total, term_total, added;
    private TextView name, week_num, class_num, term_num;

    private static String className, termStart, currentWeek, classTotal, termTotal;
    private SqHelper sqHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_data);

        configInit();
        initView();
        setListener();
    }

    /**
     * 设置配置信息存储到sp
     */
    private void configInit(){
        sqHelper = new SqHelper(this);
        className = sqHelper.queryConfig("className");
        termStart = sqHelper.queryConfig("termStart");
        currentWeek = sqHelper.queryConfig("currentWeek");
        classTotal = sqHelper.queryConfig("classTotal");
        termTotal = sqHelper.queryConfig("termTotal");
    }



    /**
     * 初始控件
     * https://blog.csdn.net/qq_20451879/article/details/54745068 Include（复用layout）的使用方式
     *
     */
    private void initView(){

        tvTitle = findViewById(R.id.tvTitle);
        ivDonate = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        tvTitle.setText(R.string.ScheduleData);
        ivDonate.setImageResource(R.drawable.donation);

        name = findViewById(R.id.name);
        current_week = findViewById(R.id.current_week);
        class_total = findViewById(R.id.class_total);
        term_total = findViewById(R.id.term_total);

        week_num = findViewById(R.id.week_num);
        class_num = findViewById(R.id.class_num);
        term_num = findViewById(R.id.term_num);

        name.setText(className);
        week_num.setText(currentWeek);
        class_num.setText(classTotal);
        term_num.setText(termTotal);

        added = findViewById(R.id.added);
        class_time = findViewById(R.id.class_time);
        table_name = findViewById(R.id.table_name);

    }

    /**
     * 设置监听
     */
    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);

        current_week.setOnClickListener(this);
        class_total.setOnClickListener(this);
        term_total.setOnClickListener(this);

        added.setOnClickListener(this);
        class_time.setOnClickListener(this);
        table_name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.table_name:
                BtnTable();
                break;
            case R.id.class_time:
                intentActivity(MenuClock.class);
                break;
            case R.id.ivBack:
                intentActivity(MenuSetting.class);
                break;
            case R.id.ivSave:
                ToastCustom.showMsgTrue(this, "捐赠");
                break;
            case R.id.current_week:
                BtnWeek();
                break;
            case R.id.class_total:
                BtnClass();
                break;
            case R.id.term_total:
                BtnTerm();
                break;
            case R.id.added:
                intentActivity(MenuAdded.class);
                break;
        }
    }


    /**
     * 页面跳转
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(ScheduleData.this, cls);
        startActivity(intent);
    }

    /**
     * 课表名称按钮
     */
    private void BtnTable() {
        String sname = sqHelper.queryConfig("className");
        dialogCustomName = new DialogCustom(this, R.layout.layout_dialog_tablename, 0.7);
        //设置文本的初始文字
        dialogCustomName.setnEdit(sname);
        dialogCustomName.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustomName.dismiss();
            }
        });
        dialogCustomName.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String names = dialogCustomName.getnEdit();
                if(TextUtils.isEmpty(names)){
                    ToastCustom.showMsgFalse(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else{
                    name.setText(names);
                    //同步数据库
                    sqHelper.updateConfig("className", names);
                    dialogCustomName.dismiss();
                }
            }
        });
        dialogCustomName.show();
    }

    /**
     * 当前周按钮
     */
    private void BtnWeek(){
        String current = sqHelper.queryConfig("currentWeek");
        dialogCustomWeek = new DialogCustom(this, R.layout.layout_dialog_schedule, 0.7);
        dialogCustomWeek.setScheduleTitle("当前周");
        //设置文本的初始文字
        dialogCustomWeek.setScheduleMax(term_num.getText().toString());
        dialogCustomWeek.setScheduleEdit(current);
        dialogCustomWeek.setScheduleCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustomWeek.dismiss();
            }
        });
        dialogCustomWeek.setScheduleConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String week = dialogCustomWeek.getScheduleEdit();
                if(TextUtils.isEmpty(week)){
                    ToastCustom.showMsgFalse(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(week) > Integer.parseInt(termTotal) ||Integer.parseInt(week) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    week_num.setText(week);
                    //同步数据库
                    sqHelper.updateConfig("currentWeek", week);
                    dialogCustomWeek.dismiss();
                }
            }
        });
        dialogCustomWeek.show();
    }

    /**
     * 一天课程节数按钮
     */
    private void BtnClass(){
        String cTotal = sqHelper.queryConfig("classTotal");
        dialogCustomClass = new DialogCustom(this, R.layout.layout_dialog_schedule, 0.7);
        dialogCustomClass.setScheduleTitle("一天课程节数");
        //设置文本的初始文字
        dialogCustomClass.setScheduleMax("30");
        dialogCustomClass.setScheduleEdit(cTotal);

        dialogCustomClass.setScheduleCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustomClass.dismiss();
            }
        });
        dialogCustomClass.setScheduleConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String  number= dialogCustomClass.getScheduleEdit();
                if(TextUtils.isEmpty(number)){
                    ToastCustom.showMsgFalse(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(number) > 30 || Integer.parseInt(number) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    class_num.setText(number);
                    //同步数据库
                    sqHelper.updateConfig("classTotal", number);
                    //todo 设置一天的课程节数后 主页应该相应的刷新
                    dialogCustomClass.dismiss();
                }
            }
        });
        dialogCustomClass.show();
    }

    /**
     * 学期周数按钮
     */
    private void BtnTerm(){
        String tTotal = sqHelper.queryConfig("termTotal");
        dialogCustomTerm = new DialogCustom(this, R.layout.layout_dialog_schedule, 0.7);
        dialogCustomTerm.setScheduleTitle("学期周数").setScheduleMax("60");
        //设置文本的初始文字
        dialogCustomTerm.setScheduleEdit(tTotal);
        dialogCustomTerm.setScheduleCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustomTerm.dismiss();
            }
        });
        dialogCustomTerm.setScheduleConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String term = dialogCustomTerm.getScheduleEdit();
                if(TextUtils.isEmpty(term)){
                    ToastCustom.showMsgFalse(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(term) > 60 || Integer.parseInt(term) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    term_num.setText(term);
                    //同步到数据库
                    sqHelper.updateConfig("termTotal", term);
                    dialogCustomTerm.dismiss();
                }
            }
        });
        dialogCustomTerm.show();
    }
}