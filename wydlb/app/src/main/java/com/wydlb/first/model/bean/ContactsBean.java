package com.wydlb.first.model.bean;

import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.RxTool;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * 书架缓存数据
 */
@Entity
public class ContactsBean implements Serializable {

    private static final long serialVersionUID = 536871008L;

    @Id(autoincrement = true)
    private Long _id;

    @Unique
    private String mobile;
    private String nick;

    @Generated(hash = 1235213800)
    public ContactsBean(Long _id, String mobile, String nick) {
        this._id = _id;
        this.mobile = mobile;
        this.nick = nick;
    }
    @Generated(hash = 747317112)
    public ContactsBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getNick() {
        return this.nick;
    }
    public void setNick(String nick) {
        this.nick = nick;
    }


}
