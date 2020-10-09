package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivityForTranslucent;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ReportDetailBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.ReportDetailItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2019/03/20.
 * 举报详情弹框
 */

public class ActivityReportDetail extends BaseActivityForTranslucent implements BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.activity_creatquanzi_recycle)
    RecyclerView activity_creatquanzi_recycle;

    /**
     * 显示的数据
     */
    private List<ReportDetailBean.DataBean.ListBean> replyListData = new ArrayList<>();
    /**
     * RecyclerView的适配器
     */
    private ReportDetailItemAdapter reportDetailItemAdapter;

    CustomLoadMoreView customLoadMoreView;

    String nextTime = "";

    String objectId;//对象ID
    String objectType;//对象类型


    @Override
    public int getLayoutId() {
        return R.layout.activity_report_detail;
    }

    public static void startActivity(Context context, String objectId, String objectType) {
//        RxActivityTool.removeActivityPostFloor();
        Bundle bundle = new Bundle();
        bundle.putString("objectId", objectId);
        bundle.putString("objectType", objectType);
        RxActivityTool.skipActivity(context, ActivityReportDetail.class, bundle);
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {

    }
    @Override
    public void configViews(Bundle savedInstanceState) {
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        try{
            objectId = getIntent().getExtras().getString("objectId");
            objectType = getIntent().getExtras().getString("objectType");
        }catch (Exception e){

        }


        reportDetailItemAdapter = new ReportDetailItemAdapter(replyListData);
        reportDetailItemAdapter.setOnLoadMoreListener(this, activity_creatquanzi_recycle);
        customLoadMoreView = new CustomLoadMoreView();
        reportDetailItemAdapter.setLoadMoreView(customLoadMoreView);

        reportDetailItemAdapter.setContentClickListener(new ReportDetailItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(ActivityReportDetail.this, String.valueOf(replyListData.get(pos).getUserId()));
                } catch (Exception e) {
                }
            }

        });


        activity_creatquanzi_recycle.setLayoutManager(new RxLinearLayoutManager(this));
        //设置添加,移除item的动画,DefaultItemAnimator为默认的
        activity_creatquanzi_recycle.setItemAnimator(new DefaultItemAnimator());
        activity_creatquanzi_recycle.setAdapter(reportDetailItemAdapter);
//        activity_creatquanzi_recycle.addItemDecoration(new RxRecyclerViewDividerTool(0,0 , 0,RxImageTool.dip2px(10)));


        requestReportDetailList (true);
    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    @OnClick(R.id.img_back)void closeClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }

    @OnClick(R.id.bg_click)void bg_clickClick(){
        finish();
        overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_bottom);
    }


    @Override
    public void onLoadMoreRequested() {
        requestReportDetailList(false);
    }

    /**
     * 话题详情
     */
    private void requestReportDetailList(boolean isRefresh) {

        if (isRefresh) {
            nextTime = "";
        }
        Map<String, String> map = new HashMap<>();
        if(!TextUtils.isEmpty(nextTime))
        map.put("nextTime", String.valueOf(nextTime));
        map.put("pageSize", String.valueOf(100));
        map.put("objectId", objectId);
        map.put("objectType", objectType);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/reportDetailList", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circleManage/reportDetailList").show();
                    reportDetailItemAdapter.loadMoreFail();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("requestHistory:" + response);
                    ReportDetailBean baseBean = GsonUtil.getBean(response, ReportDetailBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (isRefresh) {
                            tv_title.setText("该动态已被举报" + baseBean.getData().getTotal() + "次");
                            replyListData.clear();
                        }

                        List<ReportDetailBean.DataBean.ListBean> tempList = baseBean.getData().getList();
                        if(null == tempList){
                            tempList = new ArrayList<>();
                        }
                        replyListData.addAll(tempList);
                        reportDetailItemAdapter.replaceData(replyListData);



                        if(null != tempList && ! tempList.isEmpty()) {
                            reportDetailItemAdapter.setEnableLoadMore(true);
                            reportDetailItemAdapter.loadMoreComplete();
                            nextTime = String.valueOf(tempList.get(tempList.size() - 1).getCreateTime());
                        }else {
                            reportDetailItemAdapter.setEnableLoadMore(false);
                            reportDetailItemAdapter.loadMoreEnd();
                        }

                    } else {
                        reportDetailItemAdapter.loadMoreFail();
                        RxToast.custom(baseBean.getMsg()).show();
                    }

                } catch (Exception e) {
//                    RxToast.custom("加载失败/circleManage/reportDetailList").show();
                    reportDetailItemAdapter.loadMoreFail();
                }
            }
        });
    }



}
