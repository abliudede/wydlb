package com.lianzai.reader.ui.activity.chatRoomSetting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ChatRoomPersonBaseInfo;
import com.lianzai.reader.bean.ChatRoomPersonListBean;
import com.lianzai.reader.bean.MilepostBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.ChatRoomMilePostAdapter;
import com.lianzai.reader.ui.presenter.ChatRoomPersonListPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogVotingRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/*里程碑页面*/

public class ActivityMilepost extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;


    ChatRoomMilePostAdapter chatRoomMilePostAdapter;
    List<MilepostBean.DataBean.ListBean> barBeanArrayList = new ArrayList<>();

    RxLinearLayoutManager manager;


    boolean isLoadMore = false;
    boolean isError = false;
    int page = 1;

    CustomLoadMoreView customLoadMoreView;
    private String roomId;

    RxDialogVotingRules rxDialogVotingRules;//规则弹窗
    private MilepostBean milepostBean;


    public static void startActivity(Context context,String chatRoomId) {
        Bundle bundle=new Bundle();
        bundle.putString("roomId",chatRoomId);
        RxActivityTool.skipActivity(context,ActivityMilepost.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_milepost;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("里程碑");

        roomId=getIntent().getExtras().getString("roomId");

        rxDialogVotingRules=new RxDialogVotingRules(ActivityMilepost.this,R.style.OptionDialogStyle);
        rxDialogVotingRules.getTv_title().setText("里程碑说明");
        rxDialogVotingRules.getTv_des().setText("1、里程碑是什么？\n" +
                "里程碑旨在记录聊天室在运营发展过程中，所发生的较为重大的、具有纪念意义的节点事件。当符合里程碑记录规则的事件发生时，系统会自动发布和更新提醒。\n\n" +
                "2、哪些事件会被记录到里程碑？\n" +
                "包括但不限于聊天室首次开启；在线人数突破50人；成员数量突破500人；关注人数突破1000人；重要嘉宾、管理员或其他特殊人员入驻、更换；群内重要活动事件等。");

        chatRoomMilePostAdapter= new ChatRoomMilePostAdapter(barBeanArrayList,ActivityMilepost.this);
        chatRoomMilePostAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
        customLoadMoreView = new CustomLoadMoreView();
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#333333"));

        chatRoomMilePostAdapter.setLoadMoreView(customLoadMoreView);
        chatRoomMilePostAdapter.setOnLoadMoreListener(this,rv_data);


        LinearLayoutManager manager=new LinearLayoutManager(ActivityMilepost.this);
        rv_data.setAdapter(chatRoomMilePostAdapter);
//        rv_data.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
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
            chatRoomMilePostAdapter.setEmptyView(R.layout.view_disconnect_network,rv_data);
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
        page = 1;
        isLoadMore = false;
        requestMilepost();
    }

    @Override
    public void onLoadMoreRequested() {


        if (chatRoomMilePostAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
            RxLogTool.e("onLoadMoreRequested---不足一页");
            chatRoomMilePostAdapter.loadMoreEnd();
        } else {
                page++;
                isLoadMore = true;
                requestMilepost();
        }

    }


    private void requestMilepost(){
        Map<String,String> hashMap=new HashMap();
        hashMap.put("pageNum",String.valueOf(page));
        hashMap.put("pageSize","10");
        hashMap.put("roomId",roomId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/milestone/" + roomId,hashMap, new CallBackUtil.CallBackString() {
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
                            if(null != chatRoomMilePostAdapter && page == 1) {
                                chatRoomMilePostAdapter.setEnableLoadMore(true);
                                chatRoomMilePostAdapter.loadMoreComplete();
                            }
                        }
                    },getResources().getInteger(R.integer.refresh_delay));

                    milepostBean= GsonUtil.getBean(response,MilepostBean.class);

                    if (milepostBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if(null != milepostBean ){
                            if(page == 1)
                                barBeanArrayList.clear();
                            barBeanArrayList.addAll(milepostBean.getData().getList());
                            chatRoomMilePostAdapter.replaceData(barBeanArrayList);
                        }else{
                            chatRoomMilePostAdapter.setEmptyView(R.layout.view_alluse_empty,rv_data);
                        }
                    }else{
                        RxToast.custom(milepostBean.getMsg()).show();
                        chatRoomMilePostAdapter.setEmptyView(R.layout.view_disconnect_network,rv_data);
                    }

                }catch (Exception e){

                }


            }
        });
    }


}
