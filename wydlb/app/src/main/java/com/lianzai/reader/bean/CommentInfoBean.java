package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2018/1/8.
 */

public class CommentInfoBean implements Serializable{
    /**
     * 评论ID
     */
    private int id;
    /**
     * 评论人名称
     */
    private String nickName;
    /**
     * 评论内容
     */
    private String comment;
    /**
     * 被评论人名称
     */
    private String toNickName;

    private String json;

    public String getJson() {
        return json;
    }

    public CommentInfoBean setJson(String json) {
        this.json = json;
        return this;
    }

    public int getId() {
        return id;
    }

    public CommentInfoBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public CommentInfoBean setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CommentInfoBean setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getToNickName() {
        return toNickName;
    }

    public CommentInfoBean setToNickName(String toNickName) {
        this.toNickName = toNickName;
        return this;
    }

    @Override
    public String toString() {
        return "CommentInfoBean{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", comment='" + comment + '\'' +
                ", toNickName='" + toNickName + '\'' +
                '}';
    }
}
