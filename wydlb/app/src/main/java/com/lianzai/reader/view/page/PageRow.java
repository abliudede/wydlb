package com.lianzai.reader.view.page;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: PageRow
 * Author: lrz
 * Date: 2018/11/12 11:27
 * Description: ${DESCRIPTION}
 */
public class PageRow {
    public String line;
    public int currentParagraphPos=0;//当前这行所在的段落

    public int pagePosition;//当前这行是属于那页

    public PageRow(String line, int currentParagraphPos) {
        this.line = line;
        this.currentParagraphPos = currentParagraphPos;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getCurrentParagraphPos() {
        return currentParagraphPos;
    }

    public void setCurrentParagraphPos(int currentParagraphPos) {
        this.currentParagraphPos = currentParagraphPos;
    }

    public int getPagePosition() {
        return pagePosition;
    }

    public void setPagePosition(int pagePosition) {
        this.pagePosition = pagePosition;
    }

    public PageRow() {
    }
}
