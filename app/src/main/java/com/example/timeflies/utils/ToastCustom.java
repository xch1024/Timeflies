package com.example.timeflies.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timeflies.R;

/**
 *
 *
 */
public class ToastCustom extends Toast {

    private static ToastCustom toast;

    public ToastCustom(Context context) {
        super(context);
    }

    /**
     * 构造centext
     *
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * 为了实现可以把当前Toast顶下去的需求，需要重写几个方法
     *
     */
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


    /**
     * 初始化自定义的Toast
     *
     */
    public static void showMsgFalse(Context context, String msg){
        try{
            cancelToast();
            //引用布局文件
            View view1 = LayoutInflater.from(context).inflate(R.layout.toast_false,null);
            TextView textView = view1.findViewById(R.id.toast_tvFalse);

            toast = new ToastCustom(context);
            toast.setView(view1);
            textView.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showMsgTrue(Context context, String msg){
        try{
            cancelToast();
            //引用布局文件
            View view1 = LayoutInflater.from(context).inflate(R.layout.toast_true,null);
            TextView textView = view1.findViewById(R.id.toast_tvTrue);

            toast = new ToastCustom(context);
            toast.setView(view1);
            textView.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showMsgWarning(Context context, String msg){
        try{
            cancelToast();
            //引用布局文件
            View view1 = LayoutInflater.from(context).inflate(R.layout.toast_warning,null);
            TextView textView = view1.findViewById(R.id.toast_tvTrue);

            toast = new ToastCustom(context);
            toast.setView(view1);
            textView.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
