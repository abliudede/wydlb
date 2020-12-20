package com.wydlb.first.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lrz on 2018/1/24.
 */

public class HotSearchBean implements Serializable{


    /**
     * status_code : 200
     * message : 获取数据成功
     * data : [{"id":89,"uid":36900,"source_link":"www.lianzai.com","title":"玉皇大帝之少年行","cover":"http://xccover.b0.upaiyun.com/1509507421427.jpg","intro":"一粒沙石一世界，一个修真一宇宙。舍三千二百生，历亿万无量劫，看草莽少年起于微末，逆转生死，在万族林立，群雄并起，诸皇争霸的大时代，挥斧斩妖魔，驱异族，开天地，成大道，从孤儿奋斗成寰宇第一人！（QQ交流群434805723，欢迎大家加入。）","price":"0.00","word_num":309859,"fav_num":4113,"genre_path":"0,1,18,22","status":1,"is_mining":1,"is_reward":1,"is_vote":0,"type":0,"vip_level":0,"vip_cer":0,"shelf_time":"2017-10-21 13:00:47","create_time":"2017-10-21 13:00:47","update_time":"2018-01-24 09:35:39","deleted_at":null,"pen_name":"泡杯大红袍","click_count":3125},{"id":30,"uid":17025,"source_link":"www.lianzai.com","title":"巅峰转世","cover":"https://static.lianzai.com/cover//1513085569630.JPG","intro":"作为曾经的巅峰，他死过。今生作为转世者，刚一清醒便遇到了，童养媳？为了重踏巅峰之路，获得风云剑，手持神兵，在脑海中多出了一本仅限于传说的功法。刚一触碰功法，便引来神秘声音，这功法，是逆转还是正转，逆转之后，突破之时伴随生死之劫。今生，必重踏巅峰之路，前世死于哪里，今生就屠他个血雨腥风！","price":"0.00","word_num":199519,"fav_num":3023,"genre_path":"0,1,2,4","status":1,"is_mining":1,"is_reward":1,"is_vote":0,"type":0,"vip_level":0,"vip_cer":0,"shelf_time":"2017-10-18 12:03:59","create_time":"2017-10-18 11:55:21","update_time":"2018-01-24 09:36:49","deleted_at":null,"pen_name":"寒灵梦","click_count":4018},{"id":400,"uid":16777,"source_link":"www.lianzai.com","title":"梦回开天","cover":"https://static.lianzai.com/cover//1514877964875.jpg","intro":"有梦敢想，那就叫梦想，成真只是时间的问题。而梦却成了我这一生的传奇，直到最后神马都是浮云。盘古为我开天，女娲为我补天，如来与三清视我为道友，三皇只是我的门人。","price":"0.00","word_num":168073,"fav_num":2602,"genre_path":"0,1,23,26","status":1,"is_mining":1,"is_reward":1,"is_vote":0,"type":0,"vip_level":0,"vip_cer":0,"shelf_time":"2017-11-28 22:30:42","create_time":"2017-11-27 08:51:25","update_time":"2018-01-24 10:15:34","deleted_at":null,"pen_name":"夜与非","click_count":1875},{"id":55,"uid":12737,"source_link":"www.lianzai.com","title":"云巅仙缘","cover":"https://static.lianzai.com/cover//1511588681429.jpg","intro":"对沈云来说，生存是第一位的，如何没有威胁的生存，更是他的向往。 然而，就在他寻求更好的生存时，变故发生了，一切由命运之手推动，如历史车轮般滚滚向前。从此踏入一条全新的世界，一个古老又神秘的修仙世界，徐徐展开。群魔乱舞，一式定乾坤，族群大战，一枪退万敌！一切源于生存！","price":"0.00","word_num":274486,"fav_num":2188,"genre_path":"0,1,18,21","status":1,"is_mining":1,"is_reward":1,"is_vote":0,"type":0,"vip_level":0,"vip_cer":0,"shelf_time":"2017-10-18 20:18:11","create_time":"2017-10-18 20:18:11","update_time":"2018-01-24 09:33:16","deleted_at":null,"pen_name":"幻梦","click_count":5614}]
     */

    private int status_code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 89
         * uid : 36900
         * source_link : www.lianzai.com
         * title : 玉皇大帝之少年行
         * cover : http://xccover.b0.upaiyun.com/1509507421427.jpg
         * intro : 一粒沙石一世界，一个修真一宇宙。舍三千二百生，历亿万无量劫，看草莽少年起于微末，逆转生死，在万族林立，群雄并起，诸皇争霸的大时代，挥斧斩妖魔，驱异族，开天地，成大道，从孤儿奋斗成寰宇第一人！（QQ交流群434805723，欢迎大家加入。）
         * price : 0.00
         * word_num : 309859
         * fav_num : 4113
         * genre_path : 0,1,18,22
         * status : 1
         * is_mining : 1
         * is_reward : 1
         * is_vote : 0
         * type : 0
         * vip_level : 0
         * vip_cer : 0
         * shelf_time : 2017-10-21 13:00:47
         * create_time : 2017-10-21 13:00:47
         * update_time : 2018-01-24 09:35:39
         * deleted_at : null
         * pen_name : 泡杯大红袍
         * click_count : 3125
         */

        private int id;
        private int uid;
        private String source_link;
        private String title;
        private String cover;
        private String intro;
        private String price;
        private int word_num;
        private int fav_num;
        private String genre_path;
        private int status;
        private int is_mining;
        private int is_reward;
        private int is_vote;
        private int type;
        private int vip_level;
        private int vip_cer;
        private String shelf_time;
        private String create_time;
        private String update_time;
        private Object deleted_at;
        private String pen_name;
        private int click_count;

        private String bar_id;

        public String getBarId() {
            return bar_id;
        }

        public void setBarId(String barId) {
            this.bar_id = barId;
        }

        public String getId() {
            return String.valueOf(id);
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

        public String getSource_link() {
            return source_link;
        }

        public void setSource_link(String source_link) {
            this.source_link = source_link;
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

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getWord_num() {
            return word_num;
        }

        public void setWord_num(int word_num) {
            this.word_num = word_num;
        }

        public int getFav_num() {
            return fav_num;
        }

        public void setFav_num(int fav_num) {
            this.fav_num = fav_num;
        }

        public String getGenre_path() {
            return genre_path;
        }

        public void setGenre_path(String genre_path) {
            this.genre_path = genre_path;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getVip_level() {
            return vip_level;
        }

        public void setVip_level(int vip_level) {
            this.vip_level = vip_level;
        }

        public int getVip_cer() {
            return vip_cer;
        }

        public void setVip_cer(int vip_cer) {
            this.vip_cer = vip_cer;
        }

        public String getShelf_time() {
            return shelf_time;
        }

        public void setShelf_time(String shelf_time) {
            this.shelf_time = shelf_time;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getPen_name() {
            return pen_name;
        }

        public void setPen_name(String pen_name) {
            this.pen_name = pen_name;
        }

        public int getClick_count() {
            return click_count;
        }

        public void setClick_count(int click_count) {
            this.click_count = click_count;
        }
    }
}
