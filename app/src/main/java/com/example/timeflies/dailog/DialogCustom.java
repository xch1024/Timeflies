package com.example.timeflies.dailog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.timeflies.R;


public class DialogCustom extends Dialog implements View.OnClickListener {

    //在构造方法里提前加载了样式
    private Context context;//上下文
    private int layoutResID;//布局文件id
    private TextView tvTitle, tvEdit;
    private String title, hint;
    private int[] listenedItem;//监听的控件id
    private OnCenterItemClickListener listener;

    //传标题和提示文字
    public DialogCustom(Context context, int layoutResID, int[] listenedItem, String title, String hint){
        super(context, R.style.dialog);//加载dialog的样式,否则边框会出现问题
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItem = listenedItem;
        this.title = title;
        this.hint = hint;
    }

    //不传标题和提示文字
    public DialogCustom(Context context, int layoutResID, int[] listenedItem){
        super(context, R.style.dialog);//加载dialog的样式,否则边框会出现问题
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItem = listenedItem;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        //设置dialog的宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int) (size.x * 0.8);// 设置dialog宽度为屏幕的0.8
        getWindow().setAttributes(p);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失


        //遍历控件id添加点击注册
        for(int id:listenedItem){
            findViewById(id).setOnClickListener(this);
        }

        //设置title标题
        tvTitle = findViewById(R.id.credit_title);
        if(!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }

        //设置hint提示
        tvEdit = findViewById(R.id.course_credit);
        if(!TextUtils.isEmpty(hint)){
            tvEdit.setHint(hint);
        }

    }


    //设置title标题
    public DialogCustom setTitle(String title) {
        this.title = title;
        return this;
    }

    //设置hint提示
    public DialogCustom setHint(String hint) {
        this.hint = hint;
        return this;
    }


    public interface OnCenterItemClickListener {
        void OnCenterItemClick(DialogCustom dialog, View view);
    }
    //很明显我们要在这里面写个接口，然后添加一个方法
    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        dismiss();//只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
        listener.OnCenterItemClick(this,view);
    }
}
