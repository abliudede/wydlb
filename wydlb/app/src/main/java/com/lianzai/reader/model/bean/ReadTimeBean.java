package com.lianzai.reader.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class ReadTimeBean implements Serializable {
    private static final long serialVersionUID = 0L;

    @Id(autoincrement = true)
    private Long _id;

    private int bookId;
    private int chapterId;
    private int clickCount;
    private int font;
    private Long endTime;
    private Long startTime;
    private String phoneModel;
    private String turnCoordinate;
    private int turnCount;

    private String angle;

    private String userId;

    private int readType;

    private int pageNum;

    private String deviceCode;

    private int pageCount;

    private String source;

    private String chapterTitle;

    @Generated(hash = 1822611140)
    public ReadTimeBean(Long _id, int bookId, int chapterId, int clickCount,
            int font, Long endTime, Long startTime, String phoneModel,
            String turnCoordinate, int turnCount, String angle, String userId,
            int readType, int pageNum, String deviceCode, int pageCount,
            String source, String chapterTitle) {
        this._id = _id;
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.clickCount = clickCount;
        this.font = font;
        this.endTime = endTime;
        this.startTime = startTime;
        this.phoneModel = phoneModel;
        this.turnCoordinate = turnCoordinate;
        this.turnCount = turnCount;
        this.angle = angle;
        this.userId = userId;
        this.readType = readType;
        this.pageNum = pageNum;
        this.deviceCode = deviceCode;
        this.pageCount = pageCount;
        this.source = source;
        this.chapterTitle = chapterTitle;
    }

    @Generated(hash = 1664191103)
    public ReadTimeBean() {
    }

    public int getBookId() {
        return this.bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getChapterId() {
        return this.chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getClickCount() {
        return this.clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public int getFont() {
        return this.font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getPhoneModel() {
        return this.phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getTurnCoordinate() {
        return this.turnCoordinate;
    }

    public void setTurnCoordinate(String turnCoordinate) {
        this.turnCoordinate = turnCoordinate;
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public String getAngle() {
        return this.angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getReadType() {
        return this.readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getDeviceCode() {
        return this.deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getChapterTitle() {
        return this.chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }





}
