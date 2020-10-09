package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookListCategoryBean;
import com.lianzai.reader.utils.RxImageTool;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class BooksCategoryItemAdapter extends BaseQuickAdapter<BookListCategoryBean.DataBeanX.BookListBean.DataBean,BaseViewHolder> {


    Context context;
    public BooksCategoryItemAdapter(int layoutResId, @Nullable List<BookListCategoryBean.DataBeanX.BookListBean.DataBean> data, Context mContext) {
        super(layoutResId, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, BookListCategoryBean.DataBeanX.BookListBean.DataBean bean) {
        final ImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(context,bean.getCover(),roundedImageView);

        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        TextView tvIsUpdate=baseViewHolder.getView(R.id.tv_is_update);
        TextView tvUpdateChapter=baseViewHolder.getView(R.id.tv_book_update_chapter);
        TextView tvAttentionCount=baseViewHolder.getView(R.id.tv_book_attention_count);

        tvBookTitle.setText(bean.getTitle());
        tvIsUpdate.setVisibility(View.GONE);
        tvAttentionCount.setText(bean.getMember_count()+"关注 | "+bean.getPost_count()+"帖子 | "+bean.getWord_num()+" 字");
        tvUpdateChapter.setText(bean.getIntro());
    }

}
