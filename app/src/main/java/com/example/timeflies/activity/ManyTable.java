package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeflies.R;
import com.example.timeflies.adapter.TimetableAdapter;
import com.example.timeflies.model.ConfigData;
import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManyTable extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivBack, ivSave;
    private View addItem;
    private RecyclerView rv_many_table;
    private DialogCustom dialog;
    private SharedPreferences sp;

    private SqHelper sqHelper = new SqHelper(ManyTable.this);
    private List<ConfigData> configDataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_many_table);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
        initTable();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivSave = findViewById(R.id.ivSave);
        addItem = findViewById(R.id.addItem);

        rv_many_table = findViewById(R.id.rv_many_table);

        tvTitle.setText("多课表管理");
        ivSave.setVisibility(View.GONE);

        ivBack.setOnClickListener(view -> finish());
        addItem.setOnClickListener(view -> btnInsertTable());
    }

    private void initTable(){
        configDataList.clear();
        String id = sp.getString("termId","1");
        configDataList = sqHelper.queryConfig(Integer.parseInt(id));
        LinearLayoutManager manager = new LinearLayoutManager(ManyTable.this);
        rv_many_table.setLayoutManager(manager);
        TimetableAdapter timetableAdapter = new TimetableAdapter(configDataList, ManyTable.this);
        rv_many_table.setAdapter(timetableAdapter);
        timetableAdapter.setOnItemClickListener(MyclickListener);
        rv_many_table.setNestedScrollingEnabled(false);
    }

    //新建课表
    private void btnInsertTable() {
        dialog = new DialogCustom(ManyTable.this, R.layout.dialog_tablename,0.8);
        dialog.setTableTitle("新建课表");
        dialog.setTableNameCancelListener(view -> dialog.dismiss());
        dialog.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = dialog.getTableEdit();
                if(TextUtils.isEmpty(tableName)){
                    ToastCustom.showMsgWarning(ManyTable.this, "名称不能为空哦~");
                }else{
                    long insert = btnInsert(tableName);
                    if(insert > 0 ){
                        ToastCustom.showMsgTrue(ManyTable.this, "新建成功~");
                        initTable();
                    }else{
                        ToastCustom.showMsgFalse(ManyTable.this, "新建失败~");
                    }
                    dialog.dismiss();
                }
            }

            private long btnInsert(String tableName) {
                String timeId = sp.getString("timeId","1");
                long termStart = sp.getLong("termStart",new Date().getTime());
                String curWeek = sp.getString("curWeek","1");
                int secTime = sp.getInt("secTime",10);
                String termWeeks = sp.getString("termWeeks","20");
                ContentValues values = new ContentValues();
                values.put("className", tableName);
                values.put("timeId", timeId);
                values.put("termStart", termStart);
                values.put("curWeek", curWeek);
                values.put("secTime", secTime);
                values.put("termWeeks", termWeeks);
                long insert = sqHelper.insertConfig(null, values);
                return insert;
//                Log.d(TAG, "tableName: "+tableName);
//                Log.d(TAG, "timeId: "+timeId);
//                Log.d(TAG, "termStart: "+ ScheduleSupport.longToDate(termStart));
//                Log.d(TAG, "curWeek: "+curWeek);
//                Log.d(TAG, "secTime: "+secTime);
//                Log.d(TAG, "termWeeks: "+termWeeks);
            }
        });
        dialog.show();
    }

    /**
     * item＋item里的控件点击监听事件
     */
    private TimetableAdapter.OnItemClickListener MyclickListener = new TimetableAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, TimetableAdapter.ViewName viewName, int position) {
            //viewName可区分item及item内部控件
            switch (v.getId()){
                case R.id.many_delete:
                    //需要时再设置删除按钮可见
                    btnDel(position);
                    break;
                case R.id.many_edit:
                    //需要时再设置按钮可见
//                    ToastCustom.showMsgWarning(ManyTable.this, "编辑按钮"+(position+1));
                    break;
                default:
                    int term_id = configDataList.get(position).getId();
                    Intent intent = new Intent(ManyTable.this, MenuAdded.class);
                    intent.putExtra("termId",String.valueOf(term_id));
//                    Log.d("xch", "MenuAdded.class: "+term_id);
                    startActivity(intent);
//                    ToastCustom.showMsgWarning(ManyTable.this, "前往课表显示所有课程"+(position+1));
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v, TimetableAdapter.ViewName viewName, int position) {
            switch (v.getId()){
                default:
                    ToastCustom.showMsgWarning(ManyTable.this, "长按==="+configDataList.get(position).getClassName());
                    break;
             }
        }
    };


    private void btnDel(int position) {
        String name = configDataList.get(position).getClassName();
        dialog = new DialogCustom(ManyTable.this, R.layout.dialog_back, 0.8);
        dialog.setContent("确定要删除课表 ["+name+"] 吗？此操作不可撤销");
        dialog.setCancelListener(view -> dialog.dismiss());
        dialog.setConfirmListener(view -> {
            int del = sqHelper.delConfig(configDataList.get(position).getId());
            if(del > 0){
                ToastCustom.showMsgTrue(ManyTable.this, "删除成功");
                initTable();
            }else{
                ToastCustom.showMsgTrue(ManyTable.this, "删除失败");
            }
            dialog.dismiss();
        });
        dialog.show();
    }

}