package com.zhihuan.daoyi.http.base;


import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * AUTHOR : daoyi
 * TODO : 接口日志类
 * DATE : 2020/3/14
 * VERSION : 1.0
 */
public class InterfaceLogInterceptor  implements Interceptor {

    @Override
    public synchronized okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();

        Log.e("daoyi","-----------------------------------------------------------------------------------------------------------------");
        Log.e("daoyi","|                                              接口请求                                                         |");
        Log.e("daoyi","|  【请求地址】：" + request.url().toString());
        printParams(request.body());
        Log.e("daoyi","|  【返回数据】：" + content);
        Log.e("daoyi","|  【请求时间】：" + duration + " 毫秒");
        Log.e("daoyi","|                                                                                                               |");
        Log.e("daoyi","-----------------------------------------------------------------------------------------------------------------");
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }


    private void printParams(RequestBody body) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset();

            }
            String params = buffer.readString(charset);
            Log.e("daoyi","|  【请求参数】：" + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}