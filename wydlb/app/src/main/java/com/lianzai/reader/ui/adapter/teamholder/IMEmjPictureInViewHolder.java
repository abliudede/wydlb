package com.lianzai.reader.ui.adapter.teamholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxLogTool;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.business.session.emoji.StickerManager;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.StickerAttachment;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

public class IMEmjPictureInViewHolder extends IMBaseViewHolder {


    TextView im_tv_nickname;

    ImageView im_iv_emj_picture;

    public IMEmjPictureInViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_nickname=holder.getView(R.id.im_tv_nickname);
        im_iv_emj_picture=holder.getView(R.id.im_iv_emj_picture);

        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(data.getFromAccount());
        String nickName = TeamHelper.getTeamMemberDisplayName(data.getSessionId(), data.getFromAccount());
        im_tv_nickname.setText(nickName);

        StickerAttachment stickerAttachment=(StickerAttachment)data.getAttachment();

        if (null==stickerAttachment)return;

        RxLogTool.e("IMEmjPictureOutViewHolder Catalog:"+stickerAttachment.getCatalog()+"--Chartlet:"+stickerAttachment.getChartlet());

        String emjPath=StickerManager.getInstance().getStickerUri(stickerAttachment.getCatalog(), stickerAttachment.getChartlet());



        Glide.with(teamChatAdapter.getContext())
                .load(emjPath)
                .apply(new RequestOptions()
                        .error(R.drawable.nim_default_img_failed)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(im_iv_emj_picture);

        im_iv_emj_picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                teamChatAdapter.getChatItemClickListener().contentLongClick(im_iv_emj_picture,position,data);
                return false;
            }
        });

    }

}
