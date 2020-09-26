package com.lianzai.reader.bean;

import java.util.List;

public class TaskCenterBean {


    /**
     * code : 0
     * data : {"checkTaskList":[{"helpUrl":"string","isShow":0,"rewardAmount":0,"rewardStatus":0,"rewardType":0,"sort":0,"targetUrl":"string","taskImg":"string","taskIntro":"string","taskName":"string","taskType":0,"unfinishedStatusDesc":"string"}],"dailyTaskCount":0,"dailyTaskList":[{"helpUrl":"string","isShow":0,"rewardAmount":0,"rewardStatus":0,"rewardType":0,"sort":0,"targetUrl":"string","taskImg":"string","taskIntro":"string","taskName":"string","taskType":0,"unfinishedStatusDesc":"string"}],"newbieTaskCount":0,"newbieTaskList":[{"helpUrl":"string","isShow":0,"rewardAmount":0,"rewardStatus":0,"rewardType":0,"sort":0,"targetUrl":"string","taskImg":"string","taskIntro":"string","taskName":"string","taskType":0,"unfinishedStatusDesc":"string"}],"totalPower":0}
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
         * checkTaskList : [{"helpUrl":"string","isShow":0,"rewardAmount":0,"rewardStatus":0,"rewardType":0,"sort":0,"targetUrl":"string","taskImg":"string","taskIntro":"string","taskName":"string","taskType":0,"unfinishedStatusDesc":"string"}]
         * dailyTaskCount : 0
         * dailyTaskList : [{"helpUrl":"string","isShow":0,"rewardAmount":0,"rewardStatus":0,"rewardType":0,"sort":0,"targetUrl":"string","taskImg":"string","taskIntro":"string","taskName":"string","taskType":0,"unfinishedStatusDesc":"string"}]
         * newbieTaskCount : 0
         * newbieTaskList : [{"helpUrl":"string","isShow":0,"rewardAmount":0,"rewardStatus":0,"rewardType":0,"sort":0,"targetUrl":"string","taskImg":"string","taskIntro":"string","taskName":"string","taskType":0,"unfinishedStatusDesc":"string"}]
         * totalPower : 0
         */

        private int dailyTaskCount;
        private int dailyTaskAllCount;
        private int dailyTaskFinishCount ;
        private int newbieTaskCount;
        private int newBieAllCount;
        private int newBieFinishCount;
        private double totalPower;
        private double totalDailyTicket;
        private List<CheckTaskListBean> checkTaskList;
        private List<DailyTaskListBean> dailyTaskList;
        private List<DailyTaskListBean> newbieTaskList;

        public int getDailyTaskAllCount() {
            return dailyTaskAllCount;
        }

        public void setDailyTaskAllCount(int dailyTaskAllCount) {
            this.dailyTaskAllCount = dailyTaskAllCount;
        }

        public int getDailyTaskFinishCount() {
            return dailyTaskFinishCount;
        }

        public void setDailyTaskFinishCount(int dailyTaskFinishCount) {
            this.dailyTaskFinishCount = dailyTaskFinishCount;
        }

        public int getNewBieAllCount() {
            return newBieAllCount;
        }

        public void setNewBieAllCount(int newBieAllCount) {
            this.newBieAllCount = newBieAllCount;
        }

        public int getNewBieFinishCount() {
            return newBieFinishCount;
        }

        public void setNewBieFinishCount(int newBieFinishCount) {
            this.newBieFinishCount = newBieFinishCount;
        }

        public double getTotalDailyTicket() {
            return totalDailyTicket;
        }

        public void setTotalDailyTicket(double totalDailyTicket) {
            this.totalDailyTicket = totalDailyTicket;
        }

        public int getDailyTaskCount() {
            return dailyTaskCount;
        }

        public void setDailyTaskCount(int dailyTaskCount) {
            this.dailyTaskCount = dailyTaskCount;
        }

        public int getNewbieTaskCount() {
            return newbieTaskCount;
        }

        public void setNewbieTaskCount(int newbieTaskCount) {
            this.newbieTaskCount = newbieTaskCount;
        }

        public double getTotalPower() {
            return totalPower;
        }

        public void setTotalPower(double totalPower) {
            this.totalPower = totalPower;
        }

        public List<CheckTaskListBean> getCheckTaskList() {
            return checkTaskList;
        }

        public void setCheckTaskList(List<CheckTaskListBean> checkTaskList) {
            this.checkTaskList = checkTaskList;
        }

        public List<DailyTaskListBean> getDailyTaskList() {
            return dailyTaskList;
        }

        public void setDailyTaskList(List<DailyTaskListBean> dailyTaskList) {
            this.dailyTaskList = dailyTaskList;
        }

        public List<DailyTaskListBean> getNewbieTaskList() {
            return newbieTaskList;
        }

        public void setNewbieTaskList(List<DailyTaskListBean> newbieTaskList) {
            this.newbieTaskList = newbieTaskList;
        }

        public static class CheckTaskListBean {
            /**
             * helpUrl : string
             * isShow : 0
             * rewardAmount : 0
             * rewardStatus : 0
             * rewardType : 0
             * sort : 0
             * targetUrl : string
             * taskImg : string
             * taskIntro : string
             * taskName : string
             * taskType : 0
             * unfinishedStatusDesc : string
             */

