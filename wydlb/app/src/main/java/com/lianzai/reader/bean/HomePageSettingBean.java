package com.lianzai.reader.bean;

import java.util.List;

public class HomePageSettingBean {


    /**
     * code : 0
     * data : {"headBookList":[{"bookListId":0,"detailList":[{"bid":"string","bookAuthor":"string","bookCategory":"string","bookTitle":"string","editorComment":"string","id":0,"isConcern":false,"isCopyright":true,"isJoin":false,"novelId":0,"novelType":0,"platformCover":"string","platformId":0,"platformIntroduce":"string","platformType":"string","score":0,"secret":"string","source":"string","wordNum":0,"yxAccid":"string"}],"moduleName":"string"}],"selfBuiltBookList":[{"bookListId":0,"detailList":[{"bid":"string","bookAuthor":"string","bookCategory":"string","bookTitle":"string","editorComment":"string","id":0,"isConcern":false,"isCopyright":true,"isJoin":false,"novelId":0,"novelType":0,"platformCover":"string","platformId":0,"platformIntroduce":"string","platformType":"string","score":0,"secret":"string","source":"string","wordNum":0,"yxAccid":"string"}],"moduleName":"string"}]}
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
        private List<HeadBookListBean> headBookList;
        private List<HeadBookListBean> selfBuiltBookList;

        public List<HeadBookListBean> getHeadBookList() {
            return headBookList;
        }

        public void setHeadBookList(List<HeadBookListBean> headBookList) {
            this.headBookList = headBookList;
        }

        public List<HeadBookListBean> getSelfBuiltBookList() {
            return selfBuiltBookList;
        }

        public void setSelfBuiltBookList(List<HeadBookListBean> selfBuiltBookList) {
            this.selfBuiltBookList = selfBuiltBookList;
        }

        public static class HeadBookListBean {
            /**
             * bookListId : 0
             * detailList : [{"bid":"string","bookAuthor":"string","bookCategory":"string","bookTitle":"string","editorComment":"string","id":0,"isConcern":false,"isCopyright":true,"isJoin":false,"novelId":0,"novelType":0,"platformCover":"string","platformId":0,"platformIntroduce":"string","platformType":"string","score":0,"secret":"string","source":"string","wordNum":0,"yxAccid":"string"}]
             * moduleName : string
             */

            private int bookListId;
            private String moduleName;
            private List<DetailListBean> detailList;

            public int getBookListId() {
                return bookListId;
            }

            public void setBookListId(int bookListId) {
                this.bookListId = bookListId;
            }

            public String getModuleName() {
                return moduleName;
            }

            public void setModuleName(String moduleName) {
                this.moduleName = moduleName;
            }

            public List<DetailListBean> getDetailList() {
                return detailList;
            }

            public void setDetailList(List<DetailListBean> detailList) {
                this.detailList = detailList;
            }

            public static class DetailListBean {
                /**
                 * bid : string
                 * bookAuthor : string
                 * bookCategory : string
                 * bookTitle : string
                 * editorComment : string
                 * id : 0
                 * isConcern : false
                 * isCopyright : true
                 * isJoin : false
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
                private boolean isCopyright;
                private boolean isJoin;
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

                public boolean isIsCopyright() {
                    return isCopyright;
                }

                public void setIsCopyright(boolean isCopyright) {
                    this.isCopyright = isCopyright;
                }

                public boolean isIsJoin() {
                    return isJoin;
                }

                public void setIsJoin(boolean isJoin) {
                    this.isJoin = isJoin;
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
}
