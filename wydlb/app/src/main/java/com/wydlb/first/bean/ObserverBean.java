package com.wydlb.first.bean;

import java.util.List;

public class ObserverBean {


    /**
     * code : 0
     * data : [{"data":{"action":"string","head":"string","nickName":"string","rewardImg":"string","rewardName":"string","rewardValue":"string","time":"2018-11-13T01:11:32.215Z","url":"string"},"newsType":0}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<DataBeanX> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * data : {"action":"string","head":"string","nickName":"string","rewardImg":"string","rewardName":"string","rewardValue":"string","time":"2018-11-13T01:11:32.215Z","url":"string"}
         * newsType : 0
         */

        private DataBean data;
        private int newsType;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public int getNewsType() {
            return newsType;
        }

        public void setNewsType(int newsType) {
            this.newsType = newsType;
        }

        public static class DataBean {
            /**
             * action : string
             * head : string
             * nickName : string
             * rewardImg : string
             * rewardName : string
             * rewardValue : string
             * time : 2018-11-13T01:11:32.215Z
             * url : string
             */

            private String action;
            private String head;
            private String nickName;
            private String rewardImg;
            private String rewardName;
            private String rewardValue;
            private String time;
            private String url;

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
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

            public String getRewardImg() {
                return rewardImg;
            }

            public void setRewardImg(String rewardImg) {
                this.rewardImg = rewardImg;
            }

            public String getRewardName() {
                return rewardName;
            }

            public void setRewardName(String rewardName) {
                this.rewardName = rewardName;
            }

            public String getRewardValue() {
                return rewardValue;
            }

            public void setRewardValue(String rewardValue) {
                this.rewardValue = rewardValue;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
