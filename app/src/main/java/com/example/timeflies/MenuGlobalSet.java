package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.utils.ToastCustom;

public class MenuGlobalSet extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTitle;
    private ImageView ivBack, ivDonate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_global_set);
        initView();
        setListener();

    }

    private void initView(){
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivDonate = findViewById(R.id.ivSave);
        tvTitle.setText(R.string.menu_globalset);
        ivDonate.setImageResource(R.drawable.donation);
    }

    private void setListener(){
        ivBack.setOnClickListener(this);
        ivDonate.setOnClickListener(this);
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
        }
    }

    /**
     * 页面跳转
     *
     *
     */
    private void intentActivity(Class<?> cls){
        Intent intent = new Intent(MenuGlobalSet.this, cls);
        startActivity(intent);
    }
}