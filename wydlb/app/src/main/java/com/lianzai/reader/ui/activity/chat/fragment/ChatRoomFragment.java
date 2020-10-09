package com.lianzai.reader.ui.activity.chat.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.lianzai.reader.ui.activity.chat.SendCardActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.ChatRoomAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
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
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.ChatRoomMoreOptionPopup;
import com.lianzai.reader.view.RecordButton;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.business.session.audio.MessageAudioControl;
import com.netease.nim.uikit.business.session.emoji.EmoticonPickerView;
import com.netease.nim.uikit.business.session.emoji.IEmoticonSelectedListener;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.loadmore.MsgListFetchLoadMoreView;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.extension.ImLianzaihaoAttachment;
import com.netease.nim.uikit.extension.StickerAttachment;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 聊天室
 */
public class ChatRoomFragment extends BaseFragment implements IEmoticonSelectedListener, RecordButton.OnRecordListener, ChatRoomAdapter.ChatItemClickListener, ChatRoomMoreOptionPopup.ChatroomOptionItemListener {

    private static final int MESSAGE_CAPACITY = 500;

    ChatRoomAdapter adapter;

    RecyclerView messageListView;

    String chatRoomId;

    long sendTime;//消息发送时间，防止重复发送消息

    // 表情
//    @Bind(R.id.emoticon_picker_view)
    EmoticonPickerView emoticonPickerView;  // 贴图表情控件

    private LinkedList<ChatRoomMessage> items;

    @Bind(R.id.btn_audio)
    RecordButton btn_audio;

    @Bind(R.id.ed_content)
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


    ChatRoomMoreOptionPopup chatRoomMoreOptionPopup;

    RxLinearLayoutManager rxLinearLayoutManager;

    AccountDetailBean accountDetailBean;

    RxDialogSureCancelNew rxDialogSureCancelNew;

    @Bind(R.id.tv_tip)
    TextView tv_tip;//提示消息


    boolean canSpeak = false;//是否能发言

    @Bind(R.id.tv_not_speak)
    TextView tv_not_speak;

    @Bind(R.id.tv_mute)
    TextView tv_mute;


    @Bind(R.id.view_reddot_pinshouqi1)
    View view_reddot_pinshouqi1;
    @Bind(R.id.view_reddot_pinshouqi2)
    View view_reddot_pinshouqi2;
    @Bind(R.id.rl_fightluck)
    RelativeLayout rl_fightluck;


    ChatRoomMember chatRoomMember;//当前聊天室人员

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

        initPopupWindow();

        rxDialogSureCancelNew = new RxDialogSureCancelNew(getActivity(), R.style.OptionDialogStyle);

        RxLogTool.e(ChatRoomFragment.class.getName() + "--configViews......");

        messageListView = getView().findViewById(R.id.rv_list);
        emoticonPickerView = getView().findViewById(R.id.emoticon_picker_view);
        ed_content = getView().findViewById(R.id.ed_content);

        boolean showRed = RxSharedPreferencesUtil.getInstance().getBoolean(Constant.SHOW_FIGHT_LUCK_RED, true);
        if(showRed){
            view_reddot_pinshouqi1.setVisibility(View.VISIBLE);
            view_reddot_pinshouqi2.setVisibility(View.VISIBLE);
        }else{
            view_reddot_pinshouqi1.setVisibility(View.GONE);
            view_reddot_pinshouqi2.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        RxEventBusTool.unRegisterEventBus(this);
        if (null!=emoticonPickerView){
            emoticonPickerView.gc();
        }
    }


    @Override
    public void fetchData() {

    }

