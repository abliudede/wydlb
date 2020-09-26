package com.lianzai.reader.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.ImLianzaihaoAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

public class IMLianzaihaoInViewHolder extends IMBaseViewHolder {

    TextView im_tv_nickname;

    SelectableRoundedImageView im_iv_lianozaihao_cover;

    TextView im_tv_lianzaihao_name;
    TextView im_tv_lianzaihao_type;

    RelativeLayout rl_content;

    public IMLianzaihaoInViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_nickname=holder.getView(R.id.im_tv_nickname);

        im_iv_lianozaihao_cover=holder.getView(R.id.im_iv_lianozaihao_cover);
        im_tv_lianzaihao_name=holder.getView(R.id.im_tv_lianzaihao_name);
        im_tv_lianzaihao_type=holder.getView(R.id.im_tv_lianzaihao_type);

        rl_content=holder.getView(R.id.rl_content);

        if (!TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(data))) {
            ChatRoomViewHolderHelper.setNameText(data,im_tv_nickname,roleType,holder.getContext());
        }

        if (null!=data.getAttachment()){
            ImLianzaihaoAttachment lianzaihaoAttachment=(ImLianzaihaoAttachment)data.getAttachment();
            im_tv_lianzaihao_name.setText(lianzaihaoAttachment.getPlatformName());
            RxImageTool.loadImage(chatRoomAdapter.getContext(),lianzaihaoAttachment.getPlatformCover(),im_iv_lianozaihao_cover);

            rl_content.setOnClickListener(
                    v->{
                        if (!TextUtils.isEmpty(lianzaihaoAttachment.getPlatformId())){
                            chatRoomAdapter.getChatItemClickListener().lianzaihaoClick(lianzaihaoAttachment.getPlatformId());
                        }

                    }
            );

            rl_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    chatRoomAdapter.getChatItemClickListener().contentLongClick(rl_content,position,data);
                    return false;
                }
            });
        }


    }

}
