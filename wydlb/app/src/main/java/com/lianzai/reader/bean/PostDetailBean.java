package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2019/3/18.
 */

public class PostDetailBean {


    /**
     * code : 0
     * data : {"commentPage":{"list":[{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0},"dynamicResp":{"address":"string","commentCount":0,"commentResp":{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}},"contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","headList":["string"],"id":0,"images":["string"],"isLike":false,"latitude":"string","likeCount":0,"longitude":"string","picturesShow":"string","platformCover":"string","platformId":0,"platformName":"string","platformPeopleNumber":0,"postType":0,"rateImages":["string"],"thumbnailImages":["string"],"titleShow":"string","userId":0,"userName":"string","userPic":"string"},"hotComments":[{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}}],"isDelete":false}
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
         * commentPage : {"list":[{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
         * dynamicResp : {"address":"string","commentCount":0,"commentResp":{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}},"contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","headList":["string"],"id":0,"images":["string"],"isLike":false,"latitude":"string","likeCount":0,"longitude":"string","picturesShow":"string","platformCover":"string","platformId":0,"platformName":"string","platformPeopleNumber":0,"postType":0,"rateImages":["string"],"thumbnailImages":["string"],"titleShow":"string","userId":0,"userName":"string","userPic":"string"}
         * hotComments : [{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}}]
         * isDelete : false
         */

        private CommentPageBean commentPage;
        private CircleDynamicBean.DataBean.DynamicRespBean dynamicResp;
        private boolean isDelete;
        private List<CommentPageBean.ListBean> hotComments;
        private int platformId;
        private String platformName;
        private String platformCover;
        private int platformPeopleNumber;
        private List<String> headList;

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

        public int getPlatformPeopleNumber() {
            return platformPeopleNumber;
        }

        public void setPlatformPeopleNumber(int platformPeopleNumber) {
            this.platformPeopleNumber = platformPeopleNumber;
        }

        public List<String> getHeadList() {
            return headList;
        }

        public void setHeadList(List<String> headList) {
            this.headList = headList;
        }

        public CommentPageBean getCommentPage() {
            return commentPage;
        }

        public void setCommentPage(CommentPageBean commentPage) {
            this.commentPage = commentPage;
        }

        public CircleDynamicBean.DataBean.DynamicRespBean getDynamicResp() {
            return dynamicResp;
        }

        public void setDynamicResp(CircleDynamicBean.DataBean.DynamicRespBean dynamicResp) {
            this.dynamicResp = dynamicResp;
        }

        public boolean isIsDelete() {
            return isDelete;
        }

        public void setIsDelete(boolean isDelete) {
            this.isDelete = isDelete;
        }

        public List<CommentPageBean.ListBean> getHotComments() {
            return hotComments;
        }

        public void setHotComments(List<CommentPageBean.ListBean> hotComments) {
            this.hotComments = hotComments;
        }

        public static class CommentPageBean {
            /**
             * list : [{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}}]
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
                 * comUserId : 0
                 * comUserName : string
                 * comUsrePic : string
                 * contentShow : string
                 * createTime : 2019-03-20T02:58:11.427Z
                 * id : 0
                 * isLike : false
                 * likeCount : 0
                 * postId : 0
                 * replyCount : 0
                 * replyResp : {"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-20T02:58:11.427Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}
                 */

                private int auditStatus;
                private int comUserId;
                private String comUserName;
                private String comUserPic;
                private String contentShow;
                private String createTime;
                private int id;
                private boolean isLike;
                private int likeCount;
                private int postId;
                private int replyCount;
                private int totalReplyCount;
                private ReplyRespBean replyResp;
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

                public int getTotalReplyCount() {
                    return totalReplyCount;
                }

                public void setTotalReplyCount(int totalReplyCount) {
                    this.totalReplyCount = totalReplyCount;
                }

                public int getAuditStatus() {
                    return auditStatus;
                }

                public void setAuditStatus(int auditStatus) {
                    this.auditStatus = auditStatus;
                }

                public int getComUserId() {
                    return comUserId;
                }

                public void setComUserId(int comUserId) {
                    this.comUserId = comUserId;
                }

                public String getComUserName() {
                    return comUserName;
                }

                public void setComUserName(String comUserName) {
                    this.comUserName = comUserName;
                }

                public String getComUsrePic() {
                    return comUserPic;
                }

                public void setComUsrePic(String comUsrePic) {
                    this.comUserPic = comUsrePic;
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

                public ReplyRespBean getReplyResp() {
                    return replyResp;
                }

                public void setReplyResp(ReplyRespBean replyResp) {
                    this.replyResp = replyResp;
                }

                public static class ReplyRespBean {
                    /**
                     * auditStatus : 0
                     * beReplyUserName : string
                     * contentShow : string
                     * createTime : 2019-03-20T02:58:11.427Z
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
                    private String urlTitle;
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

    }


}
