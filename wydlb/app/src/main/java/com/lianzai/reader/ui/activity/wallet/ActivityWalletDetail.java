package com.lianzai.reader.ui.activity.wallet;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalance;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BindAccountListResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.WalletDetailListResponse;
import com.lianzai.reader.bean.WalletListSumResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.adapter.WalletTransactionAdapter;
import com.lianzai.reader.ui.contract.BindAccountListContract;
import com.lianzai.reader.ui.contract.WalletBalanceContract;
import com.lianzai.reader.ui.contract.WalletDetailListContract;
import com.lianzai.reader.ui.contract.WalletListSumContract;
import com.lianzai.reader.ui.presenter.BindAccountListPresenter;
import com.lianzai.reader.ui.presenter.WalletBalancePresenter;
import com.lianzai.reader.ui.presenter.WalletDetailListPresenter;
import com.lianzai.reader.ui.presenter.WalletListSumPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWalletPicker;
import com.lianzai.reader.view.dialog.RxDialogWhatIsJinzuan;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包详情
 */
public class ActivityWalletDetail extends BaseActivity implements  BaseQuickAdapter.RequestLoadMoreListener,WalletBalanceContract.View,WalletDetailListContract.View,SwipeRefreshLayout.OnRefreshListener, WalletListSumContract.View,BindAccountListContract.View{

    @Bind(R.id.rv_list)
    RecyclerView rv_list;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    //筛选按钮
    @Bind(R.id.tv_options)
            TextView tv_options;

    @Bind(R.id.rl_first)
    RelativeLayout rl_first;
    @Bind(R.id.rl_second)
    RelativeLayout rl_second;
    @Bind(R.id.rl_third)
    RelativeLayout rl_third;

    @Bind(R.id.tv_first)
    TextView tv_first;
    @Bind(R.id.tv_second)
    TextView tv_second;
    @Bind(R.id.tv_third)
    TextView tv_third;

    @Bind(R.id.view_line1)
    View view_line1;
    @Bind(R.id.view_line2)
    View view_line2;


    TextView tv_list_description;

    public final static int RECHARGE_READ_TOKEN_REQUEST_CODE=10001;

    public final static int RECHARGE_READ_TICKET_REQUEST_CODE=10002;

    public final static int RECHARGE_GOLD_COIN_REQUEST_CODE=10003;

    public final static int WITHDRAW_READ_TOKEN_REQUEST_CODE=10004;

    public final static int WITHDRAW_READ_TICKET_REQUEST_CODE=10005;

    public final static int WITHDRAW_GOLD_COIN_REQUEST_CODE=10006;

    @Inject
    WalletListSumPresenter walletListSumPresenter;

    WalletTransactionAdapter walletTransactionAdapter;


    @Inject
    WalletDetailListPresenter walletDetailListPresenter;

    @Inject
    BindAccountListPresenter bindAccountListPresenter;


    View headerView;
    View view_balance;

    CustomLoadMoreView customLoadMoreView;

    RxLinearLayoutManager manager;

    TextView tv_des_total;
    ImageView iv_img_status;

    String mAccountType= Constant.WalletAccoutType.READ_COIN;

    RxDialogWalletPicker rxDialogWalletPicker;

    RelativeLayout rl_conditions;

    @Inject
    WalletBalancePresenter walletBalancePresenter;


    List<WalletDetailListResponse.DataBean.ListBean>dataBeanList=new ArrayList<>();

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    TextView tv_total;
    TextView tv_available;
    TextView tv_unlocked;
    TextView tv_stop;

    TextView tv_date_picker;

    View view_empty;

    int pageNo=1;
    boolean isLoadMore = false;
    boolean isError = false;

    String yearMonth;
    String type;

    String coinType;

    WalletListSumResponse walletListSumResponse;

    long nextTime=0;

    RxDialogSureCancelNew rxDialogSureCancel;

    //什么是金钻弹窗
    RxDialogWhatIsJinzuan rxDialogWhatIsJinzuan;
    private AccountDetailBean accountDetailBean;

    RxDialogPayment rxDialogPayment;//支付密码框
    private AccountBalance mAccountBalance;

