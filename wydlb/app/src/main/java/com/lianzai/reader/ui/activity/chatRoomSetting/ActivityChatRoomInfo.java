package com.lianzai.reader.ui.activity.chatRoomSetting;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ChatInfoResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.chat.PublicNumberDetailActivity;
import com.lianzai.reader.ui.adapter.ChatRoomPersonGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.view.RxToast;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 聊天室信息頁面
 */
public class ActivityChatRoomInfo extends BaseActivity {

    //标题栏
    @Bind(R.id.tv_common_title)
    TextView tv_common_title;

//    @Bind(R.id.tv_chat_room_title)
//    TextView tv_chat_room_title;

    //成员展示
    @Bind(R.id.rv_grid_person)
    RecyclerView rv_grid_person;

    //展示更多点击
    @Bind(R.id.tv_showmore)
    TextView tv_showmore;

    //公告点击
    @Bind(R.id.ly_notice)
    LinearLayout ly_notice;
    //公告展示
    @Bind(R.id.tv_notice)
    TextView tv_notice;

    //排名和投票点击
    @Bind(R.id.tv_ranking_and_voting)
    TextView tv_ranking_and_voting;

    //里程碑点击
    @Bind(R.id.tv_milepost)
    TextView tv_milepost;

    //聊天室管理点击

    @Bind(R.id.tv_chatroom_management_view)
    View tv_chatroom_management_view;
    @Bind(R.id.tv_chatroom_management)
    TextView tv_chatroom_management;

    //昵称点击
    @Bind(R.id.ly_nickname)
    RelativeLayout ly_nickname;
    //昵称文字
    @Bind(R.id.tv_nickname)
    TextView tv_nickname;

    //举报点击
    @Bind(R.id.tv_report)
    TextView tv_report;

    ChatRoomPersonGridAdapter chatRoomPersonGridAdapter;
    List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
    private List<ChatRoomMember> chatRoomMembersAll = new ArrayList<>();

    String chatRoomId;

    ChatRoomInfo roomInfo;

//    String onlineCount="0";//在线人数

    int maxMemberCount = 100;
    private ChatInfoResponse chatInfoResponse;
    private int mRole = 0;

    String chatRoomName;

    long lastTime;

