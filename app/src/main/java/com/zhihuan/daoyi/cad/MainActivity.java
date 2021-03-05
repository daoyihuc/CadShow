package com.zhihuan.daoyi.cad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zhihuan.daoyi.cad.databinding.ActivityMainBinding;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.views.DragImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    ActivityMainBinding activityMainBinding;
    DragImageView mView;

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
    public boolean onTouch(View v, MotionEvent event) {

        return down(v,event);
    }

    public boolean down(View v,MotionEvent event){
        if(v.getId()==R.id.rootBox){
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                outSelect = true;
                Log.e("daoyi","最外层盒子被按下");
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
}