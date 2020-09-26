package com.lianzai.reader.ui.activity.circle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CircleInfoBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.FeedBackTypeResponse;
import com.lianzai.reader.bean.PlacedTopListBean;
import com.lianzai.reader.bean.SearchRandomBean;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.local.BookPayDialogRepository;
import com.lianzai.reader.ui.activity.ActivityBookShortageArtifact2;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.TeamChat.TeamMessageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.listenPay.ActivityPayForBook;
import com.lianzai.reader.ui.adapter.CircleTopAdapter;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.ui.fragment.CircleArticleFragment;
import com.lianzai.reader.ui.fragment.CircleDynamicFragment;
import com.lianzai.reader.ui.fragment.CircleReadFragment;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxReadTimeUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogCheckBox;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Copyright (C), 2019
 * FileName: ActivityCircleDetail
 * Author: lrz
 * Date: 2019/3/5 15:21
 * Description: ${DESCRIPTION}
 */
public class ActivityCircleDetail extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String LIANZAIGUANFANG = "连载官方";

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.appbar)
    AppBarLayout appbar;

    Bitmap blurBitmap;//头部高斯模糊背景
    //圈子头像
    @Bind(R.id.iv_circle_detail_cover)
    SelectableRoundedImageView iv_circle_detail_cover;

    //圈子背景
    @Bind(R.id.iv_blur_img)
    ImageView iv_blur_img;

    //圈子名称
    @Bind(R.id.tv_circle_detail_name)
    TextView tv_circle_detail_name;

    //作者
    @Bind(R.id.tv_book_author)
    TextView tv_book_author;
    //书籍信息栏
    @Bind(R.id.ly_book_info)
    LinearLayout ly_book_info;
    //粉丝数
    @Bind(R.id.tv_book_fans_count)
    TextView tv_book_fans_count;
    //字数
    @Bind(R.id.tv_book_word_total)
    TextView tv_book_word_total;
    //章节数
    @Bind(R.id.tv_book_chapter_total)
    TextView tv_book_chapter_total;

    //加入圈子按钮和阅读按钮
    @Bind(R.id.ll_circle_detail_operation)
    LinearLayout ll_circle_detail_operation;
    @Bind(R.id.tv_add_circle)
    ImageView tv_add_circle;
    @Bind(R.id.tv_read_book)
    RelativeLayout tv_read_book;
    @Bind(R.id.tv_read_booktv)
    TextView tv_read_booktv;

    @Bind(R.id.tv_circle_detail_description)
    TextView tv_circle_detail_description;
    @Bind(R.id.tv_circle_detail_description2)
    TextView tv_circle_detail_description2;


    @Bind(R.id.view_circle_detail_toolbar)
    LinearLayout view_circle_detail_toolbar;

    @Bind(R.id.ll_book_chapter_count)
    LinearLayout ll_book_chapter_count;

    @Bind(R.id.ll_book_word_count)
    LinearLayout ll_book_word_count;

    @Bind(R.id.ll_book_fans_count)
    LinearLayout ll_book_fans_count;

    @Bind(R.id.rl_circle_detail_header_toolbar)
    LinearLayout rl_circle_detail_header_toolbar;


    @Bind(R.id.iv_circle_back)
    ImageView iv_circle_back;//展开操作，当处于吸顶状态时，点后退键展开当前
