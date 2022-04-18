package com.example.timeflies;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timeflies.adapter.ClockManageAdapter;
import com.example.timeflies.adapter.CourseAdapter;
import com.example.timeflies.adapter.ScheduleAdapter;
import com.example.timeflies.model.ScheduleData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity{

    private View view, menu_clock, menu_setting, menu_added, menu_help,
            menu_about, menu_connect, menu_global_set, menu_alarm;
    private TextView update_week, add_table, manage;
    private ImageView add, ellipsis;
    private TextView mon,tues,wed,thur,fri,sat,sun;
    private RecyclerView rvSchedule, rvMon, rvTues, rvWed, rvThur, rvFri, rvSat, rvSun;
    private DialogCustom dialog;

    private List<ScheduleData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SQLiteStudioService.instance().start(this);


        initView();

        //设置状态栏字体颜色
        setBar_color();

        //获取当前时间
        get_time();

        //展示课程
        initCourse();
        //展示作息时间
        queryDb();
    }


    /**
     * 初始化控件
     * https://blog.csdn.net/qq_28484355/article/details/50804711
     * https://www.cnblogs.com/homg/p/3345012.html
     *
     */
    private void initView(){
        mon = findViewById(R.id.week_mon);
        tues = findViewById(R.id.week_tues);
        wed = findViewById(R.id.week_wed);
        thur = findViewById(R.id.week_thur);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);
        sun = findViewById(R.id.week_sun);
        add = findViewById(R.id.bt_add);
        ellipsis = findViewById(R.id.bt_ellipsis);

        rvSchedule = findViewById(R.id.rv_schedule);
        rvMon = findViewById(R.id.rv_mon);
        rvTues = findViewById(R.id.rv_tues);
        rvWed = findViewById(R.id.rv_wed);
        rvThur = findViewById(R.id.rv_thur);
        rvFri = findViewById(R.id.rv_fri);
        rvSat = findViewById(R.id.rv_sat);
        rvSun = findViewById(R.id.rv_sun);

        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_popwindow, null);
        menu_clock = view.findViewById(R.id.menu_clock);
        menu_setting = view.findViewById(R.id.menu_setting);
        menu_added = view.findViewById(R.id.menu_added);
        menu_help = view.findViewById(R.id.menu_help);
        menu_about = view.findViewById(R.id.menu_about);
        menu_connect = view.findViewById(R.id.menu_connect);
        menu_global_set = view.findViewById(R.id.menu_global_set);
        menu_alarm = view.findViewById(R.id.menu_alarm);
        update_week = view.findViewById(R.id.update_week);
        add_table = view.findViewById(R.id.add_table);
        manage = view.findViewById(R.id.manage);
    }

    /**
     * popWindow弹窗
     * https://www.jianshu.com/p/e331ffd2452f
     *
     */
    private void showPopWindow(){
        View contentView = getLayoutInflater().inflate(R.layout.layout_popwindow, null);
        PopupWindow popupWindow = new PopupWindow(contentView,1000,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.dismiss();
        popupWindow.isShowing();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.pop_style);
        popupWindow.showAtLocation(MainActivity.this.findViewById(R.id.bt_ellipsis), Gravity.BOTTOM|Gravity.CENTER_VERTICAL,0,0);
    }


    /**
     * 设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色
     * https://blog.csdn.net/brian512/article/details/52096445  Android实现沉浸式状态栏的那些坑
     *
     */
    private void setBar_color(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 初始化recycleview,展示作息时间表
     *
     *
     */
    private void initTime(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rvSchedule.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(list, this);
        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化每一列课程recycleView
     *
     * @param recyclerView
     */
    private void initRecycle(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        CourseAdapter courseAdapter = new CourseAdapter();
        recyclerView.setAdapter((courseAdapter));
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化所有课程
     *
     */
    private void initCourse(){
        initRecycle(rvMon);
        initRecycle(rvTues);
        initRecycle(rvWed);
        initRecycle(rvThur);
        initRecycle(rvFri);
        initRecycle(rvSat);
        initRecycle(rvSun);
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
     * 设置字体颜色
     * @param textView
     */
    public void initStyle(TextView textView){
        textView.setTextColor(Color.parseColor("#FF000000"));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    /**
     * 设置当日颜色为粗体
     *
     */
    private void setWeekBold(){
        Date date = new Date(currentTimeMillis());
        SimpleDateFormat xq = new SimpleDateFormat("E");
        if(xq.format(date).equals("周一")){
            initStyle(mon);
        }else if(xq.format(date).equals("周二")){
            initStyle(tues);
        }
        else if(xq.format(date).equals("周三")){
            initStyle(wed);
        }
        else if(xq.format(date).equals("周四")){
            initStyle(thur);
        }
        else if(xq.format(date).equals("周五")){
            initStyle(fri);
        }
        else if(xq.format(date).equals("周六")){
            initStyle(sat);
        }else{
            initStyle(sun);
        }
    }

    /**
     * 页面跳转
     *
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    /**布局文件onclick
     * https://www.cnblogs.com/princenwj/p/5967336.html
     *
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_add:
                intentActivity(AddCourse.class);
                break;
            case R.id.bt_ellipsis:
                showPopWindow();
                break;
            case R.id.update_week:
                ToastCustom.showMsgTrue(this,"修改当前周按钮");
                break;
            case R.id.add_table:
                ToastCustom.showMsgTrue(this,"新建课表按钮");
                break;
            case R.id.manage:
                ToastCustom.showMsgTrue(this,"管理按钮");
                break;
            case R.id.menu_clock:
                intentActivity(MenuClock.class);
                break;
            case R.id.menu_setting:
                intentActivity(MenuSetting.class);
                break;
            case R.id.menu_added:
                intentActivity(MenuAdded.class);
                break;
            case R.id.menu_help:
                ToastCustom.showMsgTrue(this,"常见问题按钮");
                break;
            case R.id.menu_about:
                intentActivity(MenuAbout.class);
                break;
            case R.id.menu_connect:
                BtnContact();
                break;
            case R.id.menu_global_set:
                intentActivity(MenuGlobalSet.class);
                break;
            case R.id.menu_alarm:
                intentActivity(MenuAlarm.class);
                break;
        }

    }


    private void BtnContact(){
        dialog = new DialogCustom(MainActivity.this, R.layout.layout_dialog_menu, 0.8);
        dialog.setMenuConfirmListener(view -> {
            dialog.dismiss();
            ToastCustom.showMsgTrue(getApplicationContext(), "联系按钮的确定反馈");
        });
        dialog.setMenuCancelListener(view -> {
            ToastCustom.showMsgTrue(getApplicationContext(), "备注按钮的取消反馈");
            dialog.dismiss();
        });
        dialog.show();
    }

    //查询数据库内容
    public void queryDb() {

        //清除数据
        list.clear();
        Log.i("xch", "queryDb: 查询");
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //规范：确保数据库打开成功，才能放心操作
        if (db.isOpen()) {
            //返回游标
            Cursor cursor = db.rawQuery("select * from schedules", null);
            while(cursor.moveToNext()){
                int _id = cursor.getInt(0);
                String startTime = cursor.getString(1);
                String endTime = cursor.getString(2);
                ScheduleData s = new ScheduleData(_id, startTime, endTime);
                list.add(s);
                Log.i("xch", "BtnQuery: 主键：" + _id + "\t" + "用户名：" + startTime + "\t" + "内容：" + endTime);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
            //规范：必须关闭数据库
            db.close();

            //重新加载recycle
            initTime();
        }
    }

}