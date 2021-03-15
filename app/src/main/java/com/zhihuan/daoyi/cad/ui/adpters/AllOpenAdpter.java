package com.zhihuan.daoyi.cad.ui.adpters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.ui.room.entity.FileBeans;

import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class AllOpenAdpter extends BaseQuickAdapter<FileBeans, BaseViewHolder> {

    private Context mContext;
    public AllOpenAdpter(int layoutResId, @Nullable List<FileBeans> data) {

        super(layoutResId, data);
    }

    public AllOpenAdpter(Context context, @Nullable List<FileBeans> data) {
        super(R.layout.recent_open_item,data);
        this.mContext=context;
    }

    public AllOpenAdpter(int layoutResId) {
        super(layoutResId);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(BaseViewHolder helper, FileBeans item) {
        if(item.isFavorites==1){
            Drawable drawable1 = mContext.getResources().getDrawable(R.drawable.star);
            drawable1.setBounds(0,0,drawable1.getMinimumWidth(),drawable1.getMinimumHeight());
            drawable1.setTint(mContext.getResources().getColor(R.color.yellow));
            ImageView imageView=helper.getView(R.id.start);
            imageView.setImageDrawable(drawable1);
        }
        helper.setText(R.id.name,item.name); // 文件名
        helper.setText(R.id.time,item.time); // 时间
        Drawable drawable=mContext.getResources().getDrawable(R.drawable.folder);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        drawable.setTint(mContext.getResources().getColor(R.color.green));
        if(item.type==1){
            if(item.p_type==0){ // png
                drawable = mContext.getResources().getDrawable(R.drawable.png);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            }else if(item.p_type==1){
                drawable = mContext.getResources().getDrawable(R.drawable.jpg);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            }else{
                drawable = mContext.getResources().getDrawable(R.drawable.question);
                drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
                drawable.setTint(mContext.getResources().getColor(R.color.yellow));
            }
            helper.setVisible(R.id.item_back,false);
        }
        helper.setImageDrawable(R.id.icon,drawable);
        helper.addOnClickListener(R.id.box)
                .addOnClickListener(R.id.start);
    }
}
