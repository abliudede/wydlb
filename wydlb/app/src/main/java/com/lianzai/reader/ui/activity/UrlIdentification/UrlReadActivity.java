package com.lianzai.reader.ui.activity.UrlIdentification;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.graphics.Bitmap;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.baidu.tts.client.TtsMode;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.just.agentweb.LogUtils;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookInfoBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetBookAllSourceBean;
import com.lianzai.reader.bean.GetBookRewardDetailBean;
import com.lianzai.reader.bean.GetChapterSourceBean;
import com.lianzai.reader.bean.OutsideChapterDetail;
import com.lianzai.reader.bean.ReadSettingsResponse;
import com.lianzai.reader.bean.SendSmsResponse;
import com.lianzai.reader.bean.StartVersionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.model.bean.DownloadTaskBean;
import com.lianzai.reader.model.bean.ReadTimeBean;
import com.lianzai.reader.model.local.BookPayDialogRepository;
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.model.local.LocalRepository;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.model.local.ReadTimeRepository;
import com.lianzai.reader.receiver.PhoneStateReceiver;
import com.lianzai.reader.service.MusicService;
import com.lianzai.reader.service.ParserService;
import com.lianzai.reader.ui.SplashActivity;
import com.lianzai.reader.ui.activity.ActivityAdvertisement;
import com.lianzai.reader.ui.activity.ActivityBookShare2;
import com.lianzai.reader.ui.activity.ActivityBookShare3;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostCircle;
import com.lianzai.reader.ui.activity.listenPay.ActivityListenPay;
import com.lianzai.reader.ui.activity.listenPay.ActivityPayForBook;
import com.lianzai.reader.ui.adapter.CategoryAdapter;
import com.lianzai.reader.ui.contract.NewReadContract;
import com.lianzai.reader.ui.presenter.NewReadPresenter;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.BrightnessUtils;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DecelerateAccelerateInterpolator;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.NetworkUtils;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.ProtectedEyeUtils;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxDataTool;
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
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.StringUtils;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.ItemRewardBarrage;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.ReadAutoPageDialog;
import com.lianzai.reader.view.dialog.ReadListenDialog;
import com.lianzai.reader.view.dialog.ReadSettingDialog;
import com.lianzai.reader.view.dialog.RxDialogCacheChapter;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.page.ChapterUrlsVo;
import com.lianzai.reader.view.page.PageLoader;
import com.lianzai.reader.view.page.PageMode;
import com.lianzai.reader.view.page.PageParagraphVo;
import com.lianzai.reader.view.page.PageView;
import com.lianzai.reader.view.page.TxtChapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;

import static android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * url阅读界面2.5版本，智能目录3.0版本。大于五千万id的为url识别，小于五千万的是智能目录，必须传source
 */
public class UrlReadActivity extends BaseActivity implements NewReadContract.View, SensorEventListener {

    /*********************静态常量定义区域****************************/
    private static final String TAG = "NewReadActivity";//日志标签
    public static final String EXTRA_BOOK_ID = "extra_book_id";
    public static final String EXTRA_CHAPTER_ID = "extra_chapter_id";
    public static final String EXTRA_CHAPTER_URL = "extra_chapter_url";
    public static final String EXTRA_CHAPTER_TITLE = "extra_chapter_title";
    public static final String EXTRA_SOURCE = "extra_source";
    public static final String EXTRA_OPENMULU = "extra_openmulu";
    public static final String EXTRA_PAGE = "extra_page";
    public static final String EXTRA_CHANGESOURCE = "extra_changesource";

    //消息
    private static final int WHAT_CATEGORY = 11;
    private static final int WHAT_CHAPTER = 12;

    //加载状态
    private static final int LOAD_ERROR = -1;
    private static final int LOAD_NORMAL = 0;
    private static final int LOAD_PAUSE = 1;
    /*********************静态常量定义区域****************************/

    // 注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");


    /*********************视图定义区域****************************/
    @Bind(R.id.content_rl)
    RelativeLayout content_rl;

    @Bind(R.id.img_back)
    ImageView img_back;
    //听书按钮
    @Bind(R.id.iv_listen_book)
    ImageView iv_listen_book;

    //共享版权按钮
    @Bind(R.id.icon_gongxiangbanquan)
    ImageView icon_gongxiangbanquan;

    @Bind(R.id.iv_more_option)
    ImageView iv_more_option;

    @Bind(R.id.iv_download_book_black)
    ImageView iv_download_book_black;

    //打赏按钮
    @Bind(R.id.iv_payfor_book_black)
    ImageView iv_payfor_book_black;

    @Bind(R.id.read_dl_slide)
    DrawerLayout mDlSlide;
    /*************top_menu_view*******************/
    @Bind(R.id.read_abl_top_menu)
    LinearLayout mAblTopMenu;
    @Bind(R.id.read_abl_top_menu_rl)
    RelativeLayout read_abl_top_menu_rl;

    /***************content_view******************/
    @Bind(R.id.read_pv_page)
    PageView mPvPage;
    /***************bottom_menu_view***************************/
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

    /***************left slide*******************************/

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

    CategoryAdapter mCategoryAdapter;


    @Bind(R.id.tv_book_cache_status)
    TextView tv_book_cache_status;

    @Bind(R.id.rl_book_cache)//点击缓存
            RelativeLayout rl_book_cache;

    @Bind(R.id.iv_guide_read)//引导图层
            ImageView iv_guide_read;

    @Bind(R.id.iv_chat_room_enter)
    ImageView iv_chat_room_enter;
    //弹幕----------------

    @Bind(R.id.rl_read_loading)
    RelativeLayout rl_read_loading;

    @Bind(R.id.tv_loading_slogan)
    TextView tv_loading_slogan;

    @Bind(R.id.tv_loading_percent)
    TextView tv_loading_percent;

    @Bind(R.id.lottie_loading_view)
    LottieAnimationView lottie_loading_view;

    @Bind(R.id.fl_reload_chapter)
    ImageView fl_reload_chapter;//重新下载本章节

    @Bind(R.id.iv_read_tip_bottom)
    ImageView iv_read_tip_bottom;

    @Bind(R.id.iv_exchange)
    ImageView iv_exchange;

    @Bind(R.id.view_post_red)
    View view_post_red;

    @Bind(R.id.read_tip_iv)
    ImageView read_tip_iv;

    @Bind(R.id.iv_follow)
    ImageView iv_follow;
    boolean ivFollowVisible = false;

    @Bind(R.id.iv_return_bookshelf)
    ImageView iv_return_bookshelf;

    /*********************视图定义区域****************************/


    /*****************view******************/
    private ReadSettingDialog mSettingDialog;
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private CollBookBean mCollBook;
    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;


    /***************params*****************/
//    private boolean isCollected = false; // isFromSDCard
    private boolean isNightMode = false;
    //    private boolean isFullScreen = false;
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

    ReadListenDialog readListenDialog;//听书弹框

    ReadAutoPageDialog readAutoPageDialog;//自动翻页弹框
    //暂存开启自动阅读之前的翻页模式
    private PageMode prePageMode = PageMode.NONE;

    boolean isCategoryFinish = false;//目录加载是否完成

    long listenStartTime = 0;//单次听书的开始时间
    long listenEndTime = 0;//听书定时结束
    int listenMin = 0;//默认不限时
    boolean canListen = false;


    RxDialogGoLogin rxDialogGoLogin;//去登录

    @Inject
    NewReadPresenter mPresenter;
    private BookInfoBean mBookInfoBean;


    @Bind(R.id.rl_book_detail)
    RelativeLayout rl_book_detail;


    //阅读时长需要传的数据
    int clickCount = 0;
    int turnCount = 0;
    String deviceNo;
    String phoneModel;

    String angle = "";//倾斜角度

    String userId;

    Long startEnterPageTime = 0L;//进入页面的时间

//    List<ReadTimeBean> readTimeBeanList = new ArrayList<>();//阅读时长集合


    //章节ID-通过传过来的章节ID打开指定章节
    String chapterId;//
    String chapterUrl;
    String chapterTitleEnter;
    int page;
    boolean openMulu = false;

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


    private String loadingSlogan = "爱连载,不孤单";

    List<BookChapterBean> categoryChaptersList = new ArrayList<>();//目录列表

    RxDialogSureCancelNew rxDialogSureCancelTip;//提示

    private boolean hasNotch = false;

    PhoneStateReceiver phoneStateReceiver;
    private boolean isShow = true;

    //游客阅读章数
    private int visitorReadCount;
    private MyHandler myHandler;
    //是否能展示加入书架按钮
    private String bqtKey;
    private TtsMode ttsMode;

