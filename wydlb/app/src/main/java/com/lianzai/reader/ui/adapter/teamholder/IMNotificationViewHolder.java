package com.lianzai.reader.ui.adapter.teamholder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.lianzai.reader.ui.adapter.TeamChatAdapter;
import com.netease.nim.uikit.business.chatroom.helper.ChatRoomNotificationHelper;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;

public class IMNotificationViewHolder extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, IMMessage> {


    TextView im_tv_notification;

    TextView im_tv_timeline;

    RelativeLayout rl_notification;

    protected TeamChatAdapter teamChatAdapter;

    public IMNotificationViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        teamChatAdapter = (TeamChatAdapter)adapter;
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {

        im_tv_notification=holder.getView(R.id.im_tv_notification);
        im_tv_timeline=holder.getView(R.id.im_tv_timeline);
        rl_notification=holder.getView(R.id.rl_notification);

//        //尝试去屏蔽所有加入群和离开群的消息
//        try{
//            NotificationAttachment attach = (NotificationAttachment) data.getAttachment();
//            switch (attach.getType()) {
//                case InviteMember:
//                    rl_notification.setVisibility(View.GONE);
//                    break;
//                case KickMember:
//                    rl_notification.setVisibility(View.GONE);
//                    break;
//                case LeaveTeam:
//                    rl_notification.setVisibility(View.GONE);
//                    break;
//                case PassTeamApply:
//                    rl_notification.setVisibility(View.GONE);
//                    break;
//                case AcceptInvite:
//                    rl_notification.setVisibility(View.GONE);
//                    break;
//            }
//        }catch (Exception e){
//
//        }

        im_tv_timeline.setText(TimeUtil.getTimeShowString(data.getTime(), false));

        if (teamChatAdapter.needShowTime(data)){
            im_tv_timeline.setVisibility(View.VISIBLE);
        }else{
            im_tv_timeline.setVisibility(View.GONE);
        }

        if (null!=data){
            try{
                im_tv_notification.setText(TeamNotificationHelper.getTeamNotificationText(data,data.getSessionId()));
            }catch (Exception e){
//                e.printStackTrace();
            }

        }else{
            rl_notification.setVisibility(View.GONE);
            im_tv_timeline.setVisibility(View.GONE);
        }
    }

}
