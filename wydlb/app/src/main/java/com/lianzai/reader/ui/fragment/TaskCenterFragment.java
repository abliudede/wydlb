package com.lianzai.reader.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BindAccountListResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.bean.GetTaskIsCompleteByUserIdBean;
import com.lianzai.reader.bean.ReceiveBean;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.bean.UserRelaListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.model.bean.ContactsBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.ui.activity.ActivityBindAccount;
import com.lianzai.reader.ui.activity.ActivityBindPhone;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivitySafety;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.BookRecommendSettingActivity;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.taskCenter.ActivityFaceToFace;
import com.lianzai.reader.ui.activity.wallet.ActivityGoldDrillDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletMain;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletRechargeGoldCoin;
import com.lianzai.reader.ui.activity.wallet.ActivityWithdrawReadCoinBindWallet;
import com.lianzai.reader.ui.adapter.MyFriendsItemAdapterNew;
import com.lianzai.reader.ui.adapter.RenWuItemAdapterForLinearLayout;
import com.lianzai.reader.ui.contract.BindAccountListContract;
import com.lianzai.reader.ui.contract.TaskCenterContract;
import com.lianzai.reader.ui.presenter.BindAccountListPresenter;
import com.lianzai.reader.ui.presenter.TaskCenterPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBindPhone;
import com.lianzai.reader.view.dialog.RxDialogCheckBox;
import com.lianzai.reader.view.dialog.RxDialogOneKeyDistinguish;
import com.lianzai.reader.view.dialog.RxDialogQiandaoRewardMore;
import com.lianzai.reader.view.dialog.RxDialogWebShare;
import com.lianzai.reader.view.dialog.RxDialogWhatIsJinzuan;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2018/10/26.
 */

public class TaskCenterFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, TaskCenterContract.View,BindAccountListContract.View {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    //当前金钻数量
    @Bind(R.id.tv_jinzuan)
    TextView tv_jinzuan;

    //什么是金钻弹窗
    @Bind(R.id.rule_tv)
    TextView rule_tv;

    //金钻详情点击
    @Bind(R.id.jinzuan_xiangqing_tv)
    TextView jinzuan_xiangqing_tv;

    //页面切换tab，任务中心和邀请好友
    @Bind(R.id.tab1_tv)
    RelativeLayout tab1_tv;
    @Bind(R.id.tab2_tv)
    RelativeLayout tab2_tv;
    @Bind(R.id.tab_tv_1)
    TextView tab_tv_1;
    @Bind(R.id.tab_tv_2)
    TextView tab_tv_2;
    @Bind(R.id.red_dot1)
    View red_dot1;
    @Bind(R.id.red_dot2)
    View red_dot2;


    //###############任务中心页面对应视图############################################
    @Bind(R.id.nestedscroll1)
    LinearLayout nestedscroll1;
    @Bind(R.id.qiandao_tv1)
    TextView qiandao_tv1;
    @Bind(R.id.qiandao_tv2)
    TextView qiandao_tv2;
    @Bind(R.id.qiandao_tv3)
    TextView qiandao_tv3;
    @Bind(R.id.qiandao_tv4)
    TextView qiandao_tv4;
    @Bind(R.id.qiandao_tv5)
    TextView qiandao_tv5;
    @Bind(R.id.qiandao_tv6)
    TextView qiandao_tv6;
    @Bind(R.id.qiandao_tv7)
    TextView qiandao_tv7;

    @Bind(R.id.tv_day1)
    TextView tv_day1;
    @Bind(R.id.tv_day2)
    TextView tv_day2;
    @Bind(R.id.tv_day3)
    TextView tv_day3;
    @Bind(R.id.tv_day4)
    TextView tv_day4;
    @Bind(R.id.tv_day5)
    TextView tv_day5;
    @Bind(R.id.tv_day6)
    TextView tv_day6;
    @Bind(R.id.tv_day7)
    TextView tv_day7;

//    //签到可领取金钻数
//    @Bind(R.id.qiandao_amount_tv)
//    TextView qiandao_amount_tv;
    //签到按钮
    @Bind(R.id.qiandao_anniu)
    TextView qiandao_anniu;
    //可领取奖励数
    @Bind(R.id.kelingqu1_tv)
    TextView kelingqu1_tv;
    @Bind(R.id.kelingqu2_tv)
    TextView kelingqu2_tv;
    //新手任务标题
    @Bind(R.id.xinshou_tit_ly)
    RelativeLayout xinshou_tit_ly;
    //新手任务列表
    @Bind(R.id.rv_xinshou)
    LinearLayout rv_xinshou;
    //新手任务列表展开按钮
    @Bind(R.id.tv_xinshou_zhankai)
    TextView tv_xinshou_zhankai;
    //日常任务列表
    @Bind(R.id.rv_richang)
    LinearLayout rv_richang;

    @Bind(R.id.title_des1)
    TextView title_des1;
    @Bind(R.id.title_des2)
    TextView title_des2;

    //##################任务中心页面对应视图#结束#######################################################


    //###################邀请好友页面对应视图######################################
    @Bind(R.id.nestedscroll2)
    RelativeLayout nestedscroll2;
    //最高获取奖励描述
    @Bind(R.id.amount_tv)
    TextView amount_tv;
    //邀请好友技巧
    @Bind(R.id.jiqiao_tv)
    TextView jiqiao_tv;
    //立即邀请按钮
    @Bind(R.id.yaoqing_anniu)
    TextView yaoqing_anniu;
    //切换tab，我的好友，唤醒好友
    @Bind(R.id.my_friends_tab1_tv)
    RelativeLayout my_friends_tab1_tv;
    @Bind(R.id.my_friends_tab2_tv)
    RelativeLayout my_friends_tab2_tv;
    //tab上显示人数和可领取红点
    @Bind(R.id.my_friends_tab1_tv2)
    TextView my_friends_tab1_tv2;
    @Bind(R.id.my_friends_tab1_tv3)
    TextView my_friends_tab1_tv3;
    @Bind(R.id.my_friends_tab2_tv2)
    TextView my_friends_tab2_tv2;
    @Bind(R.id.my_friends_tab2_tv3)
    TextView my_friends_tab2_tv3;


    //#######我的好友视图######
    @Bind(R.id.haoyou_ly1)
    LinearLayout haoyou_ly1;
    @Bind(R.id.yaoqing_des_tv)
    TextView yaoqing_des_tv;
    //我的好友列表
    @Bind(R.id.rv_myfriend1)
    RecyclerView rv_myfriend1;
//    @Bind(R.id.shibie_tv1)
//    TextView shibie_tv1;
    //########唤醒好友视图######
    @Bind(R.id.haoyou_ly2)
    LinearLayout haoyou_ly2;
    //唤醒好友列表
    @Bind(R.id.rv_myfriend2)
    RecyclerView rv_myfriend2;
//    @Bind(R.id.shibie_tv2)
//    TextView shibie_tv2;
    //识别的悬浮按钮
//    @Bind(R.id.shibie_iv)
//    ImageView shibie_iv;
    @Bind(R.id.view_empty1)
    LinearLayout view_empty1;
    @Bind(R.id.view_empty2)
    LinearLayout view_empty2;
    //##################邀请好友页面对应视图#结束#######################################################
    List<TaskCenterBean.DataBean.DailyTaskListBean> booksBeanList0 = new ArrayList<>();
    List<TaskCenterBean.DataBean.DailyTaskListBean> booksBeanList1 = new ArrayList<>();
    List<TaskCenterBean.DataBean.DailyTaskListBean> booksBeanList2 = new ArrayList<>();

    MyFriendsItemAdapterNew mfriendAdapter;
    MyFriendsItemAdapterNew mhuanxingAdapter;
    List<UserRelaListBean.DataBean.UserPageListBean.ListBean> booksBeanList3 = new ArrayList<>();
    List<UserRelaListBean.DataBean.UserPageListBean.ListBean> booksBeanList4 = new ArrayList<>();


    @Inject
    TaskCenterPresenter taskCenterPresenter;

