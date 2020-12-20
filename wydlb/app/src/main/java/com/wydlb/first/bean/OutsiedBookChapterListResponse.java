package com.wydlb.first.bean;

import java.util.List;

public class OutsiedBookChapterListResponse {
    /**
     * code : 200
     * message : 信息拉取成功
     * data : {"response":[{"chapterName":"第1章 唐记食铺","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/SfNU_tn41e76ItTi_ILQ7A2","chapterUpdateTime":1467798679,"chapterCreateTime":1467734400,"chapterId":311377154,"chapteWordNum":3373,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第2章 拍桌子（求收藏）","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/ga0w5_sfktm2uJcMpdsVgA2","chapterUpdateTime":1467887682,"chapterCreateTime":1467820800,"chapterId":311434113,"chapteWordNum":3517,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第3章 注定失败的新政（求收藏）","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/WIsz-ECCtk74p8iEw--PPw2","chapterUpdateTime":1467909267,"chapterCreateTime":1467907200,"chapterId":311456679,"chapteWordNum":3383,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第4章 眼光够高啊（求收藏）","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/ZtZ5ex6v9c74p8iEw--PPw2","chapterUpdateTime":1467996908,"chapterCreateTime":1467993600,"chapterId":311508459,"chapteWordNum":3038,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第5章 郎情妾意","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/7zGFgH0-asuaGfXRMrUjdw2","chapterUpdateTime":1468122882,"chapterCreateTime":1468080000,"chapterId":311569545,"chapteWordNum":2221,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第6章 提亲","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/E2rHb6aiiOr4p8iEw--PPw2","chapterUpdateTime":1468215295,"chapterCreateTime":1468166400,"chapterId":311625569,"chapteWordNum":2229,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第7章 宋之疾","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/8WTzenSI4YqaGfXRMrUjdw2","chapterUpdateTime":1468301190,"chapterCreateTime":1468252800,"chapterId":311677375,"chapteWordNum":2550,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第8章 贼婆子","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/G0vCjb8XmzTM5j8_3RRvhw2","chapterUpdateTime":1468391432,"chapterCreateTime":1468339200,"chapterId":312046546,"chapteWordNum":2424,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第9章 范仲淹","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/1VU47gEhGgK2uJcMpdsVgA2","chapterUpdateTime":1468479405,"chapterCreateTime":1468425600,"chapterId":312526863,"chapteWordNum":2636,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第10章 再提亲","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/IwFysf2t9clMs5iq0oQwLQ2","chapterUpdateTime":1468586557,"chapterCreateTime":1468512000,"chapterId":312768151,"chapteWordNum":2475,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"}],"hasNext":true}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
         * response : [{"chapterName":"第1章 唐记食铺","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/SfNU_tn41e76ItTi_ILQ7A2","chapterUpdateTime":1467798679,"chapterCreateTime":1467734400,"chapterId":311377154,"chapteWordNum":3373,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第2章 拍桌子（求收藏）","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/ga0w5_sfktm2uJcMpdsVgA2","chapterUpdateTime":1467887682,"chapterCreateTime":1467820800,"chapterId":311434113,"chapteWordNum":3517,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第3章 注定失败的新政（求收藏）","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/WIsz-ECCtk74p8iEw--PPw2","chapterUpdateTime":1467909267,"chapterCreateTime":1467907200,"chapterId":311456679,"chapteWordNum":3383,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第4章 眼光够高啊（求收藏）","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/ZtZ5ex6v9c74p8iEw--PPw2","chapterUpdateTime":1467996908,"chapterCreateTime":1467993600,"chapterId":311508459,"chapteWordNum":3038,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第5章 郎情妾意","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/7zGFgH0-asuaGfXRMrUjdw2","chapterUpdateTime":1468122882,"chapterCreateTime":1468080000,"chapterId":311569545,"chapteWordNum":2221,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第6章 提亲","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/E2rHb6aiiOr4p8iEw--PPw2","chapterUpdateTime":1468215295,"chapterCreateTime":1468166400,"chapterId":311625569,"chapteWordNum":2229,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第7章 宋之疾","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/8WTzenSI4YqaGfXRMrUjdw2","chapterUpdateTime":1468301190,"chapterCreateTime":1468252800,"chapterId":311677375,"chapteWordNum":2550,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第8章 贼婆子","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/G0vCjb8XmzTM5j8_3RRvhw2","chapterUpdateTime":1468391432,"chapterCreateTime":1468339200,"chapterId":312046546,"chapteWordNum":2424,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第9章 范仲淹","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/1VU47gEhGgK2uJcMpdsVgA2","chapterUpdateTime":1468479405,"chapterCreateTime":1468425600,"chapterId":312526863,"chapteWordNum":2636,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"},{"chapterName":"第10章 再提亲","chapterLink":"https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/IwFysf2t9clMs5iq0oQwLQ2","chapterUpdateTime":1468586557,"chapterCreateTime":1468512000,"chapterId":312768151,"chapteWordNum":2475,"source":2,"outSource":"qidian","novelId":1003631173,"encodeNovelId":"26092574123679228689938","barId":1003631173,"encodeBarId":"26092574123679228689938"}]
         * hasNext : true
         */

