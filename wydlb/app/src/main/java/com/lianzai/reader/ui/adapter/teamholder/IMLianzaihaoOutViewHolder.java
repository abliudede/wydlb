package com.lianzai.reader.ui.adapter.teamholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.ImLianzaihaoAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMLianzaihaoOutViewHolder extends IMBaseViewHolder {

    SelectableRoundedImageView im_iv_lianozaihao_cover;

    TextView im_tv_lianzaihao_name;
    TextView im_tv_lianzaihao_penname;
    TextView im_tv_lianzaihao_type;

    RelativeLayout rl_content;


    public IMLianzaihaoOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);

        im_iv_lianozaihao_cover=holder.getView(R.id.im_iv_lianozaihao_cover);
        im_tv_lianzaihao_name=holder.getView(R.id.im_tv_lianzaihao_name);
        im_tv_lianzaihao_type=holder.getView(R.id.im_tv_lianzaihao_type);
        im_tv_lianzaihao_penname =holder.getView(R.id.im_tv_lianzaihao_penname);

        rl_content=holder.getView(R.id.rl_content);

        if (null!=data.getAttachment()){
            ImLianzaihaoAttachment lianzaihaoAttachment=(ImLianzaihaoAttachment)data.getAttachment();
            im_tv_lianzaihao_name.setText(lianzaihaoAttachment.getPlatformName());
            im_tv_lianzaihao_penname.setText(lianzaihaoAttachment.getPenName());
            RxImageTool.loadImage(teamChatAdapter.getContext(),lianzaihaoAttachment.getPlatformCover(),im_iv_lianozaihao_cover);

            rl_content.setOnClickListener(
                    v->{
                        if (!TextUtils.isEmpty(lianzaihaoAttachment.getPlatformId())){
                            teamChatAdapter.getChatItemClickListener().lianzaihaoClick(lianzaihaoAttachment.getPlatformId());
                        }

                    }
            );

            rl_content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    teamChatAdapter.getChatItemClickListener().contentLongClick(rl_content,position,data);
                    return false;
                }
            });
        }

    }

}