    public static void startActivity(Context context,String chatRoomId,String chatRoomName){
        Bundle bundle=new Bundle();
        bundle.putString("chatRoomId",chatRoomId);
        bundle.putString("chatRoomName",chatRoomName);
        RxActivityTool.skipActivity(context,ActivityChatRoomInfo.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chatroom_info;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //查询聊天室消息
        requestChatinfo();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        chatRoomId=getIntent().getExtras().getString("chatRoomId");
//        onlineCount=getIntent().getExtras().getString("onlineCount");
        chatRoomName=getIntent().getExtras().getString("chatRoomName");


        tv_common_title.setText("《"+chatRoomName+"》");
//        tv_chat_room_title.setText("聊天室("+onlineCount+")人在线");

        chatRoomPersonGridAdapter = new ChatRoomPersonGridAdapter(chatRoomMembers, ActivityChatRoomInfo.this);
        chatRoomPersonGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //用云信账号获取用户id，跳个人主页
                try {
                    String userInfo = UserInfoHelper.getExtension(chatRoomPersonGridAdapter.getData().get(position).getAccount());
                    if(null != userInfo) {
                        JSONObject jsonObject = JSONObject.parseObject(userInfo);
                        int userId = jsonObject.getInteger("objectId");
                        PerSonHomePageActivity.startActivity(ActivityChatRoomInfo.this, String.valueOf(userId));
                    }
                }catch (Exception e){

                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityChatRoomInfo.this, 5);
        rv_grid_person.setLayoutManager(gridLayoutManager);
        rv_grid_person.setAdapter(chatRoomPersonGridAdapter);

        //聊天室信息
//        fetchChatRoomInfo();

        tv_showmore.setVisibility(View.GONE);
    }

    public List<ChatRoomMember> getChatRoomMembersAll() {
        return chatRoomMembersAll;
    }

    public void setChatRoomMembersAll(List<ChatRoomMember> chatRoomMembersAll) {
        this.chatRoomMembersAll = chatRoomMembersAll;
    }

    //    private void fetchChatRoomInfo(){
//        NIMClient.getService(ChatRoomService.class).fetchRoomInfo(chatRoomId).setCallback(new RequestCallback<ChatRoomInfo>() {
//            @Override
//            public void onSuccess(ChatRoomInfo chatRoomInfo) {
//                roomInfo=chatRoomInfo;
//                try{
//                    if (null==tv_notice)return;
//                    tv_notice.setText(roomInfo.getAnnouncement());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                RxLogTool.e("roomInfo onlineCount:" + onlineCount);
//            }
//            @Override
//            public void onFailed(int i) {
//            }
//            @Override
//            public void onException(Throwable throwable) {
//            }
//        });
//    }


    /**
     * 获取聊天室人员
     */
    private void fetchRoomMembers1(){
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(chatRoomId, MemberQueryType.NORMAL, 0, maxMemberCount).setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
            @Override
            public void onResult(int code, List<ChatRoomMember> result, Throwable exception) {
                //回调的视图操作加try，catch
              try{
                  if(null != result && null != chatInfoResponse){
                      RxLogTool.e("fetchRoomMembers size:"+result.size());
                      for(int k = 0 ; k < result.size() && k <= maxMemberCount ; k++){
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
                          if( result.get(k).isInBlackList()){
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
                      if(null != chatRoomMembersAll && null != chatRoomPersonGridAdapter) {
                          chatRoomMembersAll.clear();
                          chatRoomMembersAll.addAll(result);
                          fetchRoomMembers2(maxMemberCount);
                      }
                  }


              }catch (Exception e){
                  RxLogTool.e("personlist crash:" + e.toString());

              }


            }
        });
    }

    //显示在线游客
    private void fetchRoomMembers2(int size){
        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(chatRoomId, MemberQueryType.GUEST, 0, size).setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
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
                        if (null != chatRoomMembersAll && null != chatRoomPersonGridAdapter) {
                            chatRoomMembersAll.addAll(result);
                        }
                    }
                    //加入自己
                    ChatRoomMember myself = new ChatRoomMember();
                    myself.setAccount(chatInfoResponse.getData().getMyAccid());
                    myself.setAvatar(chatInfoResponse.getData().getMyHead());
                    myself.setNick(chatInfoResponse.getData().getMyNickName());
                    myself.setMemberLevel(chatInfoResponse.getData().getRoleType());
                    if(myself.getMemberLevel() != Constant.ChatRoomRole.SYSTEM_ACCOUNT) {
                        if (chatRoomMembersAll.size() > 0) {
                            chatRoomMembersAll.add(0, myself);
                        } else {
                            chatRoomMembersAll.add(myself);
                        }
                    }
                    //截取出20长度的列表来。
                    if(null == chatRoomMembersAll || chatRoomMembersAll.isEmpty()){
                        tv_showmore.setVisibility(View.GONE);
                        chatRoomMembers = chatRoomMembersAll;
                    }else if(chatRoomMembersAll.size() > 20){
                        tv_showmore.setVisibility(View.VISIBLE);
                        chatRoomMembers = chatRoomMembersAll.subList(0, 20);
                    }else{
                        tv_showmore.setVisibility(View.GONE);
                        chatRoomMembers = chatRoomMembersAll;
                    }
                    chatRoomPersonGridAdapter.replaceData(chatRoomMembers);

                }catch (Exception ee){

                }
            }
        });
    }


    @Override
    public void gc() {
    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    /**
     * 查看更多群成员点击事件
     */
    @OnClick(R.id.tv_showmore)void showMoreClick(){
        ActivityChatRoomShowPerson.startActivity(this,chatRoomId);
    }

    /**
     * 公告点击事件
     */
    @OnClick(R.id.ly_notice)void noticeClick(){
        if(null == chatInfoResponse){
            RxToast.custom("未获取到聊天室信息").show();
            return;
        }
        if(null == chatInfoResponse.getData()){
            RxToast.custom("未获取到聊天室信息").show();
            return;
        }
        if(null == chatInfoResponse.getData().getNoticeInfo()){
            if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ) {
                RxToast.custom("没有公告信息").show();
                return;
            }else {
                ChatRoomEditNotice.startActivity(this,chatRoomId,"","",0L,"",mRole);
              return;
            }
        }

        if(TextUtils.isEmpty(chatInfoResponse.getData().getNoticeInfo().getNotice())){
            if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ) {
                RxToast.custom("没有公告信息").show();
                return;
            }else {
                ChatInfoResponse.DataBean.NoticeInfoBean noticeData = chatInfoResponse.getData().getNoticeInfo();
                ChatRoomEditNotice.startActivity(this,chatRoomId,noticeData.getUsername(),noticeData.getHead(),noticeData.getCreateTime(),noticeData.getNotice(),mRole);
                return;
            }
        }

        ChatInfoResponse.DataBean.NoticeInfoBean noticeData = chatInfoResponse.getData().getNoticeInfo();
        ChatRoomEditNotice.startActivity(this,chatRoomId,noticeData.getUsername(),noticeData.getHead(),noticeData.getCreateTime(),noticeData.getNotice(),mRole);

    }

    /**
     * 里程碑点击事件
     */
    @OnClick(R.id.tv_milepost)void milepostClick(){
        ActivityMilepost.startActivity(this,chatRoomId);
    }

    /**
     * 聊天室管理点击事件
     */
    @OnClick(R.id.tv_chatroom_management)void managementClick(){
        if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ){
            RxToast.custom("没有权限").show();
            return;
        }
        ActivityBanPerson.startActivity(this,chatRoomId);
    }

    /**
     * 我在本聊天室的昵称点击事件
     */
    @OnClick(R.id.ly_nickname)void nicknameClick(){

//        if(mRole > Constant.ChatRoomRole.NORMAL_ACCOUNT  || mRole < Constant.ChatRoomRole.HAOZHU_ACCOUNT){
//            RxToast.custom("没有权限").show();
//            return;
//        }
        Bundle bundle=new Bundle();
        bundle.putString("roomId",chatRoomId);
        bundle.putString("nickName",tv_nickname.getText().toString());
        RxActivityTool.skipActivity(this,ChatRoomEditNickName.class,bundle);
    }

    /**
     * 举报点击事件
     */
    @OnClick(R.id.tv_report)void reportClick(){
        //旧链接
        String url = Constant.H5_BASE_URL+"/author/#/report?source=3&targetId="+chatRoomId + "&createdBy=" + RxLoginTool.getLoginAccountToken().getData().getUid();

        //新链接
        //        String url = Constant.H5_BASE_URL+"/inform/#/report?source=3&targetId=" + chatRoomId + "&createdBy=" + RxLoginTool.getLoginAccountToken().getData().getUid();
        RxLogTool.e("URl" , url);
        ActivityWebView.startActivity(ActivityChatRoomInfo.this,url,1);
    }

    private void requestChatinfo(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/" + chatRoomId, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                RxLogTool.e("onResponse:"+response);

                try{
                    chatInfoResponse= GsonUtil.getBean(response,ChatInfoResponse.class);
                    if (chatInfoResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if (!TextUtils.isEmpty(chatInfoResponse.getData().getMyNickName())){
                            tv_nickname.setText(chatInfoResponse.getData().getMyNickName());
                        }
                        if (!TextUtils.isEmpty(chatInfoResponse.getData().getNoticeInfo().getNotice())){
                            tv_notice.setText(chatInfoResponse.getData().getNoticeInfo().getNotice());
                        }else {
                            tv_notice.setText("");
                        }
                        mRole = chatInfoResponse.getData().getRoleType();
                          if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ){
                              tv_chatroom_management.setVisibility(View.GONE);
                              tv_chatroom_management_view.setVisibility(View.GONE);
                              }else{
                              tv_chatroom_management.setVisibility(View.VISIBLE);
                              tv_chatroom_management_view.setVisibility(View.VISIBLE);
                          }
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





}
