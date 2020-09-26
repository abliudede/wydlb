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
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.CirclePersonBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.CirclePersonItemAdapter;
import com.lianzai.reader.ui.contract.UserAttentionContract;
import com.lianzai.reader.ui.presenter.UserAttentionPresenter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
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
 * 共享版权人员列表
 */

public class ActivityCircleGXBQPersonList extends BaseActivity implements UserAttentionContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Inject
    UserAttentionPresenter userAttentionPresenter;


    CirclePersonItemAdapter circlePersonItemAdapter;
    List<UserAttentionBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private int page = 1;
    private CustomLoadMoreView customLoadMoreView;
    private String bookId;
    private String type;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity, String bookId ) {
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        RxActivityTool.skipActivity(activity, ActivityCircleGXBQPersonList.class, bundle);
    }

    public static void startActivity(Activity activity, String bookId, String type ) {
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        bundle.putString("type", type);

        RxActivityTool.skipActivity(activity, ActivityCircleGXBQPersonList.class, bundle);
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

        bookId = getIntent().getExtras().getString("bookId");

        tv_commom_title.setText("预约的人");

        //“1”代表预约的人，“2”代表众筹的人，“3”代表参与兑换的人
        try{
            type = getIntent().getExtras().getString("type");
            if(!TextUtils.isEmpty(type)){
                if("1".equals(type)){
                    tv_commom_title.setText("预约的人");
                }else if("2".equals(type)){
                    tv_commom_title.setText("众筹的人");
                }else {
                    tv_commom_title.setText("参与兑换的人");
                }
            }
        }catch (Exception e){

        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        circlePersonItemAdapter= new CirclePersonItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        circlePersonItemAdapter.setLoadMoreView(customLoadMoreView);
        circlePersonItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCircleGXBQPersonList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(circlePersonItemAdapter);

        circlePersonItemAdapter.setOnPersonClickListener(new CirclePersonItemAdapter.OnPersonClickListener() {
            @Override
            public void attentionClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCircleGXBQPersonList.this);
                    return;
                }
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
                        PerSonHomePageActivity.startActivity(ActivityCircleGXBQPersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });

        circlePersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
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
        ArrayMap map=new ArrayMap();
        map.put("bookId",bookId);
        map.put("pageNum",page);
        map.put("pageSize",pageSize);
        userAttentionPresenter.gxbqPerson(map);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        ArrayMap map=new ArrayMap();
        map.put("bookId",bookId);
        map.put("pageNum",page);
        map.put("pageSize",pageSize);
        userAttentionPresenter.gxbqPerson(map);
    }

    @Override
    public void userAttentionSuccess(UserAttentionBean bean) {
        try{
            showOrCloseRefresh(false);
            List<UserAttentionBean.DataBean.ListBean> tempList = bean.getData().getList();
            if(null == tempList){
                tempList = new ArrayList<>();
            }
            if(page == 1){
                personArrayList.clear();
                personArrayList.addAll( tempList);
                circlePersonItemAdapter.replaceData(personArrayList);
            }else{
                personArrayList.addAll( tempList);
                circlePersonItemAdapter.replaceData(personArrayList);
            }

            circlePersonItemAdapter.setEnableLoadMore(true);
            if(personArrayList.isEmpty()){
                circlePersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
            }
            //是否允许加载更多
            if (tempList.isEmpty()){//最后一页
                circlePersonItemAdapter.loadMoreEnd();
            }else{
                circlePersonItemAdapter.loadMoreComplete();
            }
        }catch (Exception e){

        }
    }

    @Override
    public void attentionOrUnattentionSuccess(AttentionBean bean, int pos,int type) {
        personArrayList.get(pos).setAttentionStatus(bean.getData().getAttentionStatus());
        //粉丝数增减
        personArrayList.get(pos).setFansSum(bean.getData().getFansSum());

        circlePersonItemAdapter.notifyItemChanged(pos + circlePersonItemAdapter.getHeaderLayoutCount());
        //刷新我的fragment
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
    }

    @Override
    public void circlePersonSuccess(CirclePersonBean bean) {

    }

}
