package com.lianzai.reader.ui.adapter.teamholder;

import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMVideoOutViewHolder extends IMBaseViewHolder {

    public IMVideoOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
    }

}
