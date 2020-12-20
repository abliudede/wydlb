package com.lianzai.reader.ui.activity.GameFightLuck;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.LuckInfoBean;
import com.lianzai.reader.bean.LuckInfoFollowBean;
import com.lianzai.reader.bean.LuckLaunchBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.contract.FightLuckContract;
import com.lianzai.reader.ui.presenter.FightLuckPresenter;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.DiscountOptionPopup;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWhatIsJinzuan;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/07/23
 * 发起拼手气页面
 */

public class ActivityFightLuckEnter extends BaseActivity implements FightLuckContract.View{

    @Bind(R.id.tv_amount)
    TextView tv_amount;

    @Bind(R.id.join_amount_tv)
    TextView join_amount_tv;

    @Bind(R.id.des_tv)
    TextView des_tv;

    @Bind(R.id.balance_tv)
    TextView balance_tv;

    @Bind(R.id.sure_tv)
    TextView sure_tv;

    @Bind(R.id.tv_title)
    TextView tv_title;



    @Bind(R.id.mask_view)
    View mask_view;

    @Inject
    FightLuckPresenter fightLuckPresenter;
    private LuckLaunchBean mData;
    private DiscountOptionPopup discountOptionPopup;
    private Integer amount;
    //确认支付
    RxDialogSureCancelNew rxDialogSureCancel;
    private String roomId;
    //什么是阅卷弹窗
    RxDialogWhatIsJinzuan rxDialogWhatIsJinzuan;

