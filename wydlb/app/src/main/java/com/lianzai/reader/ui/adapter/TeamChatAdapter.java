package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.teamholder.IMAudioInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMAudioOutViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMDiyTipsViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMEmjPictureInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMEmjPictureOutViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMFightLuckInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMFightLuckOutViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMLianzaihaoInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMLianzaihaoOutViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMNotificationViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMOutUnknowViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMPictureInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMPictureOutViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMTextInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMTextOutViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMTipsViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMUnknowViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMUrlBookInViewHolder;
import com.lianzai.reader.ui.adapter.teamholder.IMUrlBookOutViewHolder;
import com.lianzai.reader.utils.DemoCache;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.DiyTipsBean;
import com.netease.nim.uikit.extension.FightLuckBean;
import com.netease.nim.uikit.extension.ImLianzaihaoAttachment;
import com.netease.nim.uikit.extension.StickerAttachment;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 聊天室
 */

public class TeamChatAdapter extends BaseMultiItemFetchLoadAdapter<IMMessage, BaseViewHolder> {

    interface ViewType {
        int VIEW_TYPE_UN_KNOW_IN = 0;
        int VIEW_TYPE_UN_KNOW_OUT = -1;

        int VIEW_TYPE_TEXT_IN = 1;
        int VIEW_TYPE_TEXT_OUT = 2;

        int VIEW_TYPE_TIPS = 3;

        int VIEW_TYPE_PICTURE_IN = 4;
        int VIEW_TYPE_PICTURE_OUT = 5;

        int VIEW_TYPE_AUDIO_IN = 6;
        int VIEW_TYPE_AUDIO_OUT = 7;

        int VIEW_TYPE_VIDEO_IN = 8;
        int VIEW_TYPE_VIDEO_OUT = 9;

        int VIEW_TYPE_LIANZAIHAO_IN = 10;
        int VIEW_TYPE_LIANZAIHAO_OUT = 11;

        int VIEW_TYPE_EMJ_PICTURE_IN = 12;
        int VIEW_TYPE_EMJ_PICTURE_OUT = 13;

        int VIEW_TYPE_NOTIFICATION = 14;

        int VIEW_TYPE_FIGHTLUCK_IN = 15;
        int VIEW_TYPE_FIGHTLUCK_OUT = 16;

        int VIEW_TYPE_DIY_TIPS = 17;

        int VIEW_TYPE_URLBOOK_IN = 18;
        int VIEW_TYPE_URLBOOK_OUT = 19;
    }

    private Set<String> timedItems; // 需要显示消息时间的消息ID

    private IMMessage lastShowTimeItem; // 用于消息时间显示,判断和上条消息间的时间间隔

    private Map<String, Float> progresses; // 有文件传输，需要显示进度条的消息ID map


    ChatItemClickListener chatItemClickListener;

    public TeamChatAdapter(RecyclerView recyclerView, List<IMMessage> data, ChatItemClickListener listener) {
        super(recyclerView, data);

        timedItems = new HashSet<>();
        progresses = new HashMap<>();

        addItemType(ViewType.VIEW_TYPE_TEXT_IN, R.layout.im_item_text_in, IMTextInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_TEXT_OUT, R.layout.im_item_text_out, IMTextOutViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_TIPS, R.layout.im_item_tips, IMTipsViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_PICTURE_IN, R.layout.im_item_picture_in, IMPictureInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_PICTURE_OUT, R.layout.im_item_picture_out, IMPictureOutViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_AUDIO_IN, R.layout.im_item_audio_in, IMAudioInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_AUDIO_OUT, R.layout.im_item_audio_out, IMAudioOutViewHolder.class);

//        addItemType(ViewType.VIEW_TYPE_VIDEO_IN, R.layout.im_item_video_in, IMVideoInViewHolder.class);
//        addItemType(ViewType.VIEW_TYPE_VIDEO_OUT, R.layout.im_item_video_out, IMVideoOutViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_LIANZAIHAO_IN, R.layout.im_item_lianzaihao_in, IMLianzaihaoInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_LIANZAIHAO_OUT, R.layout.im_item_lianzaihao_out, IMLianzaihaoOutViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_FIGHTLUCK_IN, R.layout.im_item_fightluck_in, IMFightLuckInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_FIGHTLUCK_OUT, R.layout.im_item_fightluck_out, IMFightLuckOutViewHolder.class);

        //url书籍类型
        addItemType(ViewType.VIEW_TYPE_URLBOOK_IN, R.layout.im_item_urlbook_in, IMUrlBookInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_URLBOOK_OUT, R.layout.im_item_urlbook_out, IMUrlBookOutViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_EMJ_PICTURE_IN, R.layout.im_item_emj_picture_in, IMEmjPictureInViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_EMJ_PICTURE_OUT, R.layout.im_item_emj_picture_out, IMEmjPictureOutViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_UN_KNOW_IN, R.layout.im_item_unknow_in, IMUnknowViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_UN_KNOW_OUT, R.layout.im_item_unknow_out, IMOutUnknowViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_NOTIFICATION, R.layout.im_item_notification, IMNotificationViewHolder.class);

        addItemType(ViewType.VIEW_TYPE_DIY_TIPS, R.layout.im_item_diy_tips, IMDiyTipsViewHolder.class);

        chatItemClickListener=listener;

    }


