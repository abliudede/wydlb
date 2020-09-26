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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AdministratorListBean;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookListResponse;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.CircleManagerPersonItemAdapter;
import com.lianzai.reader.ui.adapter.CirclePersonItemAdapter;
import com.lianzai.reader.ui.contract.UserAttentionContract;
import com.lianzai.reader.ui.presenter.UserAttentionPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogConfirm;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 圈子管理员列表
 */

public class ActivityCircleManagerPersonList extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    CircleManagerPersonItemAdapter circleManagerPersonItemAdapter;
    List<AdministratorListBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private String nextId = "";
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
        RxActivityTool.skipActivity(activity, ActivityCircleManagerPersonList.class, bundle);
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


        tv_commom_title.setText("管理员名单");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        circleManagerPersonItemAdapter= new CircleManagerPersonItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        circleManagerPersonItemAdapter.setLoadMoreView(customLoadMoreView);
        circleManagerPersonItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCircleManagerPersonList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new RxRecyclerViewDividerTool(0, 0, RxImageTool.dip2px(10), 0));
        recycler_view.setAdapter(circleManagerPersonItemAdapter);

        circleManagerPersonItemAdapter.setOnPersonClickListener(new CircleManagerPersonItemAdapter.OnPersonClickListener() {
            @Override
            public void removeClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleManagerPersonList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        AdministratorListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        rxDialogSureCancelNew.getSureView().setOnClickListener(
                                v1 -> {
                                    requestRemoveAdministrator(String.valueOf(temp.getUserId()),pos);
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
                        AdministratorListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCircleManagerPersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });

        circleManagerPersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


        //头部显示
        headview = getLayoutInflater().inflate(R.layout.header_circle_manager_person_list, null);
        TextView tv_des = headview.findViewById(R.id.tv_des);
        tv_des.setText("管理员职责为协助圈主/代圈主维护圈内秩序，活跃圈子气氛，协助圈子建设。且每个圈子最多拥有10个管理员。");
        circleManagerPersonItemAdapter.addHeaderView(headview);


        rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        rxDialogSureCancelNew.getTitleView().setText("确定移除管理员");
        rxDialogSureCancelNew.getContentView().setText("确定要将该管理员移除？");
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
        nextId="";
        personRequest();
    }

    @Override
    public void onLoadMoreRequested() {
        personRequest();
    }

    private void personRequest() {
        ArrayMap map=new ArrayMap();
        if(!TextUtils.isEmpty(nextId))
        map.put("nextId",String.valueOf(nextId));
        map.put("pageSize",String.valueOf(pageSize));
        map.put("circleId",circleId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/administratorList", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circleManage/administratorList").show();
                    circleManagerPersonItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    AdministratorListBean administratorListBean = GsonUtil.getBean(response, AdministratorListBean.class);
                    if (administratorListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<AdministratorListBean.DataBean.ListBean> tempList = administratorListBean.getData().getList();
                        if(null == tempList){
                            tempList = new ArrayList<>();
                        }
                        if(TextUtils.isEmpty(nextId)){
                            personArrayList.clear();
                            personArrayList.addAll(tempList);
                            circleManagerPersonItemAdapter.replaceData(personArrayList);
                        }else{
                            personArrayList.addAll( tempList);
                            circleManagerPersonItemAdapter.replaceData(personArrayList);
                        }

                        //是否允许加载更多
                        if (tempList.isEmpty()){//最后一页
                            circleManagerPersonItemAdapter.setEnableLoadMore(false);
                            circleManagerPersonItemAdapter.loadMoreEnd();
                        }else{
                            circleManagerPersonItemAdapter.setEnableLoadMore(true);
                            circleManagerPersonItemAdapter.loadMoreComplete();
                            nextId = String.valueOf(tempList.get(tempList.size() - 1).getId());
                        }
                    } else {//加载失败
                        circleManagerPersonItemAdapter.loadMoreFail();
                        RxToast.custom(administratorListBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //移除管理员
    private void requestRemoveAdministrator (String userId,int pos) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("circleId", circleId);
        jsonObject.addProperty("removeUserId", userId);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/removeAdministrator" , jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                        circleManagerPersonItemAdapter.remove(pos);
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }






}