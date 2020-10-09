package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.HotRankingListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.adapter.HotRankListListAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 用作排行榜的书单列表
 */
public class ActivityBookListForRank extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    HotRankListListAdapter hotRankListListAdapter;

    List<HotRankingListBean.DataBean>  dataSource = new ArrayList<>();

    @Bind(R.id.tv_commom_title)
    TextView tv_common_title;


    public static void startActivity(Activity activity) {
        RxActivityTool.skipActivity(activity, ActivityBookListForRank.class);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_book_list;
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

        tv_common_title.setText("排行榜");

        hotRankListListAdapter=new HotRankListListAdapter(dataSource,this);

        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(this);
        recyclerView.setLayoutManager(rxLinearLayoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(hotRankListListAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        hotRankListListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityBookListDetail.startActivity(ActivityBookListForRank.this,String.valueOf(hotRankListListAdapter.getData().get(position).getBooklistId()));
            }
        });

        onRefresh();
    }

    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }



    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @Override
    public void onRefresh() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showSwipeRefresh(true);
                bookListRequest();
                hotRankListListAdapter.setEnableLoadMore(true);//可以加载更多
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }


    private void showSwipeRefresh(boolean isShow){
        try{
            if (null!=mSwipeRefreshLayout){
                mSwipeRefreshLayout.setRefreshing(isShow);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void bookListRequest(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/hotRankingList", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                showSwipeRefresh(false);
            }

            @Override
            public void onResponse(String response) {
                try{
                    showSwipeRefresh(false);
                    HotRankingListBean hotRankingListBean= GsonUtil.getBean(response,HotRankingListBean.class);
                    if (hotRankingListBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

                            dataSource.clear();
                            dataSource.addAll(hotRankingListBean.getData());
                            hotRankListListAdapter.replaceData(dataSource);
                            if(dataSource.isEmpty()){
                                hotRankListListAdapter.setEmptyView(R.layout.view_book_list_empty,recyclerView);
                            }
                    }else{//加载失败
                        RxToast.custom(hotRankingListBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}