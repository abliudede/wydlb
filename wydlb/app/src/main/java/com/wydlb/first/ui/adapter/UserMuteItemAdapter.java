package com.wydlb.first.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.BannedListBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 黑名单item
 */

public class UserMuteItemAdapter extends BaseQuickAdapter<BannedListBean.DataBean.ListBean, BaseViewHolder> {

    Context context;
//    Drawable nanDrawable;
//    Drawable nvDrawable;

    private OnPersonClickListener onPersonClickListener;

    public OnPersonClickListener getOnPersonClickListener() {
        return onPersonClickListener;
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }

    public UserMuteItemAdapter(@Nullable List<BannedListBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_person2, data);
        this.context = mContext;

//        nanDrawable = RxImageTool.getDrawable(R.mipmap.nan_icon);
//        nvDrawable = RxImageTool.getDrawable(R.mipmap.nv_icon);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BannedListBean.DataBean.ListBean bean) {


        CircleImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);
        TextView attention_tv = baseViewHolder.getView(R.id.attention_tv);
        ImageView iv_gender = baseViewHolder.getView(R.id.iv_gender);
        TextView tv_des = baseViewHolder.getView(R.id.tv_des);


        RxImageTool.loadLogoImage(context,bean.getAvatar(),iv_logo);
        tv_nickname.setText(bean.getNickName());
        int minute = bean.getReadTime()/60;
        if(minute >= 1) {
            tv_des.setText("阅读时长："+ String.valueOf(minute) + "分钟");
        }else {
            tv_des.setText("阅读时长：少于1分钟");
        }
        //0男1女
        if(bean.getGender() == 0){
            iv_gender.setImageResource(R.mipmap.nan_icon);
        }else {
            iv_gender.setImageResource(R.mipmap.nv_icon);
        }

//        if(bean.()){
            attention_tv.setBackgroundResource(R.drawable.blue_light_15dpconor_bg);
            attention_tv.setText("解除禁言");
            attention_tv.setTextColor(mContext.getResources().getColor(R.color.bluetext_color));
            attention_tv.setVisibility(View.VISIBLE);
//        }else {
//            attention_tv.setVisibility(View.GONE);
//        }


        iv_logo.setOnClickListener(
                v->onPersonClickListener.avatorClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
        attention_tv.setOnClickListener(
                v->onPersonClickListener.removeClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );
    }

    public interface OnPersonClickListener{
        void removeClick(int pos);
        void avatorClick(int pos);
    }


}
