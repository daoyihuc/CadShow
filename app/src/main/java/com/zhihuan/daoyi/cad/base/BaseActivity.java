package com.zhihuan.daoyi.cad.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.databinding.ActivityBaseBinding;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected ActivityBaseBinding baseBinding;
    public T viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseBinding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(baseBinding.getRoot());
        viewBinding = getViewBinding();
        initData();
        init();
    }

    protected abstract T getViewBinding();
    protected  abstract void initData();
    protected  abstract void init();

    // 初始化头部
    public void initTitle(CharSequence title) {
        baseBinding.title.init();
        baseBinding.title.setCenterTitle(""+title);
        baseBinding.title.setCenterColor(0xff000000);
        baseBinding.title.setCenterFontSize(18);
        baseBinding.title.setBackGroundColor(0xffffffff);
        baseBinding.title.setLeftDrawable(R.drawable.ic_baseline_arrow_back_ios_24,0xff000000);
        baseBinding.title.setRightTitle("");
        baseBinding.title.setRightFontSize(18);
        baseBinding.title.setRightColor(0xff000000);
        baseBinding.title.setLeftMargin(MacUtils.dpto(10),0,0,0);
        baseBinding.title.setRightMargin(0,0,MacUtils.dpto(10),0);
        baseBinding.title.addviews();
        baseBinding.title.setVisibility(View.VISIBLE);
    }
    // 权限获取
    @SuppressLint("CheckResult")
    protected  void getPermissions(String[] permission){
        RxPermissions rxPermissions= new RxPermissions(this);
        rxPermissions.setLogging(true);
        Observable<Permission> permissionObservable = rxPermissions.requestEach(permission);
        permissionObservable.subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // 用户已经同意该权限
                    Log.d("daoyi", "text_sms granted" );
                    //result.agree(permission);
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                    //result.refuse(permission);
                    Log.d("daoyi", "text_sms shouldShowRequestPermissionRationale" );
                } else {
                    // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                    //result.noMoreQuestions(permission);
                    Log.d("daoyi", "text_sms " );
                }
            }
        });
    }



}