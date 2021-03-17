package com.zhihuan.daoyi.cad.ui.Fragments;

import android.Manifest;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.tabs.TabLayout;
import com.zhihuan.daoyi.cad.Dialogs.LoadDialog;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.base.Constans;
import com.zhihuan.daoyi.cad.databinding.ShopFragmentBinding;
import com.zhihuan.daoyi.cad.help.RecycleViewDivider;
import com.zhihuan.daoyi.cad.help.SaveHelper;
import com.zhihuan.daoyi.cad.hodel.SpacesItemDecoration;
import com.zhihuan.daoyi.http.base.HttpMethods;
import com.zhihuan.daoyi.cad.ui.Activitys.MainActivity;
import com.zhihuan.daoyi.cad.ui.adpterBean.ShopBean;
import com.zhihuan.daoyi.cad.ui.adpters.ShopAdpter;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.utils.DateTimeUtil;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class ShopFragment extends BaseFragment<ShopFragmentBinding> {

    private ShopAdpter shopAdpter;
    private List<ShopBean> list;
    String[] stringArray; //tabs
    String downUrl="";
    private LoadDialog loadDialog;

    private String[] permission;
    {
        permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
    }

    @Override
    protected ShopFragmentBinding getViewBinding() {
        return ShopFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {
        stringArray= getResources().getStringArray(R.array.shoptabs);
        list=new ArrayList<>(10);
        loadData(Constans.srcCF,0);
        shopAdpter=new ShopAdpter(getActivity(),list);
    }

    @Override
    protected void init() {
        handlers=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==0x12){
                    down(downUrl);
                    return;
                }
            }
        };
        loadDialog=new LoadDialog(getActivity());
        loadDialog.setMessage("正在下载");
        initTitle("素材");
        baseBinding.titleBar.setLeftDrawable(null);
        addTabs();
        viewBinding.tab.addOnTabSelectedListener(tabSelectedListener);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        viewBinding.recy.setAdapter(shopAdpter);
        viewBinding.recy.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.HORIZONTAL,1,0xfff5f5f5));
        viewBinding.recy.addItemDecoration(new SpacesItemDecoration(MacUtils.dpto(8)));
        shopAdpter.setOnItemChildClickListener(itemChildClickListener);
    }

    private void loadData(int[] srcs, int type){
        for (int i = 0; i < srcs.length; i++) {
            ShopBean shopBean=new ShopBean();
            shopBean.setSrc(srcs[i]);
            shopBean.setDownSrc(Constans.downSrcBase+Constans.dowmSrcL[type]+(i+1)+".skp");
            shopBean.setTime("2020-03-17");
            list.add(shopBean);
        }
    }

    // tabs 添加
    public  void addTabs(){

        for (String s : stringArray) {
            if(viewBinding.tab!=null){
                viewBinding.tab.addTab(viewBinding.tab.newTab().setText(s));
            }
        }
    }

    TabLayout.OnTabSelectedListener tabSelectedListener=new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.e("tabselect",""+tab.getPosition());
            list.clear();
            switch (tab.getPosition()){
                case 0:
                    loadData(Constans.srcCF,0);
                    break;
                case 1:
                    loadData(Constans.srcChunag,1);
                    break;
                case 2:
                    loadData(Constans.srcErt,2);
                    break;
                case 3:
                    loadData(Constans.srcPF,3);
                    break;
                case 4:
                    loadData(Constans.srcQm,4);
                    break;
            }
            shopAdpter.notifyDataSetChanged();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    BaseQuickAdapter.OnItemChildClickListener itemChildClickListener=new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()){
                case R.id.goEdit:
                    if(null!=list&&list.size()>0&&list.size()>position){
                        FileBeans fileBeans=new FileBeans();
                        fileBeans.setName(new Date().getTime()+"");
                        fileBeans.setIsFavorites(0);
                        fileBeans.setP_type(10);
                        fileBeans.setIsRecent(1);
                        fileBeans.setTime(DateTimeUtil.DateToString(new Date(),"yyyy-MM-dd"));
                        fileBeans.setPath(list.get(position).getSrc()+"");
                        fileBeans.setType(1);
                        MainActivity.startFile(getActivity(),fileBeans);
                    }
                    break;
                case R.id.down_skp:
                    if(null!=list&&list.size()>0&&list.size()>position){
                        downUrl= (String) list.get(position).getDownSrc();
                        getPermissions(permission);

                    }

                    break;
            }
        }
    };

    private void down(String url){
        String parentpath = SaveHelper.newInstance().getCachePathSKP();
        File fileP=new File(parentpath);
        if(!fileP.exists()){
           fileP.mkdirs();
        }
        loadDialog.show();
        String path = SaveHelper.newInstance().getCachePathSKP() + "/" + new Date().getTime() + ".skp";
        File file=new File(path);
        HttpMethods.getInstance().download(url, file, new Subscriber() {
            @Override
            public void onCompleted() {
                loadDialog.dismiss();
                MacUtils.ToastShow(getActivity(),"下载完成",-2,0);
            }

            @Override
            public void onError(Throwable e) {
                loadDialog.dismiss();
            }

            @Override
            public void onNext(Object o) {


            }
        });
    }

}
