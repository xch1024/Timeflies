package com.example.timeflies.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.timeflies.R;

/**
 * 参考
 * https://blog.csdn.net/sakurakider/article/details/80735400
 * https://www.cnblogs.com/holyday/p/7284394.html
 * https://blog.csdn.net/xuyin1204/article/details/109351800
 * https://blog.csdn.net/weixin_40481076/article/details/106262508?spm=1001.2014.3001.5501
 * https://blog.csdn.net/weixin_43607099/article/details/105822321?spm=1001.2014.3001.5501
 *
 */
public class DialogCustom extends Dialog implements View.OnClickListener{

    private Context context;      // 上下文
    private int layoutResID;      // 布局文件id
    private TextView tv_leave, tv_stay;
    private TextView tv_creditClear;
    private Double percent;

    public DialogCustom(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public DialogCustom(@NonNull Context context, int layoutResID, Double percent) {
        super(context, R.style.dialog);
        this.context = context;
        this.layoutResID = layoutResID;
        this.percent = percent;
    }

    /**
     * 初始化控件
     *
     */
    public void initView(){
        tv_leave = findViewById(R.id.leave);
        tv_stay = findViewById(R.id.stay);
//        tv_creditClear = findViewById(R.id.credit_clear);
//        tv_creditCancel = findViewById(R.id.credit_cancel);
//        tv_creditConfirm = findViewById(R.id.credit_confirm);
        setListener();
    }

    /**
     * 设置监听
     *
     */
    public void setListener(){
        tv_leave.setOnClickListener(this);
        tv_stay.setOnClickListener(this);
//        tv_creditClear.setOnClickListener(this);
//        tv_creditCancel.setOnClickListener(this);
//        tv_creditConfirm.setOnClickListener(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResID);

        setSize(percent);
        initView();

    }

    /**
     * 设置dialog宽度为屏幕的百分比
     *
     * @param wid
     */
    public void setSize(Double wid){
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * wid);
        getWindow().setAttributes(p);
        //点击外部Dialog消失
        setCanceledOnTouchOutside(true);
    }





    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leave:
                ToastCustom.showMsgTrue(context.getApplicationContext(),"back离开");
                break;
            case R.id.stay:
                ToastCustom.showMsgTrue(context.getApplicationContext(),"back留下");
                break;
            case R.id.credit_clear:
                ToastCustom.showMsgTrue(context.getApplicationContext(),"credit留下");
                break;
        }
    }


}
