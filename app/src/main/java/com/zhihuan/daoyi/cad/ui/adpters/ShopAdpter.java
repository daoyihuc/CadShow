package com.zhihuan.daoyi.cad.ui.adpters;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.ui.adpterBean.ShopBean;

import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class ShopAdpter extends BaseQuickAdapter<ShopBean, BaseViewHolder> {

    private Context context;
    public ShopAdpter(int layoutResId, @Nullable List<ShopBean> data) {
        super(layoutResId, data);
    }

    public ShopAdpter(Context context,@Nullable List<ShopBean> data) {
        super(R.layout.shop_item,data);
        this.context=context;
    }

    public ShopAdpter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopBean item) {
        ImageView imageView=helper.getView(R.id.icon);
        Glide.with(context).load(item.getSrc()).into(imageView);
        helper.setText(R.id.time,item.getTime());

        helper.addOnClickListener(R.id.down_skp)
                .addOnClickListener(R.id.goEdit);

    }
}
