package com.lianzai.reader.ui.activity.PersonHomePage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CirclePersonBean;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.bean.GetUserPersonalInfoBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapPerson;
import com.lianzai.reader.ui.activity.taskCenter.ActivityFaceToFace;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.ui.contract.UserAttentionContract;
import com.lianzai.reader.ui.fragment.MyDynamicFragmentNotRefresh;
import com.lianzai.reader.ui.fragment.PersonHomeFragment;
import com.lianzai.reader.ui.presenter.UserAttentionPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogPersonOpt;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/*
 * 个人主页
 * */
public class PerSonHomePageActivity extends BaseActivity implements UserAttentionContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    String userId;

    @Bind(R.id.img_back)
    ImageView img_back;
    @Bind(R.id.img_more)
    ImageView img_more;

    @Bind(R.id.iv_user_logo)
    CircleImageView iv_user_logo;
    @Bind(R.id.iv_head_bg)
    ImageView iv_head_bg;
    @Bind(R.id.tv_user_role)
    TextView tv_user_role;


    @Bind(R.id.tv_user_name)
    TextView tv_user_name;
    @Bind(R.id.iv_sex)
    ImageView iv_sex;
    @Bind(R.id.tv_user_intro)
    TextView tv_user_intro;

    @Bind(R.id.tv_read_time)
    TextView tv_read_time;
    //    @Bind(R.id.tv_read_book)
//     TextView tv_read_book;
    @Bind(R.id.tv_follow)
    TextView tv_follow;
    @Bind(R.id.tv_follower)
    TextView tv_follower;

    @Bind(R.id.iv_edit)
    ImageView iv_edit;
    //    @Bind(R.id.iv_chat)
