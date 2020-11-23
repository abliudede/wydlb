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
         * id : 2
         * referralCode : 0002
         * account : ceshi
         * nickname : CESHI
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOjIsImV4cCI6MTU5OTQxNzg0NTU3OX0.Ib XQ-Db-A3kIOocdSJJ33rFpB-CcwnjiYxeoaDSFTNo
         */

        private int id;
        private String referralCode;
        private String account;
        private String nickname;
        private String token;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
