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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());


//        activityMainBinding.r1.setListener(new TouchEventListener() {
//            @Override
//            public boolean dispatchTouchEvent(MotionEvent event) {
//                return activityMainBinding.r1.dispatchTouchEvent(event);
//            }
//        });
        activityMainBinding.maxBox.setListener(new TouchEventListener() {
            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                Rect rect = new Rect();
                activityMainBinding.r1.getLocalVisibleRect(rect);
                Rect rect2 = new Rect();
                activityMainBinding.c1.getLocalVisibleRect(rect2);
                if (rect.contains((int) event.getX(), (int) event.getY())
                        ||rect.contains((int) event.getX(), (int) event.getY())) {
                    return true;
                } else {
                    return false;
                }
            }
        });

    }


}