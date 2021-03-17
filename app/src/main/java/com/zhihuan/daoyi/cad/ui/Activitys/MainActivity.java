package com.zhihuan.daoyi.cad.ui.Activitys;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.zhihuan.daoyi.cad.Dialogs.LoadDialog;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityMainBinding;
import com.zhihuan.daoyi.cad.help.RoomHelper;
import com.zhihuan.daoyi.cad.help.SaveHelper;
import com.zhihuan.daoyi.cad.interfaces.TouchEventListener;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;
import com.zhihuan.daoyi.cad.utils.MacUtils;
import com.zhihuan.daoyi.cad.views.DragBaseView;
import com.zhihuan.daoyi.cad.views.DragFrameLayoutView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    ActivityMainBinding activityMainBinding;
    boolean drawingMode = false; //  绘制事件
    int drawingType = 10; // 0： 矩形， 1： 圆形  2: 画笔 3: 画笔
    boolean outSelect = false;
    private String name = "样板图纸编辑";
    private String OkTitle = "保存";
    int url;
    String urlPath;
    private String[] permission;
    Observable<Object> baseFObservable;

    {
        permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
    }

    DragFrameLayoutView dragFrameLayoutView;

    int type = 0;// 默认是样列 1：需要保存到最近打开文件
    int isDrawble=0;// 0： drawble 1: string

    // 草图预览编辑
    public static void start(Activity activity, int url) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("url", url);
        bundle.putInt("type", 0);
        intent.putExtra("file", bundle);
        activity.startActivity(intent);
    }

    public static void startFile(Activity activity, FileBeans url) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("url", url);
        bundle.putInt("type", 1);
        intent.putExtra("file", bundle);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MacUtils.initWindow(this, 0xffffffff, false, null, true);
        baseBinding.maxBox.setVisibility(View.GONE);
        baseBinding.maxBox2.setVisibility(View.VISIBLE);
        initTitle("");
        activityMainBinding = viewBinding;
        dragFrameLayoutView = activityMainBinding.maxBox;
        baseBinding.title.setCenterTitle(name);
        baseBinding.title.setRightTitle(OkTitle);
        baseBinding.title.setRightOnClickListener(SaveClick);
        baseBinding.title.setLeftOnClickListener(leftOnClick);
        activityMainBinding.maxBox.setListener(touchEventListener);
        activityMainBinding.rectBtn.setOnClickListener(v -> {
            drawingType = 0;
            setCheck(!drawingMode);

        });
        activityMainBinding.circleBtn.setOnClickListener(v -> {
            drawingType = 1;
            setCheck(!drawingMode);
        });
        activityMainBinding.canvasBtn.setOnClickListener(v -> {
            drawingType = 2;
            setCheck(!drawingMode);
        });

        activityMainBinding.touch.setOnClickListener(v -> {
            drawingType = 10;
            setCheck(!drawingMode);
        });
        activityMainBinding.textBtn.setOnClickListener(v -> {
//            Toast.makeText(this,"dsd",Toast.LENGTH_LONG).show();
            drawingType = 3;
            setCheck(!drawingMode);
        });
        getPermissions(permission);
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater(), baseBinding.maxBox2, true);
    }

    @Override
    protected void initData() {
        db = RoomHelper.newInstance().db();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("file");

        if (bundle.containsKey("type")) {
            type=bundle.getInt("type");
            if(type==0){
                url = bundle.getInt("url");
                if (url != 0) {
                    name = "草图编辑中";
                }
            }else{
                FileBeans fileBeans=bundle.getParcelable("url");
                if(fileBeans.getP_type()==10){
                    isDrawble=0;
                    url=Integer.parseInt(fileBeans.getPath());
                }else{
                    isDrawble=1;
                    urlPath=fileBeans.getPath();
                }
                name = "草图编辑中";
                List<FileBeans> ls=new ArrayList<>();
                ls.add(fileBeans);
                objectObservable = StartObservable(ls, DATABASES.QUERYRP);
                baseFObservable = StartObservable(ls, DATABASES.INSERT);
            }
        }

    }

    @Override
    protected void init() {

        if(isDrawble==0){
            if (url != 0) {
                Glide.with(this).load(url).into(viewBinding.back);
            }
        }else{
            Glide.with(this).load(urlPath).into(viewBinding.back);
        }

    }

    // 设置选中
    private void setCheck(boolean select) {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_touch_app_24);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(Color.parseColor("#000000"));
        }
        activityMainBinding.message.setVisibility(View.GONE);
        switch (drawingType) {
            case 0:
                if (select) {
                    activityMainBinding.rectBtn.setChecked(true);
                } else {
                    activityMainBinding.rectBtn.setChecked(false);
                }
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.canvasBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.textBtn.setChecked(false);
                break;
            case 1:
                if (select) {
                    activityMainBinding.circleBtn.setChecked(true);
                } else {
                    activityMainBinding.circleBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.canvasBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.textBtn.setChecked(false);
                break;
            case 2:
                if (select) {
                    activityMainBinding.canvasBtn.setChecked(true);
                } else {
                    activityMainBinding.canvasBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.textBtn.setChecked(false);
                break;

            case 3:
                if (select) {
                    activityMainBinding.textBtn.setChecked(true);
                    activityMainBinding.message.setVisibility(View.VISIBLE);
                } else {
                    activityMainBinding.textBtn.setChecked(false);
                }
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.touch.setImageDrawable(drawable);
                activityMainBinding.canvasBtn.setChecked(false);

                break;
            case 10:
                if (select) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTint(Color.parseColor("#00BCD4"));
                    }
                    activityMainBinding.touch.setImageDrawable(drawable);
                } else {
                    activityMainBinding.touch.setImageDrawable(drawable);
                }
                activityMainBinding.canvasBtn.setChecked(false);
                activityMainBinding.rectBtn.setChecked(false);
                activityMainBinding.circleBtn.setChecked(false);
                activityMainBinding.textBtn.setChecked(false);
                break;
        }
        drawingMode = select;
    }


    TouchEventListener touchEventListener = new TouchEventListener() {
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
            Bitmap bitmap = SaveHelper.newInstance().getBitmapView(dragFrameLayoutView);
            loadDialog = new LoadDialog(MainActivity.this);
            loadDialog.show();
            int i = SaveHelper.newInstance().SaveBitmap(bitmap, path);
            toSubscribe(new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {
                    if(((List<FileBeans>)o).size()==0){
                        insertRecent();
                    }

                }
            },objectObservable);

//            drawingCache.recycle();
            dragFrameLayoutView.destroyDrawingCache();
            dragFrameLayoutView.setDrawingCacheEnabled(false);

        }
    };


    View.OnClickListener leftOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    Handler handler = new Handler();
    Runnable close = new Runnable() {
        @Override
        public void run() {
            loadDialog.dismiss();
            onBackPressed();
        }
    };


    // 添加最近打开记录
    public void insertRecent(){
        toSubscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//
            }

            @Override
            public void onNext(Object o) {
                handler.postDelayed(close, 2000);
            }
        },baseFObservable);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }

}