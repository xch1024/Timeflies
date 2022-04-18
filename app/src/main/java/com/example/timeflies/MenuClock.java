package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.utils.ToastCustom;

public class MenuClock extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageView ivNull, ivBack;
    private ImageView ivDel, ivEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_clock);
        initView();
        setListener();
    }

    private void initView(){
        tvTitle = findViewById(R.id.tvTitle);
        ivNull = findViewById(R.id.ivSave);
        ivBack = findViewById(R.id.ivBack);
        ivDel = findViewById(R.id.ivDelete);
        ivEdit = findViewById(R.id.ivEdit);
        tvTitle.setText(R.string.menu_clock_view);
        ivNull.setVisibility(View.GONE);
        ivDel.setColorFilter(getResources().getColor(R.color.toast_solid_false));
        ivEdit.setColorFilter(getResources().getColor(R.color.button_add));
    }
    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDel.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                intentActivity(MainActivity.class);
                break;
            case R.id.ivDelete:
                ToastCustom.showMsgFalse(this,"删除按钮");
                break;
            case R.id.ivEdit:
                intentActivity(ClockManage.class);
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
}