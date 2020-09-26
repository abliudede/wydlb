package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.UserLockListResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.WalletTicketLockAdapter;
import com.lianzai.reader.ui.contract.UserLockListContract;
import com.lianzai.reader.ui.presenter.UserLockListPresenter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的钱包-阅券解锁明细
 */
public class ActivityWalletReadLockTicketList extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener,UserLockListContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    WalletTicketLockAdapter walletTicketLockAdapter;

    RxLinearLayoutManager manager;

    CustomLoadMoreView customLoadMoreView;

    @Inject
    UserLockListPresenter userLockListPresenter;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    int pageNo=1;
    boolean isLoadMore = false;
    boolean isError = false;

    List<UserLockListResponse.DataBean.ListBean>listBeans=new ArrayList<>();

    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityWalletReadLockTicketList.class);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_read_ticket_lock_list;
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
        tv_commom_title.setText("阅券解锁明细");
        userLockListPresenter.attachView(this);


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        walletTicketLockAdapter=new WalletTicketLockAdapter(listBeans,this);

        customLoadMoreView=new CustomLoadMoreView();
        walletTicketLockAdapter.setLoadMoreView(customLoadMoreView);

        walletTicketLockAdapter.setOnLoadMoreListener(this,rv_list);

        manager=new RxLinearLayoutManager(ActivityWalletReadLockTicketList.this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_list.setAdapter(walletTicketLockAdapter);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    mSwipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },getResources().getInteger(R.integer.refresh_delay));
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
        if (walletTicketLockAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
            RxLogTool.e("onLoadMoreRequested---不足一页");
            walletTicketLockAdapter.loadMoreEnd();
        } else {
            if (!isError) {
                requestListData(false);
            } else {
                isError = true;
                walletTicketLockAdapter.loadMoreFail();
            }
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void userLockListSuccess(UserLockListResponse bean) {
        if (!isLoadMore) {
            try{
                mSwipeRefreshLayout.setRefreshing(false);
            }catch (Exception e){
                e.printStackTrace();
            }
            listBeans.clear();
            listBeans.addAll(bean.getData().getList());
            if (listBeans.size() > 0) {
                walletTicketLockAdapter.replaceData(listBeans);
                walletTicketLockAdapter.setEnableLoadMore(true);
                walletTicketLockAdapter.loadMoreComplete();
            } else {
                walletTicketLockAdapter.replaceData(listBeans);
                walletTicketLockAdapter.setEmptyView(R.layout.view_records_empty);
            }
        } else {
            if (bean.getData().getList().size() > 0) {
                listBeans.addAll(bean.getData().getList());
                walletTicketLockAdapter.setNewData(listBeans);
            } else {
                walletTicketLockAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void complete(String message) {

    }
    @Override
    public void onRefresh() {
        requestListData(true);
    }

    private void requestListData(boolean isRefresh){
        if (isRefresh) {
            pageNo = 1;
            isLoadMore = false;
        } else {
            pageNo++;
            isLoadMore = true;
        }
        ArrayMap<String,Object> arrayMap=new ArrayMap<>();
        arrayMap.put("balanceType","3");

        arrayMap.put("pageSize",10);
        arrayMap.put("pageNumber",pageNo);

        userLockListPresenter.userLockList(arrayMap);
    }

}
