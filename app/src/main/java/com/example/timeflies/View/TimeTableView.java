package com.example.timeflies.View;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.timeflies.R;
import com.example.timeflies.model.CourseData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTableView extends LinearLayout {

    private static String TAG = "xch";

    //最大星期数
    private int weeksNum = 7;

    //最大节数
    private int maxSection = 20;

    //最大学期周数
    private int maxTerm = 30;

    //圆角半径
    private int radius = 9;

    //线宽
    private int tableLineWidth = 1;

    //课表信息字体大小
    private int courseSize = 11;

    //单元格高度
    private int cellHeight = 75;

    private Context mContext;
    private List<CourseData> courseList;
    private Map<Integer, List<CourseData>> courseMap = new HashMap<>();

    //开学日期
    private Date startDate;
    private long weekNum = 1;
    private long curWeek;


    private LinearLayout mMainLayout;

    public TimeTableView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public TimeTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    /**
     * 数据预处理
     */
    private void preprocessorParam() {
        tableLineWidth = dip2px(tableLineWidth);
        cellHeight = dip2px(cellHeight);
    }

    /**
     * 加载数据
     *
     * @param courses
     */
    public int loadData(List<CourseData> courses, Date date) {
        this.courseList = courses;
        this.startDate = date;
        weekNum = calcWeek(startDate);
        handleData(courseMap, courses, weekNum);
        int key = flushView(courseMap, weekNum);
        return key;
    }

    /**
     * 处理数据
     * @param courseMap 处理结果
     * @param courseList 数据
     * @param weekNum 周次
     */
    private void handleData(Map<Integer, List<CourseData>> courseMap, List<CourseData> courseList, long weekNum) {
        courseMap.clear();
        for (CourseData c : courseList) {
            String courseTime = c.getCourseTime();
            if(TextUtils.isEmpty(courseTime)) continue;
            String[] courseArray = courseTime.split(";");
            for(int i = 0; i < courseArray.length; i++){
                CourseData clone = c.clone();
                String[] info = courseArray[i].split(":");
                if (Integer.parseInt(info[0]) > weekNum || Integer.parseInt(info[1]) < weekNum) continue;
                if ("全周".equals(info[2]) || weekNum % 2 == 0 && "双周".equals(info[2]) || weekNum % 2 == 1 && "单周".equals(info[2])) {
                    clone.setStartWeek(Integer.parseInt(info[0]));
                    clone.setEndWeek(Integer.parseInt(info[1]));
                    clone.setWeekType(info[2]);
                    clone.setDay(Integer.parseInt(info[3]));
                    clone.setSectionStart(Integer.parseInt(info[4]));
                    clone.setSectionEnd(Integer.parseInt(info[5]));
                    clone.setTeacherName(info[6]);
                    clone.setClassroom(info[7]);
                    List<CourseData> courses = courseMap.get(clone.getDay());
                    if (null == courses) {
                        courses = new ArrayList<>();
                        courseMap.put(clone.getDay(), courses);
                    }
                    courses.add(clone);
                }
            }
        }
    }

    /**
     * 初始化视图
     */
    private void initView(){
        preprocessorParam();
        //课程信息
        flushView(null, weekNum);
    }

    /**
     * 刷新课程视图
     * @param courseMap 课程数据
     */
    public int flushView(Map<Integer, List<CourseData>> courseMap,long weekNum) {
        int value = 0;
        if (null != mMainLayout) removeView(mMainLayout);
        mMainLayout = new LinearLayout(mContext);
        mMainLayout.setOrientation(HORIZONTAL);
        mMainLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mMainLayout);
        setCurWeek(weekNum);
        if (null == courseMap || courseMap.isEmpty()) {
            value = -1;
        } else {
            for (int i = 1; i <= weeksNum; i++) {
                value = 1;
                addDayCourse(mMainLayout, courseMap, i);
            }
        }
        invalidate();
        return value;
    }


    /**
     * 添加单天课程
     *
     * @param pViewGroup pViewGroup 父组件
     * @param day        星期
     */
    private void addDayCourse(ViewGroup pViewGroup, Map<Integer, List<CourseData>> courseMap, int day) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(VERTICAL);
        List<CourseData> courses = getCourses(courseMap, day);
        if (null != courses) {
            for (int i = 0, size = courses.size(); i < size; i++) {
                CourseData course = courses.get(i);
                int start = course.getSectionStart();
                if (i == 0) {
                    addBlankCell(linearLayout, start - 1);
                }else{
                    int spacing = course.getSectionStart() - courses.get(i - 1).getSectionEnd() - 1;
                    addBlankCell(linearLayout, spacing);
                }
                int height = course.getSectionEnd()-course.getSectionStart()+1;
                AddAddCourseActivityCell(linearLayout, course, height);

                if (i == size - 1) {
                    int last = courses.get(i).getSectionEnd();
                    addBlankCell(linearLayout, maxSection - last);
                }
            }
        } else {
            addBlankCell(linearLayout, maxSection);
        }
        pViewGroup.addView(linearLayout);
    }

    /**
     * 获取单天课程信息
     *
     * @param day 星期
     * @return 课程信息List
     */
    private List<CourseData> getCourses(Map<Integer, List<CourseData>> courseMap, int day) {
        final List<CourseData> courses = courseMap.get(day);
        if (null != courses) {
            Collections.sort(courses, new Comparator<CourseData>() {
                @Override
                public int compare(CourseData o1, CourseData o2) {
                    return o1.getSectionStart() - o2.getSectionStart();
                }
            });
        }
        return courses;
    }

    /**
     * 添加课程单元格
     * @param pViewGroup 父组件
     * @param course 课程信息
     */
    private void AddAddCourseActivityCell(ViewGroup pViewGroup, CourseData course, int height) {
        String color = course.getCourseColor();
        RoundTextView textView = new RoundTextView(mContext, radius, Color.parseColor(color));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height*cellHeight+2*tableLineWidth*(height-1));
        layoutParams.setMargins(tableLineWidth,tableLineWidth,tableLineWidth,tableLineWidth);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(courseSize);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        if(course.getClassroom().equals("null")){
            course.setClassroom("");
        }else{
            course.setClassroom("\n@"+course.getClassroom());
        }
        if(course.getTeacherName().equals("null")){
            course.setTeacherName("");
        }else{
            course.setTeacherName("\n"+course.getTeacherName());
        }
        textView.setText(String.format("%s%s\n\n第%d~%d周%s\n%s",
                course.getCourseName(), course.getClassroom(),course.getStartWeek(),
                course.getEndWeek(), course.getTeacherName(), course.getWeekType()));
        pViewGroup.addView(textView);
    }


    /**
     * 添加空白块
     *
     * @param pViewGroup 父组件
     * @param num        空白块数量
     */
    private void addBlankCell(ViewGroup pViewGroup, int num) {
        for (int i = 0; i < num; i++) {
            TextView blank = new TextView(mContext);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, cellHeight);
            layoutParams.setMargins(tableLineWidth,tableLineWidth,tableLineWidth,tableLineWidth);
            blank.setLayoutParams(layoutParams);
            pViewGroup.addView(blank);
        }
    }

    public int toggleWeek(int flag){
        if(flag < 0){
            weekNum = weekNum - 1 <= 0 ? weekNum : weekNum - 1;
        }else{
            weekNum = weekNum + 1 > maxTerm  ? weekNum : weekNum + 1;
        }
        handleData(courseMap, courseList, weekNum);
       int key = flushView(courseMap, weekNum);
       return key;
    }

    /**
     * 计算当前周次
     * @param date
     * @return
     */
    public long calcWeek(Date date) {
        return (new Date().getTime() - date.getTime()) / (1000 * 3600 * 24 * 7) + 1;
    }

    //int转dp
    private int dip2px(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }

    public void setMaxSection(int maxSection) {
        this.maxSection = maxSection;
    }

    public int getMaxSection() {
        return maxSection;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setTableLineWidth(int tableLineWidth) {
        this.tableLineWidth = tableLineWidth;
    }

    public void setCourseSize(int courseSize) {
        this.courseSize = courseSize;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public long getCurWeek() {
        return curWeek;
    }

    public void setCurWeek(long curWeek) {
        this.curWeek = curWeek;
    }

    public int getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(int maxTerm) {
        this.maxTerm = maxTerm;
    }
}
