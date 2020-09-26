package com.lianzai.reader.ui.activity.PersonHomePage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CirclePersonBean;
import com.lianzai.reader.bean.LuckInfoBean;
import com.lianzai.reader.bean.LuckInfoFollowBean;
import com.lianzai.reader.bean.LuckLaunchBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.AttentionPersonItemAdapter;
import com.lianzai.reader.ui.adapter.FightLuckPersonItemAdapter;
import com.lianzai.reader.ui.contract.FightLuckContract;
import com.lianzai.reader.ui.contract.UserAttentionContract;
import com.lianzai.reader.ui.presenter.FightLuckPresenter;
import com.lianzai.reader.ui.presenter.UserAttentionPresenter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 关注人员列表
 */

public class ActivityAttentionPersonList extends BaseActivity implements UserAttentionContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Inject
    UserAttentionPresenter userAttentionPresenter;

    //类型 1我的关注列表(token必填) 2我的粉丝列表(token必填) 3他的关注列表(otherUserId必填) 4他的粉丝列表(otherUserId必填)
    private int userId;
    private int type;

    AttentionPersonItemAdapter attentionPersonItemAdapter;
    List<UserAttentionBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private int page = 1;
    private CustomLoadMoreView customLoadMoreView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    //类型 1我的关注列表(token必填) 2我的粉丝列表(token必填) 3他的关注列表(otherUserId必填) 4他的粉丝列表(otherUserId必填)
    public static void startActivity(Activity activity, int userId ,int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        bundle.putInt("type", type);
        RxActivityTool.skipActivity(activity, ActivityAttentionPersonList.class, bundle);
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
        userAttentionPresenter.attachView(this);

        userId = getIntent().getExtras().getInt("userId",0);
        type = getIntent().getExtras().getInt("type",0);

        if(type == 0){
            finish();
            return;
        }else if(type == 1){
            tv_commom_title.setText("我关注的人");
        }else if(type == 2){
            tv_commom_title.setText("我的粉丝");
        }else if(type == 3){
            tv_commom_title.setText("TA关注的人");
        }else if(type == 4){
            tv_commom_title.setText("TA的粉丝");
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        attentionPersonItemAdapter= new AttentionPersonItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        attentionPersonItemAdapter.setLoadMoreView(customLoadMoreView);
        attentionPersonItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityAttentionPersonList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(attentionPersonItemAdapter);

        attentionPersonItemAdapter.setOnPersonClickListener(new AttentionPersonItemAdapter.OnPersonClickListener() {
            @Override
            public void attentionClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        UserAttentionBean.DataBean.ListBean temp = personArrayList.get(pos);
                        ArrayMap map=new ArrayMap();
                        map.put("attentionUserId",temp.getUserId());
                        int type;
                        if(temp.getAttentionStatus() == 0) {
                            type = 1;
                        }else {
                            type = 2;
                        }
                        map.put("attentionType", type);
                        userAttentionPresenter.attentionOrUnattention(map,pos,0);
                    }
                }
            }

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        UserAttentionBean.DataBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityAttentionPersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });

        attentionPersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
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
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onRefresh() {
        page=1;
        attentionPersonItemAdapter.setEnableLoadMore(true);

        ArrayMap map=new ArrayMap();
        map.put("pageNum",page);
        map.put("pageSize",pageSize);
        map.put("type",type);
        if(userId != 0)
        map.put("otherUserId",userId);
        userAttentionPresenter.userAttention(map);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;

        ArrayMap map=new ArrayMap();
        map.put("pageNum",page);
        map.put("pageSize",pageSize);
        map.put("type",type);
        if(userId != 0)
        map.put("otherUserId",userId);
        userAttentionPresenter.userAttention(map);
    }

    @Override
    public void userAttentionSuccess(UserAttentionBean bean) {
        showOrCloseRefresh(false);
        if(page == 1){
            personArrayList.clear();
            personArrayList.addAll( bean.getData().getList());
            attentionPersonItemAdapter.replaceData(personArrayList);
        }else{
            personArrayList.addAll( bean.getData().getList());
            attentionPersonItemAdapter.replaceData(personArrayList);
        }

        if(personArrayList.isEmpty()){
            attentionPersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
        }
        //是否允许加载更多
        if (bean.getData().getList().size()<pageSize){//最后一页
            attentionPersonItemAdapter.loadMoreEnd();
        }else{
            attentionPersonItemAdapter.loadMoreComplete();
        }
    }

    @Override
    public void attentionOrUnattentionSuccess(AttentionBean bean, int pos,int type) {
        personArrayList.get(pos).setAttentionStatus(bean.getData().getAttentionStatus());
        //粉丝数增减
        personArrayList.get(pos).setFansSum(bean.getData().getFansSum());

        attentionPersonItemAdapter.notifyItemChanged(pos);
        //刷新我的fragment
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
    }

    @Override
    public void circlePersonSuccess(CirclePersonBean bean) {

    }


}
