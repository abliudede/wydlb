package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxTool;

import java.util.List;

public class PublicNumberHistoryBean extends  BaseBean{

    /**
     * data : {"total":3,"list":[{"newsType":2,"data":{"title":"有新章节更新","content":"撒旦法是的法","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"阿斯顿放","bookId":1,"chapterId":1,"createdAt":1531557805000}},{"newsType":2,"data":{"title":"有新章节更新","content":"string","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"string","bookId":1,"chapterId":0,"createdAt":1531558508000}},{"newsType":2,"data":{"title":"有新章节更新","content":"string","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"string","bookId":1,"chapterId":0,"createdAt":1531558521000}}],"pageNum":1,"pageSize":100,"pages":1,"size":3,"platformId":3802,"platformType":2,"platformCover":"修改2","platformTitle":"测试书2","platformNum":"LZ7OvFlpMU","platformIntro":"修改3","bookId":1}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * total : 3
         * list : [{"newsType":2,"data":{"title":"有新章节更新","content":"撒旦法是的法","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"阿斯顿放","bookId":1,"chapterId":1,"createdAt":1531557805000}},{"newsType":2,"data":{"title":"有新章节更新","content":"string","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"string","bookId":1,"chapterId":0,"createdAt":1531558508000}},{"newsType":2,"data":{"title":"有新章节更新","content":"string","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"string","bookId":1,"chapterId":0,"createdAt":1531558521000}}]
         * pageNum : 1
         * pageSize : 100
         * pages : 1
         * size : 3
         * platformId : 3802
         * platformType : 2
         * platformCover : 修改2
         * platformTitle : 测试书2
         * platformNum : LZ7OvFlpMU
         * platformIntro : 修改3
         * bookId : 1
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private int platformId;
        private int platformType;
        private String platformCover;
        private String platformTitle;
        private String platformNum;
        private String platformIntro;
        private int bookId;

        private List<ListBean> list;

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

        public String getPlatformCover() {
            return platformCover;
        }

        public void setPlatformCover(String platformCover) {
            this.platformCover = platformCover;
        }

        public String getPlatformTitle() {
            return platformTitle;
        }

        public void setPlatformTitle(String platformTitle) {
            this.platformTitle = platformTitle;
        }

        public String getPlatformNum() {
            return platformNum;
        }

        public void setPlatformNum(String platformNum) {
            this.platformNum = platformNum;
        }

        public String getPlatformIntro() {
            return RxTool.filterContent(platformIntro);
        }

        public void setPlatformIntro(String platformIntro) {
            this.platformIntro = platformIntro;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * newsType : 2
             * data : {"title":"有新章节更新","content":"撒旦法是的法","img":"http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg","url":"阿斯顿放","bookId":1,"chapterId":1,"createdAt":1531557805000}
             */

            private int newsType;
            private DataBean data;

            public int getNewsType() {
                return newsType;
            }

            public void setNewsType(int newsType) {
                this.newsType = newsType;
            }

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * title : 有新章节更新
                 * content : 撒旦法是的法
                 * img : http://pic13.photophoto.cn/20091008/0005018325060806_b.jpg
                 * url : 阿斯顿放
                 * bookId : 1
                 * chapterId : 1
                 * createdAt : 1531557805000
                 */

                private String title;
                private String content;
                private String img;
                private String url;
                private int bookId;
                private int chapterId;
                private long createdAt;
                private String teamId;
                private String platformName;

                private  String source;

                public String getTeamId() {
                    return teamId;
                }

                public void setTeamId(String teamId) {
                    this.teamId = teamId;
                }

                public String getPlatformName() {
                    return platformName;
                }

                public void setPlatformName(String platformName) {
                    this.platformName = platformName;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getContent() {
                    return RxTool.filterContent(content);
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getBookId() {
                    return bookId;
                }

                public void setBookId(int bookId) {
                    this.bookId = bookId;
                }

                public int getChapterId() {
                    return chapterId;
                }

                public void setChapterId(int chapterId) {
                    this.chapterId = chapterId;
                }

                public long getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(long createdAt) {
                    this.createdAt = createdAt;
                }
            }
        }
    }
}
