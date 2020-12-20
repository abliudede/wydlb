package com.wydlb.first.bean;

import com.wydlb.first.model.bean.BookStoreBeanN;

import java.util.List;

public class GetUserPersonalInfoBean {

    /**
     * code : 0
     * data : {"attentionStatus":0,"attentionSum":0,"avatar":"string","bookListSum":0,"bookShelfs":[{"bookId":0,"categoryName":"string","isConcern":false,"isUnread":false,"penName":"string","platformCover":"string","platformId":0,"platformIntroduce":"string","platformName":"string","platformType":0,"shareUrl":"string","source":"string","yxAccid":"string"}],"fansSum":0,"gender":0,"introduce":"string","nickName":"string","readLikeTags":[{"id":0,"name":"string"}],"readTime":0,"userId":0,"yxAccid":"string"}
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
         * attentionStatus : 0
         * attentionSum : 0
         * avatar : string
         * bookListSum : 0
         * bookShelfs : [{"bookId":0,"categoryName":"string","isConcern":false,"isUnread":false,"penName":"string","platformCover":"string","platformId":0,"platformIntroduce":"string","platformName":"string","platformType":0,"shareUrl":"string","source":"string","yxAccid":"string"}]
         * fansSum : 0
         * gender : 0
         * introduce : string
         * nickName : string
         * readLikeTags : [{"id":0,"name":"string"}]
         * readTime : 0
         * userId : 0
         * yxAccid : string
         */

        private int attentionStatus;
        private int attentionSum;
        private String avatar;
        private int bookListSum;
        private int fansSum;
        private int gender;
        private String introduce;
        private String nickName;
        private int readTime;
        private String userId;
        private String yxAccid;
        private List<BookStoreBeanN> bookShelfs;
        private List<ReadLikeTagsBean> readLikeTags;
        private int readLikeGender;
        private int isBlack;//isBlack
        private List<RankInfos> rankInfos;

        public List<RankInfos> getRankInfos() {
            return rankInfos;
        }

        public void setRankInfos(List<RankInfos> rankInfos) {
            this.rankInfos = rankInfos;
        }

        public int getIsBlack() {
            return isBlack;
        }

        public void setIsBlack(int isBlack) {
            this.isBlack = isBlack;
        }

        public int getReadLikeGender() {
            return readLikeGender;
        }

        public void setReadLikeGender(int readLikeGender) {
            this.readLikeGender = readLikeGender;
        }

        public int getAttentionStatus() {
            return attentionStatus;
        }

        public void setAttentionStatus(int attentionStatus) {
            this.attentionStatus = attentionStatus;
        }

        public int getAttentionSum() {
            return attentionSum;
        }

        public void setAttentionSum(int attentionSum) {
            this.attentionSum = attentionSum;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getBookListSum() {
            return bookListSum;
        }

        public void setBookListSum(int bookListSum) {
            this.bookListSum = bookListSum;
        }

        public int getFansSum() {
            return fansSum;
        }

        public void setFansSum(int fansSum) {
            this.fansSum = fansSum;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getReadTime() {
            return readTime;
        }

        public void setReadTime(int readTime) {
            this.readTime = readTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getYxAccid() {
            return yxAccid;
        }

        public void setYxAccid(String yxAccid) {
            this.yxAccid = yxAccid;
        }

        public List<BookStoreBeanN> getBookShelfs() {
            return bookShelfs;
        }

        public void setBookShelfs(List<BookStoreBeanN> bookShelfs) {
            this.bookShelfs = bookShelfs;
        }

        public List<ReadLikeTagsBean> getReadLikeTags() {
            return readLikeTags;
        }

        public void setReadLikeTags(List<ReadLikeTagsBean> readLikeTags) {
            this.readLikeTags = readLikeTags;
        }

        public static class RankInfos{

            /**
             * code : string
             * icon : string
             * name : string
             */

            private String code;
            private String icon;
            private String name;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


        public static class ReadLikeTagsBean {
            /**
             * id : 0
             * name : string
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