    public static void startActivity(Context context,String accountType){
        Bundle bundle=new Bundle();
        bundle.putString("accountType",accountType);
        RxActivityTool.skipActivity(context,ActivityWalletDetail.class,bundle);
    }



    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_wallet_account;
    }

    @Override
    protected void onDestroy() {
        RxEventBusTool.unRegisterEventBus(this);
        super.onDestroy();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
       onRefresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        walletBalancePresenter.attachView(this);
        walletDetailListPresenter.attachView(this);
        walletListSumPresenter.attachView(this);
        bindAccountListPresenter.attachView(this);

        accountDetailBean= RxTool.getAccountBean();

        rxDialogSureCancel=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        rxDialogWhatIsJinzuan = new RxDialogWhatIsJinzuan(this,R.style.OptionDialogStyle);



        initConditionPicker();

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        headerView=getLayoutInflater().inflate(R.layout.header_account_detail,null);
        tv_total=(TextView)headerView.findViewById(R.id.tv_total);
        //字体设置
        Typeface typeface = Typeface.createFromAsset(ActivityWalletDetail.this.getAssets(),"DINMedium.ttf");
        tv_total.setTypeface(typeface);
        tv_available=(TextView)headerView.findViewById(R.id.tv_available);
        tv_unlocked=(TextView)headerView.findViewById(R.id.tv_unlocked);
        tv_stop=(TextView)headerView.findViewById(R.id.tv_stop);

        view_balance=(View)headerView.findViewById(R.id.view_balance);

        tv_list_description=(TextView)headerView.findViewById(R.id.tv_list_description);
        view_empty=headerView.findViewById(R.id.view_empty);

        tv_date_picker=(TextView)headerView.findViewById(R.id.tv_date_picker);

        rl_conditions=(RelativeLayout)headerView.findViewById(R.id.rl_conditions);

        tv_des_total=(TextView) headerView.findViewById(R.id.tv_des_total);
        iv_img_status=(ImageView)headerView.findViewById(R.id.iv_img_status);
        tv_options.setText("筛选");

        tv_options.setOnClickListener(
                v->{

                    if (rxDialogWalletPicker.getSpinner_date_select().getSelectedItemPosition()==0){
                        rxDialogWalletPicker.getView_date().setVisibility(View.GONE);
                    }else{
                        rxDialogWalletPicker.getView_date().setVisibility(View.VISIBLE);
                    }
                    rxDialogWalletPicker.getTv_list_description().setText(tv_list_description.getText().toString());
                    rxDialogWalletPicker.getTv_date_picker().setText(tv_date_picker.getText().toString());
                    rxDialogWalletPicker.setQueryCallback(new RxDialogWalletPicker.QueryCallback() {
                        @Override
                        public void queryClick(int mType, String date) {
                            rxDialogWalletPicker.dismiss();
                            if (mType>0){
                               type=String.valueOf(mType);
                            }else{
                                type="";//清空条件
                            }
                            if (!TextUtils.isEmpty(date)||!date.equals("全部")){
                                yearMonth=date;
                            }

                            if (!yearMonth.equals("全部")){
                                tv_date_picker.setText(yearMonth.substring(0,4)+"年"+yearMonth.substring(4)+"月");
                            }else{
                                yearMonth="";
                            }

                            requestListData(true);

                            if (null!=walletListSumResponse){
                                for(WalletListSumResponse.DataBean dataBean:walletListSumResponse.getData()){
                                    if (dataBean.getYearMonth().equals(yearMonth)){
                                        tv_list_description.setText("支出 "+ RxDataTool.format2Decimals(String.valueOf(dataBean.getOutAmt()))+coinType+" 收入"+RxDataTool.format2Decimals(String.valueOf(dataBean.getInAmt()))+coinType);
                                        break;
                                    }else{
                                        tv_list_description.setText("支出 "+0+coinType+" 收入"+0+coinType);
                                    }
                                }
                            }

                        }
                    });

                    rxDialogWalletPicker.show();
                }
        );

        mAccountType=getIntent().getExtras().getString("accountType");
        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            coinType=Constant.WalletAccoutType.READ_COIN;
            RxTool.stringFormat(tv_commom_title,R.string.wallet_account_title,coinType,this);
            RxTool.stringFormat(tv_des_total,R.string.wallet_account_total,coinType,this);
            iv_img_status.setImageResource(R.mipmap.v2_icon_wallet_read_coin);

            tv_first.setText("锁定为阅券");
            tv_third.setText("划转到BTZ");
            rl_first.setVisibility(View.VISIBLE);
            view_line2.setVisibility(View.VISIBLE);
            rl_third.setVisibility(View.VISIBLE);


            if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.SHANDUI_ANNIU, false)){
                tv_second.setText("兑换为金币");
                rl_second.setVisibility(View.VISIBLE);
                view_line1.setVisibility(View.VISIBLE);
            }else {
                rl_second.setVisibility(View.GONE);
                view_line1.setVisibility(View.GONE);
            }

            view_balance.setVisibility(View.VISIBLE);

            rxDialogWhatIsJinzuan.getTv_title().setText("待解锁阅点说明");
            rxDialogWhatIsJinzuan.getTv_description().setText("阅券兑换为阅点，需经过100天兑换周期（每天解锁），解锁完成即是阅点，解锁中即是待解锁阅点。");
            rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                    v -> {
                        //跳转到具体链接
                        ActivityWebView.startActivity(ActivityWalletDetail.this,Constant.H5_HELP_BASE_URL + "/helper/show/13");
                    }
            );


            tv_stop.setOnClickListener(
                    v -> {
                        rxDialogSureCancel.getTitleView().setVisibility(View.GONE);
                        rxDialogSureCancel.getContentView().setText("您是否确定要撤销解锁？撤销后，所有待解锁状态将恢复为阅券。");
                        rxDialogSureCancel.getSureView().setText("确定");
                        rxDialogSureCancel.getCancelView().setText("取消");
                        rxDialogSureCancel.getSureView().setOnClickListener(
                                v1 -> {
                                    showStopPaymentDialog();
                                    rxDialogSureCancel.dismiss();
                                }
                        );
                        rxDialogSureCancel.show();
                    }
            );

        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){
            coinType=Constant.WalletAccoutType.READ_TICKET;
            RxTool.stringFormat(tv_commom_title,R.string.wallet_account_title,coinType,this);
            RxTool.stringFormat(tv_des_total,R.string.wallet_account_total,coinType,this);
            iv_img_status.setImageResource(R.mipmap.v2_icon_wallet_read_ticket);

            tv_second.setText("解锁为阅点");

            rl_second.setVisibility(View.GONE);
            view_line1.setVisibility(View.GONE);
            rl_second.setVisibility(View.VISIBLE);
            view_line2.setVisibility(View.GONE);
            rl_third.setVisibility(View.GONE);


            view_balance.setVisibility(View.GONE);
        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            coinType=Constant.WalletAccoutType.GOLD_COIN;
            RxTool.stringFormat(tv_commom_title,R.string.wallet_account_title,coinType,this);
            RxTool.stringFormat(tv_des_total,R.string.wallet_account_total,coinType,this);
            iv_img_status.setImageResource(R.mipmap.v2_icon_wallet_gold_coin);


            tv_first.setText("充值");
            tv_third.setText("提现");
            rl_first.setVisibility(View.VISIBLE);
            view_line2.setVisibility(View.VISIBLE);
            rl_third.setVisibility(View.VISIBLE);


            if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.SHANDUI_ANNIU, false)){
                tv_second.setText("兑换为阅点");
                rl_second.setVisibility(View.VISIBLE);
                view_line1.setVisibility(View.VISIBLE);
            }else {
                rl_second.setVisibility(View.GONE);
                view_line1.setVisibility(View.GONE);
            }

            view_balance.setVisibility(View.VISIBLE);

            rxDialogWhatIsJinzuan.getTv_title().setText("待解锁金币说明");
            rxDialogWhatIsJinzuan.getTv_description().setText("打赏n金币，可解锁10%*n金币，直至待解锁金币全部解锁。");
            rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                    v -> {
                        //跳转到具体链接
                        ActivityWebView.startActivity(ActivityWalletDetail.this,Constant.H5_HELP_BASE_URL + "/helper/show/14");
                    }
            );
        }


        tv_list_description.setText("支出 "+0+coinType+" 收入"+0+coinType);

