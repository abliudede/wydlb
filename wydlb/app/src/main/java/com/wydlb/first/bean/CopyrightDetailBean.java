package com.wydlb.first.bean;

import java.math.BigDecimal;

public class CopyrightDetailBean {


    /**
     * code : 0
     * data : {"bookAuthor":"string","bookCoinCurrency":"string","bookCoinName":"string","bookId":0,"bookName":"string","coinBookBalance":0,"couponBookBalance":0,"cover":"string","iconUrl":"string","readBalance":0,"userId":0}
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
         * bookAuthor : string
         * bookCoinCurrency : string
         * bookCoinName : string
         * bookId : 0
         * bookName : string
         * coinBookBalance : 0
         * couponBookBalance : 0
         * cover : string
         * iconUrl : string
         * readBalance : 0
         * userId : 0
         */

        private String bookAuthor;
        private String bookCoinCurrency;
        private String bookCoinName;
        private int bookId;
        private String bookName;
        private double coinBookBalance;
        private double couponBookBalance;
        private double coinLockBookBalance;
        private String cover;
        private String iconUrl;
        private double readBalance;
        private int userId;

        public double getCoinLockBookBalance() {
            return coinLockBookBalance;
        }

        public void setCoinLockBookBalance(double coinLockBookBalance) {
            this.coinLockBookBalance = coinLockBookBalance;
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

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public double getReadBalance() {
            return readBalance;
        }

        public void setReadBalance(double readBalance) {
            this.readBalance = readBalance;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