    //什么是金钻弹窗
//    RxDialogWhatIsJinzuan rxDialogWhatIsJinzuan;
    //签到多奖励时，小钻石弹窗
    RxDialogQiandaoRewardMore rxDialogQiandaoRewardMore;
    //一键识别好友弹窗
    RxDialogOneKeyDistinguish rxDialogOneKeyDistinguish;
    //邀请好友的分享弹窗，换成新样式
//    RxDialogInviteShare rxDialogInviteShare;
    RxDialogWebShare rxDialogWebShare;
//    //绑定白兔子弹窗
//    RxBindWalletDialog rxBindWalletDialog;
    //签到数据列表
    private List<TaskCenterBean.DataBean.CheckTaskListBean> checklist;
    //签到获得金额是否已设置
    private boolean qiandao_amount_hasset = false;
    //签到接口上传序号
    private int signDay = 0;
    private double signAmount = 0;
    //是否是展开状态
    private boolean zhankai = false;
    private WbShareHandler shareHandler;
    private int haoyouPage = 1;
    private int awakePage = 1;

    private int tabType;
    private int upLimit;
    private AccountDetailBean accountDetailBean;

    @Bind(R.id.nestedscrol)
    NestedScrollView nestedscrol;
    private LinearLayoutManager manager2;
    private LinearLayoutManager manager1;
    //是否正在加载
    private boolean isLoading = false;

    int PageSize = 6;
    private boolean hasMore1 = true;
    private boolean hasMore2 = true;

    private RxDialogCheckBox rxDialogCheckBox;

    @Inject
    BindAccountListPresenter bindAccountListPresenter;

    public void setTabType(int tab){
        tabType = tab;
        //初始化大页面状态
        if (tabType == 0) {
            tab1_tv.setBackgroundResource(R.mipmap.taskcenter_left);
            tab2_tv.setBackground(null);
            tab_tv_1.setTextSize(17);
            tab_tv_2.setTextSize(15);

            nestedscroll1.setVisibility(View.VISIBLE);
            nestedscroll2.setVisibility(View.GONE);
//            shibie_iv.setVisibility(View.GONE);
        } else {
            //切换到邀请好友界面
            tab1_tv.setBackground(null);
            tab2_tv.setBackgroundResource(R.mipmap.taskcenter_right);
            tab_tv_1.setTextSize(15);
            tab_tv_2.setTextSize(17);

            nestedscroll1.setVisibility(View.GONE);
            nestedscroll2.setVisibility(View.VISIBLE);
//            shibie_iv.setVisibility(View.VISIBLE);

            //判断是否显示引导图
//            if (getActivity() instanceof MainActivity) {
//                ((MainActivity) getActivity()).showOrNotTaskCenterYindaoye(booksBeanList3,tabType);
//            }

        }
    }
    public int getUpLimit() {
        return upLimit;
    }
    public void setUpLimit(int upLimit) {
        this.upLimit = upLimit;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_task_center;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (RxLoginTool.isLogin()){
            //回到此页面刷新数据,这个时机只会刷新任务列表
            taskCenterPresenter.todayTaskList();
            //重置标志位
            qiandao_amount_hasset = false;
            signDay = 0;
            signAmount = 0;
        }
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        //初始化dialog
        rxDialogQiandaoRewardMore = new RxDialogQiandaoRewardMore(getActivity());
//        rxDialogWhatIsJinzuan = new RxDialogWhatIsJinzuan(getActivity(), R.style.OptionDialogStyle);
        rxDialogOneKeyDistinguish = new RxDialogOneKeyDistinguish(getActivity(), R.style.OptionDialogStyle);
        accountDetailBean = RxTool.getAccountBean();
        //好友分享样式
        rxDialogWebShare = new RxDialogWebShare(getActivity(), R.style.BottomDialogStyle);
        rxDialogWebShare.getTv_share_refresh().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.GONE);

//        rxBindWalletDialog=new RxBindWalletDialog(getActivity(),R.style.OptionDialogStyle);
//        rxBindWalletDialog.setCancelable(false);
//        rxBindWalletDialog.setCanceledOnTouchOutside(false);
//        rxBindWalletDialog.getTv_cancel().setOnClickListener(
//                v->{
//                    rxBindWalletDialog.dismiss();
//                }
//        );
//        rxBindWalletDialog.getTv_sure().setOnClickListener(
//                v->{
//                    rxBindWalletDialog.dismiss();
//                    ActivityWithdrawReadCoinBindWallet.startActivity(getActivity());
//                }
//        );

        bindAccountListPresenter.attachView(this);
        taskCenterPresenter.attachView(this);
        //微博初始化
        shareHandler = new WbShareHandler(getActivity());
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);


        //刷新控件
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));
        //初始化大页面状态
        int tabIndex=0;
       setTabType(tabIndex);

        //初始化小页面状态
        my_friends_tab2_tv.setSelected(false);
        my_friends_tab1_tv.setSelected(true);
        haoyou_ly1.setVisibility(View.VISIBLE);
        haoyou_ly2.setVisibility(View.GONE);
        //避免网络错误时出现两个
