package com.lianzai.reader.bean;

import java.util.List;

public class UserShieldingBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":12,"list":[{"id":2,"userId":476370,"shieldingUserId":71759,"shieldingUserName":"一九九","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/static/avatar.png","status":true,"createTime":1559008726000,"updateTime":null},{"id":3,"userId":476370,"shieldingUserId":601924,"shieldingUserName":"Boh","shieldingUserSex":1,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/34.jpg","status":true,"createTime":1559008737000,"updateTime":null},{"id":7,"userId":476370,"shieldingUserId":35869,"shieldingUserName":"哝哝君٩😛ི۶٩😛ི۶٩😛ི۶🦊🦊🦊","shieldingUserSex":1,"shieldingUserHeadPortrait":"https://static.lianzai.com/iOS_avatar/155384705554560.jpeg","status":true,"createTime":1559013528000,"updateTime":null},{"id":8,"userId":476370,"shieldingUserId":602372,"shieldingUserName":"洛小阳","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_woman/164.jpg","status":true,"createTime":1559013541000,"updateTime":null},{"id":9,"userId":476370,"shieldingUserId":602456,"shieldingUserName":"浪一直很","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_woman/248.jpg","status":true,"createTime":1559013552000,"updateTime":null},{"id":10,"userId":476370,"shieldingUserId":602539,"shieldingUserName":"rrMar","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/kt_man/18.jpg","status":true,"createTime":1559013565000,"updateTime":null},{"id":11,"userId":476370,"shieldingUserId":602231,"shieldingUserName":"宇平而不","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_woman/23.jpg","status":true,"createTime":1559013578000,"updateTime":null},{"id":12,"userId":476370,"shieldingUserId":602164,"shieldingUserName":"eU龙门","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/274.jpg","status":true,"createTime":1559013593000,"updateTime":null},{"id":13,"userId":476370,"shieldingUserId":601999,"shieldingUserName":"的风Agq","shieldingUserSex":1,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/109.jpg","status":true,"createTime":1559013604000,"updateTime":null},{"id":14,"userId":476370,"shieldingUserId":602118,"shieldingUserName":"鲍林昌","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/228.jpg","status":true,"createTime":1559013612000,"updateTime":null}],"pageNum":1,"pageSize":10,"pages":2,"size":10}
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
         * total : 12
         * list : [{"id":2,"userId":476370,"shieldingUserId":71759,"shieldingUserName":"一九九","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/static/avatar.png","status":true,"createTime":1559008726000,"updateTime":null},{"id":3,"userId":476370,"shieldingUserId":601924,"shieldingUserName":"Boh","shieldingUserSex":1,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/34.jpg","status":true,"createTime":1559008737000,"updateTime":null},{"id":7,"userId":476370,"shieldingUserId":35869,"shieldingUserName":"哝哝君٩😛ི۶٩😛ི۶٩😛ི۶🦊🦊🦊","shieldingUserSex":1,"shieldingUserHeadPortrait":"https://static.lianzai.com/iOS_avatar/155384705554560.jpeg","status":true,"createTime":1559013528000,"updateTime":null},{"id":8,"userId":476370,"shieldingUserId":602372,"shieldingUserName":"洛小阳","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_woman/164.jpg","status":true,"createTime":1559013541000,"updateTime":null},{"id":9,"userId":476370,"shieldingUserId":602456,"shieldingUserName":"浪一直很","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_woman/248.jpg","status":true,"createTime":1559013552000,"updateTime":null},{"id":10,"userId":476370,"shieldingUserId":602539,"shieldingUserName":"rrMar","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/kt_man/18.jpg","status":true,"createTime":1559013565000,"updateTime":null},{"id":11,"userId":476370,"shieldingUserId":602231,"shieldingUserName":"宇平而不","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_woman/23.jpg","status":true,"createTime":1559013578000,"updateTime":null},{"id":12,"userId":476370,"shieldingUserId":602164,"shieldingUserName":"eU龙门","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/274.jpg","status":true,"createTime":1559013593000,"updateTime":null},{"id":13,"userId":476370,"shieldingUserId":601999,"shieldingUserName":"的风Agq","shieldingUserSex":1,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/109.jpg","status":true,"createTime":1559013604000,"updateTime":null},{"id":14,"userId":476370,"shieldingUserId":602118,"shieldingUserName":"鲍林昌","shieldingUserSex":0,"shieldingUserHeadPortrait":"https://static.lianzai.com/robot_profile/laugh_man/228.jpg","status":true,"createTime":1559013612000,"updateTime":null}]
         * pageNum : 1
         * pageSize : 10
         * pages : 2
         * size : 10
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
             * id : 2
             * userId : 476370
             * shieldingUserId : 71759
             * shieldingUserName : 一九九
             * shieldingUserSex : 0
             * shieldingUserHeadPortrait : https://static.lianzai.com/static/avatar.png
             * status : true
             * createTime : 1559008726000
             * updateTime : null
             */

            private int id;
            private int userId;
            private int shieldingUserId;
            private String shieldingUserName;
            private int shieldingUserSex;
            private String shieldingUserHeadPortrait;
            private boolean status;
            private long createTime;
            private Object updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getShieldingUserId() {
                return shieldingUserId;
            }

            public void setShieldingUserId(int shieldingUserId) {
                this.shieldingUserId = shieldingUserId;
            }

            public String getShieldingUserName() {
                return shieldingUserName;
            }

            public void setShieldingUserName(String shieldingUserName) {
                this.shieldingUserName = shieldingUserName;
            }

            public int getShieldingUserSex() {
                return shieldingUserSex;
            }

            public void setShieldingUserSex(int shieldingUserSex) {
                this.shieldingUserSex = shieldingUserSex;
            }

            public String getShieldingUserHeadPortrait() {
                return shieldingUserHeadPortrait;
            }

            public void setShieldingUserHeadPortrait(String shieldingUserHeadPortrait) {
                this.shieldingUserHeadPortrait = shieldingUserHeadPortrait;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
