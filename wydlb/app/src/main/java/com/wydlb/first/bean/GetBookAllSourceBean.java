package com.wydlb.first.bean;

import java.util.List;

public class GetBookAllSourceBean {


    /**
     * code : 0
     * data : {"bookId":0,"chapterSourceResponses":[{"chapter_url":"string","first_chapter_id":"string","first_chapter_title":"string","first_chapter_url":"string","gender":"string","latest_chapter_title":"string","latest_chapter_url":"string","latest_updated_time":"string","source":"string"}],"isStartBrowser":false,"isStartTool":false,"platformType":0,"searchUrl":"string","toolUrl":"string"}
     * msg : string
     */

    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * bookId : 0
         * chapterSourceResponses : [{"chapter_url":"string","first_chapter_id":"string","first_chapter_title":"string","first_chapter_url":"string","gender":"string","latest_chapter_title":"string","latest_chapter_url":"string","latest_updated_time":"string","source":"string"}]
         * isStartBrowser : false
         * isStartTool : false
         * platformType : 0
         * searchUrl : string
         * toolUrl : string
         */

        private int bookId;
        private boolean isStartBrowser;
        private boolean isStartTool;
        private int platformType;
        private String searchUrl;
        private String toolUrl;
        private List<ChapterSourceResponsesBean> chapterSourceResponses;

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public boolean isIsStartBrowser() {
            return isStartBrowser;
        }

        public void setIsStartBrowser(boolean isStartBrowser) {
            this.isStartBrowser = isStartBrowser;
        }

        public boolean isIsStartTool() {
            return isStartTool;
        }

        public void setIsStartTool(boolean isStartTool) {
            this.isStartTool = isStartTool;
        }

        public int getPlatformType() {
            return platformType;
        }

        public void setPlatformType(int platformType) {
            this.platformType = platformType;
        }

        public String getSearchUrl() {
            return searchUrl;
        }

        public void setSearchUrl(String searchUrl) {
            this.searchUrl = searchUrl;
        }

        public String getToolUrl() {
            return toolUrl;
        }

        public void setToolUrl(String toolUrl) {
            this.toolUrl = toolUrl;
        }

        public List<ChapterSourceResponsesBean> getChapterSourceResponses() {
            return chapterSourceResponses;
        }

        public void setChapterSourceResponses(List<ChapterSourceResponsesBean> chapterSourceResponses) {
            this.chapterSourceResponses = chapterSourceResponses;
        }

        public static class ChapterSourceResponsesBean {
            /**
             * chapter_url : string
             * first_chapter_id : string
             * first_chapter_title : string
             * first_chapter_url : string
             * gender : string
             * latest_chapter_title : string
             * latest_chapter_url : string
             * latest_updated_time : string
             * source : string
             */

            private String chapter_url;
            private String first_chapter_id;
            private String first_chapter_title;
            private String first_chapter_url;
            private String gender;
            private String latest_chapter_title;
            private String latest_chapter_url;
            private String latest_updated_time;
            private String source;

            public String getChapter_url() {
                return chapter_url;
            }

            public void setChapter_url(String chapter_url) {
                this.chapter_url = chapter_url;
            }

            public String getFirst_chapter_id() {
                return first_chapter_id;
            }

            public void setFirst_chapter_id(String first_chapter_id) {
                this.first_chapter_id = first_chapter_id;
            }

            public String getFirst_chapter_title() {
                return first_chapter_title;
            }

            public void setFirst_chapter_title(String first_chapter_title) {
                this.first_chapter_title = first_chapter_title;
            }

            public String getFirst_chapter_url() {
                return first_chapter_url;
            }

            public void setFirst_chapter_url(String first_chapter_url) {
                this.first_chapter_url = first_chapter_url;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getLatest_chapter_title() {
                return latest_chapter_title;
            }

            public void setLatest_chapter_title(String latest_chapter_title) {
                this.latest_chapter_title = latest_chapter_title;
            }

            public String getLatest_chapter_url() {
                return latest_chapter_url;
            }

            public void setLatest_chapter_url(String latest_chapter_url) {
                this.latest_chapter_url = latest_chapter_url;
            }

            public String getLatest_updated_time() {
                return latest_updated_time;
            }

            public void setLatest_updated_time(String latest_updated_time) {
                this.latest_updated_time = latest_updated_time;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }
        }
    }
}
