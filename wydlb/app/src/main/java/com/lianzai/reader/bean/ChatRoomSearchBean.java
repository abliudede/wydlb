package com.lianzai.reader.bean;

import java.util.List;

public class ChatRoomSearchBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":17,"list":[{"nickName":"幻梦","head":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","roleType":3,"accid":"3e874a95895047b0868caf5df9e30ca7"},{"nickName":"张学有","head":"https://static.lianzai.com/avatar/android_1533608461875.jpg","roleType":4,"accid":"eccfd6f29d7444e2a5658d3e04245b82"},{"nickName":"Mond","head":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI81hOdg7FFb52Xuv0JEentv3cACAibNTjpic5c6SiceRHZ2DG04iah2JhevENGY3EVf9deHfwqT3iabew/0","roleType":5,"accid":"fea00ede1c044222961deb4a9493c08a"},{"nickName":"樱木子","head":"https://static.lianzai.com/avatar/android_1537431728404.png","roleType":4,"accid":"8427cf687a904911bbec571d162b207e"},{"nickName":"哝哝君在[云巅仙缘]的昵称","head":"https://static.lianzai.com/avatar/1519377609669.png","roleType":5,"accid":"07b6aeaf090844a6b12b0639bd6c76fa"},{"nickName":"连载用户170016","head":"","roleType":5,"accid":"52b37a9eaaf34793b613f0deee0a0751"},{"nickName":"一九九","head":"","roleType":5,"accid":"798d4747e46e4d10ad67809fcd1a1d68"},{"nickName":"新","head":"https://static.lianzai.com/iOS_avatar/153794638530098.jpeg","roleType":5,"accid":"87187188d62c4ddb838ade869684c815"},{"nickName":"kkk","head":"https://static.lianzai.com/bar_post/android_1525336845098.jpg","roleType":5,"accid":"f0ad8a631b8e4843b155742e6d1002be"},{"nickName":"连载阅读575524","head":"https://static.lianzai.com/iOS_avatar/153786911744999.jpeg","roleType":5,"accid":"67bde179c014457f8a8ab7453f520991"}],"pageNum":1,"pageSize":10,"pages":2,"size":10}
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
         * total : 17
         * list : [{"nickName":"幻梦","head":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","roleType":3,"accid":"3e874a95895047b0868caf5df9e30ca7"},{"nickName":"张学有","head":"https://static.lianzai.com/avatar/android_1533608461875.jpg","roleType":4,"accid":"eccfd6f29d7444e2a5658d3e04245b82"},{"nickName":"Mond","head":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI81hOdg7FFb52Xuv0JEentv3cACAibNTjpic5c6SiceRHZ2DG04iah2JhevENGY3EVf9deHfwqT3iabew/0","roleType":5,"accid":"fea00ede1c044222961deb4a9493c08a"},{"nickName":"樱木子","head":"https://static.lianzai.com/avatar/android_1537431728404.png","roleType":4,"accid":"8427cf687a904911bbec571d162b207e"},{"nickName":"哝哝君在[云巅仙缘]的昵称","head":"https://static.lianzai.com/avatar/1519377609669.png","roleType":5,"accid":"07b6aeaf090844a6b12b0639bd6c76fa"},{"nickName":"连载用户170016","head":"","roleType":5,"accid":"52b37a9eaaf34793b613f0deee0a0751"},{"nickName":"一九九","head":"","roleType":5,"accid":"798d4747e46e4d10ad67809fcd1a1d68"},{"nickName":"新","head":"https://static.lianzai.com/iOS_avatar/153794638530098.jpeg","roleType":5,"accid":"87187188d62c4ddb838ade869684c815"},{"nickName":"kkk","head":"https://static.lianzai.com/bar_post/android_1525336845098.jpg","roleType":5,"accid":"f0ad8a631b8e4843b155742e6d1002be"},{"nickName":"连载阅读575524","head":"https://static.lianzai.com/iOS_avatar/153786911744999.jpeg","roleType":5,"accid":"67bde179c014457f8a8ab7453f520991"}]
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
             * nickName : 幻梦
             * head : https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0
             * roleType : 3
             * accid : 3e874a95895047b0868caf5df9e30ca7
             */

            private String nickName;
            private String head;
            private int roleType;
            private String accid;

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public int getRoleType() {
                return roleType;
            }

            public void setRoleType(int roleType) {
                this.roleType = roleType;
            }

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }
        }
    }
}
