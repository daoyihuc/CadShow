package com.zhihuan.daoyi.cad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.zhihuan.daoyi.cad.databinding.ActivityMainBinding;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.views.CropRectView;
import com.zhihuan.daoyi.cad.views.DragBaseView;
import com.zhihuan.daoyi.cad.views.DragImageView;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding activityMainBinding;
    DragImageView mView;
    private int currentX;
    private int currentY;

    boolean drawingMode =true; //  绘制事件
    int drawingType = -1; // 0： 矩形， 1： 圆形

    boolean outSelect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

//        activityMainBinding.rootBox.setOnTouchListener(this);

        activityMainBinding.maxBox.setListener(touchEventListener);
//        activityMainBinding.maxBox.setOnTouchListener(this);
        activityMainBinding.rectBtn.setOnClickListener( v -> {
            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            if(drawingType !=0 && !drawingMode){
                drawingType = 0;
                drawingMode=!drawingMode;
            }else if(drawingType == 0&& drawingMode){
                drawingMode =false;
            }else if(drawingType == 0&& !drawingMode){
                drawingMode = true;
            }
        });
        activityMainBinding.circleBtn.setOnClickListener( v -> {
            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            if(drawingType !=1 && !drawingMode){
                drawingType = 1;
                drawingMode=!drawingMode;
            }else if(drawingType == 1&& drawingMode){
                drawingMode =false;
            }else if(drawingType == 1&& !drawingMode){
                drawingMode = true;
            }
        });

    }
    TouchEventListener touchEventListener=new TouchEventListener() {
        @Override
        public void addViews(DragBaseView dragBaseView) {
            activityMainBinding.maxBox.addView(dragBaseView);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent event) {
            return outSelect;
        }

        @Override
        public boolean DrawingOption() {
            return drawingMode;
        }

        @Override
        public int DrawingType() {
            return drawingType;
        }

        @Override
        public boolean DrawingCloseCall(boolean close) {
            drawingMode = close;
            return drawingMode;
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public boolean down(MotionEvent event){
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                currentX = (int) event.getRawX();
                currentY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();
//                activityMainBinding.maxBox.scrollBy(currentX - x2 , currentY - y2);
                currentX = x2;
                currentY = y2;
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                break;
            }
        }
        return true;
    }
}