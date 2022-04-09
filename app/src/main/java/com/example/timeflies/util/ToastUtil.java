package com.example.timeflies.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeflies.R;

public class ToastUtil extends Toast {

//    Toast单例
    private static ToastUtil toast;

    public ToastUtil(Context context) {
        super(context);
    }

//    构造centext
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

//    为了实现可以把当前Toast顶下去的需求，需要重写几个方法
    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {

        }
    }


//    初始化自定义的Toast
    public static void initToast(Context context, String msg){
        try{
            cancelToast();
            View view1 = LayoutInflater.from(context).inflate(R.layout.layout_toast,null);
            TextView textView = view1.findViewById(R.id.toast_tv);
            toast = new ToastUtil(context);
            toast.setView(view1);
            textView.setText(msg);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void showToast(Context context, String msg, int time) {
        // 初始化一个新的Toast对象
        initToast(context, msg);

        // 设置显示时长
        if (time == Toast.LENGTH_LONG) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void showMsg(Context context, String msg){
        showToast(context, msg,Toast.LENGTH_LONG);
    }

}
