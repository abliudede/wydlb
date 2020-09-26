package com.lianzai.reader.bean;

/**
 * Created by lrz on 2017/10/19.
 */

public class MessageAndNoticeBean {

    public MessageAndNoticeBean() {
    }

    public MessageAndNoticeBean(String tag, String title, String content, String date) {
        this.tag = tag;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    private String tag;
    private String title;
    private String content;
    private String date;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
