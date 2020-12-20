package com.wydlb.first.bean;

import java.util.List;

public class GetGoldDrillBean {


    /**
     * code : 0
     * data : {"details":{"list":[{"createTime":"2018-10-23T01:35:18.068Z","power":0,"sourceType":"string","value":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0},"totalPower":0}
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
         * details : {"list":[{"createTime":"2018-10-23T01:35:18.068Z","power":0,"sourceType":"string","value":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
         * totalPower : 0
         */

        private DetailsBean details;
        private double totalPower;

        public DetailsBean getDetails() {
            return details;
        }

        public void setDetails(DetailsBean details) {
            this.details = details;
        }

        public double getTotalPower() {
            return totalPower;
        }

        public void setTotalPower(double totalPower) {
            this.totalPower = totalPower;
        }

        public static class DetailsBean {
            /**
             * list : [{"createTime":"2018-10-23T01:35:18.068Z","power":0,"sourceType":"string","value":0}]
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
                 * createTime : 2018-10-23T01:35:18.068Z
                 * power : 0
                 * sourceType : string
                 * value : 0
                 */

                private String createTime;
                private Double power;
                private String sourceType;
                private double value;
                private String header;

                public String getHeader() {
                    return header;
                }

                public void setHeader(String header) {
                    this.header = header;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public Double getPower() {
                    return power;
                }

                public void setPower(Double power) {
                    this.power = power;
                }

                public String getSourceType() {
                    return sourceType;
                }

                public void setSourceType(String sourceType) {
                    this.sourceType = sourceType;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }
        }
    }
}
