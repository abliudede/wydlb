package com.lianzai.reader.ui.activity.TeamChat;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.TeamChatInfoBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.TeamChatGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMessageNotifyTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 聊天室信息頁面
 */
public class ActivityTeamChatInfo extends BaseActivity {

    //标题栏
    @Bind(R.id.tv_common_title)
    TextView tv_common_title;

    //成员展示
    @Bind(R.id.rv_grid_person)
    RecyclerView rv_grid_person;

    //展示更多点击
    @Bind(R.id.tv_showmore)
    TextView tv_showmore;

    //群聊名称
    @Bind(R.id.tv_teamchat_name)
    TextView tv_teamchat_name;

    //公告点击
    @Bind(R.id.ly_notice)
    LinearLayout ly_notice;
    //公告展示
    @Bind(R.id.tv_notice)
    TextView tv_notice;

    //相关连载号
    @Bind(R.id.rl_teamchat_lianzaihao)
    RelativeLayout rl_teamchat_lianzaihao;
    //相关连载号名称
    @Bind(R.id.tv_teamchat_lianzaihao)
    TextView tv_teamchat_lianzaihao;

    //群管理点击
    @Bind(R.id.tv_teamchat_management)
    TextView tv_teamchat_management;

    //昵称点击
    @Bind(R.id.ly_nickname)
    RelativeLayout ly_nickname;
    //昵称文字
    @Bind(R.id.tv_nickname)
    TextView tv_nickname;

    //查找聊天内容
    @Bind(R.id.tv_search)
    TextView tv_search;

    //消息免打扰和聊天室置顶
    @Bind(R.id.sb_no_message)
    CheckBox sb_no_message;
    @Bind(R.id.sb_top)
    CheckBox sb_top;

    //举报点击
    @Bind(R.id.tv_report)
    TextView tv_report;

    //删除并退出
    @Bind(R.id.tv_delete_recent)
    TextView tv_delete_recent;


    TeamChatGridAdapter teamChatGridAdapter;
    List<TeamChatInfoBean.DataBean.UserListBean> teamChatMembers = new ArrayList<>();

    String teamId;
    String teamName;
    private TeamChatInfoBean teamChatInfoBean;
    private int mRole = 0;
    private Team team;

    RxDialogSureCancelNew rxDialogSureCancel;

