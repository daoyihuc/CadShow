package com.zhihuan.daoyi.cad.ui.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.base.BaseActivity;
import com.zhihuan.daoyi.cad.base.Constans;
import com.zhihuan.daoyi.cad.databinding.ActivityWebviewBinding;
import com.zhihuan.daoyi.cad.utils.MacUtils;
import com.zhihuan.daoyi.cad.views.X5WebView;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class WebViewActivity extends BaseActivity<ActivityWebviewBinding> {

    private String mCM;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    private final static int FCR = 1;

    private static final int REQUEST_STORAGE = 1;
    private static final int REQUEST_LOCATION = 2;
    public ValueCallback<Uri> mUploadMessage;
    public static final int FILE_CHOOSER_RESULT_CODE = 5173;
    private X5WebView webView; // 网页展示
    String url = "";
    String name = "";
    int type=0;

    public static void start(Activity activity,int type,String url){
        Intent intent=new Intent();
        intent.setClass(activity,WebViewActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("url",url);
        activity.startActivity(intent);

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MacUtils.setStatusText(this,true);
//        StatusBarUtil.setTranslucentForImageView(this,1,baseBinding.maxBox);
        MacUtils.initWindow(this,0xffffffff,false,null,true);
    }

    @Override
    protected ActivityWebviewBinding getViewBinding() {
        return ActivityWebviewBinding.inflate(getLayoutInflater(),baseBinding.maxBox,true);
    }

    protected void initData(){
        Intent intent = getIntent();
         type = intent.getIntExtra("type",0);
         String urlS = intent.getStringExtra("url");
         switch (type){
             case 0: // 用户协议
                 url= Constans.YHXY;
                 name="用户协议";
                 break;
             case 1: // 隐私政策
                 url=Constans.YSZC;
                 name="隐私政策";
                 break;
             case 3:
                 url= urlS;
                 name="新手帮助";
                 break;
         }

    }

    @Override
    protected void init() {
        setWebView();
        viewBinding.title.init();
        viewBinding.title.setCenterTitle(""+name);
        viewBinding.title.setCenterColor(0xff000000);
        viewBinding.title.setCenterFontSize(18);
        viewBinding.title.setBackGroundColor(0xffffffff);
        viewBinding.title.setLeftDrawable(R.drawable.ic_baseline_arrow_back_ios_24,0xff4F4F4F);
        viewBinding.title.setRightTitle("");
        viewBinding.title.setRightFontSize(18);
        viewBinding.title.setRightColor(0xff000000);
        viewBinding.title.setLeftMargin(MacUtils.dpto(20),0,0,0);
        viewBinding.title.setRightMargin(0,0,MacUtils.dpto(10),0);
        viewBinding.title.addviews();
        viewBinding.title.setLeftOnClickListener(v -> {
            onBackPressed();
        });

    }

    protected void initUI() {


    }

    // webview
    private void setWebView() {
        webView = viewBinding.webview;
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
//        webView.setLayoutParams(params);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        // 其他细节操作
        settings.setCacheMode(settings.LOAD_NO_CACHE); // 关闭webview中缓存
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true); // 设置可以访问文件
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        settings.setDefaultTextEncodingName("utf-8");// 设置编码格式
        webView.setWebChromeClient(new WebChromeClient());
//        webView.loadUrl("about:blank");
        //适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String urls = "";
//                view.loadUrl("about:blank");
//                return super.shouldOverrideUrlLoading(view, request);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    urls = request.getUrl().toString();
                } else {
                    urls = String.valueOf(request.getUrl());
                }

                try {
                    view.loadUrl(urls);
                    if (urls.startsWith("http:") || urls.startsWith("https:")) {

                    } else {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls));
//                        startActivity(intent);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            //页面加载结束时

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                Log.e("webView", "加载完成"+url);
            }

        });
    }
}
