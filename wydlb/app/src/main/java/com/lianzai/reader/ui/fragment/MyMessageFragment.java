package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.collection.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.MessageBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityMessageCenter;
import com.lianzai.reader.ui.activity.ActivityNoticeDetail;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.adapter.MessageItemAdapter;
import com.lianzai.reader.ui.contract.MessageListContract;
import com.lianzai.reader.ui.contract.UpdateMessageStatusContract;
import com.lianzai.reader.ui.presenter.MessageListPresenter;
import com.lianzai.reader.ui.presenter.UpdateMessageStatusPresenter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by lrz on 2017/10/13.
 * 我的消息
 */

public class MyMessageFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener,MessageListContract.View,SwipeRefreshLayout.OnRefreshListener, UpdateMessageStatusContract.View{
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    boolean isLoadMore=false;
    boolean isError=false;

    CustomLoadMoreView customLoadMoreView;

    MessageItemAdapter messageAndNoticeAdapter;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    int page=1;

    int type=0;//0我的消息  1系统公告

    ActivityMessageCenter activityMessageCenter;

    @Inject
    MessageListPresenter messageListPresenter;

    List<MessageBean.DataBeanX.DataBean> list = new ArrayList<>();

    @Inject
    UpdateMessageStatusPresenter updateMessageStatusPresenter;

    AccountDetailBean accountDetailBean;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (null!=activity){
            activityMessageCenter=(ActivityMessageCenter)activity;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        messageListPresenter.attachView(this);
        updateMessageStatusPresenter.attachView(this);

        type=getArguments().getInt("type",0);

        messageAndNoticeAdapter= new MessageItemAdapter(list);
        messageAndNoticeAdapter.setOnLoadMoreListener(this,recyclerView);

        customLoadMoreView=new CustomLoadMoreView();
        messageAndNoticeAdapter.setLoadMoreView(customLoadMoreView);

        RxLinearLayoutManager manager=new RxLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(messageAndNoticeAdapter);

        messageAndNoticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (!messageAndNoticeAdapter.getData().get(i).isRead()){//未读才更新状态
                    messageAndNoticeAdapter.getData().get(i).setDeleted_at(String.valueOf(System.currentTimeMillis()));
                    messageAndNoticeAdapter.notifyItemChanged(i);
                    updateMessageStatusRequest(false,messageAndNoticeAdapter.getData().get(i).getId());
                }
                if (messageAndNoticeAdapter.getData().get(i).getType()==Constant.MessageType.NOTICE_TYPE&& !TextUtils.isEmpty(messageAndNoticeAdapter.getData().get(i).getUrl())){
                    String language= RxSharedPreferencesUtil.getInstance().getBoolean(Constant.CURRENT_LANGUAGE_KEY,true)?"zh":"en";
                    StringBuffer sb=new StringBuffer(messageAndNoticeAdapter.getData().get(i).getUrl());
                    if (null!=accountDetailBean){
                        sb.append("?uid="+accountDetailBean.getData().getUid());
                        sb.append("&platform=1&language="+language);
                    }else{
                        sb.append("?platform=1&language="+language);
                    }
                    //通知中心
                    ActivityWebView.startActivity(getActivity(),sb.toString());
                }else{
                    ActivityNoticeDetail.startActivity(getContext(),messageAndNoticeAdapter.getData().get(i));
                }

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

    }

