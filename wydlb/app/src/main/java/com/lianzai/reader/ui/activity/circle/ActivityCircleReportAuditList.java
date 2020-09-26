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
import com.lianzai.reader.bean.AdministratorListBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.CircleManagerPersonItemAdapter;
import com.lianzai.reader.ui.adapter.CircleReportAuditItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogMuteOptions;
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
 * 圈子审核举报内容列表
 */

public class ActivityCircleReportAuditList extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    CircleReportAuditItemAdapter circleReportAuditItemAdapter;
    List<ReportAuditListBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private String nextTime = "";
    private CustomLoadMoreView customLoadMoreView;
    private String circleId;
    private int userType;

    /*头部信息相关*/
    private View headview;

    RxDialogSureCancelNew rxDialogSureCancelNew;//二次确认提示
    RxDialogMuteOptions rxDialogMuteOptions;//禁言理由选项

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity, String circleId ,int userType) {
        Bundle bundle = new Bundle();
        bundle.putString("circleId", circleId);
        bundle.putInt("userType", userType);
        RxActivityTool.skipActivity(activity, ActivityCircleReportAuditList.class, bundle);
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
        try{
            circleId = getIntent().getExtras().getString("circleId");
            userType = getIntent().getExtras().getInt("userType");
        }catch (Exception e){
        }


        tv_commom_title.setText("审核举报内容");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        circleReportAuditItemAdapter= new CircleReportAuditItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        circleReportAuditItemAdapter.setLoadMoreView(customLoadMoreView);
        circleReportAuditItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCircleReportAuditList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new RxRecyclerViewDividerTool(0, 0, 0, RxImageTool.dip2px(10)));
        recycler_view.setAdapter(circleReportAuditItemAdapter);

        circleReportAuditItemAdapter.setOnPersonClickListener(new CircleReportAuditItemAdapter.OnPersonClickListener() {

            @Override
            public void muteClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleReportAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ReportAuditListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        muteWay(String.valueOf(temp.getObjectId()),String.valueOf(temp.getContentType()),String.valueOf(temp.getUserId()),pos);
                    }
                }
            }

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ReportAuditListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCircleReportAuditList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }

            @Override
            public void ignoreClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleReportAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ReportAuditListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        requestReportManage("",1,String.valueOf(temp.getObjectId()),String.valueOf(temp.getContentType()),String.valueOf(temp.getUserId()),pos);
                    }
                }

            }

            @Override
            public void deleteClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleReportAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ReportAuditListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        requestReportManage("",2,String.valueOf(temp.getObjectId()),String.valueOf(temp.getContentType()),String.valueOf(temp.getUserId()),pos);
                    }
                }
            }

            @Override
            public void detailClick(int pos) {
                //举报明细页面
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleReportAuditList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        ReportAuditListBean.DataBean.ListBean temp = personArrayList.get(pos);
                        ActivityReportDetail.startActivity(ActivityCircleReportAuditList.this,String.valueOf(temp.getObjectId()),String.valueOf(temp.getContentType()));
                    }
                }
            }

            @Override
            public void imageClick(int index, int pos) {
                try{
                    //普通动态
                    ReportAuditListBean.DataBean.ListBean listbean = personArrayList.get(pos);
                    ArrayList<String> listtemp = new ArrayList<String>();
                    listtemp.addAll(listbean.getImages());
                    ActivityImagesPreview.startActivity(ActivityCircleReportAuditList.this,listtemp,index);
                }catch (Exception e){
                }
            }
        });

        circleReportAuditItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


        //头部显示
        headview = getLayoutInflater().inflate(R.layout.header_circle_manager_person_list, null);
        TextView tv_des = headview.findViewById(R.id.tv_des);
        tv_des.setText("圈内被举报的内容会出现该处，请您依据维护圈内秩序的原则酌情处理。");
        circleReportAuditItemAdapter.addHeaderView(headview);

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

    private void muteWay(String objectId,String objectType,String objectUserId,int pos){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("禁言");
        rxDialogSureCancelNew.setContent("确定对Ta进行禁言吗？禁言后，Ta将不能再在圈子里发表任何言论。");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                showMuteOptionsDialog(objectId,objectType,objectUserId,pos);
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

    private void showMuteOptionsDialog(String objectId,String objectType,String objectUserId,int pos){
        if (null == rxDialogMuteOptions) {
            rxDialogMuteOptions = new RxDialogMuteOptions(this);
        }
        rxDialogMuteOptions.getTv_des1().setOnClickListener(
                v -> {
                    requestReportManage(rxDialogMuteOptions.getTv_des1().getText().toString(),3,objectId,objectType,objectUserId,pos);
                    rxDialogMuteOptions.dismiss();
                }
        );
        rxDialogMuteOptions.getTv_des2().setOnClickListener(
                v -> {
                    requestReportManage(rxDialogMuteOptions.getTv_des2().getText().toString(),3,objectId,objectType,objectUserId,pos);
                    rxDialogMuteOptions.dismiss();
                }
        );
        rxDialogMuteOptions.getTv_des3().setOnClickListener(
                v -> {
                    requestReportManage(rxDialogMuteOptions.getTv_des3().getText().toString(),3,objectId,objectType,objectUserId,pos);
                    rxDialogMuteOptions.dismiss();
                }
        );
        rxDialogMuteOptions.getTv_des4().setOnClickListener(
                v -> {
                    requestReportManage(rxDialogMuteOptions.getTv_des4().getText().toString(),3,objectId,objectType,objectUserId,pos);
                    rxDialogMuteOptions.dismiss();
                }
        );
        rxDialogMuteOptions.show();
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
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/reportAuditList" , map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circleManage/reportAuditList").show();
                    circleReportAuditItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    ReportAuditListBean reportAuditListBean = GsonUtil.getBean(response, ReportAuditListBean.class);
                    if (reportAuditListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<ReportAuditListBean.DataBean.ListBean> tempList = reportAuditListBean.getData().getList();
                        if(null == tempList){
                            tempList = new ArrayList<>();
                        }
                        //过滤掉无权限的选项
                        List<ReportAuditListBean.DataBean.ListBean> tempResultList  = new ArrayList<>();
                        for(ReportAuditListBean.DataBean.ListBean item : tempList){
                            if(item.getUserType() > userType){
                                tempResultList.add(item);
                            }
                        }


                        if(TextUtils.isEmpty(nextTime)){
                            personArrayList.clear();
                            personArrayList.addAll(tempResultList);
                            circleReportAuditItemAdapter.replaceData(personArrayList);
                        }else{
                            personArrayList.addAll( tempResultList);
                            circleReportAuditItemAdapter.replaceData(personArrayList);
                        }


                        //是否允许加载更多
                        if (tempList.isEmpty()){//最后一页
                            circleReportAuditItemAdapter.setEnableLoadMore(false);
                            circleReportAuditItemAdapter.loadMoreEnd();
                        }else{
                            circleReportAuditItemAdapter.setEnableLoadMore(true);
                            circleReportAuditItemAdapter.loadMoreComplete();
                            nextTime = String.valueOf(tempList.get(tempList.size() - 1).getCreateTime());
                        }

                    } else {//加载失败
                        circleReportAuditItemAdapter.loadMoreFail();
                        RxToast.custom(reportAuditListBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    //举报处理
    /*manageType (integer, optional): 处理类型(1忽略,2删除,3禁言（禁言用户且当前内容忽略）)
objectId (integer, optional): 对象ID
objectType (integer, optional): 对象类型（1帖子,2评论,3回复）
*/
    private void requestReportManage  (String bannedDescribe,int manageType,String objectId ,String objectType,String objectUserId,int pos) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bannedDescribe", bannedDescribe);
        jsonObject.addProperty("circleId", circleId);
        jsonObject.addProperty("manageType", String.valueOf(manageType));
        jsonObject.addProperty("objectId", objectId);
        jsonObject.addProperty("objectType", objectType);
        jsonObject.addProperty("objectUserId", objectUserId);

        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/reportManage" , jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                        circleReportAuditItemAdapter.remove(pos);
                        if(manageType == 3) {
                            RxToast.custom("禁言成功").show();
                        }else if(manageType == 2){
                            RxToast.custom("已删除").show();
                        }else {
                            RxToast.custom("已忽略").show();
                        }
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }






}
