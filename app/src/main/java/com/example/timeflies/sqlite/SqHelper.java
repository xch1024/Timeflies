package com.example.timeflies.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        Log.i("xch", "queryConfig: 调用方查询");
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




}
