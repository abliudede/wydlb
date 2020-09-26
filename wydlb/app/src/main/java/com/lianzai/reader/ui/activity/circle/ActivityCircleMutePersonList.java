package com.lianzai.reader.ui.activity.circle;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AdministratorListBean;
import com.lianzai.reader.bean.BannedListBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.CircleManagerPersonItemAdapter;
import com.lianzai.reader.ui.adapter.UserMuteItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 圈子禁言列表
 */

public class ActivityCircleMutePersonList extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    UserMuteItemAdapter userMuteItemAdapter;
    List<BannedListBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private String nextTime = "";
    private CustomLoadMoreView customLoadMoreView;
    private String circleId;
    private int userType;

    /*头部信息相关*/
    private View headview;

    private RxDialogSureCancelNew rxDialogSureCancelNew;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity, String circleId ,int userType) {
        Bundle bundle = new Bundle();
        bundle.putString("circleId", circleId);
        bundle.putInt("userType", userType);
        RxActivityTool.skipActivity(activity, ActivityCircleMutePersonList.class, bundle);
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
        try{
            circleId = getIntent().getExtras().getString("circleId");
            userType = getIntent().getExtras().getInt("userType");
        }catch (Exception e){
        }


        tv_commom_title.setText("被禁言名单");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        userMuteItemAdapter= new UserMuteItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        userMuteItemAdapter.setLoadMoreView(customLoadMoreView);
        userMuteItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCircleMutePersonList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new RxRecyclerViewDividerTool(0, 0, RxImageTool.dip2px(10), 0));
        recycler_view.setAdapter(userMuteItemAdapter);

        userMuteItemAdapter.setOnPersonClickListener(new UserMuteItemAdapter.OnPersonClickListener() {
            @Override
            public void removeClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleMutePersonList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        BannedListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        rxDialogSureCancelNew.getSureView().setOnClickListener(
                                v1 -> {
                                    requestRemoveBanned(String.valueOf(temp.getUserId()),pos);
                                    rxDialogSureCancelNew.dismiss();
                                }
                        );
                        rxDialogSureCancelNew.show();

                    }
                }
            }

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        BannedListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCircleMutePersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });

        userMuteItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


        //头部显示
        headview = getLayoutInflater().inflate(R.layout.header_circle_manager_person_list, null);
        TextView tv_des = headview.findViewById(R.id.tv_des);
        tv_des.setText("若检测到圈内成员有发表违法言论，或者恶意灌水，圈主/管理员可对其禁言。 禁言后，成员将不能在圈内发表动态，也不能对圈内的任何动态、评论、回复等进行回复。 在用户任意动态、评论、回复下点击，可对其进行禁言操作");
        userMuteItemAdapter.addHeaderView(headview);

        rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        rxDialogSureCancelNew.getTitleView().setText("确定解除禁言");
        rxDialogSureCancelNew.getContentView().setText("确定要将该成员解除禁言？");
        rxDialogSureCancelNew.getSureView().setText("确定");
        rxDialogSureCancelNew.getCancelView().setText("取消");

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
        nextTime="";
        personRequest();
    }

    @Override
    public void onLoadMoreRequested() {
        personRequest();
    }

    private void personRequest() {
        ArrayMap map=new ArrayMap();
        if(!TextUtils.isEmpty(nextTime))
        map.put("nextTime",String.valueOf(nextTime));
        map.put("pageSize",String.valueOf(pageSize));
        map.put("circleId",circleId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/bannedList", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circleManage/bannedList").show();
                    userMuteItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    BannedListBean administratorListBean = GsonUtil.getBean(response, BannedListBean.class);
                    if (administratorListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<BannedListBean.DataBean.ListBean> tempList = administratorListBean.getData().getList();
                        if(null == tempList){
                            tempList = new ArrayList<>();
                        }
                        if(TextUtils.isEmpty(nextTime)){
                            personArrayList.clear();
                            personArrayList.addAll(tempList);
                            userMuteItemAdapter.replaceData(personArrayList);
                        }else{
                            personArrayList.addAll( tempList);
                            userMuteItemAdapter.replaceData(personArrayList);
                        }


                        //是否允许加载更多
                        if (tempList.isEmpty()){//最后一页
                            userMuteItemAdapter.setEnableLoadMore(false);
                            userMuteItemAdapter.loadMoreEnd();
                        }else{
                            userMuteItemAdapter.setEnableLoadMore(true);
                            userMuteItemAdapter.loadMoreComplete();
                            nextTime = tempList.get(tempList.size() - 1).getCreateTime();
                        }

                    } else {//加载失败
                        userMuteItemAdapter.loadMoreFail();
                        RxToast.custom(administratorListBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //解除禁言
    private void requestRemoveBanned (String userId,int pos) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("circleId", circleId);
        jsonObject.addProperty("removeUserId", userId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/removeBanned" , jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        userMuteItemAdapter.remove(pos);
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }






}
