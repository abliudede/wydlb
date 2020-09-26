package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;
import com.lianzai.reader.bean.PublicNumberBean;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.RxLogTool;

/**
 * Created by zhoujianghua on 2015/4/9.
 */
public class PublicNumberAttachment extends CustomAttachment {

    private String json;

    PublicNumberBean publicNumberBean;

    public PublicNumberAttachment() {
        super(CustomAttachmentType.PUBLIC_NUMBER);
    }

    @Override
    protected void parseData(JSONObject data) {
        json = data.toJSONString();
        try{
            //此处改成直接从JSONObject里取
            publicNumberBean = new PublicNumberBean();
            if (data.containsKey("content")){
                publicNumberBean.setContent(data.getString("content"));
            }
            if (data.containsKey("img")){
                publicNumberBean.setImg(data.getString("img"));
            }
            if (data.containsKey("title")){
                publicNumberBean.setTitle(data.getString("title"));
            }
            if (data.containsKey("bookId")){
                publicNumberBean.setBookId(data.getString("bookId"));
            }
            if (data.containsKey("url")){
                publicNumberBean.setUrl(data.getString("url"));
            }
            if (data.containsKey("chapterId")){
                publicNumberBean.setChapterId(data.getString("chapterId"));
            }
            if (data.containsKey("source")){
                publicNumberBean.setSource(data.getString("source"));
            }
            if (data.containsKey("platformName")){
                publicNumberBean.setPlatformName(data.getString("platformName"));
            }
            if (data.containsKey("teamId")){
                publicNumberBean.setTeamId(data.getString("teamId"));
            }
        }catch (Exception e){
            RxLogTool.e(e.toString());
        }
    }


    @Override
    protected JSONObject packData() {
        return null;
    }

    public PublicNumberBean getPublicNumberBean() {
        return publicNumberBean;
    }
}
