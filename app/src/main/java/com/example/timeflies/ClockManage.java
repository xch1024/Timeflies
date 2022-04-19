package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.adapter.ClockManageAdapter;
import com.example.timeflies.model.TimeTableData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class ClockManage extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvRecyclerView;
    private ClockManageAdapter adapter;
    private List<TimeTableData> list = new ArrayList<>();

    private TextView tvTitle;
    private ImageView ivDonate, ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_manage);

        initView();
        setListener();
        queryDb();
    }

    private void initSchedule() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRecyclerView.setLayoutManager(layoutManager);
        adapter = new ClockManageAdapter(list, this);
        rvRecyclerView.setAdapter(adapter);
        rvRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initView(){
        rvRecyclerView = findViewById(R.id.rv_clockManage);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivDonate = findViewById(R.id.ivSave);
        tvTitle.setText(R.string.clock_manage);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);
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
        }
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
    public void queryDb() {

        //清除数据
        list.clear();
        Log.i("xch", "queryDb: 查询");
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        //规范：确保数据库打开成功，才能放心操作
        if (db.isOpen()) {
            //返回游标
            Cursor cursor = db.rawQuery("select * from schedules", null);
            while(cursor.moveToNext()){
                int _id = cursor.getInt(0);
                String startTime = cursor.getString(1);
                String endTime = cursor.getString(2);
                TimeTableData s = new TimeTableData(_id, startTime, endTime);
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


}