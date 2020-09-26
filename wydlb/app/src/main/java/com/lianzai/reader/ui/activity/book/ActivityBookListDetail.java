package com.lianzai.reader.ui.activity.book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookListDetailHeaderResponse;
import com.lianzai.reader.bean.BookListDetailResponse;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.MyBookListBean;
import com.lianzai.reader.model.local.MyBookListRepository;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBookList;
import com.lianzai.reader.ui.adapter.BookListDetailAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CenteredImageSpan;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogCheckBox;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWebShare;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
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

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 书单详情
 */

public class ActivityBookListDetail extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BookListDetailAdapter.BookListDetailItemClickListener {


    TextView rm_tv_description;

    @Bind(R.id.rl_actionbar)
    RelativeLayout rl_actionbar;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Bind(R.id.iv_share)
    ImageView iv_share;

    @Bind(R.id.tv_common_title)
    TextView tv_common_title;


    @Bind(R.id.img_back)
    ImageView img_back;


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    CustomLoadMoreView customLoadMoreView;

    List<BookListDetailResponse.DataBean.ListBean> dataSource = new ArrayList<>();


    View headerView;

    TextView tv_collection;//收藏


    RxLinearLayoutManager manager;

    BookListDetailAdapter bookListDetailAdapter;


    //头部视图
    SelectableRoundedImageView iv_book_list_cover;

    RelativeLayout rl_header;

    Drawable moreDrawable;

    String optionStr;

    LinearLayout ll_book_list_tags;//书单标签

    Bitmap blurBitmap;//头部高斯模糊背景


    TextView tv_book_count;
    TextView tv_creator_name;
    CircleImageView civ_logo;
    TextView tv_book_list_name;


    int pageNum = 1;
    int pageSize = 10;

    String bookListId;

    BookListDetailResponse.DataBean dataBean;

    BookListDetailHeaderResponse.DataBean headerDataBean;

    boolean isExpand = false;//内容是否展开

    //分享弹窗
    RxDialogWebShare rxDialogWebShare;

    //提示框，包含chkeckbox
    RxDialogCheckBox rxDialogSureCancel;

    RxDialogSureCancelNew rxDialogSureCancelNewCancelCollection;

    RxDialogGoLogin rxDialogGoLogin;//去登录弹窗

    ImageView iv_blur_img;

    private WbShareHandler shareHandler;
    private AccountDetailBean accountDetailBean;
    private String bqtKey;
    private String uid;

    public static void startActivity(Context context, String bookListId, String optionStr) {
        Bundle bundle = new Bundle();
        bundle.putString("bookListId", bookListId);
        bundle.putString("optionStr", optionStr);
        RxActivityTool.skipActivity(context, ActivityBookListDetail.class, bundle);
    }

    public static void startActivity(Context context, String bookListId) {
        Bundle bundle = new Bundle();
        bundle.putString("bookListId", bookListId);
        RxActivityTool.skipActivity(context, ActivityBookListDetail.class, bundle);
    }


