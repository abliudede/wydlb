package com.wydlb.first.ui.adapter;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wydlb.first.R;
import com.wydlb.first.bean.BookCatalogResponse;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxTool;

import java.util.List;

public class BookCatalogExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = BookCatalogExpandableItemAdapter.class.getSimpleName();

    public static final int VOLUME = 0;
    public static final int CHAPTER = 1;

    Drawable drawableOn;
    Drawable drawableOff;

    int chapterPosition=0;

    Drawable vipDrawable;

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
    public BookCatalogExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(VOLUME, R.layout.item_book_volume_name);
        addItemType(CHAPTER, R.layout.item_book_directory_name);
        drawableOn= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_directory_on);
        drawableOff=RxTool.getContext().getResources().getDrawable(R.mipmap.icon_directory_off);

        int iconSize= RxImageTool.dp2px(20);

        drawableOn.setBounds(0,0, iconSize,iconSize);
        drawableOff.setBounds(0,0, iconSize,iconSize);

        vipDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_chapter_vip);
        vipDrawable.setBounds(0,0, iconSize,iconSize);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case VOLUME:
                BookCatalogResponse.DataBean.VolumesBean bean=(BookCatalogResponse.DataBean.VolumesBean)item;
                TextView tv_book_volume_name=holder.getView(R.id.tv_book_volume_name);
                tv_book_volume_name.setText(bean.getTitle()+"(共"+bean.getChapters().size()+"章)");
                int pos=holder.getAdapterPosition() - getHeaderLayoutCount();
                if (bean.isExpanded()){
                    tv_book_volume_name.setCompoundDrawables(null,null,drawableOff,null);
                }else{
                    tv_book_volume_name.setCompoundDrawables(null,null,drawableOn,null);
                }
//                tv_book_volume_name.setOnClickListener(
//                        v->{
//                            RxLogTool.e("tvBookDirectoryName......"+bean.isExpanded()+bean.getTitle()+pos);
//                           if (bean.isExpanded()){
//                               collapse(pos);
//                           }else{
//                               expand(pos);
//                           }
//                        });

                break;
            case CHAPTER:
                BookCatalogResponse.DataBean.VolumesBean.ChaptersBean bean1=(BookCatalogResponse.DataBean.VolumesBean.ChaptersBean)item;
                TextView tv_book_chapter_name=holder.getView(R.id.tv_book_chapter_name);

                if (bean1.isVip()){
                    tv_book_chapter_name.setText(bean1.getTitle());
                    tv_book_chapter_name.setCompoundDrawables(null,null,vipDrawable,null);
                }else{
                    tv_book_chapter_name.setText(bean1.getTitle());
                    tv_book_chapter_name.setCompoundDrawables(null,null,null,null);
                }

                RxLogTool.e("getChapterPos:"+bean1.getChapterPos());
                if (getChapterPosition()==bean1.getChapterPos()){//当前章节颜色区分
                    tv_book_chapter_name.setTextColor(mContext.getResources().getColor(R.color.blue_color));
                }else{
                    tv_book_chapter_name.setTextColor(mContext.getResources().getColor(R.color.second_text_color));
                }
                break;
        }
    }
}
