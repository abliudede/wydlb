package com.wydlb.first.bean;

import com.wydlb.first.utils.RxTool;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: BookListDetailResponse
 * Author: lrz
 * Date: 2018/10/12 17:09
 * Description: ${DESCRIPTION}
 */
public class BookListDetailResponse extends BaseBean {


    /**
     * data : {"total":4,"list":[{"id":161169,"bid":"12544","novelId":null,"bookTitle":"他来了，请闭眼","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/3440417","score":0,"editorComment":"","platformIntroduce":""},{"id":161173,"bid":"12544","novelId":"415536","bookTitle":"美人为馅","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/3347150","score":0,"editorComment":"","platformIntroduce":""},{"id":161179,"bid":"12544","novelId":"453306","bookTitle":"你和我的倾城时光","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/1003428690","score":0,"editorComment":"","platformIntroduce":""},{"id":161178,"bid":"12544","novelId":"496453","bookTitle":"如果蜗牛有爱情","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/3440419","score":0,"editorComment":"","platformIntroduce":""}],"pageNum":1,"pageSize":10,"pages":1,"size":4}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 4
         * list : [{"id":161169,"bid":"12544","novelId":null,"bookTitle":"他来了，请闭眼","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/3440417","score":0,"editorComment":"","platformIntroduce":""},{"id":161173,"bid":"12544","novelId":"415536","bookTitle":"美人为馅","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/3347150","score":0,"editorComment":"","platformIntroduce":""},{"id":161179,"bid":"12544","novelId":"453306","bookTitle":"你和我的倾城时光","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/1003428690","score":0,"editorComment":"","platformIntroduce":""},{"id":161178,"bid":"12544","novelId":"496453","bookTitle":"如果蜗牛有爱情","bookAuthor":"丁墨","bookCategory":"现代言情","bookUrl":"//book.qidian.com/info/3440419","score":0,"editorComment":"","platformIntroduce":""}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 4
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
             * id : 161169
             * bid : 12544
             * novelId : null
             * bookTitle : 他来了，请闭眼
             * bookAuthor : 丁墨
             * bookCategory : 现代言情
             * bookUrl : //book.qidian.com/info/3440417
             * score : 0
             * editorComment :
             * platformIntroduce :
             */

            private int id;
            private String bid;
            private Integer novelId;
            private String bookTitle;
            private String bookAuthor;
            private String bookCategory;
            private String bookUrl;
            private float score;
            private String editorComment;
            private String platformIntroduce;
            private String platformId;
            private String source;
            private boolean isJoin;

            private String wordNum;

            private int novelType;

            private boolean isCopyright;

            public boolean isCopyright() {
                return isCopyright;
            }

            public void setCopyright(boolean copyright) {
                isCopyright = copyright;
            }

            public int getNovelType() {
                return novelType;
            }

            public void setNovelType(int novelType) {
                this.novelType = novelType;
            }

            public String getWordNum() {
                return RxTool.getWordNumFormat(wordNum);
            }

            public void setWordNum(String wordNum) {
                this.wordNum = wordNum;
            }

            public boolean isConcern() {
                return isJoin;
            }

            public void setConcern(boolean concern) {
                isJoin = concern;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getPlatformId() {
                return platformId;
            }

            public void setPlatformId(String platformId) {
                this.platformId = platformId;
            }

            private boolean isExpand=false;

            public boolean isExpand() {
                return isExpand;
            }

            public void setExpand(boolean expand) {
                isExpand = expand;
            }

            private String platformCover;

            public String getPlatformCover() {
                return platformCover;
            }

            public void setPlatformCover(String platformCover) {
                this.platformCover = platformCover;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getBid() {
                return bid;
            }

            public void setBid(String bid) {
                this.bid = bid;
            }

            public Integer getNovelId() {
                return novelId;
            }

            public void setNovelId(Integer novelId) {
                this.novelId = novelId;
            }

            public String getBookTitle() {
                return bookTitle;
            }

            public void setBookTitle(String bookTitle) {
                this.bookTitle = bookTitle;
            }

            public String getBookAuthor() {
                return bookAuthor;
            }

            public void setBookAuthor(String bookAuthor) {
                this.bookAuthor = bookAuthor;
            }

            public String getBookCategory() {
                return bookCategory;
            }

            public void setBookCategory(String bookCategory) {
                this.bookCategory = bookCategory;
            }

            public String getBookUrl() {
                return bookUrl;
            }

            public void setBookUrl(String bookUrl) {
                this.bookUrl = bookUrl;
            }

            public float getScore() {
                return score;
            }

            public void setScore(float score) {
                this.score = score;
            }

            public String getEditorComment() {
                return editorComment;
            }

            public void setEditorComment(String editorComment) {
                this.editorComment = editorComment;
            }

            public String getPlatformIntroduce() {
                return platformIntroduce;
            }

            public void setPlatformIntroduce(String platformIntroduce) {
                this.platformIntroduce = platformIntroduce;
            }
        }
    }
}
