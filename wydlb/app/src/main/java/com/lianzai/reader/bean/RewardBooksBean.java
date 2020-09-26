package com.lianzai.reader.bean;

import java.util.List;

/**
 * Created by lrz on 2018/1/30.
 */

public class RewardBooksBean {

    /**
     * status_code : 200
     * message : 获取数据成功
     * data : {"novels":[{"cover":"https://static.lianzai.com/cover//1515725485801.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"大学校园里懵懂的BL情","pen_name":"王子橙","title":"大学BoyLove事件簿","id":913,"uid":58277,"chapterCount":7,"chapterWordNum":"1497","followCount":18,"core":0},{"cover":"https://static.lianzai.com/cover//1515059611511.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"标签：＃无敌爽文＃＃装逼＃＃天才流＃＃系统流＃＃主宰流＃正式版简介：当这个修武的世界慢慢转变为修真世界后，在人类看到长生的曙光时，秦风早已经在众生的背后主宰着这一切\u2026\u2026","pen_name":"二十一","title":"全民修真","id":829,"uid":39071,"chapterCount":7,"chapterWordNum":"2005","followCount":90,"core":0},{"cover":"https://static.lianzai.com/cover//1513236139947.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"乱弹大侠，江湖就在现在！","pen_name":"不屈狼君","title":"乱弹","id":528,"uid":30804,"chapterCount":7,"chapterWordNum":"2805","followCount":88,"core":0},{"cover":"https://static.lianzai.com/cover/book_title_1513087835_9055.png","is_mining":1,"is_reward":1,"is_vote":0,"intro":"人类从何而来？又将驶向何方？奋起抵抗是为了生存，还是为了揭露这不可告人的秘密？人类中的外星人，造物者的落寞。一切，可能都只是谎言","pen_name":"鲜血很美丽","title":"创世者：神之序章","id":615,"uid":40382,"chapterCount":6,"chapterWordNum":"3067","followCount":91,"core":0},{"cover":"https://static.lianzai.com/cover//1514515943809.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"人生在世，逍遥当道。只需要你足够大胆，只需要你有两条腿到达逍遥窟，那么世间所有的人生百味，你尽情品尝。","pen_name":"婉溪清扬","title":"逍遥窟里的僵尸","id":675,"uid":39637,"chapterCount":7,"chapterWordNum":"2280","followCount":95,"core":0},{"cover":"https://static.lianzai.com/cover//1516613717186.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"桃花村里桃花树，桃花树下桃花人，桃花人里有个仙，人人喊他桃成仙。今日桃仙来桃镇，坐了轿车去南城。桃成仙来到南城不是为别的，就是为了开开眼界，顺带求学。","pen_name":"龙行天下","title":"风流桃成仙","id":939,"uid":60144,"chapterCount":7,"chapterWordNum":"1512","followCount":102,"core":0},{"cover":"https://static.lianzai.com/cover//1515549869342.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"林芸赫年少无知的时候，总是在想，她心中的盖世英雄到底来着到底来着白车开是黑车来救她于苦难只中，然后后来，她长大了，才知道她想多了...\u2026【连载外联编辑道鹍所邀驻站】","pen_name":"芸赫","title":"重生之商女","id":899,"uid":55150,"chapterCount":21,"chapterWordNum":"3123","followCount":184,"core":0},{"cover":"https://static.lianzai.com/cover//1516612420762.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"担任位面裁决官的陈渊在内战中落败，待他醒来发现自己已经来到了一个陌生的位面。而在这个位面，陈渊决心先恢复伤势在积攒实力，最终反杀回裁决位面！","pen_name":"圣女贞德的绯闻男友","title":"位面最强裁决官","id":669,"uid":36937,"chapterCount":7,"chapterWordNum":"2263","followCount":155,"core":0},{"cover":"https://static.lianzai.com/cover//1510897750269.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"一场噩梦，让她的每一处伤疤，都透着狰狞和鲜血； 一段旧恨，让她失去了哥哥、妹妹、未婚夫、生死兄弟； 一种命运，让她越来越感觉到自己的渺小； 一个意外，让她对某些执念，永远无法释怀。 人生若只如初见，当时只道是寻常。年龄增长的唯一好处就在于，人终于欣然接受，自己的快乐与痛苦，可以跟全世界都不一样。","pen_name":"格桑花","title":"墨色染卿","id":277,"uid":37103,"chapterCount":6,"chapterWordNum":"3704","followCount":90,"core":0}],"needClear":0,"localData":[913,829,528,615,675,939,899,669,277]}
     */

