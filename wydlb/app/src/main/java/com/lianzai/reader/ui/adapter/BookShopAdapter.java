package com.lianzai.reader.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.SectionBookShopBean;
import com.lianzai.reader.utils.RxImageTool;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class BookShopAdapter extends BaseSectionQuickAdapter<SectionBookShopBean,BaseViewHolder> {


    public BookShopAdapter(int layoutResId, int sectionHeadResId, List<SectionBookShopBean> data) {
        super(layoutResId,sectionHeadResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SectionBookShopBean bean) {

        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        TextView tvIsUpdate=baseViewHolder.getView(R.id.tv_is_update);
        TextView tvUpdateChapter=baseViewHolder.getView(R.id.tv_book_update_chapter);
        TextView tvDescription=baseViewHolder.getView(R.id.tv_book_attention_count);
        tvDescription.setText(bean.t.getMember_count()+"关注 | "+bean.t.getPost_count()+"帖子 | "+bean.t.getWord_num()+"字");
        tvUpdateChapter.setText(bean.t.getIntro());
        tvBookTitle.setText(bean.t.getTitle());
        ImageView ivCover=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(mContext,bean.t.getCover(),ivCover);
        tvIsUpdate.setVisibility(View.GONE);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, SectionBookShopBean bean) {
        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        TextView tvBookDescriptionTitle=baseViewHolder.getView(R.id.tv_book_description);
        ImageView ivArrowRight=baseViewHolder.getView(R.id.iv_arrow_right);

        if (bean.header.equals("编辑推荐")){
            ivArrowRight.setVisibility(View.GONE);
        }else{
            ivArrowRight.setVisibility(View.VISIBLE);
        }
        tvBookTitle.setText(bean.header);
        tvBookDescriptionTitle.setText(bean.getmContent());

    }
}
