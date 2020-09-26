package com.lianzai.reader.bean;

import android.text.TextUtils;

import com.lianzai.reader.utils.RxTool;

import java.io.Serializable;

public class PublicNumberBean implements Serializable {


    /**
     * content : 大兄弟
     * img : http://img3.redocn.com/tupian/20160108/lvsehuawentuankazhibeijingbiankuang_5728261.jpg
     * title : 哈哈兄弟哈哈哈哈
     */

    private String content;
    private String img;
    private String title;
    private String bookId;
    private String url;
    private String chapterId;
    private String source;
    private String platformName;
    private String teamId;

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public String getContent() {
        return RxTool.filterContent(TextUtils.isEmpty(content)?"":content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "PublicNumberBean{" +
                "content='" + content + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", bookId='" + bookId + '\'' +
                ", url='" + url + '\'' +
                ", chapterId='" + chapterId + '\'' +
                '}';
    }
}
