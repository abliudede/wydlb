package com.lianzai.reader.ui.activity.TeamChat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.fragment.TeamMessageFragment;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群聊界面
 * <p/>
 * Created by lrz on 2018/11/30.
 */
public class TeamMessageActivity extends BaseActivity {

    @Bind(R.id.tv_chat_room)
    TextView tv_chat_room;
    @Bind(R.id.tv_chat_room_title)
    TextView tv_chat_room_title;

    @Bind(R.id.rl_actionbar)
    protected RelativeLayout rl_actionbar;

    @Bind(R.id.new_tv)
    TextView new_tv;

    @Bind(R.id.tv_message_count)
    TextView tv_message_count;


    //中奖弹幕
//    @Bind(R.id.super_award_rl)
//    RelativeLayout super_award_rl;
//    @Bind(R.id.super_award_iv_logo)
//    CircleImageView super_award_iv_logo;
//    @Bind(R.id.super_award_nickname_tv)
//    TextView super_award_nickname_tv;
//    @Bind(R.id.super_award_des_tv)
//    TextView super_award_des_tv;
//    @Bind(R.id.super_award_iv)
//    ImageView super_award_iv;
//
//    //中奖弹幕
//    @Bind(R.id.big_award_rl)
//    RelativeLayout big_award_rl;
//    @Bind(R.id.big_award_iv_logo)
//    CircleImageView big_award_iv_logo;
//    @Bind(R.id.big_award_nickname_tv)
//    TextView big_award_nickname_tv;
//    @Bind(R.id.big_award_des_tv)
//    TextView big_award_des_tv;
//    @Bind(R.id.big_award_type_tv)
//    TextView big_award_type_tv;
//    @Bind(R.id.big_award_iv)
//    ImageView big_award_iv;
//
//    @Bind(R.id.rl_open_box)
//    RelativeLayout rl_open_box;


//    private boolean isPlaying = false;
//    List<ObserverBean.DataBeanX> arrayList;
//    private String boxUrl = Constant.H5_BASE_URL + "/treasureBox/#/";

    FragmentManager fragmentManager;

    // model
    private String teamId;
    private Team team;
    private TeamMessageFragment fragment;
    private final static String EXTRA_TEAM_ID = "TEAM_ID";
    private final static String EXTRA_UNREAD = "extra_unread";
    private IMMessage anchor;
    private int unRead;


