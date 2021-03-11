package com.zhihuan.daoyi.cad.help;

import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class tes extends ViewOutlineProvider {

    private Rect rect;

    public tes(Rect res){
        this.rect=res;
    }

    @Override
    public void getOutline(View view, Outline outline) {

        outline.setRect(rect);
    }
}
