package com.lianzai.reader.bean;

import java.io.Serializable;

/**
 * Created by lrz on 2017/10/21.
 */

public class AccountTokenBean extends  BaseBean {


    /**
     * data : {"uid":16660,"mobile":"15974125876","status":1,"token":"4B26949C78324ECE9F8E2940ED4AE04E","avatar":"https://static.lianzai.com//avatar/head_pic_1523339580647.png","nickName":"hh","password":null,"role":0,"authStatus":2,"imAccount":"7724dfd6499c4edaa7efdadd734a938d","imToken":"b5ed52a5bdce4f8fb15497a7a3d8f80c"}
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
         * mobile : 15974125876
         * status : 1
         * token : 4B26949C78324ECE9F8E2940ED4AE04E
         * avatar : https://static.lianzai.com//avatar/head_pic_1523339580647.png
         * nickName : hh
         * password : null
         * role : 0
         * authStatus : 2
         * imAccount : 7724dfd6499c4edaa7efdadd734a938d
         * imToken : b5ed52a5bdce4f8fb15497a7a3d8f80c
         */

        private int uid;
        private String mobile;
        private int status;
        private String token;
        private String avatar;
        private String nickName;
        private Object password;
        private int role;
        private int authStatus;
        private String imAccount;
        private String imToken;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getToken() {
            return token;//"94CFD396D17148DBA940F88552124740"
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

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(int authStatus) {
            this.authStatus = authStatus;
        }

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
    }
}
