package com.wydlb.first.ui.adapter;

import androidx.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class MinerFriendsAdapter extends BaseQuickAdapter<Object,BaseViewHolder> {



    public MinerFriendsAdapter(@Nullable List<Object> data) {
        super(R.layout.item_miner_friends, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, Object bean) {
        TextView tv_nickname=baseViewHolder.getView(R.id.tv_nickname);

        tv_nickname.setText(Html.fromHtml("您的好友 <font color='#F8E71C'>"+(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())+"号大黄蜂</font>给你增加了<font color='#F8E71C'>"+(baseViewHolder.getAdapterPosition()*100+100)+" </font>点算力"));
    }

}
