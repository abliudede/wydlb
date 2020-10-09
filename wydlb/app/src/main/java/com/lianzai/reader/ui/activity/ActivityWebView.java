package com.lianzai.reader.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.bean.ObserverBean;
import com.lianzai.reader.bean.ReadabilityBean;
import com.lianzai.reader.bean.ShowShareBean;
import com.lianzai.reader.bean.UrlRecognitionBean;
import com.lianzai.reader.interfaces.AndroidInterface;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.WebHistoryBean;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.model.local.WebHistoryRepository;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketRecord;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletMain;
import com.lianzai.reader.ui.adapter.PureReadItemAdapter;
import com.lianzai.reader.utils.AndroidBug5497Workaround;
import com.lianzai.reader.utils.BrightnessUtils;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEncodeTool;
import com.lianzai.reader.utils.RxEncryptTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.UIController;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWebShare;
import com.lianzai.reader.view.page.PageStyle;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.hashids.Hashids;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/14.
 * web界面
 */

public class ActivityWebView extends PermissionActivity {

    public final static String PARAM_URL = "param_url";
    public final static String PARAM_TIT = "param_tit";
    public final static String PARAM_DES = "param_des";
    public final static String PARAM_IMG = "param_img";
    public final static String PARAM_BID = "param_bid";
    public final static String PARAM_CTITLE = "param_ctitle";
    public final static String PARAM_CPAGE = "param_cpage";
    public final static String PARAM_SOURCE = "source";
    public final static String OPEN_MULU = "open_mulu";


    Hashids hashids;


    @Bind(R.id.ly_title)
    LinearLayout ly_title;

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;

    AgentWeb mAgentWeb;

    @Bind(R.id.action_bar)
    LinearLayout action_bar;

    @Bind(R.id.iv_more)
    ImageView iv_more;

//    @Bind(R.id.iv_show)
//    ImageView iv_show;

    @Bind(R.id.iv_exchange)
    ImageView iv_exchange;

    //纯净模式开关
    @Bind(R.id.iv_pureread)
    ImageView iv_pureread;
    boolean canPureClick = true;

    //纯净模式图层
    @Bind(R.id.pure_view)
    RecyclerView pure_view;
    /**
     * 显示的数据
     */
    private List<ReadabilityBean> pureListData = new ArrayList<>();
    /**
     * RecyclerView的适配器
     */
    private PureReadItemAdapter pureReadItemAdapter;
    private RxLinearLayoutManager linearLayoutManager;
    CustomLoadMoreView customLoadMoreView;


    //引导图层
    private int count = 0;
    @Bind(R.id.iv_readmode_enter)
    ImageView iv_readmode_enter;

    boolean isLoaded = false;

    RxDialogSureCancelNew rxDialogSureCancelNew;
    private String titleShare = "";
    private String desShare = "";
    private String imgShare = "";
    private WbShareHandler shareHandler;

    //分享弹窗
    RxDialogWebShare rxDialogWebShare;
    //网页标识0：普通网页，从页面内抓取分享内容 1：普通网页且不显示更多没有分享，2:周报等网页，从上个页面传递过来分享数据3：游戏特定网页，从接口请求分享数据
    private int type;
    private int gameId;

    //识别出来的书id
    private int bid;
    //扫描器，假如页面没有加载任务，则视为加载完成
    MyHandler myHandler;
    private long lastrequest;


    private String shareCodeFormServer;
    private String picUrl;
    //此bookId不用做识别，专用切换到之前的正版阅读
    private String bookId;
    private String chapterTitle;
    private int page;
    private String source;
    private boolean openMulu;
    private String exchangeTip = "暂不支持该网页";

    //是否展示标题
    private boolean showTitle = false;
    private LinearLayout parent_view;
    //用作双击检测
    private long mClickTime;
    private boolean pureMode;


