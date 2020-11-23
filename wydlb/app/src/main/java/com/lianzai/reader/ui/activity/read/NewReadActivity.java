package com.lianzai.reader.ui.activity.read;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import androidx.collection.ArrayMap;
import androidx.drawerlayout.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.just.agentweb.LogUtils;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BarrageBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookInfoBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.IsOpenChatRoomResponse;
import com.lianzai.reader.bean.OutsideChapterDetail;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.model.bean.DownloadTaskBean;
import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.model.local.CloudRecordRepository;
import com.lianzai.reader.model.local.LocalRepository;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.receiver.PhoneStateReceiver;
import com.lianzai.reader.service.MusicService;
import com.lianzai.reader.service.ParserService;
import com.lianzai.reader.ui.activity.ActivityBookShare2;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostCircle;
import com.lianzai.reader.ui.adapter.CategoryAdapter;
import com.lianzai.reader.ui.contract.NewReadContract;
import com.lianzai.reader.ui.presenter.NewReadPresenter;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.BrightnessUtils;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.CrashSaver;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.NetworkUtils;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.ProtectedEyeUtils;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxReadTimeUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.utils.StringUtils;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.BarrageChatRoomView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.ReadAutoPageDialog;
import com.lianzai.reader.view.dialog.ReadListenDialog;
import com.lianzai.reader.view.dialog.ReadSettingDialog;
import com.lianzai.reader.view.dialog.RxDialogCacheChapter;
import com.lianzai.reader.view.dialog.RxDialogChapterSourceList;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.page.ChapterUrlsVo;
import com.lianzai.reader.view.page.PageLoader;
import com.lianzai.reader.view.page.PageMode;
import com.lianzai.reader.view.page.PageParagraphVo;
import com.lianzai.reader.view.page.PageView;
import com.lianzai.reader.view.page.TxtChapter;
import com.lianzai.reader.view.page.UrlsVo;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

import static androidx.core.view.ViewCompat.LAYER_TYPE_SOFTWARE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 阅读界面2.4版本
 */
public class NewReadActivity extends BaseActivity implements NewReadContract.View, SensorEventListener {

    /*********************************定义常量区域开始**********************************************/
    private static final String TAG = "NewReadActivity";
    public static final int REQUEST_MORE_SETTING = 1;
    public static final String EXTRA_BOOK_ID = "extra_book_id";
    public static final String EXTRA_CHAPTER_ID = "extra_chapter_id";

    // 注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");

    private static final int WHAT_CATEGORY = 1;
    private static final int WHAT_CHAPTER = 2;

    /*********************************定义常量区域结束**********************************************/


    /*********************************视图绑定区域开始**********************************************/
    @Bind(R.id.img_back)
    ImageView img_back;

    @Bind(R.id.iv_listen_book)
    ImageView iv_listen_book;

    @Bind(R.id.iv_more_option)
    ImageView iv_more_option;

    @Bind(R.id.iv_download_book_black)
    ImageView iv_download_book_black;

    @Bind(R.id.read_dl_slide)
    DrawerLayout mDlSlide;
    /*top_menu_view*/
    @Bind(R.id.read_abl_top_menu)
    LinearLayout mAblTopMenu;
    /*content_view*/
    @Bind(R.id.read_pv_page)
    PageView mPvPage;
    /*bottom_menu_view*/
    @Bind(R.id.ll_chapter_tip)//章节父视图
            LinearLayout ll_chapter_tip;

    @Bind(R.id.tv_cache_chapter_progress)//拖动章节显示的进度
            TextView tv_cache_chapter_progress;

    @Bind(R.id.tv_chapter_status)//拖动章节显示的章节名称或者是缓存章节的时候显示的提示信息
            TextView tv_chapter_status;

    @Bind(R.id.iv_return_chapter)//返回到之前的章节
            ImageView iv_return_chapter;

    @Bind(R.id.read_ll_bottom_menu)
    LinearLayout mLlBottomMenu;
    @Bind(R.id.read_tv_pre_chapter)
    TextView mTvPreChapter;
    @Bind(R.id.read_sb_chapter_progress)
    SeekBar mSbChapterProgress;
    @Bind(R.id.read_tv_next_chapter)
    TextView mTvNextChapter;
    @Bind(R.id.read_tv_category)
    TextView mTvCategory;
    @Bind(R.id.read_tv_night_mode)
    TextView mTvNightMode;
    @Bind(R.id.read_tv_setting)
    TextView mTvSetting;

    @Bind(R.id.read_tv_feedback)
    TextView read_tv_feedback;

    /*****left slide*****/
    @Bind(R.id.ll_category)
    LinearLayout ll_category;
    //目录列表
    @Bind(R.id.read_iv_category)
    ListView mLvCategory;
    @Bind(R.id.view_category_line)
    View view_category_line;
    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;//书封面
    @Bind(R.id.tv_book_title)//书名
            TextView tv_book_title;
    @Bind(R.id.tv_book_author)//作者
            TextView tv_book_author;
    @Bind(R.id.tv_book_total_chapter)//总共章节
            TextView tv_book_total_chapter;
    @Bind(R.id.iv_sort_category)//排序
            ImageView iv_sort_category;
    @Bind(R.id.tv_book_cache_status)
    TextView tv_book_cache_status;
    @Bind(R.id.rl_book_cache)//点击缓存
            RelativeLayout rl_book_cache;
    @Bind(R.id.iv_guide_read)//引导图层
            ImageView iv_guide_read;
//    @Bind(R.id.tv_change_source)//换源
//            TextView tv_change_source;
    @Bind(R.id.rl_book_detail)
    RelativeLayout rl_book_detail;

    //弹幕----------------
    @Bind(R.id.barrage_view)
    BarrageChatRoomView barrage_view;

    @Bind(R.id.iv_close_barrage)
    ImageView iv_close_barrage;
    MyHandler myHandler;
    boolean barragePause = true;
    //聊天室
    IsOpenChatRoomResponse isOpenChatRoomResponse;
    @Bind(R.id.iv_chat_room_enter)
    ImageView iv_chat_room_enter;

    @Bind(R.id.rl_read_loading)
    RelativeLayout rl_read_loading;

    @Bind(R.id.tv_loading_slogan)
    TextView tv_loading_slogan;

    @Bind(R.id.tv_loading_percent)
    TextView tv_loading_percent;

    @Bind(R.id.lottie_loading_view)
    LottieAnimationView lottie_loading_view;

    @Bind(R.id.iv_read_tip_bottom)
    ImageView iv_read_tip_bottom;

    @Bind(R.id.view_post_red)
    View view_post_red;
    /*********************************视图绑定区域结束**********************************************/


    /************************************view********************************************************/
    CategoryAdapter mCategoryAdapter;
    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CollBookBean mCollBook;
    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;
    RxDialogGoLogin rxDialogGoLogin;//去登录
    RxDialogChapterSourceList rxDialogChapterSourceList;//源列表
    ReadListenDialog readListenDialog;//听书弹框
    ReadAutoPageDialog readAutoPageDialog;//自动翻页弹框

    RxDialogSureCancelNew rxDialogSureCancelTip;//提示
    /************************************view********************************************************/


    /***********************************params*****************************************************/
//    private boolean isCollected = false; // isFromSDCard
    private boolean isNightMode = false;
    private boolean isFullScreen = false;
    private boolean isRegistered = false;

    private String mBookId;
    private boolean isSortDesc = true;//书本目录
    int lastChapterPosition = 0;//临时章节位置-用于返回上次的所在章节
    //章节列表
    boolean isDownloading = false;//是否在下载中...
    RxDialogCacheChapter rxDialogCacheChapter;//缓存弹框

    int cacheChapterCount = 0;//已成功缓存的章节数量
    int totalChapterCount = 0;//当前总章节数量
    int cacheChapterCont = 0;//已经缓存的章节数量

    public long autoPageTime;
    //暂存开启自动阅读之前的翻页模式
    private PageMode prePageMode = PageMode.NONE;
    boolean isCategoryFinish = false;//目录加载是否完成

    long listenStartTime = 0;//单次听书的开始时间
    long listenEndTime = 0;//听书定时结束
    int listenMin = 0;//默认不限时

    //阅读时长需要传的数据
    int clickCount = 0;
    int turnCount = 0;
    String deviceNo;
    String phoneModel;
    String angle = "";//倾斜角度
    String userId;
    Long startEnterPageTime = 0L;//进入页面的时间
    List<ReadTimeBean> readTimeBeanList = new ArrayList<>();//阅读时长集合


    //章节ID-通过传过来的章节ID打开指定章节
    String chapterId;//
    SensorManager mySM;//传感器管理器
    private Sensor acc_sensor;
    private Sensor mag_sensor;
    // 加速度传感器数据
    float accValues[] = new float[3];
    // 地磁传感器数据
    float magValues[] = new float[3];
    // 旋转矩阵，用来保存磁场和加速度的数据
    float r[] = new float[9];
    // 模拟方向传感器的数据（原始数据为弧度）
    float values[] = new float[3];

    List<ChapterUrlsVo> mChapterUrlsList = new ArrayList<>();//源列表

    private String loadingSlogan = "爱连载,不孤单";

    boolean isChapterRepairing = false;//章节修复中

    List<BookChapterBean> categoryChaptersList = new ArrayList<>();//目录列表

    private boolean hasNotch = false;

    PhoneStateReceiver phoneStateReceiver;
    private boolean isShow = true;

    //游客阅读章数
    private int visitorReadCount;
    private String bqtKey;

    int lastPagePosition = 0;//章节最后一页的页码

    boolean isStartOperation = false;//是否开始翻页

    boolean isOperationChapter = false;//是否是直接跳章
    /***********************************params*****************************************************/


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_CATEGORY://章节选择
                    if (!isSortDesc) {
                        mLvCategory.setSelection(categoryChaptersList.size() - mPageLoader.getChapterPos() - 1);
                    } else {
                        mLvCategory.setSelection(mPageLoader.getChapterPos());
                    }