    public static void startActivity(Context context,String teamId,String teamName){
        Bundle bundle=new Bundle();
        bundle.putString("teamId",teamId);
        bundle.putString("teamName",teamName);
        RxActivityTool.skipActivity(context,ActivityTeamChatInfo.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_teamchat_info;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //查询聊天室消息
        requestChatinfo();
        requestTeamInfo();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        teamId=getIntent().getExtras().getString("teamId");
        teamName=getIntent().getExtras().getString("teamName");

        //群聊名称
        tv_common_title.setText(teamName);
        tv_teamchat_name.setText(teamName);

        teamChatGridAdapter = new TeamChatGridAdapter(teamChatMembers, ActivityTeamChatInfo.this);
        teamChatGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TeamChatInfoBean.DataBean.UserListBean itemData = teamChatGridAdapter.getData().get(position);
                PerSonHomePageActivity.startActivity(ActivityTeamChatInfo.this, String.valueOf(itemData.getUid()));

            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(ActivityTeamChatInfo.this, 5);
        rv_grid_person.setLayoutManager(gridLayoutManager);
        rv_grid_person.setAdapter(teamChatGridAdapter);

        //初始状态不显示查看更多
        tv_showmore.setVisibility(View.GONE);
        //初始状态
        if (RecentContactsFragment.isTop){
            sb_top.setChecked(true);
        }else{
            sb_top.setChecked(false);
        }

        rxDialogSureCancel=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        //红色按钮
        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
    }

    /**
     * 请求群基本信息
     */
    private void requestTeamInfo() {
        // 请求群基本信息
        Team t = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (t != null) {
           team = t;
           updateMessage();
        } else {
            NimUIKit.getTeamProvider().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result, int code) {
                    if (success && result != null) {
                        team = result;
                        updateMessage();
                    } else {
                        onRequestTeamInfoFailed();
                    }
                }
            });
        }
    }

    private void updateMessage() {
        TeamMessageNotifyTypeEnum type = team.getMessageNotifyType();
        if(type.equals(TeamMessageNotifyTypeEnum.All)){
            sb_no_message.setChecked(false);
        }else if(type.equals(TeamMessageNotifyTypeEnum.Mute)){
            sb_no_message.setChecked(true);
        }
    }

    private void onRequestTeamInfoFailed() {
        RxToast.custom( "获取群组信息失败!").show();
        finish();
    }


    @Override
    public void gc() {
    }
    @Override
    public void initToolBar() {
    }
    @OnCheckedChanged(R.id.sb_no_message)void sb_no_messageChanged(boolean isChecked){
        if (!NetworkUtil.isNetAvailable(ActivityTeamChatInfo.this)) {
           RxToast.custom(getString(R.string.network_is_not_available)).show();
            sb_no_message.setChecked(!isChecked);
            return;
        }

        TeamMessageNotifyTypeEnum typeEnum = isChecked ? TeamMessageNotifyTypeEnum.Mute : TeamMessageNotifyTypeEnum.All;
        NIMClient.getService(TeamService.class).muteTeam(team.getId(), typeEnum).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                //会话列表刷新
                RxEventBusTool.sendEvents(Constant.EventTag.MESSAGE_REFRESH);
            }

            @Override
            public void onFailed(int code) {
                try{
                    if (code == 408) {
                        RxToast.custom(getString(R.string.network_is_not_available)).show();
                    } else {
                        RxToast.custom("设置失败").show();
                    }
                    sb_no_message.setChecked(!isChecked);
                }catch (Exception e){

                }

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    @OnCheckedChanged(R.id.sb_top)void sb_topChanged(boolean isChecked){
        if (isChecked){
            RecentContactsFragment.isTop=true;
            RxEventBusTool.sendEvents(String.valueOf(teamId),Constant.EventTag.CONTACT_TOP_TAG);
        }else{
            RecentContactsFragment.isTop=false;
            RxEventBusTool.sendEvents(String.valueOf(teamId),Constant.EventTag.CONTACT_CALCLE_TOP_TAG);
        }
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @OnClick(R.id.tv_delete_recent)void tv_delete_recentClick()
    {
        //取消关注
        rxDialogSureCancel.getSureView().setText("确定");
        rxDialogSureCancel.getCancelView().setText("取消");
        rxDialogSureCancel.setTitle("提示");
        rxDialogSureCancel.setContent("退出该群聊后，您将接收不到该群聊的任何消息。您可以点击该群聊的入群邀请重新入群。");
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestUnConcernPlatform();
                rxDialogSureCancel.dismiss();
            }
        });
        rxDialogSureCancel.show();
    }

    /**
     * 查看更多群成员点击事件
     */
    @OnClick(R.id.tv_showmore)void showMoreClick(){
        ActivityTeamChatShowPerson.startActivity(this,teamId);
    }

    /**
     * 点击跳转到相关连载号
     */
    @OnClick(R.id.rl_teamchat_lianzaihao)void lianzaihaoClick(){
        if(null == teamChatInfoBean){
            RxToast.custom("未获取到聊天室信息").show();
            return;
        }
        if(null == teamChatInfoBean.getData()){
            RxToast.custom("未获取到聊天室信息").show();
            return;
        }
        ActivityCircleDetail.startActivity(ActivityTeamChatInfo.this, String.valueOf(teamChatInfoBean.getData().getPlatformId()));
    }



    /**
     * 公告点击事件
     */
    @OnClick(R.id.ly_notice)void noticeClick(){
        if(null == teamChatInfoBean){
            RxToast.custom("未获取到聊天室信息").show();
            return;
        }
        if(null == teamChatInfoBean.getData()){
            RxToast.custom("未获取到聊天室信息").show();
            return;
        }
        if(null == teamChatInfoBean.getData().getNoticeInfo()){
            if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ) {
                RxToast.custom("没有公告信息").show();
                return;
            }else {
                TeamChatEditNotice.startActivity(this,teamId,"","",0L,"",mRole);
              return;
            }
        }

        if(TextUtils.isEmpty(teamChatInfoBean.getData().getNoticeInfo().getNotice())){
            if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ) {
                RxToast.custom("没有公告信息").show();
                return;
            }else {
                TeamChatInfoBean.DataBean.NoticeInfoBean noticeData = teamChatInfoBean.getData().getNoticeInfo();
                TeamChatEditNotice.startActivity(this,teamId,noticeData.getUsername(),noticeData.getHead(),Long.parseLong(noticeData.getCreateTime()),noticeData.getNotice(),mRole);
                return;
            }
        }

        TeamChatInfoBean.DataBean.NoticeInfoBean noticeData = teamChatInfoBean.getData().getNoticeInfo();
        TeamChatEditNotice.startActivity(this,teamId,noticeData.getUsername(),noticeData.getHead(),Long.parseLong(noticeData.getCreateTime()) ,noticeData.getNotice(),mRole);
    }


    /**
     * 群管理点击事件
     */
    @OnClick(R.id.tv_teamchat_management)void managementClick(){
        if(mRole > Constant.ChatRoomRole.ADMIN_ACCOUNT ){
            RxToast.custom("没有权限").show();
            return;
        }
        ActivityTeamChatPersonList.startActivity(this,teamId);
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
        bundle.putString("teamId",teamId);
        bundle.putString("nickName",tv_nickname.getText().toString());
        RxActivityTool.skipActivity(this,TeamChatEditNickName.class,bundle);
    }

    /**
     * 举报点击事件
     */
    @OnClick(R.id.tv_report)void reportClick(){
        //旧链接
        String url = Constant.H5_BASE_URL + "/author/#/report?source=4&targetId="+teamId + "&createdBy=" + RxLoginTool.getLoginAccountToken().getData().getUid();
        //新链接
        //String url = Constant.H5_BASE_URL+"/inform/#/report?source=3&targetId=" + chatRoomId + "&createdBy=" + RxLoginTool.getLoginAccountToken().getData().getUid();
        RxLogTool.e("URl" , url);
        ActivityWebView.startActivity(ActivityTeamChatInfo.this,url,1);
    }

    /**
     * 退出群聊
     */
    private void requestUnConcernPlatform(){
        HashMap map=new HashMap();
        map.put("teamId",teamId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/exist",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }
            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean= GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //退出成功
                        RxEventBusTool.sendEvents(String.valueOf(teamId),Constant.EventTag.CHAT_ROOM_EXIT);
                        finish();
                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    RxLogTool.e("requestUnConcernPlatform e:"+e.getMessage());
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

                        //群聊名称
                        if(!TextUtils.isEmpty(teamChatInfoBean.getData().getTeamName())){
                            tv_common_title.setText(teamChatInfoBean.getData().getTeamName());
                            tv_teamchat_name.setText(teamChatInfoBean.getData().getTeamName());
                        }
                        //群公告
                        if (!TextUtils.isEmpty(teamChatInfoBean.getData().getNoticeInfo().getNotice())){
                            tv_notice.setText(teamChatInfoBean.getData().getNoticeInfo().getNotice());
                        }else {
                            tv_notice.setText("");
                        }
                        //我的群昵称
                        if (!TextUtils.isEmpty(teamChatInfoBean.getData().getNickName())){
                            tv_nickname.setText(teamChatInfoBean.getData().getNickName());
                        }
                        //相关连载号
                        if (!TextUtils.isEmpty(teamChatInfoBean.getData().getPlatformName())){
                            tv_teamchat_lianzaihao.setText(teamChatInfoBean.getData().getPlatformName());
                        }

                        mRole = teamChatInfoBean.getData().getRoleType();
                        //根据角色显示不同视图
                          if(mRole == Constant.ChatRoomRole.HAOZHU_ACCOUNT || mRole == Constant.ChatRoomRole.SYSTEM_ACCOUNT || mRole == Constant.ChatRoomRole.AUTHOR_ACCOUNT) {
                              //群主和系统账号，修改公告，可拉黑，不能退出群聊，可禁言
                              tv_teamchat_management.setVisibility(View.VISIBLE);
                              tv_delete_recent.setVisibility(View.GONE);
                          }else if(mRole == Constant.ChatRoomRole.ADMIN_ACCOUNT ){
                              //管理员，可禁言
                              tv_teamchat_management.setVisibility(View.VISIBLE);
                          }else {
                              //普通成员，无法看到群管理视图
                              tv_teamchat_management.setVisibility(View.GONE);
                          }
                          //成员列表布局
                        teamChatMembers.clear();
                        if(null != teamChatInfoBean.getData().getUserList() && !teamChatInfoBean.getData().getUserList().isEmpty()){
                            int size = teamChatInfoBean.getData().getUserList().size();
                            if(size < 21){
                                teamChatMembers.addAll(teamChatInfoBean.getData().getUserList());
                                tv_showmore.setVisibility(View.GONE);
                            }else {
                                teamChatMembers.addAll(teamChatInfoBean.getData().getUserList());
                                teamChatMembers.remove(teamChatMembers.size() - 1);
                                tv_showmore.setVisibility(View.VISIBLE);
                            }
                        }
                        teamChatGridAdapter.notifyDataSetChanged();

                    }else{
                        RxToast.custom(teamChatInfoBean.getMsg()).show();
                    }
                }catch (Exception e){

                }

            }
        });
    }





}
