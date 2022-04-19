package com.example.timeflies.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class ScheduleSqlite extends SQLiteOpenHelper {

    private static String message;

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


    //1.构造函数私有化
    private ScheduleSqlite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //数据库初始化用
    //创建表 表数据初始化 数据库第一次创建的时候调用 第二次发现有了 就不会重复创建了 也意味着：此函数只会执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表：persons表 _id name
        //自动增长数字：autoincrement
        //主键：primary key 必须唯一
        //_id标准写法 并且主键只能是integer类型的
        //id不标准
        String sql = "create table schedules(_id integer primary key autoincrement not null, startTime Text, endTime Text)";
        db.execSQL(sql);
        String sql1 = "insert into schedules(startTime, endTime) values('08:00', '08:45')";
        String sql2 = "insert into schedules(startTime, endTime) values('08:55', '09:40')";
        String sql3 = "insert into schedules(startTime, endTime) values('10:10', '10:55')";
        String sql4 = "insert into schedules(startTime, endTime) values('11:05', '11:50')";

        String sql5 = "insert into schedules(startTime, endTime) values('14:00', '14:45')";
        String sql6 = "insert into schedules(startTime, endTime) values('14:55', '15:40')";
        String sql7 = "insert into schedules(startTime, endTime) values('16:00', '16:45')";
        String sql8 = "insert into schedules(startTime, endTime) values('16:55', '17:40')";

        String sql9 = "insert into schedules(startTime, endTime) values('18:30', '19:15')";
        String sql10 = "insert into schedules(startTime, endTime) values('19:25', '20:10')";

        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        db.execSQL(sql5);
        db.execSQL(sql6);
        db.execSQL(sql7);
        db.execSQL(sql8);
        db.execSQL(sql9);
        db.execSQL(sql10);

        //本地配置表
        String sqlConfig = "create table configs(_id integer primary key autoincrement not null, className Text, termStart Text, currentWeek text, classTotal text, termTotal text)";
        db.execSQL(sqlConfig);
        String sqlNormal = "insert into configs(className, termStart, currentWeek, classTotal, termTotal) values('大二上学期', '2022-02-22', '2', '10', '30')";
        db.execSQL(sqlNormal);
    }

    //数据库升级用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
