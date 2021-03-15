package com.zhihuan.daoyi.cad.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.viewbinding.ViewBinding;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.databinding.ActivityBaseBinding;
import com.zhihuan.daoyi.cad.databinding.BaseFragmentBinding;
import com.zhihuan.daoyi.cad.help.SaveHelper;
import com.zhihuan.daoyi.cad.ui.adpterBean.CacheBean;
import com.zhihuan.daoyi.cad.ui.room.AppDatabase;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;
import com.zhihuan.daoyi.cad.utils.DateTimeUtil;
import com.zhihuan.daoyi.cad.utils.MacUtils;
import com.zhihuan.daoyi.cad.utils.SPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.LinkedArrayList;
import rx.schedulers.Schedulers;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public abstract class BaseFragment <T extends ViewBinding> extends Fragment {

    protected BaseFragmentBinding baseBinding;
    public T viewBinding;

    public AppDatabase db;
    public List<FileBeans> listFile; // 文件存储
    public LiveData<List<FileBeans>> listLiveData;
    protected Handler handlers;
    protected LinkedList<CacheBean> linkedList;// 存储文件路径

    // 初始化收藏文件
    String[] files=new String[]{
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/tencent/QQ_Images",
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/CadShow",
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pictures/WeiXin"
    };
    String[] cadName=new String[]{
           "QQ收藏",
            "CadShow",
            "微信"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        baseBinding=BaseFragmentBinding.inflate(inflater,container,false);
        viewBinding=getViewBinding();

        initData();
        init();
        return baseBinding.getRoot();
    }

    protected abstract T getViewBinding();
    protected  abstract void initData();
    protected  abstract void init();

    // 初始化头部
    public void initTitle(CharSequence title) {
        baseBinding.titleBar.init();
        baseBinding.titleBar.setCenterTitle("");
        baseBinding.titleBar.setCenterColor(0xff000000);
        baseBinding.titleBar.setCenterFontSize(18);
        baseBinding.titleBar.setBackGroundColor(0xffffffff);
        baseBinding.titleBar.setLeftDrawable(R.drawable.ic_baseline_arrow_back_ios_24,0xff000000);
        baseBinding.titleBar.setRightTitle("");
        baseBinding.titleBar.setRightFontSize(18);
        baseBinding.titleBar.setRightColor(0xff000000);
        baseBinding.titleBar.setLeftMargin(MacUtils.dpto(10),0,0,0);
        baseBinding.titleBar.setRightMargin(0,0,MacUtils.dpto(10),0);
        baseBinding.titleBar.addviews();
        baseBinding.titleBar.setVisibility(View.VISIBLE);
    }

    // 权限获取
    @SuppressLint("CheckResult")
    protected  void getPermissions(String[] permission){
        RxPermissions rxPermissions= new RxPermissions(this);
        rxPermissions.setLogging(true);
        Observable<Permission> permissionObservable = rxPermissions.requestEach(permission);
        permissionObservable.subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // 用户已经同意该权限
                    Log.d("daoyi", "text_sms granted" );
                    //result.agree(permission);
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                    //result.refuse(permission);
                    Log.d("daoyi", "text_sms shouldShowRequestPermissionRationale" );
                } else {
                    // 用户拒绝了该权限，并且选中『不再询问』，提醒用户手动打开权限
                    //result.noMoreQuestions(permission);
                    Log.d("daoyi", "text_sms " );
                }
            }
        });
    }


    public rx.Observable<BaseF> StartObservable(List<FileBeans> va, DATABASES type){
        BaseF baseF = new BaseF();
        rx.Observable<BaseF> objectObservable = rx.Observable.create(s -> {

            for (int i = 0; i < va.size(); i++) {
                switch (DATABASES.stateOf(type.getType())){
                    case INSERT:
                        db.fileDao().insert(va.get(i));
                        break;
                    case QUERY:
                        listLiveData = db.fileDao().queryAll();
                        break;
                    case QUERYF:
                        listLiveData = db.fileDao().queryF(va.get(i).isFavorites);
                        break;
                    case QUERYFN:
                        db.fileDao().queryFN(va.get(i).path);
                        break;
                    case QUERYR:
                        listLiveData = db.fileDao().queryR(va.get(i).isRecent);
                        break;
                    case DELETE:
                        db.fileDao().delete(va.get(i));
                        break;
                    case UPDATE:
                        db.fileDao().update(va.get(i));
                        break;
                }
                s.onNext(baseF);
            }

            s.onCompleted();
        });
        return objectObservable;
    }


    public final Observer<List<FileBeans>> observable=new Observer<List<FileBeans>>() {
        @Override
        public void onChanged(List<FileBeans> fileBeans) {
            Log.e("daoyi_live",fileBeans.size()+"");
            Log.e("daoyi_live",fileBeans.toString()+"");
            listFile.addAll(fileBeans);
            if(handlers!=null){
                handlers.sendEmptyMessage(0x11);
            }

        }
    };

    public void toSubscribe(Subscriber subscriber, rx.Observable observable){
        observable.subscribeOn(Schedulers.io())//指定订阅关系发生在IO线程
                .unsubscribeOn(Schedulers.io())//指定解绑发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//指回调发生在主线程
                .subscribe(subscriber);//创建订阅关系
    }
    protected void initFile(){
        boolean isFirstUse = SPUtils.getIsFirstUse(getActivity());
        if(isFirstUse){

            List<FileBeans> a=new ArrayList<>();

            for (int i = 0; i < files.length; i++) {
                FileBeans fileBeans=new FileBeans();
                fileBeans.name=cadName[i];
                fileBeans.isFavorites=1;
                fileBeans.time= DateTimeUtil.dateToStrLong(new Date());
                fileBeans.p_type=0;
                fileBeans.path=files[i];
                fileBeans.type=0;
                fileBeans.isRecent = 0;
                a.add(fileBeans);
            }


            rx.Observable<BaseF> baseFObservable = StartObservable(a, DATABASES.INSERT);
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
            SPUtils.setIsFirstUse(getActivity(),false);

        }
    }
    protected  void getFileList(String path){
        Log.e("daoyiPath_:",path);
        SaveHelper.newInstance().FileName(getActivity());
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            File file=new File(path);
            if(file.isFile()){
                if(handlers!=null){
                    handlers.sendEmptyMessage(0x11);
                }
                return;
            }else{
                listFile.clear();
            }
            CacheBean cacheBean=new CacheBean();
            cacheBean.setName(file.getName());
            cacheBean.setPath(path);
            if(null!=linkedList&&linkedList.size()>0){
                if(!linkedList.get(linkedList.size()-1).getName().equals(file.getName())){
                    linkedList.add(cacheBean);
                }
            }else{
                linkedList.add(cacheBean);
            }
            File[] files2 = file.listFiles();
            if(null!=files2&&files2.length>0){
                for (File file1 : files2) {
                    FileBeans fileBeans = new FileBeans();
                    fileBeans.name = file1.getName();
                    fileBeans.time = DateTimeUtil.DateToString(new Date(file1.lastModified()), "yyyy-MM-dd");
                    fileBeans.path = file1.getAbsolutePath();
                    fileBeans.isFavorites = 0;

                    if (file1.isDirectory()) {
                        fileBeans.type = 0;
                    } else {
                        fileBeans.type = 1;
                        String fileName = file1.getName();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                        Log.e("daoyi_suffix", suffix);
                        if (suffix.equals("png")) {
                            fileBeans.p_type = 0;
                        } else if (suffix.equals("jpg")) {
                            fileBeans.p_type = 1;
                        } else {
                            fileBeans.p_type = 2;
                        }
                    }
                    listFile.add(fileBeans);
                }
                }
            }
            if(handlers!=null){
                handlers.sendEmptyMessage(0x11);
            }
    }

}
