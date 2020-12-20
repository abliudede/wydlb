package com.wydlb.first.bean;

import java.util.List;

/**
 * Created by lrz on 2018/4/26.
 */

public class UserLockListResponse {


    /**
     * code : 0
     * msg : success
     * data : {"total":10,"list":[{"id":22,"userId":16660,"lockedAmt":20110,"initAmt":20110,"unLockAmt":0,"createTime":1524642834000,"balanceType":3,"exchangeNo":"fb301a02380909b558b301053c6f5d87"},{"id":23,"userId":16660,"lockedAmt":20110,"initAmt":20110,"unLockAmt":0,"createTime":1524643649000,"balanceType":3,"exchangeNo":"e74965b29ef953655796b11e12f4b18b"},{"id":24,"userId":16660,"lockedAmt":9159780,"initAmt":9159780,"unLockAmt":0,"createTime":1524643757000,"balanceType":3,"exchangeNo":"f041325154eb8f69310019ae16f32b78"},{"id":25,"userId":16660,"lockedAmt":1,"initAmt":1,"unLockAmt":0,"createTime":1524650646000,"balanceType":3,"exchangeNo":"3c36b74aebeee397aef00551ecae90cb"},{"id":26,"userId":16660,"lockedAmt":25,"initAmt":25,"unLockAmt":0,"createTime":1524650701000,"balanceType":3,"exchangeNo":"b43a9d1473902f27cd5487e02fda8f6a"},{"id":27,"userId":16660,"lockedAmt":2,"initAmt":2,"unLockAmt":0,"createTime":1524652195000,"balanceType":3,"exchangeNo":"034e115de36d2fb7672402ff4cb8b7c1"},{"id":28,"userId":16660,"lockedAmt":18,"initAmt":18,"unLockAmt":0,"createTime":1524653987000,"balanceType":3,"exchangeNo":"c81ff72a6261aba95ecf8213802f1da0"},{"id":29,"userId":16660,"lockedAmt":20,"initAmt":20,"unLockAmt":0,"createTime":1524655038000,"balanceType":3,"exchangeNo":"a4370c262d198e6914678b92ae050bfa"},{"id":37,"userId":16660,"lockedAmt":100,"initAmt":100,"unLockAmt":0,"createTime":1524809917000,"balanceType":3,"exchangeNo":"67F068367D09193FFC303A921D88AAB6"},{"id":39,"userId":16660,"lockedAmt":5,"initAmt":5,"unLockAmt":0,"createTime":1524815567000,"balanceType":3,"exchangeNo":"CF6078D22D0CC1202594B3811B4E8D75"}],"pageNum":1,"pageSize":10,"pages":1,"size":10}
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
         * total : 10
         * list : [{"id":22,"userId":16660,"lockedAmt":20110,"initAmt":20110,"unLockAmt":0,"createTime":1524642834000,"balanceType":3,"exchangeNo":"fb301a02380909b558b301053c6f5d87"},{"id":23,"userId":16660,"lockedAmt":20110,"initAmt":20110,"unLockAmt":0,"createTime":1524643649000,"balanceType":3,"exchangeNo":"e74965b29ef953655796b11e12f4b18b"},{"id":24,"userId":16660,"lockedAmt":9159780,"initAmt":9159780,"unLockAmt":0,"createTime":1524643757000,"balanceType":3,"exchangeNo":"f041325154eb8f69310019ae16f32b78"},{"id":25,"userId":16660,"lockedAmt":1,"initAmt":1,"unLockAmt":0,"createTime":1524650646000,"balanceType":3,"exchangeNo":"3c36b74aebeee397aef00551ecae90cb"},{"id":26,"userId":16660,"lockedAmt":25,"initAmt":25,"unLockAmt":0,"createTime":1524650701000,"balanceType":3,"exchangeNo":"b43a9d1473902f27cd5487e02fda8f6a"},{"id":27,"userId":16660,"lockedAmt":2,"initAmt":2,"unLockAmt":0,"createTime":1524652195000,"balanceType":3,"exchangeNo":"034e115de36d2fb7672402ff4cb8b7c1"},{"id":28,"userId":16660,"lockedAmt":18,"initAmt":18,"unLockAmt":0,"createTime":1524653987000,"balanceType":3,"exchangeNo":"c81ff72a6261aba95ecf8213802f1da0"},{"id":29,"userId":16660,"lockedAmt":20,"initAmt":20,"unLockAmt":0,"createTime":1524655038000,"balanceType":3,"exchangeNo":"a4370c262d198e6914678b92ae050bfa"},{"id":37,"userId":16660,"lockedAmt":100,"initAmt":100,"unLockAmt":0,"createTime":1524809917000,"balanceType":3,"exchangeNo":"67F068367D09193FFC303A921D88AAB6"},{"id":39,"userId":16660,"lockedAmt":5,"initAmt":5,"unLockAmt":0,"createTime":1524815567000,"balanceType":3,"exchangeNo":"CF6078D22D0CC1202594B3811B4E8D75"}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 10
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
             * id : 22
             * userId : 16660
             * lockedAmt : 20110
             * initAmt : 20110
             * unLockAmt : 0
             * createTime : 1524642834000
             * balanceType : 3
             * exchangeNo : fb301a02380909b558b301053c6f5d87
             */

            private int id;
            private int userId;
            private Double lockedAmt;
            private Double initAmt;
            private Double unLockAmt;
            private long createTime;
            private int balanceType;
            private String exchangeNo;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }


            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getBalanceType() {
                return balanceType;
            }

            public Double getLockedAmt() {
                return lockedAmt;
            }

            public void setLockedAmt(Double lockedAmt) {
                this.lockedAmt = lockedAmt;
            }

            public Double getInitAmt() {
                return initAmt;
            }

            public void setInitAmt(Double initAmt) {
                this.initAmt = initAmt;
            }

            public Double getUnLockAmt() {
                return unLockAmt;
            }

            public void setUnLockAmt(Double unLockAmt) {
                this.unLockAmt = unLockAmt;
            }

            public void setBalanceType(int balanceType) {
                this.balanceType = balanceType;
            }

            public String getExchangeNo() {
                return exchangeNo;
            }

            public void setExchangeNo(String exchangeNo) {
                this.exchangeNo = exchangeNo;
            }
        }
    }
}
