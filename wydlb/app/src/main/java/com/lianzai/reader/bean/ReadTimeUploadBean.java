package com.lianzai.reader.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadTimeUploadBean {

    private String date;
    private int uid;
    private Boolean isChange;//value = "是否有变化 0没有 1有变化"
    private String deviceCode;
    private String phoneModel;
    private int readType;//value = "1：看书，2：离线看书，3：听书"

    private List<ReadInfoDtoSon> readInfoDtoSons;

    public ReadInfoDtoSon contain(int bid){
        List<ReadInfoDtoSon> listtemp = getReadInfoDtoSons();
        if(null == listtemp){
            //保证readinfo不为空
            setReadInfoDtoSons(new ArrayList<>());
            return null;
        }
        if(listtemp.isEmpty()){
            return null;
        }

        ReadInfoDtoSon contain = null;
        for(ReadInfoDtoSon item : listtemp){
            if(item.getBookId() == bid){
                contain = item;
                break;
            }
        }

        if(null != contain){
            listtemp.remove(contain);
            setReadInfoDtoSons(listtemp);
        }

        return contain;
    }

    public void add(ReadInfoDtoSon info){
        List<ReadInfoDtoSon> listtemp = getReadInfoDtoSons();
        if(null == listtemp){
            //保证readinfo不为空
            listtemp = new ArrayList<>();
        }
        listtemp.add(info);
        setReadInfoDtoSons(listtemp);
    }

    public static class ReadInfoDtoSon {
        private int bookId;
        private int second;
        private String startTime;
        private int pageNum;
        private int pageCount;
        private int chapterId;

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getChapterId() {
            return chapterId;
        }

        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Boolean getChange() {
        return isChange;
    }

    public void setChange(Boolean change) {
        isChange = change;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public int getReadType() {
        return readType;
    }

    public void setReadType(int readType) {
        this.readType = readType;
    }

    public List<ReadInfoDtoSon> getReadInfoDtoSons() {
        return readInfoDtoSons;
    }

    public void setReadInfoDtoSons(List<ReadInfoDtoSon> readInfoDtoSons) {
        this.readInfoDtoSons = readInfoDtoSons;
    }
}
