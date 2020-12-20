package com.wydlb.first.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.wydlb.first.R;
import com.wydlb.first.base.BaseFragment;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.ObserverBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.ui.activity.ActivityBountyHunter;
import com.wydlb.first.ui.activity.ActivityLoginNew;
import com.wydlb.first.ui.activity.ActivityOneKeyReward;
import com.wydlb.first.ui.activity.ActivityWebView;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.view.ItemLuckyBox;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.dialog.RxDialogGoLogin;

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
