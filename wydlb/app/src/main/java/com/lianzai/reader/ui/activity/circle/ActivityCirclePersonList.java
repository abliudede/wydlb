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
import android.widget.RelativeLayout;
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
import com.lianzai.reader.ui.adapter.CirclePersonItemAdapter2;
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
 * 圈子人员列表
 */

public class ActivityCirclePersonList extends BaseActivity implements UserAttentionContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Inject
    UserAttentionPresenter userAttentionPresenter;


    CirclePersonItemAdapter2 circlePersonItemAdapter;
    List<CirclePersonBean.DataBean.CircleMemberListBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 12;
    private String nextId = "";
    private CustomLoadMoreView customLoadMoreView;
    private String circleId;
    private int userType = Constant.Role.FANS_ROLE;

    /*头部信息相关*/
    private View headview;
    private RelativeLayout rl_manager;
    private TextView tv_person_count1;
    private RelativeLayout rl_mute;
    private TextView tv_person_count2;
    private View line1;
    private TextView tit_tv1;
    private TextView tit_tv2;
    private RecyclerView rv_grid_recommend1;
    private RecyclerView rv_grid_recommend2;
    private View line2;

    CirclePersonItemAdapter2 circlePersonItemAdapter1;
    List<CirclePersonBean.DataBean.CircleMemberListBean.ListBean> personArrayList1 = new ArrayList<>();
    CirclePersonItemAdapter2 circlePersonItemAdapter2;
    List<CirclePersonBean.DataBean.CircleMemberListBean.ListBean> personArrayList2 = new ArrayList<>();



    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity, String circleId ,int userType) {
        Bundle bundle = new Bundle();
        bundle.putString("circleId", circleId);
        bundle.putInt("userType", userType);
        RxActivityTool.skipActivity(activity, ActivityCirclePersonList.class, bundle);
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

        circleId = getIntent().getExtras().getString("circleId");
        userType = getIntent().getExtras().getInt("userType");

        tv_commom_title.setText("圈子成员");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        circlePersonItemAdapter= new CirclePersonItemAdapter2(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        circlePersonItemAdapter.setLoadMoreView(customLoadMoreView);
        circlePersonItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityCirclePersonList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(circlePersonItemAdapter);
        recycler_view.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                            //此方法常用作判断是否能下拉刷新，来解决滑动冲突
//                            int findFirstCompletelyVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
//                            //如果有滑动冲突--可以用以下方法解决(如果可见位置是position==0的话才能有下拉刷新否则禁掉)
//                            mSwipeRefreshLayout.setEnabled(findFirstCompletelyVisibleItemPosition==0);
                            //在网上还看到一种解决滑动冲突的方法
                            int topPosition =
                                    (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                            mSwipeRefreshLayout.setEnabled(topPosition >= 0);

                    }
                }
        );

        circlePersonItemAdapter.setOnPersonClickListener(new CirclePersonItemAdapter2.OnPersonClickListener() {
            @Override
            public void attentionClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCirclePersonList.this);
                    return;
                }
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp = personArrayList.get(pos);
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
                        CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCirclePersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });

        circlePersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


        //头部显示
        headview = getLayoutInflater().inflate(R.layout.header_circle_person_list, null);
        rl_manager = headview.findViewById(R.id.rl_manager);
        tv_person_count1 = headview.findViewById(R.id.tv_person_count1);
        rl_mute = headview.findViewById(R.id.rl_mute);
        tv_person_count2 = headview.findViewById(R.id.tv_person_count2);
        line1 = headview.findViewById(R.id.line1);
        tit_tv1 = headview.findViewById(R.id.tit_tv1);
        tit_tv2 = headview.findViewById(R.id.tit_tv2);
        rv_grid_recommend1 = headview.findViewById(R.id.rv_grid_recommend1);
        rv_grid_recommend2 = headview.findViewById(R.id.rv_grid_recommend2);
        line2 = headview.findViewById(R.id.line2);

       if(userType >= Constant.Role.FANS_ROLE || userType < Constant.Role.ADMIN_ROLE){
            rl_manager.setVisibility(View.GONE);
            rl_mute.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        }else if(userType >= Constant.Role.MANAGE2_ROLE){
            rl_manager.setVisibility(View.GONE);
            rl_mute.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
        }else {
            rl_manager.setVisibility(View.VISIBLE);
            rl_mute.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
        }

        circlePersonItemAdapter1= new CirclePersonItemAdapter2(personArrayList1,this);
        //纵向列表设置
        RxLinearLayoutManager manager1 = new RxLinearLayoutManager(ActivityCirclePersonList.this);
        rv_grid_recommend1.setLayoutManager(manager1);
