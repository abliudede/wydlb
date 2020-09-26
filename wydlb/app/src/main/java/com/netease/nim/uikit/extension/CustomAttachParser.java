package com.netease.nim.uikit.extension;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lianzai.reader.utils.RxLogTool;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

public class CustomAttachParser implements MsgAttachmentParser {

    private static final String KEY_TYPE = "newsType";
    private static final String KEY_DATA = "data";

    @Override
    public MsgAttachment parse(String json) {
        CustomAttachment attachment = null;
        try {
            JSONObject object = JSON.parseObject(json);
            int type = 0;
            if (object.containsKey(KEY_TYPE)){
                type=object.getInteger(KEY_TYPE);
            }
            RxLogTool.e("MsgAttachment type--"+type);
            JSONObject data = object.getJSONObject(KEY_DATA);

            if (type>0&&type<CustomAttachmentType.Sticker){
                attachment = new PublicNumberAttachment();
                attachment.setNewsType(type);
            }else if(type==CustomAttachmentType.Sticker){
                attachment = new StickerAttachment();
            }else if(type==CustomAttachmentType.LIANZAIHAO){
                attachment = new ImLianzaihaoAttachment();
            }else if(type==CustomAttachmentType.FIGHTLUCK){
                attachment = new FightLuckBean();
            }else if(type==CustomAttachmentType.URLBOOK){
                attachment = new UrlBookBean();
            }else if(type==CustomAttachmentType.DIYTIPS1){
                attachment = new DiyTipsBean(CustomAttachmentType.DIYTIPS1);
            }else if(type==CustomAttachmentType.DIYTIPS2){
                attachment = new DiyTipsBean(CustomAttachmentType.DIYTIPS2);
            }else{
                attachment=new DefaultCustomAttachment();
            }

            if (attachment != null) {
                RxLogTool.e("MsgAttachment parse--"+data.toString());
                attachment.fromJson(data);
            }
        } catch (Exception e) {
            RxLogTool.e("MsgAttachment parse--"+e.getMessage());

        }

        return attachment;
    }

    public static String packData(int type, JSONObject data) {
        JSONObject object = new JSONObject();
        object.put(KEY_TYPE, type);
        if (data != null) {
            object.put(KEY_DATA, data);
        }

        return object.toJSONString();
    }
}
