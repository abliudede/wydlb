package com.lianzai.reader.bean;

import java.util.List;

public class ShareBitmapBean {


    /**
     * code : 0
     * msg : success
     * data : {"userId":496903,"nickName":"皮皮猪","headImg":"https://static.lianzai.com/avatar/android_1555555641164.gif","description":"推荐你阅读这本小说","url":"http://fx.bendixing.net/taskCenter/#/transfer?type=dO1rwjDQ&id=KZKrA7jr&uid=AVWl5Ln1&ext=yunlaige.com","extendField":"复制整段信息，打开手机『连载神器』，即可免费阅读《小世界其乐无穷》小说:≡KjdG6w1k≡『连载口令』","bookInfo":{"bookId":4634,"bookName":"小世界其乐无穷","authorName":"听日","cover":"https://qidian.qpic.cn/qdbimg/349573/1011452759/180","bookIntroduce":"超凡者打破了世界的寂静，科技树从此拐弯。当人类在黑暗中寻找进化的道路，我拆下肋骨，燃烧心脏，熬夜爆肝，成为他们的先驱。这是一个玩家玩弄世界的游戏历程。小世界，其乐无穷。"},"bookListInfo":null,"personalInfo":null,"circleInfo":null,"activityInfo":null,"indexInfo":null,"loginInfo":null,"groupChatInfo":null,"bookShelfInfo":null}
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
         * userId : 496903
         * nickName : 皮皮猪
         * headImg : https://static.lianzai.com/avatar/android_1555555641164.gif
         * description : 推荐你阅读这本小说
         * url : http://fx.bendixing.net/taskCenter/#/transfer?type=dO1rwjDQ&id=KZKrA7jr&uid=AVWl5Ln1&ext=yunlaige.com
         * extendField : 复制整段信息，打开手机『连载神器』，即可免费阅读《小世界其乐无穷》小说:≡KjdG6w1k≡『连载口令』
         * bookInfo : {"bookId":4634,"bookName":"小世界其乐无穷","authorName":"听日","cover":"https://qidian.qpic.cn/qdbimg/349573/1011452759/180","bookIntroduce":"超凡者打破了世界的寂静，科技树从此拐弯。当人类在黑暗中寻找进化的道路，我拆下肋骨，燃烧心脏，熬夜爆肝，成为他们的先驱。这是一个玩家玩弄世界的游戏历程。小世界，其乐无穷。"}
         * bookListInfo : null
         * personalInfo : null
         * circleInfo : null
         * activityInfo : null
         * indexInfo : null
         * loginInfo : null
         * groupChatInfo : null
         * bookShelfInfo : null
         */

        private int userId;
        private String nickName;
        private String headImg;
        private String description;
        private String url;
        private String extendField;
        private BookInfoBean bookInfo;
        private BookListInfoBean bookListInfo;
        private PersonalInfoBean personalInfo;
        private CircleInfoBean circleInfo;
        private Object activityInfo;
        private Object indexInfo;
        private Object loginInfo;
        private Object groupChatInfo;
        private Object bookShelfInfo;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getExtendField() {
            return extendField;
        }

        public void setExtendField(String extendField) {
            this.extendField = extendField;
        }

        public BookInfoBean getBookInfo() {
            return bookInfo;
        }

        public void setBookInfo(BookInfoBean bookInfo) {
            this.bookInfo = bookInfo;
        }

        public BookListInfoBean getBookListInfo() {
            return bookListInfo;
        }

        public void setBookListInfo(BookListInfoBean bookListInfo) {
            this.bookListInfo = bookListInfo;
        }

        public PersonalInfoBean getPersonalInfo() {
            return personalInfo;
        }

        public void setPersonalInfo(PersonalInfoBean personalInfo) {
            this.personalInfo = personalInfo;
        }

        public CircleInfoBean getCircleInfo() {
            return circleInfo;
        }

        public void setCircleInfo(CircleInfoBean circleInfo) {
            this.circleInfo = circleInfo;
        }

        public Object getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(Object activityInfo) {
            this.activityInfo = activityInfo;
        }

