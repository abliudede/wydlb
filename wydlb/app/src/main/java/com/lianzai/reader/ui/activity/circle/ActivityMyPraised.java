package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.MyNoticeDataBean;
import com.lianzai.reader.bean.MyPraisedBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityAttentionPersonList;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.MyNoticeItemAdapter;
import com.lianzai.reader.ui.adapter.MyPraisedItemAdapter;
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
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 收到的赞
 */

public class ActivityMyPraised extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    CustomLoadMoreView customLoadMoreView;
    MyPraisedItemAdapter myPraisedItemAdapter;
    List<MyPraisedBean.DataBean.ListBean> arrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageNumber;
    private int pageSize = 10;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Context context){
        RxActivityTool.skipActivity(context,ActivityMyPraised.class);
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
        tv_commom_title.setText("收到的赞");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        myPraisedItemAdapter= new MyPraisedItemAdapter(arrayList);

        customLoadMoreView = new CustomLoadMoreView();
        myPraisedItemAdapter.setLoadMoreView(customLoadMoreView);
        myPraisedItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNumber++;
                myNoticeRequest(false);
            }
        }, recycler_view);


        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityMyPraised.this);
        recycler_view.setLayoutManager(manager);
//        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(myPraisedItemAdapter);

        myPraisedItemAdapter.setContentClickListener(new MyPraisedItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(ActivityMyPraised.this, String.valueOf(arrayList.get(pos).getUserId()));
                } catch (Exception e) {
                }
            }

            @Override
            public void praiseClick(View v, int pos) {

            }

            @Override
            public void replyClick(View v, int pos) {

            }

            @Override
            public void contentClick(View v, int pos) {
                try{
                    MyPraisedBean.DataBean.ListBean bean = arrayList.get(pos);
//                    1评论/2回复
                    if(bean.getLikeType() == 1){
                        ActivityPostDetail.startActivity(ActivityMyPraised.this,String.valueOf(bean.getPostId()));
                    }else if(bean.getLikeType() == 2) {
                        ActivityPostDetail.startActivity(ActivityMyPraised.this,String.valueOf(bean.getPostId()),String.valueOf(bean.getObjectId()));
                    }else {
                        ActivityPostFloor.startActivity(ActivityMyPraised.this,String.valueOf(bean.getSuperiorId()),String.valueOf(bean.getObjectId()),String.valueOf(bean.getPostId()),String.valueOf(bean.getPlatformId()));
                    }

                }catch (Exception e){
                }
            }
        });

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
        myNoticeRequest(true);
    }

    public void myNoticeRequest(boolean isRefresh){
        if (isRefresh){
            pageNumber=1;
        }
        Map<String, String> map=new HashMap<>();
        map.put("pageNumber",String.valueOf(pageNumber));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/receivedPraise", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/receivedPraise").show();
                    myPraisedItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    showOrCloseRefresh(false);
                    MyPraisedBean myPraisedBean = GsonUtil.getBean(response, MyPraisedBean.class);
                    if (myPraisedBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh){
                            arrayList.clear();
                        }

                        List<MyPraisedBean.DataBean.ListBean> listBeans = myPraisedBean.getData().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            myPraisedItemAdapter.setEnableLoadMore(true);
                            myPraisedItemAdapter.loadMoreComplete();
                            arrayList.addAll(listBeans);
                            myPraisedItemAdapter.notifyDataSetChanged();
                        } else {
                            myPraisedItemAdapter.loadMoreEnd();
                        }
                    }else{//加载失败
                        RxToast.custom(myPraisedBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}
