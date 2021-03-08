package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.customview.widget.ViewDragHelper;

import com.nineoldandroids.view.ViewHelper;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: "daoyi"
 * @date:
 * @params: "说明...."
 */
public class PowerFullLayout extends FrameLayout {
    // 屏幕宽高
    private int screenHeight;
    private int screenWidth;
    private ViewDragHelper mDragHelper;
    private long lastMultiTouchTime;// 记录多点触控缩放后的时间
    private int originalWidth;// view宽度
    private int originalHeight;// view高度
    private ScaleGestureDetector mScaleGestureDetector = null;
    // private View view;
    private int downX;// 手指按下的x坐标值
    private int downY;// 手指按下的y坐标值
    private int left;// view的左坐标值
    private int top;// view的上坐标值
    private int right;// view的右坐标值
    private int bottom;// view的下坐标值
    private int newHeight;
    private int newWidth;

    public  boolean isScale = false;
    private float scale;
    private float preScale = 1;// 默认前一次缩放比例为1





    /**
     * 记录是拖拉照片模式还是放大缩小照片模式
     */
    private int mode = 0;// 初始状态
    /**
     * 拖拉照片模式
     */
    private static final int MODE_DRAG = 1;
    /**
     * 放大缩小照片模式
     */
    private static final int MODE_ZOOM = 2;

    /**
     * 用于记录开始时候的坐标位置
     */
    private PointF startPoint = new PointF();

    /**
     * 两个手指的开始距离
     */
    private float startDis;
    /**
     * 两个手指的中间点
     */
    private PointF midPoint;
    private Context mContext;


    // 组件类型
    DragScaleCircleView scaleCircleView;
    DragScaleRectView scaleRectView;
    DragBaseView dragBaseView;

    List<DragScaleCircleView> listCircle = new ArrayList<>();
    List<DragScaleRectView> listRect = new ArrayList<>();
    List<DragBaseView> listBase = new ArrayList<>();
    List<DragBaseView> listBaseEnd = new ArrayList<>();
    List<FrameLayout.LayoutParams> listParams = new ArrayList<>();
    FrameLayout.LayoutParams params;

    boolean first = true;

    boolean touchChild = false; // 子类消费事件
    boolean touchParent = false; // 父类消费事件
    boolean touchDraw = false;// 绘制事件
    boolean touch = true; // 父类消费事件

    int childFlage = 0;// 子节点标识
    private int mWidth = 2000;
    private int mHeight = 2000;

    private float scaleW,scaleH,scaleA;

    private Matrix matrix=new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();
    RectF rect=new RectF();




