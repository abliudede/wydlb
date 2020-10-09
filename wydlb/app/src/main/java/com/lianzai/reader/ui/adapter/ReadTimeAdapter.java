package com.lianzai.reader.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.utils.GsonUtil;

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
