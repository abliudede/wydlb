package com.lianzai.reader.ui.activity.UrlIdentification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.adapter.TeamItemAdapter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 已加入的群列表
 */

public class ActivityTeamChoose extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.parent_view)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;


    TeamItemAdapter teamItemAdapter;
    List<Team>  teamArrayList = new ArrayList<>();
    RxLinearLayoutManager manager;

    private String titleShare = "";
    private String desShare = "";
    private String imgShare = "";
    private String url = "";
    private int bid;

    RxDialogSureCancelNew rxDialogSureCancelNew;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_personlist;
    }

    public static void startActivity(Context context,  String tit, String des, String img, int bid, String url){
        Intent intent=new Intent(context,ActivityTeamChoose.class);
        intent.putExtra(ActivityWebView.PARAM_TIT,tit);
        intent.putExtra(ActivityWebView.PARAM_DES,des);
        intent.putExtra(ActivityWebView.PARAM_IMG,img);
        intent.putExtra(ActivityWebView.PARAM_BID,bid);
        intent.putExtra("url",url);
        context.startActivity(intent);
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

        try {
            Intent intent = getIntent();
            titleShare = intent.getStringExtra(ActivityWebView.PARAM_TIT);
            desShare = intent.getStringExtra(ActivityWebView.PARAM_DES);
            imgShare = intent.getStringExtra(ActivityWebView.PARAM_IMG);
            bid = intent.getIntExtra(ActivityWebView.PARAM_BID,0);
            url = intent.getStringExtra("url");
        }catch (Exception e){
        }

        rxDialogSureCancelNew=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("提示");
        rxDialogSureCancelNew.setContent("是否发送此链接?");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });



        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        teamItemAdapter= new TeamItemAdapter(teamArrayList,this);
        //纵向列表设置
        manager=new RxLinearLayoutManager(ActivityTeamChoose.this);
        recycler_view.setLayoutManager(manager);
        recycler_view.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recycler_view.setAdapter(teamItemAdapter);

        teamItemAdapter.setOnItemClickListener(
                (adapter, view, position) -> {

                    rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            rxDialogSureCancelNew.dismiss();
                            sendUrlBookMessage(teamArrayList.get(position).getId());
                        }
                    });
                    rxDialogSureCancelNew.show();

                }
        );

    }

    /**
     * 分享url识别书籍
     *
     * @param teamId
     */
    private void sendUrlBookMessage( String teamId) {
        UrlBookBean urlBookBean = new UrlBookBean(bid,imgShare,titleShare,desShare,url);
        IMMessage imMessage = MessageBuilder.createCustomMessage(teamId, SessionTypeEnum.Team, urlBookBean);
        sendMessage(imMessage);
    }

    private void sendMessage(IMMessage message) {
        //检查网络是否可用
        if (!RxNetTool.isAvailable()) {
            RxToast.custom("无网络", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
            // send message to server and save to db
            NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    RxToast.custom("发送成功").show();
                    finish();
                }

                @Override
                public void onFailed(int code) {
                    RxToast.custom("发送失败", Constant.ToastType.TOAST_ERROR).show();
                }

                @Override
                public void onException(Throwable exception) {
                    RxToast.custom("发送失败", Constant.ToastType.TOAST_ERROR).show();
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
        teamArrayList.clear();
        teamArrayList.addAll(NIMClient.getService(TeamService.class).queryTeamListBlock());
        teamItemAdapter.replaceData(teamArrayList);
        showOrCloseRefresh(false);
    }
}
