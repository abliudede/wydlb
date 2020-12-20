package com.wydlb.first.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.BookCategoryBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class NewBookAdapter extends BaseQuickAdapter<BookCategoryBean.DataBean.NewBookBean,BaseViewHolder> {



    public NewBookAdapter(@Nullable List<BookCategoryBean.DataBean.NewBookBean> data) {
        super(R.layout.new_book_image, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, BookCategoryBean.DataBean.NewBookBean bean) {
        SelectableRoundedImageView ivBookCover=baseViewHolder.getView(R.id.iv_book_cover);

        RxImageTool.loadImage(mContext,bean.getCover(),ivBookCover);
    }

}
