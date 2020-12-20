package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.model.bean.ReadTimeBean;
import com.wydlb.first.utils.GsonUtil;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 好友item
 */

public class ReadTimeAdapter extends BaseQuickAdapter<ReadTimeBean, BaseViewHolder> {


    Context context;

    public ReadTimeAdapter(@Nullable List<ReadTimeBean> data, Context mContext) {
        super(R.layout.item_readtime, data);
        this.context = mContext;

    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,ReadTimeBean bean) {
        TextView title = baseViewHolder.getView(R.id.title);
        TextView des = baseViewHolder.getView(R.id.des);
        title.setText(String.valueOf((bean.getEndTime() - bean.getStartTime())/1000) + "秒");
        des.setText(GsonUtil.toJsonString(bean));
    }


}
