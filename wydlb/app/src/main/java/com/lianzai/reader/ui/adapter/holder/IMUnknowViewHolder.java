package com.lianzai.reader.ui.adapter.holder;

import android.text.TextUtils;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

public class IMUnknowViewHolder extends IMBaseViewHolder {

    public IMUnknowViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    TextView im_tv_nickname;

    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_nickname=holder.getView(R.id.im_tv_nickname);

        if (!TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(data))) {
            ChatRoomViewHolderHelper.setNameText(data,im_tv_nickname,roleType,holder.getContext());
        }
    }

}
