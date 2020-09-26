package com.netease.nim.uikit.business.recent;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.TopsInfoBean;
import com.lianzai.reader.bean.VisitorCircleBean;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.chat.AddFriendsActivity;
import com.lianzai.reader.ui.activity.chat.ScanCodeActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DemoCache;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.RxVibrateTool;
import com.lianzai.reader.view.ItemVisitorCircle;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;

/**
 * 最近联系人列表(会话列表)
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class RecentContactsFragment extends TFragment {

    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    // view
    private RecyclerView recyclerView;

    private View emptyBg;
    private LinearLayout visitor_circle;

    private TextView emptyHint;

    // data
    private List<RecentContact> allItems = new ArrayList<>();
    private List<RecentContact> items = new ArrayList<>();

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    PopupWindow morePopupWindow;

    public static boolean isTop = false;//是否置顶


    private ImageView iv_more;
    private RelativeLayout view_toolbar;

    private TextView tv_tip;

    boolean showVisible = true;//是否可见

    String visitorImAccount;
    String visitorImToken;

    private TextView search_tv;

    boolean imIsLogin = false;//IM账号是否已经登录过

    private int listStatus = 1;//列表的状态值，此状态为1时，表示显示圈子列表。为2时，显示群聊列表。
    private TextView tv_title1;
    private TextView tv_title2;
    private TextView tv_title1_red;
    private TextView tv_title2_red;

    private Drawable drawableBlue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nim_recent_contacts, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        showVisible = true;

        drawableBlue= RxTool.getContext().getResources().getDrawable(R.mipmap.blue_line);
        drawableBlue.setBounds(0,0,drawableBlue.getIntrinsicWidth(),drawableBlue.getIntrinsicHeight());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxLogTool.e("RecentContactsFragment onDestroy...");
        RxEventBusTool.unRegisterEventBus(this);
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        showVisible = false;
        RxLogTool.e("showVisible onPause" + showVisible);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RxEventBusTool.registerEventBus(this);
        RxLogTool.e("RecentContactsFragment onActivityCreated");

        //im处理
        processIm();
    }

    private void processIm() {
        try {
            init();
            if (RxNetTool.isAvailable()) {
                tryOptions();
            } else {
                requestMessages(true);
                if (isAdded()) {
                    showTipsAnimation(true, getResources().getString(R.string.network_is_not_available), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            findViews();
            initMessageList();
            registerObservers(true);
            registerOnlineStateChangeListener(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = findView(R.id.recycler_view);
        emptyBg = findView(R.id.emptyBg);
        visitor_circle = findView(R.id.visitor_circle);
        emptyHint = findView(R.id.message_list_empty_hint);
        view_toolbar = findView(R.id.view_toolbar);
        iv_more = findView(R.id.iv_more);
        tv_tip = findView(R.id.tv_tip);
        search_tv = findView(R.id.search_tv);
        tv_title1 = findView(R.id.tv_title1);
        tv_title2 = findView(R.id.tv_title2);
        tv_title1_red= findView(R.id.tv_title1_red);
        tv_title2_red= findView(R.id.tv_title2_red);
        tv_title1.setOnClickListener(
                v -> {
                    if(listStatus == 1){
                        return;
                    }else {
                        listStatus = 1;
                        tv_title1.setTextColor(getResources().getColor(R.color.normal_text_color));
                        tv_title1.setTextSize(26);
                        tv_title1.setCompoundDrawables(null,null,null,drawableBlue);
                        tv_title2.setTextColor(getResources().getColor(R.color.third_text_color));
                        tv_title2.setTextSize(16);
                        tv_title2.setCompoundDrawables(null,null,null,null);
                        tv_title1_red.setVisibility(View.GONE);

                        if(RxLoginTool.isLogin()){
                            if(!TextUtils.isEmpty(tv_title2_red.getText())){
                                tv_title2_red.setVisibility(View.VISIBLE);
                            }
                            refreshMessages(false);
                            msgLoaded = false;
                            requestMessages(false);
                        }else {
                            msgLoaded = false;
                            requestMessages(false);
                        }
                    }
                }
        );
        tv_title2.setOnClickListener(
                v -> {
                    if(listStatus == 2){
                        return;
                    }else {
                        listStatus = 2;
                        tv_title2.setTextColor(getResources().getColor(R.color.normal_text_color));
                        tv_title2.setTextSize(26);
                        tv_title2.setCompoundDrawables(null,null,null,drawableBlue);
                        tv_title1.setTextColor(getResources().getColor(R.color.third_text_color));
                        tv_title1.setTextSize(16);
                        tv_title1.setCompoundDrawables(null,null,null,null);
                        tv_title2_red.setVisibility(View.GONE);


                        if(RxLoginTool.isLogin()){
                            if(!TextUtils.isEmpty(tv_title1_red.getText())){
                                tv_title1_red.setVisibility(View.VISIBLE);
                            }
                            refreshMessages(false);
                            msgLoaded = false;
                            requestMessages(false);
                        }else {
                            msgLoaded = false;
                            requestMessages(false);
                        }

                    }
                }
        );

        //搜索按钮点击
        search_tv.setOnClickListener(
                v -> {
                    ActivitySearchFirst.startActivity(getActivity());
                }
        );

        initPopupWindow();

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != morePopupWindow) {
                    morePopupWindow.showAsDropDown(iv_more, RxDeviceTool.getScreenWidth(getActivity()) - morePopupWindow.getWidth(), 0);
                }
            }
        });
    }

    /**
     * 更多弹框
     */
    private void initPopupWindow() {
        FrameLayout contentView = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.popup_more, null);
        TextView tv_to_chat_room = contentView.findViewById(R.id.tv_to_chat_room);
        TextView tv_add_friend = contentView.findViewById(R.id.tv_add_friend);
        TextView tv_scan_code = contentView.findViewById(R.id.tv_scan_code);

        morePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        morePopupWindow.setOutsideTouchable(true);
        morePopupWindow.setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), (Bitmap) null));
        morePopupWindow.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        morePopupWindow.setFocusable(true);

        tv_to_chat_room.setOnClickListener(
                v -> {
                    morePopupWindow.dismiss();
                }
        );
        tv_add_friend.setOnClickListener(
                v -> {
                    AddFriendsActivity.startActivity(getContext());
                    morePopupWindow.dismiss();
                }
        );

        tv_scan_code.setOnClickListener(
                v -> {

                    checkPermission(new PermissionActivity.CheckPermListener() {
                        @Override
                        public void superPermission() {
                            ScanCodeActivity.startActivity(getActivity());
                            morePopupWindow.dismiss();
                        }
                        @Override
                        public void noPermission() {

                        }
                    }, R.string.ask_camera, Manifest.permission.CAMERA);

                }
        );
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        allItems = new ArrayList<>();
        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);

        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(touchListener);

        // ios style
