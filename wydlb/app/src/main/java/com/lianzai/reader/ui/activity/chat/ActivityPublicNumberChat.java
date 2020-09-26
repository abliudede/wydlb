package com.lianzai.reader.ui.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.adapter.PublicNumberAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 连载号
 */
public class ActivityPublicNumberChat extends BaseActivity implements PublicNumberAdapter.PublicNumberOptionCallback, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.iv_options)
    ImageView iv_options;

    @Bind(R.id.tv_back)
    TextView tv_back;

    protected String sessionId;

    private SessionCustomization customization;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    PublicNumberAdapter adapter;

    private List<IMMessage> imMessageList = new ArrayList<>();

    int identity = 0;
    int objectId = 0;

    String userJson;

    public static void startActivity(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, ActivityPublicNumberChat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_public_nubmer_chat;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, SessionTypeEnum.P2P);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortMessages(List<IMMessage> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<IMMessage> comp = new Comparator<IMMessage>() {

        @Override
        public int compare(IMMessage o1, IMMessage o2) {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time < 0 ? -1 : 1);
        }
    };

    boolean fetchAgain = false;
    private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
        @Override
        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
            try {
                if (code != ResponseCode.RES_SUCCESS || exception != null) {
//                    RxToast.custom("加载失败PublicNumberChat", Constant.ToastType.TOAST_ERROR).show();
                    return;
                }

                if (messages != null) {
                    tv_commom_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));

                    imMessageList.clear();
                    imMessageList.addAll(messages);
                    sortMessages(imMessageList);


                    adapter.setNewData(imMessageList);
                    adapter.notifyDataSetChanged();

                    if (imMessageList.size() == 0 && !fetchAgain) {//再刷一次
                        NIMClient.getService(MsgService.class).queryMessageListEx(MessageBuilder.createEmptyMessage(sessionId, SessionTypeEnum.P2P, 0), QueryDirectionEnum.QUERY_NEW, 100, false).setCallback(oldMessageCallback);
                        fetchAgain = true;
                    }

                    RxLogTool.e("callbackadapter size:" + adapter.getData().size());
                    scrollBottom();
                }
            } catch (Exception e) {

            }
        }
    };

    private RequestCallback<List<IMMessage>> oldMessageCallback = new RequestCallbackWrapper<List<IMMessage>>() {
        @Override
        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
            try {
            } catch (Exception e) {
            }
            if (code != ResponseCode.RES_SUCCESS || exception != null) {

//                RxToast.custom("加载失败PublicNumberChat", Constant.ToastType.TOAST_ERROR).show();
                return;
            }

            if (messages != null) {
                imMessageList.clear();
                if(null != tv_commom_title)
                tv_commom_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));

                imMessageList.addAll(messages);
                sortMessages(imMessageList);

                RxLogTool.e("oldMessageCallback imMessageList size:" + imMessageList.size());
                adapter.setNewData(imMessageList);
                scrollBottom();
            }
        }
    };

    private void getUnreadCount(int count) {
        if (count > 0) {
            tv_back.setText("连载(" + (count > 99 ? "99+" : count) + ")");
        } else {
            tv_back.setText("连载");
        }
    }

    private void processIntentData(Bundle savedInstanceState, Intent intent) {
        if (null != savedInstanceState) {
            sessionId = savedInstanceState.getString(Extras.EXTRA_ACCOUNT);
            customization = (SessionCustomization) savedInstanceState.getSerializable(Extras.EXTRA_CUSTOMIZATION);
        } else if (null != intent) {
            sessionId = intent.getStringExtra(Extras.EXTRA_ACCOUNT);
            customization = (SessionCustomization) intent.getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);
        }

        RxLogTool.e("ActivityPublicNumberChat processIntentData...");
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        RxEventBusTool.registerEventBus(this);

        if (RxLoginTool.isLogin()) {
            iv_options.setImageResource(R.mipmap.icon_v2_public_number_card);
        } else {
            //游客状态不显示此按钮
            iv_options.setVisibility(View.INVISIBLE);
        }

        processIntentData(savedInstanceState, getIntent());

        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#333333"));

        // adapter
        adapter = new PublicNumberAdapter(rv_list, imMessageList);
        adapter.setPublicNumberOptionCallback(this);

        // recyclerView
        rv_list.setAdapter(adapter);
        rv_list.setLayoutManager(new LinearLayoutManager(this));

        registerObservers(true);

        //初始化
        init();
    }

    private void init() {
        if (null != swipe_refresh_layout)
            swipe_refresh_layout.setEnabled(true);

        userJson = UserInfoHelper.getExtension(sessionId);

        RxLogTool.e("sessionId:" + sessionId);
        RxLogTool.e("userJson:" + userJson);

        getUnreadCount(MainActivity.totalUnreadCount);

        if (TextUtils.isEmpty(sessionId)) {
//            RxToast.custom("该连载号不可用",Constant.ToastType.TOAST_ERROR).show();
            finish();
            return;
        }

        if (TextUtils.isEmpty(userJson) || userJson.equals(sessionId)) {//本地没有改连载号数据，从服务器获取
            List<String> account = new ArrayList<>();
            account.add(sessionId);
            NIMClient.getService(UserService.class).fetchUserInfo(account).setCallback(new RequestCallback<List<NimUserInfo>>() {
                @Override
                public void onSuccess(List<NimUserInfo> nimUserInfos) {
                    RxLogTool.e("fetchUserInfo onSuccess:" + nimUserInfos.size());
                    try {
                        for (NimUserInfo user : nimUserInfos) {
                            if (user.getAccount().equals(sessionId)) {//获取成功
                                RxLogTool.e("连载号数据同步成功..." + user.getExtension());
                                userJson = user.getExtension();

                                getExtensionInformation(true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(int i) {
                    RxLogTool.e("fetchUserInfo:" + i);
                    try {
                        RxToast.custom("该连载号不可用", Constant.ToastType.TOAST_ERROR).show();
                        finish();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    try {
                        RxToast.custom("该连载号不可用", Constant.ToastType.TOAST_ERROR).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            getExtensionInformation(false);
        }

        //清除未读
        NIMClient.getService(MsgService.class).clearUnreadCount(sessionId, SessionTypeEnum.P2P);
    }

    @Override
    public void gc() {
        registerObservers(false);
    }

    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.tv_back)
    void closeClick() {
        finish();
    }



    //连载号详情
    @OnClick(R.id.iv_options)
    void optionClick() {
        if (objectId == 0) {
            return;
        }
        PublicNumberDetailActivity.startActivity(this, objectId);
    }


    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }

        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        //会话监听
        service.observeRecentContact(messageObserver, register);

    }


    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
        }
    };

    private UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {//获取扩展字段
                        RxLogTool.e("registerUserInfoObserver:" + sessionId);
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }


    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }


    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            //接收新消息
            for (IMMessage message : messages) {
                if (isMyMessage(message)) {
                    RxLogTool.e("message:" + message.getFromAccount());
                    adapter.addData(message);
                    scrollBottom();
                }
            }
        }
    };

    private void scrollBottom() {
        if (null!=rv_list)
            rv_list.scrollToPosition(adapter.getItemCount() - 1);
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.P2P
                && message.getSessionId() != null
                && message.getSessionId().equals(sessionId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }


    @Override
    public void openChapterByChapterTitle(String bookId, String chapterTitle) {
        RxLogTool.e("openChapterBychapterTitle:" + chapterTitle);
        UrlReadActivity.startActivityOutsideRead(this, bookId,"",false,"", chapterTitle,0,false);
    }

    @Override
    public void openWebUrl(String url, String tit, String des, String img,int type) {
        //周报网页打开，情况2
        RxLogTool.e("openWebUrl:" + url);
        if(type == Constant.EXTENDFIELD.OFFICIAL_COMMUNICATION_TYPE || type == Constant.EXTENDFIELD.OFFICIAL_NOTICE || type == Constant.EXTENDFIELD.OFFICIAL_NOTICE ){
            ActivityWebView.startActivity(ActivityPublicNumberChat.this, url);
        }else if(type == Constant.EXTENDFIELD.OFFICIAL_NOSHARE){
            ActivityWebView.startActivity(ActivityPublicNumberChat.this, url,1);
        }else if(type == Constant.EXTENDFIELD.OFFICIAL_WEEKLY){
            ActivityWebView.startActivity(ActivityPublicNumberChat.this, url, tit, des, img, 2);
        }else {
            ActivityWebView.startActivity(ActivityPublicNumberChat.this, url);
        }
    }

    @Override
    public void avatarClick() {
        optionClick();
    }

    /**
     * 加入群聊
     *
     * @param teamId
     */
    @Override
    public void joinTeamChat(String teamId) {
            Map<String, String> map = new HashMap<>();
            map.put("teamId", teamId);
            OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/join" , map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    try{
//                        RxToast.custom("网络错误").show();
                    }catch (Exception ee){
                    }
                }

                @Override
                public void onResponse(String response) {
                    try {
                        BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        TeamMessageActivity.startActivity(ActivityPublicNumberChat.this,teamId);
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        RxLogTool.e("ActivityPublicNumberChat eventBusNotification.......");
        if (event.getTag() == Constant.EventTag.CLOSE_CLOSE_CHAT_BY_ACCID_TAG) {
            finish();
        } else if (event.getTag() == Constant.EventTag.REFRESH_HOME_RED_DOT_TAG) {//底部红点
            int unreadNum = Integer.parseInt(event.getObj().toString());
            getUnreadCount(unreadNum);//获取未读消息
        }
    }


    private void getExtensionInformation(boolean remote) {
        try {
            RxLogTool.e("getExtensionInfomation json:" + userJson);
            org.json.JSONObject object = new org.json.JSONObject(userJson);
            identity = object.getInt("type");
            objectId = object.getInt("objectId");//type 0普通用户 1官方连载号 2书连载号，objectId 0对应uid，1、2对应platfromId
            tv_commom_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
            if (remote) {
                NIMClient.getService(MsgService.class).queryMessageListEx(MessageBuilder.createEmptyMessage(sessionId, SessionTypeEnum.P2P, 0), QueryDirectionEnum.QUERY_NEW, 100, false).setCallback(oldMessageCallback);
            } else {
                NIMClient.getService(MsgService.class).queryMessageListEx(MessageBuilder.createEmptyMessage(sessionId, SessionTypeEnum.P2P, 0), QueryDirectionEnum.QUERY_OLD, 100, false).setCallback(callback);
            }
        } catch (Exception e) {
            RxLogTool.e("UserInfoHelper.getExtension ex:" + e.getMessage());
        }


    }

    private IMMessage anchor;
    private QueryDirectionEnum direction = null;
    private int loadMsgCount = NimUIKitImpl.getOptions().messageCountLoadOnce;

    /**
     * 下拉加载历史记录
     */
    @Override
    public void onRefresh() {
        loadFromRemote();
    }

    private void loadFromRemote() {
        this.direction = QueryDirectionEnum.QUERY_OLD;
        NIMClient.getService(MsgService.class).pullMessageHistory(anchor(), loadMsgCount, true)
                .setCallback(new RequestCallback<List<IMMessage>>() {
                    @Override
                    public void onSuccess(List<IMMessage> imMessages) {
                        if (null != imMessages) {
                            onMessageLoaded(imMessages);
                        }
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    private void onMessageLoaded(final List<IMMessage> messages) {
        if (messages == null) {
            return;
        }
        RxLogTool.e("onMessageLoaded messages size:" + messages.size());

        boolean noMoreMessage = messages.size() < loadMsgCount;

        Collections.reverse(messages);

        // 加入anchor
        if (anchor != null) {
            messages.add(anchor);
        }

        imMessageList.addAll(0, messages);

        adapter.setNewData(imMessageList);

        if (null != swipe_refresh_layout) {
            swipe_refresh_layout.setRefreshing(false);
        }

        if (noMoreMessage && null != swipe_refresh_layout) {
            swipe_refresh_layout.setEnabled(false);
        }

    }

    private IMMessage anchor() {
        if (imMessageList.size() == 0) {
            return anchor == null ? MessageBuilder.createEmptyMessage(sessionId, SessionTypeEnum.P2P, 0) : anchor;
        } else {
            int index = (direction == QueryDirectionEnum.QUERY_NEW ? imMessageList.size() - 1 : 0);
            return imMessageList.get(index);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Extras.EXTRA_ACCOUNT, sessionId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentData(null, getIntent());
        init();//刷新数据
    }


}