    //默认情况0,普通url，会识别，分享抓取页面内容
    public static void startActivity(Context context, String url) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putInt("type", 0);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //默认情况0,普通url，会识别，分享抓取页面内容,会鉴权
    public static void startActivityNeedUrlAuth(Context context, String url) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putInt("type", 0);
        bundle.putBoolean("needUrlAuth", true);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //默认情况0,普通url，会识别，分享抓取页面内容
    public static void startActivity(Context context, String url, boolean pureMode) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putInt("type", 0);
        bundle.putBoolean("pureMode", pureMode);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //书籍阅读url，type0，因为有了bookId不识别网址,可直接强制转换源
    public static void startActivityForReadNormal(Context context, String url, String bookId, String source, boolean openMulu) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putString(PARAM_BID, bookId);
        bundle.putString(PARAM_SOURCE, source);
        bundle.putBoolean(OPEN_MULU, openMulu);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //书籍阅读url，type0，因为有了bookId不识别网址,书评专用
    public static void startActivityForReadTitle(Context context, String url, String bookId, String chapterTitle, String source, boolean openMulu, int pagePos) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putString(PARAM_BID, bookId);
        bundle.putString(PARAM_CTITLE, chapterTitle);
        bundle.putInt(PARAM_CPAGE, pagePos);
        bundle.putString(PARAM_SOURCE, source);
        bundle.putBoolean(OPEN_MULU, openMulu);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //情况1用这个，普通网页且不显示更多没有分享
    public static void startActivity(Context context, String url, int type) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putInt("type", type);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //情况3用这个，需要带个gameId过来，用来请求接口
    public static void startActivityForGame(Context context, String url, int gameId) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putInt("type", 3);
        bundle.putInt("gameId", gameId);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }

    //情况2用这个,周报等网页
    public static void startActivity(Context context, String url, String tit, String des, String img, int type) {
//        RxActivityTool.removeWebActivity();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_URL, url);
        bundle.putString(PARAM_TIT, tit);
        bundle.putString(PARAM_DES, des);
        bundle.putString(PARAM_IMG, img);
        bundle.putInt("type", type);
        RxActivityTool.skipActivity(context, ActivityWebView.class, bundle);
    }


    String openUrl;
    private boolean needUrlAuth = false;
    View errorView;

    @Override
    protected void onNewIntent(Intent intent) {
        dealIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxEventBusTool.registerEventBus(this);

        setContentView(R.layout.activity_webview);
        AndroidBug5497Workaround.assistActivity(this);
        ButterKnife.bind(this);

        //状态栏字体设置成黑色
        SystemBarUtils.setLightStatusBar(this, true);
        SystemBarUtils.readStatusBar4(this);
        SystemBarUtils.readStatusBar3(this);


        rxDialogWebShare = new RxDialogWebShare(this, R.style.BottomDialogStyle);
        rxDialogWebShare.getTv_share_ftf().setVisibility(View.GONE);

        hashids = new Hashids("ds$#SDa", 8);
        rxDialogSureCancelNew = new RxDialogSureCancelNew(this, R.style.OptionDialogStyle);

        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);


        parent_view = findViewById(R.id.parent_view);
        errorView = LayoutInflater.from(this).inflate(R.layout.view_webview_error, null);

        pure_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (linearLayoutManager.findFirstVisibleItemPosition() >= 1) {
                    iv_readmode_enter.setVisibility(View.GONE);
                    action_bar.setVisibility(View.GONE);
                    SystemBarUtils.readStatusBar4(ActivityWebView.this);
                } else {
                    action_bar.setVisibility(View.VISIBLE);
                    SystemBarUtils.readStatusBar3(ActivityWebView.this);
                }
            }
        });

        action_bar.setOnClickListener(
                v -> {//双击
                    if (System.currentTimeMillis() - mClickTime < 800) {
                        //此处做双击具体业务逻辑
                        try {
                            pure_view.scrollToPosition(0);
                        } catch (Exception e) {

                        }
                    } else {
                        mClickTime = System.currentTimeMillis();
                        //表示单击，此处也可以做单击的操作
                    }
                }
        );

        Intent intent = getIntent();
        dealIntent(intent);
    }

    private void dealIntent(Intent intent) {
        //分享参数获得
        try {
            openUrl = intent.getStringExtra(PARAM_URL);
        } catch (Exception e) {
        }


        try {
            needUrlAuth = intent.getBooleanExtra("needUrlAuth",false);
        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(openUrl)) {
            RxToast.custom("网页地址为空，退出页面").show();
            finish();
            return;
        }

        if(skipFromUrl(openUrl)){
            return;
        }

        //先获得浏览器type
        try {
            type = intent.getIntExtra("type", 0);
        } catch (Exception e) {
        }

        try {
            bookId = intent.getStringExtra(PARAM_BID);
            source = intent.getStringExtra(PARAM_SOURCE);
        } catch (Exception e) {
        }

        try {
            chapterTitle = intent.getStringExtra(PARAM_CTITLE);
            page = intent.getIntExtra(PARAM_CPAGE, 0);
        } catch (Exception e) {
        }


        try {
            openMulu = intent.getBooleanExtra(OPEN_MULU,false);
        } catch (Exception e) {
        }

        //type
        if (type == 0) {
            //网页抓取类型
            iv_more.setVisibility(View.VISIBLE);
            //默认文案和图标
            titleShare = "连载神器";
            desShare = "连载神器";
            imgShare = Constant.UPAI_HTTP_HEADER + "/logo/share.png";
        } else if (type == 1) {
            //不分享类型
            iv_more.setVisibility(View.INVISIBLE);
        } else if (type == 2) {
            //周报类型
            iv_more.setVisibility(View.VISIBLE);
            try {
                titleShare = intent.getStringExtra(PARAM_TIT);
                desShare = intent.getStringExtra(PARAM_DES);
                imgShare = intent.getStringExtra(PARAM_IMG);
            } catch (Exception e) {
            }
        } else if (type == 3) {
            //游戏请求接口类型
            iv_more.setVisibility(View.VISIBLE);
            gameId = intent.getIntExtra("gameId", 0);
            if (gameId == 10) {
                shareCodeFormServer = "1008";
            } else if (gameId > 1) {
                shareCodeFormServer = String.valueOf(gameId);
            }
        }



        try {
            //总是显示阅读模式转换
            iv_exchange.setVisibility(View.VISIBLE);
            iv_pureread.setVisibility(View.VISIBLE);
            //假如是内站书的链接则继续
            if (openUrl.contains("lianzai.com/b/") || openUrl.contains("lianzai.com/c/") || openUrl.contains("lianzai.com/e/")) { // 内站的小说
                iv_exchange.setVisibility(View.VISIBLE);
                iv_pureread.setVisibility(View.VISIBLE);
            } else if (openUrl.contains("lianzai.com") || openUrl.contains("bendixing.net")) {
                //假如是内站链接且不是内站书链接，则不显示。
                iv_exchange.setVisibility(View.GONE);
                iv_pureread.setVisibility(View.INVISIBLE);
                showTitle = true;
            }
            //获取纯净模式内容
            pureMode = intent.getBooleanExtra("pureMode", false);
            if (pureMode && iv_pureread.getVisibility() == View.VISIBLE) {
                enterPureMode(openUrl, true,false);
            }
        } catch (Exception e) {
        }


        if (showTitle) {
            //显示成文字标题
            ly_title.setBackground(null);
            tv_commom_title.setGravity(Gravity.CENTER);
            tv_commom_title.setTextColor(0xff333333);
            tv_commom_title.setText("请稍后...");
        } else {
            //显示成网址
            tv_commom_title.setText(openUrl);
        }

        //可以转换时才显示提示阅读模式转换
        if (null != bookId) {
            iv_readmode_enter.setVisibility(View.VISIBLE);
            //假如是自动转码的状态，跳往阅读页面,传bookid的模式下，不自动跳转
//            if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
//                toRead();
//            }
        }

        String deviceNo = RxSharedPreferencesUtil.getInstance().getString("deviceNo");
        if (TextUtils.isEmpty(deviceNo)) {
            int num = (int) ((Math.random() * 9 + 1) * 100000);
            deviceNo = RxTimeTool.getCurTimeString() + "Android" + num;
            deviceNo = RxEncryptTool.encryptMD5ToString(deviceNo, "AndroidSalt");
            RxSharedPreferencesUtil.getInstance().putString("deviceNo", deviceNo);
        }

        if(needUrlAuth) {
            StringBuilder urlTemp = new StringBuilder(Constant.getAppBaseUrl());
            urlTemp.append("/urlAuth?target=");
            urlTemp.append(RxEncodeTool.urlEncode(openUrl));
            mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                    .setAgentWebParent(parent_view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                    .useDefaultIndicator(getResources().getColor(R.color.color_black_333333), 2)// 使用默认进度条
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .additionalHttpHeader("terminal", String.valueOf(Constant.Channel.ANDROID))
                    .additionalHttpHeader("deviceNo", deviceNo)
                    .additionalHttpHeader("lz_sso_token", null != RxLoginTool.getLoginAccountToken() ? RxLoginTool.getLoginAccountToken().getData().getToken() : "")
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                    .setAgentWebUIController(new UIController(ActivityWebView.this))
                    .setMainFrameErrorView(errorView)
                    .createAgentWeb()//
                    .ready()
                    .go(urlTemp.toString());
        }else {
            mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                    .setAgentWebParent(parent_view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                    .useDefaultIndicator(getResources().getColor(R.color.color_black_333333), 2)// 使用默认进度条
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                    .setAgentWebUIController(new UIController(ActivityWebView.this))
                    .setMainFrameErrorView(errorView)
                    .createAgentWeb()//
                    .ready()
                    .go(openUrl);
        }

        //这句使webview不可滑动
//        mAgentWeb.getWebCreator().getWebView().setOnTouchListener((v, event) -> (event.getAction() == MotionEvent.ACTION_MOVE));


        if (null == bookId) {
            lastrequest = System.currentTimeMillis();
        }
        errorView.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                mAgentWeb.getUrlLoader().reload();
            }
        });
        mAgentWeb.getAgentWebSettings().getWebSettings().setAllowFileAccess(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setJavaScriptEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setAppCacheEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDomStorageEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDatabaseEnabled(true);
        mAgentWeb.getAgentWebSettings().getWebSettings().setDatabasePath(RxFileTool.getDiskCacheDir());
        String ua = mAgentWeb.getAgentWebSettings().getWebSettings().getUserAgentString();
        mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString(ua + "lianzai");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mAgentWeb.getAgentWebSettings().getWebSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAgentWeb.getAgentWebSettings().getWebSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mAgentWeb.clearWebCache();
        AgentWebConfig.debug();
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", new AndroidInterface(mAgentWeb, this));
        RxLogTool.e("mAgentWeb setUserAgentString" + mAgentWeb.getAgentWebSettings().getWebSettings().getUserAgentString());

    }

    private boolean skipFromUrl(String url){
        //跳转逻辑。
        try {
            if (url.equals("account-home")) {//跳到钱包首页
                //钱包首页页面
                ActivityWalletMain.startActivity(ActivityWebView.this);
                finish();
                return true;
            }else if (url.contains(Constant.ParseUrl.VOTINGRECORDS)) {//自动投票记录
                ActivityAutoTicketRecord.startActivity(ActivityWebView.this);
                finish();
                return true;
            }else if (url.contains(Constant.ParseUrl.LIANZAIHAO)) {//连载号
                HashMap<String, String> map = executeParse(url);
                ActivityCircleDetail.startActivity(ActivityWebView.this, map.get("id"));
                finish();
                return true;
            }else if (url.contains(Constant.ParseUrl.CIRCLE)) {//圈子
                HashMap<String, String> map = executeParse(url);
                ActivityCircleDetail.startActivity(ActivityWebView.this, map.get("id"));
                finish();
                return true;
//                String[] tempstr = url.split("/");
//                if (tempstr.length > 1) {
//                    String circleId = tempstr[tempstr.length - 1];
//                    //识别圈子id是否为一串数字
//                    try{
//                        long[] temp = hashids.decode(circleId);
//                        if (null != temp && temp.length > 0) {
//                            circleId = String.valueOf(temp[0]);//解密
//                        }
//                    }catch (Exception e){
//                    }
//                   if(!TextUtils.isEmpty(circleId)){
//                       ActivityCircleDetail.startActivity(ActivityWebView.this, circleId);
//                       finish();
//                   }
//                }
            } else if (url.contains(Constant.ParseUrl.BOOK_LIST)) {//书单详情
                HashMap<String, String> map = executeParse(url);
                ActivityBookListDetail.startActivity(ActivityWebView.this, map.get("id"));
                finish();
                return true;
            } else if (url.contains(Constant.ParseUrl.BOOK_READ)) {//书籍阅读页
                String[] tempstr = url.split("-");
                if (tempstr.length > 1) {
                    SkipReadUtil.normalRead(ActivityWebView.this, tempstr[1], "", openMulu);
                    finish();
                    return true;
                }
            }  else if (url.contains(Constant.ParseUrl.BOOK_READ2)) {//书籍阅读界面
                HashMap<String, String> map = executeParse(url);
                SkipReadUtil.normalRead(ActivityWebView.this, map.get("id"), map.get("ext"), openMulu);
                finish();
                return true;
            } else if (url.contains(Constant.ParseUrl.PERSONAL)) {//个人主页
                HashMap<String, String> map = executeParse(url);
                PerSonHomePageActivity.startActivity(ActivityWebView.this, map.get("uid"));
                finish();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void addLongClick(WebView webView) {
        // 长按点击事件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    // 弹出保存图片的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityWebView.this);
                    builder.setTitle("提示");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            picUrl = hitTestResult.getExtra();//获取图片链接
//                            String[] tempList = picUrl.split(",");
//                            if(tempList.length > 1){
//                                picUrl = tempList[tempList.length - 1];
//                            }
                            try {
//                                RxImageTool.base64StringToBitmap(picUrl,iv_show);
                                saveBitmap(RxImageTool.base64StringToBitmap(picUrl));
                            } catch (Exception e) {
                                RxToast.custom("暂不支持保存该图片").show();
                            }
                            //保存图片到相册
                            //url2bitmap(picUrl);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                return false;//保持长按可以复制文字
            }
        });
    }

    public void url2bitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            if (bm != null) {
                saveBitmap(bm);
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RxToast.custom("保存失败").show();
                }
            });
            e.printStackTrace();
        }
    }

    private void saveBitmap(Bitmap shareBitmap) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    String dirPath = RxFileTool.getDiskCacheDir() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER;
                    RxFileTool.createOrExistsDir(dirPath);
                    String filePath = dirPath + "/" + System.currentTimeMillis() + ".png";
                    String path = RxImageTool.savePhotoToSD(filePath, shareBitmap, BuglyApplicationLike.getContext());
                    RxImageTool.savePicToMedia(ActivityWebView.this,new File(path));
                    RxToast.custom("保存成功").show();
                } catch (Exception e) {
                    RxToast.custom("保存失败").show();
                    e.printStackTrace();
                }
            }
        });
    }


    private String pageTitle = "";
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            if (null == bookId) {
                lastrequest = System.currentTimeMillis();
            }
        }