                    isOperationChapter = true;//操作章节
                    break;
                case WHAT_CHAPTER:
                    mPageLoader.openChapter();
                    break;
            }
        }
    };
    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            // 监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };
    // 亮度调节监听
    // 由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            // 判断当前是否跟随屏幕亮度，如果不是则返回
            if (selfChange || !mSettingDialog.isBrightFollowSystem()) return;

            // 如果系统亮度改变，则修改当前 Activity 亮度
            if (BRIGHTNESS_MODE_URI.equals(uri)) {
                Log.d(TAG, "亮度模式改变");
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(NewReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(NewReadActivity.this, BrightnessUtils.getScreenBrightness(NewReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(NewReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setDefaultBrightness(NewReadActivity.this);
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };


    @Inject
    NewReadPresenter mPresenter;
    private BookInfoBean mBookInfoBean;

    public static void startActivity(Context context, String bookId) {
        //此处判断是否有云端记录。
        if (RxLoginTool.isLogin()) {
            //登录情况时才有云端记录
            String uid = String.valueOf(RxLoginTool.getLoginAccountToken().getData().getId());
            CloudRecordBean bean = CloudRecordRepository.getInstance().getCloudRecord(Long.parseLong(bookId));//Long.parseLong(uid),
            if (null == bean) {
                context.startActivity(new Intent(context, NewReadActivity.class).putExtra(EXTRA_BOOK_ID, bookId));
            } else {
                context.startActivity(new Intent(context, NewReadActivity.class)
                        .putExtra(EXTRA_BOOK_ID, bookId)
                        .putExtra(EXTRA_CHAPTER_ID, String.valueOf(bean.getChapterId())));
//                CloudRecordRepository.getInstance().deleteCloudRecordByUid(Long.parseLong(uid), Long.parseLong(bookId));
            }
        } else {
            //未登录直接跳转页面
            context.startActivity(new Intent(context, NewReadActivity.class)
                    .putExtra(EXTRA_BOOK_ID, bookId));
        }
    }

    public static void startActivity(Context context, String bookId, String chapterId) {
        context.startActivity(new Intent(context, NewReadActivity.class)
                .putExtra(EXTRA_BOOK_ID, bookId)
                .putExtra(EXTRA_CHAPTER_ID, chapterId));
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void gc() {

    }

    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_new_read;
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
        mPresenter.attachView(this);

        try {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
            chapterId = getIntent().getStringExtra(EXTRA_CHAPTER_ID);
        } catch (Exception e) {
        }

        if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_POST_KEY,true)){
            view_post_red.setVisibility(View.VISIBLE);
        }else {
            view_post_red.setVisibility(View.GONE);
        }

        //获取到个人信息后直接初始化信息，并更新书籍阅读时间。
        initReadTimeData();

        visitorReadCount = 0;
        //听书和自动翻页相关
        Constant.isListenBook = false;//是否是听书模式=默认不是
        Constant.isAutoPage = false;//是否是自动翻页模式=默认不是

        //来电监听
        phoneStateReceiver = new PhoneStateReceiver();

        //获取slogan
        getLoadingSlogan();

        //传感器服务
        mySM = (SensorManager) getSystemService(SENSOR_SERVICE);

        //菜单动画初始化
        initMenuAnim();

        //隐藏StatusBar半透明化StatusBar
        if (SystemBarUtils.hasNotch(NewReadActivity.this)) {
            hasNotch = true;
        }
        toggleMenu();//刚进去默认隐藏菜单栏
        //状态栏字体设置成黑色
        SystemBarUtils.setLightStatusBar(this, true);
        //假如是刘海屏，设置为固定高度，把状态栏位置空余出来
//        if (SystemBarUtils.hasNotch(NewReadActivity.this)) {
//            ViewGroup.LayoutParams param = mPvPage.getLayoutParams();
//            param.height = RxDeviceTool.getScreenHeight(this) - ScreenUtil.getStatusBarHeight(this);
//            mPvPage.setLayoutParams(param);
//        }


        isNightMode = ReadSettingManager.getInstance().isNightMode();
        isFullScreen = ReadSettingManager.getInstance().isFullScreen();

        //初始化书本对象
        if (null == mCollBook) {
            mCollBook = new CollBookBean();
            if (!TextUtils.isEmpty(mBookId)) {//
                mCollBook.set_id(mBookId);
            } else {//书籍ID有误
                RxToast.custom("书本ID有误", Constant.ToastType.TOAST_ERROR).show();
                finish();
            }
        }

        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        //获取页面加载器
        if (null != chapterId) {
            mPageLoader = mPvPage.getPageLoader(mBookId);
        } else {
            mPageLoader = mPvPage.getPageLoader(mBookId);
        }

        //禁止滑动展示DrawerLayout
        mDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //侧边打开后，返回键能够起作用
        mDlSlide.setFocusableInTouchMode(false);
        mSettingDialog = new ReadSettingDialog(this);//设置弹框
        mSettingDialog.setCanceledOnTouchOutside(true);
        mSettingDialog.setReadSettingCallback(new ReadSettingDialog.ReadSettingCallback() {
            @Override
            public void changeReadBackgroundClick() {//背景切换
                if (isNightMode && null != mPageLoader) {
                    isNightMode = false;
                    mPageLoader.setNightMode(isNightMode);
                    toggleNightMode();
                }
            }
        });


        setUpAdapter();
        //夜间模式按钮的状态
        toggleNightMode();

        //注册电池和时间广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mReceiver, intentFilter);
        //主动获取一次电量值
        int battery = 0;
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        mPageLoader.updateBattery(battery);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        iv_more_option.setOnClickListener(
                v -> {
                    if (null != mBookInfoBean) {
                        if (null != mPageLoader && null != mPageLoader.getCurrentTxtChapter()) {
                            String platformCover = mBookInfoBean.getData().getPlatformCover();
                            String platformName = mBookInfoBean.getData().getPlatformName();
                            String penName = mBookInfoBean.getData().getPenName();
                            String platformIntroduce = mBookInfoBean.getData().getPlatformIntroduce();
                            int platformId = mBookInfoBean.getData().getPlatformId();
                            boolean concern = mBookInfoBean.getData().isIsConcern();
                            String webUrl = mPageLoader.getCurrentTxtChapter().getLink();
                            String chapterId = mPageLoader.getCurrentTxtChapter().getChapterId();
                            String chapterTitle = mPageLoader.getCurrentTxtChapter().getTitle();
                            int platformType = mBookInfoBean.getData().getPlatformType();
                            boolean isSigned = mBookInfoBean.getData().isSigned();
                            String signUrl = mBookInfoBean.getData().getSignUrl();
                            String signUrlPrams = RxReadTimeUtils.getSignUrl(mBookId);
                            ActivityBookShare2.startActivity(NewReadActivity.this,platformCover , platformName,penName ,platformIntroduce,platformId , webUrl, mBookId, concern ,chapterId ,chapterTitle,platformType,isSigned,signUrl+signUrlPrams);
                        }
                    } else {
                        getBookInfo();
                    }
                }
        );

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "keep bright");


        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();

        initClick();

        //目录加载
        showLoadingAnimation(true);

        //数据请求
        processRequest();

        //获取书详情信息
        getBookInfo();

        //弹幕初始化
        initBarrageData();

        //缓存服务绑定
//        initDownloadService();

        //暂停听书-如果之前有开阅读页面。并且是听书状态的情况下，暂停听书
        RxEventBusTool.sendEvents(Constant.EventTag.READ_ACTIVITY_STOP_LISTEN_TAG);

        //注册监听
        registerPhoneListener();

        //注册听书服务
        registerService();
    }


    /**
     * 动态获取loading slogan
     */
    private void getLoadingSlogan() {
        if (!TextUtils.isEmpty(RxSharedPreferencesUtil.getInstance().getString(Constant.READ_LOADING_SLOGAN))) {
            String strs = RxSharedPreferencesUtil.getInstance().getString(Constant.READ_LOADING_SLOGAN);
            if (strs.contains("&&&&")) {
                String[] strings = strs.split("&&&&");
                //随机显示slogan
                int index = new Random().nextInt(strings.length);

                if (!TextUtils.isEmpty(strings[index])) {
                    loadingSlogan = strings[index];
                }

            } else {
                loadingSlogan = strs;
            }
        }
    }

    /**
     * 阅读时长用的初始化数据
     */
    private void initReadTimeData() {
        try{
            //判断是否登录，并取到uid
            AccountTokenBean account = RxLoginTool.getLoginAccountToken();
            if (null != account) {
                userId = String.valueOf(account.getData().getId());
                bqtKey = userId + Constant.BQT_KEY;
                //在这里更新小说的阅读时间，表示本地已阅读过，红点清除返回失败也不要紧。
                BookStoreRepository.getInstance().updateBooks(Integer.parseInt(userId), Integer.parseInt(mBookId));
                RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_TAG);
//                //红点清除，红点清除之后还会更新一次时间。
//                requestClearHotPoint(mBookId);
            }
            phoneModel = RxDeviceTool.getBuildBrandModel();
            deviceNo = RxDeviceTool.getDeviceIdIMEI(this);
        }catch (Exception e){

        }
    }

    /**
     * 获取书籍详情
     */
    private void getBookInfo() {

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/read/info/" + mBookId, new CallBackUtil.CallBackString() {
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
                    BookInfoBean bookInfoBean = GsonUtil.getBean(response, BookInfoBean.class);
                    if (bookInfoBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        mBookInfoBean = bookInfoBean;
                        requestChatRoomIsOpen(String.valueOf(mBookInfoBean.getData().getPlatformId()));
                    } else {
                        RxToast.custom(bookInfoBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 数据请求处理
     */
    private void processRequest() {
        ArrayMap<String, Object> map = new ArrayMap();
        map.put("isUpdate", false);
        mPresenter.loadCategory(mBookId, map, chapterId);


    }


    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            mAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu() {
        //判断是否全屏
        if (ReadSettingManager.getInstance().isFullScreen()) {
            //还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            mLlBottomMenu.setLayoutParams(params);
        } else {
            //设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mLlBottomMenu.setLayoutParams(params);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: " + mAblTopMenu.getMeasuredHeight());
    }

    private void toggleNightMode() {
        int textColor = getResources().getColor(R.color.read_night_text_color);
        int bgColor = getResources().getColor(R.color.read_night_bg_color);
        if (isNightMode) {//夜间模式
            //导航栏颜色
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(bgColor);
            }

            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_morning));

            mAblTopMenu.setBackgroundColor(bgColor);
            mLlBottomMenu.setBackgroundColor(bgColor);
            ll_category.setBackgroundColor(bgColor);
            rl_book_cache.setBackgroundColor(bgColor);

            view_category_line.setBackgroundColor(textColor);

            img_back.setBackgroundResource(R.mipmap.icon_read_back_white);
            iv_download_book_black.setImageResource(R.mipmap.icon_read_download_white);
            iv_listen_book.setImageResource(R.mipmap.icon_read_listen_white);
            iv_more_option.setImageResource(R.mipmap.icon_read_more_white);

            mTvCategory.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_category_white), null, null);
            mTvNightMode.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_morning), null, null);
            mTvSetting.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_setting_white), null, null);
            read_tv_feedback.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.post_white), null, null);

            mTvSetting.setTextColor(textColor);
            mTvNightMode.setTextColor(textColor);
            mTvCategory.setTextColor(textColor);
            mTvNextChapter.setTextColor(textColor);
            mTvPreChapter.setTextColor(textColor);
            read_tv_feedback.setTextColor(textColor);

