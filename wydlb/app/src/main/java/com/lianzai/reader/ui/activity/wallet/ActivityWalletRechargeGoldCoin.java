package com.lianzai.reader.ui.activity.wallet;

import android.app.Activity;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GateWayResponse;
import com.lianzai.reader.bean.GoldCoinBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.WalletCoinRechargeAdapter;
import com.lianzai.reader.ui.contract.GateWayContract;
import com.lianzai.reader.ui.presenter.GateWayPresenter;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的钱包-金币充值
 */
public class ActivityWalletRechargeGoldCoin extends BaseActivity implements GateWayContract.View{

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    @Bind(R.id.tv_options)
    TextView tv_options;

    @Bind(R.id.rv_price)
    RecyclerView rv_price;


    WalletCoinRechargeAdapter walletCoinRechargeAdapter;

    @Inject
    GateWayPresenter gateWayPresenter;

    int[] amounts={6,30,98,298,998,1998};
    int[] prices={6,30,98,298,998,1998};
    private AccountDetailBean accountDetailBean;


    public static void startActivity(Activity activity){
        RxActivityTool.skipActivity(activity,ActivityWalletRechargeGoldCoin.class);
    }



    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_wallet_recharge_gold_coin;
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
        gateWayPresenter.attachView(this);

        accountDetailBean= RxTool.getAccountBean();

        tv_commom_title.setText("金币充值");
        tv_options.setText("明细");

        tv_options.setOnClickListener(
                v->{
                    ActivityWalletRecordList.startActivity(this,Constant.WalletAccoutType.GOLD_COIN,Constant.DetailType.GOLD_RECHARGE_DETAIL);
                }
        );

        List list=new ArrayList();
        for(int i=0;i<amounts.length;i++){
            GoldCoinBean goldCoinBean=new GoldCoinBean(amounts[i],prices[i]);
            list.add(goldCoinBean);
        }
        walletCoinRechargeAdapter= new WalletCoinRechargeAdapter(list,this);
        walletCoinRechargeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (i==walletCoinRechargeAdapter.getSelectionPos())return;
                walletCoinRechargeAdapter.setSelectionPos(i);
                walletCoinRechargeAdapter.notifyDataSetChanged();
            }
        });

        rv_price.setLayoutManager(new GridLayoutManager(this,3));
        rv_price.addItemDecoration(new RxRecyclerViewDividerTool(0,0, RxImageTool.dip2px(10),0));
        rv_price.setAdapter(walletCoinRechargeAdapter);
    }


    @Override
    public void gc() {
        RxEventBusTool.unRegisterEventBus(this);
    }
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }


    @Override
    public void showError(String message) {
        RxToast.custom(message).show();
        dismissDialog();
    }

    @Override
    public void gateWaySuccess(GateWayResponse bean) {
        GateWayResponse.PayInfoBean payInfoBean=bean.getPayInfo();
        if (null!=payInfoBean){

            PayReq request = new PayReq();

            request.appId = payInfoBean.getAppid();

            request.partnerId = payInfoBean.getPartnerid();

            request.prepayId= payInfoBean.getPrepayid();

            request.packageValue = "Sign=WXPay";

            request.nonceStr= payInfoBean.getNoncestr();

            request.timeStamp= payInfoBean.getTimestamp();

            request.sign= payInfoBean.getSign();

            BuglyApplicationLike.api.sendReq(request);
        }else{
            RxToast.custom(bean.getMessage()).show();
        }
        dismissDialog();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
    }

    @OnClick(R.id.tv_recharge)void rechargeClick(){
        //判断微信是否已经安装
        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        int pos=walletCoinRechargeAdapter.getSelectionPos();
        if (pos>=0){
            ArrayMap map=new ArrayMap();
            map.put("service","pay.weixin.native");
            map.put("user_id",accountDetailBean.getData().getUid());
            map.put("terminal", Constant.Terminal.ANDROID);
            map.put("body","充值"+String.valueOf(amounts[pos])+"金币，共"+prices[pos]+"元");
            if(Constant.appMode == Constant.AppMode.RELEASE){
                map.put("trans_amt",String.valueOf(prices[pos]));
            }else {
                map.put("trans_amt",String.valueOf("0.01"));
            }
//             TODO: 2018/7/27 测试价格1分钱
//            map.put("trans_amt",String.valueOf("0.01"));
            map.put("order_no", RxDataTool.getRechargeOrderNo());
            map.put("order_type", Constant.OrderType.GOLD_RECHARGE_TYPE);


            SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();

            parameters.putAll(map);

            String sortUrl=RxTool.sortMap(parameters,Constant.SIGN_KEY);
            RxLogTool.e("sortUrl:"+sortUrl);
            map.put("sign",RxEncryptTool.encryptMD5ToString(sortUrl));

            gateWayPresenter.gateWay(map);

            showDialog();
        }else {
            RxToast.custom("请选择充值金额").show();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag()==Constant.EventTag.WX_PAY){//微信支付成功
            RxEventBusTool.sendEvents(Constant.EventTag.WX_PAY_REFRESH_WEB);
            finish();
        }
    }


}
