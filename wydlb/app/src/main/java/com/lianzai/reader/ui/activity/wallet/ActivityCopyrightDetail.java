package com.lianzai.reader.ui.activity.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.collection.ArrayMap;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BaseCountBean;
import com.lianzai.reader.bean.CopyrightDetailBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.adapter.TabFragmentAdapter;
import com.lianzai.reader.ui.fragment.CopyrightDetailListFragment;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogExchange;
import com.lianzai.reader.view.dialog.RxDialogPayment;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWhatIsJinzuan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 版权资产详情
 */
public class ActivityCopyrightDetail extends BaseActivity implements  SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.img_back)
    ImageView img_back;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_amount)
    TextView tv_amount;

    @Bind(R.id.iv_cover)
    SelectableRoundedImageView iv_cover;

    @Bind(R.id.tv_amount1)
    TextView tv_amount1;

    @Bind(R.id.tv_amount2)
    TextView tv_amount2;

    @Bind(R.id.tv_amount3)
    TextView tv_amount3;
    @Bind(R.id.tv_stop)
    TextView tv_stop;


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

    @Bind(R.id.exchange_tv)
    TextView exchange_tv;

    int pageIndex = 0;

    TabFragmentAdapter tabFragmentAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    CopyrightDetailListFragment copyrightDetailListFragment1;
    CopyrightDetailListFragment copyrightDetailListFragment2;

    private int bookId;
    private CopyrightDetailBean copyrightDetailBean;
    private AccountDetailBean accountDetail;
    private String str;

    //什么是金钻弹窗
    RxDialogWhatIsJinzuan rxDialogWhatIsJinzuan;

    RxDialogSureCancelNew rxDialogSureCancel;

    public static void startActivity(Context context,int bookId){
        Bundle bundle=new Bundle();
        bundle.putInt("bookId",bookId);
        RxActivityTool.skipActivity(context,ActivityCopyrightDetail.class,bundle);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_copyright_detail;
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

        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }


        try{
            bookId = getIntent().getIntExtra("bookId",0);
        }catch (Exception e){

        }
        accountDetail = RxTool.getAccountBean();

        rxDialogSureCancel=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
        rxDialogWhatIsJinzuan = new RxDialogWhatIsJinzuan(this,R.style.OptionDialogStyle);
        rxDialogWhatIsJinzuan.getTv_title().setText("待解锁说明");
        rxDialogWhatIsJinzuan.getTv_showmore().setVisibility(View.GONE);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));
        initTabViews();

        copyrightDetailListFragment1 =  CopyrightDetailListFragment.newInstance(5,bookId);
        copyrightDetailListFragment2 = CopyrightDetailListFragment.newInstance(6,bookId);
        fragmentList.add(copyrightDetailListFragment1);
        fragmentList.add(copyrightDetailListFragment2);
        tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        tabFragmentAdapter.setFragments(fragmentList);
        viewPager.setAdapter(tabFragmentAdapter);

        requestBookInfo();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    private void showSwipeRefresh(boolean isShow){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if (null!=mSwipeRefreshLayout){
                        mSwipeRefreshLayout.setRefreshing(isShow);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 50);
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

    }

    /**
     * 导航栏切换
     *
     * @param index
     */
    private void clickTabByIndex(int index) {
        if (pageIndex == index) return;
        pageIndex = index;
        int translateX=0;

        if (pageIndex==0){
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff999999));
        }else if(pageIndex==1){
            tv_circle_dynamic.setTextColor(getResources().getColor(R.color.color_black_ff999999));
            tv_circle_dynamic_hot.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            translateX=tv_circle_dynamic.getMeasuredWidth()+ RxImageTool.dip2px(30f);
        }
        viewPager.setCurrentItem(pageIndex);

