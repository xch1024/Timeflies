package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ScheduleSupport;
import com.example.timeflies.utils.ToastCustom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleData extends AppCompatActivity implements View.OnClickListener {

    private DialogCustom dialogCustomName, dialogCustomWeek, dialogCustomClass, dialogCustomTerm;
    private TextView tvTitle;
    private ImageView ivDonate, ivBack;

    private View term_time,table_name, class_time, current_week, class_total, term_total, added;
    private TextView name, term_date, week_num, class_num, term_num;

    private static String className, termStart, currentWeek, classTotal, termTotal;
    private SqHelper sqHelper;
    private ScheduleSupport scheduleSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_data);

        configInit();
        initView();

        setListener();
    }

    /**
     * 从数据库查询设置配置信息
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

        term_time = findViewById(R.id.term_time);
        table_name = findViewById(R.id.table_name);
        class_time = findViewById(R.id.class_time);
        current_week = findViewById(R.id.current_week);
        class_total = findViewById(R.id.class_total);
        term_total = findViewById(R.id.term_total);
        added = findViewById(R.id.added);

        name = findViewById(R.id.name);
        term_date = findViewById(R.id.term_date);
        week_num = findViewById(R.id.week_num);
        class_num = findViewById(R.id.class_num);
        term_num = findViewById(R.id.term_num);

        name.setText(className);
        term_date.setText(termStart);
        week_num.setText("第 "+ currentWeek+ " 周");
        class_num.setText(classTotal);
        term_num.setText(termTotal);
    }

    /**
     * 设置监听
     */
    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);

        table_name.setOnClickListener(this);
        class_time.setOnClickListener(this);
        term_time.setOnClickListener(this);
        current_week.setOnClickListener(this);
        class_total.setOnClickListener(this);
        term_total.setOnClickListener(this);
        added.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MenuSetting.class);
                break;
            case R.id.ivSave:
                ToastCustom.showMsgTrue(this, "捐赠");
                break;
            case R.id.table_name:
                BtnTable();
                break;
            case R.id.class_time:
                intentActivity(MenuClock.class);
                break;
            case R.id.term_time:
                BtnTermStart();
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
        dialogCustomName.setTableEdit(sname);
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
                String names = dialogCustomName.getTableEdit();
                if(TextUtils.isEmpty(names)){
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
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
     * 学期开始日期按钮
     */
    private void BtnTermStart(){

        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat y = new SimpleDateFormat("yyyy");
            SimpleDateFormat m = new SimpleDateFormat("MM");
            SimpleDateFormat d = new SimpleDateFormat("dd");

            Date dt= format.parse(termStart);
            int year  = Integer.parseInt(y.format(dt));
            int month  = Integer.parseInt(m.format(dt)) - 1;
            int day  = Integer.parseInt(d.format(dt));

            //日期选择交互器
            DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleData.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    i1 = i1 +1;
                    String dates = i +"-"+ i1 +"-"+ i2;
                    term_date.setText(dates);
                    //同步到数据库
                    sqHelper.updateConfig("termStart", dates);

                    SimpleDateFormat format1 =  new SimpleDateFormat("yyyy-MM-dd");
                    Date d = new Date();
                    Date d1 = null;
                    try{
                        d1 = format1.parse(dates);
                    }catch(ParseException e){

                    }
                    int weeks = ScheduleSupport.getWeek(d, d1);
                    ToastCustom.showMsgWarning(ScheduleData.this,"当天为开学第"+weeks+"周");
                    week_num.setText("第"+weeks+"周");
                    sqHelper.updateConfig("currentWeek", String.valueOf(weeks));

                    configInit();
                    initView();
                }
            },year, month, day);
            datePickerDialog.show();

        }
        catch (ParseException e){        }

    }

    /**
     * 当前周按钮
     */
    public void BtnWeek(){
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
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(week) > Integer.parseInt(termTotal) ||Integer.parseInt(week) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    week_num.setText("第 "+week+" 周");
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
        dialogCustomClass.setScheduleMax("20");
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
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(number) > 20 || Integer.parseInt(number) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    class_num.setText(number);
                    //同步数据库
                    sqHelper.updateConfig("classTotal", number);
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
        dialogCustomTerm.setScheduleTitle("学期周数").setScheduleMax("30");
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
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(term) > 30 || Integer.parseInt(term) <= 0){
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