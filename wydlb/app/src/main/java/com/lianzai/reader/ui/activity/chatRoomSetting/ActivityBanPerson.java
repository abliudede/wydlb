package com.lianzai.reader.ui.activity.chatRoomSetting;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ChatRoomBanPersonList;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.ChatRoomBanPersonAdapter;
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
public class ActivityBanPerson extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;


    @Bind(R.id.rv_data1)
    RecyclerView rv_data1;

    @Bind(R.id.rv_data2)
    RecyclerView rv_data2;

    ChatRoomBanPersonAdapter chatRoomBanPersonAdapter1;
    List<ChatRoomBanPersonList.DataBean> barBeanArrayList1 = new ArrayList<>();

    ChatRoomBanPersonAdapter chatRoomBanPersonAdapter2;
    List<ChatRoomBanPersonList.DataBean> barBeanArrayList2 = new ArrayList<>();

    private String roomId;

    RxDialogVotingRules rxDialogVotingRules;//规则弹窗


    public static void startActivity(Context context, String chatRoomId) {
        Bundle bundle = new Bundle();
        bundle.putString("roomId", chatRoomId);
        RxActivityTool.skipActivity(context, ActivityBanPerson.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chatroom_person_manage;
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
//            chatRoomBanPersonAdapter1.setEmptyView(R.layout.view_disconnect_network,rv_data1);
//            chatRoomBanPersonAdapter2.setEmptyView(R.layout.view_disconnect_network,rv_data2);
        }

    }

    @Override
    public void configViews(Bundle savedInstanceState) {


        roomId = getIntent().getExtras().getString("roomId");

        rxDialogVotingRules=new RxDialogVotingRules(ActivityBanPerson.this,R.style.OptionDialogStyle);


        chatRoomBanPersonAdapter1 = new ChatRoomBanPersonAdapter(barBeanArrayList1, ActivityBanPerson.this);
        chatRoomBanPersonAdapter1.setMtype("jin");
        chatRoomBanPersonAdapter1.setOnEXItemClickListener(new ChatRoomBanPersonAdapter.OnEXItemClickListener() {
                                                              @Override
                                                              public void jiechuClick(int pos) {

                                                                  ChatRoomBanPersonList.DataBean itemData = chatRoomBanPersonAdapter1.getData().get(pos);
//                                                                      if(itemData.isMuted() && !itemData.isTempMuted()){
                                                                          requestUnMute(itemData.getAccid(), pos ,1);
//                                                                      }else if(!itemData.isMuted() && itemData.isTempMuted()){
//                                                                          requestUnMute(itemData.getAccid(), pos ,2);
//                                                                      }else if(itemData.isMuted() && itemData.isTempMuted()){
//                                                                          requestUnMute(itemData.getAccid(), pos ,3);
//                                                                      }else {
//                                                                          return;
//                                                                      }
                                                              }
                                                          }
        );



        chatRoomBanPersonAdapter2 = new ChatRoomBanPersonAdapter(barBeanArrayList2, ActivityBanPerson.this);
        chatRoomBanPersonAdapter2.setMtype("hei");
        chatRoomBanPersonAdapter2.setOnEXItemClickListener(new ChatRoomBanPersonAdapter.OnEXItemClickListener() {
                                                               @Override
                                                               public void jiechuClick(int pos) {

                                                                   ChatRoomBanPersonList.DataBean itemData = chatRoomBanPersonAdapter2.getData().get(pos);
                                                                       requestUnBlack(itemData.getAccid(), pos);
                                                               }
                                                           }
        );


        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#935E38"));


        LinearLayoutManager manager1 = new LinearLayoutManager(ActivityBanPerson.this);
        LinearLayoutManager manager2 = new LinearLayoutManager(ActivityBanPerson.this);
        rv_data1.setAdapter(chatRoomBanPersonAdapter1);
        rv_data2.setAdapter(chatRoomBanPersonAdapter2);
        rv_data1.setLayoutManager(manager1);
        rv_data2.setLayoutManager(manager2);


    }


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    @OnClick(R.id.rl_search)
    void searchClick() {
        ActivityChatRoomPersonList.startActivity(this,roomId);
    }

    @OnClick(R.id.iv_options1)
    void rule1Click(){

            rxDialogVotingRules.getTv_title().setText("禁言说明");
            rxDialogVotingRules.getTv_des().setText("1、禁言是什么？\n" +
                    "禁言是为了规范聊天室和谐发展而制定的惩罚机制。用户若被禁言，将无法在该聊天室内进行发言，直到禁言解除。\n\n" +
                    "2、为什么会被禁言？\n" +
                    "（1）违反聊天室的群规公告；\n" +
                    "（2）同一聊天室遭到5位或以上用户投诉；\n\n" +
                    "3、禁言如何解除？\n" +
                    "（1）若被用户投诉禁言，24小时后，禁言自动解除；\n" +
                    "（2）若被管理员或以上级别的人员禁言，需自行联系管理员或以上级别人员，申诉解除。\n\n" +
                    "4、如何避免被禁言？\n" +
                    "（1）请严格遵守聊天室群规公告；\n" +
                    "（2）积极维护好自身形象与良好信誉。");

        rxDialogVotingRules.show();
    }

    @OnClick(R.id.iv_options2)
    void rule2Click(){

            rxDialogVotingRules.getTv_title().setText("黑名单说明");
            rxDialogVotingRules.getTv_des().setText("1、黑名单是什么？\n" +
                    "黑名单是为了规范聊天室和谐发展而制定的惩罚机制。用户若被拉入黑名单，将无法阅读浏览聊天室内的任何消息，直到被解除黑名单。\n\n" +
                    "2、为什么会被拉入黑名单？\n" +
                    "（1）严重违反聊天室的群规公告；\n" +
                    "（2）用户所发言论或消息引起聊天室重大负面影响。\n\n" +
                    "3、哪些人有权将违规用户拉入黑名单？\n" +
                    "管理员或以上级别的人员。\n\n" +
                    "4、黑名单如何解除？\n" +
                    "联系管理员或以上级别的人员，进行申诉解除。\n\n" +
                    "5、如何避免被拉入黑名单？\n" +
                    "（1）请严格遵守聊天室群规；\n" +
                    "（2）积极维护好自身形象与良好信誉。");

        rxDialogVotingRules.show();
    }

    @OnClick(R.id.activity_quanzi_persondetail_approverequest)
    void approveClick(){
        //禁言列表的展开收起
        if(rv_data1.getVisibility() == View.VISIBLE)
            rv_data1.setVisibility(View.GONE);
        else if(rv_data1.getVisibility() == View.GONE)
            rv_data1.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.frag_mineinformation_deniedbt)
    void deniedClick(){
        //黑名单列表的展开收起
        if(rv_data2.getVisibility() == View.VISIBLE)
            rv_data2.setVisibility(View.GONE);
        else if(rv_data2.getVisibility() == View.GONE)
            rv_data2.setVisibility(View.VISIBLE);
    }



    @Override
    public void gc() {

    }

    @Override
    public void onRefresh() {
//            fetchRoomMembers1();
//            fetchRoomMembers2();
        requestJinYan();
        requestHeiMingDan();
    }

