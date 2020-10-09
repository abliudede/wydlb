package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.view.page.UrlsVo;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 换源
 */

public class BookChapterSourceAdapter extends BaseQuickAdapter<UrlsVo, BaseViewHolder> {

    Context context;


    int currentPos=0;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        notifyDataSetChanged();
    }

    public BookChapterSourceAdapter(@Nullable List<UrlsVo> data, Context mContext) {
        super(R.layout.item_book_chapter_source, data);
        this.context = mContext;
    }



    @Override
    protected void convert(final BaseViewHolder baseViewHolder,UrlsVo bean) {
        TextView tv_source_site = baseViewHolder.getView(R.id.tv_source_site);
        ImageView iv_status = baseViewHolder.getView(R.id.iv_status);

        RelativeLayout rl_chapter_source_view=baseViewHolder.getView(R.id.rl_chapter_source_view);

        tv_source_site.setText(!TextUtils.isEmpty(bean.getSourceName())?bean.getSourceName():bean.getSource());

        if (getCurrentPos()==baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){
            rl_chapter_source_view.setBackgroundResource(R.drawable.v2_blue_8corner_bg);
            tv_source_site.setTextColor(Color.WHITE);
            iv_status.setVisibility(View.VISIBLE);
        }else{
            tv_source_site.setTextColor(context.getResources().getColor(R.color.color_black_ff333333));
            rl_chapter_source_view.setBackgroundResource(R.drawable.white_corner_card_8dp);
            iv_status.setVisibility(View.GONE);
        }

    }


}
