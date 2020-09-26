package com.lianzai.reader.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lianzai.reader.ui.adapter.BookRecordExpandableItemAdapter;

import java.io.Serializable;
import java.util.List;

public class WebHistoryListBean extends AbstractExpandableItem<WebHistoryListBean.ListBean> implements Serializable,MultiItemEntity {
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
        private List<WebHistoryListBean.ListBean> list;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }


        public List<WebHistoryListBean.ListBean> getList() {
            return list;
        }

        public void setList(List<WebHistoryListBean.ListBean> list) {
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


            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
