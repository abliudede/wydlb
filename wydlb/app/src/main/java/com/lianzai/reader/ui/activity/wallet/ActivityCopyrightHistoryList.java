package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.WalletDetailListResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.CopyrightTransactionAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 版权资产总历史流水列表
 */

public class ActivityCopyrightHistoryList extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    CopyrightTransactionAdapter walletTransactionAdapter;
    List<WalletDetailListResponse.DataBean.ListBean>dataBeanList=new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;

    private String nextTime = "";

    private CustomLoadMoreView customLoadMoreView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity ) {
        RxActivityTool.skipActivity(activity, ActivityCopyrightHistoryList.class);
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

        tv_commom_title.setText("历史记录");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        walletTransactionAdapter= new CopyrightTransactionAdapter(dataBeanList,this);
        customLoadMoreView = new CustomLoadMoreView();
        walletTransactionAdapter.setLoadMoreView(customLoadMoreView);
        walletTransactionAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCopyrightHistoryList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(walletTransactionAdapter);

        walletTransactionAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);

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
        nextTime = "";
        getUserAllCopyrightBalanceInOrOutDetailResp();
    }

    @Override
    public void onLoadMoreRequested() {
        getUserAllCopyrightBalanceInOrOutDetailResp();
    }


    /**
     * 提交反馈请求
     */
    private void getUserAllCopyrightBalanceInOrOutDetailResp() {
        HashMap map = new HashMap();
        map.put("pageSize", "10");
        if (!TextUtils.isEmpty(nextTime)) {
            map.put("nextTime", nextTime);
        }

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/getUserAllCopyrightBalanceInOrOutDetailResp", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    showOrCloseRefresh(false);
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    WalletDetailListResponse walletDetailListResponse = GsonUtil.getBean(response, WalletDetailListResponse.class);
                        if (walletDetailListResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            if(TextUtils.isEmpty(nextTime)){
                                dataBeanList.clear();
                            }

                            List<WalletDetailListResponse.DataBean.ListBean> listBeans = walletDetailListResponse.getData().getList();
                            if (null != listBeans && !listBeans.isEmpty()) {
                                dataBeanList.addAll(listBeans);
                                walletTransactionAdapter.replaceData(dataBeanList);
                                walletTransactionAdapter.setEnableLoadMore(true);
                                walletTransactionAdapter.loadMoreComplete();
                                //保存最后一条的时间
                                nextTime = String.valueOf(listBeans.get(listBeans.size() - 1).getNextTime());
                            } else {
                                walletTransactionAdapter.replaceData(dataBeanList);
                                walletTransactionAdapter.loadMoreEnd();
                            }

                        } else {
                            walletTransactionAdapter.loadMoreEnd();
                        }
                } catch (Exception e) {
                }
            }
        });
    }



}