    public Context getContext(){
        return mContext;
    }



    @Override
    protected int getViewType(IMMessage item) {
        //排除友盟异常情况，云信内部错误
        if(null == item.getFromAccount()){
            return ViewType.VIEW_TYPE_UN_KNOW_OUT;
        }

        if (!item.getFromAccount().equals(DemoCache.getAccount())) {//收消息
            if (item.getMsgType() == MsgTypeEnum.text) {//普通消息
                return ViewType.VIEW_TYPE_TEXT_IN;
            } else if (item.getMsgType() == MsgTypeEnum.audio) {//语音
                return ViewType.VIEW_TYPE_AUDIO_IN;
            } else if (item.getMsgType() == MsgTypeEnum.image) {//图片
                return ViewType.VIEW_TYPE_PICTURE_IN;
            } else if (item.getMsgType() == MsgTypeEnum.tip) {//tip消息
                return ViewType.VIEW_TYPE_TIPS;
            } else if (item.getMsgType() == MsgTypeEnum.video) {//视频
                return ViewType.VIEW_TYPE_VIDEO_IN;
            } else if (item.getMsgType() == MsgTypeEnum.custom) {//自定义消息-连载号名片 -emj大图表情
                if (null!=item.getAttachment()&&item.getAttachment() instanceof StickerAttachment){
                    return ViewType.VIEW_TYPE_EMJ_PICTURE_IN;
                }else if(null!=item.getAttachment()&&item.getAttachment() instanceof ImLianzaihaoAttachment){//连载号
                    return ViewType.VIEW_TYPE_LIANZAIHAO_IN;
                }else if(null!=item.getAttachment()&&item.getAttachment() instanceof FightLuckBean){//拼手氣
                    return ViewType.VIEW_TYPE_FIGHTLUCK_IN;
                } else if(null!=item.getAttachment()&&item.getAttachment() instanceof UrlBookBean){//url书籍
                    return ViewType.VIEW_TYPE_URLBOOK_IN;
                } else if(null!=item.getAttachment()&&item.getAttachment() instanceof DiyTipsBean){//自定义TIP消息
                    return ViewType.VIEW_TYPE_DIY_TIPS;
                }else{
                    return ViewType.VIEW_TYPE_UN_KNOW_IN;//未知类型
                }

            }else if(item.getMsgType()==MsgTypeEnum.notification) {// notification
                return ViewType.VIEW_TYPE_NOTIFICATION;
            }else {
                return ViewType.VIEW_TYPE_UN_KNOW_IN;
            }

        } else if (item.getFromAccount().equals(DemoCache.getAccount())) { //发消息
            if (item.getMsgType() == MsgTypeEnum.text) {//普通消息
                return ViewType.VIEW_TYPE_TEXT_OUT;
            } else if (item.getMsgType() == MsgTypeEnum.audio) {//语音
                return ViewType.VIEW_TYPE_AUDIO_OUT;
            } else if (item.getMsgType() == MsgTypeEnum.image) {//图片
                return ViewType.VIEW_TYPE_PICTURE_OUT;
            } else if (item.getMsgType() == MsgTypeEnum.tip) {//tip消息
                return ViewType.VIEW_TYPE_TIPS;
            } else if (item.getMsgType() == MsgTypeEnum.video) {//视频
                return ViewType.VIEW_TYPE_VIDEO_OUT;
            } else if (item.getMsgType() == MsgTypeEnum.custom) {//自定义消息-连载号名片 -emj大图表情
                if (null!=item.getAttachment()&&item.getAttachment() instanceof StickerAttachment){
                    return ViewType.VIEW_TYPE_EMJ_PICTURE_OUT;
                }else if(null!=item.getAttachment()&&item.getAttachment() instanceof ImLianzaihaoAttachment){//连载号
                    return ViewType.VIEW_TYPE_LIANZAIHAO_OUT;
                }else if(null!=item.getAttachment()&&item.getAttachment() instanceof FightLuckBean){//拼手氣
                    return ViewType.VIEW_TYPE_FIGHTLUCK_OUT;
                }else if(null!=item.getAttachment()&&item.getAttachment() instanceof UrlBookBean){//url书籍
                    return ViewType.VIEW_TYPE_URLBOOK_OUT;
                } else if(null!=item.getAttachment()&&item.getAttachment() instanceof DiyTipsBean){//自定义TIP消息
                    return ViewType.VIEW_TYPE_DIY_TIPS;
                }else{
                    return ViewType.VIEW_TYPE_UN_KNOW_OUT;//未知类型
                }
            } else if(item.getMsgType()==MsgTypeEnum.notification) {// notification
                return ViewType.VIEW_TYPE_NOTIFICATION;
            }else {
                return ViewType.VIEW_TYPE_UN_KNOW_OUT;
            }
        }
        return ViewType.VIEW_TYPE_UN_KNOW_OUT;
    }

