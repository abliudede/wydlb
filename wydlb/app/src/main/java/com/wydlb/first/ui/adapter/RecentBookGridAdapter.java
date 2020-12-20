package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.model.bean.BookStoreBeanN;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

/**
 * 最近阅读-九宫格
 */
public class RecentBookGridAdapter extends BaseItemDraggableAdapter<BookStoreBeanN,BaseViewHolder> {

//    Drawable unReadDrawable;

    int imageWidth=0;
    Context context;



    public RecentBookGridAdapter(@Nullable List<BookStoreBeanN> data, Context mContext) {
        super(R.layout.item_book_store_grid, data);
        this.context=mContext;
        imageWidth= (RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(60))/3;

//        int iconSize= RxImageTool.dp2px(5);
//        unReadDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_red_dot);
//        unReadDrawable.setBounds(0,0, iconSize,iconSize);

    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookStoreBeanN bean) {
        SelectableRoundedImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);


        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(imageWidth,4*imageWidth/3);
        roundedImageView.setLayoutParams(layoutParams);

        RxImageTool.loadBookStoreImage(context,bean.getPlatformCover(),roundedImageView);


        baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getPlatformName())?"":bean.getPlatformName());

//        if (bean.isUnread()){
//            baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getPlatformName())?"":bean.getPlatformName());
//            ((TextView)baseViewHolder.getView(R.id.tv_book_name)).setCompoundDrawables(null,null,unReadDrawable,null);
//        }else{
//            baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getPlatformName())?"":bean.getPlatformName());
//            ((TextView)baseViewHolder.getView(R.id.tv_book_name)).setCompoundDrawables(null,null,null,null);
//        }

    }



}