        public Object getIndexInfo() {
            return indexInfo;
        }

        public void setIndexInfo(Object indexInfo) {
            this.indexInfo = indexInfo;
        }

        public Object getLoginInfo() {
            return loginInfo;
        }

        public void setLoginInfo(Object loginInfo) {
            this.loginInfo = loginInfo;
        }

        public Object getGroupChatInfo() {
            return groupChatInfo;
        }

        public void setGroupChatInfo(Object groupChatInfo) {
            this.groupChatInfo = groupChatInfo;
        }

        public Object getBookShelfInfo() {
            return bookShelfInfo;
        }

        public void setBookShelfInfo(Object bookShelfInfo) {
            this.bookShelfInfo = bookShelfInfo;
        }

        public static class BookListInfoBean{


            /**
             * id : 88350
             * booklistName : 【新人书单】女频分类小说，专注古言和现言
             * booklistAuthor : 希哩哩
             * totalNum : 17
             * counterSignBooklistSonList : [{"bookTitle":"全职高手","platformCover":"https://qidian.qpic.cn/qdbimg/349573/1887208/180","score":0,"bookAuthor":"蝴蝶蓝"},{"bookTitle":"美食供应商","platformCover":"https://qidian.qpic.cn/qdbimg/349573/1003667321/180","score":0,"bookAuthor":"会做菜的猫"},{"bookTitle":"凡人修仙传","platformCover":"https://qidian.qpic.cn/qdbimg/349573/107580/180","score":0,"bookAuthor":"忘语"},{"bookTitle":"艾若的红楼生活","score":0,"bookAuthor":"leidewen"},{"bookTitle":"娇娘医经","score":0,"bookAuthor":"希行"},{"bookTitle":"重生之药香","score":0,"bookAuthor":"希行"}]
             */

            private int id;
            private String booklistName;
            private String booklistAuthor;
            private int totalNum;
            private List<CounterSignBooklistSonListBean> counterSignBooklistSonList;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getBooklistName() {
                return booklistName;
            }

            public void setBooklistName(String booklistName) {
                this.booklistName = booklistName;
            }

            public String getBooklistAuthor() {
                return booklistAuthor;
            }

            public void setBooklistAuthor(String booklistAuthor) {
                this.booklistAuthor = booklistAuthor;
            }

            public int getTotalNum() {
                return totalNum;
            }

            public void setTotalNum(int totalNum) {
                this.totalNum = totalNum;
            }

            public List<CounterSignBooklistSonListBean> getCounterSignBooklistSonList() {
                return counterSignBooklistSonList;
            }

            public void setCounterSignBooklistSonList(List<CounterSignBooklistSonListBean> counterSignBooklistSonList) {
                this.counterSignBooklistSonList = counterSignBooklistSonList;
            }

            public static class CounterSignBooklistSonListBean {
                /**
                 * bookTitle : 全职高手
                 * platformCover : https://qidian.qpic.cn/qdbimg/349573/1887208/180
                 * score : 0
                 * bookAuthor : 蝴蝶蓝
                 */

                private String bookTitle;
                private String platformCover;
                private int score;
                private String bookAuthor;
                private String platformIntroduce;

                public String getPlatformIntroduce() {
                    return platformIntroduce;
                }

                public void setPlatformIntroduce(String platformIntroduce) {
                    this.platformIntroduce = platformIntroduce;
                }

                public String getBookTitle() {
                    return bookTitle;
                }

                public void setBookTitle(String bookTitle) {
                    this.bookTitle = bookTitle;
                }

                public String getPlatformCover() {
                    return platformCover;
                }

                public void setPlatformCover(String platformCover) {
                    this.platformCover = platformCover;
                }

                public int getScore() {
                    return score;
                }

                public void setScore(int score) {
                    this.score = score;
                }

                public String getBookAuthor() {
                    return bookAuthor;
                }

                public void setBookAuthor(String bookAuthor) {
                    this.bookAuthor = bookAuthor;
                }
            }
        }

        public static class PersonalInfoBean{

