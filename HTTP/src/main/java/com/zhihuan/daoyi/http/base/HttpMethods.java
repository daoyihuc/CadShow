package com.zhihuan.daoyi.http.base;

import android.util.Log;


import androidx.annotation.NonNull;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Administrator: "daoyi(yanwen)"
 *
 * @params: 2020/10/20
 * @details: 'http请求类'
 * @mails: '1966287146@qq.com'
 */
public class HttpMethods{
    //TODO 7.13 fqj添加原始url
    public static final String MAIN_ENGINE = Constans.MAIN_URL;
    //设置超时时间
    private static final int DEFAULT_TIMEOUT = 30;
    private final Retrofit retrofit;
    private final NetworkService networkService;
    //构造方法私有
    private HttpMethods(){
        //添加日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger(){
            @Override
            public void log(String message){
                //TODO 打印retrofit日志(Json格式数据)
                //TODO 需要的时候再开启
                Log.d("Log", "JSON = " + message);
            }

        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //初始化OKHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)//设置超时时间
                         .addInterceptor(new CommonParamInterceptor())
                         .addInterceptor(loggingInterceptor)//添加日志拦截器
                         .addInterceptor(new ResponseCookieInterceptor())//token判断
                         .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                         .sslSocketFactory(getSSLSocketFactory(),new CustomTrustManager())//支持https
                         .hostnameVerifier(getHostnameVerifier())//信任所有
                         .build();
        //初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())/*Gason转换器*/
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//这样一来我们定义的service返回值就不在是一个Call了，而是一个Observable
                .baseUrl(MAIN_ENGINE)
                .build();
        networkService = retrofit.create(NetworkService.class);
    }
    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }
    //获取单例
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public static HostnameVerifier getHostnameVerifier(){
        HostnameVerifier hostnameVerifier= new HostnameVerifier(){
            public boolean verify(String hostname, SSLSession session){
                return true;
            }
        };
        return hostnameVerifier;
    }
    public static SSLSocketFactory getSSLSocketFactory(){
        SSLSocketFactory ssfFactory = null;
        try{
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new CustomTrustManager()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        }catch (Exception e){

        }
        return ssfFactory;
    }
    //发生绑定注册关系的方法（被观察者，观察者）创建RxJava最简单的步骤，一、创建被观察者对象，二、创建观者对象，三创建订阅关系；
    private void toSubscribe(Observable observable, Subscriber subscriber){
        observable.subscribeOn(Schedulers.io())//指定订阅关系发生在IO线程
                .unsubscribeOn(Schedulers.io())//指定解绑发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//指回调发生在主线程
                .subscribe(subscriber);//创建订阅关系
    }
    /**
     * 方法样例
     *   public void login(Subscriber<LoginResponse> subscriber, Map<String, Object> map) {
         Observable observable = networkService.login(map);
         toSubscribe(observable, subscriber);
         }
     ........................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................................
     */
    //登录接口

    /**
     * 开始下载
     * @param url
     * @param file
     * @param subscriber
     */
    public void download(@NonNull String url, final File file, Subscriber subscriber) {
        networkService.downUrl(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {

                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        writeFile(inputStream, file);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * 将输入流写入文件
     * @param inputString
     * @param file
     */
    private void writeFile(InputStream inputString, File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }

}

