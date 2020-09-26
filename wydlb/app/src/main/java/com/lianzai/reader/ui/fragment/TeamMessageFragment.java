package com.lianzai.reader.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CanSpeakResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckDetail;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckEnter;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.chat.SendCardActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.TeamChatAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.ReplyHelper;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxPhotoTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RecordButton;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.TeamChatMoreOptionPopup;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.main.CustomPushContentProvider;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.ait.AitContactType;
import com.netease.nim.uikit.business.ait.AitManager;
import com.netease.nim.uikit.business.ait.AitTextChangeListener;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;
import com.netease.nim.uikit.business.session.emoji.EmoticonPickerView;
import com.netease.nim.uikit.business.session.emoji.IEmoticonSelectedListener;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.session.helper.MessageListPanelHelper;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.loadmore.MsgListFetchLoadMoreView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.extension.ImLianzaihaoAttachment;
import com.netease.nim.uikit.extension.StickerAttachment;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.netease.nimlib.sdk.msg.model.MemberPushOption;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RevokeMsgNotification;
import com.netease.nimlib.sdk.team.model.Team;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/11/30.
 */
public class TeamMessageFragment extends BaseFragment implements IEmoticonSelectedListener, RecordButton.OnRecordListener, TeamChatMoreOptionPopup.TeamChatOptionItemListener, TeamChatAdapter.ChatItemClickListener {

    private Team team;

    public String teamId;

    //@管理器
    protected AitManager aitManager;
    //回复管理器
    protected ReplyHelper replyHelper;
    private boolean nowIsAit = false;
    //消息栈最大数量
    private static final int MESSAGE_CAPACITY = 500;
    //消息列表
    private LinkedList<IMMessage> items;
    //上一条消息发送时间，防止重复发送消息
    long sendTime;


    TeamChatAdapter adapter;
    RecyclerView messageListView;
    RxLinearLayoutManager    rxLinearLayoutManager;
    // 表情
    EmoticonPickerView emoticonPickerView;  // 贴图表情控件

    @Bind(R.id.btn_audio)
    RecordButton btn_audio;

    EditText ed_content;

    @Bind(R.id.iv_audio)
    ImageView iv_audio;

    @Bind(R.id.iv_im_emoji)
    ImageView iv_im_emoji;

    @Bind(R.id.iv_keyboard)
    ImageView iv_keyboard;

    @Bind(R.id.tv_send)
    TextView tv_send;

    @Bind(R.id.rl_im_more)
    RelativeLayout rl_im_more;

    @Bind(R.id.tv_emoji)
    TextView tv_emoji;

    @Bind(R.id.ll_more_add)
    LinearLayout ll_more_add;

    @Bind(R.id.ll_input_panel)
    LinearLayout ll_input_panel;

    @Bind(R.id.tv_tip)
    TextView tv_tip;//提示消息

    @Bind(R.id.tv_not_speak)
    TextView tv_not_speak;

    @Bind(R.id.tv_mute)
    TextView tv_mute;

    @Bind(R.id.tv_reply)
    TextView tv_reply;

    @Bind(R.id.view_reddot_pinshouqi1)
    View view_reddot_pinshouqi1;
    @Bind(R.id.view_reddot_pinshouqi2)
    View view_reddot_pinshouqi2;
    @Bind(R.id.rl_fightluck)
    RelativeLayout rl_fightluck;


    //消息弹窗
    TeamChatMoreOptionPopup teamChatMoreOptionPopup;

    AccountDetailBean accountDetailBean;

    RxDialogSureCancelNew rxDialogSureCancelNew;

    boolean canSpeak = true;//是否能发言

    protected Handler uiHandler;

    //如果在发需要拍照 的消息时，拍照回来时页面可能会销毁重建，重建时会在MessageLoader 的构造方法中调一次 loadFromLocal
    //而在发送消息后，list 需要滚动到底部，又会通过RequestFetchMoreListener 调用一次 loadFromLocal
    //所以消息会重复
    private boolean mIsInitFetchingLocal;
    private TextWatcher textChangedListener;
    private AitTextChangeListener aitTextChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.fragment_chat_foom;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        accountDetailBean = RxTool.getAccountBean();
        uiHandler = new Handler();


        //从代码绑定视图
        messageListView = getView().findViewById(R.id.rv_list);
        emoticonPickerView = getView().findViewById(R.id.emoticon_picker_view);
        ed_content = getView().findViewById(R.id.ed_content);


        initPopupWindow();


