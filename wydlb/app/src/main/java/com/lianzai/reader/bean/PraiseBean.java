package com.lianzai.reader.bean;

/**
 * Created by lrz on 2018/1/18.
 */

public class PraiseBean {

    /**
     * status_code : 200
     * count : 1
     * index : 468
     * type : 2
     * flag : true
     */

    private int status_code;
    private int count;
    private String index;
    private int type;
    private boolean flag;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
