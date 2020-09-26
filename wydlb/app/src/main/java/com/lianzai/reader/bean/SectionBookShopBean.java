package com.lianzai.reader.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by lrz on 2017/12/5.
 */

public class SectionBookShopBean extends SectionEntity<BookShopBean.DataBean.CateNovelsListBean.GenresDataBean> {


    BookShopBean bean;

    String mContent;

    int mCategoryId;


    public SectionBookShopBean(boolean isHeader, String header, String content,int categoryId) {
        super(isHeader, header);
        mContent=content;
        mCategoryId=categoryId;
    }

    public String getmContent() {
        return mContent;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.mCategoryId = categoryId;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public SectionBookShopBean(BookShopBean.DataBean.CateNovelsListBean.GenresDataBean bean) {
        super(bean);
    }


}
