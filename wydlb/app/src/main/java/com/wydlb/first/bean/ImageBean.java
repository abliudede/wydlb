package com.wydlb.first.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2018/3/16.
 */

public class ImageBean implements Serializable {
    private String uri;

    private int tag;//0 local 1 network

    public ImageBean(String uri, int tag) {
        this.uri = uri;
        this.tag = tag;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