    private BannerBean.DataBean bannerData;

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
            } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(UrlReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(UrlReadActivity.this, BrightnessUtils.getScreenBrightness(UrlReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(UrlReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setDefaultBrightness(UrlReadActivity.this);
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

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

        angle = String.valueOf(xAngle + yAngle + zAngle);
//        RxLogTool.e("xAngle:" + xAngle + "---zAngle:" + zAngle + "--yAngle:" + yAngle);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private long skipTime;
    private String bidStr = "";
    private boolean changeSource = false;
    private List<GetBookRewardDetailBean.DataBean> barrageList;


    public static void bookHasRead(String bookId){
        try{
            //判断是否登录，并取到uid
            AccountTokenBean account = RxLoginTool.getLoginAccountToken();
            if (null != account) {
                int userId = account.getData().getUid();
                //在这里更新小说的阅读时间
                BookStoreRepository.getInstance().updateBooks(userId, Integer.parseInt(bookId));
                //红点清除，红点清除之后还会更新一次时间。
                UrlReadActivity.requestClearHotPoint(bookId);
            }
        }catch (Exception e){
        }
    }


    //url识别阅读和智能目录共用入口,可以用来打开目录
    public static void startActivityInsideRead(Context context, String bookId, String source, boolean openMulu
    , String chapterId, String chapterTitle, int page , boolean changeSource) {
            RxActivityTool.removeReadActivity();
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_BOOK_ID, bookId);
            bundle.putString(EXTRA_SOURCE, source);
            bundle.putBoolean(EXTRA_OPENMULU, openMulu);
            bundle.putString(EXTRA_CHAPTER_ID, chapterId);
            bundle.putString(EXTRA_CHAPTER_TITLE, chapterTitle);
            bundle.putInt(EXTRA_PAGE, page);
            bundle.putBoolean(EXTRA_CHANGESOURCE, changeSource);
            RxActivityTool.skipActivity(context, UrlReadActivity.class, bundle);

            bookHasRead(bookId);
    }


    //url识别阅读和智能目录共用入口,可以用来打开目录
    public static void startActivityOutsideRead(Context context, String bookId, String source, boolean openMulu
            ,String chapterId, String chapterTitle,int page , boolean changeSource) {
        getBookAllSourceByNovelId(context,bookId,source,openMulu,chapterId,chapterTitle,page,changeSource);
    }

    public static void getBookAllSourceByNovelId(Context context, String bookId, String source, boolean openMulu
            ,String chapterId, String chapterTitle,int page , boolean changeSource) {
        Map<String, String> map=new HashMap<>();
        map.put("novelId",bookId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/getBookAllSourceByNovelId", map,new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetBookAllSourceBean getBookAllSourceBean = GsonUtil.getBean(response, GetBookAllSourceBean.class);
                    if (getBookAllSourceBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if(getBookAllSourceBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_PUBLIC_NUMBER){
                            //内站书，直接跳阅读界面。
                            startActivityInsideRead(context,bookId,source,openMulu,chapterId,chapterTitle,page,changeSource);
                        }else {
                            String skipUrl = getBookAllSourceBean.getData().getSearchUrl();
                            String bookSource = source;
                            for(GetBookAllSourceBean.DataBean.ChapterSourceResponsesBean item :getBookAllSourceBean.getData().getChapterSourceResponses()){
                                if("master".equals(item.getGender())){
                                    skipUrl = item.getFirst_chapter_url();
                                    bookSource = item.getSource();
                                }
                            }
                            ActivityReadDialogActivity.startActivity(context,bookId,bookSource,getBookAllSourceBean.getData().isIsStartBrowser(),getBookAllSourceBean.getData().isIsStartTool(),skipUrl,getBookAllSourceBean.getData().getToolUrl());
                        }
                    }else {
                        RxToast.custom(getBookAllSourceBean.getMsg()).show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }


    /******************************Override生命周期区域*******************************************/

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            RxActivityTool.finishAllActivity();
            MainActivity.startActivity(UrlReadActivity.this, "", "");
            finish();
        }

        mPresenter.attachView(this);

        isNightMode = ReadSettingManager.getInstance().isNightMode();
//        isFullScreen = ReadSettingManager.getInstance().isFullScreen();

        try {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        } catch (Exception e) {
        }
        try {
            chapterId = getIntent().getStringExtra(EXTRA_CHAPTER_ID);
        } catch (Exception e) {
        }
        try {
            chapterUrl = getIntent().getStringExtra(EXTRA_CHAPTER_URL);
        } catch (Exception e) {
        }

        try {
            chapterTitleEnter = getIntent().getStringExtra(EXTRA_CHAPTER_TITLE);
            page = getIntent().getIntExtra(EXTRA_PAGE, 0);
        } catch (Exception e) {
        }

        try {
            openMulu = getIntent().getBooleanExtra(EXTRA_OPENMULU,false);
        } catch (Exception e) {
        }

        try {
            changeSource = getIntent().getBooleanExtra(EXTRA_CHANGESOURCE, false);
        } catch (Exception e) {
        }


        String source = "";
        try {
            source = getIntent().getStringExtra(EXTRA_SOURCE);
        } catch (Exception e) {
        }

        if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_POST_KEY, true)) {
            view_post_red.setVisibility(View.VISIBLE);
        } else {
            view_post_red.setVisibility(View.GONE);
        }

        //获取到个人信息后直接初始化信息，并更新书籍阅读时间。
        initReadTimeData();

//        tv_change_source.setVisibility(View.GONE);
        visitorReadCount = 0;
        //听书和自动翻页相关
        Constant.isListenBook = false;//是否是听书模式=默认不是
        Constant.isAutoPage = false;//是否是自动翻页模式=默认不是
        //菜单动画初始化
        initMenuAnim();
        //隐藏StatusBar半透明化StatusBar
        if (SystemBarUtils.hasNotch(UrlReadActivity.this)) {
            hasNotch = true;
        }

        //状态栏字体设置成黑色
        SystemBarUtils.setLightStatusBar(this, true);


        if (TextUtils.isEmpty(mBookId)) {
            //书籍ID有误
            RxToast.custom("书本ID有误", Constant.ToastType.TOAST_ERROR).show();
            finish();
        }

        //初始化书本对象
        mCollBook = new CollBookBean();
        //区分是哪种书，大于五千万则显示去原网页的图标，小于则显示去圈子的图标
        if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
            source = "";
            mCollBook.set_id(mBookId);

            iv_exchange.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams pramas = ((LinearLayout.LayoutParams) read_tip_iv.getLayoutParams());
            pramas.setMargins(0, 0, RxImageTool.dip2px(64), 0);
            read_tip_iv.setLayoutParams(pramas);
        } else {

            if (!TextUtils.isEmpty(source)) {
                //假如有传入source，直接覆盖本地默认源
                RxSharedPreferencesUtil.getInstance().putString(mBookId + "_source", source);
            }
            bidStr = SkipReadUtil.getSource(mBookId);
            //书结构加源的概念
            mCollBook.set_id(mBookId + bidStr);

            iv_exchange.setVisibility(View.GONE);
            LinearLayout.LayoutParams pramas = ((LinearLayout.LayoutParams) read_tip_iv.getLayoutParams());
            pramas.setMargins(0, 0, RxImageTool.dip2px(23), 0);
            read_tip_iv.setLayoutParams(pramas);
        }


        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        //获取页面加载器
        mPageLoader = mPvPage.getPageLoader(mBookId);
        //增加这一句
        if (TextUtils.isEmpty(bidStr)) {
            mPageLoader.setBidStr("");
        } else {
            mPageLoader.setBidStr(bidStr);
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

        //来电监听
        phoneStateReceiver = new PhoneStateReceiver();
        //获取slogan
        getLoadingSlogan();
        //传感器服务
        mySM = (SensorManager) getSystemService(SENSOR_SERVICE);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        mPageLoader.updateBattery(battery);

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

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
        //刚进去默认隐藏菜单栏
        toggleMenu();
        //注册监听
        registerPhoneListener();
        //注册听书服务
        registerService();


        //获取书详情信息
        getBookInfo();
        //获取书籍打赏状态
        getBookRewardStatus();
        //获取书籍弹幕信息
        getBookRewardDetail();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isOnStop = false;
        registerBrightObserver();
    }


    @Override
    public void initDatas() {
        isOnStop = false;
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (ReadSettingManager.getInstance().isScreenKeepOn()) {//只有选择常量的时候才唤醒屏幕
            mWakeLock.release();
        }

        if (ReadSettingManager.getInstance().isOpenProtectedEye()) {
            ProtectedEyeUtils.closeProtectedEyeMode(this);
            RxLogTool.e("NewReadActivity onPause openProtectedEyeMode true--closeProtectedEyeMode");
        } else {
            RxLogTool.e("NewReadActivity onPause openProtectedEyeMode false");
        }
    }
    boolean isOnStop = false;
    @Override
    protected void onStop() {
        super.onStop();
        isOnStop = true;
        unregisterBrightObserver();
        mySM.unregisterListener(this);

        if (!RxAppTool.isAppForeground(this) && Constant.isAutoPage) {//当前是自动翻页
//            RxToast.custom("退出自动翻页").show();
            //退出自动翻页
            mPageLoader.setPageMode(prePageMode);
            Constant.isAutoPage = false;
        }
    }

    @Override
    protected void onDestroy() {

        //清除电池时间相关广播
        unregisterReceiver(mReceiver);
        RxEventBusTool.unRegisterEventBus(this);
        unregisterReceiver(phoneStateReceiver);

        //清空队列
        if (null != myHandler) {
            myHandler.removeCallbacksAndMessages(null);
        }
//        if (null != barrageTimer) {
//            barrageTimer.cancel();
//        }

        //假如没释放的话，释放听书资源
        //释放听书资源
        if (null != myConn) {
            unbindService(myConn);
            myConn = null;
        }

        if (Constant.isAutoPage) {
            Constant.isAutoPage = false;
        }

        mPageLoader.closeBook();
        mPageLoader = null;

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

    /******************************Override生命周期区域*******************************************/


    /***********************************onclick点击事件以及页面触发方法区域**********************************/


    /***********************************onclick点击事件以及页面触发方法区域**********************************/
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
        try {
            AccountTokenBean account = RxLoginTool.getLoginAccountToken();
            if (null != account) {
                userId = String.valueOf(account.getData().getUid());
                bqtKey = userId + Constant.BQT_KEY;
                phoneModel = RxDeviceTool.getBuildBrandModel();
                deviceNo = RxDeviceTool.getDeviceIdIMEI(this);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取书籍弹幕信息
     */
    private void getBookRewardDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", mBookId);
        map.put("size", "20");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/bookRewardConfig/getBookRewardDetail", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                RxLogTool.e(e.toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetBookRewardDetailBean getBookRewardDetailBean = GsonUtil.getBean(response, GetBookRewardDetailBean.class);
                    if (getBookRewardDetailBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        barrageList = getBookRewardDetailBean.getData();
                    }
                } catch (Exception e) {
                    RxLogTool.e("getBookInfo e:" + e.getMessage());
                }
            }
        });
    }

    int barrageIndex = 0;
    //开关函数
    private void showRewardBarrage() {
        content_rl.removeAllViews();
        if(null != barrageList && !barrageList.isEmpty()){
            content_rl.setVisibility(View.VISIBLE);
            barrageIndex = 0;
            checkRewardBarrage();
        }
    }
    //开关函数
    private void closeRewardBarrage() {
        content_rl.removeAllViews();
        content_rl.setVisibility(View.GONE);
        myHandler.removeMessages(7);
    }

    private void checkRewardBarrage(){
        if(!isOnStop) {
            if (barrageIndex >= barrageList.size()) {
                barrageIndex = 0;
            }
            showBarrage(barrageList.get(barrageIndex));
            barrageIndex++;
        }
        myHandler.sendEmptyMessageDelayed(7, 1000);
    }

    /**
     * 展示弹幕的内容，textView设置内容 （可以在这里自定义view）
     *
     * @param tb 需要展示的内容Bean
     */
    int recentBarrageMargin = 0;
    private void showBarrage(GetBookRewardDetailBean.DataBean tb) {
        ItemRewardBarrage item = new ItemRewardBarrage(UrlReadActivity.this);
        item.bindData(tb);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        int temp = (new Random().nextInt(5)+2);
        while (temp == recentBarrageMargin){
            temp = (new Random().nextInt(5)+2);
        }
        recentBarrageMargin = temp;
        params.topMargin =recentBarrageMargin  * RxImageTool.dip2px(40);
        item.setLayoutParams(params);

        int length = tb.getNickName().length() + String.valueOf(tb.getAmt()).length() + 5;
        int dp = 12*length + 35;

        TranslateAnimation tlAnim = new TranslateAnimation(RxDeviceTool.getScreenWidth(UrlReadActivity.this), -RxImageTool.dip2px(dp), 0, 0);
        long duration = 5000;
        tlAnim.setDuration(duration);
        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
//        tlAnim.setFillAfter(true);

        tlAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                content_rl.removeView(item);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        item.startAnimation(tlAnim);
        content_rl.addView(item);
    }


    /**
     * 获取书籍打赏状态
     */
    private void getBookRewardStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", mBookId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/bookRewardConfig/getBookRewardStatus", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                RxLogTool.e("KKKK getBookRewardStatus" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("KKKK getBookRewardStatus" + response);
                    SendSmsResponse sendSmsResponse = GsonUtil.getBean(response, SendSmsResponse.class);
                    if (sendSmsResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (sendSmsResponse.isData()) {
                            iv_payfor_book_black.setVisibility(View.VISIBLE);
                            //开始弹窗计数功能，判断是否展示弹窗
                            boolean needShowPayDialog = BookPayDialogRepository.getInstance().showDialog(mBookId);
                            if (needShowPayDialog) {
                                //展示弹窗
                                showBookPayDialog();
                            }
                        }
                    }
                } catch (Exception e) {
                    RxLogTool.e("KKKK" + e.toString());
                }
            }
        });
    }

    /**
     * 获取书籍详情
     */
    private void getBookInfo() {

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/read/info/" + mBookId, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                RxLogTool.e("KKKK /book/read/info/" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("KKKK /book/read/info/" + response);
                    BookInfoBean bookInfoBean = GsonUtil.getBean(response, BookInfoBean.class);
                    if (bookInfoBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {

                        String showDownload = RxSharedPreferencesUtil.getInstance().getString(Constant.READ_SHOW_DOWNLOAD, "0");

                        if (bookInfoBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_PUBLIC_NUMBER) {
                            //是内站书，都展示。
                            iv_download_book_black.setVisibility(View.VISIBLE);
                            rl_book_cache.setVisibility(View.VISIBLE);
                        } else if ("0".equals(showDownload)) {
                            //不是内站书且接口关闭情况下就是隐藏
                            iv_download_book_black.setVisibility(View.GONE);
                            rl_book_cache.setVisibility(View.GONE);
                        } else {
                            //不是内站书且接口打开情况下就是都展示。
                            iv_download_book_black.setVisibility(View.VISIBLE);
                            rl_book_cache.setVisibility(View.VISIBLE);
                        }

                        if(Integer.parseInt(mBookId) <= Constant.bookIdLine && bookInfoBean.getData().getPlatformType() == Constant.UserIdentity.BOOK_OUTSIED_NUMBER) {
                           //只有小于50000000的外站书才有修复按钮
                            fl_reload_chapter.setVisibility(View.VISIBLE);
                        }


                        mBookInfoBean = bookInfoBean;
                        if (null != mCollBook.getBookChapters() && mCollBook.getBookChapters().size() > 0) {
                            //书籍已请求下来
                            mCollBook.setAuthor(mBookInfoBean.getData().getPenName());
                            mCollBook.setCover(mBookInfoBean.getData().getPlatformCover());
                            mCollBook.setTitle(mBookInfoBean.getData().getPlatformName());
                            BookRepository.getInstance().saveCollBook(mCollBook);
                        }
                        initBookData(mCollBook);
//                        requestChatRoomIsOpen(String.valueOf(mBookInfoBean.getData().getPlatformId()));
                        showConcern();

                        //共享版权图标是否显示
                        if (mBookInfoBean.getData().isCopyright()) {
                            icon_gongxiangbanquan.setVisibility(View.VISIBLE);
                        } else {
                            icon_gongxiangbanquan.setVisibility(View.GONE);
                        }

                    } else {
                        RxToast.custom(bookInfoBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("KKKK" + e.toString());
                }
            }
        });
    }

    private void showConcern() {
        if (RxLoginTool.isLogin()) {
            String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
            //设为偏好专用字段
            String localSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source2");
            //复杂的按钮展示逻辑
            if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
                if (!mBookInfoBean.getData().isIsConcern()) {
                    //没关注单纯的加入书架
                    ivFollowVisible = true;
                    if (isShow) {
                        iv_follow.setVisibility(View.VISIBLE);
                    }
                    iv_follow.setImageResource(R.mipmap.icon_read_follow);
                } else {
                    //内站书不显示
                    ivFollowVisible = false;
                    iv_follow.setVisibility(View.GONE);
                }
            } else if ("lianzai.com".equals(nowSource)) {
                if (!mBookInfoBean.getData().isIsConcern()) {
                    //没关注单纯的加入书架
                    ivFollowVisible = true;
                    if (isShow) {
                        iv_follow.setVisibility(View.VISIBLE);
                    }
                    iv_follow.setImageResource(R.mipmap.icon_read_follow);
                } else {
                    //内站书不显示
                    ivFollowVisible = false;
                    iv_follow.setVisibility(View.GONE);
                }
            } else {
                if (mBookInfoBean.getData().isIsConcern() && TextUtils.isEmpty(nowSource)) {
                    //关注且没有传入的nowSource时，单纯的移出书架
                    ivFollowVisible = false;
                    iv_follow.setVisibility(View.GONE);
                } else if (mBookInfoBean.getData().isIsConcern() && !TextUtils.isEmpty(nowSource) && !TextUtils.isEmpty(localSource) && nowSource.equals(localSource)) {
                    //关注且有传入nowSource且和本地相同时，单纯的移出书架
                    ivFollowVisible = false;
                    iv_follow.setVisibility(View.GONE);
                } else if (mBookInfoBean.getData().isIsConcern() && !TextUtils.isEmpty(nowSource)) {
                    //关注且有传入nowSource，不符合上面条件的，单纯的设为偏好
                    ivFollowVisible = true;
                    if (isShow) {
                        iv_follow.setVisibility(View.VISIBLE);
                    }
                    iv_follow.setImageResource(R.mipmap.icon_read_setlike);
                } else if (!mBookInfoBean.getData().isIsConcern() && !TextUtils.isEmpty(nowSource)) {
                    //没关注且有传入nowSource，那么加入书架并且设为偏好
                    ivFollowVisible = true;
                    if (isShow) {
                        iv_follow.setVisibility(View.VISIBLE);
                    }
                    iv_follow.setImageResource(R.mipmap.icon_read_follow);
                } else {
                    //没关注且没有传入nowSource，单纯的加入书架
                    ivFollowVisible = true;
                    if (isShow) {
                        iv_follow.setVisibility(View.VISIBLE);
                    }
                    iv_follow.setImageResource(R.mipmap.icon_read_follow);
                }

            }
        } else {
            ivFollowVisible = false;
            iv_follow.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.iv_return_bookshelf)
    void iv_return_bookshelfClick() {
        RxEventBusTool.sendEvents(Constant.EventTag.SWITCH_BOOK_SHELF);
        RxActivityTool.returnMainActivity();
    }

    @OnClick(R.id.iv_follow)
    void iv_followClick() {
        if (RxLoginTool.isLogin()) {
            String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
            String localSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source2");
            //复杂的按钮展示逻辑
            if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
                if (!mBookInfoBean.getData().isIsConcern()) {
                    //没关注单纯的加入书架
                    addAttentionClick();
                }
            } else if ("lianzai.com".equals(nowSource)) {
                if (!mBookInfoBean.getData().isIsConcern()) {
                    //没关注单纯的加入书架
                    requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()), "", false);
                }
            } else {
                if (mBookInfoBean.getData().isIsConcern() && TextUtils.isEmpty(nowSource)) {
                    //关注且没有传入的nowSource时，单纯的移出书架

                } else if (mBookInfoBean.getData().isIsConcern() && !TextUtils.isEmpty(nowSource) && !TextUtils.isEmpty(localSource) && nowSource.equals(localSource)) {
                    //关注且有传入nowSource且和本地相同时，单纯的移出书架

                } else if (mBookInfoBean.getData().isIsConcern() && !TextUtils.isEmpty(nowSource)) {
                    //关注且有传入nowSource，不符合上面条件的，单纯的设为偏好
                    requestPreference(String.valueOf(mBookInfoBean.getData().getPlatformId()), nowSource, true);
                } else if (!mBookInfoBean.getData().isIsConcern() && !TextUtils.isEmpty(nowSource)) {
                    //没关注且有传入nowSource，那么加入书架并且设为偏好
                    requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()), nowSource, false);
                } else {
                    //没关注且没有传入source，单纯的加入书架
                    requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()), "", false);
                }
            }
        }
    }

    /**
     * 数据请求处理
     */
    private void processRequest() {
        ArrayMap<String, Object> map = new ArrayMap();

        //大于50000000的书籍，没有源的概念
        if (Integer.parseInt(mBookId) <= Constant.bookIdLine) {
            //获取本地存储的source
            String localSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
            if (!TextUtils.isEmpty(localSource)) {
                map.put("source", localSource);
            }
        }

        mPresenter.urlLoadCategory(mBookId, map, chapterId, chapterUrl, chapterTitleEnter, bidStr);//,chapterUrl
    }


    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            read_abl_top_menu_rl.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
        }
    }

    private void initBottomMenu() {
        //判断是否全屏
//        if (isFullScreen) {
//            //还需要设置mBottomMenu的底部高度
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
//            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
//            mLlBottomMenu.setLayoutParams(params);
//        } else {
        //设置mBottomMenu的底部距离
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLlBottomMenu.getLayoutParams();
        params.bottomMargin = 0;
        mLlBottomMenu.setLayoutParams(params);
//        }
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

//            mAblTopMenu.setBackgroundColor(bgColor);
            read_abl_top_menu_rl.setBackgroundColor(bgColor);
            mLlBottomMenu.setBackgroundColor(bgColor);
            ll_category.setBackgroundColor(bgColor);
            rl_book_detail.setBackgroundColor(bgColor);
            rl_book_cache.setBackgroundColor(bgColor);

            view_category_line.setBackgroundColor(textColor);

            img_back.setImageResource(R.mipmap.icon_read_back_white);
            iv_payfor_book_black.setImageResource(R.mipmap.pay_for_book_white);
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

            tv_book_author.setTextColor(textColor);
            tv_book_title.setTextColor(textColor);
            view_category_line.setBackgroundColor(Color.parseColor("#1AF9F9F9"));

            tv_book_total_chapter.setTextColor(textColor);

            iv_sort_category.setImageResource(R.mipmap.icon_category_desc);

            tv_loading_slogan.setTextColor(getResources().getColor(R.color.color_black_ff666666));
            tv_loading_percent.setTextColor(getResources().getColor(R.color.color_white_4alpha));
            iv_read_tip_bottom.setImageResource(R.mipmap.bg_read_tip_bottom_night);

            //修复章节
            fl_reload_chapter.setImageResource(R.mipmap.icon_read_reload_chapter_white);

            iv_chat_room_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.chat_room_enter_animation_night));
        } else {//日间模式
            textColor = getResources().getColor(R.color.color_black_ff666666);
            bgColor = getResources().getColor(R.color.white);

            //导航栏颜色
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(bgColor);
            }

            mTvNightMode.setText(StringUtils.getString(R.string.nb_mode_night));

