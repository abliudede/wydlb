package com.lianzai.reader.ui.activity.chatRoomSetting;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ChatInfoResponse;
import com.lianzai.reader.bean.ChatRoomSearchBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.ChatRoomPersonListItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.view.PersonOptionPopup;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/13.
 * 成员管理的搜索结果页
 */

public class ActivityChatRoomPersonList extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;
    //列表
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.mask_view)
    View mask_view;//遮罩


    ChatRoomPersonListItemAdapter chatRoomPersonListItemAdapter;
    List<ChatRoomSearchBean.DataBean.ListBean> barBeanArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    //房间号
    String roomId;

    //头部视图
    View headerView;
    private EditText search_ed;

    //成员操作弹窗
    PersonOptionPopup personOptionPopup;

    //用作双击检测
    long mClickTime = 0;
    //我的accid
    private String mineImAccount;
    //我的角色
    private int mRole;
    private ChatRoomSearchBean chatRoomSearchBean;
    private ChatInfoResponse chatInfoResponse;

    public static void startActivity(Context context, String roomId) {
        Bundle bundle = new Bundle();
        bundle.putString("roomId", roomId);
        RxActivityTool.skipActivity(context, ActivityChatRoomPersonList.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chatroom_persondetail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {

    }


    @Override
    public void configViews(Bundle savedInstanceState) {

        tv_commom_title.setText("聊天室管理");

        roomId = getIntent().getExtras().getString("roomId");

        personOptionPopup = new PersonOptionPopup(this);

        headerView = getLayoutInflater().inflate(R.layout.view_header_persondetail, null);
        search_ed = (EditText) headerView.findViewById(R.id.search_ed);
//        search_ed.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (null == s) {
//                    requestSearch();
//                } else if (TextUtils.isEmpty(s.toString())) {
//                    requestSearch();
//                } else {
//                    requestSearch();
//                }
//            }
//        });

        search_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestSearch();
                    return true;
                }
                return false;
            }
        });


        //标题双击回到顶部。
        tv_commom_title.setOnClickListener(
                v -> {//双击
                    if (System.currentTimeMillis() - mClickTime < 800) {
                        //此处做双击具体业务逻辑
                        recyclerView.scrollToPosition(0);
                    } else {
                        mClickTime = System.currentTimeMillis();
                        //表示单击，此处也可以做单击的操作
                    }
                }
        );

        chatRoomPersonListItemAdapter = new ChatRoomPersonListItemAdapter(barBeanArrayList, this);
        chatRoomPersonListItemAdapter.addHeaderView(headerView, 0);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        mineImAccount = RxLoginTool.getLoginAccountToken().getData().getImAccount();

        //item点击监听
        chatRoomPersonListItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                ChatRoomSearchBean.DataBean.ListBean temp = (ChatRoomSearchBean.DataBean.ListBean) baseQuickAdapter.getData().get(i);
                //点击的自己
                if (temp.getAccid().equals(mineImAccount)) {
                    return;
                }
                //自己是号主或作者或者管理员的情况
                if (mRole == Constant.ChatRoomRole.SYSTEM_ACCOUNT ||mRole == Constant.ChatRoomRole.HAOZHU_ACCOUNT || mRole == Constant.ChatRoomRole.AUTHOR_ACCOUNT || mRole == Constant.ChatRoomRole.ADMIN_ACCOUNT) {
                    //对方比管理员身份低
                    if (temp.getRoleType() > Constant.ChatRoomRole.ADMIN_ACCOUNT) {
                        mask_view.setVisibility(View.VISIBLE);
                        personOptionPopup.showPopupWindow(view);


                        if (temp.getRoleType() == Constant.ChatRoomRole.BLACK_ACCOUNT) {
                            //拉黑状态
                            personOptionPopup.getTv_collect().setVisibility(View.VISIBLE);
                            personOptionPopup.getTv_delete().setVisibility(View.GONE);
                            personOptionPopup.getTv_collect().setText("取消拉黑");
                            personOptionPopup.getTv_collect().setOnClickListener(
                                    v -> {
                                        requestUnBlack(temp.getAccid(), i);
                                        personOptionPopup.dismiss();
                                    }
                            );
                        } else if (temp.getRoleType() == Constant.ChatRoomRole.MUTED_ACCOUNT) {
                            //禁言状态
                            personOptionPopup.getTv_collect().setVisibility(View.GONE);
                            personOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                            personOptionPopup.getTv_delete().setText("取消禁言");
                            personOptionPopup.getTv_delete().setOnClickListener(
                                    v -> {
                                        requestUnMute(temp.getAccid(), i);
                                        personOptionPopup.dismiss();
                                    }
                            );
                        } else {
                            //正常状态
                            personOptionPopup.getTv_collect().setVisibility(View.VISIBLE);
                            personOptionPopup.getTv_delete().setVisibility(View.VISIBLE);

                            personOptionPopup.getTv_collect().setText("永久拉黑");
                            personOptionPopup.getTv_collect().setOnClickListener(
                                    v -> {
                                        requestBlack(temp.getAccid(), i);
                                        personOptionPopup.dismiss();
                                    }
                            );
                            personOptionPopup.getTv_delete().setText("禁言");
                            personOptionPopup.getTv_delete().setOnClickListener(
                                    v -> {
                                        requestMute(temp.getAccid(), i);
                                        personOptionPopup.dismiss();
                                    }
                            );


                        }

                    }

                    //对方是管理员身份且比我的身份低
                    else if (temp.getRoleType() == Constant.ChatRoomRole.ADMIN_ACCOUNT && temp.getRoleType() > mRole) {
                        mask_view.setVisibility(View.VISIBLE);
                        personOptionPopup.showPopupWindow(view);
                        personOptionPopup.getTv_collect().setVisibility(View.VISIBLE);
                        //没有禁言功能
                        personOptionPopup.getTv_delete().setVisibility(View.GONE);
                        personOptionPopup.getTv_collect().setText("永久拉黑");
                        personOptionPopup.getTv_collect().setOnClickListener(
                                v -> {
                                    requestBlack(temp.getAccid(), i);
                                    personOptionPopup.dismiss();
                                }
                        );
                    } else {
                        return;
                    }


                    //取消
                    personOptionPopup.getTv_cancel().setOnClickListener(
                            v -> {
                                personOptionPopup.dismiss();
                            }
                    );
                    personOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mask_view.setVisibility(View.GONE);
                        }
                    });


                } else {
                    return;
                }

            }
        });

        //纵向列表设置
        manager = new RxLinearLayoutManager(ActivityChatRoomPersonList.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(chatRoomPersonListItemAdapter);

        //只刷一遍聊天室信息
        //查询聊天室信息
        requestChatinfo();

        //弹出键盘
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    RxKeyboardTool.showSoftInput(BuglyApplicationLike.getContext(), search_ed);
                }catch (Exception e){

                }

            }
        }, 100);

    }


    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }


    private void showOrCloseRefresh(boolean isShow) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(isShow);
                } catch (Exception e) {
                    RxLogTool.e("showOrCloseRefresh:" + e.getMessage());
                }
            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }

    @Override
    public void onRefresh() {
        //查询聊天室消息
        requestChatinfo();
        requestSearch();

    }

    //拉黑操作
    private void requestBlack(String accid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("byOperatorAccid", accid);
        hashMap.put("roomId", roomId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/blacklist/" + roomId, hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }

            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                RxLogTool.e("onResponse:" + response);
                BaseBean basebean = GsonUtil.getBean(response, BaseBean.class);
                if (basebean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                    //本地数据操作？todo
//                    ChatRoomSearchBean.DataBean.ListBean temp = chatRoomPersonListItemAdapter.getItem(position);
//                    chatRoomPersonListItemAdapter.setData(position, temp);
                    onRefresh();
                } else {
                    onRefresh();
                    RxToast.custom(basebean.getMsg()).show();
                }
            }
        });
    }

    //禁言操作
    private void requestMute(String accid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("byOperatorAccid", accid);
        hashMap.put("roomId", roomId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/mute/" + roomId, hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }

            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                RxLogTool.e("onResponse:" + response);
                BaseBean basebean = GsonUtil.getBean(response, BaseBean.class);
                if (basebean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                    //todo本地数据操作
//                    ChatRoomSearchBean.DataBean.ListBean temp = chatRoomPersonListItemAdapter.getItem(position);
//                    chatRoomPersonListItemAdapter.setData(position, temp);
                    onRefresh();
                } else {
                    onRefresh();
                    RxToast.custom(basebean.getMsg()).show();
                }
            }
        });
    }

    //取消拉黑操作
    private void requestUnBlack(String accid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("byOperatorAccid", accid);
        hashMap.put("roomId", roomId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/unblacklist/" + roomId, hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }

            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                RxLogTool.e("onResponse:" + response);
                BaseBean basebean = GsonUtil.getBean(response, BaseBean.class);
                if (basebean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                    //TODO本地数据操作
//                    ChatRoomSearchBean.DataBean.ListBean temp = chatRoomPersonListItemAdapter.getItem(position);
//                    chatRoomPersonListItemAdapter.setData(position, temp);
                    onRefresh();
                } else {
                    onRefresh();
                    RxToast.custom(basebean.getMsg()).show();
                }
            }
        });
    }

    //取消禁言操作
    private void requestUnMute(String accid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("byOperatorAccid", accid);
        hashMap.put("roomId", roomId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/unMute/" + roomId, hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }

            }

            @Override
            public void onResponse(String response) {
                dismissDialog();
                RxLogTool.e("onResponse:" + response);
                BaseBean basebean = GsonUtil.getBean(response, BaseBean.class);
                if (basebean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                    //本地数据操作 TODO
//                    ChatRoomSearchBean.DataBean.ListBean temp = chatRoomPersonListItemAdapter.getItem(position);
//                    chatRoomPersonListItemAdapter.setData(position, temp);
                    onRefresh();
                } else {
                    onRefresh();
                    RxToast.custom(basebean.getMsg()).show();
                }
            }
        });
    }


    private void requestChatinfo() {
        showDialog();
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/" + roomId, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {

                }
            }

            @Override
            public void onResponse(String response) {
                RxLogTool.e("onResponse:" + response);
                try {
                    dismissDialog();
                    showOrCloseRefresh(false);
                    chatInfoResponse = GsonUtil.getBean(response, ChatInfoResponse.class);
                    if (chatInfoResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //聊天室成员
                        mRole = chatInfoResponse.getData().getRoleType();
                    } else {
                        RxToast.custom(chatInfoResponse.getMsg()).show();
                    }
                } catch (Exception e) {

                }

            }
        });
    }

    private void requestSearch() {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("nickName", search_ed.getText().toString().trim());
        hashMap.put("pageNum", "1");
        hashMap.put("pageSize", "20");
//        hashMap.put("pageSize", "10");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/member/search/" + roomId, hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    dismissDialog();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {

                }

            }

            @Override
            public void onResponse(String response) {

                try {
                    RxLogTool.e("onResponse:" + response);
                    dismissDialog();
                    showOrCloseRefresh(false);
                    chatRoomSearchBean = GsonUtil.getBean(response, ChatRoomSearchBean.class);
                    if (chatRoomSearchBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        barBeanArrayList = chatRoomSearchBean.getData().getList();
                        chatRoomPersonListItemAdapter.replaceData(barBeanArrayList);
                        if (null == barBeanArrayList) {
//                            chatRoomPersonListItemAdapter.setEmptyView(R.layout.view_alluse_empty, recyclerView);
                        } else if (barBeanArrayList.isEmpty()) {
//                            chatRoomPersonListItemAdapter.setEmptyView(R.layout.view_alluse_empty, recyclerView);
                        }

                    } else {
                        RxToast.custom(chatRoomSearchBean.getMsg()).show();
                    }
                } catch (Exception e) {

                }


            }
        });
    }


    @OnClick(R.id.img_back)
    void closeClick() {
        finish();
    }

}
