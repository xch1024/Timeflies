package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
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
import com.example.timeflies.model.TimeTableData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

public class MenuClock extends AppCompatActivity implements View.OnClickListener {

    private TableChoiceAdapter tableChoiceAdapter;
    private RecyclerView recyclerView;
    private List<TimeTableData> list = new ArrayList<>();

    private TextView tvTitle;
    private ImageView ivNull, ivBack;
    private View vAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clock);

        initView();
        setListener();
        initTable();
    }

    private void initTable() {
        queryTable();
        LinearLayoutManager LayoutManager = new LinearLayoutManager(MenuClock.this);
        recyclerView.setLayoutManager(LayoutManager);
        tableChoiceAdapter = new TableChoiceAdapter(MenuClock.this, list);
        recyclerView.setAdapter(tableChoiceAdapter);

        recyclerView.setNestedScrollingEnabled(false);
        LayoutManager.setStackFromEnd(true);
        tableChoiceAdapter.setOnItemClickListener(MyItemClickListener);
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
                BtnAddItem();
                break;
        }
    }

    /**
     * 页面跳转
     *
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
                    //页面跳转及传值
                    Intent intent = new Intent(MenuClock.this,ClockManage.class);
                    String name = list.get(position).getTableName();
                    String id = String.valueOf(list.get(position).getId());
                    intent.putExtra("name",name);
                    intent.putExtra("table_id",id);
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v, int position) {
            switch (v.getId()){
                case R.id.ivDelete:
                    int id = list.get(position).getId();
                    list.remove(position);
                    tableChoiceAdapter.notifyItemRemoved(position);
                    tableChoiceAdapter.delTable(MenuClock.this, id);
                    initTable();
                    ToastCustom.showMsgTrue(MenuClock.this,"删除成功~");
                    break;
            }
        }
    };

    /**
     * 查询时间表
     */
    private void queryTable(){
        list.clear();
        SQLiteOpenHelper helper = ScheduleSqlite.getInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from tableNames", null);
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                TimeTableData data = new TimeTableData(id, name);
                list.add(data);
            }
            cursor.close();
        }
        db.close();

        LinearLayoutManager layoutManager = new LinearLayoutManager(MenuClock.this);
        recyclerView.setLayoutManager(layoutManager);
        tableChoiceAdapter = new TableChoiceAdapter(this, list);
        recyclerView.setAdapter(tableChoiceAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager.setStackFromEnd(true);
    }



    private void BtnAddItem(){
        DialogCustom dialogCustom = new DialogCustom(MenuClock.this,R.layout.layout_dialog_tablename,0.7);
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
                    tableChoiceAdapter.insertTable(MenuClock.this ,name);
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