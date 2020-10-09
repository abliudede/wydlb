package com.lianzai.reader.ui.activity.chatRoomSetting;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ChatInfoResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.ChatRoomPersonGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/09/26.
 * 更多成员的显示列表
 */

public class ActivityChatRoomShowPerson extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;
    //列表
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    ChatRoomPersonGridAdapter chatRoomPersonGridAdapter;
    List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
    //房间号
    String roomId;
    //用作双击检测
    long mClickTime=0;
    //聊天室信息
    private ChatInfoResponse chatInfoResponse;
    private int gudingPerson = 100;

    long lastTime;

    public static void startActivity(Context context, String roomId){
        Bundle bundle=new Bundle();
        bundle.putString("roomId",roomId);
        RxActivityTool.skipActivity(context,ActivityChatRoomShowPerson.class,bundle);
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

        tv_commom_title.setText("聊天室成员");

        roomId=getIntent().getExtras().getString("roomId");

        //标题双击回到顶部。
        tv_commom_title.setOnClickListener(
                v->{//双击
                    if(System.currentTimeMillis()-mClickTime<800){
                        //此处做双击具体业务逻辑
                        recyclerView.scrollToPosition(0);
                    }
                    else{
                        mClickTime= System.currentTimeMillis();
                        //表示单击，此处也可以做单击的操作
                    }
                }
        );

        chatRoomPersonGridAdapter = new ChatRoomPersonGridAdapter(chatRoomMembers, ActivityChatRoomShowPerson.this);
        chatRoomPersonGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //用云信账号获取用户id，跳个人主页
                try {
                    String userInfo = UserInfoHelper.getExtension(chatRoomMembers.get(position).getAccount());
                    if(null != userInfo) {
                        JSONObject jsonObject = JSONObject.parseObject(userInfo);
                        int userId = jsonObject.getInteger("objectId");
                        PerSonHomePageActivity.startActivity(ActivityChatRoomShowPerson.this, String.valueOf(userId));
                    }
                }catch (Exception e){

                }


            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));
        //表格列表
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(chatRoomPersonGridAdapter);


        if (RxNetTool.isAvailable()) {
            showDialog();
                        showOrCloseRefresh(true);
                        onRefresh();
        } else {
            chatRoomPersonGridAdapter.setEmptyView(R.layout.view_disconnect_network,recyclerView);
        }

    }


    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }



    private  void showOrCloseRefresh(boolean isShow){
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if(null!=mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(isShow);
                }catch (Exception e){
                    RxLogTool.e("showOrCloseRefresh:"+e.getMessage());
                }

            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }

    @Override
    public void onRefresh() {
        //查询聊天室消息
        requestChatinfo();

    }


    private void requestChatinfo(){

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/" + roomId, new CallBackUtil.CallBackString() {
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
                    chatInfoResponse= GsonUtil.getBean(response,ChatInfoResponse.class);
                    if (chatInfoResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //聊天室成员一分钟只能请求一次。
                        if(System.currentTimeMillis() - lastTime > 60000) {
                            lastTime = System.currentTimeMillis();
                            fetchRoomMembers1();
                        }
                    }else{
                        RxToast.custom(chatInfoResponse.getMsg()).show();
                    }

                }catch (Exception e){

                }

            }
        });
    }


    /**
     * 获取聊天室人员
     */
    private void fetchRoomMembers1(){
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.NORMAL, 0, gudingPerson).setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
            @Override
            public void onResult(int code, List<ChatRoomMember> result, Throwable exception) {
                try{
                    RxLogTool.e("fetchRoomMembers code:"+ code);
                    RxLogTool.e("fetchRoomMembers size:"+result.size());
                    if(null != result && null != chatInfoResponse){
                        for(int k = 0 ; k < result.size() && k <= gudingPerson ; k++){
                            result.get(k).setMemberLevel(5);
                            for(int i = 0 ; i < chatInfoResponse.getData().getIdentityInfoResponses().size() ; i++){
                                if(result.get(k).getAccount().equals(chatInfoResponse.getData().getIdentityInfoResponses().get(i).getAccid())){
                                    result.get(k).setMemberLevel(chatInfoResponse.getData().getIdentityInfoResponses().get(i).getRoleType());
                                }
                            }
                            if(result.get(k).getMemberLevel() == Constant.ChatRoomRole.SYSTEM_ACCOUNT){
                                result.remove(k);
                                k--;
                                continue;
                            }
                            //去掉受限用户
                            if(result.get(k).isInBlackList()){
                                result.remove(k);
                                k--;
                                continue;
                            }
                            //去重
                            if(result.get(k).getAccount().equals(chatInfoResponse.getData().getMyAccid())){
                                result.remove(k);
                                k--;
                                continue;
                            }
                        }
                        Collections.sort(result, new Comparator<ChatRoomMember>() {

                            @Override
                            public int compare(ChatRoomMember o1, ChatRoomMember o2) {
                                if(o1.getMemberLevel() <  o2.getMemberLevel()){
                                    return -1;
                                }else if(o1.getMemberLevel() >  o2.getMemberLevel()){
                                    return 1;
                                }else {
                                    return 0;
                                }
                            }
                        });
                        //假如页面没有退出则继续请求在线游客
                        if(null != chatRoomMembers && null != chatRoomPersonGridAdapter) {
                            chatRoomMembers.clear();
                            chatRoomMembers.addAll(result);
                            fetchRoomMembers2(50);
                        }
                    }
                }catch (Exception e){

                }
            }
        });
    }

    //显示在线游客
    private void fetchRoomMembers2(int size){
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.GUEST, 0, size).setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
            @Override
            public void onResult(int code, List<ChatRoomMember> result, Throwable exception) {
                try{

                    if(null != result) {
                        RxLogTool.e("fetchRoomMembers size:" + result.size());
                        for (int k = 0; k < result.size(); k++) {
                            result.get(k).setMemberLevel(Constant.ChatRoomRole.GUEST_ACCOUNT);
                            //去掉受限用户
                            if(result.get(k).isInBlackList()){
                                result.remove(k);
                                k--;
                                continue;
                            }
                            //去重
                            if (result.get(k).getAccount().equals(chatInfoResponse.getData().getMyAccid())) {
                                result.remove(k);
                                k--;
                                continue;
                            }
                        }
                        //直接全部加进去
                        if (null != chatRoomMembers && null != chatRoomPersonGridAdapter) {
                            chatRoomMembers.addAll(result);
                        }
                    }
                    //加入自己
                    ChatRoomMember myself = new ChatRoomMember();
                    myself.setAccount(chatInfoResponse.getData().getMyAccid());
                    myself.setAvatar(chatInfoResponse.getData().getMyHead());
                    myself.setNick(chatInfoResponse.getData().getMyNickName());
                    myself.setMemberLevel(chatInfoResponse.getData().getRoleType());
                    if(myself.getMemberLevel() != Constant.ChatRoomRole.SYSTEM_ACCOUNT) {
                        if (chatRoomMembers.size() > 0) {
                            chatRoomMembers.add(0, myself);
                        } else {
                            chatRoomMembers.add(myself);
                        }
                    }
                    chatRoomPersonGridAdapter.replaceData(chatRoomMembers);


                }catch (Exception e){

                }






            }
        });
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

}
