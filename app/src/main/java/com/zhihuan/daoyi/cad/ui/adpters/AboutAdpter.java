package com.zhihuan.daoyi.cad.ui.adpters;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.ui.adpterBean.AboutBean;

import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class AboutAdpter extends BaseQuickAdapter<AboutBean, BaseViewHolder> {

    private Context context;
    public AboutAdpter(int layoutResId, @Nullable List<AboutBean> data) {
        super(layoutResId, data);
    }

    public AboutAdpter(Context context, @Nullable List<AboutBean> data) {
        super(R.layout.about_item,data);
        this.context=context;
    }

    public AboutAdpter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AboutBean item) {
        helper.setText(R.id.label,item.getName())
                .setText(R.id.value,item.getValue());
    }
}