//        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onSwipeItemClick(RecentContact recent, int position, int type) {
                if (type == Constant.SwipeType.TOP || type == Constant.SwipeType.CANCEL_TOP) {//置顶-取消置顶操作
                    if (isTagSet(recent, RECENT_TAG_STICKY)) {
                        removeTag(recent, RECENT_TAG_STICKY,true);
                    } else {
                        addTag(recent, RECENT_TAG_STICKY,true);
                    }
                    NIMClient.getService(MsgService.class).updateRecent(recent);
                    refreshMessages(false);
                } else if (type == Constant.SwipeType.DELETE) {//删除会话
                    // 删除会话，删除后，消息历史被一起删除
                    NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                    NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                    for (RecentContact item : allItems) {
                        if(item.getContactId().equals(recent.getContactId())){
                            allItems.remove(item);
                            break;
                        }
                    }
                    refreshMessages(true);
                }
            }

            @Override
            public void onItemClick(RecentContact recent, int position) {
                if (isTagSet(recent, RECENT_TAG_STICKY)) {
                    isTop = true;
                } else {
                    isTop = false;
                }
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    if (recent.getUnreadCount() < 10) {
                        NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                    } else {
                        getTeamMessage(recent);
                    }
                } else if (recent.getSessionType() == SessionTypeEnum.P2P && listStatus == 2) {//连载号详情
                    RxLogTool.e("recent getFromAccount:" + recent.getFromNick());
//                  imAccount = recent.getFromAccount();
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }else if(recent.getSessionType() == SessionTypeEnum.P2P && listStatus == 1){
                    //直接进入圈子页面，不执行之前的逻辑。
                    String userJson = UserInfoHelper.getExtension(recent.getContactId());
                    try {
                        org.json.JSONObject object = new org.json.JSONObject(userJson);
                        int identity = 0;
                        int objectId = 0;
                        identity = object.getInt("type");
                        objectId = object.getInt("objectId");//type 0普通用户 1官方连载号 2书连载号，objectId 0对应uid，1、2对应platfromId
                        if (identity == 1 &&  RxAppTool.isCircle(objectId) ) {
                            ActivityCircleDetail.startActivity(getActivity(),String.valueOf(objectId),1);
                        } else if (identity == 2) {
                            ActivityCircleDetail.startActivity(getActivity(),String.valueOf(objectId),1);
                        } else if (identity == 3) {
                            ActivityCircleDetail.startActivity(getActivity(),String.valueOf(objectId),1);
                        } else if (identity == 4) {
                            ActivityCircleDetail.startActivity(getActivity(),String.valueOf(objectId),1);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {
        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent, position);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
//            showLongClickMenu(adapter.getItem(position), position);
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {

        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {

        }
    };

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
        }
    };

    private void addTag(RecentContact recent, long tag ,boolean up) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);

        //上传置顶配置
        if(up){
            upLoadTop(true,recent.getContactId());
        }

    }

    private void removeTag(RecentContact recent, long tag,boolean up) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);

        //上传置顶配置
        if(up){
            upLoadTop(false,recent.getContactId());
        }

    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }


    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if(!RxLoginTool.isLogin()){
            //未登陆不操作
            if(isAdded()){
               visitorShow();
            }
            return;
        }
        if (msgLoaded) {
            return;
        }
        getHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        RxLogTool.e("queryRecentContacts onResult:" + code);
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        loadedRecents = recents;
                        // 初次加载，更新离线的消息中是否有@我的消息
                        for (RecentContact loadedRecent : loadedRecents) {
                            if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                //更新艾特信息
                                updateOfflineContactAited(loadedRecent);
                            }
                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                        tv_tip.setVisibility(View.GONE);
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        allItems.clear();
        //此处进行遍历筛选
        if (loadedRecents != null) {
            allItems.addAll(loadedRecents);
            loadedRecents = null;
        }

        //同步置顶配置
        topSynchronization();

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    //用作区分在第一个tab还是第二个tab
    private int getExtensionInformation(RecentContact item) {
        if(item.getSessionType() != SessionTypeEnum.P2P){
            //不是单聊直接放到聊天tab
            return 2;
        }else {
            String userJson = UserInfoHelper.getExtension(item.getContactId());
            if(TextUtils.isEmpty(userJson)){
                //假如没有用户信息，则去获取，获取到之后刷会话列表
                List<String> account = new ArrayList<>();
                account.add(item.getContactId());
                NIMClient.getService(UserService.class).fetchUserInfo(account).setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        RxLogTool.e("fetchUserInfo onSuccess:" + nimUserInfos.size());
                        try {
                            refreshMessages(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(int i) {
                        RxLogTool.e("fetchUserInfo:" + i);
                    }

                    @Override
                    public void onException(Throwable throwable) {
                    }
                });
            }else {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(userJson);
                    int identity = 0;
                    int objectId = 0;
                    identity = object.getInt("type");
                    objectId = object.getInt("objectId");//type 0普通用户 1官方连载号 2书连载号，objectId 0对应uid，1、2对应platfromId
                    if (identity == 1 &&  RxAppTool.isCircle(objectId) ) {
                        return 1;
                    } else if (identity == 2) {
                        return 1;
                    } else if (identity == 3) {
                        return 1;
                    } else if (identity == 4) {
                        return 1;
                    } else {
                        return 2;
                    }
                } catch (Exception e) {
                }
            }
        }
        return 2;
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(allItems);

        items.clear();
        int unreadNum = 0;
        int unreadNum1 = 0;
        int unreadNum2 = 0;
        //列表遍历筛选
        for (RecentContact item : allItems) {
            int viewType = 0;
            try {
                viewType = getExtensionInformation(item);
            }catch (Exception e){
            }
            //当数据类别和列表类别相等时，加入列表数据中
            if(listStatus == viewType){
                items.add(item);
            }

            //计算未读数
            if(unreadChanged){
                unreadNum += item.getUnreadCount();
                if(viewType == 1){
                    unreadNum1 += item.getUnreadCount();
                }else {
                    unreadNum2 += item.getUnreadCount();
                }
            }
        }


        notifyDataSetChanged();

        if (unreadChanged) {
            Badger.updateBadgerCount(unreadNum);
            //首页刷新
            RxEventBusTool.sendEvents(String.valueOf(unreadNum), Constant.EventTag.REFRESH_HOME_RED_DOT_TAG);
            //tab红点更新
            if(unreadNum1 > 0) {
                tv_title1_red.setText(String.valueOf(unreadNum1 > 99 ? 99 : unreadNum1));
                if(listStatus == 2){
                    tv_title1_red.setVisibility(View.VISIBLE);
                }
            }else {
                tv_title1_red.setText("");
                tv_title1_red.setVisibility(View.GONE);
            }
            if(unreadNum2 > 0) {
                tv_title2_red.setText(String.valueOf(unreadNum2 > 99 ? 99 : unreadNum2));
                if(listStatus == 1){
                    tv_title2_red.setVisibility(View.VISIBLE);
                }
            }else {
                tv_title2_red.setText("");
                tv_title2_red.setVisibility(View.GONE);
            }
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };


    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
    }

