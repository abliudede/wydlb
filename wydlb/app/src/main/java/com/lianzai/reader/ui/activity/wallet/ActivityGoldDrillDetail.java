package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.UserDailyTicket;
import com.lianzai.reader.bean.UserDailyTicketStreamBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.GoldDrillAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
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
 * 推荐票流水详情
 */
public class ActivityGoldDrillDetail extends BaseActivity implements  BaseQuickAdapter.RequestLoadMoreListener,SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
            TextView tv_options;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    GoldDrillAdapter goldDrillAdapter;
    CustomLoadMoreView customLoadMoreView;
    RxLinearLayoutManager manager;
    List<com.lianzai.reader.bean.UserDailyTicketStreamBean.DataBean.ListBean> dataBeanList = new ArrayList<>();

    View headerView;
    TextView tv_total;
    TextView tv_des_total;
    ImageView iv_img_status;
    TextView tv_available;
    TextView tv_unlocked;
    LinearLayout ly_auto;

    View view_empty;
    private String nexttime;
    private int pageNumber;



    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityGoldDrillDetail.class);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_gold_drill;
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

        tv_options.setVisibility(View.GONE);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        headerView=getLayoutInflater().inflate(R.layout.header_gold_drill,null);
        tv_total=(TextView)headerView.findViewById(R.id.tv_total);
        //字体设置
        Typeface typeface = Typeface.createFromAsset(ActivityGoldDrillDetail.this.getAssets(),"DINMedium.ttf");
        tv_total.setTypeface(typeface);
        tv_available=(TextView)headerView.findViewById(R.id.tv_available);
        tv_unlocked=(TextView)headerView.findViewById(R.id.tv_unlocked);
        view_empty=headerView.findViewById(R.id.view_empty);
        tv_des_total=(TextView) headerView.findViewById(R.id.tv_des_total);
        iv_img_status=(ImageView)headerView.findViewById(R.id.iv_img_status);
        ly_auto=(LinearLayout) headerView.findViewById(R.id.ly_auto);

        ly_auto.setOnClickListener(
                v -> {
                    ActivityAutoTicketManage.startActivity(this,tv_total.getText().toString());
                }
        );


        tv_commom_title.setText("我的推荐票");
        tv_des_total.setText("今日剩余推荐票数");


        goldDrillAdapter= new GoldDrillAdapter(dataBeanList,this);
        customLoadMoreView = new CustomLoadMoreView();
        goldDrillAdapter.setLoadMoreView(customLoadMoreView);
        goldDrillAdapter.setOnLoadMoreListener(this, rv_list);
        goldDrillAdapter.addHeaderView(headerView,0);

        manager=new RxLinearLayoutManager(ActivityGoldDrillDetail.this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_list.setAdapter(goldDrillAdapter);

        onRefresh();
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
    public void onRefresh() {
        //请求金钻详情
        nexttime = "";
        pageNumber = 0;
        getUserDailyTicket();
        requestData();
    }

    private void getUserDailyTicket(){

            OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/dailyTicket/getUserDailyTicket", new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    try{
                        showNetworkDialog();
                        showOrCloseRefresh(false);
                    }catch (Exception ee){
                    }
                }
                @Override
                public void onResponse(String response) {
                    try {
                        showOrCloseRefresh(false);
                        UserDailyTicket userDailyTicket = GsonUtil.getBean(response, UserDailyTicket.class);
                        if (userDailyTicket.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            tv_total.setText(String.valueOf(userDailyTicket.getData().getAmt()));
                            tv_available.setText("已投 "  + String.valueOf(userDailyTicket.getData().getUseAmt()));
                            tv_unlocked.setText("总获得 "  + String.valueOf(userDailyTicket.getData().getTotalAmt()));
                        } else {
                            RxToast.custom(userDailyTicket.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                        }
                    } catch (Exception e) {
                        RxLogTool.e("getBookInfo e:" + e.getMessage());
                    }
                }
            });
    }

    public void requestData(){
        //请求金钻详情
        Map<String, String> map = new HashMap<>();
        map.put("nextTime", nexttime);
        map.put("pageSize", "10");
        map.put("pageNumber", String.valueOf(pageNumber));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/dailyTicket/getUserDailyTicketStream",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/dailyTicket/getUserDailyTicketStream").show();
                    goldDrillAdapter.loadMoreFail();
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    UserDailyTicketStreamBean userDailyTicketStreamBean = GsonUtil.getBean(response, UserDailyTicketStreamBean.class);
                    if (userDailyTicketStreamBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (pageNumber == 0) {
                            dataBeanList.clear();
                        }

                        List<com.lianzai.reader.bean.UserDailyTicketStreamBean.DataBean.ListBean> templist = userDailyTicketStreamBean.getData().getList();
                        if(null == templist) {
                            templist = new ArrayList<>();
                        }

                        dataBeanList.addAll(templist);
                        goldDrillAdapter.replaceData(dataBeanList);
                            if (dataBeanList.size()>0){
                                view_empty.setVisibility(View.GONE);
                                nexttime = dataBeanList.get(dataBeanList.size() - 1).getCreateTime();
                            }else{
                                view_empty.setVisibility(View.VISIBLE);
                            }

                            if(templist.isEmpty()) {
                                goldDrillAdapter.setEnableLoadMore(false);
                                goldDrillAdapter.loadMoreEnd();
                            }else {
                                goldDrillAdapter.setEnableLoadMore(true);
                                goldDrillAdapter.loadMoreComplete();
                            }

                    } else {
                        RxToast.custom(userDailyTicketStreamBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }

    private void showOrCloseRefresh(boolean isShow) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(isShow);
                } catch (Exception e) {
                    RxLogTool.e("showOrCloseRefresh:" + e.getMessage());
                }
            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }



    @Override
    public void onLoadMoreRequested() {
            pageNumber++;
            requestData();
    }
}
