package com.example.timeflies.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.timeflies.model.TimeData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:halo
 * @projectName:com.example.timeflies.sqlite
 * @date:2022-04-19
 * @time:19:53
 * @description:
 */
public class SqHelper {

    private final SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private static String message;

    public SqHelper(Context context) {
        helper =  ScheduleSqlite.getInstance(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 查询配置信息
     * @param target
     * @return
     */
    public String queryConfig(String target) {
        db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from configs", null);
            while(cursor.moveToNext()){
                int targetIndex = cursor.getColumnIndex(target);
                if( targetIndex >= 0){
                    message = cursor.getString(targetIndex);
                }
            }
            cursor.close();
        }
        db.close();
        return message;
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
     *
     */
    public List<TimeData> queryDb() {
        List<TimeData> list = new ArrayList<>();
        //清除数据
        list.clear();
        SQLiteDatabase db = helper.getReadableDatabase();
        //规范：确保数据库打开成功，才能放心操作
        if (db.isOpen()) {
            //返回游标
            Cursor cursor = db.rawQuery("select * from schedules", null);
            while(cursor.moveToNext()){
                int _id = cursor.getInt(0);
                String startTime = cursor.getString(1);
                String endTime = cursor.getString(2);
                TimeData s = new TimeData(_id, startTime, endTime);
                list.add(s);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
            //规范：必须关闭数据库
            db.close();
        }
        return list;
    }


}
