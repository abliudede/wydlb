package com.lianzai.reader.ui.adapter.teamholder;

import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.FightLuckBean;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMFightLuckOutViewHolder extends IMBaseViewHolder {

    TextView im_tv_nickname;

    TextView amount_tv;

    TextView tit_tv;

    RelativeLayout rl_content;


    public IMFightLuckOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);

        amount_tv=holder.getView(R.id.amount_tv);
        tit_tv=holder.getView(R.id.tit_tv);
        rl_content=holder.getView(R.id.rl_content);

        if (null!=data.getAttachment()){
            FightLuckBean fightLuckBean=(FightLuckBean)data.getAttachment();
            if(!TextUtils.isEmpty(fightLuckBean.getTitle()))
                tit_tv.setText(fightLuckBean.getTitle());
            StringBuilder sb = new StringBuilder();
            sb.append(fightLuckBean.getThreshold());
            sb.append("阅券/次");
            amount_tv.setText(sb.toString());
            rl_content.setOnClickListener(
                    v->{
                        if (fightLuckBean.getGameId() != 0){
                            teamChatAdapter.getChatItemClickListener().fightLuckClick(fightLuckBean.getGameId());
                        }

                    }
            );

//            rl_content.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    chatRoomAdapter.getChatItemClickListener().contentLongClick(rl_content,position,data);
//                    return false;
//                }
//            });
        }

    }

}
