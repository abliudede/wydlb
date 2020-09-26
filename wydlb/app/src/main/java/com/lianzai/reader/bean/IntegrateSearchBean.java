package com.lianzai.reader.bean;

/**
 * Copyright (C), 2018
 * FileName: IntegrateSearchResponse
 * Author: lrz
 * Date: 2018/10/19 15:47
 * Description: 综合搜索
 */
public class IntegrateSearchBean {
    private int tag;//0 书

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public IntegrateSearchBean(int tag, IntegrateSearchResponse.DataBean.BooklistBean booklistBean, IntegrateSearchResponse.DataBean.PlatformBean platformBean) {
        this.tag = tag;
        this.booklistBean = booklistBean;
        this.platformBean = platformBean;
    }

    private IntegrateSearchResponse.DataBean.BooklistBean booklistBean;

    private IntegrateSearchResponse.DataBean.PlatformBean platformBean;


    public IntegrateSearchResponse.DataBean.BooklistBean getBooklistBean() {
        return booklistBean;
    }

    public void setBooklistBean(IntegrateSearchResponse.DataBean.BooklistBean booklistBean) {
        this.booklistBean = booklistBean;
    }

    public IntegrateSearchResponse.DataBean.PlatformBean getPlatformBean() {
        return platformBean;
    }

    public void setPlatformBean(IntegrateSearchResponse.DataBean.PlatformBean platformBean) {
        this.platformBean = platformBean;
    }
}
