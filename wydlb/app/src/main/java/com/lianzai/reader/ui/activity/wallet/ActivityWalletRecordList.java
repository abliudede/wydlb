package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.collection.ArrayMap;
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
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.adapter.WalletTransactionByTypeAdapter;
import com.lianzai.reader.ui.contract.WalletDetailListContract;
import com.lianzai.reader.ui.presenter.WalletDetailListPresenter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 明细列表
 */
public class ActivityWalletRecordList extends BaseActivity implements  BaseQuickAdapter.RequestLoadMoreListener,WalletDetailListContract.View,SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
            TextView tv_options;

    WalletTransactionByTypeAdapter walletTransactionAdapter;


    @Inject
    WalletDetailListPresenter walletDetailListPresenter;


    CustomLoadMoreView customLoadMoreView;

    RxLinearLayoutManager manager;

    String mAccountType= Constant.WalletAccoutType.READ_COIN;


    List<WalletDetailListResponse.DataBean.ListBean>dataBeanList=new ArrayList<>();

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    int pageNo=1;
    boolean isLoadMore = false;
    boolean isError = false;

    String detailType;

    long nextTime=0;

    public static void startActivity(Context context,String accountType,String detailType){
        Bundle bundle=new Bundle();
        bundle.putString("accountType",accountType);
        bundle.putString("detailType",detailType);
        RxActivityTool.skipActivity(context,ActivityWalletRecordList.class,bundle);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_record_list;
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
        walletDetailListPresenter.attachView(this);
        tv_commom_title.setText("明细");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        mAccountType=getIntent().getExtras().getString("accountType");
        detailType=getIntent().getExtras().getString("detailType");

        walletTransactionAdapter= new WalletTransactionByTypeAdapter(dataBeanList,this);
        walletTransactionAdapter.setOnLoadMoreListener(this,rv_list);

        customLoadMoreView=new CustomLoadMoreView();
        walletTransactionAdapter.setLoadMoreView(customLoadMoreView);


        manager=new RxLinearLayoutManager(ActivityWalletRecordList.this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_list.setAdapter(walletTransactionAdapter);

        walletTransactionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                WalletDetailListResponse.DataBean.ListBean bean=walletTransactionAdapter.getData().get(i);
                Intent intent = new Intent(ActivityWalletRecordList.this, ActivityWebView.class);
                intent.putExtra(ActivityWebView.PARAM_URL, Constant.H5_BASE_URL+"/new-wallet/#/tradingParticulars/"+bean.getId()+"/"+bean.getInOutType());
                intent.putExtra("hiddenActionBar",false);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    mSwipeRefreshLayout.setRefreshing(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
                onRefresh();
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }

    private void requestListData(boolean isRefresh){
        ArrayMap<String,Object>arrayMap=new ArrayMap<>();
        if (isRefresh) {
            pageNo = 1;
            isLoadMore = false;
        } else {
            pageNo++;
            isLoadMore = true;

            arrayMap.put("nextTime",nextTime);
        }

        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            arrayMap.put("balanceType","3");

        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){
            arrayMap.put("balanceType","4");
        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            arrayMap.put("balanceType","1");
        }

        if (!TextUtils.isEmpty(detailType)){
            arrayMap.put("detailType",detailType);
        }

        arrayMap.put("pageSize",10);
        arrayMap.put("pageNumber",pageNo);

        walletDetailListPresenter.getBalancePage(arrayMap);
    }


    @Override
    public void gc() {
    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }

    @Override
    public void onLoadMoreRequested() {
        if (walletTransactionAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
            RxLogTool.e("onLoadMoreRequested---不足一页");
            walletTransactionAdapter.loadMoreEnd();
        } else {
            if (!isError) {
                requestListData(false);
            } else {
                isError = true;
                walletTransactionAdapter.loadMoreFail();
            }
        }
    }


    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }

    @Override
    public void getBalancePageSuccess(WalletDetailListResponse bean) {
        if (bean.getData().getList().size()>0){
            nextTime=bean.getData().getList().get(0).getNextTime();
            RxLogTool.e("nextTime:"+nextTime);
        }
        if (!isLoadMore) {
            try{
                mSwipeRefreshLayout.setRefreshing(false);
            }catch (Exception e){
                e.printStackTrace();
            }
            dataBeanList.clear();
            dataBeanList.addAll(bean.getData().getList());
            if (dataBeanList.size() > 0) {
                walletTransactionAdapter.replaceData(dataBeanList);
                walletTransactionAdapter.setEnableLoadMore(true);
                walletTransactionAdapter.loadMoreComplete();
            } else {
                dataBeanList.clear();
                walletTransactionAdapter.replaceData(dataBeanList);
                walletTransactionAdapter.setEmptyView(R.layout.view_records_empty);
            }
        } else {
            if (bean.getData().getList().size() > 0) {
                dataBeanList.addAll(bean.getData().getList());
                walletTransactionAdapter.setNewData(dataBeanList);
            } else {
                walletTransactionAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onRefresh() {
        requestListData(true);

    }


}
