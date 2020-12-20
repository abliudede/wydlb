package com.wydlb.first.bean;

import com.wydlb.first.base.Constant;

import java.util.List;

public class CirclePersonBean {


    /**
     * code : 0
     * data : {"administratorCount":0,"agentCircleMaster":{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0},"bannedCount":0,"circleAdministratorList":[{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}],"circleMaster":{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0},"circleMemberList":{"list":[{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}}
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
         * administratorCount : 0
         * agentCircleMaster : {"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}
         * bannedCount : 0
         * circleAdministratorList : [{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}]
         * circleMaster : {"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}
         * circleMemberList : {"list":[{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}],"pageNum":0,"pageSize":0,"pages":0,"size":0,"total":0}
         */

        private int administratorCount;
        private CircleMemberListBean.ListBean agentCircleMaster;
        private int bannedCount;
        private CircleMemberListBean.ListBean circleMaster;
        private CircleMemberListBean circleMemberList;
        private List<CircleMemberListBean.ListBean> circleAdministratorList;

        public int getAdministratorCount() {
            return administratorCount;
        }

        public void setAdministratorCount(int administratorCount) {
            this.administratorCount = administratorCount;
        }

        public CircleMemberListBean.ListBean getAgentCircleMaster() {
            return agentCircleMaster;
        }

        public void setAgentCircleMaster(CircleMemberListBean.ListBean agentCircleMaster) {
            this.agentCircleMaster = agentCircleMaster;
        }

        public int getBannedCount() {
            return bannedCount;
        }

        public void setBannedCount(int bannedCount) {
            this.bannedCount = bannedCount;
        }

        public CircleMemberListBean.ListBean getCircleMaster() {
            return circleMaster;
        }

        public void setCircleMaster(CircleMemberListBean.ListBean circleMaster) {
            this.circleMaster = circleMaster;
        }

        public CircleMemberListBean getCircleMemberList() {
            return circleMemberList;
        }

        public void setCircleMemberList(CircleMemberListBean circleMemberList) {
            this.circleMemberList = circleMemberList;
        }

        public List<CircleMemberListBean.ListBean> getCircleAdministratorList() {
            return circleAdministratorList;
        }

        public void setCircleAdministratorList(List<CircleMemberListBean.ListBean> circleAdministratorList) {
            this.circleAdministratorList = circleAdministratorList;
        }


        public static class CircleMemberListBean {
            /**
             * list : [{"attentionStatus":0,"avatar":"string","createTime":"2019-08-19T03:11:21.847Z","gender":0,"id":0,"nickName":"string","readTime":0,"userId":0,"userType":0}]
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
                 * attentionStatus : 0
                 * avatar : string
                 * createTime : 2019-08-19T03:11:21.847Z
                 * gender : 0
                 * id : 0
                 * nickName : string
                 * readTime : 0
                 * userId : 0
                 * userType : 0
                 */

                private int attentionStatus;
                private String avatar;
                private String createTime;
                private int gender;
                private int id;
                private String nickName;
                private int readTime;
                private int userId;
                private int userType;

                public int getAttentionStatus() {
                    return attentionStatus;
                }

                public void setAttentionStatus(int attentionStatus) {
                    this.attentionStatus = attentionStatus;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public int getGender() {
                    return gender;
                }

                public void setGender(int gender) {
                    this.gender = gender;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
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
            }
        }


    }
}
