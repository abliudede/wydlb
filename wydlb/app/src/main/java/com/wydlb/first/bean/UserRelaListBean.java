package com.wydlb.first.bean;

import java.util.List;

public class UserRelaListBean {


    /**
     * code : 0
     * data : {"descList":[{"conditionType":0,"helpUrl":"string","taskIntro":"string","taskName":"string"}],"number":0,"userPageList":{"list":[{"awakenCompleteId":0,"awakenStatus":0,"createTime":"2018-10-22T10:36:56.038Z","friendsCompleteId":0,"headImg":"string","id":0,"isAwaken":0,"isInvitationFriends":0,"isLogin":0,"isRead":0,"isSign":0,"mobile":"string","mobileName":"string","nickName":"string","readCompleteId":0,"rinvitationCode":"string","ruid":0,"signCompleteId":0,"userId":0,"userInvitationCode":"string"}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}}
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
         * descList : [{"conditionType":0,"helpUrl":"string","taskIntro":"string","taskName":"string"}]
         * number : 0
         * userPageList : {"list":[{"awakenCompleteId":0,"awakenStatus":0,"createTime":"2018-10-22T10:36:56.038Z","friendsCompleteId":0,"headImg":"string","id":0,"isAwaken":0,"isInvitationFriends":0,"isLogin":0,"isRead":0,"isSign":0,"mobile":"string","mobileName":"string","nickName":"string","readCompleteId":0,"rinvitationCode":"string","ruid":0,"signCompleteId":0,"userId":0,"userInvitationCode":"string"}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
         */

        private int number;
        private int upperLimit;
        private UserPageListBean userPageList;
        private List<DescListBean> descList;

        public int getUpperLimit() {
            return upperLimit;
        }

