package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.utils.MacUtils;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date : 3
 */
public class DragRelativeLayoutView extends RelativeLayout {

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
     * 用于记录拖拉图片移动的坐标位置
     */
    private Matrix matrix = new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();

    // 总体放大
    float scale = 0f;
    // 累计放大
    float scaleN = 0f;
    private RectF rect = new RectF();
    private RectF rectSrc = new RectF();
    private RectF rectEnd = new RectF();

    /**
     * 两个手指的开始距离
     */
    private float startDis;
    /**
     * 两个手指的中间点
     */
    private PointF midPoint;

    private Context mContext;
    private Canvas mCanvas;

    boolean falge = false;


    public DragRelativeLayoutView(Context context) {
        super(context);
        this.mContext = context;
    }

    public DragRelativeLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public DragRelativeLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rect.offset(MacUtils.dpto(getWidth() / 2), MacUtils.dpto(getHeight() / 2));
        rectSrc.offset(MacUtils.dpto(getWidth() / 2), MacUtils.dpto(getHeight() / 2));

//        setMeasuredDimension((int)rect.width(),(int) rect.height());

    }

    @Override
    protected void onDraw(Canvas canvas) {


        this.mCanvas = canvas;
        super.onDraw(this.mCanvas);
        Log.e("daoyi", String.valueOf(getMatrix()));
    }

    TouchEventListener mListener;

    public void setListener(TouchEventListener listener) {
        mListener = listener;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return s(event);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public boolean s(MotionEvent event) {
        /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                mode = MODE_DRAG;
                // 记录ImageView当前的移动位置
                currentMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                break;
            // 手指在屏幕上移动，改事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                // 拖拉图片
                if (mode == MODE_DRAG) {
                    float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                    float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                    // 在没有移动之前的位置上进行移动
                    matrix.set(currentMatrix);
                    matrix.postTranslate(dx / 2, dy / 2);
                }
                // 放大缩小图片
                else if (mode == MODE_ZOOM) {
                    float endDis = distance(event);// 结束距离
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        scale = endDis / startDis;// 得到缩放倍数
                        scaleN += scale;
                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);

                    }
                }
                matrix.mapRect(rectEnd, rectSrc);

                matrix.setPolyToPoly(new float[]{0, 0, rectSrc.width() / 2, rectSrc.height() / 2}, 0,
                        new float[]{0, 0, rectEnd.width() / 2, rectEnd.height() / 2}, 0, 2);
                setAnimationMatrix(matrix);
                currentMatrix.set(matrix);
//                rectSrc.set(rectEnd);
                getLayoutParams().width = (int) rectEnd.width();
                getLayoutParams().height = (int) rectEnd.height();
                onMeasure((int) rectEnd.width(), (int) rectEnd.height());
                invalidate();
                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = MODE_ZOOM;
                /** 计算两个手指间的距离 */
                startDis = distance(event);
                /** 计算两个手指间的中间点 */
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                    midPoint = mid(event);
                    //记录当前ImageView的缩放倍数
//                    currentMatrix.set(getMatrix());
                    currentMatrix.set(matrix);
                }
                break;
        }
        if (mListener != null && mListener.dispatchTouchEvent(event)) {
            return false;
        }else{
            return true;
        }
//        return true;
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

}