//    @Bind(R.id.tv_circle_name)
//    TextView tv_circle_name;//当处于吸顶状态时，展示圈子名称

    @Bind(R.id.iv_circle_more_operation)
    ImageView iv_circle_more_operation;

    @Bind(R.id.iv_circle_member)
    ImageView iv_circle_member;

    @Bind(R.id.iv_circle_more_operation_other)
    ImageView iv_circle_more_operation_other;

    @Bind(R.id.iv_circle_member_other)
    ImageView iv_circle_member_other;


    @Bind(R.id.tv_circle_dynamic)
    TextView tv_circle_dynamic;

    @Bind(R.id.tv_circle_dynamic_hot)
    TextView tv_circle_dynamic_hot;


    @Bind(R.id.tv_circle_read_article)
    TextView tv_circle_read_article;

    @Bind(R.id.tv_circle_read_circle)
    TextView tv_circle_read_circle;
    @Bind(R.id.red_dongtai_view)
    View red_dongtai_view;

    @Bind(R.id.view_slide)
    View view_slide;

    int pageIndex = 0;

    @Bind(R.id.iv_circle_dynamic_add)
    ImageView iv_circle_dynamic_add;

    @Bind(R.id.ll_circle_detail_tags_guanfang)//官方标签
            LinearLayout ll_circle_detail_tags_guanfang;
    @Bind(R.id.ll_circle_detail_tags)//标签
            LinearLayout ll_circle_detail_tags;

    @Bind(R.id.tv_xiangguan_quanzi)//相关圈子
            TextView tv_xiangguan_quanzi;


    @Bind(R.id.iv_circle_payforbook)//打赏按钮
            ImageView iv_circle_payforbook;
    @Bind(R.id.iv_circle_payforbook_other)//打赏按钮
            ImageView iv_circle_payforbook_other;

    @Bind(R.id.iv_circle_copyright_show)//版权上链图标
            ImageView iv_circle_copyright_show;
    @Bind(R.id.iv_circle_copyright_show_other)//版权上链图标
            ImageView iv_circle_copyright_show_other;



    @Bind(R.id.mask_view)
    View mask_view;//遮罩

    //问号
    @Bind(R.id.iv_circle_question)
    ImageView iv_circle_question;

    //引导投资视图
    @Bind(R.id.rl_touzi)
    RelativeLayout rl_touzi;

    String circleId;

    TabFragmentAdapter tabFragmentAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    CircleDynamicFragment circleDynamicFragment;
    CircleDynamicFragment circleDynamicFragment1;
    CircleArticleFragment circleArticleFragment;
    CircleReadFragment circleReadFragment;
    private CircleInfoBean circleInfoBean;

    //提示框，包含chkeckbox
    RxDialogCheckBox rxDialogSureCancel;

    private String bqtKey;
    private String uid;
    private AccountDetailBean accountDetailBean;
    private int tab;
    private boolean up;


    //共享版权相关视图
    @Bind(R.id.gongxiangbanquan_ly)
    LinearLayout gongxiangbanquan_ly;
    @Bind(R.id.vf_notice_scroll1)
    ViewFlipper vf_notice_scroll1;

    //写作挖矿相关视图
    @Bind(R.id.ll_book_ticket_count)
    LinearLayout ll_book_ticket_count;
    @Bind(R.id.tv_book_ticket_total)//推荐票数
            TextView tv_book_ticket_total;
    @Bind(R.id.tv_book_goticket)//投票
            RelativeLayout tv_book_goticket;
    @Bind(R.id.tv_book_investment)//投资
            RelativeLayout tv_book_investment;


    private boolean showZhankai;


    public static void startActivity(Context context, String circleId) {
//        RxActivityTool.removeCircleActivity();
        //未登录直接跳转页面
        Bundle bundle = new Bundle();
        bundle.putString("circleId", circleId);
        RxActivityTool.skipActivity(context, ActivityCircleDetail.class, bundle);
    }

    public static void startActivity(Context context, String circleId, int tab) {
//        RxActivityTool.removeCircleActivity();
        //未登录直接跳转页面
        Bundle bundle = new Bundle();
        bundle.putString("circleId", circleId);
        bundle.putInt("tab", tab);
        RxActivityTool.skipActivity(context, ActivityCircleDetail.class, bundle);
    }

    public static void startActivity(Context context, String circleId, boolean up) {
//        RxActivityTool.removeCircleActivity();
        //未登录直接跳转页面
        Bundle bundle = new Bundle();
        bundle.putString("circleId", circleId);
        bundle.putBoolean("up", up);
        RxActivityTool.skipActivity(context, ActivityCircleDetail.class, bundle);
    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_circle_detail;
    }

    @Override
    protected void onDestroy() {
        RxEventBusTool.unRegisterEventBus(this);
        super.onDestroy();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }


        try {
            circleId = getIntent().getStringExtra("circleId");
            tab = getIntent().getIntExtra("tab", 0);
        } catch (Exception e) {
        }
        try {
            up = getIntent().getBooleanExtra("up", false);
        } catch (Exception e) {
        }



        if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_WHAT_IS_CIRCLE, true)) {
            iv_circle_question.setVisibility(View.VISIBLE);
        } else {
            iv_circle_question.setVisibility(View.GONE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));
        initTabViews();

        //请求圈子信息
        requestCircleInfo(false);
    }

    @Override
    public void initToolBar() {
    }

    @Override
    public void gc() {
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        rl_circle_detail_header_toolbar.setLayoutParams(layoutParams);


        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
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
                RxLogTool.e("verticalOffset:" + verticalOffset + "---total:" + appBarLayout.getTotalScrollRange());
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {//吸顶位置
                    try {
                        if (null == circleInfoBean) {
                            tv_xiangguan_quanzi.setVisibility(View.INVISIBLE);

                            ly_book_info.setVisibility(View.INVISIBLE);
                            ll_circle_detail_tags.setVisibility(View.INVISIBLE);
                            ll_circle_detail_operation.setVisibility(View.INVISIBLE);
                            tv_add_circle.setVisibility(View.INVISIBLE);
                        } else if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || circleInfoBean.getData().getPlatformType() > Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
                            ll_circle_detail_tags_guanfang.setVisibility(View.INVISIBLE);
                        } else if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
                            ly_book_info.setVisibility(View.INVISIBLE);
                            ll_circle_detail_tags.setVisibility(View.INVISIBLE);
                            ll_circle_detail_operation.setVisibility(View.INVISIBLE);
                            tv_add_circle.setVisibility(View.INVISIBLE);
                        } else {
                            tv_xiangguan_quanzi.setVisibility(View.INVISIBLE);

                            ly_book_info.setVisibility(View.INVISIBLE);
                            ll_circle_detail_tags.setVisibility(View.INVISIBLE);
                            ll_circle_detail_operation.setVisibility(View.INVISIBLE);
                            tv_add_circle.setVisibility(View.INVISIBLE);
                        }
                        iv_circle_detail_cover.setVisibility(View.INVISIBLE);
                        tv_circle_detail_description.setVisibility(View.INVISIBLE);
                        tv_circle_detail_description2.setVisibility(View.INVISIBLE);
                        tv_circle_detail_name.setVisibility(View.INVISIBLE);
                        tv_book_author.setVisibility(View.INVISIBLE);
                        view_circle_detail_toolbar.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                    }
                } else {
                    try {
                        if (null == circleInfoBean) {
                            tv_xiangguan_quanzi.setVisibility(View.VISIBLE);

                            ly_book_info.setVisibility(View.VISIBLE);
                            ll_circle_detail_tags.setVisibility(View.VISIBLE);
                            ll_circle_detail_operation.setVisibility(View.VISIBLE);
                            tv_add_circle.setVisibility(View.VISIBLE);
                        } else if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || circleInfoBean.getData().getPlatformType() > Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
                            ll_circle_detail_tags_guanfang.setVisibility(View.VISIBLE);
                        } else if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.CAN_ATTENTION_NUMBER) {

                            ly_book_info.setVisibility(View.VISIBLE);
                            ll_circle_detail_tags.setVisibility(View.VISIBLE);
                            ll_circle_detail_operation.setVisibility(View.VISIBLE);
                            tv_add_circle.setVisibility(View.VISIBLE);
                        } else {
                            tv_xiangguan_quanzi.setVisibility(View.VISIBLE);

                            ly_book_info.setVisibility(View.VISIBLE);
                            ll_circle_detail_tags.setVisibility(View.VISIBLE);
                            ll_circle_detail_operation.setVisibility(View.VISIBLE);
                            tv_add_circle.setVisibility(View.VISIBLE);
                        }
                        iv_circle_detail_cover.setVisibility(View.VISIBLE);
                        tv_circle_detail_description.setVisibility(View.VISIBLE);
                        if (showZhankai)
                            tv_circle_detail_description2.setVisibility(View.VISIBLE);
                        tv_circle_detail_name.setVisibility(View.VISIBLE);
                        tv_book_author.setVisibility(View.VISIBLE);
                        view_circle_detail_toolbar.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }
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
                        try {
                            if (null == tv_circle_dynamic) return;
                            if (null == view_slide) return;
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tv_circle_dynamic.getMeasuredWidth(), RxImageTool.dip2px(2f));
                            layoutParams.setMargins(tv_circle_dynamic.getLeft() + RxImageTool.dp2px(16), tv_circle_dynamic.getTop(), 0, 0);
                            view_slide.setLayoutParams(layoutParams);
                        } catch (Exception e) {

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
                if (position == 2 && tv_circle_read_article.getVisibility() != View.VISIBLE) {
                    clickTabByIndex(position + 1);
                } else {
                    clickTabByIndex(position);
                }
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
        tv_circle_read_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTabByIndex(2);
            }
        });

        tv_circle_read_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTabByIndex(3);
            }
        });


    }

    public View getMask_view() {
        return mask_view;
    }

    public void setMask_view(View mask_view) {
        this.mask_view = mask_view;
    }

    /**
     * 导航栏切换
     *
     * @param index
     */
    private void clickTabByIndex(int index) {
        if (index == 3) {
            RxSharedPreferencesUtil.getInstance().putBoolean(Constant.FIRST_CIRCLE_READ, false);
            red_dongtai_view.setVisibility(View.GONE);
        }


        if (pageIndex == index) return;

        pageIndex = index;
        int translateX = 0;

        if (pageIndex == 0) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_read_article.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_read_circle.setTextColor(getResources().getColor(R.color.color_black_ff999999));
        } else if (pageIndex == 1) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_circle_read_article.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_read_circle.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            translateX = tv_circle_dynamic.getMeasuredWidth() + RxImageTool.dip2px(30f);
        } else if (pageIndex == 2) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_read_article.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_circle_read_circle.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            translateX = tv_circle_dynamic.getMeasuredWidth() * 2 + RxImageTool.dip2px(60f);
        } else if (pageIndex == 3) {
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_read_article.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_read_circle.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            if (tv_circle_read_article.getVisibility() == View.VISIBLE) {
                translateX = tv_circle_dynamic.getMeasuredWidth() * 3 + RxImageTool.dip2px(90f);
            } else {
                translateX = tv_circle_dynamic.getMeasuredWidth() * 2 + RxImageTool.dip2px(60f);
            }
        }

        if (tv_circle_read_article.getVisibility() != View.VISIBLE && pageIndex == 3) {
            viewPager.setCurrentItem(pageIndex - 1);
        } else {
            viewPager.setCurrentItem(pageIndex);
        }

        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(view_slide, "translationX", translateX);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translateAnim);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float radius = 100;//模糊程度
        Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
        view.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        RxLogTool.e("blur time:" + (System.currentTimeMillis() - startMs));
    }

    //不拉起投票框
    @OnClick(R.id.ll_book_ticket_count)
    void toupiaoClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        ActivityWebView.startActivity(this, Constant.H5_BASE_URL + "/vote/#/vote?bookId=" + circleInfoBean.getData().getBookId() + "&bookName=" + circleInfoBean.getData().getPlatformName(), 1);
    }

    //拉起投票框
    @OnClick(R.id.tv_book_goticket)
    void toupiao2Click() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        ActivityWebView.startActivity(this, Constant.H5_BASE_URL + "/vote/#/vote?bookId=" + circleInfoBean.getData().getBookId() + "&bookName=" + circleInfoBean.getData().getPlatformName() + "&isOpen=true", 1);
    }

    //打开投资页面
    @OnClick(R.id.tv_book_investment)
    void touziClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        ActivityWebView.startActivity(ActivityCircleDetail.this, circleInfoBean.getData().getSkipUrl());
    }


    //版权上链点击事件
    @OnClick({R.id.iv_circle_copyright_show, R.id.iv_circle_copyright_show_other})
    void copyrightClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        String signUrl = circleInfoBean.getData().getSignUrl();
        String signUrlPrams = RxReadTimeUtils.getSignUrl(String.valueOf(circleInfoBean.getData().getBookId()));
        ActivityWebView.startActivity(ActivityCircleDetail.this,signUrl + signUrlPrams);
    }

    //打赏点击事件
    @OnClick({R.id.iv_circle_payforbook, R.id.iv_circle_payforbook_other})
    void payForBookClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (!RxLoginTool.isLogin()) {
            ActivityLoginNew.startActivity(ActivityCircleDetail.this);
            return;
        }
        ActivityPayForBook.startActivity(this, String.valueOf(circleInfoBean.getData().getBookId()));
    }

    @OnClick({R.id.iv_circle_back, R.id.iv_circle_header_back})
    void circleBackClick() {
        finish();
    }

    @OnClick(R.id.tv_book_author)
    void tv_book_authorClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }

        //官方群跳连载官方
        if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || circleInfoBean.getData().getPlatformType() > Constant.UserIdentity.CAN_ATTENTION_NUMBER || circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
            ActivitySearchFirst.skiptoSearch("连载官方", ActivityCircleDetail.this);
        } else {
            //跳到网页
            ActivitySearchFirst.skiptoSearch( circleInfoBean.getData().getPenName(), ActivityCircleDetail.this);
        }
    }

    @OnClick({R.id.iv_circle_more_operation, R.id.iv_circle_more_operation_other})
    void circleMoreOperationClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        String accid = circleInfoBean.getData().getAccid();
        String cover = circleInfoBean.getData().getPlatformCover();
        String bookname = circleInfoBean.getData().getPlatformName();
        //需要增加字段
        String days = TimeFormatUtil.getJoinDays(circleInfoBean.getData().getJoinTime());
        String minute = String.valueOf((int) (circleInfoBean.getData().getBookReadTime() / 60));
