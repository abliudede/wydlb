package com.lianzai.reader.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.ShareBitmapBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class BookListShareAdapter extends BaseQuickAdapter<ShareBitmapBean.DataBean.BookListInfoBean.CounterSignBooklistSonListBean,BaseViewHolder> {


    Context context;
    public BookListShareAdapter(@Nullable List<ShareBitmapBean.DataBean.BookListInfoBean.CounterSignBooklistSonListBean> data, Context mContext) {
        super(R.layout.item_book_list_share, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,ShareBitmapBean.DataBean.BookListInfoBean.CounterSignBooklistSonListBean bean) {
        SelectableRoundedImageView iv_book_cover=baseViewHolder.getView(R.id.iv_book_cover);
        TextView tv_book_name=baseViewHolder.getView(R.id.tv_book_name);
        TextView tv_author=baseViewHolder.getView(R.id.tv_author);
//        RatingBar rb_star=baseViewHolder.getView(R.id.rb_star);
        TextView tv_des=baseViewHolder.getView(R.id.tv_des);


        RxImageTool.loadImage(context,bean.getPlatformCover(),iv_book_cover);
        tv_book_name.setText(bean.getBookTitle());
        tv_author.setText(bean.getBookAuthor());
        tv_des.setText(bean.getPlatformIntroduce());
    }


}
