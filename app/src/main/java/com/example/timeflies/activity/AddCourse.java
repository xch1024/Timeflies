package com.example.timeflies.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timeflies.R;
import com.example.timeflies.adapter.ContentAdapter;
import com.example.timeflies.model.CourseData;
import com.example.timeflies.sqlite.ScheduleSqlite;
import com.example.timeflies.utils.ColorPickerDialog;
import com.example.timeflies.utils.ToastCustom;
import com.example.timeflies.utils.DialogCustom;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * https://www.jianshu.com/p/971396467a62   RecyclerView系列之三：处理item的点击事件
 * https://www.jianshu.com/p/15d2ddc1eba7   如何正确的给RecycleView添加点击事件
 * https://blog.csdn.net/jk_szl/article/details/120970030   recycleView页面点击跳转
 *
 */
public class AddCourse extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "xch";

    private NestedScrollView nestedScrollView;
    private static RecyclerView recyclerView;
    private ContentAdapter contentAdapter;
    private ImageView ivBack,ivSave, ivColor;
    private View vAdd;

    private TextView tvTitle;
    private View view_Name, view_Color, View_Credit, View_Remark;
    private TextView course_color, credit;
    private EditText course_name, course_credit, course_remark;

    private DialogCustom dialogCustom;

    private ScheduleSqlite sqlite = new ScheduleSqlite(this);
    private CourseData courseData;

    private int mColor;
    private boolean mHexValueEnable = true;

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        setListener();
        initContent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseData = (CourseData) getIntent().getSerializableExtra("course");
        //切分当前课程的时间段
        handleCourse(AddCourse.this.courseData.toDetail());
    }

    //处理当前课程的数据
    private void handleCourse(List<CourseData> courseList){
        for(CourseData c : courseList){
            String courseTime = c.getCourseTime();
            Log.d(TAG, "handleCourse: "+c.getCourseName()+"======="+courseTime);
            if(TextUtils.isEmpty(courseTime)) continue;
            String[] courseArray = courseTime.split(";");
            for(int i = 0; i < courseArray.length; i++){
                CourseData clone = c.clone();
                String[] info = courseArray[i].split(":");
                clone.setStartWeek(Integer.parseInt(info[0]));
                clone.setEndWeek(Integer.parseInt(info[1]));
                clone.setWeekType(info[2]);
                Log.d(TAG, "clone: ==="+clone);
            }
        }
    }

    /**
     * 显示课次信息
     * 初始化recycle，展示时间段的内容
     * https://blog.csdn.net/weixin_42229694/article/details/103513003?spm=1001.2014.3001.5501 最简单的RecyclerView Item动画全解析
     * https://www.jianshu.com/p/3acc395ae933 RecycleView4种定位滚动方式演示
     *
     */
    private void initContent(){
        List<CourseData> list = courseData.toDetail();
        if(list == null) return;
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
        tvTitle = findViewById(R.id.include_add_head).findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        ivSave = findViewById(R.id.ivSave);
        vAdd = findViewById(R.id.addItem);

        view_Name = findViewById(R.id.view_Name);
        view_Color = findViewById(R.id.view_Color);
        View_Credit = findViewById(R.id.View_Credit);
        View_Remark = findViewById(R.id.View_Remark);
        course_name = findViewById(R.id.course_name);
        ivColor = findViewById(R.id.colorMap);
        course_color = findViewById(R.id.course_color);
        course_credit = findViewById(R.id.course_credit);
        credit = findViewById(R.id.credit);
        course_remark = findViewById(R.id.course_remark);

        //不可粘贴，长按不会弹出粘贴框
        course_name.setKeyListener(null);
        course_credit.setKeyListener(null);
        course_remark.setKeyListener(null);

        //显示课程信息
        tvTitle.setText(courseData.getCourseName());//设置添加课程页面的标题文字
        course_name.setText(courseData.getCourseName());
        if(TextUtils.isEmpty(courseData.getCourseCredit())){
        }else{
            credit.setVisibility(View.VISIBLE);
            course_credit.setText(courseData.getCourseCredit());
        }
        course_remark.setText(courseData.getCourseRemark());

        mColor = Color.parseColor(courseData.getCourseColor());
        ivColor.setColorFilter(Color.parseColor(courseData.getCourseColor()));//设置图片颜色与文字同步
        course_color.setTextColor(Color.parseColor(courseData.getCourseColor()));//设置图片颜色与文字同步
        course_color.setText(courseData.getCourseColor());
    }


    /**
     * 设置控件的监听
     */
    private void setListener(){
        ivBack.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        vAdd.setOnClickListener(this);
        view_Name.setOnClickListener(this);
        view_Color.setOnClickListener(this);
        View_Credit.setOnClickListener(this);
        View_Remark.setOnClickListener(this);
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
                    btnDelLesson(position);
                    break;
                case R.id.rv_week:
                    btnUpdateWeek(position);
                    ToastCustom.showMsgTrue(AddCourse.this, "R.id.rv_week"+(position+1));
                    break;
                case R.id.rv_time:
                    ToastCustom.showMsgTrue(AddCourse.this, "R.id.rv_time"+(position+1));
                    break;
                case R.id.rv_teacher:
                    ToastCustom.showMsgTrue(AddCourse.this, "R.id.rv_teacher"+(position+1));
                    break;
                case R.id.rv_location:
                    ToastCustom.showMsgTrue(AddCourse.this, "R.id.rv_location"+(position+1));
                    break;
                default:
                    ToastCustom.showMsgTrue(AddCourse.this, "你点击了item按钮"+(position+1));
                    break;
            }
        }

        //todo 长按功能未实现
        @Override
        public void onItemLongClick(View v, ContentAdapter.ViewName viewName, int position) {
            switch (v.getId()){
                default:
                    ToastCustom.showMsgTrue(AddCourse.this, "长按"+(position+1));
                    break;
            }
        }

        //删除时间段按钮
        private void btnDelLesson(int position) {
            dialogCustom = new DialogCustom(AddCourse.this, R.layout.dialog_back, 0.8);
            dialogCustom.setContent("确定要删除当前时间段吗？");
            dialogCustom.setCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogCustom.dismiss();
                }
            });
            dialogCustom.setConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delLesson(position);
                    dialogCustom.dismiss();
                }
            });
            dialogCustom.show();
        }

        private void delLesson(int position) {
            List<CourseData> list = courseData.toDetail();
            CourseData course = list.get(position);
            Iterator<CourseData> iterator = list.iterator();
            while (iterator.hasNext()) {
                CourseData c = iterator.next();
                if (c.getWeekType().equals(course.getWeekType()) &&
                        c.getStartWeek() == course.getStartWeek() &&
                        c.getEndWeek() == course.getEndWeek() &&
                        c.getDay() == course.getDay() &&
                        c.getSectionStart() == course.getSectionStart() &&
                        c.getSectionEnd() == course.getSectionEnd()&&
                        c.getTeacherName().equals(course.getTeacherName()) &&
                        c.getClassroom().equals(course.getClassroom()) ){
                    iterator.remove();
                    break;
                }
            }
            CourseData toCourse = CourseData.toCourse(list, AddCourse.this.courseData.getId());
            if(null != toCourse){
                int update = sqlite.update(toCourse);
                if (update > 0) {
                    String time = toCourse.getCourseTime();
                    AddCourse.this.courseData.setCourseTime(time);
                    ToastCustom.showMsgWarning(AddCourse.this,"删除成功");
                    initContent();
                    return;
                }
            }
            ToastCustom.showMsgWarning(AddCourse.this,"删除失败");

        }

        //修改周次
        private void btnUpdateWeek(int position) {
            dialogCustom = new DialogCustom(AddCourse.this, R.layout.dialog_update_week, 0.8);
            dialogCustom.setUpdateWeekCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogCustom.dismiss();
                }
            });
            dialogCustom.setUpdateWeekConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogCustom.dismiss();
                    updateWeek(position);
                    ToastCustom.showMsgTrue(AddCourse.this, "修改周次"+(position+1));
                }
            });
            dialogCustom.show();
        }

        private int updateWeek(int position) {
            //封装信息
            CourseData course = new CourseData();

            //单双周
            int rbId = dialogCustom.getRadio();
            if(R.id.radio_full == rbId){
                course.setWeekType("全周");
            }else if(R.id.radio_single == rbId){
                course.setWeekType("单周");
            }else if(R.id.radio_dual == rbId){
                course.setWeekType("双周");
            }

            //开始周次
            String weekStart = dialogCustom.getWeekStart();
            if(!TextUtils.isEmpty(weekStart)){
                course.setStartWeek(Integer.parseInt(weekStart));
            }else{
                ToastCustom.showMsgWarning(AddCourse.this,"开始周次不可为空");
                return 0;
            }

            //结束周次
            String weekEnd = dialogCustom.getWeekEnd();
            if(!TextUtils.isEmpty(weekEnd)){
                //周次格式
                if(Integer.parseInt(weekStart) > Integer.parseInt(weekEnd)){
                    ToastCustom.showMsgWarning(AddCourse.this,"周次时间段异常");
                    return 0;
                }else{
                    course.setEndWeek(Integer.parseInt(weekEnd));
                }
            }else{
                ToastCustom.showMsgWarning(AddCourse.this,"结束周次不可为空");
                return 0;
            }

            Log.d("xch", "updateWeek: "+course);
            //todo
            //向数据库更新数据
            return 1;
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
                updateCourse();
                break;
            case R.id.view_Name:
                viewName();
                break;
            //更改颜色
            case R.id.view_Color:
                viewColor();
                break;
            //添加学分
            case R.id.View_Credit:
                viewCredit();
                break;
            //添加备注
            case R.id.View_Remark:
                viewRemark();
                break;
            //添加时间段
            case R.id.addItem:
                BtnAddLesson();
                break;
        }
    }

    // 颜色选择
    private void viewColor() {
        new ColorPickerDialog.Builder(AddCourse.this, mColor)
        .setHexValueEnabled(mHexValueEnable)
        .setOnColorPickedListener(mListener)
        .build()
        .show();
    }

    private ColorPickerDialog.OnColorPickedListener mListener = new ColorPickerDialog.OnColorPickedListener() {
        @Override
        public void onColorPicked(int color) {
            mColor = color;
            ivColor.setColorFilter(mColor);
            course_color.setTextColor(mColor);
            course_color.setText("#"+com.example.timeflies.utils.Utils.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
    };


    //课程名称
    private void viewName(){
        dialogCustom = new DialogCustom(AddCourse.this,R.layout.dialog_tablename, 0.8);
        if(!TextUtils.isEmpty(course_name.getText().toString())){
            dialogCustom.setTableEdit(course_name.getText().toString());
        }
        dialogCustom.setTableTitle("课程名称");
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseName = dialogCustom.getTableEdit();
                if(!TextUtils.isEmpty(courseName)){
                    course_name.setText(courseName);
                    dialogCustom.dismiss();
                }else{
                    ToastCustom.showMsgWarning(AddCourse.this,"课表名称不能为空哦");
                }
            }
        });
        dialogCustom.show();
    }

    //更改学分
    private void viewCredit(){
        dialogCustom = new DialogCustom(this, R.layout.dialog_tablename, 0.8);
        if(!TextUtils.isEmpty(course_credit.getText())){
            dialogCustom.setTableEdit(course_credit.getText().toString());
        }else{
            dialogCustom.setTableEdit("0.0");
        }
        dialogCustom.setTableTitle("学分").setInputType(InputType.TYPE_CLASS_PHONE);
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCredit = dialogCustom.getTableEdit();
                if(courseCredit.equals("0") || courseCredit.equals("0.0") || TextUtils.isEmpty(courseCredit)){
                }else{
                    course_credit.setText(courseCredit);
                    credit.setVisibility(View.VISIBLE);
                }
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }


    //更改备注
    private void viewRemark(){
        dialogCustom = new DialogCustom(AddCourse.this, R.layout.dialog_tablename, 0.8);
        if(!TextUtils.isEmpty(course_remark.getText())){
            dialogCustom.setTableEdit(course_remark.getText().toString());
        }
        dialogCustom.setTableTitle("备注");
        dialogCustom.setTableNameCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setTableNameConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseRemark = dialogCustom.getTableEdit();
                    course_remark.setText(courseRemark);
                dialogCustom.dismiss();
            }
        });
        dialogCustom.show();
    }

    //添加时间段对话框
    private void BtnAddLesson() {
        DialogCustom dialogCustom = new DialogCustom(AddCourse.this, R.layout.dialog_add_lesson, 0.9);
        dialogCustom.setCanceledOnTouchOutside(false);
        dialogCustom.setAddCourseCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCustom.dismiss();
            }
        });
        dialogCustom.setAddCourseConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int succeed = addLesson(dialogCustom);
                if(succeed > 0 ){
                    ToastCustom.showMsgTrue(AddCourse.this,"保存成功");
                    scrollToEnd();
                    dialogCustom.dismiss();
                }
            }
        });
        dialogCustom.show();
    }

    //添加时间段
    private int addLesson(DialogCustom dialogCustom) {
        //封装信息
        CourseData course = new CourseData();

        //单双周
        int rbId = dialogCustom.getRadio();
        if(R.id.radio_full == rbId){
            course.setWeekType("全周");
        }else if(R.id.radio_single == rbId){
            course.setWeekType("单周");
        }else if(R.id.radio_dual == rbId){
            course.setWeekType("双周");
        }

        //开始周次
        String weekStart = dialogCustom.getWeekStart();
        if(!TextUtils.isEmpty(weekStart)){
            course.setStartWeek(Integer.parseInt(weekStart));
        }else{
            ToastCustom.showMsgWarning(AddCourse.this,"开始周次不可为空");
            return 0;
        }

        //结束周次
        String weekEnd = dialogCustom.getWeekEnd();
        if(!TextUtils.isEmpty(weekEnd)){
            //周次格式
            if(Integer.parseInt(weekStart) > Integer.parseInt(weekEnd)){
                ToastCustom.showMsgWarning(AddCourse.this,"周次时间段异常");
                return 0;
            }else{
                course.setEndWeek(Integer.parseInt(weekEnd));
            }
        }else{
            ToastCustom.showMsgWarning(AddCourse.this,"结束周次不可为空");
            return 0;
        }

        //星期
        String weekDay = dialogCustom.getWeekDay();
        if(!TextUtils.isEmpty(weekDay)){
            if(7 < Integer.parseInt(weekDay) || Integer.parseInt(weekDay) < 1 ){
                ToastCustom.showMsgWarning(AddCourse.this,"星期超出范围");
                return 0;
            }else{
                course.setDay(Integer.parseInt(weekDay));
            }
        }else{
            ToastCustom.showMsgWarning(AddCourse.this,"星期不可为空");
            return 0;
        }

        //开始节次
        String stepStart = dialogCustom.getStepStart();
        if(!TextUtils.isEmpty(stepStart)){
            course.setSectionStart(Integer.parseInt(stepStart));
        }else{
            ToastCustom.showMsgWarning(AddCourse.this,"开始节次不可为空");
            return 0;
        }

        //结束节次
        String stepEnd = dialogCustom.getStepEnd();
        if(!TextUtils.isEmpty(stepEnd)){
            //节次格式
            if(Integer.parseInt(stepEnd) < Integer.parseInt(stepStart)){
                ToastCustom.showMsgWarning(AddCourse.this,"节次时间段异常");
                return 0;
            }else{
                course.setSectionEnd(Integer.parseInt(stepEnd));
            }
        }else{
            ToastCustom.showMsgWarning(AddCourse.this,"结束节次不可为空");
            return 0;
        }

        //授课老师
        String teacherName = dialogCustom.getTeacherName();
        if(!TextUtils.isEmpty(teacherName)){
            course.setTeacherName(teacherName);
        }else{
            course.setTeacherName("null");
        }

        //上课教室
        String classRoom = dialogCustom.getClassRoom();
        if(!TextUtils.isEmpty(classRoom)){
            course.setClassroom(classRoom);
        }else{
            course.setClassroom("null");
        }

        //组装上课时间
        String courseTime = AddCourse.this.courseData.getCourseTime();
        int id = AddCourse.this.courseData.getId();
        if(TextUtils.isEmpty(courseTime)){
            course.setCourseTime(course.toTime());
        }else{
            course.setCourseTime(courseTime + ";" + course.toTime());
        }
        course.setId(id);

        //修改
        int update = sqlite.update(course);
        if( update > 0){
            AddCourse.this.courseData.setCourseTime(course.getCourseTime());
            initContent();
            ToastCustom.showMsgWarning(AddCourse.this,"添加成功");
            return update;
        }else{
            ToastCustom.showMsgWarning(AddCourse.this,"添加失败");
            return 0;
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
        DialogCustom dialog = new DialogCustom(AddCourse.this, R.layout.dialog_back, 0.8);
        dialog.setLeave("离开");
        dialog.setStay("留下");
        dialog.setCancelListener(view -> {
            this.finish();
        });
        dialog.setConfirmListener(view -> dialog.dismiss());
        dialog.show();
    }


    /**
     * 保存按钮
     *
     */
    private void updateCourse(){
        String courseName = course_name.getText().toString();
        String courseColor = course_color.getText().toString();
        String courseCredit = course_credit.getText().toString();
        String courseRemark = course_remark.getText().toString();

        //封装信息
        CourseData course = new CourseData();
        course.setId(this.courseData.getId());

        course.setCourseName(courseName);
        course.setCourseColor(courseColor);
        course.setCourseCredit(courseCredit);
        course.setCourseRemark(courseRemark);

        if(AddCourse.this.courseData.equals(course)){
            ToastCustom.showMsgWarning(AddCourse.this, "您尚未修改课程信息\n无需保存！");
            ToastCustom.showMsgWarning(AddCourse.this,course.getCourseName()+course.getCourseColor()+course.getCourseCredit()+course.getCourseRemark());
        }else{
            int update = sqlite.update(course);
            if (update > 0) {
                tvTitle.setText(courseName);
                ToastCustom.showMsgTrue(AddCourse.this, "修改课程成功");
                ToastCustom.showMsgTrue(AddCourse.this,course.getCourseName()+course.getCourseColor()+course.getCourseCredit()+course.getCourseRemark());
            }else{
                ToastCustom.showMsgFalse(AddCourse.this, "修改失败");
                ToastCustom.showMsgFalse(AddCourse.this,course.getCourseName()+course.getCourseColor()+course.getCourseCredit()+course.getCourseRemark());
            }
        }

    }


}