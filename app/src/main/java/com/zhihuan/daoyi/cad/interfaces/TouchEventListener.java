package com.zhihuan.daoyi.cad.interfaces;

import android.view.MotionEvent;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public interface TouchEventListener{
    boolean dispatchTouchEvent(MotionEvent event);
    boolean DrawingOption(); // 绘制事件
    int DrawingType();
    boolean DrawingCloseCall(boolean close);
}