    int type = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_enter;
    }

    public static void startActivity(Activity activity, String roomId ,int type) {
        Bundle bundle = new Bundle();
        bundle.putString("roomId", roomId);
        bundle.putInt("type", type);
        RxActivityTool.skipActivity(activity, ActivityFightLuckEnter.class, bundle);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //回到此页面刷新数据
//        accountDetailBean= RxTool.getAccountBean();
        refresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        fightLuckPresenter.attachView(this);

        roomId = getIntent().getExtras().getString("roomId");
        type = getIntent().getExtras().getInt("type");

        rxDialogWhatIsJinzuan = new RxDialogWhatIsJinzuan(this,R.style.OptionDialogStyle);

        rxDialogSureCancel=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        rxDialogSureCancel.setContent("你将花费5阅券发起该次拼手气小游戏，希望你是那个幸运儿哟");
        rxDialogSureCancel.setCanceledOnTouchOutside(false);
        rxDialogSureCancel.setCancel("取消");
        rxDialogSureCancel.setSure("确定");
        rxDialogSureCancel.setCancelable(false);

        rxDialogSureCancel.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
            }
        });

    }

    private void refresh(){
        showDialog();
        fightLuckPresenter.luckLaunch();
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }

    //参与点击，弹出门槛弹窗
    @OnClick(R.id.join_ly)
    void join_lyClick() {
        mask_view.setVisibility(View.VISIBLE);
        discountOptionPopup.showPopupWindow(mask_view);
    }

    //什么是阅券弹窗
    @OnClick(R.id.help_tv)
    void help_tvClick() {
        rxDialogWhatIsJinzuan.getTv_title().setText("什么是阅券？");
        rxDialogWhatIsJinzuan.getTv_description().setText("阅券是连载神器的一种特殊权益凭证，也是特殊形态下的阅点，拥有开启连载宝箱、获取推荐票等权益，更多使用场景正完善中。");
        rxDialogWhatIsJinzuan.getTv_showmore().setOnClickListener(
                v -> {
                    //跳转到具体链接
                    ActivityWebView.startActivity(ActivityFightLuckEnter.this,Constant.H5_HELP_BASE_URL + "/helper/show/12");
                }
        );
        rxDialogWhatIsJinzuan.show();
    }

    @OnClick(R.id.sure_tv)
    void sure_tvClick() {

        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();

                //支付密码输入完后执行下面操作
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("roomId",roomId);
                jsonObject.addProperty("threshold",amount);
                jsonObject.addProperty("title",mData.getData().getTitle());
                if(type == 2){
                    jsonObject.addProperty("type",2);
                }else {
                    jsonObject.addProperty("type",1);
                }

                //加密操作
                Map map = new HashMap();
                map.put("roomId", roomId);
                map.put("threshold", amount);
                map.put("title", mData.getData().getTitle());
                if(type == 2){
                    map.put("type",2);
                }else {
                    map.put("type",1);
                }
                SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                parameters.putAll(map);
                String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                jsonObject.addProperty("sign",RxEncryptTool.encryptMD5ToString(sortUrl));

                fightLuckPresenter.luckPublish(jsonObject.toString());
                showDialog();
            }
        });
        rxDialogSureCancel.show();

    }

    @Override
    public void gc() {
        RxToast.gc();
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        if (RxNetTool.showNetworkUnavailable(message)){
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }


    @Override
    public void complete(String message) {
        dismissDialog();
        showSeverErrorDialog(message);
    }


    @Override
    public void initToolBar() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void luckLaunchSuccess(LuckLaunchBean bean) {
        dismissDialog();
        if(null == bean) {
            finish();
            return;
        }
        if(null == bean.getData().getThresholdList()){
            finish();
            return;
        }
        mData = bean;

        tv_title.setText(mData.getData().getTitle());
        amount = bean.getData().getThresholdList().get(0);
        rxDialogSureCancel.setContent("你将花费" + amount + "阅券发起该次拼手气小游戏，希望你是那个幸运儿哟");

        StringBuilder sb1 = new StringBuilder();
        sb1.append(amount);
        sb1.append("阅券/次");
        tv_amount.setText(sb1.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(amount);
        sb2.append("阅券");
        join_amount_tv.setText(sb2.toString());
        if(amount < mData.getData().getBalance()){
            sure_tv.setEnabled(true);
            sure_tv.setBackgroundResource(R.drawable.gradient_bg);
        }else{
            sure_tv.setEnabled(false);
            sure_tv.setBackgroundResource(R.drawable.btn_disable_bg);
        }

        des_tv.setText(bean.getData().getRule());
        balance_tv.setText(String.valueOf((int)bean.getData().getBalance()));


        //折扣按钮弹窗
        discountOptionPopup=new DiscountOptionPopup(this,bean.getData().getThresholdList(),bean.getData().getThresholdList().get(0));
        //设置itemclick
        discountOptionPopup.setOnItemClickListener(
                new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                        amount = mData.getData().getThresholdList().get(i);

                        rxDialogSureCancel.setContent("你将花费" + amount + "阅券发起该次拼手气小游戏，希望你是那个幸运儿哟");
                        discountOptionPopup.setChoosestr(amount);
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append(amount);
                        sb1.append("阅券/次");
                        tv_amount.setText(sb1.toString());
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(amount);
                        sb2.append("阅券");
                        join_amount_tv.setText(sb2.toString());

                        if(amount < mData.getData().getBalance()){
                            sure_tv.setEnabled(true);
                            sure_tv.setBackgroundResource(R.drawable.gradient_bg);
                        }else{
                            sure_tv.setEnabled(false);
                            sure_tv.setBackgroundResource(R.drawable.btn_disable_bg);
                        }

                        discountOptionPopup.dismiss();
                    }
                }
        );
        discountOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mask_view.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void luckPublishSuccess(BaseBean bean) {
        dismissDialog();
        RxEventBusTool.sendEvents(Constant.EventTag.FIGHT_LUCK_SUCCESS);//发起拼手气成功
        finish();
    }

    @Override
    public void luckInfoSuccess(LuckInfoBean bean) {

    }

    @Override
    public void luckFollowSuccess(LuckInfoFollowBean bean) {

    }


    @Override
    public void luckJoinSuccess(BaseBean bean) {

    }
}