//        shibie_tv1.setVisibility(View.GONE);
//        shibie_tv2.setVisibility(View.GONE);

        String str = RxSharedPreferencesUtil.getInstance().getString(Constant.TASK_SETTINGS_INVITE_AMOUNT,"10");
        SpannableString span = new SpannableString("您将最高获得价值" + str + "元奖励");
        span.setSpan(new TextAppearanceSpan(getActivity(), R.style.ChengNormalStyle), 8, 9 + str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        amount_tv.setText(span);


        //两个好友列表初始化
        mfriendAdapter = new MyFriendsItemAdapterNew(booksBeanList3, getActivity());
        manager1 = new LinearLayoutManager(getActivity());
        rv_myfriend1.setLayoutManager(manager1);
        rv_myfriend1.setAdapter(mfriendAdapter);
        rv_myfriend1.setNestedScrollingEnabled(false);

        mfriendAdapter.setType(1);
        mfriendAdapter.setOnFriendItemClickListener(new MyFriendsItemAdapterNew.OnFriendItemClickListener() {
            @Override
            public void signClick(int pos) {
                //领取签到任务
                int id = mfriendAdapter.getData().get(pos).getSignCompleteId();
                if (id != 0) {
                    ArrayMap map = new ArrayMap();
                    map.put("completeId", id);
                    taskCenterPresenter.receive(map);
                }
            }

            @Override
            public void readClick(int pos) {
                //领取阅读任务
                int id = mfriendAdapter.getData().get(pos).getReadCompleteId();
                if (id != 0) {
                    ArrayMap map = new ArrayMap();
                    map.put("completeId", id);
                    taskCenterPresenter.receive(map);
                }
            }

            @Override
            public void inviteClick(int pos) {
                //领取邀请任务
                int id = mfriendAdapter.getData().get(pos).getFriendsCompleteId();
                if (id != 0) {
                    ArrayMap map = new ArrayMap();
                    map.put("completeId", id);
                    taskCenterPresenter.receive(map);
                }
            }

            @Override
            public void awakeClick(int pos) {
                //提醒好友
                showShareBarDialog(1004,0);
            }

            @Override
            public void avatorClick(int pos) {
                int id = mfriendAdapter.getData().get(pos).getUserId();
                PerSonHomePageActivity.startActivity(getActivity(),String.valueOf(id));
            }
        });
//        rv_myfriend1.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mhuanxingAdapter = new MyFriendsItemAdapterNew(booksBeanList4, getActivity());
        manager2 = new LinearLayoutManager(getActivity());
        rv_myfriend2.setLayoutManager(manager2);
        rv_myfriend2.setAdapter(mhuanxingAdapter);
        rv_myfriend2.setNestedScrollingEnabled(false);

        mhuanxingAdapter.setType(2);
        mhuanxingAdapter.setOnFriendItemClickListener(new MyFriendsItemAdapterNew.OnFriendItemClickListener() {
            @Override
            public void signClick(int pos) {

            }

            @Override
            public void readClick(int pos) {

            }

            @Override
            public void inviteClick(int pos) {
                //唤醒好友
                int id = mhuanxingAdapter.getData().get(pos).getUserId();
                showShareBarDialog(1005,id);
            }

            @Override
            public void awakeClick(int pos) {
                //领取唤醒任务
                int id = mhuanxingAdapter.getData().get(pos).getAwakenCompleteId();
                if (id != 0) {
                    ArrayMap map = new ArrayMap();
                    map.put("completeId", id);
                    taskCenterPresenter.receive(map);
                }
            }

            @Override
            public void avatorClick(int pos) {
                int id = mhuanxingAdapter.getData().get(pos).getUserId();
                PerSonHomePageActivity.startActivity(getActivity(),String.valueOf(id));
            }
        });

        nestedscrol.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                shouldLoad();
            }
        });


        if (RxLoginTool.isLogin()){
            //只会刷新好友列表
            //返回时不刷新好友列表
            isLoading = true;
            hasMore1 = true;
            hasMore2 = true;
            //请求页面详情
            haoyouPage = 1;
            awakePage = 1;
            ArrayMap map1 = new ArrayMap();
            map1.put("currPage", haoyouPage);
            map1.put("pageSize", PageSize);
            taskCenterPresenter.getUserRelaList(map1);
            ArrayMap map2 = new ArrayMap();
            map2.put("currPage", awakePage);
            map2.put("pageSize", PageSize);
            taskCenterPresenter.getUserAwakenRelaList(map2);
            red_dot2.setVisibility(View.GONE);
        }

        bindAccountListPresenter.getBindAccountList();
    }

    private void shouldLoad(){
        if(tabType == 1){
            //请求下一页逻辑1
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            int[] location1 = new int[2];
            int[] location2 = new int[2];
            View item1 = manager1.getChildAt(mfriendAdapter.getItemCount()-1);
            View item2 = manager2.getChildAt(mhuanxingAdapter.getItemCount()-1);
            if(item1 != null && !isLoading && hasMore1) {
                item1.getLocationOnScreen(location1);
                if(location1[1] < height){
                    if (mfriendAdapter.getItemCount() < PageSize) {
                        RxLogTool.e("onLoadMoreRequested---不足一页");
                        return;
                    } else {
                        isLoading = true;
                        haoyouPage++;
                        ArrayMap map1 = new ArrayMap();
                        map1.put("currPage", haoyouPage);
                        map1.put("pageSize", PageSize);
                        taskCenterPresenter.getUserRelaList(map1);
                    }
                }
            }
            if(item2 != null && !isLoading && hasMore2){
                item2.getLocationOnScreen (location2);
                if(location2[1] < height){
                    if (mhuanxingAdapter.getData().size() < PageSize) {
                        RxLogTool.e("onLoadMoreRequested---不足一页");
                    } else {
                        isLoading = true;
                        awakePage++;
                        ArrayMap map2 = new ArrayMap();
                        map2.put("currPage", awakePage);
                        map2.put("pageSize", PageSize);
                        taskCenterPresenter.getUserAwakenRelaList(map2);
                    }
                }

            }
        }
    }

    private RxDialogBindPhone rxDialogBindPhone;//用户没有手机号时，提醒绑定手机号

    public void goClick(TaskCenterBean.DataBean.DailyTaskListBean bean) {
        //跳不同页面
        String target = bean.getTargetUrl();
        if (!TextUtils.isEmpty(target)) {
            if (target.equals("invatation-friends")) {
                //切换到邀请好友界面
                setTabType(1);
            } else if (target.equals("bookshelf")) {
                //首页切换到书架
                RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_BOOK_SHELF);
                getActivity().finish();
            }  else if (target.equals("search")) {
                //跳到搜索页面
                ActivitySearchFirst.startActivity(getActivity());
            } else if (target.equals("booklist")) {
                //首页切换到书单
                RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_BOOK_LIST);
                getActivity().finish();
            } else if (target.equals("my-fav")) {
                //阅读喜好跳转
                RxActivityTool.skipActivity(getActivity(), BookRecommendSettingActivity.class);
            } else if (target.equals("account-home")) {
                //充值页面跳转
                ActivityWalletRechargeGoldCoin.startActivity(getActivity());
            } else if (target.equals("my-binding")) {
                //账户绑定与设置
                RxActivityTool.skipActivity(getActivity(), ActivityBindAccount.class);
            } else if (target.equals("my-authentication")) {
                //实名认证页面
                RxActivityTool.skipActivity(getActivity(), ActivitySafety.class);
            } else if (target.equals("account-home-vouncher")) {
                //阅券详情页面
                if (RxLoginTool.isLogin()) {
                    AccountDetailBean accountDetailBean = RxTool.getAccountBean();
                    if (null != accountDetailBean && !TextUtils.isEmpty(accountDetailBean.getData().getMobile())) {
                        ActivityWalletMain.startActivity(getActivity());
                    } else {
                        //绑定手机号弹窗
                        if (null == rxDialogBindPhone) {
                            rxDialogBindPhone = new RxDialogBindPhone(getActivity(), R.style.OptionDialogStyle);
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
                                    RxActivityTool.skipActivity(getActivity(), ActivityBindPhone.class, bundle);
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
                    }
                } else {
                    //直接跳登录页，不关闭之前页面
                    RxActivityTool.skipActivity(getActivity(), ActivityLoginNew.class);
                }
//                        ActivityWalletDetail.startActivity(ActivityTaskCenter.this, Constant.WalletAccoutType.READ_TICKET);
            } else if (target.equals("bindingBTZ")) {
                //绑定白兔子
//                rxBindWalletDialog.show();
                ActivityWithdrawReadCoinBindWallet.startActivity(getActivity());
            } else if (target.contains("appremark")) {
                //跳到指定url
                String[] tempstr = target.split("-");
                if(tempstr.length > 1)
                ActivityWebView.startActivity(getActivity(),tempstr[1],1);
            }else if (target.contains("circle")) {
                //跳到圈子详情
                String[] tempstr = target.split("-");
                if(tempstr.length > 1)
                    ActivityCircleDetail.startActivity(getActivity(),tempstr[1]);
            }else if (target.equals("lianzaiTab")) {
                //主页切换到连载tab
                RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_LIANZAI_TAB);
                getActivity().finish();
            }else {
                //提示用户去完成或者去更新版本
                RxToast.custom("请更新到最新版本完成任务").show();
            }
        }
    }

    public void getAward(TaskCenterBean.DataBean.DailyTaskListBean bean) {
        //领取奖励
        if(null == bean){
            RxToast.custom("页面过期请刷新").show();
            return;
        }
        String id = bean.getCompleteId();
        if (!TextUtils.isEmpty(id)) {
            ArrayMap map = new ArrayMap();
            map.put("completeId", id);
            taskCenterPresenter.receive(map);
        }
    }

    private void showOrCloseRefresh(boolean isShow) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != mSwipeRefreshLayout)
                        mSwipeRefreshLayout.setRefreshing(isShow);
                } catch (Exception e) {
                    RxLogTool.e("showOrCloseRefresh:" + e.getMessage());
                }
            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }

    @OnClick(R.id.jinzuan_xiangqing_tv)
    void jinzuan_xiangqing_tvClick() {
        ActivityGoldDrillDetail.startActivity(getActivity());
    }

    @OnClick(R.id.iv_back)
    void iv_backClick() {
       getActivity().finish();
    }


    @OnClick(R.id.tab1_tv)
    void tab1_tvClick() {
        //任务中心
       setTabType(0);
    }

    @OnClick(R.id.tab2_tv)
    void tab2_tvClick() {
        //邀请好友
        setTabType(1);
    }

    @OnClick(R.id.my_friends_tab1_tv)
    void my_friends_tab1Click() {
        //好友列表
        my_friends_tab2_tv.setSelected(false);
        my_friends_tab1_tv.setSelected(true);
        haoyou_ly1.setVisibility(View.VISIBLE);
        haoyou_ly2.setVisibility(View.GONE);
    }

    @OnClick(R.id.my_friends_tab2_tv)
    void my_friends_tab2Click() {
        //唤醒好友列表
        my_friends_tab2_tv.setSelected(true);
        my_friends_tab1_tv.setSelected(false);
        haoyou_ly1.setVisibility(View.GONE);
        haoyou_ly2.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rule_tv)
    void ruleClick() {
        //参与规则点击事件
        //跳转到具体链接
        ActivityWebView.startActivity(getActivity(),"https://fx.bendixing.net/taskCenter/#/recommend");
//        rxDialogWhatIsJinzuan.show();
//        RxActivityTool.skipActivity(this, ActivityJoinRule.class);
    }

    @OnClick(R.id.rule_tv2)
    void rule2Click() {
        //金钻提现流程点击事件
        //跳转到具体链接
        ActivityWebView.startActivity(getActivity(),"https://fx.bendixing.net/taskCenter/#/votingProcess");
    }


    //识别按钮点击
