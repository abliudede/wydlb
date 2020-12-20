package com.wydlb.first.bean;

import com.wydlb.first.model.bean.BookStoreBeanN;

import java.util.List;

public class BookStoreResponseForVisitor extends  BaseBean{


    /**
     * data : {"list":[{"bookId":30,"platformId":10021,"platformName":"巅峰转世","platformCover":"https://static.lianzai.com/updatecover/30.jpg","penName":"寒灵梦","platformIntroduce":"作为曾经的巅峰，他死过。今生作为转世者，刚一清醒便遇到了，童养媳？为了重踏巅峰之路，获得风云剑，手持神兵，在脑海中多出了一本仅限于传说的功法。刚一触碰功法，便引来神秘声音，这功法，是逆转还是正转，逆转之后，突破之时伴随生死之劫。今生，必重踏巅峰之路，前世死于哪里，今生就屠他个血雨腥风！","isUnread":false,"isConcern":true,"yxAccid":"5e088c70d42c40299946f8b23613a954","shareUrl":null,"source":"","categoryName":null},{"bookId":833,"platformId":10599,"platformName":"哈利波特之宁姬","platformCover":"https://static.lianzai.com/updatecover/833.jpg","penName":"安小叶女王","platformIntroduce":"公主驾到！闲人退散！什么？家族穷？她挣钱！家族弱？她凶残！家族很粗鲁？她琴棋书画，刺绣礼仪，领兵打仗，样样齐全！家族人丁凋零？她分分钟给拐个帅哥回来！努力造人！额，这是一个古代公主重生到西方魔幻世界，变成大佬的传说。哈利波特原创女主同人，额，女主凶残，苏苏苏。","isUnread":false,"isConcern":true,"yxAccid":null,"shareUrl":null,"source":"","categoryName":null},{"bookId":206,"platformId":10161,"platformName":"傲界仙尊","platformCover":"https://static.lianzai.com/updatecover/206.jpg","penName":"凯撒天帝","platformIntroduce":"是谁，千世轮回中坠入凡尘？\n是谁，应劫而生将扶救苍生？\n诡异的世界，处处陷阱，\n前行的路途，步步惊心。\n一个平凡的农家少年，\n如何在这诡异的十界里成就仙尊？","isUnread":false,"isConcern":true,"yxAccid":null,"shareUrl":null,"source":"","categoryName":null}],"timestamp":1534823594266}
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
         * list : [{"bookId":30,"platformId":10021,"platformName":"巅峰转世","platformCover":"https://static.lianzai.com/updatecover/30.jpg","penName":"寒灵梦","platformIntroduce":"作为曾经的巅峰，他死过。今生作为转世者，刚一清醒便遇到了，童养媳？为了重踏巅峰之路，获得风云剑，手持神兵，在脑海中多出了一本仅限于传说的功法。刚一触碰功法，便引来神秘声音，这功法，是逆转还是正转，逆转之后，突破之时伴随生死之劫。今生，必重踏巅峰之路，前世死于哪里，今生就屠他个血雨腥风！","isUnread":false,"isConcern":true,"yxAccid":"5e088c70d42c40299946f8b23613a954","shareUrl":null,"source":"","categoryName":null},{"bookId":833,"platformId":10599,"platformName":"哈利波特之宁姬","platformCover":"https://static.lianzai.com/updatecover/833.jpg","penName":"安小叶女王","platformIntroduce":"公主驾到！闲人退散！什么？家族穷？她挣钱！家族弱？她凶残！家族很粗鲁？她琴棋书画，刺绣礼仪，领兵打仗，样样齐全！家族人丁凋零？她分分钟给拐个帅哥回来！努力造人！额，这是一个古代公主重生到西方魔幻世界，变成大佬的传说。哈利波特原创女主同人，额，女主凶残，苏苏苏。","isUnread":false,"isConcern":true,"yxAccid":null,"shareUrl":null,"source":"","categoryName":null},{"bookId":206,"platformId":10161,"platformName":"傲界仙尊","platformCover":"https://static.lianzai.com/updatecover/206.jpg","penName":"凯撒天帝","platformIntroduce":"是谁，千世轮回中坠入凡尘？\n是谁，应劫而生将扶救苍生？\n诡异的世界，处处陷阱，\n前行的路途，步步惊心。\n一个平凡的农家少年，\n如何在这诡异的十界里成就仙尊？","isUnread":false,"isConcern":true,"yxAccid":null,"shareUrl":null,"source":"","categoryName":null}]
         * timestamp : 1534823594266
         */

        private List<BookStoreBeanN> hots;
        private ImInfoBean imInfo;


        public List<BookStoreBeanN> getHots() {
            return hots;
        }

        public void setHots(List<BookStoreBeanN> hots) {
            this.hots = hots;
        }

        public ImInfoBean getImInfo() {
            return imInfo;
        }

        public void setImInfo(ImInfoBean imInfo) {
            this.imInfo = imInfo;
        }
    }
}
