package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;

import java.util.List;

/**
 * Created by lrz on 2019/3/11.
 */

public class BookShortageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    Context context;
    public BookShortageAdapter(@Nullable List<String> data, Context mContext) {
        super(R.layout.item_book_shortage, data);
        this.context=mContext;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,String bean) {
        TextView tv_bookname=baseViewHolder.getView(R.id.tv_bookname);
        tv_bookname.setText("《"+bean+"》");
    }

}
