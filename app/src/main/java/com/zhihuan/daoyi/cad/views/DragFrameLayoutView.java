package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.utils.MatrixUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class DragFrameLayoutView extends RelativeLayout implements View.OnTouchListener {

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
    List<LayoutParams> listParams = new ArrayList<>();
    LayoutParams params;

    boolean first = true;

    boolean touchChild = false; // 子类消费事件
    boolean touchParent = false; // 父类消费事件
    boolean touchDraw = false;// 绘制事件
    boolean touch = true; // 父类消费事件

    int childFlage = 0;// 子节点标识
    private int mWidth = 2000;
    private int mHeight = 2000;
    private int screenHeight;
    private int screenWidth;

    private float scaleW,scaleH,scaleA;

    private Matrix matrix=new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();
    RectF rect=new RectF();



    public DragFrameLayoutView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DragFrameLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public DragFrameLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(mWidth,mHeight);
    }

    private  void init(){
        initScreenW_H();
        rect.left = 0;
        rect.right = 2000;
        rect.top = 0;
        rect.bottom = 2000;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return s(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
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


    int ids = 110;

    // 绘制事件
    private void DrawingAdd(MotionEvent event, int type) {
        params = new LayoutParams(0, 0);
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

    DragBaseView.Option childOption = new DragBaseView.Option() {
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
    };

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
                scaleA= (startDis-endDis)/(startDis+endDis);
//                scaleA= (endDis-startDis);
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
            matrix.postScale(scaleB,scaleB,midPoint.x, midPoint.y);
            matrix.mapRect(rectdst,rect);
            layoutParams.width = (int) rectdst.width();
            layoutParams.height = (int) rectdst.height();
//            rect.right = rectdst.width();
//            rect.bottom = rectdst.height();
            setLayoutParams(layoutParams);
            allZoom();
//            matrix.reset();
//                currentMatrix.set(matrix);
            Log.e("daoyi_1",""+endDis);
            Log.e("daoyi_2",""+startDis);
            Log.e("daoyi_3",""+rect);
            Log.e("daoyi_4",""+rectdst);
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
            clearSelect();
        } else if (touchChild) {
            touch = !touchChild;
        } else  {
            touch = true;
        }
        //&&!isShowChild(event)
    }


    int offset = 100;

    // 清除所有选中
    public void clearSelect() {
        for (int i = 0; i < listBase.size(); i++) {
            listBase.get(i).isSelect = false;
            listBase.get(i).show();
            touchChild = false;
            childFlage = 0;

        }

    }

    private void allZoom(){
        for(int i=0;i<listBase.size();i++){
//            int w = listBase.get(i).getWidth()-(int) (listBase.get(i).getWidth()*scaleA*0.1);
//            int h = listBase.get(i).getHeight()-(int) ((int) listBase.get(i).getHeight()*scaleA*0.1);
            int left = listBase.get(i).getLeft();
            int right = listBase.get(i).getRight();
            int top = listBase.get(i).getTop();
            int bottom = listBase.get(i).getBottom();
            Rect rect=new Rect();
            rect.left = left;
            rect.right = right;
            rect.top = top;
            rect.bottom = bottom;
            RectF rectdst= new RectF();
            matrix.mapRect(rectdst,new RectF(rect));
            int w = (int) rectdst.width();
            int h = (int) rectdst.height();
            RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(w,h);
            params.setMargins((int) rectdst.left,(int) rectdst.top,0,0);
            listBase.get(i).setLayoutParams(params);
            Log.e("daoyi_5",""+rect);
            Log.e("daoyi_6",""+rectdst);
        }


    }


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
