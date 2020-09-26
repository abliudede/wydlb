package com.lianzai.reader.ui.activity.chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CanSpeakResponse;
import com.lianzai.reader.bean.CheckUpdateResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.ObserverBean;
import com.lianzai.reader.bean.TureOrFalseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.chat.fragment.ChatRoomFragment;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityChatRoomInfo;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomKickOutEvent;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomStatusChangeData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 聊天室
 */
public class ChatRoomActivity extends BaseActivity {

    @Bind(R.id.tv_chat_room_title)
    TextView tv_chat_room_title;

    @Bind(R.id.rl_actionbar)
    protected RelativeLayout rl_actionbar;

    //中奖弹幕
    @Bind(R.id.super_award_rl)
    RelativeLayout super_award_rl;
    @Bind(R.id.super_award_iv_logo)
    CircleImageView super_award_iv_logo;
    @Bind(R.id.super_award_nickname_tv)
    TextView super_award_nickname_tv;
    @Bind(R.id.super_award_des_tv)
    TextView super_award_des_tv;
    @Bind(R.id.super_award_iv)
    ImageView super_award_iv;

    //中奖弹幕
    @Bind(R.id.big_award_rl)
    RelativeLayout big_award_rl;
    @Bind(R.id.big_award_iv_logo)
    CircleImageView big_award_iv_logo;
    @Bind(R.id.big_award_nickname_tv)
    TextView big_award_nickname_tv;
    @Bind(R.id.big_award_des_tv)
    TextView big_award_des_tv;
    @Bind(R.id.big_award_type_tv)
    TextView big_award_type_tv;
    @Bind(R.id.big_award_iv)
    ImageView big_award_iv;

    @Bind(R.id.rl_open_box)
    RelativeLayout rl_open_box;




    FragmentManager fragmentManager;

    ChatRoomFragment chatRoomMessageFragment;


    private AbortableFuture<EnterChatRoomResultData> enterRequest;

    private final static String EXTRA_ROOM_ID = "ROOM_ID";
    /**
     * 聊天室基本信息
     */
    private String roomId;
    private ChatRoomInfo roomInfo;
    private boolean hasEnterSuccess = false; // 是否已经成功登录聊天室

//    int onlineCount = 0;//在线人数

    private boolean isPlaying = false;
    List<ObserverBean.DataBeanX> arrayList;
    private String boxUrl = Constant.H5_BASE_URL + "/treasureBox/#/";


