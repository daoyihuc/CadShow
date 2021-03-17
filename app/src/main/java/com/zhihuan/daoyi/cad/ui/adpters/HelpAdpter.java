package com.zhihuan.daoyi.cad.ui.adpters;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhihuan.daoyi.cad.R;
import com.zhihuan.daoyi.cad.ui.adpterBean.HelpBean;

import java.util.List;

/**
 * @author: daoyi(yanwen)
 * @Emaill: 1966287146
 * @params: “details”
 * @date :
 */
public class HelpAdpter extends BaseQuickAdapter<HelpBean, BaseViewHolder> {

    public HelpAdpter(int layoutResId, @Nullable List<HelpBean> data) {
        super(layoutResId, data);
    }

    public HelpAdpter(@Nullable List<HelpBean> data) {
        super(R.layout.help_item,data);
    }

    public HelpAdpter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HelpBean item) {
        helper.setText(R.id.label,item.getName());
    }
}
