package com.lianzai.reader.bean;

import java.util.List;

public class OutsideBookResponse {


    /**
     * code : 200
     * message : 信息拉取成功
     * data : {"response":{"detailInfo":{"novelName":"圣墟","novelId":1004608738,"novelCover":"http://image-es.test.upcdn.net/qd/1004608738","wordNum":41418,"authorName":"辰东","favNum":0,"postCount":0,"encodeNovelId":"260610906447569227624384","intro":"在破败中崛起，在寂灭中复苏。沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角\u2026\u2026","isMining":false,"isReward":false,"isLike":false,"isFollow":false,"barId":"6","encodeBarId":"0913582816","novelCate":"玄幻","chapterTotal":14,"defaultSource":"qidian.com"},"sourceInfo":[{"lastChapterId":4377634,"lastChapterName":"第1103章 爱是一道光","lastChapterTime":false,"authorName":"辰东","bookLink":"https://m.5du5.la/xs/559.html","source":"5du5.la","novelCate":"玄幻","chapterTotal":1103,"sourceName":"5du5","firstChapterLink":"https://m.5du5.la/0/559/181906.html"},{"lastChapterId":416570435,"lastChapterName":"第1107章 天髓炼金身","lastChapterTime":1529597070,"authorName":"辰东","bookLink":"https://m.qidian.com/book/1004608738","source":"qidian.com","novelCate":"玄幻","chapterTotal":1127,"sourceName":"qidian","firstChapterLink":"https://read.qidian.com/chapter/_AaqI-dPJJ4uTkiRw_sFYA2/-doT6biEpYlOBDFlr9quQA2"},{"lastChapterId":24021671,"lastChapterName":"第1103章 爱是一道光","lastChapterTime":1529423988,"authorName":"辰东","bookLink":"http://m.biqiuge.com/book_4772/","source":"biqiuge.com","novelCate":"玄幻","chapterTotal":1103,"sourceName":"biqiuge","firstChapterLink":"http://m.biqiuge.com/book_4772/2940354.html"}]}}
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
         * response : {"detailInfo":{"novelName":"圣墟","novelId":1004608738,"novelCover":"http://image-es.test.upcdn.net/qd/1004608738","wordNum":41418,"authorName":"辰东","favNum":0,"postCount":0,"encodeNovelId":"260610906447569227624384","intro":"在破败中崛起，在寂灭中复苏。沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角\u2026\u2026","isMining":false,"isReward":false,"isLike":false,"isFollow":false,"barId":"6","encodeBarId":"0913582816","novelCate":"玄幻","chapterTotal":14,"defaultSource":"qidian.com"},"sourceInfo":[{"lastChapterId":4377634,"lastChapterName":"第1103章 爱是一道光","lastChapterTime":false,"authorName":"辰东","bookLink":"https://m.5du5.la/xs/559.html","source":"5du5.la","novelCate":"玄幻","chapterTotal":1103,"sourceName":"5du5","firstChapterLink":"https://m.5du5.la/0/559/181906.html"},{"lastChapterId":416570435,"lastChapterName":"第1107章 天髓炼金身","lastChapterTime":1529597070,"authorName":"辰东","bookLink":"https://m.qidian.com/book/1004608738","source":"qidian.com","novelCate":"玄幻","chapterTotal":1127,"sourceName":"qidian","firstChapterLink":"https://read.qidian.com/chapter/_AaqI-dPJJ4uTkiRw_sFYA2/-doT6biEpYlOBDFlr9quQA2"},{"lastChapterId":24021671,"lastChapterName":"第1103章 爱是一道光","lastChapterTime":1529423988,"authorName":"辰东","bookLink":"http://m.biqiuge.com/book_4772/","source":"biqiuge.com","novelCate":"玄幻","chapterTotal":1103,"sourceName":"biqiuge","firstChapterLink":"http://m.biqiuge.com/book_4772/2940354.html"}]}
         */

        private ResponseBean response;

        public ResponseBean getResponse() {
            return response;
        }

        public void setResponse(ResponseBean response) {
            this.response = response;
        }