//        rv_grid_recommend1.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_grid_recommend1.setAdapter(circlePersonItemAdapter1);

        circlePersonItemAdapter1.setOnPersonClickListener(new CirclePersonItemAdapter2.OnPersonClickListener() {
            @Override
            public void attentionClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCirclePersonList.this);
                    return;
                }
                if(null != personArrayList1 && !personArrayList1.isEmpty()){
                    if(pos < personArrayList1.size()){
                        CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp = personArrayList1.get(pos);
                        ArrayMap map=new ArrayMap();
                        map.put("attentionUserId",temp.getUserId());
                        int type;
                        if(temp.getAttentionStatus() == 0) {
                            type = 1;
                        }else {
                            type = 2;
                        }
                        map.put("attentionType", type);
                        userAttentionPresenter.attentionOrUnattention(map,pos,1);
                    }
                }
            }

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList1 && !personArrayList1.isEmpty()){
                    if(pos < personArrayList1.size()){
                        CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp = personArrayList1.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCirclePersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });


        circlePersonItemAdapter2= new CirclePersonItemAdapter2(personArrayList2,this);
        //纵向列表设置
        RxLinearLayoutManager manager2 = new RxLinearLayoutManager(ActivityCirclePersonList.this);
        rv_grid_recommend2.setLayoutManager(manager2);
