package com.lianzai.reader.bean;

import java.util.List;

public class LuckInfoBean {


    /**
     * code : 0
     * data : {"balance":0,"gameInfo":{"joinInCount":0,"needThreshold":0,"partJoinUserInfo":["string"],"pond":0,"publisherHead":"string","publisherNickName":"string","remainSecond":0,"rule":"string"},"isJoin":false,"isOver":false,"isPrize":false,"prizeInfo":{"prize":0,"prizeUserHead":"string","prizeUserNickName":"string"}}
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
         * balance : 0
         * gameInfo : {"joinInCount":0,"needThreshold":0,"partJoinUserInfo":["string"],"pond":0,"publisherHead":"string","publisherNickName":"string","remainSecond":0,"rule":"string"}
         * isJoin : false
         * isOver : false
         * isPrize : false
         * prizeInfo : {"prize":0,"prizeUserHead":"string","prizeUserNickName":"string"}
         */

        private double balance;
        private GameInfoBean gameInfo;
        private boolean isJoin;
        private boolean isOver;
        private boolean isPrize;
        private PrizeInfoBean prizeInfo;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public GameInfoBean getGameInfo() {
            return gameInfo;
        }

        public void setGameInfo(GameInfoBean gameInfo) {
            this.gameInfo = gameInfo;
        }

        public boolean isIsJoin() {
            return isJoin;
        }

        public void setIsJoin(boolean isJoin) {
            this.isJoin = isJoin;
        }

        public boolean isIsOver() {
            return isOver;
        }

        public void setIsOver(boolean isOver) {
            this.isOver = isOver;
        }

        public boolean isIsPrize() {
            return isPrize;
        }

        public void setIsPrize(boolean isPrize) {
            this.isPrize = isPrize;
        }

        public PrizeInfoBean getPrizeInfo() {
            return prizeInfo;
        }

        public void setPrizeInfo(PrizeInfoBean prizeInfo) {
            this.prizeInfo = prizeInfo;
        }

        public static class GameInfoBean {
            /**
             * joinInCount : 0
             * needThreshold : 0
             * partJoinUserInfo : ["string"]
             * pond : 0
             * publisherHead : string
             * publisherNickName : string
             * remainSecond : 0
             * rule : string
             */

            private int joinInCount;
            private int needThreshold;
            private int pond;
            private String publisherHead;
            private String publisherNickName;
            private int remainSecond;
            private String rule;
            private List<String> partJoinUserInfo;
            private String title;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getJoinInCount() {
                return joinInCount;
            }

            public void setJoinInCount(int joinInCount) {
                this.joinInCount = joinInCount;
            }

            public int getNeedThreshold() {
                return needThreshold;
            }

            public void setNeedThreshold(int needThreshold) {
                this.needThreshold = needThreshold;
            }

            public int getPond() {
                return pond;
            }

            public void setPond(int pond) {
                this.pond = pond;
            }

            public String getPublisherHead() {
                return publisherHead;
            }

            public void setPublisherHead(String publisherHead) {
                this.publisherHead = publisherHead;
            }

            public String getPublisherNickName() {
                return publisherNickName;
            }

            public void setPublisherNickName(String publisherNickName) {
                this.publisherNickName = publisherNickName;
            }

            public int getRemainSecond() {
                return remainSecond;
            }

            public void setRemainSecond(int remainSecond) {
                this.remainSecond = remainSecond;
            }

            public String getRule() {
                return rule;
            }

            public void setRule(String rule) {
                this.rule = rule;
            }

            public List<String> getPartJoinUserInfo() {
                return partJoinUserInfo;
            }

            public void setPartJoinUserInfo(List<String> partJoinUserInfo) {
                this.partJoinUserInfo = partJoinUserInfo;
            }
        }

        public static class PrizeInfoBean {
            /**
             * prize : 0
             * prizeUserHead : string
             * prizeUserNickName : string
             */

            private int prize;
            private String prizeUserHead;
            private String prizeUserNickName;

            public int getPrize() {
                return prize;
            }

            public void setPrize(int prize) {
                this.prize = prize;
            }

            public String getPrizeUserHead() {
                return prizeUserHead;
            }

            public void setPrizeUserHead(String prizeUserHead) {
                this.prizeUserHead = prizeUserHead;
            }

            public String getPrizeUserNickName() {
                return prizeUserNickName;
            }

            public void setPrizeUserNickName(String prizeUserNickName) {
                this.prizeUserNickName = prizeUserNickName;
            }
        }
    }
}