//    private void registerDropCompletedListener(boolean register) {
//        if (register) {
//            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
//        } else {
//            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
//        }
//    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                RxLogTool.e("messageReceiverObserver receive new message...");
//                //新消息
                for (IMMessage imMessage : imMessages) {

                    if (!showVisible) {
                        msgLoaded = false;
                        requestMessages(true);
                    }

                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }

                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);

                    //收到消息震动一下
                    if (msgLoaded) {
                        //假如是开启消息免打扰,则不震动
                        boolean mute = false;
                        try {
                            //判断消息是否开启了免打扰
                            Team team = NimUIKit.getTeamProvider().getTeamById(imMessage.getSessionId());
                            TeamMessageNotifyTypeEnum type = team.getMessageNotifyType();
                            if (type.equals(TeamMessageNotifyTypeEnum.Mute)) {
                                mute = true;
                            }
                        } catch (Exception e) {
                        }
                        if (!mute) {
                            RxVibrateTool.vibrateOnce(BuglyApplicationLike.getContext(), 100);
                        }
                    }

                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < allItems.size(); i++) {
                if (r.getContactId().equals(allItems.get(i).getContactId())
                        && r.getSessionType() == (allItems.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }


            if (index >= 0) {
                allItems.remove(index);
            }
            allItems.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }

        cacheMessages.clear();

        refreshMessages(true);
    }
