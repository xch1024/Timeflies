package com.example.timeflies.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author:halo
 * @projectName:com.example.timeflies.model
 * @date:2022-05-05
 * @time:11:26
 * @description:
 */
public class CourseData implements Cloneable, Serializable {
    private int id;
    private String courseName;//课程名
    private String courseColor;//课程显示颜色
    private String courseCredit;//课程学分
    private String courseRemark;//课程备注

    private String weekType;//单双周类型
    private int startWeek;//开始周次
    private int endWeek;//结束周次

    private String teacherName;//教师名
    private String classroom;//教室

    private int day;//星期几
    private int sectionStart;//开始节次
    private int sectionEnd;//结束节次
    private int termId;//学期

    //格式：起始周次-单双周-星期-节次-几节-授课老师-教室
    private String courseTime;//上课时间

    public CourseData(){

    }

    public CourseData(String courseName, String courseColor, String courseCredit, String courseRemark, String courseTime) {
        this.courseName = courseName;
        this.courseColor = courseColor;
        this.courseCredit = courseCredit;
        this.courseRemark = courseRemark;
        this.courseTime = courseTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseColor() {
        return courseColor;
    }

    public void setCourseColor(String courseColor) {
        this.courseColor = courseColor;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCourseRemark() {
        return courseRemark;
    }

    public void setCourseRemark(String courseRemark) {
        this.courseRemark = courseRemark;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSectionStart() {
        return sectionStart;
    }

    public void setSectionStart(int section) {
        this.sectionStart = section;
    }

    public int getSectionEnd() {
        return sectionEnd;
    }

    public void setSectionEnd(int step) {
        this.sectionEnd = step;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public CourseData clone() {
        try {
            return (CourseData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "CourseData{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseColor='" + courseColor + '\'' +
                ", courseCredit='" + courseCredit + '\'' +
                ", courseRemark='" + courseRemark + '\'' +
                ", weekType='" + weekType + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", teacherName='" + teacherName + '\'' +
                ", classroom='" + classroom + '\'' +
                ", day=" + day + '\'' +
                ", sectionStart=" + sectionStart + '\'' +
                ", sectionEnd=" + sectionEnd + '\'' +
                ", termId=" + termId + '\'' +
                ", courseTime='" + courseTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseData course = (CourseData) o;
        return  Objects.equals(courseName, course.courseName) &&
                Objects.equals(courseColor, course.courseColor) &&
                Objects.equals(courseCredit, course.courseCredit) &&
                Objects.equals(courseRemark, course.courseRemark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, startWeek, endWeek, teacherName);
    }

    public List<CourseData> toDetail(){
        List<CourseData> courseDataList = new ArrayList<>();
        if (TextUtils.isEmpty(courseTime)) return courseDataList;
        String[] courseArray = courseTime.split(";");
        for (int i = 0; i < courseArray.length; i++) {
            CourseData clone = this.clone();
            String[] info = courseArray[i].split(":");

            clone.setStartWeek(Integer.parseInt(info[0]));
            clone.setEndWeek(Integer.parseInt(info[1]));
            clone.setWeekType(info[2]);
            clone.setDay(Integer.parseInt(info[3]));
            clone.setSectionStart(Integer.parseInt(info[4]));
            clone.setSectionEnd(Integer.parseInt(info[5]));
            clone.setTeacherName(info[6]);
            clone.setClassroom(info[7]);

            courseDataList.add(clone);
        }
        return courseDataList;
    }

    //格式：开始周次-结束周次-单双周-星期-开始节次-几节-授课老师-教室
    public String toTime(){
        return String.format("%d:%d:%s:%d:%d:%d:%s:%s", startWeek, endWeek, weekType, day, sectionStart, sectionEnd, teacherName, classroom);
    }

    public static CourseData toCourse(List<CourseData> courseList, int id){
        if(null == courseList) return null;
        CourseData course = new CourseData();
        course.setId(id);
        StringBuffer sb = new StringBuffer();
        for(int i = 0, len = courseList.size(); i < len; i++){
            sb.append(courseList.get(i).toTime());
            if(i != len - 1)sb.append(";");
        }
        course.setCourseTime(String.valueOf(sb));
        return course;
    }

}
