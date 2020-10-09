package com.lianzai.reader.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.FeedBackTypeResponse;

import java.util.List;


public class FeedbackTypesAdapter extends BaseQuickAdapter<FeedBackTypeResponse.DataBean,BaseViewHolder> {
    public FeedbackTypesAdapter(@Nullable List<FeedBackTypeResponse.DataBean> data) {
        super(R.layout.item_feedback_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedBackTypeResponse.DataBean item) {
        helper.setText(R.id.tv_feedback_name,item.getName());
    }
}