//            mAblTopMenu.setBackgroundColor(bgColor);
            read_abl_top_menu_rl.setBackgroundColor(bgColor);
            mLlBottomMenu.setBackgroundColor(bgColor);
            ll_category.setBackgroundColor(bgColor);
            rl_book_detail.setBackgroundColor(getResources().getColor(R.color.colorf5f5f5));
            rl_book_cache.setBackgroundColor(bgColor);
            view_category_line.setBackgroundColor(Color.parseColor("#FFF9F9F9"));

            mTvCategory.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_category), null, null);
            mTvNightMode.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_night), null, null);
            mTvSetting.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.ic_read_menu_setting), null, null);
            read_tv_feedback.setCompoundDrawables(null, RxImageTool.getDrawable(23, R.drawable.post_black), null, null);

            img_back.setImageResource(R.mipmap.icon_read_back_black);
            iv_payfor_book_black.setImageResource(R.mipmap.pay_for_book_black);
            iv_download_book_black.setImageResource(R.mipmap.icon_read_download);
            iv_listen_book.setImageResource(R.mipmap.icon_read_listen_black);
            iv_more_option.setImageResource(R.mipmap.icon_read_more_black);

            tv_book_author.setTextColor(getResources().getColor(R.color.five_text_color));
            tv_book_title.setTextColor(getResources().getColor(R.color.color_black_ff333333));

            tv_book_total_chapter.setTextColor(getResources().getColor(R.color.color_black_ff999999));

            iv_sort_category.setImageResource(R.mipmap.icon_category_desc);

            tv_loading_slogan.setTextColor(getResources().getColor(R.color.color_black_ff333333));
            tv_loading_percent.setTextColor(getResources().getColor(R.color.color_black_4alpha));
            iv_read_tip_bottom.setImageResource(R.mipmap.bg_read_tip_bottom);

            //修复章节
            fl_reload_chapter.setImageResource(R.mipmap.icon_read_reload_chapter_black);


            iv_chat_room_enter.setBackground(ContextCompat.getDrawable(this, R.drawable.chat_room_enter_animation_night));
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

    int lastPagePosition = 0;//章节最后一页的页码

    boolean isStartOperation = false;//是否开始翻页

    boolean isOperationChapter = false;//是否是直接跳章

    /**
     * 点击事件初始化
     */
    protected void initClick() {
        iv_more_option.setOnClickListener(
                v -> {
                    if (null != mBookInfoBean) {
                        if (null != mPageLoader && null != mPageLoader.getCurrentTxtChapter()) {
                            if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
                                //进url识别的详情页
                                String platformCover = mBookInfoBean.getData().getPlatformCover();
                                String platformName = mBookInfoBean.getData().getPlatformName();
                                String penName = mBookInfoBean.getData().getPenName();
                                String platformIntroduce = mBookInfoBean.getData().getPlatformIntroduce();
                                boolean concern = mBookInfoBean.getData().isIsConcern();
                                String platformId = String.valueOf(mBookInfoBean.getData().getPlatformId());
                                String webUrl = mPageLoader.getCurrentTxtChapter().getLink();
                                String chapterId = mPageLoader.getCurrentTxtChapter().getChapterId();
                                String chapterTitle = mPageLoader.getCurrentTxtChapter().getTitle();
                                ActivityBookShare3.startActivity(UrlReadActivity.this, platformCover, platformName, penName, platformIntroduce, webUrl, mBookId, concern, chapterId, chapterTitle, platformId);
                            } else {
                                //进智能目录的详情页
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
                                ActivityBookShare2.startActivity(UrlReadActivity.this, platformCover, platformName, penName, platformIntroduce, platformId, webUrl, mBookId, concern, chapterId, chapterTitle,platformType,isSigned,signUrl+signUrlPrams);
                            }
                        }
                    } else {
                        getBookInfo();
                    }
                }
        );
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

                        mPresenter.loadCurrentChapter(mBookId + bidStr, txtChapter);

                        if (null == myHandler) {
                            myHandler = new MyHandler(UrlReadActivity.this);
                        }
                        myHandler.sendEmptyMessage(WHAT_CATEGORY);
                    }

                    @Override
                    public void onChapterChange(int pos) {
                        //每章刷新一下广告
                        getAdRequest(pos);

                        mCategoryAdapter.setChapter(pos);
//                        //平时都隐藏换源提示
//                        showChangeSourceIv = false;
                        //已读信息
                        StringBuilder sb = new StringBuilder();
                        if (null != mBookInfoBean && mBookInfoBean.getData().getTotalMinute() > 0) {
                            sb.append("阅读");
                            sb.append(mBookInfoBean.getData().getTotalMinute());
                            sb.append("分钟 ");
                        }
                        double precent = (double) (pos + 1) / (double) mCollBook.getBookChapterList().size();
                        sb.append("已读");
                        sb.append(RxDataTool.getPercentValue(precent, 2));
                        sb.append("%");
                        tv_book_author.setText(sb.toString());

                        if (null != mSbChapterProgress) {
                            mSbChapterProgress.post(
                                    () -> {
                                        if (null != mSbChapterProgress) {
                                            mSbChapterProgress.setProgress(pos);
                                        }
                                    }
                            );
                        }

                        isOperationChapter = false;
                        isStartOperation = true;
                    }

                    @Override
                    public void preLoadingChapters(List<TxtChapter> requestChapters) {
                        RxLogTool.e("preLoadingChapters size...." + requestChapters.size());
                        mPresenter.preLoadingChapter(mBookId + bidStr, requestChapters);//预加载章节
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
                        try {
                            //每翻一页，保存一下
                            mPageLoader.saveRecord();
                        } catch (Exception e) {
                        }

                        myPageChange(pos);
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
                            iv_chat_room_enter.setVisibility(View.GONE);
//                            isShowBarrageView(false);//拖动的时候隐藏弹幕
                        }
                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
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
                } else if (mPageLoader.getPageStatus() == 3) {//加载错误的情况
                    //请求接口并换源
//                    RxToast.custom("重试点击").show();
                    getChapterSourceRequest();
                } else {
                    toggleMenu();
                }
            }

            @Override
            public void adClick() {
//                if (Constant.isListenBook) {//听书模式
//                    showReadListenDialog();
//                } else if (Constant.isAutoPage) {//自动翻页模式
//                    showAutoPageDialog();
//                } else if (mPageLoader.isAdShow ) {//广告的情况
//                    //广告点击
//                    RxToast.custom("广告点击").show();
//                } else {
//                    toggleMenu();
//                }
                //广告点击
                RxAppTool.adSkip(bannerData, UrlReadActivity.this);
//                RxToast.custom("广告点击").show();
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
                    //优先用书评传进来的source
                    String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
                    ActivityWebView.startActivityForReadNormal(UrlReadActivity.this, webUrl, mBookId, nowSource, false);
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
                                myHandler.removeMessages(2);
                                myHandler.sendEmptyMessage(2);
                            }
                    );
                }
        );

        mTvPreChapter.setOnClickListener(
                (v) -> {
                    if (!isCategoryFinish) return;//未加载完成不可点击
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
                    if (!isCategoryFinish) return;//未加载完成不可点击
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




    public void getChapterSourceRequest() {
        if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
            RxToast.custom("暂无更多资源").show();
            return;
        }
        if (null == mPageLoader) {
            RxToast.custom("信息异常，请重新进入阅读界面").show();
            return;
        } else if (null == mPageLoader.getCurrentTxtChapter()) {
            RxToast.custom("信息异常，请重新进入阅读界面").show();
            return;
        } else {
            RxToast.custom("正在修复中～").show();
        }
        String chapterTitle = mPageLoader.getCurrentTxtChapter().getTitle();
        doGetChapterSourceRequest(chapterTitle);
    }

    private void doGetChapterSourceRequest(String chapterTitle) {
        Map<String, String> map = new HashMap<>();
        map.put("novelId", mBookId);
        map.put("chapterTitle", chapterTitle);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/getChapterSource", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ex) {
//                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    GetChapterSourceBean circleDynamicBean = GsonUtil.getBean(response, GetChapterSourceBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null != circleDynamicBean.getData() && !circleDynamicBean.getData().isEmpty()) {
                            //先排除掉当前源，并记录当前源位置
                            List<GetChapterSourceBean.DataBean> listTemp = circleDynamicBean.getData();
                            int nowIndex = 0;
                            String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
                            for (GetChapterSourceBean.DataBean item : listTemp) {
                                if (item.getSource().equals(nowSource)) {
                                    listTemp.remove(item);
                                    break;
                                }
                                nowIndex++;
                            }
                            int size = listTemp.size();
                            if (size < 1) {
                                RxToast.custom("暂无更多资源").show();
                            } else if (size >= 1) {
//                                RxToast.custom("修复成功").show();
                                int num = 0;
                                if (Constant.changesource != 0 || nowIndex == 0) {
                                    //假如不是第一次，或者当前是第一个源，则进入下一个源
                                    num = nowIndex;
                                    if (num >= size) {
                                        num = 0;
                                    }
                                }

                                //释放听书资源
                                if (null != myConn) {
                                    unbindService(myConn);
                                    myConn = null;
                                }

                                openNewRead(listTemp, num, chapterTitle);

                            }
                        } else {
                            RxToast.custom("暂无更多资源").show();
                        }
                    } else {
                        RxToast.custom("暂无更多资源").show();
                    }
                } catch (Exception e) {
                    RxToast.custom("暂无更多资源").show();
//                    e.printStackTrace();
                }
            }
        });
    }

    private void openNewRead(List<GetChapterSourceBean.DataBean> listTemp, int num, String chapterTitle) {
        Constant.changesource = Constant.changesource + 1;
        if (Constant.changesource > 5) {
            //展示是否查找更多的弹窗
            showchangeSourceDialog();
        } else {
            //异步地去开启新页面，让service先释放掉
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    RxSharedPreferencesUtil.getInstance().putString(mBookId + "_source", listTemp.get(num).getSource());
                    UrlReadActivity.startActivityInsideRead(UrlReadActivity.this, mBookId, listTemp.get(num).getSource(),false, "", chapterTitle, 0, true);
                }
            }, 300);
        }
    }

    private void showBookPayDialog() {
        if (null == rxDialogSureCancelTip) {
            rxDialogSureCancelTip = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelTip.setButtonText("打赏一下", "算了吧");
        rxDialogSureCancelTip.getCancelView().setVisibility(View.VISIBLE);
        rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
        rxDialogSureCancelTip.setTitle("打赏提示");
        rxDialogSureCancelTip.setContent("好像您最近非常喜欢看这本书呢，打赏作者几块零花钱以资鼓励吧！");
        rxDialogSureCancelTip.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                //跳到打赏
                if (!RxLoginTool.isLogin()) {//登录提示
                    showLoginDialog();
                }else {
                    ActivityPayForBook.startActivity(UrlReadActivity.this, mBookId);
                }
                rxDialogSureCancelTip.dismiss();
            }
        });
        rxDialogSureCancelTip.show();
    }

    private void showchangeSourceDialog() {
        if (null == rxDialogSureCancelTip) {
            rxDialogSureCancelTip = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelTip.setButtonText("查找更多", "取消");
        rxDialogSureCancelTip.getCancelView().setVisibility(View.VISIBLE);
        rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
        rxDialogSureCancelTip.setTitle("修复失败");
        rxDialogSureCancelTip.setContent("修复不成功？点击查找更多，找寻更优质的链接。");
        rxDialogSureCancelTip.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                //跳到网页
                ActivitySearchFirst.skiptoSearch(mBookInfoBean.getData().getPlatformName(), UrlReadActivity.this);
            }
        });
        rxDialogSureCancelTip.show();
    }


    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {

        if (mAblTopMenu.getVisibility() == VISIBLE) {
            toggleMenu();
            return true;
        } else if (mSettingDialog.isShowing()) {
            hideSystemBar();
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    private void showSystemBar() {
        //显示,要先清除视图标记
        if (!hasNotch) {
            SystemBarUtils.readStatusBar3(this);
        }

//        if (isFullScreen) {
//            SystemBarUtils.showUnStableNavBar(this);
//        }
    }

    private void hideSystemBar() {
        //隐藏
        if (!hasNotch) {
            SystemBarUtils.readStatusBar1(this);
        } else {
            SystemBarUtils.readStatusBar2(this);
        }

//        if (isFullScreen) {
//            SystemBarUtils.hideUnStableNavBar(this);
//        }
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
     */
    private void initBookData(CollBookBean collBookBean) {
        if (!TextUtils.isEmpty(collBookBean.getTitle())) {
            tv_book_title.setText(collBookBean.getTitle());
        }
        //已读信息
        StringBuilder sb = new StringBuilder();
        if (null != mBookInfoBean && mBookInfoBean.getData().getTotalMinute() > 0) {
            sb.append("阅读");
            sb.append(mBookInfoBean.getData().getTotalMinute());
            sb.append("分钟 ");
        }
        try {
            sb.append("已读");
            double precent = (double) (mPageLoader.getChapterPos() + 1) / (double) mCollBook.getBookChapterList().size();
            sb.append(RxDataTool.getPercentValue(precent, 2));
            sb.append("%");
        } catch (Exception e) {

        }
        tv_book_author.setText(sb.toString());
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
     * 根据章节标题获取当前章节的位置
     *
     * @return
     */
    private void getChapterPositionByChapterTitle(List<BookChapterBean> bookChapterBeans) {
//        RxLogTool.e("getChapterPositionBychapterTitle:" + chapterTitleEnter);
        boolean equal = false;
        for (int i = 0; i < bookChapterBeans.size(); i++) {
            BookChapterBean txtChapter = bookChapterBeans.get(i);
            if (txtChapter.getTitle().equals(chapterTitleEnter)) {//根据章节ID
                mPageLoader.skipToChapter(i, page);
                equal = true;
                break;
            }
        }

        if (equal) {
            return;
        } else {
            //在末尾位置插入新的一个伪造章节，并跳到末尾位置
            List<BookChapterBean> tempList = mCollBook.getBookChapters();
            if (null == tempList) return;
            if (tempList.isEmpty()) return;

            BookChapterBean tempBean = new BookChapterBean();
            tempBean.setTitle(chapterTitleEnter);
            tempBean.setId("999999999999");
            tempBean.setLink("");
            tempBean.setChapterSource("");
            tempBean.setBookId(mCollBook.get_id());

            tempList.add(tempBean);

            mCollBook.setBookChapters(tempList);

            mPageLoader.refreshChapterList(false, mCollBook.getBookChapters());
            mPageLoader.skipToChapter(tempList.size() - 1, page);
        }
    }

    /**
     * 根据章节URL获取当前章节的位置
     *
     * @return
     */
    private int getChapterPositionByChapterUrl(List<BookChapterBean> bookChapterBeans) {
        RxLogTool.e("getChapterPositionByChapterUrl:" + chapterUrl);
        for (int i = 0; i < bookChapterBeans.size(); i++) {
            BookChapterBean txtChapter = bookChapterBeans.get(i);
            if (txtChapter.getLink().equals(chapterUrl)) {//根据章节ID
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
            if (isShow && rl_read_loading.getVisibility() == View.GONE) {
                rl_read_loading.setVisibility(View.VISIBLE);
                //如果加载动画显示中
                tv_loading_slogan.setText(loadingSlogan);

                if (null == myHandler) {
                    myHandler = new MyHandler(this);
                }
                Constant.percent = 0;
                tv_loading_percent.setText("正实时从原网页转码 0%");
                myHandler.sendEmptyMessageDelayed(3, 50);
            } else if (!isShow && rl_read_loading.getVisibility() == View.VISIBLE) {
                myHandler.removeMessages(3);
                rl_read_loading.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void getBookDirectoryView(CollBookBean collBookBean, boolean isUpdateCategory, boolean needReopen) {

    }

    @Override
    public void getBookDirectoryView2(CollBookBean collBookBean, boolean isUpdateCategory) {//书的信息返回成功//,String chapterId
//        RxToast.custom("目录获取成功").show();
//        showLoadingAnimation(false);
        long t1 = System.currentTimeMillis();

        if (!isCategoryFinish) {
            if (RxSharedPreferencesUtil.getInstance().getBoolean("is_show_read_guide", true)) {//是否显示引导图层
                iv_guide_read.setVisibility(View.VISIBLE);
            }
            startEnterPageTime = System.currentTimeMillis();//初始化出第一个页面的时间
        }

        if (Integer.parseInt(mBookId) <= Constant.bookIdLine) {
            String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
            if (TextUtils.isEmpty(nowSource)) {
                if (null != collBookBean.getBookChapters()) {
                    if (!collBookBean.getBookChapters().isEmpty()) {
                        RxSharedPreferencesUtil.getInstance().putString(mBookId + "_source", collBookBean.getBookChapters().get(0).getChapterSource());
                        if (null != mBookInfoBean) {
                            //只有书信息接口请求下来之后，才会后置刷新一次
                            showConcern();
                        }
                    }
                }
            }
        }

        mCollBook = collBookBean;
        if (null != mBookInfoBean) {
            mCollBook.setAuthor(mBookInfoBean.getData().getPenName());
            mCollBook.setCover(mBookInfoBean.getData().getPlatformCover());
            mCollBook.setTitle(mBookInfoBean.getData().getPlatformName());
            BookRepository.getInstance().saveCollBook(mCollBook);
        }
        //更新一下书信息
        initBookData(mCollBook);

        //目录数据初始化
        categoryChaptersList.clear();
        categoryChaptersList.addAll(collBookBean.getBookChapterList());

        totalChapterCount = collBookBean.getBookChapters().size();//总章节数量

        RxLogTool.e("getBookDirectoryView totalChapterCount:" + totalChapterCount);

        //总共章节数
        if (null != collBookBean.getBookChapterList()) {
            tv_book_total_chapter.setText("共" + totalChapterCount + "章");
        } else {
            tv_book_total_chapter.setText("共0章");
        }

        //删除添加的错误章节。
        BookRepository.getInstance().deleteBookChapterById("error");
        if (isUpdateCategory) {
            //更新目录-先删除之前的缓存目录，存储最新目录-保证本地数据都是最新的
            //获取源字符串
            if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
                BookRepository.getInstance().deleteBookChapterById(mBookId);
                BookRepository.getInstance()
                        .saveBookChaptersWithAsync(collBookBean.getBookChapters());
            } else {
                BookRepository.getInstance().deleteBookChapterById(mBookId + bidStr);
                BookRepository.getInstance()
                        .saveBookChaptersWithAsync(collBookBean.getBookChapters());
            }
        }
        mPageLoader.refreshChapterList(false, collBookBean.getBookChapters());

        //跳章节做延迟，读缓存的时候，pageview可能没准备好
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    //优先标题
                    if (!TextUtils.isEmpty(chapterTitleEnter)) {//指定打开的章节-如果是最新章节-本地数据库没有这个章节-先跳转到最后章节，然后等最新目录获取到的时候才跳转到最新章节
//                        RxLogTool.e("chapterTitleEnter:" + chapterTitleEnter);
                        getChapterPositionByChapterTitle(mCollBook.getBookChapters());
                    } else if (!TextUtils.isEmpty(chapterId)) {//指定打开的章节-如果是最新章节-本地数据库没有这个章节-先跳转到最后章节，然后等最新目录获取到的时候才跳转到最新章节
                        RxLogTool.e("chapterId:" + chapterId);
                        int chapterPosition = getChapterPositionByChapterId(mCollBook.getBookChapters());
                        RxLogTool.e("chapterPosition:" + chapterPosition);
                        mPageLoader.skipToChapter(chapterPosition);
                    } else if (!TextUtils.isEmpty(chapterUrl)) {//指定打开的章节-如果是最新章节-本地数据库没有这个章节-先跳转到最后章节，然后等最新目录获取到的时候才跳转到最新章节
                        RxLogTool.e("chapterUrl:" + chapterUrl);
                        int chapterPosition = getChapterPositionByChapterUrl(mCollBook.getBookChapters());
                        RxLogTool.e("chapterPosition:" + chapterPosition);
                        mPageLoader.skipToChapter(chapterPosition);
                    }
                } catch (Exception e) {
                }
            }
        }, 1000);


        initCacheChapterData();

        isCategoryFinish = true;

        long t2 = System.currentTimeMillis();


        //此指令打开目录
        if (openMulu) {
            openMulu = false;
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

    }

    @Override
    public void getBookDirectoryErrorView(String message) {
        if (TextUtils.isEmpty(message)) {
            RxToast.custom("目录获取失败").show();
        } else {
            RxToast.custom(message).show();
        }
//        showLoadingAnimation(false);
    }

    int tryRequestCount = 0;

    @Override
    public void getBookDirectoryTimeout() {
        if (!isCategoryFinish && tryRequestCount < 3) {//目录未加载过-启动重试机制
            tryRequestCount++;
//            RxToast.custom("目录获取超时，开始重新请求").show();
            //重新请求
            processRequest();
        } else {
            RxToast.custom("目录获取失败").show();
//            showLoadingAnimation(false);
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
                if (null != mAblTopMenu) {
                    mAblTopMenu.setVisibility(View.VISIBLE);
                }
                if (null != iv_chat_room_enter) {
                    iv_chat_room_enter.setVisibility(View.VISIBLE);
                }
                if (ivFollowVisible && null != iv_follow) {
                    iv_follow.setVisibility(View.VISIBLE);
                }
                if (null != iv_return_bookshelf) {
                    iv_return_bookshelf.setVisibility(View.VISIBLE);
                }
                //第一次的时候展示阅读提示
                if (null != read_tip_iv) {
                    int count = RxSharedPreferencesUtil.getInstance().getInt(Constant.READ_TIP_IMG, 0);
                    if (count < 2 && Integer.parseInt(mBookId) <= Constant.bookIdLine) {
                        count++;
                        RxSharedPreferencesUtil.getInstance().putInt(Constant.READ_TIP_IMG, count);
                        read_tip_iv.setVisibility(View.VISIBLE);
                    } else {
                        read_tip_iv.setVisibility(View.GONE);
                    }
                }
                showRewardBarrage();
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
                if (null != mAblTopMenu) {
                    mAblTopMenu.setVisibility(GONE);
                }
                if (null != ll_chapter_tip) {
                    ll_chapter_tip.setVisibility(GONE);
                }
                if (null != read_tip_iv) {
                    read_tip_iv.setVisibility(View.GONE);
                }
                if (null != iv_follow) {
                    iv_follow.setVisibility(View.GONE);
                }
                if (null != iv_chat_room_enter) {
                    iv_chat_room_enter.setVisibility(View.GONE);
                }
                if (null != iv_return_bookshelf) {
                    iv_return_bookshelf.setVisibility(View.GONE);
                }
//                isShowBarrageView(false);//隐藏弹幕
                closeRewardBarrage();
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
                if (null != mLlBottomMenu) {
                    mLlBottomMenu.setVisibility(View.VISIBLE);
                }
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
                if (null != mLlBottomMenu) {
                    mLlBottomMenu.setVisibility(GONE);
                }
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

//
//    @Override
//    public void preLoadingChapterSuccess() {//预加载成功
//
//    }


    @Override
    public void vipChapter(TxtChapter txtChapter) {//vip章节，自动修复
        showLoadingAnimation(false);
        //只有一个站点，自动修复也没用，直接提示
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
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
        if (Integer.parseInt(mBookId) <= Constant.bookIdLine && isCategoryFinish && null != mBookInfoBean && RxLoginTool.isLogin() && !mBookInfoBean.getData().isIsConcern()) {//已登录-并且未关注-并且目录加载完毕
            if (null == addBookStoreDialog) {
                addBookStoreDialog = new RxDialogSureCancelNew(this, R.style.OptionDialogStyle);
            }
            addBookStoreDialog.setTitle("喜欢这本书，就加入书架吧！");
            addBookStoreDialog.setContent("");//加入书架后，不仅可以随时开启阅读，还能快速和本书友的读者们聊天互动哦~
            addBookStoreDialog.setButtonText("加入书架", "取消");

            addBookStoreDialog.show();

            addBookStoreDialog.setSureListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    addBookStoreDialog.dismiss();
                    String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
                    requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()), nowSource, true);
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

    private void addAttentionClick() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bookCover", mBookInfoBean.getData().getPlatformCover());
        jsonObject.addProperty("bookName", mBookInfoBean.getData().getPlatformName());
        jsonObject.addProperty("bookId", mBookInfoBean.getData().getBookId());
        requestConcernUrlBook(jsonObject.toString());
    }

    @OnClick(R.id.iv_exchange)
    void exchangeClick() {
        if (null != mPageLoader && null != mPageLoader.getCurrentTxtChapter()) {
            String webUrl = mPageLoader.getCurrentTxtChapter().getLink();
            //优先用书评传进来的source
            String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");
            ActivityWebView.startActivityForReadNormal(UrlReadActivity.this, webUrl, mBookId, nowSource, false);
        } else {
            RxToast.custom("原网页尚未获取到").show();
        }
    }

    //共享版权的点击
    @OnClick(R.id.icon_gongxiangbanquan)
    void icon_gongxiangbanquanClick() {
        if (null == mBookInfoBean) {
            RxToast.custom("书籍信息尚未获取到").show();
            return;
        }
        if (null == mBookInfoBean.getData()) {
            RxToast.custom("书籍信息尚未获取到").show();
            return;
        }
        ActivityWebView.startActivity(UrlReadActivity.this, mBookInfoBean.getData().getSkipUrl());
    }

    /**
     * 重新下载当前章节-修复章节
     */
    @OnClick(R.id.fl_reload_chapter)
    void reloadChapterClick() {
        //体验阅读修复功能
        requestReloadChapterClick();
        getChapterSourceRequest();
    }

    //体验阅读修复功能
    private void requestReloadChapterClick() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/task/commonMessage/21035", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    private void sortCategory() {
        if (null != categoryChaptersList) {
            Collections.reverse(categoryChaptersList);

//            int currentChapterPosition = categoryChaptersList.size() - mCategoryAdapter.getCurrentSelected() - 1;
//            RxLogTool.e("currentChapterPosition:" + currentChapterPosition);

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
     * 打赏书籍
     */
    @OnClick(R.id.iv_payfor_book_black)
    void payforBookClick() {
        if (!RxLoginTool.isLogin()) {//登录提示
            showLoginDialog();
            return;
        }
        ActivityPayForBook.startActivity(this, mBookId);
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
                    //点击缓存动作后，偏好自动切换到此源
                    String nowSource = RxSharedPreferencesUtil.getInstance().getString(mBookId + "_source");

                    //点击缓存动作之后，自动加入书架
                    if (isCategoryFinish && null != mBookInfoBean && RxLoginTool.isLogin() && !mBookInfoBean.getData().isIsConcern()) {
                        if (Integer.parseInt(mBookId) > Constant.bookIdLine) {
                            addAttentionClick();
                        } else {
                            requestConcernPlatform(String.valueOf(mBookInfoBean.getData().getPlatformId()), nowSource, false);
                        }
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


        //判断是否是内站书
        Boolean isInside = false;
        try {
            if (bean.getLink().contains("lianzai.com") || bean.getLink().contains("bendixing.net")) {
                isInside = true;
            }
        }catch (Exception e){
        }


        if(isInside) {
            ArrayMap<String, Object> arrayMap = new ArrayMap<>();
            arrayMap.put("chapterId", bean.getId());

            Disposable disposable = mPresenter.getmReaderApi()
                    .insideBookChapterDetail(arrayMap)
                    //表示在当前环境下执行
                    .subscribe(
                            responseBody -> {
                                try {
                                    String currentSource;
                                    //取源
                                    currentSource = bean.getChapterSource();
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
                                    String responseData = responseBody.getData().getContent();
                                    outsideChapterDetail = ParserService.getDataBeanFromInside(responseData);
                                    if (null != outsideChapterDetail && !TextUtils.isEmpty(outsideChapterDetail.getContent())) {
                                        String content = RxTool.filterChar(outsideChapterDetail.getContent());
                                        //存储数据
                                        BookRepository.getInstance().saveChapterInfo(
                                                mBookId, bean.getId(), content);
                                    }
                                } catch (Exception e) {
//                                e.printStackTrace();
                                }
                            },
                            e -> {
                                //当前进度加载错误（这里需要判断是什么问题，根据相应的问题做出相应的回答）
                                LogUtils.e("test", "loadChapter error:" + e.getMessage());
                            }
                    );
            addDisposable(disposable);
            return result[0];

        }else {
            Disposable disposable = mPresenter.getmReaderApi()
                    .outsideBookChapterDetail(bean.getLink())
                    //表示在当前环境下执行
                    .subscribe(
                            responseBody -> {
                                try {
                                    String currentSource;
                                    //取源
                                    currentSource = bean.getChapterSource();
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
                                        BookRepository.getInstance().saveChapterInfo(
                                                mBookId, bean.getId(), content);
                                    }
                                } catch (Exception e) {
//                                e.printStackTrace();
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
    }

    private void postDownloadChange(DownloadTaskBean task, int status, String msg, int totalChapter, String mBookId) {
        //通过handler,切换回主线程
        if (null == myHandler) {
            myHandler = new MyHandler(UrlReadActivity.this);
        }
        myHandler.post(
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


            if (null != ll_chapter_tip && bookId.equals(mBookId + bidStr)) {//当前缓存的书是打开的这本书
                ll_chapter_tip.setVisibility(View.GONE);
                cacheChapterCount = 0;
                //刷新目录状态
                mCategoryAdapter.notifyDataSetChanged();
                //刷新数据
                initCacheChapterData();
            }
        } else if (status == DownloadTaskBean.STATUS_LOADING) {//正在下载
            isDownloading = true;//正在下载中
            if (null != mLlBottomMenu && bookId.equals(mBookId + bidStr)) {
                RxLogTool.e("cacheChapterProgress cache success cacheChapterCount:" + cacheChapterCount);
                cacheChapterCount++;
                if (mLlBottomMenu.getVisibility() == VISIBLE) {//只有底部的菜单栏在的时候才显示
                    ll_chapter_tip.setVisibility(View.VISIBLE);
                }
                tv_chapter_status.setText("正在缓存中...");
                iv_return_chapter.setVisibility(View.GONE);
                tv_cache_chapter_progress.setText(msg + "/" + currentTotalChapters);

                iv_chat_room_enter.setVisibility(View.GONE);
                //缓存中，弹幕不显示
//                isShowBarrageView(false);
            }
        } else if (status == DownloadTaskBean.STATUS_ERROR) {
            if (null != tv_chapter_status && bookId.equals(mBookId + bidStr)) {
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
        cacheChapterCont = BookManager.getCacheChapterCount(mBookId + bidStr);
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
//                    e.printStackTrace();
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
//                    e.printStackTrace();
                }
            }
        });
    }


    List<PageParagraphVo> pageParagraphVos = new ArrayList<>();
    private MusicService.MyBinder myBinder;//中间人对象
    private MyConn myConn;

    private void registerService() {
        Intent intent = new Intent(UrlReadActivity.this, MusicService.class);
//        startService(intent);
        myConn = new MyConn();
        //【1】绑定服务,并在MyConn中获取中间人对象（IBinder）
        bindService(intent, myConn, BIND_AUTO_CREATE);
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
//        if (!RxNetTool.isAvailable()) {
//            RxToast.custom("没有网络无法开启听书模式").show();
//            return;
//        }
        if (!RxLoginTool.isLogin()) {
            showLoginDialog();
            return;
        }

        if (null == mBookInfoBean || null == mBookInfoBean.getData()) {
            RxToast.custom("书籍信息获取失败").show();
            return;
        }

        if (mBookInfoBean.getData().getLimitListen() && mBookInfoBean.getData().getRemainSecond() <= 0) {
            RxToast.custom("听书时长不足").show();
            ActivityListenPay.startActivity(UrlReadActivity.this);
            return;
        }

        if (!canListen) {
            RxToast.custom("听书尚未准备完毕，请稍后重试").show();
            return;
        }

        if (null == myBinder) {
            RxToast.custom("听书尚未准备完毕，请稍后重试").show();
            return;
        }

        if (isCategoryFinish && null != mPageLoader) {
            //获取听书数据
            mPresenter.prepareListenBookData(mPageLoader, true);
            mPageLoader.setListenBookStyle(true, 1);
            toggleMenu();//隐藏
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
            listenStartTime = System.currentTimeMillis();
            toListenBookRequest();
        }

//        //绑定听书服务，要改动
//        registerService();
//        //释放听书资源
//        unbindService(myConn);
//        myConn = null;

    }


    @Override
    public void returnListenBookDataView(List<PageParagraphVo> mPageParagraphVos) {
        if (null == mPageParagraphVos || mPageParagraphVos.isEmpty()) {
            exitListenBook(false);
            RxToast.custom("当前页面无法朗读").show();
            return;
        }
        if (!Constant.isListenBook) {
            Constant.isListenBook = true;//听书模式
        }
        pageParagraphVos.clear();
        pageParagraphVos.addAll(mPageParagraphVos);
        if (null != myBinder) {
            myBinder.play(pageParagraphVos);
        }
    }


    private void dealFinish(String utteranceId) {

//        if (!RxNetTool.isAvailable()) {
//            showListenMessageDialog("网络断开，退出听书模式");
//            exitListenBook(true);
//            return;
//        }

        if (listenEndTime != 0 && System.currentTimeMillis() > listenEndTime) {//倒计时结束
            exitListenBook(true);
            RxLogTool.e("定时退出听书模式" + System.currentTimeMillis());
            showListenMessageDialog("本次定时听书完成，已自动退出听书模式");
        } else if (System.currentTimeMillis() - listenStartTime > 10800000) {
            exitListenBook(true);
            RxLogTool.e("定时退出听书模式" + System.currentTimeMillis());
            showListenMessageDialog("您已连续听书3小时了，快休息一下吧！");
        } else if (null == pageParagraphVos) {
            exitListenBook(true);
            showListenMessageDialog("数据异常，退出听书");
        } else {
            int size = pageParagraphVos.size();
            if (Integer.parseInt(utteranceId) < pageParagraphVos.get(size - 1).paragraphPosition) {
                //不需要翻页的情况
                mPageLoader.setListenBookStyle(true, Integer.parseInt(utteranceId) + 1);
            } else {
                //需要翻页的情况
                if (mPageLoader.getChapterPos() == mPageLoader.getChapterCategory().size() - 1 && mPageLoader.getPagePos() == mPageLoader.getCurPageList().size() - 1) {//是否能翻页
                    exitListenBook(true);
                    RxLogTool.e("最后一章已经阅读完毕，自动退出听书模式" + System.currentTimeMillis());
                    showListenMessageDialog("最后一章已经阅读完毕，自动退出听书模式");
                } else {

                    if (mPageLoader.getPagePos() == mPageLoader.getCurPageList().size()) {
                        mPageLoader.skipToNextPage();//下一页
                        //默认选中第一行
                        mPageLoader.setListenBookStyle(true, 1);
                        mPresenter.prepareListenBookData(mPageLoader, true);
                    } else {
                        mPageLoader.skipToNextPage();//下一页
                        //默认选中第二行
                        mPageLoader.setListenBookStyle(true, 1);
                        mPresenter.prepareListenBookData(mPageLoader, false);
                    }

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
                    if (Constant.isListenBook) {
                        if (null != myBinder) {
                            if (!myBinder.isStop())
                                myBinder.stop();
                            if (null == myHandler) {
                                myHandler = new MyHandler(UrlReadActivity.this);
                            }
                            myHandler.sendEmptyMessageDelayed(4, 0);
                        }
                    }
                }

                @Override
                public void changeVoicer(String voicer) {
                    ReadSettingManager.getInstance().setVoicer(voicer);
                    if (Constant.isListenBook) {
                        if (null != myBinder) {
                            myBinder.changeVoice();
                            if (null == myHandler) {
                                myHandler = new MyHandler(UrlReadActivity.this);
                            }
                            myHandler.sendEmptyMessageDelayed(5, 0);
                        }
                    }
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
        readListenDialog.setListenTime(mBookInfoBean.getData().getRemainSecond(), mBookInfoBean.getData().getLimitListen());
        //夜间白天模式切换
        readListenDialog.toggleDayNightMode(isNightMode, listenMin);

        //暂停听书
        if (null != myBinder) {
            myBinder.stop();
        }

        readListenDialog.tv_exit_listen_book.setOnClickListener(
                v -> {
                    exitListenBook(true);
                    RxToast.custom("退出听书模式").show();
                    readListenDialog.dismiss();
                }
        );

        readListenDialog.setOnDismissListener(
                v -> {
                    hideSystemBar();
                    if (Constant.isListenBook && myBinder.isStop()) {
                        myBinder.moveon();
                    }
                }
        );
    }

    private void exitListenBook(boolean needup) {
        listenEndTime = 0;
        Constant.isListenBook = false;
        if (null != myBinder) {
            myBinder.dissmissNotification();
        }

        if (null != mPvPage) {
            mPvPage.setEnabled(true);
        }
        if (null != mPageLoader) {
            mPageLoader.setListenBookStyle(false, 1);
        }

//        if (needup) {
//            //退出听书阅读模式，上传一次阅读记录
//            uploadTimeline();
//        }
    }

    /**
     * 提交反馈
     */
    @OnClick(R.id.read_tv_feedback)
    void readFeedbackClick() {
        //跳到发表动态的页面
        if (null == mBookInfoBean) {
            RxToast.custom("书籍信息未加载").show();
            return;
        }
        if (null == mPageLoader) {
            RxToast.custom("页面未加载完毕").show();
            return;
        }
        if (null == mPageLoader.getCurrentTxtChapter()) {
            RxToast.custom("页面未加载完毕").show();
            return;
        }

        //隐藏红点
        view_post_red.setVisibility(View.GONE);
        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.FIRST_POST_KEY, false);

        String circleId = String.valueOf(mBookInfoBean.getData().getPlatformId());
        ActivityPostCircle.startActivity(this, circleId);
//        String circleId = String.valueOf(mBookInfoBean.getData().getPlatformId());
//        String bookName = mBookInfoBean.getData().getPlatformName();
//        String chapterId = mPageLoader.getCurrentTxtChapter().getChapterId();
//        String chapterName = mPageLoader.getCurrentTxtChapter().getTitle();
//        String cover = mBookInfoBean.getData().getPlatformCover();
//        String source = mPageLoader.getCurrentTxtChapter().getSourceKey();
//        String url = mPageLoader.getCurrentTxtChapter().getLink();
//        ActivityPostCircle.startActivity(UrlReadActivity.this, circleId, mBookId, bookName, chapterId, chapterName, cover, source, url);
    }



    /**
     * 每翻一页，添加一条阅读时长数据
     *
     * @param pagePos
     * @param chapterId
     * @param pageTotalCount
     * @param mTextSize
     */
    private void addReadTimeData(int pagePos, int chapterId, int pageTotalCount, int mTextSize, String source, String title) {
        if (chapterId == 0 && pagePos == 0) {
            //可能是第一次打开的跳转
            return;
        }
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

            readTimeBean.setSource(source);
            readTimeBean.setChapterTitle(title);

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

                //复位起始时间
                startEnterPageTime = readTimeBean.getEndTime();
            }
            //每页阅读在一秒钟以上的，才存入数据库
            if (readTimeBean.getEndTime() - readTimeBean.getStartTime() > 1000) {
                //直接存入数据库
                ReadTimeRepository.getInstance().addReadTime(readTimeBean);
                //每次新增阅读数据，进行扫描。
                RxReadTimeUtils.scanReadTime();
            }


        }

    }

    /**
     * 上传有效时长
     */
//    private void uploadTimeline() {//上传阅读时长
//        if (RxLoginTool.isLogin() && !TextUtils.isEmpty(userId)) {
//            readTimePresenter.uploadReadTime(userId, readTimeBeanList, 1);
//        }
//    }
    @OnClick(R.id.img_back)
    void closeClick() {//退出阅读
        exit();
    }


    private class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UrlReadActivity newReadActivity = (UrlReadActivity) reference.get();
            try {
                if (msg.what == 2) {
                    if (Constant.isAutoPage) {
                        //翻到下一页并再发送一个自动翻页消息
                        mPageLoader.skipToNextPage();
                        sendEmptyMessageDelayed(2, autoPageTime);
                    }
                }else if (msg.what == 3) {
                    Constant.percent++;
                    if (Constant.percent > 0 && Constant.percent < 100) {
                        tv_loading_percent.setText("正实时从原网页转码 " + Constant.percent + "%");
                    } else {
                        tv_loading_percent.setText("正实时从原网页转码 99%");
                    }
                    sendEmptyMessageDelayed(3, 50);
                } else if (msg.what == 4) {
                    if (null != myBinder) {
                        myBinder.setParams(1);
                        //获取听书数据
                        mPresenter.prepareListenBookData(mPageLoader, true);
                    }
                } else if (msg.what == 5) {
                    if (null != myBinder) {
                        myBinder.setParams(2);
                        //获取听书数据
                        mPresenter.prepareListenBookData(mPageLoader, true);
                    }
                } else if (msg.what == 6) {
                    //获取听书数据
                    mPresenter.prepareListenBookData(mPageLoader, false);
                } else if (msg.what == 7) {
                    checkRewardBarrage();
                } else if (msg.what == WHAT_CATEGORY) {
                    if (!isSortDesc) {
                        mLvCategory.setSelection(categoryChaptersList.size() - mPageLoader.getChapterPos() - 1);
                    } else {
                        mLvCategory.setSelection(mPageLoader.getChapterPos());
                    }

                    isOperationChapter = true;//操作章节

                } else if (msg.what == WHAT_CHAPTER) {
                    mPageLoader.openChapter();
                }


//                else if (null != barrage_view.getBarrageBeanList()) {
//                    if (msg.what == 0) {
//                        barrage_view.addAnimationView(barrage_view.getBarrageBeanList().get(currentIndex));
//                    } else if (msg.what == 1) {
//                        barrage_view.removeFirstView(barrage_view.getBarrageBeanList().get(currentIndex));
//                    }
//                }
            } catch (Exception e) {
                RxLogTool.e("MyHandler e:" + e.getMessage());
            }
        }
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
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
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
//                    e.printStackTrace();
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





    /**
     * 设为偏好
     */
    private void requestPreference(String platformId, String source, boolean showTaost) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(source)) {
            map.put("source", source);
            RxSharedPreferencesUtil.getInstance().putString(mBookId + "_source2", source);
            showConcern();
        }
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + platformId + "/preference", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (showTaost) {
                            RxToast.custom("已成功设为偏好", Constant.ToastType.TOAST_SUCCESS).show();
                        }
                    } else if (showTaost) {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 关注智能目录书籍
     */
    private void requestConcernPlatform(String platformId, String source, boolean needFinish) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(source)) {
            map.put("source", source);
            RxSharedPreferencesUtil.getInstance().putString(mBookId + "_source2", source);
            showConcern();
        }
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + platformId + "/Concern", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore(needFinish);
                        //更新关注信息
                        mBookInfoBean.getData().setIsConcern(true);
                        showConcern();
                        RxToast.custom("已成功添加至书架", Constant.ToastType.TOAST_SUCCESS).show();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 关注url书籍
     */
    private void requestConcernUrlBook(String json) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/book/shelf/bookAttention", json, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore(false);
                        RxToast.custom("已成功添加至书架", Constant.ToastType.TOAST_SUCCESS).show();
                        mBookInfoBean.getData().setIsConcern(true);
                        showConcern();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestConcernUrlBook e:" + e.getMessage());
                }
            }
        });
    }

    //同步书架数据
    private void requestBookStore(boolean needFinish) {
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

                        if (needFinish) {
                            finish();
                        }

                    } else {//加载失败
                    }

                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void returnChapterUrlsView(List<ChapterUrlsVo> chapterUrlsVos) {
    }


