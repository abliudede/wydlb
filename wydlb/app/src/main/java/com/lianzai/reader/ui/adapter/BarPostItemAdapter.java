package com.lianzai.reader.ui.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.CommentInfoBean;
import com.lianzai.reader.bean.PostDetailBean;
import com.lianzai.reader.interfaces.CommentClickListener;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.CommentListTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrz on 2019/03/13.
 * 帖子详情评论列表
 */

public class BarPostItemAdapter extends BaseQuickAdapter<PostDetailBean.DataBean.CommentPageBean.ListBean,BaseViewHolder> {

    ContentClickListener contentClickListener;

    public BarPostItemAdapter( @Nullable List<PostDetailBean.DataBean.CommentPageBean.ListBean> data) {
        super(R.layout.item_bar_post_comment, data);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, PostDetailBean.DataBean.CommentPageBean.ListBean bean) {
        View view_content=baseViewHolder.getView(R.id.view_content);
        CircleImageView iv_logo= baseViewHolder.getView(R.id.iv_logo);
        TextView tv_nickname= baseViewHolder.getView(R.id.tv_nickname);

        LinearLayout praise_view= baseViewHolder.getView(R.id.praise_view);
        TextView tv_praise_count=baseViewHolder.getView(R.id.tv_praise_count);
        ImageView shine_btn=baseViewHolder.getView(R.id.shine_btn);

        TextView tv_expandable_text = baseViewHolder.getView(R.id.tv_expandable_text);

        CommentListTextView tv_comment_list= baseViewHolder.getView(R.id.tv_comment_list);

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

        RxImageTool.loadLogoImage(mContext,bean.getComUsrePic(),iv_logo);
        tv_nickname.setText(bean.getComUserName());
        //已被删除的动态
        if(bean.getAuditStatus() == 4){
            tv_expandable_text.setText("该内容已被删除");
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            tv_expandable_text.setBackgroundResource(R.drawable.gray_corner_bg_f5f5f5);
            tv_expandable_text.setPadding(20,6,20,6);
            tv_expandable_text.setTextSize(12);
        }else if(bean.getAuditStatus() == 5){
            tv_expandable_text.setText("--该内容举报过多，连载客服正在审核中--");
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
            tv_expandable_text.setBackgroundResource(R.drawable.gray_corner_bg_f5f5f5);
            tv_expandable_text.setPadding(20,6,20,6);
            tv_expandable_text.setTextSize(12);
        }else {
//            tv_expandable_text.setText(bean.getContentShow());
            tv_expandable_text.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
            tv_expandable_text.setBackgroundResource(R.color.white);
            tv_expandable_text.setPadding(0,0,0,0);
            tv_expandable_text.setTextSize(14);

            URLUtils.replaceBook(bean.getContentShow(),mContext,tv_expandable_text,bean.getUrlTitle());
        }


        tv_praise_count.setText(RxDataTool.getLikeCount(bean.getLikeCount()));
        if(bean.isIsLike()) {
            shine_btn.setImageResource(R.mipmap.icon_dynamic_praised);
        }else {
            shine_btn.setImageResource(R.mipmap.icon_dynamic_not_praise);
        }

        tv_date.setText(TimeFormatUtil.getInterval(bean.getCreateTime()));

        if (null!=bean.getReplyResp()&&bean.getTotalReplyCount()>0){
            tv_comment_list.setVisibility(View.VISIBLE);
            List<CommentInfoBean> mCommentInfos = new ArrayList<>();
            PostDetailBean.DataBean.CommentPageBean.ListBean.ReplyRespBean replyBean = bean.getReplyResp();
            if(replyBean.getAuditStatus() == 4){
                tv_comment_list.setCommentColor (Color.parseColor ("#666666"));
                mCommentInfos.add (new CommentInfoBean().setId (replyBean.getId()).setComment ("该内容已被删除").setNickName (replyBean.getReplyUserName()).setToNickName(replyBean.getBeReplyUserName()));
            }else if(replyBean.getAuditStatus() == 5){
                tv_comment_list.setCommentColor (Color.parseColor ("#666666"));
                mCommentInfos.add (new CommentInfoBean().setId (replyBean.getId()).setComment ("--该内容举报过多，连载客服正在审核中--").setNickName (replyBean.getReplyUserName()).setToNickName(replyBean.getBeReplyUserName()));
            }else if(replyBean.getParentId() == 0){
                tv_comment_list.setCommentColor (Color.parseColor ("#333333"));
                mCommentInfos.add (new CommentInfoBean().setId (replyBean.getId()).setComment (replyBean.getContentShow()).setNickName (replyBean.getReplyUserName()).setJson(replyBean.getUrlTitle()));
            }else {
                tv_comment_list.setCommentColor (Color.parseColor ("#333333"));
                mCommentInfos.add (new CommentInfoBean().setId (replyBean.getId()).setComment (replyBean.getContentShow()).setNickName (replyBean.getReplyUserName()).setToNickName(replyBean.getBeReplyUserName()).setJson(replyBean.getUrlTitle()));
            }
            tv_comment_list.setMaxlines(1);
            tv_comment_list.setmCount(bean.getTotalReplyCount());
            tv_comment_list.setMoreStr("共"+bean.getTotalReplyCount()+"条回复 >");
            initCommentsData(tv_comment_list,mCommentInfos,baseViewHolder,bean);
        }else{
            tv_comment_list.setVisibility(View.GONE);
        }
    }

    private void initCommentsData(CommentListTextView tvCommentList,List<CommentInfoBean> mCommentInfos ,BaseViewHolder baseViewHolder,PostDetailBean.DataBean.CommentPageBean.ListBean bean){


        tvCommentList.setNameColor (Color.parseColor ("#333333"));
        tvCommentList.setTalkStr ("回复");
        tvCommentList.setTalkColor (Color.parseColor ("#2A2A2A"));

        tvCommentList.setonCommentListener(new CommentClickListener() {
            @Override
            public void onNickNameClick(int position, CommentInfoBean mInfo) {

                RxLogTool.e("onNickNameClick:"+mInfo.getNickName()+"--position:"+position);
              getContentClickListener().replyClick(tvCommentList,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }

            @Override
            public void onToNickNameClick(int position, CommentInfoBean mInfo) {
                RxLogTool.e("onToNickNameClick:"+mInfo.getToNickName()+"--position:"+position);
                getContentClickListener().replyClick(tvCommentList,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }

            @Override
            public void onCommentItemClick(int position, CommentInfoBean mInfo) {
                RxLogTool.e("onCommentItemClick:"+mInfo.getComment()+"--position:"+position);
                getContentClickListener().replyClick(tvCommentList,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }

            @Override
            public void onOtherClick() {
                RxLogTool.e("onOtherClick---");
                getContentClickListener().replyClick(tvCommentList,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
            }

            @Override
            public void onWebClick(String url) {
                ActivityWebView.startActivity(mContext,url);
            }

            @Override
            public void onSearchClick(String search) {
                ActivitySearchFirst.skiptoSearch(search,mContext);
            }
        });

        tvCommentList.setData (mCommentInfos);

    }

    public interface ContentClickListener{
        void headNickClick(View v,int pos);
        void praiseClick(View v,int pos);
        void replyClick(View v,int pos);
        void contentClick(View v,int pos);
    }
}
