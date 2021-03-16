package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.utils.MacUtils;

/**
 * @author: "daoyi"
 * @date:
 * @params: "说明...."
 */
public class EmptyView extends RelativeLayout implements View.OnClickListener {

    private ImageView LogoImg;
    private TextView textView;

    public EmptyView(Context context) {
        super(context);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init (Context context){
        textView=new TextView(context);
        textView.setId(R.id.em_message);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(-2,-2);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(layoutParams);
        addView(textView);

        LogoImg=new ImageView(context);
        LogoImg.setId(R.id.em_logo);
        RelativeLayout.LayoutParams logo_params=new RelativeLayout.LayoutParams(MacUtils.dpto(200),MacUtils.dpto(200));
        logo_params.addRule(RelativeLayout.ABOVE,R.id.em_message);
        logo_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        LogoImg.setLayoutParams(logo_params);
        addView(LogoImg);

        textView.setOnClickListener(this);
        LogoImg.setOnClickListener(this);

    }

    public void setText(String val){
        textView.setText(val);
    }
    public void setLogoImg(Object url){
        Glide.with(this).load(url).into(LogoImg);
    }

    View.OnClickListener onClickListener;

    public void setOnclick(View.OnClickListener v){
        this.onClickListener=v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.em_message:
                case R.id.em_logo:
                    if(onClickListener!=null){
                        onClickListener.onClick(v);
                    }
                    break;

        }
    }
}
