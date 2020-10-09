package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountBalanceList;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseCountBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityAccountSecuritySettings;
import com.lianzai.reader.ui.activity.ActivityBindPhone;
import com.lianzai.reader.ui.activity.ActivityEditPasswordShowPhone;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.contract.WalletBalanceListContract;
import com.lianzai.reader.ui.presenter.WalletBalanceListPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogSureCancel;
import com.lianzai.reader.view.dialog.RxDialogWhatIsJinzuan;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的钱包
 */
public class ActivityWalletMain extends BaseActivity implements WalletBalanceListContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.view_red)
    View view_red;

    @Bind(R.id.tv_read_token)
    TextView tv_read_token;

    @Bind(R.id.tv_gold_coin_balance)
    TextView tv_gold_coin_balance;

    @Bind(R.id.tv_read_ticket)
    TextView tv_read_ticket;

    @Bind(R.id.tv_copyright)
    TextView tv_copyright;



    @Inject
    WalletBalanceListPresenter walletBalanceListPresenter;

    RxDialogSureCancel rxDialogSureCancel;
    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号

    //什么是金钻弹窗
    RxDialogWhatIsJinzuan rxDialogWhatIsJinzuan;
    private AccountDetailBean accountDetailBean;


    public static void startActivity(Context context){
        Bundle bundle=new Bundle();
        RxActivityTool.skipActivity(context,ActivityWalletMain.class,bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_main;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.WALLET_FIRST_CLICK,true)){
            view_red.setVisibility(View.VISIBLE);
        }else {
            view_red.setVisibility(View.GONE);
        }

        ArrayMap map=new ArrayMap();
        walletBalanceListPresenter.getWalletBalanceList(map);

        accountDetailBean= RxTool.getAccountBean();

        if (null==accountDetailBean)
            finish();

        if(TextUtils.isEmpty(accountDetailBean.getData().getMobile())){
            //绑定手机号弹窗
            if (null == rxDialogBindPhone) {
                rxDialogBindPhone = new RxDialogBindPhone(ActivityWalletMain.this, R.style.OptionDialogStyle);
                rxDialogBindPhone.setContent("您暂未绑定手机号，请先绑定手机号再使用相关功能。");
                rxDialogBindPhone.setTitle("绑定手机号提示");
                rxDialogBindPhone.getCancelView().setVisibility(View.VISIBLE);
                rxDialogBindPhone.setButtonText("立即绑定", "取消");
                rxDialogBindPhone.setSureListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        //跳转到绑定手机号页面
                        rxDialogBindPhone.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putInt("flag", Constant.RegisterOrPassword.BindPhone);
                        RxActivityTool.skipActivity(ActivityWalletMain.this, ActivityBindPhone.class, bundle);
                    }
                });

                rxDialogBindPhone.setCancelListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        rxDialogBindPhone.dismiss();
                    }
                });
            }
            rxDialogBindPhone.show();
        } else if (!accountDetailBean.getData().isHasPayPwd()){//未设置
            rxDialogSureCancel.show();
        }

        getUserBookBalanceCount();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        tv_commom_title.setText("我的钱包");
        walletBalanceListPresenter.attachView(this);

        rxDialogWhatIsJinzuan = new RxDialogWhatIsJinzuan(this,R.style.OptionDialogStyle);

        rxDialogSureCancel=new RxDialogSureCancel(this,R.style.OptionDialogStyle);
        rxDialogSureCancel.setContent("请设置支付密码后再使用我的钱包功能");
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.setCancel("再看看");
        rxDialogSureCancel.setSure("立即设置");
        rxDialogSureCancel.setCancelable(false);

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
                finish();
            }
        });

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt("flag", Constant.RegisterOrPassword.PayPassword);
                RxActivityTool.skipActivity(ActivityWalletMain.this, ActivityEditPasswordShowPhone.class, bundle);
            }
        });

        //字体设置
        Typeface typeface = Typeface.createFromAsset(ActivityWalletMain.this.getAssets(),"DINMedium.ttf");
        tv_read_token.setTypeface(typeface);
        tv_gold_coin_balance.setTypeface(typeface);
        tv_read_ticket.setTypeface(typeface);
        tv_copyright.setTypeface(typeface);



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

    @OnClick(R.id.iv_more)void moreClick(){
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.WALLET_FIRST_CLICK,false);
        view_red.setVisibility(View.GONE);
        RxActivityTool.skipActivity(this,ActivityAccountSecuritySettings.class);
    }

    @OnClick(R.id.rl_read_coin)void readCoinClick(){
        ActivityWalletDetail.startActivity(this, Constant.WalletAccoutType.READ_COIN);
    }
    @OnClick(R.id.rl_read_ticket)void readTicketClick(){
        ActivityWalletDetail.startActivity(this, Constant.WalletAccoutType.READ_TICKET);
    }
    @OnClick(R.id.rl_copyright)void copyrightClick(){
        ActivityCopyrightAssets.startActivity(this);
    }
    @OnClick(R.id.rl_gold_coin)void goldCoinClick(){
        ActivityWalletDetail.startActivity(this, Constant.WalletAccoutType.GOLD_COIN);
    }

    @OnClick(R.id.help_yuedian)void help_yuedianClick(){
        rxDialogWhatIsJinzuan.getTv_title().setText("什么是阅点？");
        rxDialogWhatIsJinzuan.getTv_description().setText("阅点是连载神器的积分单位，代表着阅读权益。");
        rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                v -> {
                    //跳转到具体链接
                    ActivityWebView.startActivity(ActivityWalletMain.this,Constant.H5_HELP_BASE_URL + "/helper/show/13");
                }
        );
        rxDialogWhatIsJinzuan.show();
    }

    @OnClick(R.id.help_jinbi)void help_jinbiClick(){
        rxDialogWhatIsJinzuan.getTv_title().setText("什么是金币？");
        rxDialogWhatIsJinzuan.getTv_description().setText("金币是连载神器的虚拟积分。");
        rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                v -> {
                    //跳转到具体链接
                    ActivityWebView.startActivity(ActivityWalletMain.this,Constant.H5_HELP_BASE_URL + "/helper/show/14");
                }
        );
        rxDialogWhatIsJinzuan.show();
    }

    @OnClick(R.id.help_yuejuan)void help_yuejuanClick(){
        rxDialogWhatIsJinzuan.getTv_title().setText("什么是阅券？");
        rxDialogWhatIsJinzuan.getTv_description().setText("阅券是连载神器的一种特殊权益凭证，也是特殊形态下的阅点，拥有开启连载宝箱、获取推荐票等权益，更多使用场景正完善中。");
        rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                v -> {
                    //跳转到具体链接
                    ActivityWebView.startActivity(ActivityWalletMain.this,Constant.H5_HELP_BASE_URL + "/helper/show/12");
                }
        );
        rxDialogWhatIsJinzuan.show();
    }

    @OnClick(R.id.help_copyright)void help_copyrightClick(){
        rxDialogWhatIsJinzuan.getTv_title().setText("什么是版权资产？");
        rxDialogWhatIsJinzuan.getTv_description().setText("连载阅读共享版权是指，由作者、读者、连载平台以及投资者共同组成的社区。共同为小说作品的发展作出贡献，共同拥有作品的版权，共同拥有作品的收益权。而版权资产则是指所拥有作品的版权积分。");
        rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                v -> {
                    //跳转到具体链接
                    ActivityWebView.startActivity(ActivityWalletMain.this,"https://fx.bendixing.net/taskCenter/#/sharecopyrightquestion");
                }
        );
        rxDialogWhatIsJinzuan.show();
    }


    @Override
    public void showError(String message) {
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }

    @Override
    public void getWalletBalanceListSuccess(AccountBalanceList bean) {

        for(AccountBalanceList.DataBean dataBean:bean.getData()){
            RxLogTool.e("total:"+String.valueOf(dataBean.getBalanceAmt()+dataBean.getLockAmt()));
            if (dataBean.getBalanceType()==1){//金币
                    tv_gold_coin_balance.setText(RxDataTool.format2Decimals(String.valueOf(dataBean.getBalanceAmt()+dataBean.getLockAmt())));
            }else if (dataBean.getBalanceType()==3){//阅点
                tv_read_token.setText(RxDataTool.format2Decimals(String.valueOf(dataBean.getBalanceAmt()+dataBean.getLockAmt())));
            }else if (dataBean.getBalanceType()==4){//阅券
                tv_read_ticket.setText(RxDataTool.format2Decimals(String.valueOf(dataBean.getBalanceAmt()+dataBean.getLockAmt())));
            }
        }
    }

    @Override
    public void complete(String message) {

    }

    public void getUserBookBalanceCount() {
//        Map<String, String> map = new HashMap<>();
//        map.put("platformId", circleId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/getUserBookBalanceCount", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                } catch (Exception ex) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseCountBean baseCountBean = GsonUtil.getBean(response, BaseCountBean.class);
                    if (baseCountBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                      tv_copyright.setText(String.valueOf(baseCountBean.getData()) + "种");
                    }
                } catch (Exception e) {
                }
            }
        });
    }

}
