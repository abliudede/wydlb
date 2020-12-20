package com.wydlb.first.bean;

import java.util.List;

public class ChapterDynamicBean {


    /**
     * code : 0
     * data : [{"address":"string","commentCount":0,"commentResp":{"auditStatus":0,"comUserId":0,"comUserName":"string","comUsrePic":"string","contentShow":"string","createTime":"2019-03-21T01:06:47.484Z","id":0,"isLike":false,"likeCount":0,"postId":0,"replyCount":0,"replyResp":{"auditStatus":0,"beReplyUserName":"string","contentShow":"string","createTime":"2019-03-21T01:06:47.484Z","id":0,"isLike":false,"likeCount":0,"parentId":0,"postId":0,"replyCount":0,"replyUserId":0,"replyUserName":"string","replyUsrePic":"string"}},"contentShow":"string","createTime":"2019-03-21T01:06:47.484Z","headList":["string"],"id":0,"images":["string"],"isLike":false,"latitude":"string","likeCount":0,"longitude":"string","picturesShow":"string","platformCover":"string","platformId":0,"platformName":"string","platformPeopleNumber":0,"postType":0,"rateImages":["string"],"thumbnailImages":["string"],"titleShow":"string","userId":0,"userName":"string","userPic":"string"}]
     * msg : string
     */

    private int code;
    private String msg;
    private List<CircleDynamicBean.DataBean.DynamicRespBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CircleDynamicBean.DataBean.DynamicRespBean> getData() {
        return data;
    }

    public void setData(List<CircleDynamicBean.DataBean.DynamicRespBean> data) {
        this.data = data;
    }

}