        boolean showRed = RxSharedPreferencesUtil.getInstance().getBoolean(Constant.SHOW_FIGHT_LUCK_RED, true);
        if(showRed){
            view_reddot_pinshouqi1.setVisibility(View.VISIBLE);
            view_reddot_pinshouqi2.setVisibility(View.VISIBLE);
        }else{
            view_reddot_pinshouqi1.setVisibility(View.GONE);
            view_reddot_pinshouqi2.setVisibility(View.GONE);
        }
    }

    /*初始化弹窗*/
    private void initPopupWindow() {
        teamChatMoreOptionPopup = new TeamChatMoreOptionPopup(getActivity(), this);
        rxDialogSureCancelNew = new RxDialogSureCancelNew(getActivity(), R.style.OptionDialogStyle);
    }

    //艾特功能
    private void initAitManager() {
        UIKitOptions options = NimUIKitImpl.getOptions();
        if (options.aitEnable) {
            //都判断一遍，不要调用第二次
            if(null == aitManager) {
                aitManager = new AitManager(getContext(), options.aitTeamMember ? teamId : null, options.aitIMRobot);
            }
            if(null == textChangedListener) {
                textChangedListener = new TextWatcher() {
                    private int start;
                    private int count;

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        this.start = start;
                        this.count = count;
                        if (aitManager != null) {
                            aitManager.onTextChanged(s, start, before, count);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (aitManager != null) {
                            aitManager.beforeTextChanged(s, start, count, after);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        MoonUtil.replaceEmoticons(getActivity(), s, start, count);
                        int editEnd = ed_content.getSelectionEnd();
                        ed_content.removeTextChangedListener(this);
                        while (StringUtil.counterChars(s.toString()) > NimUIKitImpl.getOptions().maxInputTextLength && editEnd > 0) {
                            s.delete(editEnd - 1, editEnd);
                            editEnd--;
                        }
                        ed_content.setSelection(editEnd);
                        ed_content.addTextChangedListener(this);

                        if (aitManager != null) {
                            aitManager.afterTextChanged(s);
                        }
                        //显示发送更多
                        if (null != s) {
                            if (s.toString().length() == 0) {
                                rl_im_more.setVisibility(View.VISIBLE);
                                tv_send.setVisibility(View.GONE);
                            } else {
                                rl_im_more.setVisibility(View.GONE);
                                tv_send.setVisibility(View.VISIBLE);
                            }
                        }
                        //发送正在输入的消息
//                    sendTypingCommand();
                    }
                };
                ed_content.addTextChangedListener(textChangedListener);
            }
            if(null  == aitTextChangeListener){
                aitTextChangeListener = new AitTextChangeListener() {
                    @Override
                    public void onTextAdd(String content, int start, int length) {
                        if (ed_content.getVisibility() != View.VISIBLE ||
                                (emoticonPickerView != null && emoticonPickerView.getVisibility() == View.VISIBLE)) {
                            emojiPanelClick();
                        } else {
                            showInputMethod();
                        }
                        ed_content.getEditableText().insert(start, content);
                    }

                    @Override
                    public void onTextDelete(int start, int length) {
                        if (ed_content.getVisibility() != View.VISIBLE) {
                            emojiPanelClick();
                        } else {
                            showInputMethod();
                        }
                        int end = start + length - 1;
                        ed_content.getEditableText().replace(start, end, "");
                    }
                };
                aitManager.setTextChangeListener(aitTextChangeListener);
            }

        }
    }


    public void init(String teamId,Team team) {
        this.teamId = teamId;
        this.team = team;

        NIMClient.getService(MsgService.class).setChattingAccount(teamId, SessionTypeEnum.Team);
        initAitManager();
        //回复相关
        replyHelper = new ReplyHelper();
        tv_reply.setOnClickListener(
                v -> {
                    //点击清空
                    if(null != replyHelper) {
                        replyHelper.setMessage(null);
                    }
                    tv_reply.setVisibility(View.GONE);
                    tv_reply.setText("");
                }
        );

        registerObservers(true);

        rxLinearLayoutManager = new RxLinearLayoutManager(getActivity());
        messageListView.setLayoutManager(rxLinearLayoutManager);
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {//滑动，输入框收缩
                    RxKeyboardTool.hideKeyboard(getActivity(), ed_content);
                    if (null != emoticonPickerView && emoticonPickerView.getVisibility() == View.VISIBLE) {
                        emojiPanelClick();
                    }
                }
            }
        });
        messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // adapter
        items = new LinkedList<>();
        adapter = new TeamChatAdapter(messageListView, items, this);
        adapter.closeLoadAnimation();
        adapter.setFetchMoreView(new MsgListFetchLoadMoreView());
        adapter.setLoadMoreView(new MsgListFetchLoadMoreView());
        adapter.setOnFetchMoreListener(new TeamMessageFragment.MessageLoader(null)); // load from start
        messageListView.setAdapter(adapter);

        //表情初始化
        emoticonPickerView.show(this);
        btn_audio.setOnRecordListener(this);


        ed_content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ed_content.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    emojiPanelClick();
                }
                return false;
            }
        });

        //默认显示
        tv_emoji.setVisibility(View.VISIBLE);
        ed_content.setVisibility(View.GONE);
        //未被禁言才查询是否可以发言
        ll_input_panel.setVisibility(View.VISIBLE);
        tv_mute.setVisibility(View.GONE);
        canSpeakRequest();
    }

    public void setSkipAdapter(IMMessage anchor) {
        adapter = null;
        items = null;
        items = new LinkedList<>();
        adapter = new TeamChatAdapter(messageListView, items, this);
        adapter.closeLoadAnimation();
        adapter.setFetchMoreView(new MsgListFetchLoadMoreView());
        adapter.setLoadMoreView(new MsgListFetchLoadMoreView());
        TeamMessageFragment.MessageLoader loader = new TeamMessageFragment.MessageLoader(anchor);
        //设置可以双向load
        adapter.setOnFetchMoreListener(loader); // load from start
        adapter.setOnLoadMoreListener(loader);
        messageListView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        RxEventBusTool.unRegisterEventBus(this);
        if (null!=emoticonPickerView){
            emoticonPickerView.gc();
        }
        if (aitManager != null) {
            aitManager.reset();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止语音播放
        MessageAudioControl.getInstance(getActivity()).stopAudio();
    }


    @Override
    public void fetchData() {

    }
    //重新布数据方法
    public void refreshMessageList() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if(null != adapter) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * *************** MessageLoader ***************
     */
    private class MessageLoader implements BaseFetchLoadAdapter.RequestLoadMoreListener, BaseFetchLoadAdapter.RequestFetchMoreListener {

        private int loadMsgCount = NimUIKitImpl.getOptions().messageCountLoadOnce;

        private QueryDirectionEnum direction = null;

        private IMMessage anchor;

        private boolean firstLoad = true;

        public MessageLoader(IMMessage anchor) {
            this.anchor = anchor;
            if (null == anchor) {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
                mIsInitFetchingLocal = true;
            } else {
                loadAnchorContext(); // 加载指定anchor的上下文
            }
        }

        private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                mIsInitFetchingLocal = false;
                if (code != ResponseCode.RES_SUCCESS || exception != null) {
                    if (direction == QueryDirectionEnum.QUERY_OLD) {
                        adapter.fetchMoreFailed();
                    } else if (direction == QueryDirectionEnum.QUERY_NEW) {
                        adapter.loadMoreFail();
                    }

                    return;
                }

                if (messages != null) {
                    onMessageLoaded(messages);
                }
            }
        };

        public void loadAnchorContext() {
            // query new, auto load old
            direction = QueryDirectionEnum.QUERY_NEW;
            NIMClient.getService(MsgService.class)
                    .queryMessageListEx(anchor(), direction, loadMsgCount, true)
                    .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                        @Override
                        public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                            if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                return;
                            }
                            onAnchorContextMessageLoaded(messages);
                        }
                    });
        }

        private void loadFromLocal(QueryDirectionEnum direction) {
            if (mIsInitFetchingLocal) {
                return;
            }
            this.direction = direction;
            NIMClient.getService(MsgService.class)
                    .queryMessageListEx(anchor(), direction, loadMsgCount, true)
                    .setCallback(callback);
        }

        private void loadFromRemote() {
            this.direction = QueryDirectionEnum.QUERY_OLD;
            NIMClient.getService(MsgService.class)
                    .pullMessageHistory(anchor(), loadMsgCount, true)
                    .setCallback(callback);
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return anchor == null ? MessageBuilder.createEmptyMessage(teamId, SessionTypeEnum.Team , 0) : anchor;
            } else {
                int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
                return items.get(index);
            }
        }

        private void onMessageLoaded(final List<IMMessage> messages) {

            if (messages == null) {
                return;
            }
            boolean noMoreMessage = messages.size() < loadMsgCount;

            // 在第一次加载的过程中又收到了新消息，做一下去重
            if (firstLoad && items.size() > 0) {
                for (IMMessage message : messages) {
                    int removeIndex = 0;
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            adapter.remove(removeIndex);
                            break;
                        }
                        removeIndex++;
                    }
                }
            }

            //去掉需要屏蔽的消息