            private String helpUrl;
            private int isShow;
            private int rewardStatus;
            private int sort;
            private String targetUrl;
            private String taskImg;
            private String taskIntro;
            private String taskName;
            private int taskType;
            private String unfinishedStatusDesc;
            private List<RewardListBean> rewardList;

            public List<RewardListBean> getRewardList() {
                return rewardList;
            }

            public void setRewardList(List<RewardListBean> rewardList) {
                this.rewardList = rewardList;
            }


            public String getHelpUrl() {
                return helpUrl;
            }

            public void setHelpUrl(String helpUrl) {
                this.helpUrl = helpUrl;
            }

            public int getIsShow() {
                return isShow;
            }

            public void setIsShow(int isShow) {
                this.isShow = isShow;
            }

            public int getRewardStatus() {
                return rewardStatus;
            }

            public void setRewardStatus(int rewardStatus) {
                this.rewardStatus = rewardStatus;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getTargetUrl() {
                return targetUrl;
            }

            public void setTargetUrl(String targetUrl) {
                this.targetUrl = targetUrl;
            }

            public String getTaskImg() {
                return taskImg;
            }

            public void setTaskImg(String taskImg) {
                this.taskImg = taskImg;
            }

            public String getTaskIntro() {
                return taskIntro;
            }

            public void setTaskIntro(String taskIntro) {
                this.taskIntro = taskIntro;
            }

            public String getTaskName() {
                return taskName;
            }

            public void setTaskName(String taskName) {
                this.taskName = taskName;
            }

            public int getTaskType() {
                return taskType;
            }

            public void setTaskType(int taskType) {
                this.taskType = taskType;
            }

            public String getUnfinishedStatusDesc() {
                return unfinishedStatusDesc;
            }

            public void setUnfinishedStatusDesc(String unfinishedStatusDesc) {
                this.unfinishedStatusDesc = unfinishedStatusDesc;
            }



        }

        public static class DailyTaskListBean {
            /**
             * helpUrl : string
             * isShow : 0
             * rewardAmount : 0
             * rewardStatus : 0
             * rewardType : 0
             * sort : 0
             * targetUrl : string
             * taskImg : string
             * taskIntro : string
             * taskName : string
             * taskType : 0
             * unfinishedStatusDesc : string
             */

            private String helpUrl;
            private int isShow;
            private int rewardStatus;
            private int sort;
            private String completeId;
            private String targetUrl;
            private String taskImg;
            private String taskIntro;
            private String taskName;
            private int taskType;
            private String unfinishedStatusDesc;
            private List<RewardListBean> rewardList;
            private int zhankai;

            public int getZhankai() {
                return zhankai;
            }

            public void setZhankai(int zhankai) {
                this.zhankai = zhankai;
            }

            public List<RewardListBean> getRewardList() {
                return rewardList;
            }

            public void setRewardList(List<RewardListBean> rewardList) {
                this.rewardList = rewardList;
            }

            public String getCompleteId() {
                return completeId;
            }

            public void setCompleteId(String completeId) {
                this.completeId = completeId;
            }

            public String getHelpUrl() {
                return helpUrl;
            }

            public void setHelpUrl(String helpUrl) {
                this.helpUrl = helpUrl;
            }

            public int getIsShow() {
                return isShow;
            }

            public void setIsShow(int isShow) {
                this.isShow = isShow;
            }

            public int getRewardStatus() {
                return rewardStatus;
            }

            public void setRewardStatus(int rewardStatus) {
                this.rewardStatus = rewardStatus;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getTargetUrl() {
                return targetUrl;
            }

            public void setTargetUrl(String targetUrl) {
                this.targetUrl = targetUrl;
            }

            public String getTaskImg() {
                return taskImg;
            }

            public void setTaskImg(String taskImg) {
                this.taskImg = taskImg;
            }

            public String getTaskIntro() {
                return taskIntro;
            }

            public void setTaskIntro(String taskIntro) {
                this.taskIntro = taskIntro;
            }

            public String getTaskName() {
                return taskName;
            }

            public void setTaskName(String taskName) {
                this.taskName = taskName;
            }

            public int getTaskType() {
                return taskType;
            }

            public void setTaskType(int taskType) {
                this.taskType = taskType;
            }

            public String getUnfinishedStatusDesc() {
                return unfinishedStatusDesc;
            }

            public void setUnfinishedStatusDesc(String unfinishedStatusDesc) {
                this.unfinishedStatusDesc = unfinishedStatusDesc;
            }
        }

        public static class RewardListBean {
            /**
             * rewardType : 20000
             * rewardAmount : 18.0
             */

            private int rewardType;
            private double rewardAmount;

            public int getRewardType() {
                return rewardType;
            }

            public void setRewardType(int rewardType) {
                this.rewardType = rewardType;
            }

            public double getRewardAmount() {
                return rewardAmount;
            }

            public void setRewardAmount(double rewardAmount) {
                this.rewardAmount = rewardAmount;
            }
        }
    }
}
