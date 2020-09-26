package com.netease.nim.uikit.business.recent.holder;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.RxLogTool;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.extension.DefaultCustomAttachment;
import com.netease.nim.uikit.extension.PublicNumberAttachment;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

public class CommonRecentViewHolder extends RecentViewHolder {

    CommonRecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    protected String getContent(RecentContact recent) {
        return descOfMsg(recent);
    }

    @Override
    protected String getOnlineStateContent(RecentContact recent) {
        if (recent.getSessionType() == SessionTypeEnum.P2P && NimUIKitImpl.enableOnlineState()) {
            return NimUIKitImpl.getOnlineStateContentProvider().getSimpleDisplay(recent.getContactId());
        } else {
            return super.getOnlineStateContent(recent);
        }
    }

    String descOfMsg(RecentContact recent) {
        if (recent.getMsgType() == MsgTypeEnum.text) {
            RxLogTool.e("MsgTypeEnum.text--"+recent.getContent()+"--"+recent.getFromAccount());
            return recent.getContent();
        } else if (recent.getMsgType() == MsgTypeEnum.tip) {
            String digest = null;
            if (getCallback() != null) {
                digest = getCallback().getDigestOfTipMsg(recent);
            }

            if (digest == null) {
                digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent);
            }

            return digest;
        } else if (recent.getAttachment() != null) {
            if (recent.getAttachment() instanceof PublicNumberAttachment){//连载号
                PublicNumberAttachment publicNumberAttachment=(PublicNumberAttachment)recent.getAttachment();
                //判空操作
                if(null == publicNumberAttachment.getPublicNumberBean()){
                    return "";
                }
                if (publicNumberAttachment.getNewsType()== Constant.EXTENDFIELD.LOCAL_CHAPTER_UPADTE_TYPE||publicNumberAttachment.getNewsType()== Constant.EXTENDFIELD.OUTSIDE_CHAPTER_UPADTE_TYPE){
                    return "「章节有更新」"+publicNumberAttachment.getPublicNumberBean().getTitle();
                }else{
                    return publicNumberAttachment.getPublicNumberBean().getTitle();
                }
            }else if(recent.getAttachment() instanceof DefaultCustomAttachment){
                DefaultCustomAttachment defaultCustomAttachment=(DefaultCustomAttachment)recent.getAttachment();
                return defaultCustomAttachment.getNormalMessageBean().getContent();
            }
            String digest = null;
            if (getCallback() != null) {
                digest = getCallback().getDigestOfAttachment(recent, recent.getAttachment());
            }

            if (digest == null) {
                digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent);
            }

            return digest;
        }

        return "[未知]";
    }
}
