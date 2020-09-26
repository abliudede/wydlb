package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookListResponse;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class BookListGridAdapter extends BaseQuickAdapter<BookListResponse.DataBean.ListBean,BaseViewHolder> {


    Context context;

    int imageWidth=0;

    public BookListGridAdapter(@Nullable List<BookListResponse.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_book_list_grid, data);
        this.context=mContext;

        imageWidth= (RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(48))/3;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookListResponse.DataBean.ListBean bean) {
        SelectableRoundedImageView iv_book_list_cover=baseViewHolder.getView(R.id.iv_book_list_cover);
        TextView tv_book_list_name=baseViewHolder.getView(R.id.tv_book_list_name);

        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(imageWidth,imageWidth);
        iv_book_list_cover.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams1=new RelativeLayout.LayoutParams(imageWidth,RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.topMargin=RxImageTool.dip2px(5);
        layoutParams1.addRule(RelativeLayout.BELOW,R.id.iv_book_list_cover);
        tv_book_list_name.setLayoutParams(layoutParams1);

        tv_book_list_name.setText(bean.getBooklistName());

        RxImageTool.loadSquareImage(context,bean.getCover(),iv_book_list_cover);

    }

}
