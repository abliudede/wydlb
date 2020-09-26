package com.lianzai.reader.ui.adapter.teamholder;

import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.lianzai.reader.ui.adapter.TeamChatAdapter;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMTipsViewHolder extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, IMMessage> {


    TextView im_tv_tips;

    TextView im_tv_timeline;

    TeamChatAdapter teamChatAdapter;

    public IMTipsViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        teamChatAdapter=(TeamChatAdapter)adapter;
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        im_tv_tips=holder.getView(R.id.im_tv_tips);
        im_tv_timeline=holder.getView(R.id.im_tv_timeline);


//        im_tv_tips.setText(data.getContent());
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), im_tv_tips, data.getContent(), -1, 0.65f,0);

        im_tv_timeline.setText(TimeUtil.getTimeShowString(data.getTime(), false));

        if (teamChatAdapter.needShowTime(data)){
            im_tv_timeline.setVisibility(View.VISIBLE);
        }else{
            im_tv_timeline.setVisibility(View.GONE);
        }
    }

}
