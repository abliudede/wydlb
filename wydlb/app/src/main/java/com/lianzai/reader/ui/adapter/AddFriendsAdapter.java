package com.lianzai.reader.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;

import java.util.List;


public class AddFriendsAdapter extends BaseQuickAdapter<Object,BaseViewHolder> {
    public AddFriendsAdapter( @Nullable List<Object> data) {
        super(R.layout.item_add_friends, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {

    }
}