        public static class ResponseBean {
            /**
             * detailInfo : {"novelName":"圣墟","novelId":1004608738,"novelCover":"http://image-es.test.upcdn.net/qd/1004608738","wordNum":41418,"authorName":"辰东","favNum":0,"postCount":0,"encodeNovelId":"260610906447569227624384","intro":"在破败中崛起，在寂灭中复苏。沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角\u2026\u2026","isMining":false,"isReward":false,"isLike":false,"isFollow":false,"barId":"6","encodeBarId":"0913582816","novelCate":"玄幻","chapterTotal":14,"defaultSource":"qidian.com"}
             * sourceInfo : [{"lastChapterId":4377634,"lastChapterName":"第1103章 爱是一道光","lastChapterTime":false,"authorName":"辰东","bookLink":"https://m.5du5.la/xs/559.html","source":"5du5.la","novelCate":"玄幻","chapterTotal":1103,"sourceName":"5du5","firstChapterLink":"https://m.5du5.la/0/559/181906.html"},{"lastChapterId":416570435,"lastChapterName":"第1107章 天髓炼金身","lastChapterTime":1529597070,"authorName":"辰东","bookLink":"https://m.qidian.com/book/1004608738","source":"qidian.com","novelCate":"玄幻","chapterTotal":1127,"sourceName":"qidian","firstChapterLink":"https://read.qidian.com/chapter/_AaqI-dPJJ4uTkiRw_sFYA2/-doT6biEpYlOBDFlr9quQA2"},{"lastChapterId":24021671,"lastChapterName":"第1103章 爱是一道光","lastChapterTime":1529423988,"authorName":"辰东","bookLink":"http://m.biqiuge.com/book_4772/","source":"biqiuge.com","novelCate":"玄幻","chapterTotal":1103,"sourceName":"biqiuge","firstChapterLink":"http://m.biqiuge.com/book_4772/2940354.html"}]
             */

            private DetailInfoBean detailInfo;
            private List<SourceInfoBean> sourceInfo;

            public DetailInfoBean getDetailInfo() {
                return detailInfo;
            }

            public void setDetailInfo(DetailInfoBean detailInfo) {
                this.detailInfo = detailInfo;
            }

            public List<SourceInfoBean> getSourceInfo() {
                return sourceInfo;
            }

            public void setSourceInfo(List<SourceInfoBean> sourceInfo) {
                this.sourceInfo = sourceInfo;
            }

            public static class DetailInfoBean {
                /**
                 * novelName : 圣墟
                 * novelId : 1004608738
                 * novelCover : http://image-es.test.upcdn.net/qd/1004608738
                 * wordNum : 41418
                 * authorName : 辰东
                 * favNum : 0
                 * postCount : 0
                 * encodeNovelId : 260610906447569227624384
                 * intro : 在破败中崛起，在寂灭中复苏。沧海成尘，雷电枯竭，那一缕幽雾又一次临近大地，世间的枷锁被打开了，一个全新的世界就此揭开神秘的一角……
                 * isMining : false
                 * isReward : false
                 * isLike : false
                 * isFollow : false
                 * barId : 6
                 * encodeBarId : 0913582816
                 * novelCate : 玄幻
                 * chapterTotal : 14
                 * defaultSource : qidian.com
                 */

                private String novelName;
                private int novelId;
                private String novelCover;
                private int wordNum;
                private String authorName;
                private int favNum;
                private int postCount;
                private String encodeNovelId;
                private String intro;
                private boolean isMining;
                private boolean isReward;
                private boolean isLike;
                private boolean isFollow;
                private String barId;
                private String encodeBarId;
                private String novelCate;
                private int chapterTotal;
                private String defaultSource;
                private String shareUrl;

                public String getShareUrl() {
                    return shareUrl;
                }

                public void setShareUrl(String shareUrl) {
                    this.shareUrl = shareUrl;
                }

                public String getNovelName() {
                    return novelName;
                }

                public void setNovelName(String novelName) {
                    this.novelName = novelName;
                }

                public int getNovelId() {
                    return novelId;
                }

                public void setNovelId(int novelId) {
                    this.novelId = novelId;
                }

                public String getNovelCover() {
                    return novelCover;
                }

                public void setNovelCover(String novelCover) {
                    this.novelCover = novelCover;
                }

                public int getWordNum() {
                    return wordNum;
                }

                public void setWordNum(int wordNum) {
                    this.wordNum = wordNum;
                }

                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }

                public int getFavNum() {
                    return favNum;
                }

                public void setFavNum(int favNum) {
                    this.favNum = favNum;
                }

                public int getPostCount() {
                    return postCount;
                }

                public void setPostCount(int postCount) {
                    this.postCount = postCount;
                }

                public String getEncodeNovelId() {
                    return encodeNovelId;
                }