//
//    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
//        @Override
//        public void onCompleted(Object id, boolean explosive) {
//            if (cached != null && !cached.isEmpty()) {
//                // 红点爆裂，已经要清除未读，不需要再刷cached
//                if (explosive) {
//                    RxLogTool.e("IDropCompletedListener.......");
//                    if (id instanceof RecentContact) {
//                        RecentContact r = (RecentContact) id;
//                        cached.remove(r.getContactId());
//                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
//                        cached.clear();
//                    }
//
//                }
//
//                // 刷cached
//                if (!cached.isEmpty()) {
//                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
//                    recentContacts.addAll(cached.values());
//                    cached.clear();
//
//                    onRecentContactChanged(recentContacts);
//                }
//            }
//        }
//    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : allItems) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        allItems.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                allItems.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {

        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

//    private void registerUserInfoObserver() {
//        if (userInfoObserver == null) {
//            userInfoObserver = new UserInfoObserver() {
//                @Override
//                public void onUserInfoChanged(List<String> accounts) {
//                    refreshMessages(false);
//                }
//            };
//        }
//        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
//    }
//
//    private void unregisterUserInfoObserver() {
//        if (userInfoObserver != null) {
//            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
//        }
//    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            return;
        }

        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    Set<IMMessage> messages = null;
                    // 过滤存在的@我的消息
                    for (IMMessage msg : result) {

                        if (TeamMemberAitHelper.isAitMessage(msg)) {
                            if (messages == null) {
                                messages = new HashSet<>();
                            }
                            messages.add(msg);
                        }
                    }

                    // 更新并展示
                    if (messages != null) {
                        TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void getTeamMessage(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            TeamMessageActivity.startActivity(getActivity(), recentContact.getContactId());
            return;
        }
        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            TeamMessageActivity.startActivity(getActivity(), recentContact.getContactId());
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    // 过滤存在的@我的消息
                    for (int i = 0; i < result.size(); i++) {
                        IMMessage msg = result.get(i);
                        if (TeamMemberAitHelper.isAitMessage(msg)) {
                            TeamMessageActivity.startActivity(getActivity(), recentContact.getContactId(), msg, 0);
                            break;
                        } else if (i == result.size() - 1) {
                            TeamMessageActivity.startActivity(getActivity(), recentContact.getContactId(), msg, recentContact.getUnreadCount());
                            break;
                        }
                    }
                } else {
                    TeamMessageActivity.startActivity(getActivity(), recentContact.getContactId());
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        RxLogTool.e("RecentContactsFragment eventBusNotification.......");
        if (event.getTag() == Constant.EventTag.CONTACT_TOP_TAG) {
            String bookId = event.getObj().toString();
            if (!TextUtils.isEmpty(bookId)) {
                topRecentContactByBookId(bookId, true);
            }

        } else if (event.getTag() == Constant.EventTag.CONTACT_CALCLE_TOP_TAG) {//取消置顶
            String bookId = event.getObj().toString();
            if (!TextUtils.isEmpty(bookId)) {
                topRecentContactByBookId(bookId, false);
            }
        } else if (event.getTag() == Constant.EventTag.REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG) {//移除会话
            String bookId = event.getObj().toString();
            RxLogTool.e("REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG bookid:" + bookId);
            if (!TextUtils.isEmpty(bookId)) {
                removeRecentContactByBookId(bookId);
            }
        }
        else if (event.getTag() == Constant.EventTag.CHAT_ROOM_EXIT) {
            String bookId = event.getObj().toString();
            RxLogTool.e("REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG bookid:" + bookId);
            if (!TextUtils.isEmpty(bookId)) {
                removeRecentContactByBookId(bookId);
            }
        }
        else if (event.getTag() == Constant.EventTag.REFRESH_RED_DOT_TAG) {//刷新
            refreshMessages(true);
        } else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录刷新
            //重新刷新一遍数据
            tryOptions();
        } else if (event.getTag() == Constant.EventTag.LOGIN_OUT_REFRESH_TAG) {//退出刷新
            visitorShow();
        } else if (event.getTag() == Constant.EventTag.MESSAGE_REFRESH) {//消息免打扰开关刷新
            //重新刷新一遍视图
            notifyDataSetChanged();
        } else if (event.getTag() == Constant.EventTag.NETWORK_DISCONNECT_TAG) {//网络断开
            if (isAdded()) {
                showTipsAnimation(true, getResources().getString(R.string.network_is_not_available), false);
            }
        } else if (event.getTag() == Constant.EventTag.NETWORK_CONNECT_TAG) {//网络已连接
            if (isAdded()) {
                showTipsAnimation(false, getResources().getString(R.string.network_is_available), true);
                tryOptions();
            }
        }
    }

    /*同步置顶配置*/
    private void topSynchronization(){
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/tops/info", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                refreshMessages(true);
            }

            @Override
            public void onResponse(String response) {
                try {
                    TopsInfoBean baseBean = GsonUtil.getBean(response, TopsInfoBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                       if(null != baseBean.getData() && !baseBean.getData().isEmpty()){
                          for(String item : baseBean.getData()){

                              for (RecentContact recent : allItems) {
                                  try {
                                      if (String.valueOf(recent.getContactId()).equals(item)) {
                                          //置顶
                                          addTag(recent, RECENT_TAG_STICKY,false);
                                          NIMClient.getService(MsgService.class).updateRecent(recent);
                                          break;
                                      }
                                  } catch (Exception e) {
                                  }
                              }

                          }
                       }
                    }

                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            refreshMessages(true);
                        }
                    });

                } catch (Exception e) {
                    refreshMessages(true);
                }
            }
        });
    }

    private void upLoadTop(boolean up ,String id){
        if(up){
            OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/tops/" + id + "/top", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                }

                @Override
                public void onResponse(String response) {
                }
            });
        }else {
            OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/tops/" + id + "/unTop", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                }

                @Override
                public void onResponse(String response) {
                }
            });
        }
    }

    /**
     * 更加bookId置顶
     *
     * @param bookId
     */
    private void topRecentContactByBookId(String bookId, boolean isTop) {
        int position = 0;
        for (RecentContact recent : allItems) {
            try {
                if (String.valueOf(recent.getContactId()).equals(bookId)) {
                    if (isTop) {//置顶
                        addTag(recent, RECENT_TAG_STICKY,true);
                    } else {//取消置顶
                        removeTag(recent, RECENT_TAG_STICKY,true);
                    }
                    NIMClient.getService(MsgService.class).updateRecent(recent);

                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            refreshMessages(true);
                        }
                    });
                } else {
                    String json = UserInfoHelper.getExtension(recent.getContactId());
                    org.json.JSONObject object = new org.json.JSONObject(json);
                    int platformId = object.getInt("objectId");//type 0普通用户 1官方连载号 2书连载号，objectId 0对应uid，1、2对应platfromId
                    if (String.valueOf(platformId).equals(bookId)) {
                        if (isTop) {//置顶
                            addTag(recent, RECENT_TAG_STICKY,true);
                        } else {//取消置顶
                            removeTag(recent, RECENT_TAG_STICKY,true);
                        }
                        NIMClient.getService(MsgService.class).updateRecent(recent);

                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                refreshMessages(true);
                            }
                        });
                    }
                }
                position++;

            } catch (Exception e) {
                RxLogTool.e("UserInfoHelper.getExtension ex:" + e.getMessage());
            }

        }
    }

    /**
     * 根据书本ID移除会话
     *
     * @param bookId
     */
    private void removeRecentContactByBookId(String bookId) {
        int position = 0;
        for (RecentContact recent : allItems) {
            try {
                if (String.valueOf(recent.getContactId()).equals(bookId)) {
                    NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                    NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                    allItems.remove(position);
                    postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            refreshMessages(true);
                        }
                    });
                } else {
                    String json = UserInfoHelper.getExtension(recent.getContactId());
                    org.json.JSONObject object = new org.json.JSONObject(json);
                    int platformId = object.getInt("objectId");//type 0普通用户 1官方连载号 2书连载号，objectId 0对应uid，1、2对应platfromId
                    if (String.valueOf(platformId).equals(bookId)) {
                        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                        allItems.remove(position);
                        postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                refreshMessages(true);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                RxLogTool.e("UserInfoHelper.getExtension ex:" + e.getMessage());
            }
            position++;
        }
    }