//            List<IMMessage> tempMessages = new ArrayList<>();
//            for(IMMessage message:messages){
//                if(!isNeedDelete(message)){
//                    tempMessages.add(message);
//                }
//            }
//            messages.clear();
//            messages.addAll(tempMessages);



            // 加入anchor
            if (firstLoad && anchor != null) {
                messages.add(anchor);
            }

            // 在更新前，先确定一些标记
            List<IMMessage> total = new ArrayList<>(items);
            boolean isBottomLoad = direction == QueryDirectionEnum.QUERY_NEW;
            if (isBottomLoad) {
                total.addAll(messages);
            } else {
                total.addAll(0, messages);
            }
            // 更新要显示时间的消息
            adapter.updateShowTimeItem(total, true, firstLoad);


            //消息加载完之后，判断是否加载到之前的锚点。
            TeamMessageActivity act = null;
            if(null != getActivity()) {
                act  = ((TeamMessageActivity) getActivity());
                if(null != act.getAnchor()){
                    for(IMMessage message:messages){
                        //假如加载到之前的锚点，则去掉跳转按钮的显示
                        if(null != act ){
                            if(act.getAnchor().isTheSame(message)){
                                act.getNew_tv().setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }


            // 加载状态修改,刷新界面
            if (isBottomLoad) {
                // 底部加载
                if (noMoreMessage) {
                    adapter.loadMoreEnd(messages, true);
                } else {
                    adapter.loadMoreComplete(messages);
                }
            } else {
                // 顶部加载
                if (noMoreMessage) {
                    adapter.fetchMoreEnd(messages, true);
                } else {
                    adapter.fetchMoreComplete(messages);
                }
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                doScrollToBottom();
//                sendReceipt(); // 发送已读回执
            }

            // 通过历史记录加载的群聊消息，需要刷新一下已读未读最新数据
//            if (container.sessionType == SessionTypeEnum.Team) {
                NIMSDK.getTeamService().refreshTeamMessageReceipt(messages);
//            }

            firstLoad = false;
        }

        private void onAnchorContextMessageLoaded(final List<IMMessage> messages) {
            if (messages == null) {
                return;
            }

//            if (remote) {
//                Collections.reverse(messages);
//            }

            int loadCount = messages.size();
            if (firstLoad && anchor != null) {
                messages.add(0, anchor);
            }

            // 在更新前，先确定一些标记
            adapter.updateShowTimeItem(messages, true, firstLoad); // 更新要显示时间的消息
//            updateReceipt(messages); // 更新已读回执标签

//            scrollToPosition(0);

            // new data
            if (loadCount < loadMsgCount) {
                adapter.loadMoreEnd(messages, true);
            } else {
                adapter.appendData(messages);
            }
            firstLoad = false;
        }

        @Override
        public void onFetchMoreRequested() {
            // 顶部加载历史数据
//            if (remote) {
//                loadFromRemote();
//            } else {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
//            }
        }

        @Override
        public void onLoadMoreRequested() {
            // 底部加载新数据
//            if (!remote) {
                loadFromLocal(QueryDirectionEnum.QUERY_NEW);
//            }
        }
    }

    /**
     * *************** MessageLoader结束***************
     */


//    private boolean isNeedDelete(IMMessage data){
//        //尝试去屏蔽所有加入群和离开群的消息
//        try{
//            NotificationAttachment attach = (NotificationAttachment) data.getAttachment();
//            switch (attach.getType()) {
//                case InviteMember:
//                   return true;
//                case KickMember:
//                    return true;
//                case LeaveTeam:
//                    return true;
//                case PassTeamApply:
//                    return true;
//                case AcceptInvite:
//                    return true;
//            }
//        }catch (Exception e){
//
//        }
//        return false;
//    }



    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doScrollToBottom();
            }
        }, 200);

    }

    public void scrollToPosition(int pos) {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doScrollToPosition(pos);
            }
        }, 200);

    }

    private void doScrollToBottom() {
        messageListView.scrollToPosition(adapter.getBottomDataPosition());
    }

    private void doScrollToPosition(int position) {
        messageListView.scrollToPosition(position);
    }


    /**
     * 消息监听 附件进度下载监听，消息状态监听
     *
     * @param register
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
        // 已读回执监听
//        if (NimUIKitImpl.getOptions().shouldHandleReceipt) {
//            service.observeMessageReceipt(messageReceiptObserver, register);
//        }
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
        service.observeRevokeMessage(revokeMessageObserver, register);
//        service.observeTeamMessageReceipt(teamMessageReceiptObserver, register);
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
        MessageListPanelHelper.getInstance().registerObserver(incomingLocalMessageObserver, register);

    }

    /**
     * 本地消息接收观察者
     */
    private MessageListPanelHelper.LocalMessageObserver incomingLocalMessageObserver = new MessageListPanelHelper.LocalMessageObserver() {
        @Override
        public void onAddMessage(IMMessage message) {
            if (message == null || !teamId.equals(message.getSessionId())) {
                return;
            }
            onMsgSend(message);
        }

        @Override
        public void onClearMessages(String account) {
            items.clear();
            refreshMessageList();
            adapter.fetchMoreEnd(null, true);
        }
    };

    /**
     * 用户信息观察者
     */
    private UserInfoObserver userInfoObserver = new UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            // 群的，简单的全部重刷
                adapter.notifyDataSetChanged();
        }
    };

    /**
     * 消息撤回观察者
     */
    private Observer<RevokeMsgNotification> revokeMessageObserver = new Observer<RevokeMsgNotification>() {
        @Override
        public void onEvent(RevokeMsgNotification notification) {
            if (notification == null || notification.getMessage() == null) {
                return;
            }
            IMMessage message = notification.getMessage();
            if (!teamId.equals(message.getSessionId())) {
                return;
            }
            deleteItem(message, false);
        }
    };

    /**
     * 消息附件上传/下载进度观察者
     */
    private Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    /**
     * 消息状态变化观察者
     */
    private Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };

    /**
     * 消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            onIncomingMessage(messages);
        }
    };

    /**
     * 已读回执观察者
     */
