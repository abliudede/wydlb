package com.lianzai.reader.bean;

import java.util.List;

public class ReportDetailBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":1,"list":[{"userId":602924,"nickName":"用户id：602924","avatar":"https://static.lianzai.com/avatar/android_1554793427247.jpg","gender":0,"createTime":1564802069000}],"pageNum":1,"pageSize":100,"pages":1,"size":1}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 1
         * list : [{"userId":602924,"nickName":"用户id：602924","avatar":"https://static.lianzai.com/avatar/android_1554793427247.jpg","gender":0,"createTime":1564802069000}]
         * pageNum : 1
         * pageSize : 100
         * pages : 1
         * size : 1
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * userId : 602924
             * nickName : 用户id：602924
             * avatar : https://static.lianzai.com/avatar/android_1554793427247.jpg
             * gender : 0
             * createTime : 1564802069000
             */

            private int userId;
            private String nickName;
            private String avatar;
            private int gender;
            private long createTime;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
