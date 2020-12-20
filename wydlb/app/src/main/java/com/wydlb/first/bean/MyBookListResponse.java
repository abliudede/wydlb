package com.wydlb.first.bean;

import com.wydlb.first.model.bean.MyBookListBean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: BookListResponse
 * Author: lrz
 * Date: 2018/10/12 16:12
 * Description:我的书单列表
 */
public class MyBookListResponse extends BaseBean {


    /**
     * data : {"total":215663,"list":[{"id":65536,"booklistName":"恐怖小说","booklistIntro":"独爱恐怖类型","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/329543","tagList":null},{"id":131072,"booklistName":"杰xa的书单","booklistIntro":"本人力荐，全是大神或者准大神之作，\n文笔洗练而充满跃动的美感，张力十足！\n你值得拥有，去看看这些书吧。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/461622","tagList":null},{"id":196608,"booklistName":"爱看好书","booklistIntro":"真心看书，真心说书，真真的！","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/556452","tagList":null},{"id":256,"booklistName":"唯爱韩娱","booklistIntro":"这是一段别样的青春，每个读者都是主角","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/231645","tagList":null},{"id":65792,"booklistName":"精品+精选=经典～个人喜好","booklistIntro":"分类不限！收藏一些独特粮草仙草！新颖题材、高智商酱！","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/329863","tagList":null},{"id":131328,"booklistName":"你到底想看什么","booklistIntro":"我自己看的书","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/462296","tagList":null},{"id":196864,"booklistName":"大神。经典。","booklistIntro":"总有你喜欢","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/557079","tagList":null},{"id":512,"booklistName":"做任务了，求鲜花","booklistIntro":"做任务的书友都来这里了，都是我自己喜欢的，有你喜欢的就关注下，顺手做下鲜花任务","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/232003","tagList":null},{"id":66048,"booklistName":"老书虫的书单","booklistIntro":"口味不挑，喜欢题材很多。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/330153","tagList":null},{"id":131584,"booklistName":"洁火","booklistIntro":"不种马，不脑残，不无脑后宫的好书，\n构思新颖，题材独具匠心，段落清晰，情节诡异，跌宕起伏。\n收藏本书单，书荒不用怕。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/462997","tagList":null},{"id":197120,"booklistName":"水过无痕","booklistIntro":"随心而记","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/557614","tagList":null},{"id":768,"booklistName":"部部经典","booklistIntro":"试毒十余年，老书虫为大家推荐的精品。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/232361","tagList":null},{"id":66304,"booklistName":"厉害了","booklistIntro":"好看","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/330465","tagList":null},{"id":131840,"booklistName":"鞁乘","booklistIntro":"看了还想看，永远看不够，爱不够的书，\n路清晰，故事严谨，结构框架非常好，文笔优美，行文风格漂亮。\n这些好书，先收着，等减肥了再杀！","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/463650","tagList":null},{"id":197376,"booklistName":"我觉得好看的","booklistIntro":"四年累计","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/558081","tagList":null},{"id":1024,"booklistName":"我的藏书","booklistIntro":"大神的作品","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/232743","tagList":null},{"id":66560,"booklistName":"个人喜好的精品","booklistIntro":"自己喜欢的小说，每一本都有它吸引人的特色，一开始阅读就不想停下来。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/330771","tagList":null},{"id":132096,"booklistName":"永杰wy的书单","booklistIntro":"非常值得追，推荐指数五颗星的一些书，\n故事结构很完善，情节丰富，人物饱满。\n收藏本书单，书荒不用怕。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/464369","tagList":null},{"id":197632,"booklistName":"明末","booklistIntro":"专注明末的历史类小说集合","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/558583","tagList":null},{"id":1280,"booklistName":"喜欢各种书","booklistIntro":"各种书都有","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/233074","tagList":null}],"pageNum":1,"pageSize":20,"pages":10784,"size":20}
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
         * total : 215663
         * list : [{"id":65536,"booklistName":"恐怖小说","booklistIntro":"独爱恐怖类型","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/329543","tagList":null},{"id":131072,"booklistName":"杰xa的书单","booklistIntro":"本人力荐，全是大神或者准大神之作，\n文笔洗练而充满跃动的美感，张力十足！\n你值得拥有，去看看这些书吧。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/461622","tagList":null},{"id":196608,"booklistName":"爱看好书","booklistIntro":"真心看书，真心说书，真真的！","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/556452","tagList":null},{"id":256,"booklistName":"唯爱韩娱","booklistIntro":"这是一段别样的青春，每个读者都是主角","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/231645","tagList":null},{"id":65792,"booklistName":"精品+精选=经典～个人喜好","booklistIntro":"分类不限！收藏一些独特粮草仙草！新颖题材、高智商酱！","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/329863","tagList":null},{"id":131328,"booklistName":"你到底想看什么","booklistIntro":"我自己看的书","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/462296","tagList":null},{"id":196864,"booklistName":"大神。经典。","booklistIntro":"总有你喜欢","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/557079","tagList":null},{"id":512,"booklistName":"做任务了，求鲜花","booklistIntro":"做任务的书友都来这里了，都是我自己喜欢的，有你喜欢的就关注下，顺手做下鲜花任务","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/232003","tagList":null},{"id":66048,"booklistName":"老书虫的书单","booklistIntro":"口味不挑，喜欢题材很多。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/330153","tagList":null},{"id":131584,"booklistName":"洁火","booklistIntro":"不种马，不脑残，不无脑后宫的好书，\n构思新颖，题材独具匠心，段落清晰，情节诡异，跌宕起伏。\n收藏本书单，书荒不用怕。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/462997","tagList":null},{"id":197120,"booklistName":"水过无痕","booklistIntro":"随心而记","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/557614","tagList":null},{"id":768,"booklistName":"部部经典","booklistIntro":"试毒十余年，老书虫为大家推荐的精品。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/232361","tagList":null},{"id":66304,"booklistName":"厉害了","booklistIntro":"好看","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/330465","tagList":null},{"id":131840,"booklistName":"鞁乘","booklistIntro":"看了还想看，永远看不够，爱不够的书，\n路清晰，故事严谨，结构框架非常好，文笔优美，行文风格漂亮。\n这些好书，先收着，等减肥了再杀！","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/463650","tagList":null},{"id":197376,"booklistName":"我觉得好看的","booklistIntro":"四年累计","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/558081","tagList":null},{"id":1024,"booklistName":"我的藏书","booklistIntro":"大神的作品","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/232743","tagList":null},{"id":66560,"booklistName":"个人喜好的精品","booklistIntro":"自己喜欢的小说，每一本都有它吸引人的特色，一开始阅读就不想停下来。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/330771","tagList":null},{"id":132096,"booklistName":"永杰wy的书单","booklistIntro":"非常值得追，推荐指数五颗星的一些书，\n故事结构很完善，情节丰富，人物饱满。\n收藏本书单，书荒不用怕。","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/464369","tagList":null},{"id":197632,"booklistName":"明末","booklistIntro":"专注明末的历史类小说集合","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/558583","tagList":null},{"id":1280,"booklistName":"喜欢各种书","booklistIntro":"各种书都有","collectionNum":0,"booklistUrl":"https://book.qidian.com/booklist/detail/233074","tagList":null}]
         * pageNum : 1
         * pageSize : 20
         * pages : 10784
         * size : 20
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        private List<MyBookListBean> list;

        private List<MyBookListBean> delete_List;

        public List<MyBookListBean> getDelete_List() {
            return delete_List;
        }

        public void setDelete_List(List<MyBookListBean> delete_List) {
            this.delete_List = delete_List;
        }

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

        public List<MyBookListBean> getList() {
            return list;
        }

        public void setList(List<MyBookListBean> list) {
            this.list = list;
        }

    }
}
