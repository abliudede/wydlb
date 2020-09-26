package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.MyNoticeDataBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityAttentionPersonList;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.MyNoticeItemAdapter;
import com.lianzai.reader.ui.adapter.TeamItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 我的通知列表
 */

public class ActivityMyNotice extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    CustomLoadMoreView customLoadMoreView;
    MyNoticeItemAdapter myNoticeItemAdapter;
    List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> arrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageNumber;
    private int pageSize = 10;

    View headerView;
    private LinearLayout ly_praise;
    private TextView tv_red1;
    private LinearLayout ly_fans;
    private TextView tv_red2;
    private LinearLayout ly_manager;
    private TextView tv_red3;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityMyNotice.class);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //回到此页面刷新数据
        showOrCloseRefresh(true);
        onRefresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tv_commom_title.setText("我的通知");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));



        myNoticeItemAdapter= new MyNoticeItemAdapter(arrayList);

        customLoadMoreView = new CustomLoadMoreView();
        myNoticeItemAdapter.setLoadMoreView(customLoadMoreView);
        myNoticeItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNumber++;
                    myNoticeRequest(false);
            }
        }, recycler_view);

        headerView = getLayoutInflater().inflate(R.layout.view_header_mynotice, null);
        ly_praise = headerView.findViewById(R.id.ly_praise);
        tv_red1 = headerView.findViewById(R.id.tv_red1);
        ly_fans = headerView.findViewById(R.id.ly_fans);
        tv_red2 = headerView.findViewById(R.id.tv_red2);
        ly_manager = headerView.findViewById(R.id.ly_manager);
        tv_red3 = headerView.findViewById(R.id.tv_red3);
        myNoticeItemAdapter.addHeaderView(headerView,0);
        ly_praise.setOnClickListener(
                v -> {
                    ActivityMyPraised.startActivity(ActivityMyNotice.this);
                }
        );

        ly_fans.setOnClickListener(
                v -> {
                    //跳转到我的粉丝列表
                    ActivityAttentionPersonList.startActivity(ActivityMyNotice.this,0,2);
                }
        );

        ly_manager.setOnClickListener(
                v -> {
                    //跳转到管理员申请列表
                    ActivityCircleManagerAuditList.startActivity(ActivityMyNotice.this);
                }
        );

        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityMyNotice.this);
        recycler_view.setLayoutManager(manager);
//        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(myNoticeItemAdapter);

        myNoticeItemAdapter.setContentClickListener(new MyNoticeItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(ActivityMyNotice.this, String.valueOf(arrayList.get(pos).getComUserId()));
                } catch (Exception e) {
                }
            }

            @Override
            public void praiseClick(View v, int pos) {

            }

            @Override
            public void replyClick(View v, int pos) {

            }

            @Override
            public void contentClick(View v, int pos) {
                try{
                    MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean bean = arrayList.get(pos);
//                    1评论/2回复
                    if(bean.getType() == 1){
                        ActivityPostDetail.startActivity(ActivityMyNotice.this,String.valueOf(bean.getPostId()),String.valueOf(bean.getId()));
                    }else {
                        ActivityPostFloor.startActivity(ActivityMyNotice.this,String.valueOf(bean.getCommentId()),String.valueOf(bean.getId()),String.valueOf(bean.getPostId()),String.valueOf(bean.getPlatformId()));
                    }

                }catch (Exception e){
                }
            }
        });

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


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }


    @Override
    public void gc() {
        RxToast.gc();
    }


    @Override
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        myNoticeRequest(true);
    }

    public void myNoticeRequest(boolean isRefresh){
        if (isRefresh){
            pageNumber=1;
        }
        Map<String, String> map=new HashMap<>();
        map.put("pageNumber",String.valueOf(pageNumber));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/userNotice", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/userNotice").show();
                    myNoticeItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    showOrCloseRefresh(false);
                    MyNoticeDataBean myNoticeDataBean = GsonUtil.getBean(response, MyNoticeDataBean.class);
                    if (myNoticeDataBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh){
                            arrayList.clear();
                            if(myNoticeDataBean.getData().getIsShowApplyCount() == 1){
                                ly_manager.setVisibility(View.VISIBLE);
                            }else {
                                ly_manager.setVisibility(View.GONE);
                            }
                            if(myNoticeDataBean.getData().getAdministratorApplyCount() > 0){
                                tv_red3.setVisibility(View.VISIBLE);
                                tv_red3.setText(String.valueOf(myNoticeDataBean.getData().getAdministratorApplyCount()));
                            }else {
                                tv_red3.setVisibility(View.GONE);
                                tv_red3.setText("");
                            }

                            //红点逻辑
                            if(myNoticeDataBean.getData().getLikeUnreadCount() > 0){
                                tv_red1.setVisibility(View.VISIBLE);
                                tv_red1.setText(String.valueOf(myNoticeDataBean.getData().getLikeUnreadCount()));
                            }else {
                                tv_red1.setVisibility(View.GONE);
                            }
                            if(myNoticeDataBean.getData().getConcernUnreadCount() > 0){
                                tv_red2.setVisibility(View.VISIBLE);
                                tv_red2.setText(String.valueOf(myNoticeDataBean.getData().getConcernUnreadCount()));
                            }else {
                                tv_red2.setVisibility(View.GONE);
                            }
                        }


//                        ly_manager.setVisibility(View.VISIBLE);
//                        if(myNoticeDataBean.getData().getConcernUnreadCount() > 0){
//                            tv_red3.setVisibility(View.VISIBLE);
//                            tv_red3.setText(String.valueOf(myNoticeDataBean.getData().getConcernUnreadCount()));
//                        }else {
//                            tv_red3.setVisibility(View.GONE);
//                        }

                        List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> listBeans = myNoticeDataBean.getData().getUserCommentPage().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            myNoticeItemAdapter.setEnableLoadMore(true);
                            myNoticeItemAdapter.loadMoreComplete();
                            arrayList.addAll(listBeans);
                            myNoticeItemAdapter.notifyDataSetChanged();
                        } else {
                            myNoticeItemAdapter.loadMoreEnd();
                        }
                    }else{//加载失败
                        RxToast.custom(myNoticeDataBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}
