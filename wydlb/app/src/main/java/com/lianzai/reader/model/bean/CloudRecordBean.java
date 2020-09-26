package com.lianzai.reader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class CloudRecordBean {

    @Unique
    private long bookId;
    private long chapterId;
    private String createdAt;
    private long id;
    private String updatedAt;
    private long userId;
    private String chapterTitle;
    private String source;
    private int pos;
    private int chapter;
    @Generated(hash = 1723484140)
    public CloudRecordBean(long bookId, long chapterId, String createdAt, long id,
            String updatedAt, long userId, String chapterTitle, String source,
            int pos, int chapter) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.createdAt = createdAt;
        this.id = id;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.chapterTitle = chapterTitle;
        this.source = source;
        this.pos = pos;
        this.chapter = chapter;
    }
    @Generated(hash = 1824021472)
    public CloudRecordBean() {
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public long getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }
    public String getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUpdatedAt() {
        return this.updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getChapterTitle() {
        return this.chapterTitle;
    }
    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }
    public String getSource() {
        return this.source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public int getPos() {
        return this.pos;
    }
    public void setPos(int pos) {
        this.pos = pos;
    }
    public int getChapter() {
        return this.chapter;
    }
    public void setChapter(int chapter) {
        this.chapter = chapter;
    }
  



}
