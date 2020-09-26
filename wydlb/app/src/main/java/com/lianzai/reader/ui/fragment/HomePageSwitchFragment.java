package com.lianzai.reader.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.FindIntervalRewardBean;
import com.lianzai.reader.bean.ReceiveIntervalRewardBean;
import com.lianzai.reader.bean.TabEntity;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.inner.MyCountDownTimerForMMSS;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.taskCenter.ActivityTaskCenter;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogQiandaoReward;
import com.lianzai.reader.view.tab.listener.CustomTabEntity;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 首页-用于切换
 */
public class HomePageSwitchFragment extends BaseFragment {
    //时段奖励视图
    @Bind(R.id.get_reward_tv)
    TextView get_reward_tv;
    @Bind(R.id.notget_reward_tv)
    TextView notget_reward_tv;
    @Bind(R.id.more_reward_iv)
    View more_reward_iv;
    private MyCountDownTimerForMMSS myCountDownTimer;
    //时段奖励，大钻石弹窗
    RxDialogQiandaoReward rxDialogQiandaoReward;

    @Bind(R.id.viewpager)
    ViewPager viewPager;
    TabFragmentAdapter tabFragmentAdapter;
    List<Fragment> fragments = new ArrayList<>();

    BookListFragment bookListFragment;

    AdForExampleFragment adForExampleFragment;

    @Bind(R.id.tv_my_book)
    TextView tv_my_book;

    @Bind(R.id.tv_my_book_list)
    TextView tv_my_book_list;

    @Bind(R.id.view_slide)
    View view_slide;

    @Bind(R.id.red_dongtai_view)
    View red_dongtai_view;


    int pageIndex=0;

    private boolean needRefresh = false;
    private boolean canClick = false;

