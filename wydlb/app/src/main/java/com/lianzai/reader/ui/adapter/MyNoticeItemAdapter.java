package com.lianzai.reader.ui.adapter;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.MyNoticeDataBean;
import com.lianzai.reader.ui.activity.circle.ActivityMyNotice;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.EllipsizeTextView;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2019/3/21.
 * 我的通知和我的评论共用视图
 */

public class MyNoticeItemAdapter extends BaseQuickAdapter<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean,BaseViewHolder> {

    ContentClickListener contentClickListener;

    public MyNoticeItemAdapter(@Nullable List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> data) {
        super(R.layout.item_my_notice, data);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean bean) {
        View view_content=baseViewHolder.getView(R.id.view_content);
        CircleImageView iv_logo= baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname= baseViewHolder.getView(R.id.tv_nickname);

        TextView tv_dynamic= baseViewHolder.getView(R.id.tv_dynamic);
        SelectableRoundedImageView iv_dynamic=baseViewHolder.getView(R.id.iv_dynamic);

        EllipsizeTextView tv_expandable_text=baseViewHolder.getView(R.id.tv_expandable_text);

        TextView tv_date=baseViewHolder.getView(R.id.tv_date);


        //主体区域点击
        view_content.setOnClickListener(
                v -> {
                    getContentClickListener().contentClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );

        iv_logo.setOnClickListener(
                v -> {
                    getContentClickListener().headNickClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );


        RxImageTool.loadLogoImage(mContext,bean.getComUsrePic(),iv_logo);

        if(mContext instanceof ActivityMyNotice) {
            StringBuilder sb = new StringBuilder(bean.getComUserName());
            if (bean.getType() == 1) {
                sb.append(" 评论了你");
            } else {
                sb.append(" 回复了你");
            }
            tv_nickname.setText(sb.toString());
        }else {
            if (bean.getType() == 1) {
                //评论
                tv_nickname.setText("我回复的内容");
            } else {
                //回复
                tv_nickname.setText("我回复的内容");
            }
        }

        //已被删除的动态
        if(bean.getAuditStatus() == 4){
            tv_expandable_text.setText("该内容已被删除");
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            tv_expandable_text.setBackgroundResource(R.drawable.gray_corner_bg_f5f5f5);
            tv_expandable_text.setPadding(20,6,20,6);
            tv_expandable_text.setTextSize(14);
        }else if(bean.getAuditStatus() == 5){
            tv_expandable_text.setText("--该内容举报过多，连载客服正在审核中--");
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            tv_expandable_text.setBackgroundResource(R.drawable.gray_corner_bg_f5f5f5);
            tv_expandable_text.setPadding(20,6,20,6);
            tv_expandable_text.setTextSize(14);
        }else {
//            tv_expandable_text.setText(bean.getContentShow());
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
            tv_expandable_text.setBackgroundResource(R.color.transparent);
            tv_expandable_text.setPadding(0,0,10,0);
            tv_expandable_text.setTextSize(16);

            URLUtils.replaceBook(bean.getContentShow().replace("\n"," "),mContext,tv_expandable_text,bean.getUrlTitle());
        }

        tv_date.setText(TimeFormatUtil.getInterval(String.valueOf(bean.getCreateTime())) + " | " + bean.getPlatformName());

        if(TextUtils.isEmpty(bean.getPicturesShow())){
            tv_dynamic.setText(bean.getPostContent());
            iv_dynamic.setVisibility(View.GONE);
        }else {
            tv_dynamic.setText("");
            iv_dynamic.setVisibility(View.VISIBLE);
            RxImageTool.loadImage(mContext,bean.getPicturesShow(),iv_dynamic);
        }

    }


    public interface ContentClickListener{
        void headNickClick(View v, int pos);
        void praiseClick(View v, int pos);
        void replyClick(View v, int pos);
        void contentClick(View v, int pos);
    }
}
