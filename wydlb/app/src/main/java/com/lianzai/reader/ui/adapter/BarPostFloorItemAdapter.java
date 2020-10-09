package com.lianzai.reader.ui.adapter;

import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.CommentDetailBean;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 评论详情回复列表
 */

public class BarPostFloorItemAdapter extends BaseQuickAdapter<CommentDetailBean.DataBean.ListBean,BaseViewHolder> {

    ContentClickListener contentClickListener;

    public BarPostFloorItemAdapter(@Nullable List<CommentDetailBean.DataBean.ListBean> data) {
        super(R.layout.item_bar_postfloor_comment, data);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, CommentDetailBean.DataBean.ListBean bean) {
        View view_content=baseViewHolder.getView(R.id.view_content);
        CircleImageView iv_logo= baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname= baseViewHolder.getView(R.id.tv_nickname);

        LinearLayout praise_view= baseViewHolder.getView(R.id.praise_view);
        TextView tv_praise_count=baseViewHolder.getView(R.id.tv_praise_count);
        ImageView shine_btn=baseViewHolder.getView(R.id.shine_btn);

        TextView tv_expandable_text=baseViewHolder.getView(R.id.tv_expandable_text);

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
        tv_nickname.setOnClickListener(
                v -> {
                    getContentClickListener().headNickClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );

        praise_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentClickListener().praiseClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }
        });

        RxImageTool.loadLogoImage(mContext,bean.getReplyUsrePic(),iv_logo);
        tv_nickname.setText(bean.getReplyUserName());

        if(bean.getParentId() == 0){
            if(bean.getAuditStatus() == 4){
                tv_expandable_text.setText("该内容已被删除");
                tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            }else if(bean.getAuditStatus() == 5){
                tv_expandable_text.setText("--该内容举报过多，连载客服正在审核中--");
                tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            }else {
//                tv_expandable_text.setText(bean.getContentShow());
                tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
                URLUtils.replaceBook(bean.getContentShow(),mContext,tv_expandable_text,bean.getUrlTitle());
            }
        }else {
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
            if(bean.getAuditStatus() == 4){
                SpannableString mString = new SpannableString ("回复 " + bean.getBeReplyUserName() + "：" + "该内容已被删除");
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.StyleReplyName),3,3 + bean.getBeReplyUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.DeleteReplyContent),3 + bean.getBeReplyUserName().length(),mString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_expandable_text.setText(mString);
            }if(bean.getAuditStatus() == 5){
                SpannableString mString = new SpannableString ("回复 " + bean.getBeReplyUserName() + "：" + "--该内容举报过多，连载客服正在审核中--");
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.StyleReplyName),3,3 + bean.getBeReplyUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mString.setSpan(new TextAppearanceSpan(mContext, R.style.DeleteReplyContent),3 + bean.getBeReplyUserName().length(),mString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_expandable_text.setText(mString);
            }else {
//                SpannableString mString = new SpannableString ("回复 " + bean.getBeReplyUserName() + "：" + bean.getContentShow());
//                mString.setSpan(new TextAppearanceSpan(mContext, R.style.StyleReplyName),3,3 + bean.getBeReplyUserName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tv_expandable_text.setText(mString);
                URLUtils.replaceBookForReplyNick(bean.getBeReplyUserName(),bean.getContentShow(),mContext,tv_expandable_text,bean.getUrlTitle());
            }
        }

        tv_praise_count.setText(RxDataTool.getLikeCount(bean.getLikeCount()));
        if(bean.isIsLike()) {
            shine_btn.setImageResource(R.mipmap.icon_dynamic_praised);
        }else {
            shine_btn.setImageResource(R.mipmap.icon_dynamic_not_praise);
        }

        tv_date.setText(TimeFormatUtil.getInterval(bean.getCreateTime()));

    }


    public interface ContentClickListener{
        void headNickClick(View v, int pos);
        void praiseClick(View v, int pos);
        void replyClick(View v, int pos);
        void contentClick(View v, int pos);
    }
}
