package com.lianzai.reader.bean;

import java.util.List;

public class CopyRightBean {


    /**
     * code : 0
     * data : {"allRound":0,"bigCover":"string","bookId":0,"categoryName":"string","collectCount":0,"crowdCount":0,"crowdHeadList":["string"],"crowdPoint":0,"crowdRead":0,"crowdTime":"string","currentRound":0,"dealCount":0,"dealHeadList":["string"],"dynamicCount":0,"extraAward":0,"extraType":0,"iconUrl":"string","id":0,"introduce":"string","introduceUrl":"string","isInTeam":false,"isOrder":true,"orderCount":0,"orderHeadList":["string"],"outputRate":"string","penName":"string","platformCover":"string","platformIntroduce":"string","platformName":"string","pointName":"string","remainBookPointCount":0,"remainSecond":0,"skipUrl":"string","status":0,"teamId":0,"todayCrowdReadCount":"string","todayTicketCount":0,"wordCount":0,"yesterdayPoint":0}
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
         * allRound : 0
         * bigCover : string
         * bookId : 0
         * categoryName : string
         * collectCount : 0
         * crowdCount : 0
         * crowdHeadList : ["string"]
         * crowdPoint : 0
         * crowdRead : 0
         * crowdTime : string
         * currentRound : 0
         * dealCount : 0
         * dealHeadList : ["string"]
         * dynamicCount : 0
         * extraAward : 0
         * extraType : 0
         * iconUrl : string
         * id : 0
         * introduce : string
         * introduceUrl : string
         * isInTeam : false
         * isOrder : true
         * orderCount : 0
         * orderHeadList : ["string"]
         * outputRate : string
         * penName : string
         * platformCover : string
         * platformIntroduce : string
         * platformName : string
         * pointName : string
         * remainBookPointCount : 0
         * remainSecond : 0
         * skipUrl : string
         * status : 0
         * teamId : 0
         * todayCrowdReadCount : string
         * todayTicketCount : 0
         * wordCount : 0
         * yesterdayPoint : 0
         */

        private int allRound;
        private String bigCover;
        private int bookId;
        private String categoryName;
        private int collectCount;
        private int crowdCount;
        private int crowdPoint;
        private int crowdRead;
        private String crowdTime;
        private int currentRound;
        private int dealCount;
        private int dynamicCount;
        private int extraAward;
        private int extraType;
        private String iconUrl;
        private int id;
        private String introduce;
        private String introduceUrl;
        private boolean isInTeam;
        private boolean isOrder;
        private int orderCount;
        private String outputRate;
        private String penName;
        private String platformCover;
        private String platformIntroduce;
        private String platformName;
        private String pointName;
        private int remainBookPointCount;
        private int remainSecond;
        private String skipUrl;
        private int status;
        private int teamId;
        private String todayCrowdReadCount;
        private int todayTicketCount;
        private int wordCount;
        private int yesterdayPoint;
        private List<String> crowdHeadList;
        private List<String> dealHeadList;
        private List<String> orderHeadList;

        public int getAllRound() {
            return allRound;
        }

        public void setAllRound(int allRound) {
            this.allRound = allRound;
        }

        public String getBigCover() {
            return bigCover;
        }

