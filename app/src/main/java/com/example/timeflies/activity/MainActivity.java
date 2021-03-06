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
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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

    //?????????
    private List<TimeData> timeDataList = new ArrayList<>();
    private RecyclerView rvSchedule;

    //????????????
    private LinearLayoutManager manager;
    private PopupWindow popupWindow;
    private TableNameAdapter tableNameAdapter;
    private View contentView;
    private List<ConfigData> configDataList = new ArrayList<>();
    private RecyclerView rvTableName;

    //?????????
    private List<CourseData> courses = new ArrayList<>();
    private TimeTableView timeTable;
    private View bg_none;

    //sp????????????
    private SharedPreferences sp;
    private String className;//????????????
    private String timeId;//???????????????id
    private long termStart = new Date().getTime();//??????????????????
    private String curWeek;//?????????
    private int secTime;//??????????????????
    private String termWeeks;//????????????
    private String termId;//????????????id

    //????????????????????????
    private int currentX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: begin");
        //???????????????
        SQLiteStudioService.instance().start(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);

        initView();
        setBar_color();

    }

    /**
     * Android ????????????????????????????????????????????? https://blog.csdn.net/weixin_44177244/article/details/108861732
     * Android?????????????????????   https://blog.csdn.net/lcj_zhengjiubukaixin/article/details/51705690
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");

        //?????????????????????????????????????????????
        timeDataList = sqHelper.queryTime(timeId);

        initView();
        initTime(secTime, timeId);

        //????????????
        contentView = getLayoutInflater().inflate(R.layout.pop_window, null);
        rvTableName = contentView.findViewById(R.id.pop_rv_table_name);
        popupWindow = new PopupWindow(contentView,1000,ViewGroup.LayoutParams.WRAP_CONTENT);


        //??????????????????????????????
        setWeekBold();
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
                        Log.d(TAG, "???????????????: ??? "+timeTable.getCurWeek()+" ???");
                        setTv_curWeek("???"+timeTable.getCurWeek()+"???");
                        setView(key);
                    }else if(i < -30){
                        int key = timeTable.toggleWeek(1);
                        Log.d(TAG, "???????????????: ??? "+timeTable.getCurWeek()+" ???");
                        setTv_curWeek("???"+timeTable.getCurWeek()+"???");
                        setView(key);
                    }
                    break;
            }
            return true;
        });
    }

    /**
     * ???????????????
     * https://blog.csdn.net/qq_28484355/article/details/50804711
     * https://www.cnblogs.com/homg/p/3345012.html
     */
    public void initView(){
        sqHelper = new SqHelper(this);

        //??????-?????????-????????????
        //????????????
        TextView tv_Date = findViewById(R.id.tv_Date);
        tv_curWeek = findViewById(R.id.tv_curWeek);
        tv_curTime = findViewById(R.id.tv_curTime);

        //?????????
        rvSchedule = findViewById(R.id.rv_schedule);

        //????????????
        timeTable = findViewById(R.id.timeTable);

        bg_none = findViewById(R.id.bg_none);
        bg_none.setVisibility(View.GONE);

        //???????????? ???-??????
        tv_Date.setText(sdfDay.format(date));
        //??????????????????
        tv_curTime.setText(sdfWeek.format(date));

        //???????????????????????????
        mon = findViewById(R.id.week_mon);
        tues = findViewById(R.id.week_tues);
        wed = findViewById(R.id.week_wed);
        thur = findViewById(R.id.week_thur);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);
        sun = findViewById(R.id.week_sun);

        //???sp??????????????????
        termId = sp.getString("termId","1");
        className = sp.getString("className","??????");
        timeId = sp.getString("timeId","1");
        termStart = sp.getLong("termStart", new Date().getTime());
        curWeek = sp.getString("curWeek", "1");
        secTime = sp.getInt("secTime", 10);
        termWeeks = sp.getString("termWeeks","20");

        //??????????????????-???????????????
        tv_curWeek.setText("???"+curWeek+"???");

        //??????????????????=????????????
        timeTable.setMaxSection(secTime);
        timeTable.setMaxTerm(Integer.parseInt(termWeeks));

        //??????????????????????????????
        int visible = timeTable.loadData(acquireData(), new Date(termStart));
        setView(visible);
    }

    /**
     * ?????????recycleView,?????????????????????
     * @count ??????????????????
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
        //??????item???????????????
        scheduleAdapter.setItemTotal(count);
        rvSchedule.setAdapter(scheduleAdapter);
        rvSchedule.setNestedScrollingEnabled(false);
    }


    /**
     * ????????????????????????
     */
    public void initTableName(){
        configDataList.clear();
        String termId = sp.getString("termId","1");
        configDataList = sqHelper.queryConfig(Integer.parseInt(termId));
//        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager = new LinearLayoutManager(this);
        tableNameAdapter = new TableNameAdapter(configDataList,MainActivity.this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        rvTableName.setLayoutManager(manager);
        rvTableName.setAdapter(tableNameAdapter);
        //?????????????????????
        tableNameAdapter.setOnItemClickListener(this.myTableListener);
    }
    //?????????????????????
    private TableNameAdapter.OnItemClickListener myTableListener = new TableNameAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if(!configDataList.get(position).isChecked()){
                for(int i=0; i<configDataList.size(); i++){
                    if(i != position){
                        configDataList.get(i).setChecked(false);
                    }else{
                        configDataList.get(i).setChecked(true);
                        int termId = configDataList.get(i).getId();
                        String className = configDataList.get(i).getClassName();
                        String timeId = sqHelper.queryTimeId(String.valueOf(termId));
                        long termStart = Long.valueOf(configDataList.get(i).getTermStart());
                        String curWeek = configDataList.get(i).getCurWeek();
                        int secTime = sqHelper.querySecTime(String.valueOf(termId));
                        String termWeeks = configDataList.get(i).getTermWeeks();
//                        Log.d(TAG, "onItemClick: =======================");
//                        Log.d(TAG, "termId: "+configDataList.get(i).getId());
//                        Log.d(TAG, "className: "+className);
//                        Log.d(TAG, "timeId: "+timeId);
//                        Log.d(TAG, "termStart: "+termStart);
//                        Log.d(TAG, "curWeek: "+curWeek);
//                        Log.d(TAG, "secTime: "+secTime);
//                        Log.d(TAG, "termWeeks: "+termWeeks);
//                        Log.d(TAG, "onItemClick: =======================");
                        //??????sp???????????????
                        sp.edit().putString("termId",String.valueOf(termId)).apply();
                        sp.edit().putString("className",className).apply();
                        sp.edit().putString("timeId",timeId).apply();
//                        Log.d(TAG, "onItemClick: sp.get====="+sp.getString("timeId", "0"));
                        sp.edit().putLong("termStart", termStart).apply();
                        sp.edit().putString("curWeek",curWeek).apply();
                        sp.edit().putInt("secTime",secTime).apply();
                        sp.edit().putString("termWeeks",termWeeks).apply();
                    }
                }
                //?????????????????????recycle???????????????
                tableNameAdapter.notifyDataSetChanged();
            }
            //??????????????? ???????????????
            initView();
            initTime(sp.getInt("secTime",10), sp.getString("timeId","1"));
            long start = sp.getLong("termStart",new Date().getTime());
            timeTable.loadData(acquireData(), new Date(start));
//            ToastCustom.showMsgWarning(MainActivity.this, "????????????"+configDataList.get(position).getClassName());
        }

        @Override
        public void onItemLongClick(View view, int position) {
            if(!configDataList.get(position).isChecked()){
                ToastCustom.showMsgWarning(MainActivity.this, "??????"+configDataList.get(position).getClassName());
            }
        }
    };

    private List<CourseData> acquireData(){
        //????????????
        if (sp.getBoolean("isFirstUse", true)) {
            sp.edit().putBoolean("isFirstUse", false).apply();
            sp.edit().putString("termId", termId).apply();
            sp.edit().putString("className", className).apply();
            sp.edit().putString("timeId", timeId).apply();
            sp.edit().putLong("termStart", termStart).apply();
            sp.edit().putString("curWeek", curWeek).apply();
            sp.edit().putInt("secTime", secTime).apply();
            sp.edit().putString("termWeeks", termWeeks).apply();

        }else{
            courses = sqlite.listAll(termId);
        }
        return courses;
    }


    /**
     * ?????????????????? ????????????????????????????????????
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
     * ??????????????????????????? ???????????????
     *
     */
    private void setWeekBold(){
        String week = tv_curTime.getText().toString();
        if(week.equals("??????")){
            initStyle(mon, true);
        }else if(week.equals("??????")){
            initStyle(tues, true);
        }else if(week.equals("??????")){
            initStyle(wed, true);
        }else if(week.equals("??????")){
            initStyle(thur, true);
        }else if(week.equals("??????")){
            initStyle(fri, true);
        }else if(week.equals("??????")){
            initStyle(sat, true);
        }else if(week.equals("??????")){
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


    //????????????????????????????????????
    private void setView(int key){
        if(key < 0){
            bg_none.setVisibility(View.VISIBLE);
        }
        else{
            bg_none.setVisibility(View.GONE);
        }
    }


    /**
     * ????????????????????????????????????
     */
    public void setTv_curWeek(String week){
        tv_curWeek.setText(week);
        Date date = new Date(currentTimeMillis());
        if(String.valueOf(timeTable.getCurWeek()).equals(curWeek)){
//            Log.d(TAG, "setTv_curWeek: =="+ day.format(date));
            tv_curTime.setText(sdfWeek.format(date));
        }else{
            tv_curTime.setText("?????????");
        }
        setWeekBold();
    }


    /**
     * popWindow??????
     * https://www.jianshu.com/p/e331ffd2452f
     */
    private void showPopWindow(){
        //seekBar  ???????????????
        SeekBar seekBar = contentView.findViewById(R.id.seekBar);
        seekBar.setVisibility(View.GONE);

        popupWindow.dismiss();
//        popupWindow.isShowing();
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.pop_style);
        popupWindow.showAtLocation(MainActivity.this.findViewById(R.id.bt_ellipsis), Gravity.BOTTOM|Gravity.CENTER_VERTICAL,0,0);
    }

    /**
     * ???????????????????????????
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     * https://blog.csdn.net/brian512/article/details/52096445  Android????????????????????????????????????
     */
    private void setBar_color(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    /**
     * ????????????popWindow????????????????????????
     * ????????????onClick??????????????????
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
                //??????????????????
                initTableName();
                showPopWindow();
                break;
            case R.id.update_week:
                intentActivity(ScheduleData.class);
                break;
            case R.id.add_table:
                popupWindow.dismiss();
                btnInsertTable();
                break;
            case R.id.manage:
                popupWindow.dismiss();
                intentActivity(ManyTable.class);
                break;
            case R.id.menu_clock:
                intentActivity(MenuClock.class);
                break;
            case R.id.menu_setting:
                intentActivity(MenuSetting.class);
                break;
            case R.id.menu_help:
                Uri uri = Uri.parse("https://github.com/xch1024/Timeflies");
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);
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

    //????????????
    private void btnInsertTable() {
        popupWindow.dismiss();
        dialog = new DialogCustom(MainActivity.this, R.layout.dialog_tablename,0.8);
        dialog.setTableTitle("????????????");
        dialog.setTableNameCancelListener(view -> dialog.dismiss());
        dialog.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = dialog.getTableEdit();
                if(TextUtils.isEmpty(tableName)||tableName.equals("")){
                    ToastCustom.showMsgWarning(MainActivity.this, "???????????????????????????~");
                }else{
                    long insert = btnInsert(tableName);
                    if(insert > 0 ){
                        ToastCustom.showMsgTrue(MainActivity.this, "????????????~");
                        //                    initTableName("1");
                    }else{
                        ToastCustom.showMsgFalse(MainActivity.this, "????????????~");
                    }
                    dialog.dismiss();
                }
            }

            private long btnInsert(String tableName) {
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
     * ????????????
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    /**
     * ?????????????????????
     */
    private void BtnContact(){
        dialog = new DialogCustom(MainActivity.this, R.layout.dialog_menu, 0.8);
        dialog.setMenuConfirmListener(view -> {
            dialog.dismiss();
            ToastCustom.showMsgTrue(getApplicationContext(), "??????????????????");
        });
        dialog.setMenuCancelListener(view -> {
            ToastCustom.showMsgTrue(getApplicationContext(), "??????????????????");
            dialog.dismiss();
        });
        dialog.show();
    }

}