    private int status_code;
    private String message;
    private DataBean data;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * novels : [{"cover":"https://static.lianzai.com/cover//1515725485801.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"大学校园里懵懂的BL情","pen_name":"王子橙","title":"大学BoyLove事件簿","id":913,"uid":58277,"chapterCount":7,"chapterWordNum":"1497","followCount":18,"core":0},{"cover":"https://static.lianzai.com/cover//1515059611511.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"标签：＃无敌爽文＃＃装逼＃＃天才流＃＃系统流＃＃主宰流＃正式版简介：当这个修武的世界慢慢转变为修真世界后，在人类看到长生的曙光时，秦风早已经在众生的背后主宰着这一切\u2026\u2026","pen_name":"二十一","title":"全民修真","id":829,"uid":39071,"chapterCount":7,"chapterWordNum":"2005","followCount":90,"core":0},{"cover":"https://static.lianzai.com/cover//1513236139947.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"乱弹大侠，江湖就在现在！","pen_name":"不屈狼君","title":"乱弹","id":528,"uid":30804,"chapterCount":7,"chapterWordNum":"2805","followCount":88,"core":0},{"cover":"https://static.lianzai.com/cover/book_title_1513087835_9055.png","is_mining":1,"is_reward":1,"is_vote":0,"intro":"人类从何而来？又将驶向何方？奋起抵抗是为了生存，还是为了揭露这不可告人的秘密？人类中的外星人，造物者的落寞。一切，可能都只是谎言","pen_name":"鲜血很美丽","title":"创世者：神之序章","id":615,"uid":40382,"chapterCount":6,"chapterWordNum":"3067","followCount":91,"core":0},{"cover":"https://static.lianzai.com/cover//1514515943809.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"人生在世，逍遥当道。只需要你足够大胆，只需要你有两条腿到达逍遥窟，那么世间所有的人生百味，你尽情品尝。","pen_name":"婉溪清扬","title":"逍遥窟里的僵尸","id":675,"uid":39637,"chapterCount":7,"chapterWordNum":"2280","followCount":95,"core":0},{"cover":"https://static.lianzai.com/cover//1516613717186.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"桃花村里桃花树，桃花树下桃花人，桃花人里有个仙，人人喊他桃成仙。今日桃仙来桃镇，坐了轿车去南城。桃成仙来到南城不是为别的，就是为了开开眼界，顺带求学。","pen_name":"龙行天下","title":"风流桃成仙","id":939,"uid":60144,"chapterCount":7,"chapterWordNum":"1512","followCount":102,"core":0},{"cover":"https://static.lianzai.com/cover//1515549869342.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"林芸赫年少无知的时候，总是在想，她心中的盖世英雄到底来着到底来着白车开是黑车来救她于苦难只中，然后后来，她长大了，才知道她想多了...\u2026【连载外联编辑道鹍所邀驻站】","pen_name":"芸赫","title":"重生之商女","id":899,"uid":55150,"chapterCount":21,"chapterWordNum":"3123","followCount":184,"core":0},{"cover":"https://static.lianzai.com/cover//1516612420762.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"担任位面裁决官的陈渊在内战中落败，待他醒来发现自己已经来到了一个陌生的位面。而在这个位面，陈渊决心先恢复伤势在积攒实力，最终反杀回裁决位面！","pen_name":"圣女贞德的绯闻男友","title":"位面最强裁决官","id":669,"uid":36937,"chapterCount":7,"chapterWordNum":"2263","followCount":155,"core":0},{"cover":"https://static.lianzai.com/cover//1510897750269.jpg","is_mining":1,"is_reward":1,"is_vote":0,"intro":"一场噩梦，让她的每一处伤疤，都透着狰狞和鲜血； 一段旧恨，让她失去了哥哥、妹妹、未婚夫、生死兄弟； 一种命运，让她越来越感觉到自己的渺小； 一个意外，让她对某些执念，永远无法释怀。 人生若只如初见，当时只道是寻常。年龄增长的唯一好处就在于，人终于欣然接受，自己的快乐与痛苦，可以跟全世界都不一样。","pen_name":"格桑花","title":"墨色染卿","id":277,"uid":37103,"chapterCount":6,"chapterWordNum":"3704","followCount":90,"core":0}]
         * needClear : 0
         * localData : [913,829,528,615,675,939,899,669,277]
         */

        private int needClear;
        private List<NovelsBean> novels;
        private List<Integer> localData;

        public int getNeedClear() {
            return needClear;
        }

        public void setNeedClear(int needClear) {
            this.needClear = needClear;
        }

        public List<NovelsBean> getNovels() {
            return novels;
        }

        public void setNovels(List<NovelsBean> novels) {
            this.novels = novels;
        }

        public List<Integer> getLocalData() {
            return localData;
        }

        public void setLocalData(List<Integer> localData) {
            this.localData = localData;
        }

        public static class NovelsBean {
            /**
             * cover : https://static.lianzai.com/cover//1515725485801.jpg
             * is_mining : 1
             * is_reward : 1
             * is_vote : 0
             * intro : 大学校园里懵懂的BL情
             * pen_name : 王子橙
             * title : 大学BoyLove事件簿
             * id : 913
             * uid : 58277
             * chapterCount : 7
             * chapterWordNum : 1497
             * followCount : 18
             * core : 0
             */

            private String cover;
            private int is_mining;
            private int is_reward;
            private int is_vote;
            private String intro;
            private String pen_name;
            private String title;
            private int id;
            private int uid;
            private int chapterCount;
            private String chapterWordNum;
            private int followCount;
            private int core;

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getIs_mining() {
                return is_mining;
            }

            public void setIs_mining(int is_mining) {
                this.is_mining = is_mining;
            }

            public int getIs_reward() {
                return is_reward;
            }

            public void setIs_reward(int is_reward) {
                this.is_reward = is_reward;
            }

            public int getIs_vote() {
                return is_vote;
            }

            public void setIs_vote(int is_vote) {
                this.is_vote = is_vote;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getPen_name() {
                return pen_name;
            }

            public void setPen_name(String pen_name) {
                this.pen_name = pen_name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getChapterCount() {
                return chapterCount;
            }

            public void setChapterCount(int chapterCount) {
                this.chapterCount = chapterCount;
            }

            public String getChapterWordNum() {
                return chapterWordNum;
            }

            public void setChapterWordNum(String chapterWordNum) {
                this.chapterWordNum = chapterWordNum;
            }

            public int getFollowCount() {
                return followCount;
            }

            public void setFollowCount(int followCount) {
                this.followCount = followCount;
            }

            public int getCore() {
                return core;
            }

            public void setCore(int core) {
                this.core = core;
            }
        }
    }
}