//        @Override
//        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//            return super.onJsAlert(view, url, message, result);
//        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            RxLogTool.e("onReceivedTitle:" + title);
            if (!TextUtils.isEmpty(title)) {
                pageTitle = title;
                if (type == 0) {
                    //抓取标题
                    titleShare = title;
                    desShare = title;
                }
                if (showTitle && tv_commom_title != null) {
                    tv_commom_title.setText(title);
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.clearWebCache();
            RxLogTool.e("mAgentWeb clearWebCache.....");
//            mAgentWeb.getJsAccessEntrace().callJs("javascript:(function(){window.localStorage.clear();"+
//                        "  })()");
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        if(null != myHandler) {
            myHandler.removeCallbacksAndMessages(null);
        }
        RxEventBusTool.unRegisterEventBus(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (null != mAgentWeb)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (null != mAgentWeb)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.img_back)
    void backClick() {
        if (!mAgentWeb.back()) {
            finish();
        }
    }

    @OnClick(R.id.iv_pureread)
    void purereadClick() {

        if (!canPureClick) {
            RxToast.custom("正在努力加载中...").show();
            return;
        }

        if (pure_view.getVisibility() == View.VISIBLE) {
            pure_view.setVisibility(View.GONE);
            iv_pureread.setImageResource(R.mipmap.purereadmode_close);
            RxToast.custom("您已退出无广告阅读模式").show();
        } else {
            RxToast.custom("正在努力加载中...").show();
            canPureClick = false;
            enterPureMode(openUrl, true,true);
        }
    }


    @OnClick(R.id.iv_exchange)
    void exchangeClick() {
        iv_readmode_enter.setVisibility(View.GONE);
        //不满足条件时直接返回
        if (null == bookId && bid <= 0) {
            RxToast.custom(exchangeTip).show();
            urlRecognitionRequest(openUrl);
            return;
        }
        if (!RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE, false)) {
            //偶尔弹框
            int num = new Random().nextInt(4);
            //第一次点击，必弹框
            if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.FIRST_AUTO_EXCHANGE, true)) {
                RxSharedPreferencesUtil.getInstance().putBoolean(Constant.FIRST_AUTO_EXCHANGE, false);
                num = 1;
            }
            if (num == 1 && !rxDialogSureCancelNew.isShowing()) {
                rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
                rxDialogSureCancelNew.setTitle("自动转码阅读");
                rxDialogSureCancelNew.setContent("以后是否都自动进入转码阅读提升阅读体验?");
                rxDialogSureCancelNew.setCancel("取消");
                rxDialogSureCancelNew.setSure("确定");
                rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        rxDialogSureCancelNew.dismiss();
                        toRead();
                        RxSharedPreferencesUtil.getInstance().putBoolean(Constant.AUTO_EXCHANGE, true);
                    }
                });
                rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        rxDialogSureCancelNew.dismiss();
                        toRead();
                    }
                });
                rxDialogSureCancelNew.show();
            } else {
                //不弹窗时直接去阅读界面
                toRead();
            }
        } else {
            //不弹窗时直接去阅读界面
            toRead();
        }

    }

    @OnClick(R.id.iv_readmode_enter)
    void enterClick() {
        iv_readmode_enter.setVisibility(View.GONE);
    }

    private void toRead() {
        //假如是禁止阅读进来的，不进行识别。直接可以跳旧原生阅读页面。
        if (null != bookId) {
            if (!TextUtils.isEmpty(chapterTitle)) {
                UrlReadActivity.startActivityOutsideRead(ActivityWebView.this, bookId,source,openMulu,"", chapterTitle,page , false);
            } else {
                //禁止阅读进来的
                SkipReadUtil.normalRead(ActivityWebView.this, String.valueOf(bookId), source, openMulu);
            }
        } else if (bid > 0) {
            SkipReadUtil.normalRead(ActivityWebView.this, String.valueOf(bid), source, openMulu);
        } else {
            RxToast.custom(exchangeTip).show();
        }
    }


    private boolean first = true;

    protected WebViewClient mWebViewClient = new WebViewClient() {


        @Override
        public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
            return super.onRenderProcessGone(view, detail);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
//            RxLogTool.e("onReceivedSslError:"+error.toString());
            handler.proceed();//默认同意
        }

        @TargetApi(21)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl() + "");
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        //
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            RxLogTool.e("mWebViewClient shouldOverrideUrlLoading:" + url);
            if (null == bookId) {
                lastrequest = System.currentTimeMillis();
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            RxLogTool.e("mWebViewClient onPageStarted  target:" + url);
            if (null == bookId) {
                lastrequest = System.currentTimeMillis();
            }
            if (first) {
                first = false;
                addLongClick(view);
            }
        }

        @Override
        public void onPageFinished(WebView view, String mUrl) {
            super.onPageFinished(view, mUrl);

            //显示成网址
            if(!openUrl.equals(mUrl)){
                openUrl = mUrl;
                if(skipFromUrl(openUrl)){
                    return;
                }
            }

            if (!showTitle && tv_commom_title != null) {
                tv_commom_title.setText(openUrl);
            }

            RxLogTool.e("mAgentWeb setUserAgentString" + mAgentWeb.getAgentWebSettings().getWebSettings().getUserAgentString());

            if (null == bookId) {
                lastrequest = System.currentTimeMillis();
                //开始自动扫描模式
                if (null == myHandler) {
                    myHandler = new MyHandler(ActivityWebView.this);
                }
                //清空队列,2s后开始扫描
                myHandler.removeCallbacksAndMessages(null);
                myHandler.sendEmptyMessageDelayed(2, 2001);
            }


            if (openUrl.contains(Constant.UPAI_HTTP_HEADER) && openUrl.lastIndexOf(".apk") > 0) {//官方的下载链接才允许下载
                if (!rxDialogSureCancelNew.isShowing()) {
                    rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
                    rxDialogSureCancelNew.setTitle("下载提示");
                    rxDialogSureCancelNew.setContent("是否跳转至第三方浏览器下载该安装文件?");
                    rxDialogSureCancelNew.setCancel("取消下载");
                    rxDialogSureCancelNew.setSure("立即下载");
                    rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
                        @Override
                        public void onRepeatClick(View v) {
                            rxDialogSureCancelNew.dismiss();
                            startBrowserActivity(openUrl);
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
            }

        }

    };

    private class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ActivityWebView activityWebView = (ActivityWebView) reference.get();
            try {
                if (msg.what == 2) {
                    //判断，两秒内没有任何请求，则识别网址，同时终止扫描。
                    long nowTime = System.currentTimeMillis();
                    if (nowTime - lastrequest > 1000) {
                        //插入网页历史记录
                        addHistory(pageTitle, openUrl);
                        urlRecognitionRequest(openUrl);
                        myHandler.removeCallbacksAndMessages(null);
                    } else {
                        //没达到时间，继续扫描
                        sendEmptyMessageDelayed(2, 200);
                    }
                }
            } catch (Exception e) {
                RxLogTool.e("MyHandler e:" + e.getMessage());
            }
        }
    }

    /**
     * url链接识别
     */
    private void urlRecognitionRequest(String url) {
        //假如是内站书的链接则继续
        if (url.contains("lianzai.com/b/") || url.contains("lianzai.com/c/") || url.contains("lianzai.com/e/")) { // 内站的小说

        } else if (url.contains("lianzai.com") || url.contains("bendixing.net") || url.contains("help2.bendixing.net") || url.contains("help.bendixing.net")) {
            //假如是内站链接且不是内站书链接，则返回。
            return;
        }


        RxLogTool.e("urlRecognition start:" + url);
        Map map2 = new HashMap();
        map2.put("bookUrl", url);

        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/book/read/urlRecognition", map2, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    //识别失败的时候，隐藏按钮
                    bid = 0;
                    iv_readmode_enter.setVisibility(View.GONE);
                    RxLogTool.e("urlRecognition" + e.toString());
                } catch (Exception ee) {

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("urlRecognition" + response);
                    UrlRecognitionBean urlRecognitionBean = GsonUtil.getBean(response, UrlRecognitionBean.class);
                    if (urlRecognitionBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        bid = urlRecognitionBean.getData().getNovel_id();
                        //识别为书的时候展示切换到阅读模式按钮
                        if (count < 1) {
                            iv_readmode_enter.setVisibility(View.VISIBLE);
                            count++;
                            //假如是自动转码的状态,且不是自动的纯净模式，跳往阅读页面
                            if (RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE, false) && !pureMode) {
                                toRead();
                            }
                        }
                    } else {
                        //识别失败的时候，隐藏按钮
                        bid = 0;
                        iv_readmode_enter.setVisibility(View.GONE);
                        exchangeTip = urlRecognitionBean.getMsg();
                    }
                } catch (Exception e) {
                    //识别失败的时候，隐藏按钮
                    bid = 0;
                    iv_readmode_enter.setVisibility(View.GONE);
                    RxLogTool.e("urlRecognition" + e.toString());
                }

            }
        });
    }


    private HashMap<String, String> executeParse(String url) {
//        String encodeStr=hashids.encode(10003);
//        RxLogTool.e("executeParse encode:"+encodeStr);
        HashMap<String, String> map = new HashMap<>();

        if (!TextUtils.isEmpty(url) && url.indexOf("?") > 0) {
            String[] paramStr = url.split("\\?");
            for (String str1 : paramStr) {
                String[] params = str1.split("&");
                for (String param : params) {
                    if (param.contains("=")) {
                        String[] tempList = param.split("=");
                        if (tempList.length <= 1) {
                            continue;
                        }

                        String key = tempList[0];
                        String value = tempList[1];
                        //尝试解密
                        try{
                            long[] temp = hashids.decode(value);
                            if (null != temp && temp.length > 0) {
                                value = String.valueOf(temp[0]);//解密
                            }
                        }catch (Exception e){
                        }

                        RxLogTool.e("key:" + key);
                        RxLogTool.e("value:" + value);
                        map.put(key, value);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 直接关闭
     */
    @OnClick(R.id.iv_close)
    void closeActivityClick() {
        finish();
    }

    @OnClick(R.id.iv_more)
    public void moreClick() {
//        if (null==morePopupWindow)return;
//        if (!morePopupWindow.isShowing()){
//            morePopupWindow.showAsDropDown(action_bar, RxDeviceTool.getScreenWidth(this) - morePopupWindow.getWidth(), 0);
//        }
        rxDialogWebShare.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    if (openUrl.contains(Constant.ParseUrl.LIANZAI_CODE)) {//分享获取code
                        try{
                            HashMap<String, String> map = executeParse(openUrl);
                            shareCodeFormServer = map.get(Constant.ParseUrl.LIANZAI_CODE);
                            if (!TextUtils.isEmpty(shareCodeFormServer)) {
                                getCommonShareUrl(1);
                            }
                        }catch (Exception e){
                        }
                        return;
                    }
                    if (type == 0) {
                        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                            return;
                        } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                            RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                            return;
                        } else {
                            new Thread() {
                                @Override
                                public void run() {
                                    //需要在子线程中处理的逻辑
                                    File file = new File(RxImageTool.getImgPathFromCache(imgShare, getApplicationContext()));
                                    RxShareUtils.shareWebUrl(file, openUrl, titleShare, desShare, BuglyApplicationLike.getsInstance().api, true);
                                }
                            }.start();
                        }
                    } else if (type == 1) {
                        //不分享类型
                        RxToast.custom("此页面不能分享").show();
                    } else if (type == 2) {
                        if (!TextUtils.isEmpty(imgShare)) {
                            if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                return;
                            } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                return;
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //需要在子线程中处理的逻辑
                                        File file = new File(RxImageTool.getImgPathFromCache(imgShare, getApplicationContext()));
                                        RxShareUtils.shareWebUrl(file, openUrl, titleShare, desShare, BuglyApplicationLike.getsInstance().api, true);
                                    }
                                }.start();
                            }
                        } else {
                            RxShareUtils.shareWebUrl(null, openUrl, titleShare, desShare, BuglyApplicationLike.getsInstance().api, true);
                        }
                    } else if (type == 3) {
                        getCommonShareUrl(1);
                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    if (openUrl.contains(Constant.ParseUrl.LIANZAI_CODE)) {//分享获取code
                        try{
                            HashMap<String, String> map = executeParse(openUrl);
                            shareCodeFormServer = map.get(Constant.ParseUrl.LIANZAI_CODE);
                            if (!TextUtils.isEmpty(shareCodeFormServer)) {
                                getCommonShareUrl(3);
                            }
                        }catch (Exception e){
                        }
                        return;
                    }
                    if (type == 0) {
                        RxShareUtils.QQShareUrl(this, titleShare, desShare, openUrl, imgShare, qqShareListener);
                    } else if (type == 1) {
                        //不分享类型
                        RxToast.custom("此页面不能分享").show();
                    } else if (type == 2) {
                        RxShareUtils.QQShareUrl(this, titleShare, desShare, openUrl, imgShare, qqShareListener);
                    } else if (type == 3) {
                        getCommonShareUrl(3);
                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    if (openUrl.contains(Constant.ParseUrl.LIANZAI_CODE)) {//分享获取code
                        try{
                            HashMap<String, String> map = executeParse(openUrl);
                            shareCodeFormServer = map.get(Constant.ParseUrl.LIANZAI_CODE);
                            if (!TextUtils.isEmpty(shareCodeFormServer)) {
                                getCommonShareUrl(2);
                            }
                        }catch (Exception e){
                        }
                        return;
                    }
                    if (type == 0) {
                        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                            return;
                        } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                            RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                            return;
                        } else {
                            new Thread() {
                                @Override
                                public void run() {
                                    //需要在子线程中处理的逻辑
                                    File file = new File(RxImageTool.getImgPathFromCache(imgShare, getApplicationContext()));
                                    RxShareUtils.shareWebUrl(file, openUrl, titleShare, desShare, BuglyApplicationLike.getsInstance().api, false);
                                }
                            }.start();
                        }
                    } else if (type == 1) {
                        //不分享类型
                        RxToast.custom("此页面不能分享").show();
                    } else if (type == 2) {
                        if (!TextUtils.isEmpty(imgShare)) {
                            if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                return;
                            } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                return;
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        //需要在子线程中处理的逻辑
                                        File file = new File(RxImageTool.getImgPathFromCache(imgShare, getApplicationContext()));
                                        RxShareUtils.shareWebUrl(file, openUrl, titleShare, desShare, BuglyApplicationLike.getsInstance().api, false);
                                    }
                                }.start();
                            }
                        } else {
                            RxShareUtils.shareWebUrl(null, openUrl, titleShare, desShare, BuglyApplicationLike.getsInstance().api, false);
                        }
                    } else if (type == 3) {
                        getCommonShareUrl(2);
                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    if (openUrl.contains(Constant.ParseUrl.LIANZAI_CODE)) {//分享获取code
                        try{
                            HashMap<String, String> map = executeParse(openUrl);
                            shareCodeFormServer = map.get(Constant.ParseUrl.LIANZAI_CODE);
                            if (!TextUtils.isEmpty(shareCodeFormServer)) {
                                getCommonShareUrl(5);
                            }
                        }catch (Exception e){
                        }
                        return;
                    }
                    if (type == 0) {
                        RxShareUtils.WBShare(shareHandler, this, true, desShare, titleShare, openUrl, true, null);
                    } else if (type == 1) {
                        //不分享类型
                        RxToast.custom("此页面不能分享").show();
                    } else if (type == 2) {
                        RxShareUtils.WBShare(shareHandler, this, true, desShare, titleShare, openUrl, true, null);
                    } else if (type == 3) {
                        getCommonShareUrl(5);
                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_refresh().setOnClickListener(
                v -> {
                    mAgentWeb.getUrlLoader().reload();
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_copy().setOnClickListener(
                v -> {
                    if (openUrl.contains(Constant.ParseUrl.LIANZAI_CODE)) {//分享获取code
                        try{
                            HashMap<String, String> map = executeParse(openUrl);
                            shareCodeFormServer = map.get(Constant.ParseUrl.LIANZAI_CODE);
                            if (!TextUtils.isEmpty(shareCodeFormServer)) {
                                getCommonShareUrl(6);
                            }
                        }catch (Exception e){
                        }
                        return;
                    }
                    if (type == 0) {
                        RxClipboardTool.copyText(ActivityWebView.this, openUrl);
                        RxToast.custom("复制成功").show();
                    } else if (type == 1) {
                        //不分享类型
                        RxToast.custom("此页面不能复制").show();
                    } else if (type == 2) {
                        RxClipboardTool.copyText(ActivityWebView.this, openUrl);
                        RxToast.custom("复制成功").show();
                    } else if (type == 3) {
                        getCommonShareUrl(6);
                    }

                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_browseopen().setOnClickListener(
                v -> {
                    if (openUrl.contains(Constant.ParseUrl.LIANZAI_CODE)) {//分享获取code
                        try{
                            HashMap<String, String> map = executeParse(openUrl);
                            shareCodeFormServer = map.get(Constant.ParseUrl.LIANZAI_CODE);
                            if (!TextUtils.isEmpty(shareCodeFormServer)) {
                                getCommonShareUrl(7);
                            }
                        }catch (Exception e){
                        }
                        return;
                    }
                    if (type == 0) {
                        startBrowserActivity(openUrl);
                    } else if (type == 1) {
                        //不分享类型
                        RxToast.custom("此页面不能跳转").show();
                    } else if (type == 2) {
                        startBrowserActivity(openUrl);
                    } else if (type == 3) {
                        getCommonShareUrl(7);
                    }

                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_refresh().setVisibility(View.VISIBLE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.VISIBLE);
        rxDialogWebShare.show();
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

    /**
     * 更多弹框
     */
//    private void initPopupWindow() {
//        FrameLayout contentView = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.popup_more_webview, null);
//        TextView tv_copy_url = contentView.findViewById(R.id.tv_copy_url);
//        TextView tv_open_browser = contentView.findViewById(R.id.tv_open_browser);
//        TextView tv_refresh = contentView.findViewById(R.id.tv_refresh);
//
//        morePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        morePopupWindow.setOutsideTouchable(true);
//        morePopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        morePopupWindow.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
//        morePopupWindow.setFocusable(true);
//
//        tv_copy_url.setOnClickListener(
//                v -> {
//                    morePopupWindow.dismiss();
//                    RxClipboardTool.copyText(ActivityWebView.this,openUrl);
//                }
//        );
//        tv_open_browser.setOnClickListener(
//                v -> {
//                    morePopupWindow.dismiss();
//                    startBrowserActivity(openUrl);
//                }
//        );
//
//        tv_refresh.setOnClickListener(
//                v -> {
//                    morePopupWindow.dismiss();
//                    mAgentWeb.getUrlLoader().reload();
//                }
//        );
//    }
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

    public void webMoreClick() {
        moreClick();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void chatRoomNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.DANMU_ECENT) {
            if (null != event.getObj()) {
                try {
                    String content = event.getObj().toString();
                    ObserverBean.DataBeanX observerBean = GsonUtil.getBean(content, ObserverBean.DataBeanX.class);
                    //调用H5方法
//                if(observerBean.getNewsType() == 2100) {
//                    observerBean.setNewsType(2100);
                    mAgentWeb.getJsAccessEntrace().quickCallJs("javaCallJs", content);
//                }
                } catch (Exception e) {
                }
            }
        } else if (event.getTag() == Constant.EventTag.SHOW_SHARE) {
            webMoreClick();
        }else if (event.getTag() == Constant.EventTag.SHOW_SHARE_H5) {//移除会话
            String json = event.getObj().toString();
            try{
                ShowShareBean bean = GsonUtil.getBean(json, ShowShareBean.class);
                showShare(bean);
            }catch (Exception e){
            }

        }else if(event.getTag() == Constant.EventTag.ERROR_WEB_JSOUP){
            canPureClick = true;
            if(null != pureReadItemAdapter) {
                pureReadItemAdapter.setEnableLoadMore(false);
            }
        }else if (event.getTag() == Constant.EventTag.WX_PAY_REFRESH_WEB) {
            //刷新
            try{
                if(null != mAgentWeb){
                    mAgentWeb.getUrlLoader().reload();
                }
            }catch (Exception e){
            }

        }
    }


    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl(int shareMode) {
        Map<String, String> map = new HashMap<>();
        if (gameId == 1017) {
            //针对书荒神器的特殊处理
            String result = "";
            try {
                HashMap<String, String> map2 = executeParse(openUrl);
                result = map2.get("keyword");
            } catch (Exception e) {
            }
            if (!TextUtils.isEmpty(result)) {
                map.put("ext", result);
            }
        }

        if("1019".equals(shareCodeFormServer)){
            //针对共享版权详情的特殊处理
            String result = "";
            try {
                HashMap<String, String> map2 = executeParse(openUrl);
                result = map2.get("id");
            } catch (Exception e) {
            }
            if (!TextUtils.isEmpty(result)) {
                map.put("objId", result);
            }
        }

        map.put("shareCodeFormServer", shareCodeFormServer);
        map.put("shareMode", String.valueOf(shareMode));

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/common/share/getCommonShareUrl", map, new CallBackUtil.CallBackString() {
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
                    GetCommonShareUrlBean baseBean = GsonUtil.getBean(response, GetCommonShareUrlBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {

                        switch (shareMode) {
                            case 1:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else {
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
                                    RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else {
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
                                RxShareUtils.QQShareUrl(ActivityWebView.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityWebView.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityWebView.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal(), true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(ActivityWebView.this, baseBean.getData().getOneUrlVal());
                                RxToast.custom("复制成功").show();
                                break;

                            case 7:
                                //浏览器打开
                                startBrowserActivity(baseBean.getData().getOneUrlVal());
                                break;

                            default:
                                break;
                        }
                    } else {
                        RxToast.custom(baseBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    RxLogTool.e("requestUnConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }


    private void enterPureMode(String url, boolean enter , boolean click) {
        //这里需要放在子线程中完成，否则报这个错android.os.NetworkOnMainThreadException
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).timeout(30000).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:22.0) Gecko/20100101 Firefox/22.0").validateTLSCertificates(false).get();
                    String content = doc.html();
                    RxLogTool.e(content);
                    requestReadability(content, url, enter,click);
                } catch (IOException e) {
                    RxEventBusTool.sendEvents(Constant.EventTag.ERROR_WEB_JSOUP);
                }
            }
        }).start();

    }


    /*请求纯净模式的内容*/
    public void requestReadability(String content, String url, boolean enter , boolean click) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("url", url);
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/book/read/readability", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    canPureClick = true;
                    if(click) {
//                        RxToast.custom("加载失败/book/read/readability").show();
                    }
                    if(null != pureReadItemAdapter) {
                        pureReadItemAdapter.setEnableLoadMore(false);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    canPureClick = true;
                    ReadabilityBean readabilityBean = GsonUtil.getBean(response, ReadabilityBean.class);
                    if (readabilityBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE ) {
                        if (enter) {
                            //显示出纯净模式的界面，并且开始做上一页下一页逻辑
                            pure_view.setVisibility(View.VISIBLE);
                            iv_pureread.setImageResource(R.mipmap.purereadmode_open);
                            initAdapter();
                            initReadSetting();
                            pureListData.clear();
                            RxToast.custom("您已进入无广告阅读模式").show();
                        }
                        //增加数据，显示到末尾
                        pureListData.add(readabilityBean);
                        pureReadItemAdapter.replaceData(pureListData);
                        pureReadItemAdapter.loadMoreComplete();
                        if (null == readabilityBean.getData()) {
                            pureReadItemAdapter.setEnableLoadMore(false);
                        } else if (null == readabilityBean.getData().getUrls()) {
                            pureReadItemAdapter.setEnableLoadMore(false);
                        } else if (TextUtils.isEmpty(readabilityBean.getData().getUrls().getNext())) {
                            pureReadItemAdapter.setEnableLoadMore(false);
                        } else {
                            pureReadItemAdapter.setEnableLoadMore(true);
                        }
                        //插入网页历史记录
                        addHistory(readabilityBean.getData().getTitle(), url);
                    } else if (readabilityBean.getCode() == Constant.ResponseCodeStatus.DISABLE_PURE) {
                        iv_pureread.setVisibility(View.INVISIBLE);
                    }else {
                        if(click) {
//                            RxToast.custom("加载失败/book/read/readability").show();
                        }
                        if(null != pureReadItemAdapter) {
                            pureReadItemAdapter.setEnableLoadMore(false);
                        }
                    }
                } catch (Exception e) {
                    if(click) {
//                        RxToast.custom("加载失败/book/read/readability").show();
                    }
                    if(null != pureReadItemAdapter) {
                        pureReadItemAdapter.setEnableLoadMore(false);
                    }
                }
            }
        });
    }

    private void addHistory(String title, String url) {
        if (url.contains("lianzai.com") || url.contains("bendixing.net")) {
            //假如是内站链接则过滤
             return;
        }
        //插入网页历史记录
        WebHistoryBean webHistoryBean = new WebHistoryBean();
        webHistoryBean.setTitle(title);
        webHistoryBean.setUrl(url);
        if (RxLoginTool.isLogin()) {
            try {
                String uid = String.valueOf(RxLoginTool.getLoginAccountToken().getData().getUid());
                webHistoryBean.setUserId(uid);
            } catch (Exception e) {
                webHistoryBean.setUserId("0");
            }
        } else {
            webHistoryBean.setUserId("0");
        }
        webHistoryBean.setCreattime(System.currentTimeMillis());
        WebHistoryRepository.getInstance().addWebHistory(webHistoryBean);
    }

    private void initReadSetting() {
        //设置当前Activity的Brightness
        ReadSettingManager mSettingManager = ReadSettingManager.getInstance();
        if (mSettingManager.isBrightnessAuto()) {
            BrightnessUtils.setDefaultBrightness(this);
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }
        //设置字体大小
        if (mSettingManager.isDefaultTextSize()) {
            pureReadItemAdapter.setTextSize(18);
        } else {
            int fontSize = mSettingManager.getTextSize();
            pureReadItemAdapter.setTextSize(ScreenUtils.pxToDp(fontSize));
        }

        //设置背景
        if (mSettingManager.isNightMode()) {
            PageStyle mPageStyle = PageStyle.NIGHT;
            pureReadItemAdapter.setPageStyleChecked(mPageStyle);

//            //设置背景
            int mBgColor = ContextCompat.getColor(this, mPageStyle.getBgColor());
            pure_view.setBackgroundColor(mBgColor);
//            if (mPageStyle.getBgColor() == R.color.nb_read_bg_8) {
//                pure_view.setBackgroundResource(R.drawable.bg1);
//            }

        } else {
            PageStyle mPageStyle = mSettingManager.getPageStyle();
            pureReadItemAdapter.setPageStyleChecked(mPageStyle);

            //设置背景
            int mBgColor = ContextCompat.getColor(this, mPageStyle.getBgColor());
            pure_view.setBackgroundColor(mBgColor);
            if (mPageStyle.getBgColor() == R.color.nb_read_bg_8) {
                pure_view.setBackgroundResource(R.drawable.bg1);
            }
        }
    }

    private void initAdapter() {
        if (null == pureReadItemAdapter) {
            pureReadItemAdapter = new PureReadItemAdapter(pureListData);
            pureReadItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    try {
                        if (null != pureListData && !pureListData.isEmpty()) {
                            String nextUrl = pureListData.get(pureListData.size() - 1).getData().getUrls().getNext();
                            enterPureMode(nextUrl, false,false);
                        }
                    } catch (Exception e) {

                    }
                }
            }, pure_view);
            customLoadMoreView = new CustomLoadMoreView();
            pureReadItemAdapter.setLoadMoreView(customLoadMoreView);

            pureReadItemAdapter.setContentClickListener(new PureReadItemAdapter.ContentClickListener() {
                @Override
                public void titleClick(int position, int x, int y) {
                    int hight = RxDeviceTool.getScreenHeight(ActivityWebView.this);

                    int actionBarHight = 0;
                    if(action_bar.getVisibility() == View.VISIBLE){
                        actionBarHight = action_bar.getHeight();
                    }

                    int oneHight = (hight - actionBarHight)/4;
                    if (y < actionBarHight + oneHight) {
                        //往上翻页
                        pure_view.smoothScrollBy(0, -(hight - actionBarHight));
                    } else if (y < actionBarHight + 3*oneHight) {
                        //弹出菜单
                        action_bar.setVisibility(View.VISIBLE);
                        SystemBarUtils.readStatusBar3(ActivityWebView.this);
                    } else {
                        //往下翻页
                        pure_view.smoothScrollBy(0, hight - actionBarHight);
                    }
                }

                @Override
                public void contentClick(int position, int x, int y) {
                    int hight = RxDeviceTool.getScreenHeight(ActivityWebView.this);

                    int actionBarHight = 0;
                    if(action_bar.getVisibility() == View.VISIBLE){
                        actionBarHight = action_bar.getHeight();
                    }

                    int oneHight = (hight - actionBarHight)/4;
                    if (y < actionBarHight + oneHight) {
                        //往上翻页
                        pure_view.smoothScrollBy(0, -(hight - actionBarHight));
                    } else if (y < actionBarHight + 3*oneHight) {
                        //弹出菜单
                        action_bar.setVisibility(View.VISIBLE);
                        SystemBarUtils.readStatusBar3(ActivityWebView.this);
                    } else {
                        //往下翻页
                        pure_view.smoothScrollBy(0, hight - actionBarHight);
                    }
                }
            });

            linearLayoutManager = new RxLinearLayoutManager(this);
            pure_view.setLayoutManager(linearLayoutManager);
            //设置添加,移除item的动画,DefaultItemAnimator为默认的
            pure_view.setItemAnimator(new DefaultItemAnimator());
            pure_view.setAdapter(pureReadItemAdapter);
        }
    }


    private void showShare(ShowShareBean bean){
        rxDialogWebShare.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    getCommonShareUrlForShowShare(1,bean);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    getCommonShareUrlForShowShare(3,bean);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    getCommonShareUrlForShowShare(2,bean);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    getCommonShareUrlForShowShare(5,bean);
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.getTv_share_copy().setOnClickListener(
                v -> {
                    getCommonShareUrlForShowShare(6,bean);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_refresh().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.GONE);

        rxDialogWebShare.show();
    }


    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrlForShowShare(int shareMode,ShowShareBean bean) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(bean.getCode())) {
            map.put("shareCodeFormServer", bean.getCode());
        }
        if (!TextUtils.isEmpty(bean.getObjId())) {
            map.put("objId", bean.getObjId());
        }
        if (!TextUtils.isEmpty(bean.getExt())) {
            map.put("ext", bean.getExt());
        }
        map.put("shareMode", String.valueOf(shareMode));



        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/common/share/getCommonShareUrl", map, new CallBackUtil.CallBackString() {
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
                    GetCommonShareUrlBean baseBean = GsonUtil.getBean(response, GetCommonShareUrlBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {

                        switch (shareMode) {
                            case 1:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else {
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
                                    RxToast.custom("请先更新微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else {
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
                                RxShareUtils.QQShareUrl(ActivityWebView.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityWebView.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityWebView.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal(), true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(ActivityWebView.this, baseBean.getData().getOneUrlVal());
                                RxToast.custom("复制成功").show();
                                break;

                            case 7:
                                //浏览器打开
                                startBrowserActivity(baseBean.getData().getOneUrlVal());
                                break;

                            default:
                                break;
                        }
                    } else {
                        RxToast.custom(baseBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    RxLogTool.e("requestUnConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }


}
