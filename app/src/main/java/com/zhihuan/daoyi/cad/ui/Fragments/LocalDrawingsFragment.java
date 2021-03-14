package com.zhihuan.daoyi.cad.ui.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.LocalDrawingsFragmentBinding;
import com.zhihuan.daoyi.cad.ui.adpters.LocalAdapter;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class LocalDrawingsFragment extends BaseFragment<LocalDrawingsFragmentBinding> {

    String[] stringArray; //tabs
    List<Fragment> list;
    private LocalAdapter localAdapter;

    @Override
    protected LocalDrawingsFragmentBinding getViewBinding() {
        return LocalDrawingsFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }
    @Override
    protected void initData() {
        stringArray= getResources().getStringArray(R.array.tabs);
        list=new ArrayList<>();
        list.add( new RecentOpenFragment());
        list.add( new RecentOpenFragment());
        list.add( new AllFileFragment());

    }

    @Override
    protected void init() {

        addTabs();
        // 绑定TabLayout 与 ViewPager 关联
        localAdapter=new LocalAdapter(getChildFragmentManager(),list);
        viewBinding.viewPager.setAdapter(localAdapter);
//        viewBinding.tab.setupWithViewPager(viewBinding.viewPager);

    }
    // tabs 添加
    public  void addTabs(){

        for (String s : stringArray) {
            if(viewBinding.tab!=null){
                viewBinding.tab.addTab(viewBinding.tab.newTab().setText(s));
            }

        }
    }
    // 自定义view添加
    TextView addCustview(String val){
        TextView view=new TextView(getActivity());
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(-1,-2);
        view.setLayoutParams(params);
        view.setText(val);
        view.setPadding(MacUtils.dpto(10),MacUtils.dpto(5),MacUtils.dpto(10),MacUtils.dpto(5));
        view.setTextColor(getResources().getColor(R.color.black));
        view.setBackground(getResources().getDrawable(R.drawable.tabshap_select));
        return view;
    }


}