//        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(view_slide, "translationX", translateX);
//        AnimatorSet animatorSet=new AnimatorSet();
//        animatorSet.play(translateAnim);
//        animatorSet.setDuration(300);
//        animatorSet.start();
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
    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.tv_stop)void tv_stopClick(){
        rxDialogSureCancel.getTitleView().setVisibility(View.GONE);
        rxDialogSureCancel.getContentView().setText("您是否确定要撤销解锁？撤销后，所有待解锁状态将恢复积分券。");
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

    private void showStopPaymentDialog( ){
        if(null == rxDialogPayment){
            rxDialogPayment=new RxDialogPayment(ActivityCopyrightDetail.this,R.style.OptionDialogStyle);
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
                    String uid = accountDetail.getData().getUid();
                    long timeStamp = System.currentTimeMillis();
                    map.put("balanceType", "6");
                    map.put("bookId", bookId);
                    map.put("password",password);
                    map.put("timestamp", timeStamp);
                    map.put("userId",uid);
                    map.put("amount",copyrightDetailBean.getData().getCoinLockBookBalance());

                    SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                    parameters.putAll(map);
                    String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                    RxLogTool.e("sortUrl:" + sortUrl);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("balanceType", "6");
                    jsonObject.addProperty("bookId", bookId);
                    jsonObject.addProperty("password", password);
                    jsonObject.addProperty("timestamp", timeStamp);
                    jsonObject.addProperty("userId", uid);
                    jsonObject.addProperty("amount",copyrightDetailBean.getData().getCoinLockBookBalance());
                    jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
                    requestStop(jsonObject.toString());
                    //清除支付密码框
                    rxDialogPayment.getEd_pay_password().setText("");
                }
        );
        rxDialogPayment.show();
    }


    @OnClick(R.id.tv_amount3)void tv_amount3Click(){
       //展示待解锁弹窗
        rxDialogWhatIsJinzuan.show();
    }

    @OnClick(R.id.img_back)void closeClick(){
        finish();
    }


    @OnClick(R.id.exchange_tv)void exchange_tvClick(){
        exchangeWay();
    }

    RxDialogSureCancelNew rxDialogSureCancelNew;//二次确认提示
    RxDialogExchange rxDialogExchange;
    private void exchangeWay(){
        if(null == accountDetail){
            ActivityLoginNew.startActivity(this);
            return;
        }

        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }

        StringBuilder contentSb = new StringBuilder();
        contentSb.append(str);
        contentSb.append("券将按照1:1的比例兑换为");
        contentSb.append(str);
        contentSb.append("点，兑换周期为20天，每天兑换1/20。兑换后，将不再产生");
        contentSb.append(str);
        contentSb.append("点的分红，且不可逆转。确定要兑换吗？");

        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("兑换");
        rxDialogSureCancelNew.setContent(contentSb.toString());
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                showExchaneDialog();
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }

    private void showExchaneDialog(){
        if(null == rxDialogExchange) {
            rxDialogExchange = new RxDialogExchange(this, R.style.BottomDialogStyle);
        }


        rxDialogExchange.getTv_day_count().setText("可兑换" + str + "券：" + RxDataTool.format2Decimals(String.valueOf(copyrightDetailBean.getData().getCouponBookBalance())));
        rxDialogExchange.getTv_all().setOnClickListener(
                v -> {
                    rxDialogExchange.getEd_search_keyword().setText(String.valueOf(copyrightDetailBean.getData().getCouponBookBalance()));
                }
        );

        rxDialogExchange.getTv_save().setOnClickListener(
                v -> {
                    String count = rxDialogExchange.getEd_search_keyword().getText().toString();
                    if(TextUtils.isEmpty(count)){
                        RxToast.custom("请输入兑换数量").show();
                        return;
                    }
                    double amt = 0;
                    try {
                        amt = Double.parseDouble(count);
                    }catch (Exception e){
                    }
                    if(amt <= 0){
                        RxToast.custom("请输入兑换数量").show();
                        return;
                    }
                    showPaymentDialog(amt);
                    rxDialogExchange.dismiss();
                }
        );
        rxDialogExchange.show();
    }
    RxDialogPayment rxDialogPayment;//支付密码框
    private void showPaymentDialog(double count){
        if(null == rxDialogPayment){
            rxDialogPayment=new RxDialogPayment(ActivityCopyrightDetail.this,R.style.OptionDialogStyle);
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
                    String currency = copyrightDetailBean.getData().getBookCoinCurrency();
                    String password =  rxDialogPayment.getEd_pay_password().getText().toString();
                    String uid = accountDetail.getData().getUid();
                    long timeStamp = System.currentTimeMillis();
                    map.put("amt", count);
                    map.put("bookId", bookId);
                    map.put("currency", currency);
                    map.put("payPassword",password);
                    map.put("uid",uid);
                    map.put("timestamp", timeStamp);
                    SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
                    parameters.putAll(map);
                    String sortUrl = RxTool.sortMap(parameters, Constant.SIGN_KEY);
                    RxLogTool.e("sortUrl:" + sortUrl);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("amt", count);
                    jsonObject.addProperty("bookId", bookId);
                    jsonObject.addProperty("currency", currency);
                    jsonObject.addProperty("payPassword", password);
                    jsonObject.addProperty("uid", uid);
                    jsonObject.addProperty("timestamp", timeStamp);
                    jsonObject.addProperty("sign", RxEncryptTool.encryptMD5ToString(sortUrl));
                    requestExchange(jsonObject.toString());
                    //清除支付密码框
                    rxDialogPayment.getEd_pay_password().setText("");
                }
        );
        rxDialogPayment.show();

    }

    private void requestExchange(String json){
        showDialog();
        OKHttpUtil.okHttpPostJson(Constant.GATEWAY_API_BASE_URL + "/api/bookCouponUnlock", json, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
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
                        //同步数据库
                        RxToast.custom("兑换成功", Constant.ToastType.TOAST_SUCCESS).show();
                       onRefresh();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                }
            }
        });
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

    @Override
    public void onRefresh() {
        requestBookInfo();
        if(null != copyrightDetailListFragment1){
            copyrightDetailListFragment1.requestListData();
        }
        if(null != copyrightDetailListFragment2){
            copyrightDetailListFragment2.requestListData();
        }

    }

    private void requestBookInfo(){
        Map<String, String> map = new HashMap<>();
        map.put("bookId", String.valueOf(bookId));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/getUserCopyrightDetailBalanceInfo",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    showSwipeRefresh(false);
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showSwipeRefresh(false);
                    copyrightDetailBean = GsonUtil.getBean(response, CopyrightDetailBean.class);
                    if (copyrightDetailBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //请求是否可以解锁
                        requestBookCouponLockDay();
                        str = "书籍";
                        if(null != copyrightDetailBean && null != copyrightDetailBean.getData()){
                            str = copyrightDetailBean.getData().getBookCoinName();
                        }
                        rxDialogWhatIsJinzuan.getTv_description().setText(str + "券将按照1：1的比例兑换为"+ str +"点，兑换周期为20天，每天兑换1/20。");

                        tv_title.setText("版权资产-"+str);
                        if(copyrightDetailBean.getData().getReadBalance() > 0){
                            tv_amount.setText(RxDataTool.format2Decimals(String.valueOf(copyrightDetailBean.getData().getReadBalance())));
                        }else {
                            tv_amount.setText("--");
                        }
                        RxImageTool.loadImage(ActivityCopyrightDetail.this,copyrightDetailBean.getData().getCover(),iv_cover);
                        tv_amount1.setText(str + "点  " + RxDataTool.format2Decimals(String.valueOf(copyrightDetailBean.getData().getCoinBookBalance())));
                        tv_amount2.setText(str + "券  " + RxDataTool.format2Decimals(String.valueOf(copyrightDetailBean.getData().getCouponBookBalance())));
                        tv_amount3.setText("待解锁 " + RxDataTool.format2Decimals(String.valueOf(copyrightDetailBean.getData().getCoinLockBookBalance())));
                        if(copyrightDetailBean.getData().getCoinLockBookBalance() > 0){
                            tv_stop.setVisibility(View.VISIBLE);
                        }else {
                            tv_stop.setVisibility(View.INVISIBLE);
                        }

                        tv_circle_dynamic.setText(str + "点");
                        tv_circle_dynamic_hot.setText(str + "券");
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

                    } else {
                        RxToast.custom(copyrightDetailBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }

    private void requestBookCouponLockDay(){
        Map<String, String> map = new HashMap<>();
        map.put("bookId", String.valueOf(bookId));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/bookBalance/lzBookCouponLockDay",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    showSwipeRefresh(false);
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showSwipeRefresh(false);
                    BaseCountBean baseCountBean = GsonUtil.getBean(response, BaseCountBean.class);
                    if (baseCountBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        StringBuilder sb = new StringBuilder();
                        if(baseCountBean.getData() > 0){
                            exchange_tv.setBackgroundResource(R.drawable.cccccc_5dpconor_bg);
                            sb.append("还有");
                            sb.append(baseCountBean.getData());
                            sb.append("天，");
                            sb.append(str);
                            sb.append("券便可以兑换");
                            sb.append(str);
                            sb.append("点");
                            exchange_tv.setClickable(false);
                        }else {
                            exchange_tv.setBackgroundResource(R.drawable.f58923_5dpconor_bg);
                            sb.append(str);
                            sb.append("券兑换");
                            sb.append(str);
                            sb.append("点");
                            exchange_tv.setClickable(true);
                        }
                        exchange_tv.setText(sb.toString());
                    } else {
                        RxToast.custom(baseCountBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }

    private float scaleLarge;
    private int translateX;
    private int translateXAnimation;
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

}
