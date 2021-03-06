package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.databinding.BaseviewBinding;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class DragBaseView3 extends RelativeLayout implements View.OnTouchListener {


    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0;
    private int screenHeight;
    private int screenWidth;
    public int flage = 110;

    protected Paint paint = new Paint();
    private Context mContext;
    private Canvas mCanvas;


    public interface Option{
        int Type();// 设置type
        void select(DragBaseView3 view);
    }
    private Option option=null;
    public boolean isSelect =false;
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

    public DragBaseView3(Context context, int type, int flag) {
        super(context);
        this.mContext= context;
        this.types=type;
        this.flage=flag;
        init();

    }

    public DragBaseView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext= context;
        init();
    }

    public DragBaseView3(Context context, AttributeSet attrs, int defStyleAttr) {
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
        view.close.setOnTouchListener(this);
        if(types==0){
            scaleRectView =new DragScaleRectView(mContext);
            LayoutParams params=new LayoutParams(-1,-1);
            scaleRectView.setLayoutParams(params);
            scaleRectView.setClickable(true);
            scaleRectView.setId(R.id.centers);
            center.addView(scaleRectView);
            scaleRectView.setOnTouchListener(this);
        }else  if(types==1){
            scaleCircleView =new DragScaleCircleView(mContext);
            LayoutParams params=new LayoutParams(-1,-1);
            scaleCircleView.setLayoutParams(params);
            scaleCircleView.setClickable(true);
            scaleCircleView.setId(R.id.centers);
            center.addView(scaleCircleView);
            scaleCircleView.setOnTouchListener(this);
        }

        initScreenW_H();
    }

    // 关闭
    public void close(boolean select){
        isSelect = select;

        if (!select&&option != null){
            option.select(this);
        }
        show();
    }

    // 设置选中状态
    public void setSelect(boolean select){
        isSelect = select;

        if (option != null){
            option.select(this);
        }
        show();

    }
    public void show(){

        if(isSelect){
            lt1.setOnTouchListener(this);
            lb1.setOnTouchListener(this);
            rt1.setOnTouchListener(this);
            rb1.setOnTouchListener(this);
            view.viewBox.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.shape_react));
            view.lt1.setVisibility(VISIBLE);
            view.lb1.setVisibility(VISIBLE);
            view.rt1.setVisibility(VISIBLE);
            view.rb1.setVisibility(VISIBLE);
            if(types==0){
                scaleRectView.setOnTouchListener(this);
            }else  if(types==1){
                scaleCircleView.setOnTouchListener(this);
            }
        }else{
            view.viewBox.setBackgroundDrawable(null);
            view.lt1.setVisibility(GONE);
            view.lb1.setVisibility(GONE);
            view.rt1.setVisibility(GONE);
            view.rb1.setVisibility(GONE);
        }
    }


    OnClickListener clickListener= new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.centers:
                    Log.e("daoyi","点击被相应");
                    setSelect(!isSelect);
                    break;

                case R.id.close:
                    setSelect(false);
                    Log.e("daoyi","close");
                    break;
            }

        }
    };


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
        dragDirection = getDirection(v,(int) event.getX()+getLeft(),(int) event.getY()+getTop());
        // 处理拖动事件
        delDrag(v, event);
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
                p.set(event.getX()+getLeft(),event.getY()+getTop());
                break;
            case MotionEvent.ACTION_MOVE:
                endPoint.set(event.getX()+getLeft(),event.getY()+getTop());
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
                        Log.e("daoyi","center在拖动");
                        break;

                }
                int w = (int) (right - left);
                int h = (int) (bottom - top);
                if(v.getId()!=R.id.centers){
                    LayoutParams params=new LayoutParams(w,h);
                    params.setMargins((int) left, (int) top,0, 0);
                    setLayoutParams(params);
                }else{
                    LayoutParams params= (LayoutParams) getLayoutParams();
                    params.setMargins((int) left, (int) top,0, 0);
                    setLayoutParams(params);
                }
                StartP.set(event.getX()+getLeft(),event.getY()+getTop());
                break;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX()+getLeft();
                float y2 = event.getY()+getTop();
                float distance = (float) Math.sqrt(Math.abs(p.x-x2)*Math.abs(p.x-x2)+Math.abs(p.y-y2)*Math.abs(p.y-y2));//两点之间的距离
                switch (v.getId()){
                    case R.id.centers:
                        Log.i("i", "x1 - x2>>>>>>"+ distance);
                        if (distance < 15) { // 距离较小，当作click事件来处理
                            setSelect(!isSelect);
                        }
                        break;
                }
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

    public int getFlage() {
        return flage;
    }


    /**
     * 获取触摸点flag
     *
     * @param v
     * @param x
     * @param y
     * @return
     */
    protected int getDirection(View v, int x, int y) {
        Rect rectLt=new Rect();
        Rect rectRt=new Rect();
        Rect rectRb=new Rect();
        Rect rectLb=new Rect();
        lt1.getLocalVisibleRect(rectLt);
        rt1.getLocalVisibleRect(rectRt);
        rb1.getLocalVisibleRect(rectRb);
        lb1.getLocalVisibleRect(rectLb);
        if(rectLt.contains(x,y)){
            return LEFT_TOP;
        }
        if(rectRt.contains(x,y)){
            return RIGHT_TOP;
        }
        if(rectRb.contains(x,y)){
            return RIGHT_BOTTOM;
        }
        if(rectLb.contains(x,y)){
            return LEFT_BOTTOM;
        }
        return  0;
    }



}