//    @OnClick(R.id.shibie_tv1)
//    void shibie_tv1Click() {
//        shibie();
////        for(int i = 0 ;  i < 10 ; i ++){
////            booksBeanList3.add(booksBeanList3.get(0));
////            mfriendAdapter.notifyDataSetChanged();
////        }
//    }

//    @OnClick(R.id.shibie_tv2)
//    void shibie_tv2Click() {
//        shibie();
//    }
//
//    @OnClick(R.id.shibie_iv)
//    void shibie_ivClick() {
//        shibie();
//    }

    //签到按钮
    @OnClick(R.id.qiandao_anniu)
    void qiandao_anniuClick() {
        if (signDay != 0) {
            ArrayMap map = new ArrayMap();
            map.put("signDay", signDay);
            taskCenterPresenter.dailyCheck(map);

            //按钮直接置灰
            qiandao_anniu.setClickable(false);
            qiandao_anniu.setText("今日已签到");
            qiandao_anniu.setBackgroundResource(R.drawable.cccccc_corner_card_26);
        }
    }

    //展开按钮
    @OnClick(R.id.tv_xinshou_zhankai)
    void tv_xinshou_zhankaiClick() {
        if (!zhankai) {
//            zhankai = false;
//            tv_xinshou_zhankai.setText("展开新手任务");
//            booksBeanList1.clear();
//            booksBeanList1.add(booksBeanList0.get(0));
//            booksBeanList1.add(booksBeanList0.get(1));
//            booksBeanList1.add(booksBeanList0.get(2));
//            mXinshouAdapter.replaceData(booksBeanList1);
//
//        }else{
            zhankai = true;
            tv_xinshou_zhankai.setVisibility(View.GONE);
            booksBeanList1.clear();
            booksBeanList1.addAll(booksBeanList0);
            RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
        }
    }

    @OnClick(R.id.jiqiao_tv)
    void jiqiao_tvClick() {
        //跳转到具体链接
        ActivityWebView.startActivity(getActivity(),Constant.H5_BASE_URL + "/taskCenter/#/jiqiao");
    }

    //邀请弹窗，注册功能
    @OnClick(R.id.yaoqing_anniu)
    void yaoqing_anniuClick() {
        //跳转到具体链接
        //保存唤醒记录
        showShareBarDialog(1003,0);
    }

    //邀请弹窗，注册功能
    @OnClick(R.id.yaoqing_anniu2)
    void yaoqing_anniu2Click() {
        //跳转到具体链接
        showShareBarDialog(1003,0);
    }

    //邀请弹窗，注册功能
    @OnClick(R.id.yaoqing_anniu3)
    void yaoqing_anniu3Click() {
        //跳转到具体链接
        showShareBarDialog(1003,0);
    }

    private void shibie() {
        rxDialogOneKeyDistinguish.show();
        rxDialogOneKeyDistinguish.getTv_sure().setOnClickListener(
                v -> {
                    //请求权限后再获取联系人
                    checkPermission(new PermissionActivity.CheckPermListener() {
                        @Override
                        public void superPermission() {
                            getPhoneContacts();
                            getSIMContacts();
                            rxDialogOneKeyDistinguish.dismiss();
                        }
                        @Override
                        public void noPermission() {

                        }
                    }, R.string.ask_again, Manifest.permission.READ_CONTACTS);
                }
        );
        rxDialogOneKeyDistinguish.getTv_nomore().setOnClickListener(
                v -> {
                    rxDialogOneKeyDistinguish.dismiss();
                }
        );
    }

    private ContactsBeanDao getContactsBeanDao() {
        return DaoDbHelper.getInstance().getSession().getContactsBeanDao();
    }

    private void insertOrReplace(ContactsBean bean) {
        if (bean == null) {
            return;
        }
        getContactsBeanDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                getContactsBeanDao().insertOrReplace(bean);
            }
        });
    }


    /**
     * 获取库Phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;

    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;

    /**
     * 联系人名称
     **/
    private ArrayList<String> mContactsName = new ArrayList<String>();
    /**
     * 联系人手机号
     **/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();


    //匹配数据，并只存储匹配到的数据
    private void matchingWithData(String contactName, String phoneNumber) {

        //直接存入数据库
        ContactsBean dataTemp = new ContactsBean();
        dataTemp.setMobile(phoneNumber);
        dataTemp.setNick(contactName);
        insertOrReplace(dataTemp);

//        for (int i = 0; i < mfriendAdapter.getData().size(); i++) {
//            if (phoneNumber.equals(mfriendAdapter.getData().get(i).getMobile())) {
//                //假如匹配到，则替换数据。
//                mfriendAdapter.getData().get(i).setNickName(contactName);
//                mfriendAdapter.notifyItemChanged(i);
//            }
//        }
//        for (int i = 0; i < mhuanxingAdapter.getData().size(); i++) {
//            if (phoneNumber.equals(mhuanxingAdapter.getData().get(i).getMobile())) {
//                //假如匹配到，则替换数据。
//                mhuanxingAdapter.getData().get(i).setNickName(contactName);
//                mhuanxingAdapter.notifyItemChanged(i);
//            }
//        }
    }

    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {
        ContentResolver resolver = getActivity().getContentResolver();
        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                if(TextUtils.isEmpty(phoneNumber)) continue;

                phoneNumber = phoneNumber.replace(" ", "");
                phoneNumber = phoneNumber.replace("+86", "");
                phoneNumber = phoneNumber.replace("-", "");
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                // 得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                // 得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                // 得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                mContactsName.add(contactName);
                mContactsNumber.add(phoneNumber);
                RxLogTool.e("==========" + contactName + phoneNumber);
                matchingWithData(contactName, phoneNumber);
            }
            phoneCursor.close();
            RxToast.custom("识别成功").show();
        }
    }


    /**
     * 得到手机SIM卡联系人人信息
     **/
    private void getSIMContacts() {
        ContentResolver resolver = getActivity().getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                if(TextUtils.isEmpty(phoneNumber)) continue;

                phoneNumber = phoneNumber.replace(" ", "");
                phoneNumber = phoneNumber.replace("+86", "");
                phoneNumber = phoneNumber.replace("-", "");
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                mContactsName.add(contactName);
                mContactsNumber.add(phoneNumber);
                matchingWithData(contactName, phoneNumber);
            }
            phoneCursor.close();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void fetchData() {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void onRefresh() {
        zhankai = false;
        isLoading = true;
        hasMore1 = true;
        hasMore2 = true;
        //请求页面详情
        haoyouPage = 1;
        awakePage = 1;
        ArrayMap map1 = new ArrayMap();
        map1.put("currPage", haoyouPage);
        map1.put("pageSize", PageSize);
        taskCenterPresenter.getUserRelaList(map1);
        ArrayMap map2 = new ArrayMap();
        map2.put("currPage", awakePage);
        map2.put("pageSize", PageSize);
        taskCenterPresenter.getUserAwakenRelaList(map2);
        red_dot2.setVisibility(View.GONE);

        taskCenterPresenter.todayTaskList();
        //重置标志位
        qiandao_amount_hasset = false;
        signDay = 0;
        signAmount = 0;

    }

    public void onRefreshKeep() {
        //返回时不刷新好友列表
        isLoading = true;
        hasMore1 = true;
        hasMore2 = true;
        //请求页面详情
        haoyouPage = 1;
        awakePage = 1;
        ArrayMap map1 = new ArrayMap();
        map1.put("currPage", haoyouPage);
        map1.put("pageSize", PageSize);
        taskCenterPresenter.getUserRelaList(map1);
        ArrayMap map2 = new ArrayMap();
        map2.put("currPage", awakePage);
        map2.put("pageSize", PageSize);
        taskCenterPresenter.getUserAwakenRelaList(map2);
        red_dot2.setVisibility(View.GONE);

        taskCenterPresenter.todayTaskList();
        //重置标志位
        qiandao_amount_hasset = false;
        signDay = 0;
        signAmount = 0;
    }


    /**
     * 查询指定用户
     */
    private List<ContactsBean> SearchContactsBean(String mobile)
    {
        //惰性加载
        List<ContactsBean> list =  getContactsBeanDao().queryBuilder().where(ContactsBeanDao.Properties.Mobile.eq(mobile)).list();
        return list;
    }

    //匹配数据库的手机号和昵称
    private List<UserRelaListBean.DataBean.UserPageListBean.ListBean> matchMobileNick(List<UserRelaListBean.DataBean.UserPageListBean.ListBean> list){
        if(null == list){
            return new ArrayList<UserRelaListBean.DataBean.UserPageListBean.ListBean>();
        }else{
            for(UserRelaListBean.DataBean.UserPageListBean.ListBean item : list){
                //循环匹配
                List<ContactsBean> listtemp = SearchContactsBean(item.getMobile());
                if(null != listtemp){
                    if(!listtemp.isEmpty()){
                        item.setNickName(listtemp.get(0).getNick());
                    }
                }
            }
            return list;
        }
    }

    @Override
    public void getUserAwakenRelaListSuccess(UserRelaListBean bean) {


        if (null == bean) {
            my_friends_tab2_tv2.setText("0人");
            my_friends_tab2_tv3.setText("0");
            my_friends_tab2_tv3.setVisibility(View.INVISIBLE);
            booksBeanList4.clear();
            mhuanxingAdapter.notifyDataSetChanged();
        } else if (null == bean.getData()) {
            my_friends_tab2_tv2.setText("0人");
            my_friends_tab2_tv3.setText("0");
            my_friends_tab2_tv3.setVisibility(View.INVISIBLE);
            booksBeanList4.clear();
            mhuanxingAdapter.notifyDataSetChanged();
        } else if (null == bean.getData().getUserPageList()) {
            my_friends_tab2_tv2.setText("0人");
            my_friends_tab2_tv3.setText("0");
            my_friends_tab2_tv3.setVisibility(View.INVISIBLE);
            booksBeanList4.clear();
            mhuanxingAdapter.notifyDataSetChanged();
        } else {

            if (awakePage == 1) {
                booksBeanList4.clear();
                booksBeanList4.addAll(matchMobileNick(bean.getData().getUserPageList().getList()));
                my_friends_tab2_tv2.setText(String.valueOf(bean.getData().getUserPageList().getTotal()) + "人");
                int number = bean.getData().getNumber();
                if (number > 99) {
                    my_friends_tab2_tv3.setVisibility(View.VISIBLE);
                    my_friends_tab2_tv3.setText("99+");
                    red_dot2.setVisibility(View.VISIBLE);
                } else if (number > 0) {
                    my_friends_tab2_tv3.setVisibility(View.VISIBLE);
                    my_friends_tab2_tv3.setText(String.valueOf(number));
                    red_dot2.setVisibility(View.VISIBLE);
                } else {
                    my_friends_tab2_tv3.setVisibility(View.INVISIBLE);
                }

                if (booksBeanList4.size() > 0) {
                    view_empty2.setVisibility(View.GONE);
                    rv_myfriend2.setVisibility(View.VISIBLE);
//                    shibie_tv2.setVisibility(View.VISIBLE);
                    mhuanxingAdapter.replaceData(booksBeanList4,true,0);
                } else {
                    view_empty2.setVisibility(View.VISIBLE);
                    rv_myfriend2.setVisibility(View.GONE);
//                    shibie_tv2.setVisibility(View.GONE);
                    hasMore2 = false;
                }
            } else {
                //加载下一页时
                int size = bean.getData().getUserPageList().getList().size();
                if (size > 0) {
                    booksBeanList4.addAll(matchMobileNick(bean.getData().getUserPageList().getList()));
                    mhuanxingAdapter.replaceData(booksBeanList4,false,size);
                } else {
                    hasMore2 = false;
                }
            }
        }

        showOrCloseRefresh(false);
        isLoading = false;
        //判断是否需要继续加载
        shouldLoad();
    }

    @Override
    public void getUserRelaListSuccess(UserRelaListBean bean) {

        if (null == bean) {
            my_friends_tab1_tv2.setText("0人");
            my_friends_tab1_tv3.setText("0");
            yaoqing_des_tv.setVisibility(View.GONE);
            my_friends_tab1_tv3.setVisibility(View.INVISIBLE);
            booksBeanList3.clear();
            mfriendAdapter.notifyDataSetChanged();
        } else if (null == bean.getData()) {
            my_friends_tab1_tv2.setText("0人");
            my_friends_tab1_tv3.setText("0");
            yaoqing_des_tv.setVisibility(View.GONE);
            my_friends_tab1_tv3.setVisibility(View.INVISIBLE);
            booksBeanList3.clear();
            mfriendAdapter.notifyDataSetChanged();
        } else if (null == bean.getData().getUserPageList()) {
            my_friends_tab1_tv2.setText("0人");
            my_friends_tab1_tv3.setText("0");
            yaoqing_des_tv.setVisibility(View.GONE);
            my_friends_tab1_tv3.setVisibility(View.INVISIBLE);
            booksBeanList3.clear();
            mfriendAdapter.notifyDataSetChanged();
        } else {

            if (haoyouPage == 1) {
                booksBeanList3.clear();
                booksBeanList3.addAll(matchMobileNick(bean.getData().getUserPageList().getList()));
                //判断是否显示引导图
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).showOrNotTaskCenterYindaoye(booksBeanList3,tabType);
//                }

                if (booksBeanList3.size() > 0) {
                    view_empty1.setVisibility(View.GONE);
                    rv_myfriend1.setVisibility(View.VISIBLE);
//                    shibie_tv1.setVisibility(View.VISIBLE);
                    mfriendAdapter.replaceData(booksBeanList3,true,0);
                } else {
                    view_empty1.setVisibility(View.VISIBLE);
                    rv_myfriend1.setVisibility(View.GONE);
//                    shibie_tv1.setVisibility(View.GONE);
                    hasMore1 = false;
                }

                upLimit = bean.getData().getUpperLimit();
                my_friends_tab1_tv2.setText(String.valueOf(bean.getData().getUserPageList().getTotal()) + "人");
                int number = bean.getData().getNumber();
                if (number > 99) {
                    my_friends_tab1_tv3.setVisibility(View.VISIBLE);
                    my_friends_tab1_tv3.setText("99+");
                    red_dot2.setVisibility(View.VISIBLE);
                } else if (number > 0) {
                    my_friends_tab1_tv3.setVisibility(View.VISIBLE);
                    my_friends_tab1_tv3.setText(String.valueOf(number));
                    red_dot2.setVisibility(View.VISIBLE);
                } else {
                    my_friends_tab1_tv3.setVisibility(View.INVISIBLE);
                }

                StringBuilder sb = new StringBuilder();
//                help_url = "";
                for (int s = 0; s < bean.getData().getDescList().size(); s++) {
                    UserRelaListBean.DataBean.DescListBean item = bean.getData().getDescList().get(s);
                    sb.append(item.getTaskName());
                    sb.append(":");
                    sb.append(item.getTaskIntro());
                    if (s < bean.getData().getDescList().size() - 1) {
                        //最后一行不打回车
                        sb.append("\n");
                    }
                }
                yaoqing_des_tv.setText(sb.toString());
                yaoqing_des_tv.setVisibility(View.VISIBLE);
//                yaoqing_des_tv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!TextUtils.isEmpty(help_url)) {
//                            Intent intent = new Intent(ActivityTaskCenter.this, ActivityWebView.class);
//                            intent.putExtra(ActivityWebView.PARAM_URL, help_url);
//                            ActivityTaskCenter.this.startActivity(intent);
//                        }
//                    }
//                });

            } else {
                //加载下一页时
                int size = bean.getData().getUserPageList().getList().size();
                if (size > 0) {
                    booksBeanList3.addAll(matchMobileNick(bean.getData().getUserPageList().getList()));
                    mfriendAdapter.replaceData(booksBeanList3,false,size);
                } else {
                    hasMore1 = false;
                }
            }

        }

        //最后重置状态
        showOrCloseRefresh(false);
        isLoading = false;
        //判断是否需要继续加载
        shouldLoad();
    }

    /**
     * 金钻弹出层配置接口
     * 投放位置 1首页Banner 2任务中心Banner 3金钻领取弹出层 4发现页banner 默认为1
     */
    private void getJinZuanRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("putPosition", String.valueOf(3));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/banner/getBanner", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    if(null != getActivity()) {
                        //请求失败的情况下，只弹窗，不展示广告
                        rxDialogQiandaoRewardMore.setBannerBean(null);
                        rxDialogQiandaoRewardMore.show();
                    }
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    BannerBean bannerBean = GsonUtil.getBean(response, BannerBean.class);
                    if (bannerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null != bannerBean.getData() && !bannerBean.getData().isEmpty()) {
                            BannerBean.DataBean temp = bannerBean.getData().get(0);

                            rxDialogQiandaoRewardMore.setBannerBean(temp);
                            rxDialogQiandaoRewardMore.show();
                            return;
                        }
                    }
                    //请求失败的情况下，只弹窗，不展示广告
                    rxDialogQiandaoRewardMore.setBannerBean(null);
                    rxDialogQiandaoRewardMore.show();
                } catch (Exception e) {
                    if(null != getActivity()){
                        //请求失败的情况下，只弹窗，不展示广告
                        rxDialogQiandaoRewardMore.setBannerBean(null);
                        rxDialogQiandaoRewardMore.show();
                    }
                }
            }
        });
    }

    @Override
    public void receiveSuccess(ReceiveBean bean) {
        //页面已经销毁则不再做操作
        if(null == getActivity()) return;
        if(bean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE){
        List<ReceiveBean.DataBean.RewardDetailListBean> list = bean.getData().getRewardDetailList();
        if (null != list) {
            if (!list.isEmpty()) {
                rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.GONE);
                rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.GONE);
                rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.GONE);
                int count = 0;
                for (ReceiveBean.DataBean.RewardDetailListBean item : list) {
                    if (item.getRewardAmount() <= 0) {
                        continue;
                    }
                    if (item.getRewardType() == Constant.AwardType.JINZUAN) {
                        count++;
                        switch (count) {
                            case 1:
                                rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_1().setText("金钻");
                                rxDialogQiandaoRewardMore.getAmount_tv1().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 2:
                                rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_2().setText("金钻");
                                rxDialogQiandaoRewardMore.getAmount_tv2().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 3:
                                rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_3().setText("金钻");
                                rxDialogQiandaoRewardMore.getAmount_tv3().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                        }
                    }

                    if (item.getRewardType() == Constant.AwardType.JINBI) {
                        count++;
                        switch (count) {
                            case 1:
                                rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_1().setText("金币");
                                rxDialogQiandaoRewardMore.getAmount_tv1().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 2:
                                rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_2().setText("金币");
                                rxDialogQiandaoRewardMore.getAmount_tv2().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 3:
                                rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_3().setText("金币");
                                rxDialogQiandaoRewardMore.getAmount_tv3().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            default:
                                break;
                        }
                    }

                    if (item.getRewardType() == Constant.AwardType.YUEJUAN) {
                        count++;
                        switch (count) {
                            case 1:
                                rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_1().setText("阅券");
                                rxDialogQiandaoRewardMore.getAmount_tv1().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 2:
                                rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_2().setText("阅券");
                                rxDialogQiandaoRewardMore.getAmount_tv2().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 3:
                                rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_3().setText("阅券");
                                rxDialogQiandaoRewardMore.getAmount_tv3().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            default:
                                break;
                        }
                    }

                    if (item.getRewardType() == Constant.AwardType.YUEDIAN) {
                        count++;
                        switch (count) {
                            case 1:
                                rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_1().setText("阅点");
                                rxDialogQiandaoRewardMore.getAmount_tv1().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 2:
                                rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_2().setText("阅点");
                                rxDialogQiandaoRewardMore.getAmount_tv2().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 3:
                                rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_3().setText("阅点");
                                rxDialogQiandaoRewardMore.getAmount_tv3().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            default:
                                break;
                        }
                    }

                    if (item.getRewardType() == Constant.AwardType.TUIJIANPIAO) {
                        count++;
                        switch (count) {
                            case 1:
                                rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_1().setText("推荐票");
                                rxDialogQiandaoRewardMore.getAmount_tv1().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 2:
                                rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_2().setText("推荐票");
                                rxDialogQiandaoRewardMore.getAmount_tv2().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            case 3:
                                rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.VISIBLE);
                                rxDialogQiandaoRewardMore.getTv_3().setText("推荐票");
                                rxDialogQiandaoRewardMore.getAmount_tv3().setText(String.valueOf((int)item.getRewardAmount()));
                                break;
                            default:
                                break;
                        }
                    }

                }
                rxDialogQiandaoRewardMore.getTv_know_it().setOnClickListener(
                        v -> {
                            rxDialogQiandaoRewardMore.dismiss();
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //延迟一秒再刷新接口
                                        onRefreshKeep();
                                    } catch (Exception e) {
                                        RxLogTool.e("delay request:" + e.getMessage());
                                    }

                                }
                            }, 1000);
                        }
                );
                //请求接口后展示弹窗
                getJinZuanRequest();
            }
        }
    }else{
            RxToast.custom(bean.getMsg()).show();
        }
    }

    @Override
    public void dailyCheckSuccess(BaseBean bean) {
        //页面已经销毁则不再做操作
        if(null == getActivity()) return;

        rxDialogQiandaoRewardMore.getLy_1().setVisibility(View.VISIBLE);
        rxDialogQiandaoRewardMore.getLy_2().setVisibility(View.GONE);
        rxDialogQiandaoRewardMore.getLy_3().setVisibility(View.GONE);

//        "第" + String.valueOf(signDay) + "天签到，恭喜获得"
        rxDialogQiandaoRewardMore.getTv_1().setText("推荐票");
        rxDialogQiandaoRewardMore.getAmount_tv1().setText(String.valueOf((int) signAmount));

        rxDialogQiandaoRewardMore.getTv_know_it().setOnClickListener(
                v -> {
                    rxDialogQiandaoRewardMore.dismiss();
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //延迟一秒再刷新接口
                                onRefreshKeep();
                            } catch (Exception e) {
                                RxLogTool.e("delay request:" + e.getMessage());
                            }

                        }
                    }, 1000);
                }
        );
        //请求接口后展示弹窗
        getJinZuanRequest();
    }

    @Override
    public void todayTaskListSuccess(TaskCenterBean bean) {
        showOrCloseRefresh(false);

        if (null == bean) {
            kelingqu1_tv.setVisibility(View.GONE);
            kelingqu2_tv.setVisibility(View.GONE);
            title_des1.setVisibility(View.GONE);
            title_des2.setVisibility(View.GONE);
            rv_xinshou.setVisibility(View.GONE);
            tv_xinshou_zhankai.setVisibility(View.GONE);
            rv_richang.setVisibility(View.GONE);
            booksBeanList1.clear();
            booksBeanList2.clear();
            RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
            RenWuItemAdapterForLinearLayout.reSetData2(rv_richang, booksBeanList2, getActivity());
        } else if (null == bean.getData()) {
            kelingqu1_tv.setVisibility(View.GONE);
            kelingqu2_tv.setVisibility(View.GONE);
            title_des1.setVisibility(View.GONE);
            title_des2.setVisibility(View.GONE);
            rv_xinshou.setVisibility(View.GONE);
            tv_xinshou_zhankai.setVisibility(View.GONE);
            rv_richang.setVisibility(View.GONE);
            booksBeanList1.clear();
            booksBeanList2.clear();
            RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
            RenWuItemAdapterForLinearLayout.reSetData2(rv_richang, booksBeanList2, getActivity());
        } else if (null == bean.getData().getCheckTaskList()
                || null == bean.getData().getDailyTaskList()
                || null == bean.getData().getNewbieTaskList()) {

            kelingqu1_tv.setVisibility(View.GONE);
            kelingqu2_tv.setVisibility(View.GONE);
            title_des1.setVisibility(View.GONE);
            title_des2.setVisibility(View.GONE);
            rv_xinshou.setVisibility(View.GONE);
            tv_xinshou_zhankai.setVisibility(View.GONE);
            rv_richang.setVisibility(View.GONE);
            booksBeanList1.clear();
            booksBeanList2.clear();
            RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
            RenWuItemAdapterForLinearLayout.reSetData2(rv_richang, booksBeanList2, getActivity());
        } else {
            //字体设置
//            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "DINMedium.ttf");
//            tv_jinzuan.setTypeface(typeface);
            tv_jinzuan.setText(String.valueOf((int)bean.getData().getTotalDailyTicket()));

            //签到样式########################################
            checklist = bean.getData().getCheckTaskList();
            for (int i = 0; i < checklist.size(); i++) {
                //首先取到签到任务列表的推荐票数额
                TaskCenterBean.DataBean.RewardListBean rewardTemp = checklist.get(i).getRewardList().get(0);
                for(TaskCenterBean.DataBean.RewardListBean item:checklist.get(i).getRewardList()){
                    if(item.getRewardType() == Constant.AwardType.TUIJIANPIAO){
                        rewardTemp = item;
                    }
                }

                if (checklist.get(i).getRewardStatus() == 1) {
                    //已领取
                    if (!qiandao_amount_hasset && i == checklist.size() - 1) {
                        qiandao_anniu.setClickable(false);
                        qiandao_anniu.setText("今日已签到");
                        qiandao_anniu.setBackgroundResource(R.drawable.cccccc_corner_card_26);
                        signDay = 0;
                        signAmount = 0;
                        qiandao_amount_hasset = true;
                    }

                    switch (i) {
                        case 0:
                            qiandao_tv1.setAlpha(0.4f);
                            tv_day1.setText("已领");
                            tv_day1.setBackground(null);
                            break;
                        case 1:
                            qiandao_tv2.setAlpha(0.4f);
                            tv_day2.setText("已领");
                            tv_day2.setBackground(null);
                            break;
                        case 2:
                            qiandao_tv3.setAlpha(0.4f);
                            tv_day3.setText("已领");
                            tv_day3.setBackground(null);
                            break;
                        case 3:
                            qiandao_tv4.setAlpha(0.4f);
                            tv_day4.setText("已领");
                            tv_day4.setBackground(null);
                            break;
                        case 4:
                            qiandao_tv5.setAlpha(0.4f);
                            tv_day5.setText("已领");
                            tv_day5.setBackground(null);
                            break;
                        case 5:
                            qiandao_tv6.setAlpha(0.4f);
                            tv_day6.setText("已领");
                            tv_day6.setBackground(null);
                            break;
                        case 6:
                            qiandao_tv7.setAlpha(0.4f);
                            tv_day7.setText("已领");
                            tv_day7.setBackground(null);
                            break;
                    }
                } else if (checklist.get(i).getRewardStatus() == 0) {

                    //可签到
                    if (!qiandao_amount_hasset) {
                        qiandao_anniu.setClickable(true);
                        qiandao_anniu.setText("签到");
                        qiandao_anniu.setBackgroundResource(R.drawable.jianbian_chengse_corner_card_26);
                        signDay = checklist.get(i).getSort();
                        signAmount = rewardTemp.getRewardAmount();
                        qiandao_amount_hasset = true;
                    }


                    switch (i) {
                        case 0:
                            qiandao_tv1.setAlpha(1f);
                            tv_day1.setText("");
                            tv_day1.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                        case 1:
                            qiandao_tv2.setAlpha(1f);
                            tv_day2.setText("");
                            tv_day2.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                        case 2:
                            qiandao_tv3.setAlpha(1f);
                            tv_day3.setText("");
                            tv_day3.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                        case 3:
                            qiandao_tv4.setAlpha(1f);
                            tv_day4.setText("");
                            tv_day4.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                        case 4:
                            qiandao_tv5.setAlpha(1f);
                            tv_day5.setText("");
                            tv_day5.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                        case 5:
                            qiandao_tv6.setAlpha(1f);
                            tv_day6.setText("");
                            tv_day6.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                        case 6:
                            qiandao_tv7.setAlpha(1f);
                            tv_day7.setText("");
                            tv_day7.setBackgroundResource(R.mipmap.icon_jinri);
                            break;
                    }

                } else {

                    //不可签到
                    if (!qiandao_amount_hasset) {
                        qiandao_anniu.setClickable(false);
                        qiandao_anniu.setText("今日已签到");
                        qiandao_anniu.setBackgroundResource(R.drawable.cccccc_corner_card_26);
                        signDay = 0;
                        signAmount = 0;
                        qiandao_amount_hasset = true;
                    }


                    switch (i) {
                        case 0:
                            qiandao_tv1.setAlpha(1f);
                            tv_day1.setText("第1天");
                            tv_day1.setBackground(null);
                            break;
                        case 1:
                            qiandao_tv2.setAlpha(1f);
                            tv_day2.setText("第2天");
                            tv_day2.setBackground(null);
                            break;
                        case 2:
                            qiandao_tv3.setAlpha(1f);
                            tv_day3.setText("第3天");
                            tv_day3.setBackground(null);
                            break;
                        case 3:
                            qiandao_tv4.setAlpha(1f);
                            tv_day4.setText("第4天");
                            tv_day4.setBackground(null);
                            break;
                        case 4:
                            qiandao_tv5.setAlpha(1f);
                            tv_day5.setText("第5天");
                            tv_day5.setBackground(null);
                            break;
                        case 5:
                            qiandao_tv6.setAlpha(1f);
                            tv_day6.setText("第6天");
                            tv_day6.setBackground(null);
                            break;
                        case 6:
                            qiandao_tv7.setAlpha(1f);
                            tv_day7.setText("第7天");
                            tv_day7.setBackground(null);
                            break;
                    }
                }


                switch (i) {
                    case 0:
                        qiandao_tv1.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                    case 1:
                        qiandao_tv2.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                    case 2:
                        qiandao_tv3.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                    case 3:
                        qiandao_tv4.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                    case 4:
                        qiandao_tv5.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                    case 5:
                        qiandao_tv6.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                    case 6:
                        qiandao_tv7.setText( String.valueOf((int) rewardTemp.getRewardAmount()));
                        break;
                }

            }
            //签到样式结束###################################################

            //任务列表样式################################################
            if (bean.getData().getNewbieTaskCount() + bean.getData().getDailyTaskCount() > 0) {
                red_dot1.setVisibility(View.VISIBLE);
            } else {
                red_dot1.setVisibility(View.GONE);
            }
            //可领取奖励数
            if (bean.getData().getNewbieTaskCount() > 0) {
                kelingqu1_tv.setVisibility(View.VISIBLE);
                kelingqu1_tv.setText(String.valueOf(bean.getData().getNewbieTaskCount()) + "个奖励可领取");
            } else {
                kelingqu1_tv.setVisibility(View.GONE);
            }

            if(bean.getData().getNewBieAllCount() > 0){
                title_des1.setVisibility(View.VISIBLE);
                String str1 = String.valueOf(bean.getData().getNewBieFinishCount());
                String str2 = String.valueOf(bean.getData().getNewBieAllCount());
                SpannableString mString = new SpannableString("已完成 "+ str1 + "/" + str2);
                mString.setSpan(new TextAppearanceSpan(getActivity(), R.style.PinkText), 4, 4 + str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                title_des1.setText(mString);
            }else {
                title_des1.setVisibility(View.GONE);
            }


            if (bean.getData().getDailyTaskCount() > 0) {
                kelingqu2_tv.setVisibility(View.VISIBLE);
                kelingqu2_tv.setText(String.valueOf(bean.getData().getDailyTaskCount()) + "个奖励可领取");
            } else {
                kelingqu2_tv.setVisibility(View.GONE);
            }

            if(bean.getData().getDailyTaskAllCount() > 0){
                title_des2.setVisibility(View.VISIBLE);
                String str1 = String.valueOf(bean.getData().getDailyTaskFinishCount());
                String str2 = String.valueOf(bean.getData().getDailyTaskAllCount());
                SpannableString mString = new SpannableString("已完成 "+ str1 + "/" + str2);
                mString.setSpan(new TextAppearanceSpan(getActivity(), R.style.PinkText), 4, 4 + str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                title_des2.setText(mString);
            }else {
                title_des2.setVisibility(View.GONE);
            }


            //暂存到booksBeanList0里面
            booksBeanList0 = bean.getData().getNewbieTaskList();
            if (booksBeanList0.isEmpty()) {
                booksBeanList1.clear();
                RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
                xinshou_tit_ly.setVisibility(View.GONE);
                rv_xinshou.setVisibility(View.GONE);
                tv_xinshou_zhankai.setVisibility(View.GONE);
            } else if (booksBeanList0.size() <= 3) {
                booksBeanList1.clear();
                booksBeanList1.addAll(booksBeanList0);
                RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
                xinshou_tit_ly.setVisibility(View.VISIBLE);
                rv_xinshou.setVisibility(View.VISIBLE);
                tv_xinshou_zhankai.setVisibility(View.GONE);
            } else {
                if (zhankai) {
                    booksBeanList1.clear();
                    booksBeanList1.addAll(booksBeanList0);
                    RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
                    xinshou_tit_ly.setVisibility(View.VISIBLE);
                    rv_xinshou.setVisibility(View.VISIBLE);
                    //展开状态下隐藏按钮
                    tv_xinshou_zhankai.setVisibility(View.GONE);
                } else {
                    booksBeanList1.clear();
                    booksBeanList1.add(booksBeanList0.get(0));
                    booksBeanList1.add(booksBeanList0.get(1));
                    booksBeanList1.add(booksBeanList0.get(2));
                    RenWuItemAdapterForLinearLayout.reSetData(rv_xinshou, booksBeanList1, getActivity());
                    xinshou_tit_ly.setVisibility(View.VISIBLE);
                    rv_xinshou.setVisibility(View.VISIBLE);
                    tv_xinshou_zhankai.setVisibility(View.VISIBLE);
                }

            }

            booksBeanList2 = bean.getData().getDailyTaskList();
            if (booksBeanList2.isEmpty()) {
                booksBeanList2.clear();
                RenWuItemAdapterForLinearLayout.reSetData2(rv_richang, booksBeanList2, getActivity());
            } else {
                RenWuItemAdapterForLinearLayout.reSetData2(rv_richang, booksBeanList2, getActivity());
            }

        }
    }

    @Override
    public void getTaskIsCompleteByUserIdSuccess(GetTaskIsCompleteByUserIdBean bean) {

    }

    @Override
    public void showError(String message) {
        showOrCloseRefresh(false);
        if (RxNetTool.showNetworkUnavailable(message)) {
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }

    @Override
    public void complete(String message) {
        showOrCloseRefresh(false);
        showSeverErrorDialog(message);
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

    public List<UserRelaListBean.DataBean.UserPageListBean.ListBean> getBooksBeanList3() {
        return booksBeanList3;
    }

    public int getTabType() {
        return tabType;
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
        if(code == 1005){
            //唤醒好友加个短信按钮
            rxDialogWebShare.getTv_share_sms().setVisibility(View.VISIBLE);
            rxDialogWebShare.getTv_share_sms().setOnClickListener(
                    v -> {
                        //面对面
                        getCommonShareUrl(1006,9,id);
                        rxDialogWebShare.dismiss();
                    }
            );
        }else {
            rxDialogWebShare.getTv_share_sms().setVisibility(View.GONE);
        }
        rxDialogWebShare.getTv_share_ftf().setOnClickListener(
                v -> {
                    //面对面
                    getCommonShareUrl(code,8,id);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    getCommonShareUrl(code,1,id);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    getCommonShareUrl(code,3,id);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    getCommonShareUrl(code,2,id);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    getCommonShareUrl(code,5,id);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_copy().setOnClickListener(
                v -> {
                    getCommonShareUrl(code,6,id);
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.show();
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
                                RxShareUtils.QQShareUrl(getActivity(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(getActivity(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, getActivity(), true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(getActivity(),baseBean.getData().getOneUrlVal());
                                RxToast.custom("复制成功").show();
                                break;

                            case 8:
                                //面对面
                                ActivityFaceToFace.startActivity(getActivity(),baseBean.getData().getOneUrlVal());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
       if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {//登录成功
           onRefresh();
        }
    }

    @Override
    public void getBindAccountList(BindAccountListResponse bean) {
        if(null == bean) return;
        if(null == bean.getData()) return;
        //遍历绑定列表
        BindAccountListResponse.DataBean phoneBind = null;
        BindAccountListResponse.DataBean wxBind = null;
        for(BindAccountListResponse.DataBean item : bean.getData()){
            if(item.getType() == 0){
                phoneBind = item;
            }
            if(item.getType() == 1){
                wxBind = item;
            }
        }


        if(null != wxBind){
            if(wxBind.isBind()) {
                RxLogTool.e("微信已绑定");
            }else {
                RxLogTool.e("微信未绑定");
                try{
                    //弹窗提示
                    showWXTipsDialog();
                }catch (Exception e){
                    RxLogTool.e(e.toString());
                }

            }
        }else {
            RxLogTool.e("微信未绑定");
            try{
                //弹窗提示
                showWXTipsDialog();
            }catch (Exception e){
                RxLogTool.e(e.toString());
            }
        }
    }

    @Override
    public void bindAccountSuccess(BaseBean baseBean) {

    }


    private void showWXTipsDialog(){
        if(!RxSharedPreferencesUtil.getInstance().getBoolean(Constant.NO_MORE_TIPS_WX,false)){
            if(null == rxDialogCheckBox){
                rxDialogCheckBox = new RxDialogCheckBox(getActivity(), R.style.OptionDialogStyle);
                rxDialogCheckBox.getTitleView().setText("提示");
                rxDialogCheckBox.getContentView().setText("哎呀~您还没有绑定微信呢！现在去绑定微信，享受更多阅读权益吧！");
                rxDialogCheckBox.getCb_nomore_tip().setChecked(false);

                rxDialogCheckBox.getCb_nomore_tip().setOnCheckedChangeListener(
                        (buttonView, isChecked1) -> {
                            RxSharedPreferencesUtil.getInstance().putBoolean(Constant.NO_MORE_TIPS_WX,isChecked1);
                        }
                );

                rxDialogCheckBox.getSureView().setOnClickListener(
                        v -> {
                            RxActivityTool.skipActivity(getActivity(), ActivityBindAccount.class);
                            rxDialogCheckBox.dismiss();
                        }
                );
                rxDialogCheckBox.getCancelView().setOnClickListener(
                        v -> {
                            rxDialogCheckBox.dismiss();
                        }
                );
            }
            rxDialogCheckBox.show();
        }
    }

}
