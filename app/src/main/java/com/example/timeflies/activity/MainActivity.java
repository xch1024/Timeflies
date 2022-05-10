package com.example.timeflies.activity;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeflies.R;
import com.example.timeflies.View.TimeTableView;
import com.example.timeflies.adapter.ScheduleAdapter;
import com.example.timeflies.adapter.ViewPagerAdapter;
import com.example.timeflies.model.CourseData;
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

    private String TAG = "xch";

    private DialogCustom dialog;
    private TextView tv_Date, tv_curWeek, tv_curTime;
    private long weekNum = 1;
    private long date;

    private List<TimeTableData> list = new ArrayList<>();
    private SqHelper sqHelper;
    private int num;
    private RecyclerView rvSchedule;

    private List<CourseData> courses = new ArrayList<>();
    private ScheduleSqlite sqlite = new ScheduleSqlite(this);
    public TimeTableView timeTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //数据库配置
        SQLiteStudioService.instance().start(this);


        initView();
        setBar_color();
        //查询作息时间表，并展示指定条数
        list = sqHelper.queryDb();

    }

    /**
     * Android 返回上一个界面并刷新数据的方法 https://blog.csdn.net/weixin_44177244/article/details/108861732
     * Android的任务和回退栈   https://blog.csdn.net/lcj_zhengjiubukaixin/article/details/51705690
     */
    @Override
    protected void onStart() {
        super.onStart();

        get_time();
        initView();
        initTime(num);
        //获取开学时间
        // 第四周 1649779200905
        // 第三周 1650384000522
        date = Long.parseLong("1649779200905");

        timeTable.loadData(acquireData(), new Date(date));

        //获取开学日期-现在第几周
        weekNum = timeTable.calcWeek(new Date(date));
        tv_curWeek.setText("第"+weekNum+"周");
    }


    private int currentX;

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
                        timeTable.toggleWeek(-1);
                        Log.d(TAG, "前往上一周: 第 "+weekNum+" 周");
                        setTv_curWeek("第"+timeTable.getCurWeek()+"周");
                    }else if(i < -30){
                        timeTable.toggleWeek(1);
                        Log.d(TAG, "前往下一周: 第 "+weekNum+" 周");
                        setTv_curWeek("第"+timeTable.getCurWeek()+"周");
                    }
                    break;
            }
            return true;
        });
    }



    /**
     * 置当前页面是第几周的课
     */
    public void setTv_curWeek(String message){
        tv_curWeek.setText(message);
        if(timeTable.getCurWeek() != weekNum ){
            tv_curTime.setText("非本周");
        }else{
            SimpleDateFormat xq = new SimpleDateFormat("E");
            tv_curTime.setText(xq.format(date));
        }
    }

    private List<CourseData> acquireData(){
        courses = sqlite.listAll();
        return courses;
    }

    /**
     * 获取当前时间(年月日)
     */
    private void get_time(){
        //设置时间 年-月日
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy/M/d");
        Date date = new Date(currentTimeMillis());
        tv_Date.setText(SimpleDateFormat.format(date));
        //设置当天周几
        SimpleDateFormat xq = new SimpleDateFormat("E");
        tv_curTime.setText(xq.format(date));
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
        num = Integer.parseInt(sqHelper.queryConfig("classTotal"));

        //课表数据
        timeTable = findViewById(R.id.timeTable);


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
     * popWindow弹窗
     * https://www.jianshu.com/p/e331ffd2452f
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