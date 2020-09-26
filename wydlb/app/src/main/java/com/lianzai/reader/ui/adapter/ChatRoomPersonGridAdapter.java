package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.List;

/**
 * 成员列表-九宫格
 */
public class ChatRoomPersonGridAdapter extends BaseItemDraggableAdapter<ChatRoomMember,BaseViewHolder> {

    Context context;


    public ChatRoomPersonGridAdapter(@Nullable List<ChatRoomMember> data, Context mContext) {
        super(R.layout.item_person_grid, data);
        this.context=mContext;

    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,ChatRoomMember bean) {
        SelectableRoundedImageView iv_member_logo=baseViewHolder.getView(R.id.iv_member_logo);
        TextView tv_member_name=baseViewHolder.getView(R.id.tv_member_name);

        ImageView iv_status=baseViewHolder.getView(R.id.iv_status);

        RxImageTool.loadFangLogoImage(context,bean.getAvatar(),iv_member_logo);

        tv_member_name.setText(bean.getNick());

        RxLogTool.e("bean account:"+bean.getAccount());

        switch (bean.getMemberLevel()){
            case Constant.ChatRoomRole.SYSTEM_ACCOUNT:
                iv_status.setVisibility(View.VISIBLE);
                iv_status.setImageResource(R.mipmap.im_chat_room_admin1);
                break;
            case Constant.ChatRoomRole.HAOZHU_ACCOUNT:
                iv_status.setVisibility(View.VISIBLE);
                iv_status.setImageResource(R.mipmap.im_chat_room_admin1);
                break;
            case Constant.ChatRoomRole.AUTHOR_ACCOUNT:
                iv_status.setVisibility(View.VISIBLE);
                iv_status.setImageResource(R.mipmap.im_chat_room_admin1);
                break;

            case Constant.ChatRoomRole.ADMIN_ACCOUNT:
                iv_status.setVisibility(View.VISIBLE);
                iv_status.setImageResource(R.mipmap.im_chat_room_admin2);
                break;

            case Constant.ChatRoomRole.NORMAL_ACCOUNT:
                iv_status.setVisibility(View.GONE);
                break;

            default:
                iv_status.setVisibility(View.GONE);
                break;

        }


    }



}
