package com.wydlb.first.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by lrz on 2018/4/4.
 */

@Entity
public class TokenBean implements Serializable {

    private static final long serialVersionUID = 56423411313L;


    @Id(autoincrement=true)
    private Long _id;

    private String token;
    private long expireTime;
    private boolean ok;

    @Generated(hash = 932521429)
    public TokenBean(Long _id, String token, long expireTime, boolean ok) {
        this._id = _id;
        this.token = token;
        this.expireTime = expireTime;
        this.ok = ok;
    }

    @Generated(hash = 1886787915)
    public TokenBean() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }


    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public boolean getOk() {
        return this.ok;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "_id=" + _id +
                ", token='" + token + '\'' +
                ", expireTime=" + expireTime +
                ", ok=" + ok +
                '}';
    }

}
