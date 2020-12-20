package com.lianzai.reader.ui.activity.GameFightLuck;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.collection.ArrayMap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.lianzai.reader.inner.MyCountDownTimerForGame;
import com.lianzai.reader.ui.contract.FightLuckContract;
import com.lianzai.reader.ui.presenter.FightLuckPresenter;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.OverlapLogoViewForFightLuck;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/11/7
 * 拼手气详情页面
 */

public class ActivityFightLuckDetail extends BaseActivity implements FightLuckContract.View{

    @Bind(R.id.tv_amount)
    TextView tv_amount;

    @Bind(R.id.kouhao_tv)
    TextView kouhao_tv;

    @Bind(R.id.name_tv)
    TextView name_tv;

    @Bind(R.id.head_iv)
    SelectableRoundedImageView head_iv;



    @Bind(R.id.all_amount_tv)
    TextView all_amount_tv;

    @Bind(R.id.join_iv)
    ImageView join_iv;

    @Bind(R.id.time_tv)
    TextView time_tv;

    @Bind(R.id.headview_counttv)
    OverlapLogoViewForFightLuck headview_counttv;

    //获得者视图
    @Bind(R.id.owner_rl)
    RelativeLayout owner_rl;
    @Bind(R.id.owner_head_iv)
    SelectableRoundedImageView owner_head_iv;
    @Bind(R.id.owner_name_tv)
    TextView owner_name_tv;
    @Bind(R.id.owner_des_tv)
    TextView owner_des_tv;

    @Bind(R.id.rule_content_tv)
    TextView rule_content_tv;

    @Inject
    FightLuckPresenter fightLuckPresenter;
    //确认支付
    RxDialogSureCancelNew rxDialogSureCancel;

