package com.lianzai.reader.ui.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;

import java.util.Map;

public class IMBaseViewHolder extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter,
        BaseViewHolder, ChatRoomMessage>  {

    protected TextView im_tv_timeline;
    protected SelectableRoundedImageView im_iv_logo;

    protected ImageView iv_retry;

    protected ChatRoomAdapter chatRoomAdapter;

    protected ImageView iv_level;

    protected int roleType;


    public IMBaseViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        chatRoomAdapter=(ChatRoomAdapter)adapter;
    }


    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
//        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(data.getFromAccount());
//
//        if (null==userInfo){
//            RxLogTool.e("userInfo is null....");
//            return;
//        }

        im_tv_timeline=holder.getView(R.id.im_tv_timeline);
        im_iv_logo=holder.getView(R.id.im_iv_logo);
        iv_retry=holder.getView(R.id.iv_retry);
        iv_level=holder.getView(R.id.iv_level);


        if(null != im_iv_logo)
        RxImageTool.loadLogoImage(holder.getContext(),ChatRoomViewHolderHelper.getAvatar(data),im_iv_logo);
        if(null != iv_level) {
        if (null!=data.getChatRoomMessageExtension()&&null!=data.getChatRoomMessageExtension().getSenderExtension()){
            RxLogTool.e(ChatRoomViewHolderHelper.getNameText(data)+"---getRemoteExtension"+data.getChatRoomMessageExtension().getSenderExtension());
            Map<String, Object> map=data.getChatRoomMessageExtension().getSenderExtension();
            try {

                    roleType = Integer.parseInt(map.get("roleType").toString());//身份角色
                    if (roleType == Constant.ChatRoomRole.AUTHOR_ACCOUNT || roleType == Constant.ChatRoomRole.SYSTEM_ACCOUNT || roleType == Constant.ChatRoomRole.ADMIN_ACCOUNT || roleType == Constant.ChatRoomRole.HAOZHU_ACCOUNT) {//作者
                        iv_level.setVisibility(View.VISIBLE);
                        if (roleType == Constant.ChatRoomRole.AUTHOR_ACCOUNT || roleType == Constant.ChatRoomRole.SYSTEM_ACCOUNT || roleType == Constant.ChatRoomRole.HAOZHU_ACCOUNT) {
                            iv_level.setImageResource(R.mipmap.im_chat_room_admin1);
                        } else {
                            iv_level.setImageResource(R.mipmap.im_chat_room_admin2);
                        }
                    } else {
                        iv_level.setVisibility(View.GONE);
                    }

            }catch (Exception e){
//                e.printStackTrace();
            }
        }else{
            iv_level.setVisibility(View.GONE);
        }
        }
        if(null != iv_retry) {
            if (data.getDirect() == MsgDirectionEnum.Out) {
                if (data.getAttachStatus() == AttachStatusEnum.fail) {
                    iv_retry.setVisibility(View.VISIBLE);
                } else {
                    iv_retry.setVisibility(View.GONE);
                }
            } else {
                iv_retry.setVisibility(View.GONE);
            }


            iv_retry.setOnClickListener(
                    v -> {
                        chatRoomAdapter.getChatItemClickListener().retrySendMsgClick(data);
                    }
            );
        }



        if(null != im_iv_logo) {
            im_iv_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String userInfo = UserInfoHelper.getExtension(data.getFromAccount());
                        if(null != userInfo) {
                            JSONObject jsonObject = JSONObject.parseObject(userInfo);
                            int userId = jsonObject.getInteger("objectId");
                            PerSonHomePageActivity.startActivity(holder.getContext(), String.valueOf(userId));
                        }
                    }catch (Exception e){

                    }
                }
            });

            im_iv_logo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    chatRoomAdapter.getChatItemClickListener().userLogoClick(data);
                    return true;
                }
            });
        }

        //时间
        if(null != im_tv_timeline) {
            im_tv_timeline.setText(TimeUtil.getTimeShowString(data.getTime(), false));
            if (chatRoomAdapter.needShowTime(data) || holder.getAdapterPosition() == 0) {//第一条消息要显示
                im_tv_timeline.setVisibility(View.VISIBLE);
            } else {
                im_tv_timeline.setVisibility(View.GONE);
            }
        }

    }

}
