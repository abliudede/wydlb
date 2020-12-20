package com.wydlb.first.bean;

import java.util.List;

public class MyNoticeDataBean {


    /**
     * code : 0
     * msg : success
     * data : {"likeUnreadCount":null,"concernUnreadCount":null,"userCommentPage":{"total":0,"list":[],"pageNum":1,"pageSize":10,"pages":0,"size":0}}
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
         * likeUnreadCount : null
         * concernUnreadCount : null
         * userCommentPage : {"total":0,"list":[],"pageNum":1,"pageSize":10,"pages":0,"size":0}
         */

        private int likeUnreadCount;
        private int concernUnreadCount;
        private UserCommentPageBean userCommentPage;
        private int administratorApplyCount;
        private int isShowApplyCount;

        public int getIsShowApplyCount() {
            return isShowApplyCount;
        }

        public void setIsShowApplyCount(int isShowApplyCount) {
            this.isShowApplyCount = isShowApplyCount;
        }

        public int getAdministratorApplyCount() {
            return administratorApplyCount;
        }

        public void setAdministratorApplyCount(int administratorApplyCount) {
            this.administratorApplyCount = administratorApplyCount;
        }

        public int getLikeUnreadCount() {
            return likeUnreadCount;
        }

        public void setLikeUnreadCount(int likeUnreadCount) {
            this.likeUnreadCount = likeUnreadCount;
        }

        public int getConcernUnreadCount() {
            return concernUnreadCount;
        }

        public void setConcernUnreadCount(int concernUnreadCount) {
            this.concernUnreadCount = concernUnreadCount;
        }

        public UserCommentPageBean getUserCommentPage() {
            return userCommentPage;
        }

        public void setUserCommentPage(UserCommentPageBean userCommentPage) {
            this.userCommentPage = userCommentPage;
        }

        public static class UserCommentPageBean {

            /**
             * total : 1
             * list : [{"id":15,"type":1,"platformId":459003,"platformName":"樱木子小说","postId":764870,"commentId":15,"postType":30,"postContent":"发布一条动态，收通知用。上自习自助胡","picturesShow":"","comUserId":499712,"comUserName":"连载书友424193","comUsrePic":null,"contentShow":"嘻嘻嘻嘻","createTime":1553148162000}]
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
                 * id : 15
                 * type : 1
                 * platformId : 459003
                 * platformName : 樱木子小说
                 * postId : 764870
                 * commentId : 15
                 * postType : 30
                 * postContent : 发布一条动态，收通知用。上自习自助胡
                 * picturesShow :
                 * comUserId : 499712
                 * comUserName : 连载书友424193
                 * comUsrePic : null
                 * contentShow : 嘻嘻嘻嘻
                 * createTime : 1553148162000
                 */

                private int id;
                private int type;
                private int platformId;
                private String platformName;
                private int postId;
                private int commentId;
                private int postType;
                private String postContent;
                private String picturesShow;
                private int comUserId;
                private String comUserName;
                private String comUserPic;
                private String contentShow;
                private long createTime;
                private int auditStatus;
                private String urlTitle;//网页解析字符串

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

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
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

                public int getCommentId() {
                    return commentId;
                }

                public void setCommentId(int commentId) {
                    this.commentId = commentId;
                }

                public int getPostType() {
                    return postType;
                }

                public void setPostType(int postType) {
                    this.postType = postType;
                }

                public String getPostContent() {
                    return postContent;
                }

                public void setPostContent(String postContent) {
                    this.postContent = postContent;
                }

                public String getPicturesShow() {
                    return picturesShow;
                }

                public void setPicturesShow(String picturesShow) {
                    this.picturesShow = picturesShow;
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

                public void setComUsrePic(String comUserPic) {
                    this.comUserPic = comUserPic;
                }

                public String getContentShow() {
                    return contentShow;
                }

                public void setContentShow(String contentShow) {
                    this.contentShow = contentShow;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }
            }
        }
    }
}
