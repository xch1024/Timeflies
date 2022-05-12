package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.operater.ScheduleSupport;
import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.Calendar;
import java.util.Date;

public class ScheduleData extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "xch";
    private DialogCustom dialogCustom;
    private TextView tvTitle;
    private ImageView ivDonate, ivBack;

    private View view_course_name,view_course_time, view_term_time, view_cur_week, view_sec_time, view_term_weeks, view_menu_added;
    private TextView class_name, term_Start, cur_week, sec_time, term_weeks;

    //更新数据库
    private SqHelper sqHelper = new SqHelper(ScheduleData.this);
    //sp存储数据
    private SharedPreferences sp;
    private String className = "默认";//课表名称
    private String timeId = "1";//当前时间表id
    private long termStart = new Date().getTime();//学期开始日期
    private String curWeek = "1";//当前周
    private int secTime = 10;//一天课程节数
    private String termWeeks = "20";//学期周数
    private String termId = "1";//当前学期id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_data);

        sp = getSharedPreferences("config",MODE_PRIVATE);
        configInit();
        initView();
        setListener();
    }

    /**
     * 从数据库查询设置配置信息
     */
    private void configInit(){
        //从sp获取初始数据
//        Log.d("xch", "sp.getString(\"className\",\"默认\") "+sp.getString("className","默认"));
        className = sp.getString("className","默认");
        timeId = sp.getString("timeId","1");
        termStart = sp.getLong("termStart", termStart);
        curWeek = sp.getString("curWeek", "1");
        secTime = sp.getInt("secTime", 10);
        termWeeks = sp.getString("termWeeks","20");
        termId = sp.getString("termId","1");
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
        ivDonate.setImageResource(R.drawable.photo_donation);

        view_course_name = findViewById(R.id.view_course_name);
        view_course_time = findViewById(R.id.view_course_time);
        view_term_time = findViewById(R.id.view_term_time);
        view_cur_week = findViewById(R.id.view_cur_week);
        view_sec_time = findViewById(R.id.view_sec_time);
        view_term_weeks = findViewById(R.id.view_term_weeks);
        view_menu_added = findViewById(R.id.view_menu_added);

        class_name = findViewById(R.id.class_name);
        term_Start = findViewById(R.id.term_Start);
        cur_week = findViewById(R.id.cur_week);
        sec_time = findViewById(R.id.sec_time);
        term_weeks = findViewById(R.id.term_weeks);

        class_name.setText(className);
        term_Start.setText(ScheduleSupport.longToDate(termStart));
        cur_week.setText("第 "+ curWeek+ " 周");
        sec_time.setText(String.valueOf(secTime));
        term_weeks.setText(termWeeks);
    }

    /**
     * 设置监听
     */
    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);

        view_course_name.setOnClickListener(this);
        view_course_time.setOnClickListener(this);
        view_term_time.setOnClickListener(this);
        view_cur_week.setOnClickListener(this);
        view_sec_time.setOnClickListener(this);
        view_term_weeks.setOnClickListener(this);
        view_menu_added.setOnClickListener(this);
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
            case R.id.view_course_name:
                BtnCourseName();
                break;
            case R.id.view_course_time:
                intentActivity(MenuClock.class);
                break;
            case R.id.view_term_time:
                BtnTermTime();
                break;
            case R.id.view_cur_week:
                BtnCurWeek();
                break;
            case R.id.view_sec_time:
                BtnSecTime();
                break;
            case R.id.view_term_weeks:
                BtnTermWeeks();
                break;
            case R.id.view_menu_added:
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
    private void BtnCourseName() {
        className = sp.getString("className",className);
        dialogCustom = new DialogCustom(this, R.layout.dialog_tablename, 0.8);
        //设置文本的初始文字
        dialogCustom.setTableEdit(className);
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String names = dialogCustom.getTableEdit();
                if(TextUtils.isEmpty(names)){
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else{
                    class_name.setText(names);
                    //同步数据库和sp
                    sp.edit().putString("className",String.valueOf(names)).apply();
                    sqHelper.updateConfig("className", names);
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }

    //todo 日期转换没设置
    /**
     * 学期开始日期按钮
     */
    private void BtnTermTime(){
        long date = sp.getLong("termStart", new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//        Log.d(TAG, "BtnTermTime: year==month==dayOfMonth"+year+"==="+month+"==="+dayOfMonth);
        //日期选择交互器
        DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleData.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dayOfMonth = datePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 0, 0, 0);
                Date time = calendar.getTime();

                String timeNow = ScheduleSupport.longToDate(time.getTime());
                Log.d(TAG, "日期选择交互器选择的时间:==== "+timeNow);
                //计算当前周
                int week = ScheduleSupport.timeTransform(timeNow);
                Log.d(TAG, "计算当前周: " +week);

                //同步数据库和sp
                cur_week.setText("第"+week+"周");
                term_Start.setText(ScheduleSupport.longToDate(time.getTime()));

                sp.edit().putLong("termStart",time.getTime()).apply();
                sp.edit().putString("curWeek",String.valueOf(week)).apply();
                sqHelper.updateConfig("termStart",String.valueOf(time.getTime()));
                sqHelper.updateConfig("curWeek", String.valueOf(week));
            }
        },year, month, dayOfMonth);
        datePickerDialog.show();
    }



    /**
     * 当前周按钮
     */
    public void BtnCurWeek(){
        termWeeks = sp.getString("termWeeks",termWeeks);
        curWeek = sp.getString("curWeek",curWeek);
        dialogCustom = new DialogCustom(this, R.layout.dialog_schedule, 0.8);
        dialogCustom.setScheduleTitle("当前周");
        //设置文本的初始文字
        dialogCustom.setScheduleMax(termWeeks);
        dialogCustom.setScheduleEdit(curWeek);
        dialogCustom.setScheduleCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setScheduleConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String week = dialogCustom.getScheduleEdit();
                if(TextUtils.isEmpty(week)){
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(week) > Integer.parseInt(termWeeks) ||Integer.parseInt(week) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    cur_week.setText("第 "+week+" 周");
                    //同步数据库和sp
                    sp.edit().putString("curWeek",week).apply();
                    sqHelper.updateConfig("curWeek", week);
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }

    /**
     * 一天课程节数按钮
     * secTime int
     */
    private void BtnSecTime(){
        secTime = sp.getInt("secTime",secTime);
//        Log.d(TAG, "sp.getInt(\"secTime\",secTime);: "+secTime);
        dialogCustom = new DialogCustom(this, R.layout.dialog_schedule, 0.8);
        dialogCustom.setScheduleTitle("一天课程节数");
        //设置文本的初始文字
        dialogCustom.setScheduleMax("20");
        dialogCustom.setScheduleEdit(String.valueOf(secTime));

        dialogCustom.setScheduleCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setScheduleConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String  number= dialogCustom.getScheduleEdit();
                if(TextUtils.isEmpty(number)){
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(number) > 20 || Integer.parseInt(number) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    sec_time.setText(number);
                    //同步数据库
//                    Log.d(TAG, "Integer.parseInt(number): "+Integer.parseInt(number));
                    sp.edit().putInt("secTime", Integer.parseInt(number)).apply();
                    sqHelper.updateConfig("secTime", number);
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }

    /**
     * 学期周数按钮
     */
    private void BtnTermWeeks(){
        termWeeks = sp.getString("termWeeks",termWeeks);
        dialogCustom = new DialogCustom(this, R.layout.dialog_schedule, 0.7);
        dialogCustom.setScheduleTitle("学期周数").setScheduleMax("30");
        //设置文本的初始文字
        dialogCustom.setScheduleEdit(termWeeks);
        dialogCustom.setScheduleCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setScheduleConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框文字
                String term = dialogCustom.getScheduleEdit();
                if(TextUtils.isEmpty(term)){
                    ToastCustom.showMsgWarning(ScheduleData.this, "名称不能为空哦ʕ ᵔᴥᵔ ʔ");
                }else if(Integer.parseInt(term) > 30 || Integer.parseInt(term) <= 0){
                    ToastCustom.showMsgFalse(ScheduleData.this, "请注意范围ʕ ᵔᴥᵔ ʔ");
                }else{
                    term_weeks.setText(term);
                    //同步到数据库
                    sp.edit().putString("termWeeks",term).apply();
                    sqHelper.updateConfig("termWeeks", term);

                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }


}