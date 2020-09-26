package com.lianzai.reader.ui.activity.TeamChat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ChatInfoResponse;
import com.lianzai.reader.bean.TeamBanPersonBean;
import com.lianzai.reader.bean.TeamChatInfoBean;
import com.lianzai.reader.bean.TeamChatShowPersonBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.ChatRoomPersonGridAdapter;
import com.lianzai.reader.ui.adapter.TeamChatGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.view.RxToast;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/09/26.
 * 更多成员的显示列表
 */

public class ActivityTeamChatShowPerson extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;
    //列表
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TeamChatShowPersonBean teamChatShowPersonBean;
    TeamChatGridAdapter teamChatGridAdapter;
    List<TeamChatInfoBean.DataBean.UserListBean> teamChatMembers = new ArrayList<>();

    //房间号
    String teamId;
    //用作双击检测
    long mClickTime=0;


    public static void startActivity(Context context, String teamId){
        Bundle bundle=new Bundle();
        bundle.putString("teamId",teamId);
        RxActivityTool.skipActivity(context,ActivityTeamChatShowPerson.class,bundle);
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

        tv_commom_title.setText("群成员");

        teamId = getIntent().getExtras().getString("teamId");

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

        teamChatGridAdapter = new TeamChatGridAdapter(teamChatMembers, ActivityTeamChatShowPerson.this);
        teamChatGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TeamChatInfoBean.DataBean.UserListBean itemData = teamChatGridAdapter.getData().get(position);
                PerSonHomePageActivity.startActivity(ActivityTeamChatShowPerson.this, String.valueOf(itemData.getUid()));

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));
        //表格列表
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(teamChatGridAdapter);


        if (RxNetTool.isAvailable()) {
            showDialog();
                        showOrCloseRefresh(true);
                        onRefresh();
        } else {
            teamChatGridAdapter.setEmptyView(R.layout.view_disconnect_network,recyclerView);
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
                        teamChatGridAdapter.notifyDataSetChanged();
                        if(teamChatMembers.size() > 0){
                        }else {
                            teamChatGridAdapter.setEmptyView(R.layout.view_disconnect_network,recyclerView);
                        }

                    }else{
                        RxToast.custom(teamChatShowPersonBean.getMsg()).show();
                    }

                }catch (Exception e){

                }
            }
        });
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

}
