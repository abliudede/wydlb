package com.lianzai.reader.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lianzai.reader.ui.adapter.BookRecordExpandableItemAdapter;

import java.io.Serializable;
import java.util.List;

public class BookRecordHistoryResponse {

    /**
     * code : 0
     * msg : success
     * data : {"total":1,"list":[{"day":"2018-07-23","total":30,"list":[{"userId":16660,"bookId":88,"readMinute":30}]}],"pageNum":1,"pageSize":10,"pages":1,"size":1}
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
         * total : 1
         * list : [{"day":"2018-07-23","total":30,"list":[{"userId":16660,"bookId":88,"readMinute":30}]}]
         * pageNum : 1
         * pageSize : 10
         * pages : 1
         * size : 1
         */

        private int total;
        private int pageNum;
        private int pageSize;
        private int pages;
        private int size;
        private List<ListBeanX> list;

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

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public static class ListBeanX extends AbstractExpandableItem<ListBeanX.ListBean> implements Serializable,MultiItemEntity {
            /**
             * day : 2018-07-23
             * total : 30
             * list : [{"userId":16660,"bookId":88,"readMinute":30}]
             */

            @Override
            public int getLevel() {
                return 1;
            }

            @Override
            public int getItemType() {
                return BookRecordExpandableItemAdapter.DATE;
            }

            private String day;
            private int total;
            private List<ListBean> list;

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean implements Serializable,MultiItemEntity{
                /**
                 * userId : 16660
                 * bookId : 88
                 * readMinute : 30
                 */

                @Override
                public int getItemType() {
                    return BookRecordExpandableItemAdapter.CONTENT;
                }

                private int userId;
                private int bookId;
                private Double readMinute;
                private String name;
                private String cover;
                private boolean isConcern;
                private String platformId;

                private String source;

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getPlatformId() {
                    return platformId;
                }

                public void setPlatformId(String platformId) {
                    this.platformId = platformId;
                }

                public boolean isConcern() {
                    return isConcern;
                }

                public void setConcern(boolean concern) {
                    isConcern = concern;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getCover() {
                    return cover;
                }

                public void setCover(String cover) {
                    this.cover = cover;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }

                public int getBookId() {
                    return bookId;
                }

                public void setBookId(int bookId) {
                    this.bookId = bookId;
                }

                public int getReadMinute() {
                    try{
                        return (int)Math.floor(readMinute);
                    }catch (Exception e){

                    }
                    return 0;
                }

                public void setReadMinute(Double readMinute) {
                    this.readMinute = readMinute;
                }
            }
        }
    }
}
