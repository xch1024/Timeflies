package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.adapter.MySpinnerAdapter;
import com.example.timeflies.adapter.TableChoiceAdapter;
import com.example.timeflies.model.TimeData;
import com.example.timeflies.operater.ScheduleSupport;
import com.example.timeflies.sqlite.SqHelper;
import com.example.timeflies.utils.DialogCustom;
import com.example.timeflies.utils.ToastCustom;

import java.util.Date;
import java.util.List;

public class MenuClock extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String TAG = "xch";
    private TableChoiceAdapter tableChoiceAdapter;
    private RecyclerView recyclerView;
    private List<TimeData> timeDataList;

    private List<TimeData> list;
    private MySpinnerAdapter adapter;
    private SharedPreferences sp;
    private String timeId;

    private TextView tvTitle;
    private ImageView ivNull, ivBack;
    private Spinner spinner;
    private View vAddItem;
    private SqHelper sqHelper = new SqHelper(MenuClock.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clock);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initView();
        initSpinner();
        initTable();
        setListener();
    }

    private void initView(){
        recyclerView = findViewById(R.id.clockManage);
        tvTitle = findViewById(R.id.tvTitle);
        ivNull = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        spinner = findViewById(R.id.sp_time_id);
        vAddItem = findViewById(R.id.addItem);

        tvTitle.setText(R.string.menu_clock_view);
        ivNull.setVisibility(View.GONE);
    }

    private void initSpinner(){
        timeId = sp.getString("timeId", "0");
        list = sqHelper.queryTime();
        int i = queryPosById(list, Integer.parseInt(timeId));
        adapter = new MySpinnerAdapter(list,MenuClock.this);
        spinner.setAdapter(adapter);
        spinner.setSelection(i);
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
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * 找对应id的pos
     */
    private int queryPosById(List<TimeData> list, int timeId){
        int i =0;
        while(timeId != list.get(i).getId()){
            i++;
        }
//        Log.d(TAG, "sp中的timeId: "+list.get(i).getId());
//        Log.d(TAG, "timeId对应的位置i: "+i);
        return i;
    }

    /**
     * 监听spinner 列表选择框
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //sp中当前timeId
        timeId = sp.getString("timeId", "1");
        if(i != queryPosById(timeDataList, Integer.parseInt(timeId))){
            String id = String.valueOf(list.get(i).getId());
            String termId = sp.getString("termId", "1");
//                Log.d(TAG, "onItemSelected: list.get(i).getId()"+id);
            sp.edit().putString("timeId",id).apply();
//                Log.d(TAG, "sp.getString(\"termId\", \"1\"): "+term);
            sqHelper.updateConfig("timeId",id, termId);
//                Log.d("xch", "onItemSelected: "+sp.getString("timeId","1"));
            ToastCustom.showMsgTrue(MenuClock.this,"时间表切换成功~");
//                Log.d(TAG, "当前作息表id "+id);
            adapter.notifyDataSetChanged();
            spinner.setSelection(i);
        }else{

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                    ToastCustom.showMsgWarning(MenuClock.this,"长按确认删除哦~");
                    break;
                case R.id.ivEdit:
                    int id = timeDataList.get(position).getId();
                    String timeName = timeDataList.get(position).getTableName();
                    //页面跳转及传值
                    TimeData timeData = timeDataList.get(position);
                    Intent intent = new Intent(MenuClock.this, ClockManage.class);
                    intent.putExtra("timeData", timeData);
                    intent.putExtra("timeId", id);
                    intent.putExtra("timeName", timeName);
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v, int position) {
            switch (v.getId()){
                case R.id.ivDelete:
                    String spId = sp.getString("timeId","1");
                    int id = timeDataList.get(position).getId();
                    if(Integer.parseInt(spId) == id){
                        ToastCustom.showMsgWarning(MenuClock.this,"不能删除已选中的时间表哦~");
                    }else{
                        int delete = sqHelper.delTimes(id);
                        if(delete > 0 ){
                            ToastCustom.showMsgTrue(MenuClock.this,"删除成功~");
                            timeDataList.clear();
                            initTable();
                        }else{
                            ToastCustom.showMsgTrue(MenuClock.this,"删除失败~");
                        }
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
                    copy(name);
                    initTable();
                    initSpinner();
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
    private void copy(String name){
        String id = sp.getString("timeId","1");
        TimeData data = sqHelper.queryTimeData(id);
//        Log.d(TAG, "copy: "+data);
        TimeData newData = new TimeData();
        newData.setId(Integer.parseInt(id));
        newData.setTableName(name);
        newData.setTableTime(data.getTableTime());
//        Log.d(TAG, "newData: "+newData);
        sqHelper.insert(newData);
    }

}