//    /**
//     * 游客接口请求-获取imtoken imaccount
//     */
//
//    private void requestVisitorBook() {
//        //生成唯一设备号
//        String deviceNo = RxSharedPreferencesUtil.getInstance().getString("deviceNo");
//        if (TextUtils.isEmpty(deviceNo)) {
//            int num = (int) ((Math.random() * 9 + 1) * 100000);
//            deviceNo = RxTimeTool.getCurTimeString() + "Android" + num;
//            deviceNo = RxEncryptTool.encryptMD5ToString(deviceNo, "AndroidSalt");
//            RxSharedPreferencesUtil.getInstance().putString("deviceNo", deviceNo);
//        }
//        //加密操作
//        Map map = new HashMap();
//        map.put("deviceNo", deviceNo);
//        map.put("terminal", String.valueOf(Constant.Channel.ANDROID));
//        map.put("pageSize", "9");
//        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
//        parameters.putAll(map);
//        String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
//
//        //上传map2
//        Map map2 = new HashMap();
//        map2.put("pageSize", "9");
//        map2.put("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
//
//
//        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/user/touristLogin", map2, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                if (isAdded()) {
//                    showTipsAnimation(true, getResources().getString(R.string.im_login_failed), false);
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    RxLogTool.e("touristLogin:" + response);
//                    BookStoreResponseForVisitor bookStoreResponseforvisitor = GsonUtil.getBean(response, BookStoreResponseForVisitor.class);
//                    if (bookStoreResponseforvisitor.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {//成功
//                        visitorImAccount = bookStoreResponseforvisitor.getData().getImInfo().getYxAccid();
//                        visitorImToken = bookStoreResponseforvisitor.getData().getImInfo().getYxToken();
//                        //登录云信。
//                        imLogin(visitorImAccount, visitorImToken);
//                    } else {//游客数据返回失败
//                        if (isAdded()) {
//                            showTipsAnimation(true, "服务器繁忙，请稍后再试", false);
//                        }
//                    }
//                } catch (Exception e) {
//                    if (isAdded()) {
//                        //初始化
//                        showTipsAnimation(true, "服务器繁忙，请稍后再试", false);
//                    }
//                }
//
//            }
//
//        });
//    }


    private void imLogin(String imaccount, String imtoken) {
        if (!imIsLogin) {//未登录过才允许登录
            RxLogTool.e("RecentContactsFragment  imLogin....");

            NimUIKit.login(new LoginInfo(imaccount, imtoken), new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {

                    //云信登录成功
                    try {
                        RxLogTool.e("NimUIKit onSuccess:" + loginInfo.getAccount());
                        DemoCache.setAccount(imaccount);
                        RxTool.saveLoginInfo(imaccount, imtoken);

                        imIsLogin = true;

                        msgLoaded = false;
                        requestMessages(false);


                        if (tv_tip.getVisibility() == View.VISIBLE) {
                            tv_tip.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailed(int i) {
                    if (isAdded()) {
                        RxLogTool.e("NimUIKit.....onFailed--code" + i);
                        //重新登录IM
                        //初始化
                        showTipsAnimation(true, getResources().getString(R.string.im_login_failed415), false);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    if (isAdded()) {
                        RxLogTool.e("NimUIKit.....onException-----" + throwable.getMessage());
                        showTipsAnimation(true, getResources().getString(R.string.im_login_failed), false);
                    }
                }
            });

            RxLogTool.e("start nim login......" + imaccount + "--nim token--" + imtoken);
        } else {
            //已经登录的情况下，再请求一次最近会话
            msgLoaded = false;
            requestMessages(false);
        }
    }


    private void tryOptions() {
            if (RxLoginTool.isLogin()) {
                if (!msgLoaded) {//未加载数据
                    visitorImAccount = RxLoginTool.getLoginAccountToken().getData().getImAccount();
                    visitorImToken = RxLoginTool.getLoginAccountToken().getData().getImToken();
                    imLogin(visitorImAccount, visitorImToken);
                } else {
                    msgLoaded = false;
                    //重新拉取数据
                    requestMessages(false);
                }
            }else {
                if(isAdded()){
                   visitorShow();
                }
            }
    }

    private void visitorShow(){
        tv_title1_red.setText("");
        tv_title1_red.setVisibility(View.GONE);
        tv_title2_red.setText("");
        tv_title2_red.setVisibility(View.GONE);
        msgLoaded = true;
        allItems.clear();
        items.clear();
        notifyDataSetChanged();
    }

    private void requestVisitorShow(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/platforms/visitor/circle", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    VisitorCircleBean visitorCircleBean = GsonUtil.getBean(response, VisitorCircleBean.class);
                    if (visitorCircleBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        visitor_circle.removeAllViews();
                        if (null != visitorCircleBean.getData() && !visitorCircleBean.getData().isEmpty()) {
                            for(VisitorCircleBean.DataBean item : visitorCircleBean.getData()){
                                ItemVisitorCircle itemCircle = new ItemVisitorCircle(getActivity());
                                itemCircle.bindData(item);
                                if(listStatus == getType(item)) {
                                    visitor_circle.addView(itemCircle);
                                }
                            }
                            if(visitor_circle.getChildCount() > 0) {
                                visitor_circle.setVisibility(View.VISIBLE);
                                emptyBg.setVisibility(View.GONE);
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private int getType(VisitorCircleBean.DataBean bean){
        int identity = bean.getPlatformType();
        int objectId = bean.getId();
        if (identity == 1 && RxAppTool.isCircle(objectId) ) {
            return 1;
        } else if (identity == 2) {
            return 1;
        } else if (identity == 3) {
            return 1;
        } else if (identity == 4) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 异常消息提示
     *
     * @param isErrorMessage
     * @param showMessage
     * @param needHidden
     */
    private void showTipsAnimation(boolean isErrorMessage, String showMessage, boolean needHidden) {

        try {
            if (!msgLoaded && null == recyclerView) {//未初始化过
                init();
            }

            if (TextUtils.isEmpty(showMessage)) return;
            if (null == tv_tip) return;

            tv_tip.setOnClickListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    if (RxNetTool.isAvailable()) {
                        tv_tip.setBackgroundColor(getResources().getColor(R.color.v2_blue_color));
                        tv_tip.setText("正在重连，请稍后...");
                        tryOptions();
                    } else {
                        requestMessages(false);
                        RxToast.custom(getResources().getString(R.string.network_is_not_available), Constant.ToastType.TOAST_NETWORK_DISCONNECT).show();
                    }
                }
            });
            tv_tip.setVisibility(View.VISIBLE);


            tv_tip.setTextColor(getResources().getColor(R.color.white));
            if (isErrorMessage) {
                tv_tip.setBackgroundColor(getResources().getColor(R.color.redFE3838));
            } else {
                tv_tip.setBackgroundColor(getResources().getColor(R.color.v2_blue_color));
            }

            tv_tip.setText(showMessage);

            if (needHidden) {
                tv_tip.setVisibility(View.GONE);
            } else {
                tv_tip.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void notifyDataSetChanged() {
        try {
            adapter.notifyDataSetChanged();
            boolean empty = items.isEmpty() && msgLoaded;
            if(empty){
                emptyBg.setVisibility(View.VISIBLE);
                emptyHint.setHint("还没有会话记录");
                if(!RxLoginTool.isLogin()) {
                    requestVisitorShow();
                }else {
                    visitor_circle.setVisibility(View.GONE);
                }
            }else {
                visitor_circle.setVisibility(View.GONE);
                emptyBg.setVisibility(View.GONE);
            }

        } catch (Exception e) {

        }
    }



}
