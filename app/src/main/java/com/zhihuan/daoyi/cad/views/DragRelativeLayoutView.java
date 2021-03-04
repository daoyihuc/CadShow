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
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.zhihuan.daoyi.cad.interfaces.TouchEventChildListener;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.ArrayList;
import java.util.List;

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

    boolean first =true;

    // 组件类型
    DragScaleCircleView scaleCircleView;
    DragScaleRectView scaleRectView;
    DragBaseView dragBaseView;

    List<DragScaleCircleView> listCircle = new ArrayList<>();
    List<DragScaleRectView> listRect = new ArrayList<>();


    RelativeLayout.LayoutParams params;



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
        if(false){
            Rect rects=new Rect();
            getLocalVisibleRect(rects);
            rect.set(rects);
            first=false;
            rectSrc.set(rect);
        }


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
//                currentMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                if(mListener!=null&&mListener.DrawingOption()){
                    Log.e("daoyi","绘制模式");
                    DrawingAdd(event, mListener.DrawingType());
                }else{
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(matrix);
                }

                break;
            // 手指在屏幕上移动，改事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                if(mListener!=null&&mListener.DrawingOption()){
                    Log.e("daoyi","绘制模式");
                    Log.e("daoyi","绘制类型："+mListener.DrawingType());
                    if(mListener.DrawingType()==0){
                        DrawingMove(event,dragBaseView);
                    }
                    if(mListener.DrawingType()==1){
                        DrawingMove(event,dragBaseView);
                    }

                }else{
                    MatrixF(event);
                    invalidate();
                }

                break;
            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                if(mListener!=null&&mListener.DrawingOption()){
                    mListener.DrawingCloseCall(false);
                }
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


    // 绘制事件
    private void DrawingAdd(MotionEvent event, int type){
        params=new RelativeLayout.LayoutParams(0,0);
        params.setMargins((int)event.getX(),(int) event.getY(),0,0);
        switch (type){
            case 0:
                dragBaseView = new DragBaseView(mContext,0);
                dragBaseView.setLayoutParams(params);
                addView(dragBaseView);
                break;
            case 1:
                dragBaseView = new DragBaseView(mContext,1);
                dragBaseView.setLayoutParams(params);
                addView(dragBaseView);
                break;
        }

    }

    // 绘制移动
    private void DrawingMove(MotionEvent event,View v){
        float endx=event.getX();
        float endy=event.getY();

        if(v!=null){
            int wx,hy; // 最重的宽高
            if(endx>startPoint.x){
                wx= (int) (endx-startPoint.x);
            }else{
                wx = (int) (startPoint.x-endx);
            }
            if(endy>startPoint.y){
                hy= (int) (endy-startPoint.y);
            }else{
                hy = (int) (startPoint.y-endy);
            }
            params.width = wx;
            params.height = hy;
            v.setLayoutParams(params);

        }else{
            Log.e("daoyi","view为空");
        }

    }
    // 为所有组件设置监听
    private void DragbindClick(){
        if(listRect.size()>0){
            listRect.forEach(dragScaleRectView -> {
//                dragScaleRectView.setOnClickListener(onClickListener);
            });
        }
    }



    // 矩阵事件
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void MatrixF(MotionEvent event){
        // 拖拉图片
        if (mode == MODE_DRAG) {
            float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
            float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
            // 在没有移动之前的位置上进行移动
//                    matrix.set(currentMatrix);
            matrix.postTranslate(MacUtils.dpto((int) dx), MacUtils.dpto((int) dy));
            matrix.mapRect(rectEnd, rectSrc);
        }
        // 放大缩小图片
        else if (mode == MODE_ZOOM) {
            float endDis = distance(event);// 结束距离
            float a= 0;
            if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                scale = endDis / startDis;// 得到缩放倍数
                Log.e("daoyi", "" + scale);
                if(scaleN!=0){
                    scaleN= 1-scale-Math.min(scale,scaleN)/Math.max(scale,scaleN);
                }else{
                    scaleN = Math.abs(scale-1);
                }
            }
//            if(scaleN<=10f&&scaleN>0.4f){
                matrix.set(currentMatrix);
                matrix.postScale(scale, scale, midPoint.x, midPoint.y);
//            }
            Log.e("daoyi","scaleN:"+scaleN);
            Log.e("daoyi","scaleN2:"+Math.min(scale,scaleN)/Math.max(scale,scaleN));
        }
        setAnimationMatrix(matrix);
        currentMatrix.set(matrix);
        startPoint.set(event.getX(), event.getY());
        rectSrc.set(rectEnd);
    }

}