        public void setUpperLimit(int upperLimit) {
            this.upperLimit = upperLimit;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public UserPageListBean getUserPageList() {
            return userPageList;
        }

        public void setUserPageList(UserPageListBean userPageList) {
            this.userPageList = userPageList;
        }

        public List<DescListBean> getDescList() {
            return descList;
        }

        public void setDescList(List<DescListBean> descList) {
            this.descList = descList;
        }

        public static class UserPageListBean {
            /**
             * list : [{"awakenCompleteId":0,"awakenStatus":0,"createTime":"2018-10-22T10:36:56.038Z","friendsCompleteId":0,"headImg":"string","id":0,"isAwaken":0,"isInvitationFriends":0,"isLogin":0,"isRead":0,"isSign":0,"mobile":"string","mobileName":"string","nickName":"string","readCompleteId":0,"rinvitationCode":"string","ruid":0,"signCompleteId":0,"userId":0,"userInvitationCode":"string"}]
             * pageNum : 0
             * pageSize : 0
             * pages : 0
             * size : 0
             * total : 0
             */

            private int pageNum;
            private int pageSize;
            private int pages;
            private int size;
            private int total;
            private List<ListBean> list;

            public int getPageNum() {
                return pageNum;
            }

            public void setPageNum(int pageNum) {
                this.pageNum = pageNum;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getPages() {
                return pages;
            }

            public void setPages(int pages) {
                this.pages = pages;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * awakenCompleteId : 0
                 * awakenStatus : 0
                 * createTime : 2018-10-22T10:36:56.038Z
                 * friendsCompleteId : 0
                 * headImg : string
                 * id : 0
                 * isAwaken : 0
                 * isInvitationFriends : 0
                 * isLogin : 0
                 * isRead : 0
                 * isSign : 0
                 * mobile : string
                 * mobileName : string
                 * nickName : string
                 * readCompleteId : 0
                 * rinvitationCode : string
                 * ruid : 0
                 * signCompleteId : 0
                 * userId : 0
                 * userInvitationCode : string
                 */

                private int awakenCompleteId;
                private int awakenStatus;
                private String createTime;
                private int friendsCompleteId;
                private String headImg;
                private int id;
                private int isAwaken;
                private int isInvitationFriends;
                private int isLogin;
                private int isRead;
                private int isSign;
                private String mobile;
                private String mobileName;
                private String nickName;
                private int readCompleteId;
                private String rinvitationCode;
                private int ruid;
                private int signCompleteId;
                private int userId;
                private String userInvitationCode;
                private int invitedNumber;
                private int upperLimit;

                public int getUpperLimit() {
                    return upperLimit;
                }

                public void setUpperLimit(int upperLimit) {
                    this.upperLimit = upperLimit;
                }

                public int getInvitedNumber() {
                    return invitedNumber;
                }

                public void setInvitedNumber(int invitedNumber) {
                    this.invitedNumber = invitedNumber;
                }

                public int getAwakenCompleteId() {
                    return awakenCompleteId;
                }

                public void setAwakenCompleteId(int awakenCompleteId) {
                    this.awakenCompleteId = awakenCompleteId;
                }

                public int getAwakenStatus() {
                    return awakenStatus;
                }

                public void setAwakenStatus(int awakenStatus) {
                    this.awakenStatus = awakenStatus;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public int getFriendsCompleteId() {
                    return friendsCompleteId;
                }

                public void setFriendsCompleteId(int friendsCompleteId) {
                    this.friendsCompleteId = friendsCompleteId;
                }

                public String getHeadImg() {
                    return headImg;
                }

                public void setHeadImg(String headImg) {
                    this.headImg = headImg;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getIsAwaken() {
                    return isAwaken;
                }

                public void setIsAwaken(int isAwaken) {
                    this.isAwaken = isAwaken;
                }

                public int getIsInvitationFriends() {
                    return isInvitationFriends;
                }

                public void setIsInvitationFriends(int isInvitationFriends) {
                    this.isInvitationFriends = isInvitationFriends;
                }

                public int getIsLogin() {
                    return isLogin;
                }

                public void setIsLogin(int isLogin) {
                    this.isLogin = isLogin;
                }

                public int getIsRead() {
                    return isRead;
                }

                public void setIsRead(int isRead) {
                    this.isRead = isRead;
                }

                public int getIsSign() {
                    return isSign;
                }

                public void setIsSign(int isSign) {
                    this.isSign = isSign;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public String getMobileName() {
                    return mobileName;
                }

                public void setMobileName(String mobileName) {
                    this.mobileName = mobileName;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public int getReadCompleteId() {
                    return readCompleteId;
                }

                public void setReadCompleteId(int readCompleteId) {
                    this.readCompleteId = readCompleteId;
                }

                public String getRinvitationCode() {
                    return rinvitationCode;
                }

                public void setRinvitationCode(String rinvitationCode) {
                    this.rinvitationCode = rinvitationCode;
                }

                public int getRuid() {
                    return ruid;
                }

                public void setRuid(int ruid) {
                    this.ruid = ruid;
                }

                public int getSignCompleteId() {
                    return signCompleteId;
                }

                public void setSignCompleteId(int signCompleteId) {
                    this.signCompleteId = signCompleteId;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }

                public String getUserInvitationCode() {
                    return userInvitationCode;
                }

                public void setUserInvitationCode(String userInvitationCode) {
                    this.userInvitationCode = userInvitationCode;
                }
            }
        }

        public static class DescListBean {
            /**
             * conditionType : 0
             * helpUrl : string
             * taskIntro : string
             * taskName : string
             */

            private int conditionType;
            private String helpUrl;
            private String taskIntro;
            private String taskName;

            public int getConditionType() {
                return conditionType;
            }

            public void setConditionType(int conditionType) {
                this.conditionType = conditionType;
            }

            public String getHelpUrl() {
                return helpUrl;
            }

            public void setHelpUrl(String helpUrl) {
                this.helpUrl = helpUrl;
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
        }
    }
}
