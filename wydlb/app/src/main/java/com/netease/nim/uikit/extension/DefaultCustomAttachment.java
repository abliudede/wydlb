package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSONObject;
import com.lianzai.reader.bean.NormalMessageBean;
import com.lianzai.reader.utils.GsonUtil;

/**
 * Created by zhoujianghua on 2015/4/10.
 */
public class DefaultCustomAttachment extends CustomAttachment {

    private String json;

    NormalMessageBean normalMessageBean;

    public DefaultCustomAttachment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {
        json = data.toJSONString();
        try{
            normalMessageBean= GsonUtil.getBean(json,NormalMessageBean.class);
        }catch (Exception e){

        }
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = null;
        try {
            data = JSONObject.parseObject(json);
        } catch (Exception e) {

        }
        return data;
    }


    public NormalMessageBean getNormalMessageBean() {
        return normalMessageBean;
    }
}
