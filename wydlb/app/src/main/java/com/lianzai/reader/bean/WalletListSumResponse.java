package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2018/4/24.
 */

public class WalletListSumResponse extends BaseBean {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * yearMonth : 201804
         * inAmt : 12091
         * outAmt : 9371
         */

        private String yearMonth;
        private Double inAmt;
        private Double outAmt;

        public String getYearMonth() {
            return yearMonth;
        }

        public void setYearMonth(String yearMonth) {
            this.yearMonth = yearMonth;
        }

        public Double getInAmt() {
            return inAmt;
        }

        public void setInAmt(Double inAmt) {
            this.inAmt = inAmt;
        }

        public Double getOutAmt() {
            return outAmt;
        }

        public void setOutAmt(Double outAmt) {
            this.outAmt = outAmt;
        }
    }
}