//        String athourHead = circleInfoBean.getData().getPlatformName();

        int platformType = circleInfoBean.getData().getPlatformType();
        int userType = circleInfoBean.getData().getUserType();
        int personCount = circleInfoBean.getData().getMemberCount();
        int mayApplyRole = circleInfoBean.getData().getMayApplyRole();
        boolean isSigned = circleInfoBean.getData().isSigned();
        String signUrl = circleInfoBean.getData().getSignUrl();
        String signUrlPrams = RxReadTimeUtils.getSignUrl(String.valueOf(circleInfoBean.getData().getBookId()));
        ActivityCircleSetting.startActivity(ActivityCircleDetail.this, circleId, accid, cover, bookname, days, minute, platformType, userType, personCount, mayApplyRole,isSigned,signUrl+signUrlPrams);
    }

    /*加入群聊*/
    @OnClick({R.id.iv_circle_member, R.id.iv_circle_member_other})
    void circleMemberClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (!RxLoginTool.isLogin()) {
            ActivityLoginNew.startActivity(ActivityCircleDetail.this);
            return;
        }
        if (circleInfoBean.getData().isIsInTeam()) {
            TeamMessageActivity.startActivity(ActivityCircleDetail.this, String.valueOf(circleInfoBean.getData().getTeamId()));
        } else {
            joinTeamChat(String.valueOf(circleInfoBean.getData().getTeamId()));
        }
    }

    @OnClick(R.id.iv_circle_question)
    void iv_circle_questionClick() {
        //点击后设置为false，不再显示问号
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.FIRST_WHAT_IS_CIRCLE, false);
        if (Constant.appMode == Constant.AppMode.RELEASE) {
            ActivityWebView.startActivity(ActivityCircleDetail.this, "https://fx.bendixing.net/taskCenter/#/circle");
        } else {
            ActivityWebView.startActivity(ActivityCircleDetail.this, "https://beta.fx.bendixing.net/taskCenter/#/circle");
        }
    }


    @OnClick(R.id.ll_book_fans_count)
    void ll_book_fans_countClick() {
        //已加入则弹出退出提示框
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        ActivityCirclePersonList.startActivity(ActivityCircleDetail.this, circleId, circleInfoBean.getData().getUserType());
    }

    @OnClick(R.id.tv_add_circle)
    void tv_add_circleClick() {
        //已加入则弹出退出提示框
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (circleInfoBean.getData().isIsEnter()) {
            exit();
        } else {
            if (RxLoginTool.isLogin()) {
                joinCircle();
            } else {
                ActivityLoginNew.startActivity(ActivityCircleDetail.this);
            }
        }
    }

    @OnClick(R.id.rl_touzi)
    void rl_touziClick() {
        rl_touzi.setVisibility(View.GONE);
    }

    //退出圈子流程
    private void exit() {
        if (null == rxDialogSureCancel) {
            rxDialogSureCancel = new RxDialogCheckBox(this, R.style.OptionDialogStyle);
        }
        rxDialogSureCancel.getTitleView().setText("不再考虑下吗？");
        if (circleInfoBean.getData().getUserType() < Constant.Role.FANS_ROLE && circleInfoBean.getData().getUserType() >= Constant.Role.ADMIN_ROLE) {
            rxDialogSureCancel.getContentView().setText("退出该圈子，您的圈子角色也会被同时被取消，确认退出吗？");
        } else {
            rxDialogSureCancel.getContentView().setText("退出圈子，你将失去和书友互动的机会哦～");
        }
        //红色按钮
        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
        rxDialogSureCancel.getSureView().setText("确定退出");
        rxDialogSureCancel.getCancelView().setText("取消");
//        if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_PUBLIC_NUMBER || circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_OUTSIED_NUMBER) {
//            rxDialogSureCancel.getCheckbox_ly().setVisibility(View.VISIBLE);
//            rxDialogSureCancel.getCb_nomore_tip().setChecked(false);
//            rxDialogSureCancel.getTv_checkbox().setText("同时将书籍移出书架");
//        } else {
        rxDialogSureCancel.getCheckbox_ly().setVisibility(View.GONE);
//        }


        rxDialogSureCancel.getSureView().setOnClickListener(
                v -> {
                    exitCircle(false);//rxDialogSureCancel.getCb_nomore_tip().isChecked()
                    rxDialogSureCancel.dismiss();
                }
        );
        rxDialogSureCancel.show();
    }


    /**
     * 动态添加标签
     *
     * @param tags
     */
    private void showBookListTags(List<String> tags, LinearLayout ly) {
        if (null == tags) return;
        ly.removeAllViews();//清除所有Tag
        for (String tag : tags) {
            TextView textView = new TextView(this);
            textView.setPadding(RxImageTool.dip2px(10), RxImageTool.dip2px(3), RxImageTool.dip2px(10), RxImageTool.dip2px(3));

            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.label_corner_bg);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, RxImageTool.dip2px(8), 0);
            textView.setLayoutParams(layoutParams);

            textView.setText(tag);
            if (tag.equals("连载官方")) {
                textView.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_lianzai_guanfang), null, null, null);
            }
            ly.addView(textView);
        }
    }

    @OnClick(R.id.tv_circle_detail_description)
    void tv_circle_detail_descriptionClick() {
        //跳到新页面
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        ActivityCircleDetailShow.startActivity(ActivityCircleDetail.this, circleInfoBean.getData().getPlatformCover(), circleInfoBean.getData().getPlatformName(), circleInfoBean.getData().getPenName(), circleInfoBean.getData().getPlatformIntroduce(), circleInfoBean.getData().getTypeText(), circleInfoBean.getData().getCategoryName(), circleInfoBean.getData().getPlatformType());
    }

    /**
     * 发布动态
     */
    @OnClick(R.id.iv_circle_dynamic_add)
    void postCircleClick() {
        if (RxLoginTool.isLogin()) {
            ActivityPostCircle.startActivity(this, circleId);
        } else {
            ActivityLoginNew.startActivity(ActivityCircleDetail.this);
        }
    }

    @OnClick(R.id.tv_xiangguan_quanzi)
    void tv_xiangguan_quanziClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        //跳搜索页面的书荒神器tab
        if(!TextUtils.isEmpty(circleInfoBean.getData().getPlatformName())){
            ActivitySearchFirst.skiptoSearch2(circleInfoBean.getData().getPlatformName(),ActivityCircleDetail.this);
        }
    }


    @OnClick(R.id.tv_read_book)
    void tv_read_bookClick() {
        if (null == circleInfoBean) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }
        if (null == circleInfoBean.getData()) {
            RxToast.custom("圈子信息暂未获取到").show();
            return;
        }

        if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_PUBLIC_NUMBER) {
            //内站书
            UrlReadActivity.startActivityInsideRead(ActivityCircleDetail.this, String.valueOf(circleInfoBean.getData().getBookId()), "", false,"","",0,false);
        } else if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_OUTSIED_NUMBER) {//外站书
            SkipReadUtil.normalRead(ActivityCircleDetail.this, String.valueOf(circleInfoBean.getData().getBookId()), "", false);
        }
    }



    private void startBrowserActivity(String url) {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            intent.setAction(Intent.ACTION_VIEW);
            startActivity(intent);
        } catch (Exception e) {
            RxToast.custom("打开第三方浏览器失败", Constant.ToastType.TOAST_ERROR).show();
        }
    }


    /**
     * 获取书籍打赏状态
     */
    private void getBookRewardStatus(String mBookId) {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", mBookId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/bookRewardConfig/getBookRewardStatus", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    SendSmsResponse sendSmsResponse = GsonUtil.getBean(response, SendSmsResponse.class);
                    if (sendSmsResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (sendSmsResponse.isData()) {
                            iv_circle_payforbook.setVisibility(View.VISIBLE);
                            iv_circle_payforbook_other.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 进圈子请求圈子基本信息
     */
    private void requestCircleInfo(boolean refresh) {
        //此处置true，表示可以调用一次置顶刷新接口
        canRequestTopList = true;

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/" + circleId, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxLogTool.e("requestHistory e:" + e.getMessage());
                    showSwipeRefresh(false);
                    dismissDialog();
                    if (null == circleDynamicFragment) {
                        //情况2，不显示共读tab
                        showTab(2, false);
                    }
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("gameSwitchRequest:" + response);
                    showSwipeRefresh(false);
                    circleInfoBean = GsonUtil.getBean(response, CircleInfoBean.class);
                    if (circleInfoBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //获取打赏状态
                        getBookRewardStatus(String.valueOf(circleInfoBean.getData().getBookId()));
                        if(circleInfoBean.getData().isSigned()){
                            iv_circle_copyright_show.setVisibility(View.VISIBLE);
                            iv_circle_copyright_show_other.setVisibility(View.VISIBLE);
                        }else {
                            iv_circle_copyright_show.setVisibility(View.GONE);
                            iv_circle_copyright_show_other.setVisibility(View.GONE);
                        }

                        //清除未读
                        NIMClient.getService(MsgService.class).clearUnreadCount(circleInfoBean.getData().getAccid(), SessionTypeEnum.P2P);

                        if (circleInfoBean.getData().isShowBookCircle() && null == circleDynamicFragment) {
                            //情况1，显示共读tab
                            showTab(1, circleInfoBean.getData().isShowArticleTab());
                        } else if (!circleInfoBean.getData().isShowBookCircle() && null == circleDynamicFragment) {
                            //情况2，不显示共读tab
                            showTab(2, circleInfoBean.getData().isShowArticleTab());
                        }

                        List<String> copyRightList = new ArrayList<>();
                        List<String> writeInfoList = new ArrayList<>();
                        //共享版权标签显示
                        if (circleInfoBean.getData().isCopyright()) {
                            tv_book_investment.setVisibility(View.VISIBLE);
                            if (null != circleInfoBean.getData().getCopyrightInfoList()) {
                                copyRightList.addAll(circleInfoBean.getData().getCopyrightInfoList());
                            }
                            //name = "共享版权状态", value = "1预约 2众筹中 3共享版权"
                            if(circleInfoBean.getData().getCopyrightStatus() == 2 || circleInfoBean.getData().getCopyrightStatus() == 3){
                                if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.CIRCLE_TOUZI_TIP,true)){
                                    rl_touzi.setVisibility(View.VISIBLE);
                                    RxSharedPreferencesUtil.getInstance().putBoolean(Constant.CIRCLE_TOUZI_TIP,false);
                                }
                            }

                        }
                        //写作挖矿签显示
                        if (circleInfoBean.getData().isShowMining()) {
                            ll_book_ticket_count.setVisibility(View.VISIBLE);
                            tv_book_ticket_total.setText(RxDataTool.getFormatNumber(circleInfoBean.getData().getRecommendCount()));
                            tv_book_goticket.setVisibility(View.VISIBLE);
                            if (null != circleInfoBean.getData().getWriteInfoList()) {
                                writeInfoList.addAll(circleInfoBean.getData().getWriteInfoList());
                            }
                        }

                        //显示上下滚动效果
                        vf_notice_scroll1.clearAnimation();
                        vf_notice_scroll1.removeAllViews();
                        if (null != writeInfoList && !writeInfoList.isEmpty()) {
                            gongxiangbanquan_ly.setVisibility(View.VISIBLE);
                            vf_notice_scroll1.setVisibility(View.VISIBLE);
                            for (int i = 0; i < writeInfoList.size(); i++) {
                                TextView tv = new TextView(ActivityCircleDetail.this);
                                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                tv.setText(writeInfoList.get(i));
                                tv.setMaxLines(1);
                                tv.setEllipsize(TextUtils.TruncateAt.END);
                                tv.setTextSize(14);
                                tv.setTextColor(0xFFFFFFFF);
                                tv.setCompoundDrawablePadding(4);
                                tv.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_xiezuowakuang_quanquan), null, RxImageTool.getDrawable(R.mipmap.icon_xiangyoujiantou), null);
                                tv.setOnClickListener(
                                        v -> {
                                            ActivityWebView.startActivity(ActivityCircleDetail.this, Constant.H5_BASE_URL + "/vote/#/vote?bookId=" + circleInfoBean.getData().getBookId() + "&bookName=" + circleInfoBean.getData().getPlatformName(), 1);
                                        }
                                );
                                vf_notice_scroll1.addView(tv);
                            }
                        }
                        if (null != copyRightList && !copyRightList.isEmpty()) {
                            gongxiangbanquan_ly.setVisibility(View.VISIBLE);
                            vf_notice_scroll1.setVisibility(View.VISIBLE);

                            for (int i = 0; i < copyRightList.size(); i++) {
                                TextView tv = new TextView(ActivityCircleDetail.this);
                                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                tv.setText(copyRightList.get(i));
                                tv.setMaxLines(1);
                                tv.setEllipsize(TextUtils.TruncateAt.END);
                                tv.setTextSize(14);
                                tv.setTextColor(0xFFFFFFFF);
                                tv.setCompoundDrawablePadding(4);
                                tv.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_gongxiangbanquan_quanquan), null, RxImageTool.getDrawable(R.mipmap.icon_xiangyoujiantou), null);
                                tv.setOnClickListener(
                                        v -> {
                                            ActivityWebView.startActivity(ActivityCircleDetail.this, circleInfoBean.getData().getSkipUrl());
                                        }
                                );
                                vf_notice_scroll1.addView(tv);
                            }
                        }



                        //开启动画
                        if ((null != copyRightList && !copyRightList.isEmpty()) || (null != writeInfoList && !writeInfoList.isEmpty())) {
                            vf_notice_scroll1.setInAnimation(ActivityCircleDetail.this, R.anim.notice_in);
                            vf_notice_scroll1.setOutAnimation(ActivityCircleDetail.this, R.anim.notice_out);
                            //设置多少秒切换
                            vf_notice_scroll1.setFlipInterval(3000);
                            vf_notice_scroll1.startFlipping();
                        }


                        //圈子头像背景显示
                        String cover = circleInfoBean.getData().getPlatformCover();
                        int errorImgId = RxImageTool.getNoCoverImgByRandom();
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                        requestOptions.placeholder(errorImgId);
                        requestOptions.error(errorImgId);
                        requestOptions.skipMemoryCache(false);
                        requestOptions.dontAnimate();
                        Glide.with(ActivityCircleDetail.this).load(cover).apply(requestOptions).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                blurBitmap = RxImageTool.drawable2Bitmap(getResources().getDrawable(errorImgId), 5);
                                blur(blurBitmap, iv_blur_img);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                RxLogTool.e("onResourceReady:");
                                blurBitmap = RxImageTool.drawable2Bitmap(resource, 5);
                                blur(blurBitmap, iv_blur_img);

                                return false;
                            }
                        }).into(iv_circle_detail_cover);
                        //是否显示入群标签,只要有群聊就显示此标签
                        if (circleInfoBean.getData().getTeamId() != 0) {
                            iv_circle_member.setVisibility(View.VISIBLE);
                            iv_circle_member_other.setVisibility(View.VISIBLE);
                        } else {
                            iv_circle_member.setVisibility(View.GONE);
                            iv_circle_member_other.setVisibility(View.GONE);
                        }
                        //圈子名称
                        tv_circle_detail_name.setText(circleInfoBean.getData().getPlatformName());
