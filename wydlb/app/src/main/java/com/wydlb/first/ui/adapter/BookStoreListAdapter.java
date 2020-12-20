package com.wydlb.first.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.model.bean.BookStoreBeanN;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * 书架
 */
public class BookStoreListAdapter extends BaseItemDraggableAdapter<BookStoreBeanN,BaseViewHolder> {


    Drawable unReadDrawable;
    Context context;

    boolean isEditMode=false;


    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }


    public BookStoreListAdapter(@Nullable List<BookStoreBeanN> data, Context mContext) {
        super(R.layout.item_book_store_list, data);
        this.context=mContext;
        int iconSize= RxImageTool.dp2px(5);

        unReadDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_red_dot);
        unReadDrawable.setBounds(0,0, iconSize,iconSize);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookStoreBeanN bean) {
        final SelectableRoundedImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(context,bean.getPlatformCover(),roundedImageView);
        ImageView iv_status=baseViewHolder.getView(R.id.iv_status);

        if (bean.getIsUnread()){
            baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getPlatformName())?"":bean.getPlatformName());
            ((TextView)baseViewHolder.getView(R.id.tv_book_name)).setCompoundDrawables(null,null,unReadDrawable,null);
        }else{
            baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getPlatformName())?"":bean.getPlatformName());
            ((TextView)baseViewHolder.getView(R.id.tv_book_name)).setCompoundDrawables(null,null,null,null);
        }

        if (isEditMode()){
            iv_status.setVisibility(View.VISIBLE);
            if (bean.getIsSelect()){
                iv_status.setImageResource(R.mipmap.v2_icon_blue_selected);
            }else {
                iv_status.setImageResource(R.mipmap.v2_icon_not_selected);
            }
        }else{
            iv_status.setVisibility(View.GONE);
        }

        baseViewHolder.setText(R.id.tv_book_author,TextUtils.isEmpty(bean.getPenName())?"":bean.getPenName());
        baseViewHolder.setText(R.id.tv_book_last_chapter,TextUtils.isEmpty(bean.getPlatformIntroduce())?"":bean.getPlatformIntroduce());

    }



}
