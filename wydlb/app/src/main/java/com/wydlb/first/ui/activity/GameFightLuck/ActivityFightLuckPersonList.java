package com.wydlb.first.ui.activity.GameFightLuck;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.bean.LuckInfoBean;
import com.wydlb.first.bean.LuckInfoFollowBean;
import com.wydlb.first.bean.LuckLaunchBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.component.DaggerAccountComponent;
import com.wydlb.first.ui.adapter.FightLuckPersonItemAdapter;
import com.wydlb.first.ui.contract.FightLuckContract;
import com.wydlb.first.ui.presenter.FightLuckPresenter;
import com.wydlb.first.utils.DividerItemDecoration;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxLinearLayoutManager;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.CustomLoadMoreView;
import com.wydlb.first.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 拼手气人员列表
 */

public class ActivityFightLuckPersonList extends BaseActivity implements FightLuckContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Inject
    FightLuckPresenter fightLuckPresenter;
    private int gameId;

    FightLuckPersonItemAdapter barItemAdapter;
    List<LuckInfoFollowBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private int page = 1;
    private CustomLoadMoreView customLoadMoreView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity, int gameId) {
        Bundle bundle = new Bundle();
        bundle.putInt("gameId", gameId);
        RxActivityTool.skipActivity(activity, ActivityFightLuckPersonList.class, bundle);
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
        fightLuckPresenter.attachView(this);

        gameId = getIntent().getExtras().getInt("gameId",0);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        barItemAdapter= new FightLuckPersonItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        barItemAdapter.setLoadMoreView(customLoadMoreView);
        barItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityFightLuckPersonList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(barItemAdapter);

        showOrCloseRefresh(true);

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
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }


    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void luckLaunchSuccess(LuckLaunchBean bean) {
        dismissDialog();
    }

    @Override
    public void luckPublishSuccess(BaseBean bean) {
        dismissDialog();
    }

    @Override
    public void luckInfoSuccess(LuckInfoBean bean) {

    }

    @Override
    public void luckFollowSuccess(LuckInfoFollowBean bean) {
        showOrCloseRefresh(false);
        if(page == 1){
            personArrayList.clear();
            personArrayList.addAll( bean.getData().getList());
            barItemAdapter.replaceData(personArrayList);
        }else{
            personArrayList.addAll( bean.getData().getList());
            barItemAdapter.replaceData(personArrayList);
        }

        //是否允许加载更多
        if (bean.getData().getList().size()<pageSize){//最后一页
            barItemAdapter.loadMoreEnd();
        }else{
            barItemAdapter.loadMoreComplete();
        }

    }


    @Override
    public void luckJoinSuccess(BaseBean bean) {

    }

    @Override
    public void onRefresh() {
        page=1;
        barItemAdapter.setEnableLoadMore(true);

        ArrayMap map=new ArrayMap();
        map.put("pageNum",page);
        map.put("pageSize",pageSize);
        map.put("gameId",gameId);
        fightLuckPresenter.luckFollow(map);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;

        ArrayMap map=new ArrayMap();
        map.put("pageNum",page);
        map.put("pageSize",pageSize);
        map.put("gameId",gameId);
        fightLuckPresenter.luckFollow(map);
    }
}
