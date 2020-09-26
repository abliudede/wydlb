package com.lianzai.reader.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: IntegrateSearchResponse
 * Author: lrz
 * Date: 2018/10/19 15:47
 * Description: 综合搜索
 */
public class IntegrateSearchResponse {


    /**
     * code : 0
     * msg : success
     * data : {"booklist":[{"id":88756,"booklistName":"妖灵位业","booklistIntro":"【专注好书三十年】\n1 《修真聊天群》望天了解一下?\n2 《赘婿》一天五更了解一下?\n3 《重生之财源滚滚》嘴炮了解一下?\n4 《东方神探九录》神探了解一下?\n5 《人道崛起》升级了解一下?\n6 《奇遇无限》仗义了解一下?\n7 《奶爸的文艺人生》奶爸了解一下?\n8 《重启世界》散财童子了解一下?\n9 《神宠进化》五毒教主了解一下?\n10《我的姐姐是大明星》德骨了解一下?\n11《我只想当一个安静的学霸》学神了解一下?\n12《黑夜玩家》幼儿园校车了解一下?\n13《前方高能》杀人了解一下?\n14《续薪火》随笔了解一下?\n15《直播之工匠大师》传统文化了解一下?\n16《明日支配者》支配未来了解一下\n17《进击的咸鱼少女》嘤嘤嘤了解一下?\n18《大王饶命》负面情绪了解一下?\n19《文娱大戏精》戏精了解一下?\n20《仙家萌喵娇养成》艹猫了解一下?\n21《深夜书屋》鬼了解一下?\n22《香爱》爱情了解一下?\n23《未来天王》百年单身狗了解一下?\n24《尘骨》断更筹盟了解一下?\n25《我被系统托管了》男主系统了解一下?\n26 《逆流纯真年代》青云门弃徒韩立了解一下?\n27《妖灵位业》每天四更了解一下？","booklistAuthor":"球球","authorCover":"https://static.lianzai.com/booklist/1.jpg","collectionNum":null,"collectionNumShow":true,"cover":"https://static.lianzai.com/system/20181016/lz_1539653753619.jpg","isCollection":false,"tag":null,"shareUrl":null,"totalNum":null,"isUnread":null}],"platform":[{"bookId":407083,"platformId":388054,"platformType":3,"platformName":"机侠","platformCover":"https://qidian.qpic.cn/qdbimg/349573/1678657/180","penName":"机侠","platformIntroduce":"属于机侠的世界，巅峰科技文明的世界新的思路，新的剧情，新的小说类别机侠，坚韧的成长而我，也在坚韧的成长我思来想去还是不写主角背景垃圾，天资垃圾了那只是跟风作品显现自己特别的手段吧还是写的比较正常一点吧，毕竟天资好了，才能更强","isUnread":null,"isConcern":null,"yxAccid":"b1e00f31e104409ba0c108af9e07bc8b","shareUrl":null,"source":"qidian.com","categoryName":null},{"bookId":399792,"platformId":383606,"platformType":3,"platformName":"机徒","platformCover":"https://qidian.qpic.cn/qdbimg/349573/1784431/180","penName":"机徒","platformIntroduce":"\u201c我，名为影宁\u2026\u2026我只想知道，这个宇宙的极限到底在那里\u2026\u2026\u201d这句话摘自人类史上最伟大机师回忆录第一册第一页，而这位机师从来都没有承认他已经找到了所谓的极限。他说：\u201c我不知道所谓的极限到底在那里，我只知道，在寻找极限的道路上，我有着太多东西需要学习，所以我永远都只是一个学徒\u2026\u2026\u201d","isUnread":null,"isConcern":null,"yxAccid":"287b383932e84463a733cdecbe276811","shareUrl":null,"source":"qidian.com","categoryName":null},{"bookId":287708,"platformId":295607,"platformType":3,"platformName":"机器危机","platformCover":"https://qidian.qpic.cn/qdbimg/349573/2925256/180","penName":"人狼互变兽","platformIntroduce":"2067年，位于美国科罗拉多州的罗拉特实验大楼。一向高明的美国人在实验自己发明的全新机器人－－－－H356，有着人类的智慧，全新的武器系统。就在测试它的武器系统如何时，H356把自己手上的O9机枪的枪口移向了监控实验室。然后机器人的变革开始了。(本书不在更新。本书章节为\u201c乱码\u201d)","isUnread":null,"isConcern":null,"yxAccid":"6ef79464032147bab14180c0ebf87b74","shareUrl":null,"source":"qidian.com","categoryName":null}]}
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
        private List<BooklistBean> booklist;
        private List<PlatformBean> platform;

        public List<BooklistBean> getBooklist() {
            return booklist;
        }

        public void setBooklist(List<BooklistBean> booklist) {
            this.booklist = booklist;
        }

        public List<PlatformBean> getPlatform() {
            return platform;
        }

        public void setPlatform(List<PlatformBean> platform) {
            this.platform = platform;
        }

        public static class BooklistBean {
            /**
             * id : 88756
             * booklistName : 妖灵位业
             * booklistIntro : 【专注好书三十年】
             1 《修真聊天群》望天了解一下?
             2 《赘婿》一天五更了解一下?
             3 《重生之财源滚滚》嘴炮了解一下?
             4 《东方神探九录》神探了解一下?
             5 《人道崛起》升级了解一下?
             6 《奇遇无限》仗义了解一下?
             7 《奶爸的文艺人生》奶爸了解一下?
             8 《重启世界》散财童子了解一下?
             9 《神宠进化》五毒教主了解一下?
             10《我的姐姐是大明星》德骨了解一下?
             11《我只想当一个安静的学霸》学神了解一下?
             12《黑夜玩家》幼儿园校车了解一下?
             13《前方高能》杀人了解一下?
             14《续薪火》随笔了解一下?
             15《直播之工匠大师》传统文化了解一下?
             16《明日支配者》支配未来了解一下
             17《进击的咸鱼少女》嘤嘤嘤了解一下?
             18《大王饶命》负面情绪了解一下?
             19《文娱大戏精》戏精了解一下?
             20《仙家萌喵娇养成》艹猫了解一下?
             21《深夜书屋》鬼了解一下?
             22《香爱》爱情了解一下?
             23《未来天王》百年单身狗了解一下?
             24《尘骨》断更筹盟了解一下?
             25《我被系统托管了》男主系统了解一下?
             26 《逆流纯真年代》青云门弃徒韩立了解一下?
             27《妖灵位业》每天四更了解一下？
             * booklistAuthor : 球球
             * authorCover : https://static.lianzai.com/booklist/1.jpg
             * collectionNum : null
             * collectionNumShow : true
             * cover : https://static.lianzai.com/system/20181016/lz_1539653753619.jpg
             * isCollection : false
             * tag : null
             * shareUrl : null
             * totalNum : null
             * isUnread : null
             */

