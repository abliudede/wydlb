package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class SearchHistoryAdapter extends BaseQuickAdapter<String,BaseViewHolder> {


    Context context;
    public SearchHistoryAdapter(@Nullable List<String> data, Context mContext) {
        super(R.layout.item_search_history, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,String str) {

        TextView tv_search_keyword=baseViewHolder.getView(R.id.tv_search_keyword);
        tv_search_keyword.setText(str);

    }

}
