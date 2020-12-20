package com.wydlb.first.bean;

import java.util.List;

public class AdministratorListBean {


    /**
     * code : 0
     * data : {"list":[{"avatar":"string","bannedCount":0,"commentAndReplyCount":0,"deleteCommentAndReplyCount":0,"deletePostCount":0,"gender":0,"nickName":"string","readTime":0,"sendPostCount":0,"userId":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
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
         * list : [{"avatar":"string","bannedCount":0,"commentAndReplyCount":0,"deleteCommentAndReplyCount":0,"deletePostCount":0,"gender":0,"nickName":"string","readTime":0,"sendPostCount":0,"userId":0}]
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
             * avatar : string
             * bannedCount : 0
             * commentAndReplyCount : 0
             * deleteCommentAndReplyCount : 0
             * deletePostCount : 0
             * gender : 0
             * nickName : string
             * readTime : 0
             * sendPostCount : 0
             * userId : 0
             */

            private String avatar;
            private int bannedCount;
            private int commentAndReplyCount;
            private int deleteCommentAndReplyCount;
            private int deletePostCount;
            private int gender;
            private String nickName;
            private int readTime;
            private int sendPostCount;
            private int userId;
            private String createTime;
            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getBannedCount() {
                return bannedCount;
            }

            public void setBannedCount(int bannedCount) {
                this.bannedCount = bannedCount;
            }

            public int getCommentAndReplyCount() {
                return commentAndReplyCount;
            }

            public void setCommentAndReplyCount(int commentAndReplyCount) {
                this.commentAndReplyCount = commentAndReplyCount;
            }

            public int getDeleteCommentAndReplyCount() {
                return deleteCommentAndReplyCount;
            }

            public void setDeleteCommentAndReplyCount(int deleteCommentAndReplyCount) {
                this.deleteCommentAndReplyCount = deleteCommentAndReplyCount;
            }

            public int getDeletePostCount() {
                return deletePostCount;
            }

            public void setDeletePostCount(int deletePostCount) {
                this.deletePostCount = deletePostCount;
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

            public int getSendPostCount() {
                return sendPostCount;
            }

            public void setSendPostCount(int sendPostCount) {
                this.sendPostCount = sendPostCount;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }
        }
    }
}
