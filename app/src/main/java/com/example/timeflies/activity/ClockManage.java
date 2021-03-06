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
import android.text.TextUtils;
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

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class ClockManage extends AppCompatActivity implements View.OnClickListener {

    private TimeData timeData;
    private List<TimeData> list = new ArrayList<>();
    private RecyclerView rvRecyclerView;
    private ClockManageAdapter adapter;

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
        list.clear();
        list = timeData.toDetail();
        Log.d("xch", "List<TimeData> list==="+ list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvRecyclerView.setLayoutManager(layoutManager);
        adapter = new ClockManageAdapter(list, this);
        rvRecyclerView.setAdapter(adapter);
        rvRecyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemClickListener(clickListener);
    }

    private void initView(){
        //页面传值  获取值
        Bundle bundle = getIntent().getExtras();
        timeData = (TimeData) bundle.getSerializable("timeData");
        timeId = bundle.getInt("timeId");
        timeName = bundle.getString("timeName");
//        Log.d("xch", "bundle:timeId== "+timeId);
//        Log.d("xch", "bundle:timeName== "+timeName);
        sqHelper = new SqHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivDonate = findViewById(R.id.ivSave);
        ivDonate.setVisibility(View.GONE);

        view_time_Name = findViewById(R.id.view_time_Name);
        time_name =findViewById(R.id.time_name);

        rvRecyclerView = findViewById(R.id.rv_clockManage);

        tvTitle.setText(R.string.clock_manage);
        time_name.setText(timeName);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);
        view_time_Name.setOnClickListener(this);
    }

    private ClockManageAdapter.OnItemClickListener clickListener = new ClockManageAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            switch (v.getId()){
                case R.id.update_time:
                    ToastCustom.showMsgWarning(ClockManage.this, "现在设置第 "+(position+1)+ " 节上课时间");
                    btnUpdateTime(position);
                    break;
            }
        }
    };

    private String TAG = "xch";

    /**
     * 修改时间
     * @param position
     */
    private void btnUpdateTime(int position) {
        String start = list.get(position).getStartTime();
        String end = list.get(position).getEndTime();
        dialogCustom = new DialogCustom(ClockManage.this, R.layout.dialog_update_time, 0.8);
        dialogCustom.setTimeStep("第 "+(position+1)+" 节").setUptimeStart(start).setUptimeEnd(end);
        dialogCustom.setUpdateTimeCancelListener(view -> dialogCustom.dismiss());
        dialogCustom.setUpdateTimeConfirmListener(view -> {
            String start1 = dialogCustom.getUptimeStart();
            String end1 = dialogCustom.getUptimeEnd();
            if(TextUtils.isEmpty(start1) || TextUtils.isEmpty(end1)){
                ToastCustom.showMsgWarning(ClockManage.this, "输入内容不可为空哦~");
            }else{
                int update = btnUpdate(position, start1, end1);
                if(update > 0 ){
                    initSchedule();
                    ToastCustom.showMsgTrue(ClockManage.this, "修改成功~");
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
        dialogCustom.setCanceledOnTouchOutside(false);
    }

    private int btnUpdate(int position, String start, String end) {
//        Log.d(TAG, "btnUpdate: getUptimeStart"+start);
//        Log.d(TAG, "btnUpdate: getUptimeEnd"+end);
        list.get(position).setStartTime(start);
        list.get(position).setEndTime(end);
        timeData = TimeData.toCourse(list, timeId);
        timeData.setTableName(list.get(position).getTableName());
        int update = sqHelper.updateTimeStep(timeData);
        return update;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MenuClock.class);
                break;
            case R.id.view_time_Name:
//                Log.d("xch", "view_time_Name: "+timeId);
                if(timeId == 1){
                    ToastCustom.showMsgWarning(this, "默认时间表不能改名哦~");
                }else{
                    BtnUpdate();
                }
                break;
            case R.id.ivSave:
                ToastCustom.showMsgWarning(this, "保存~");
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
                if(TextUtils.isEmpty(newName)){
                    ToastCustom.showMsgWarning(ClockManage.this, "输入内容不可为空哦~");
                }else{
                    int update = sqHelper.updateTimes(timeId, newName);
                    if( update > 0 ){
                        time_name.setText(newName);
                        ToastCustom.showMsgTrue(ClockManage.this, "修改成功~");
                        dialogCustom.dismiss();
                    }
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