        private boolean hasNext;
        private List<ResponseBean> response;

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<ResponseBean> getResponse() {
            return response;
        }

        public void setResponse(List<ResponseBean> response) {
            this.response = response;
        }

        public static class ResponseBean {
            /**
             * chapterName : 第1章 唐记食铺
             * chapterLink : https://read.qidian.com/chapter/Bj4oI_RLz4BwKI0S3HHgow2/SfNU_tn41e76ItTi_ILQ7A2
             * chapterUpdateTime : 1467798679
             * chapterCreateTime : 1467734400
             * chapterId : 311377154
             * chapteWordNum : 3373
             * source : 2
             * outSource : qidian
             * novelId : 1003631173
             * encodeNovelId : 26092574123679228689938
             * barId : 1003631173
             * encodeBarId : 26092574123679228689938
             */

            private String chapterName;
            private String chapterLink;
//            private int chapterUpdateTime;
//            private int chapterCreateTime;
            private int chapterId;
            private int chapteWordNum;
            private int source;
            private String outSource;
            private int novelId;
            private String encodeNovelId;
            private int barId;
            private String encodeBarId;
            private boolean chapterVip;

            public boolean isChapterVip() {
                return chapterVip;
            }

            public void setChapterVip(boolean chapterVip) {
                this.chapterVip = chapterVip;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public String getChapterLink() {
                return chapterLink;
            }

            public void setChapterLink(String chapterLink) {
                this.chapterLink = chapterLink;
            }

//            public int getChapterUpdateTime() {
//                return chapterUpdateTime;
//            }
//
//            public void setChapterUpdateTime(int chapterUpdateTime) {
//                this.chapterUpdateTime = chapterUpdateTime;
//            }
//
//            public int getChapterCreateTime() {
//                return chapterCreateTime;
//            }
//
//            public void setChapterCreateTime(int chapterCreateTime) {
//                this.chapterCreateTime = chapterCreateTime;
//            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public int getChapteWordNum() {
                return chapteWordNum;
            }

            public void setChapteWordNum(int chapteWordNum) {
                this.chapteWordNum = chapteWordNum;
            }

            public int getSource() {
                return source;
            }

            public void setSource(int source) {
                this.source = source;
            }

            public String getOutSource() {
                return outSource;
            }

            public void setOutSource(String outSource) {
                this.outSource = outSource;
            }

            public int getNovelId() {
                return novelId;
            }

            public void setNovelId(int novelId) {
                this.novelId = novelId;
            }

            public String getEncodeNovelId() {
                return encodeNovelId;
            }

            public void setEncodeNovelId(String encodeNovelId) {
                this.encodeNovelId = encodeNovelId;
            }

            public int getBarId() {
                return barId;
            }

            public void setBarId(int barId) {
                this.barId = barId;
            }

            public String getEncodeBarId() {
                return encodeBarId;
            }

            public void setEncodeBarId(String encodeBarId) {
                this.encodeBarId = encodeBarId;
            }
        }
    }
}
