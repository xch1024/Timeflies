package com.example.timeflies.activity;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.View.TimeTableView;
import com.example.timeflies.adapter.ScheduleAdapter;
import com.example.timeflies.adapter.TableNameAdapter;
import com.example.timeflies.model.ConfigData;
import com.example.timeflies.model.CourseData;
import com.example.timeflies.model.TimeData;
import com.example.timeflies.operater.ScheduleSupport;
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

    private TextView tv_curWeek;
    private TextView tv_curTime;
    private TextView mon,tues,wed,thur,fri,sat,sun;
    private final SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy/M/d");
    private final SimpleDateFormat sdfWeek = new SimpleDateFormat("E");
    private final Date date = new Date();

    private SqHelper sqHelper;
    private final ScheduleSqlite sqlite = new ScheduleSqlite(this);

    //时间表
    private List<TimeData> timeDataList = new ArrayList<>();
    private RecyclerView rvSchedule;

    //切换课表
    private TableNameAdapter tableNameAdapter;
    private View contentView;
    private List<ConfigData> configDataList = new ArrayList<>();
    private RecyclerView rvTableName;

    //课程表
    private List<CourseData> courses = new ArrayList<>();
    private TimeTableView timeTable;
    private View bg_none;

    //sp存储数据
    private SharedPreferences sp;
    private String className;//课表名称
    private String timeId;//当前时间表id
    private long termStart = new Date().getTime();//学期开始日期
    private String curWeek;//当前周
    private int secTime;//一天课程节数
    private String termWeeks;//学期周数
    private String termId;//当前学期id

    //获取页面左右滑动
    private int currentX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: begin");
        //数据库配置
        SQLiteStudioService.instance().start(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        initView();
        setBar_color();

    }

    /**
     * Android 返回上一个界面并刷新数据的方法 https://blog.csdn.net/weixin_44177244/article/details/108861732
     * Android的任务和回退栈   https://blog.csdn.net/lcj_zhengjiubukaixin/article/details/51705690
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        //查询作息时间表，并展示指定条数
        timeDataList = sqHelper.queryTime(timeId);

        setWeekBold();
        initView();

        //设置最大节次=最大周次
        timeTable.setMaxSection(secTime);
        timeTable.setMaxTerm(Integer.parseInt(termWeeks));

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
        Log.d(TAG, "onResume: ");

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
     * 初始化控件
     * https://blog.csdn.net/qq_28484355/article/details/50804711
     * https://www.cnblogs.com/homg/p/3345012.html
     */
    public void initView(){
        sqHelper = new SqHelper(this);

        //日期-当前周-今天周几
        //周标题栏
        TextView tv_Date = findViewById(R.id.tv_Date);
        tv_curWeek = findViewById(R.id.tv_curWeek);
        tv_curTime = findViewById(R.id.tv_curTime);

        //时间表
        rvSchedule = findViewById(R.id.rv_schedule);

        //课表数据
        timeTable = findViewById(R.id.timeTable);

        //切换课表
        contentView = getLayoutInflater().inflate(R.layout.pop_window, null);
        rvTableName = contentView.findViewById(R.id.pop_rv_table_name);

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
        timeId = sp.getString("timeId","1");
        termStart = sp.getLong("termStart", new Date().getTime());
        curWeek = sp.getString("curWeek", "1");
        secTime = sp.getInt("secTime", 10);
        termWeeks = sp.getString("termWeeks","20");
        termId = sp.getString("termId","1");
//        Log.d(TAG, "sp.getString(\"termId\",\"1\");: "+termId);
    }

    /**
     * 初始化recycleView,展示作息时间表
     * @count 一天课程节数
     */
    private void initTime(int count, String timeId){
        String id = sp.getString("timeId","1");
//        Log.d(TAG, "initTime: "+id);
        timeDataList.clear();
        TimeData timeData = sqHelper.queryTimeData(id);
        timeDataList = timeData.toDetail();
//        list = timeData.();
//        Log.d(TAG, "initTime: "+timeId);

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

    private TableNameAdapter.OnItemClickListener onItemClickListener;

    /**
     * 初始化切换课程表
     */
    private void initTableName(String termId){
        configDataList.clear();
        String id = sp.getString("termId","1");
        configDataList = sqHelper.queryConfig(Integer.parseInt(id));
        for(int i =0; i<configDataList.size();i++){
            Log.d(TAG, "initTableName: "+configDataList.get(i).getClassName());
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rvTableName.setLayoutManager(manager);
        tableNameAdapter = new TableNameAdapter(configDataList,MainActivity.this);
        rvTableName.setAdapter(tableNameAdapter);
        tableNameAdapter.setOnItemClickListener(new TableNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(!configDataList.get(position).isChecked()){
                    for(int i=0; i<configDataList.size(); i++){
                        if(i == position){
                            configDataList.get(i).setChecked(true);
                        }else{
                            configDataList.get(i).setChecked(false);
                        }
                    }
                    tableNameAdapter.notifyDataSetChanged();
                    ToastCustom.showMsgWarning(MainActivity.this, "点击课表"+configDataList.get(position).getClassName());
                }


            }

            @Override
            public void onItemLongClick(View view, int position) {
                if(!configDataList.get(position).isChecked()){
                    ToastCustom.showMsgWarning(MainActivity.this, "长按"+configDataList.get(position).getClassName());
                }
            }
        });
    }

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
            courses = sqlite.listAll(termId);
        }
        return courses;
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
     * popWindow弹窗
     * https://www.jianshu.com/p/e331ffd2452f
     */
    private void showPopWindow(){

        //seekBar  设置不可见
        SeekBar seekBar = contentView.findViewById(R.id.seekBar);
        seekBar.setVisibility(View.GONE);
        initTableName("1");
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
            case R.id.menu_added:
                String term_id = sp.getString("termId","1");
                Intent intent = new Intent(MainActivity.this, MenuAdded.class);
                intent.putExtra("termId",term_id);
                startActivity(intent);
                break;
            case R.id.bt_ellipsis:
                showPopWindow();
                break;
            case R.id.update_week:
                intentActivity(ScheduleData.class);
                break;
            case R.id.add_table:
                btnInsertTable();
                break;
            case R.id.manage:
                intentActivity(ManyTable.class);
                break;
            case R.id.menu_clock:
                intentActivity(MenuClock.class);
                break;
            case R.id.menu_setting:
                intentActivity(MenuSetting.class);
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

    //新建课表
    private void btnInsertTable() {
        dialog = new DialogCustom(MainActivity.this, R.layout.dialog_tablename,0.8);
        dialog.setTableTitle("新建课表");
        dialog.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long insert = btnInsert();
                if(insert > 0 ){
                    ToastCustom.showMsgTrue(MainActivity.this, "新建成功~");
                    initTableName("1");
                }else{
                    ToastCustom.showMsgFalse(MainActivity.this, "新建失败~");
                }
                dialog.dismiss();
            }

            private long btnInsert() {
                String tableName = dialog.getTableEdit();
                String timeId = sp.getString("timeId","1");
                long termStart = sp.getLong("termStart",new Date().getTime());
                String curWeek = sp.getString("curWeek","1");
                int secTime = sp.getInt("secTime",10);
                String termWeeks = sp.getString("termWeeks","20");
                ContentValues values = new ContentValues();
                values.put("className", tableName);
                values.put("timeId", timeId);
                values.put("termStart", termStart);
                values.put("curWeek", curWeek);
                values.put("secTime", secTime);
                values.put("termWeeks", termWeeks);
                long insert = sqHelper.insertConfig(null, values);
//                Log.d(TAG, "tableName: "+tableName);
//                Log.d(TAG, "timeId: "+timeId);
//                Log.d(TAG, "termStart: "+ ScheduleSupport.longToDate(termStart));
//                Log.d(TAG, "curWeek: "+curWeek);
//                Log.d(TAG, "secTime: "+secTime);
//                Log.d(TAG, "termWeeks: "+termWeeks);
                return insert;
            }
        });
        dialog.show();
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