package com.zhihuan.daoyi.cad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhihuan.daoyi.cad.base.Datas;
import com.zhihuan.daoyi.cad.databinding.ActivityMainBinding;
import com.zhihuan.daoyi.cad.datas.OvalData;
import com.zhihuan.daoyi.cad.utils.MacUtils;
import com.zhihuan.daoyi.cad.views.CanvasView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,GestureDetector.OnGestureListener {

    ActivityMainBinding activityMainBinding;
    GestureDetector gestureDetector;
    List<CanvasView> list_v = new ArrayList<>(10);
    List<Datas> list_d = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = activityMainBinding.getRoot();

        setContentView(root);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;

        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setIsLongpressEnabled(true);

    }

    private float startx, starty;

    OvalData ovalData =null;



    // 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        Log.i("tag00", "================================");
        Log.i("tag00", "onDown");


        return false;
    }

    /*
     * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
     * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
     *
     * 而onDown也是由一个MotionEventACTION_DOWN触发的，但是他没有任何限制，
     * 也就是说当用户点击的时候，首先MotionEventACTION_DOWN，onDown就会执行，
     * 如果在按下的瞬间没有松开或者是拖动的时候onShowPress就会执行，如果是按下的时间超过瞬间
     * （这块我也不太清楚瞬间的时间差是多少，一般情况下都会执行onShowPress），拖动了，就不执行onShowPress。
     */
    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.i("tag00", "onShowPress");
    }

    // 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
    // 轻击一下屏幕，立刻抬起来，才会有这个触发
    // 从名子也可以看出,一次单独的轻击抬起操作,当然,如果除了Down以外还有其它操作,那就不再算是Single操作了,所以这个事件 就不再响应
    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.i("tag00", "onSingleTapUp");
        return false;
    }

    // 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发大
    // v: x轴的滑动距离 v1: y
    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

        Log.i("tag00", "onScroll");
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.i("daoyi", "" + "daoyi_up");
        }


        return false;
    }

    // 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.i("tag00", "onLongPress");
    }

    // 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.i("tag00", "onFling");
        return false;
    }
    CanvasView view;
    RelativeLayout.LayoutParams layoutParams = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        view= new CanvasView(this);;

        ovalData = new OvalData(0,0);
        int count=0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("daoyi", "DOWN");
                startx = event.getX();
                starty = event.getY();
                layoutParams=new RelativeLayout.LayoutParams(10,10);
                layoutParams.setMargins((int) startx,(int) starty,0,0);
                view.setLayoutParams(layoutParams);
                view.setClickable(true);
                view.setOnTouchListener(this);
                list_v.add(view);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("daoyi", "startX_"+startx+"::startY_"+starty);
                Log.e("daoyi", "MOVE: x_"+event.getX()+";;Y_"+event.getY());
                float endx=event.getX();
                float endy=event.getY();

                if(view!=null){
                    int wx,hy; // 最重的宽高
                    if(endx>startx){
                        wx= (int) (endx-startx);
                    }else{
                        wx = (int) (startx-endx);
                    }
                    if(endy>starty){
                        hy= (int) (endy-starty);
                    }else{
                        hy = (int) (starty-endy);
                    }
                    layoutParams.width = wx;
                    layoutParams.height = hy;
                    layoutParams.setMargins((int) startx,(int) starty,0,0);
                    view.setLayoutParams(layoutParams);
                    activityMainBinding.maxBox.addView(view);
                }else{
                    Log.e("daoyi","view为空");
                }

                break;

            case MotionEvent.ACTION_UP:

                break;

        }

        return false;
    }

    int screenWidth;
    int screenHeight;
    int lastX;
    int lastY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx =(int)event.getRawX() - lastX;
                int dy =(int)event.getRawY() - lastY;

                int left = v.getLeft() + dx;
                int top = v.getTop() + dy;
                int right = v.getRight() + dx;
                int bottom = v.getBottom() + dy;
                if(left < 0){
                    left = 0;
                    right = left + v.getWidth();
                }
                if(right > screenWidth){
                    right = screenWidth;
                    left = right - v.getWidth();
                }
                if(top < 0){
                    top = 0;
                    bottom = top + v.getHeight();
                }
                if(bottom > screenHeight){
                    bottom = screenHeight;
                    top = bottom - v.getHeight();
                }
                v.layout(left, top, right, bottom);
                Log.i("@@@@@@", "position��" + left +", " + top + ", " + right + ", " + bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}