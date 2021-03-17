package com.zhihuan.daoyi.cad.ui.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityAboutBinding;
import com.zhihuan.daoyi.cad.help.RecycleViewDivider;
import com.zhihuan.daoyi.cad.ui.adpterBean.AboutBean;
import com.zhihuan.daoyi.cad.ui.adpters.AboutAdpter;
import com.zhihuan.daoyi.cad.ui.adpters.RecentOpenAdpter;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    private AboutAdpter aboutAdpter;
    private List<AboutBean> listA;
    private String[] lables,values;
    {
        lables=new String[]{
                "版本信息","检查更新","联系我们"
        };
        values=new String[]{
                "1.0","已是最新","3379913221@qq.com"
        };
    }

    public static void start(Activity activity){
        Intent intent=new Intent();
        intent.setClass(activity,AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MacUtils.setStatusText(this,true);
        MacUtils.setRootView(this);
    }

    @Override
    protected ActivityAboutBinding getViewBinding() {
        return ActivityAboutBinding.inflate(getLayoutInflater(),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {
        listA=new ArrayList<>();
        for (int i = 0; i < lables.length; i++) {
            AboutBean aboutBean=new AboutBean();
            aboutBean.setName(lables[i]);
            aboutBean.setValue(values[i]);
            listA.add(aboutBean);
        }
        aboutAdpter=new AboutAdpter(this,listA);
    }

    @Override
    protected void init() {
        initTitle("关于我们");
        baseBinding.title.setLeftDrawable(R.drawable.ic_baseline_arrow_back_ios_24,0xff4F4F4F);
        baseBinding.title.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        viewBinding.recy.setAdapter(aboutAdpter);
        viewBinding.recy.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL,1,0xfff5f5f5));

    }
}
