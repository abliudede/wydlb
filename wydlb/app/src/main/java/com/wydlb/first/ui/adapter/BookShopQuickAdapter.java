package com.wydlb.first.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.BookShopMultipleItem;

import java.util.List;

public class BookShopQuickAdapter extends BaseMultiItemQuickAdapter<BookShopMultipleItem, BaseViewHolder> {

    public BookShopQuickAdapter(Context context, List data) {
        super(data);
        addItemType(BookShopMultipleItem.BOOKS, R.layout.item_chasing_book);
        addItemType(BookShopMultipleItem.TITLE, R.layout.item_book_title);
    }


    @Override
    protected void convert(BaseViewHolder helper, BookShopMultipleItem item) {
        switch (helper.getItemViewType()) {
            case BookShopMultipleItem.TITLE:
                break;
            case BookShopMultipleItem.BOOKS:
                break;
        }
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

}
