package com.lianzai.reader.ui.adapter;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.bean.SectionBookDirectoryBean;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class BookDirectoryAdapter extends BaseSectionQuickAdapter<SectionBookDirectoryBean,BaseViewHolder> {


    public BookDirectoryAdapter(int layoutResId, int sectionHeadResId, List<SectionBookDirectoryBean> data) {
        super(layoutResId,sectionHeadResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, SectionBookDirectoryBean bean) {

//        TextView tvBookDirectoryName=baseViewHolder.getView(R.id.tv_book_directory_name);
////        tvBookDirectoryName.setBackground(mContext.getResources().getDrawable(R.drawable.white_corner_card));
//        tvBookDirectoryName.setText(bean.t.getTitle());
//        tvBookDirectoryName.setTextColor(mContext.getResources().getColor(R.color.second_text_color));
//        tvBookDirectoryName.setTextSize(12);
    }

    @Override
    protected void convertHead(BaseViewHolder baseViewHolder, SectionBookDirectoryBean bean) {
//        TextView tvBookDirectoryName=baseViewHolder.getView(R.id.tv_book_directory_name);
//        tvBookDirectoryName.setText(bean.header);
////        tvBookDirectoryName.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_color));
//        tvBookDirectoryName.setText(bean.header);
//        tvBookDirectoryName.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
//        tvBookDirectoryName.setTextSize(16);
    }
}
