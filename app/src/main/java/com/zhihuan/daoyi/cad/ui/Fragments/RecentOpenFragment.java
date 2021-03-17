package com.zhihuan.daoyi.cad.ui.Fragments;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.RecentOpenFragmentBinding;
import com.zhihuan.daoyi.cad.help.RoomHelper;
import com.zhihuan.daoyi.cad.ui.Activitys.MainActivity;
import com.zhihuan.daoyi.cad.ui.adpterBean.CacheBean;
import com.zhihuan.daoyi.cad.ui.adpterBean.FileBean;
import com.zhihuan.daoyi.cad.ui.adpters.RecentOpenAdpter;
import com.zhihuan.daoyi.cad.ui.room.AppDatabase;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;
import com.zhihuan.daoyi.cad.views.EmptyView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhihuan.daoyi.cad.ui.room.types.DATABASES.INSERT;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class RecentOpenFragment extends BaseFragment<RecentOpenFragmentBinding> {

    private RecentOpenAdpter adpter; // 最近查看
    private EmptyView emptyView;

    @Override
    protected RecentOpenFragmentBinding getViewBinding() {
        return RecentOpenFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void init() {
        handlers=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(adpter!=null){
                    adpter.notifyDataSetChanged();
                }

            }
        };

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        adpter=new RecentOpenAdpter(getActivity(),listFile);
        viewBinding.recy.setAdapter(adpter);
        // 设置空布局
        emptyView=new EmptyView(getActivity());
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(-1,-1));
        adpter.setEmptyView(emptyView);
        emptyView.setText(getResources().getString(R.string.recent_empty));
        adpter.setOnItemChildClickListener(onItemClickListener);
    }

    BaseQuickAdapter.OnItemChildClickListener onItemClickListener=new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()){
                case R.id.box:
                    if(null!=listFile&&listFile.size()>0){
                        FileBeans fileBeans = listFile.get(position);
                        fileBeans.setIsRecent(1);
                        if(fileBeans.getP_type()==1||fileBeans.getP_type()==2||
                                fileBeans.getP_type()==10
                        ){
                            MainActivity.startFile(getActivity(),fileBeans);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        listFile=new ArrayList<>();
        linkedList=new LinkedList<CacheBean>();
        db=RoomHelper.newInstance().db();


        FileBeans fileBeans=new FileBeans();
        fileBeans.isRecent=1;
        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<Object> baseFObservable = StartObservable(a, DATABASES.QUERYR);
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
                listLiveData.observe(getActivity(),observable);
            }
        },baseFObservable);

    }

}

