package com.lianzai.reader.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.MyCommentDataBean;
import com.lianzai.reader.bean.MyNoticeDataBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostFloor;
import com.lianzai.reader.ui.adapter.MyNoticeItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 我的评论fragment
 */
public class MyCommentFragment extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recyclerView)
    RecyclerView rv_list;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    CustomLoadMoreView customLoadMoreView;

    MyNoticeItemAdapter myNoticeItemAdapter;

    int pageNum=1;
    int pageSize=10;

    List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> mdatalist=new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        myNoticeItemAdapter=new MyNoticeItemAdapter(mdatalist);

        customLoadMoreView = new CustomLoadMoreView();
        myNoticeItemAdapter.setLoadMoreView(customLoadMoreView);
        myNoticeItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                circleDynamicRequest(false);
            }
        }, rv_list);

        myNoticeItemAdapter.setContentClickListener(new MyNoticeItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(getActivity(), String.valueOf(mdatalist.get(pos).getComUserId()));
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
                    MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean bean = mdatalist.get(pos);
//                    1评论/2回复
                    if(bean.getType() == 1){
                        ActivityPostDetail.startActivity(getActivity(),String.valueOf(bean.getPostId()),String.valueOf(bean.getId()));
                    }else {
                        ActivityPostFloor.startActivity(getActivity(),String.valueOf(bean.getCommentId()),String.valueOf(bean.getId()),String.valueOf(bean.getPostId()),String.valueOf(bean.getPlatformId()));
                    }
                }catch (Exception e){
                }
            }
        });
        RxLinearLayoutManager manager = new RxLinearLayoutManager(getContext());
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new RxRecyclerViewDividerTool(0,0 ,0,RxImageTool.dip2px(1)));
        rv_list.setAdapter(myNoticeItemAdapter);

        circleDynamicRequest(true);
        myNoticeItemAdapter.setEmptyView(R.layout.view_alluse_empty,rv_list);
    }


    @Override
    public void fetchData() {
    }

    @Override
    public void onRefresh() {
        circleDynamicRequest(true);
    }


    private void showSwipeRefresh(boolean isShow){
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if (null!=mSwipeRefreshLayout){
                        mSwipeRefreshLayout.setRefreshing(isShow);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }


    public void circleDynamicRequest(boolean isRefresh){
        if (isRefresh){
            pageNum=1;
        }
        Map<String, String> map=new HashMap<>();
        map.put("pageNumber",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/userComment", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/userComment").show();
                    myNoticeItemAdapter.loadMoreFail();
                    showSwipeRefresh(false);
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    showSwipeRefresh(false);
                    MyCommentDataBean myCommentDataBean = GsonUtil.getBean(response, MyCommentDataBean.class);
                    if (myCommentDataBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(isRefresh){
                            mdatalist.clear();
                        }
                        List<MyNoticeDataBean.DataBean.UserCommentPageBean.ListBean> listBeans = myCommentDataBean.getData().getList();
                        if (null != listBeans && !listBeans.isEmpty()) {
                            mdatalist.addAll(listBeans);
                            myNoticeItemAdapter.replaceData(mdatalist);
                            myNoticeItemAdapter.setEnableLoadMore(true);
                            myNoticeItemAdapter.loadMoreComplete();
                        } else {
                            myNoticeItemAdapter.loadMoreEnd();
                        }
                    }else {
                        myNoticeItemAdapter.loadMoreEnd();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
