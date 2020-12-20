package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AutoDailyTicketStreamBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.AutoTicketRecordAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.SystemBarUtils;
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
 * Created by lrz on 2018/07/23
 * 自动投票记录
 */

public class ActivityAutoTicketRecord extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    AutoTicketRecordAdapter autoTicketRecordAdapter;
    List<AutoDailyTicketStreamBean.DataBean.ListBean> dataArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private int page = 1;
    private CustomLoadMoreView customLoadMoreView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity) {
        RxActivityTool.skipActivity(activity, ActivityAutoTicketRecord.class);
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

        tv_commom_title.setText("自动投票记录");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        autoTicketRecordAdapter= new AutoTicketRecordAdapter(dataArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        autoTicketRecordAdapter.setLoadMoreView(customLoadMoreView);
        autoTicketRecordAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityAutoTicketRecord.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(autoTicketRecordAdapter);


        autoTicketRecordAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
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
        page=1;

        requestData();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;

        requestData();
    }

    public void requestData(){
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/dailyTicket/getUserAutoSettingDailyTicketStream",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/dailyTicket/getUserAutoSettingDailyTicketStream").show();
                    autoTicketRecordAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    AutoDailyTicketStreamBean autoDailyTicketStreamBean = GsonUtil.getBean(response, AutoDailyTicketStreamBean.class);
                    if (autoDailyTicketStreamBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(page == 1) {
                            dataArrayList.clear();
                        }
                        List<AutoDailyTicketStreamBean.DataBean.ListBean> templist = autoDailyTicketStreamBean.getData().getList();
                        if(null == templist) {
                            templist = new ArrayList<>();
                        }

                        dataArrayList.addAll(templist);
                        autoTicketRecordAdapter.replaceData(dataArrayList);

                        if(templist.isEmpty()) {
                            autoTicketRecordAdapter.setEnableLoadMore(false);
                            autoTicketRecordAdapter.loadMoreEnd();
                        }else {
                            autoTicketRecordAdapter.setEnableLoadMore(true);
                            autoTicketRecordAdapter.loadMoreComplete();
                        }

                    } else {
                        RxToast.custom(autoDailyTicketStreamBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }


}
