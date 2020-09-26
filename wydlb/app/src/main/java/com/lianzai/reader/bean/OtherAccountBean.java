package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxDataTool;

import java.io.Serializable;

/**
 * Created by lrz on 2017/11/15.
 */

public class OtherAccountBean extends BaseBean {

    /**
     * data : {"uid":16660,"nick_name":"Qing","phone":"15974125876","avatar":"http://wx.qlogo.cn/mmopen/vi_32/dcHgp1ibhplWcBmc6nibicQb0sHSVbpibB7RBSUDsk9aZ3licmZlSN5sdDU1olqTdETtNiaWILD0sicDqA6P9uO43ia1DA/0"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * uid : 16660
         * nick_name : Qing
         * phone : 15974125876
         * avatar : http://wx.qlogo.cn/mmopen/vi_32/dcHgp1ibhplWcBmc6nibicQb0sHSVbpibB7RBSUDsk9aZ3licmZlSN5sdDU1olqTdETtNiaWILD0sicDqA6P9uO43ia1DA/0
         */

        private int uid;
        private String nick_name;
        private String phone;
        private String avatar;

        private String outerRate;//转账手续费

        public String getOuterRate() {
            return outerRate;
        }

        public void setOuterRate(String outerRate) {
            this.outerRate = outerRate;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getPhone() {
            return RxDataTool.hideMobilePhone4(phone);
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
