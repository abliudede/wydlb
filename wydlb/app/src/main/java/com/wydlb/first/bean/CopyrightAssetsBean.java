package com.wydlb.first.bean;

import java.util.List;

public class CopyrightAssetsBean {


    /**
     * code : 0
     * data : {"copyrightList":[{"bookAttenNum":0,"bookAuthor":"string","bookCoinCurrency":"string","bookCoinName":"string","bookId":0,"bookName":"string","bookWordNum":0,"coinBookBalance":0,"couponBookBalance":0,"cover":"string","readBalance":0}],"readAllBalance":0}
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
         * copyrightList : [{"bookAttenNum":0,"bookAuthor":"string","bookCoinCurrency":"string","bookCoinName":"string","bookId":0,"bookName":"string","bookWordNum":0,"coinBookBalance":0,"couponBookBalance":0,"cover":"string","readBalance":0}]
         * readAllBalance : 0
         */

        private double readAllBalance;
        private List<CopyrightListBean> copyrightList;

        public double getReadAllBalance() {
            return readAllBalance;
        }

        public void setReadAllBalance(double readAllBalance) {
            this.readAllBalance = readAllBalance;
        }

        public List<CopyrightListBean> getCopyrightList() {
            return copyrightList;
        }

        public void setCopyrightList(List<CopyrightListBean> copyrightList) {
            this.copyrightList = copyrightList;
        }

        public static class CopyrightListBean {
            /**
             * bookAttenNum : 0
             * bookAuthor : string
             * bookCoinCurrency : string
             * bookCoinName : string
             * bookId : 0
             * bookName : string
             * bookWordNum : 0
             * coinBookBalance : 0
             * couponBookBalance : 0
             * cover : string
             * readBalance : 0
             */

            private int bookAttenNum;
            private String bookAuthor;
            private String bookCoinCurrency;
            private String bookCoinName;
            private int bookId;
            private String bookName;
            private int bookWordNum;
            private double coinBookBalance;
            private double couponBookBalance;
            private String cover;
            private double readBalance;
            private String symbols;
            private String platformId;
            private double corwdBalance;

            public double getCorwdBalance() {
                return corwdBalance;
            }

            public void setCorwdBalance(double corwdBalance) {
                this.corwdBalance = corwdBalance;
            }

            public String getPlatformId() {
                return platformId;
            }

            public void setPlatformId(String platformId) {
                this.platformId = platformId;
            }

            public String getSymbols() {
                return symbols;
            }

            public void setSymbols(String symbols) {
                this.symbols = symbols;
            }

            public int getBookAttenNum() {
                return bookAttenNum;
            }

            public void setBookAttenNum(int bookAttenNum) {
                this.bookAttenNum = bookAttenNum;
            }

            public String getBookAuthor() {
                return bookAuthor;
            }

            public void setBookAuthor(String bookAuthor) {
                this.bookAuthor = bookAuthor;
            }

            public String getBookCoinCurrency() {
                return bookCoinCurrency;
            }

            public void setBookCoinCurrency(String bookCoinCurrency) {
                this.bookCoinCurrency = bookCoinCurrency;
            }

            public String getBookCoinName() {
                return bookCoinName;
            }

            public void setBookCoinName(String bookCoinName) {
                this.bookCoinName = bookCoinName;
            }

            public int getBookId() {
                return bookId;
            }

            public void setBookId(int bookId) {
                this.bookId = bookId;
            }

            public String getBookName() {
                return bookName;
            }

            public void setBookName(String bookName) {
                this.bookName = bookName;
            }

            public int getBookWordNum() {
                return bookWordNum;
            }

            public void setBookWordNum(int bookWordNum) {
                this.bookWordNum = bookWordNum;
            }

            public double getCoinBookBalance() {
                return coinBookBalance;
            }

            public void setCoinBookBalance(double coinBookBalance) {
                this.coinBookBalance = coinBookBalance;
            }

            public double getCouponBookBalance() {
                return couponBookBalance;
            }

            public void setCouponBookBalance(double couponBookBalance) {
                this.couponBookBalance = couponBookBalance;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public double getReadBalance() {
                return readBalance;
            }

            public void setReadBalance(double readBalance) {
                this.readBalance = readBalance;
            }
        }
    }
}
