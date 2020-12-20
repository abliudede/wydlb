package com.wydlb.first.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Copyright (C), 2018
 * FileName: MyBookListBean
 * Author: lrz
 * Date: 2018/10/23 16:43
 * Description: ${DESCRIPTION}
 */
@Entity
public class MyBookListBean implements Serializable{

    private static final long serialVersionUID = 536871008L;

    @Id(autoincrement = true)
    private Long _id;

    private int id;
    private String booklistName;
    private String booklistIntro;
    private String cover;
    private String booklistAuthor;
    private boolean isUnread;

    private int uid;

    private long indexTime;

    private long updateDate=0;

    private int topIndex=0;

    @Generated(hash = 854145084)
    public MyBookListBean(Long _id, int id, String booklistName,
            String booklistIntro, String cover, String booklistAuthor,
            boolean isUnread, int uid, long indexTime, long updateDate,
            int topIndex) {
        this._id = _id;
        this.id = id;
        this.booklistName = booklistName;
        this.booklistIntro = booklistIntro;
        this.cover = cover;
        this.booklistAuthor = booklistAuthor;
        this.isUnread = isUnread;
        this.uid = uid;
        this.indexTime = indexTime;
        this.updateDate = updateDate;
        this.topIndex = topIndex;
    }




    @Generated(hash = 932815212)
    public MyBookListBean() {
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBooklistName() {
        return booklistName;
    }

    public void setBooklistName(String booklistName) {
        this.booklistName = booklistName;
    }

    public String getBooklistIntro() {
        return booklistIntro;
    }

    public void setBooklistIntro(String booklistIntro) {
        this.booklistIntro = booklistIntro;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBooklistAuthor() {
        return booklistAuthor;
    }

    public void setBooklistAuthor(String booklistAuthor) {
        this.booklistAuthor = booklistAuthor;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(long indexTime) {
        this.indexTime = indexTime;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public int getTopIndex() {
        return topIndex;
    }

    public void setTopIndex(int topIndex) {
        this.topIndex = topIndex;
    }

    @Override
    public String toString() {
        return "MyBookListBean{" +
                "id=" + id +
                ", booklistName='" + booklistName + '\'' +
                ", booklistIntro='" + booklistIntro + '\'' +
                ", cover='" + cover + '\'' +
                ", booklistAuthor='" + booklistAuthor + '\'' +
                ", isUnread=" + isUnread +
                ", uid=" + uid +
                ", indexTime=" + indexTime +
                ", updateDate=" + updateDate +
                ", topIndex=" + topIndex +
                '}';
    }

    public boolean getIsUnread() {
        return this.isUnread;
    }

    public void setIsUnread(boolean isUnread) {
        this.isUnread = isUnread;
    }




    public Long get_id() {
        return this._id;
    }




    public void set_id(Long _id) {
        this._id = _id;
    }
}
