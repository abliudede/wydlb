package com.wydlb.first.interfaces;

import com.wydlb.first.bean.CommentInfoBean;

/**
 * Created by lrz on 2018/1/8.
 */

public interface CommentClickListener {

    /**
     * 点击人名的回调
     *
     * @param position 第几条评论  从0开始
     * @param mInfo    评论相关信息
     */
    public void onNickNameClick (int position, CommentInfoBean mInfo);

    /**
     * 点击被评论人名的回调
     *
     * @param position 第几条评论  从0开始
     * @param mInfo    评论相关信息
     */
    public void onToNickNameClick (int position, CommentInfoBean mInfo);

    /**
     * 点击评论文本回调，主要针对对谁回复操作
     *
     * @param position
     * @param mInfo
     */
    public void onCommentItemClick (int position, CommentInfoBean mInfo);

    /**
     * 点击非文字部分
     */
    public void onOtherClick ();
    /**
     * 点击url
     */
    public void onWebClick (String url);
    /**
     * 点击url
     */
    public void onSearchClick (String search);
}
