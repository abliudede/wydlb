package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.UserAutoSettingDailyTicketListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.AutoTicketListAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogAutoTicket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 自动投票管理
 */
public class ActivityAutoTicketManage extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.search_ed)
    EditText search_ed;


    AutoTicketListAdapter autoTicketListAdapter;
    RxLinearLayoutManager manager;
    List<UserAutoSettingDailyTicketListBean.DataBean> dataBeanList = new ArrayList<>();
    List<UserAutoSettingDailyTicketListBean.DataBean> showDataBeanList = new ArrayList<>();
    private String searchKey = "";

    RxDialogAutoTicket rxDialogAutoTicket;
    private String amount;


    public static void startActivity(Context context,String amount){
        Bundle bundle = new Bundle();
        bundle.putString("amount", amount);
        RxActivityTool.skipActivity(context, ActivityAutoTicketManage.class, bundle);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_auto_ticket;
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
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        try{
            amount = getIntent().getStringExtra("amount");
        }catch (Exception e){

        }

        rxDialogAutoTicket = new RxDialogAutoTicket(this, R.style.BottomDialogStyle);
        rxDialogAutoTicket.getTv_save().setOnClickListener(
                v -> {
                    saveAutoTicket(rxDialogAutoTicket.getEd_search_keyword().getText().toString(),rxDialogAutoTicket.getBookId());

                }
        );

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        autoTicketListAdapter= new AutoTicketListAdapter(showDataBeanList,this);
        autoTicketListAdapter.setDynamicClickListener(new AutoTicketListAdapter.DynamicClickListener() {
            @Override
            public void modifyClick(int position) {
                try{
                    UserAutoSettingDailyTicketListBean.DataBean item = autoTicketListAdapter.getData().get(position);
                    rxDialogAutoTicket.getEd_search_keyword().setText(String.valueOf(item.getAmt()));
                    rxDialogAutoTicket.getTv_day_count().setText("今日可投推荐票剩余" + String.valueOf(amount) + "张");
                    rxDialogAutoTicket.setBookId(item.getBookId());
                    rxDialogAutoTicket.show();
                }catch (Exception e){
                    RxLogTool.e(e.toString());
                }
            }

            @Override
            public void picClick(int position) {
                try{
                    UserAutoSettingDailyTicketListBean.DataBean item = autoTicketListAdapter.getData().get(position);
                }catch (Exception e){
                    RxLogTool.e(e.toString());
                }
            }
        });

        manager=new RxLinearLayoutManager(ActivityAutoTicketManage.this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new RxRecyclerViewDividerTool(0, 0, RxImageTool.dip2px(10), 0));
        rv_list.setAdapter(autoTicketListAdapter);

        search_ed.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = s.toString();
                        if (TextUtils.isEmpty(str)){
                            searchKey = "";
                        }else{//模糊匹配
                            searchKey = str;
                        }
                        useSearchKey();

                    }
                }
        );

        onRefresh();
    }

    private void useSearchKey() {
        showDataBeanList.clear();
        if(TextUtils.isEmpty(searchKey)){
            showDataBeanList.addAll(dataBeanList);
        }else {
            for(UserAutoSettingDailyTicketListBean.DataBean item:dataBeanList){
                if(item.getBookName().contains(searchKey)){
                    showDataBeanList.add(item);
                }
            }
        }
        autoTicketListAdapter.replaceData(showDataBeanList);
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
    @OnClick(R.id.tv_more)void tv_moreClick(){
       ActivityAutoTicketRecord.startActivity(this);
    }



    @Override
    public void onRefresh() {
        //请求自动投票管理
        requestData();
    }

    private void saveAutoTicket(String amount,int bid){
        if(TextUtils.isEmpty(amount)){
            RxToast.custom("请输入数量").show();
            return;
        }
        showDialog();
        rxDialogAutoTicket.dismiss();
        Map<String, String> map = new HashMap<>();
        map.put("amt", amount);
        map.put("bookId", String.valueOf(bid));
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/dailyTicket/saveOrUpdAutoDailyTicketSetting", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    dismissDialog();
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("修改成功").show();
                        onRefresh();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    public void requestData(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/dailyTicket/getUserAutoSettingDailyTicketList", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/dailyTicket/getUserAutoSettingDailyTicketList").show();
                    autoTicketListAdapter.loadMoreFail();
                    showOrCloseRefresh(false);
                } catch (Exception ee) {
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    showOrCloseRefresh(false);
                    UserAutoSettingDailyTicketListBean userAutoSettingDailyTicketListBean = GsonUtil.getBean(response, UserAutoSettingDailyTicketListBean.class);
                    if (userAutoSettingDailyTicketListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        dataBeanList.clear();
                        List<UserAutoSettingDailyTicketListBean.DataBean> templist = userAutoSettingDailyTicketListBean.getData();
                        if(null == templist) {
                            templist = new ArrayList<>();
                        }

                        dataBeanList.addAll(templist);
                        useSearchKey();
                            if(templist.isEmpty()) {
                                autoTicketListAdapter.setEnableLoadMore(false);
                                autoTicketListAdapter.loadMoreEnd();
                            }else {
                                autoTicketListAdapter.setEnableLoadMore(true);
                                autoTicketListAdapter.loadMoreComplete();
                            }

                    } else {
                        RxToast.custom(userAutoSettingDailyTicketListBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
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



}