        public void setBigCover(String bigCover) {
            this.bigCover = bigCover;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public int getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(int collectCount) {
            this.collectCount = collectCount;
        }

        public int getCrowdCount() {
            return crowdCount;
        }

        public void setCrowdCount(int crowdCount) {
            this.crowdCount = crowdCount;
        }

        public int getCrowdPoint() {
            return crowdPoint;
        }

        public void setCrowdPoint(int crowdPoint) {
            this.crowdPoint = crowdPoint;
        }

        public int getCrowdRead() {
            return crowdRead;
        }

        public void setCrowdRead(int crowdRead) {
            this.crowdRead = crowdRead;
        }

        public String getCrowdTime() {
            return crowdTime;
        }

        public void setCrowdTime(String crowdTime) {
            this.crowdTime = crowdTime;
        }

        public int getCurrentRound() {
            return currentRound;
        }

        public void setCurrentRound(int currentRound) {
            this.currentRound = currentRound;
        }

        public int getDealCount() {
            return dealCount;
        }

        public void setDealCount(int dealCount) {
            this.dealCount = dealCount;
        }

        public int getDynamicCount() {
            return dynamicCount;
        }

        public void setDynamicCount(int dynamicCount) {
            this.dynamicCount = dynamicCount;
        }

        public int getExtraAward() {
            return extraAward;
        }

        public void setExtraAward(int extraAward) {
            this.extraAward = extraAward;
        }

        public int getExtraType() {
            return extraType;
        }

        public void setExtraType(int extraType) {
            this.extraType = extraType;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getIntroduceUrl() {
            return introduceUrl;
        }

        public void setIntroduceUrl(String introduceUrl) {
            this.introduceUrl = introduceUrl;
        }

        public boolean isIsInTeam() {
            return isInTeam;
        }

        public void setIsInTeam(boolean isInTeam) {
            this.isInTeam = isInTeam;
        }

        public boolean isIsOrder() {
            return isOrder;
        }

        public void setIsOrder(boolean isOrder) {
            this.isOrder = isOrder;
        }

        public int getOrderCount() {
            return orderCount;
        }

        public void setOrderCount(int orderCount) {
            this.orderCount = orderCount;
        }

        public String getOutputRate() {
            return outputRate;
        }

        public void setOutputRate(String outputRate) {
            this.outputRate = outputRate;
        }

        public String getPenName() {
            return penName;
        }

        public void setPenName(String penName) {
            this.penName = penName;
        }

        public String getPlatformCover() {
            return platformCover;
        }

        public void setPlatformCover(String platformCover) {
            this.platformCover = platformCover;
        }

        public String getPlatformIntroduce() {
            return platformIntroduce;
        }

        public void setPlatformIntroduce(String platformIntroduce) {
            this.platformIntroduce = platformIntroduce;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public int getRemainBookPointCount() {
            return remainBookPointCount;
        }

        public void setRemainBookPointCount(int remainBookPointCount) {
            this.remainBookPointCount = remainBookPointCount;
        }

        public int getRemainSecond() {
            return remainSecond;
        }

        public void setRemainSecond(int remainSecond) {
            this.remainSecond = remainSecond;
        }

        public String getSkipUrl() {
            return skipUrl;
        }

        public void setSkipUrl(String skipUrl) {
            this.skipUrl = skipUrl;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTeamId() {
            return teamId;
        }

        public void setTeamId(int teamId) {
            this.teamId = teamId;
        }

        public String getTodayCrowdReadCount() {
            return todayCrowdReadCount;
        }

        public void setTodayCrowdReadCount(String todayCrowdReadCount) {
            this.todayCrowdReadCount = todayCrowdReadCount;
        }

        public int getTodayTicketCount() {
            return todayTicketCount;
        }

        public void setTodayTicketCount(int todayTicketCount) {
            this.todayTicketCount = todayTicketCount;
        }

        public int getWordCount() {
            return wordCount;
        }

        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        public int getYesterdayPoint() {
            return yesterdayPoint;
        }

        public void setYesterdayPoint(int yesterdayPoint) {
            this.yesterdayPoint = yesterdayPoint;
        }

        public List<String> getCrowdHeadList() {
            return crowdHeadList;
        }

        public void setCrowdHeadList(List<String> crowdHeadList) {
            this.crowdHeadList = crowdHeadList;
        }

        public List<String> getDealHeadList() {
            return dealHeadList;
        }

        public void setDealHeadList(List<String> dealHeadList) {
            this.dealHeadList = dealHeadList;
        }

        public List<String> getOrderHeadList() {
            return orderHeadList;
        }

        public void setOrderHeadList(List<String> orderHeadList) {
            this.orderHeadList = orderHeadList;
        }
    }
}
