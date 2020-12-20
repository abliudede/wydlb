package com.wydlb.first.bean;

import java.io.Serializable;

/**
 * @author qicheng.qing
 * @description 表情实体
 * @create 17/1/16,12:01
 */
public class EmjBean implements Serializable {
    private String tag;//表情名称
    private  Integer res;//表情资源ID

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getRes() {
        return res;
    }

    public void setRes(Integer res) {
        this.res = res;
    }

    public EmjBean(String tag, Integer res) {
        this.tag = tag;
        this.res = res;
    }
}
