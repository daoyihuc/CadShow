package com.zhihuan.daoyi.cad.ui.adpters;

import android.content.Context;

import androidx.annotation.Nullable;

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
public class WebAdpter extends BaseQuickAdapter<FileBeans, BaseViewHolder> {

    private Context mContext;
    public WebAdpter(int layoutResId, @Nullable List<FileBeans> data) {
        super(layoutResId, data);
    }

    public WebAdpter(Context context,@Nullable List<FileBeans> data) {
        super(R.layout.recent_open_item,data);
        this.mContext=context;
    }

    public WebAdpter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBeans item) {

    }
}
