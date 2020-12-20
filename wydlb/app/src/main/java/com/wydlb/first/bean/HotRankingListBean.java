package com.wydlb.first.bean;

import java.util.List;

public class HotRankingListBean {


    /**
     * code : 0
     * data : [{"booklistId":0,"cover":"string","details":[{"bid":"string","bookAuthor":"string","bookCategory":"string","bookTitle":"string","editorComment":"string","id":0,"isConcern":false,"novelId":0,"novelType":0,"platformCover":"string","platformId":0,"platformIntroduce":"string","platformType":"string","score":0,"secret":"string","source":"string","wordNum":0,"yxAccid":"string"}],"id":0,"info":"string","name":"string","sort":0,"type":0,"updateType":0}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * booklistId : 0
         * cover : string
         * details : [{"bid":"string","bookAuthor":"string","bookCategory":"string","bookTitle":"string","editorComment":"string","id":0,"isConcern":false,"novelId":0,"novelType":0,"platformCover":"string","platformId":0,"platformIntroduce":"string","platformType":"string","score":0,"secret":"string","source":"string","wordNum":0,"yxAccid":"string"}]
         * id : 0
         * info : string
         * name : string
         * sort : 0
         * type : 0
         * updateType : 0
         */

        private int booklistId;
        private String cover;
        private int id;
        private String info;
        private String name;
        private int sort;
        private int type;
        private int updateType;
        private List<DetailsBean> details;

        public int getBooklistId() {
            return booklistId;
        }

        public void setBooklistId(int booklistId) {
            this.booklistId = booklistId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUpdateType() {
            return updateType;
        }

        public void setUpdateType(int updateType) {
            this.updateType = updateType;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * bid : string
             * bookAuthor : string
             * bookCategory : string
             * bookTitle : string
             * editorComment : string
             * id : 0
             * isConcern : false
             * novelId : 0
             * novelType : 0
             * platformCover : string
             * platformId : 0
             * platformIntroduce : string
             * platformType : string
             * score : 0
             * secret : string
             * source : string
             * wordNum : 0
             * yxAccid : string
             */

            private String bid;
            private String bookAuthor;
            private String bookCategory;
            private String bookTitle;
            private String editorComment;
            private int id;
            private boolean isConcern;
            private int novelId;
            private int novelType;
            private String platformCover;
            private int platformId;
            private String platformIntroduce;
            private String platformType;
            private int score;
            private String secret;
            private String source;
            private int wordNum;
            private String yxAccid;

            public String getBid() {
                return bid;
            }

            public void setBid(String bid) {
                this.bid = bid;
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

            public String getBookTitle() {
                return bookTitle;
            }

            public void setBookTitle(String bookTitle) {
                this.bookTitle = bookTitle;
            }

            public String getEditorComment() {
                return editorComment;
            }

            public void setEditorComment(String editorComment) {
                this.editorComment = editorComment;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean isIsConcern() {
                return isConcern;
            }

            public void setIsConcern(boolean isConcern) {
                this.isConcern = isConcern;
            }

            public int getNovelId() {
                return novelId;
            }

            public void setNovelId(int novelId) {
                this.novelId = novelId;
            }

            public int getNovelType() {
                return novelType;
            }

            public void setNovelType(int novelType) {
                this.novelType = novelType;
            }

            public String getPlatformCover() {
                return platformCover;
            }

            public void setPlatformCover(String platformCover) {
                this.platformCover = platformCover;
            }

            public int getPlatformId() {
                return platformId;
            }

            public void setPlatformId(int platformId) {
                this.platformId = platformId;
            }

            public String getPlatformIntroduce() {
                return platformIntroduce;
            }

            public void setPlatformIntroduce(String platformIntroduce) {
                this.platformIntroduce = platformIntroduce;
            }

            public String getPlatformType() {
                return platformType;
            }

            public void setPlatformType(String platformType) {
                this.platformType = platformType;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getWordNum() {
                return wordNum;
            }

            public void setWordNum(int wordNum) {
                this.wordNum = wordNum;
            }

            public String getYxAccid() {
                return yxAccid;
            }

            public void setYxAccid(String yxAccid) {
                this.yxAccid = yxAccid;
            }
        }
    }
}
