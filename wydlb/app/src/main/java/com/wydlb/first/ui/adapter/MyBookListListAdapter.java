package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.model.bean.MyBookListBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class MyBookListListAdapter extends BaseQuickAdapter<MyBookListBean,BaseViewHolder> {



    Context context;
    public MyBookListListAdapter(@Nullable List<MyBookListBean> data, Context mContext) {
        super(R.layout.item_book_list_list, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,MyBookListBean bean) {
        if (null==bean)return;
        SelectableRoundedImageView iv_book_list_cover=baseViewHolder.getView(R.id.iv_book_list_cover);
        TextView tv_book_list_name=baseViewHolder.getView(R.id.tv_book_list_name);
        TextView iv_book_list_description=baseViewHolder.getView(R.id.iv_book_list_description);
        TextView iv_book_list_more=baseViewHolder.getView(R.id.iv_book_list_more);
        TextView tv_update=baseViewHolder.getView(R.id.tv_update);

        tv_book_list_name.setText(bean.getBooklistName());
        RxImageTool.loadSquareImage(context,bean.getCover(),iv_book_list_cover);
        iv_book_list_description.setText(bean.getBooklistIntro());

        String moreStr=(TextUtils.isEmpty(bean.getBooklistAuthor())?"":bean.getBooklistAuthor());
        iv_book_list_more.setText(moreStr);

        if (bean.isUnread()){
            tv_update.setVisibility(View.VISIBLE);
        }else{
            tv_update.setVisibility(View.GONE);
        }

    }


}
