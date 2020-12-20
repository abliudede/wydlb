package com.wydlb.first.bean;

/**
 * Copyright (C), 2018
 * FileName: BarrageBean
 * Author: lrz
 * Date: 2018/9/19 10:39
 * Description: ${DESCRIPTION}
 */
public class BarrageBean {
    private String logoUrl;
    private String text;

    public BarrageBean() {
    }

    public BarrageBean(String logoUrl, String text) {
        this.logoUrl = logoUrl;
        this.text = text;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
