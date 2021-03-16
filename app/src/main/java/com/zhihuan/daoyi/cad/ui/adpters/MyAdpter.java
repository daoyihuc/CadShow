package com.zhihuan.daoyi.cad.ui.adpters;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.ui.adpterBean.MyBean;

import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class MyAdpter extends BaseQuickAdapter<MyBean, BaseViewHolder> {

    private Context context;
    public MyAdpter(int layoutResId, @Nullable List<MyBean> data) {
        super(layoutResId, data);
    }

    public MyAdpter(Context context,@Nullable List<MyBean> data) {
        super(R.layout.my_item,data);
        this.context=context;
    }

    public MyAdpter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyBean item) {
        ImageView imageView=helper.getView(R.id.icon);
        Glide.with(context).load(item.getIcon()).into(imageView);

        helper.setText(R.id.name,item.getName());
    }
}
