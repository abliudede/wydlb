package com.lianzai.reader.bean;

import com.lianzai.reader.utils.RxTool;

import java.util.List;

/**
 * Created by lrz on 2018/1/23.
 */

public class SearchNovelsBean {


    /**
     * code : 0
     * msg : success
     * data : {"total":2,"list":[{"bookId":28,"platformId":3806,"platformName":"诛天荒圣","platformCover":"https://static.lianzai.com/updatecover/28.jpg","penName":"横亘万古","platformIntroduce":"弹指一挥间，逝万年。造化轮回前，九世转。天地动，山河荡。乱世又至，谁能相抗？万载重现，谁与我笑傲世间？身份扑朔迷离，兼怀诸多秘密，为探知这一切谜底，他走向一条未知之路，而这条路，究竟\u2026\u2026","isUnread":0,"isConcern":1},{"bookId":626,"platformId":3815,"platformName":"天殒","platformCover":"https://static.lianzai.com/updatecover/626.jpg","penName":"凌心","platformIntroduce":"万千世界，位面众多，少年自玄武出，骑着九头蜥蜴闯荡这精彩绝伦的世界，复仇之路漫长曲折，看少年如何逆转乾坤，翻云覆雨！","isUnread":0,"isConcern":1}],"pageNum":1,"pageSize":10,"pages":1,"size":2}
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
         * total : 2
         * list : [{"bookId":28,"platformId":3806,"platformName":"诛天荒圣","platformCover":"https://static.lianzai.com/updatecover/28.jpg","penName":"横亘万古","platformIntroduce":"弹指一挥间，逝万年。造化轮回前，九世转。天地动，山河荡。乱世又至，谁能相抗？万载重现，谁与我笑傲世间？身份扑朔迷离，兼怀诸多秘密，为探知这一切谜底，他走向一条未知之路，而这条路，究竟\u2026\u2026","isUnread":0,"isConcern":1},{"bookId":626,"platformId":3815,"platformName":"天殒","platformCover":"https://static.lianzai.com/updatecover/626.jpg","penName":"凌心","platformIntroduce":"万千世界，位面众多，少年自玄武出，骑着九头蜥蜴闯荡这精彩绝伦的世界，复仇之路漫长曲折，看少年如何逆转乾坤，翻云覆雨！","isUnread":0,"isConcern":1}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 2
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
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

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * bookId : 28
             * platformId : 3806
             * platformName : 诛天荒圣
             * platformCover : https://static.lianzai.com/updatecover/28.jpg
             * penName : 横亘万古
             * platformIntroduce : 弹指一挥间，逝万年。造化轮回前，九世转。天地动，山河荡。乱世又至，谁能相抗？万载重现，谁与我笑傲世间？身份扑朔迷离，兼怀诸多秘密，为探知这一切谜底，他走向一条未知之路，而这条路，究竟……
             * isUnread : 0
             * isConcern : 1
             */

            private int bookId;
            private int platformId;
            private String platformName;
            private String platformCover;
            private String penName;
            private String platformIntroduce;
            private boolean isUnread;
            private boolean isConcern;
            private  String source;
            private int platformType;

            private String yxAccid;

            private boolean isCopyright;

            public boolean isCopyright() {
                return isCopyright;
            }

            public void setCopyright(boolean copyright) {
                isCopyright = copyright;
            }

            public int getPlatformType() {
                return platformType;
            }

            public void setPlatformType(int platformType) {
                this.platformType = platformType;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getYxAccid() {
                return yxAccid;
            }

            public void setYxAccid(String yxAccid) {
                this.yxAccid = yxAccid;
            }

            public int getBookId() {
                return bookId;
            }

            public void setBookId(int bookId) {
                this.bookId = bookId;
            }

            public int getPlatformId() {
                return platformId;
            }

            public void setPlatformId(int platformId) {
                this.platformId = platformId;
            }

            public String getPlatformName() {
                return platformName;
            }

            public void setPlatformName(String platformName) {
                this.platformName = platformName;
            }

            public String getPlatformCover() {
                return platformCover;
            }

            public void setPlatformCover(String platformCover) {
                this.platformCover = platformCover;
            }

            public String getPenName() {
                return penName;
            }

            public void setPenName(String penName) {
                this.penName = penName;
            }

            public String getPlatformIntroduce() {
                return RxTool.deleteHtmlChar(platformIntroduce);
            }

            public void setPlatformIntroduce(String platformIntroduce) {
                this.platformIntroduce = platformIntroduce;
            }

            public boolean isUnread() {
                return isUnread;
            }

            public void setUnread(boolean unread) {
                isUnread = unread;
            }

            public boolean isConcern() {
                return isConcern;
            }

            public void setConcern(boolean concern) {
                isConcern = concern;
            }
        }
    }
}
