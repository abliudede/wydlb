package com.lianzai.reader.bean;

/**
 * Created by lrz on 2018/3/26.
 */

public class PostReplyResponse extends  BaseBean{

    /**
     * data : {"id":24895,"avatar":"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1367603268,3951883271&fm=27&gp=0.jpg","assistCount":0,"fromUid":442477,"toUid":442477,"fromName":"JIUJIUJIU","toName":"JIUJIUJIU","isFloor":true,"content":"空军建军节","createTime":1522219087}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 24895
         * avatar : https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1367603268,3951883271&fm=27&gp=0.jpg
         * assistCount : 0
         * fromUid : 442477
         * toUid : 442477
         * fromName : JIUJIUJIU
         * toName : JIUJIUJIU
         * isFloor : true
         * content : 空军建军节
         * createTime : 1522219087
         */

        private int id;
        private String avatar;
        private int assistCount;
        private String fromUid;
        private String toUid;
        private String fromName;
        private String toName;
        private boolean isFloor;
        private String content;
        private String createTime;

        private String name;

        private int floor;

        private boolean isDeleteAble;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDeleteAble() {
            return isDeleteAble;
        }

        public void setDeleteAble(boolean deleteAble) {
            isDeleteAble = deleteAble;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getAssistCount() {
            return assistCount;
        }

        public void setAssistCount(int assistCount) {
            this.assistCount = assistCount;
        }


        public String getFromName() {
            return fromName;
        }

        public String getFromUid() {
            return fromUid;
        }

        public void setFromUid(String fromUid) {
            this.fromUid = fromUid;
        }

        public String getToUid() {
            return toUid;
        }

        public void setToUid(String toUid) {
            this.toUid = toUid;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getToName() {
            return toName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }

        public boolean isIsFloor() {
            return isFloor;
        }

        public void setIsFloor(boolean isFloor) {
            this.isFloor = isFloor;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
