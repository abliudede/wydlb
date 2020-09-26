package com.lianzai.reader.bean;

import java.util.List;

public class TeamChatInfoBean {


    /**
     * code : 0
     * data : {"nickName":"string","noticeInfo":{"createTime":"2018-12-08T06:05:17.717Z","head":"string","notice":"string","username":"string"},"platformId":0,"platformName":"string","roleType":0,"teamName":"string","userList":[{"head":"string","nickName":"string","roleType":0}]}
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
         * nickName : string
         * noticeInfo : {"createTime":"2018-12-08T06:05:17.717Z","head":"string","notice":"string","username":"string"}
         * platformId : 0
         * platformName : string
         * roleType : 0
         * teamName : string
         * userList : [{"head":"string","nickName":"string","roleType":0}]
         */

        private String nickName;
        private NoticeInfoBean noticeInfo;
        private int platformId;
        private String platformName;
        private int roleType;
        private String teamName;
        private List<UserListBean> userList;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public NoticeInfoBean getNoticeInfo() {
            return noticeInfo;
        }

        public void setNoticeInfo(NoticeInfoBean noticeInfo) {
            this.noticeInfo = noticeInfo;
        }

        public int getPlatformId() {
            return platformId;
        }

        public void setPlatformId(int platformId) {
            this.platformId = platformId;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public List<UserListBean> getUserList() {
            return userList;
        }

        public void setUserList(List<UserListBean> userList) {
            this.userList = userList;
        }

        public static class NoticeInfoBean {
            /**
             * createTime : 2018-12-08T06:05:17.717Z
             * head : string
             * notice : string
             * username : string
             */

            private String createTime;
            private String head;
            private String notice;
            private String username;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }

        public static class UserListBean {
            /**
             * head : string
             * nickName : string
             * roleType : 0
             */

            private String head;
            private int uid;
            private String nickName;
            private int roleType;
            private int inBanned;

            public int getInBanned() {
                return inBanned;
            }

            public void setInBanned(int inBanned) {
                this.inBanned = inBanned;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
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

            public int getRoleType() {
                return roleType;
            }

            public void setRoleType(int roleType) {
                this.roleType = roleType;
            }
        }
    }
}
