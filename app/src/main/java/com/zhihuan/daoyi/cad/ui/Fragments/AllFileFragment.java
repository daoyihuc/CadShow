package com.zhihuan.daoyi.cad.ui.Fragments;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.AllFragmentBinding;
import com.zhihuan.daoyi.cad.databinding.RecentOpenFragmentBinding;
import com.zhihuan.daoyi.cad.help.RoomHelper;
import com.zhihuan.daoyi.cad.ui.Activitys.MainActivity;
import com.zhihuan.daoyi.cad.ui.adpterBean.CacheBean;
import com.zhihuan.daoyi.cad.ui.adpterBean.FileBean;
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

    String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/";

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
        queryF(false);

    }
    // 文件查询
    public void queryF(boolean fal){
        FileBeans fileBeans=new FileBeans();
        fileBeans.isFavorites=1;
        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<Object> baseFObservable = StartObservable(a, DATABASES.QUERYF);
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
    // 是否存在
    private void queryFN(FileBeans fileBeans){

        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<Object> baseFObservable = StartObservable(a, DATABASES.QUERYFN);
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
                Log.e("当前数量",""+((int)o));
               if(((int)o)>0){
                   UpdateFN(fileBeans);
               }else{
                   InsertFN(fileBeans);
               }

            }
        },baseFObservable);
    }

    // 更新
    private void UpdateFN(FileBeans fileBeans){

        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<Object> baseFObservable = StartObservable(a, DATABASES.UPDATE);
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
                if(((BaseF)o).getCode()==200){
                    queryF(false);
                }
            }
        },baseFObservable);
    }


    // 是否存在
    private void InsertFN(FileBeans fileBeans){

        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<Object> baseFObservable = StartObservable(a, DATABASES.INSERT);
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
                if(((BaseF)o).getCode()==200){
                    queryF(false);
                }
            }
        },baseFObservable);
    }




    public final Observer<List<FileBeans>> observableAll=new Observer<List<FileBeans>>() {
        @Override
        public void onChanged(List<FileBeans> fileBeans) {
            Log.e("daoyi_live",fileBeans.size()+"");
            Log.e("daoyi_live",fileBeans.toString()+"");
//            linkedList.clear();
            AllList.clear();
            AllList.addAll(fileBeans);
            getFileList(path);
        }
    };




    @Override
    protected void init() {
        handlers=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                for (FileBeans fileBeans : listFile) {
                    if(fileBeans.isFavorites==1){
                        Log.e("daoyiFavor",fileBeans.toString());
                    }
                }
                adpter.notifyDataSetChanged();
                setLabel();
            }
        };

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        adpter=new AllOpenAdpter(getActivity(),listFile);

        // 设置空布局
        emptyView=new EmptyView(getActivity());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        adpter.setEmptyView(emptyView);
        emptyView.setText(getResources().getString(R.string.recent_empty));
        adpter.setOnItemChildClickListener(childClickListener);
        adpter.setHasStableIds(true);
        viewBinding.recy.setAdapter(adpter);
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
                    if(null!=listFile&&listFile.size()>0){
                        FileBeans fileBeans = listFile.get(position);
                        if(fileBeans.getIsFavorites()==0){
                            listFile.get(position).setIsFavorites(1);
                            fileBeans.setIsFavorites(1);
                        }else{
                            listFile.get(position).setIsFavorites(0);
                            fileBeans.setIsFavorites(0);
                        }
                        adpter.notifyDataSetChanged();
//                        path=fileBeans.path;
                        queryFN(fileBeans);

                    }
                    break;
                case R.id.box:
                    if(null!=listFile&&listFile.size()>0){
                        if(listFile.get(position).type==1){
                            FileBeans fileBeans = listFile.get(position);
                            fileBeans.setIsRecent(1);
                            if(fileBeans.getP_type()==1||fileBeans.getP_type()==2||
                                    fileBeans.getP_type()==10
                            ){
                                MainActivity.startFile(getActivity(),fileBeans);
                            }

                        }else{
                            path=listFile.get(position).path;
                            queryF(false);
                        }

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
                    listFile.clear();
                    linkedList.clear();
                    queryF(false);
                    handlers.sendEmptyMessage(0x11);

                }else if(linkedList.size()>1){
                    linkedList.removeLast();
                    path=linkedList.get(linkedList.size()-1).getPath();
                    queryF(false);
                }
            }
        }
    };


}
