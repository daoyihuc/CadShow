package com.zhihuan.daoyi.cad.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author: "daoyi"
 * @date:
 * @params: "说明...."
 */
public class EmptyView extends RelativeLayout {

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
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(-2,-2);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(layoutParams);
        addView(textView);
    }

    public void setText(String val){
        textView.setText(val);
    }

}
