package com.lianzai.reader.ui.activity.UrlIdentification;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.model.local.ReadTimeRepository;
import com.lianzai.reader.ui.adapter.ReadTimeAdapter;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 圈子人员列表
 */

public class ActivityReadTimeShow extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.iv_more)
    ImageView iv_more;



    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    ReadTimeAdapter readTimeAdapter;
    List<ReadTimeBean> readTimeBeans = new ArrayList<>();
    RxLinearLayoutManager manager;



    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Activity activity) {
        RxActivityTool.skipActivity(activity, ActivityReadTimeShow.class);
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

        tv_commom_title.setText("阅读时长记录");
        iv_more.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        readTimeAdapter= new ReadTimeAdapter(readTimeBeans,this);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityReadTimeShow.this);
        recycler_view.setLayoutManager(manager);
//        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(readTimeAdapter);


        readTimeAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


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

    @OnClick(R.id.iv_more)
    void iv_moreClick() {
        ReadTimeRepository.getInstance().deleteReadTime();
        RxToast.custom("删除成功").show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    onRefresh();
                }catch (Exception e){
                }
            }
        },100);
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
        AccountTokenBean accountTokenBean = RxLoginTool.getLoginAccountToken();
        if (null != accountTokenBean) {
            String uid = String.valueOf(accountTokenBean.getData().getId());
            readTimeBeans = ReadTimeRepository.getInstance().getReadTimeList(uid);
            readTimeAdapter.replaceData(readTimeBeans);

            long total = 0;
            int size = readTimeBeans.size();
            for(int i = 0; i < size; i ++){
                ReadTimeBean item = readTimeBeans.get(i);
                long itemTime = (item.getEndTime() - item.getStartTime())/1000;
                //数据最大40秒
                if(itemTime > 40){
                    itemTime = 40;
                }
                 total  +=  itemTime;
            }

            tv_commom_title.setText("总时长：" + String.valueOf(total));
            showOrCloseRefresh(false);
        }
    }


}
