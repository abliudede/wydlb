package com.lianzai.reader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by newbiechen on 17-5-10.
 * 书的章节链接(作为下载的进度数据)
 * 同时作为网络章节和本地章节 (没有找到更好分离两者的办法)
 */
@Entity
public class BookChapterBean implements Serializable{
    private static final long serialVersionUID = 56423411313L;
    /**
     * title : 第一章 他叫白小纯
     * link : http://read.qidian.com/chapter/rJgN8tJ_cVdRGoWu-UQg7Q2/6jr-buLIUJSaGfXRMrUjdw2
     * unreadble : false
     */

    @Id(autoincrement = true)
    private Long _id;

    private String id;//章节ID

    private String link;

    private String title;

    //所属的下载任务
    private String taskName;

    private boolean unreadble;

    private boolean isVip;

    private String chapterSource;//当前章节的源key

    //所属的书籍
    @Index
    private String bookId;

    //本地书籍参数


    //在书籍文件中的起始位置
    private long start;

    //在书籍文件中的终止位置
    private long end;

    //章节的创建时间
    private String time;


    @Generated(hash = 1645169312)
    public BookChapterBean(Long _id, String id, String link, String title, String taskName, boolean unreadble,
            boolean isVip, String chapterSource, String bookId, long start, long end, String time) {
        this._id = _id;
        this.id = id;
        this.link = link;
        this.title = title;
        this.taskName = taskName;
        this.unreadble = unreadble;
        this.isVip = isVip;
        this.chapterSource = chapterSource;
        this.bookId = bookId;
        this.start = start;
        this.end = end;
        this.time = time;
    }

    @Generated(hash = 853839616)
    public BookChapterBean() {
    }

    public String getChapterSource() {
        return chapterSource;
    }

    public void setChapterSource(String chapterSource) {
        this.chapterSource = chapterSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean getUnreadble() {
        return this.unreadble;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return "BookChapterBean{" +
                "id='" + id + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", taskName='" + taskName + '\'' +
                ", unreadble=" + unreadble +
                ", isVip=" + isVip +
                ", chapterSource='" + chapterSource + '\'' +
                ", bookId='" + bookId + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean getIsVip() {
        return this.isVip;
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}