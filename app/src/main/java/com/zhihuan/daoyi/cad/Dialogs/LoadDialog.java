package com.zhihuan.daoyi.cad.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.databinding.LoadDialogBinding;
import com.zhihuan.daoyi.cad.utils.MacUtils;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class LoadDialog extends Dialog {

    LoadDialogBinding loadDialogBinding;
    private Context mContext;
    private String msg="图片保存中";
    Animation hyperspaceJumpAnimation;
    public LoadDialog(@NonNull Context context) {
        super(context);
        this.mContext=context;
    }

    public LoadDialog(@NonNull Context context, int themeResId) {
        super(context,themeResId);
        this.mContext=context;
        //
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);//点击其他区域时   true  关闭弹窗  false  不关闭弹窗
        loadDialogBinding=LoadDialogBinding.inflate(LayoutInflater.from(mContext));
        setContentView(loadDialogBinding.getRoot());//加载布局
        loadDialogBinding.tvLoadingTx.setText(msg);
        // 加载动画
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                mContext, R.anim.loading_animation);
        // 使用ImageView显示动画
        loadDialogBinding.ivLoading.startAnimation(hyperspaceJumpAnimation);
    }

    public void setMessage(String val){
        this.msg=val;
    }

    @Override
    public void show() {
        super.show();
        loadDialogBinding.ivLoading.startAnimation(hyperspaceJumpAnimation);
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Window window=getWindow();
        setCanceledOnTouchOutside(false);//不允许外部点击消失
//        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(0,0,0, 0);
        WindowManager.LayoutParams layoutParams=window.getAttributes();//获取窗口属性
        layoutParams.gravity = Gravity.CENTER;
        DisplayMetrics displayMetrics=mContext.getResources().getDisplayMetrics();
        layoutParams.width= (int) (displayMetrics.widthPixels*0.5);
        layoutParams.height=MacUtils.dpto(150);
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(MacUtils.dpto(10));
        window.setBackgroundDrawable(gradientDrawable);

        window.setAttributes(layoutParams);
    }
}