    private int gameId;
    private LuckInfoBean mData;
    private int amount;
    private MyCountDownTimerForGame myCountDownTimer;
    private int refreshCount = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fightluck_detail;
    }

    public static void startActivity(Activity activity, int gameId) {
        Bundle bundle = new Bundle();
        bundle.putInt("gameId", gameId);
        RxActivityTool.skipActivity(activity, ActivityFightLuckDetail.class, bundle);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
        //回到此页面刷新数据
        refresh();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
//        accountDetailBean= RxTool.getAccountBean();

        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        fightLuckPresenter.attachView(this);

        gameId = getIntent().getExtras().getInt("gameId",0);

        join_iv.setEnabled(false);
        rxDialogSureCancel=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        rxDialogSureCancel.setContent("你将花费5阅券参与该次拼手气小游戏，希望你是那个幸运儿哟");
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
//        showDialog();
//        ArrayMap map=new ArrayMap();
//        map.put("pageNum",1);
//        map.put("pageSize",10);
//        map.put("gameId",gameId);
//        fightLuckPresenter.luckFollow(map);

        ArrayMap map2=new ArrayMap();
        map2.put("gameId",gameId);
        fightLuckPresenter.luckInfo(map2);
    }


    @OnClick(R.id.img_back)
    void finishClick() {
        finish();
    }

    //头像点击弹出新页面
    @OnClick(R.id.headview_counttv)
    void headview_counttvClick() {

        ActivityFightLuckPersonList.startActivity(this,gameId);

    }

    //参与点击
    @OnClick(R.id.join_iv)
    void join_ivClick() {
        rxDialogSureCancel.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSureCancel.dismiss();
                //支付密码输入完后执行下面操作
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("gameId",gameId);

                //加密操作
                Map map = new HashMap();
                map.put("gameId", gameId);
                SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                parameters.putAll(map);
                String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));

                fightLuckPresenter.luckJoin(jsonObject.toString());
                showDialog();
            }
        });
        rxDialogSureCancel.show();
    }

    @Override
    public void gc() {
        RxToast.gc();
        if(null != myCountDownTimer)
        myCountDownTimer.cancel();
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
    }

    @Override
    public void luckPublishSuccess(BaseBean bean) {
        dismissDialog();
    }

    @Override
    public void luckInfoSuccess(LuckInfoBean bean) {

        if(null == bean){
            finish();
            return;
        }
        if(null == bean.getData()){
            finish();
            return;
        }
        dismissDialog();
        mData = bean;

        amount = mData.getData().getGameInfo().getNeedThreshold();

        rxDialogSureCancel.setContent("你将花费" + amount + "阅券发起该次拼手气小游戏，希望你是那个幸运儿哟");
        StringBuilder sb = new StringBuilder();
        sb.append(amount);
        sb.append("阅券/次");
        tv_amount.setText(sb.toString());
        kouhao_tv.setText(mData.getData().getGameInfo().getTitle());
        RxImageTool.loadLogoImage(ActivityFightLuckDetail.this,mData.getData().getGameInfo().getPublisherHead(),head_iv);
        name_tv.setText(mData.getData().getGameInfo().getPublisherNickName());
        all_amount_tv.setText(String.valueOf(mData.getData().getGameInfo().getPond()) + "阅券");
        rule_content_tv.setText(mData.getData().getGameInfo().getRule());

        //参与人信息
        List<String> list = new ArrayList<>();
        for(String item : bean.getData().getGameInfo().getPartJoinUserInfo()){
            list.add(item);
        }
        if(null != list && !list.isEmpty()){
            String[] arrString = (String[])list.toArray(new String[list.size()]);
            headview_counttv.initViewData(arrString,String.valueOf(bean.getData().getGameInfo().getJoinInCount()),String.format(getString(R.string.等X人参与),String.valueOf(bean.getData().getGameInfo().getJoinInCount())),String.valueOf(bean.getData().getGameInfo().getJoinInCount()).length());
        }

        //状态判断
        if(mData.getData().isIsOver()){
            //已结束下面三种状态
            join_iv.setEnabled(false);
            time_tv.setVisibility(View.GONE);
            //获得者视图
            owner_rl.setVisibility(View.VISIBLE);
            RxImageTool.loadLogoImage(ActivityFightLuckDetail.this,mData.getData().getPrizeInfo().getPrizeUserHead(),owner_head_iv);
            if(TextUtils.isEmpty(mData.getData().getPrizeInfo().getPrizeUserNickName())){
                //延迟一秒刷新页面
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            refreshCount ++;
                            if(refreshCount < 5) {
                                refresh();
                            }
                        } catch (Exception e) {
                        }
                    }
                }, 1000);
            }
            owner_name_tv.setText(mData.getData().getPrizeInfo().getPrizeUserNickName());
            StringBuilder ownerDes = new StringBuilder();
            ownerDes.append("成为大赢家，获得");
            ownerDes.append(mData.getData().getPrizeInfo().getPrize());
            ownerDes.append("阅券");
            SpannableString contentSpan=new SpannableString(ownerDes.toString());
            contentSpan.setSpan(new TextAppearanceSpan(ActivityFightLuckDetail.this,R.style.StyleReplyNormal),0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            contentSpan.setSpan(new TextAppearanceSpan(ActivityFightLuckDetail.this,R.style.StyleBountyRedNormal),8,ownerDes.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            owner_des_tv.setText(contentSpan);

            if(mData.getData().isIsJoin()){
                if(mData.getData().isIsPrize()){
                    //已结束已中奖
                    join_iv.setImageResource(R.mipmap.fightluck_status5);
                }else{
                    //已结束未中奖
                    join_iv.setImageResource(R.mipmap.fightluck_status4);
                }
            }else{
                //已结束未参与
                join_iv.setImageResource(R.mipmap.fightluck_status3);
            }
        }else{
            //未结束下面两种状态
            if(mData.getData().isIsJoin()){
                join_iv.setEnabled(false);
                join_iv.setImageResource(R.mipmap.fightluck_status2);
            }else{
                join_iv.setEnabled(true);
                join_iv.setImageResource(R.mipmap.fightluck_status1);
            }
            time_tv.setVisibility(View.VISIBLE);
            if(null != myCountDownTimer)
            myCountDownTimer.cancel();
            myCountDownTimer=new MyCountDownTimerForGame(mData.getData().getGameInfo().getRemainSecond()*1000 + 3000,1000l,this,time_tv,"已结束");
            myCountDownTimer.setCall(new MyCountDownTimerForGame.finishCall() {
                @Override
                public void onFinish() {
                    refresh();
                }
            });
            myCountDownTimer.start();
            //获得者视图
            owner_rl.setVisibility(View.GONE);

        }

    }

    @Override
    public void luckFollowSuccess(LuckInfoFollowBean bean) {
        if(null == bean) return;
        if(null == bean.getData()) return;
        dismissDialog();
    }

    @Override
    public void luckJoinSuccess(BaseBean bean) {
        dismissDialog();
        refresh();
    }
}
