package com.lianzai.reader.view.page;

import java.io.Serializable;

/**
 * Copyright (C), 2018
 * FileName: UrlsVo
 * Author: lrz
 * Date: 2018/11/17 11:32
 * Description: ${DESCRIPTION}
 */
public class UrlsVo implements Serializable{
    private String source;
    private String sourceUrl;
    private String sourceName;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

}
