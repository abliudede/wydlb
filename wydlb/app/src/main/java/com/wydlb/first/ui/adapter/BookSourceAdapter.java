package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.BookSourceResponse;
import com.wydlb.first.utils.TimeFormatUtil;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 换源
 */

public class BookSourceAdapter extends BaseQuickAdapter<BookSourceResponse.DataBean.NovelSourceBean, BaseViewHolder> {

    Context context;


    int currentPos=1;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }

    public BookSourceAdapter(@Nullable List<BookSourceResponse.DataBean.NovelSourceBean> data, Context mContext) {
        super(R.layout.item_book_source, data);
        this.context = mContext;
    }



    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookSourceResponse.DataBean.NovelSourceBean bean) {
        TextView tv_source_site = baseViewHolder.getView(R.id.tv_source_site);
        TextView tv_source_date = baseViewHolder.getView(R.id.tv_source_date);
        TextView tv_source_chapter = baseViewHolder.getView(R.id.tv_source_chapter);
        ImageView iv_status = baseViewHolder.getView(R.id.iv_status);

        tv_source_site.setText(TextUtils.isEmpty(bean.getSourceCname())?bean.getSourceName():bean.getSourceCname());
        if (bean.getShelfTime()==0){
            tv_source_date.setVisibility(View.GONE);
        }else{
            tv_source_date.setVisibility(View.VISIBLE);
            tv_source_date.setText(TimeFormatUtil.getInterval(String.valueOf(bean.getShelfTime())));
        }

        tv_source_chapter.setText(bean.getChapterTitle());

        if (getCurrentPos()==baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){
            iv_status.setImageResource(R.mipmap.icon_selected);
        }else{
            iv_status.setImageResource(R.mipmap.icon_not_selected);
        }

    }


}
