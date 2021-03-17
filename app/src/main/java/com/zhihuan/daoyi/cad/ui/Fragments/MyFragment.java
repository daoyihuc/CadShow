package com.zhihuan.daoyi.cad.ui.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.MyFragmentBinding;
import com.zhihuan.daoyi.cad.help.RecycleViewDivider;
import com.zhihuan.daoyi.cad.ui.Activitys.AboutActivity;
import com.zhihuan.daoyi.cad.ui.Activitys.HelpActivity;
import com.zhihuan.daoyi.cad.ui.Activitys.WebViewActivity;
import com.zhihuan.daoyi.cad.ui.adpterBean.MyBean;
import com.zhihuan.daoyi.cad.ui.adpters.MyAdpter;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class MyFragment extends BaseFragment<MyFragmentBinding> {

    private MyAdpter myAdpter;
    private List<String> nameL; // 名称
    private List<Integer> iconL; // 名称
    private List<MyBean> listData; // 主数据

    @Override
    protected MyFragmentBinding getViewBinding() {
        return MyFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {
        listData=new ArrayList<>(5);
        iconL=new ArrayList<>(4);
        iconL.add(R.drawable.help); // 指南
        iconL.add(R.drawable.about); // 关于我们
//        iconL.add(R.drawable.about); // 关于我们
        iconL.add(R.drawable.yhxy1); // 用户协议
        iconL.add(R.drawable.ysxy); // 隐私政策
        String[] stringArray = getActivity().getResources().getStringArray(R.array.my_list);
        nameL=new ArrayList<>(Arrays.asList(stringArray));
        for (int i = 0; i < nameL.size(); i++) {
            MyBean myBean = new MyBean();
            myBean.setIcon(iconL.get(i));
            myBean.setName(nameL.get(i));
            listData.add(myBean);
        }


    }

    @Override
    protected void init() {

        myAdpter=new MyAdpter(getActivity(),listData);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        viewBinding.recy.setAdapter(myAdpter);
        viewBinding.recy.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayout.HORIZONTAL,1,0xfff5f5f5));
        myAdpter.setOnItemClickListener(itemClickListener);



    }

    BaseQuickAdapter.OnItemClickListener itemClickListener=new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            if(null!=listData&&listData.size()>0&& listData.size()>position){
                switch (position){
                    case 0:
                        HelpActivity.start(getActivity());
                        break;
                    case 1:
                        AboutActivity.start(getActivity());
                        break;
                    case 2:
                        WebViewActivity.start(getActivity(),0,"s");
                        break;
                    case 3:
                        WebViewActivity.start(getActivity(),1,"s");
                        break;
                }
            }
        }
    };
}