//            tv_change_source.setTextColor(textColor);

            tv_book_author.setTextColor(textColor);
            tv_book_title.setTextColor(textColor);
            view_category_line.setBackgroundColor(Color.parseColor("#1AF9F9F9"));

            tv_book_total_chapter.setTextColor(textColor);

            iv_sort_category.setImageResource(R.mipmap.icon_category_desc);

            tv_loading_slogan.setTextColor(getResources().getColor(R.color.color_black_ff666666));
            tv_loading_percent.setTextColor(getResources().getColor(R.color.color_white_4alpha));
            iv_read_tip_bottom.setImageResource(R.mipmap.bg_read_tip_bottom_night);



            iv_chat_room_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.chat_room_enter_animation_night));


        } else {//日间模式
//            tv_change_source.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            textColor = getResources().getColor(R.color.color_black_ff666666);
            bgColor = getResources().getColor(R.color.white);

            //导航栏颜色
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(bgColor);
            }

            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_night));

            mAblTopMenu.setBackgroundColor(bgColor);
            mLlBottomMenu.setBackgroundColor(bgColor);
            ll_category.setBackgroundColor(bgColor);
            rl_book_cache.setBackgroundColor(bgColor);
            view_category_line.setBackgroundColor(Color.parseColor("#FFF9F9F9"));

            mTvCategory.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_category), null, null);
            mTvNightMode.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_night), null, null);
            mTvSetting.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_setting), null, null);
            read_tv_feedback.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.post_black), null, null);

            img_back.setBackgroundResource(R.mipmap.icon_read_back_black);
            iv_download_book_black.setImageResource(R.mipmap.icon_read_download);
            iv_listen_book.setImageResource(R.mipmap.icon_read_listen_black);
            iv_more_option.setImageResource(R.mipmap.icon_read_more_black);

            tv_book_author.setTextColor(getResources().getColor(R.color.second_text_color));
            tv_book_title.setTextColor(getResources().getColor(R.color.color_black_ff333333));

            tv_book_total_chapter.setTextColor(getResources().getColor(R.color.color_black_ff999999));

            iv_sort_category.setImageResource(R.mipmap.icon_category_desc);

            tv_loading_slogan.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_loading_percent.setTextColor(getResources().getColor(R.color.color_black_4alpha));
            iv_read_tip_bottom.setImageResource(R.mipmap.bg_read_tip_bottom);

            iv_chat_room_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.chat_room_enter_animation));

        }

        if (iv_chat_room_enter.getVisibility() == VISIBLE && iv_chat_room_enter.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) iv_chat_room_enter.getBackground();
            animation.start();
        }

        mCategoryAdapter.notifyDataSetChanged();

        //字体颜色切换

        mTvSetting.setTextColor(textColor);
        mTvNightMode.setTextColor(textColor);
        mTvCategory.setTextColor(textColor);
        mTvNextChapter.setTextColor(textColor);
        mTvPreChapter.setTextColor(textColor);
        read_tv_feedback.setTextColor(textColor);

    }

    private void setUpAdapter() {//初始化目录
        mCategoryAdapter = new CategoryAdapter();
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);
    }


    // 注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            LogUtils.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }


    /**
     * 点击事件初始化
     */
    protected void initClick() {

        mPageLoader.setOnPageChangeListener(
                new PageLoader.OnPageChangeListener() {

                    @Override
                    public void currentChapterHasCached(TxtChapter txtChapter) {
                        showLoadingAnimation(false);
                    }

                    @Override
                    public void requestCurrentChapter(TxtChapter txtChapter) {//请求当前章节
                        RxLogTool.e("requestCurrentChapter :" + txtChapter.getTitle());
                        //显示加载进度
                        showLoadingAnimation(true);

                        mPresenter.loadCurrentChapter(mBookId, txtChapter);

                        mHandler.sendEmptyMessage(WHAT_CATEGORY);
                    }

                    @Override
                    public void onChapterChange(int pos) {
                        mCategoryAdapter.setChapter(pos);

                        RxLogTool.e("onChapterChange:" + pos);

                        if (null != mSbChapterProgress) {
                            mSbChapterProgress.post(
                                    () -> {
                                        if (null != mSbChapterProgress) {
                                            mSbChapterProgress.setProgress(pos);
                                        }
                                    }
                            );
                        }

                        if (null != mPageLoader && RxLoginTool.isLogin() && !isOperationChapter && null != mPageLoader.getCurPageList()) {//只有登录用户才统计阅读时长--直接跳章，不纳入阅读记录
                            TxtChapter txtChapter = mPageLoader.getCurrentTxtChapter();
                            int pageTotalSize = mPageLoader.getCurPageList().size();
                            lastPagePosition = pageTotalSize;
                            if (null != txtChapter)
                                addReadTimeData(0, Integer.parseInt(txtChapter.getChapterId()), pageTotalSize, ReadSettingManager.getInstance().getTextSize());
                        }
                        isOperationChapter = false;
                        isStartOperation = true;

                    }

                    @Override
                    public void preLoadingChapters(List<TxtChapter> requestChapters) {
                        RxLogTool.e("preLoadingChapters size...." + requestChapters.size());
                        mPresenter.preLoadingChapter(mBookId, requestChapters);//预加载章节

                    }

                    @Override
                    public void onCategoryFinish(List<TxtChapter> chapters) {
                        //目录加载完成
                        RxLogTool.e("onCategoryFinish....." + chapters.size());
                        mCategoryAdapter.refreshItems(mCollBook.getBookChapterList());

                        mSbChapterProgress.setMax(Math.max(0, chapters.size() - 1));
                        if (!isCategoryFinish) {
                            mSbChapterProgress.setProgress(0);
                        }

//                        // 如果处于错误状态，那么就冻结使用
//                        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING
//                                || mPageLoader.getPageStatus() == PageLoader.STATUS_ERROR) {
//                            mSbChapterProgress.setEnabled(false);
//                        }
//                        else {
//                            mSbChapterProgress.setEnabled(true);
//                        }

                    }

                    @Override
                    public void onPageCountChange(int count) {//页码变动

                    }

                    @Override
                    public void onPageChange(int pos) {//翻页事件
                        if (RxLoginTool.isLogin() && null != mPageLoader && null != mPageLoader.getCurPageList()) {//只有登录用户才统计阅读时长-直接跳章，不纳入阅读记录
                            isStartOperation = true;
                            isOperationChapter = false;

                            TxtChapter txtChapter = mPageLoader.getCurrentTxtChapter();
                            int pageTotalSize = mPageLoader.getCurPageList().size();
                            lastPagePosition = pageTotalSize;
                            if (null != txtChapter)
                                addReadTimeData(pos, Integer.parseInt(txtChapter.getChapterId()), pageTotalSize, ReadSettingManager.getInstance().getTextSize());
                        } else if (!RxLoginTool.isLogin() && null != mPageLoader && null != mPageLoader.getCurPageList()) {
                            if (pos == mPageLoader.getCurPageList().size() - 1) {
                                visitorReadCount++;
                                //展示登录框
                                if (visitorReadCount == 5) {
                                    showLoginDialog();
                                } else if (visitorReadCount == 8) {
                                    showLoginDialog();
                                } else if (visitorReadCount > 8) {
                                    showLoginDialog();
                                }
                            }
                        }
                    }
                }
        );

        mSbChapterProgress.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (!isCategoryFinish) return;
                        if (mLlBottomMenu.getVisibility() == VISIBLE && null != mCollBook.getBookChapters() && mCollBook.getBookChapters().size() > 0) {
                            //显示标题
                            tv_chapter_status.setText(mCollBook.getBookChapters().get(progress).getTitle());

                            NumberFormat nt = NumberFormat.getPercentInstance();
                            nt.setMinimumFractionDigits(1);
                            float value1 = (progress + 1);
                            float value2 = (mSbChapterProgress.getMax() + 1);
                            float value = value1 / value2;
                            tv_cache_chapter_progress.setText(nt.format(value));


                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        if (!isCategoryFinish || null == mCollBook.getBookChapters()) return;
                        if (null != mPageLoader && mCollBook.getBookChapters().size() > 0) {
                            lastChapterPosition = mPageLoader.getChapterPos();//当前章节
                            iv_return_chapter.setVisibility(View.GONE);
                            ll_chapter_tip.setVisibility(VISIBLE);
                            isShowBarrageView(false);//拖动的时候隐藏弹幕
                        }

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        try {
                            //进行章节切换
                            int chapterPos = mSbChapterProgress.getProgress();
                            if (null != mCollBook.getBookChapters() && mCollBook.getBookChapters().size() > 0 && chapterPos != mPageLoader.getChapterPos()) {
                                mPageLoader.skipToChapter(chapterPos);
                            }
                            //隐藏提示
                            ll_chapter_tip.setVisibility(VISIBLE);
                            iv_return_chapter.setVisibility(VISIBLE);//可以返回到上次所在章
                            iv_return_chapter.setOnClickListener(
                                    view -> {
                                        if (null != mPageLoader) {
                                            mPageLoader.skipToChapter(lastChapterPosition);
                                            ll_chapter_tip.setVisibility(GONE);//隐藏
                                        }
                                    }
                            );
                        } catch (Exception e) {

                        }
                    }
                }
        );

        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public void retry() {
                if (Constant.isListenBook) {//听书模式
                    showReadListenDialog();
                } else if (Constant.isAutoPage) {//自动翻页模式
                    showAutoPageDialog();
                }else if (mPageLoader.getPageStatus() == 3) {//加载错误的情况
                    //请求接口并换源
//                    getChapterSourceRequest();
                } else {
                    toggleMenu();
                }
            }

            @Override
            public void adClick() {

            }

            @Override
            public void center() {
                if (Constant.isListenBook) {//听书模式
                    showReadListenDialog();
                } else if (Constant.isAutoPage) {//自动翻页模式
                    showAutoPageDialog();
                } else {
                    toggleMenu();
                }
            }

            @Override
            public void prePage() {
                RxLogTool.e("prePage...");
            }

            @Override
            public void nextPage() {
                RxLogTool.e("nextPage...");
            }

            @Override
            public void cancel() {
            }

            @Override
            public void goOriginWeb() {//原网页
                RxLogTool.e("goOriginWeb...");
                if (null != mPageLoader && null != mPageLoader.getCurrentTxtChapter()) {
                    String webUrl = mPageLoader.getCurrentTxtChapter().getLink();
                    ActivityWebView.startActivityForReadNormal(NewReadActivity.this, webUrl, mBookId,"",false);
                }

            }
        });

        //书籍章节切换
        mLvCategory.setOnItemClickListener(
                (parent, view, position, id) -> {
                    mDlSlide.closeDrawer(Gravity.START);

                    if (!isSortDesc) {//倒序
                        position = categoryChaptersList.size() - position - 1;
                    }
                    mPageLoader.skipToChapter(position);
                }
        );


        mTvCategory.setOnClickListener(
                (v) -> {
                    if (!isCategoryFinish) return;//未加载完成不可点击

                    //移动到指定位置
                    if (mCategoryAdapter.getCount() > 0) {
                        if (!isSortDesc) {
                            mLvCategory.setSelection(categoryChaptersList.size() - mPageLoader.getChapterPos() - 1);
                        } else {
                            mLvCategory.setSelection(mPageLoader.getChapterPos());
                        }
                    }
                    //切换菜单
                    toggleMenu();
                    //打开侧滑动栏
                    mDlSlide.openDrawer(Gravity.START);
                }
        );
        mTvSetting.setOnClickListener(
                (v) -> {
                    if (!isCategoryFinish) return;//未加载完成不可点击
                    toggleMenu();
                    mSettingDialog.changeNightMode();
                    mSettingDialog.show();
                    mSettingDialog.getRead_setting_tv_auto().setOnClickListener(
                            v1 -> {
                                //开始自动翻页模式
                                if (null == myHandler) {
                                    myHandler = new MyHandler(this);
                                }
                                //暂存
                                prePageMode = mPageLoader.getmPageMode();
                                Constant.isAutoPage = true;
                                //设置初始自动翻页时间
                                autoPageTime = RxSharedPreferencesUtil.getInstance().getLong(Constant.AUTO_PAGE_TIME, 15000L);
                                mPageLoader.setAutoPageMode(PageMode.COVER_AUTO, autoPageTime);
                                mSettingDialog.dismiss();
                                //开始翻页
                                myHandler.sendEmptyMessage(2);
                            }
                    );
                }
        );

        mTvPreChapter.setOnClickListener(
                (v) -> {
                    if (!isCategoryFinish ) return;//未加载完成不可点击
                    isOperationChapter = true;
                    if (mPageLoader.skipPreChapter()) {
                        isStartOperation = false;

                        lastChapterPosition = mPageLoader.getChapterPos() + 1;
                        mCategoryAdapter.setChapter(mPageLoader.getChapterPos());

                    }
                }
        );

        mTvNextChapter.setOnClickListener(
                (v) -> {
                    if (!isCategoryFinish ) return;//未加载完成不可点击
                    isOperationChapter = true;
                    if (mPageLoader.skipNextChapter()) {
                        isStartOperation = false;

                        lastChapterPosition = mPageLoader.getChapterPos() - 1;
                        mCategoryAdapter.setChapter(mPageLoader.getChapterPos());


                    }
                }
        );

        mTvNightMode.setOnClickListener(
                (v) -> {
                    if (!isCategoryFinish) return;//未加载完成不可点击
                    if (isNightMode) {
                        isNightMode = false;
                    } else {
                        isNightMode = true;
                    }
                    mPageLoader.setNightMode(isNightMode);
                    toggleNightMode();
                }
        );


        mSettingDialog.setOnDismissListener(
                dialog -> hideSystemBar()
        );
    }


    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {

        try {
            if (mAblTopMenu.getVisibility() == VISIBLE) {
                toggleMenu();
                return true;
            } else if (mSettingDialog.isShowing()) {
                hideSystemBar();
                mSettingDialog.dismiss();
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    private void showSystemBar() {
        //显示,要先清除视图标记
        if (!hasNotch) {
            SystemBarUtils.readStatusBar3(this);
        }

        if (isFullScreen) {
            SystemBarUtils.showUnStableNavBar(this);
        }
    }

    private void hideSystemBar() {
        //隐藏
        if (!hasNotch) {
            SystemBarUtils.readStatusBar1(this);
        } else {
            SystemBarUtils.readStatusBar2(this);
        }

        if (isFullScreen) {
            SystemBarUtils.hideUnStableNavBar(this);
        }
    }

    private void hideAll() {
        isShow = false;
        //先清除动画
        mAblTopMenu.clearAnimation();
        mLlBottomMenu.clearAnimation();
        //关闭
        mAblTopMenu.startAnimation(mTopOutAnim);
        mLlBottomMenu.startAnimation(mBottomOutAnim);
        hideSystemBar();
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu() {
        initMenuAnim();
        if (isShow) {
            //关闭视图
            isShow = false;
            //先清除动画
            mAblTopMenu.clearAnimation();
            mLlBottomMenu.clearAnimation();
            mAblTopMenu.startAnimation(mTopOutAnim);
            mLlBottomMenu.startAnimation(mBottomOutAnim);
            hideSystemBar();
        } else {
            //弹出视图
            isShow = true;
            //先清除动画
            mAblTopMenu.clearAnimation();
            mLlBottomMenu.clearAnimation();
            mAblTopMenu.startAnimation(mTopInAnim);
            mLlBottomMenu.startAnimation(mBottomInAnim);

            showSystemBar();
        }
    }

    /**
     * 书本信息
     *
     * @param collBookBean
     */
    private void initBookData(CollBookBean collBookBean) {
        if (!TextUtils.isEmpty(collBookBean.getTitle())) {
            tv_book_title.setText(collBookBean.getTitle());
        } else {
            tv_book_title.setText("");
        }
        if (!TextUtils.isEmpty(collBookBean.getAuthor())) {
            tv_book_author.setText(collBookBean.getAuthor());
        } else {
            tv_book_author.setText("佚名");
        }
        if (null != collBookBean.getBookChapterList()) {
            tv_book_total_chapter.setText("共" + totalChapterCount + "章");
        } else {
            tv_book_total_chapter.setText("共0章");
        }

        RxImageTool.loadImage(this, collBookBean.getCover(), iv_book_cover);
    }

    /**
     * 根据章节ID获取当前章节的位置
     *
     * @return
     */
    private int getChapterPositionByChapterId(List<BookChapterBean> bookChapterBeans) {
        RxLogTool.e("getChapterPositionByChapterId:" + chapterId);

        for (int i = 0; i < bookChapterBeans.size(); i++) {
            BookChapterBean txtChapter = bookChapterBeans.get(i);
            if (txtChapter.getId().equals(chapterId)) {//根据章节ID
                return i;
            }
        }
        return bookChapterBeans.size() - 1;//如果没匹配到打开最后一章
    }


    /**
     * 加载动画显示
     *
     * @param isShow
     */
    private void showLoadingAnimation(boolean isShow) {
        if (null != rl_read_loading) {
            if (isShow && rl_read_loading.getVisibility() == View.GONE ) {
                rl_read_loading.setVisibility(View.VISIBLE);
                //如果加载动画显示中
                tv_loading_slogan.setText(loadingSlogan);

                if (null == myHandler) {
                    myHandler = new MyHandler(this);
                }
                Constant.percent = 0;
                tv_loading_percent.setText("正实时从原网页转码 0%");
                myHandler.sendEmptyMessageDelayed(3, 50);
            } else if(!isShow && rl_read_loading.getVisibility() == View.VISIBLE){
                myHandler.removeMessages(3);
                rl_read_loading.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void getBookDirectoryView(CollBookBean collBookBean, boolean isUpdateCategory, boolean needReopen) {//书的信息返回成功
        showLoadingAnimation(false);
        if (!isCategoryFinish) {
            if (RxSharedPreferencesUtil.getInstance().getBoolean("is_show_read_guide", true)) {//是否显示引导图层
                iv_guide_read.setVisibility(View.VISIBLE);
            }
            startEnterPageTime = System.currentTimeMillis();//初始化出第一个页面的时间
        }

        //卷名初始化
        mCollBook = collBookBean;

        //目录数据初始化
        categoryChaptersList.clear();
        categoryChaptersList.addAll(collBookBean.getBookChapterList());

        totalChapterCount = collBookBean.getBookChapters().size();//总章节数量

        RxLogTool.e("getBookDirectoryView totalChapterCount:" + totalChapterCount);

        initBookData(collBookBean);

        mPageLoader.refreshChapterList(needReopen,collBookBean.getBookChapters());

        if (!TextUtils.isEmpty(chapterId)) {//指定打开的章节-如果是最新章节-本地数据库没有这个章节-先跳转到最后章节，然后等最新目录获取到的时候才跳转到最新章节
            RxLogTool.e("chapterId:" + chapterId);
            int chapterPosition = getChapterPositionByChapterId(mCollBook.getBookChapters());
            RxLogTool.e("chapterPosition:" + chapterPosition);
            mPageLoader.skipToChapter(chapterPosition);
        }

        if (isUpdateCategory) {
            //更新目录-先删除之前的缓存目录，存储最新目录-保证本地数据都是最新的
            RxLogTool.e("getBookDirectoryView update category");
            BookRepository.getInstance().deleteBookChapterById(mBookId);
            BookRepository.getInstance()
                    .saveBookChaptersWithAsync(collBookBean.getBookChapters());
        }
        initCacheChapterData();
        isCategoryFinish = true;

    }

    @Override
    public void getBookDirectoryView2(CollBookBean collBookBean, boolean isUpdateCategory) {//, String ChapterId

    }

    @Override
    public void getBookDirectoryErrorView(String message) {
        if(TextUtils.isEmpty(message)) {
            RxToast.custom("目录获取失败").show();
        }else {
            RxToast.custom(message).show();
        }
        showLoadingAnimation(false);
    }

    int tryRequestCount = 0;

    @Override
    public void getBookDirectoryTimeout() {
        if (!isCategoryFinish && tryRequestCount < 3) {//目录未加载过-启动重试机制
            tryRequestCount++;
            RxToast.custom("目录获取超时，开始重新请求").show();
            //重新请求
            processRequest();
        } else {
            showLoadingAnimation(false);
        }

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void complete(String message) {

    }


    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0);
        mTopInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAblTopMenu.setVisibility(View.VISIBLE);
                isShowBarrageView(true);//隐藏弹幕
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTopOutAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1);
        mTopOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAblTopMenu.setVisibility(GONE);
                ll_chapter_tip.setVisibility(GONE);
                isShowBarrageView(false);//隐藏弹幕
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomInAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        mBottomInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLlBottomMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mBottomOutAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        mBottomOutAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLlBottomMenu.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //退出的速度要快
        mTopInAnim.setDuration(400);
        mTopOutAnim.setDuration(200);
        mBottomInAnim.setDuration(400);
        mBottomOutAnim.setDuration(200);
    }


    int chapterRequestTryCount = 0;//章节请求超时重试次数


//    @Override
//    public void preLoadingChapterSuccess() {//预加载成功
//        initCacheChapterData();
//    }


    @Override
    public void vipChapter(TxtChapter txtChapter) {//vip章节，自动修复
        showLoadingAnimation(false);

        ChapterUrlsVo chapterUrlsVo = getChapterUrlsVo();

        if (null != chapterUrlsVo && chapterUrlsVo.getUrlsVoList().size() > 1) {//大于1个站点
            //自动修复
            if (!isChapterRepairing)
                reloadChapterClick();
        } else {//只有一个站点，自动修复也没用，直接提示
            if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
                mPageLoader.chapterError();
            }
        }
    }

    RxDialogSureCancelNew addBookStoreDialog;

    // 退出
    private boolean exit() {
        if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        } else if (mDlSlide.isDrawerOpen(Gravity.START)) {
            mDlSlide.closeDrawer(Gravity.START);
            return true;
        }
        if (isCategoryFinish && null != mBookInfoBean && RxLoginTool.isLogin() && !mBookInfoBean.getData().isIsConcern()) {//已登录-并且未关注-并且目录加载完毕
            if (null == addBookStoreDialog) {
                addBookStoreDialog = new RxDialogSureCancelNew(this, R.style.OptionDialogStyle);
            }
            addBookStoreDialog.setTitle("喜欢这本书，就加入书架吧！");
            addBookStoreDialog.setContent("加入书架后，就可以随时开启阅读，同时也能收到小说更新的推送通知哦～");
            addBookStoreDialog.setButtonText("加入书架", "取消");

            addBookStoreDialog.show();

            addBookStoreDialog.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    addBookStoreDialog.dismiss();
                    requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()));
                }
            });
            addBookStoreDialog.setCancelListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    addBookStoreDialog.dismiss();
                    finish();
                }
            });
            return true;

        } else {//直接退出
            finish();
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ReadSettingManager.getInstance().isOpenProtectedEye()) {
            ProtectedEyeUtils.openProtectedEyeMode(this);
            RxLogTool.e("NewReadActivity onResume openProtectedEyeMode true");
        } else {
            RxLogTool.e("NewReadActivity onResume openProtectedEyeMode false");
        }
        if (ReadSettingManager.getInstance().isScreenKeepOn()) {//只有选择常量的时候才唤醒屏幕
            mWakeLock.acquire();
            RxLogTool.e("isScreenKeepOn...keep on");
        } else {
            RxLogTool.e("isScreenKeepOn follow system...");
        }

        acc_sensor = mySM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = mySM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // 给传感器注册监听：
        mySM.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_GAME);
        mySM.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_GAME);

        //每次回到页面，重置视图。
        hideAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (ReadSettingManager.getInstance().isScreenKeepOn()) {//只有选择常量的时候才唤醒屏幕
                mWakeLock.release();
            }
        } catch (Exception e) {
        }

        try {
            //切到主页面时也需要保存
            mPageLoader.saveRecord();
        }catch (Exception e){

        }

        if (ReadSettingManager.getInstance().isOpenProtectedEye()) {
            ProtectedEyeUtils.closeProtectedEyeMode(this);
            RxLogTool.e("NewReadActivity onPause openProtectedEyeMode true--closeProtectedEyeMode");
        } else {
            RxLogTool.e("NewReadActivity onPause openProtectedEyeMode false");
        }

        //暂停，上传阅读时长
        if (readTimeBeanList.size() > 0) {
            uploadTimeline();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
        mySM.unregisterListener(this);

        if (!RxAppTool.isAppForeground(this) && Constant.isAutoPage) {//当前是自动翻页
            //退出自动翻页
            mPageLoader.setPageMode(prePageMode);
            Constant.isAutoPage = false;
        }
    }

    @Override
    protected void onDestroy() {

        try {
            mPageLoader.saveRecord();
        }catch (Exception e){

        }

        //清除电池时间相关广播
        unregisterReceiver(mReceiver);
        RxEventBusTool.unRegisterEventBus(this);
        unregisterReceiver(phoneStateReceiver);

        //清空队列
        if (null != myHandler) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (null != barrageTimer) {
            barrageTimer.cancel();
        }

        //释放听书服务
        unbindService(myConn);
        myConn=null;

        if (Constant.isAutoPage) {
            Constant.isAutoPage = false;
        }

        mHandler.removeMessages(WHAT_CATEGORY);
        mHandler.removeMessages(WHAT_CHAPTER);

        mPageLoader.closeBook();
        mPageLoader = null;

        if (ReadSettingManager.getInstance().isOpenProtectedEye()) {
            ProtectedEyeUtils.closeProtectedEyeMode(this);
        }

        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager
                .getInstance().isVolumeTurnPage();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                RxLogTool.e("onKeyDown KEYCODE_BACK...");
                if (exit()) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (!Constant.isListenBook && !Constant.isAutoPage && isVolumeTurnPage) {
                    return mPageLoader.skipToPrePage();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (!Constant.isListenBook && !Constant.isAutoPage && isVolumeTurnPage) {
                    return mPageLoader.skipToNextPage();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MORE_SETTING) {
            boolean fullScreen = ReadSettingManager.getInstance().isFullScreen();
            if (isFullScreen != fullScreen) {
                isFullScreen = fullScreen;
                // 刷新BottomMenu
                initBottomMenu();
            }

            // 设置显示状态
            if (isFullScreen) {
                SystemBarUtils.hideUnStableNavBar(this);
            } else {
                SystemBarUtils.showUnStableNavBar(this);
            }
        }
    }


    /**
     * 排序
     */
    @OnClick(R.id.iv_sort_category)
    void sortCategoryClick() {
        if (isSortDesc) {//顺序排序
            iv_sort_category.setImageResource(R.mipmap.icon_category_asc);
        } else {
            iv_sort_category.setImageResource(R.mipmap.icon_category_desc);
        }
        isSortDesc = !isSortDesc;
        //数据倒序
        sortCategory();
    }

    private void sortCategory() {
        if (null != categoryChaptersList) {
            Collections.reverse(categoryChaptersList);

            mCategoryAdapter.setSortDesc(isSortDesc);

            mCategoryAdapter.refreshItems(categoryChaptersList);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    int position;
                    if (isSortDesc) {
                        position = mPageLoader.getChapterPos();//默认位置
                        mLvCategory.setSelection(position);
                    } else {
                        position = 0;
                        mLvCategory.setSelection(position);
                    }
                    RxLogTool.e("sortCategory position:" + position);
                }
            }, 100);
        }

    }

    /**
     * 缓存书本
     */
    @OnClick(R.id.iv_download_book_black)
    void downloadBookClick() {
        if (!isCategoryFinish) return;//未加载完成不可点击

        if (!RxNetTool.isAvailable()) {//离线模式
            RxToast.custom(getResources().getString(R.string.network_is_not_available), Constant.ToastType.TOAST_NETWORK_DISCONNECT).show();
            return;
        }

        if (!RxLoginTool.isLogin()) {//登录提示
            showLoginDialog();
            return;
        }

//        String serviceName=DownloadService.class.getName();
//        RxLogTool.e("serviceName:"+serviceName);
//        if (!RxServiceTool.isRunningService(this,serviceName)){//服务未运行时
//            RxLogTool.e("NewReadActivity DownloadService restart....");
//            try{
//                startService(new Intent(BuglyApplicationLike.getContext(), DownloadService.class));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }else{
//            RxLogTool.e("NewReadActivity DownloadService running....");
//        }
//        //是否是下载中
//        isDownloading = DownloadService.isDownloading;

        if (isDownloading) {
            RxToast.custom("正在缓存中,请耐心等待").show();
            return;
        }
        //埋点接口
        offLineDownloadRequest();
        if (null == rxDialogCacheChapter) {
            rxDialogCacheChapter = new RxDialogCacheChapter(this);
            rxDialogCacheChapter.setCacheChapterCallback(new RxDialogCacheChapter.CacheChapterCallback() {
                @Override
                public void cacheChapter(int type) {
                    RxLogTool.e("downloadBookClick type:" + type);
                    //书籍目录加载未完成
                    if (null == mCollBook.getBookChapters() || mCollBook.getBookChapters().size() == 0) {
                        RxToast.custom("书籍目录加载未完成").show();
                        return;
                    }
                    //点击缓存动作之后，自动加入书架
                    if (isCategoryFinish && null != mBookInfoBean && RxLoginTool.isLogin() && !mBookInfoBean.getData().isIsConcern()) {//已登录-并且未关注-并且目录加载完毕
                        requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()));
                    }

                    isDownloading = true;
                    List<BookChapterBean> bookChapterBeans = new ArrayList<>();
                    bookChapterBeans.addAll(mCollBook.getBookChapters());//临时集合
                    RxLogTool.e("temp bookChapterBeans size:" + bookChapterBeans.size());

                    if (type == 0) {//后50章-当前章节后面有50章就缓存50，否则就缓存当前章节后面所有的章节
                        if (mCategoryAdapter.getCurrentSelected() + 51 < totalChapterCount) {//当前章节后面的50章
                            bookChapterBeans = bookChapterBeans.subList(mCategoryAdapter.getCurrentSelected() + 1, mCategoryAdapter.getCurrentSelected() + 51);
                        } else {
                            bookChapterBeans = bookChapterBeans.subList(mCategoryAdapter.getCurrentSelected() + 1, totalChapterCount);
                        }
                    } else if (type == 1) {//缓存当前章节后面的全部章节
                        bookChapterBeans = bookChapterBeans.subList(mCategoryAdapter.getCurrentSelected() + 1, totalChapterCount);
                    } else if (type == 2) {//整本缓存

                    }

                    RxLogTool.e("cache bookChapterBeans size:" + bookChapterBeans.size());
                    RxToast.custom("开始缓存中").show();

                    //发起一个缓存任务
                    DownloadTaskBean task = new DownloadTaskBean();
                    task.setTaskName(mCollBook.get_id() + "_" + System.currentTimeMillis());
                    task.setBookId(mCollBook.get_id());
                    task.setBookChapters(bookChapterBeans);//需要缓存的章节
                    task.setLastChapter(bookChapterBeans.size());
                    //开始执行任务
                    executeTask(task);
//                    RxBus.getInstance().post(task);
                }
            });
        }
        rxDialogCacheChapter.show();
    }


    /*
     * 具体下载方法开始
     *
     * */
    OutsideChapterDetail outsideChapterDetail;
    private boolean isCancel = false;
    ReadSettingsResponse readSettingsResponse;
    //加载状态
    private static final int LOAD_ERROR = -1;
    private static final int LOAD_NORMAL = 0;
    private static final int LOAD_PAUSE = 1;

    //开始下载
    private void executeTask(DownloadTaskBean taskEvent) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Runnable runnable = () -> {
            RxLogTool.e("executeTask thread name:" + Thread.currentThread().getName());
            isDownloading = true;
            taskEvent.setStatus(DownloadTaskBean.STATUS_LOADING);
            int result = LOAD_NORMAL;
            List<BookChapterBean> bookChapterBeans = taskEvent.getBookChapters();
            int currentTotalChapter = bookChapterBeans.size();//本地缓存的总共章节
            //调用for循环，下载数据
            for (int i = taskEvent.getCurrentChapter(); i < bookChapterBeans.size(); ++i) {
                BookChapterBean bookChapterBean = bookChapterBeans.get(i);
                RxLogTool.e("executeTask bookChapterBean:" + bookChapterBean.getTitle());
                //首先判断该章节是否曾经被加载过 (从文件中判断)
                if (BookManager
                        .isChapterCached(taskEvent.getBookId(), bookChapterBean.getId())) {
                    //设置任务进度
                    taskEvent.setCurrentChapter(i);
                    //章节加载完成
                    postDownloadChange(taskEvent, DownloadTaskBean.STATUS_LOADING, (i + 1) + "", currentTotalChapter, taskEvent.getBookId());
                    //无需进行下一步
                    continue;
                }
                //判断网络是否出问题
                if (!NetworkUtils.isAvailable(BuglyApplicationLike.getContext())) {
                    result = LOAD_ERROR;
                    break;
                }
                if (isCancel) {
                    result = LOAD_PAUSE;
                    isCancel = false;
                    break;
                }
                //加载数据
                result = loadChapter(taskEvent.getBookId(), bookChapterBean);
                //章节加载完成
                if (result == LOAD_NORMAL) {
                    taskEvent.setCurrentChapter(i);
                    postDownloadChange(taskEvent, DownloadTaskBean.STATUS_LOADING, i + "", currentTotalChapter, taskEvent.getBookId());
                    RxLogTool.e("cache success chapter:" + i);
                }
            }
            if (result == LOAD_NORMAL) {
                //存储DownloadTask的状态
                taskEvent.setStatus(DownloadTaskBean.STATUS_FINISH);//Task的状态
                taskEvent.setCurrentChapter(taskEvent.getBookChapters().size());//当前下载的章节数量
                taskEvent.setSize(BookManager.getBookSize(taskEvent.getBookId()));//Task的大小
                //发送完成状态
                postDownloadChange(taskEvent, DownloadTaskBean.STATUS_FINISH, "下载完成", currentTotalChapter, taskEvent.getBookId());
            } else if (result == LOAD_ERROR) {
                taskEvent.setStatus(DownloadTaskBean.STATUS_ERROR);//Task的状态
                //任务加载失败
                postDownloadChange(taskEvent, DownloadTaskBean.STATUS_ERROR, "网络已断开，缓存已终止", currentTotalChapter, taskEvent.getBookId());
            }
            isDownloading = false;
            //存储状态
            LocalRepository.getInstance().saveDownloadTask(taskEvent);
            //关闭
            executorService.shutdown();
        };
        executorService.execute(runnable);
    }

    private int loadChapter(String mBookId, BookChapterBean bean) {
        //加载的结果参数
        final int[] result = {LOAD_NORMAL};
        //配置读取
        readSettingsResponse = RxSharedPreferencesUtil.getInstance().getObject(Constant.READ_SETTINGS_KEY, ReadSettingsResponse.class);
        Disposable disposable = mPresenter.getmReaderApi()
                .outsideBookChapterDetail(bean.getLink())
                //表示在当前环境下执行
                .subscribe(
                        responseBody -> {
                            try {
                                //当前章节的源
                                String chapterSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_" + bean.getId() + "_source");
                                String currentSource;
                                //取源
                                if (!TextUtils.isEmpty(chapterSource)) {
                                    currentSource = chapterSource;//本章节有修复后记录的源，则优先
                                } else {
                                    currentSource = bean.getChapterSource();//直接取书换源获得的源，或者是智能目录安排的源
                                }
                                String charset = "UTF-8";
                                String regContent = null;
                                String[] illegalWords = null;
                                for (int i = 0; i < readSettingsResponse.getReadSettings().size(); i++) {
                                    if (readSettingsResponse.getReadSettings().get(i).getSource().equals(currentSource)) {
                                        RxLogTool.e("source:" + readSettingsResponse.getReadSettings().get(i).getSource());
                                        charset = readSettingsResponse.getReadSettings().get(i).getCharset();
                                        regContent = readSettingsResponse.getReadSettings().get(i).getRegContent();
                                        illegalWords = readSettingsResponse.getReadSettings().get(i).getIllegalWords();
                                    }
                                }

                                byte[] responseBytes = responseBody.bytes();//转码
                                String responseData = new String(responseBytes, charset);
                                outsideChapterDetail = ParserService.getDataBeanFromHtml(responseData, regContent, illegalWords);
                                if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                    String content = RxTool.filterChar(outsideChapterDetail.getContent());
                                    // RxLogTool.e("DownloadService cache chapter content:"+content);
                                    //存储数据
                                    BookRepository.getInstance().saveChapterInfo(mBookId, bean.getId(), content);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        e -> {
                            //当前进度加载错误（这里需要判断是什么问题，根据相应的问题做出相应的回答）
                            LogUtils.e("test", "loadChapter error:" + e.getMessage());
                        }
                );
        addDisposable(disposable);
        return result[0];
    }

    private void postDownloadChange(DownloadTaskBean task, int status, String msg, int totalChapter, String mBookId) {
        //通过handler,切换回主线程
        mHandler.post(
                () -> {
                    onDownloadChange(0, status, msg, totalChapter, mBookId);
                }
        );
    }

    /*
     * 具体下载方法终止
     *
     * */
    public void onDownloadChange(int pos, int status, String msg, int currentTotalChapters, String bookId) {

        if (status == DownloadTaskBean.STATUS_FINISH) {//本地缓存下载完成
            isDownloading = false;
            RxLogTool.e("finishCacheChapter cache success");
            RxToast.custom("本次缓存已完成", Constant.ToastType.TOAST_SUCCESS).show();

            if (null != ll_chapter_tip && bookId.equals(mBookId)) {//当前缓存的书是打开的这本书
                ll_chapter_tip.setVisibility(View.GONE);
                cacheChapterCount = 0;
                //刷新目录状态
                mCategoryAdapter.notifyDataSetChanged();
                //刷新数据
                initCacheChapterData();
            }
        } else if (status == DownloadTaskBean.STATUS_LOADING) {//正在下载
            isDownloading = true;//正在下载中
            if (null != mLlBottomMenu && bookId.equals(mBookId)) {
                RxLogTool.e("cacheChapterProgress cache success cacheChapterCount:" + cacheChapterCount);
                cacheChapterCount++;
                if (mLlBottomMenu.getVisibility() == VISIBLE) {//只有底部的菜单栏在的时候才显示
                    ll_chapter_tip.setVisibility(View.VISIBLE);
                }
                tv_chapter_status.setText("正在缓存中...");
                iv_return_chapter.setVisibility(View.GONE);
                tv_cache_chapter_progress.setText(msg + "/" + currentTotalChapters);
                //缓存中，弹幕不显示
                isShowBarrageView(false);
            }
        } else if (status == DownloadTaskBean.STATUS_ERROR) {
            if (null != tv_chapter_status && bookId.equals(mBookId)) {
                tv_chapter_status.setText("已中断缓存操作");
            }
            RxToast.custom(msg, Constant.ToastType.TOAST_ERROR).show();

            isDownloading = false;//已结束
        } else {
            isDownloading = false;//已结束
        }
    }


    /**
     * 书目录缓存按钮数据初始化
     */
    private void initCacheChapterData() {
        cacheChapterCont = BookManager.getCacheChapterCount(mBookId);
//        RxLogTool.e("cacheChapterCont:" + cacheChapterCont);

        if (cacheChapterCont > 0) {
            tv_book_cache_status.setText("已缓存" + cacheChapterCont + "章");
            tv_book_cache_status.setCompoundDrawables(RxImageTool.getDrawable(23, R.mipmap.icon_book_cached_gray), null, null, null);
            tv_book_cache_status.setTextColor(getResources().getColor(R.color.color_black_ff333333_alpha5));

            if (isNightMode) {

            } else {

            }
        } else {
            tv_book_cache_status.setText("缓存");
            tv_book_cache_status.setCompoundDrawables(RxImageTool.getDrawable(23, R.mipmap.icon_book_cached), null, null, null);
            tv_book_cache_status.setTextColor(getResources().getColor(R.color.v2_blue_color));

            if (isNightMode) {

            } else {

            }
        }

        rl_book_cache.setOnClickListener(
                v -> {
                    //缓存
                    downloadBookClick();
                }
        );
        //刷新目录
        mCategoryAdapter.notifyDataSetChanged();
    }

    /*缓存书籍埋点请求*/
    private void offLineDownloadRequest() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/task/offLineDownload", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //成功
                    } else {//加载失败
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*听书埋点请求*/
    private void toListenBookRequest() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/task/toListenBook", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //成功
                    } else {//加载失败
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int listenRetryCount;

    List<PageParagraphVo> pageParagraphVos = new ArrayList<>();
    public static  MusicService.MyBinder myBinder;//中间人对象
    private MyConn myConn;

    private void registerService(){
        Intent intent=new Intent(this,MusicService.class);
//        startService(intent);
        myConn = new MyConn();
        //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
        bindService(intent, myConn,BIND_AUTO_CREATE);
    }

    public class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //获取中间人对象
            myBinder = (MusicService.MyBinder) service;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }
    }

    /**
     * 听书
     */
    @OnClick(R.id.iv_listen_book)
    void listenBookClick() {


        if (!RxNetTool.isAvailable()) {
            RxToast.custom("没有网络无法开启听书模式").show();
            return;
        }
        if (!RxLoginTool.isLogin()) {
            showLoginDialog();
            return;
        }
        if (isCategoryFinish && null != mPageLoader) {
            //获取听书数据
            mPresenter.prepareListenBookData(mPageLoader,true);
            if (null == pageParagraphVos || pageParagraphVos.isEmpty()) {
                RxToast.custom("本章还未下载,无法开启听书模式").show();
                return;
            }
            toggleMenu();//隐藏
            mPageLoader.setListenBookStyle(true, 1);
            RxToast.custom("开启听书模式").show();
            if (listenMin == 0) {
                listenEndTime = 0;
            } else if (listenMin == 15) {
                listenEndTime = 15 * 60 * 1000 + System.currentTimeMillis();
            } else if (listenMin == 30) {
                listenEndTime = 30 * 60 * 1000 + System.currentTimeMillis();
            } else if (listenMin == 60) {
                listenEndTime = 60 * 60 * 1000 + System.currentTimeMillis();
            }
            if (null != myBinder) {
                myBinder.play(pageParagraphVos);
            }
            listenStartTime = System.currentTimeMillis();
            toListenBookRequest();
        }

    }


    @Override
    public void returnListenBookDataView(List<PageParagraphVo> mPageParagraphVos) {
        if(!Constant.isListenBook){
            Constant.isListenBook = true;//听书模式
        }
        pageParagraphVos.clear();
        if (null != mPageParagraphVos) {
            pageParagraphVos.addAll(mPageParagraphVos);
        }

    }


    private void dealFinish(String utteranceId) {
        if(!RxNetTool.isAvailable()){
            showListenMessageDialog("网络断开，退出听书模式");
            exitListenBook();
            return;
        }

        if (listenEndTime != 0 && System.currentTimeMillis() > listenEndTime) {//倒计时结束
            exitListenBook();
            CrashSaver.saveLog("定时退出听书模式");
            showListenMessageDialog("本次定时听书完成，已自动退出听书模式");
        } else if (System.currentTimeMillis() - listenStartTime > 10800000) {
            exitListenBook();
            CrashSaver.saveLog("您已连续听书3小时退出听书模式");
            showListenMessageDialog("您已连续听书3小时了，快休息一下吧！");
        } else if (null == pageParagraphVos) {
            exitListenBook();
            CrashSaver.saveLog("数据异常，退出听书");
            showListenMessageDialog("数据异常，退出听书");
        } else {
            int size = pageParagraphVos.size();
            if (Integer.parseInt(utteranceId) < pageParagraphVos.get(size - 1).paragraphPosition) {
                //不需要翻页的情况
                mPageLoader.setListenBookStyle(true, Integer.parseInt(utteranceId) + 1);
            } else {
                //需要翻页的情况
                if (mPageLoader.getChapterPos() == mPageLoader.getChapterCategory().size() - 1 && mPageLoader.getPagePos() == mPageLoader.getCurPageList().size() - 1) {//是否能翻页
                    exitListenBook();
                    CrashSaver.saveLog("最后一章已经阅读完毕退出听书模式");
                    RxLogTool.e("最后一章已经阅读完毕，自动退出听书模式" + System.currentTimeMillis());
                    showListenMessageDialog("最后一章已经阅读完毕，自动退出听书模式");
                } else {
                    mPageLoader.skipToNextPage();//下一页
                    //默认选中第一行
                    mPageLoader.setListenBookStyle(true, 1);
                    mPresenter.prepareListenBookData(mPageLoader,true);
                    if (null != myBinder) {
                        myBinder.play(pageParagraphVos);
                    }
                    CrashSaver.saveLog("翻页动作，当前页：" + mPageLoader.getCurPage().position + "/" + mPageLoader.getCurPageList().size() + "\n");
                }
            }
        }
    }

    /**
     * 自动翻页弹框
     */
    private void showAutoPageDialog() {
        if (null == readAutoPageDialog) {
            readAutoPageDialog = new ReadAutoPageDialog(this, new ReadAutoPageDialog.ListenModeChangeCallback() {
                @Override
                public void changePageSpeed(long speed) {
                    RxSharedPreferencesUtil.getInstance().putLong(Constant.AUTO_PAGE_TIME, speed);
                    //设置自动翻页时间
                    autoPageTime = speed;
                    mPvPage.setAutoPageTime(autoPageTime);
                }
            }, autoPageTime);
        }
        readAutoPageDialog.show();
        //夜间白天模式切换
        readAutoPageDialog.toggleDayNightMode(isNightMode);

        readAutoPageDialog.tv_exit_listen_book.setOnClickListener(
                v -> {
                    RxToast.custom("退出自动翻页").show();
                    readAutoPageDialog.dismiss();
                    //退出自动翻页
                    mPageLoader.setPageMode(prePageMode);
                    Constant.isAutoPage = false;
                }
        );

        readAutoPageDialog.setOnDismissListener(
                v -> {
                    hideSystemBar();
                }
        );
    }

    /**
     * 听书弹框
     */
    private void showReadListenDialog() {
        if (null == readListenDialog) {
            readListenDialog = new ReadListenDialog(this, new ReadListenDialog.ListenModeChangeCallback() {
                @Override
                public void changeVoiceSpeed(int speed) {
                    ReadSettingManager.getInstance().setVoiceSpeed(speed);
                }

                @Override
                public void changeVoicer(String voicer) {
                    ReadSettingManager.getInstance().setVoicer(voicer);
                }

                @Override
                public void listenTimer(int min) {
                    listenMin = min;
                    if (min == 0) {
                        listenEndTime = 0;
                    } else {
                        listenEndTime = min * 60 * 1000 + System.currentTimeMillis();
                        RxLogTool.e("listenEndTime:" + listenEndTime);
                    }
                }
            });
        }
        RxLogTool.e("listenEndTime:" + listenEndTime);
        RxLogTool.e("listenMin:" + listenMin);
        readListenDialog.show();
        //夜间白天模式切换
        readListenDialog.toggleDayNightMode(isNightMode, listenMin);

        //暂停听书
        if (null != myBinder) {
            myBinder.stop();
        }

        readListenDialog.tv_exit_listen_book.setOnClickListener(
                v -> {
                    exitListenBook();
                    RxToast.custom("退出听书模式").show();
                    readListenDialog.dismiss();
                }
        );

        readListenDialog.setOnDismissListener(
                v -> {
                    hideSystemBar();
                    if (Constant.isListenBook) {
                        if (null != myBinder) {
                            myBinder.setParams(2);
                        }
                        //获取听书数据
                        mPresenter.prepareListenBookData(mPageLoader,true);
                        if (null != myBinder) {
                            myBinder.play(pageParagraphVos);
                        }
                    }
                }
        );
    }

    private void exitListenBook() {
        listenEndTime = 0;
        Constant.isListenBook = false;
        if (null != myBinder) {
            myBinder.stop();
            myBinder.dissmissNotification();
        }
        if (null != mPvPage) {
            mPvPage.setEnabled(true);
        }
        if (null != mPageLoader) {
            mPageLoader.setListenBookStyle(false, 0);
        }
        //退出听书阅读模式，上传一次阅读记录
        uploadTimeline();
    }

    /**
     * 提交反馈
     */
    @OnClick(R.id.read_tv_feedback)
    void readFeedbackClick() {
        //跳往发表书评页面
        //跳到发表动态的页面
        if(null == mBookInfoBean){
            RxToast.custom("书籍信息未加载").show();
            return;
        }
        if(null == mPageLoader){
            RxToast.custom("页面未加载完毕").show();
            return;
        }
        if(null == mPageLoader.getCurrentTxtChapter()){
            RxToast.custom("页面未加载完毕").show();
            return;
        }

        //隐藏红点
        view_post_red.setVisibility(View.GONE);
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.FIRST_POST_KEY,false);

        String circleId = String.valueOf(mBookInfoBean.getData().getPlatformId());
        String bookName = mBookInfoBean.getData().getPlatformName();
        String chapterId = mPageLoader.getCurrentTxtChapter().getChapterId();
        String chapterName = mPageLoader.getCurrentTxtChapter().getTitle();
        String cover = mBookInfoBean.getData().getPlatformCover();
        String source = mPageLoader.getCurrentTxtChapter().getSourceKey();
        String url = mPageLoader.getCurrentTxtChapter().getLink();
        ActivityPostCircle.startActivity(NewReadActivity.this,circleId,mBookId,bookName,chapterId,chapterName,cover,source,url);
    }



    /**
     * 每翻一页，添加一条阅读时长数据
     *
     * @param pagePos
     * @param chapterId
     * @param pageTotalCount
     * @param mTextSize
     */
    private void addReadTimeData(int pagePos, int chapterId, int pageTotalCount, int mTextSize) {
        if (isStartOperation && !isOperationChapter) {//用户开始操作，才开始纳入阅读时长统计
            clickCount++;
            turnCount++;
            ReadTimeBean readTimeBean = new ReadTimeBean();
            readTimeBean.setBookId(Integer.parseInt(mBookId));
            readTimeBean.setChapterId(chapterId);

            readTimeBean.setClickCount(clickCount);
            readTimeBean.setStartTime(startEnterPageTime);

            readTimeBean.setEndTime(System.currentTimeMillis());//当前时间
            readTimeBean.setUserId(userId);
            readTimeBean.setPhoneModel(phoneModel);

            readTimeBean.setPageCount(pageTotalCount);
            readTimeBean.setPageNum(pagePos + 1);//页码从0开始
            readTimeBean.setFont(mTextSize);

            if (RxNetTool.isAvailable()) {
                if (Constant.isListenBook) {
                    readTimeBean.setReadType(3);//听书
                } else {
                    readTimeBean.setReadType(1);//普通看书
                }
            } else {
                readTimeBean.setReadType(2);//离线看书
            }


            readTimeBean.setAngle(angle);

            readTimeBean.setDeviceCode(deviceNo);
            if (null != mPvPage) {
                readTimeBean.setTurnCoordinate(mPvPage.getXY());
            } else {
                readTimeBean.setTurnCoordinate("100,200");
            }

            readTimeBean.setTurnCount(turnCount);


            if (readTimeBean.getStartTime().longValue() != readTimeBean.getEndTime().longValue()) {

                if (readTimeBeanList.size() > 0) {
                    ReadTimeBean readTimeBeanLast = readTimeBeanList.get(readTimeBeanList.size() - 1);
                    if (readTimeBeanLast.getChapterId() == readTimeBean.getChapterId() && readTimeBean.getPageNum() == readTimeBeanLast.getPageNum() && readTimeBeanLast.getEndTime() == readTimeBean.getStartTime()) {//过滤重复页面
                        //重复记录不予记录
                        clickCount--;
                        turnCount--;
                    } else {
                        readTimeBeanList.add(readTimeBean);
                        //复位起始时间
                        startEnterPageTime = readTimeBean.getEndTime();
                    }
                } else {
                    readTimeBeanList.add(readTimeBean);
                    //复位起始时间
                    startEnterPageTime = readTimeBean.getEndTime();
                }

            }
        }

        if (Constant.isListenBook) {
            if (!RxAppTool.isAppForeground(this) && readTimeBeanList.size() >= 1) {//听书模式-后台运行-每读一页上传一次
                uploadTimeline();//提交一次
            } else if (readTimeBeanList.size() >= 20) {//听书模式-未在后台运行-每5页传一次
                uploadTimeline();//提交一次
            }
        } else {
            if (readTimeBeanList.size() >= 20) {
                uploadTimeline();
            }
        }

    }

    /**
     * 上传有效时长
     */
    private void uploadTimeline() {//上传阅读时长
//        if (RxLoginTool.isLogin() && !TextUtils.isEmpty(userId)) {
//            readTimePresenter.uploadReadTime(userId, readTimeBeanList, 1);
//        }
    }

    @OnClick(R.id.img_back)
    void closeClick() {//退出阅读
        exit();
    }


    /**
     * ------------------------------------弹幕--------------------------------------------------------------------
     */

    Timer barrageTimer;
    TimerTask barrageTimerTask;
    int currentIndex = 0;
    public void startBarrageAnimation() {
        if (null == barrage_view) return;
        if (barrage_view.getBarrageBeanList().size() <= 0) {//有弹幕才跑动画
            return;
        }
        //只初始化一次
        if (null == barrageTimer) {
            barrageTimer = new Timer();
            barrageTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!barragePause) {
                        if (currentIndex == barrage_view.getBarrageBeanList().size() - 1) {
                            currentIndex = 0;
                        } else {
                            currentIndex++;
                        }
                        if (barrage_view.getBarrageBeanList().size() < 3 && barrage_view.getViewCount() == barrage_view.getBarrageBeanList().size()) {
                            barragePause = true;
                        } else {
                            if (barrage_view.getViewCount() < 3) {
                                myHandler.sendEmptyMessage(0);//add view
                            } else {
                                myHandler.sendEmptyMessage(1);//remove view
                            }
                        }
                    }
                }
            };
            barrageTimer.schedule(barrageTimerTask, 0, 1000);
        }

    }

    public static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewReadActivity newReadActivity = (NewReadActivity) reference.get();
            try {
                if (msg.what == 2) {
                    if (Constant.isAutoPage) {
                        //翻到下一页并再发送一个自动翻页消息
                        newReadActivity.mPageLoader.skipToNextPage();
                        sendEmptyMessageDelayed(2, newReadActivity.autoPageTime);
                    }
                }else if(msg.what == 3){
                    Constant.percent++;
                    if(Constant.percent > 0 && Constant.percent < 100) {
                        newReadActivity.tv_loading_percent.setText("正实时从原网页转码 "+ Constant.percent + "%");
                    }else {
                        newReadActivity.tv_loading_percent.setText("正实时从原网页转码 99%");
                    }
                    sendEmptyMessageDelayed(3, 50);
                } else if (null != newReadActivity.barrage_view.getBarrageBeanList()) {
                    if (msg.what == 0) {
                        newReadActivity.barrage_view.addAnimationView(newReadActivity.barrage_view.getBarrageBeanList().get(newReadActivity.currentIndex));
                    } else if (msg.what == 1) {
                        newReadActivity.barrage_view.removeFirstView(newReadActivity.barrage_view.getBarrageBeanList().get(newReadActivity.currentIndex));
                    }
                }
            } catch (Exception e) {
                RxLogTool.e("MyHandler e:" + e.getMessage());
            }
        }
    }

    private void isShowBarrageView(boolean isShow) {
        RxLogTool.e("isShowBarrageView:" + isShow);
        if (showBarrage) {
            if (isShow) {
                barragePause = false;

                if (barrage_view.getBarrageBeanList().size() <= 0) {//有弹幕才跑动画
                    iv_close_barrage.setVisibility(View.GONE);
                    barrage_view.setVisibility(View.GONE);
                } else {
                    iv_close_barrage.setVisibility(View.VISIBLE);
                    barrage_view.setVisibility(View.VISIBLE);
                }

                iv_chat_room_enter.setVisibility(View.VISIBLE);
                if (iv_chat_room_enter.getVisibility() == VISIBLE && iv_chat_room_enter.getBackground() instanceof AnimationDrawable) {
                    AnimationDrawable animation = (AnimationDrawable) iv_chat_room_enter.getBackground();
                    animation.start();
                }
            } else {
                iv_close_barrage.setVisibility(View.GONE);
                barrage_view.setVisibility(View.GONE);
                iv_chat_room_enter.setVisibility(View.GONE);

                barragePause = true;
            }

        } else {
            iv_close_barrage.setVisibility(View.GONE);
            barrage_view.setVisibility(View.GONE);
            iv_chat_room_enter.setVisibility(View.GONE);

            barragePause = true;
        }
    }

    private void requestChatRoomIsOpen(String platformId) {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/isopen/" + platformId, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("requestChatRoomIsOpen:" + response);
                    isOpenChatRoomResponse = GsonUtil.getBean(response, IsOpenChatRoomResponse.class);
                    if (isOpenChatRoomResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE ){

                        //弹幕数据初始化
                        List<BarrageBean> barrageBeanList = new ArrayList<>();

                        for (IsOpenChatRoomResponse.DataBean.ChatroomRecentNewsListBean bean : isOpenChatRoomResponse.getData().getChatroomRecentNewsList()) {
                            barrageBeanList.add(new BarrageBean(bean.getFromAvator(), bean.getAttach()));
                        }
                        //倒序显示
                        Collections.reverse(barrageBeanList);
                        barrage_view.setBarrageBeanList(barrageBeanList);
                        //表示可以显示弹幕
                        showBarrage = true;
                        //弹幕动画
                        startBarrageAnimation();

                        //假如正在展示中。显示弹幕。
                        if (isShow) {
                            iv_chat_room_enter.setVisibility(View.VISIBLE);
                            isShowBarrageView(barrageBeanList.size() > 0 ? true : false);
                        }

                    } else {
                        showBarrage = false;
                        closeBarrageView();//关闭弹幕
                        iv_chat_room_enter.setVisibility(View.GONE);
//                        RxToast.custom("该连载号聊天室暂未开通").show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 弹幕关闭
     */
    @OnClick(R.id.iv_close_barrage)
    void closeBarrageView() {
        barrage_view.setVisibility(View.GONE);
        iv_close_barrage.setVisibility(View.GONE);
        barragePause = true;
    }

    private boolean showBarrage = false;//是否要显示弹幕 //表示可以显示弹幕

    /**
     * 进入聊天室
     */
    @OnClick(R.id.iv_chat_room_enter)
    void chatRoomClick() {
        if (null == mBookInfoBean) return;
        if (null == mBookInfoBean.getData()) return;
        ActivityCircleDetail.startActivity(this, String.valueOf(mBookInfoBean.getData().getPlatformId()));
    }

    /**
     * 弹幕初始化
     */
    private void initBarrageData() {
        if (null == myHandler) {
            myHandler = new MyHandler(this);
        }
            //弹幕宽度设置
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(2 * ScreenUtil.getDisplayWidth() / 3, 3 * ScreenUtil.dip2px(38));
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.iv_chat_room_enter);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            barrage_view.setLayoutParams(layoutParams);
            barrage_view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        barragePause = true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        barragePause = false;
                    }
                    return true;
                }
            });
    }



    /**
     * 提交反馈请求
     */
    private void postFeedback(String typeId, String bookId, String mChapterId, String content, boolean isAutoUpload, String chapterTitle) {
        HashMap map = new HashMap();
        map.put("typeId", typeId);
        map.put("bookId", bookId);

        map.put("chapterId", mChapterId);

        if (!TextUtils.isEmpty(chapterTitle)) {
            map.put("chapterTitle", chapterTitle);
        }

        if (!TextUtils.isEmpty(content)) {
            map.put("content", content);
        }

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/feedback/save", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    if (!isAutoUpload) {
                        BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                        if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                            RxToast.custom("反馈成功").show();
                        } else {
                            RxToast.custom("反馈失败").show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 关闭引导图层
     */
    @OnClick(R.id.iv_guide_read)
    void clickGuideView() {
        iv_guide_read.setVisibility(GONE);
        RxSharedPreferencesUtil.getInstance().putBoolean("is_show_read_guide", false);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // 获取手机触发event的传感器的类型
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                accValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magValues = event.values.clone();
                break;
        }
        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        SensorManager.getOrientation(r, values);

        // 获取　沿着Z轴转过的角度
        int zAngle = (int) Math.toDegrees(values[0]);

        // 获取　沿着X轴倾斜时　与Y轴的夹角
        int yAngle = (int) Math.toDegrees(values[1]);

        // 获取　沿着Y轴的滚动时　与X轴的角度
        int xAngle = (int) Math.toDegrees(values[2]);

        angle = xAngle + "," + yAngle + "," + zAngle;

//        RxLogTool.e("xAngle:"+xAngle+"---zAngle:"+zAngle+"--yAngle:"+yAngle);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    /**
     * 关注
     */
    private void requestConcernPlatform(String platformId) {
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + platformId + "/Concern", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore();
                        //更新关注信息
                        mBookInfoBean.getData().setIsConcern(true);
                        RxToast.custom("已成功添加至书架", Constant.ToastType.TOAST_SUCCESS).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }


    //同步书架数据
    private void requestBookStore() {
        ArrayMap map = new ArrayMap();
        long timestamp = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
        if (timestamp > 0) {//
            map.put("timestamp", String.valueOf(timestamp));
        }
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/shelf/refresh", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    BookStoreResponse bookStoreResponse = GsonUtil.getBean(response, BookStoreResponse.class);
                    if (bookStoreResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<BookStoreBeanN> bookStoreBeanList = new ArrayList<>();
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getDelete_List() && bookStoreResponse.getData().getDelete_List().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getDelete_List()) {//本地移除该书
                                if (!TextUtils.isEmpty(bookStoreBean.getBookId())) {
                                    //移除该书,旧书模式
                                    BookStoreRepository.getInstance().deleteAllByPlatformIdAndUid(Integer.parseInt(userId), bookStoreBean.getPlatformId());
                                }
                            }
                        }
                        //需要删除的书籍
                        if (null != bookStoreResponse.getData().getRecognitionDeleteList() && bookStoreResponse.getData().getRecognitionDeleteList().size() > 0) {
                            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionDeleteList()) {//本地移除该书
                                if (!TextUtils.isEmpty(bookStoreBean.getBookId())) {
                                    //移除该书,新url模式
                                    BookStoreRepository.getInstance().deleteAllByBookIdAndUid(Integer.parseInt(userId), bookStoreBean.getBookId());
                                }
                            }
                        }
                        //给每本书指定用户ID
                        int i = 0;
                        long time = System.currentTimeMillis();
                        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionList()) {
                            i++;
                            bookStoreBean.setUid(Integer.parseInt(userId));
                            bookStoreBean.setUpdateDate(time - i);
                            bookStoreBeanList.add(bookStoreBean);
                        }
                        //给每本书指定用户ID
                        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getList()) {
                            i++;
                            bookStoreBean.setUid(Integer.parseInt(userId));
                            bookStoreBean.setUpdateDate(time - i);
                            bookStoreBeanList.add(bookStoreBean);
                        }
                        //缓存这次的请求时间
                        RxSharedPreferencesUtil.getInstance().putLong(bqtKey, bookStoreResponse.getData().getTimestamp());
                        //显示数据-缓存数据
                        BookStoreRepository.getInstance().saveBooks(bookStoreBeanList);
                        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_TAG);

                    } else {//加载失败
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 重新下载当前章节-修复章节
     */
    @OnClick(R.id.fl_reload_chapter)
    void reloadChapterClick() {
        if (!isCategoryFinish) return;

        ChapterUrlsVo chapterUrlsVo = getChapterUrlsVo();

        if (null == chapterUrlsVo) {//主要是由于目录接口还未响应
//            RxToast.custom("当前章节不可修复").show();
            return;
        }

        if (isChapterRepairing) {
            RxToast.custom("章节正在修复中,请稍后...").show();
            return;
        }

        RxLogTool.e("chapterUrlsVo url size:" + chapterUrlsVo.getUrlsVoList().size());

        try {
            isChapterRepairing = true;

                TxtChapter repairTxtChapter = mPageLoader.getCurrentTxtChapter();

                int selectSourceIndex = 0;
                for (int i = 0; i < chapterUrlsVo.getUrlsVoList().size(); i++) {
                    UrlsVo urlsVo = chapterUrlsVo.getUrlsVoList().get(i);
                    if (urlsVo.getSource().equals(repairTxtChapter.getSourceKey())) {

                        if (i == chapterUrlsVo.getUrlsVoList().size() - 1) {//当前的source是最后一个
                            selectSourceIndex = 0;
                        } else {
                            selectSourceIndex = i + 1;
                            break;
                        }
                    }
                }
                RxLogTool.e("reloadChapterClick selectSourceIndex:" + selectSourceIndex);

                RxLogTool.e("urlsVo.getSource():" + chapterUrlsVo.getUrlsVoList().get(selectSourceIndex).getSource());
                repairTxtChapter.setSourceKey(chapterUrlsVo.getUrlsVoList().get(selectSourceIndex).getSource());
                repairTxtChapter.setLink(chapterUrlsVo.getUrlsVoList().get(selectSourceIndex).getSourceUrl());

                RxSharedPreferencesUtil.getInstance().putString(mBookId + "_" + mPageLoader.getCurrentTxtChapter().getChapterId() + "_source", repairTxtChapter.getSourceKey());

                mPageLoader.setCurrentTxtChapter(repairTxtChapter);//可能会修复失败，那就轮到下一个


                if (null != mPageLoader && null != repairTxtChapter) {
                    mPresenter.reloadChapter(mBookId, repairTxtChapter);
                }
        } catch (Exception e) {

        }

    }

    @Override
    public void returnChapterUrlsView(List<ChapterUrlsVo> chapterUrlsVos) {//源URL列表
        if (null != chapterUrlsVos) {
            mChapterUrlsList = chapterUrlsVos;
        }
    }

    /**
     * 获取当前对应的url对象
     *
     * @return
     */
    private ChapterUrlsVo getChapterUrlsVo() {
        ChapterUrlsVo chapterUrlsVo = null;
        try {
            TxtChapter currentChapter = null;
            if (null != mPageLoader) {
                currentChapter = mPageLoader.getCurrentTxtChapter();
            }

            if (!TextUtils.isEmpty(mBookId) && null != mChapterUrlsList) {
                for (ChapterUrlsVo vo : mChapterUrlsList) {
                    if (null != currentChapter && vo.getBookId().equals(mBookId) && vo.getChapterId().equals(currentChapter.getChapterId())) {
                        chapterUrlsVo = vo;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapterUrlsVo;
    }


//    @Override
//    public void uploadErrorChapterView(List<BookChapterBean> errorBookChapterBeans) {
//        try {
//            if (null != errorBookChapterBeans && errorBookChapterBeans.size() > 0) {//上报给后台
//                //上报给后台
//                postFeedback("0", mBookId, errorBookChapterBeans.get(0).getId(), "章节有问题", true, mPageLoader.getCurrentTxtChapter().getTitle());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 登录弹框
     */
    private void showLoginDialog() {
        if (null == rxDialogGoLogin) {
            rxDialogGoLogin = new RxDialogGoLogin(this, R.style.OptionDialogStyle);
        }
        rxDialogGoLogin.setOnDismissListener(
                dialog -> {
                    hideSystemBar();
                }
        );
        rxDialogGoLogin.show();
    }


    /**
     * 换源
     */
//    @OnClick(R.id.tv_change_source)
//    void changeSourceClick() {
//        ChapterUrlsVo chapterUrlsVo = getChapterUrlsVo();
//        if (null == chapterUrlsVo) return;
//        if (null == rxDialogChapterSourceList) {
//            rxDialogChapterSourceList = new RxDialogChapterSourceList(this, R.style.BottomDialogStyle);
//        }
//        String source = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
//        if (!TextUtils.isEmpty(source) && null != mPageLoader.getCurrentTxtChapter() && !mPageLoader.getCurrentTxtChapter().getSourceKey().equals(source)) {
//            source = mPageLoader.getCurrentTxtChapter().getSourceKey();//主源与章节ID不一样的时候，优先显示章节的源
//        }
//
//        String chapterSource = null;
//        try {
//            chapterSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_" + mPageLoader.getCurrentTxtChapter().getChapterId() + "_source");
//        } catch (Exception e) {
//
//        }
//
//
//        rxDialogChapterSourceList.setData(chapterUrlsVo.getUrlsVoList(), TextUtils.isEmpty(chapterSource) ? source : chapterSource);
//
//        rxDialogChapterSourceList.setChapterChangeSourceListener(new RxDialogChapterSourceList.ChapterChangeSourceListener() {
//            @Override
//            public void changeChapterSourceClick(String source) {
//                try {
//                    RxLogTool.e("changeChapterSourceClick source:" + source);
//                    if (TextUtils.isEmpty(source)) {//移除这个source
//                        RxSharedPreferencesUtil.getInstance().remove(mBookId + "_source");
//                    } else {
//                        RxSharedPreferencesUtil.getInstance().putString(mBookId + "_source", source);
//                    }
//                    //批量清除章节ID source
//                    for (TxtChapter chapter : mPageLoader.getChapterCategory()) {
//                        RxSharedPreferencesUtil.getInstance().remove(mBookId + "_" + chapter.getChapterId() + "_source");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        BookManager.deleteBookAllChapter(mBookId);//清除这本书的所有文件
//                    }
//                }).start();
//
//                //重开一个阅读页面
//                //单例情况下不能重开。只刷新本页面数据。
////                startActivity(NewReadActivity.this, mBookId);
////                finish();
//                ArrayMap<String, Object> map = new ArrayMap();
//                map.put("isUpdate", false);
//                map.put("needReopen", true);
//                mPresenter.loadCategory(mBookId, map, chapterId);
//
//            }
//        });
//        rxDialogChapterSourceList.show();
//    }

    private void showListenMessageDialog(String message) {
        if (null == rxDialogSureCancelTip) {
            rxDialogSureCancelTip = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelTip.setButtonText("我知道了", "取消");
        rxDialogSureCancelTip.getCancelView().setVisibility(View.GONE);
        rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
        rxDialogSureCancelTip.setContent(message);
        rxDialogSureCancelTip.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelTip.dismiss();
            }
        });
        rxDialogSureCancelTip.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.READ_ACTIVITY_STOP_LISTEN_TAG) {//听书暂停
            if (Constant.isListenBook) {//退出听书模式
                exitListenBook();
            }
        } else if (event.getTag() == Constant.EventTag.CALL_STATE_IDLE_TAG) {//电话挂断
            RxLogTool.e("Constant.EventTag.CALL_STATE_IDLE_TAG...");
            if (Constant.isListenBook) {
                if (null != readListenDialog && readListenDialog.isShowing()) {
                    readListenDialog.dismiss();
                }
            }
        } else if (event.getTag() == Constant.EventTag.CALL_STATE_RINGING) {//来电
            RxLogTool.e("Constant.EventTag.CALL_STATE_RINGING...");
            //此时暂停听书，弹出设置框
            if (Constant.isListenBook) {
                showReadListenDialog();
            }
        } else if (event.getTag() == Constant.EventTag.REFRESH_BOOK_INFO) {//取消关注动作之后刷新书籍信息
            getBookInfo();
        }else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {
            //获取到个人信息后直接初始化信息，并更新书籍阅读时间。
            initReadTimeData();
            getBookInfo();
        }  else if (event.getTag() == Constant.EventTag.LOADCURRENTCHAPTERSUCCESS) {
            //当前章节加载成功
            showLoadingAnimation(false);
            if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
                mHandler.sendEmptyMessage(WHAT_CHAPTER);
            }
            // 当完成章节的时候，刷新列表
            initCacheChapterData();
        } else if (event.getTag() == Constant.EventTag.LOADCURRENTCHAPTERFAILED) {
            //当前章节加载失败
            showLoadingAnimation(false);
            ChapterUrlsVo chapterUrlsVo = getChapterUrlsVo();
            if (null != chapterUrlsVo && chapterUrlsVo.getUrlsVoList().size() > 1) {//大于1个站点
                //自动修复
                if (!isChapterRepairing)
                    reloadChapterClick();
            } else {//只有一个站点，自动修复也没用，直接提示
                if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
                    mPageLoader.chapterError();
                }
            }
        } else if (event.getTag() == Constant.EventTag.LOADCURRENTCHAPTERTIMEOUT) {
            //当前章节请求超时
            String content = event.getObj().toString();
            TxtChapter txtChapter = GsonUtil.getBean(content, TxtChapter.class);
            if (chapterRequestTryCount < 3) {//重新请求
                mPresenter.loadCurrentChapter(mBookId, txtChapter);
                chapterRequestTryCount++;
            } else {//重试三次失败，执行修复功能
                chapterRequestTryCount = 0;
                if (!isChapterRepairing)
                    reloadChapterClick();//修复该章节
            }
        } else if (event.getTag() == Constant.EventTag.RELOADCHAPTERSUCCESSVIEW) {
            //重新加载成功
            String content = event.getObj().toString();
            TxtChapter txtChapter = GsonUtil.getBean(content, TxtChapter.class);
            isChapterRepairing = false;
            if (null != mPageLoader) {//刷新当前章节
                mPageLoader.skipToChapter(mPageLoader.getChapterPos());
            }
        } else if (event.getTag() == Constant.EventTag.RELOADCHAPTERFAILEDVIEW) {
            //重新加载失败
            isChapterRepairing = false;
        }else if(event.getTag() == Constant.EventTag.LISTEN_INIT_SUCCESS){
            try {
                iv_listen_book.setClickable(true);
            }catch (Exception e){
            }
        }else if(event.getTag() == Constant.EventTag.LISTEN_NEXT){
            try{
                String content = event.getObj().toString();
                dealFinish(content);
            }catch (Exception e){
            }
        }else if(event.getTag() == Constant.EventTag.LISTEN_EXIT){
            exitListenBook();
        }

    }


    /**
     * 目录书籍详情点击
     */
    @OnClick(R.id.rl_book_detail)
    void bookDetailClick() {
        if ( null != mBookInfoBean) {
            ActivityCircleDetail.startActivity(this, String.valueOf(mBookInfoBean.getData().getPlatformId()));
        }
    }


    private void registerPhoneListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(phoneStateReceiver, intentFilter);
    }


//    /**
//     * 书架红点清除接口
//     */
//    private void requestClearHotPoint(String bookId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("bookId", bookId);
//        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/book/read/clearHotPoint", map, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
//                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                        //红点清除成功后调用刷新数据库
//                        requestBookStore();
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
//    }

}

