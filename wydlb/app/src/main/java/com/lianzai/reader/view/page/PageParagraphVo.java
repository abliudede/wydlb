package com.lianzai.reader.view.page;

/**
 * Copyright (C), 2018
 * FileName: PageParagraphVo
 * Author: lrz
 * Date: 2018/11/13 17:26
 * Description: ${DESCRIPTION}
 */
public class PageParagraphVo {
    public int pagePosition;//段落属于页面的位置
    public int paragraphPosition;//段落位置
    public String text; //本段落文档

    public int getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }

    public int getParagraphPosition() {
        return paragraphPosition;
    }

    public void setParagraphPosition(int paragraphPosition) {
        this.paragraphPosition = paragraphPosition;
    }


    @Override
    public String toString() {
        return "PageParagraphVo{" +
                "pagePosition=" + pagePosition +
                ", paragraphPosition=" + paragraphPosition +
                '}';
    }
}

