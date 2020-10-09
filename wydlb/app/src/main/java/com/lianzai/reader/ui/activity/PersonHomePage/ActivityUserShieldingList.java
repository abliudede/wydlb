package com.lianzai.reader.ui.activity.PersonHomePage;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.UserShieldingBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.UserShieldingItemAdapter;
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
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/07/23
 * 黑名单列表
 */

public class ActivityUserShieldingList extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;

    UserShieldingItemAdapter userShieldingItemAdapter;
    List<UserShieldingBean.DataBean.ListBean> personArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;
    private int pageSize = 10;
    private int page = 1;
    private CustomLoadMoreView customLoadMoreView;

    RxDialogSureCancelNew rxDialogSureCancelNew;


    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }


    public static void startActivity(Activity activity) {
        RxActivityTool.skipActivity(activity, ActivityUserShieldingList.class);
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

        tv_commom_title.setText("黑名单");

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        userShieldingItemAdapter= new UserShieldingItemAdapter(personArrayList,this);
        customLoadMoreView = new CustomLoadMoreView();
        userShieldingItemAdapter.setLoadMoreView(customLoadMoreView);
        userShieldingItemAdapter.setOnLoadMoreListener(this,recycler_view);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityUserShieldingList.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(userShieldingItemAdapter);

        userShieldingItemAdapter.setOnPersonClickListener(new UserShieldingItemAdapter.OnPersonClickListener() {
            @Override
            public void attentionClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        UserShieldingBean.DataBean.ListBean temp = personArrayList.get(pos);
                       //取消拉黑
                        rxDialogSureCancelNew.getSureView().setOnClickListener(
                                v -> {
                                    //调用取消拉黑接口之后刷新
                                    cancelShielding(String.valueOf(temp.getShieldingUserId()));
                                    rxDialogSureCancelNew.dismiss();
                                }
                        );
                        rxDialogSureCancelNew.show();
                    }
                }
            }

            @Override
            public void avatorClick(int pos) {
                if(null != personArrayList && !personArrayList.isEmpty()){
                    if(pos < personArrayList.size()){
                        UserShieldingBean.DataBean.ListBean temp = personArrayList.get(pos);
                        PerSonHomePageActivity.startActivity(ActivityUserShieldingList.this,String.valueOf(temp.getShieldingUserId()));
                    }
                }
            }
        });

        userShieldingItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);


        rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        rxDialogSureCancelNew.getTitleView().setText("解除黑名单");
        rxDialogSureCancelNew.getContentView().setText("确定要将该用户从黑名单内移除");
        rxDialogSureCancelNew.getSureView().setText("确定");
        rxDialogSureCancelNew.getCancelView().setText("取消");


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
        page=1;
        userShieldingItemAdapter.setEnableLoadMore(true);
        userShieldingList();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        userShieldingList();
    }


    /**
     * 取消拉黑用户
     */
    private void cancelShielding (String userId){
        HashMap map=new HashMap();
        map.put("shieldingUserId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/circles/cancelShielding",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("取消拉黑成功").show();
                        onRefresh();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 黑名单列表
     */
    private void userShieldingList(){
        HashMap map=new HashMap();
        map.put("pageNum",String.valueOf(page));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/userShieldingList",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/userShieldingList").show();
                    userShieldingItemAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    UserShieldingBean baseBean= GsonUtil.getBean(response,UserShieldingBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        showOrCloseRefresh(false);
                        if(page == 1){
                            personArrayList.clear();
                            personArrayList.addAll(baseBean.getData().getList());
                            userShieldingItemAdapter.replaceData(personArrayList);
                        }else{
                            personArrayList.addAll(baseBean.getData().getList());
                            userShieldingItemAdapter.replaceData(personArrayList);
                        }

                        if(personArrayList.isEmpty()){
                            userShieldingItemAdapter.setEmptyView(R.layout.view_records_empty,recycler_view);
                        }
                        //是否允许加载更多
                        if (baseBean.getData().getList().size()<pageSize){//最后一页
                            userShieldingItemAdapter.loadMoreEnd();
                        }else{
                            userShieldingItemAdapter.loadMoreComplete();
                        }
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}
