package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookCategoryListResponse;
import com.lianzai.reader.bean.BookSourceResponse;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.page.TxtChapter;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 书本章节目录适配器
 */

public class BookChapterAdapter extends BaseQuickAdapter<TxtChapter, BaseViewHolder> {

    Context context;


    private int chapterCurrentPosition=1;//顺序的位置索引


    private boolean isNightMode=false;

    public void setCurrentChapterPosition(int currentPos) {
        this.chapterCurrentPosition = currentPos;
        notifyDataSetChanged();
    }

    public int getChapterCurrentPosition() {
        return chapterCurrentPosition;
    }

    public BookChapterAdapter(@Nullable List<TxtChapter> data, Context mContext) {
        super(R.layout.item_book_chapter, data);
        this.context = mContext;
    }


    public void setNightMode(boolean nightMode) {
        isNightMode = nightMode;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,TxtChapter bean) {
        TextView tv_chapter_name=baseViewHolder.getView(R.id.tv_chapter_name);
        ImageView iv_chapter_is_vip=baseViewHolder.getView(R.id.iv_chapter_is_vip);

        if (!TextUtils.isEmpty(bean.getTitle())){
            tv_chapter_name.setText(bean.getTitle());
        }else{
            tv_chapter_name.setText("");
        }

        if (getChapterCurrentPosition()==baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){//索引
            tv_chapter_name.setTextColor(context.getResources().getColor(R.color.v2_blue_color));
        }else{//如果此章节已经缓存 #FF999999颜色 未缓存 #FF333333
            if (!TextUtils.isEmpty(bean.getLink())&& BookManager
                    .isChapterCached(bean.getBookId(),bean.getChapterId())){
                tv_chapter_name.setTextColor(context.getResources().getColor(R.color.color_black_ff999999));
            }else{
                tv_chapter_name.setTextColor(context.getResources().getColor(R.color.color_black_ff333333));
            }
        }

        if (bean.isChapterVip()){//是否是vip
            iv_chapter_is_vip.setVisibility(View.VISIBLE);
        }else {
            iv_chapter_is_vip.setVisibility(View.GONE);
        }
    }


}
