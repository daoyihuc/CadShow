package com.zhihuan.daoyi.cad.ui.Activitys;

import android.app.Activity;
import android.content.Intent;

import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityAboutBinding;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {


    public static void start(Activity activity){
        Intent intent=new Intent();
        intent.setClass(activity,AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected ActivityAboutBinding getViewBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater(),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void init() {
        initTitle("关于我们");
    }
}