//    @Override
//    public void uploadErrorChapterView(List<BookChapterBean> errorBookChapterBeans) {
//        try {
//            if (null != errorBookChapterBeans && errorBookChapterBeans.size() > 0) {//上报给后台
//                //上报给后台
//                postFeedback("0", mBookId, errorBookChapterBeans.get(0).getId(), "章节有问题", true, mBookInfoBean.getData().getPlatformName() + "::" + mPageLoader.getCurrentTxtChapter().getTitle());
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


    private void showListenMessageDialog(String message) {
        if (null == rxDialogSureCancelTip) {
            rxDialogSureCancelTip = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelTip.setButtonText("我知道了", "取消");
        rxDialogSureCancelTip.getCancelView().setVisibility(View.GONE);
        rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
        rxDialogSureCancelTip.setTitle("听书停止");
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
                exitListenBook(true);
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
            if (Constant.isListenBook) {
                showReadListenDialog();
            }
        } else if (event.getTag() == Constant.EventTag.SKIP_TO_CIRCLE) {//进入圈子
            if (null != mBookInfoBean) {
                //防止多次跳转
                long temp = System.currentTimeMillis();
                if (temp - skipTime > 3000) {
                    skipTime = temp;
                    ActivityCircleDetail.startActivity(UrlReadActivity.this, String.valueOf(mBookInfoBean.getData().getPlatformId()), 1);
                }
            }
        } else if (event.getTag() == Constant.EventTag.REFRESH_BOOK_INFO) {//取消关注动作之后刷新书籍信息
            getBookInfo();
        } else if (event.getTag() == Constant.EventTag.REFRESH_LIKE_INFO) {//仅仅刷新书籍偏好显示
            showConcern();
        } else if (event.getTag() == Constant.EventTag.LOGIN_REFRESH_TAG) {
            //获取到个人信息后直接初始化信息，并更新书籍阅读时间。
            initReadTimeData();
            getBookInfo();
        } else if (event.getTag() == Constant.EventTag.LOADCURRENTCHAPTERSUCCESS) {
            //当前章节加载成功
            showLoadingAnimation(false);
            if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
                if (null == myHandler) {
                    myHandler = new MyHandler(UrlReadActivity.this);
                }
                myHandler.sendEmptyMessage(WHAT_CHAPTER);
            }
            // 当完成章节的时候，刷新列表
            initCacheChapterData();
        } else if (event.getTag() == Constant.EventTag.LOADCURRENTCHAPTERFAILED) {
            //当前章节加载失败
            showLoadingAnimation(false);
            //只有一个站点，自动修复也没用，直接提示
            if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
                mPageLoader.chapterError();
            }
        } else if (event.getTag() == Constant.EventTag.LOADCURRENTCHAPTERTIMEOUT) {
            //当前章节请求超时
            String content = event.getObj().toString();
            TxtChapter txtChapter = GsonUtil.getBean(content, TxtChapter.class);
            if (chapterRequestTryCount < 3) {//重新请求
                mPresenter.loadCurrentChapter(mBookId + bidStr, txtChapter);
                chapterRequestTryCount++;
            } else {
                chapterRequestTryCount = 0;
                //当前章节加载失败
                showLoadingAnimation(false);
                //只有一个站点，自动修复也没用，直接提示
                if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
                    mPageLoader.chapterError();
                }
            }
        } else if (event.getTag() == Constant.EventTag.LISTEN_INIT_SUCCESS) {
            try {
                canListen = true;
            } catch (Exception e) {
            }
        } else if (event.getTag() == Constant.EventTag.LISTEN_NEXT) {
            try {
                String content = event.getObj().toString();
                if (content.contains("error")) {
                    //暂停并继续听书
                    if (null != myBinder) {
                        if (!myBinder.isStop())
                            myBinder.stop();
                        if (null == myHandler) {
                            myHandler = new MyHandler(UrlReadActivity.this);
                        }
                        myHandler.sendEmptyMessageDelayed(6, 0);
                    }
                } else {
                    dealFinish(content);
                }
            } catch (Exception e) {
            }
        } else if (event.getTag() == Constant.EventTag.LISTEN_EXIT) {
            exitListenBook(true);
        } else if (event.getTag() == Constant.EventTag.LISTEN_START) {
            //告诉主线程播放开始，使加载动画消失
//            showListenAnimation(false);
        } else if (event.getTag() == Constant.EventTag.PRELOADINGCHAPTERSUCCESS) {
            //预加载成功
            initCacheChapterData();
        } else if (event.getTag() == Constant.EventTag.UPLOADERRORCHAPTERVIEW) {
            //上报错误章节
            String content = event.getObj().toString();
            BookChapterBean errorBookChapterBeans = GsonUtil.getBean(content, BookChapterBean.class);
            try {
                if (null != errorBookChapterBeans) {//上报给后台
                    //上报给后台
                    postFeedback("0", mBookId, errorBookChapterBeans.getId(), "章节有问题", true, mBookInfoBean.getData().getPlatformName() + "::" + mPageLoader.getCurrentTxtChapter().getTitle());
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }


    /**
     * 目录书籍详情点击
     */
    @OnClick(R.id.rl_book_detail)
    void bookDetailClick() {
        if (null != mBookInfoBean) {
            //关闭侧边栏
            mDlSlide.closeDrawer(Gravity.START);
            ActivityCircleDetail.startActivity(this, String.valueOf(mBookInfoBean.getData().getPlatformId()));
        } else {
            getBookInfo();
        }
    }

    private void registerPhoneListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(phoneStateReceiver, intentFilter);
    }

//    private void requestChatRoomIsOpen(String platformId) {
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/chatrooms/isopen/" + platformId, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    RxLogTool.e("requestChatRoomIsOpen:" + response);
//                    isOpenChatRoomResponse = GsonUtil.getBean(response, IsOpenChatRoomResponse.class);
//                    if (isOpenChatRoomResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {//开通了聊天室
//
//                        //弹幕数据初始化
//                        List<BarrageBean> barrageBeanList = new ArrayList<>();
//
//                        for (IsOpenChatRoomResponse.DataBean.ChatroomRecentNewsListBean bean : isOpenChatRoomResponse.getData().getChatroomRecentNewsList()) {
//                            barrageBeanList.add(new BarrageBean(bean.getFromAvator(), bean.getAttach()));
//                        }
//                        //倒序显示
//                        Collections.reverse(barrageBeanList);
//                        barrage_view.setBarrageBeanList(barrageBeanList);
//                        //表示可以显示弹幕
//                        showBarrage = true;
//                        //弹幕动画
//                        startBarrageAnimation();
//
//                        //假如正在展示中。显示弹幕。
//                        if (isShow) {
//                            iv_chat_room_enter.setVisibility(View.VISIBLE);
//                            isShowBarrageView(barrageBeanList.size() > 0 ? true : false);
//                        }
//
//                    } else {
//                        showBarrage = false;
//                        closeBarrageView();//关闭弹幕
//                        iv_chat_room_enter.setVisibility(View.GONE);
////                        RxToast.custom("该连载号聊天室暂未开通").show();
//                    }
//                } catch (Exception e) {
////                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * ------------------------------------弹幕--------------------------------------------------------------------
     */

//    Timer barrageTimer;
//    TimerTask barrageTimerTask;
//    int currentIndex = 0;
//
//    public void startBarrageAnimation() {
//        if (null == barrage_view) return;
//        if (barrage_view.getBarrageBeanList().size() <= 0) {//有弹幕才跑动画
//            return;
//        }
//        //只初始化一次
//        if (null == barrageTimer) {
//            barrageTimer = new Timer();
//            barrageTimerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    if (!barragePause) {
//                        if (currentIndex == barrage_view.getBarrageBeanList().size() - 1) {
//                            currentIndex = 0;
//                        } else {
//                            currentIndex++;
//                        }
//                        if (barrage_view.getBarrageBeanList().size() < 3 && barrage_view.getViewCount() == barrage_view.getBarrageBeanList().size()) {
//                            barragePause = true;
//                        } else {
//                            if (barrage_view.getViewCount() < 3) {
//                                myHandler.sendEmptyMessage(0);//add view
//                            } else {
//                                myHandler.sendEmptyMessage(1);//remove view
//                            }
//                        }
//                    }
//                }
//            };
//            barrageTimer.schedule(barrageTimerTask, 0, 1000);
//        }
//    }
//
//    private void isShowBarrageView(boolean isShow) {
//        RxLogTool.e("isShowBarrageView:" + isShow);
//        if (showBarrage) {
//            if (isShow) {
//                barragePause = false;
//                if (barrage_view.getBarrageBeanList().size() <= 0) {//有弹幕才跑动画
//                    iv_close_barrage.setVisibility(View.GONE);
//                    barrage_view.setVisibility(View.GONE);
//                } else {
//                    iv_close_barrage.setVisibility(View.VISIBLE);
//                    barrage_view.setVisibility(View.VISIBLE);
//                }
//
//                iv_chat_room_enter.setVisibility(View.VISIBLE);
//                if (iv_chat_room_enter.getVisibility() == VISIBLE && iv_chat_room_enter.getBackground() instanceof AnimationDrawable) {
//                    AnimationDrawable animation = (AnimationDrawable) iv_chat_room_enter.getBackground();
//                    animation.start();
//                }
//            } else {
//                iv_close_barrage.setVisibility(View.GONE);
//                barrage_view.setVisibility(View.GONE);
//                iv_chat_room_enter.setVisibility(View.GONE);
//
//                barragePause = true;
//            }
//
//        } else {
//            iv_close_barrage.setVisibility(View.GONE);
//            barrage_view.setVisibility(View.GONE);
//            iv_chat_room_enter.setVisibility(View.GONE);
//
//            barragePause = true;
//        }
//    }

    /**
     * 弹幕关闭
     */
//    @OnClick(R.id.iv_close_barrage)
//    void closeBarrageView() {
//        barrage_view.setVisibility(View.GONE);
//        iv_close_barrage.setVisibility(View.GONE);
//        barragePause = true;
//    }

    /**
     * 进入圈子主页
     */
    @OnClick(R.id.iv_chat_room_enter)
    void chatRoomClick() {
//        ChatRoomActivity.startActivity(this,"55977960");
        if (null == mBookInfoBean) return;
        if (null == mBookInfoBean.getData()) return;
//        if (Constant.appMode == Constant.AppMode.RELEASE) {
            ActivityCircleDetail.startActivity(this, String.valueOf(mBookInfoBean.getData().getPlatformId()));
//        } else {
//            ActivityReadTimeShow.startActivity(this);
//        }

    }

    /**
     * 弹幕初始化
     */
//    private void initBarrageData() {
//        if (null == myHandler) {
//            myHandler = new MyHandler(this);
//        }
//        if (RxLoginTool.isLogin()) {
//            //弹幕宽度设置
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(2 * ScreenUtil.getDisplayWidth() / 3, 3 * ScreenUtil.dip2px(38));
//            layoutParams.addRule(RelativeLayout.ABOVE, R.id.iv_chat_room_enter);
//            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            barrage_view.setLayoutParams(layoutParams);
//            barrage_view.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                        barragePause = true;
//                    } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                        barragePause = false;
//                    }
//                    return true;
//                }
//            });
//        }
//    }

    /**
     * 书架红点清除接口
     */
    public static void requestClearHotPoint(String bookId) {
        Map<String, String> map = new HashMap<>();
        map.put("bookId", bookId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/book/read/clearHotPoint", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //红点清除成功后调用刷新书架接口，更新数据库
                        RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_REQUEST);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public PageLoader getmPageLoader() {
        return mPageLoader;
    }

    int pageChangePos = -1;
    private void myPageChange(int pos) {
        if(pageChangePos == pos){
            //同一个页面的跳转，比如刚进阅读界面时
            return;
        }else {
            pageChangePos = pos;
            //最后一页增加一个提示
            try {
                if (null != mPageLoader && mPageLoader.getChapterPos() == mPageLoader.getChapterCategory().size() - 1 && pos == mPageLoader.getCurPageList().size() - 1) {//是否能翻页
                    RxToast.custom("已是最后一页").show();
                }
            } catch (Exception e) {
            }

            if (null != mPageLoader && RxLoginTool.isLogin() && null != mPageLoader.getCurPageList()) {//只有登录用户才统计阅读时长-直接跳章，不纳入阅读记录
                isStartOperation = true;
                isOperationChapter = false;

                TxtChapter txtChapter = mPageLoader.getCurrentTxtChapter();
                int pageTotalSize = mPageLoader.getCurPageList().size();
                lastPagePosition = pageTotalSize;

                //阅读时长数据添加
                if (null != txtChapter)
                    addReadTimeData(pos, Integer.parseInt(txtChapter.getChapterId()), pageTotalSize, ReadSettingManager.getInstance().getTextSize(), txtChapter.getSourceKey(), txtChapter.getTitle());
            } else if (null != mPageLoader && !RxLoginTool.isLogin() && null != mPageLoader.getCurPageList()) {
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

    /**
     * 阅读页广告
     * 投放位置 1首页Banner 2任务中心Banner 3金钻领取弹出层 4发现页banner5阅读页章节末尾 6阅读页修复时  默认为1
     */
    int adPos = -1;
    private void getAdRequest(int pos) {
        if (adPos == pos) {
            //同一个章节的跳转，比如刚进阅读界面时
            return;
        } else {
            adPos = pos;
            //每章同步一下云端记录
            readProgress();
            //第一次进来或者章节切换在这里
            //假如是换源来的，则第一次不把计数置空
            if (changeSource) {
                changeSource = false;
            } else {
                Constant.changesource = 0;
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("putPosition", String.valueOf(5));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/banner/getBanner", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                //请求失败的情况下，不展示广告
            }

            @Override
            public void onResponse(String response) {
                try {
                    BannerBean bannerBean = GsonUtil.getBean(response, BannerBean.class);
                    if (bannerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (null != bannerBean.getData() && !bannerBean.getData().isEmpty()) {
                            bannerData = bannerBean.getData().get(0);
                            if (null == UrlReadActivity.this) {
                                return;
                            }
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        int width = ScreenUtil.getDisplayWidth() - RxImageTool.dip2px(32);
                                        Bitmap myBitmap = null;
                                        myBitmap = Glide.with(UrlReadActivity.this).asBitmap().load(bannerData.getBannerPhoto())
                                                .into(width, (int) (width / 2.35))
                                                .get();
                                        if (null != myBitmap) {
                                            mPageLoader.setAdBitmap(myBitmap);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }
                    }
                } catch (Exception e) {
                    //请求失败的情况下，不展示广告
                    e.printStackTrace();
                }
            }
        });
    }

    private void readProgress() {
        if (null == mPageLoader) {
            return;
        }
        if (null == mPageLoader.getCurrentTxtChapter()) {
            return;
        }
        TxtChapter curTxtChapter = mPageLoader.getCurrentTxtChapter();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bookId", mBookId);
        jsonObject.addProperty("chapterId", curTxtChapter.getChapterId());
        jsonObject.addProperty("chapterTitle", curTxtChapter.getTitle());
        jsonObject.addProperty("lastUpdateTime", System.currentTimeMillis());
        jsonObject.addProperty("source", curTxtChapter.getSourceKey());
        OKHttpUtil.okHttpPutJson(Constant.API_BASE_URL + "/user/read/progress", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                } catch (Exception ex) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                    } else {
                    }
                } catch (Exception e) {
                }
            }
        });

    }

}

