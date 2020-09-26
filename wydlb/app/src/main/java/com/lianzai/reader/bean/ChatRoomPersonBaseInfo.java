package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by xiajing on 2018/5/7.
 */

public class ChatRoomPersonBaseInfo {

    /**
     * code : 0
     * data : {"member":[{"headPic":"string","id":0,"joinTime":"2018-05-08T08:27:21.252Z","nickName":"string","role":0,"userId":0}],"myself":{"headPic":"string","id":0,"joinTime":"2018-05-08T08:27:21.253Z","nickName":"string","role":0,"userId":0}}
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
         * member : [{"headPic":"string","id":0,"joinTime":"2018-05-08T08:27:21.252Z","nickName":"string","role":0,"userId":0}]
         * myself : {"headPic":"string","id":0,"joinTime":"2018-05-08T08:27:21.253Z","nickName":"string","role":0,"userId":0}
         */

        private MyselfBean myself;
        private List<MemberBean> member;

        public MyselfBean getMyself() {
            return myself;
        }

        public void setMyself(MyselfBean myself) {
            this.myself = myself;
        }

        public List<MemberBean> getMember() {
            return member;
        }

        public void setMember(List<MemberBean> member) {
            this.member = member;
        }

        public static class MyselfBean {
            /**
             * headPic : string
             * id : 0
             * joinTime : 2018-05-08T08:27:21.253Z
             * nickName : string
             * role : 0
             * userId : 0
             */

            private String headPic;
            private int id;
            private String joinTime;
            private String nickName;
            private int role;
            private int userId;

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getJoinTime() {
                return joinTime;
            }

            public void setJoinTime(String joinTime) {
                this.joinTime = joinTime;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }

        public static class MemberBean {
            /**
             * headPic : string
             * id : 0
             * joinTime : 2018-05-08T08:27:21.252Z
             * nickName : string
             * role : 0
             * userId : 0
             */

            private String headPic;
            private int id;
            private String joinTime;
            private String nickName;
            private int role;
            private int userId;
            private int isBan;

            public String getHeadPic() {
                return headPic;
            }

            public void setHeadPic(String headPic) {
                this.headPic = headPic;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getJoinTime() {
                return joinTime;
            }

            public void setJoinTime(String joinTime) {
                this.joinTime = joinTime;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getIsBan() {
                return isBan;
            }

            public void setIsBan(int isBan) {
                this.isBan = isBan;
            }
        }
    }
}
