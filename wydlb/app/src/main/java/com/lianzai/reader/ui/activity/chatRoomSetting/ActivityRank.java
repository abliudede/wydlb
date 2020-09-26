package com.lianzai.reader.ui.activity.chatRoomSetting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ChatInfoResponse;
import com.lianzai.reader.bean.ChatRoomPersonBaseInfo;
import com.lianzai.reader.bean.ChatRoomPersonListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityChoosePayWay;
import com.lianzai.reader.ui.adapter.ChatRoomRankPersonListItemAdapter;
import com.lianzai.reader.ui.presenter.ChatRoomPersonListPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogVotingMustKnow;
import com.lianzai.reader.view.dialog.RxDialogVotingRules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

public class ActivityRank extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;

    @Bind(R.id.tv_rank_num)
    TextView tv_rank_num;

    @Bind(R.id.tv_nickname)
    TextView tv_nickname;

    @Bind(R.id.tv_des)
    TextView tv_des;

    @Bind(R.id.iv_user_head)
    SelectableRoundedImageView iv_user_head;

    @Bind(R.id.mine_view)
    RelativeLayout mine_view;



    ChatRoomRankPersonListItemAdapter chatRoomRankPersonListItemAdapter;
    List<ChatRoomPersonListBean.DataBean.NormalListBean> barBeanArrayList = new ArrayList<ChatRoomPersonListBean.DataBean.NormalListBean>();

    RxLinearLayoutManager manager;


    boolean isLoadMore = false;
    boolean isError = false;
    int page = 1;

    CustomLoadMoreView customLoadMoreView;


    RxDialogVotingRules rxDialogVotingRules;//规则弹窗
    RxDialogVotingMustKnow rxDialogVotingMustKnow;//须知弹窗

    String roomId;
    private ChatRoomPersonListBean chatInfoResponse = new ChatRoomPersonListBean();

    public static void startActivity(Context context,String chatRoomId) {
        Bundle bundle=new Bundle();
        bundle.putString("roomId",chatRoomId);
        RxActivityTool.skipActivity(context,ActivityRank.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rank;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("聊天室成员");

        roomId=getIntent().getExtras().getString("roomId");

        rxDialogVotingRules=new RxDialogVotingRules(ActivityRank.this,R.style.OptionDialogStyle);
        rxDialogVotingMustKnow=new RxDialogVotingMustKnow(ActivityRank.this,R.style.OptionDialogStyle);

        chatRoomRankPersonListItemAdapter= new ChatRoomRankPersonListItemAdapter(barBeanArrayList,ActivityRank.this);
        chatRoomRankPersonListItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                //只有点不再显示了之后才不显示
//                if(RxSharedPreferencesUtil.getInstance().getBoolean("votingmustknow",true)){
//                    rxDialogVotingMustKnow.show();
//                }


            }
        });
        customLoadMoreView = new CustomLoadMoreView();
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#333333"));

//        chatRoomRankPersonListItemAdapter.setLoadMoreView(customLoadMoreView);
//        chatRoomRankPersonListItemAdapter.setOnLoadMoreListener(this,rv_data);

        LinearLayoutManager manager=new LinearLayoutManager(ActivityRank.this);
        rv_data.setAdapter(chatRoomRankPersonListItemAdapter);
        rv_data.setLayoutManager(manager);

        if (RxNetTool.isAvailable()){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null!=swipe_refresh_layout)
                        swipe_refresh_layout.setRefreshing(true);
                    onRefresh();
                }
            },getResources().getInteger(R.integer.refresh_delay));
        }else{
            chatRoomRankPersonListItemAdapter.setEmptyView(R.layout.view_disconnect_network);
        }
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @OnClick(R.id.iv_options)
    void ruleClick(){
        rxDialogVotingRules.show();
    }


    @Override
    public void gc() {

    }

    @Override
    public void onRefresh() {
        requestChatActivenessInfo();
    }


    private void requestChatActivenessInfo(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/activeness/" + roomId, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null!=swipe_refresh_layout)
                                swipe_refresh_layout.setRefreshing(false);
//                            RxToast.custom("网络错误").show();
                        }
                    },getResources().getInteger(R.integer.refresh_delay));
                }catch (Exception ee){

                }

            }
            @Override
            public void onResponse(String response) {
                RxLogTool.e("onResponse:"+response);
                try {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null!=swipe_refresh_layout)
                                swipe_refresh_layout.setRefreshing(false);
                        }
                    },getResources().getInteger(R.integer.refresh_delay));

                    chatInfoResponse= GsonUtil.getBean(response,ChatRoomPersonListBean.class);

                    if (chatInfoResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if(null != chatInfoResponse ){
                            barBeanArrayList.clear();
                            barBeanArrayList.addAll(chatInfoResponse.getData().getCandidateList());
                            barBeanArrayList.addAll(chatInfoResponse.getData().getNormalList());
                            chatRoomRankPersonListItemAdapter.replaceData(barBeanArrayList);
                            //个人信息
                            if(null != chatInfoResponse.getData().getMyActiveness()) {
                                ChatRoomPersonListBean.DataBean.MyActivenessBean mineData = chatInfoResponse.getData().getMyActiveness();
                                if(mineData.isIsInRank()) {
                                    tv_rank_num.setText("已上榜");
                                }else{
                                    tv_rank_num.setText("未上榜");
                                }
                                RxImageTool.loadFangLogoImage(ActivityRank.this,mineData.getHead(),iv_user_head);
                                tv_nickname.setText(mineData.getNickName());
                                tv_des.setText("活跃度：" + mineData.getActiveness());
                            }else{
                                //未获取到
                                RxImageTool.loadFangLogoImage(ActivityRank.this,"",iv_user_head);
                                tv_rank_num.setText("未上榜");
                                tv_nickname.setText("我");
                                tv_des.setText("活跃度：0");
                            }
                        }else{
                            chatRoomRankPersonListItemAdapter.setEmptyView(R.layout.view_alluse_empty);
                        }
                    }else{
                        RxToast.custom(chatInfoResponse.getMsg()).show();
                        chatRoomRankPersonListItemAdapter.setEmptyView(R.layout.view_disconnect_network);
                    }
                }catch (Exception e){

                }



            }
        });
    }


}