//     ImageView iv_chat;
    @Bind(R.id.iv_attention)
    ImageView iv_attention;

    private AccountDetailBean accountDetail;

    private GetUserPersonalInfoBean mData;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.appbar)
    AppBarLayout appbar;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_circle_dynamic)
    TextView tv_circle_dynamic;

    @Bind(R.id.tv_circle_dynamic_hot)
    TextView tv_circle_dynamic_hot;

    @Bind(R.id.view_slide)
    View view_slide;

    @Bind(R.id.mask_view)
    View mask_view;//遮罩

    int pageIndex = 0;

    TabFragmentAdapter tabFragmentAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    PersonHomeFragment personHomePageFragment;
    MyDynamicFragmentNotRefresh myDynamicFragmentNotRefresh;

    @Inject
    UserAttentionPresenter userAttentionPresenter;
    private float scaleLarge;
    private int translateX;
    private int translateXAnimation;

    RxDialogPersonOpt rxDialogPersonOpt;
    RxDialogSureCancelNew rxDialogSureCancelNew;

    private WbShareHandler shareHandler;

    public static void startActivity(Context context, String userId) {
        Intent intent = new Intent(context, PerSonHomePageActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_homepage;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
        accountDetail = RxTool.getAccountBean();
        requestUserPersonalInfo();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }
        //状态栏字体设置成黑色
        SystemBarUtils.setLightStatusBar(this, true);

        userAttentionPresenter.attachView(this);

        //微博初始化
        shareHandler = new WbShareHandler(PerSonHomePageActivity.this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        try {
            userId = getIntent().getStringExtra("userId");
        } catch (Exception e) {

        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));
        initTabViews();

        personHomePageFragment = new PersonHomeFragment();
        myDynamicFragmentNotRefresh = MyDynamicFragmentNotRefresh.newInstance(userId);
        fragmentList.add(myDynamicFragmentNotRefresh);
        fragmentList.add(personHomePageFragment);
        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        tabFragmentAdapter.setFragments(fragmentList);
        viewPager.setAdapter(tabFragmentAdapter);

        rxDialogPersonOpt = new RxDialogPersonOpt(this, R.style.BottomDialogStyle);
        rxDialogPersonOpt.getTv_share_jubao().setOnClickListener(
                v -> {
                    //举报，直接调用举报接口
                    againstCircleUser("8",userId);
                    rxDialogPersonOpt.dismiss();
                }
        );
        rxDialogPersonOpt.getTv_share_heimingdan().setOnClickListener(
                v -> {
                    rxDialogSureCancelNew.show();
                    rxDialogPersonOpt.dismiss();
                }
        );
        rxDialogPersonOpt.getTv_share_kouling().setOnClickListener(
                v -> {
                    //连载口令
                    ActivityShareBitmapPerson.startActivity(PerSonHomePageActivity.this,userId);
                    rxDialogPersonOpt.dismiss();
                }
        );


        rxDialogSureCancelNew = new RxDialogSureCancelNew(this);



        img_back.setOnClickListener(
                v -> {
                    backClick();
                }
        );
        img_more.setOnClickListener(
                v -> {
                    showShareBarDialog(1018,Integer.parseInt(userId));
                }
        );

    }

    public View getMask_view() {
        return mask_view;
    }

    public void setMask_view(View mask_view) {
        this.mask_view = mask_view;
    }


    private void showSwipeRefresh(boolean isShow) {
        try {
            if (null != mSwipeRefreshLayout) {
                mSwipeRefreshLayout.setRefreshing(isShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTabViews() {

        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                RxLogTool.e("verticalOffset:" + verticalOffset + "---total:" + appBarLayout.getTotalScrollRange());
                if (verticalOffset == 0) {//完全展开的位置
                    try {
                        if (null != mSwipeRefreshLayout) {
                            mSwipeRefreshLayout.setEnabled(true);
                        }
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        if (null != mSwipeRefreshLayout) {
                            mSwipeRefreshLayout.setEnabled(false);
                        }
                    } catch (Exception e) {
                    }
                }

                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()){
                    toolbar.setBackgroundResource(R.color.white);
                }else {
                    //吸顶位置
                    toolbar.setBackgroundResource(R.color.transparent);
                }
            }
        });

        tv_circle_dynamic.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //此处回调之后，里面对象可能为空

                    tv_circle_dynamic.post(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                if(null == tv_circle_dynamic) return;
                                if(null == view_slide) return;
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tv_circle_dynamic.getMeasuredWidth(), RxImageTool.dip2px(2f));
                                layoutParams.setMargins(RxImageTool.dp2px(16), 0, 0, 0);
                                view_slide.setLayoutParams(layoutParams);
                            }catch (Exception e){

                            }
                        }
                    });

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tv_circle_dynamic.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                } catch (Exception e) {

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

        tv_circle_dynamic.setOnClickListener(
                v -> {
                    clickTabByIndex(0);
                }
        );

        tv_circle_dynamic_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTabByIndex(1);
            }
        });

        setCircleDynamicText("动态");
    }

    /**
     * 导航栏切换
     *
     * @param index
     */
    private void clickTabByIndex(int index) {
        if (pageIndex == index) return;
        pageIndex = index;

        if (pageIndex == 0) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff999999));
        } else if (pageIndex == 1) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff333333));
        }
        viewPager.setCurrentItem(pageIndex);

        //改变长度和位置的方法
        if (pageIndex == 0) {
            view_slide.setScaleX(1f);
            view_slide.setTranslationX(0);
        } else {
            view_slide.setScaleX(1f/scaleLarge);
            view_slide.setTranslationX(translateX);
        }