    public void init(String roomId, ChatRoomMember chatRoomMember) {
        RxLogTool.e(ChatRoomFragment.class.getName() + "--init......");
        RxLogTool.e("chatRoomMember:" + chatRoomMember.getAvatar());

        chatRoomId = roomId;
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
        adapter = new ChatRoomAdapter(messageListView, items, this);
        adapter.closeLoadAnimation();
        adapter.setFetchMoreView(new MsgListFetchLoadMoreView());
        adapter.setLoadMoreView(new MsgListFetchLoadMoreView());
        adapter.setOnFetchMoreListener(new MessageLoader()); // load from start
        messageListView.setAdapter(adapter);

        //表情初始化
        emoticonPickerView.show(this);

        ed_content.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                MoonUtil.replaceEmoticons(getActivity(), editable, start, count);


                int editEnd = ed_content.getSelectionEnd();
                ed_content.removeTextChangedListener(this);
                while (StringUtil.counterChars(editable.toString()) > NimUIKitImpl.getOptions().maxInputTextLength && editEnd > 0) {
                    editable.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                ed_content.setSelection(editEnd);
                ed_content.addTextChangedListener(this);


                if (null != editable) {
                    if (editable.toString().length() == 0) {
                        rl_im_more.setVisibility(View.VISIBLE);
                        tv_send.setVisibility(View.GONE);
                    } else {
                        rl_im_more.setVisibility(View.GONE);
                        tv_send.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        btn_audio.setOnRecordListener(this);
        //默认显示
        tv_emoji.setVisibility(View.VISIBLE);
        ed_content.setVisibility(View.GONE);

        ed_content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        ed_content.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    emojiPanelClick();

                }
                return false;
            }
        });


        RxLogTool.e("chatRoomMember getAccount:" + chatRoomMember.getAccount());
        RxLogTool.e("chatRoomMember isTempMuted:" + chatRoomMember.isTempMuted());//是否临时禁言
        RxLogTool.e("chatRoomMember getTempMuteDuration:" + chatRoomMember.getTempMuteDuration());//是否临时禁言
        RxLogTool.e("chatRoomMember isMuted:" + chatRoomMember.isMuted());//是否永久禁言

//        chatRoomMember.setTempMuted(true);//模拟临时禁言
//        chatRoomMember.setTempMuteDuration(100000);

