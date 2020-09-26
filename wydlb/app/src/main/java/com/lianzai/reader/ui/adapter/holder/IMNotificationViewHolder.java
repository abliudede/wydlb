package com.lianzai.reader.ui.adapter.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.netease.nim.uikit.business.chatroom.helper.ChatRoomNotificationHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;

public class IMNotificationViewHolder extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, ChatRoomMessage> {


    TextView im_tv_notification;

    TextView im_tv_timeline;

    ChatRoomAdapter chatRoomAdapter;

    RelativeLayout rl_notification;


    public IMNotificationViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        chatRoomAdapter=(ChatRoomAdapter)adapter;
    }


    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        im_tv_notification=holder.getView(R.id.im_tv_notification);
        im_tv_timeline=holder.getView(R.id.im_tv_timeline);
        rl_notification=holder.getView(R.id.rl_notification);


        ChatRoomNotificationAttachment notificationAttachment=(ChatRoomNotificationAttachment)data.getAttachment();

        im_tv_timeline.setText(TimeUtil.getTimeShowString(data.getTime(), false));

        if (chatRoomAdapter.needShowTime(data)){
            im_tv_timeline.setVisibility(View.VISIBLE);
        }else{
            im_tv_timeline.setVisibility(View.GONE);
        }

        if (null!=notificationAttachment){
            try{
                im_tv_notification.setText(ChatRoomNotificationHelper.getNotificationText(notificationAttachment));
            }catch (Exception e){
//                e.printStackTrace();
            }

//            if (notificationAttachment.getType()== NotificationType.ChatRoomMemberIn){
//                rl_notification.setVisibility(View.GONE);
//            }else if(notificationAttachment.getType()==NotificationType.ChatRoomMemberExit){
//                rl_notification.setVisibility(View.GONE);
//            }else{
//                rl_notification.setVisibility(View.VISIBLE);
//            }

        }else{
            rl_notification.setVisibility(View.GONE);
            im_tv_timeline.setVisibility(View.GONE);
//            im_tv_notification.setVisibility(View.GONE);
        }
    }

}
