package com.lianzai.reader.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: BookCategoryListResponse
 * Author: lrz
 * Date: 2018/11/6 13:33
 * Description: ${DESCRIPTION}
 */
public class BookCategoryListResponse extends BaseBean {

    /**
     * data : {"volumes":[{"chapters":[{"chapter_id":0,"title":"第一章：消失的案发现场","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407209990","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370491.html","isvip":false}]},{"chapter_id":50,"title":"第二章：变异","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407278974","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370492.html","isvip":false}]}],"volume_id":0,"title":"正文"},{"chapters":[{"chapter_id":150,"title":"第三章：快逃","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407209990","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/443704914.html","isvip":false}]},{"chapter_id":200,"title":"第四章：灰色的小石块","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407278974","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370493.html","isvip":false}]}],"volume_id":1,"title":"正文1"}],"novel_id":"9939","book_name":"绝密试验档案","author_name":"稻草天师","cover":"https://bookcover.yuewen.com/qdbimg/349573/1012053719/180"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * volumes : [{"chapters":[{"chapter_id":0,"title":"第一章：消失的案发现场","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407209990","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370491.html","isvip":false}]},{"chapter_id":50,"title":"第二章：变异","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407278974","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370492.html","isvip":false}]}],"volume_id":0,"title":"正文"},{"chapters":[{"chapter_id":150,"title":"第三章：快逃","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407209990","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/443704914.html","isvip":false}]},{"chapter_id":200,"title":"第四章：灰色的小石块","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407278974","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370493.html","isvip":false}]}],"volume_id":1,"title":"正文1"}]
         * novel_id : 9939
         * book_name : 绝密试验档案
         * author_name : 稻草天师
         * cover : https://bookcover.yuewen.com/qdbimg/349573/1012053719/180
         */

        private String chapter_index_id;
        private String novel_id;
        private String book_name;
        private String author_name;
        private String cover;
        private List<VolumesBean> volumes;

        public String getChapter_index_id() {
            return chapter_index_id;
        }

        public void setChapter_index_id(String chapter_index_id) {
            this.chapter_index_id = chapter_index_id;
        }

        public String getNovel_id() {
            return novel_id;
        }

        public void setNovel_id(String novel_id) {
            this.novel_id = novel_id;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public List<VolumesBean> getVolumes() {
            return volumes;
        }

        public void setVolumes(List<VolumesBean> volumes) {
            this.volumes = volumes;
        }

        public static class VolumesBean implements Serializable{
            /**
             * chapters : [{"chapter_id":0,"title":"第一章：消失的案发现场","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407209990","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370491.html","isvip":false}]},{"chapter_id":50,"title":"第二章：变异","url":[{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407278974","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370492.html","isvip":false}]}]
             * volume_id : 0
             * title : 正文
             */

            private int volume_id;
            private String title;
            private List<ChaptersBean> chapters;

            public int getVolume_id() {
                return volume_id;
            }

            public void setVolume_id(int volume_id) {
                this.volume_id = volume_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<ChaptersBean> getChapters() {
                return chapters;
            }

            public void setChapters(List<ChaptersBean> chapters) {
                this.chapters = chapters;
            }

            public static class ChaptersBean implements Serializable{
                /**
                 * chapter_id : 0
                 * title : 第一章：消失的案发现场
                 * url : [{"source":"qidian.com","url":"https://m.qidian.com/book/1012053719/407209990","isvip":false},{"source":"7dsw.com","url":"https://www.7dsw.com/book/175/175580/44370491.html","isvip":false}]
                 */

                private String chapter_id;
                private String title;
                private String time;
                private List<UrlBean> urls;

                private boolean hasCached=false;//是否已经缓存

                private int sourceKeyPosition=0;//源的索引，默认取第一个

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public int getSourceKeyPosition() {
                    return sourceKeyPosition;
                }

                public void setSourceKeyPosition(int sourceKeyPosition) {
                    this.sourceKeyPosition = sourceKeyPosition;
                }

                public boolean isHasCached() {
                    return hasCached;
                }

                public void setHasCached(boolean hasCached) {
                    this.hasCached = hasCached;
                }

                public String getChapter_id() {
                    return chapter_id;
                }

                public void setChapter_id(String chapter_id) {
                    this.chapter_id = chapter_id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public List<UrlBean> getUrls() {
                    return urls;
                }

                public void setUrls(List<UrlBean> urls) {
                    this.urls = urls;
                }

                public static class UrlBean implements Serializable{
                    /**
                     * source : qidian.com
                     * url : https://m.qidian.com/book/1012053719/407209990
                     * isvip : false
                     */

                    private String source;
                    private String url;
                    private boolean isvip;

                    public String getSource() {
                        return source;
                    }

                    public void setSource(String source) {
                        this.source = source;
                    }

                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    public boolean isIsvip() {
                        return isvip;
                    }

                    public void setIsvip(boolean isvip) {
                        this.isvip = isvip;
                    }
                }
            }
        }
    }
}