    @Override
    protected String getItemKey(IMMessage item) {
        return item.getUuid();
    }


    public float getProgress(IMMessage message) {
        Float progress = progresses.get(message.getUuid());
        return progress == null ? 0 : progress;
    }

    public void putProgress(IMMessage message, float progress) {
        progresses.put(message.getUuid(), progress);
    }


    public boolean needShowTime(IMMessage message) {
        return timedItems.contains(message.getUuid());
    }

    /**
     * 列表加入新消息时，更新时间显示
     */
    public void updateShowTimeItem(List<IMMessage> items, boolean fromStart, boolean update) {
        IMMessage anchor = fromStart ? null : lastShowTimeItem;
        for (IMMessage message : items) {
            if (setShowTimeFlag(message, anchor)) {
                anchor = message;
            }
        }
        if (update) {
            lastShowTimeItem = anchor;
        }
    }

    /**
     * 是否显示时间item
     */
    private boolean setShowTimeFlag(IMMessage message, IMMessage anchor) {
        boolean update = false;

        if (hideTimeAlways(message)) {
            setShowTime(message, false);
        } else {
            if (anchor == null) {
                setShowTime(message, true);
                update = true;
            } else {
                long time = anchor.getTime();
                long now = message.getTime();

                if (now - time == 0) {
                    // 消息撤回时使用
                    setShowTime(message, true);
                    lastShowTimeItem = message;
                    update = true;
                } else if (now - time < (long) (5 * 60 * 1000)) {
                    setShowTime(message, false);
                } else {
                    setShowTime(message, true);
                    update = true;
                }
            }
        }

        return update;
    }

    private void setShowTime(IMMessage message, boolean show) {
        if (show) {
            timedItems.add(message.getUuid());
        } else {
            timedItems.remove(message.getUuid());
        }
    }

    public void deleteItem(IMMessage message, boolean isRelocateTime) {
        if (message == null) {
            return;
        }

        int index = 0;
        for (IMMessage item : getData()) {
            if (item.isTheSame(message)) {
                break;
            }
            ++index;
        }

        if (index < getDataSize()) {
            remove(index);
            if (isRelocateTime) {
                relocateShowTimeItemAfterDelete(message, index);
            }
//            notifyDataSetChanged(); // 可以不要！！！
        }
    }

    private void relocateShowTimeItemAfterDelete(IMMessage messageItem, int index) {
        // 如果被删的项显示了时间，需要继承
        if (needShowTime(messageItem)) {
            setShowTime(messageItem, false);
            if (getDataSize() > 0) {
                IMMessage nextItem;
                if (index == getDataSize()) {
                    //删除的是最后一项
                    nextItem = getItem(index - 1);
                } else {
                    //删除的不是最后一项
                    nextItem = getItem(index);
                }

                // 增加其他不需要显示时间的消息类型判断
                if (hideTimeAlways(nextItem)) {
                    setShowTime(nextItem, false);
                    if (lastShowTimeItem != null && lastShowTimeItem != null
                            && lastShowTimeItem.isTheSame(messageItem)) {
                        lastShowTimeItem = null;
                        for (int i = getDataSize() - 1; i >= 0; i--) {
                            IMMessage item = getItem(i);
                            if (needShowTime(item)) {
                                lastShowTimeItem = item;
                                break;
                            }
                        }
                    }
                } else {
                    setShowTime(nextItem, true);
                    if (lastShowTimeItem == null
                            || (lastShowTimeItem != null && lastShowTimeItem.isTheSame(messageItem))) {
                        lastShowTimeItem = nextItem;
                    }
                }
            } else {
                lastShowTimeItem = null;
            }
        }
    }

    private boolean hideTimeAlways(IMMessage message) {
        if (message.getSessionType() == SessionTypeEnum.ChatRoom) {
            return false;
        }
        switch (message.getMsgType()) {
            case notification:
                return true;
            default:
                return false;
        }
    }


    public ChatItemClickListener getChatItemClickListener() {
        return chatItemClickListener;
    }

    public void setChatItemClickListener(ChatItemClickListener chatItemClickListener) {
        this.chatItemClickListener = chatItemClickListener;
    }

    public interface ChatItemClickListener{
        void showImageClick(View view, int pos, IMMessage iMMessage);
        void lianzaihaoClick(String platformId);
        void fightLuckClick(int gameId);
        void contentLongClick(View view, int pos, IMMessage iMMessage, int x, int y);
        void contentLongClick(View view, int pos, IMMessage iMMessage);
        void userLogoClick(IMMessage iMMessage);
        void retrySendMsgClick(IMMessage iMMessage);
    }

}
