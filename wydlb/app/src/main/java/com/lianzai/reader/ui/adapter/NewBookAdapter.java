package com.lianzai.reader.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookCategoryBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

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
