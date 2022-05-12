package com.example.timeflies.activity;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.View.TimeTableView;
import com.example.timeflies.adapter.ScheduleAdapter;
import com.example.timeflies.model.CourseData;
import com.example.timeflies.model.TimeData;
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

    private String TAG = "xch";

    private DialogCustom dialog;

    //周标题栏
    private TextView tv_Date, tv_curWeek, tv_curTime;
    private TextView mon,tues,wed,thur,fri,sat,sun;
    private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy/M/d");
    private SimpleDateFormat sdfWeek = new SimpleDateFormat("E");
    private Date date = new Date();

    //时间表
    private List<TimeData> list = new ArrayList<>();
    private SqHelper sqHelper;
    private RecyclerView rvSchedule;

    //课程表
    private List<CourseData> courses = new ArrayList<>();
    private ScheduleSqlite sqlite = new ScheduleSqlite(this);
    private TimeTableView timeTable;
    private View bg_none;

    //sp存储数据
    private SharedPreferences sp;
    private String className = "默认";//课表名称
    private String timeId = "1";//当前时间表id
    private long termStart = new Date().getTime();//学期开始日期
    private String curWeek = "1";//当前周
    private int secTime = 10;//一天课程节数
    private String termWeeks = "20";//学期周数
    private String termId = "1";//当前学期id

    private List<CourseData> acquireData(){
        //首次使用
        if (sp.getBoolean("isFirstUse", true)) {
            sp.edit().putBoolean("isFirstUse", false).apply();

            sp.edit().putString("className", className).apply();
            sp.edit().putString("timeId", timeId).apply();
            sp.edit().putLong("termStart", termStart).apply();
            sp.edit().putString("curWeek", curWeek).apply();
            sp.edit().putInt("secTime", secTime).apply();
            sp.edit().putString("termWeeks", termWeeks).apply();
            sp.edit().putString("termId", termId).apply();

        }else{
            courses = sqlite.listAll();
        }
        return courses;
    }

    //获取页面左右滑动
    private int currentX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //数据库配置
        SQLiteStudioService.instance().start(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        initView();
        setBar_color();

        //查询作息时间表，并展示指定条数
        list = sqHelper.queryTime(timeId);

    }

    /**
     * Android 返回上一个界面并刷新数据的方法 https://blog.csdn.net/weixin_44177244/article/details/108861732
     * Android的任务和回退栈   https://blog.csdn.net/lcj_zhengjiubukaixin/article/details/51705690
     */
    @Override
    protected void onStart() {
        super.onStart();

        setWeekBold();
        initView();

        timeTable.setMaxSection(secTime);
        int visible = timeTable.loadData(acquireData(), new Date(termStart));
        initTime(secTime, timeId);

        setView(visible);

        //获取开学日期-现在第几周
        tv_curWeek.setText("第"+curWeek+"周");
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onResume() {
        super.onResume();
        timeTable.setOnTouchListener((View view, MotionEvent event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    currentX = (int) event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    int i = (int) event.getX() - currentX;
                    if(i > 30){
                        int key = timeTable.toggleWeek(-1);
                        Log.d(TAG, "前往上一周: 第 "+timeTable.getCurWeek()+" 周");
                        setTv_curWeek("第"+timeTable.getCurWeek()+"周");
                        setView(key);
                    }else if(i < -30){
                        int key = timeTable.toggleWeek(1);
                        Log.d(TAG, "前往下一周: 第 "+timeTable.getCurWeek()+" 周");
                        setTv_curWeek("第"+timeTable.getCurWeek()+"周");
                        setView(key);
                    }
                    break;
            }
            return true;
        });
    }


    /**
     * 设置字体颜色 文字加粗，设置颜色为黑色
     * @param textView
     */
    public void initStyle(TextView textView, Boolean yes){
        if(yes){
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            textView.setTextColor(getResources().getColor(R.color.week_normal));
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    /**
     * 设置当日颜色为粗体 周一到周日
     *
     */
    private void setWeekBold(){
        String week = tv_curTime.getText().toString();
        if(week.equals("周一")){
            initStyle(mon, true);
        }else if(week.equals("周二")){
            initStyle(tues, true);
        }else if(week.equals("周三")){
            initStyle(wed, true);
        }else if(week.equals("周四")){
            initStyle(thur, true);
        }else if(week.equals("周五")){
            initStyle(fri, true);
        }else if(week.equals("周六")){
            initStyle(sat, true);
        }else if(week.equals("周日")){
            initStyle(sun, true);
        }else{
            initStyle(mon, false);
            initStyle(tues, false);
            initStyle(wed, false);
            initStyle(thur, false);
            initStyle(fri, false);
            initStyle(sat, false);
            initStyle(sun, false);
        }
    }


    //本周没有课程视图是否可见
    private void setView(int key){
        if(key < 0){
            bg_none.setVisibility(View.VISIBLE);
        }
        else{
            bg_none.setVisibility(View.GONE);
        }
    }


    /**
     * 设置当前页面是第几周的课
     */
    public void setTv_curWeek(String week){
        tv_curWeek.setText(week);
        Date date = new Date(currentTimeMillis());
        if(String.valueOf(timeTable.getCurWeek()).equals(curWeek)){
//            Log.d(TAG, "setTv_curWeek: =="+ day.format(date));
            tv_curTime.setText(sdfWeek.format(date));
        }else{
            tv_curTime.setText("非本周");
        }
        setWeekBold();
    }


    /**
     * 初始化控件
     * https://blog.csdn.net/qq_28484355/article/details/50804711
     * https://www.cnblogs.com/homg/p/3345012.html
     */
    public void initView(){
        //日期-当前周-今天周几
        tv_Date = findViewById(R.id.tv_Date);
        tv_curWeek = findViewById(R.id.tv_curWeek);
        tv_curTime = findViewById(R.id.tv_curTime);

        //时间表
        rvSchedule = findViewById(R.id.rv_schedule);
        sqHelper = new SqHelper(this);

        //课表数据
        timeTable = findViewById(R.id.timeTable);

        bg_none = findViewById(R.id.bg_none);
        bg_none.setVisibility(View.GONE);

        //设置时间 年-月日
        tv_Date.setText(sdfDay.format(date));
        //设置当天周几
        tv_curTime.setText(sdfWeek.format(date));

        //日期栏的周一到周日
        mon = findViewById(R.id.week_mon);
        tues = findViewById(R.id.week_tues);
        wed = findViewById(R.id.week_wed);
        thur = findViewById(R.id.week_thur);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);
        sun = findViewById(R.id.week_sun);

        //从sp获取初始数据
        className = sp.getString("className","默认");
        timeId = sp.getString("timeId","2");
        termStart = sp.getLong("termStart", new Date().getTime());
        curWeek = sp.getString("curWeek", "1");
        secTime = sp.getInt("secTime", 10);
        termWeeks = sp.getString("termWeeks","20");
        termId = sp.getString("termId","1");
    }

    /**
     * 初始化recycleView,展示作息时间表
     * @count 一天课程节数
     *
     */
    private void initTime(int count, String timeId){
//        Log.d(TAG, "initTime: "+timeId);
        List<TimeData> timeDataList = timeSplit(timeId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rvSchedule.setLayoutManager(layoutManager);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(timeDataList, this);
//        String[] times = new String[listSize];
//        for (int i = 0; i < listSize; i++) times[i] = list.get(i).getTableName();
        //设置item显示的数量
        scheduleAdapter.setItemTotal(count);
        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setNestedScrollingEnabled(false);
    }

    /**
     * 切分时间段
     * @param timeId
     * @return
     */
    private List<TimeData> timeSplit(String timeId){
        list.clear();
        list = sqHelper.queryTime(timeId);
//        Log.d(TAG, "timeSplit: "+list.toString());
        int id = list.get(0).getId();
        String name = list.get(0).getTableName();
        String time = list.get(0).getTableTime();
//        Log.d(TAG, "list.get(0).getTableTime();: "+id+"=============="+time);
        String[] timeArray = time.split(";");
        List<TimeData> timeDataList = new ArrayList<>();
        for (int i = 0; i < timeArray.length; i++){
//            Log.d(TAG, "String[] timeArray: "+timeArray[i]);
            String[] info = timeArray[i].split("-");
            TimeData td = new TimeData();
            td.setId(id);
            td.setTableName(name);
            td.setStartTime(info[0]);
            td.setEndTime(info[1]);
//            Log.d(TAG, "td: "+td);
            timeDataList.add(td);
        }
//        Log.d(TAG, "timeDataList.add(td);: "+timeDataList);
        return timeDataList;
    }

    /**
     * popWindow弹窗
     * https://www.jianshu.com/p/e331ffd2452f
     */
    private void showPopWindow(){
        View contentView = getLayoutInflater().inflate(R.layout.popwindow, null);
        PopupWindow popupWindow = new PopupWindow(contentView,1000,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.dismiss();
        popupWindow.isShowing();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.pop_style);
        popupWindow.showAtLocation(MainActivity.this.findViewById(R.id.bt_ellipsis), Gravity.BOTTOM|Gravity.CENTER_VERTICAL,0,0);
    }

    /**
     * 设置状态栏字体颜色
     * 设置状态栏文字颜色及图标为深色，当状态栏为白色时候，改变其颜色为深色
     * https://blog.csdn.net/brian512/article/details/52096445  Android实现沉浸式状态栏的那些坑
     */
    private void setBar_color(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    /**
     * 主页面、popWindow上的一些按钮监听
     * 布局文件onClick属性点击事件
     * https://www.cnblogs.com/princenwj/p/5967336.html
     * @param view
     */
    public void homePage(View view) {
        switch (view.getId()){
            case R.id.bt_add:
                intentActivity(MenuAdded.class);
                break;
            case R.id.bt_ellipsis:
                showPopWindow();
                break;
            case R.id.update_week:
                intentActivity(ScheduleData.class);
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
     * 页面跳转
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    /**
     * 联系按钮对话框
     */
    private void BtnContact(){
        dialog = new DialogCustom(MainActivity.this, R.layout.dialog_menu, 0.8);
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

}