    private  void listMessageRequest(boolean isRefresh){
        if (isRefresh){
            page=1;
            isLoadMore=false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null!=mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(true);
                }
            },100);
        }else{
            page++;
            isLoadMore=true;
        }

        ArrayMap map=new ArrayMap();
        map.put("project","1");
        map.put("type",type==Constant.MessageType.NOTICE_TYPE?"public":"private");
        map.put("page",String.valueOf(page));
        messageListPresenter.listMessage(map);

    }

    @Override
    public void onRefresh() {
        listMessageRequest(true);
    }

    private void updateMessageStatusRequest(boolean updateAll,int id){
        ArrayMap map=new ArrayMap();
        if (updateAll){
            map.put("flag","2");
        }else{
            map.put("flag","1");
            map.put("id",String.valueOf(id));
        }
        map.put("project","1");
        updateMessageStatusPresenter.updateMessageStatus(map,type==Constant.MessageType.NOTICE_TYPE?false:true);
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        if (messageAndNoticeAdapter.getData().size()< Constant.PageSize.MaxPageSize){
            RxLogTool.e("onLoadMoreRequested---不足一页");
            messageAndNoticeAdapter.loadMoreEnd();
        }else{
            if (!isError){
                listMessageRequest(false);
            }else{
                isError=true;
                messageAndNoticeAdapter.loadMoreFail();
            }
        }
        mSwipeRefreshLayout.setEnabled(true);
    }

    @Override
    public void messageListSuccess(MessageBean bean) {
        if (!isLoadMore){
            try{
                mSwipeRefreshLayout.setRefreshing(false);
            }catch (Exception e){
                e.printStackTrace();
            }
            list.clear();
            list.addAll(bean.getData().getData());
            if (list.size()>0){
                messageAndNoticeAdapter.replaceData(list);

                messageAndNoticeAdapter.setEnableLoadMore(true);
                messageAndNoticeAdapter.loadMoreComplete();

            }else{
                messageAndNoticeAdapter.replaceData(list);
                messageAndNoticeAdapter.setEmptyView(R.layout.view_message_empty);
                TextView tv_empty=(TextView) messageAndNoticeAdapter.getEmptyView().findViewById(R.id.tv_empty);
                if (type==Constant.MessageType.NOTICE_TYPE){
                    tv_empty.setText(getResources().getString(R.string.empty_notice_record));
                }else{
                    tv_empty.setText(getResources().getString(R.string.empty_message_record));
                }
            }
        }else{
            if (bean.getData().getData().size()>0){
                list.addAll(bean.getData().getData());
                messageAndNoticeAdapter.setNewData(list);
            }else{
                messageAndNoticeAdapter.loadMoreEnd();
            }
        }

    }

    @Override
    public void showError(String message) {
        try{
            mSwipeRefreshLayout.setRefreshing(false);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (isLoadMore){
            messageAndNoticeAdapter.loadMoreFail();
        }else{
            if (RxNetTool.showNetworkUnavailable(message)){
                messageAndNoticeAdapter.setEmptyView(R.layout.view_network_unavailable);
                TextView tv_try_again=(TextView) messageAndNoticeAdapter.getEmptyView().findViewById(R.id.tv_try_again);
                tv_try_again.setOnClickListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        onRefresh();
                    }
                });
            }else{
                RxToast.custom(message).show();
            }
        }


    }

    @Override
    public void complete(String message) {
        try{
            if (null!=mSwipeRefreshLayout)
                mSwipeRefreshLayout.setRefreshing(false);
            if (isLoadMore){
                messageAndNoticeAdapter.loadMoreFail();
            }else{
                messageAndNoticeAdapter.setEmptyView(R.layout.view_network_unavailable);
                TextView tv_try_again=(TextView) messageAndNoticeAdapter.getEmptyView().findViewById(R.id.tv_try_again);
                TextView tv_empty=(TextView) messageAndNoticeAdapter.getEmptyView().findViewById(R.id.tv_empty);
                tv_empty.setText(getResources().getString(R.string.dialog_server_error));
                tv_try_again.setOnClickListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        onRefresh();
                    }
                });
            }
        }catch (Exception e){

        }
    }

    @Override
    public void updateMessageStatusSuccess(BaseBean bean) {

    }

    @Override
    public void updateMessageStatusFailed(BaseBean bean) {

    }

    @Override
    public void fetchData() {
        RxLogTool.e("MyMessageFragment ---fetchData--"+type);
        mSwipeRefreshLayout.setEnabled(true);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();

            }
        },getResources().getInteger(R.integer.refresh_delay));
    }
}
