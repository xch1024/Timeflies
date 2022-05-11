package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.adapter.ClockManageAdapter;
import com.example.timeflies.model.TimeData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class ClockManage extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvRecyclerView;
    private ClockManageAdapter adapter;
    private TimeData timeData;

    private TextView tvTitle;
    private ImageView ivDonate, ivBack;

    private TextView time_name;
    private View view_time_Name;

    private SqHelper sqHelper;
    private DialogCustom dialogCustom;
    private int timeId;
    private String timeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_manage);

        initView();
        initSchedule();
        setListener();
    }

    private void initSchedule() {
        List<TimeData> list = timeData.toDetail();
        Log.d("xch", "timeData==="+ timeData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRecyclerView.setLayoutManager(layoutManager);
        adapter = new ClockManageAdapter(list, this);
        rvRecyclerView.setAdapter(adapter);
        rvRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initView(){
        //页面传值  获取值
        Bundle bundle = getIntent().getExtras();
        timeData = (TimeData) bundle.getSerializable("timeData");
        timeId = bundle.getInt("timeId");
        timeName = bundle.getString("timeName");
        Log.d("xch", "bundle:timeId== "+timeId);
        Log.d("xch", "bundle:timeName== "+timeName);
        sqHelper = new SqHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivDonate = findViewById(R.id.ivSave);

        view_time_Name = findViewById(R.id.view_time_Name);
        time_name =findViewById(R.id.time_name);

        rvRecyclerView = findViewById(R.id.rv_clockManage);

        tvTitle.setText(R.string.clock_manage);
        ivDonate.setVisibility(View.GONE);
        time_name.setText(timeName);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);
        view_time_Name.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MenuClock.class);
                break;
            case R.id.view_time_Name:
                Log.d("xch", "view_time_Name: "+timeId);
                if(timeId == 1){
                    ToastCustom.showMsgWarning(this, "默认时间表不能改名哦~");
                }else{
                    BtnUpdate();
                }
                break;
        }
    }

    private void BtnUpdate() {
        dialogCustom = new DialogCustom(this,R.layout.dialog_tablename, 0.8);
        dialogCustom.setTableTitle("时间表名称");
        dialogCustom.setTableEdit(timeName);
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
                int update = sqHelper.updateTimes(timeId, newName);
                if( update > 0 ){
                    time_name.setText(newName);
                    ToastCustom.showMsgTrue(ClockManage.this, "修改成功~");
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }

    /**
     * 页面跳转
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(ClockManage.this, cls);
        startActivity(intent);
    }




}