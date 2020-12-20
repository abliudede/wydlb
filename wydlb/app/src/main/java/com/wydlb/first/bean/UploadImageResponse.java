package com.wydlb.first.bean;

import com.google.gson.annotations.SerializedName;
import com.wydlb.first.base.Constant;

/**
 * Created by lrz on 2018/3/16.
 */

public class UploadImageResponse {

    /**
     * image-type : JPEG
     * image-frames : 1
     * image-height : 2248
     * code : 200
     * file_size : 438606
     * image-width : 250
     * url : bar_test/upload1521212393737.jpg
     * time : 1521212394
     * message : ok
     * mimetype : image/jpeg
     */

    @SerializedName("image-type")
    private String imagetype;
    @SerializedName("image-frames")
    private int imageframes;
    @SerializedName("image-height")
    private int imageheight;
    private int code;
    private int file_size;
    @SerializedName("image-width")
    private int imagewidth;
    private String url;
    private int time;
    private String message;
    private String mimetype;

    public String getImagetype() {
        return imagetype;
    }

    public void setImagetype(String imagetype) {
        this.imagetype = imagetype;
    }

    public int getImageframes() {
        return imageframes;
    }

    public void setImageframes(int imageframes) {
        this.imageframes = imageframes;
    }

    public int getImageheight() {
        return imageheight;
    }

    public void setImageheight(int imageheight) {
        this.imageheight = imageheight;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getFile_size() {
        return file_size;
    }

    public void setFile_size(int file_size) {
        this.file_size = file_size;
    }

    public int getImagewidth() {
        return imagewidth;
    }

    public void setImagewidth(int imagewidth) {
        this.imagewidth = imagewidth;
    }

    public String getUrl() {
        return Constant.UPAI_HTTP_HEADER+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
}