    @Override
    public int getLayoutId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.activity_book_list_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    long mClickTime = 0;

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);

        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }

        optionStr = getIntent().getExtras().getString("optionStr");
        bookListId = getIntent().getExtras().getString("bookListId");

        if (TextUtils.isEmpty(bookListId)) return;

        //common code
        accountDetailBean = RxTool.getAccountBean();
        if(null != accountDetailBean) {
            uid = accountDetailBean.getData().getUid();
            bqtKey = uid + Constant.BQT_KEY;
        }

        //书单分享样式
        rxDialogWebShare = new RxDialogWebShare(this, R.style.BottomDialogStyle);
        rxDialogWebShare.getTv_share_refresh().setText("连载口令");
        rxDialogWebShare.getTv_share_refresh().setCompoundDrawables(null,RxImageTool.getDrawable(R.mipmap.icon_lianzaikouling),null,null);
        rxDialogWebShare.getTv_share_ftf().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.GONE);

        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        initView();


        tv_common_title.setOnClickListener(
                v -> {//双击

                    if (System.currentTimeMillis() - mClickTime < 800) {
                        //此处做双击具体业务逻辑
                        recyclerView.scrollToPosition(0);
                    } else {
                        mClickTime = System.currentTimeMillis();
                        //表示单击，此处也可以做单击的操作
                    }
                }
        );


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int distance = getScrolledDistance();
                if (distance <= 0) {
                    rl_actionbar.setBackgroundColor(Color.TRANSPARENT);
                    SystemBarUtils.transparentStatusBar(ActivityBookListDetail.this);
                    mSwipeRefreshLayout.setEnabled(true);
                    tv_common_title.setTextColor(Color.TRANSPARENT);

                    iv_share.setImageResource(R.mipmap.v2_icon_share_white);
                    img_back.setImageResource(R.mipmap.v2_icon_back);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookListDetail.this);
                        tintManager.setStatusBarTintEnabled(true);
                        tintManager.setNavigationBarTintEnabled(true);
                        // 自定义颜色
                        tintManager.setTintColor(getResources().getColor(R.color.black));
                    }

                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                    if (distance < RxImageTool.dip2px(45)) {
                        int progress = (int) (new Float(distance) / new Float(RxImageTool.dip2px(45)) * 255);//255
                        rl_actionbar.setBackgroundColor(Color.argb(progress, 72, 92, 248));
                        tv_common_title.setTextColor(Color.argb(progress, 255, 255, 255));
                        SystemBarUtils.setStatusBarColor(ActivityBookListDetail.this, Color.argb(progress, 72, 92, 248));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookListDetail.this);
                            tintManager.setStatusBarTintEnabled(true);
                            tintManager.setNavigationBarTintEnabled(true);
                            // 自定义颜色
                            tintManager.setTintColor(Color.argb(progress, 72, 92, 248));
                        }
                    } else {
                        iv_share.setImageResource(R.mipmap.v2_icon_share_white);
                        img_back.setImageResource(R.mipmap.v2_icon_back);
                        rl_actionbar.setBackgroundColor(getResources().getColor(R.color.v2_blue_color));
                        SystemBarUtils.setStatusBarColor(ActivityBookListDetail.this, getResources().getColor(R.color.v2_blue_color));
                        tv_common_title.setTextColor(getResources().getColor(R.color.white));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookListDetail.this);
                            tintManager.setStatusBarTintEnabled(true);
                            tintManager.setNavigationBarTintEnabled(true);
                            // 自定义颜色
                            tintManager.setTintColor(getResources().getColor(R.color.v2_blue_color));
                        }
                    }
                }
            }
        });

        showDialog();

        initRequest();
    }

    private void initView() {
        int statusBarHeight=ScreenUtil.getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.action_bar_height));
        layoutParams.setMargins(0,statusBarHeight,0,0);
        rl_actionbar.setLayoutParams(layoutParams);

        headerView = getLayoutInflater().inflate(R.layout.header_book_list_detail, null);
        headerView.setVisibility(View.INVISIBLE);
        tv_collection = headerView.findViewById(R.id.tv_collection);

        rl_header = headerView.findViewById(R.id.rl_header);
        iv_blur_img=headerView.findViewById(R.id.iv_blur_img);

        tv_book_count = headerView.findViewById(R.id.tv_book_count);
        tv_creator_name = headerView.findViewById(R.id.tv_creator_name);
        civ_logo = headerView.findViewById(R.id.civ_logo);

        tv_book_list_name = headerView.findViewById(R.id.tv_book_list_name);

        moreDrawable = RxImageTool.getDrawable(19, 13, R.mipmap.icon_text_more);

        iv_book_list_cover = headerView.findViewById(R.id.iv_book_list_cover);
        rm_tv_description = headerView.findViewById(R.id.rm_tv_description);
        ll_book_list_tags = headerView.findViewById(R.id.ll_book_list_tags);

        bookListDetailAdapter = new BookListDetailAdapter(dataSource, this);
        bookListDetailAdapter.setBookListDetailItemClickListener(this);
        bookListDetailAdapter.addHeaderView(headerView, 0);


        customLoadMoreView = new CustomLoadMoreView();
        bookListDetailAdapter.setLoadMoreView(customLoadMoreView);
        bookListDetailAdapter.setOnLoadMoreListener(this, recyclerView);


        manager = new RxLinearLayoutManager(ActivityBookListDetail.this);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new RxRecyclerViewDividerTool(0,0,0,RxImageTool.dp2px(5),true));

