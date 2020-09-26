package com.lianzai.reader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

@Entity
public class WebHistoryBean implements Serializable {
    private static final long serialVersionUID = 0L;

    @Id(autoincrement=true)
    private Long _id;

    private String title;
    private String  url;
    private long creattime;
    private String icon;
    private String extra;
    private String userId;
    @Generated(hash = 1205991128)
    public WebHistoryBean(Long _id, String title, String url, long creattime,
            String icon, String extra, String userId) {
        this._id = _id;
        this.title = title;
        this.url = url;
        this.creattime = creattime;
        this.icon = icon;
        this.extra = extra;
        this.userId = userId;
    }
    @Generated(hash = 407221786)
    public WebHistoryBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getCreattime() {
        return this.creattime;
    }
    public void setCreattime(long creattime) {
        this.creattime = creattime;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getExtra() {
        return this.extra;
    }
    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }


    
}
