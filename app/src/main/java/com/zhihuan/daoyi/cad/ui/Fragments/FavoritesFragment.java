package com.zhihuan.daoyi.cad.ui.Fragments;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.AllFragmentBinding;
import com.zhihuan.daoyi.cad.databinding.FavoritesFragmentBinding;
import com.zhihuan.daoyi.cad.databinding.RecentOpenFragmentBinding;
import com.zhihuan.daoyi.cad.help.RoomHelper;
import com.zhihuan.daoyi.cad.ui.adpterBean.CacheBean;
import com.zhihuan.daoyi.cad.ui.adpters.FavoritesOpenAdpter;
import com.zhihuan.daoyi.cad.ui.adpters.RecentOpenAdpter;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;
import com.zhihuan.daoyi.cad.views.EmptyView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “收藏”
 * @date :
 */
public class FavoritesFragment extends BaseFragment<FavoritesFragmentBinding> {

    private FavoritesOpenAdpter adpter; // 最近查看
    private EmptyView emptyView;
//    private List<FileBeans> AllList;


    @Override
    protected FavoritesFragmentBinding getViewBinding() {
        return FavoritesFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {
        listFile=new ArrayList<>();
        linkedList=new LinkedList<CacheBean>();
        AllList=new ArrayList<>();
        db= RoomHelper.newInstance().db();
        initFile();
        queryF();
    }

    @Override
    protected void init() {
        handlers=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                isF();
                adpter.notifyDataSetChanged();
                setLabel();
                Log.e("daoyiFile",listFile.toString());
            }
        };

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        adpter=new FavoritesOpenAdpter(getActivity(),listFile);
        viewBinding.recy.setAdapter(adpter);
        // 设置空布局
        emptyView=new EmptyView(getActivity());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        adpter.setEmptyView(emptyView);
        emptyView.setText(getResources().getString(R.string.recent_empty));

        adpter.setOnItemChildClickListener(childClickListener);

        viewBinding.back.setOnClickListener(back);
    }
    // 文件查询
    private void queryF(){

        FileBeans fileBeans=new FileBeans();
        fileBeans.isFavorites=1;
        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<BaseF> baseFObservable = StartObservable(a, DATABASES.QUERYF);
        toSubscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
//
            }

            @Override
            public void onNext(Object o) {
                listLiveData.observe(getActivity(),observableAll);
            }
        },baseFObservable);
    }
    private void setLabel(){
        StringBuilder stringBuilder=new StringBuilder();
        for (CacheBean cacheBean : linkedList) {
            stringBuilder.append(cacheBean.getName()+"/");
        }
        viewBinding.label2.setText("");
        viewBinding.label2.setText(stringBuilder);
    }


    public final Observer<List<FileBeans>> observableAll=new Observer<List<FileBeans>>() {
        @Override
        public void onChanged(List<FileBeans> fileBeans) {
            Log.e("daoyi_live",fileBeans.size()+"");
            Log.e("daoyi_live",fileBeans.toString()+"");
            listFile.addAll(fileBeans);
            AllList.addAll(fileBeans);
            isF();
        }
    };


    //
    BaseQuickAdapter.OnItemChildClickListener childClickListener=new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()){
                case R.id.start:

                    break;
                case R.id.box:
                    if(null!=listFile&&listFile.size()>0){
                        String path=listFile.get(position).path;

                        getFileList(path);
                    }
                    break;
            }
        }
    };
    // 返回事件
    View.OnClickListener back=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(null!=linkedList&&linkedList.size()>0){


                if(linkedList.size()==1){
                   queryF();
                   listFile.clear();
                   linkedList.clear();
                   handlers.sendEmptyMessage(0x11);

                }else if(linkedList.size()>1){
                    linkedList.removeLast();
                    String path=linkedList.get(linkedList.size()-1).getPath();
                    getFileList(path);

                }

            }
        }
    };
    // 判断是否是收藏
    private void isF(){

        for (int i = 0; i < listFile.size(); i++) {
            for (int i1 = 0; i1 < AllList.size(); i1++) {
                if(listFile.get(i).path.equals(AllList.get(i1).path)){
                    listFile.get(i).isFavorites=1;
                }else{
                }
            }
        }
        adpter.notifyDataSetChanged();
    }
}
