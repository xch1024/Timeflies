package com.example.timeflies.operater;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 课程表的工具包，主要提供几个便捷的方法
 */
public class ScheduleSupport {
    private static final String TAG = "xch";

    //*****************
    // 日期相关方法
    // getDateStringFromWeek()
    // getWeekDate()
    //*****************

    /**
     * 根据开学时间计算当前周
     * @param termStart 满足"yyyy-M-dd"模式的long类型
     * @return
     */
    public static int timeTransform(Long termStart) {
        long start = termStart;
        long end = new Date().getTime();//当前时间
        //如果当前日期小于开学日期 直接返回
        if(start > end){
            return -1;
        }
        long seconds = (end - start) / 1000;
        long day = seconds / (24 * 3600);
        int week = (int) (Math.floor(day / 7) +1);
        return week;
    }

    /**
     * 计算开学日期
     * @param week 当前周
     * @return
     */
    public static long setDate(int week){
        long now = new Date().getTime();
        long day = week * 7 * 3600 * 24 * 1000;
        long start = now - day;
        return start;
    }

    /**
     * long 类型转换成日期 返回string：yyyy-M-d
     */
    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        return sdf.format(date);
    }



}