//    private Observer<List<MessageReceipt>> messageReceiptObserver = new Observer<List<MessageReceipt>>() {
//        @Override
//        public void onEvent(List<MessageReceipt> messageReceipts) {
////            receiveReceipt();
//        }
//    };

    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        adapter.updateShowTimeItem(addedListItems, false, true);

        adapter.appendData(message);

        doScrollToBottom();
    }

    // 删除消息
    private void deleteItem(IMMessage messageItem, boolean isRelocateTime) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
        List<IMMessage> messages = new ArrayList<>();
        for (IMMessage message : items) {
            if (message.getUuid().equals(messageItem.getUuid())) {
                continue;
            }
            messages.add(message);
        }
//        updateReceipt(messages);
        adapter.deleteItem(messageItem, isRelocateTime);
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    //刷新消息状态逻辑，直接替换列表中的消息。
    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
//            IMMessage item = items.get(index);
//            item.setStatus(message.getStatus());
//            item.setAttachStatus(message.getAttachStatus());
//            // 处理语音、音视频通话
//            if (item.getMsgType() == MsgTypeEnum.audio || item.getMsgType() == MsgTypeEnum.avchat) {
//                item.setAttachment(message.getAttachment()); // 附件可能更新了
//            }
            items.set(index, message);

            // resend的的情况，可能时间已经变化了，这里要重新检查是否要显示时间
            List<IMMessage> msgList = new ArrayList<>(1);
            msgList.add(message);
            adapter.updateShowTimeItem(msgList, false, true);
            refreshViewHolderByIndex(index);
        }
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }
        return -1;
    }

    public void onIncomingMessage(List<IMMessage> messages) {
        boolean needScrollToBottom = isLastMessageVisible();
        boolean needRefresh = false;
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        for (IMMessage message : messages) {
            if (isMyMessage(message)) {
                items.add(message);
                addedListItems.add(message);
                needRefresh = true;
            }
        }
        if (needRefresh) {
            sortMessages(items);
            adapter.notifyDataSetChanged();
        }

        adapter.updateShowTimeItem(addedListItems, false, true);

        // incoming messages tip
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg)) {
            if (needScrollToBottom) {
                doScrollToBottom();
            }
//            else if (incomingMsgPrompt != null && lastMsg.getSessionType() != SessionTypeEnum.ChatRoom) {
//                incomingMsgPrompt.show(lastMsg);
//            }
        }

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


    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) messageListView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= adapter.getBottomDataPosition();
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.Team
                && message.getSessionId() != null
                && message.getSessionId().equals(teamId);
    }

    public void saveMessage(final IMMessage message) {
        if (message == null) {
            return;
        }

        if (items.size() >= MESSAGE_CAPACITY) {
            items.poll();
        }
        items.add(message);

        adapter.updateShowTimeItem(items, false, true);
    }

    /**
     * 刷新单条消息
     */
    private void refreshViewHolderByIndex(final int index) {
        if (null != getActivity()) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (index < 0) {
                        return;
                    }
                    adapter.notifyDataItemChanged(index);
                }
            });
        }
    }

    @Override
    public void onEmojiSelected(String key) {

        RxLogTool.e("onEmojiSelected:" + key);
        Editable mEditable = ed_content.getText();
        if (key.equals("/DEL")) {
            ed_content.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = ed_content.getSelectionStart();
            int end = ed_content.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }

        ed_content.setVisibility(View.VISIBLE);
        tv_emoji.setVisibility(View.GONE);
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName) {
        StickerAttachment stickerAttachment = new StickerAttachment(categoryName, stickerName);
        IMMessage imMessage = MessageBuilder.createCustomMessage(teamId,SessionTypeEnum.Team, stickerAttachment);
        sendMessage(imMessage);
        RxLogTool.e("onStickerSelected categoryName:" + categoryName + "-stickerName:" + stickerName);
    }


    private void sendTextMessage(String text) {

        if (!TextUtils.isEmpty(text)) {
            if (text.trim().equals("")) {
                showTipsAnimation(true, "不能发送空白消息", true);
                return;
            } else {
                String replacement = "**";
                LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(text, replacement);
                int operator = result.getOperator();

                RxLogTool.e("sendTextMessage operator:"+operator);

                if (operator== 1) {
                    IMMessage imMessage = MessageBuilder.createTextMessage(teamId,SessionTypeEnum.Team, result.getContent());
                    sendMessage(imMessage);
                } else if (operator== 2) {
                    showTipsAnimation(true, "你说话太快了，我处理不过来了。", true);
                    return;
                } else if (operator== 3) {
                    IMMessage imMessage = MessageBuilder.createTextMessage(teamId,SessionTypeEnum.Team, text);
                    imMessage.setClientAntiSpam(true);
                    sendMessage(imMessage);
                } else {
                    IMMessage imMessage = MessageBuilder.createTextMessage(teamId,SessionTypeEnum.Team, text);
                    sendMessage(imMessage);
                }


            }

        }
    }


    private void sendPictureMessage(File file) {

        if (null != file && file.exists()) {
            IMMessage imMessage = MessageBuilder.createImageMessage(teamId,SessionTypeEnum.Team, file, file.getName());
            sendMessage(imMessage);
        }
    }

    private void sendAudioMessage(File file, long duration) {

        if (null != file && file.exists() && duration > 0) {
            IMMessage imMessage = MessageBuilder.createAudioMessage(teamId,SessionTypeEnum.Team, file, duration);
            sendMessage(imMessage);
        }
    }

    /**
     * 分享连载号
     *
     * @param bookId
     * @param platformId
     * @param platformName
     * @param platformCover
     */
    private void sendLianzaihaoMessage(String bookId, String platformId, String platformName, String platformCover, String platformIntroduce) {
        ImLianzaihaoAttachment lianzaihaoAttachment = new ImLianzaihaoAttachment(bookId, platformId, platformName, platformCover, platformIntroduce);
        IMMessage imMessage = MessageBuilder.createCustomMessage(teamId,SessionTypeEnum.Team, lianzaihaoAttachment);
        sendMessage(imMessage);
    }

    private void sendVedioMessage(File file, long duration) {
        // TODO: 2018/9/7 发送视频待实现
    }

    private void sendTipsMessage(String tipContent) {
        if (!TextUtils.isEmpty(tipContent)) {
            IMMessage imMessage = MessageBuilder.createTipMessage(teamId,SessionTypeEnum.Team);
            imMessage.setContent(tipContent);
            imMessage.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            imMessage.setConfig(config);

            sendMessage(imMessage);
        }

    }

    // 是否允许发送消息
    protected boolean isAllowSendMessage(final IMMessage message) {
        return true;
    }

    private void appendTeamMemberPush(IMMessage message) {
        if (aitManager == null || replyHelper == null) {
            return;
        }
        List<String> pushList = new ArrayList<>();
        boolean hasReply = false;

        if(null != replyHelper.getMessage() ){
            //添加被引用人的id
            hasReply = true;
            pushList.add(replyHelper.getMessage().getFromAccount());
        }
        pushList.addAll(aitManager.getAitTeamMember());

            if (pushList == null || pushList.isEmpty()) {
                return;
            }
            MemberPushOption memberPushOption = new MemberPushOption();
            memberPushOption.setForcePush(true);
            //推送显示文案，待修正
        if(hasReply){
            String myNick = TeamHelper.getDisplayNameWithoutMe(teamId, message.getFromAccount());
            String replyedNick = TeamHelper.getDisplayNameWithoutMe(teamId, replyHelper.getMessage().getFromAccount());
            memberPushOption.setForcePushContent(myNick +"回复了你");
            Map map = new HashMap();
            map.put("remoteType","quote");
            String content = replyHelper.getMessage().getContent();
            if(content.length() > 50){
                content = content.substring(0,50);
            }
            map.put("name",replyedNick);
            map.put("content", content);
            message.setRemoteExtension(map);
        }else {
            memberPushOption.setForcePushContent(TeamHelper.getDisplayNameWithoutMe(teamId, message.getFromAccount())+"@了你");
            Map map = new HashMap();
            map.put("remoteType","at");
            message.setRemoteExtension(map);
        }
            memberPushOption.setForcePushList(pushList);
            message.setMemberPushOption(memberPushOption);
    }

    private void appendPushConfig(IMMessage message) {
        CustomPushContentProvider customConfig = NimUIKitImpl.getCustomPushContentProvider();
        if (customConfig == null) {
            return;
        }
        String content = customConfig.getPushContent(message);
        Map<String, Object> payload = customConfig.getPushPayload(message);
        if (!TextUtils.isEmpty(content)) {
            message.setPushContent(content);
        }
        if (payload != null) {
            message.setPushPayload(payload);
        }
    }

    // 被对方拉入黑名单后，发消息失败的交互处理
    private void sendFailWithBlackList(int code, IMMessage msg) {
        if (code == ResponseCode.RES_IN_BLACK_LIST) {
            // 如果被对方拉入黑名单，发送的消息前不显示重发红点
            msg.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
            refreshMessageList();
            // 同时，本地插入被对方拒收的tip消息
            IMMessage tip = MessageBuilder.createTipMessage(msg.getSessionId(), msg.getSessionType());
            tip.setContent(getActivity().getString(R.string.black_list_send_tip));
            tip.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            tip.setConfig(config);
            NIMClient.getService(MsgService.class).saveMessageToLocal(tip, true);
        }else {
            msg.setAttachStatus(AttachStatusEnum.fail);
            msg.setStatus(MsgStatusEnum.fail);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(msg);
            refreshMessageList();
        }
    }

    private void sendMessage(IMMessage message) {
        //检查网络是否可用
        if (!RxNetTool.isAvailable()) {
            showTipsAnimation(true, getResources().getString(R.string.send_message_failed_when_network_broken), false);
            return;
        }

        if (isAllowSendMessage(message)) {
            appendTeamMemberPush(message);
//            message = changeToRobotMsg(message);
            final IMMessage msg = message;
            appendPushConfig(message);
            // send message to server and save to db
            NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {

                }

                @Override
                public void onFailed(int code) {
                    //移除最后发送的那条消息
//                    items.remove(msg);
//                    adapter.notifyDataSetChanged();

                    sendFailWithBlackList(code, msg);
//                    if (code == ResponseCode.RES_CHATROOM_MUTED) {
//                        showTipsAnimation(true, "用户被禁言", true);
//                    } else if (code == ResponseCode.RES_CHATROOM_ROOM_MUTED) {
//                        showTipsAnimation(true, "全体禁言", true);
//                    } else {
//                        showTipsAnimation(true, "消息发送失败", true);
//                    }
                }

                @Override
                public void onException(Throwable exception) {

                }
            });

        } else {
            // 替换成tip
            message = MessageBuilder.createTipMessage(message.getSessionId(), message.getSessionType());
            message.setContent("该消息无法发送");
            message.setStatus(MsgStatusEnum.success);
            NIMClient.getService(MsgService.class).saveMessageToLocal(message, false);
        }
        if (aitManager != null) {
            aitManager.reset();
        }
        //清空引用结构
        if (replyHelper != null) {
            replyHelper.setMessage(null);
        }
        tv_reply.setVisibility(View.GONE);
        tv_reply.setText("");

        sendTime = System.currentTimeMillis();//发送时间
        saveMessage(message);
        adapter.notifyDataSetChanged();
        doScrollToBottom();
    }

    /**
     * 显示表情面板
     */
    @OnClick(R.id.iv_im_emoji)
    void emojiClick() {
        if (null != emoticonPickerView) {
            if (emoticonPickerView.getVisibility() == View.VISIBLE) {//收起表情面板
                iv_im_emoji.setImageResource(R.mipmap.v2_im_emoji);
                emojiPanelClick();
            } else {
                iv_audio.setVisibility(View.VISIBLE);
                iv_keyboard.setVisibility(View.GONE);

                btn_audio.setVisibility(View.GONE);
                ll_more_add.setVisibility(View.GONE);
                emoticonPickerView.setVisibility(View.VISIBLE);

                iv_im_emoji.setImageResource(R.mipmap.v2_im_keybord);
                tv_emoji.setVisibility(View.VISIBLE);
                ed_content.setVisibility(View.GONE);

                //隐藏键盘
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            if(null != ed_content) {
                                RxKeyboardTool.hideKeyboard(getActivity(), ed_content);
                            }
                        }catch (Exception e){

                        }
                    }
                }, 200);

            }
        }
    }

    /**
     * 录音点击
     */
    @OnClick(R.id.iv_audio)
    void audioClick() {

        tv_emoji.setVisibility(View.GONE);
        iv_im_emoji.setImageResource(R.mipmap.v2_im_emoji);

        iv_audio.setVisibility(View.GONE);
        iv_keyboard.setVisibility(View.VISIBLE);

        ed_content.setVisibility(View.GONE);
        btn_audio.setVisibility(View.VISIBLE);

        emoticonPickerView.setVisibility(View.GONE);
        ll_more_add.setVisibility(View.GONE);

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if(null != ed_content) {
                        RxKeyboardTool.hideKeyboard(getActivity(), ed_content);
                    }
                }catch (Exception e){

                }
            }
        }, 200);
    }

    /**
     * 键盘点击
     */
    @OnClick(R.id.iv_keyboard)
    void keyboardClick() {
        iv_audio.setVisibility(View.VISIBLE);
        iv_keyboard.setVisibility(View.GONE);

        emojiPanelClick();
    }

    /**
     * 点击，显示输入框,转变为文字输入模式
     */
    @OnClick(R.id.tv_emoji)
    void emojiPanelClick() {
        tv_emoji.setVisibility(View.GONE);
        ll_more_add.setVisibility(View.GONE);
        emoticonPickerView.setVisibility(View.GONE);
        btn_audio.setVisibility(View.GONE);

        ed_content.setVisibility(View.VISIBLE);
        ed_content.requestFocus();

        showInputMethod();
    }

    private void showInputMethod() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    RxKeyboardTool.showSoftInput(getActivity(), ed_content);
                    scrollToBottom();
                } catch (Exception e) {
                }
            }
        }, 200);
    }

    /**
     * 更多-图片，连载号
     */
    @OnClick(R.id.rl_im_more)
    void moreClick() {
        emoticonPickerView.setVisibility(View.GONE);
        btn_audio.setVisibility(View.GONE);


        if (ll_more_add.getVisibility() == View.GONE) {
            ll_more_add.setVisibility(View.VISIBLE);
            emoticonPickerView.setVisibility(View.GONE);

            //录音部分
            iv_audio.setVisibility(View.VISIBLE);
            iv_keyboard.setVisibility(View.GONE);

            //表情
            iv_im_emoji.setImageResource(R.mipmap.v2_im_emoji);

            ed_content.setVisibility(View.GONE);
            tv_emoji.setVisibility(View.VISIBLE);

            //隐藏键盘
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        if(null != ed_content) {
                            RxKeyboardTool.hideKeyboard(getActivity(), ed_content);
                        }
                    }catch (Exception e){

                    }
                }
            }, 200);
        } else {
            ll_more_add.setVisibility(View.GONE);

            //聚焦，弹出键盘
            emojiPanelClick();
        }
    }

    /**
     * 发送
     */
    @OnClick(R.id.tv_send)
    void sendMessageClick() {
        if (!TextUtils.isEmpty(ed_content.getText().toString())) {
            sendTextMessage(ed_content.getText().toString());
            ed_content.setText("");
        }
    }

    /**
     * 选择图片发送
     */
    @OnClick(R.id.tv_choose_pic)
    void choosePictureClick() {
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                RxPhotoTool.openLocalImage(TeamMessageFragment.this);
            }
            @Override
            public void noPermission() {

            }
        }, R.string.im_choose_picture, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 选择图片发送
     */
    @OnClick(R.id.tv_take_pic)
    void takePhotoClick() {
        checkPermission(new PermissionActivity.CheckPermListener() {
            @Override
            public void superPermission() {
                RxPhotoTool.openCameraImage(TeamMessageFragment.this);
            }
            @Override
            public void noPermission() {

            }
        }, R.string.im_take_picture, Manifest.permission.CAMERA);
    }

    @OnClick(R.id.tv_card)
    void sendCardClick() {
        startActivityForResult(new Intent(getSupportActivity(), SendCardActivity.class), SendCardActivity.REQUEST_SEND_CARD);
    }

    @OnClick(R.id.rl_fightluck)
    void rl_fightluckClick() {
        //去除红点
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.SHOW_FIGHT_LUCK_RED, false);
        view_reddot_pinshouqi1.setVisibility(View.GONE);
        view_reddot_pinshouqi2.setVisibility(View.GONE);

        //拼手气跳转
        ActivityFightLuckEnter.startActivity(getActivity(),teamId,2);
    }


    String filePath;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RxLogTool.e("requestCode:" + requestCode);
        if (resultCode == getActivity().RESULT_OK && requestCode == RxPhotoTool.GET_IMAGE_BY_CAMERA) {
            if (null != RxPhotoTool.imageUriFromCamera) {
                try {
                    filePath = RxPhotoTool.getImageAbsolutePath(getActivity(), RxPhotoTool.imageUriFromCamera);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(filePath)) {
                    return;
                }

                RxLogTool.e("filePath:" + filePath);

                String dirPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER;
                RxFileTool.createOrExistsDir(dirPath);

                int degree = RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题

                if (degree > 0) {//旋转图片
                    filePath = RxImageTool.amendRotatePhoto(filePath, dirPath + "/" + System.currentTimeMillis() + ".jpg", getActivity());
                    RxLogTool.e("degree new path:" + filePath);
                }

                File originFile = new File(filePath);
                sendPictureMessage(originFile);

            }

        } else if (resultCode == getActivity().RESULT_OK && requestCode == RxPhotoTool.GET_IMAGE_FROM_PHONE) {
            try {
                filePath = RxPhotoTool.getImageAbsolutePath(getActivity(), data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            RxLogTool.e("filePath:" + filePath);

            String dirPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER;
            RxFileTool.createOrExistsDir(dirPath);

            int degree = RxImageTool.readPictureDegree(filePath);//部分手机拍照图片旋转问题

            if (degree > 0) {//旋转图片
                filePath = RxImageTool.amendRotatePhoto(filePath, dirPath + "/" + System.currentTimeMillis() + ".jpg", getActivity());
                RxLogTool.e("degree new path:" + filePath);
            }
            File originFile = new File(filePath);
            sendPictureMessage(originFile);
        } else if (resultCode == Activity.RESULT_OK && requestCode == SendCardActivity.REQUEST_SEND_CARD) {//发送名片
            RxLogTool.e("SendCardActivity.REQUEST_SEND_CARD....");
            if (null != data && null != data.getExtras()) {
                Bundle bundle = data.getExtras();
                BookStoreBeanN bookStoreBean = (BookStoreBeanN) bundle.getSerializable("book");
                RxLogTool.e("BookStoreBean" + bookStoreBean.toString());
                if(Integer.parseInt(bookStoreBean.getBookId()) > Constant.bookIdLine){
                    sendUrlBookMessage(Integer.parseInt(bookStoreBean.getBookId()),bookStoreBean.getBookCover(),bookStoreBean.getBookName(),bookStoreBean.getPlatformIntroduce(),bookStoreBean.getSource());
                }else {
                    sendLianzaihaoMessage(bookStoreBean.getBookId(), bookStoreBean.getPlatformId(), bookStoreBean.getPlatformName(), bookStoreBean.getPlatformCover(), bookStoreBean.getPlatformIntroduce());
                }
            }
        }else{
            if (aitManager != null) {
                aitManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 分享url识别书籍
     *
     * @param
     */
    private void sendUrlBookMessage( int bookId,  String platformCover, String platformName, String intro,String url) {
        UrlBookBean urlBookBean = new UrlBookBean(bookId,platformCover,platformName,intro,url);
        IMMessage imMessage = MessageBuilder.createCustomMessage(teamId, SessionTypeEnum.Team, urlBookBean);
        sendMessage(imMessage);
    }

    @Override
    public void onFinishedRecord(int length, String audioPath) {
        RxLogTool.e("onFinishedRecord audioPath:" + audioPath + "--length：" + length);
        if (System.currentTimeMillis() - sendTime > 1000) {//一秒内不能重复发送消息
            sendAudioMessage(new File(audioPath), length * 1000);
        }

    }

    boolean hasAudioPermission = false;

    @Override
    public boolean checkPermission() {

        RxLogTool.e("checkPermission......");
        checkPermission(new PermissionActivity.CheckPermListener() {

            @Override
            public void superPermission() {
                hasAudioPermission = true;
            }
            @Override
            public void noPermission() {

            }
        }, R.string.im_record_audio, Manifest.permission.RECORD_AUDIO);

        return hasAudioPermission;
    }


    int currentClickPosition = -1;


    private ArrayList<String> filterImages(IMMessage iMMessage) {
        ArrayList<IMMessage> iMMessages = new ArrayList<>();
        ArrayList<String> imgs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (null != items.get(i).getAttachment() && items.get(i).getAttachment() instanceof ImageAttachment) {
                iMMessages.add(items.get(i));
            }
        }
        for (int i = 0; i < iMMessages.size(); i++) {
            if (iMMessages.get(i).getUuid().equals(iMMessage.getUuid())) {
                currentClickPosition = i;
            }
            MsgAttachment attachment = iMMessages.get(i).getAttachment();
            ImageAttachment imageAttachment = (ImageAttachment) attachment;

            if (null != imageAttachment.getUrl()) {
                imgs.add(imageAttachment.getUrl());//原路径
            } else if (null != imageAttachment.getPath()) {//本地文件-有可能还没发送至服务器
                imgs.add(imageAttachment.getPath());
            }
        }
        return imgs;
    }

    @Override
    public void showImageClick(View view, int pos, IMMessage iMMessage) {
        ArrayList<String> pictureList = filterImages(iMMessage);
        RxLogTool.e("iMMessage uuid:" + iMMessage.getUuid());
        ActivityImagesPreview.startActivity(getActivity(), pictureList, currentClickPosition);
    }

    @Override
    public void lianzaihaoClick(String platformId) {
        ActivityCircleDetail.startActivity(getActivity(), platformId);
    }

    @Override
    public void fightLuckClick(int gameId) {
        ActivityFightLuckDetail.startActivity(getActivity(),gameId);
    }

    @Override
    public void contentLongClick(View view, int pos, IMMessage iMMessage, int x, int y) {
        if (!canSpeak) return;

        //隐藏键盘
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if(null != ed_content) {
                        RxKeyboardTool.hideKeyboard(getActivity(), ed_content);
                        float bottomMargin = ScreenUtil.getDisplayHeight() - ll_input_panel.getY() - RxImageTool.dip2px(55);
                        teamChatMoreOptionPopup.showPopupWindow(view, (int) bottomMargin, iMMessage, x, y);
                    }
                }catch (Exception e){

                }

            }
        }, 200);

    }

    @Override
    public void contentLongClick(View view, int pos, IMMessage iMMessage) {
        if (!canSpeak) return;

        //隐藏键盘
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if(null != ed_content) {
                        RxKeyboardTool.hideKeyboard(getActivity(), ed_content);
                        float bottomMargin = ScreenUtil.getDisplayHeight() - ll_input_panel.getY() - RxImageTool.dip2px(55);
                        teamChatMoreOptionPopup.showPopupWindow(view, (int) bottomMargin, iMMessage, 0, 0);
                    }
                }catch (Exception e){

                }

            }
        }, 200);


    }

    @Override
    public void userLogoClick(IMMessage iMMessage) {
        if (!canSpeak) return;
        if (null == iMMessage) return;
        if(null == aitManager) return;

        aitManager.insertAitMemberInner(iMMessage.getFromAccount(), TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()), AitContactType.TEAM_MEMBER, aitManager.curPos, true);
