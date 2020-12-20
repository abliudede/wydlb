package com.wydlb.first.bean;

import com.wydlb.first.utils.RxLogTool;

import java.io.Serializable;

/**
 * Created by lrz on 2017/10/21.
 */

public class DataSynEvent implements Serializable{
    private int tag=0;

    private Object obj;

    public DataSynEvent(Object mObj,int mTag) {
        this.obj = mObj;
        this.tag=mTag;
        RxLogTool.e("mObj:"+mObj+"--tag:"+tag);
    }

    public DataSynEvent(Object obj) {
        this.obj = obj;
    }

    public DataSynEvent(int mTag) {
        this.tag = mTag;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
