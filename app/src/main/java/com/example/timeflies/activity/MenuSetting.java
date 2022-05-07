package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.utils.ToastCustom;

public class MenuSetting extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTitle;
    private ImageView ivDonate, ivBack;

    private View table_data, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_setting);

        initView();
        setListener();
    }
    /**
     * https://blog.csdn.net/qq_20451879/article/details/54745068 Include（复用layout）的使用方式
     *
     */
    private void initView(){
        tvTitle = findViewById(R.id.tvTitle);
        ivDonate = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        tvTitle.setText(R.string.menu_setting);
        ivDonate.setImageResource(R.drawable.donation);

        table_data = findViewById(R.id.table_data);
        about = findViewById(R.id.about);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);
        table_data.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MainActivity.class);
                break;
            case R.id.ivSave:
                ToastCustom.showMsgTrue(this, "捐赠");
                break;
            case R.id.table_data:
                intentActivity(ScheduleData.class);
                break;
            case R.id.about:
                intentActivity(MenuAbout.class);
                break;
        }
    }

    /**
     * 页面跳转
     *
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MenuSetting.this, cls);
        startActivity(intent);
    }
}