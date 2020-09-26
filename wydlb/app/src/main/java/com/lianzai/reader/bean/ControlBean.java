package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2018/1/26.
 */

public class ControlBean implements Serializable{
    public boolean isRefresh=true;//是否刷新
    public int optionIndex=-1;//操作索引
    public int tag=0;

    public ControlBean(boolean isRefresh, int optionIndex, int tag) {
        this.isRefresh = isRefresh;
        this.optionIndex = optionIndex;
        this.tag = tag;
    }

    public ControlBean(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public ControlBean(boolean isRefresh, int optionIndex) {
        this.isRefresh = isRefresh;
        this.optionIndex = optionIndex;
    }

    @Override
    public String toString() {
        return "ControlBean{" +
                "isRefresh=" + isRefresh +
                ", optionIndex=" + optionIndex +
                ", tag=" + tag +
                '}';
    }
}