//                        tv_circle_name.setText(circleInfoBean.getData().getPlatformName());
                        //是加入还是退出
                        if (circleInfoBean.getData().isIsEnter() && RxLoginTool.isLogin()) {
                            iv_circle_more_operation.setVisibility(View.VISIBLE);
                            iv_circle_more_operation_other.setVisibility(View.VISIBLE);
                            tv_add_circle.setImageResource(R.mipmap.button_hasenter);
                        } else {
                            //未加入圈子时，群聊和更多按钮都不显示
                            iv_circle_more_operation.setVisibility(View.GONE);
                            iv_circle_more_operation_other.setVisibility(View.GONE);
                            iv_circle_member.setVisibility(View.GONE);
                            iv_circle_member_other.setVisibility(View.GONE);
                            tv_add_circle.setImageResource(R.mipmap.button_enter_circle);

                        }
                        //圈子描述
                        tv_circle_detail_description.setText(circleInfoBean.getData().getPlatformIntroduce().replace("\n", " "));
                        tv_circle_detail_description.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        addEllipsisAndAllAtEnd(tv_circle_detail_description);
                                    }
                                }
                        );

                        //根据圈子类型不同，展示不同的界面
                        if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || circleInfoBean.getData().getPlatformType() > Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
                            //不可以关注取关的官方号
                            tv_book_author.setText("连载官方");
                            ly_book_info.setVisibility(View.GONE);
                            //显示圈子标签
                            List<String> tags = new ArrayList<>();
                            tags.add(LIANZAIGUANFANG);
                            showBookListTags(tags, ll_circle_detail_tags_guanfang);
                            ll_circle_detail_tags.setVisibility(View.GONE);
                            ll_circle_detail_tags_guanfang.setVisibility(View.VISIBLE);
                            ll_circle_detail_operation.setVisibility(View.GONE);
                            tv_read_book.setVisibility(View.GONE);
                            iv_circle_dynamic_add.setVisibility(View.GONE);
                            tv_xiangguan_quanzi.setVisibility(View.INVISIBLE);
                            tv_add_circle.setVisibility(View.GONE);
                        } else if (circleInfoBean.getData().getPlatformType() == Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
                            //可以关注取关的官方号
                            tv_book_author.setText("连载官方");
                            tv_book_fans_count.setText(RxDataTool.getFormatNumber(circleInfoBean.getData().getMemberCount()));
                            tv_book_word_total.setText("- -");
                            tv_book_chapter_total.setText("- -");
                            ly_book_info.setVisibility(View.VISIBLE);
                            //显示圈子标签
                            List<String> tags = new ArrayList<>();
                            tags.add(LIANZAIGUANFANG);
                            showBookListTags(tags, ll_circle_detail_tags);
                            ll_circle_detail_tags.setVisibility(View.VISIBLE);
                            ll_circle_detail_tags_guanfang.setVisibility(View.GONE);
                            ll_circle_detail_operation.setVisibility(View.VISIBLE);
                            tv_read_book.setVisibility(View.GONE);
                            iv_circle_dynamic_add.setVisibility(View.VISIBLE);
                            tv_xiangguan_quanzi.setVisibility(View.INVISIBLE);
                            tv_add_circle.setVisibility(View.VISIBLE);
                        } else {
                            //书籍号
                            tv_book_author.setText(circleInfoBean.getData().getPenName() + " 著");
                            tv_book_fans_count.setText(RxDataTool.getFormatNumber(circleInfoBean.getData().getMemberCount()));
                            tv_book_word_total.setText(RxDataTool.getFormatNumber(circleInfoBean.getData().getWordCount()));
                            if (circleInfoBean.getData().getChapterCount() > 0) {
                                tv_book_chapter_total.setText(String.valueOf(circleInfoBean.getData().getChapterCount()));
                            } else {
                                tv_book_chapter_total.setText("- -");
                            }
                            ly_book_info.setVisibility(View.VISIBLE);
                            iv_circle_dynamic_add.setVisibility(View.VISIBLE);
                            //显示圈子标签
                            List<String> tags = new ArrayList<>();
                            if (!TextUtils.isEmpty(circleInfoBean.getData().getTypeText())) {
                                tags.add(circleInfoBean.getData().getTypeText());
                            }
                            if (!TextUtils.isEmpty(circleInfoBean.getData().getCategoryName())) {
                                tags.add(circleInfoBean.getData().getCategoryName());
                            }
                            if (tags.size() > 0) {
                                showBookListTags(tags, ll_circle_detail_tags);
                            }
                            ll_circle_detail_tags.setVisibility(View.VISIBLE);
                            ll_circle_detail_tags_guanfang.setVisibility(View.GONE);
                            ll_circle_detail_operation.setVisibility(View.VISIBLE);
                            tv_read_book.setVisibility(View.VISIBLE);
                            tv_read_booktv.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.icon_button_read), null, null, null);
                            tv_xiangguan_quanzi.setVisibility(View.VISIBLE);
                            tv_add_circle.setVisibility(View.VISIBLE);
                        }

                        //info接口请求下来之后再请求列表
                        if (refresh) {
                            if (null != circleDynamicFragment) {
                                circleDynamicFragment.onRefresh();
                            }
                            if (null != circleDynamicFragment1) {
                                circleDynamicFragment1.onRefresh();
                            }
                            if (null != circleArticleFragment) {
                                circleArticleFragment.circleDynamicRequest(true);
                            }
                            if (null != circleReadFragment) {
                                circleReadFragment.circleDynamicRequest();
                            }
                        }


                    } else if (circleInfoBean.getCode() == Constant.ResponseCodeStatus.DISABLE_CIRCLE) {
                        //跳到圈子已关闭的页面
                        ActivityCircleClose.startActivity(ActivityCircleDetail.this);
                        finish();
                    } else {
                        RxToast.custom(circleInfoBean.getMsg()).show();
                        if (null == circleDynamicFragment) {
                            //情况2，不显示共读tab
                            showTab(2, false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addEllipsisAndAllAtEnd(TextView tv) {
        try {
            Layout ll = tv.getLayout();
            if (null == ll) {
                RxLogTool.e("注意复用问题，复用时ll为空" + tv.getText().toString());
                return;
            }
            int line = ll.getLineCount();
            int count = ll.getEllipsisCount(line - 1);
            if (count > 0) {
                //表示文字被折叠，此时需显示更多
                String sourceStr = tv.getText().toString();
                CharSequence temp = tv.getText().subSequence(0, ll.getLineEnd(line - 1) - count - 5);
                tv.setText(temp + "...");
                showZhankai = true;
                tv_circle_detail_description2.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 加入群聊
     *
     * @param teamId
     */
    public void joinTeamChat(String teamId) {
        Map<String, String> map = new HashMap<>();
        map.put("teamId", teamId);
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/teams/join", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        TeamMessageActivity.startActivity(ActivityCircleDetail.this, teamId);
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 退出圈子
     */
    public void exitCircle(boolean check) {
        showDialog();
        Map<String, String> map = new HashMap<>();
        map.put("isUnConcern", String.valueOf(check));
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/" + circleId + "/exist", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        requestCircleInfo(true);
                        RxEventBusTool.sendEvents(String.valueOf(circleId), Constant.EventTag.REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG);
                        if (check) {
                            //告诉阅读页面重新请求书籍信息
                            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_INFO);
                            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_REQUEST);
                        }
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加入圈子
     */
    public void joinCircle() {
        showDialog();
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/" + circleId + "/join", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                    dismissDialog();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        requestCircleInfo(true);
                        //同步数据库,此时本书加入书架，要同步一下
//                        requestBookStore();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.REFRESH_CIRCLE_LIST) {
            showSwipeRefresh(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        onRefresh();
                    } catch (Exception e) {
                    }
                }
            }, 2000);
        } else if (event.getTag() == Constant.EventTag.REFRESH_SWITCH_CIRCLE_LIST) {
            //发动态后切换到最新
            clickTabByIndex(1);
            showSwipeRefresh(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        onRefresh();
                    } catch (Exception e) {
                    }
                }
            }, 2000);
        } else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {
            showSwipeRefresh(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        //登录后请求圈子信息
                        requestCircleInfo(true);
                    } catch (Exception e) {
                    }
                }
            }, 2000);
        } else if (event.getTag() == Constant.EventTag.REFRESH_BOOK_INFO) {
            showSwipeRefresh(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        //登录后请求圈子信息
                        requestCircleInfo(true);
                    } catch (Exception e) {
                    }
                }
            }, 2000);
        }
    }

    @Override
    public void onRefresh() {
        requestCircleInfo(true);
    }

    public CircleInfoBean getCircleInfoBean() {
        return circleInfoBean;
    }

    public void setCircleInfoBean(CircleInfoBean circleInfoBean) {
        this.circleInfoBean = circleInfoBean;
    }

    private void showTab(int showTab, boolean isShowArticleTab) {
        if (showTab == 1) {
            circleDynamicFragment =  new CircleDynamicFragment();
            circleDynamicFragment.setType(2);
            circleDynamicFragment.setCircleId(circleId);

            circleDynamicFragment1 = new CircleDynamicFragment();
            circleDynamicFragment1.setType(1);
            circleDynamicFragment1.setCircleId(circleId);

            circleReadFragment = new CircleReadFragment();
            circleReadFragment.setCircleId(circleId);

            fragmentList.clear();
            fragmentList.add(circleDynamicFragment);
            fragmentList.add(circleDynamicFragment1);

            if (isShowArticleTab) {
                circleArticleFragment = new CircleArticleFragment();
                circleArticleFragment.setCircleId(circleId);
                fragmentList.add(circleArticleFragment);
                tv_circle_read_article.setVisibility(View.VISIBLE);
            }

            fragmentList.add(circleReadFragment);

            tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
            tabFragmentAdapter.setFragments(fragmentList);
            viewPager.setAdapter(tabFragmentAdapter);

            tv_circle_read_circle.setVisibility(View.VISIBLE);

            //是否展示动态红点
            if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_CIRCLE_READ, true)) {
                red_dongtai_view.setVisibility(View.VISIBLE);
            } else {
                red_dongtai_view.setVisibility(View.GONE);
            }
        } else if (showTab == 2) {
            circleDynamicFragment =  new CircleDynamicFragment();
            circleDynamicFragment.setType(2);
            circleDynamicFragment.setCircleId(circleId);

            circleDynamicFragment1 = new CircleDynamicFragment();
            circleDynamicFragment1.setType(1);
            circleDynamicFragment1.setCircleId(circleId);

            fragmentList.clear();
            fragmentList.add(circleDynamicFragment);
            fragmentList.add(circleDynamicFragment1);
            if (isShowArticleTab) {
                circleArticleFragment = new CircleArticleFragment();
                circleArticleFragment.setCircleId(circleId);
                fragmentList.add(circleArticleFragment);
                tv_circle_read_article.setVisibility(View.VISIBLE);
            }

            tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
            tabFragmentAdapter.setFragments(fragmentList);
            viewPager.setAdapter(tabFragmentAdapter);
        }

        if (tab == 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        clickTabByIndex(1);
                    } catch (Exception e) {
                    }
                }
            }, 200);
        }

        if (up) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
                        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
                        AppBarLayout.Behavior appBehavior = (AppBarLayout.Behavior) behavior;
                        //展开-appBehavior.getTopAndBottomOffset()
                        appBehavior.setTopAndBottomOffset(-1920);
                        layoutParams.setBehavior(appBehavior);
                        appbar.setLayoutParams(layoutParams);
                    } catch (Exception e) {
                    }
                }
            }, 200);
        }
    }


    private boolean canRequestTopList = false;
    private String topResponse = "";
    private List<String> topRequestList = new ArrayList<>();

    public void placedTopListRequest(String type) {
        RxLogTool.e("placedTopListRequest Need " + type);
        topRequestList.add(type);

        if (canRequestTopList) {
            RxLogTool.e("placedTopListRequest request " + type);
            //表示已经在执行刷新操作，后续操作不请求接口
            canRequestTopList = false;
            //把之前的数据清掉，保证只使用最新一次的信息
            topResponse = "";
            topListRequest();
        } else {
            if (!TextUtils.isEmpty(topResponse)) {
                if (null != circleDynamicFragment && topRequestList.contains("1")) {
                    circleDynamicFragment.placedTopListRequest(topResponse);
                    topRequestList.remove("1");
                    RxLogTool.e("placedTopListRequest get 1");
                }
                if (null != circleDynamicFragment1 && topRequestList.contains("2")) {
                    circleDynamicFragment1.placedTopListRequest(topResponse);
                    topRequestList.remove("2");
                    RxLogTool.e("placedTopListRequest get 2");
                }
                if (null != circleArticleFragment && topRequestList.contains("3")) {
                    circleArticleFragment.placedTopListRequest(topResponse);
                    topRequestList.remove("3");
                    RxLogTool.e("placedTopListRequest get 3");
                }
            }
        }
    }

    public void topListRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("platformId", circleId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/placedTopList", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                } catch (Exception ex) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    PlacedTopListBean placedTopListBean = GsonUtil.getBean(response, PlacedTopListBean.class);
                    if (placedTopListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        topResponse = response;
                        if (null != circleDynamicFragment && topRequestList.contains("1")) {
                            circleDynamicFragment.placedTopListRequest(topResponse);
                            topRequestList.remove("1");
                            RxLogTool.e("placedTopListRequest get 1 Request");
                        }
                        if (null != circleDynamicFragment1 && topRequestList.contains("2")) {
                            circleDynamicFragment1.placedTopListRequest(topResponse);
                            topRequestList.remove("2");
                            RxLogTool.e("placedTopListRequest get 2 Request");
                        }
                        if (null != circleArticleFragment && topRequestList.contains("3")) {
                            circleArticleFragment.placedTopListRequest(topResponse);
                            topRequestList.remove("3");
                            RxLogTool.e("placedTopListRequest get 3 Request");
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

}
