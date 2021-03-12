package com.zhihuan.daoyi.cad.ui.Fragments;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zhihuan.daoyi.cad.base.BaseFragment;
import com.zhihuan.daoyi.cad.databinding.RecentOpenFragmentBinding;
import com.zhihuan.daoyi.cad.help.RoomHelper;
import com.zhihuan.daoyi.cad.ui.adpterBean.FileBean;
import com.zhihuan.daoyi.cad.ui.adpters.RecentOpenAdpter;
import com.zhihuan.daoyi.cad.ui.room.AppDatabase;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;

import java.util.ArrayList;
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
    private List<FileBeans> listFile; // 文件存储
    LiveData<List<FileBeans>> listLiveData;
    AppDatabase db;



    @Override
    protected RecentOpenFragmentBinding getViewBinding() {
        return RecentOpenFragmentBinding.inflate(LayoutInflater.from(getActivity()),baseBinding.getRoot(),true);
    }

    @Override
    protected void init() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBinding.recy.setLayoutManager(linearLayoutManager);
        adpter=new RecentOpenAdpter(getActivity(),listFile);
        viewBinding.recy.setAdapter(adpter);

    }

    @Override
    protected void initData() {
        listFile=new ArrayList<>();
        db=RoomHelper.newInstance().db();


        FileBeans fileBeans=new FileBeans();
        fileBeans.name="daoyi";
        fileBeans.isFavorites=1;
        fileBeans.time="2021-3-18";
        fileBeans.p_type=0;
        fileBeans.path="http://www.daoyiksw.cn";
        fileBeans.type=0;
        List<FileBeans> a=new ArrayList<>();
        a.add(fileBeans);
        Observable<BaseF> baseFObservable = StartObservable(a, DATABASES.QUERY);
        toSubscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                listLiveData.observe(getActivity(),observable);
            }
        },baseFObservable);


    }
    public Observable<BaseF> StartObservable(List<FileBeans> va, DATABASES type){
        Observable<BaseF> objectObservable = Observable.create(s -> {
            switch (DATABASES.stateOf(type.getType())){
                case INSERT:
                    db.fileDao().insert(va.get(0));
                    break;
                case QUERY:
                   listLiveData = db.fileDao().queryAll();
                    break;
                case DELETE:
                    db.fileDao().delete(va.get(0));
                    break;
                case UPDATE:
                    db.fileDao().update(va.get(0));
                    break;
            }
            s.onNext(new BaseF());
            s.onCompleted();
        });
        return objectObservable;
    }


    final Observer<List<FileBeans>> observable=new Observer<List<FileBeans>>() {
        @Override
        public void onChanged(List<FileBeans> fileBeans) {
            Log.e("daoyi_live",fileBeans.size()+"");
            Log.e("daoyi_live",fileBeans.toString()+"");
            listFile.addAll(fileBeans);
        }
    };

    private void toSubscribe(Subscriber subscriber,Observable observable){
        observable.subscribeOn(Schedulers.io())//指定订阅关系发生在IO线程
                .unsubscribeOn(Schedulers.io())//指定解绑发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//指回调发生在主线程
                .subscribe(subscriber);//创建订阅关系
    }

}

