package com.zhihuan.daoyi.cad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
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

    boolean drawingMode =false; //  绘制事件
    int drawingType = -1; // 0： 矩形， 1： 圆形 2: 画笔

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
//            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            drawingType=0;
            setCheck(!drawingMode);

        });
        activityMainBinding.circleBtn.setOnClickListener( v -> {
//            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            drawingType=1;
            setCheck(!drawingMode);
        });
        activityMainBinding.canvasBtn.setOnClickListener( v -> {
//            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            drawingType=2;
            setCheck(!drawingMode);
        });

    }
    // 设置选中
    private void setCheck(boolean select){
        switch (drawingType){
            case 0:
                if(select){
                    activityMainBinding.rectBtn.setChecked(true);
                }else{
                    activityMainBinding.rectBtn.setChecked(false);
                }
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.canvasBtn.setChecked(false);
                break;
            case 1:
                if(select){
                    activityMainBinding.circleBtn.setChecked(true);
                }else{
                    activityMainBinding.circleBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.canvasBtn.setChecked(false);
                break;
            case 2:
                if(select){
                    activityMainBinding.canvasBtn.setChecked(true);
                }else{
                    activityMainBinding.canvasBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);

                break;
        }
        drawingMode=select;
    }


    TouchEventListener touchEventListener=new TouchEventListener() {
        @Override
        public void BackBitmap(Bitmap bitmap) {
//            activityMainBinding.back.setImageBitmap(bitmap);
        }

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
            setCheck(drawingMode);
            return drawingMode;
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

}