    public PowerFullLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        init(context);
    }

    public PowerFullLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        this.mContext=context;
        init(context);
    }

    public PowerFullLayout(Context context) {
        this(context,null);
        this.mContext=context;
        init(context);
    }

    private void init(Context context) {
        mDragHelper = ViewDragHelper.create(this, callback);
        mScaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleGestureListener());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);

        return isScale;
    }


    private boolean needToHandle=true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointerCount = event.getPointerCount(); // 获得多少点
        if (pointerCount > 1) {// 多点触控，
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    needToHandle=true;
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_POINTER_2_UP://第二个手指抬起的时候
                    needToHandle=true;
                    break;

                default:
                    break;
            }
            return mScaleGestureDetector.onTouchEvent(event);//让mScaleGestureDetector处理触摸事件
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastMultiTouchTime > 200&&needToHandle) {
//                  多点触控全部手指抬起后要等待200毫秒才能执行单指触控的操作，避免多点触控后出现颤抖的情况
                try {
                    mDragHelper.processTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s(event);
            }
//            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean s(MotionEvent event) {
        // 优先级计算
        judgment(event);
        if (touch) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    startPoint.set(event.getRawX(), event.getRawY());
                    if (mListener != null && mListener.DrawingOption()) {
                        Log.e("daoyi", "绘制模式");
                        Log.e("daoyi", "downx:"+getXm(event));
                        Log.e("daoyi", "downy:"+getYm(event));
                        DrawingAdd(event, mListener.DrawingType());
                    }
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    if (mListener != null && mListener.DrawingOption()) {
                        Log.e("daoyi", "绘制模式");
                        Log.e("daoyi", "绘制类型：" + mListener.DrawingType());
                        if (mListener.DrawingType() == 0) {
                            DrawingMove(event, dragBaseView);
                        }
                        if (mListener.DrawingType() == 1) {
                            DrawingMove(event, dragBaseView);
                        }

                    } else {
                        MatrixF(event);

                        invalidate();
                    }

                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    if (mListener != null && mListener.DrawingOption()) {
                        mListener.DrawingCloseCall(false);
                    }
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    if (event.getPointerCount() == 2) {
                        startDis = distance(event);
                    }
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                    }
                    break;
            }
        }


        return touch;
    }



    // 矩阵事件
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void MatrixF(MotionEvent event) {
        float scaleB=0f;
        // 拖拉图片
        if (mode == MODE_DRAG) {
            float dx =startPoint.x- event.getRawX() ; // 得到x轴的移动距离
            float dy =startPoint.y- event.getRawY() ; // 得到x轴的移动距离
            // 在没有移动之前的位置上进行移动
            scrollBy((int) dx,(int) dy);
            startPoint.set(event.getRawX(),event.getRawY());
        }
        // 放大缩小图片
        else if (mode == MODE_ZOOM) {
            float endDis = distance(event);// 结束距离
            float a = 0;
            if (endDis > 30f) {
//                scaleA= (startDis-endDis)/(startDis+endDis);
                scaleA= (endDis-startDis);
                scaleB= (float) ((startDis+scaleA)/getWidth());
//                ViewGroup.LayoutParams layoutParams = getLayoutParams();
//                scaleW=(getWidth()-(int) (getWidth()*scaleA*0.1))/getWidth();
//                scaleH=(getHeight()-(int) (getHeight()*scaleA*0.1))/getHeight();

//                RectF rectdst= new RectF();
//                layoutParams.width = getWidth()-(int) (getWidth()*scaleA*0.1);
//                layoutParams.height = getHeight()-(int) (getHeight()*scaleA*0.1);
//                mWidth = getWidth()-(int) (getWidth()*scaleA*0.1);
//                mHeight =  getHeight()-(int) (getHeight()*scaleA*0.1);
//                matrix.set(currentMatrix);
            }
            RectF rectdst= new RectF();
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
//            matrix.postScale(scaleB,scaleB,midPoint.x, midPoint.y);
//            matrix.mapRect(rectdst,rect);
//            layoutParams.width = (int) rectdst.width();
//            layoutParams.height = (int) rectdst.height();
//            rect.right = rectdst.width();
//            rect.bottom = rectdst.height();


            scaleW=(getWidth()+(int) (getWidth()*scaleA*0.1))/getWidth();
            scaleH=(getHeight()+(int) (getHeight()*scaleA*0.1))/getHeight();
            if(scaleW==0){
                scaleW+=0.1;
            }
            if(scaleH==0){
                scaleH+=0.1;
            }

//            RectF rectdst= new RectF();
            layoutParams.width = getWidth()-(int) (getWidth()*scaleW);
            layoutParams.height = getHeight()-(int) (getHeight()*scaleH);
            mWidth = getWidth()-(int) (getWidth()*scaleA*0.1);
            mHeight =  getHeight()-(int) (getHeight()*scaleA*0.1);

//            setLayoutParams(layoutParams);
//            allZoom();
//            matrix.reset();
//                currentMatrix.set(matrix);
            Log.e("daoyi_1",""+endDis);
            Log.e("daoyi_2",""+startDis);
            Log.e("daoyi_3",""+rect);
            Log.e("daoyi_4",""+rectdst);
        }

    }



    // 绘制移动
    private void DrawingMove(MotionEvent event, View v) {
        float endx = event.getRawX();
        float endy = event.getRawY();

        if (v != null) {
            int wx, hy; // 最重的宽高
            if (endx > startPoint.x) {
                wx = (int) (endx - startPoint.x);
            } else {
                wx = (int) (startPoint.x - endx);
            }
            if (endy > startPoint.y) {
                hy = (int) (endy - startPoint.y);
            } else {
                hy = (int) (startPoint.y - endy);
            }
            params.width = wx;
            params.height = hy;
            v.setLayoutParams(params);

        } else {
            Log.e("daoyi", "view为空");
        }

    }


    // 事件优先级判断
    public void judgment(MotionEvent event) {
//        isShowChild(event);
        Log.e("daoyi", "绘制事件：" + touchDraw);
        Log.e("daoyi", "子事件：" + touchChild);
        Log.e("daoyi", "默认：" + touch);
        if (mListener != null) {
            touchDraw = mListener.DrawingOption();
        }
        if (touchDraw) {
            touch = touchDraw;
//            clearSelect();
        } else if (touchChild) {
            touch = !touchChild;
        } else  {
            touch = true;
        }
        //&&!isShowChild(event)
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        /**
         * 用于判断是否捕获当前child的触摸事件
         *
         * @param child
         *            当前触摸的子view
         * @param pointerId
         * @return true就捕获并解析；false不捕获
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (preScale > 1){
                return true;
            }
            return false;
        }

        /**
         * 控制水平方向上的位置
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            if (left < (screenWidth - screenWidth * preScale) / 2)
                left = (int) (screenWidth - screenWidth * preScale) / 2;// 限制mainView可向左移动到的位置
            if (left > (screenWidth * preScale - screenWidth) / 2)
                left = (int) (screenWidth * preScale - screenWidth) / 2;// 限制mainView可向右移动到的位置
            return left;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {

            if (top < (screenHeight - screenHeight * preScale) / 2) {
                top = (int) (screenHeight - screenHeight * preScale) / 2;// 限制mainView可向上移动到的位置
            }
            if (top > (screenHeight * preScale - screenHeight) / 2) {
                top = (int) (screenHeight * preScale - screenHeight) / 2;// 限制mainView可向上移动到的位置
            }
            return top;
        }

    };

    public class ScaleGestureListener implements
            ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            float previousSpan = detector.getPreviousSpan();// 前一次双指间距
            float currentSpan = detector.getCurrentSpan();// 本次双指间距
            if (currentSpan < previousSpan) {
                // 缩小
                scale = preScale - (previousSpan - currentSpan) / 1000;
            } else {
                // 放大
                scale = preScale + (currentSpan - previousSpan) / 1000;
            }
            // 缩放view
            if (scale > 0.5) {
                ViewHelper.setScaleX(PowerFullLayout.this, scale);// x方向上缩放
                ViewHelper.setScaleY(PowerFullLayout.this, scale);// y方向上缩放
            }
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // 一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            preScale = scale;// 记录本次缩放比例
            lastMultiTouchTime = System.currentTimeMillis();// 记录双指缩放后的时间
        }
    }



    int ids = 110;

    // 绘制事件
    private void DrawingAdd(MotionEvent event, int type) {
        params = new FrameLayout.LayoutParams(0, 0);
        params.setMargins((int) getXm(event), (int) getYm(event), 0, 0);
        listParams.add(params);
        switch (type) {
            case 0:
                dragBaseView = new DragBaseView(mContext, 0, ids);
                dragBaseView.setLayoutParams(params);
                dragBaseView.setOption(childOption);
                if(mListener!=null){
                    mListener.addViews(dragBaseView);
                }
                listBase.add(dragBaseView);
                listBaseEnd.add(dragBaseView);
                ids++;
                break;
            case 1:
                dragBaseView = new DragBaseView(mContext, 1, ids);
                dragBaseView.setLayoutParams(params);
                dragBaseView.setOption(childOption);
                if(mListener!=null){
                    mListener.addViews(dragBaseView);
                }
                listBase.add(dragBaseView);
                listBaseEnd.add(dragBaseView);
                ids++;
                break;
        }
    }


    DragBaseView.Option childOption = new DragBaseView.Option(){
        @Override
        public int Type() {
            return 0;
        }

        @Override
        public void select(DragBaseView view) {
            Log.e("daoyi", "" + view.flage);
            Log.e("daoyi", "" + view.isSelect);
//            Log.e("daoyi", "" + listBase.get(0).flage);
            for (int i = 0; i < listBase.size(); i++) {
                if (listBase.get(i).flage != view.flage) {
                    listBase.get(i).isSelect = false;
                    listBase.get(i).show();
                } else {
                    touchChild = view.isSelect;
                    childFlage = view.flage;
                }
            }

        }

        @Override
        public void delView(DragBaseView view) {

        }
    };




    /**
     * 计算两个手指间的距离
     */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算两个手指间的中间点
     */
    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }



    TouchEventListener mListener;

    public void setListener(TouchEventListener listener) {
        mListener = listener;
    }
    /**
     * 初始化获取屏幕宽高
     */
    protected void initScreenW_H() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        Log.e("daoyi","width:"+screenWidth);
        Log.e("daoyi","height:"+screenHeight);
    }
    // getx
    private float getXm(MotionEvent event){
        int a = screenWidth/getWidth();
        return (event.getX()+getScrollX());
    }
    // gety
    private float getYm(MotionEvent event){
        int a = screenHeight/(getHeight()-40);
        return (Math.abs(event.getY()+getScrollY()));
    }
}