    @Override
    public int getLayoutResId() {
        return R.layout.homepage_fragment;
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
    public void configViews(Bundle savedInstanceState) {
        rxDialogQiandaoReward = new RxDialogQiandaoReward(getActivity());
        //请求数据
        findIntervalRewardRequest();

        fragments.clear();
        bookListFragment = new BookListFragment();
        adForExampleFragment = new AdForExampleFragment();
        fragments.add(bookListFragment);
        fragments.add(adForExampleFragment);

        String [] mTitles={"推荐","动态"};
        ArrayList<CustomTabEntity>mTabEntities=new ArrayList<>();
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i],0,0));
        }
        tabFragmentAdapter=new TabFragmentAdapter(getChildFragmentManager());
        tabFragmentAdapter.setTitles(mTitles);
        tabFragmentAdapter.setFragments(fragments);
        viewPager.setAdapter(tabFragmentAdapter);

        //是否展示动态红点
        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_RED_DONGTAI,true)){
            red_dongtai_view.setVisibility(View.VISIBLE);
        }else {
            red_dongtai_view.setVisibility(View.GONE);
        }

        tv_my_book.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //此处回调之后，里面对象可能为空

                    tv_my_book.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(null == view_slide) return;
                                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams( RxImageTool.dip2px(16), RxImageTool.dip2px(3));
                                layoutParams.setMargins( RxImageTool.dp2px(21),0,0,0);
                                view_slide.setLayoutParams(layoutParams);
                            }catch (Exception e){

                            }
                        }
                    });

                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv_my_book.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }catch (Exception e){

                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clickTabByIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tv_my_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTabByIndex(0);
            }
        });

        tv_my_book_list.setOnClickListener(
                v->{
                    clickTabByIndex(1);
                }
        );

    }

    public void refresh(){
        if(null != bookListFragment && null != adForExampleFragment){
            try{
                findIntervalRewardRequest();
                bookListFragment.getData();
                adForExampleFragment.getData();
            }catch (Exception e){
            }
        }
    }

    /**
     * 导航栏切换
     * @param index
     */
    private void clickTabByIndex(int index){
        if(index == 1){
            RxSharedPreferencesUtil.getInstance().putBoolean(Constant.FIRST_RED_DONGTAI,false);
            red_dongtai_view.setVisibility(View.GONE);
        }

        if (pageIndex==index)return;

        pageIndex=index;
        int translateX=0;
        if (pageIndex==0){
            tv_my_book.setTextColor(getResources().getColor(R.color.normal_text_color));
            tv_my_book_list.setTextColor(getResources().getColor(R.color.third_text_color));
            tv_my_book.setTextSize(26);
            tv_my_book_list.setTextSize(16);
        }else if(pageIndex==1){
            tv_my_book.setTextColor(getResources().getColor(R.color.third_text_color));
            tv_my_book_list.setTextColor(getResources().getColor(R.color.normal_text_color));
            translateX=RxImageTool.dip2px(47);
            tv_my_book.setTextSize(16);
            tv_my_book_list.setTextSize(26);
        }

        //此时切换需要刷新数据
        if(pageIndex == 1 && needRefresh){
            needRefresh = false;
            if( null != adForExampleFragment){
                try{
                    adForExampleFragment.getData();
                }catch (Exception e){
                }
            }
        }

        viewPager.setCurrentItem(pageIndex);

        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(view_slide, "translationX", translateX);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(translateAnim);
        animatorSet.setDuration(300);
        animatorSet.start();
    }




    @Override
    public void fetchData() {

    }


    @OnClick(R.id.get_reward_tv) void getClick(){
            if(RxLoginTool.isLogin()) {
                if(canClick){
                receiveIntervalRewardRequest();
                }
            }else {
                ActivityLoginNew.startActivity(getActivity());
            }

    }

    @OnClick(R.id.notget_reward_tv) void notGetClick(){
        if(RxLoginTool.isLogin()) {
            if(canClick){
                receiveIntervalRewardRequest();
            }
        }else {
            ActivityLoginNew.startActivity(getActivity());
        }
    }

    @OnClick(R.id.more_reward_iv) void moreClick(){
        if(RxLoginTool.isLogin()) {
            ActivityTaskCenter.startActivity(getActivity(),0);
        }else {
            ActivityLoginNew.startActivity(getActivity());
        }
    }

    public void findIntervalRewardRequest(){
        canClick = false;
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/account/findIntervalReward", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    get_reward_tv.setVisibility(View.VISIBLE);
                    notget_reward_tv.setVisibility(View.GONE);
                    more_reward_iv.setVisibility(View.GONE);

                    canClick = true;
                    get_reward_tv.setText("领取8推荐票");
                }catch (Exception ee){

                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    FindIntervalRewardBean findIntervalRewardBean= GsonUtil.getBean(response,FindIntervalRewardBean.class);
                    if (findIntervalRewardBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if(findIntervalRewardBean.getData().getIsReceive() == 1) {
                            if(null != myCountDownTimer) {
                                myCountDownTimer.cancel();
                            }
                            get_reward_tv.setVisibility(View.VISIBLE);
                            notget_reward_tv.setVisibility(View.GONE);
                            more_reward_iv.setVisibility(View.GONE);

                            canClick = true;
                            get_reward_tv.setText("领取" + String.valueOf(findIntervalRewardBean.getData().getRewardNum()) + "推荐票");
                        }else {
                            if(null != myCountDownTimer) {
                                myCountDownTimer.cancel();
                            }
                            get_reward_tv.setVisibility(View.GONE);
                            notget_reward_tv.setVisibility(View.VISIBLE);
                            more_reward_iv.setVisibility(View.VISIBLE);

                            long time = findIntervalRewardBean.getData().getNextInterval();
                            myCountDownTimer=new MyCountDownTimerForMMSS(time*1000,1000l,getContext(),notget_reward_tv,"可领取");
                            myCountDownTimer.setCall(new MyCountDownTimerForMMSS.finishCall() {
                                @Override
                                public void onFinish() {
                                    findIntervalRewardRequest();
                                }
                            });
                            myCountDownTimer.start();
                        }
                    }else{//加载失败
                        if(null != myCountDownTimer) {
                            myCountDownTimer.cancel();
                        }
                        get_reward_tv.setVisibility(View.VISIBLE);
                        notget_reward_tv.setVisibility(View.GONE);
                        more_reward_iv.setVisibility(View.GONE);

                        canClick = true;
                        get_reward_tv.setText("领取8推荐票");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void receiveIntervalRewardRequest(){
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/account/receiveIntervalReward", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    RxToast.custom("领取失败").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    ReceiveIntervalRewardBean baseBean= GsonUtil.getBean(response,ReceiveIntervalRewardBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        //请求接口展示弹窗
                        getJinZuanRequest(String.valueOf((int) baseBean.getData().getRewardNum()),"恭喜获得");
                        //弹窗的同时就去刷接口
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //延迟50ms再刷新接口
                                    findIntervalRewardRequest();
                                } catch (Exception e) {
                                    RxLogTool.e("delay request:" + e.getMessage());
                                }
                            }
                        }, 50);
                    }else{//加载失败
                        RxToast.custom(baseBean.getMsg()).show();
                        //弹窗的同时就去刷接口
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //延迟50ms再刷新接口
                                    findIntervalRewardRequest();
                                } catch (Exception e) {
                                    RxLogTool.e("delay request:" + e.getMessage());
                                }
                            }
                        }, 50);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    RxToast.custom("领取失败").show();
                    //弹窗的同时就去刷接口
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //延迟50ms再刷新接口
                                findIntervalRewardRequest();
                            } catch (Exception e) {
                                RxLogTool.e("delay request:" + e.getMessage());
                            }
                        }
                    }, 50);
                }
            }
        });
    }


    /**
     * 推荐票弹出层配置接口
     * 投放位置 1首页Banner 2任务中心Banner 3推荐票领取弹出层 4发现页banner 默认为1
     */
    private void getJinZuanRequest(String count,String des) {
        Map<String, String> map = new HashMap<>();
        map.put("putPosition", String.valueOf(3));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/banner/getBanner", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                //请求失败的情况下，只弹窗，不展示广告
                rxDialogQiandaoReward.getTv_description().setText(count);
                rxDialogQiandaoReward.getQiandao_des_tv().setText(des);
                rxDialogQiandaoReward.setBannerBean(null);

                rxDialogQiandaoReward.show();
            }
            @Override
            public void onResponse(String response) {
                try {
                    BannerBean bannerBean = GsonUtil.getBean(response, BannerBean.class);
                    if (bannerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null != bannerBean.getData() && !bannerBean.getData().isEmpty()) {
                            BannerBean.DataBean temp = bannerBean.getData().get(0);
                            rxDialogQiandaoReward.getTv_description().setText(count);
                            rxDialogQiandaoReward.getQiandao_des_tv().setText(des);

                            rxDialogQiandaoReward.setBannerBean(temp);

                            rxDialogQiandaoReward.show();
                            return;
                        }
                    }

                    //请求失败的情况下，只弹窗，不展示广告
                    rxDialogQiandaoReward.getTv_description().setText(count);
                    rxDialogQiandaoReward.getQiandao_des_tv().setText(des);

                    rxDialogQiandaoReward.setBannerBean(null);

                    rxDialogQiandaoReward.show();

                } catch (Exception e) {
                    //请求失败的情况下，只弹窗，不展示广告
                    rxDialogQiandaoReward.getTv_description().setText(count);
                    rxDialogQiandaoReward.getQiandao_des_tv().setText(des);

                    rxDialogQiandaoReward.setBannerBean(null);

                    rxDialogQiandaoReward.show();
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != myCountDownTimer)
            myCountDownTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public AdForExampleFragment getAdForExampleFragment() {
        return adForExampleFragment;
    }

    public void setAdForExampleFragment(AdForExampleFragment adForExampleFragment) {
        this.adForExampleFragment = adForExampleFragment;
    }

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }
}
