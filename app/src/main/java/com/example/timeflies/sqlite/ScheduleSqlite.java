package com.example.timeflies.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.timeflies.model.CourseData;

import java.util.ArrayList;
import java.util.List;


public class ScheduleSqlite extends SQLiteOpenHelper {

    private static String time = "08:00-08:45;08:55-09:40;10:10-10:55;11:05-11:50;14:00-14:45;14:55-15:40;16:00-16:45;16:55-17:40;18:30-19:15;19:25-20:10;20:20-21:05;21:15-22:00;22:10-22:55;23:05-23:50;23:05-23:50;23:05-23:50;23:05-23:50;23:05-23:50;23:05-23:50;23:05-23:50";
    private static String tableName = "courses";
    private Context context;
    /**
     * MySqliteHelper 工具类 单例模式(1.构造函数私有化 2.对外提供函数(单例函数))
     *
     */
    //2.对外提供函数(单例模式)
    private static  SQLiteOpenHelper Instance;
    public static SQLiteOpenHelper getInstance(Context context) {
        if(Instance == null){
            Instance = new ScheduleSqlite(context, "TimeTable", null, 1);
        }
        return Instance;
    }

    public ScheduleSqlite(Context context){
        super(context, "TimeTable", null, 1);
        this.context = context;
    }

    //1.构造函数私有化
    private ScheduleSqlite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    //数据库初始化用
    //创建表 表数据初始化 数据库第一次创建的时候调用 第二次发现有了 就不会重复创建了 也意味着：此函数只会执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {

        //时间节次表
        String times = "create table times(_id integer primary key autoincrement not null, tableName Text ,startTime Text, endTime Text, time Text)";
        db.execSQL(times);

        //在Insert Into...中采用变量的方法  https://blog.csdn.net/denlee/article/details/1591789
        String norTime = "insert into times(_id, tableName, time) values('1', '默认', '"+time+"')";
        db.execSQL(norTime);

        //创建本地配置表
        String sqlConfig = "create table configs(_id integer primary key autoincrement not null, className Text, timeId Text, termStart Text, curWeek Text, secTime Text, termWeeks Text)";
        db.execSQL(sqlConfig);

        //插入默认配置信息
        String sqlNormal = "insert into configs(className, timeId, termStart, curWeek, secTime, termWeeks) values('默认', '1', '2022-4-25', '1', '10', '20')";
        db.execSQL(sqlNormal);

        String sqlCourse = "create table courses(" +
                "_id integer primary key autoincrement," +
                "courseName text," +
                "courseColor text," +
                "courseCredit text," +
                "courseRemark text," +

                "weekType text, " +
                "startWeek integer," +
                "endWeek integer," +

                "teacherName text," +
                "classroom text, " +

                "day integer," +
                "sectionStart integer," +
                "sectionEnd integer," +

                "term_id integer," +

                "courseTime text" +
                ")";
        db.execSQL(sqlCourse);
    }

    //数据库升级用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //=======================================================================================
    public long insert(String nullColumnHack, ContentValues values){
        SQLiteDatabase database = getWritableDatabase();
        long count = database.insert(tableName, nullColumnHack, values);
        database.close();
        return count;
    }

    //
    public long insert(CourseData courseData){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!TextUtils.isEmpty(courseData.getCourseName())){
            values.put("courseName", courseData.getCourseName());
        }
        if(!TextUtils.isEmpty(courseData.getCourseColor())){
            values.put("courseColor", courseData.getCourseColor());
        }
        if(!TextUtils.isEmpty(courseData.getCourseCredit())) {
            values.put("courseCredit", courseData.getCourseCredit());
        }
        if(!TextUtils.isEmpty(courseData.getCourseRemark())) {
            values.put("courseRemark", courseData.getCourseRemark());
        }
        long count = database.insert(tableName, null, values);
        database.close();
        return count;
    }

    public int delete(String whereClause, String[] whereArgs){
        SQLiteDatabase database = getWritableDatabase();
        int count = database.delete(tableName, whereClause, whereArgs);
        database.close();
        return count;
    }

    public int delete(int id){
        SQLiteDatabase database = getWritableDatabase();
        int count = database.delete(tableName, "_id=?", new String[]{String.valueOf(id)});
        database.close();
        return count;
    }

    public void clear(){
        SQLiteDatabase database = getWritableDatabase();
        if(database.isOpen()){
            Cursor cursor = database.rawQuery("select * from courses", null);
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                delete(id);
            }
            cursor.close();
        }
        database.close();
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs){
        SQLiteDatabase database = getWritableDatabase();
        int count = database.update(tableName, values, whereClause, whereArgs);
        database.close();
        return count;
    }

    public int update(CourseData courseData){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!TextUtils.isEmpty(courseData.getCourseName())){
            values.put("courseName", courseData.getCourseName());
        }
        if(!TextUtils.isEmpty(courseData.getCourseColor())){
            values.put("courseColor", courseData.getCourseColor());
        }
        if(!TextUtils.isEmpty(courseData.getCourseCredit())){
            values.put("courseCredit", courseData.getCourseCredit());
        }
        if(!TextUtils.isEmpty(courseData.getCourseRemark())){
            values.put("courseRemark", courseData.getCourseRemark());
        }
        if(null != courseData.getCourseTime()) {
            values.put("courseTime", courseData.getCourseTime());
        }
        int count = database.update(tableName, values, "_id=?", new String[]{String.valueOf(courseData.getId())});
        database.close();
        return count;
    }

    //更新时间段
    public int updateWeek(int id, String courseTime){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseTime", courseTime);
//        Log.d("xch", "courseTime: ==="+courseTime);
        int count = database.update(tableName, values, "_id=?", new String[]{String.valueOf(id)});
        database.close();
        return count;
    }

    public List<CourseData> listAll(){
        List<CourseData> list = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor data = database.query(tableName, null, null, null, null, null, null);
        if(data.getCount() > 0){
            while(data.moveToNext()) {
                CourseData course = new CourseData();
                course.setId(data.getInt(0));
                course.setCourseName(data.getString(1));
                course.setCourseColor(data.getString(2));
                course.setCourseCredit(data.getString(3));
                course.setCourseRemark(data.getString(4));
                course.setWeekType(data.getString(5));
                course.setStartWeek(data.getInt(6));
                course.setEndWeek(data.getInt(7));
                course.setTeacherName(data.getString(8));
                course.setClassroom(data.getString(9));
                course.setDay(data.getInt(10));
                course.setSectionStart(data.getInt(11));
                course.setSectionEnd(data.getInt(12));
                course.setTermId(data.getInt(13));
                course.setCourseTime(data.getString(14));
                list.add(course);
            }
        }
        return list;
    }



}
