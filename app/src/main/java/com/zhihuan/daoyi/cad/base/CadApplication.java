package com.zhihuan.daoyi.cad.base;

import android.app.Application;
import android.content.Context;

import com.zhihuan.daoyi.cad.utils.MacUtils;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class CadApplication extends Application {
    public CadApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MacUtils.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