//    /**
//     * 获取聊天室禁言人员
//     */
//    private void fetchRoomMembers1() {
//        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.NORMAL, 0, 1000).setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
//            @Override
//            public void onResult(int code, List<ChatRoomMember> result, Throwable exception) {
//                barBeanArrayList1.clear();
//                if (null != result) {
//                    RxLogTool.e("fetchRoomMembers size:" + result.size());
//                    for (int k = 0; k < result.size(); k++) {
//                        //加上禁言和临时禁言的人员在此列表
//                        if (result.get(k).isMuted()) {
//                            barBeanArrayList1.add(result.get(k));
//                        }else if (result.get(k).isTempMuted()) {
//                            barBeanArrayList1.add(result.get(k));
//                        }
//                    }
//                    chatRoomBanPersonAdapter1.replaceData(barBeanArrayList1);
//                }
//                if(barBeanArrayList1.isEmpty()){
//                    chatRoomBanPersonAdapter1.setEmptyView(R.layout.view_alluse_empty,rv_data1);
//                }
//
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (null != swipe_refresh_layout)
//                            swipe_refresh_layout.setRefreshing(false);
//                    }
//                }, getResources().getInteger(R.integer.refresh_delay));
//            }
//        });
//    }
//
//    /**
//     * 获取聊天室黑名单人员
//     */
//    private void fetchRoomMembers2() {
//        NIMClient.getService(ChatRoomService.class).fetchRoomMembers(roomId, MemberQueryType.NORMAL, 0, 1000).setCallback(new RequestCallbackWrapper<List<ChatRoomMember>>() {
//            @Override
//            public void onResult(int code, List<ChatRoomMember> result, Throwable exception) {
//                barBeanArrayList2.clear();
//                if (null != result) {
//                    RxLogTool.e("fetchRoomMembers size:" + result.size());
//                    for (int k = 0; k < result.size(); k++) {
//                        if (result.get(k).isInBlackList()) {
//                            barBeanArrayList2.add(result.get(k));
//                        }
//                    }
//                    chatRoomBanPersonAdapter2.replaceData(barBeanArrayList2);
//                }
//                if(barBeanArrayList2.isEmpty()){
//                    chatRoomBanPersonAdapter2.setEmptyView(R.layout.view_alluse_empty,rv_data2);
//                }
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (null != swipe_refresh_layout)
//                            swipe_refresh_layout.setRefreshing(false);
//                    }
//                }, getResources().getInteger(R.integer.refresh_delay));
//            }
//        });
//    }


    //取消拉黑操作
    private void requestUnBlack(String accid, int position) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("byOperatorAccid", accid);
        hashMap.put("roomId", roomId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/unblacklist/" + roomId, hashMap, new CallBackUtil.CallBackString() {
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
                try {
                    dismissDialog();
                    RxLogTool.e("onResponse:" + response);
                    BaseBean basebean = GsonUtil.getBean(response, BaseBean.class);
                    if (basebean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            barBeanArrayList2.remove(position);
                            chatRoomBanPersonAdapter2.replaceData(barBeanArrayList2);
                            if(chatRoomBanPersonAdapter2.getData().size() == 0){
//                            chatRoomBanPersonAdapter2.setEmptyView(R.layout.view_alluse_empty,rv_data2);
                            }
                    } else {
                        onRefresh();
                        RxToast.custom(basebean.getMsg()).show();
                    }
                }catch (Exception ee){

                }



            }
        });
    }

    //取消禁言操作,取消所有禁言 1永久 2临时 3全部
    private void requestUnMute(String accid, int position,int type) {
        showDialog();
        Map<String, String> hashMap = new HashMap();
        hashMap.put("byOperatorAccid", accid);
        hashMap.put("roomId", roomId);
        hashMap.put("unMuteType", String.valueOf(type));

        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/chatrooms/unAllMute/" + roomId, hashMap, new CallBackUtil.CallBackString() {
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
                            chatRoomBanPersonAdapter1.replaceData(barBeanArrayList1);
                            if(chatRoomBanPersonAdapter1.getData().size() == 0){
//                            chatRoomBanPersonAdapter1.setEmptyView(R.layout.view_alluse_empty,rv_data1);
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


    private void requestHeiMingDan(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/blacklist/" + roomId, new CallBackUtil.CallBackString() {
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

                    ChatRoomBanPersonList chatRoomBanPersonList = GsonUtil.getBean(response, ChatRoomBanPersonList.class);

                    if (chatRoomBanPersonList.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if(null != chatRoomBanPersonList ){
                            barBeanArrayList2.clear();
                            barBeanArrayList2.addAll(chatRoomBanPersonList.getData());
                            chatRoomBanPersonAdapter2.replaceData(barBeanArrayList2);

                        }else{
//                        chatRoomBanPersonAdapter2.setEmptyView(R.layout.view_alluse_empty,rv_data2);
                        }
                    }else{
                        RxToast.custom(chatRoomBanPersonList.getMsg()).show();
//                     chatRoomBanPersonAdapter2.setEmptyView(R.layout.view_disconnect_network,rv_data2);
                    }


                }catch (Exception e){

                }

            }
        });
    }

    private void requestJinYan(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/mute/" + roomId, new CallBackUtil.CallBackString() {
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

                    ChatRoomBanPersonList chatRoomBanPersonList = GsonUtil.getBean(response, ChatRoomBanPersonList.class);

                    if (chatRoomBanPersonList.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if(null != chatRoomBanPersonList ){
                            barBeanArrayList1.clear();
                            barBeanArrayList1.addAll(chatRoomBanPersonList.getData());
                            chatRoomBanPersonAdapter1.replaceData(barBeanArrayList1);
                        }else{
//                        chatRoomBanPersonAdapter1.setEmptyView(R.layout.view_alluse_empty,rv_data1);
                        }
                    }else{
                        RxToast.custom(chatRoomBanPersonList.getMsg()).show();
//                    chatRoomBanPersonAdapter1.setEmptyView(R.layout.view_disconnect_network,rv_data1);
                    }

                }catch (Exception e){

                }

            }
        });
    }


}
