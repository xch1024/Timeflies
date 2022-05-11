package com.example.timeflies.model;


import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:halo
 * @projectName:com.example.timeflies.model
 * @date:2022-04-17
 * @time:17:33
 * @description:
 */
public class TimeData implements Cloneable, Serializable {

    public int id;
    public String tableName;
    public String startTime;
    public String endTime;
    public String tableTime;

    public TimeData(){

    }

    public TimeData(int id, String tableName, String startTime, String endTime, String tableTime) {
        this.id = id;
        this.tableName = tableName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tableTime = tableTime;

    }


    //拆分
    public List<TimeData> toDetail(){
        List<TimeData> timeDataList = new ArrayList<>();
        String[] timeArray = tableTime.split(";");
        for(int i = 0; i < timeArray.length; i++){
            TimeData clone = this.clone();
            String[] info = timeArray[i].split("-");
            clone.setStartTime(info[0]);
            clone.setEndTime(info[1]);
            timeDataList.add(clone);
        }
        return timeDataList;
    }

    public TimeData clone() {
        try {
            return (TimeData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 切分时间段
     * @return
     */
    public static List<TimeData> timeSplit(List<TimeData> list){
        list.clear();
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

    @Override
    public String toString() {
        return "CourseData{" +
                "id=" + id +
                ", tableName='" + tableName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", tableTime='" + tableTime + '\'' +
                '}';
    }

    public String toTime(){
        return String.format("%s-%s", startTime, endTime);
    }

    public static TimeData toCourse(List<TimeData> courseList, int id){
        if(null == courseList) return null;
        TimeData course = new TimeData();
        course.setId(id);
        StringBuffer sb = new StringBuffer();
        for(int i = 0, len = courseList.size(); i < len; i++){
            sb.append(courseList.get(i).toTime());
            if(i != len - 1)sb.append(";");
        }
        course.setTableTime(String.valueOf(sb));
        return course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTableTime() {
        return tableTime;
    }

    public void setTableTime(String tableTime) {
        this.tableTime = tableTime;
    }
}
