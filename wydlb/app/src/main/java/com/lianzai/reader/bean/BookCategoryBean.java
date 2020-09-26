package com.lianzai.reader.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/7.
 */

public class BookCategoryBean implements Serializable {

    /**
     * status_code : 200
     * message : 获取小说分类成功
     * data : {"cates":[{"id":18,"p_path":"0,1","title":"仙侠","i_path":"0,1,18","count":38},{"id":142,"p_path":"0,1","title":" 短篇","i_path":"0,1,142","count":27},{"id":136,"p_path":"0,1","title":"同人","i_path":"0,1,136","count":2},{"id":70,"p_path":"0,1","title":"二次元","i_path":"0,1,70","count":14},{"id":14,"p_path":"0,1","title":"武侠","i_path":"0,1,14","count":43},{"id":64,"p_path":"0,1","title":"灵异","i_path":"0,1,64","count":29},{"id":40,"p_path":"0,1","title":"历史","i_path":"0,1,40","count":8},{"id":7,"p_path":"0,1","title":"奇幻","i_path":"0,1,7","count":40},{"id":2,"p_path":"0,1","title":"玄幻","i_path":"0,1,2","count":240},{"id":56,"p_path":"0,1","title":"科幻","i_path":"0,1,56","count":51},{"id":23,"p_path":"0,1","title":"都市","i_path":"0,1,23","count":90},{"id":52,"p_path":"0,1","title":"体育","i_path":"0,1,52","count":1},{"id":34,"p_path":"0,1","title":"军事","i_path":"0,1,34","count":4},{"id":30,"p_path":"0,1","title":"现实","i_path":"0,1,30","count":10},{"id":47,"p_path":"0,1","title":"游戏","i_path":"0,1,47","count":9},{"id":77,"p_path":"0,76","title":"古代言情","i_path":"0,76,77","count":44},{"id":152,"p_path":"0,76","title":"耽美","i_path":"0,76,152","count":47},{"id":89,"p_path":"0,76","title":"现代言情\t","i_path":"0,76,89","count":43},{"id":104,"p_path":"0,76","title":"玄幻言情","i_path":"0,76,104","count":23},{"id":111,"p_path":"0,76","title":"悬疑灵异","i_path":"0,76,111","count":14},{"id":117,"p_path":"0,76","title":"科幻空间\t","i_path":"0,76,117","count":7},{"id":125,"p_path":"0,76","title":"游戏竞技","i_path":"0,76,125","count":3},{"id":130,"p_path":"0,76","title":"N次元","i_path":"0,76,130","count":7},{"id":99,"p_path":"0,76","title":"浪漫青春","i_path":"0,76,99","count":20},{"id":84,"p_path":"0,76","title":"仙侠奇缘\t","i_path":"0,76,84","count":11}],"new_book":[{"id":913,"title":"大学BoyLove事件簿","cover":"https://static.lianzai.com/cover/book_title_1514967367_8931.png","genre_path":"0,76,152,153","type":0,"pen_name":"王子橙","is_mining":0,"is_reward":0,"is_vote":0},{"id":902,"title":"黑枭洛神传","cover":"https://static.lianzai.com/cover/book_title_1514887994_9381.png","genre_path":"0,1,2,4","type":0,"pen_name":"心衍","is_mining":0,"is_reward":0,"is_vote":0},{"id":899,"title":"重生之商女","cover":"https://static.lianzai.com/cover/book_title_1514883146_1181.png","genre_path":"0,76,89,96","type":0,"pen_name":"芸赫","is_mining":0,"is_reward":0,"is_vote":0},{"id":889,"title":"永恒之域","cover":"https://static.lianzai.com/cover//1515046298548.jpg","genre_path":"0,1,2,4","type":0,"pen_name":"望月的龙","is_mining":0,"is_reward":0,"is_vote":0},{"id":888,"title":"墨染天河","cover":"https://static.lianzai.com/cover/book_title_1514801592_9942.png","genre_path":"0,1,18,21","type":0,"pen_name":"流云映雪","is_mining":0,"is_reward":0,"is_vote":0},{"id":886,"title":"天道至尊","cover":"https://static.lianzai.com/cover/book_title_1514795940_6430.png","genre_path":"0,1,2,3","type":0,"pen_name":"田园上的一条龙","is_mining":0,"is_reward":0,"is_vote":0},{"id":884,"title":"铸神","cover":"https://static.lianzai.com/cover//1515217576818.jpg","genre_path":"0,1,2,3","type":0,"pen_name":"干戈","is_mining":0,"is_reward":0,"is_vote":0},{"id":880,"title":"魂战天涯","cover":"https://static.lianzai.com/cover//1514860662191.jpg","genre_path":"0,1,7,13","type":0,"pen_name":"三度光","is_mining":0,"is_reward":0,"is_vote":0},{"id":875,"title":"三国梦","cover":"https://static.lianzai.com/cover//1515217799275.jpg","genre_path":"0,1,40,42","type":0,"pen_name":"杀猪刀","is_mining":0,"is_reward":0,"is_vote":0},{"id":870,"title":"修真教父炼成记","cover":"https://static.lianzai.com/cover/book_title_1514563451_4592.png","genre_path":"0,1,2,4","type":0,"pen_name":"姓高名俊","is_mining":0,"is_reward":0,"is_vote":0}]}
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

    public static class DataBean implements Serializable {
        private List<CatesBean> genresList;
        private List<NewBookBean> newRecommendBookList;

        public List<CatesBean> getGenresList() {
            return genresList;
        }

        public void setGenresList(List<CatesBean> genresList) {
            this.genresList = genresList;
        }

        public List<NewBookBean> getNewRecommendBookList() {
            return newRecommendBookList;
        }

        public void setNewRecommendBookList(List<NewBookBean> newRecommendBookList) {
            this.newRecommendBookList = newRecommendBookList;
        }

        public static class CatesBean implements Serializable {
            /**
             * id : 18
             * p_path : 0,1
             * title : 仙侠
             * i_path : 0,1,18
             * count : 38
             */

            private int id;
            private String p_path;
            private String title;
            private String i_path;
            private int count;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getP_path() {
                return p_path;
            }

            public void setP_path(String p_path) {
                this.p_path = p_path;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getI_path() {
                return i_path;
            }

            public void setI_path(String i_path) {
                this.i_path = i_path;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }

        public static class NewBookBean implements Serializable {
            /**
             * id : 913
             * title : 大学BoyLove事件簿
             * cover : https://static.lianzai.com/cover/book_title_1514967367_8931.png
             * genre_path : 0,76,152,153
             * type : 0
             * pen_name : 王子橙
             * is_mining : 0
             * is_reward : 0
             * is_vote : 0
             */

            private String title;
            private String cover;
            private String bar_id;
            private String genre_path;
            private int type;
            private String pen_name;
            private int is_mining;
            private int is_reward;
            private int is_vote;

            private String novel_id;

            public String getBarId() {
                return bar_id;
            }

            public void setBarId(String barId) {
                this.bar_id = barId;
            }

            public String getNovel_id() {
                return novel_id;
            }

            public void setNovel_id(String novel_id) {
                this.novel_id = novel_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getGenre_path() {
                return genre_path;
            }

            public void setGenre_path(String genre_path) {
                this.genre_path = genre_path;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getPen_name() {
                return pen_name;
            }

            public void setPen_name(String pen_name) {
                this.pen_name = pen_name;
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
        }
    }
}
