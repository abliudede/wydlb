package com.lianzai.reader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * 书架缓存数据
 */
@Entity
public class BookStoreBeanN implements Serializable {

    private static final long serialVersionUID = 536871008L;

    @Id(autoincrement = true)
    private Long id;

    private String bookId;
    private String platformId;
    private String platformName;
    private String platformCover;
    private String penName;
    private String platformIntroduce;

    private String bookName;
    private String bookCover;

    private int uid;

    private long indexTime;

    private  String source;

    private boolean isUnread;

    private boolean isSelect=false;

    private long updateDate=0;

    private int topIndex=0;

    private String yxAccid;

    private int platformType;

    private boolean isCopyright;

    @Generated(hash = 280862491)
    public BookStoreBeanN(Long id, String bookId, String platformId,
            String platformName, String platformCover, String penName,
            String platformIntroduce, String bookName, String bookCover, int uid,
            long indexTime, String source, boolean isUnread, boolean isSelect,
            long updateDate, int topIndex, String yxAccid, int platformType,
            boolean isCopyright) {
        this.id = id;
        this.bookId = bookId;
        this.platformId = platformId;
        this.platformName = platformName;
        this.platformCover = platformCover;
        this.penName = penName;
        this.platformIntroduce = platformIntroduce;
        this.bookName = bookName;
        this.bookCover = bookCover;
        this.uid = uid;
        this.indexTime = indexTime;
        this.source = source;
        this.isUnread = isUnread;
        this.isSelect = isSelect;
        this.updateDate = updateDate;
        this.topIndex = topIndex;
        this.yxAccid = yxAccid;
        this.platformType = platformType;
        this.isCopyright = isCopyright;
    }

    @Generated(hash = 1492556504)
    public BookStoreBeanN() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return this.platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformCover() {
        return this.platformCover;
    }

    public void setPlatformCover(String platformCover) {
        this.platformCover = platformCover;
    }

    public String getPenName() {
        return this.penName;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }

    public String getPlatformIntroduce() {
        return this.platformIntroduce;
    }

    public void setPlatformIntroduce(String platformIntroduce) {
        this.platformIntroduce = platformIntroduce;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCover() {
        return this.bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getIndexTime() {
        return this.indexTime;
    }

    public void setIndexTime(long indexTime) {
        this.indexTime = indexTime;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean getIsUnread() {
        return this.isUnread;
    }

    public void setIsUnread(boolean isUnread) {
        this.isUnread = isUnread;
    }

    public boolean getIsSelect() {
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public long getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public int getTopIndex() {
        return this.topIndex;
    }

    public void setTopIndex(int topIndex) {
        this.topIndex = topIndex;
    }

    public String getYxAccid() {
        return this.yxAccid;
    }

    public void setYxAccid(String yxAccid) {
        this.yxAccid = yxAccid;
    }

    public int getPlatformType() {
        return this.platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }

    public boolean getIsCopyright() {
        return this.isCopyright;
    }

    public void setIsCopyright(boolean isCopyright) {
        this.isCopyright = isCopyright;
    }




}
