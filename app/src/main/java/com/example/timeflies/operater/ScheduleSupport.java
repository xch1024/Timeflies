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
     * 根据需要算的周数和当前周数计算日期,
     * 用于周次切换时对日期的更新
     *
     * @param targetWeek 需要算的周数
     * @param curWeek    当前周数
     * @return 当周日期集合，共8个元素，第一个为月份（高亮日期的月份），之后7个为周一至周日的日期
     */
    public static List<String> getDateStringFromWeek(int curWeek, int targetWeek) {
        Calendar calendar = Calendar.getInstance();
        if (targetWeek == curWeek)
            return getDateStringFromCalendar(calendar);
        int amount = targetWeek - curWeek;
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return getDateStringFromCalendar(calendar);
    }

    /**
     * 根据周一的时间获取当周的日期
     * @param calendar 周一的日期
     * @return 当周日期数组
     */
    private static List<String> getDateStringFromCalendar(Calendar calendar) {
        List<String> dateList = new ArrayList<>();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        dateList.add((calendar.get(Calendar.MONTH) + 1) + "");
        for (int i = 0; i < 7; i++) {
            dateList.add(calendar.get(Calendar.DAY_OF_MONTH) + "");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

    /**
     * 获取本周的周一-周日的所有日期
     *
     * @return 8个元素的集合，第一个为月份，之后7个依次为周一-周日
     */
    public static List<String> getWeekDate() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);

        int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayOfWeek) {
            calendar1.add(Calendar.DAY_OF_MONTH, -1);
        }
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
        calendar1.add(Calendar.DATE, 0);
        calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);

        Date beginDate = calendar1.getTime();
        calendar1.add(Calendar.DATE, 6);
        Date endDate = calendar1.getTime();
        return getBetweenDates(beginDate, endDate);
    }

    /**
     * 获取两个日期之间的日期集合
     * @param start
     * @param end
     * @return
     */
    private static List<String> getBetweenDates(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        List<String> result = new ArrayList<String>();
        result.add(sdf2.format(new Date()));
        result.add(sdf.format(start));
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd)) {
            result.add(sdf.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        result.add(sdf.format(end));
        return result;
    }

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
        return now - day;
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