//        ScaleAnimation scaleAnim;
//        TranslateAnimation rotateAnim;
//        if (pageIndex == 0) {
//            //长短
//            scaleAnim = new ScaleAnimation(1, scaleLarge, 1, 1, Animation.RELATIVE_TO_SELF,1f, Animation.RELATIVE_TO_SELF,0.5f);
//            //左右
//            rotateAnim = new TranslateAnimation(0, -translateX, 0, 0);
//        } else {
//            //长短
//            scaleAnim = new ScaleAnimation(1, 1f/scaleLarge, 1, 1, Animation.RELATIVE_TO_SELF,0f, Animation.RELATIVE_TO_SELF,0.5f);
//            //左右
//            rotateAnim = new TranslateAnimation(0, translateX, 0, 0);
//        }
//
//        rotateAnim.setDuration(300);
//        rotateAnim.setRepeatMode(Animation.RESTART);
//        rotateAnim.setRepeatCount(0);
//        scaleAnim.setDuration(300);
//        scaleAnim.setRepeatMode(Animation.RESTART);
//        scaleAnim.setRepeatCount(0);
//        AnimationSet smallAnimationSet = new AnimationSet(false);
//        smallAnimationSet.addAnimation(scaleAnim);
//        smallAnimationSet.addAnimation(rotateAnim);
//        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                //改变长度和位置的方法
//                if (pageIndex == 0) {
//                    view_slide.setScaleX(1f);
//                    view_slide.setTranslationX(0);
//                } else {
//                    view_slide.setScaleX(1f/scaleLarge);
//                    view_slide.setTranslationX(translateX);
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        view_slide.startAnimation(smallAnimationSet);
    }

    private void changeLen() {
        translateXAnimation = tv_circle_dynamic.getMeasuredWidth() + RxImageTool.dip2px(30f);
        translateX = tv_circle_dynamic.getMeasuredWidth()/2 + RxImageTool.dip2px(30f) + tv_circle_dynamic_hot.getMeasuredWidth()/2;
        scaleLarge = ((float) tv_circle_dynamic.getMeasuredWidth()) / tv_circle_dynamic_hot.getMeasuredWidth();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view_slide.getLayoutParams();
        layoutParams.width = tv_circle_dynamic.getMeasuredWidth();
        view_slide.setLayoutParams(layoutParams);
        //改变长度和位置的方法
        if (pageIndex == 0) {
            view_slide.setScaleX(1f);
            view_slide.setTranslationX(0);
        } else {
            view_slide.setScaleX(1f/scaleLarge);
            view_slide.setTranslationX(translateX);
        }
    }

    @Override
    public void gc() {

    }

    public String getUserId() {
        return userId;
    }

    private void requestUserPersonalInfo() {
        showDialog();
        Map<String, String> map = new HashMap<>();
        map.put("otherUserId", userId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/userAttention/getUserPersonalInfo", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxLogTool.e("requestHistory e:" + e.getMessage());
                    showSwipeRefresh(false);
                    dismissDialog();
                } catch (Exception ee) {

                }

            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    showSwipeRefresh(false);
                    GetUserPersonalInfoBean baseBean = GsonUtil.getBean(response, GetUserPersonalInfoBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        mData = baseBean;
                        RxImageTool.loadLogoImage(PerSonHomePageActivity.this, baseBean.getData().getAvatar(), iv_user_logo);

                        List<GetUserPersonalInfoBean.DataBean.RankInfos> rankInfos = baseBean.getData().getRankInfos();
                        if(null != rankInfos && !rankInfos.isEmpty()){
                            iv_head_bg.setVisibility(View.VISIBLE);
                            tv_user_role.setVisibility(View.VISIBLE);
                        }else {
                            iv_head_bg.setVisibility(View.GONE);
                            tv_user_role.setVisibility(View.GONE);
                        }


                        tv_user_name.setText(baseBean.getData().getNickName());
                        if (baseBean.getData().getGender() == 0) {
                            iv_sex.setImageResource(R.mipmap.nan_icon);
                        } else {
                            iv_sex.setImageResource(R.mipmap.nv_icon);
                        }
                        if (TextUtils.isEmpty(baseBean.getData().getIntroduce())) {
                            tv_user_intro.setVisibility(View.GONE);
                        } else {
                            tv_user_intro.setVisibility(View.VISIBLE);
                            tv_user_intro.setText(baseBean.getData().getIntroduce());
                        }

                        tv_read_time.setText(RxTimeTool.getHourTime(baseBean.getData().getReadTime()));
//                        tv_read_book.setText(String.valueOf(baseBean.getData().getBookListSum()));
                        tv_follow.setText(String.valueOf(baseBean.getData().getAttentionSum()) + "人");
                        tv_follower.setText(String.valueOf(baseBean.getData().getFansSum()) + "人");

                        if (null != accountDetail) {
                            //已登录情况
                            if (accountDetail.getData().getUid().equals(baseBean.getData().getUserId())) {
                                //这是自己的个人主页
                                iv_edit.setVisibility(View.VISIBLE);
                                iv_edit.setImageResource(R.mipmap.icon_hpmepage_edit);
//                                iv_chat.setVisibility(View.GONE);
                                iv_attention.setVisibility(View.GONE);

                                rxDialogPersonOpt.getTv_share_jubao().setVisibility(View.GONE);
                                rxDialogPersonOpt.getTv_share_heimingdan().setVisibility(View.GONE);
                            } else {
                                //这是别人的个人主页
                                iv_edit.setVisibility(View.GONE);
//                                iv_chat.setVisibility(View.VISIBLE);
                                iv_attention.setVisibility(View.VISIBLE);

                                rxDialogPersonOpt.getTv_share_jubao().setVisibility(View.VISIBLE);
                                rxDialogPersonOpt.getTv_share_heimingdan().setVisibility(View.VISIBLE);

                                //s根据状态值判断拉黑按钮
                                switch(baseBean.getData().getIsBlack()){
                                    case 0:
                                        //拉黑，先弹出确认弹窗，然后再调用拉黑接口并刷新
                                        rxDialogSureCancelNew.getTitleView().setText("加入黑名单");
                                        rxDialogSureCancelNew.getContentView().setText("确定要将Ta加入到黑名单么？拉黑后，将看不到此人所有动态、评论与回复");
                                        rxDialogSureCancelNew.getSureView().setText("确定");
                                        rxDialogSureCancelNew.getCancelView().setText("取消");
                                        rxDialogSureCancelNew.getSureView().setOnClickListener(
                                                v1 -> {
                                                    //调用拉黑接口之后刷新
                                                    userShielding(userId);
                                                    rxDialogSureCancelNew.dismiss();
                                                }
                                        );
                                        rxDialogPersonOpt.getTv_share_heimingdan().setText("加入黑名单");

                                        break;
                                    case 1:
                                        //取消拉黑先弹窗，接口并刷新
                                        rxDialogSureCancelNew.getTitleView().setText("解除黑名单");
                                        rxDialogSureCancelNew.getContentView().setText("确定要将该用户从黑名单内移除");
                                        rxDialogSureCancelNew.getSureView().setText("确定");
                                        rxDialogSureCancelNew.getCancelView().setText("取消");
                                        rxDialogSureCancelNew.getSureView().setOnClickListener(
                                                v1 -> {
                                                    //调用取消拉黑接口之后刷新
                                                    cancelShielding(userId);
                                                    rxDialogSureCancelNew.dismiss();
                                                }
                                        );
                                        rxDialogPersonOpt.getTv_share_heimingdan().setText("取消拉黑");

                                        break;
                                }


                                //关注状态开关
                                switch (baseBean.getData().getAttentionStatus()) {
                                    case 0:
//                                        iv_chat.setImageResource(R.mipmap.icon_hpmepage_chatwhite);
                                        iv_attention.setImageResource(R.mipmap.icon_hpmepage_attention_blue);
                                        break;
                                    case 1:
//                                        iv_chat.setImageResource(R.mipmap.icon_hpmepage_chatblue);
                                        iv_attention.setImageResource(R.mipmap.icon_hpmepage_attentioned);
                                        break;
                                    case 2:
//                                        iv_chat.setImageResource(R.mipmap.icon_hpmepage_chatblue);
                                        iv_attention.setImageResource(R.mipmap.icon_hpmepage_attentionover);
                                        break;
                                }
                            }
                        } else {
                            //未登录情况
                            iv_edit.setVisibility(View.GONE);
//                            iv_chat.setVisibility(View.GONE);
                            iv_attention.setVisibility(View.GONE);

                            rxDialogPersonOpt.getTv_share_jubao().setVisibility(View.GONE);
                            rxDialogPersonOpt.getTv_share_heimingdan().setVisibility(View.GONE);
                        }

                        if (null != personHomePageFragment) {
                            personHomePageFragment.bindData(baseBean);
                        }

                    }
                } catch (Exception e) {
                    RxLogTool.e("requestHistory e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 我的关注列表跳转
     */
    @OnClick(R.id.tv_follow)
    void tv_followClick() {

        if (null == mData) {
            RxToast.custom("信息尚未获取到").show();
            return;
        }

        if (null != accountDetail) {
            //已登录情况并且是自己的主页
            if (accountDetail.getData().getUid().equals(mData.getData().getUserId())) {
                ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, 0, 1);
                return;
            }
        }
        ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, Integer.parseInt(mData.getData().getUserId()), 3);
    }

    /**
     * 我的关注列表跳转
     */
    @OnClick(R.id.tv_follow2)
    void tv_follow2Click() {

        if (null == mData) {
            RxToast.custom("信息尚未获取到").show();
            return;
        }

        if (null != accountDetail) {
            //已登录情况并且是自己的主页
            if (accountDetail.getData().getUid().equals(mData.getData().getUserId())) {
                ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, 0, 1);
                return;
            }
        }
        ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, Integer.parseInt(mData.getData().getUserId()), 3);
    }

    /**
     * 我的粉丝列表跳转
     */
    @OnClick(R.id.tv_follower)
    void tv_followerClick() {

        if (null == mData) {
            RxToast.custom("信息尚未获取到").show();
            return;
        }

        if (null != accountDetail) {
            //已登录情况并且是自己的主页
            if (accountDetail.getData().getUid().equals(mData.getData().getUserId())) {
                ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, 0, 2);
                return;
            }
        }
        ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, Integer.parseInt(mData.getData().getUserId()), 4);
    }

    /**
     * 我的粉丝列表跳转
     */
    @OnClick(R.id.tv_follower2)
    void tv_follower2Click() {
        if (null == mData) {
            RxToast.custom("信息尚未获取到").show();
            return;
        }

        if (null != accountDetail) {
            //已登录情况并且是自己的主页
            if (accountDetail.getData().getUid().equals(mData.getData().getUserId())) {
                ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, 0, 2);
                return;
            }
        }
        ActivityAttentionPersonList.startActivity(PerSonHomePageActivity.this, Integer.parseInt(mData.getData().getUserId()), 4);
    }

    //编辑按钮
    @OnClick(R.id.iv_edit)
    void iv_editClick() {
        if (RxLoginTool.isLogin()) {
            RxActivityTool.skipActivity(PerSonHomePageActivity.this, ActivityUserProfile.class);
        }
    }

    //关注按钮
    @OnClick(R.id.iv_attention)
    void iv_attentionClick() {
        if (null == mData) {
            RxToast.custom("信息尚未获取到").show();
            return;
        }

        if (RxLoginTool.isLogin()) {
            ArrayMap map = new ArrayMap();
            map.put("attentionUserId", mData.getData().getUserId());
            int type;
            switch (mData.getData().getAttentionStatus()) {
                case 0:
                    type = 1;
                    break;
                case 1:
                    type = 2;
                    break;
                case 2:
                    type = 2;
                    break;
                default:
                    type = 1;
                    break;
            }
            map.put("attentionType", type);
            userAttentionPresenter.attentionOrUnattention(map, 0,0);
        }


    }


    @Override
    public void userAttentionSuccess(UserAttentionBean bean) {

    }

    @Override
    public void attentionOrUnattentionSuccess(AttentionBean bean, int pos,int type) {
        requestUserPersonalInfo();
        //刷新我的fragment
        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_USER_MINE_TAG);
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }

    @Override
    public void onRefresh() {
        requestUserPersonalInfo();
        if (null != myDynamicFragmentNotRefresh) {
            myDynamicFragmentNotRefresh.circleDynamicRequest(true);
        }
    }


    public void setCircleDynamicText(String text) {
        tv_circle_dynamic.setText(text);
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if(null == tv_circle_dynamic) return;
                    changeLen();
                }catch (Exception e){

                }
            }
        }, 150);
    }

    /**
     * 举报圈子用户
     */
    private void againstCircleUser(String source,String userId){
        HashMap map=new HashMap();
        map.put("source",source);
        map.put("targetId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/against/circleUser",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("举报成功").show();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 拉黑用户
     */
    private void userShielding (String userId){
        HashMap map=new HashMap();
        map.put("shieldingUserId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/circles/userShielding",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("拉黑成功").show();
                        onRefresh();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消拉黑用户
     */
    private void cancelShielding (String userId){
        HashMap map=new HashMap();
        map.put("shieldingUserId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/circles/cancelShielding",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("取消拉黑成功").show();
                        onRefresh();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 分享bar详情框
     * ///微信
     case wechatSession = 1
     ///朋友圈
     case wechatTimeLine= 2
     ///qq
     case QQ = 3
     ///qq空间
     case qzone = 4
     ///微博
     case sina = 5
     ///复制链接
     case copyUrl = 6
     //打开浏览器
     case openWeb = 7
     //面对面邀请
     case faceToFace = 8
     //短信
     case SMS = 9
     */
    private void showShareBarDialog(int code,int id) {

        rxDialogPersonOpt.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    getCommonShareUrl(code,1,id);
                    rxDialogPersonOpt.dismiss();
                }
        );
        rxDialogPersonOpt.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    getCommonShareUrl(code,3,id);
                    rxDialogPersonOpt.dismiss();
                }
        );
        rxDialogPersonOpt.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    getCommonShareUrl(code,2,id);
                    rxDialogPersonOpt.dismiss();
                }
        );
        rxDialogPersonOpt.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    getCommonShareUrl(code,5,id);
                    rxDialogPersonOpt.dismiss();
                }
        );

        rxDialogPersonOpt.show();
    }

    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl(int code,int shareMode,int id){

        Map<String,String> map=new HashMap<>();
        map.put("code", String.valueOf(code));
        map.put("shareMode", String.valueOf(shareMode));
        if(id != 0)
            map.put("objId", String.valueOf(id));

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/common/share/getCommonShareUrl" ,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }

            }
            @Override
            public void onResponse(String response) {
                try{
                    GetCommonShareUrlBean baseBean= GsonUtil.getBean(response,GetCommonShareUrlBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

                        switch (shareMode){
                            case 1:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                }else {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //需要在子线程中处理的逻辑
                                            File file1 = new File(RxImageTool.getImgPathFromCache(baseBean.getData().getHeadVal(), getApplicationContext()));
                                            RxShareUtils.shareWebUrl(file1, baseBean.getData().getOneUrlVal(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), BuglyApplicationLike.getsInstance().api, true);
                                        }
                                    }.start();
                                }
                                break;
                            case 2:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                }else {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //需要在子线程中处理的逻辑
                                            File file2 = new File(RxImageTool.getImgPathFromCache(baseBean.getData().getHeadVal(), getApplicationContext()));
                                            RxShareUtils.shareWebUrl(file2, baseBean.getData().getOneUrlVal(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), BuglyApplicationLike.getsInstance().api, false);
                                        }
                                    }.start();
                                }
                                break;
                            case 3:
                                RxShareUtils.QQShareUrl(PerSonHomePageActivity.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(PerSonHomePageActivity.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, PerSonHomePageActivity.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(PerSonHomePageActivity.this,baseBean.getData().getOneUrlVal());
                                RxToast.custom("复制成功").show();
                                break;

                            case 8:
                                //面对面
                                ActivityFaceToFace.startActivity(PerSonHomePageActivity.this,baseBean.getData().getOneUrlVal());
                                break;

                            case 9:
                                //短信
                                RxToast.custom("发送成功").show();
                                break;

                            default:
                                break;
                        }

                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    RxLogTool.e("requestUnConcernPlatform e:"+e.getMessage());
                }
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
//            RxToast.custom("分享取消").show();
        }

        @Override
        public void onComplete(Object response) {
//            RxToast.custom("分享成功").show();
        }

        @Override
        public void onError(UiError e) {
//            RxToast.custom("分享失败").show();
        }
    };

    @Override
    public void circlePersonSuccess(CirclePersonBean bean) {

    }

}