            private int id;
            private String booklistName;
            private String booklistIntro;
            private String booklistAuthor;
            private String authorCover;
            private int collectionNum;
            private boolean collectionNumShow;
            private String cover;
            private boolean isCollection;
            private Object tag;
            private Object shareUrl;
            private Object totalNum;
            private boolean isUnread;

            private String platformName;

            public String getPlatformName() {
                return platformName;
            }

            public void setPlatformName(String platformName) {
                this.platformName = platformName;
            }

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

            public String getBooklistIntro() {
                return booklistIntro;
            }

            public void setBooklistIntro(String booklistIntro) {
                this.booklistIntro = booklistIntro;
            }

            public String getBooklistAuthor() {
                return booklistAuthor;
            }

            public void setBooklistAuthor(String booklistAuthor) {
                this.booklistAuthor = booklistAuthor;
            }

            public String getAuthorCover() {
                return authorCover;
            }

            public void setAuthorCover(String authorCover) {
                this.authorCover = authorCover;
            }

            public int getCollectionNum() {
                return collectionNum;
            }

            public void setCollectionNum(int collectionNum) {
                this.collectionNum = collectionNum;
            }

            public boolean isCollectionNumShow() {
                return collectionNumShow;
            }

            public void setCollectionNumShow(boolean collectionNumShow) {
                this.collectionNumShow = collectionNumShow;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public boolean isIsCollection() {
                return isCollection;
            }

            public void setIsCollection(boolean isCollection) {
                this.isCollection = isCollection;
            }

            public Object getTag() {
                return tag;
            }

            public void setTag(Object tag) {
                this.tag = tag;
            }

            public Object getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(Object shareUrl) {
                this.shareUrl = shareUrl;
            }

            public Object getTotalNum() {
                return totalNum;
            }

            public void setTotalNum(Object totalNum) {
                this.totalNum = totalNum;
            }

            public boolean getIsUnread() {
                return isUnread;
            }

            public void setIsUnread(boolean isUnread) {
                this.isUnread = isUnread;
            }
        }

        public static class PlatformBean {
            /**
             * bookId : 407083
             * platformId : 388054
             * platformType : 3
             * platformName : 机侠
             * platformCover : https://qidian.qpic.cn/qdbimg/349573/1678657/180
             * penName : 机侠
             * platformIntroduce : 属于机侠的世界，巅峰科技文明的世界新的思路，新的剧情，新的小说类别机侠，坚韧的成长而我，也在坚韧的成长我思来想去还是不写主角背景垃圾，天资垃圾了那只是跟风作品显现自己特别的手段吧还是写的比较正常一点吧，毕竟天资好了，才能更强
             * isUnread : null
             * isConcern : null
             * yxAccid : b1e00f31e104409ba0c108af9e07bc8b
             * shareUrl : null
             * source : qidian.com
             * categoryName : null
             */

            private int bookId;
            private int platformId;
            private int platformType;
            private String platformName;
            private String platformCover;
            private String penName;
            private String platformIntroduce;
            private Object isUnread;
            private boolean isConcern;
            private String yxAccid;
            private Object shareUrl;
            private String source;
            private Object categoryName;
            private boolean isCopyright;

            public boolean isCopyright() {
                return isCopyright;
            }

            public void setCopyright(boolean copyright) {
                isCopyright = copyright;
            }

            public int getBookId() {
                return bookId;
            }

            public void setBookId(int bookId) {
                this.bookId = bookId;
            }

            public int getPlatformId() {
                return platformId;
            }

            public void setPlatformId(int platformId) {
                this.platformId = platformId;
            }

            public int getPlatformType() {
                return platformType;
            }

            public void setPlatformType(int platformType) {
                this.platformType = platformType;
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

            public String getPenName() {
                return penName;
            }

            public void setPenName(String penName) {
                this.penName = penName;
            }

            public String getPlatformIntroduce() {
                return platformIntroduce;
            }

            public void setPlatformIntroduce(String platformIntroduce) {
                this.platformIntroduce = platformIntroduce;
            }

            public Object getIsUnread() {
                return isUnread;
            }

            public void setIsUnread(Object isUnread) {
                this.isUnread = isUnread;
            }

            public boolean getIsConcern() {
                return isConcern;
            }

            public void setIsConcern(boolean isConcern) {
                this.isConcern = isConcern;
            }

            public String getYxAccid() {
                return yxAccid;
            }

            public void setYxAccid(String yxAccid) {
                this.yxAccid = yxAccid;
            }

            public Object getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(Object shareUrl) {
                this.shareUrl = shareUrl;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public Object getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(Object categoryName) {
                this.categoryName = categoryName;
            }
        }
    }
}
