package com.example.timeflies.activity;

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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.adapter.CourseAdapter;
import com.example.timeflies.adapter.ScheduleAdapter;
import com.example.timeflies.model.TimeTableData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.sqlite.SqHelper;
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

    private List<TimeTableData> list = new ArrayList<>();
    private SqHelper sqHelper;
    private static int num;


    /**
     * Android 返回上一个界面并刷新数据的方法 https://blog.csdn.net/weixin_44177244/article/details/108861732
     * Android的任务和回退栈   https://blog.csdn.net/lcj_zhengjiubukaixin/article/details/51705690
     */
    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initTime(num);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏字体颜色
        setBar_color();
        //数据库配置
        SQLiteStudioService.instance().start(this);

        //一些控件初始化
        initView();

        //获取当前时间
        get_time();

        //展示课程
        initCourse();

        //查询作息时间表，并展示指定条数
        queryDb(num);
    }


    /**
     * 初始化控件
     * https://blog.csdn.net/qq_28484355/article/details/50804711
     * https://www.cnblogs.com/homg/p/3345012.html
     *
     */
    public void initView(){
        //日期栏的周一到周日
        mon = findViewById(R.id.week_mon);
        tues = findViewById(R.id.week_tues);
        wed = findViewById(R.id.week_wed);
        thur = findViewById(R.id.week_thur);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);
        sun = findViewById(R.id.week_sun);

        //添加和弹出popWindow按钮
        add = findViewById(R.id.bt_add);
        ellipsis = findViewById(R.id.bt_ellipsis);

        //作息表和课程recycleView初始化
        rvSchedule = findViewById(R.id.rv_schedule);
        rvMon = findViewById(R.id.rv_mon);
        rvTues = findViewById(R.id.rv_tues);
        rvWed = findViewById(R.id.rv_wed);
        rvThur = findViewById(R.id.rv_thur);
        rvFri = findViewById(R.id.rv_fri);
        rvSat = findViewById(R.id.rv_sat);
        rvSun = findViewById(R.id.rv_sun);

        //popWindow布局的控件
        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_popwindow, null);
        update_week = view.findViewById(R.id.update_week);
        add_table = view.findViewById(R.id.add_table);
        manage = view.findViewById(R.id.manage);
        menu_clock = view.findViewById(R.id.menu_clock);
        menu_setting = view.findViewById(R.id.menu_setting);
        menu_added = view.findViewById(R.id.menu_added);
        menu_help = view.findViewById(R.id.menu_help);
        menu_about = view.findViewById(R.id.menu_about);
        menu_connect = view.findViewById(R.id.menu_connect);
        menu_global_set = view.findViewById(R.id.menu_global_set);
        menu_alarm = view.findViewById(R.id.menu_alarm);

        sqHelper = new SqHelper(this);
        num = Integer.parseInt(sqHelper.queryConfig("classTotal"));

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
     * 初始化recycleView,展示作息时间表
     * @count 一天课程节数
     *
     */
    private void initTime(int count){
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rvSchedule.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(list, this);
        //设置item显示的数量
        scheduleAdapter.setItemTotal(count);
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
     * 初始化周一到周日的所有课程
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
        //设置时间
        TextView time = findViewById(R.id.tv_sj);
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/M/d");
        Date date = new Date(currentTimeMillis());
        time.setText(SimpleDateFormat.format(date));
        //设置当前周数
        TextView week = findViewById(R.id.tv_week);
        SimpleDateFormat xq = new SimpleDateFormat("E");
        week.setText(xq.format(date));
//        setWeekBold();
    }


    /**
     * 设置字体颜色 文字加粗，设置颜色为黑色
     * @param textView
     */
    public void initStyle(TextView textView){
        textView.setTextColor(Color.parseColor("#FF000000"));
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    /**
     * 设置当日颜色为粗体 周一到周日
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

    /**
     * 主页面、popWindow上的一些按钮监听
     * 布局文件onClick属性点击事件
     * https://www.cnblogs.com/princenwj/p/5967336.html
     *
     * @param view
     */
    public void homePage(View view) {
        switch (view.getId()){
            case R.id.bt_add:
                intentActivity(CourseActivity.class);
                break;
            case R.id.bt_ellipsis:
                showPopWindow();
                break;
            case R.id.update_week:
                intentActivity(ScheduleData.class);
                ScheduleData scheduleData = new ScheduleData();
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


    /**
     * 联系按钮
     *
     */
    private void BtnContact(){
        dialog = new DialogCustom(MainActivity.this, R.layout.layout_dialog_menu, 0.8);
        dialog.setMenuConfirmListener(view -> {
            dialog.dismiss();
            ToastCustom.showMsgTrue(getApplicationContext(), "真的要反馈啦");
        });
        dialog.setMenuCancelListener(view -> {
            ToastCustom.showMsgTrue(getApplicationContext(), "我再摸索一下");
            dialog.dismiss();
        });
        dialog.show();
    }

    /**
     * 查询数据库作息时间表的所有内容
     * @count 展示指定的item个数
     *
     */
    public void queryDb(int count) {
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
                TimeTableData s = new TimeTableData(_id, startTime, endTime);
                list.add(s);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
            //规范：必须关闭数据库
            db.close();

            //重新加载recycle
            initTime(count);
        }
    }

}