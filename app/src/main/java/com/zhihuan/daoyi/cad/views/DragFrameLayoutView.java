package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.objects.CanvasOption;
import com.zhihuan.daoyi.cad.objects.PaintsOPtion;
import com.zhihuan.daoyi.cad.utils.CalcuLationUtils;
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
    private PointF startPoint = new PointF(); // 记录屏幕开始
    private PointF startPointBox = new PointF(); // 记录自身
    private PointF drawPoint = new PointF();

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
    List<PointF> listPoint = new ArrayList<>();
    List<PointF> listPointX = new ArrayList<>();
    LayoutParams params;

    Paint paint;
    Canvas canvas;
    Bitmap bitmap2;
    boolean first = true;

    boolean touchChild = true; // 子类消费事件
    boolean touchParent = false; // 父类消费事件
    boolean touchDraw = false;// 绘制事件
    boolean touch = true; // 父类消费事件

    int childFlage = 0;// 子节点标识
    private int mWidth = 0;
    private int mHeight = 0;
    private int mWidthEnd = 0;
    private int mHeightEnd = 0;
    private int screenHeight;
    private int screenWidth;
    // 累计放大
    float scaleN = 0f;
    float scalY = 0f;

    private float scaleA;
    double scaleW,scaleH;

    private Matrix matrix=new Matrix();
    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private Matrix currentMatrix = new Matrix();
    RectF rect=new RectF();
    Path path=new Path();



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
        setOnTouchListener(this);
        rect.left = 0;
        rect.right = 2000;
        rect.top = 0;
        rect.bottom = 2000;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(touchDraw){
            return touchDraw;
        }else if(touchChild){
            return false;
        }else{
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        judgment(event);
        if(touch){
            return  s(event);
        }else{
            return false;
        }
//        return  s(event);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean s(MotionEvent event) {
        // 优先级计算
        if (touch) {
            Log.e("daoyi","本地事件消费");
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    startPoint.set(event.getRawX(), event.getRawY());
                    startPointBox.set(event.getX(), event.getY());
                    drawPoint.set(event.getRawX(), event.getRawY());
                    listPoint.add(new PointF(event.getRawX(),event.getRawY()));
                    listPointX.add(new PointF(event.getX(),event.getX()));
                    path.moveTo(event.getX(),event.getY());
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
                        DrawingMove(event, dragBaseView,mListener.DrawingType());
                    } else {
                        MatrixF(event);
//                        invalidate();
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                    if (mListener != null && mListener.DrawingOption()) {
                        if(mListener.DrawingType()==2){
                            RectF rectF = CutView(event);

                        }

                        Bitmap bitmap = loadBitmapFromViewBySystem();
                        if(bitmap!=null){
                            touchDraw = false;
//                            mListener.DrawingCloseCall(false);
//                            mListener.BackBitmap(bitmap);
                            bitmap.recycle();
                            removeAllViews();
                        }
                        // 裁剪view
                    }
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    scaleN = getScaleX();
                    scalY = getScaleY();
                    /** 计算两个手指间的距离 */
                    if (event.getPointerCount() == 2) {
                        startDis = distance(event);
                    }
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
//                        currentMatrix.set(matrix);
                    }
                    break;
            }
        }


        return touch;
    }
    DragBaseView.Option childOption = new DragBaseView.Option() {
        @Override
        public int Type() {
            return 0;
        }

        @Override
        public boolean isDraw() {
            if(mListener!=null){
                return mListener.DrawingOption();
            }else{
                return false;
            }

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
            removeView(view);
        }
    };


    int ids = 110;

    // 绘制事件
    private void DrawingAdd(MotionEvent event, int type) {
        MotionEvent event1=event;
        CanvasOption canvasOption=new CanvasOption();
        if(type!=2){
            params = new LayoutParams(0, 0);
            params.setMargins((int) getXm(event), (int) getYm(event), 0, 0);
        }else{
            params = new LayoutParams((int)(screenWidth),(int)(screenHeight));
            params.setMargins((int) (getXm2(startPointBox.x)-startPoint.x), (int) (getYm2(startPointBox.y)-startPoint.y), 0, 0);
        }
        dragBaseView = new DragBaseView(mContext, type, ids);
        dragBaseView.setLayoutParams(params);
        dragBaseView.setOption(childOption);
        if(mListener!=null){
            mListener.addViews(dragBaseView);
        }
        listBase.add(dragBaseView);
        listBaseEnd.add(dragBaseView);
        listParams.add(params);
        if(type==2){
            initPaint(null);
            initCanvas(null);
        }
        ids++;
    }



    // 绘制移动
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void DrawingMove(MotionEvent event, View v, int type) {
        float endx = event.getRawX();
        float endy = event.getRawY();
        listPoint.add(new PointF(event.getRawX(),event.getRawY()));
        listPointX.add(new PointF(event.getX(),event.getY()));

        if (v != null) {
            int wx, hy; // 最重的宽高
            if (endx > startPoint.x) {
                wx = (int) (endx - startPoint.x);
            } else if(endx<startPoint.x){
                wx = (int) (startPoint.x - endx);
            }else{
                wx = 10;
            }
            if (endy > startPoint.y) {
                hy = (int) (endy - startPoint.y);
            } else if(endy<startPoint.y) {
                hy = (int) (startPoint.y - endy);
            }else{
                hy = 10;
            }
            if(type!=2){
                params.width = wx;
                params.height = hy;
                v.setLayoutParams(params);
            }

            if(type==2){
//                v.setLayoutParams(params);
                canvas.drawLine(drawPoint.x,drawPoint.y,
                        event.getRawX(),event.getRawY(),paint);
                drawPoint.set(event.getRawX(),event.getRawY());
                dragBaseView.setBitmap(bitmap2);
//                RectF rectF = CutView(event);
            }

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
            startPointBox.set(event.getX(), event.getY());
        }
        // 放大缩小图片
        else if (mode == MODE_ZOOM) {
            float endDis = distance(event);// 结束距离
            if (endDis > 30f) {
                matrix=new Matrix();
                scaleA= (endDis-startDis)/(startDis+endDis);
//                scaleA= (endDis-startDis);
                float a= (float) (scaleN+(scaleA));
                float b= (float) (scalY+(scaleA));
                matrix.set(currentMatrix);
                matrix.postScale(a, b, midPoint.x, midPoint.y);
                mWidthEnd= mWidth+(int) MatrixUtils.getMatrixScaleX(mWidth,matrix);
                mHeightEnd= mHeight+(int) MatrixUtils.getMatrixScaleY(mHeight,matrix);
                scaleW=CalcuLationUtils.div(mWidthEnd,mWidth,2);
                scaleH=CalcuLationUtils.div(mHeightEnd,mHeight,2);
                if(a>0.2&&a<3){
                    setScaleX(a);
                }
                if(b>0.2&&b<3){
                    setScaleY(b);
                }
            }
            Log.e("daoyi_10",""+mWidthEnd);
            Log.e("daoyi_11",""+mWidth);
            Log.e("daoyi_20",""+mHeightEnd);
            Log.e("daoyi_21",""+mHeight);
            Log.e("daoyi_3",""+rect);
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
            touch = true;
            clearSelect();
            return;
        }
        if (touchChild) {
            int count = 0;
            for(int i=0;i<listBase.size();i++){
                Rect rect=new Rect();
                listBase.get(i).getLocalVisibleRect(rect);
                if(rect.contains((int) event.getX(),(int) event.getY())){
                    count++;
                }
            }
            if(count==0){
                clearSelect();
            }else{
                touch = false;
            }

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
            touch=true;
            childFlage = 0;

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initPaint(PaintsOPtion paintsOPtion){
        paint=new Paint();
        if(paintsOPtion !=null){

        }else{
            paint.setAntiAlias(true);
            paint.setStrokeWidth(6f);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
        }

    }

    private void initCanvas(CanvasOption canvasOption){



//        canvas.setBitmap(bitmap2);
        if(canvasOption!=null){
            bitmap2= Bitmap.createBitmap(canvasOption.getWidth(),canvasOption.getHeight(), Bitmap.Config.ARGB_8888);
        }else{
            bitmap2= Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_4444);
        }
        canvas=new Canvas(bitmap2);
    }

    /**
     * 初始化获取屏幕宽高
     */
    protected void initScreenW_H() {
        screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        mWidth=mContext.getResources().getDrawable(R.drawable.test).getMinimumWidth();
        mHeight=mContext.getResources().getDrawable(R.drawable.test).getMinimumHeight();
        mWidthEnd=mWidth;
        mHeightEnd=mHeight;
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
    // getx
    private float getXm2(float x){
        int a = screenWidth/getWidth();
        return Math.abs(x+getScrollX());
    }
    // gety
    private float getYm2(float y){
        int a = screenHeight/(getHeight()-40);
        return Math.abs(y+getScrollY());
    }
    // 比较点的大小
    private PointF MaxPonit(){
        PointF pointF = new PointF();
        pointF.set(0,0);
        if(listPoint.size()>0){
            float x=listPoint.get(0).x,y=listPoint.get(0).y;
            for(int i=1;i<listPoint.size();i++){
                if(x<listPoint.get(i).x){
                    x=listPoint.get(i).x;
                }
                if(y<listPoint.get(i).y){
                    y=listPoint.get(i).y;
                }
            }
            pointF.set(x,y);
        }
        return pointF;
    }

    // 比较点X的大小
    private PointF MaxPonitX(){
        PointF pointF = new PointF();
        pointF.set(0,0);
        if(listPointX.size()>0){
            float x=listPointX.get(0).x,y=listPointX.get(0).y;
            for(int i=1;i<listPointX.size();i++){
                if(x<listPointX.get(i).x){
                    x=listPointX.get(i).x;
                }
                if(y<listPointX.get(i).y){
                    y=listPointX.get(i).y;
                }
            }
            pointF.set(x,y);
        }
        return pointF;
    }

    // 比较点的大小
    private PointF MinPonit(){
        PointF pointF = new PointF();
        pointF.set(0,0);
        if(listPoint.size()>0){
            float x=listPoint.get(0).x,y=listPoint.get(0).y;
            for(int i=1;i<listPoint.size();i++){
                if(x>listPoint.get(i).x){
                    x=listPoint.get(i).x;
                }
                if(y>listPoint.get(i).y){
                    y=listPoint.get(i).y;
                }
            }
            pointF.set(x,y);
        }


        return pointF;
    }

    // 比较点的大小
    private PointF MinPonitX(){
        PointF pointF = new PointF();
        pointF.set(0,0);
        if(listPointX.size()>0){
            float x=listPointX.get(0).x,y=listPointX.get(0).y;
            for(int i=1;i<listPointX.size();i++){
                if(x>listPointX.get(i).x){
                    x=listPointX.get(i).x;
                }
                if(y>listPointX.get(i).y){
                    y=listPointX.get(i).y;
                }
            }
            pointF.set(x,y);
        }


        return pointF;
    }

    // 重新测量view并进行裁剪
    private RectF CutView(MotionEvent event){
        RectF rectF=new RectF();
        PointF pointMax = MaxPonit();
        PointF pointMin = MinPonit();

        PointF pointMaxX = MaxPonitX();
        PointF pointMinX = MinPonitX();

        if(pointMin.x==0){
            pointMin.x=1f;
        }
        if(pointMin.y==0){
            pointMin.y=1f;
        }
        float dx = pointMax.x - pointMin.x;
        float dy = pointMax.y - pointMin.y;
        if(dx<=0){
            dx=10f;
        }else if(dy<=0){
            dy=10f;
        }
        rectF.set(0,0,dx,dy);


        params.width = (int) rectF.width();
        params.height = (int) rectF.height();
        CanvasOption canvasOption=new CanvasOption();
        canvasOption.setWidth((int) rectF.width());
        canvasOption.setHeight((int) rectF.height());
        Canvas canvast=new Canvas();

        if(startPointBox.x<pointMinX.x){
            params.setMargins((int) (getXm2(pointMinX.x)),
                    (int) (getYm2(startPointBox.y)), 0, 0);
            if(startPointBox.y<pointMinX.y){
                params.setMargins((int) (getXm2(pointMinX.x)),
                        (int) (getYm2(pointMinX.y)), 0, 0);
            }else{
                params.setMargins((int) (getXm2(pointMinX.x)),
                        (int) (getYm2(startPointBox.y)), 0, 0);
            }
        }else{
            params.setMargins((int) (getXm2(startPointBox.x)),
                    (int) (getYm2(startPointBox.y)), 0, 0);
            if(startPointBox.y<pointMinX.y){
                params.setMargins((int) (getXm2(startPointBox.x)),
                        (int) (getYm2(pointMinX.y)), 0, 0);
            }else{
                params.setMargins((int) (getXm2(startPointBox.x)),
                        (int) (getYm2(startPointBox.y)), 0, 0);
            }

        }


        if(listPoint.size()>0){
            PointF s=listPoint.get(0);
        }

        dragBaseView.setLayoutParams(params);
//        if(rectF.width()>0&&rectF.height()>0){
//            Bitmap bitmap1= Bitmap.createBitmap((int)rectF.width(),(int)rectF.height(), Bitmap.Config.ARGB_4444);
//            canvas.setBitmap(bitmap1);
//            for(int i=1;i<listPoint.size();i++){
//                PointF e=listPoint.get(i);
//                canvast.drawLine(s.x,s.y,e.x,e.y,paint);
//                s.set(e);
//            }
//        }

        if(rectF.width()>0&&rectF.height()>0&&pointMin.x+rectF.width()<screenWidth&&pointMin.y+rectF.height()<screenHeight){

            Bitmap bitmap=Bitmap.createBitmap(bitmap2,(int) pointMin.x,(int)pointMin.y,(int)dx,(int)dy);
            dragBaseView.setBitmap(bitmap);
            listPoint.clear();
        }

//        bitmap2.recycle();

        listPoint.clear();


        return  rectF;
    }


    public  Bitmap loadBitmapFromViewBySystem() {

        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap = getDrawingCache();
        return bitmap;
    }
}