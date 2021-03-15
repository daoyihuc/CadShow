package com.zhihuan.daoyi.cad.utils;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具保存类
 */
public class SPUtils {
    /**
     * 第一次使用的保存信息
     * @param context
     * @param bool
     */
    //该方法设置用户是否是第一次使用 以便做引导页面的逻辑处理
    public static void setIsFirstUse(Context context,boolean bool){
        SharedPreferences sharedPreferences = context.getSharedPreferences("FirstUse", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstUse",bool);
        editor.commit();
    }
    //该方法活得用户是否第一次使用 返回一个bool类型
    public static boolean getIsFirstUse(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("FirstUse",Activity.MODE_PRIVATE);
        return sharedPreferences.getBoolean("firstUse",true);
    }
    /**
     * 用户user_id的保存信息
     * @param context
     * @param app_user_id
     */
    //保存用户的user_id 第一次登录保存了token 以后进入app只要有token的话就直接进入主界面
    public static void saveAppUserId(Context context,String app_user_id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("token",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("app_user_id",app_user_id);
        editor.commit();
    }
    //获取用户user_id
    public static String getAppUserId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("token",Activity.MODE_PRIVATE);
        return sharedPreferences.getString("app_user_id","");//没有拿到用户id就赋值为""
    }
    //清除用户user_id
    public static void clearAppUserid(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("token",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 用户登录信息保存
     * @param context
     * @param username
     * @param password
     */
    //保存用户登录信息
    public static void saveUserLoginInfor(Context context,String username,String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserLoginInfor",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
    }
    //获取用户登录信息 Editor
    public static SharedPreferences getUserLoginInfor(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserLoginInfor",Context.MODE_PRIVATE);
        return sharedPreferences;
    }
    //清除用户登录信息
    public static void clearUserLoginInfor(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserLoginInfor",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    /**
     * 保存用户手机号信息
     * @param context
     * @param phone
     */
    //保存用户手机号
    public static void saveUserPhone(Context context,String phone){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPhone",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone",phone);
        editor.commit();
    }
    //获取用户手机号
    public static String  getUserPhone(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPhone",Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone","");
    }
    //清除用户手机号
    public static void  clearUserPhone(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPhone",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    //保存用户的支付宝账号信息
    public static void saveAlipayAccountInfo(Context context,String username,String account){
        SharedPreferences sharedPreferences = context.getSharedPreferences("alipay",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("account",account);
        editor.commit();
    }
    //清除用户支付宝账号信息
    public static void clearAlipayAccountInfo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("alipay",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    //保存启动页加载图片
    public static void saveSplashImageUrl(Context context,String url){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Splash",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url",url);
        editor.commit();
    }
    //获取启动页加载图片
    public static String getSplashImageUrl(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Splash",Context.MODE_PRIVATE);
        return sharedPreferences.getString("url","");
    }
    /**
     * 保存一个本地时间戳
     */
    public static void saveTimeStamp(Context context,String time_stamp){
        SharedPreferences sharedPreferences = context.getSharedPreferences("time_stamp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("time_stamp",time_stamp);
        editor.commit();
    }
    /**
     * 获取本地时间戳
     */
    public static String getTimeStamp(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("time_stamp",Context.MODE_PRIVATE);
        return sharedPreferences.getString("time_stamp","");
    }
    /**
     * 清除本地时间戳
     */
    public static void clearTimeStamp(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("time_stamp",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}