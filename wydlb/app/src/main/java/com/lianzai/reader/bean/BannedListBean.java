package com.lianzai.reader.bean;

import com.lianzai.reader.base.Constant;

import java.util.List;

public class BannedListBean {


    /**
     * code : 0
     * data : {"list":[{"attentionStatus":0,"avatar":"string","createTime":"2019-07-25T07:52:13.730Z","gender":0,"nickName":"string","readTime":0,"userId":0,"userType":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
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
         * list : [{"attentionStatus":0,"avatar":"string","createTime":"2019-07-25T07:52:13.730Z","gender":0,"nickName":"string","readTime":0,"userId":0,"userType":0}]
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
             * attentionStatus : 0
             * avatar : string
             * createTime : 2019-07-25T07:52:13.730Z
             * gender : 0
             * nickName : string
             * readTime : 0
             * userId : 0
             * userType : 0
             */

            private int attentionStatus;
            private String avatar;
            private String createTime;
            private int gender;
            private String nickName;
            private int readTime;
            private int userId;
            private int userType;


            public int getAttentionStatus() {
                return attentionStatus;
            }

            public void setAttentionStatus(int attentionStatus) {
                this.attentionStatus = attentionStatus;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getReadTime() {
                return readTime;
            }

            public void setReadTime(int readTime) {
                this.readTime = readTime;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserType() {
                if(userType == 0){
                    return Constant.Role.FANS_ROLE;
                }else {
                    return userType;
                }
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }
        }
    }
}