//        bookListDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });

        recyclerView.setAdapter(bookListDetailAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

    }


    private void initData() {
        if (null == headerDataBean) return;
        headerView.setVisibility(View.VISIBLE);

        tv_creator_name.setText(headerDataBean.getBooklistAuthor());//书单创建者

        tv_book_list_name.setText(headerDataBean.getBooklistName());//书单名称

        RxImageTool.loadFangLogoImage(this, headerDataBean.getAuthorCover(), civ_logo);//创建者的头像

        RxTool.stringFormat(tv_book_count, R.string.book_list_book_count, headerDataBean.getTotalNum(), this);


        if (headerDataBean.isIsCollection()){
            tv_collection.setBackgroundColor(Color.parseColor("#FFEFF1F5"));
            tv_collection.setTextColor(Color.parseColor("#FF535353"));
            tv_collection.setCompoundDrawables(RxImageTool.getDrawable(22,R.mipmap.icon_book_list_collectioned),null,null,null);

            if (headerDataBean.getCollectionNum() > 0) {
                if (headerDataBean.isCollectionNumShow()){
                    tv_collection.setText("已收藏(" + headerDataBean.getCollectionNum() + ")");
                }else{
                    tv_collection.setText("已收藏");
                }

            } else {
                tv_collection.setText("收藏");
            }
        }else{
            tv_collection.setBackgroundColor(getResources().getColor(R.color.v2_blue_color));
            tv_collection.setCompoundDrawables(RxImageTool.getDrawable(12,R.mipmap.icon_add_white_big),null,null,null);
            tv_collection.setTextColor(getResources().getColor(R.color.white));

            if (headerDataBean.getCollectionNum() > 0) {
                if (headerDataBean.isCollectionNumShow()){
                    tv_collection.setText("收藏(" + headerDataBean.getCollectionNum() + ")");
                }else{
                    tv_collection.setText("收藏");
                }

            } else {
                tv_collection.setText("收藏");
            }
        }

        tv_collection.setOnClickListener(
                v -> {
                    if (RxLoginTool.isLogin()) {//收藏操作
                        if (headerDataBean.isIsCollection()) {
                            showCancelCollectionDialog();
                        } else {
                            showDialog();
                            bookListCollectionRequest();//添加收藏
                        }

                    } else {
                        //跳转至登录界面
                        //缓存操作内容
//                        RxTool.saveBeforeLoginOption(Constant.BeforeLoginOption.Collection_Book_List, "10");
                        showLoginDialog();

                    }
                }
        );

        tv_common_title.setText(headerDataBean.getBooklistName());

        int errorImgId=RxImageTool.getNoCoverImgByRandom();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.placeholder(errorImgId);
        requestOptions.error(errorImgId);
        requestOptions.skipMemoryCache(false);
        requestOptions.dontAnimate();

        Glide.with(this).load(headerDataBean.getCover()).apply(requestOptions).listener(new RequestListener<Drawable>() {
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
        }).into(iv_book_list_cover);


        SpannableString ss = new SpannableString(headerDataBean.getBooklistIntro());
        RxLogTool.e("word total count:" + ss.toString().length());
        showBookListDescription(ss);

        //显示书单标签
        List<String> tags = new ArrayList<>();

        if (null != headerDataBean.getTag()) {
            for (BookListDetailHeaderResponse.DataBean.TagBean tagBean : headerDataBean.getTag()) {
                tags.add(tagBean.getName());
            }
        }
        showBookListTags(tags);
    }


    /**
     * 动态添加标签
     *
     * @param tags
     */
    private void showBookListTags(List<String> tags) {
        if (null == tags) return;
        ll_book_list_tags.removeAllViews();//清除所有Tag
        for (String tag : tags) {
            TextView textView = new TextView(this);
            textView.setPadding(RxImageTool.dip2px(10), RxImageTool.dip2px(3), RxImageTool.dip2px(10), RxImageTool.dip2px(3));

            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.label_corner_bg);
            textView.setTextSize(12);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, RxImageTool.dip2px(8), 0);
            textView.setLayoutParams(layoutParams);

            textView.setText(tag);
            ll_book_list_tags.addView(textView);
        }
    }

    /**
     * 显示书单简介，超过三行末尾显示更多图标，点击展开
     */
    private void showBookListDescription(SpannableString ss) {
        if (null == ss) return;
//        rm_tv_description.setText(ss);

        int contentWidth = ScreenUtil.getDisplayWidth() - RxImageTool.dip2px(20);//显示内容的宽度

        int maxWordCount = getThreeLineStringCount(contentWidth, ss.toString(), rm_tv_description.getPaint());

        if (!isExpand && maxWordCount > 0) {//截取字符串
            SpannableString shortSs = new SpannableString(ss.subSequence(0, maxWordCount - 1));
            int len = shortSs.length();

            CenteredImageSpan span = new CenteredImageSpan(moreDrawable);
            shortSs.setSpan(span, len - 1, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    rm_tv_description.setText(ss);//显示原始的字符串
                    isExpand = true;//已经展开
                }
            };
            shortSs.setSpan(clickableSpan, len - 1, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            rm_tv_description.setMovementMethod(LinkMovementMethod.getInstance());
            rm_tv_description.setText(shortSs);
        } else {//未超过三行
            rm_tv_description.setText(ss);
        }
    }

    //获取显示三行需要多少个字
    private int getThreeLineStringCount(int maxLineWidth, String str, Paint paint) {
        int width = 0;
        int maxCount = 0;//三行最多显示的字符数
        RxLogTool.e("maxLineWidth:" + maxLineWidth);
        int lineCount=0;//换行符个数
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).equals("\n")){//换行符
                lineCount++;
                RxLogTool.e("有换行符:"+lineCount);
            }
            width += paint.measureText(String.valueOf(str.charAt(i)));
            if (3 * maxLineWidth <= width) {//超过三行最多显示了多少个字
                RxLogTool.e("word count:" + i);
                maxCount = i;
                break;
            }
            if (lineCount>3){//有三个换行符
                RxLogTool.e("word count:"+i);
                maxCount=i;
                break;
            }

