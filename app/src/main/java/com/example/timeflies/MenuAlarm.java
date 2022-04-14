package com.example.timeflies;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.timeflies.utils.ToastCustom;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        dateTime();
    }

    private void initView(){
        tvTime = findViewById(R.id.time);
        tvDate = findViewById(R.id.date);
        tvWeek = findViewById(R.id.week);
        ivSet = findViewById(R.id.set);
        tvSchedule = findViewById(R.id.schedule);
        tvCourse = findViewById(R.id.courseName);
        tvTeacher = findViewById(R.id.teacherName);
        tvLocation = findViewById(R.id.location);
        tvRemark = findViewById(R.id.remark);
        ivSet.setColorFilter(getResources().getColor(R.color.content_text_color));
        ivSet.setOnClickListener(this);
    }

    /**
     * 设置时间
     * https://blog.csdn.net/qq2116538183/article/details/98494855 java中SimpleDateFormat用法详解
     *
     */
    private void dateTime(){
        SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date(currentTimeMillis());
        tvTime.setText(SimpleDateFormat.format(date));
        SimpleDateFormat month = new SimpleDateFormat("M");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        tvDate.setText(month.format(date)+"月"+day.format(date)+"日");
        SimpleDateFormat week = new SimpleDateFormat("E");
        tvWeek.setText(week.format(date));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.set:
                ToastCustom.showMsgTrue(MenuAlarm.this,"设置按钮");
                break;
        }
    }

}