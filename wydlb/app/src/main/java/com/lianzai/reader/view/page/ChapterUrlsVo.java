package com.lianzai.reader.view.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: ChapterUrlsVo
 * Author: lrz
 * Date: 2018/11/17 11:25
 * Description: ${DESCRIPTION}路径集合
 */
public class ChapterUrlsVo implements Serializable{

    private String bookId;

    private String chapterId;


    private List<UrlsVo>urlsVoList=new ArrayList<>();

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public List<UrlsVo> getUrlsVoList() {
        return urlsVoList;
    }

    public void setUrlsVoList(List<UrlsVo> urlsVoList) {
        this.urlsVoList = urlsVoList;
    }


    @Override
    public String toString() {
        return "ChapterUrlsVo{" +
                "bookId='" + bookId + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", urlsVoList=" + urlsVoList +
                '}';
    }
}
