package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class AddImageAdapter extends BaseItemDraggableAdapter<String,BaseViewHolder> {

    int width=0;
    OnImageClickListener onImageClickListener;

    Context context;
    public AddImageAdapter(@Nullable List<String> imgs, Context mContext,int mWidth) {
        super(R.layout.item_add_image, imgs);
        this.context=mContext;
        width= mWidth;
    }

    public OnImageClickListener getOnImageClickListener() {
        return onImageClickListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,String img) {
        SelectableRoundedImageView imageView=baseViewHolder.getView(R.id.iv_add_img);
        ImageView iv_close=baseViewHolder.getView(R.id.iv_close);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width-RxImageTool.dip2px(10),width-RxImageTool.dip2px(10));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);

        if (img.equals("add")){//add image
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setBackgroundResource(R.drawable.v2_add_pic_bg_corner);
            imageView.setImageResource(R.mipmap.icon_v3_circle_add_picture);
            iv_close.setVisibility(View.GONE);
            imageView.setOnClickListener(
                    v->onImageClickListener.addImgClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
            );
        }else{
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(img).into(imageView);
            iv_close.setVisibility(View.VISIBLE);
            iv_close.setOnClickListener(
                    v->onImageClickListener.closeImgClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
            );
            imageView.setOnClickListener(
                    v->onImageClickListener.previewImgClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
            );
        }
    }


    public interface OnImageClickListener{
        void closeImgClick(int pos);
        void previewImgClick(int pos);
        void addImgClick(int pos);
    }

}
