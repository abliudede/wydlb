package com.netease.nim.uikit.business.recent.holder;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

public abstract class RecentViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, RecentContact> {

    private Boolean mute;

    public RecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    private int lastUnreadCount = 0;

    protected FrameLayout portraitPanel;

    protected HeadImageView imgHead;

    protected TextView tvNickname;

    protected TextView tvMessage;

    protected TextView tvDatetime;

    // 消息发送错误状态标记，目前没有逻辑处理
    protected ImageView imgMsgStatus;

    // 未读红点（一个占坑，一个全屏动画）
    protected TextView tvUnread;
    protected ImageView mute_iv;

    protected TextView tvOnlineState;

    Drawable identityDrawable;

    TextView tv_top;
    TextView tv_delete_recent;
    TextView tv_cancel_top;

    SwipeMenuLayout swipe_menu_layout;

    // 子类覆写
    protected abstract String getContent(RecentContact recent);



    @Override
    public void convert(BaseViewHolder holder, RecentContact data, int position, boolean isScrolling) {
        inflate(holder, data);
        refresh(holder, data, position);
    }

    public void inflate(BaseViewHolder holder, final RecentContact recent) {
        this.portraitPanel = holder.getView(R.id.portrait_panel);
        this.imgHead = holder.getView(R.id.img_head);
        this.tvNickname = holder.getView(R.id.tv_nickname);
        this.tvMessage = holder.getView(R.id.tv_message);
        this.tvUnread = holder.getView(R.id.unread_number_tip);
        this.mute_iv= holder.getView(R.id.mute_iv);
        this.tvDatetime = holder.getView(R.id.tv_date_time);
        this.imgMsgStatus = holder.getView(R.id.img_msg_status);
        this.tvOnlineState = holder.getView(R.id.tv_online_state);
        this.tv_top=holder.getView(R.id.tv_top);
        this.tv_delete_recent=holder.getView(R.id.tv_delete_recent);
        this.tv_cancel_top=holder.getView(R.id.tv_cancel_top);
        this.swipe_menu_layout=holder.getView(R.id.swipe_menu_layout);

        //假如是开启消息免打扰
        mute = false;

        try {
            if (recent.getSessionType() == SessionTypeEnum.Team){
                //判断消息是否开启了免打扰
                Team team = NimUIKit.getTeamProvider().getTeamById(recent.getContactId());
            TeamMessageNotifyTypeEnum type = team.getMessageNotifyType();
            if (type.equals(TeamMessageNotifyTypeEnum.Mute)) {
                mute = true;
            }
        }
        }catch (Exception e){
        }


        try{
            if (recent.getSessionType() == SessionTypeEnum.P2P) {
                //是否开启消息免打扰
                mute = !NIMClient.getService(FriendService.class).isNeedMessageNotify(recent.getContactId());
            }
        }catch (Exception e){

        }

        if(mute){
            mute_iv.setVisibility(View.VISIBLE);
        }else {
            mute_iv.setVisibility(View.GONE);
        }

        holder.addOnClickListener(R.id.unread_number_tip);

        int size= RxImageTool.dp2px(17);
        identityDrawable= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_v2_offical_identity);
        identityDrawable.setBounds(0,0, size,size);

//        this.tvUnread.setTouchListener(new DropFake.ITouchListener() {
//            @Override
//            public void onDown() {
//                RxLogTool.e("DropFake onDown...");
//                DropManager.getInstance().setCurrentId(recent);
//                DropManager.getInstance().down(tvUnread, tvUnread.getText());
//            }
//
//            @Override
//            public void onMove(float curX, float curY) {
//                RxLogTool.e("DropFake onMove curX:"+curX+"--curY:"+curY);
//                DropManager.getInstance().move(curX, curY);
//            }
//
//            @Override
//            public void onUp() {
//                DropManager.getInstance().up();
//            }
//        });
    }

    public void refresh(BaseViewHolder holder, RecentContact recent, final int position) {
        if (null==NimUIKit.getUserInfoProvider())
            return;

        NimUserInfo userInfo= NimUIKit.getUserInfoProvider().getUserInfo(recent.getFromAccount());

        if (null==userInfo){
            RxLogTool.e(recent.getContactId()+"--userInfo is null");
            return;
        }else{
            RxLogTool.e(recent.getContactId()+"--userInfo is not null");
        }

        // unread count animation
        boolean shouldBoom = lastUnreadCount > 0 && recent.getUnreadCount() == 0; // 未读数从N->0执行爆裂动画;
        lastUnreadCount = recent.getUnreadCount();

        updateBackground(holder, recent, position);

        loadPortrait(recent);

        int identity=0;
//        int objectId=0;

        try{
            String json=userInfo.getExtension();
            RxLogTool.e("RecentViewHolder getExtension json:"+json);
            if (!TextUtils.isEmpty(json)){
                org.json.JSONObject object=new org.json.JSONObject(json);
                identity=object.getInt("type");
                //            objectId=object.getInt("objectId");//type 0普通用户 1官方连载号 2书连载号，objectId 0对应uid，1、2对应platfromId
            }

        }catch (Exception e){
            RxLogTool.e("RecentViewHolder:"+e.getMessage());
        }


        tv_delete_recent.setOnClickListener(
                v->{
                    swipe_menu_layout.smoothClose();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getCallback().onSwipeItemClick(recent,position,Constant.SwipeType.DELETE);
                            }catch (Exception e){
                            }
                        }
                    },500);

                }
        );
        tv_cancel_top.setOnClickListener(
                v->{
                    swipe_menu_layout.smoothClose();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getCallback().onSwipeItemClick(recent,position,Constant.SwipeType.CANCEL_TOP);
                            }catch (Exception e){
                            }
                        }
                    },500);

                }
        );

        tv_top.setOnClickListener(
                v->{
                    swipe_menu_layout.smoothClose();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getCallback().onSwipeItemClick(recent,position,Constant.SwipeType.TOP);
                            }catch (Exception e){
                            }
                        }
                    },500);

                }
        );

        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()),identity);

        updateOnlineState(recent);

        updateMsgLabel(holder, recent);

        updateNewIndicator(recent);
    }

    private void updateBackground(BaseViewHolder holder, RecentContact recent, int position) {
        if ((recent.getTag() & RecentContactsFragment.RECENT_TAG_STICKY) == 0) {
            holder.getConvertView().setBackgroundResource(R.color.white);

            tv_cancel_top.setVisibility(View.GONE);
            tv_top.setVisibility(View.VISIBLE);
        } else {
            holder.getConvertView().setBackgroundResource(R.drawable.gray_bg1);
            //置顶按钮
            tv_cancel_top.setVisibility(View.VISIBLE);
            tv_top.setVisibility(View.GONE);
        }
    }

    protected void loadPortrait(RecentContact recent) {
        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
            imgHead.loadBuddyAvatar(recent.getContactId());
        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = NimUIKit.getTeamProvider().getTeamById(recent.getContactId());
            imgHead.loadTeamIconByTeam(team);
        }
    }

    private void updateNewIndicator(RecentContact recent) {
        int unreadNum = recent.getUnreadCount();
//        unreadNum=1;
        tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)tvUnread.getLayoutParams();
            int len;
            int left;
            if(unreadNum > 99){
                len = RxImageTool.dip2px(24);
                left = RxImageTool.dip2px(27);
                params.width =len;
                params.height =len*2/3;
            }else {
                len = RxImageTool.dip2px(16);
                left = RxImageTool.dip2px(35);
                params.width =len;
                params.height =len;
            }
            int bottom = RxImageTool.dip2px(10);
            params.leftMargin = left;
            params.bottomMargin = bottom;
            tvUnread.setLayoutParams(params);
            tvUnread.setText(unreadCountShowRule(unreadNum));
            if(mute){
                tvUnread.setBackgroundResource(R.drawable.dot_gray_bg);
            }else {
                tvUnread.setBackgroundResource(R.drawable.dot_red_bg);
            }

    }

    private void updateMsgLabel(BaseViewHolder holder, RecentContact recent) {
        // 显示消息具体内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), tvMessage, getContent(recent), -1, 0.45f,0);
        //tvMessage.setText(getContent());

        MsgStatusEnum status = recent.getMsgStatus();
        switch (status) {
            case fail:
                imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            case sending:
                imgMsgStatus.setImageResource(R.drawable.nim_recent_contact_ic_sending);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            default:
                imgMsgStatus.setVisibility(View.GONE);
                break;
        }

        String timeString = TimeUtil.getTimeShowString(recent.getTime(), true);
        tvDatetime.setText(timeString);
    }

    protected String getOnlineStateContent(RecentContact recent) {
        return "";
    }

    protected void updateOnlineState(RecentContact recent) {
        if (recent.getSessionType() == SessionTypeEnum.Team) {
            tvOnlineState.setVisibility(View.GONE);
        } else {
            String onlineStateContent = getOnlineStateContent(recent);
            if (TextUtils.isEmpty(onlineStateContent)) {
                tvOnlineState.setVisibility(View.GONE);
            } else {
                tvOnlineState.setVisibility(View.VISIBLE);
                tvOnlineState.setText(getOnlineStateContent(recent));
            }
        }
    }

    protected void updateNickLabel(String nick,int identity) {
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            tvNickname.setMaxWidth(labelWidth);
        }

        if (identity== Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || identity== Constant.UserIdentity.CAN_ATTENTION_NUMBER){
            tvNickname.setCompoundDrawablePadding(15);
            tvNickname.setCompoundDrawables(null,null,identityDrawable,null);
        }else{
            tvNickname.setCompoundDrawablePadding(0);
            tvNickname.setCompoundDrawables(null,null,null,null);
        }
        tvNickname.setText(nick);
    }

    protected String unreadCountShowRule(int unread) {
//        unread = Math.min(unread, 99);
//        unread=1001;
        return String.valueOf(unread>99?"99+":unread);
    }

    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }
}
