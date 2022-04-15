package com.example.timeflies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.model.ContentData;
import com.example.timeflies.utils.ToastCustom;
import com.example.timeflies.utils.DialogCustom;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.jianshu.com/p/971396467a62   RecyclerView系列之三：处理item的点击事件
 * https://www.jianshu.com/p/15d2ddc1eba7   如何正确的给RecycleView添加点击事件
 * https://blog.csdn.net/jk_szl/article/details/120970030   recycleView页面点击跳转
 *
 */
public class AddCourse extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private static RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private List<ContentData> list;
    private ImageView ivBack,ivSave, ivColor;
    private TextView tvColor, tvTitle;
    private View vColor, vAddCredit, vAddRemark, addItem;
    private DialogCustom dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        initView();
        initData();
        setListener();
        initContent();
    }

    /**
     * 初始化recycle，展示时间段的内容
     * https://blog.csdn.net/weixin_42229694/article/details/103513003?spm=1001.2014.3001.5501 最简单的RecyclerView Item动画全解析
     * https://www.jianshu.com/p/3acc395ae933 RecycleView4种定位滚动方式演示
     *
     */
    private void initContent(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddCourse.this);
        recyclerView.setLayoutManager(layoutManager);
        contentAdapter = new ContentAdapter(AddCourse.this, list);
        recyclerView.setAdapter(contentAdapter);
        //setHasFixedSize(true)方法使得RecyclerView能够固定自身size不受adapter变化的影响；
        //而setNestedScrollingeEnabled(false)方法则是进一步调用了RecyclerView内部NestedScrollingChildHelper对象的setNestedScrollingeEnabled(false)方法
        //进而，NestedScrollingChildHelper对象通过该方法关闭RecyclerView的嵌套滑动特性
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager.setStackFromEnd(true);
        contentAdapter.setOnItemClickListener(MyItemClickListener);
    }

    /**
     * 初始化控件
     *
     */
    private void initView(){
        recyclerView = findViewById(R.id.rv_content);
        ivBack = findViewById(R.id.ivBack);
        ivSave = findViewById(R.id.ivSave);
        vAddCredit = findViewById(R.id.addCredit);
        vAddRemark = findViewById(R.id.addRemark);
        addItem = findViewById(R.id.addItem);
        vColor = findViewById(R.id.color);
        ivColor = findViewById(R.id.colorMap);
        tvColor = findViewById(R.id.colorText);
        tvTitle = findViewById(R.id.include_add_head).findViewById(R.id.tvTitle);

        ivColor.setColorFilter(tvColor.getCurrentTextColor());//设置图片颜色与文字同步
        tvTitle.setText(R.string.course_title);//设置添加课程页面的标题文字
    }

    /**
     *
     *
     */
    private void initData(){
        list = new ArrayList<>();
        list.add(new ContentData("第几周", "周一 第几节", "授课老师(可不填)","上课地点(可不填)"));
    }


    /**
     * 设置控件的监听
     */
    private void setListener(){
        ivBack.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        vAddCredit.setOnClickListener(this);
        vAddRemark.setOnClickListener(this);
        addItem.setOnClickListener(this);
        vColor.setOnClickListener(this);
    }

    /**
     * item＋item里的控件点击监听事件
     *
     */
    private ContentAdapter.OnItemClickListener MyItemClickListener = new ContentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, ContentAdapter.ViewName viewName, int position) {
            switch (v.getId()){
                case R.id.delItem:
                    if(list.size()== 1){
                        ToastCustom.showMsgFalse(AddCourse.this, "至少要保留一个时间段！");
                    }else{
                        contentAdapter.delItem(position);
                        ToastCustom.showMsgTrue(AddCourse.this, "你点击了删除"+(position+1));
                    }
                    break;
                case R.id.rv_week:
                    ToastCustom.showMsgTrue(AddCourse.this, "周次按钮"+(position+1));
                    break;
                case R.id.rv_time:
                    ToastCustom.showMsgTrue(AddCourse.this, "时间按钮"+(position+1));
                    break;
                case R.id.rv_checkbox:
                    ToastCustom.showMsgTrue(AddCourse.this, "自定义时间按钮"+(position+1));
                    break;
                case R.id.rv_teacher:
                    ToastCustom.showMsgTrue(AddCourse.this, "授课老师"+(position+1));
                    BtnTeacher(position);
                    break;
                case R.id.rv_location:
                    ToastCustom.showMsgTrue(AddCourse.this, "上课地点"+(position+1));
                    BtnLocation(position);
                    break;
                default:
                    ToastCustom.showMsgTrue(AddCourse.this, "你点击了item按钮"+(position+1));
                    break;
            }
        }

        @Override
        public void onItemLongClick(View v) {

        }
    };

    //===================================addCourse界面上的按钮===================================
    /**
     * addCourse界面上的按钮监听
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回
            case R.id.ivBack:
                BtnBack();
                break;
            // 保存
            case R.id.ivSave:
                BtnSave();
                break;
            //更改颜色
            case R.id.color:
                ivColor.setColorFilter(Color.parseColor("#cc0000"));
                tvColor.setTextColor(Color.parseColor("#cc0000"));
                ToastCustom.showMsgTrue(this, "更改颜色成功");
                break;
            //添加学分
            case R.id.addCredit:
                BtnCredit();
                break;
            //添加备注
            case R.id.addRemark:
                BtnRemark();
                break;
            //添加时间段
            case R.id.addItem:
                contentAdapter.addItem(list.size());
                recyclerView.scrollToPosition(contentAdapter.getItemCount() - 1);
                scrollToEnd();
                break;
        }
    }

    /**
     * scrollTo滚动到指定位置
     * https://blog.csdn.net/neabea2016/article/details/101209787 NestScrollView滚动到指定位置
     *
     */
    private void scrollToEnd(){
        nestedScrollView = findViewById(R.id.nsv);
        nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN));
    }

    /**
     * 返回按钮
     *
     */
    private void BtnBack(){
        DialogCustom dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_back, 0.8);
        dialog.setCancelListener(view -> {
            Intent intent = new Intent(AddCourse.this,MainActivity.class);
            startActivity(intent);
        });
        dialog.setConfirmListener(view -> dialog.dismiss());
        dialog.show();
    }


    /**
     * 保存按钮
     *
     */
    private void BtnSave(){
        //获取课程名称
        EditText et1 = findViewById(R.id.course_name);
        String course_name = et1.getText().toString();
        //如果课程名称为空
        if(course_name.equals("")){
            ToastCustom.showMsgFalse(AddCourse.this, "请填写课程名称！");
        }else{
            ToastCustom.showMsgTrue(AddCourse.this, "保存成功");
            Intent intent = new Intent(AddCourse.this,MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 学分按钮
     *
     */
    private void BtnCredit(){
        dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_credit, 0.8);
        dialog.setTitle("学分").setEdit("0.0");
        dialog.setClearListener(view -> {
            ToastCustom.showMsgTrue(AddCourse.this, "学分按钮的清除");
            dialog.setEdit("0.0");
            dialog.dismiss();
        });
        dialog.setCreditCancelListener(view -> {
            dialog.dismiss();
            ToastCustom.showMsgTrue(AddCourse.this, "学分按钮的取消");
        });
        dialog.setCreditConfirmListener(view -> {
            dialog.dismiss();
            ToastCustom.showMsgTrue(AddCourse.this, "学分按钮的确定");
        });
        dialog.show();
    }

    /**
     * 备注按钮
     *
     */
    private void BtnRemark(){
        dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_credit, 0.8);
        dialog.setTitle("备注");
        dialog.setClearListener(view -> {
            dialog.dismiss();
            ToastCustom.showMsgTrue(AddCourse.this, "备注按钮的清除");
        });
        dialog.setCreditCancelListener(view -> {
            ToastCustom.showMsgTrue(AddCourse.this, "备注按钮的取消");
            dialog.dismiss();
        });
        dialog.setCreditConfirmListener(view -> {
            ToastCustom.showMsgTrue(AddCourse.this, "备注按钮的确定");
            dialog.dismiss();
        });
        dialog.show();
    }


    //===================================recycleView界面上的按钮===================================
    /**
     * 添加老师按钮
     *
     */
    private void BtnTeacher(int position){
        dialog = new DialogCustom(AddCourse.this,R.layout.layout_dialog_teacher,0.8);
        dialog.setTeacherTitle("授课老师");
        dialog.setTeacherConfirmListener(view -> {
            ToastCustom.showMsgFalse(getApplicationContext(), "授课老师的确定按钮"+(position+1));
            dialog.dismiss();
        });
        dialog.show();
    }

    /**
     * 添加地点按钮
     *
     */
    private void BtnLocation(int position) {
        dialog = new DialogCustom(AddCourse.this, R.layout.layout_dialog_teacher, 0.8);
        dialog.setTeacherTitle("上课地点");
        dialog.setTeacherConfirmListener(view -> {
            ToastCustom.showMsgFalse(getApplicationContext(), "上课地点的确定按钮" + (position + 1));
            dialog.dismiss();
        });
        dialog.show();
    }

}