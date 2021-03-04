package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.database.DatabaseUtils;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.databinding.BaseviewBinding;
import com.zhihuan.daoyi.cad.utils.MacUtils;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class DragBaseView extends RelativeLayout implements View.OnTouchListener {



    private int screenHeight;
    private int screenWidth;
    public boolean flage = true;

    protected Paint paint = new Paint();
    private Context mContext;
    private Canvas mCanvas;


    public interface Option{
        int Type();// 设置type
    }
    private Option option=null;
    private boolean isSelect =false;
    private int dragDirection;

    private int offset = 20;

    private RectF rect = new RectF();
    private RectF rectSrc = new RectF();
    private RectF rectEnd = new RectF();
    private int mode = 0;// 初始状态

    BaseviewBinding view;


    /**
     * 初始化获取屏幕宽高
     */
    protected void initScreenW_H() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    public DragBaseView(Context context,int type) {
        super(context);
        this.mContext= context;
        this.types=type;
        init();

    }

    public DragBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext= context;
        init();
    }

    public DragBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext= context;
        init();
    }

    View lt1,lb1,rt1,rb1;
    RelativeLayout box;
    RelativeLayout center;
    DragScaleRectView scaleRectView;
    DragScaleCircleView scaleCircleView;
    int types=0; // 0：矩形 1:圆形


    private void init(){
        view=BaseviewBinding.inflate(LayoutInflater.from(mContext));
        addView(view.getRoot());
        lt1=view.lt1;
        lb1=view.lb1;
        rt1=view.rt1;
        rb1=view.rb1;
        box=view.box;
        center=view.viewBox;
        lt1.setOnTouchListener(this);
        lb1.setOnTouchListener(this);
        rt1.setOnTouchListener(this);
        rb1.setOnTouchListener(this);
        if(types==0){
            scaleRectView =new DragScaleRectView(mContext);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1,-1);
            scaleRectView.setLayoutParams(params);
            scaleRectView.setClickable(true);
            scaleRectView.setId(R.id.centers);
            center.addView(scaleRectView);
            scaleRectView.setOnTouchListener(this);
        }else  if(types==1){
            scaleCircleView =new DragScaleCircleView(mContext);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(-1,-1);
            scaleCircleView.setLayoutParams(params);
            scaleCircleView.setClickable(true);
            scaleCircleView.setId(R.id.centers);
            center.addView(scaleCircleView);
            scaleCircleView.setOnTouchListener(this);
        }

        initScreenW_H();
    }

    // 设置选中状态
    void setSelect(boolean select){
        isSelect = select;
        if(isSelect){
//            view.box.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_react));
        }else{
//            view.box.setBackgroundDrawable(null);
        }
        if (select && option != null){
//            option.selected(this);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
    float left=0;
    float top=0;
    float right=0;
    float bottom=0;


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        left=getLeft();
        top=getTop();
        right=getRight();
        bottom=getBottom();
        // 处理拖动事件
        delDrag(v, event);
//        invalidate();

        return true;
    }


    /**
     * 处理拖动事件
     *
     * @param v
     * @param event
     * @param action
     */
    PointF LtPoint=new PointF();
    PointF LbPoint=new PointF();
    PointF RtPoint=new PointF();
    PointF RbPoint=new PointF();
    PointF StartP=new PointF();

    protected void delDrag(View v, MotionEvent event) {
        PointF endPoint=new PointF();


        endPoint=downMove(v,StartP,event);

    }

    private PointF downMove(View v,PointF p,MotionEvent event){
        PointF endPoint=new PointF();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                p.set(event.getRawX(),event.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                endPoint.set(event.getRawX(),event.getRawY());
                Log.e("daoyi","正在拖动");

                int l_or_r= (int) (endPoint.x-StartP.x);
                int t_or_b= (int) (endPoint.y-StartP.y);
                switch (v.getId()){
                    case R.id.lt1:
                        jumpWH(StartP,endPoint,1);
                        left(l_or_r);
                        Log.e("daoyi","left_top在拖动");
                        top(t_or_b);
                        break;
                    case R.id.lb1:
                        jumpWH(StartP,endPoint,4);
                        left(l_or_r);
                       bottom(t_or_b);
                        Log.e("daoyi","left_bottom在拖动");
                        break;
                    case R.id.rt1:
                        jumpWH(StartP,endPoint,2);
                        right(l_or_r);
                        top(t_or_b);

                        Log.e("daoyi","right_top在拖动");
                        break;
                    case R.id.rb1:
                        jumpWH(StartP,endPoint,3);
                        right(l_or_r);
                        bottom(t_or_b);

                        Log.e("daoyi","right_bottom在拖动");
                        break;
                    case R.id.centers:
                        left(l_or_r);
                        bottom(t_or_b);
                        right(l_or_r);
                        top(t_or_b);
                        break;
                }


                int w = (int) (right - left);
                int h = (int) (bottom - top);
                if(v.getId()!=R.id.centers){
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(w,h);
                    params.setMargins((int) left, (int) top,0, 0);
                    setLayoutParams(params);
                }else{
                    RelativeLayout.LayoutParams params= (LayoutParams) getLayoutParams();
                    params.setMargins((int) left, (int) top,0, 0);
                    setLayoutParams(params);
                }

                StartP.set(event.getRawX(),event.getRawY());


                break;
        }

        return endPoint;
    }
    private void jumpWH(PointF start,PointF end,int type){

//        this.layout(left, top, right, bottom);
       ;
    }


    /**
     * 触摸点为上边缘
     *
     * @param v
     * @param dy
     */
    private void top( int dy) {
        top += dy;
        if (top < -offset) {
            top = -offset;
        }
        if (bottom - top - 2 * offset < 200) {
            top = bottom - 2 * offset - 200;
        }
    }

    /**
     * 触摸点为下边缘
     *
     * @param v
     * @param dy
     */
    private void bottom( int dy) {
        bottom += dy;
        if (bottom > screenHeight + offset) {
            bottom = screenHeight + offset;
        }
        if (bottom - top - 2 * offset < 200) {
            bottom = 200 + top + 2 * offset;
        }
    }

    /**
     * 触摸点为右边缘
     *
     * @param v
     * @param dx
     */
    private void right(int dx) {
        right += dx;
        if (right > screenWidth + offset) {
            right = screenWidth + offset;
        }
        if (right - left - 2 * offset < 200) {
            right = left + 2 * offset + 200;
        }
    }

    /**
     * 触摸点为左边缘
     *
     * @param v
     * @param dx
     */
    private void left(int dx) {
        left += dx;
        if (left < -offset) {
            left = -offset;
        }
        if (right - left - 2 * offset < 200) {
            left = right - 2 * offset - 200;
        }
    }
    public void setOption(Option a){
        this.option= a;
    }



}