//        rv_grid_recommend2.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_grid_recommend2.setAdapter(circlePersonItemAdapter2);

        circlePersonItemAdapter2.setOnPersonClickListener(new CirclePersonItemAdapter2.OnPersonClickListener() {
            @Override
            public void attentionClick(int pos) {
                if(!RxLoginTool.isLogin()){
                    ActivityLoginNew.startActivity(ActivityCirclePersonList.this);
                    return;
                }
                if(null != personArrayList2 && !personArrayList2.isEmpty()){
                    if(pos < personArrayList2.size()){
                        CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp = personArrayList2.get(pos);
                        ArrayMap map=new ArrayMap();
                        map.put("attentionUserId",temp.getUserId());
                        int type;
                        if(temp.getAttentionStatus() == 0) {
                            type = 1;
                        }else {
                            type = 2;
                        }
                        map.put("attentionType", type);
                        userAttentionPresenter.attentionOrUnattention(map,pos,2);
                    }
                }
            }

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList2 && !personArrayList2.isEmpty()){
                    if(pos < personArrayList2.size()){
                        CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp = personArrayList2.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityCirclePersonList.this,String.valueOf(temp.getUserId()));
                    }
                }
            }
        });

        rl_manager.setOnClickListener(
                v -> {
                    ActivityCircleManagerPersonList.startActivity(ActivityCirclePersonList.this,circleId,userType);
                }
        );

        rl_mute.setOnClickListener(
                v -> {
                    ActivityCircleMutePersonList.startActivity(ActivityCirclePersonList.this,circleId,userType);
                }
        );

        circlePersonItemAdapter.addHeaderView(headview);
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
        nextId = "";
        ArrayMap map=new ArrayMap();

        map.put("circleId",circleId);
        if(!TextUtils.isEmpty(nextId)) {
            map.put("nextId", nextId);
        }
        map.put("pageSize",pageSize);
        userAttentionPresenter.circlePerson(map);
    }

    @Override
    public void onLoadMoreRequested() {
        ArrayMap map=new ArrayMap();
        map.put("circleId",circleId);
        if(!TextUtils.isEmpty(nextId)) {
            map.put("nextId", nextId);
        }
        map.put("pageSize",pageSize);
        userAttentionPresenter.circlePerson(map);
    }

    @Override
    public void userAttentionSuccess(UserAttentionBean bean) {

    }

    @Override
    public void attentionOrUnattentionSuccess(AttentionBean bean, int pos,int type) {
        if(type == 0){
            personArrayList.get(pos).setAttentionStatus(bean.getData().getAttentionStatus());
            circlePersonItemAdapter.notifyItemChanged(pos + circlePersonItemAdapter.getHeaderLayoutCount());
            //刷新我的fragment
            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
        }else if(type == 1){
            personArrayList1.get(pos).setAttentionStatus(bean.getData().getAttentionStatus());
            circlePersonItemAdapter1.notifyItemChanged(pos);
            //刷新我的fragment
            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
        }else if(type == 2){
            personArrayList2.get(pos).setAttentionStatus(bean.getData().getAttentionStatus());
            circlePersonItemAdapter2.notifyItemChanged(pos);
            //刷新我的fragment
            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
        }

    }

    @Override
    public void circlePersonSuccess(CirclePersonBean bean) {
        try{
            showOrCloseRefresh(false);
            tv_person_count1.setText(String.valueOf(bean.getData().getAdministratorCount()));
            tv_person_count2.setText(String.valueOf(bean.getData().getBannedCount()));

            List<CirclePersonBean.DataBean.CircleMemberListBean.ListBean> tempList1 = bean.getData().getCircleMemberList().getList();
            if(null == tempList1){
                tempList1 = new ArrayList<>();
            }

            //圈主或者代理圈主
            List<CirclePersonBean.DataBean.CircleMemberListBean.ListBean> tempList2 = new ArrayList<>();
            CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp1 = bean.getData().getCircleMaster();
            if(null != temp1){
                tit_tv1.setText("圈主");
                tempList2.add(temp1);
            }
            CirclePersonBean.DataBean.CircleMemberListBean.ListBean temp2 = bean.getData().getAgentCircleMaster();
            if(null != temp2){
                tit_tv1.setText("代圈主");
                tempList2.add(temp2);
            }

            //管理员
            List<CirclePersonBean.DataBean.CircleMemberListBean.ListBean> tempList3 = bean.getData().getCircleAdministratorList();
            if(null == tempList3){
                tempList3 = new ArrayList<>();
            }

            if(TextUtils.isEmpty(nextId)){
                personArrayList.clear();
                personArrayList1.clear();
                personArrayList2.clear();


                personArrayList.addAll(tempList1);
                circlePersonItemAdapter.replaceData(personArrayList);

                personArrayList1.addAll(tempList2);
                circlePersonItemAdapter1.replaceData(personArrayList1);
                if(personArrayList1.isEmpty()){
                    tit_tv1.setVisibility(View.GONE);
                    rv_grid_recommend1.setVisibility(View.GONE);
                }

                personArrayList2.addAll(tempList3);
                circlePersonItemAdapter2.replaceData(personArrayList2);
                if(personArrayList2.isEmpty()){
                    tit_tv2.setVisibility(View.GONE);
                    rv_grid_recommend2.setVisibility(View.GONE);
                }

                if(personArrayList1.isEmpty() && personArrayList2.isEmpty()){
                    line2.setVisibility(View.GONE);
                }

            }else{
                personArrayList.addAll(tempList1);
                circlePersonItemAdapter.replaceData(personArrayList);
            }



            circlePersonItemAdapter.setEnableLoadMore(true);
            if(personArrayList.isEmpty()){
                circlePersonItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
            }else {
                //取当前最后一项的时间
                nextId = String.valueOf(personArrayList.get(personArrayList.size() - 1).getId());
            }
            //是否允许加载更多
            if ( tempList1.isEmpty()){//最后一页
                circlePersonItemAdapter.loadMoreEnd();
            }else{
                circlePersonItemAdapter.loadMoreComplete();
            }
        }catch (Exception e){

        }
    }


}
