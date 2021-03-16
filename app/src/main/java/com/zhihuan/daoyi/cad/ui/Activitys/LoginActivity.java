package com.zhihuan.daoyi.cad.ui.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.jaeger.library.StatusBarUtil;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityLoginBinding;
import com.zhihuan.daoyi.cad.utils.MacUtils;
import com.zhihuan.daoyi.cad.utils.TimeUtils;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements OnClickListener {

    TimeUtils timeUtils;


    public static  void start(Activity activity){
        Intent intent=new Intent();
        intent.setClass(activity,LoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MacUtils.setStatusText(this,true);
        StatusBarUtil.setColor(this,0xffffffff,1);
    }

    @Override
    protected ActivityLoginBinding getViewBinding() {
        return ActivityLoginBinding.inflate(LayoutInflater.from(this),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void init() {
        timeUtils=new TimeUtils(viewBinding.codeBtn,"#000000");
        viewBinding.codeBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.codeBtn:
                timeUtils.RunTimer();
                break;
        }
    }
}
