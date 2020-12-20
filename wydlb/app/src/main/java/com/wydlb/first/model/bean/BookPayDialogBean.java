package com.wydlb.first.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by newbiechen on 17-5-10.
 * 用于记录每本书的打赏弹窗周期
 */
@Entity
public class BookPayDialogBean {

    @Id(autoincrement = true)
    private Long _id;

    @Unique
    private String bid;//书籍id

    private String startTime;//开始时间

    private String endTime;//结束时间

    private int count;//本周期进入次数

    private boolean isShow;//是否已经弹窗

    @Generated(hash = 1241056128)
    public BookPayDialogBean(Long _id, String bid, String startTime, String endTime,
            int count, boolean isShow) {
        this._id = _id;
        this.bid = bid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
        this.isShow = isShow;
    }

    @Generated(hash = 1673256261)
    public BookPayDialogBean() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getBid() {
        return this.bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean getIsShow() {
        return this.isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }


}