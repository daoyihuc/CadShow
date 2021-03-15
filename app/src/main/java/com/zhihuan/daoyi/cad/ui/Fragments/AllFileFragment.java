package com.zhihuan.daoyi.cad.ui.Fragments;

import android.os.Environment;
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
import com.zhihuan.daoyi.cad.databinding.RecentOpenFragmentBinding;
import com.zhihuan.daoyi.cad.help.RoomHelper;
import com.zhihuan.daoyi.cad.ui.adpterBean.CacheBean;
import com.zhihuan.daoyi.cad.ui.adpters.AllOpenAdpter;
import com.zhihuan.daoyi.cad.ui.adpters.RecentOpenAdpter;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;
import com.zhihuan.daoyi.cad.views.EmptyView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import rx.Observable;
import rx.Subscriber;

/**
 * @author: "daoyi"
 * @date:
 * @params: "全部"
 */
public class AllFileFragment extends BaseFragment<AllFragmentBinding> {

    private AllOpenAdpter adpter; // 最近查看
    private EmptyView emptyView;


    @Override
    protected AllFragmentBinding getViewBinding() {
        return AllFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void initData() {
        db= RoomHelper.newInstance().db();
        listFile= new CopyOnWriteArrayList<>();
        linkedList=new LinkedList<CacheBean>();
        AllList=new ArrayList<>();
        queryF();

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

    public final Observer<List<FileBeans>> observableAll=new Observer<List<FileBeans>>() {
        @Override
        public void onChanged(List<FileBeans> fileBeans) {
            Log.e("daoyi_live",fileBeans.size()+"");
            Log.e("daoyi_live",fileBeans.toString()+"");
            AllList.addAll(fileBeans);
            getFileList(Environment.getExternalStorageDirectory().getAbsolutePath()+"/");
        }
    };



    @Override
    protected void init() {
        handlers=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                adpter.notifyDataSetChanged();
                isF();
                setLabel();
            }
        };

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        adpter=new AllOpenAdpter(getActivity(),listFile);
        viewBinding.recy.setAdapter(adpter);
        // 设置空布局
        emptyView=new EmptyView(getActivity());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        adpter.setEmptyView(emptyView);
        emptyView.setText(getResources().getString(R.string.recent_empty));
        adpter.setOnItemChildClickListener(childClickListener);

        viewBinding.back.setOnClickListener(back);
    }

    private void setLabel(){
        StringBuilder stringBuilder=new StringBuilder();
        for (CacheBean cacheBean : linkedList) {
            stringBuilder.append(cacheBean.getName()+"/");
        }
        viewBinding.label2.setText("");
        viewBinding.label2.setText(stringBuilder);
    }

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
                if(linkedList.size()>1){
                    linkedList.removeLast();
                }
                String path=linkedList.get(linkedList.size()-1).getPath();
                if(linkedList.size()==1){
                    linkedList.removeLast();
                }
                getFileList(path);
            }
        }
    };

    // 判断是否是收藏
    private void isF(){

//        if(null==AllList){
//            return;
//        }
//        if(null==listFile){
//            return;
//        }
        for (int i = 0; i < listFile.size(); i++) {
            for (int i1 = 0; i1 < AllList.size(); i1++) {
                if(listFile.get(i).name.equals(AllList.get(i1).name)){
                    Log.e("daoyis",listFile.get(i).name+":__"+AllList.get(i1).name+"::__"+listFile.get(i).name.equals(AllList.get(i1).name));
                    listFile.get(i).isFavorites=1;
                }else{
                    Log.e("daoyis",listFile.get(i).name+":__"+AllList.get(i1).name+"::__"+listFile.get(i).name.equals(AllList.get(i1).name));
                }
            }
        }
        adpter.notifyDataSetChanged();
    }
}
