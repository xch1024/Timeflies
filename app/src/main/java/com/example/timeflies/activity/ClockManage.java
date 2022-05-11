package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.adapter.ClockManageAdapter;
import com.example.timeflies.model.TimeData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class ClockManage extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvRecyclerView;
    private ClockManageAdapter adapter;
    private List<TimeData> list = new ArrayList<>();

    private TextView tvTitle;
    private ImageView ivDonate, ivBack;

    private TextView tvName;
    private View uName;

    private static String name ,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_manage);

        initView();
        setListener();
        queryDb(Integer.parseInt(id));
    }

    private void initSchedule() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRecyclerView.setLayoutManager(layoutManager);
        adapter = new ClockManageAdapter(list, this);
        rvRecyclerView.setAdapter(adapter);
        rvRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initView(){
        //页面传值  获取值
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        id = bundle.getString("table_id");

        tvName =findViewById(R.id.sName);
        tvName.setText(name);

        rvRecyclerView = findViewById(R.id.rv_clockManage);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivDonate = findViewById(R.id.ivSave);
        tvTitle.setText(R.string.clock_manage);

        uName = findViewById(R.id.uName);

    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);
        uName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MenuClock.class);
                break;
            case R.id.ivSave:
                ToastCustom.showMsgTrue(this, "保存");
                break;
            case R.id.uName:
                if(Integer.valueOf(id) == 1){
                    ToastCustom.showMsgWarning(this, "默认时间表不能改名哦~");
                }else{
                    BtnUpdate();
                }
                break;
        }
    }

    private void BtnUpdate() {
        DialogCustom dialogCustom = new DialogCustom(this,R.layout.dialog_tablename, 0.7);
        dialogCustom.setTableTitle("时间表名称");
        dialogCustom.setTableEdit(name);
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = dialogCustom.getTableEdit();
                updateName(Integer.valueOf(id), newName);
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }

    /**
     * 页面跳转
     *
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(ClockManage.this, cls);
        startActivity(intent);
    }

    //查询数据库内容
    public void queryDb(int target) {
        //清除数据
        list.clear();
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //规范：确保数据库打开成功，才能放心操作
        if (db.isOpen()) {
            //返回游标
            Cursor cursor = db.rawQuery("select * from schedules where tableName_id = ?", new String[]{String.valueOf(target)});
            while(cursor.moveToNext()){
                int _id = cursor.getInt(0);
                String startTime = cursor.getString(1);
                String endTime = cursor.getString(2);
                TimeData s = new TimeData();
                list.add(s);
            }
            //规范：必须关闭游标，不然影响性能
            cursor.close();
            //规范：必须关闭数据库
            db.close();

            //重新加载recycle
            initSchedule();
        }
    }

    public void updateName(int _id,String target){
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()){
            ContentValues values = new ContentValues();
            values.put("tableName",target);
            db.update("tableNames", values, "_id = ?", new String[]{String.valueOf(_id)});
        }
        //规范：必须关闭数据库
        db.close();
        tvName.setText(target);
    }


}