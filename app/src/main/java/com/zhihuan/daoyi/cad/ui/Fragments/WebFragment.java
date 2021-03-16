package com.zhihuan.daoyi.cad.ui.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.AllFragmentBinding;
import com.zhihuan.daoyi.cad.databinding.WebFragmentBinding;
import com.zhihuan.daoyi.cad.ui.Activitys.LoginActivity;
import com.zhihuan.daoyi.cad.ui.adpters.AllOpenAdpter;
import com.zhihuan.daoyi.cad.ui.adpters.RecentOpenAdpter;
import com.zhihuan.daoyi.cad.ui.adpters.WebAdpter;
import com.zhihuan.daoyi.cad.views.EmptyView;
import com.zhihuan.daoyi.cad.views.Titlabar;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class WebFragment extends BaseFragment<WebFragmentBinding> {
    private WebAdpter adpter; // 云端文件
    private EmptyView emptyView;


    @Override
    protected WebFragmentBinding getViewBinding() {
        return WebFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void init() {
        initTitle("云盘空间");
        baseBinding.titleBar.setLeftDrawable(null);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        adpter=new WebAdpter(getActivity(),listFile);
        viewBinding.recy.setAdapter(adpter);
        // 设置空布局
        emptyView=new EmptyView(getActivity());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        adpter.setEmptyView(emptyView);
        emptyView.setText(getResources().getString(R.string.web_empty_no));
        emptyView.setLogoImg(R.drawable.webback);
        emptyView.setOnclick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(getActivity());
            }
        });
    }
}
