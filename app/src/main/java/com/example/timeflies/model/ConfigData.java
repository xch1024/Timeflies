package com.example.timeflies.model;

public class ConfigData {
    private int id;
    private String className;
    private String timeId;
    private String termStart;
    private String curWeek;
    private String sceTime;
    private String termWeeks;

    public ConfigData(){

    }

    public ConfigData(int id, String className, String timeId, String termStart, String curWeek, String sceTime, String termWeeks) {
        this.id = id;
        this.className = className;
        this.timeId = timeId;
        this.termStart = termStart;
        this.curWeek = curWeek;
        this.sceTime = sceTime;
        this.termWeeks = termWeeks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getCurWeek() {
        return curWeek;
    }

    public void setCurWeek(String curWeek) {
        this.curWeek = curWeek;
    }

    public String getSceTime() {
        return sceTime;
    }

    public void setSceTime(String sceTime) {
        this.sceTime = sceTime;
    }

    public String getTermWeeks() {
        return termWeeks;
    }

    public void setTermWeeks(String termWeeks) {
        this.termWeeks = termWeeks;
    }
}
