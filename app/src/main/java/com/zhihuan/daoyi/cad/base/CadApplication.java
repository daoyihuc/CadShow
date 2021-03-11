package com.zhihuan.daoyi.cad.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
