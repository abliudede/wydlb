package com.wydlb.first.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class BookShopMultipleItem implements MultiItemEntity {
    public static final int TITLE = 1;
    public static final int BOOKS = 2;
    private int itemType;
    private int spanSize;

    public static int getTITLE() {
        return TITLE;
    }

    public static int getBooks() {
        return BOOKS;
    }

    public BookShopMultipleItem(int itemType, int spanSize, String content) {
        this.itemType = itemType;
        this.spanSize = spanSize;
        this.content = content;
    }

    public BookShopMultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