//        getCurrentDate();


        walletTransactionAdapter= new WalletTransactionAdapter(dataBeanList,this);
        walletTransactionAdapter.setOnLoadMoreListener(this,rv_list);

        walletTransactionAdapter.addHeaderView(headerView,0);

        customLoadMoreView=new CustomLoadMoreView();
        walletTransactionAdapter.setLoadMoreView(customLoadMoreView);


        manager=new RxLinearLayoutManager(ActivityWalletDetail.this);
        rv_list.setLayoutManager(manager);
        rv_list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        rv_list.setAdapter(walletTransactionAdapter);

        walletTransactionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (!walletTransactionAdapter.getData().get(i).isShowMonth()){
                    WalletDetailListResponse.DataBean.ListBean bean=walletTransactionAdapter.getData().get(i);
                    Intent intent = new Intent(ActivityWalletDetail.this, ActivityWebView.class);
                    intent.putExtra(ActivityWebView.PARAM_URL, Constant.H5_BASE_URL+"/new-wallet/#/tradingParticulars/"+bean.getId()+"/"+bean.getInOutType());
                    intent.putExtra("hiddenActionBar",false);
                    intent.putExtra("type",1);
                    startActivity(intent);
                }
            }
        });

    }

    private void showStopPaymentDialog( ){
        if(null == rxDialogPayment){
            rxDialogPayment=new RxDialogPayment(ActivityWalletDetail.this,R.style.OptionDialogStyle);
        }
        //支付密码输入弹框
        rxDialogPayment.getTv_sure().setOnClickListener(
                view->{
                    if (TextUtils.isEmpty(rxDialogPayment.getEd_pay_password().getText().toString().trim())){
                        RxToast.custom("请输入您的支付密码").show();
                        return;
                    }
                    rxDialogPayment.dismiss();
                    //支付密码输入完后执行下面操作
                    ArrayMap map = new ArrayMap();
                    String password =  rxDialogPayment.getEd_pay_password().getText().toString();

                    String uid = accountDetailBean.getData().getUid();
                    long timeStamp = System.currentTimeMillis();
                    map.put("balanceType", "4");
                    map.put("password",password);
                    map.put("timestamp", timeStamp);
                    map.put("userId",uid);
                    map.put("amount",mAccountBalance.getData().getLockAmt());


                    SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                    parameters.putAll(map);
                    String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                    RxLogTool.e("sortUrl:" + sortUrl);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("balanceType", "4");
                    jsonObject.addProperty("password", password);
                    jsonObject.addProperty("timestamp", timeStamp);
                    jsonObject.addProperty("userId", uid);
                    jsonObject.addProperty("amount",mAccountBalance.getData().getLockAmt());
                    jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
                    requestStop(jsonObject.toString());
                    //清除支付密码框
                    rxDialogPayment.getEd_pay_password().setText("");
                }
        );
        rxDialogPayment.show();
    }

    private void requestStop(String json){
        showDialog();
        OKHttpUtil.okHttpPostJson(Constant.GATEWAY_API_BASE_URL + "/api/userLockedBalanceRelease", json, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxToast.custom("网络错误").show();
                    dismissDialog();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("操作成功", Constant.ToastType.TOAST_SUCCESS).show();
                        onRefresh();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private void requestListData(boolean isRefresh){
        ArrayMap<String,Object>arrayMap=new ArrayMap<>();
        if (isRefresh) {
            pageNo = 1;
            isLoadMore = false;
            headerMaps.clear();
        } else {
            pageNo++;
            isLoadMore = true;
            arrayMap.put("nextTime",nextTime);
        }

        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            arrayMap.put("balanceType","3");
        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){
            arrayMap.put("balanceType","4");
        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            arrayMap.put("balanceType","1");
        }


        if (!TextUtils.isEmpty(yearMonth)){
            arrayMap.put("yearMonth",yearMonth);
        }

        if (!TextUtils.isEmpty(type)){
            arrayMap.put("type",type);
        }

        arrayMap.put("pageSize",10);
        arrayMap.put("pageNumber",pageNo);

        walletDetailListPresenter.getBalancePage(arrayMap);
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

    @Override
    public void onLoadMoreRequested() {
        if (walletTransactionAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
            RxLogTool.e("onLoadMoreRequested---不足一页");
            walletTransactionAdapter.loadMoreEnd();
        } else {
            if (!isError) {
                requestListData(false);
            } else {
                isError = true;
                walletTransactionAdapter.loadMoreFail();
            }
        }
    }

    /**
     * 第一个按钮
     */
    @OnClick(R.id.rl_first)void rl_firstClick(){
        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            //锁定为阅券
            ActivityWalletRechargeReadTicket.startActivityForResult(this);
        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){
            //没有此按钮

        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            ActivityWalletRechargeGoldCoin.startActivity(this);
        }

    }

    /**
     * 第二个按钮
     */
    @OnClick(R.id.rl_second)void rl_secondClick(){
        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            //闪兑，兑换为金币
            ActivityWebView.startActivity(ActivityWalletDetail.this,Constant.H5_BASE_URL + "/quick-exchange/#/exchange?type=PointTOGold",1);

        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){//阅券不需要验证
            //解锁为阅点
            ActivityWalletWithdrawReadTicket.startActivityForResult(this);
        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            //兑换为阅点
            ActivityWebView.startActivity(ActivityWalletDetail.this,Constant.H5_BASE_URL + "/quick-exchange/#/exchange?type=GoldTOPoint",1);
        }
    }

    /**
     * 第三个按钮
     */
    @OnClick(R.id.rl_third)void rl_thirdClick(){
        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            //提现，划转到BTZ
            ActivityWalletWithdrawReadCoin.startActivityForResult(this);
        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){
            //没有此按钮

        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            //请求绑定信息，判断是否已经绑定了微信
            bindAccountListPresenter.getBindAccountList();
        }
    }

    /**
     * 跳转到微信
     */
    private void getWechatApi(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            skipToMarket();
//            RxToast.custom("检查到您手机没有安装微信，请安装后使用该功能").show();
        }
    }

    private void skipToMarket(){
        try{
            Uri uri = Uri.parse("market://details?id="+"com.tencent.mm");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch(Exception e){
            RxToast.custom("您的手机没有安装Android应用市场").show();
            e.printStackTrace();
        }
    }


    private void initConditionPicker(){
        rxDialogWalletPicker=new RxDialogWalletPicker(this,R.style.UpDialogStyle);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void getWalletBalanceSuccess(AccountBalance bean) {
        mAccountBalance = bean;
        tv_total.setText(RxDataTool.format2Decimals(String.valueOf(mAccountBalance.getData().getTotalAmt())));
        tv_available.setText("可用 "+RxDataTool.format2Decimals(String.valueOf(mAccountBalance.getData().getBalanceAmt())));
        tv_unlocked.setText("待解锁 "+RxDataTool.format2Decimals(String.valueOf(mAccountBalance.getData().getLockAmt())));
        if(mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
//            Drawable drawable =RxTool.getContext().getResources().getDrawable(R.mipmap.icon_question_milepost);
//            drawable.setBounds(0,0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            tv_unlocked.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_question_milepost),null,null,null);
            tv_unlocked.setCompoundDrawablePadding(10);
            tv_unlocked.setOnClickListener(
                    v -> {
                        rxDialogWhatIsJinzuan.show();
                    }
            );
        }
        if(mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
//            Drawable drawable =RxTool.getContext().getResources().getDrawable(R.mipmap.icon_question_milepost);
//            drawable.setBounds(0,0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            tv_unlocked.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_question_milepost),null,null,null);
            tv_unlocked.setCompoundDrawablePadding(10);
            tv_unlocked.setOnClickListener(
                    v -> {
                        rxDialogWhatIsJinzuan.show();
                    }
            );

            if(mAccountBalance.getData().getLockAmt() > 0){
                tv_stop.setVisibility(View.VISIBLE);
            }else {
                tv_stop.setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public void complete(String message) {

    }
    ArrayList<String> headerMaps=new ArrayList();
    private List<WalletDetailListResponse.DataBean.ListBean> sectionListData(List<WalletDetailListResponse.DataBean.ListBean> listBeans){
        List<WalletDetailListResponse.DataBean.ListBean> newListBeans=new ArrayList<>();
        for(int i=0;i<listBeans.size();i++){
            WalletDetailListResponse.DataBean.ListBean bean=listBeans.get(i);
            String header=RxTimeTool.milliseconds2YYMMString1(bean.getCreateTime());

            if (!headerMaps.contains(header)){//是否包含header
                headerMaps.add(header);
                WalletDetailListResponse.DataBean.ListBean headBean=new WalletDetailListResponse.DataBean.ListBean();
               if (null!=walletListSumResponse){
                   for(WalletListSumResponse.DataBean dataBean:walletListSumResponse.getData()){
                       String yearDate=header.replace("年","").replace("月","");
                       if (dataBean.getYearMonth().equals(yearDate)){
                           headBean.setDescription("支出 "+ RxDataTool.format2Decimals(String.valueOf(dataBean.getOutAmt()))+coinType+" 收入"+RxDataTool.format2Decimals(String.valueOf(dataBean.getInAmt()))+coinType);
                           break;
                       }else{
                           headBean.setDescription("支出 0"+coinType+" 收入 0"+coinType);
                       }
                   }
               }
                headBean.setShowMonth(true);
                headBean.setShowDate(header);
                newListBeans.add(headBean);
            }
            bean.setDescription("");
            bean.setShowMonth(false);
            newListBeans.add(bean);
        }
        return newListBeans;

    }

    @Override
    public void getBalancePageSuccess(WalletDetailListResponse bean) {
        if (bean.getData().getList().size()>0){
            nextTime=bean.getData().getList().get(0).getNextTime();
            RxLogTool.e("nextTime:"+nextTime);
        }
        if (!isLoadMore) {
            try{
                if (null!=mSwipeRefreshLayout)
                    mSwipeRefreshLayout.setRefreshing(false);
            }catch (Exception e){
                e.printStackTrace();
            }
            dataBeanList.clear();

            dataBeanList.addAll(sectionListData(bean.getData().getList()));
            if (dataBeanList.size() > 0) {

                walletTransactionAdapter.replaceData(dataBeanList);
                walletTransactionAdapter.setEnableLoadMore(true);
                walletTransactionAdapter.loadMoreComplete();
                view_empty.setVisibility(View.GONE);
                rl_conditions.setVisibility(View.GONE);
            } else {
                dataBeanList.clear();
                walletTransactionAdapter.replaceData(dataBeanList);
                view_empty.setVisibility(View.VISIBLE);

                if (TextUtils.isEmpty(yearMonth)) {
                    rl_conditions.setVisibility(View.GONE);
                }
                else{
                    rl_conditions.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (bean.getData().getList().size() > 0) {
                dataBeanList.addAll(sectionListData(bean.getData().getList()));
                walletTransactionAdapter.setNewData(dataBeanList);
            } else {
                walletTransactionAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onRefresh() {
        ArrayMap arrayMap=new ArrayMap();
        if (mAccountType.equals(Constant.WalletAccoutType.READ_COIN)){
            arrayMap.put("balanceType",Constant.WithdrawType.READ_TOKEN_TYPE);
        }else if (mAccountType.equals(Constant.WalletAccoutType.READ_TICKET)){
            arrayMap.put("balanceType", Constant.WithdrawType.READ_TICKET_TYPE);
        }else if (mAccountType.equals(Constant.WalletAccoutType.GOLD_COIN)){
            arrayMap.put("balanceType", Constant.WithdrawType.GOLD_TYPE);
        }
        walletBalancePresenter.getWalletBalance(arrayMap);
        if (!TextUtils.isEmpty(yearMonth)){
            arrayMap.put("yearMonth",yearMonth);
        }
        walletListSumPresenter.getUserBalanceSum(arrayMap);
    }

    //获取用户每月收支总和
    @Override
    public void getUserBalanceSumSuccess(WalletListSumResponse bean) {
        walletListSumResponse=bean;
        for(WalletListSumResponse.DataBean dataBean:walletListSumResponse.getData()){
            if (dataBean.getYearMonth().equals(yearMonth)){
                tv_list_description.setText("支出 "+ RxDataTool.format2Decimals(String.valueOf(dataBean.getOutAmt()))+coinType+" 收入"+RxDataTool.format2Decimals(String.valueOf(dataBean.getInAmt()))+coinType);
            }else{
                tv_list_description.setText("支出 "+ RxDataTool.format2Decimals(String.valueOf(dataBean.getOutAmt()))+coinType+" 收入"+RxDataTool.format2Decimals(String.valueOf(dataBean.getInAmt()))+coinType);
            }
        }
           //收入支出总和接口查询完后请求列表接口
           new android.os.Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   try{
                       if (null!=mSwipeRefreshLayout)
                           mSwipeRefreshLayout.setRefreshing(true);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                   requestListData(true);
               }
           },getResources().getInteger(R.integer.refresh_delay));
    }

    private void getCurrentDate(){
        int year=Calendar.getInstance().get(Calendar.YEAR);
        int month=Calendar.getInstance().get(Calendar.MONTH);
        if (month>8){
            yearMonth=year+""+(month+1);

            tv_date_picker.setText(year+"年"+(month+1)+"月");
        }else{
            yearMonth=year+"0"+(month+1);
            tv_date_picker.setText(year+"年0"+(month+1)+"月");
        }

        RxLogTool.e("yearmonth:"+yearMonth);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            requestListData(true);
        }
    }

    //金币充值需要
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag()==Constant.EventTag.WX_PAY_REFRESH_WEB){//微信支付成功
            requestListData(true);
        }
    }

    @Override
    public void getBindAccountList(BindAccountListResponse bean) {
        if(null == bean){
            RxLogTool.e("微信未绑定");
            try{
                //弹窗提示
                showWXTipsDialog();
            }catch (Exception e){
                RxLogTool.e(e.toString());
            }
            return;
        }
        if(null == bean.getData()){
            RxLogTool.e("微信未绑定");
            try{
                //弹窗提示
                showWXTipsDialog();
            }catch (Exception e){
                RxLogTool.e(e.toString());
            }
            return;
        }
        //遍历绑定列表
        BindAccountListResponse.DataBean phoneBind = null;
        BindAccountListResponse.DataBean wxBind = null;
        for(BindAccountListResponse.DataBean item : bean.getData()){
            if(item.getType() == 0){
                phoneBind = item;
            }
            if(item.getType() == 1){
                wxBind = item;
            }
        }


        if(null != wxBind){
            if(wxBind.isBind()) {
                RxLogTool.e("微信已绑定");
                //金币提现，弹窗提示前往公众号
                rxDialogSureCancel.getTitleView().setVisibility(View.VISIBLE);
                rxDialogSureCancel.getTitleView().setText("请前往公众号提现");
                rxDialogSureCancel.getContentView().setText("请前往《连载阅读平台》公众号提现。点击前往，即可复制“连载阅读平台”几字，并跳转微信");
                rxDialogSureCancel.getSureView().setText("前往");
                rxDialogSureCancel.getCancelView().setText("取消");
                rxDialogSureCancel.getSureView().setOnClickListener(
                        v -> {
                            //复制链接
                            RxClipboardTool.copyText(ActivityWalletDetail.this,"连载阅读平台");
                            getWechatApi();
                        }
                );
                rxDialogSureCancel.show();
            }else {
                RxLogTool.e("微信未绑定");
                try{
                    //弹窗提示
                    showWXTipsDialog();
                }catch (Exception e){
                    RxLogTool.e(e.toString());
                }

            }
        }else {
            RxLogTool.e("微信未绑定");
            try{
                //弹窗提示
                showWXTipsDialog();
            }catch (Exception e){
                RxLogTool.e(e.toString());
            }
        }
    }

    @Override
    public void bindAccountSuccess(BaseBean baseBean) {

    }

    private void showWXTipsDialog(){
        RxToast.custom("请先绑定微信").show();
        }
}
