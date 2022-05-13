package com.example.timeflies.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.utils.ToastCustom;


public class MenuAlarm extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTime, tvDate, tvWeek, tvSchedule, tvCourse, tvTeacher, tvLocation, tvRemark;
    private ImageView ivSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_alarm);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();

        //启动新的线程
        new TimeThread().start();
    }

    private void initView(){
        tvTime = findViewById(R.id.time);
        tvDate = findViewById(R.id.date);
        tvWeek = findViewById(R.id.week);
        ivSet = findViewById(R.id.set);
//        tvSchedule = findViewById(R.id.schedule);
//        tvCourse = findViewById(R.id.courseName);
//        tvTeacher = findViewById(R.id.teacherName);
//        tvLocation = findViewById(R.id.location);
//        tvRemark = findViewById(R.id.remark);
        long sysTime = System.currentTimeMillis();//获取系统时间
        CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);//时间显示格式
        CharSequence month = DateFormat.format("M", sysTime);//月显示格式
        CharSequence day = DateFormat.format("dd", sysTime);//日显示格式
        CharSequence week = DateFormat.format("E", sysTime);//周显示格式
        tvTime.setText(sysTimeStr); //更新时间
        tvDate.setText(month+"月"+day+"日");
        tvWeek.setText(week);
        ivSet.setColorFilter(getResources().getColor(R.color.content_text_color));
        ivSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.set:
                ToastCustom.showMsgTrue(MenuAlarm.this,"设置按钮");
                break;
        }
    }

    /**
     * Android——实时显示系统时间    https://blog.csdn.net/xch_yang/article/details/80590911
     */
    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();//获取系统时间
                    CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);//时间显示格式
                    CharSequence month = DateFormat.format("M", sysTime);//月显示格式
                    CharSequence day = DateFormat.format("dd", sysTime);//日显示格式
                    CharSequence week = DateFormat.format("E", sysTime);//周显示格式
                    tvTime.setText(sysTimeStr); //更新时间
                    tvDate.setText(month+"月"+day+"日");
                    tvWeek.setText(week);
                    break;
                default:
                    break;

            }
        }
    };

}