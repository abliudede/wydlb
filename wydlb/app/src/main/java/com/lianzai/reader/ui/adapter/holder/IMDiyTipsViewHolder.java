package com.lianzai.reader.ui.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.RxImageTool;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.extension.CustomAttachmentType;
import com.netease.nim.uikit.extension.DiyTipsBean;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

public class IMDiyTipsViewHolder extends IMBaseViewHolder {

    RelativeLayout big_award_rl;
    TextView big_award_nickname_tv;
    TextView big_award_des_tv;
    TextView big_award_type_tv;
    ImageView big_award_iv;

    RelativeLayout super_award_rl;
    TextView super_award_nickname_tv;
    TextView super_award_des_tv;
    TextView super_award_type_tv;
    ImageView super_award_iv;

    public IMDiyTipsViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);

        big_award_rl=holder.getView(R.id.big_award_rl);
        big_award_nickname_tv=holder.getView(R.id.big_award_nickname_tv);
        big_award_des_tv=holder.getView(R.id.big_award_des_tv);
        big_award_type_tv=holder.getView(R.id.big_award_type_tv);
        big_award_iv=holder.getView(R.id.big_award_iv);

        super_award_rl=holder.getView(R.id.super_award_rl);
        super_award_nickname_tv=holder.getView(R.id.super_award_nickname_tv);
        super_award_des_tv=holder.getView(R.id.super_award_des_tv);
        super_award_type_tv=holder.getView(R.id.super_award_type_tv);
        super_award_iv=holder.getView(R.id.super_award_iv);

        DiyTipsBean diyTipsBean = (DiyTipsBean)data.getAttachment();
        if(diyTipsBean.getType() == CustomAttachmentType.DIYTIPS1){
            //神秘大奖
            super_award_rl.setVisibility(View.VISIBLE);
            big_award_rl.setVisibility(View.INVISIBLE);
            String nickname = diyTipsBean.getNickName();
            if(!TextUtils.isEmpty(nickname)){
                if(nickname.length() > 5){
                    String temp = nickname.substring(0,5);
                    nickname = temp + "…";
                }
                super_award_nickname_tv.setText(nickname + " 开宝箱抽中了价值");
            }
            super_award_des_tv.setText(diyTipsBean.getRewardValue() + "阅券");
            super_award_type_tv.setText("的" + diyTipsBean.getRewardName());
            RxImageTool.loadAwardImage(holder.getContext(),diyTipsBean.getRewardImg(),super_award_iv);
        }else if(diyTipsBean.getType() == CustomAttachmentType.DIYTIPS2){
            //普通奖项
            super_award_rl.setVisibility(View.INVISIBLE);
            big_award_rl.setVisibility(View.VISIBLE);
            if(diyTipsBean.getCount() <= 1){
                //单次奖励
                String nickname = diyTipsBean.getNickName();
                if(!TextUtils.isEmpty(nickname)){
                    if(nickname.length() > 5){
                        String temp = nickname.substring(0,5);
                        nickname = temp + "…";
                    }
                    big_award_nickname_tv.setText(nickname + " 开宝箱抽中了价值");
                }
                big_award_des_tv.setText(diyTipsBean.getRewardValue()+ "阅券");
                big_award_type_tv.setText("的" + diyTipsBean.getRewardName());
                RxImageTool.loadAwardImage(holder.getContext(),diyTipsBean.getRewardImg(),big_award_iv);
            }else {
                //多次奖励
                String nickname = diyTipsBean.getNickName();
                if(!TextUtils.isEmpty(nickname)){
                    if(nickname.length() > 5){
                        String temp = nickname.substring(0,5);
                        nickname = temp + "…";
                    }
                    big_award_nickname_tv.setText(nickname + "连开" + diyTipsBean.getCount() + "次宝箱抽中总价值");
                }
                big_award_des_tv.setText(diyTipsBean.getRewardValue()+ "阅券");
                big_award_type_tv.setText("的奖品");
                RxImageTool.loadAwardImage(holder.getContext(),diyTipsBean.getRewardImg(),big_award_iv);
            }
        }

        super_award_rl.setOnClickListener(
                    v->{
                        if (!TextUtils.isEmpty(diyTipsBean.getUrl())){
                            ActivityWebView.startActivity(holder.getContext(), diyTipsBean.getUrl(),1);
                        }
                    }
            );

        big_award_rl.setOnClickListener(
                v->{
                    if (!TextUtils.isEmpty(diyTipsBean.getUrl())){
                        ActivityWebView.startActivity(holder.getContext(), diyTipsBean.getUrl(),1);
                    }
                }
        );


//        if (!TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(data))) {
//            ChatRoomViewHolderHelper.setNameText(data,im_tv_nickname,roleType,holder.getContext());
//        }
//
//        if (null!=data.getAttachment()){
//            FightLuckBean fightLuckBean=(FightLuckBean)data.getAttachment();
//            StringBuilder sb = new StringBuilder();
//            sb.append(fightLuckBean.getThreshold());
//            sb.append("阅券/次");
//            amount_tv.setText(sb.toString());
//            rl_content.setOnClickListener(
//                    v->{
//                        if (fightLuckBean.getGameId() != 0){
//                            chatRoomAdapter.getChatItemClickListener().fightLuckClick(fightLuckBean.getGameId());
//                        }
//
//                    }
//            );

//            rl_content.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    chatRoomAdapter.getChatItemClickListener().contentLongClick(rl_content,position,data);
//                    return false;
//                }
//            });
//        }


    }

}
