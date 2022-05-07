package com.example.timeflies.model;

/**
 * @author:halo
 * @projectName:com.example.timeflies.model
 * @date:2022-04-14
 * @time:11:42
 * @description:
 */
public class ContentData {
    public String week;
    public String weekType;
    public String day;
    public String step;
    public String teacher;
    public String classroom;

    public ContentData(String week, String weekType, String day, String step, String teacher, String classroom) {
        this.week = week;
        this.weekType = weekType;
        this.day = day;
        this.step = step;
        this.teacher = teacher;
        this.classroom = classroom;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
