package com.wydlb.first.bean;

import java.util.List;

public class AutoDailyTicketStreamBean {

    /**
     * code : 0
     * data : {"list":[{"amt":0,"bookId":0,"createTime":"2019-07-30T02:08:22.877Z","exRemark":"string","status":0,"userId":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
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
         * list : [{"amt":0,"bookId":0,"createTime":"2019-07-30T02:08:22.877Z","exRemark":"string","status":0,"userId":0}]
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
             * amt : 0
             * bookId : 0
             * createTime : 2019-07-30T02:08:22.877Z
             * exRemark : string
             * status : 0
             * userId : 0
             */

            private int amt;
            private int bookId;
            private String createTime;
            private String exRemark;
            private int status;
            private int userId;

            public int getAmt() {
                return amt;
            }

            public void setAmt(int amt) {
                this.amt = amt;
            }

            public int getBookId() {
                return bookId;
            }

            public void setBookId(int bookId) {
                this.bookId = bookId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getExRemark() {
                return exRemark;
            }

            public void setExRemark(String exRemark) {
                this.exRemark = exRemark;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
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
