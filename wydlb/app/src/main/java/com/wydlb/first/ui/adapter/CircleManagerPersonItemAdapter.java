package com.wydlb.first.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.AdministratorListBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 圈子管理员item
 */

public class CircleManagerPersonItemAdapter extends BaseQuickAdapter<AdministratorListBean.DataBean.ListBean, BaseViewHolder> {


    Context context;

    private Drawable nanDrawable;
    private Drawable nvDrawable;

    private OnPersonClickListener onPersonClickListener;

    public OnPersonClickListener getOnPersonClickListener() {
        return onPersonClickListener;
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }

    public CircleManagerPersonItemAdapter(@Nullable List<AdministratorListBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_manager_person, data);
        this.context = mContext;

        nanDrawable = RxImageTool.getDrawable(R.mipmap.nan_icon);
        nvDrawable = RxImageTool.getDrawable(R.mipmap.nv_icon);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,AdministratorListBean.DataBean.ListBean bean) {

        CircleImageView iv_logo = baseViewHolder.getView(R.id.iv_logo);
        TextView tv_remove_manager = baseViewHolder.getView(R.id.tv_remove_manager);
        TextView tv_nickname = baseViewHolder.getView(R.id.tv_nickname);

        ImageView iv_gender = baseViewHolder.getView(R.id.iv_gender);
        TextView tv_des = baseViewHolder.getView(R.id.tv_des);

        TextView post_count = baseViewHolder.getView(R.id.post_count);
        TextView comment_count = baseViewHolder.getView(R.id.comment_count);
        TextView delete_post_count = baseViewHolder.getView(R.id.delete_post_count);
        TextView delete_comment_count = baseViewHolder.getView(R.id.delete_comment_count);
        TextView mute_count = baseViewHolder.getView(R.id.mute_count);


        if(bean.getGender() == 0){
            iv_gender.setImageDrawable(nanDrawable);
        }else {
            iv_gender.setImageDrawable(nvDrawable);
        }

        RxImageTool.loadLogoImage(context,bean.getAvatar(),iv_logo);
        tv_nickname.setText(bean.getNickName());

        int minute = bean.getReadTime()/60;
        if(minute >= 1) {
            tv_des.setText("阅读时长："+ String.valueOf(minute) + "分钟");
        }else {
            tv_des.setText("阅读时长：少于1分钟");
        }

        post_count.setText(String.valueOf(bean.getSendPostCount()));
        comment_count.setText(String.valueOf(bean.getCommentAndReplyCount()));
        delete_post_count.setText(String.valueOf(bean.getDeletePostCount()));
        delete_comment_count.setText(String.valueOf(bean.getDeleteCommentAndReplyCount()));
        mute_count.setText(String.valueOf(bean.getBannedCount()));


        iv_logo.setOnClickListener(
                v->onPersonClickListener.avatorClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );

        tv_remove_manager.setOnClickListener(
                v->onPersonClickListener.removeClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount())
        );

    }

    public interface OnPersonClickListener{
        void removeClick(int pos);
        void avatorClick(int pos);
    }


}