//            RxLogTool.e(i+"--width:"+width);
        }
        return maxCount;
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float radius = 40;//模糊程度

        Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
        view.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        RxLogTool.e("blur time:" + (System.currentTimeMillis() - startMs));
    }

    /**
     * @description 获取垂直方向滑动的距离
     * @author qicheng.qing
     * @create 17/3/3 14:22
     */
    private int getScrolledDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibleItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }


    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
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

    @Override
    public void onRefresh() {
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showSwipeRefresh(true);
                initRequest();
                bookListDetailAdapter.setEnableLoadMore(true);

            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }

    private void initRequest() {
        pageNum = 1;
        bookListDetailRequest();

        //书单头部详情数据
        bookListHeaderDetailRequest();
    }

    @Override
    public void onLoadMoreRequested() {
        pageNum++;
        bookListDetailRequest();
    }

    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }

    @Override
    public void addBookClick(int position) {
        if(position < 0 || position >=  bookListDetailAdapter.getData().size()){
            return;
        }

        if (RxLoginTool.isLogin()) {//添加书操作
            if (bookListDetailAdapter.getData().get(position).isConcern()) {
                exit(position, bookListDetailAdapter.getData().get(position));
            } else {
                requestConcernPlatform(position, bookListDetailAdapter.getData().get(position));
            }
        } else {
            //跳转至登录界面
            //缓存操作内容
//            RxTool.saveBeforeLoginOption(Constant.BeforeLoginOption.Add_Book, "10");

            showLoginDialog();
        }
    }

    //退出圈子流程
    private void exit(int pos ,BookListDetailResponse.DataBean.ListBean bookBean){
        if(null == rxDialogSureCancel){
            rxDialogSureCancel=new RxDialogCheckBox(this,R.style.OptionDialogStyle);
        }
        rxDialogSureCancel.getTitleView().setText("确定退出圈子？");
        rxDialogSureCancel.getContentView().setText("退出圈子后，书友贡献的最新章节链接和精彩动态便接收不到了");
        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
        rxDialogSureCancel.getSureView().setText("确定退出");
        rxDialogSureCancel.getCancelView().setText("取消");
        rxDialogSureCancel.getCheckbox_ly().setVisibility(View.VISIBLE);
        rxDialogSureCancel.getCb_nomore_tip().setChecked(false);
        rxDialogSureCancel.getTv_checkbox().setText("同时将书籍移出书架");

        //红色按钮
        rxDialogSureCancel.getSureView().setBackgroundResource(R.drawable.shape_red_leftbottomyuanjiao);
        rxDialogSureCancel.getSureView().setOnClickListener(
                v -> {
                    requestUnConcernPlatform(pos,bookBean,rxDialogSureCancel.getCb_nomore_tip().isChecked());
                    rxDialogSureCancel.dismiss();
                }
        );
        rxDialogSureCancel.show();
    }


    private void bookListDetailRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("booklistId", bookListId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/detail/list", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/booklist/detail/list").show();
                    bookListDetailAdapter.loadMoreFail();
                    showSwipeRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showSwipeRefresh(false);
                    BookListDetailResponse bookListDetailResponse = GsonUtil.getBean(response, BookListDetailResponse.class);
                    if (bookListDetailResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        dataBean = bookListDetailResponse.getData();

                        if(pageNum == 1){
                            dataSource.clear();
                        }

                        if(null != dataBean && null != dataBean.getList()){
                            dataSource.addAll(dataBean.getList());
                        }

                        bookListDetailAdapter.replaceData(dataSource);


                        //是否允许加载更多
                        if (null != dataBean && null != dataBean.getList() && !dataBean.getList().isEmpty()) {//最后一页
                            bookListDetailAdapter.loadMoreComplete();
                        } else {
                            bookListDetailAdapter.loadMoreEnd();
                        }

                    } else {//加载失败
                        bookListDetailAdapter.loadMoreFail();
                        RxToast.custom(bookListDetailResponse.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 书单头部详情数据请求
     */
    private void bookListHeaderDetailRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("booklistId", bookListId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/detail", map, new CallBackUtil.CallBackString() {
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
                    BookListDetailHeaderResponse bookListDetailHeaderResponse = GsonUtil.getBean(response, BookListDetailHeaderResponse.class);
                    if (bookListDetailHeaderResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        headerDataBean = bookListDetailHeaderResponse.getData();

                        //刷新数据
                        initData();
                    } else {
                        RxToast.custom(bookListDetailHeaderResponse.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 添加书籍
     */
    private void requestConcernPlatform(int pos, BookListDetailResponse.DataBean.ListBean bookBean) {

        if (TextUtils.isEmpty(bookBean.getPlatformId())){
            RxToast.custom(getResources().getString(R.string.no_platform_id_title),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/"+bookBean.getPlatformId()+"/join", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
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
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("已成功关注圈子", Constant.ToastType.TOAST_SUCCESS).show();
                        bookBean.setConcern(true);
                        bookListDetailAdapter.setData(pos, bookBean);
                        bookListDetailAdapter.notifyDataSetChanged();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 移除书籍
     */
    private void requestUnConcernPlatform(int pos, BookListDetailResponse.DataBean.ListBean bookBean,boolean check) {
        if (TextUtils.isEmpty(bookBean.getPlatformId())){
            RxToast.custom(getResources().getString(R.string.no_platform_id_title),Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("isUnConcern", String.valueOf(check));

        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/circles/"+ bookBean.getPlatformId() +"/exist", map ,new CallBackUtil.CallBackString() {
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
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxEventBusTool.sendEvents(bookBean.getPlatformId(),Constant.EventTag.REMOVE_RECENT_CONTACT_BY_BOOK_ID_TAG);

                        bookBean.setConcern(false);
                        bookListDetailAdapter.setData(pos, bookBean);
                        bookListDetailAdapter.notifyDataSetChanged();
                        RxToast.custom("已成功取关圈子", Constant.ToastType.TOAST_SUCCESS).show();

                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 书单收藏
     */
    private void bookListCollectionRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("booklistId", bookListId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/booklist/collection", map, new CallBackUtil.CallBackString() {
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
                    dismissDialog();
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("书单收藏成功", Constant.ToastType.TOAST_SUCCESS).show();

                        if (null != headerDataBean) {
                            headerDataBean.setIsCollection(true);
                            headerDataBean.setCollectionNum(headerDataBean.getCollectionNum() + 1);
                            initData();

                            MyBookListBean myBookListBean=new MyBookListBean();
                            myBookListBean.setBooklistAuthor(headerDataBean.getBooklistAuthor());
                            myBookListBean.setBooklistIntro(headerDataBean.getBooklistIntro());
                            myBookListBean.setBooklistName(headerDataBean.getBooklistName());
                            myBookListBean.setUid(Integer.parseInt(uid));
                            myBookListBean.setCover(headerDataBean.getCover());
                            myBookListBean.setId(headerDataBean.getId());
                            MyBookListRepository.getInstance().saveBookList(myBookListBean);
                            //本地保存书单

                            //刷新书单列表
                            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_BOOK_LIST_TAG);
                        }


                    } else {
                        RxToast.custom(baseBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 书单取消收藏
     */
    private void bookListCollectionCancelRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("booklistId", bookListId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/booklist/collection/cancel", map, new CallBackUtil.CallBackString() {
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
                        RxToast.custom("书单已取消收藏", Constant.ToastType.TOAST_SUCCESS).show();

                        if (null != headerDataBean) {
                            headerDataBean.setIsCollection(false);
                            //取消收藏-数量不会减少
//                            headerDataBean.setCollectionNum(headerDataBean.getCollectionNum() > 0 ? (headerDataBean.getCollectionNum() - 1) : 0);
                            initData();


                            //本地移除
                            MyBookListRepository.getInstance().deleteByBookListIdAndUid(Integer.parseInt(uid),String.valueOf(headerDataBean.getId()));

                            //刷新书单列表
                            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_BOOK_STORE_BOOK_LIST_TAG);
                        }
                    } else {
                        RxToast.custom(baseBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 收藏取消弹框确认
     */
    private void showCancelCollectionDialog(){
        if (null==rxDialogSureCancelNewCancelCollection){
            rxDialogSureCancelNewCancelCollection=new RxDialogSureCancelNew(this,R.style.OptionDialogStyle);
            rxDialogSureCancelNewCancelCollection.setButtonText("取消收藏","点错了");
            rxDialogSureCancelNewCancelCollection.setContent("是否取消收藏该书单?");
        }
        rxDialogSureCancelNewCancelCollection.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNewCancelCollection.dismiss();
                showDialog();
                bookListCollectionCancelRequest();
            }
        });
        rxDialogSureCancelNewCancelCollection.show();
    }

    @Override
    public void openBookClick(int position) {
        if(position < 0 || position >=  bookListDetailAdapter.getData().size()){
            return;
        }
        BookListDetailResponse.DataBean.ListBean bean = bookListDetailAdapter.getData().get(position);
        if (TextUtils.isEmpty(bean.getPlatformId())){
            RxToast.custom(getResources().getString(R.string.no_platform_id_title),Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        //进入圈子
        ActivityCircleDetail.startActivity(this,bean.getPlatformId());
    }

    @OnClick(R.id.iv_share)
    void shareClick() {
        showShareBarDialog();
//        if (RxLoginTool.isLogin()&&null!=headerDataBean) {
//            showShareBarDialog();
//        } else if(null!=headerDataBean){
////            RxTool.saveBeforeLoginOption(Constant.BeforeLoginOption.Add_Book, "10");
//            //增加游客分享
//            showShareBarDialog();
////            showLoginDialog();
//        }else {
//            showLoginDialog();
//        }
    }

    /**
     * 分享bar详情框
     */
    private void showShareBarDialog() {
        rxDialogWebShare.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    getCommonShareUrl(1);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    getCommonShareUrl(3);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    getCommonShareUrl(2);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    getCommonShareUrl(5);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_copy().setOnClickListener(
                v -> {
                    //复制链接
                    getCommonShareUrl(6);
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_refresh().setOnClickListener(
                v -> {
                    //连载口令
                    ActivityShareBitmapBookList.startActivity(ActivityBookListDetail.this,bookListId);
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.show();
    }


    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl(int shareMode){

        Map<String,String> map=new HashMap<>();
        map.put("code", "1001");
        map.put("shareMode", String.valueOf(shareMode));
        map.put("objId", bookListId);

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
                                RxShareUtils.QQShareUrl(ActivityBookListDetail.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(ActivityBookListDetail.this, baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, ActivityBookListDetail.this, true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                            case 6:
                                //复制链接
                                RxClipboardTool.copyText(ActivityBookListDetail.this,baseBean.getData().getOneUrlVal());
                                RxToast.custom("复制成功").show();
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


    private void showLoginDialog(){
        if (null==rxDialogGoLogin){
            rxDialogGoLogin=new RxDialogGoLogin(this,R.style.OptionDialogStyle);
        }
        rxDialogGoLogin.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if(event.getTag()==Constant.EventTag.LOGIN_REFRESH_TAG){//登录刷新
            onRefresh();
        }else if(event.getTag()==Constant.EventTag.LOGIN_OUT_REFRESH_TAG){//退出登录成功
            onRefresh();
        }
    }

    //同步书架数据
//    private void requestBookStore() {
//        ArrayMap map = new ArrayMap();
//        long timestamp = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
//        if (timestamp > 0) {//
//            map.put("timestamp", String.valueOf(timestamp));
//        }
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/book/shelf/refresh", map, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    BookStoreResponse bookStoreResponse = GsonUtil.getBean(response, BookStoreResponse.class);
//                    if (bookStoreResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                        List<BookStoreBean> bookStoreBeanList = new ArrayList<>();
//                        //需要删除的书籍
//                        if (null != bookStoreResponse.getData().getDelete_List() && bookStoreResponse.getData().getDelete_List().size() > 0) {
//                            for (BookStoreBean bookStoreBean : bookStoreResponse.getData().getDelete_List()) {//本地移除该书
//                                if(!TextUtils.isEmpty(bookStoreBean.getBookId())){
//                                    //移除该书,旧书模式
//                                    BookStoreRepository.getInstance().deleteAllByPlatformIdAndUid(Integer.parseInt(uid), bookStoreBean.getPlatformId());
//                                }
//                            }
//                        }
//                        //需要删除的书籍
//                        if (null != bookStoreResponse.getData().getRecognitionDeleteList() && bookStoreResponse.getData().getRecognitionDeleteList().size() > 0) {
//                            for (BookStoreBean bookStoreBean : bookStoreResponse.getData().getRecognitionDeleteList()) {//本地移除该书
//                                if(!TextUtils.isEmpty(bookStoreBean.getBookId())){
//                                    //移除该书,新url模式
//                                    BookStoreRepository.getInstance().deleteAllByBookIdAndUid(Integer.parseInt(uid), bookStoreBean.getBookId());
//                                }
//                            }
//                        }
//                        //给每本书指定用户ID
//                        int i = 0;
//                        long time = System.currentTimeMillis();
//                        for (BookStoreBean bookStoreBean : bookStoreResponse.getData().getRecognitionList()) {
//                            i++;
//                            bookStoreBean.setUid(Integer.parseInt(uid));
//                            bookStoreBean.setUpdateDate(time - i);
//                            bookStoreBeanList.add(bookStoreBean);
//                        }
//                        //给每本书指定用户ID
//                        for (BookStoreBean bookStoreBean : bookStoreResponse.getData().getList()) {
//                            i++;
//                            bookStoreBean.setUid(Integer.parseInt(uid));
//                            bookStoreBean.setUpdateDate(time - i);
//                            bookStoreBeanList.add(bookStoreBean);
//                        }
//                        //缓存这次的请求时间
//                        RxSharedPreferencesUtil.getInstance().putLong(bqtKey, bookStoreResponse.getData().getTimestamp());
//                        //显示数据-缓存数据
//                        BookStoreRepository.getInstance().saveBooks(bookStoreBeanList);
//                        RxEventBusTool.sendEvents( Constant.EventTag.REFRESH_BOOK_STORE_TAG);
//
//                    } else {//加载失败
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