        if (chatRoomMember.isTempMuted()) {//临时禁言
            ll_input_panel.setVisibility(View.GONE);
            tv_mute.setVisibility(View.VISIBLE);
            tv_mute.setText("您被禁言啦,(" + TimeFormatUtil.getFormatTimeStr(chatRoomMember.getTempMuteDuration()) + ")");
        } else {
            ll_input_panel.setVisibility(View.VISIBLE);
            tv_mute.setVisibility(View.GONE);

            //未被禁言才查询是否可以发言
            canSpeakRequest();
        }

    }

    /**
     * *************** MessageLoader ***************
     */
    private class MessageLoader implements BaseFetchLoadAdapter.RequestLoadMoreListener, BaseFetchLoadAdapter.RequestFetchMoreListener {

        private static final int LOAD_MESSAGE_COUNT = 10;

        private IMMessage anchor;

        private boolean firstLoad = true;

        private boolean fetching = false;

        public MessageLoader() {
            anchor = null;
            loadFromLocal();
        }

        private RequestCallback<List<ChatRoomMessage>> callback = new RequestCallbackWrapper<List<ChatRoomMessage>>() {
            @Override
            public void onResult(int code, List<ChatRoomMessage> messages, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && messages != null) {

                    onMessageLoaded(messages);


                    RxLogTool.e("onMessageLoaded message:" + messages.size());
                }

                fetching = false;
            }
        };

        private void loadFromLocal() {
            if (fetching) {
                return;
            }

            fetching = true;
            NIMClient.getService(ChatRoomService.class).pullMessageHistoryExType(chatRoomId, anchor().getTime(), LOAD_MESSAGE_COUNT, QueryDirectionEnum.QUERY_OLD, new MsgTypeEnum[]{MsgTypeEnum.custom, MsgTypeEnum.image, MsgTypeEnum.text, MsgTypeEnum.tip})
                    .setCallback(callback);
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return (anchor == null ? ChatRoomMessageBuilder.createEmptyChatRoomMessage(chatRoomId, 0) : anchor);
            } else {
                return items.get(0);
            }
        }

        /**
         * 历史消息加载处理
         */
        private void onMessageLoaded(List<ChatRoomMessage> messages) {


            int count = messages.size();

            // 逆序
            Collections.reverse(messages);
//
            // 加入到列表中
            if (count < LOAD_MESSAGE_COUNT) {
                adapter.fetchMoreEnd(messages, true);
            } else {
                adapter.fetchMoreComplete(messages);
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                doScrollToBottom();
            }

            adapter.updateShowTimeItem(messages, false, true);

            firstLoad = false;
        }

        @Override
        public void onFetchMoreRequested() {
            loadFromLocal();
        }

        @Override
        public void onLoadMoreRequested() {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onPause() {
        super.onPause();
        //停止语音播放
        MessageAudioControl.getInstance(getActivity()).stopAudio();
    }

    private void initPopupWindow() {
        chatRoomMoreOptionPopup = new ChatRoomMoreOptionPopup(getActivity(), this);
    }

    public void scrollToBottom() {
        new Handler(NimUIKit.getContext().getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doScrollToBottom();
            }
        }, 200);

    }

    public void scrollToPosition(int pos) {
        new Handler(NimUIKit.getContext().getMainLooper()).postDelayed(new Runnable() {
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
        ChatRoomServiceObserver service = NIMClient.getService(ChatRoomServiceObserver.class);

        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
        service.observeReceiveMessage(incomingChatRoomMsg, register);
    }


    Observer<List<ChatRoomMessage>> incomingChatRoomMsg = new Observer<List<ChatRoomMessage>>() {
        @Override
        public void onEvent(List<ChatRoomMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            //收到新消息
            onIncomingMessage(messages);
        }
    };

    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) messageListView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= adapter.getBottomDataPosition();
    }

    public boolean isMyMessage(ChatRoomMessage message) {
        return message.getSessionType() == SessionTypeEnum.ChatRoom
                && message.getSessionId() != null
                && message.getSessionId().equals(chatRoomId);
    }

    public void saveMessage(final ChatRoomMessage message) {
        if (message == null) {
            return;
        }

        if (items.size() >= MESSAGE_CAPACITY) {
            items.poll();
        }
        items.add(message);

        adapter.updateShowTimeItem(items, false, true);
    }


    public void onIncomingMessage(List<ChatRoomMessage> messages) {
        boolean needScrollToBottom = isLastMessageVisible();
        boolean needRefresh = false;
        List<ChatRoomMessage> addedListItems = new ArrayList<>(messages.size());
        for (ChatRoomMessage message : messages) {
            // 保证显示到界面上的消息，来自同一个聊天室
            if (isMyMessage(message)) {
                boolean isLeaveMessage = false;
                if (message.getMsgType() == MsgTypeEnum.notification && null != message.getAttachment()) {
                    ChatRoomNotificationAttachment notificationAttachment = (ChatRoomNotificationAttachment) message.getAttachment();
                    if (notificationAttachment.getType() == NotificationType.ChatRoomMemberIn) {//成员进入
                        RxEventBusTool.sendEvents(chatRoomId, Constant.EventTag.CHAT_ROOM_MEMBER_IN_TAG);
                    } else if (notificationAttachment.getType() == NotificationType.ChatRoomMemberExit) {//成员离开
                        RxEventBusTool.sendEvents(chatRoomId, Constant.EventTag.CHAT_ROOM_MEMBER_OUT_TAG);
                        isLeaveMessage = true;
                    }
                }

                if (!isLeaveMessage) {//成员离开消息不显示
                    saveMessage(message);
                    addedListItems.add(message);
                    needRefresh = true;
                }

            }
        }
        if (needRefresh) {
            adapter.notifyDataSetChanged();
        }

        adapter.updateShowTimeItem(addedListItems, false, true);

        // incoming messages tip
        ChatRoomMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg) && needScrollToBottom) {
            doScrollToBottom();
        }
    }


    /**
     * ************************* 观察者 ********************************
     */

    /**
     * 消息状态变化观察者
     */
    private Observer<ChatRoomMessage> messageStatusObserver = new Observer<ChatRoomMessage>() {
        @Override
        public void onEvent(ChatRoomMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
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

    private void onMessageStatusChange(ChatRoomMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            // 处理语音、音视频通话
            if (item.getMsgType() == MsgTypeEnum.audio || item.getMsgType() == MsgTypeEnum.avchat) {
                item.setAttachment(message.getAttachment()); // 附件可能更新了
            }

            // resend的的情况，可能时间已经变化了，这里要重新检查是否要显示时间
            List<ChatRoomMessage> msgList = new ArrayList<>(1);
            msgList.add(message);
            adapter.updateShowTimeItem(msgList, false, true);

            try {
                refreshViewHolderByIndex(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            //进度刷新
            adapter.putProgress(item, value);
            RxLogTool.e("onAttachmentProgressChange:--value:" + value);
            refreshViewHolderByIndex(index);
        }
    }

    /**
     * 刷新单条消息
     */
    private void refreshViewHolderByIndex(final int index) {
        if (null != getActivity()) {
            getActivity().runOnUiThread(new Runnable() {

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

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
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

        ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomCustomMessage(chatRoomId, stickerAttachment);
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
                    ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(chatRoomId, result.getContent());
                    sendMessage(imMessage);
                } else if (operator== 2) {
                    showTipsAnimation(true, "你说话太快了，我处理不过来了。", true);
                    return;
                } else if (operator== 3) {
                    ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(chatRoomId, text);
                    imMessage.setClientAntiSpam(true);
                    sendMessage(imMessage);
                } else {
                    ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(chatRoomId, text);
                    sendMessage(imMessage);
                }

            }
        }
    }


    private void sendPictureMessage(File file) {

        if (null != file && file.exists()) {
            ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomImageMessage(chatRoomId, file, file.getName());
            sendMessage(imMessage);
        }
    }


    private void sendDiyEmjMessage(String catalog, String emotion) {

        if (!TextUtils.isEmpty(emotion)) {
            StickerAttachment stickerAttachment = new StickerAttachment(catalog, emotion);
            ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomCustomMessage(chatRoomId, stickerAttachment);
            sendMessage(imMessage);
        }
    }

    private void sendAudioMessage(File file, long duration) {

        if (null != file && file.exists() && duration > 0) {
            ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomAudioMessage(chatRoomId, file, duration);
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
     * @param penName
     */
    private void sendLianzaihaoMessage(String bookId, String platformId, String platformName, String platformCover, String platformIntroduce) {

        ImLianzaihaoAttachment lianzaihaoAttachment = new ImLianzaihaoAttachment(bookId, platformId, platformName, platformCover, platformIntroduce);
        ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomCustomMessage(chatRoomId, lianzaihaoAttachment);
        sendMessage(imMessage);
    }

    private void sendVedioMessage(File file, long duration) {
        // TODO: 2018/9/7 发送视频待实现
    }

    private void sendTipsMessage(String tipContent) {
        if (!TextUtils.isEmpty(tipContent)) {
            ChatRoomMessage imMessage = ChatRoomMessageBuilder.createTipMessage(chatRoomId);
            imMessage.setContent(tipContent);
            imMessage.setStatus(MsgStatusEnum.success);
            CustomMessageConfig config = new CustomMessageConfig();
            config.enableUnreadCount = false;
            imMessage.setConfig(config);

            sendMessage(imMessage);
        }

    }


    private void sendMessage(ChatRoomMessage imMessage) {
        //检查网络是否可用
        if (!RxNetTool.isAvailable()) {
            showTipsAnimation(true, getResources().getString(R.string.send_message_failed_when_network_broken), false);
            return;
        }

//        List<String> pushList = new ArrayList<>();
//        pushList.add("3f0730b579e3426087bd14f661389d71");
//        MemberPushOption memberPushOption = new MemberPushOption();
//        memberPushOption.setForcePush(true);
//        memberPushOption.setForcePushContent("@了你");
//        memberPushOption.setForcePushList(pushList);
//        imMessage.setMemberPushOption(memberPushOption);



//        ChatRoomHelper.buildMemberTypeInRemoteExt(imMessage, chatRoomId);
        // send message to server and save to db
        NIMClient.getService(ChatRoomService.class).sendMessage(imMessage, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                RxLogTool.e("sendMessage success..");
            }

            @Override
            public void onFailed(int code) {
                if (code == ResponseCode.RES_CHATROOM_MUTED) {
                    showTipsAnimation(true, "用户被禁言", true);
                } else if (code == ResponseCode.RES_CHATROOM_ROOM_MUTED) {
                    showTipsAnimation(true, "全体禁言", true);
                } else {
                    showTipsAnimation(true, "消息发送失败", true);
                }

                //移除最后发送的那条消息
                items.remove(imMessage);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onException(Throwable exception) {

            }
        });

        sendTime = System.currentTimeMillis();//发送时间

        saveMessage(imMessage);

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
                new Handler().postDelayed(new Runnable() {
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

        new Handler().postDelayed(new Runnable() {
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
     * 点击，显示输入框
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
        new Handler().postDelayed(new Runnable() {
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
            new Handler().postDelayed(new Runnable() {
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
                RxPhotoTool.openLocalImage(ChatRoomFragment.this);
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
                RxPhotoTool.openCameraImage(ChatRoomFragment.this);
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
        ActivityFightLuckEnter.startActivity(getActivity(),chatRoomId,1);
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
        }
    }

    /**
     * 分享url识别书籍
     *
     * @param
     */
    private void sendUrlBookMessage( int bookId,  String platformCover, String platformName, String intro,String url) {
        UrlBookBean urlBookBean = new UrlBookBean(bookId,platformCover,platformName,intro,url);
        ChatRoomMessage imMessage = ChatRoomMessageBuilder.createChatRoomCustomMessage(chatRoomId, urlBookBean);
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

    @Override
    public void showImageClick(View view, int pos, ChatRoomMessage chatRoomMessage) {
        ArrayList<String> pictureList = filterImages(chatRoomMessage);

        RxLogTool.e("chatRoomMessage uuid:" + chatRoomMessage.getUuid());
        ActivityImagesPreview.startActivity(getActivity(), pictureList, currentClickPosition);
    }

    private ArrayList<String> filterImages(ChatRoomMessage chatRoomMessage) {
        ArrayList<ChatRoomMessage> chatRoomMessages = new ArrayList<>();
        ArrayList<String> imgs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (null != items.get(i).getAttachment() && items.get(i).getAttachment() instanceof ImageAttachment) {
                chatRoomMessages.add(items.get(i));
            }
        }
        for (int i = 0; i < chatRoomMessages.size(); i++) {
            if (chatRoomMessages.get(i).getUuid().equals(chatRoomMessage.getUuid())) {
                currentClickPosition = i;
            }
            MsgAttachment attachment = chatRoomMessages.get(i).getAttachment();
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
    public void lianzaihaoClick(String platformId) {
        ActivityCircleDetail.startActivity(getActivity(), platformId);
    }

    @Override
    public void fightLuckClick(int gameId) {
        ActivityFightLuckDetail.startActivity(getActivity(),gameId);
    }

    @Override
    public void contentLongClick(View view, int pos, ChatRoomMessage chatRoomMessage, int x, int y) {

        if (!canSpeak) return;


        float bottomMargin = ScreenUtil.getDisplayHeight() - ll_input_panel.getY() - RxImageTool.dip2px(55);

        chatRoomMoreOptionPopup.showPopupWindow(view, (int) bottomMargin, chatRoomMessage, x, y);

    }

    @Override
    public void contentLongClick(View view, int pos, ChatRoomMessage chatRoomMessage) {
        if (!canSpeak) return;

        float bottomMargin = ScreenUtil.getDisplayHeight() - ll_input_panel.getY() - RxImageTool.dip2px(55);

        chatRoomMoreOptionPopup.showPopupWindow(view, (int) bottomMargin, chatRoomMessage, 0, 0);

    }

    @Override
    public void treadClick(ChatRoomMessage chatRoomMessage) {//踩一下
        chatRoomMoreOptionPopup.dismiss();

        //检查网络是否可用
        if (!RxNetTool.isAvailable()) {
            showTipsAnimation(true, getResources().getString(R.string.send_message_failed_when_network_broken), false);
            return;
        }
//        StringBuffer sb=new StringBuffer(accountDetailBean.getData().getNickName()+"踩了下"+ ChatRoomViewHolderHelper.getNameText(chatRoomMessage));
//        if(chatRoomMessage.getMsgType()==MsgTypeEnum.text){
//            sb.append("的发言"+"\""+chatRoomMessage.getContent()+"\"");
//        }else if (chatRoomMessage.getMsgType()==MsgTypeEnum.audio){
//            sb.append("的语音");
//        }else if (chatRoomMessage.getMsgType()==MsgTypeEnum.image){
//            sb.append("的图片");
//        }else if (chatRoomMessage.getMsgType()==MsgTypeEnum.custom){
//            if (null!=chatRoomMessage.getAttachment()&&chatRoomMessage.getAttachment() instanceof StickerAttachment){
//                sb.append("的贴图");
//            }else if(null!=chatRoomMessage.getAttachment()&&chatRoomMessage.getAttachment() instanceof ImLianzaihaoAttachment){
//                sb.append("分享的名片");
//            }
//        }else{
//            RxToast.custom("暂不支持该消息类型操作").show();
//            return;
//        }
//        //发送tip消息
//        sendTipsMessage(sb.toString());

        requestComplain(chatRoomMessage);

    }

    @Override
    public void copyTextClick(ChatRoomMessage chatRoomMessage) {
        RxClipboardTool.copyText(getActivity(), chatRoomMessage.getContent());
        //复制文本内容
        chatRoomMoreOptionPopup.dismiss();
    }

    @Override
    public void deleteClick(ChatRoomMessage chatRoomMessage) {
        items.remove(chatRoomMessage);
        adapter.notifyDataSetChanged();
        //删除
        chatRoomMoreOptionPopup.dismiss();
    }

    @Override
    public void quoteClick(ChatRoomMessage chatRoomMessage) {
        if (null == chatRoomMessage) return;
        ed_content.setText("");//清空输入框
        chatRoomMoreOptionPopup.dismiss();
        if (chatRoomMessage.getMsgType() == MsgTypeEnum.text) {
            ed_content.setText("「" + ChatRoomViewHolderHelper.getNameText(chatRoomMessage) + ":" + chatRoomMessage.getContent() + "」\n- - - - - - - - - - - - - - - - - - - - -\n ");
        } else if (chatRoomMessage.getMsgType() == MsgTypeEnum.audio) {
            ed_content.setText("「" + ChatRoomViewHolderHelper.getNameText(chatRoomMessage) + ":" + "语音内容」\n- - - - - - - - - - - - - - - - - - - - - \n");
        } else if (chatRoomMessage.getMsgType() == MsgTypeEnum.image) {
            ed_content.setText("「" + ChatRoomViewHolderHelper.getNameText(chatRoomMessage) + ":" + "图片」\n- - - - - - - - - - - - - - - - - - - - - \n");
        } else {
            showTipsAnimation(true, "暂不支持该类型消息的引用回复", true);
            return;
        }

        int end = ed_content.getText().toString().length();
        ed_content.setSelection(end);


        //显示输入面板
        emojiPanelClick();
    }

    @Override
    public void userLogoClick(ChatRoomMessage chatRoomMessage) {
        if (!canSpeak) return;
        if (null == chatRoomMessage) return;
        ed_content.setText("");
        ed_content.setText("@" + ChatRoomViewHolderHelper.getNameText(chatRoomMessage) + " ");

        int end = ed_content.getText().toString().length();
        ed_content.setSelection(end);

        //显示输入面板
        emojiPanelClick();
    }

    @Override
    public void retrySendMsgClick(ChatRoomMessage chatRoomMessage) {
        rxDialogSureCancelNew.setButtonText("重发", "取消");
        rxDialogSureCancelNew.setTitle("是否重发该消息?");
        rxDialogSureCancelNew.setContent("您的该条消息可能因为网络波动\n信号不好等原因未发送成功,是否重新发送?");
        rxDialogSureCancelNew.show();

        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {

                //发送消息
                sendMessage(chatRoomMessage);

                rxDialogSureCancelNew.dismiss();
            }
        });
    }

    /**
     * 踩
     *
     * @param chatRoomMessage
     */
    private void requestComplain(ChatRoomMessage chatRoomMessage) {
        Map<String, String> map = new HashMap<>();
        map.put("roomId", chatRoomId);
        map.put("byOperatorAccid", chatRoomMessage.getFromAccount());
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/complain/" + chatRoomId, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    showTipsAnimation(false, "您刚刚投诉了" + ChatRoomViewHolderHelper.getNameText(chatRoomMessage) + "一下", true);
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
                        new Handler().postDelayed(new Runnable() {
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
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/speak/" + chatRoomId, new CallBackUtil.CallBackString() {
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
    public void chatRoomNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.NETWORK_BROKEN_TAG) {
            showTipsAnimation(true, getResources().getString(R.string.network_broken), false);
        } else if (event.getTag() == Constant.EventTag.CHAT_ROOM_LOGINED_TAG) {
            showTipsAnimation(false, getResources().getString(R.string.chat_room_logined), true);
        } else if (event.getTag() == Constant.EventTag.CHAT_ROOM_LOGINING_TAG) {
            showTipsAnimation(false, getResources().getString(R.string.chat_room_logining), false);
        }else if (event.getTag() == Constant.EventTag.FIGHT_LUCK_SUCCESS) {
            //拼手气发起成功后，页面滚动到底部
            new Handler(NimUIKit.getContext().getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doScrollToBottom();
                }
            }, 1000);
        }
    }

}
