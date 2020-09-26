package com.lianzai.reader.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: BookListDetailHeaderResponse
 * Author: lrz
 * Date: 2018/10/12 17:58
 * Description: ${DESCRIPTION}
 */
public class BookListDetailHeaderResponse {


    /**
     * code : 0
     * msg : success
     * data : {"id":88756,"booklistName":"妖灵位业","booklistIntro":"【专注好书三十年】\n1 《修真聊天群》望天了解一下?\n2 《赘婿》一天五更了解一下?\n3 《重生之财源滚滚》嘴炮了解一下?\n4 《东方神探九录》神探了解一下?\n5 《人道崛起》升级了解一下?\n6 《奇遇无限》仗义了解一下?\n7 《奶爸的文艺人生》奶爸了解一下?\n8 《重启世界》散财童子了解一下?\n9 《神宠进化》五毒教主了解一下?\n10《我的姐姐是大明星》德骨了解一下?\n11《我只想当一个安静的学霸》学神了解一下?\n12《黑夜玩家》幼儿园校车了解一下?\n13《前方高能》杀人了解一下?\n14《续薪火》随笔了解一下?\n15《直播之工匠大师》传统文化了解一下?\n16《明日支配者》支配未来了解一下\n17《进击的咸鱼少女》嘤嘤嘤了解一下?\n18《大王饶命》负面情绪了解一下?\n19《文娱大戏精》戏精了解一下?\n20《仙家萌喵娇养成》艹猫了解一下?\n21《深夜书屋》鬼了解一下?\n22《香爱》爱情了解一下?\n23《未来天王》百年单身狗了解一下?\n24《尘骨》断更筹盟了解一下?\n25《我被系统托管了》男主系统了解一下?\n26 《逆流纯真年代》青云门弃徒韩立了解一下?\n27《妖灵位业》每天四更了解一下？","booklistAuthor":"球球","authorCover":"https://static.lianzai.com/booklist/1.jpg","collectionNum":0,"cover":"https://static.lianzai.com/system/20181016/lz_1539653753619.jpg","isCollection":false,"tag":[{"id":33,"name":"猥琐"},{"id":34,"name":"铁血"},{"id":35,"name":"恶搞"}],"shareUrl":null}
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
         * collectionNum : 0
         * cover : https://static.lianzai.com/system/20181016/lz_1539653753619.jpg
         * isCollection : false
         * tag : [{"id":33,"name":"猥琐"},{"id":34,"name":"铁血"},{"id":35,"name":"恶搞"}]
         * shareUrl : null
         */

        private int id;
        private String booklistName;
        private String booklistIntro;
        private String booklistAuthor;
        private String authorCover;
        private int collectionNum;
        private String cover;
        private boolean isCollection;
        private boolean collectionNumShow;

        public boolean isCollectionNumShow() {
            return collectionNumShow;
        }

        public void setCollectionNumShow(boolean collectionNumShow) {
            this.collectionNumShow = collectionNumShow;
        }
        private String shareUrl;
        private List<TagBean> tag;

        private String totalNum;

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
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

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public List<TagBean> getTag() {
            return tag;
        }

        public void setTag(List<TagBean> tag) {
            this.tag = tag;
        }

        public static class TagBean {
            /**
             * id : 33
             * name : 猥琐
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