    public static void startActivity(Context context, String teamId) {
        RxActivityTool.removeTeamActivity();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEAM_ID, teamId);
        RxActivityTool.skipActivity(context, TeamMessageActivity.class,bundle);
    }

    public static void startActivity(Context context, String teamId ,IMMessage msg,int unread) {
        RxActivityTool.removeTeamActivity();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TEAM_ID, teamId);
        if (msg != null) {
            bundle.putSerializable(Extras.EXTRA_ANCHOR, msg);
        }
        bundle.putInt(EXTRA_UNREAD, unread);
        RxActivityTool.skipActivity(context, TeamMessageActivity.class,bundle);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_team_chat;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        fragment = (TeamMessageFragment) fragmentManager.findFragmentById(R.id.fragment_chat_room);
        processIntentData(savedInstanceState, getIntent());
    }

    private void getUnreadCount(int count) {
        if (count > 0) {
            tv_message_count.setText("(" + (count > 99 ? "99+" : count) + ")");
        } else {
            tv_message_count.setText("");
        }
    }

    private void processIntentData(Bundle savedInstanceState, Intent intent) {
        if (null != savedInstanceState) {
            teamId = savedInstanceState.getString(EXTRA_TEAM_ID);
        } else {
            String chatTeamId = intent.getStringExtra(EXTRA_TEAM_ID);
            try {
                anchor = (IMMessage) intent.getSerializableExtra(Extras.EXTRA_ANCHOR);
                unRead = intent.getIntExtra(EXTRA_UNREAD,0);
            }catch (Exception e){

            }
            if (chatTeamId.equals(teamId)) {//聊天已经打开，不予处理
                return;
            } else {
                teamId = chatTeamId;
            }
        }
        getUnreadCount(MainActivity.totalUnreadCount);
        //清除未读
        NIMClient.getService(MsgService.class).clearUnreadCount(teamId, SessionTypeEnum.Team);
        // 注册监听
        registerTeamUpdateObserver(true);
        //刷新聊天室信息
        requestTeamInfo();

        //初始化弹幕列表
//        arrayList = new ArrayList<ObserverBean.DataBeanX>();
//        gameSwitchRequest();
//        lotteryRecordBarrageRequest();
        if(anchor != null){
            new_tv.setVisibility(View.VISIBLE);
            if(unRead == 0){
                new_tv.setText("有人回复你");
            }else {
                new_tv.setText(String.valueOf(unRead) + "条新消息");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 检查开宝箱按钮是否显示
     */
//    private void gameSwitchRequest() {
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/littleGame/gameSwitch", new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//            }
//            @Override
//            public void onResponse(String response) {
//                try {
//                    RxLogTool.e("gameSwitchRequest:" + response);
//                    TureOrFalseBean baseBean = GsonUtil.getBean(response, TureOrFalseBean.class);
//                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                        if(baseBean.getData().isFlag()){
//                            rl_open_box.setVisibility(View.VISIBLE);
//                        }
//                        boxUrl = baseBean.getData().getUrl();
//                    } else {
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * 进聊天室请求旧弹幕记录
     */
//    private void lotteryRecordBarrageRequest() {
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/littleGame/lotteryRecordBarrage", new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//            }
//            @Override
//            public void onResponse(String response) {
//                try {
//                    RxLogTool.e("gameSwitchRequest:" + response);
//                    ObserverBean observerBean = GsonUtil.getBean(response, ObserverBean.class);
//                    if (observerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//
//                        //添加任务进队列
//                        arrayList.addAll(observerBean.getData());
//                        if(!isPlaying){
//                            playAnim();
//                        }
//
//                    } else {
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }



    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @OnClick(R.id.new_tv)
    void new_tvClick() {
        if(null != anchor && null != fragment) {
            fragment.setSkipAdapter(anchor);
        }
        new_tv.setVisibility(View.GONE);
    }

    public TextView getNew_tv() {
        return new_tv;
    }

    public void setNew_tv(TextView new_tv) {
        this.new_tv = new_tv;
    }

    public IMMessage getAnchor() {
        return anchor;
    }

    public void setAnchor(IMMessage anchor) {
        this.anchor = anchor;
    }
    //    @OnClick(R.id.iv_close)
//    void iv_closeClick() {
//        rl_open_box.setVisibility(View.GONE);
//    }
//
//    @OnClick(R.id.iv_box)
//    void iv_boxClick() {
//        //跳转到开宝箱
//        ActivityWebView.startActivity(this, boxUrl,1);
//    }


    @Override
    public void gc() {
        registerTeamUpdateObserver(false);
        RxEventBusTool.unRegisterEventBus(this);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_TEAM_ID, teamId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentData(null, getIntent());
    }

    @OnClick(R.id.iv_options)
    void optionClick() {
        if (null==team)return;
        ActivityTeamChatInfo.startActivity(this, teamId,team.getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatRoomNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.CHAT_ROOM_MEMBER_IN_TAG) {
            requestTeamInfo();
        } else if (event.getTag() == Constant.EventTag.CHAT_ROOM_MEMBER_OUT_TAG) {
            requestTeamInfo();
        }else if (event.getTag() == Constant.EventTag.CHAT_ROOM_EXIT) {
            finish();
        }else if (event.getTag() == Constant.EventTag.REFRESH_HOME_RED_DOT_TAG) {//底部红点
            int unreadNum = Integer.parseInt(event.getObj().toString());
            getUnreadCount(unreadNum);//获取未读消息
        }

//        else if (event.getTag() == Constant.EventTag.DANMU_ECENT) {
//            if (null!=event.getObj()){
//                String content = event.getObj().toString();
//                ObserverBean.DataBeanX observerBean = GsonUtil.getBean(content, ObserverBean.DataBeanX.class);
//
//                //添加任务进队列
//                if(arrayList.isEmpty()){
//                    arrayList.add(0,observerBean);
//                }else{
//                    arrayList.add(1,observerBean);
//                }
//                if(!isPlaying){
//                    playAnim();
//                }
//            }
//        }
    }

//    private void playAnim(){
//        isPlaying = true;
//        if(arrayList.isEmpty()){
//            return;
//        }
//        ObserverBean.DataBeanX observerBean = arrayList.get(0);
//        arrayList.remove(0);
//        if(observerBean.getNewsType() == 2100){
//            //中奖弹幕
//            RxImageTool.loadLogoImage(TeamMessageActivity.this,observerBean.getData().getHead(),super_award_iv_logo);
//            String nickname = observerBean.getData().getNickName();
//            if(!TextUtils.isEmpty(nickname)){
//                if(nickname.length() > 5){
//                    String temp = nickname.substring(0,5);
//                    nickname = temp + "…";
//                }
//                super_award_nickname_tv.setText("恭喜 " + nickname + " 开宝箱");
//            }
//            super_award_des_tv.setText(observerBean.getData().getRewardValue() + "阅券");
//            RxImageTool.loadAwardImage(TeamMessageActivity.this,observerBean.getData().getRewardImg(),super_award_iv);
//            DanMuMoveViewAnim(super_award_rl);
//        }else if(observerBean.getNewsType() == 2101){
//            //中奖弹幕
//            RxImageTool.loadLogoImage(TeamMessageActivity.this,observerBean.getData().getHead(),big_award_iv_logo);
//            String nickname = observerBean.getData().getNickName();
//            if(!TextUtils.isEmpty(nickname)){
//                if(nickname.length() > 5){
//                    String temp = nickname.substring(0,5);
//                    nickname = temp + "…";
//                }
//                big_award_nickname_tv.setText(nickname + "开宝箱抽中了价值");
//            }
//            big_award_des_tv.setText(observerBean.getData().getRewardValue() + "阅券");
//            big_award_type_tv.setText("的"+observerBean.getData().getRewardName());
//            RxImageTool.loadAwardImage(TeamMessageActivity.this,observerBean.getData().getRewardImg(),big_award_iv);
//            DanMuMoveViewAnim(big_award_rl);
//        }
//    }
//
//    //弹幕运动动画
//    private void DanMuMoveViewAnim(View view) {
//        if (view == null) {
//            return;
//        }
//        if(null == TeamMessageActivity.this){
//            return;
//        }
//        //从上到下
//        float w = view.getMeasuredWidth();
//        Animation transAnim1 = new TranslateAnimation(RxDeviceTool.getScreenWidth(TeamMessageActivity.this),-w,0,0);
//        transAnim1.setDuration(8000);
//        transAnim1.setRepeatMode(Animation.REVERSE);
//        transAnim1.setRepeatCount(0);
//
//
//        AnimationSet smallAnimationSet = new AnimationSet(false);
//        smallAnimationSet.addAnimation(transAnim1);
//        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                view.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                view.setVisibility(View.INVISIBLE);
//                //假如队列空了，则停止。队列还有则继续播放。
//                if(arrayList.isEmpty()){
//                    isPlaying = false;
//                }else{
//                    playAnim();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//        view.startAnimation(smallAnimationSet);
//    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    /**
     * 聊天室初始化
     */
    private void initChatRoomFragment() {
        if (null != fragment && null == fragment.teamId ) {
            fragment.init(teamId,team);
        } else if(null == fragment){
            // 如果Fragment还未Create完成，延迟初始化
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragment = (TeamMessageFragment) fragmentManager.findFragmentById(R.id.fragment_chat_room);
                    initChatRoomFragment();
                }
            }, 100);
        }
    }



    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (t != null) {
            updateTeamInfo(t);
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void onRequestTeamInfoFailed() {
        RxToast.custom( "获取群组信息失败!").show();
        finish();
    }

    /**
     * 更新群信息
     *
     * @param d
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }
        team = d;
        initTitleData();
        initChatRoomFragment();
    }

    private void initTitleData() {
        try{
            tv_chat_room.setVisibility(View.GONE);
            tv_chat_room_title.setText(team.getName() + " (" + String.valueOf(team.getMemberCount() - 1) + ")");
        }catch (Exception e){

        }
    }

    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(TeamMessageActivity.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            if(null != fragment)
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            if(null != fragment)
            fragment.refreshMessageList();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            if(null != fragment)
            fragment.refreshMessageList();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            if(null != fragment)
            fragment.refreshMessageList();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            if(null != fragment)
            fragment.refreshMessageList();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != fragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
