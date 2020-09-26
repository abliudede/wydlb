package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2018/2/26.
 */

public class WxLoginResponse extends BaseBean implements Serializable{


    /**
     * data : {"uid":null,"mobile":null,"status":null,"token":null,"avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/ItIfwLrPrqYdWvLfPDuNQsteaIVT56ZLiaGuMlHEvEzoSHho7nRoDXFBbQo6hCWP75TSG9Wyp5e4lJMIovN9tyQ/132","nickName":"hh","unionid":"oSa9ut7-JcD21Quin16S-3Z6RQL4","thirdType":1}
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
         * uid : null
         * mobile : null
         * status : null
         * token : null
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/ItIfwLrPrqYdWvLfPDuNQsteaIVT56ZLiaGuMlHEvEzoSHho7nRoDXFBbQo6hCWP75TSG9Wyp5e4lJMIovN9tyQ/132
         * nickName : hh
         * unionid : oSa9ut7-JcD21Quin16S-3Z6RQL4
         * thirdType : 1
         */

        private String uid;
        private String mobile;
        private String status;
        private String token;
        private String avatar;
        private String nickName;
        private String unionid;
        private int thirdType;

        private String imAccount;
        private String imToken;

        public String getImAccount() {
            return imAccount;
        }

        public void setImAccount(String imAccount) {
            this.imAccount = imAccount;
        }

        public String getImToken() {
            return imToken;
        }

        public void setImToken(String imToken) {
            this.imToken = imToken;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public int getThirdType() {
            return thirdType;
        }

        public void setThirdType(int thirdType) {
            this.thirdType = thirdType;
        }
    }
}
