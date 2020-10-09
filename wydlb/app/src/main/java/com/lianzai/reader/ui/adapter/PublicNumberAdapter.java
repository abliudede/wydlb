package com.lianzai.reader.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.ui.adapter.holder.PublicNumberChapterViewHolder;
import com.lianzai.reader.ui.adapter.holder.PublicNumberJoinTeamChatViewHolder;
import com.lianzai.reader.ui.adapter.holder.PublicNumberMessageViewHolder;
import com.lianzai.reader.ui.adapter.holder.PublicNumberNormalViewHolder;
import com.lianzai.reader.ui.adapter.holder.PublicNumberNoticeViewHolder;
import com.lianzai.reader.ui.adapter.holder.PublicNumberOpenChatRoomViewHolder;
import com.lianzai.reader.ui.adapter.holder.PublicNumberUnknowViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.PublicNumberAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * 连载号
 */

public class PublicNumberAdapter extends BaseMultiItemQuickAdapter<IMMessage, BaseViewHolder> {

    PublicNumberOptionCallback publicNumberOptionCallback;

    interface ViewType {
        int VIEW_TYPE_NORMAL = 1;
        int VIEW_TYPE_CHAPTER = 2;
        int VIEW_TYPE_NOTICE = 3;
        int VIEW_TYPE_MESSAGE = 4;
        int VIEW_TYPE_UNKNOW = 5;
        int VIEW_TYPE_OPEN_CHAT_ROOM = 6;
        int VIEW_TYPE_JOIN_TEAM_MESSAGE = 7;
    }

    public PublicNumberOptionCallback getPublicNumberOptionCallback() {
        return publicNumberOptionCallback;
    }

    public void setPublicNumberOptionCallback(PublicNumberOptionCallback publicNumberOptionCallback) {
        this.publicNumberOptionCallback = publicNumberOptionCallback;
    }

    public PublicNumberAdapter(RecyclerView recyclerView, List<IMMessage> data) {
        super(recyclerView, data);
        addItemType(ViewType.VIEW_TYPE_NORMAL, R.layout.item_public_number_normal, PublicNumberNormalViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_CHAPTER, R.layout.item_public_number_chapter, PublicNumberChapterViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_NOTICE, R.layout.item_public_number_notice, PublicNumberNoticeViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_MESSAGE, R.layout.item_public_number_message, PublicNumberMessageViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_UNKNOW, R.layout.item_public_number_unknow, PublicNumberUnknowViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_OPEN_CHAT_ROOM, R.layout.item_chat_room_open_notice, PublicNumberOpenChatRoomViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_JOIN_TEAM_MESSAGE, R.layout.item_chat_room_open_notice, PublicNumberJoinTeamChatViewHolder.class);
    }

    @Override
    protected int getViewType(IMMessage item) {

        if (item.getMsgType() == MsgTypeEnum.text){
            return ViewType.VIEW_TYPE_NORMAL;
        }else {
            if (null!=item.getAttachment()&&item.getAttachment() instanceof PublicNumberAttachment){
                PublicNumberAttachment attachment=(PublicNumberAttachment)item.getAttachment();
                if (attachment.getNewsType()==Constant.EXTENDFIELD.LOCAL_CHAPTER_UPADTE_TYPE||attachment.getNewsType()==Constant.EXTENDFIELD.OUTSIDE_CHAPTER_UPADTE_TYPE){//书连载号
                    return ViewType.VIEW_TYPE_CHAPTER;
                }else if (attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_COMMUNICATION_TYPE || attachment.getNewsType()==Constant.EXTENDFIELD.TEAM_MANAGE){//通知
                    return ViewType.VIEW_TYPE_MESSAGE;
                }else if(attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_NOTICE||attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_NOSHARE||attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_WEEKLY){//公告
                    return ViewType.VIEW_TYPE_NOTICE;
                }else if(attachment.getNewsType()==Constant.EXTENDFIELD.OPEN_CHAT_ROOM_MESSAGE){
                    return ViewType.VIEW_TYPE_OPEN_CHAT_ROOM;
                }else if(attachment.getNewsType()==Constant.EXTENDFIELD.JOIN_TEAM_MESSAGE){
                    return ViewType.VIEW_TYPE_JOIN_TEAM_MESSAGE;
                }else{//未知消息
                    return ViewType.VIEW_TYPE_UNKNOW;
                }
            }else{
                return ViewType.VIEW_TYPE_UNKNOW;
            }
        }
    }

    @Override
    protected String getItemKey(IMMessage item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getSessionType().getValue()).append("_").append(item.getSessionId());
        return sb.toString();
    }

    public interface PublicNumberOptionCallback{
        void openChapterByChapterTitle(String bookId,String chapterName);
        void openWebUrl(String url,String tit,String des,String img ,int type);
        void avatarClick();
        void joinTeamChat(String teamId);
    }

}
