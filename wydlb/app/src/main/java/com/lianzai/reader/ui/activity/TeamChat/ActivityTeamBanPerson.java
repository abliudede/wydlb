package com.lianzai.reader.ui.activity.TeamChat;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.TeamBanPersonBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.adapter.TeamChatBanPersonAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogVotingRules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/*
 *新的成员管理列表。
 * case 1:被禁言成员列表页面
 * case 2:黑名单成员列表页面
 *
 *
 *
 * */
public class ActivityTeamBanPerson extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;

    @Bind(R.id.rv_data)
    RecyclerView rv_data;

    TeamChatBanPersonAdapter teamChatBanPersonAdapter;
    List<TeamBanPersonBean.DataBean> barBeanArrayList1 = new ArrayList<>();

    private String teamId;

    RxDialogVotingRules rxDialogVotingRules;//规则弹窗
    private String type;
    private TeamBanPersonBean teamBanPersonBean;


    public static void startActivity(Context context, String teamId ,String type) {
        Bundle bundle = new Bundle();
        bundle.putString("teamId", teamId);
        bundle.putString("type",type);
        RxActivityTool.skipActivity(context, ActivityTeamBanPerson.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_milepost;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {

        if (RxNetTool.isAvailable()) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != swipe_refresh_layout)
                        swipe_refresh_layout.setRefreshing(true);
                    onRefresh();
                }
            }, getResources().getInteger(R.integer.refresh_delay));
        } else {
//            teamChatBanPersonAdapter.setEmptyView(R.layout.view_disconnect_network,rv_data1);
//            teamChatBanPersonAdapter.setEmptyView(R.layout.view_disconnect_network,rv_data2);
        }
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        teamId = getIntent().getExtras().getString("teamId");
        //禁言："jin" 黑名单："black"
        type = getIntent().getExtras().getString("type");

        if("jin".equals(type)) {
            tv_commom_title.setText("禁言人员列表");
        }else {
            tv_commom_title.setText("黑名单列表");
        }

        rxDialogVotingRules=new RxDialogVotingRules(ActivityTeamBanPerson.this,R.style.OptionDialogStyle);

        teamChatBanPersonAdapter = new TeamChatBanPersonAdapter(barBeanArrayList1, ActivityTeamBanPerson.this);
        teamChatBanPersonAdapter.setMtype(type);
        teamChatBanPersonAdapter.setOnEXItemClickListener(new TeamChatBanPersonAdapter.OnEXItemClickListener() {
                                                              @Override
                                                              public void jiechuClick(int pos) {
                                                                  TeamBanPersonBean.DataBean itemData = teamChatBanPersonAdapter.getData().get(pos);
                                                                  requestUnMuteOrBlack(itemData.getUid(), pos ,type);
                                                              }

                                                              @Override
                                                              public void headClick(int pos) {
                                                                  TeamBanPersonBean.DataBean itemData = teamChatBanPersonAdapter.getData().get(pos);
                                                                  PerSonHomePageActivity.startActivity(ActivityTeamBanPerson.this, String.valueOf(itemData.getUid()));
                                                              }
                                                          }
        );

        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#935E38"));

        LinearLayoutManager manager1 = new LinearLayoutManager(ActivityTeamBanPerson.this);
        rv_data.setAdapter(teamChatBanPersonAdapter);
        rv_data.setLayoutManager(manager1);
    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @OnClick(R.id.iv_options)
    void ruleClick(){
        if("jin".equals(type)){
            rxDialogVotingRules.getTv_title().setText("禁言提示");
            rxDialogVotingRules.getTv_des().setText("1、禁言是什么？\n禁言是为了规范群聊和谐发展而制定的惩罚机制。用户若被禁言，将无法在该群聊内进行发言，直到禁言解除。 \n2、为什么会被禁言？\n（1）违反群聊的群规公告；\n3、禁言如何解除？\n（1）被管理员或以上级别的人员禁言，需自行联系管理员或以上级别人员，申诉解除。\n4、如何避免被禁言？\n（1）请严格遵守群聊群规公告；\n（2）积极维护好自身形象与良好信誉。");
        }else {

            rxDialogVotingRules.getTv_title().setText("黑名单提示");
            rxDialogVotingRules.getTv_des().setText("1、黑名单是什么？\n黑名单是为了规范群聊和谐发展而制定的惩罚机制。用户若被拉入黑名单，将被踢出群聊，并且无法入群，直到被解除黑名单。\n2、为什么会被拉入黑名单？\n（1）严重违反群聊的群规公告；\n（2）用户所发言论或消息引起群聊重大负面影响。\n3、哪些人有权将违规用户拉入黑名单？群主或以上级别的人员。");

        }
        rxDialogVotingRules.show();
    }

    @Override
    public void gc() {

    }

    @Override
    public void onRefresh() {
        requestBanPerson();
    }


    private void requestBanPerson(){
        Map<String, String> hashMap = new HashMap();
        hashMap.put("teamId", teamId);
        if("jin".equals(type)) {
            hashMap.put("type", "1");
        }else{
            hashMap.put("type", "2");
        }

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/teamMember/limit/query",hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null!=swipe_refresh_layout)
                                swipe_refresh_layout.setRefreshing(false);
//                            RxToast.custom("网络错误").show();
                        }
                    },getResources().getInteger(R.integer.refresh_delay));
                }catch (Exception ee){

                }
            }
            @Override
            public void onResponse(String response) {
                try{

                    RxLogTool.e("onResponse:"+response);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null!=swipe_refresh_layout)
                                swipe_refresh_layout.setRefreshing(false);
                        }
                    },getResources().getInteger(R.integer.refresh_delay));

                    teamBanPersonBean = GsonUtil.getBean(response, TeamBanPersonBean.class);
                    if (teamBanPersonBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        barBeanArrayList1.clear();
                        if(null != teamBanPersonBean.getData() ){
                            barBeanArrayList1.addAll(teamBanPersonBean.getData());
                            teamChatBanPersonAdapter.replaceData(barBeanArrayList1);
                        }else{
//                        teamChatBanPersonAdapter.setEmptyView(R.layout.view_alluse_empty,rv_data1);
                        }
                    }else{
                        RxToast.custom(teamBanPersonBean.getMsg()).show();
//                    teamChatBanPersonAdapter.setEmptyView(R.layout.view_disconnect_network,rv_data1);
                    }

                }catch (Exception e){

                }

            }
        });
    }



    //取消禁言操作,取消所有禁言 1永久 2临时 3全部
    private void requestUnMuteOrBlack(int uid, int position,String type) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("uid", String.valueOf(uid));
        hashMap.put("teamId", teamId);
        if("jin".equals(type)){
            hashMap.put("type", "1");
        }else{
            hashMap.put("type", "2");
        }

        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teamMember/limit/remove", hashMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){

                }


            }

            @Override
            public void onResponse(String response) {
                try{
                    dismissDialog();
                    RxLogTool.e("onResponse:" + response);
                    BaseBean basebean = GsonUtil.getBean(response, BaseBean.class);
                    if (basebean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        try {
                            barBeanArrayList1.remove(position);
                            teamChatBanPersonAdapter.replaceData(barBeanArrayList1);
                            if(teamChatBanPersonAdapter.getData().size() == 0){
//                            teamChatBanPersonAdapter.setEmptyView(R.layout.view_alluse_empty,rv_data1);
                            }
                        }catch (Exception e){

                        }
                    } else {
                        onRefresh();
                        RxToast.custom(basebean.getMsg()).show();
                    }
                }catch (Exception e){

                }
            }
        });
    }

}
