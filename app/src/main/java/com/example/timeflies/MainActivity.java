package com.example.timeflies;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timeflies.adapter.CourseAdapter;
import com.example.timeflies.adapter.ScheduleAdapter;
import com.example.timeflies.utils.ToastCustom;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置状态栏字体颜色
        setBar_color();
        //获取当前时间
        get_time();
        //展示课程
        course_show();
        //展示作息时间
        schedule_show();
    }

    /**
     * 设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色
     *
     */
    public void setBar_color(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 初始化recycleview,展示作息时间表
     *
     *
     */
    public void schedule_show(){
        RecyclerView rv_schedule = findViewById(R.id.rv_schedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rv_schedule.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
        rv_schedule.setAdapter(scheduleAdapter);

        rv_schedule.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化recycleview,展示周一课程
     *
     *
     */
    public void course_show(){
        RecyclerView rv_mon = findViewById(R.id.rv_mon);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        rv_mon.setLayoutManager(linearLayoutManager);

        CourseAdapter courseAdapter = new CourseAdapter();
        rv_mon.setAdapter(courseAdapter);

        rv_mon.setNestedScrollingEnabled(false);
    }

//    添加按钮
    public void add(View view) {
        Intent intent = new Intent(this,AddCourse.class);
        startActivity(intent);
    }

//    设置按钮
    public void ellipsis(View view) {
        showPopupWindow();

    }

    /**
     * popwindow弹窗
     *
     */
    private void showPopupWindow(){
        View pop_view = getLayoutInflater().inflate(R.layout.layout_popwindow,null);
        PopupWindow popupWindow = new PopupWindow(pop_view,1000,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.dismiss();
        popupWindow.isShowing();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.pop_style);
        popupWindow.showAtLocation(MainActivity.this.findViewById(R.id.bt_add), Gravity.BOTTOM|Gravity.CENTER_VERTICAL,0,0);
    }

    /**
     * 获取当前时间(年月日)
     *
     */
    private void get_time(){
//        设置时间
        TextView time = findViewById(R.id.tv_sj);
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/M/d");
        Date date = new Date(currentTimeMillis());
        time.setText(SimpleDateFormat.format(date));
//        设置当前周数
        TextView week = findViewById(R.id.tv_week);
        SimpleDateFormat xq = new SimpleDateFormat("E");
        week.setText(xq.format(date));

        setWeekBold();
//        设置月份
        TextView month = findViewById(R.id.week_month);
        SimpleDateFormat mon = new SimpleDateFormat("M");
        month.setText(mon.format(date)+"\n月");
    }

    /**
     * 设置当日颜色为粗体
     *
     */
    public void setWeekBold(){
        TextView mon,tues,wed,thur,fri,sat,sun;
        mon = findViewById(R.id.week_mon);
        tues = findViewById(R.id.week_tues);
        wed = findViewById(R.id.week_wed);
        thur = findViewById(R.id.week_thur);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);
        sun = findViewById(R.id.week_sun);
        Date date = new Date(currentTimeMillis());
        SimpleDateFormat xq = new SimpleDateFormat("E");
        if(xq.format(date).equals("周一")){
            mon.setTextColor(Color.parseColor("#FF000000"));
            mon.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else if(xq.format(date).equals("周二")){
            tues.setTextColor(Color.parseColor("#FF000000"));
            tues.getPaint().setFakeBoldText(true);
        }
        else if(xq.format(date).equals("周三")){
            wed.setTextColor(Color.parseColor("#FF000000"));
            wed.getPaint().setFakeBoldText(true);
        }
        else if(xq.format(date).equals("周四")){
            thur.setTextColor(Color.parseColor("#FF000000"));
            thur.getPaint().setFakeBoldText(true);

        }
        else if(xq.format(date).equals("周五")){
            fri.setTextColor(Color.parseColor("#FF000000"));
            fri.getPaint().setFakeBoldText(true);
        }
        else if(xq.format(date).equals("周六")){
            sat.setTextColor(Color.parseColor("#FF000000"));
            sat.getPaint().setFakeBoldText(true);
        }else{
            sun.setTextColor(Color.parseColor("#FF000000"));
            sun.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

//    修改当前周按钮
    public void update_week(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"修改当前周按钮");

    }

//    新建课表按钮
    public void add_table(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"新建课表按钮");

    }

//    管理按钮
    public void manage(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"管理按钮");

    }

//    上课时间按钮
    public void menu_clock(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"上课时间按钮");

    }

//    课表设置按钮
    public void menu_setting(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"课表设置按钮");

    }

//    已添课程按钮
    public void menu_added(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"已添课程按钮");

    }

//    常见问题按钮
    public void menu_help(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"常见问题按钮");

    }

//    关于按钮
    public void menu_about(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"关于按钮");

    }

//    联系我们按钮
    public void menu_connect(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"联系我们按钮");

    }

//    全局设置按钮
    public void menu_global_set(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"全局设置按钮");

    }

//    课程时钟按钮
    public void menu_alarm(View view) {
        ToastCustom.showMsgFalse(MainActivity.this,"课程时钟按钮");

    }

}