package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookListDetailResponse;
import com.lianzai.reader.bean.ShareBitmapBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

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
