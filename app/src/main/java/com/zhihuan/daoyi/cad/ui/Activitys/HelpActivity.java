package com.zhihuan.daoyi.cad.ui.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.databinding.ActivityAboutBinding;
import com.zhihuan.daoyi.cad.databinding.ActivityHelpBinding;
import com.zhihuan.daoyi.cad.help.RecycleViewDivider;
import com.zhihuan.daoyi.cad.ui.adpterBean.HelpBean;
import com.zhihuan.daoyi.cad.ui.adpters.HelpAdpter;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class HelpActivity extends BaseActivity<ActivityHelpBinding> {

    private HelpAdpter helpAdpter;
    String[] question;
    String[] url;
    private List<HelpBean> list;

    {
        question = new String[]{
                "如何打开QQ,微信图纸？",
                "如何找到并打开图纸？",
                "云盘的使用？",
                "怎么在满满的绘制中拖动指定图层",
        };
        url = new String[]{
                " http://cadclub.glodon.com/question/detail/23%20",
                "http://cadclub.glodon.com/question/detail/20",
                "http://cadclub.glodon.com/question/detail/56",
                "http://cadclub.glodon.com/question/detail/95"
        };
    }

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, HelpActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MacUtils.setStatusText(this, true);
        MacUtils.setRootView(this);
    }

    @Override
    protected ActivityHelpBinding getViewBinding() {
        return ActivityHelpBinding.inflate(getLayoutInflater(), baseBinding.getRoot(), true);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>(5);
        for (int i = 0; i < question.length; i++) {
            HelpBean helpBean = new HelpBean();
            helpBean.setName(question[i]);
            helpBean.setUrl(url[i]);
            list.add(helpBean);
        }
        helpAdpter = new HelpAdpter(list);
    }

    @Override
    protected void init() {
        initTitle("新手指南");
        baseBinding.title.setLeftDrawable(R.drawable.ic_baseline_arrow_back_ios_24, 0xff4F4F4F);
        baseBinding.title.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        viewBinding.recy.setAdapter(helpAdpter);
        viewBinding.recy.addItemDecoration(new RecycleViewDivider(this, LinearLayout.HORIZONTAL, 1, 0xfff5f5f5));

        helpAdpter.setOnItemClickListener(onItemClickListener);
    }

    BaseQuickAdapter.OnItemClickListener onItemClickListener=new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if(null!=list&&list.size()>0&&list.size()>position){
                WebViewActivity.start(HelpActivity.this,3,list.get(position).getUrl());
            }
        }
    };
}