    public static void startActivity(Context context, String roomId) {
        Intent intent = new Intent();
        intent.setClass(context, ChatRoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_chat_room;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }


    private void processIntentData(Bundle savedInstanceState, Intent intent) {
        if (null != savedInstanceState) {
            roomId = savedInstanceState.getString(EXTRA_ROOM_ID);
        } else {
            String chatRoomId = intent.getStringExtra(EXTRA_ROOM_ID);
            if (chatRoomId.equals(roomId)) {//聊天已经打开，不予处理
                return;
            } else {
                roomId = chatRoomId;
            }
        }

        // 注册监听
        registerObservers(true);


        // 登录聊天室
        enterRoom();

        refreshChatRoomInfo();
    }

    private void refreshChatRoomInfo(){
        NIMClient.getService(ChatRoomService.class).fetchRoomInfo(roomId).setCallback(new RequestCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(ChatRoomInfo chatRoomInfo) {
                try{
                    roomInfo = chatRoomInfo;
//                    onlineCount = roomInfo.getOnlineUserCount();
                    initTitleData();
                }catch (Exception e){
                    e.printStackTrace();
                }

//                RxLogTool.e("roomInfo onlineCount:" + onlineCount);
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        fragmentManager = getSupportFragmentManager();

        processIntentData(savedInstanceState, getIntent());

        //初始化弹幕列表
        arrayList = new ArrayList<ObserverBean.DataBeanX>();
        gameSwitchRequest();
        lotteryRecordBarrageRequest();
    }

    private void initTitleData() {
        tv_chat_room_title.setText("《" + roomInfo.getName() + "》");
    }

    /**
     * 检查开宝箱按钮是否显示
     */
    private void gameSwitchRequest() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/littleGame/gameSwitch", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("gameSwitchRequest:" + response);
                    TureOrFalseBean baseBean = GsonUtil.getBean(response, TureOrFalseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(baseBean.getData().isFlag()){
                            rl_open_box.setVisibility(View.VISIBLE);
                        }
                        boxUrl = baseBean.getData().getUrl();
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 进聊天室请求旧弹幕记录
     */
    private void lotteryRecordBarrageRequest() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/littleGame/lotteryRecordBarrage", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("gameSwitchRequest:" + response);
                    ObserverBean observerBean = GsonUtil.getBean(response, ObserverBean.class);
                    if (observerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {

                        //添加任务进队列
                        arrayList.addAll(observerBean.getData());
                        if(!isPlaying){
                            playAnim();
                        }

                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 聊天室初始化
     */
    private void initChatRoomFragment(ChatRoomMember chatRoomMember) {
        chatRoomMessageFragment = (ChatRoomFragment) fragmentManager.findFragmentById(R.id.fragment_chat_room);
        if (chatRoomMessageFragment != null) {
            chatRoomMessageFragment.init(roomId,chatRoomMember);
        } else {
            // 如果Fragment还未Create完成，延迟初始化
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initChatRoomFragment(chatRoomMember);
                }
            }, 100);
        }
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @OnClick(R.id.iv_close)
    void iv_closeClick() {
        rl_open_box.setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_box)
    void iv_boxClick() {
        //跳转到开宝箱
        ActivityWebView.startActivity(this, boxUrl,1);
    }


    @Override
    public void gc() {
        logoutChatRoom();

        registerObservers(false);
        RxEventBusTool.unRegisterEventBus(this);

    }

    private void enterRoom() {

        hasEnterSuccess = false;
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        try {
            //账户信息可能没有更新
            data.setAvatar(RxTool.getAccountBean().getData().getAvatar());
            data.setNick(RxTool.getAccountBean().getData().getNickName());
            RxLogTool.e("getAvatar:" + data.getAvatar());
            RxLogTool.e("getNick:" + data.getNick());
        }catch (Exception e){

        }

        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoomEx(data, 3);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                try{
                    onLoginDone();
                    roomInfo = result.getRoomInfo();
                    NimUIKit.enterChatRoomSuccess(result, false);
                    initChatRoomFragment(result.getMember());
                    hasEnterSuccess = true;

                    DialogMaker.dismissProgressDialog();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int code) {
                RxLogTool.e("ChatRoomActivity onFailed code:"+code);
                try{
                    onLoginDone();
                    if (code == ResponseCode.RES_CHATROOM_BLACKLIST) {
                        RxToast.custom("你已被拉入黑名单，不能再进入", Constant.ToastType.TOAST_ERROR).show();

                    } else if (code == ResponseCode.RES_ENONEXIST) {
                        RxToast.custom("聊天室不存在", Constant.ToastType.TOAST_ERROR).show();
                    }else if(code==ResponseCode.RES_EXCEPTION){
                        RxToast.custom("IM登录不成功", Constant.ToastType.TOAST_ERROR).show();
                    }
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(Throwable exception) {
                RxLogTool.e("ChatRoomActivity exception :"+exception.getMessage());
                onLoginDone();
                finish();
            }
        });
    }


    private void registerObservers(boolean register) {
        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
    }

    private void logoutChatRoom() {
        NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
        onExitedChatRoom();
    }

    public void onExitedChatRoom() {
        NimUIKit.exitedChatRoom(roomId);
        finish();
    }

    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
        @Override
        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
            if (!chatRoomStatusChangeData.roomId.equals(roomId)) {
                return;
            }
            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {
                RxEventBusTool.sendEvents(Constant.EventTag.CHAT_ROOM_LOGINING_TAG);
            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
                RxEventBusTool.sendEvents(Constant.EventTag.CHAT_ROOM_LOGINED_TAG);
            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {

                // 登录成功后，断网重连交给云信SDK，如果重连失败，可以查询具体失败的原因
                if (hasEnterSuccess) {
                    int code = NIMClient.getService(ChatRoomService.class).getEnterErrorCode(roomId);
//                    RxToast.custom("getEnterErrorCode=" + code).show();
//                    RxLogTool.e("chat room enter error code:" + code);
                }
            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
//                Toast.makeText(ChatRoomActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
//                RxToast.custom(getResources().getString(R.string.network_broken)).show();
                //网络端口
                RxEventBusTool.sendEvents(Constant.EventTag.NETWORK_BROKEN_TAG);
            }

            RxLogTool.e("chat room online status changed to " + chatRoomStatusChangeData.status.name());
        }
    };

    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
        @Override
        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
            try {
                RxToast.custom("您已被踢出聊天室", Constant.ToastType.TOAST_ERROR).show();
                onExitedChatRoom();
            }catch (Exception e){

            }
        }
    };

    private void onLoginDone() {
        enterRequest = null;
    }

    public ChatRoomInfo getRoomInfo() {
        return roomInfo;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_ROOM_ID, roomId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentData(null, getIntent());
    }

    @OnClick(R.id.iv_options)
    void optionClick() {
        if (null==roomInfo)return;
        ActivityChatRoomInfo.startActivity(this, roomId,roomInfo.getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatRoomNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.CHAT_ROOM_MEMBER_IN_TAG) {
            refreshChatRoomInfo();

        } else if (event.getTag() == Constant.EventTag.CHAT_ROOM_MEMBER_OUT_TAG) {
            refreshChatRoomInfo();
        } else if (event.getTag() == Constant.EventTag.DANMU_ECENT) {
            if (null!=event.getObj()){
                String content = event.getObj().toString();
                ObserverBean.DataBeanX observerBean = GsonUtil.getBean(content, ObserverBean.DataBeanX.class);
                //添加任务进队列
                if(arrayList.isEmpty()){
                    arrayList.add(0,observerBean);
                }else{
                    arrayList.add(1,observerBean);
                }
                if(!isPlaying){
                    playAnim();
                }
            }
        }
    }

    private void playAnim(){
        isPlaying = true;
        if(arrayList.isEmpty()){
           return;
        }
        ObserverBean.DataBeanX observerBean = arrayList.get(0);
        arrayList.remove(0);
        if(observerBean.getNewsType() == 2100){
            //中奖弹幕
            RxImageTool.loadLogoImage(ChatRoomActivity.this,observerBean.getData().getHead(),super_award_iv_logo);
            String nickname = observerBean.getData().getNickName();
            if(!TextUtils.isEmpty(nickname)){
                if(nickname.length() > 5){
                    String temp = nickname.substring(0,5);
                    nickname = temp + "…";
                }
                super_award_nickname_tv.setText("恭喜 " + nickname + " 开宝箱");
            }
            super_award_des_tv.setText(observerBean.getData().getRewardValue() + "阅券");
            RxImageTool.loadAwardImage(ChatRoomActivity.this,observerBean.getData().getRewardImg(),super_award_iv);
            DanMuMoveViewAnim(super_award_rl);
        }else if(observerBean.getNewsType() == 2101){
            //中奖弹幕
            RxImageTool.loadLogoImage(ChatRoomActivity.this,observerBean.getData().getHead(),big_award_iv_logo);
            String nickname = observerBean.getData().getNickName();
            if(!TextUtils.isEmpty(nickname)){
                if(nickname.length() > 5){
                    String temp = nickname.substring(0,5);
                    nickname = temp + "…";
                }
                big_award_nickname_tv.setText(nickname + "开宝箱抽中了价值");
            }
            big_award_des_tv.setText(observerBean.getData().getRewardValue() + "阅券");
            big_award_type_tv.setText("的"+observerBean.getData().getRewardName());
            RxImageTool.loadAwardImage(ChatRoomActivity.this,observerBean.getData().getRewardImg(),big_award_iv);
            DanMuMoveViewAnim(big_award_rl);
        }
    }

    //弹幕运动动画
    private void DanMuMoveViewAnim(View view) {
        if (view == null) {
            return;
        }
        if(null == ChatRoomActivity.this){
            return;
        }
        //从上到下
        float w = view.getMeasuredWidth();
        Animation transAnim1 = new TranslateAnimation(RxDeviceTool.getScreenWidth(ChatRoomActivity.this),-w,0,0);
        transAnim1.setDuration(8000);
        transAnim1.setRepeatMode(Animation.REVERSE);
        transAnim1.setRepeatCount(0);


        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(transAnim1);
        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.INVISIBLE);
                //假如队列空了，则停止。队列还有则继续播放。
                if(arrayList.isEmpty()){
                    isPlaying = false;
                }else{
                    playAnim();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(smallAnimationSet);
    }


}
