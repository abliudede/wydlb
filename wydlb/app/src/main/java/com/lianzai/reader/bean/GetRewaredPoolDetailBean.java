package com.lianzai.reader.bean;

import java.util.List;

public class GetRewaredPoolDetailBean {


    /**
     * code : 0
     * data : {"countUsers":0,"readPool":0,"sumAmt":0,"userDetailResps":[{"avatar":"string","nickName":"string","payedTime":"2018-07-24T07:19:42.653Z","transAmt":0,"userId":0}],"userRank":0,"userSumAmt":0}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * countUsers : 0
         * readPool : 0
         * sumAmt : 0
         * userDetailResps : [{"avatar":"string","nickName":"string","payedTime":"2018-07-24T07:19:42.653Z","transAmt":0,"userId":0}]
         * userRank : 0
         * userSumAmt : 0
         */

        private int countUsers;
        private int readPool;
        private Double sumAmt;
        private int userRank;
        private Double userSumAmt;
        private List<String> heads;
        private List<UserDetailRespsBean> userDetailResps;

        public List<String> getHeads() {
            return heads;
        }

        public void setHeads(List<String> heads) {
            this.heads = heads;
        }

        public int getCountUsers() {
            return countUsers;
        }

        public void setCountUsers(int countUsers) {
            this.countUsers = countUsers;
        }

        public int getReadPool() {
            return readPool;
        }

        public void setReadPool(int readPool) {
            this.readPool = readPool;
        }

        public Double getSumAmt() {
            return sumAmt;
        }

        public void setSumAmt(Double sumAmt) {
            this.sumAmt = sumAmt;
        }

        public int getUserRank() {
            return userRank;
        }

        public void setUserRank(int userRank) {
            this.userRank = userRank;
        }

        public Double getUserSumAmt() {
            return userSumAmt;
        }

        public void setUserSumAmt(Double userSumAmt) {
            this.userSumAmt = userSumAmt;
        }

        public List<UserDetailRespsBean> getUserDetailResps() {
            return userDetailResps;
        }

        public void setUserDetailResps(List<UserDetailRespsBean> userDetailResps) {
            this.userDetailResps = userDetailResps;
        }

        public static class UserDetailRespsBean {
            /**
             * avatar : string
             * nickName : string
             * payedTime : 2018-07-24T07:19:42.653Z
             * transAmt : 0
             * userId : 0
             */

            private String avatar;
            private String nickName;
            private String payedTime;
            private Double transAmt;
            private int userId;

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

            public String getPayedTime() {
                return payedTime;
            }

            public void setPayedTime(String payedTime) {
                this.payedTime = payedTime;
            }

            public Double getTransAmt() {
                return transAmt;
            }

            public void setTransAmt(Double transAmt) {
                this.transAmt = transAmt;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
