package com.lianzai.reader.view.page;

import com.lianzai.reader.utils.StringUtils;

import java.util.List;

/**
 * Created by newbiechen on 17-7-1.
 */

public class TxtPage {
    public int position;
    public String title;
    public int titleLines; //当前 lines 中为 title 的行数。

    public List<PageRow>pageRows;

    public int paraCount;//当前 页的总段落数

    public int totalWordCount;//当前页面的字数


    @Override
    public String toString() {
        return "TxtPage{" +
                "position=" + position +
                ", title='" + title + '\'' +
                ", titleLines=" + titleLines +
                ", paraCount=" + paraCount +
                '}';
    }
}

