package com.zhihuan.daoyi.cad.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.zhihuan.daoyi.cad.ui.room.AppDatabase;
import com.zhihuan.daoyi.cad.ui.room.entity.BaseF;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;
import com.zhihuan.daoyi.cad.ui.room.types.DATABASES;
import com.zhihuan.daoyi.cad.utils.MacUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
        rx.Observable<BaseF> objectObservable = rx.Observable.create(s -> {
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


    public final Observer<List<FileBeans>> observable=new Observer<List<FileBeans>>() {
        @Override
        public void onChanged(List<FileBeans> fileBeans) {
            Log.e("daoyi_live",fileBeans.size()+"");
            Log.e("daoyi_live",fileBeans.toString()+"");
            listFile.addAll(fileBeans);
        }
    };

    public void toSubscribe(Subscriber subscriber, rx.Observable observable){
        observable.subscribeOn(Schedulers.io())//指定订阅关系发生在IO线程
                .unsubscribeOn(Schedulers.io())//指定解绑发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//指回调发生在主线程
                .subscribe(subscriber);//创建订阅关系
    }

}
