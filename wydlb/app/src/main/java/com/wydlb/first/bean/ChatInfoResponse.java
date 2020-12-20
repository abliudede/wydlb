package com.wydlb.first.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: ChatInfoResponse
 * Author: lrz
 * Date: 2018/9/17 10:17
 * Description: ${DESCRIPTION}
 */
public class ChatInfoResponse extends  BaseBean {

    /**
     * data : {"roomId":56351844,"roleType":6,"identityInfoResponses":[{"uid":476342,"accid":"91e0503e63c44a7a9267bce663331f15","roleType":4,"nickName":"张三"},{"uid":499300,"accid":"16e1aa0e26614d19b09c975a19e3f82f","roleType":1,"nickName":"连载书友961460"}],"noticeInfo":{"createTime":1536907972000,"notice":"gdff"}}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * roomId : 56351844
         * roleType : 6
         * identityInfoResponses : [{"uid":476342,"accid":"91e0503e63c44a7a9267bce663331f15","roleType":4,"nickName":"张三"},{"uid":499300,"accid":"16e1aa0e26614d19b09c975a19e3f82f","roleType":1,"nickName":"连载书友961460"}]
         * noticeInfo : {"createTime":1536907972000,"notice":"gdff"}
         */

        private int roomId;
        private int roleType;
        private String myNickName;
        private String myHead;
        private String myAccid;
        private NoticeInfoBean noticeInfo;
        private List<IdentityInfoResponsesBean> identityInfoResponses;

        public String getMyHead() {
            return myHead;
        }

        public void setMyHead(String myHead) {
            this.myHead = myHead;
        }

        public String getMyAccid() {
            return myAccid;
        }

        public void setMyAccid(String myAccid) {
            this.myAccid = myAccid;
        }

        public String getMyNickName() {
            return myNickName;
        }

        public void setMyNickName(String myNickName) {
            this.myNickName = myNickName;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        public NoticeInfoBean getNoticeInfo() {
            return noticeInfo;
        }

        public void setNoticeInfo(NoticeInfoBean noticeInfo) {
            this.noticeInfo = noticeInfo;
        }

        public List<IdentityInfoResponsesBean> getIdentityInfoResponses() {
            return identityInfoResponses;
        }

        public void setIdentityInfoResponses(List<IdentityInfoResponsesBean> identityInfoResponses) {
            this.identityInfoResponses = identityInfoResponses;
        }

        public static class NoticeInfoBean {
            /**
             * createTime : 1536907972000
             * notice : gdff
             */

            private long createTime;
            private String notice;
            private String head;
            private String username;

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getNotice() {
                return notice;
            }

            public void setNotice(String notice) {
                this.notice = notice;
            }
        }

        public static class IdentityInfoResponsesBean {
            /**
             * uid : 476342
             * accid : 91e0503e63c44a7a9267bce663331f15
             * roleType : 4
             * nickName : 张三
             */

            private int uid;
            private String accid;
            private int roleType;
            private String nickName;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public int getRoleType() {
                return roleType;
            }

            public void setRoleType(int roleType) {
                this.roleType = roleType;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }
        }
    }
}
