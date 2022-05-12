package com.example.timeflies.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.timeflies.model.ConfigData;
import com.example.timeflies.model.TimeData;

import java.util.ArrayList;
import java.util.List;

public class SqHelper {

    String TAG = "xch";
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    public SqHelper(Context context) {
        helper =  ScheduleSqlite.getInstance(context);
        db = helper.getWritableDatabase();
    }

    public long insertConfig(String nullColumnHack, ContentValues values){
        db = helper.getWritableDatabase();
        long count = 0;
        if (db.isOpen()) {
            count = db.insert("configs", nullColumnHack, values);
            db.close();
        }
        return count;
    }

    public void insertConfig(String className, String timeId, String termStart, String curWeek, String secTime, String termWeeks){
        db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("className", className);
            values.put("timeId", timeId);
            values.put("termStart", termStart);
            values.put("curWeek", curWeek);
            values.put("secTime", secTime);
            values.put("termWeeks", termWeeks);
            db.insert("configs", null, values);
        }
        db.close();
    }
    /**
     * 查询配置信息
     * @return
     */
    public List<ConfigData> queryConfig() {
        List<ConfigData> list = new ArrayList<>();
        db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from configs",null);
            while(cursor.moveToNext()){
                ConfigData configData = new ConfigData();
                configData.setId(cursor.getInt(0));
                configData.setClassName(cursor.getString(1));
                configData.setTimeId(cursor.getString(2));
                configData.setTermStart(cursor.getString(3));
                configData.setCurWeek(cursor.getString(4));
                configData.setSceTime(cursor.getString(5));
                configData.setTermWeeks(cursor.getString(6));
                list.add(configData);
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    /**
     * 修改表配置信息
     * @param target
     * @return
     */
    public void updateConfig(String target, String after){
        db = helper.getWritableDatabase();
        //规范：确保数据库打开成功，才能放心操作
        if(db.isOpen()){
            ContentValues values = new ContentValues();
            values.put(target,after);
            db.update("configs", values, "_id = ?", new String[]{"1"});
        }
        db.close();
    }

    /**
     * 查询数据库作息时间表的所有内容
     * 指定timeId
     */
    public List<TimeData> queryTime(String  timeId) {
        List<TimeData> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
//        Log.d(TAG, "queryTime: "+timeId);
        Cursor cursor = db.rawQuery("select * from times where _id = ?",new String[]{timeId});
        //规范：确保数据库打开成功，才能放心操作
        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                TimeData time = new TimeData();
                time.setId(cursor.getInt(0));
                time.setTableName(cursor.getString(1));
                time.setStartTime(cursor.getString(2));
                time.setEndTime(cursor.getString(3));
                time.setTableTime(cursor.getString(4));
//                Log.d(TAG, "queryTime: "+time);
                list.add(time);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
        }
        //规范：必须关闭数据库
        db.close();
        return list;
    }

    public List<TimeData> queryTime() {
        List<TimeData> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from times",null);
        //规范：确保数据库打开成功，才能放心操作
        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                TimeData time = new TimeData();
                time.setId(cursor.getInt(0));
                time.setTableName(cursor.getString(1));
                time.setStartTime(cursor.getString(2));
                time.setEndTime(cursor.getString(3));
                time.setTableTime(cursor.getString(4));
//                Log.d(TAG, "queryTime: "+time);
                list.add(time);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
        }
        //规范：必须关闭数据库
        db.close();
        return list;
    }

    //
    public TimeData queryTimeData(String  timeId){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from times where _id = ?",new String[]{timeId});
        TimeData time = new TimeData();
        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                time.setId(cursor.getInt(0));
                time.setTableName(cursor.getString(1));
                time.setStartTime(cursor.getString(2));
                time.setEndTime(cursor.getString(3));
                time.setTableTime(cursor.getString(4));
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
        }
        //规范：必须关闭数据库
        db.close();
        return time;
    }

    /**
     * 新建时间表
     */
    public long insert(String nullColumnHack, ContentValues values){
        SQLiteDatabase db = helper.getWritableDatabase();
        long count = db.insert("times", nullColumnHack, values);
        db.close();
        return count;
    }

    public long insert(TimeData timeData){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tableName",timeData.getTableName());
        values.put("time",timeData.getTableTime());
//        Log.d(TAG, "insert: "+values);
        long count = db.insert("times", null,values);
        db.close();
        return count;
    }

    /**
     * 删除时间表
     */
    public int delTimes(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;
        if(db.isOpen()){
            count = db.delete("times", "_id= ?", new String[]{String.valueOf(id)});
        }
        db.close();
        return count;
    }
    public int delete(String whereClause, String[] whereArgs){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("times", whereClause, whereArgs);
        db.close();
        return count;
    }

    /**
     * 删除时间表 修改时间表
     * @param _id
     * @param target
     */
    public int updateTimes(int _id, String target){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = 0;
        if (db.isOpen()){
            ContentValues values = new ContentValues();
            values.put("tableName",target);
            count = db.update("times", values, "_id = ?", new String[]{String.valueOf(_id)});
        }
        //规范：必须关闭数据库
        db.close();
        return count;
    }
    public int update(ContentValues values, String whereClause, String[] whereArgs){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.update("times", values, whereClause, whereArgs);
        db.close();
        return count;
    }

    //修改上课节次
    public int updateTimeStep(TimeData timeData){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(db.isOpen()){
            values.put("tableName",timeData.getTableName());
            values.put("time",timeData.getTableTime());
        }
        int count = db.update("times", values, "_id=?", new String[]{String.valueOf(timeData.getId())});
        db.close();
        return count;
    }

}
