package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 外站书籍目录
 */

public class OutsideChapterAdapter extends BaseQuickAdapter<BookChapterBean, BaseViewHolder> {
    Context context;

    private int currentChapterPos=0;

    Drawable vipDrawable;
    public OutsideChapterAdapter(@Nullable List<BookChapterBean> data, Context mContext) {
        super(R.layout.item_outside_chapter, data);
        this.context = mContext;
        int size= RxImageTool.dp2px(20);
        vipDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_chapter_vip);
        vipDrawable.setBounds(0,0, size,size);
    }

    public int getCurrentChapterPos() {
        return currentChapterPos;
    }

    public void setCurrentChapterPos(int currentChapterPos) {
        this.currentChapterPos = currentChapterPos;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookChapterBean bean) {
        TextView tv_book_chapter_name=baseViewHolder.getView(R.id.tv_book_chapter_name);
        if (bean.getIsVip()){
            tv_book_chapter_name.setText(bean.getTitle());
            tv_book_chapter_name.setCompoundDrawables(null,null,vipDrawable,null);
        }else{
            tv_book_chapter_name.setText(bean.getTitle());
            tv_book_chapter_name.setCompoundDrawables(null,null,null,null);
        }
        if (getCurrentChapterPos()==baseViewHolder.getAdapterPosition() - getHeaderLayoutCount()){
            tv_book_chapter_name.setTextColor(mContext.getResources().getColor(R.color.v2_blue_color));
        }else{
            tv_book_chapter_name.setTextColor(mContext.getResources().getColor(R.color.second_text_color));
        }
    }


}
