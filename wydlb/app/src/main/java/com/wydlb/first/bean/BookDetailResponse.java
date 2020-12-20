package com.wydlb.first.bean;

import com.wydlb.first.utils.RxTool;

public class BookDetailResponse {


    /**
     * code : 0
     * msg : success
     * data : {"bookId":747,"platformId":4112,"platformName":"内站书--1","platformCover":"http://img3.3lian.com/2013/c2/78/d/38.jpg","penName":"小鬼上酒","platformIntroduce":"撒旦法撒旦法撒旦法撒旦法撒旦法阿斯顿放萨芬的阿斯顿放撒旦法安抚撒旦法沙发阿斯顿发发撒旦法撒旦法安抚安抚","isUnread":null,"isConcern":null,"yxAccid":null,"shareUrl":"http://lianzaihao.bendixing.netshares/shares/pf/2jDePxZk"}
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
         * bookId : 747
         * platformId : 4112
         * platformName : 内站书--1
         * platformCover : http://img3.3lian.com/2013/c2/78/d/38.jpg
         * penName : 小鬼上酒
         * platformIntroduce : 撒旦法撒旦法撒旦法撒旦法撒旦法阿斯顿放萨芬的阿斯顿放撒旦法安抚撒旦法沙发阿斯顿发发撒旦法撒旦法安抚安抚
         * isUnread : null
         * isConcern : null
         * yxAccid : null
         * shareUrl : http://lianzaihao.bendixing.netshares/shares/pf/2jDePxZk
         */

        private int bookId;
        private int platformId;
        private String platformName;
        private String platformCover;
        private String penName;
        private String platformIntroduce;
        private Object isUnread;
        private Object isConcern;
        private Object yxAccid;
        private String shareUrl;

        private String source;

        private String categoryName;

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public int getPlatformId() {
            return platformId;
        }

        public void setPlatformId(int platformId) {
            this.platformId = platformId;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPlatformCover() {
            return platformCover;
        }

        public void setPlatformCover(String platformCover) {
            this.platformCover = platformCover;
        }

        public String getPenName() {
            return penName;
        }

        public void setPenName(String penName) {
            this.penName = penName;
        }

        public String getPlatformIntroduce() {
            return RxTool.filterContent(platformIntroduce);
        }

        public void setPlatformIntroduce(String platformIntroduce) {
            this.platformIntroduce = platformIntroduce;
        }

        public Object getIsUnread() {
            return isUnread;
        }

        public void setIsUnread(Object isUnread) {
            this.isUnread = isUnread;
        }

        public Object getIsConcern() {
            return isConcern;
        }

        public void setIsConcern(Object isConcern) {
            this.isConcern = isConcern;
        }

        public Object getYxAccid() {
            return yxAccid;
        }

        public void setYxAccid(Object yxAccid) {
            this.yxAccid = yxAccid;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }
    }
}
