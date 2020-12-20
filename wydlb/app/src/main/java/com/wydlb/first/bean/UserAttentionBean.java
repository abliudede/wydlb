package com.wydlb.first.bean;

import java.util.List;

public class UserAttentionBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":9,"list":[{"userId":499571,"avatar":"https://static.lianzai.com/iOS_avatar/154459612287300.jpeg","nickName":"已关注5分钟","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1},{"userId":16660,"avatar":"https://static.lianzai.com/avatar/android_1540808512228.jpg","nickName":"张学有","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1},{"userId":499540,"avatar":"https://static.lianzai.com/avatar/android_1541039772942.jpg","nickName":"未关注5分钟","attentionSum":0,"fansSum":2,"gender":0,"attentionStatus":1},{"userId":499348,"avatar":"https://static.lianzai.com/platform/platform-official.png","nickName":"连载神器团队","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1},{"userId":11386,"avatar":"http://wx.qlogo.cn/mmopen/vi_32/Tn682cun0nESvLsjK9av9ay86RWLEx615K0cxicuA4ibcuInkO8ibX5fuGBUXrN74Saic7g1OL5wl2FniayAflRjfvg/0","nickName":"刘小忠","attentionSum":0,"fansSum":3,"gender":0,"attentionStatus":1},{"userId":35869,"avatar":"https://static.lianzai.com/avatar/1519377609669.png","nickName":"哝哝君٩😛ི۶٩😛ི۶٩😛ི۶🦊🦊🦊","attentionSum":27,"fansSum":4,"gender":1,"attentionStatus":1},{"userId":499371,"avatar":"https://static.lianzai.com/iOS_avatar/153814192072683.jpeg","nickName":"连载小妞","attentionSum":0,"fansSum":2,"gender":0,"attentionStatus":1},{"userId":19939,"avatar":"https://static.lianzai.com/avatar/android_1537431728404.png","nickName":"樱木子😛😛😛😛😛😛😛😛😝☘️🌿🍀","attentionSum":26,"fansSum":4,"gender":0,"attentionStatus":1},{"userId":16703,"avatar":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eotZibAenFnIwG3p7rIEcibFoUCepQtR0REOGE3ZQmyS7l1TjBPuhlIibtgmsY7a03ocY2j2cCtc9icpg/0","nickName":"洗的干干净净的","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1}],"pageNum":1,"pageSize":10,"pages":1,"size":9}
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
         * total : 9
         * list : [{"userId":499571,"avatar":"https://static.lianzai.com/iOS_avatar/154459612287300.jpeg","nickName":"已关注5分钟","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1},{"userId":16660,"avatar":"https://static.lianzai.com/avatar/android_1540808512228.jpg","nickName":"张学有","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1},{"userId":499540,"avatar":"https://static.lianzai.com/avatar/android_1541039772942.jpg","nickName":"未关注5分钟","attentionSum":0,"fansSum":2,"gender":0,"attentionStatus":1},{"userId":499348,"avatar":"https://static.lianzai.com/platform/platform-official.png","nickName":"连载神器团队","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1},{"userId":11386,"avatar":"http://wx.qlogo.cn/mmopen/vi_32/Tn682cun0nESvLsjK9av9ay86RWLEx615K0cxicuA4ibcuInkO8ibX5fuGBUXrN74Saic7g1OL5wl2FniayAflRjfvg/0","nickName":"刘小忠","attentionSum":0,"fansSum":3,"gender":0,"attentionStatus":1},{"userId":35869,"avatar":"https://static.lianzai.com/avatar/1519377609669.png","nickName":"哝哝君٩😛ི۶٩😛ི۶٩😛ི۶🦊🦊🦊","attentionSum":27,"fansSum":4,"gender":1,"attentionStatus":1},{"userId":499371,"avatar":"https://static.lianzai.com/iOS_avatar/153814192072683.jpeg","nickName":"连载小妞","attentionSum":0,"fansSum":2,"gender":0,"attentionStatus":1},{"userId":19939,"avatar":"https://static.lianzai.com/avatar/android_1537431728404.png","nickName":"樱木子😛😛😛😛😛😛😛😛😝☘️🌿🍀","attentionSum":26,"fansSum":4,"gender":0,"attentionStatus":1},{"userId":16703,"avatar":"https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eotZibAenFnIwG3p7rIEcibFoUCepQtR0REOGE3ZQmyS7l1TjBPuhlIibtgmsY7a03ocY2j2cCtc9icpg/0","nickName":"洗的干干净净的","attentionSum":0,"fansSum":1,"gender":0,"attentionStatus":1}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 9
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
             * userId : 499571
             * avatar : https://static.lianzai.com/iOS_avatar/154459612287300.jpeg
             * nickName : 已关注5分钟
             * attentionSum : 0
             * fansSum : 1
             * gender : 0
             * attentionStatus : 1
             */

            private int userId;
            private String avatar;
            private String nickName;
            private int attentionSum;
            private int fansSum;
            private int gender;
            private int attentionStatus;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getAttentionSum() {
                return attentionSum;
            }

            public void setAttentionSum(int attentionSum) {
                this.attentionSum = attentionSum;
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

            public int getAttentionStatus() {
                return attentionStatus;
            }

            public void setAttentionStatus(int attentionStatus) {
                this.attentionStatus = attentionStatus;
            }
        }
    }
}
