package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.adapter.TableChoiceAdapter;
import com.example.timeflies.model.TimeData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class MenuClock extends AppCompatActivity implements View.OnClickListener {

    private TableChoiceAdapter tableChoiceAdapter;
    private RecyclerView recyclerView;
    private List<TimeData> timeDataList;

    private TextView tvTitle;
    private ImageView ivNull, ivBack;
    private View vAddItem;
    private SqHelper sqHelper = new SqHelper(MenuClock.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clock);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initView();
        initTable();
        setListener();
    }

    private void initView(){
        recyclerView = findViewById(R.id.clockManage);
        tvTitle = findViewById(R.id.tvTitle);
        ivNull = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        vAddItem = findViewById(R.id.addItem);
        vAddItem.setVisibility(View.GONE);

        tvTitle.setText(R.string.menu_clock_view);
        ivNull.setVisibility(View.GONE);
    }

    private void initTable() {

        timeDataList = sqHelper.queryTime();
        LinearLayoutManager LayoutManager = new LinearLayoutManager(MenuClock.this);
        recyclerView.setLayoutManager(LayoutManager);
        tableChoiceAdapter = new TableChoiceAdapter(MenuClock.this, timeDataList);
        recyclerView.setAdapter(tableChoiceAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        LayoutManager.setStackFromEnd(true);
        tableChoiceAdapter.setOnItemClickListener(MyItemClickListener);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        vAddItem.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MainActivity.class);
                break;
            case R.id.addItem:
//                BtnAddItem();
                break;
        }
    }

    /**
     * 页面跳转
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MenuClock.this, cls);
        startActivity(intent);
    }

    /**
     * recycleView上的按钮
     */
    private TableChoiceAdapter.OnItemClickListener MyItemClickListener = new TableChoiceAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, ContentAdapter.ViewName viewName, int position) {
            switch (v.getId()){
                case R.id.ivDelete:
                    ToastCustom.showMsgWarning(MenuClock.this,"长按删除哦");
                    break;
                case R.id.ivEdit:
                    int timeId = timeDataList.get(position).getId();
                    String timeName = timeDataList.get(position).getTableName();
                    //页面跳转及传值
                    TimeData timeData = timeDataList.get(position);
                    Intent intent = new Intent(MenuClock.this,ClockManage.class);
                    intent.putExtra("timeData", timeData);
                    intent.putExtra("timeId", timeId);
                    intent.putExtra("timeName", timeName);
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v, int position) {
            switch (v.getId()){
                case R.id.ivDelete:
                    int id = timeDataList.get(position).getId();
                    int delete = sqHelper.delTimes(id);
                    if(delete > 0 ){
                        ToastCustom.showMsgTrue(MenuClock.this,"删除成功~");
                        timeDataList.clear();
                        initTable();
                    }else{
                        ToastCustom.showMsgTrue(MenuClock.this,"删除失败~");
                    }
                    break;
            }
        }
    };

    private void BtnAddItem(){
        DialogCustom dialogCustom = new DialogCustom(MenuClock.this,R.layout.dialog_tablename,0.7);
        dialogCustom.setTableTitle("时间表名称");
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = dialogCustom.getTableEdit();
                if(TextUtils.isEmpty(name)){
                    ToastCustom.showMsgWarning(MenuClock.this,"名称不能为空哦~");
                }else{
                    initTable();
                    tableChoiceAdapter.notifyDataSetChanged();
                    copy();
                    ToastCustom.showMsgTrue(MenuClock.this,"新建成功~");
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }

    /**
     * 复制20条时间
     */
    private void copy(){
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            db.execSQL("insert into schedules(startTime, endTime) select startTime, endTime from schedules where tableName_id = 1");
        }
        db.close();
    }

    /**
     * 删除
     */
    private void delete(){
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {

        }
        db.close();
    }


}