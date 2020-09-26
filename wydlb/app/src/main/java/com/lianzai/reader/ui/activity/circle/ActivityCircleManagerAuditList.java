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
import com.lianzai.reader.base.ReportAuditListBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ManagerAuditBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.CircleManagerAuditItemAdapter;
import com.lianzai.reader.ui.adapter.CircleReportAuditItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogMuteOptions;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogVotingRules;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 圈子审核管理员列表
 */

public class ActivityCircleManagerAuditList extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    CircleManagerAuditItemAdapter circleManagerAuditItemAdapter;
    List<ManagerAuditBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private String nextTime = "";
    private CustomLoadMoreView customLoadMoreView;

    /*头部信息相关*/
    private View headview;

    RxDialogSureCancelNew rxDialogSureCancelNew;//二次确认提示

    RxDialogVotingRules rxDialogVotingRules;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity) {//, String circleId ,int userType
        RxActivityTool.skipActivity(activity, ActivityCircleManagerAuditList.class);
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

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        tv_commom_title.setText("圈子申请");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        circleManagerAuditItemAdapter= new CircleManagerAuditItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        circleManagerAuditItemAdapter.setLoadMoreView(customLoadMoreView);
        circleManagerAuditItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCircleManagerAuditList.this);
        recycler_view.setLayoutManager(manager);
//        recycler_view.addItemDecoration(new RxRecyclerViewDividerTool(0, 0, RxImageTool.dip2px(10), 0));
        recycler_view.setAdapter(circleManagerAuditItemAdapter);

        circleManagerAuditItemAdapter.setOnPersonClickListener(new CircleManagerAuditItemAdapter.OnPersonClickListener() {

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ManagerAuditBean.DataBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCircleManagerAuditList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }


            @Override
            public void deleteClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleManagerAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ManagerAuditBean.DataBean.ListBean temp = personArrayList.get(pos);
                        requestReportManage(temp.getApplyId(),temp.getCircleId(),2,pos);
                    }
                }
            }

            @Override
            public void detailClick(int pos) {
                //申请原文弹窗
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleManagerAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ManagerAuditBean.DataBean.ListBean temp = personArrayList.get(pos);
                        if(null == rxDialogVotingRules){
                            rxDialogVotingRules = new RxDialogVotingRules(ActivityCircleManagerAuditList.this,R.style.OptionDialogStyle);
                        }
                        rxDialogVotingRules.getTv_title().setText(temp.getNickName() + "的申请理由");
                        rxDialogVotingRules.getTv_des().setText(temp.getApplyContent());
                        rxDialogVotingRules.show();
                    }
                }
            }

            @Override
            public void sureClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleManagerAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ManagerAuditBean.DataBean.ListBean temp = personArrayList.get(pos);
                        sureWay(temp,pos);
                    }
                }
            }
        });

        circleManagerAuditItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


        //头部显示
        headview = getLayoutInflater().inflate(R.layout.header_circle_manager_person_list, null);
        TextView tv_des = headview.findViewById(R.id.tv_des);
        tv_des.setText("管理员职责为协助圈主/代圈主维护圈内秩序，活跃圈子气氛，协助圈子建设。且每个圈子最多拥有10个管理员。");
        circleManagerAuditItemAdapter.addHeaderView(headview);


        //回到此页面刷新数据
        showOrCloseRefresh(true);
        onRefresh();

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


    private void sureWay(ManagerAuditBean.DataBean.ListBean temp,int pos){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("通过申请");
        rxDialogSureCancelNew.setContent("是否通过此用户的管理员申请?");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                requestReportManage(temp.getApplyId(),temp.getCircleId(),1,pos);
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
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
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/administratorApplyList" , map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circleManage/administratorApplyList").show();
                    circleManagerAuditItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    ManagerAuditBean managerAuditBean = GsonUtil.getBean(response, ManagerAuditBean.class);
                    if (managerAuditBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<ManagerAuditBean.DataBean.ListBean> tempList = managerAuditBean.getData().getList();
                        if(null == tempList){
                            tempList = new ArrayList<>();
                        }
                        if(TextUtils.isEmpty(nextTime)){
                            personArrayList.clear();
                        }
                        personArrayList.addAll(tempList);
                        circleManagerAuditItemAdapter.replaceData(personArrayList);

                        // 是否允许加载更多
                        if (tempList.isEmpty()){//最后一页
                            circleManagerAuditItemAdapter.setEnableLoadMore(false);
                            circleManagerAuditItemAdapter.loadMoreEnd();
                        }else{
                            circleManagerAuditItemAdapter.setEnableLoadMore(true);
                            circleManagerAuditItemAdapter.loadMoreComplete();
                            nextTime = tempList.get(tempList.size() - 1).getCreateTime();
                        }

                    } else {//加载失败
                        circleManagerAuditItemAdapter.loadMoreFail();
                        RxToast.custom(managerAuditBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


  /*申请处理
applyId (integer, optional): 申请记录ID
circleId (integer, optional): 圈子ID
manageType (integer, optional): 处理类型(1通过，2拒绝)
 */
    private void requestReportManage  (int applyId,int circleId,int manageType,int pos) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("applyId", applyId);
        jsonObject.addProperty("circleId", circleId);
        jsonObject.addProperty("manageType", manageType);

        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/applyManage" , jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                            circleManagerAuditItemAdapter.remove(pos);
                            RxToast.custom("操作成功").show();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                        onRefresh();
                    }
                } catch (Exception e) {
                }
            }
        });
    }






}
