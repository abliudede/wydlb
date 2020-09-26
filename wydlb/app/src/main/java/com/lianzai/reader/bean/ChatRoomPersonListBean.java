package com.lianzai.reader.bean;

import java.util.List;

public class ChatRoomPersonListBean {


    /**
     * code : 0
     * data : {"candidateList":[{"activeness":0,"head":"string","nickName":"string","uid":0}],"myActiveness":{"activeness":0,"head":"string","isInRank":true,"nickName":"string"},"normalList":[{"activeness":0,"head":"string","nickName":"string","uid":0}]}
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
         * candidateList : [{"activeness":0,"head":"string","nickName":"string","uid":0}]
         * myActiveness : {"activeness":0,"head":"string","isInRank":true,"nickName":"string"}
         * normalList : [{"activeness":0,"head":"string","nickName":"string","uid":0}]
         */

        private MyActivenessBean myActiveness;
        private List<NormalListBean> candidateList;
        private List<NormalListBean> normalList;

        public MyActivenessBean getMyActiveness() {
            return myActiveness;
        }

        public void setMyActiveness(MyActivenessBean myActiveness) {
            this.myActiveness = myActiveness;
        }

        public List<NormalListBean> getCandidateList() {
            return candidateList;
        }

        public void setCandidateList(List<NormalListBean> candidateList) {
            this.candidateList = candidateList;
        }

        public List<NormalListBean> getNormalList() {
            return normalList;
        }

        public void setNormalList(List<NormalListBean> normalList) {
            this.normalList = normalList;
        }

        public static class MyActivenessBean {
            /**
             * activeness : 0
             * head : string
             * isInRank : true
             * nickName : string
             */

            private int activeness;
            private String head;
            private boolean isInRank;
            private String nickName;

            public int getActiveness() {
                return activeness;
            }

            public void setActiveness(int activeness) {
                this.activeness = activeness;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public boolean isIsInRank() {
                return isInRank;
            }

            public void setIsInRank(boolean isInRank) {
                this.isInRank = isInRank;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }
        }

        public static class NormalListBean {
            /**
             * activeness : 0
             * head : string
             * nickName : string
             * uid : 0
             */

            private int activeness;
            private String head;
            private String nickName;
            private int uid;

            public int getActiveness() {
                return activeness;
            }

            public void setActiveness(int activeness) {
                this.activeness = activeness;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }
    }
}
