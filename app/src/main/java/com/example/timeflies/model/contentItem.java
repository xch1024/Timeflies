package com.example.timeflies.model;

public class contentItem {

    public String week;
    public String time;
    public String teacher;
    public String location;

    /**
     * 是不是点击添加按钮添加的时间段，添加的可以删除，否则不能删除
     * 时间段最少要显示1个
     *
     */
    public boolean isAdded = false;
    public boolean getIsAdded() {
        return isAdded;
    }



    public void setWeek(String week) {
        this.week = week;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getWeek() {
        return week;
    }

    public String getTime() {
        return time;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getLocation() {
        return location;
    }


}