                public void setEncodeNovelId(String encodeNovelId) {
                    this.encodeNovelId = encodeNovelId;
                }

                public String getIntro() {
                    return intro;
                }

                public void setIntro(String intro) {
                    this.intro = intro;
                }

                public boolean isIsMining() {
                    return isMining;
                }

                public void setIsMining(boolean isMining) {
                    this.isMining = isMining;
                }

                public boolean isIsReward() {
                    return isReward;
                }

                public void setIsReward(boolean isReward) {
                    this.isReward = isReward;
                }

                public boolean isIsLike() {
                    return isLike;
                }

                public void setIsLike(boolean isLike) {
                    this.isLike = isLike;
                }

                public boolean isIsFollow() {
                    return isFollow;
                }

                public void setIsFollow(boolean isFollow) {
                    this.isFollow = isFollow;
                }

                public String getBarId() {
                    return barId;
                }

                public void setBarId(String barId) {
                    this.barId = barId;
                }

                public String getEncodeBarId() {
                    return encodeBarId;
                }

                public void setEncodeBarId(String encodeBarId) {
                    this.encodeBarId = encodeBarId;
                }

                public String getNovelCate() {
                    return novelCate;
                }

                public void setNovelCate(String novelCate) {
                    this.novelCate = novelCate;
                }

                public int getChapterTotal() {
                    return chapterTotal;
                }

                public void setChapterTotal(int chapterTotal) {
                    this.chapterTotal = chapterTotal;
                }

                public String getDefaultSource() {
                    return defaultSource;
                }

                public void setDefaultSource(String defaultSource) {
                    this.defaultSource = defaultSource;
                }
            }

            public static class SourceInfoBean {
                /**
                 * lastChapterId : 4377634
                 * lastChapterName : 第1103章 爱是一道光
                 * lastChapterTime : false
                 * authorName : 辰东
                 * bookLink : https://m.5du5.la/xs/559.html
                 * source : 5du5.la
                 * novelCate : 玄幻
                 * chapterTotal : 1103
                 * sourceName : 5du5
                 * firstChapterLink : https://m.5du5.la/0/559/181906.html
                 */

                private int lastChapterId;
                private String lastChapterName;
                private String authorName;
                private String bookLink;
                private String source;
                private String novelCate;
                private int chapterTotal;
                private String sourceName;
                private String firstChapterLink;

                private String outerNovelId;

                public String getOuterNovelId() {
                    return outerNovelId;
                }

                public void setOuterNovelId(String outerNovelId) {
                    this.outerNovelId = outerNovelId;
                }

                private long lastChapterTime=0;

                public int getLastChapterId() {
                    return lastChapterId;
                }

                public void setLastChapterId(int lastChapterId) {
                    this.lastChapterId = lastChapterId;
                }

                public String getLastChapterName() {
                    return lastChapterName;
                }

                public long getLastChapterTime() {
                    return lastChapterTime;
                }

                public void setLastChapterTime(long lastChapterTime) {
                    this.lastChapterTime = lastChapterTime;
                }

                public void setLastChapterName(String lastChapterName) {
                    this.lastChapterName = lastChapterName;
                }


                public String getAuthorName() {
                    return authorName;
                }

                public void setAuthorName(String authorName) {
                    this.authorName = authorName;
                }

                public String getBookLink() {
                    return bookLink;
                }

                public void setBookLink(String bookLink) {
                    this.bookLink = bookLink;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getNovelCate() {
                    return novelCate;
                }

                public void setNovelCate(String novelCate) {
                    this.novelCate = novelCate;
                }

                public int getChapterTotal() {
                    return chapterTotal;
                }

                public void setChapterTotal(int chapterTotal) {
                    this.chapterTotal = chapterTotal;
                }

                public String getSourceName() {
                    return sourceName;
                }

                public void setSourceName(String sourceName) {
                    this.sourceName = sourceName;
                }

                public String getFirstChapterLink() {
                    return firstChapterLink;
                }

                public void setFirstChapterLink(String firstChapterLink) {
                    this.firstChapterLink = firstChapterLink;
                }

                @Override
                public String toString() {
                    return "SourceInfoBean{" +
                            "authorName='" + authorName + '\'' +
                            ", source='" + source + '\'' +
                            ", sourceName='" + sourceName + '\'' +
                            ", outerNovelId='" + outerNovelId + '\'' +
                            '}';
                }
            }
        }
    }
}
