package com.zhihuan.daoyi.http.base;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 请求服务接口类
 */
public interface NetworkService{

//    //登录
//    @FormUrlEncoded
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //解决中文乱码问题
//    @POST("users/loginAndRegister")
//    Observable<LoginResponse> Login(@FieldMap Map<String, Object> map);
//
//
//
//    //获取首页信息
//    // @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //解决中文乱码问题
//    @GET("homepage")
//    Observable<HomePageBean> homepage(@QueryMap Map<String, Object> params);//    //获取首页信息

    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8") //解决中文乱码问题
    @GET
    Observable<ResponseBody> downUrl(@Url String url);



}

