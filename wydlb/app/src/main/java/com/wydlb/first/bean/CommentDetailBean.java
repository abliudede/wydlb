package com.wydlb.first.bean;

import java.util.List;

public class CommentDetailBean {


    /**
     * code : 0
     * data : {"list":[{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T06:57:56.712Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
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
         * list : [{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T06:57:56.712Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}]
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
             * auditStatus : 0
             * beReplyUserName : string
             * contentShow : string
             * createTime : 2019-03-20T06:57:56.712Z
             * id : 0
             * isLike : false
             * likeCount : 0
             * parentId : 0
             * postId : 0
             * replyCount : 0
             * replyUserId : 0
             * replyUserName : string
             * replyUsrePic : string
             */

            private int auditStatus;
            private String beReplyUserName;
            private String contentShow;
            private String createTime;
            private int id;
            private boolean isLike;
            private int likeCount;
            private int parentId;
            private int postId;
            private int replyCount;
            private int replyUserId;
            private String replyUserName;
            private String replyUserPic;
            private String urlTitle;//网页解析字符串
            private String circleId;

            public String getCircleId() {
                return circleId;
            }

            public void setCircleId(String circleId) {
                this.circleId = circleId;
            }

            public String getUrlTitle() {
                return urlTitle;
            }

            public void setUrlTitle(String urlTitle) {
                this.urlTitle = urlTitle;
            }

            public int getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(int auditStatus) {
                this.auditStatus = auditStatus;
            }

            public String getBeReplyUserName() {
                return beReplyUserName;
            }

            public void setBeReplyUserName(String beReplyUserName) {
                this.beReplyUserName = beReplyUserName;
            }

            public String getContentShow() {
                return contentShow;
            }

            public void setContentShow(String contentShow) {
                this.contentShow = contentShow;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public boolean isIsLike() {
                return isLike;
            }

            public void setIsLike(boolean isLike) {
                this.isLike = isLike;
            }

            public int getLikeCount() {
                return likeCount;
            }

            public void setLikeCount(int likeCount) {
                this.likeCount = likeCount;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getPostId() {
                return postId;
            }

            public void setPostId(int postId) {
                this.postId = postId;
            }

            public int getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(int replyCount) {
                this.replyCount = replyCount;
            }

            public int getReplyUserId() {
                return replyUserId;
            }

            public void setReplyUserId(int replyUserId) {
                this.replyUserId = replyUserId;
            }

            public String getReplyUserName() {
                return replyUserName;
            }

            public void setReplyUserName(String replyUserName) {
                this.replyUserName = replyUserName;
            }

            public String getReplyUsrePic() {
                return replyUserPic;
            }

            public void setReplyUsrePic(String replyUsrePic) {
                this.replyUserPic = replyUsrePic;
            }
        }
    }
}
