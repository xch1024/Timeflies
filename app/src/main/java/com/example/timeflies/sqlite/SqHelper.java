package com.example.timeflies.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    String TAG = "xch";
    private SQLiteOpenHelper helper;
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
     */
    public List<TimeData> queryTime(String  id) {
        List<TimeData> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from times where _id = ?",new String[]{id});
        //规范：确保数据库打开成功，才能放心操作
        if (cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                TimeData time = new TimeData();
                time.setId(cursor.getInt(0));
                time.setTableName(cursor.getString(1));
                time.setStartTime(cursor.getString(2));
                time.setEndTime(cursor.getString(3));
                time.setTableTime(cursor.getString(4));
                Log.d(TAG, "queryTime: "+time);
                list.add(time);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
        }
        //规范：必须关闭数据库
        db.close();
        return list;
    }

    /**
     * 删除时间表
     */
    public void delTable(Context context, int id){
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()){
            db.delete("tableNames", "_id= ?", new String[]{String.valueOf(id)});
        }
        db.close();
    }

}
