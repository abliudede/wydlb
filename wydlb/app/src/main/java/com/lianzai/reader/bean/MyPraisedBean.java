package com.lianzai.reader.bean;

import java.util.List;

public class MyPraisedBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":1,"list":[{"platformId":459003,"platformName":"樱木子小说","postId":764870,"postType":30,"postContent":"发布一条动态，收通知用。上自习自助胡","picturesShow":"","userId":499712,"userName":"连载书友424193","usrePic":null,"createTime":1553216525000}],"pageNum":1,"pageSize":10,"pages":1,"size":1}
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
         * total : 1
         * list : [{"platformId":459003,"platformName":"樱木子小说","postId":764870,"postType":30,"postContent":"发布一条动态，收通知用。上自习自助胡","picturesShow":"","userId":499712,"userName":"连载书友424193","usrePic":null,"createTime":1553216525000}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 1
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
             * createTime : 2019-07-02T01:42:53.301Z
             * likeType : 0
             * objectId : 0
             * picturesShow : string
             * platformId : 0
             * platformName : string
             * postId : 0
             * superiorId : 0
             * userId : 0
             * userName : string
             * userPic : string
             */

            private String createTime;
            private int likeType;
            private int objectId;
            private String picturesShow;
            private int platformId;
            private String platformName;
            private int postId;
            private int superiorId;
            private int userId;
            private String userName;
            private String userPic;
            private String postContent;

            public String getPostContent() {
                return postContent;
            }

            public void setPostContent(String postContent) {
                this.postContent = postContent;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getLikeType() {
                return likeType;
            }

            public void setLikeType(int likeType) {
                this.likeType = likeType;
            }

            public int getObjectId() {
                return objectId;
            }

            public void setObjectId(int objectId) {
                this.objectId = objectId;
            }

            public String getPicturesShow() {
                return picturesShow;
            }

            public void setPicturesShow(String picturesShow) {
                this.picturesShow = picturesShow;
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

            public int getPostId() {
                return postId;
            }

            public void setPostId(int postId) {
                this.postId = postId;
            }

            public int getSuperiorId() {
                return superiorId;
            }

            public void setSuperiorId(int superiorId) {
                this.superiorId = superiorId;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserPic() {
                return userPic;
            }

            public void setUserPic(String userPic) {
                this.userPic = userPic;
            }
        }
    }
}
