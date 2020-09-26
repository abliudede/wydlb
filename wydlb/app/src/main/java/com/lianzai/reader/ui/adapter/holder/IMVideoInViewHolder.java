package com.lianzai.reader.ui.adapter.holder;

import android.text.TextUtils;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

public class IMVideoInViewHolder extends IMBaseViewHolder {

    TextView im_tv_nickname;


    public IMVideoInViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_nickname=holder.getView(R.id.im_tv_nickname);

        im_tv_nickname.setText(data.getFromNick());

        if (!TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(data)))
            im_tv_nickname.setText(ChatRoomViewHolderHelper.getNameText(data));
    }

}
