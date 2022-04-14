package com.example.timeflies.model;

/**
 * @author:halo
 * @projectName:com.example.timeflies.model
 * @date:2022-04-14
 * @time:11:42
 * @description:
 */
public class ContentData {
    public String time;
    public String week;
    public String username;
    public String location;

    public ContentData(String week, String time, String username, String location){
        this.time = time;
        this.week = week;
        this.username = username;
        this.location = location;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
