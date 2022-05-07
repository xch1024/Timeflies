package com.example.timeflies.model;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:halo
 * @projectName:com.example.timeflies.model
 * @date:2022-04-17
 * @time:17:33
 * @description:
 */
public class TimeTableData {

    public int id;
    public String startTime;
    public String endTime;
    public String tableName;

    public TimeTableData(int id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeTableData(int id, String tableName){
        this.id = id;
        this.tableName = tableName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
