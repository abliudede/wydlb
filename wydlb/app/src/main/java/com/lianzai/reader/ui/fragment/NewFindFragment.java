package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.GetReceiveFlagBean;
import com.lianzai.reader.bean.ObserverBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivityBountyHunter;
import com.lianzai.reader.ui.activity.ActivityLogin;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityOneKeyReward;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.ItemLuckyBox;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import okhttp3.Call;


/**
 * 新版发现页
 */
public class NewFindFragment extends BaseFragment {

    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.vf_notice_scroll)
    ViewFlipper vf_notice_scroll;


    RxDialogGoLogin rxDialogGoLogin;//去登录弹窗

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_new_find;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        rxDialogGoLogin = new RxDialogGoLogin(getActivity(), R.style.OptionDialogStyle);
        //弹登录提示
        rxDialogGoLogin.getTv_sure().setOnClickListener(
                view -> {
                    rxDialogGoLogin.dismiss();
                    //直接跳登录页，不关闭之前页面
                    ActivityLoginNew.startActivity(getActivity());
                }
        );

        lotteryRecordBarrageRequest();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void fetchData() {

    }

    private String boxUrl = Constant.H5_BASE_URL + "/treasureBox/#/";
    @OnClick(R.id.rl_lucky_box)
    void rl_lucky_boxClick(){
        if (RxLoginTool.isLogin()){
            //跳转到开宝箱
            ActivityWebView.startActivity(getActivity(), boxUrl,1);
        }else{
            rxDialogGoLogin.show();
        }
    }

    @OnClick(R.id.rl_bounty_hunter)
    void rl_bounty_hunterClick(){
        if (RxLoginTool.isLogin()){
            RxActivityTool.skipActivity(getActivity(),ActivityBountyHunter.class);
        }else{
            rxDialogGoLogin.show();
        }
    }

    @OnClick(R.id.rl_reward)
    void rl_rewardClick(){
        if (RxLoginTool.isLogin()){
            RxActivityTool.skipActivity(getActivity(),ActivityOneKeyReward.class);
        }else{
            rxDialogGoLogin.show();
        }
    }

    /**
     * 进聊天室请求旧弹幕记录
     */
    private void lotteryRecordBarrageRequest() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/littleGame/lotteryRecordBarrage", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("gameSwitchRequest:" + response);
                    ObserverBean observerBean = GsonUtil.getBean(response, ObserverBean.class);
                    if (observerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        vf_notice_scroll.clearAnimation();
                        vf_notice_scroll.removeAllViews();
                        List<ObserverBean.DataBeanX> ids = new ArrayList<ObserverBean.DataBeanX>();
                        ids.addAll(observerBean.getData());
                        for (int i = 0; i < ids.size(); i++) {
                            ItemLuckyBox iview= new ItemLuckyBox(getActivity());
                            iview.bindData(ids.get(i));
                            vf_notice_scroll.addView(iview);
                        }
                        vf_notice_scroll.setInAnimation(getActivity(),R.anim.notice_in);
                        vf_notice_scroll.setOutAnimation(getActivity(),R.anim.notice_out);
                        //设置多少秒切换
                        vf_notice_scroll.setFlipInterval(3000);
                        vf_notice_scroll.startFlipping();

                    } else {
                        RxToast.custom(observerBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
