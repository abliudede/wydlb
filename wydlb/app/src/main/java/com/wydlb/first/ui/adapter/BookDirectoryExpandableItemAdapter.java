package com.wydlb.first.ui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wydlb.first.R;
import com.wydlb.first.bean.BookDirectoryBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxTool;

import java.util.List;

public class BookDirectoryExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = BookDirectoryExpandableItemAdapter.class.getSimpleName();

    public static final int VOLUME = 0;
    public static final int CHAPTER = 1;

    Drawable drawableOn;
    Drawable drawableOff;

    int chapterPosition=0;

    public int getChapterPosition() {
        return chapterPosition;
    }

    public void setChapterPosition(int chapterPosition) {
        this.chapterPosition = chapterPosition;
        notifyDataSetChanged();
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BookDirectoryExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(VOLUME, R.layout.item_book_volume_name);
        addItemType(CHAPTER, R.layout.item_book_directory_name);
        drawableOn= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_directory_on);
        drawableOff=RxTool.getContext().getResources().getDrawable(R.mipmap.icon_directory_off);

        int iconSize= RxImageTool.dp2px(20);

        drawableOn.setBounds(0,0, iconSize,iconSize);
        drawableOff.setBounds(0,0, iconSize,iconSize);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case VOLUME:
                BookDirectoryBean.VolumeChaptersBean bean=(BookDirectoryBean.VolumeChaptersBean)item;
                TextView tvBookDirectoryName=holder.getView(R.id.tv_book_volume_name);
                tvBookDirectoryName.setText(bean.getTitle()+"(共"+bean.getCount()+"章)");
                int pos=holder.getAdapterPosition() - getHeaderLayoutCount();
                if (bean.isExpanded()){
                    tvBookDirectoryName.setCompoundDrawables(null,null,drawableOff,null);
                }else{
                    tvBookDirectoryName.setCompoundDrawables(null,null,drawableOn,null);
                }
                tvBookDirectoryName.setOnClickListener(
                        v->{
                            RxLogTool.e("tvBookDirectoryName......"+bean.isExpanded()+bean.getTitle()+pos);
                           if (bean.isExpanded()){
                               collapse(pos);
                           }else{
                               expand(pos);
                           }
                        });

                break;
            case CHAPTER:
                BookDirectoryBean.VolumeChaptersBean.RecordBean bean1=(BookDirectoryBean.VolumeChaptersBean.RecordBean)item;
                TextView tv_book_chapter_name=holder.getView(R.id.tv_book_chapter_name);
                tv_book_chapter_name.setText(bean1.getTitle());

                if (getChapterPosition()==bean1.getChapterPos()){//当前章节颜色区分
                    tv_book_chapter_name.setTextColor(mContext.getResources().getColor(R.color.blue_color));
                }else{
                    tv_book_chapter_name.setTextColor(mContext.getResources().getColor(R.color.second_text_color));
                }
//                RxLogTool.e("title:"+bean1.getTitle());
                break;
        }
    }
}
