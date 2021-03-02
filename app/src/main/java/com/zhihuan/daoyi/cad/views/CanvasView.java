package com.zhihuan.daoyi.cad.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.service.autofill.FillEventHistory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.zhihuan.daoyi.cad.base.Datas;
import com.zhihuan.daoyi.cad.datas.OvalData;

import java.util.logging.Level;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class CanvasView extends View  {

    public float[] datas = new float[8];
    public Paint paint;
    public int colors = 0xffFF0828; // 红色
    private float strokeW = 3f;
    public int width;
    public int height;
    Context con;
    private float sx;
    private float sy;


    public CanvasView(Context context) {
        super(context);
        this.con = context;
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.con = context;
        init();
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.con = context;
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setColor(colors);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
//        setOnTouchListener(this);
        this.width = 20;
        this.height = 20;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(0, 0, getWidth(), getHeight(), paint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean flag =true;
        int a[]=new int[2];
        RelativeLayout.LayoutParams layoutParams = null;
//        getDescendantCoordRelativeToSelf(this,a);


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                a[0] = getRight();
                a[1] = getTop();
                Log.e("子组件坐标",a.toString());
                sx = event.getRawX();
                sy = event.getRawY();
                flag =true;
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                Log.e("daoyiZZ", "startX_" + sx + "::startY_" + sy);
                Log.e("daoyiZZ", "MOVE: x_" + event.getRawX() + ";;Y_" + event.getRawY());
                float endx = event.getRawX();
                float endy = event.getRawY();
                int touchSlop = ViewConfiguration.get(con).getScaledTouchSlop();
                int pdx,pdy;
                if (endx > sx) {
                    pdx = (int) (endx-sx);
                } else {
                    pdx = (int) (sx-endx);
                }
                if (endy > sy) {
                    pdy = (int) (endy - sy);
                } else {
                    pdy = (int) (sy - endy);
                }
                int wx=0, hy=0; // 最重的宽高
                if((pdx)>touchSlop){

                    if (endx > sx) {
                        wx = (int) (endx - sx);
                    } else {
                        wx = (int) (endx - sx);
//                    wx = getLeft()-(int) (endx - sx);
                    }
//                    if(hy>=10){
                        layoutParams.width = (int) ((endx-getLeft()+wx));
//                    }



                }
                if(pdy>touchSlop){
                    if (endy > sy) {
//                    hy = getTop()+(int) (endy - sy);
                        hy = (int) (endy-sy);
                    } else {
//                    hy = getTop()-(int) (sy - endy);
                        hy = (int) (endy - sy);
                    }
//                    if(hy-getHeight()>=10){
                        layoutParams.height = (int) (endy-getTop()+ hy);
//                    }

                }
//                layoutParams.setMargins((int) (getLeft()), (int) (getTop()), 0, 0);
                setLayoutParams(layoutParams);

                flag=true;

                break;
                case MotionEvent.ACTION_UP:
//                    sx= (int) event.getRawX();;
//                    sy= (int) event.getRawY();
                    flag=false;
                    break;
        }

        return flag;
    }

    public int getWidths() {
        return width;
    }

    public void setWidths(int width) {
        this.width = width;
    }


    public int getHeights() {
        return height;
    }

    public void setHeight(int heights) {
        height = heights;
    }

    public float getDescendantCoordRelativeToSelf(View descendant, int[] coord) {
        float scale = 1.0f;
        float[] pt = {coord[0], coord[1]};
        //坐标值进行当前窗口的矩阵映射，比如View进行了旋转之类，它的坐标系会发生改变。map之后，会把点转换为改变之前的坐标。
        descendant.getMatrix().mapPoints(pt);
        //转换为直接父窗口的坐标
        scale *= descendant.getScaleX();
        pt[0] += descendant.getLeft();
        pt[1] += descendant.getTop();
        ViewParent viewParent = descendant.getParent();
        //循环获得父窗口的父窗口，并且依次计算在每个父窗口中的坐标
        while (viewParent instanceof View && viewParent != this) {
            final View view = (View) viewParent;
            view.getMatrix().mapPoints(pt);
            scale *= view.getScaleX();//这个是计算X的缩放值。此处可以不管
            //转换为相当于可视区左上角的坐标，scrollX，scollY是去掉滚动的影响
            pt[0] += view.getLeft() - view.getScrollX();
            pt[1] += view.getTop() - view.getScrollY();
            viewParent = view.getParent();
        }
        coord[0] = (int) Math.round(pt[0]);
        coord[1] = (int) Math.round(pt[1]);
        return scale;
    }

}