            /**
             * uid : 476370
             * head : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJYDTX98iaDyL0ZvZMFW7QBVXThzedUDZNjibPPEHuFzLJJNzEMHVvzLJJn3KrkLm0wMbQtHX7Guiauw/132
             * nickName : 快乐我来订
             * day : 447
             * introduce : 我就是我，不一样的烟火。
             * minute : 0
             */

            private int uid;
            private String head;
            private String nickName;
            private int day;
            private String introduce;
            private int minute;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public int getMinute() {
                return minute;
            }

            public void setMinute(int minute) {
                this.minute = minute;
            }
        }

        public static class CircleInfoBean{

            /**
             * id : 10002
             * platformType : 2
             * platformCover : https://static.lianzai.com/updatecover/7.jpg?W75XH100
             * platformName : 傲战天穹
             * memberCount : 37
             * headList : ["http://wx.qlogo.cn/mmopen/vi_32/uHskAsnK1e3B4yf3BJ0yAKsiciaedlTibzl9SHTewOlwDIGm4T0Lo7ETW5b540iczIVhZM7CHLNGmtEkkd09bQVXxA/132","http://wx.qlogo.cn/mmopen/vi_32/BF6K1pR632WQ9ZK3R5e1JKQBb1EibjTFOwBuP6KGOOlodibwIwJcEx2CRo3oibZkyRfCC5I24FXJwHiaFvBbfNwd1A/132","http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJEQ6FkGMjPp2I003nuo2hiccsb1Nhy3zeicOYrbY7Y5DpbRicIZTqhPTlUSibC8fj7Zvkla0WcfJ7Slw/132","http://wx.qlogo.cn/mmopen/vi_32/LfW08IhQO2Iht5QXzic4KcYh5RfRsmkw8wPFcu92I80HTmUbNvcPb7L4r7FGd1xXyXCUksk195PCaPiaicVCAAQKQ/132","http://wx.qlogo.cn/mmopen/vi_32/BF6K1pR632WQ9ZK3R5e1JPzcJe0Mllx1OWDicCLCl4Bb1DprKSlGP0NZzkHoFTvwtuDPSKoWuRcK77KVSAYhssA/132"]
             */

            private int id;
            private int platformType;
            private String platformCover;
            private String platformName;
            private int memberCount;
            private List<String> headList;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPlatformType() {
                return platformType;
            }

            public void setPlatformType(int platformType) {
                this.platformType = platformType;
            }

            public String getPlatformCover() {
                return platformCover;
            }

            public void setPlatformCover(String platformCover) {
                this.platformCover = platformCover;
            }

            public String getPlatformName() {
                return platformName;
            }

            public void setPlatformName(String platformName) {
                this.platformName = platformName;
            }

            public int getMemberCount() {
                return memberCount;
            }

            public void setMemberCount(int memberCount) {
                this.memberCount = memberCount;
            }

            public List<String> getHeadList() {
                return headList;
            }

            public void setHeadList(List<String> headList) {
                this.headList = headList;
            }
        }

        public static class BookInfoBean {
            /**
             * bookId : 4634
             * bookName : 小世界其乐无穷
             * authorName : 听日
             * cover : https://qidian.qpic.cn/qdbimg/349573/1011452759/180
             * bookIntroduce : 超凡者打破了世界的寂静，科技树从此拐弯。当人类在黑暗中寻找进化的道路，我拆下肋骨，燃烧心脏，熬夜爆肝，成为他们的先驱。这是一个玩家玩弄世界的游戏历程。小世界，其乐无穷。
             */

            private int bookId;
            private String bookName;
            private String authorName;
            private String cover;
            private String bookIntroduce;

            public int getBookId() {
                return bookId;
            }

            public void setBookId(int bookId) {
                this.bookId = bookId;
            }

            public String getBookName() {
                return bookName;
            }

            public void setBookName(String bookName) {
                this.bookName = bookName;
            }

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getBookIntroduce() {
                return bookIntroduce;
            }

            public void setBookIntroduce(String bookIntroduce) {
                this.bookIntroduce = bookIntroduce;
            }
        }
    }
}
