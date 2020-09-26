package com.lianzai.reader.view.page;

import com.lianzai.reader.bean.BookCategoryListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newbiechen on 17-7-1.
 */

public class TxtChapter{

    //章节所属的小说(网络)
    String bookId;
    //章节的链接(网络)
    String link;

    //章节名(共用)
    String title;

    String chapterId;

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    private String sourceKey;

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    private  boolean chapterVip;

    public boolean isChapterVip() {
        return chapterVip;
    }

    public void setChapterVip(boolean chapterVip) {
        this.chapterVip = chapterVip;
    }


    //章节内容在文章中的起始位置(本地)
    long start;
    //章节内容在文章中的终止位置(本地)
    long end;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String id) {
        this.bookId = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }


    @Override
    public String toString() {
        return "TxtChapter{" +
                "bookId='" + bookId + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", chapterId='" + chapterId + '\'' +
                ", sourceKey='" + sourceKey + '\'' +
                ", chapterVip=" + chapterVip +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
