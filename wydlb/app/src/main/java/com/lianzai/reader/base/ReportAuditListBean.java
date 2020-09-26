package com.lianzai.reader.base;

import java.util.List;

public class ReportAuditListBean {


    /**
     * code : 0
     * data : {"list":[{"againstId":0,"beCommentCount":0,"beLikeCount":0,"beReportCount":0,"content":"string","contentType":0,"headPortrait":"string","images":["string"],"nikeName":"string","objectId":0,"pictures":"string","publishTime":"2019-08-05T01:03:25.132Z","rateImages":["string"],"thumbnailImages":["string"],"userId":0,"userType":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
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
         * list : [{"againstId":0,"beCommentCount":0,"beLikeCount":0,"beReportCount":0,"content":"string","contentType":0,"headPortrait":"string","images":["string"],"nikeName":"string","objectId":0,"pictures":"string","publishTime":"2019-08-05T01:03:25.132Z","rateImages":["string"],"thumbnailImages":["string"],"userId":0,"userType":0}]
         * pageNum : 0
         * pageSize : 0
         * pages : 0
         * size : 0
         * total : 0
         */

        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private int total;
        private List<ListBean> list;

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

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * againstId : 0
             * beCommentCount : 0
             * beLikeCount : 0
             * beReportCount : 0
             * content : string
             * contentType : 0
             * headPortrait : string
             * images : ["string"]
             * nikeName : string
             * objectId : 0
             * pictures : string
             * publishTime : 2019-08-05T01:03:25.132Z
             * rateImages : ["string"]
             * thumbnailImages : ["string"]
             * userId : 0
             * userType : 0
             */

            private int againstId;
            private int beCommentCount;
            private int beLikeCount;
            private int beReportCount;
            private String content;
            private int contentType;
            private String headPortrait;
            private String nikeName;
            private int objectId;
            private String pictures;
            private String publishTime;
            private int userId;
            private int userType;
            private List<String> images;
            private List<String> rateImages;
            private List<String> thumbnailImages;
            private long createTime;
            private String urlTitle;//网页解析字符串

            public String getUrlTitle() {
                return urlTitle;
            }

            public void setUrlTitle(String urlTitle) {
                this.urlTitle = urlTitle;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getAgainstId() {
                return againstId;
            }

            public void setAgainstId(int againstId) {
                this.againstId = againstId;
            }

            public int getBeCommentCount() {
                return beCommentCount;
            }

            public void setBeCommentCount(int beCommentCount) {
                this.beCommentCount = beCommentCount;
            }

            public int getBeLikeCount() {
                return beLikeCount;
            }

            public void setBeLikeCount(int beLikeCount) {
                this.beLikeCount = beLikeCount;
            }

            public int getBeReportCount() {
                return beReportCount;
            }

            public void setBeReportCount(int beReportCount) {
                this.beReportCount = beReportCount;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getContentType() {
                return contentType;
            }

            public void setContentType(int contentType) {
                this.contentType = contentType;
            }

            public String getHeadPortrait() {
                return headPortrait;
            }

            public void setHeadPortrait(String headPortrait) {
                this.headPortrait = headPortrait;
            }

            public String getNikeName() {
                return nikeName;
            }

            public void setNikeName(String nikeName) {
                this.nikeName = nikeName;
            }

            public int getObjectId() {
                return objectId;
            }

            public void setObjectId(int objectId) {
                this.objectId = objectId;
            }

            public String getPictures() {
                return pictures;
            }

            public void setPictures(String pictures) {
                this.pictures = pictures;
            }

            public String getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(String publishTime) {
                this.publishTime = publishTime;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getUserType() {
                if(userType == 0){
                    return Constant.Role.FANS_ROLE;
                }else {
                    return userType;
                }
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public List<String> getRateImages() {
                return rateImages;
            }

            public void setRateImages(List<String> rateImages) {
                this.rateImages = rateImages;
            }

            public List<String> getThumbnailImages() {
                return thumbnailImages;
            }

            public void setThumbnailImages(List<String> thumbnailImages) {
                this.thumbnailImages = thumbnailImages;
            }
        }
    }
}
