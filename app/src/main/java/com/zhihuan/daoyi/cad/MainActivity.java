package com.zhihuan.daoyi.cad;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.zhihuan.daoyi.cad.Dialogs.LoadDialog;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityBaseBinding;
import com.zhihuan.daoyi.cad.databinding.ActivityMainBinding;
import com.zhihuan.daoyi.cad.databinding.BaseviewBinding;
import com.zhihuan.daoyi.cad.help.SaveHelper;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.utils.MacUtils;
import com.zhihuan.daoyi.cad.views.CropRectView;
import com.zhihuan.daoyi.cad.views.DragBaseView;
import com.zhihuan.daoyi.cad.views.DragFrameLayoutView;
import com.zhihuan.daoyi.cad.views.DragImageView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    ActivityMainBinding activityMainBinding;
    boolean drawingMode =false; //  绘制事件
    int drawingType = 10; // 0： 矩形， 1： 圆形  2: 画笔 3: 画笔
    boolean outSelect=false;
    private String name="样板图纸编辑";
    private String OkTitle="保存";
    private String[] permission;
    {
        permission=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
    }
    DragFrameLayoutView dragFrameLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MacUtils.initWindow(this,0xffffffff,false,null,true);
        initTitle("");
        activityMainBinding=viewBinding;
        dragFrameLayoutView=activityMainBinding.maxBox;
        baseBinding.title.setCenterTitle(name);
        baseBinding.title.setRightTitle(OkTitle);
        baseBinding.title.setRightOnClickListener(SaveClick);
        activityMainBinding.maxBox.setListener(touchEventListener);
        activityMainBinding.rectBtn.setOnClickListener( v -> {
            drawingType=0;
            setCheck(!drawingMode);

        });
        activityMainBinding.circleBtn.setOnClickListener( v -> {
            drawingType=1;
            setCheck(!drawingMode);
        });
        activityMainBinding.canvasBtn.setOnClickListener( v -> {
            drawingType=2;
            setCheck(!drawingMode);
        });

        activityMainBinding.touch.setOnClickListener( v -> {
            drawingType=10;
            setCheck(!drawingMode);
        });
        activityMainBinding.textBtn.setOnClickListener( v -> {
//            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            drawingType=3;
            setCheck(!drawingMode);
        });
        getPermissions(permission);
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater(), baseBinding.getRoot(), true);
    }

    // 设置选中
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setCheck(boolean select){
        Drawable drawable=getResources().getDrawable(R.drawable.ic_baseline_touch_app_24);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        drawable.setTint(Color.parseColor("#000000"));
        activityMainBinding.message.setVisibility(View.GONE);
        switch (drawingType){
            case 0:
                if(select){
                    activityMainBinding.rectBtn.setChecked(true);
                }else{
                    activityMainBinding.rectBtn.setChecked(false);
                }
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.canvasBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.textBtn.setChecked(false);
                break;
            case 1:
                if(select){
                    activityMainBinding.circleBtn.setChecked(true);
                }else{
                    activityMainBinding.circleBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.canvasBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.textBtn.setChecked(false);
                break;
            case 2:
                if(select){
                    activityMainBinding.canvasBtn.setChecked(true);
                }else{
                    activityMainBinding.canvasBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.textBtn.setChecked(false);
                break;

            case 3:
                if(select){
                    activityMainBinding.textBtn.setChecked(true);
                    activityMainBinding.message.setVisibility(View.VISIBLE);
                }else{
                    activityMainBinding.textBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.canvasBtn.setChecked(false);

                break;
            case 10:
                if(select){
                    drawable.setTint(Color.parseColor("#00BCD4"));
                    activityMainBinding.touch.setImageDrawable(drawable);
                }else{
                    activityMainBinding.touch.setImageDrawable(drawable);
                }
                activityMainBinding.canvasBtn.setChecked(false);
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.textBtn.setChecked(false);
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

    LoadDialog loadDialog;
    final View.OnClickListener SaveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SaveHelper.newInstance().createCache(); // 创建缓存文件
            Date date = new Date();
            long time = date.getTime();
            String path = SaveHelper.newInstance().getCachePath() + "/" + time + ".jpg"; // 获取缓存路劲
            dragFrameLayoutView.setDrawingCacheEnabled(true);
            dragFrameLayoutView.buildDrawingCache();
            Bitmap drawingCache = dragFrameLayoutView.getDrawingCache(true);
            loadDialog = new LoadDialog(MainActivity.this);
            loadDialog.show();
            int i = SaveHelper.newInstance().SaveBitmap(drawingCache, path);
            handler.postDelayed(close,2000);

//            drawingCache.recycle();
            dragFrameLayoutView.destroyDrawingCache();
            dragFrameLayoutView.setDrawingCacheEnabled(false);

        }
    };

    Handler handler=new Handler();
    Runnable close=new Runnable() {
        @Override
        public void run() {
            loadDialog.dismiss();
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

}