package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2018/4/24.
 */

public class WalletDetailListResponse {


    /**
     * code : 0
     * msg : success
     * data : {"total":6,"list":[{"id":2101,"uid":16660,"amt":400,"remark":"收入备注3","inOutType":0,"createTime":1524549612000,"ticket":"a60d696c005142548c1ec1999b3adcc","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2100,"uid":16660,"amt":2000,"remark":"收入备注2","inOutType":0,"createTime":1524549551000,"ticket":"0360db8be6834f98b445ce18015b3eda","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2098,"uid":16660,"amt":400,"remark":"收入备注3","inOutType":0,"createTime":1524549371000,"ticket":"w60d696c005142548c1ec1999b3adcc","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2095,"uid":16660,"amt":2000,"remark":"收入备注2","inOutType":0,"createTime":1524549311000,"ticket":"7360db8be6834f98b445ce18015b3eda","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2093,"uid":16660,"amt":400,"remark":"收入备注3","inOutType":0,"createTime":1524549189000,"ticket":"e60d696c005142548c1ec1999b3adcc","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2092,"uid":16660,"amt":2000,"remark":"收入备注2","inOutType":0,"createTime":1524542768000,"ticket":"6360db8be6834f98b445ce18015b3eda","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4}],"pageNum":1,"pageSize":10,"pages":1,"size":6}
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
         * total : 6
         * list : [{"id":2101,"uid":16660,"amt":400,"remark":"收入备注3","inOutType":0,"createTime":1524549612000,"ticket":"a60d696c005142548c1ec1999b3adcc","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2100,"uid":16660,"amt":2000,"remark":"收入备注2","inOutType":0,"createTime":1524549551000,"ticket":"0360db8be6834f98b445ce18015b3eda","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2098,"uid":16660,"amt":400,"remark":"收入备注3","inOutType":0,"createTime":1524549371000,"ticket":"w60d696c005142548c1ec1999b3adcc","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2095,"uid":16660,"amt":2000,"remark":"收入备注2","inOutType":0,"createTime":1524549311000,"ticket":"7360db8be6834f98b445ce18015b3eda","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2093,"uid":16660,"amt":400,"remark":"收入备注3","inOutType":0,"createTime":1524549189000,"ticket":"e60d696c005142548c1ec1999b3adcc","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4},{"id":2092,"uid":16660,"amt":2000,"remark":"收入备注2","inOutType":0,"createTime":1524542768000,"ticket":"6360db8be6834f98b445ce18015b3eda","outType":null,"incomeType":5,"outState":null,"inState":0,"balanceType":4}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 6
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
             * id : 2101
             * uid : 16660
             * amt : 400.0
             * remark : 收入备注3
             * inOutType : 0
             * createTime : 1524549612000
             * ticket : a60d696c005142548c1ec1999b3adcc
             * outType : null
             * incomeType : 5
             * outState : null
             * inState : 0
             * balanceType : 4
             */

            private boolean showMonth=false;
            private String showDate;
            private String description;

            public String getShowDate() {
                return showDate;
            }

            public void setShowDate(String showDate) {
                this.showDate = showDate;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            private int id;
            private int uid;
            private Double amt;
            private String remark;
            private int inOutType;
            private long createTime;
            private String ticket;
            private Object outType;
            private int incomeType;
            private Object outState;
            private int inState;
            private int balanceType;
            private String pointName;

            private long nextTime;


            public String getPointName() {
                return pointName;
            }

            public void setPointName(String pointName) {
                this.pointName = pointName;
            }

            public long getNextTime() {
                return nextTime;
            }

            public void setNextTime(long nextTime) {
                this.nextTime = nextTime;
            }

            public boolean isShowMonth() {
                return showMonth;
            }

            public void setShowMonth(boolean showMonth) {
                this.showMonth = showMonth;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public Double getAmt() {
                return amt;
            }

            public void setAmt(Double amt) {
                this.amt = amt;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getInOutType() {
                return inOutType;
            }

            public void setInOutType(int inOutType) {
                this.inOutType = inOutType;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getTicket() {
                return ticket;
            }

            public void setTicket(String ticket) {
                this.ticket = ticket;
            }

            public Object getOutType() {
                return outType;
            }

            public void setOutType(Object outType) {
                this.outType = outType;
            }

            public int getIncomeType() {
                return incomeType;
            }

            public void setIncomeType(int incomeType) {
                this.incomeType = incomeType;
            }

            public Object getOutState() {
                return outState;
            }

            public void setOutState(Object outState) {
                this.outState = outState;
            }

            public int getInState() {
                return inState;
            }

            public void setInState(int inState) {
                this.inState = inState;
            }

            public int getBalanceType() {
                return balanceType;
            }

            public void setBalanceType(int balanceType) {
                this.balanceType = balanceType;
            }
        }
    }
}