//        ed_content.setText("");
//        ed_content.setText("@" + TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + " ");
//        int end = ed_content.getText().toString().length();
//        ed_content.setSelection(end);

        //显示输入面板
        emojiPanelClick();
    }

    @Override
    public void retrySendMsgClick(IMMessage iMMessage) {
        rxDialogSureCancelNew.setButtonText("重发", "取消");
        rxDialogSureCancelNew.setTitle("是否重发该消息?");
        rxDialogSureCancelNew.setContent("您的该条消息可能因为网络波动\n信号不好等原因未发送成功,是否重新发送?");
        rxDialogSureCancelNew.show();

        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {

                //发送消息
                sendMessage(iMMessage);
                rxDialogSureCancelNew.dismiss();
            }
        });
    }

    @Override
    public void treadClick(IMMessage iMMessage) {//踩一下
        teamChatMoreOptionPopup.dismiss();
        //检查网络是否可用
        if (!RxNetTool.isAvailable()) {
            showTipsAnimation(true, getResources().getString(R.string.send_message_failed_when_network_broken), false);
            return;
        }
        requestComplain(iMMessage);
    }

    @Override
    public void copyTextClick(IMMessage iMMessage) {
        RxClipboardTool.copyText(getActivity(), iMMessage.getContent());
        //复制文本内容
        teamChatMoreOptionPopup.dismiss();
    }

    @Override
    public void deleteClick(IMMessage iMMessage) {
        //删除
//        items.remove(iMMessage);
        adapter.deleteItem(iMMessage,true);
        adapter.notifyDataSetChanged();
        teamChatMoreOptionPopup.dismiss();
    }

    @Override
    public void quoteClick(IMMessage iMMessage) {
        if (null == iMMessage) return;
        if(null == replyHelper){
            replyHelper = new ReplyHelper();
        }
        if(null == replyHelper.getMessage()) {
            String content = ed_content.getText().toString();
            ed_content.setText(" " + content);//固定格式
            replyHelper.setMessage(iMMessage);
            tv_reply.setVisibility(View.VISIBLE);
            tv_reply.setText(TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + ":" + iMMessage.getContent());
        }else {
            replyHelper.setMessage(iMMessage);
            tv_reply.setVisibility(View.VISIBLE);
            tv_reply.setText(TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + ":" + iMMessage.getContent());
        }
//        teamChatMoreOptionPopup.dismiss();
//        if (iMMessage.getMsgType() == MsgTypeEnum.text) {
//            ed_content.setText("「" + TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + ":" + iMMessage.getContent() + "」\n- - - - - - - - - - - - - - - - - - - - -\n ");
//        } else if (iMMessage.getMsgType() == MsgTypeEnum.audio) {
//            ed_content.setText("「" + TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + ":" + "语音内容」\n- - - - - - - - - - - - - - - - - - - - - \n");
//        } else if (iMMessage.getMsgType() == MsgTypeEnum.image) {
//            ed_content.setText("「" + TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + ":" + "图片」\n- - - - - - - - - - - - - - - - - - - - - \n");
//        } else {
//            showTipsAnimation(true, "暂不支持该类型消息的引用回复", true);
//            return;
//        }

        teamChatMoreOptionPopup.dismiss();
        int end = ed_content.getText().toString().length();
        ed_content.setSelection(end);
        //显示输入面板
        emojiPanelClick();
    }


    /**
     * 踩
     *
     * @param iMMessage
     */
    private void requestComplain(IMMessage iMMessage) {
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);
        map.put("byOperatorAccid", iMMessage.getFromAccount());
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/complain/" + teamId, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    showTipsAnimation(false, "您刚刚投诉了" + TeamHelper.getTeamMemberDisplayName(iMMessage.getSessionId(), iMMessage.getFromAccount()) + "一下", true);
//                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
//                        RxLogTool.e("success:"+baseBean.getMsg());
//                        showTipsAnimation(false,"您刚刚投诉了"+ChatRoomViewHolderHelper.getNameText(chatRoomMessage)+"一下",true);
//
//                    }else{
//                        showTipsAnimation(true,baseBean.getMsg(),true);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showTipsAnimation(boolean isErrorMessage, String showMessage, boolean needHidden) {
        try {
            if (TextUtils.isEmpty(showMessage)) return;
            if (null == tv_tip) return;

            tv_tip.setVisibility(View.VISIBLE);

            if (isErrorMessage) {
                tv_tip.setTextColor(getResources().getColor(R.color.red_color));
            } else {
                tv_tip.setTextColor(getResources().getColor(R.color.v2_blue_color));
            }

            tv_tip.setText(showMessage);
            TranslateAnimation translateInAnimation = new TranslateAnimation(0, 0, -tv_tip.getMeasuredHeight(), 0);

            TranslateAnimation translateOutAnimation = new TranslateAnimation(0, 0, 0, -tv_tip.getMeasuredHeight());
            tv_tip.setAnimation(translateOutAnimation);

            translateInAnimation.setDuration(500);
            translateOutAnimation.setDuration(500);
            translateInAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if (needHidden) {
                        uiHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (null != tv_tip)
                                    tv_tip.startAnimation(translateOutAnimation);
                            }
                        }, 1000);
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tv_tip.setAnimation(translateInAnimation);
            tv_tip.startAnimation(translateInAnimation);


            translateOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tv_tip.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查某个用户是否有发言权限
     */
    private void canSpeakRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/teams/speak/" + teamId,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }
            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("canSpeakRequest:" + response);
                    CanSpeakResponse baseBean = GsonUtil.getBean(response, CanSpeakResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        tv_not_speak.setText(baseBean.getData().getMsg());

                        if (baseBean.getData().isCanSpeak()||accountDetailBean.getData().getUid().equals("16660")) {//||accountDetailBean.getData().getUid().equals("16660")
                            canSpeak = true;
                            tv_not_speak.setVisibility(View.GONE);
                            ll_input_panel.setVisibility(View.VISIBLE);
                        } else {
                            canSpeak = false;
                            tv_not_speak.setText(baseBean.getData().getOperateMsg());
                            tv_not_speak.setVisibility(View.VISIBLE);
                            ll_input_panel.setVisibility(View.GONE);
                        }

                        //拼手气游戏开关
                        if(baseBean.getData().isLuckGameEnable()){
                            rl_fightluck.setVisibility(View.VISIBLE);
                        }

                    } else {
                        showTipsAnimation(true, baseBean.getMsg(), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void teamNotification(DataSynEvent event) {
       if (event.getTag() == Constant.EventTag.FIGHT_LUCK_SUCCESS) {
            //拼手气发起成功后，页面滚动到底部
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doScrollToBottom();
                }
            }, 1000);
        }
    }

}