package com.lianzai.reader.ui.activity.TeamChat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.TeamChatInfoBean;
import com.lianzai.reader.bean.TeamChatShowPersonBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.TeamChatPersonListItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
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

public class ActivityTeamChatPersonList extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;
    //列表
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.mask_view)
    View mask_view;//遮罩

    @Bind(R.id.black_tv)
    TextView black_tv;

    @Bind(R.id.search_ed)
    TextView search_ed;


    TeamChatPersonListItemAdapter teamChatPersonListItemAdapter;
    List<TeamChatInfoBean.DataBean.UserListBean> teamChatMembers = new ArrayList<>();
    RxLinearLayoutManager manager;
    //房间号
    String teamId;

    //头部视图
//    View headerView;
//    private EditText search_ed;

    //成员操作弹窗
    PersonOptionPopup personOptionPopup;

    //用作双击检测
    long mClickTime = 0;
    //我的角色
    private int mRole;
    private TeamChatInfoBean teamChatInfoBean;
    private TeamChatShowPersonBean teamChatShowPersonBean;

    public static void startActivity(Context context, String teamId) {
        Bundle bundle = new Bundle();
        bundle.putString("teamId", teamId);
        RxActivityTool.skipActivity(context, ActivityTeamChatPersonList.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_teamchat_persondetail;
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

        teamId = getIntent().getExtras().getString("teamId");

        personOptionPopup = new PersonOptionPopup(this);

//        headerView = getLayoutInflater().inflate(R.layout.view_header_persondetail, null);
//        search_ed = (EditText) headerView.findViewById(R.id.search_ed);
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

//        search_ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    requestSearch();
//                    return true;
//                }
//                return false;
//            }
//        });


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

        teamChatPersonListItemAdapter = new TeamChatPersonListItemAdapter(teamChatMembers, this);
//        chatRoomPersonListItemAdapter.addHeaderView(headerView, 0);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        //item点击监听
        teamChatPersonListItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                TeamChatInfoBean.DataBean.UserListBean temp = (TeamChatInfoBean.DataBean.UserListBean) baseQuickAdapter.getData().get(i);
                //自己是号主或作者的情况
                if (mRole == Constant.ChatRoomRole.SYSTEM_ACCOUNT ||mRole == Constant.ChatRoomRole.HAOZHU_ACCOUNT ||mRole == Constant.ChatRoomRole.AUTHOR_ACCOUNT){
                    //对方比我身份低
                    if (temp.getRoleType() > Constant.ChatRoomRole.AUTHOR_ACCOUNT) {
                        mask_view.setVisibility(View.VISIBLE);
                        personOptionPopup.showPopupWindow(view);

                        if (temp.getInBanned() == 1) {
                            //禁言状态
                            personOptionPopup.getTv_collect().setVisibility(View.GONE);
                            personOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                            personOptionPopup.getTv_delete().setText("取消禁言");
                            personOptionPopup.getTv_delete().setOnClickListener(
                                    v -> {
                                        requestUnMute(temp.getUid(), i);
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
                                        requestBlack(temp.getUid(), i);
                                        personOptionPopup.dismiss();
                                    }
                            );
                            personOptionPopup.getTv_delete().setText("禁言");
                            personOptionPopup.getTv_delete().setOnClickListener(
                                    v -> {
                                        requestMute(temp.getUid(), i);
                                        personOptionPopup.dismiss();
                                    }
                            );
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


                    }else{
                        return;
                    }

                }else if (mRole == Constant.ChatRoomRole.ADMIN_ACCOUNT ){
                    //自己是管理员的情况
                    //对方比我的身份低
                    if (temp.getRoleType() > Constant.ChatRoomRole.ADMIN_ACCOUNT) {
                        mask_view.setVisibility(View.VISIBLE);
                        personOptionPopup.showPopupWindow(view);
                        personOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                        //没有拉黑功能
                        personOptionPopup.getTv_collect().setVisibility(View.GONE);
                        personOptionPopup.getTv_delete().setText("禁言");
                        personOptionPopup.getTv_delete().setOnClickListener(
                                v -> {
                                    requestMute(temp.getUid(), i);
                                    personOptionPopup.dismiss();
                                }
                        );

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
                else {
                    return;
                }

            }
        });

        //纵向列表设置
        manager = new RxLinearLayoutManager(ActivityTeamChatPersonList.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(teamChatPersonListItemAdapter);

        //只刷一遍聊天室信息
        //查询聊天室信息
        requestChatinfo();
        requestChatMembersinfo();
    }


    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }


    private void showOrCloseRefresh(boolean isShow) {
        new Handler().postDelayed(new Runnable() {
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
        requestChatMembersinfo();
    }

    private void requestChatMembersinfo(){

        HashMap map=new HashMap();
        map.put("teamId",teamId);

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/teams/members",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    dismissDialog();
                    showOrCloseRefresh(false);
                }catch (Exception ee){

                }
            }
            @Override
            public void onResponse(String response) {
                RxLogTool.e("onResponse:"+response);
                try{
                    dismissDialog();
                    showOrCloseRefresh(false);
                    teamChatShowPersonBean= GsonUtil.getBean(response,TeamChatShowPersonBean.class);
                    if (teamChatShowPersonBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //聊天室成员
                        //成员列表布局
                        teamChatMembers.clear();
                        if(null != teamChatShowPersonBean.getData() && !teamChatShowPersonBean.getData().isEmpty()){
                            teamChatMembers.addAll(teamChatShowPersonBean.getData());
                        }
                        teamChatPersonListItemAdapter.notifyDataSetChanged();
                        if(teamChatMembers.size() > 0){
                        }else {
                            teamChatPersonListItemAdapter.setEmptyView(R.layout.view_disconnect_network,recyclerView);
                        }

                    }else{
                        RxToast.custom(teamChatShowPersonBean.getMsg()).show();
                    }

                }catch (Exception e){

                }

            }
        });
    }

    //拉黑操作
    private void requestBlack(int uid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("uid", String.valueOf(uid));
        hashMap.put("teamId", teamId);
        hashMap.put("type", "2");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/teamMember/limit/add", hashMap, new CallBackUtil.CallBackString() {
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
//                    TeamChatInfoBean.DataBean.UserListBean temp = chatRoomPersonListItemAdapter.getItem(position);
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
    private void requestMute(int uid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("uid", String.valueOf(uid));
        hashMap.put("teamId", teamId);
        hashMap.put("type", "1");
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/teamMember/limit/add", hashMap, new CallBackUtil.CallBackString() {
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
//                    TeamChatInfoBean.DataBean.UserListBean temp = chatRoomPersonListItemAdapter.getItem(position);
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
    private void requestUnBlack(int uid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("uid", String.valueOf(uid));
        hashMap.put("teamId", teamId);
        hashMap.put("type", "2");
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teamMember/limit/remove", hashMap, new CallBackUtil.CallBackString() {
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
//                    TeamChatInfoBean.DataBean.UserListBean temp = chatRoomPersonListItemAdapter.getItem(position);
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
    private void requestUnMute(int uid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("uid", String.valueOf(uid));
        hashMap.put("teamId", teamId);
        hashMap.put("type", "1");
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teamMember/limit/remove", hashMap, new CallBackUtil.CallBackString() {
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
//                    TeamChatInfoBean.DataBean.UserListBean temp = chatRoomPersonListItemAdapter.getItem(position);
//                    chatRoomPersonListItemAdapter.setData(position, temp);
                    onRefresh();
                } else {
                    onRefresh();
                    RxToast.custom(basebean.getMsg()).show();
                }
            }
        });
    }


    private void requestChatinfo(){
        HashMap map=new HashMap();
        map.put("teamId",teamId);

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/teams/info", map ,new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                RxLogTool.e("onResponse:"+response);

                try{
                    teamChatInfoBean = GsonUtil.getBean(response,TeamChatInfoBean.class);
                    if (teamChatInfoBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        mRole = teamChatInfoBean.getData().getRoleType();
                        if(mRole == Constant.ChatRoomRole.HAOZHU_ACCOUNT || mRole == Constant.ChatRoomRole.SYSTEM_ACCOUNT || mRole == Constant.ChatRoomRole.AUTHOR_ACCOUNT) {
                            //群主和系统账号，修改公告，可拉黑，不能退出群聊，可禁言
                            black_tv.setVisibility(View.VISIBLE);
                        }else if(mRole == Constant.ChatRoomRole.ADMIN_ACCOUNT ){
                            //管理员，可禁言
                            black_tv.setVisibility(View.GONE);
                        }
                    }else{
                        RxToast.custom(teamChatInfoBean.getMsg()).show();
                    }
                }catch (Exception e){

                }
            }
        });
    }



    @OnClick(R.id.img_back)
    void closeClick() {
        finish();
    }

    @OnClick(R.id.search_ed)
    void search_edClick() {
        if(mRole == 0) {
            return;
        }
        ActivityTeamChatSearchPersonList.startActivity(ActivityTeamChatPersonList.this, teamId, mRole);
    }

    @OnClick(R.id.mute_tv)
    void mute_tvClick() {
        ActivityTeamBanPerson.startActivity(ActivityTeamChatPersonList.this,teamId,"jin");
    }

    @OnClick(R.id.black_tv)
    void black_tvClick() {
        ActivityTeamBanPerson.startActivity(ActivityTeamChatPersonList.this,teamId,"hei");
    }

}
