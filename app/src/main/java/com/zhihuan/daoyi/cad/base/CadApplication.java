package com.zhihuan.daoyi.cad.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class CadApplication extends Application {

    //static 代码段可以防止内存泄露
    //下拉刷新全局定义头布局跟尾布局的模式
    static{
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator(){
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout){
                //layout.setPrimaryColorsId(android.R.color.white, android.R.color.white);//全局设置主题颜色
                //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context).setTextSizeTime(10).setTextSizeTitle(14).setDrawableArrowSize(14).setDrawableSize(15);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator(){
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout){
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setTextSizeTitle(14).setDrawableSize(15);
            }
        });
    }
    private static  Context aContext;

    public CadApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.aContext=this;
        MacUtils.init(this);

    }
    public static Context aContext(){
        return aContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


}
