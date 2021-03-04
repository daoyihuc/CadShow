package com.zhihuan.daoyi.cad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.MotionEvent;

import com.zhihuan.daoyi.cad.databinding.ActivityMainBinding;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.views.DragImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    DragImageView mView;

    boolean drawingMode =true;
    int drawingType = -1; // 0： 矩形， 1： 圆形

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());


        activityMainBinding.maxBox.setListener(touchEventListener);
        activityMainBinding.rectBtn.setOnClickListener( v -> {
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
        public boolean dispatchTouchEvent(MotionEvent event) {
//            Rect rect = new Rect();
//            activityMainBinding.r1.getLocalVisibleRect(rect);
//            Rect rect2 = new Rect();
//            activityMainBinding.c1.getLocalVisibleRect(rect2);
//            if (rect.contains((int) event.getX(), (int) event.getY())
//                    ||rect.contains((int) event.getX(), (int) event.getY())) {
//                return true;
//            } else {
//                return false;
//            }
            return false;
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


}