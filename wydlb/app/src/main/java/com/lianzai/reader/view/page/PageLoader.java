package com.lianzai.reader.view.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.bean.CloudRecordBean;
import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.model.local.BookRepository;
import com.lianzai.reader.model.local.CloudRecordRepository;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.utils.IOUtils;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.RxUtils;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.utils.StringUtils;
import com.lianzai.reader.utils.SystemBarUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;


public abstract class PageLoader {
    private static final String TAG = "PageLoader";

    // 当前页面的状态
    public static final int STATUS_LOADING = 1;         // 正在加载
    public static final int STATUS_FINISH = 2;          // 加载完成
    public static final int STATUS_ERROR = 3;           // 加载错误 (一般是网络加载情况)
    public static final int STATUS_EMPTY = 4;           // 空数据
    public static final int STATUS_PARING = 5;          // 正在解析 (装载本地数据)
    public static final int STATUS_PARSE_ERROR = 6;     // 本地文件解析错误(暂未被使用)
    public static final int STATUS_CATEGORY_EMPTY = 7;  // 获取到的目录为空

    public static final int STATUS_VIP_CHAPTER = 8;  //vip章节

    public static final int STATUS_NETWORD_DISCONNECT = 9;  //网络不可用

    // 默认的显示参数配置
    private static final int DEFAULT_MARGIN_HEIGHT = 40;

    private static final int CONTENT_DEFAULT_MARGIN_HEIGHT = 20;

    private static final int LINE_MARGIN_TOP = 8;

    private static final int BOOK_TITLE_DEFAULT_MARGIN = 40;//书籍名字的默认边距

    private static final int DEFAULT_MARGIN_WIDTH = 25;
    private static final int DEFAULT_TIP_SIZE = 12;
    private static final int DEFAULT_WEB_TIP_SIZE = 9;

    private static final int DEFAULT_WEB_TIP_OTHER_SIZE = 11;

    private static final int EXTRA_TITLE_SIZE = 4;
    protected  String mBookId;

    // 当前章节列表
    protected List<TxtChapter> mChapterList;
    // 监听器
    protected OnPageChangeListener mPageChangeListener;

    private Context mContext;

    // 页面显示类
    private PageView mPageView;
    // 当前显示的页
    private TxtPage mCurPage;
    // 上一章的页面列表缓存
    private List<TxtPage> mPrePageList;
    // 当前章节的页面列表
    private List<TxtPage> mCurPageList;
    // 下一章的页面列表缓存
    private List<TxtPage> mNextPageList;

    // 绘制电池的画笔
    private Paint mBatteryPaint;
    // 绘制提示的画笔
    private Paint mTipPaint;

    // 绘制原网页提示的画笔
    private Paint mWebTipPaint;

    // 已被实时转码文案
    private Paint mWebTipOtherPaint;

    private Paint mWebRectPaint;

    private Paint mTransparentPaint;

    // 绘制标题的画笔
    private Paint mTitlePaint;
    // 绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private Paint mBgPaint;
    // 绘制小说内容的画笔
    private TextPaint mTextPaint;

    private Paint mVoicePaint;//听书背景画笔

    // 阅读器的配置选项
    private ReadSettingManager mSettingManager;
    // 被遮盖的页，或者认为被取消显示的页
    private TxtPage mCancelPage;

    private Disposable mPreLoadDisp;

    /*****************params**************************/
    // 当前的状态
    protected int mStatus = STATUS_LOADING;
    // 判断章节列表是否加载完成
    protected boolean isChapterListPrepare;

    // 是否打开过章节
    private boolean isChapterOpen = false;
    private boolean isFirstOpen = true;
    private boolean isClose;
    // 页面的翻页效果模式
    private PageMode mPageMode;
    // 加载器的颜色主题
    private PageStyle mPageStyle;

    //间距样式
    private int textPaddingStyle = Constant.ReadPaddingStyle.PADDING_DEFAULT;

    //当前是否是夜间模式
    private boolean isNightMode;
    //书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    //应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;

    private int mContentMarginHeight;//内容区域边距

    private int mLineMarginHeight;//底线边距

    //章节标题字体的颜色
    private int mTitleTextColor;

    //书名字体的颜色
    private int mBookTitleTextColor;

    //字体的颜色
    private int mTextColor;
    //标题的大小
    private int mTitleSize;
    //字体的大小
    private int mTextSize;
    //行间距
    private int mTextInterval;
    //标题的行间距
    private int mTitleInterval;
    //段落距离(基于行间距的额外距离)
    private int mTextPara;
    private int mTitlePara;
    //电池的百分比
    private int mBatteryLevel;
    //当前页面的背景
    private int mBgColor;

    // 当前章
    protected int mCurChapterPos = 0;
    protected int mCurPagePos = 0;
    //上一章的记录
    private int mLastChapterPos = 0;

    Bitmap bgBitmap;
    Bitmap loadFailureBg;
    Bitmap loadFailureBg2;
    Bitmap adBitmap;

    private int currentParagraphPosition = 1;//听书段落位置

    boolean hasNotch = false;//是否是齐刘海屏幕

    //新增本地记录，本地记录初始化打开的页面。避免第一次跳到第一章第一页
    private CloudRecordBean cloudRecordBean;
    //判断广告是否在展示
    public boolean isAdShow = false;

    protected String bidStr = "";

    public String getBidStr() {
        return bidStr;
    }

    public void setBidStr(String bidStr) {
        this.bidStr = bidStr;
    }


    private void initBg(Context context) {//首次初始化根据所选背景
        if (null != bgBitmap) {
            bgBitmap.recycle();
            bgBitmap = null;
        }
        if (null != mPageStyle && mPageStyle.getBgColor() == R.color.nb_read_bg_8) {
            bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg1);
        }
//        else if (null != mPageStyle && mPageStyle.getBgColor() == R.color.nb_read_bg_9) {
//            bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
//        } else if (null != mPageStyle && mPageStyle.getBgColor() == R.color.nb_read_bg_10) {
//            bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
//        }

        //先默认使用加载失败的图。接口请求下来后覆盖此图。adadad
        if (null == loadFailureBg) {
            loadFailureBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_chapter_load_failed);
        }
        if (null == loadFailureBg2) {
            loadFailureBg2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_read_retry);
        }
    }

    /*****************************init params*******************************/
    public PageLoader(PageView pageView,String mBookId) {
        mPageView = pageView;
        mContext = pageView.getContext();
        mChapterList = new ArrayList<>(1);
        this.mBookId = mBookId;

        // 初始化数据
        initData();
        // 初始化画笔
        initPaint();
        // 初始化PageView
        initPageView();
        //初始化书籍
        prepareBook();
    }

    private void initData() {
        // 获取配置管理器
        mSettingManager = ReadSettingManager.getInstance();
        // 获取配置参数
        mPageMode = mSettingManager.getPageMode();
        mPageStyle = mSettingManager.getPageStyle();
        textPaddingStyle = mSettingManager.getTextPaddingStyle();

        // 初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        mContentMarginHeight = ScreenUtils.dpToPx(CONTENT_DEFAULT_MARGIN_HEIGHT);

        mLineMarginHeight = ScreenUtils.dpToPx(LINE_MARGIN_TOP);
        // 配置文字有关的参数
        setUpTextParams(mSettingManager.getTextSize());

        hasNotch = SystemBarUtils.hasNotch(mContext);

        initBg(mContext);//背景
    }


    /**
     * 作用：设置与文字相关的参数
     *
     * @param textSize
     */
    private void setUpTextParams(int textSize) {
        // 文字大小
        mTextSize = textSize;
        mTitleSize = mTextSize + ScreenUtils.spToPx(EXTRA_TITLE_SIZE);


//        // 行间距(大小为字体的一半)
//        mTextInterval = mTextSize / 2;
//        mTitleInterval = mTitleSize / 2;
//        // 段落间距(大小为字体的高度)//段落间距两倍字体高度
//        mTextPara =2* mTextSize;
//        mTitlePara = mTitleSize;

        //获取配置的
        if (textPaddingStyle == Constant.ReadPaddingStyle.PADDING_SMALL) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 2 / 3;
            mTitleInterval = (mTitleSize / 2) * 2 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 2 / 3;
            mTitlePara = (mTitleSize) * 2 / 3;

        } else if (textPaddingStyle == Constant.ReadPaddingStyle.PADDING_MEDIUM) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 5 / 3;
            mTitleInterval = (mTitleSize / 2) * 5 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 5 / 3;
            mTitlePara = (mTitleSize) * 5 / 3;
        } else if (textPaddingStyle == Constant.ReadPaddingStyle.PADDING_BIG) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 6 / 3;
            mTitleInterval = (mTitleSize / 2) * 6 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 6 / 3;
            mTitlePara = (mTitleSize) * 6 / 3;
        } else if (textPaddingStyle == Constant.ReadPaddingStyle.PADDING_DEFAULT) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 4 / 3;
            mTitleInterval = (mTitleSize / 2) * 4 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 4 / 3;
            mTitlePara = (mTitleSize) * 4 / 3;
        }
    }

    private void initPaint() {
        // 绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(mTextColor);
        mTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE)); // Tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);

        //不需要显示颜色的
        mTransparentPaint = new Paint();
        mTransparentPaint.setColor(Color.TRANSPARENT);

        mWebTipPaint = new Paint();
        mWebTipPaint.setColor(Color.WHITE);
        mWebTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mWebTipPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_WEB_TIP_SIZE)); // Tip默认的字体大小
        mWebTipPaint.setAntiAlias(true);
        mWebTipPaint.setSubpixelText(true);

        mWebTipOtherPaint = new Paint();
        mWebTipOtherPaint.setColor(mTitleTextColor);
        mWebTipOtherPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mWebTipOtherPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_WEB_TIP_OTHER_SIZE)); //
        mWebTipOtherPaint.setAntiAlias(true);

        mWebRectPaint = new Paint();
        mWebRectPaint.setColor(Color.parseColor("#33000000"));
        mWebRectPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mWebRectPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_WEB_TIP_SIZE)); // Tip默认的字体大小
        mWebRectPaint.setAntiAlias(true);

        // 听书背景画笔
        mVoicePaint = new Paint();
        mVoicePaint.setColor(Color.parseColor("#D0DDFF"));
        mVoicePaint.setAntiAlias(true);

        // 绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        // 绘制标题的画笔
        mTitlePaint = new TextPaint();
        mTitlePaint.setColor(mTextColor);
        mTitlePaint.setTextSize(mTitleSize);
        mTitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.setAntiAlias(true);

        // 绘制背景的画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);

        // 绘制电池的画笔
        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);

        Typeface typeface = getTypeFace(ReadSettingManager.getInstance().getFontStyle());//获取字体样式
        mTipPaint.setTypeface(typeface);
        mTitlePaint.setTypeface(typeface);
        mTextPaint.setTypeface(typeface);

        // 初始化页面样式
        setNightMode(mSettingManager.isNightMode());
    }

    private void initPageView() {
        //配置参数
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mBgColor);
    }

    /****************************** public method***************************/
    /**
     * 跳转到上一章
     *
     * @return
     */
    public boolean skipPreChapter() {
        if (!hasPrevChapter()) {
            return false;
        }

        // 载入上一章。
        if (parsePrevChapter()) {
            mCurPage = getCurPage(0);
            if (mPageChangeListener != null) {
                mPageChangeListener.onPageChange(0);
            }
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawCurPage(false);
        return true;
    }

    /**
     * 跳转到下一章
     *
     * @return
     */
    public boolean skipNextChapter() {
        if (!hasNextChapter()) {
            return false;
        }
        //判断是否达到章节的终止点
        if (parseNextChapter()) {
            mCurPage = getCurPage(0);
            if (mPageChangeListener != null) {
                mPageChangeListener.onPageChange(0);
            }
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawCurPage(false);
        return true;
    }

    /**
     * 跳转到指定章节
     *
     * @param pos:从 0 开始。
     */
    public void skipToChapter(int pos) {
        // 设置参数
        mCurChapterPos = pos;
        // 将上一章的缓存设置为null
        mPrePageList = null;
        // 将下一章缓存设置为null
        mNextPageList = null;
        // 如果当前缓存正在执行，则取消
        if (mPreLoadDisp != null) {
            mPreLoadDisp.dispose();
        }
        // 打开指定章节
        openChapter();
    }

    /**
     * 跳转到指定章节
     *
     * @param pos:从 0 开始。
     */
    public void skipToChapter(int pos,int pageP) {
        // 设置参数
        mCurChapterPos = pos;
        mCurPagePos = pageP;
        // 将上一章的缓存设置为null
        mPrePageList = null;
        // 将下一章缓存设置为null
        mNextPageList = null;
        // 如果当前缓存正在执行，则取消
        if (mPreLoadDisp != null) {
            mPreLoadDisp.dispose();
        }
        // 打开指定章节
        openChapter();
    }

    /**
     * 翻到上一页
     *
     * @return
     */
    public boolean skipToPrePage() {
        return mPageView.autoPrevPage();
    }

    /**
     * 翻到下一页
     *
     * @return
     */
    public boolean skipToNextPage() {
        return mPageView.autoNextPage();
    }

    /**
     * 更新时间
     */
    public void updateTime() {
        if (!mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    /**
     * 更新电量
     *
     * @param level
     */
    public void updateBattery(int level) {
        mBatteryLevel = level;
        if (!mPageView.isRunning()) {
            mPageView.drawCurPage(true);
        }
    }

    public void setPageBackground() {

    }

    /**
     * 设置提示的文字大小
     *
     * @param textSize:单位为 px。
     */
    public void setTipTextSize(int textSize) {
        mTipPaint.setTextSize(textSize);

        // 如果屏幕大小加载完成
        mPageView.drawCurPage(false);
    }

    /**
     * 设置文字相关参数
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        // 设置文字相关参数
        setUpTextParams(textSize);

        // 设置画笔的字体大小
        mTextPaint.setTextSize(mTextSize);
        // 设置标题的字体大小
        mTitlePaint.setTextSize(mTitleSize);
        // 存储文字大小
        mSettingManager.setTextSize(mTextSize);
        // 取消缓存
        mPrePageList = null;
        mNextPageList = null;

        // 如果当前已经显示数据
        if (isChapterListPrepare && mStatus == STATUS_FINISH) {
            // 重新计算当前页面

            setChapterPos();
            dealLoadPageList(mCurChapterPos);

            if (null == mCurPageList) return;

            // 防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
            if (mCurPage.position >= mCurPageList.size()) {
                mCurPage.position = mCurPageList.size() - 1;
            }

            // 重新获取指定页面
            mCurPage = mCurPageList.get(mCurPage.position);
        }

        mPageView.drawCurPage(false);
    }

    private void setChapterPos(){
        //设置当前章节位置为最后一章避免超出目录范围
        if(null != mChapterList && !mChapterList.isEmpty()){
            if(mCurChapterPos >= mChapterList.size()){
                mCurChapterPos = mChapterList.size() - 1;
            }
        }
    }

    /**
     * 设置夜间模式
     *
     * @param nightMode
     */
    public void setNightMode(boolean nightMode) {
        mSettingManager.setNightMode(nightMode);
        isNightMode = nightMode;

        if (isNightMode) {
            setPageStyle(PageStyle.NIGHT);
        } else {
            setPageStyle(mPageStyle);
        }
    }

    /**
     * 设置页面样式
     *
     * @param pageStyle:页面样式
     */
    public void setPageStyle(PageStyle pageStyle) {
        if (pageStyle != PageStyle.NIGHT) {
            mPageStyle = pageStyle;
            mSettingManager.setPageStyle(pageStyle);
        }

        //疑似废代码，有时间去掉它
        if (isNightMode && pageStyle != PageStyle.NIGHT) {
            return;
        }

        //背景初始化
        initBg(mContext);

        // 设置当前颜色样式
        mTextColor = ContextCompat.getColor(mContext, pageStyle.getFontColor());
        mBgColor = ContextCompat.getColor(mContext, pageStyle.getBgColor());
        mTitleTextColor = ContextCompat.getColor(mContext, R.color.nb_read_font_chapter_title_3_9);
        mBookTitleTextColor = ContextCompat.getColor(mContext, R.color.nb_read_font_book_title_3_9);

        mBatteryPaint.setColor(isNightMode ? mTextColor : mBookTitleTextColor);

        mTipPaint.setColor(isNightMode ? mTextColor : mBookTitleTextColor);

        mTitlePaint.setColor(isNightMode ? mTextColor : mTitleTextColor);

        mWebTipOtherPaint.setColor(isNightMode ? mTextColor : mBookTitleTextColor);

        mWebTipPaint.setColor(isNightMode ? Color.parseColor("#66000000") : Color.WHITE);

        mWebRectPaint.setColor(isNightMode ? Color.parseColor("#66FFFFFF") : Color.parseColor("#33000000"));

        mTextPaint.setColor(mTextColor);

        mBgPaint.setColor(mBgColor);

        mPageView.drawCurPage(false);
    }

    public PageMode getmPageMode() {
        return mPageMode;
    }

    /**
     * 翻页动画
     *
     * @param pageMode:翻页模式
     * @see PageMode
     */
    public void setPageMode(PageMode pageMode) {
        mPageMode = pageMode;

        mPageView.setPageMode(mPageMode);
        mSettingManager.setPageMode(mPageMode);

        // 重新绘制当前页
        mPageView.drawCurPage(false);
    }

    /**
     * 翻页动画
     *
     * @param pageMode:自动翻页模式
     * @see PageMode
     */
    public void setAutoPageMode(PageMode pageMode, long autoTime) {
        mPageMode = pageMode;
        mPageView.setPageMode(mPageMode);
        //不存储此状态
//        mSettingManager.setPageMode(mPageMode);
        mPageView.setAutoPageTime(autoTime);
        // 重新绘制当前页
        mPageView.drawCurPage(false);
    }

    /**
     * 设置内容与屏幕的间距
     *
     * @param marginWidth  :单位为 px
     * @param marginHeight :单位为 px
     */
    public void setMargin(int marginWidth, int marginHeight) {
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;

        // 如果是滑动动画，则需要重新创建了
        if (mPageMode == PageMode.SCROLL) {
            mPageView.setPageMode(PageMode.SCROLL);
        }

        mPageView.drawCurPage(false);
    }

    /**
     * 获取当前段落位置
     *
     * @return
     */
    public int getCurrentParagraphPosition() {
        return currentParagraphPosition;
    }

    /**
     * 设置字体
     *
     * @param fontStyle
     */
    public void setFontStyle(int fontStyle) {
        Typeface typeface = getTypeFace(fontStyle);
        mTipPaint.setTypeface(typeface);
        mTitlePaint.setTypeface(typeface);
        mTextPaint.setTypeface(typeface);

        // 取消缓存
        mPrePageList = null;
        mNextPageList = null;

        // 如果当前已经显示数据
        if (isChapterListPrepare && mStatus == STATUS_FINISH && null != mCurPageList) {
            setChapterPos();
            // 重新计算当前页面
            dealLoadPageList(mCurChapterPos);

            // 防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
            if (mCurPage.position >= mCurPageList.size()) {
                mCurPage.position = mCurPageList.size() - 1;
            }

            // 重新获取指定页面
            mCurPage = mCurPageList.get(mCurPage.position);
        }

        mPageView.drawCurPage(false);
    }


    /**
     * 听书模式-段落背景着色
     *
     * @param isListenBook
     * @param mCurrentParagraphPosition
     */
    public void setListenBookStyle(boolean isListenBook, int mCurrentParagraphPosition) {
        currentParagraphPosition = mCurrentParagraphPosition;
        if (isListenBook) {
            mPageView.drawVoiceCurPage(false, mCurrentParagraphPosition);
        } else {
            mPageView.drawCurPage(false);
        }
    }

    /**
     * 获取字体
     *
     * @param fontStyle
     * @return
     */
    private Typeface getTypeFace(int fontStyle) {
        Typeface typeface = Typeface.DEFAULT;//默认
        if (fontStyle == Constant.ReadFontStyle.KAITI_FONT) {//楷体
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/FontKaiTi.ttf");
        } else if (fontStyle == Constant.ReadFontStyle.SONGTI_FONT) {//宋体
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/FontSongTi.ttf");
        } else if (fontStyle == Constant.ReadFontStyle.HEITI_FONT) {//黑体
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/FontHeiTi.ttf");
        }
        return typeface;
    }

    /**
     * 设置行间距
     *
     * @param paddingStyle
     */
    public void setPadding(int paddingStyle) {
        textPaddingStyle = paddingStyle;
        if (paddingStyle == Constant.ReadPaddingStyle.PADDING_SMALL) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 2 / 3;
            mTitleInterval = (mTitleSize / 2) * 2 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 2 / 3;
            mTitlePara = (mTitleSize) * 2 / 3;

        } else if (paddingStyle == Constant.ReadPaddingStyle.PADDING_MEDIUM) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 5 / 3;
            mTitleInterval = (mTitleSize / 2) * 5 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 5 / 3;
            mTitlePara = (mTitleSize) * 5 / 3;
        } else if (paddingStyle == Constant.ReadPaddingStyle.PADDING_BIG) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 6 / 3;
            mTitleInterval = (mTitleSize / 2) * 6 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 6 / 3;
            mTitlePara = (mTitleSize) * 6 / 3;
        } else if (paddingStyle == Constant.ReadPaddingStyle.PADDING_DEFAULT) {
            // 行间距(大小为字体的一半)
            mTextInterval = (mTextSize / 2) * 4 / 3;
            mTitleInterval = (mTitleSize / 2) * 4 / 3;
            // 段落间距(大小为字体的高度)//段落间距两倍字体高度
            mTextPara = (2 * mTextSize) * 4 / 3;
            mTitlePara = (mTitleSize) * 4 / 3;
        }
        // 取消缓存
        mPrePageList = null;
        mNextPageList = null;

        // 如果当前已经显示数据
        if (isChapterListPrepare && mStatus == STATUS_FINISH && null != mCurPageList) {
            setChapterPos();
            // 重新计算当前页面
            dealLoadPageList(mCurChapterPos);

            // 防止在最后一页，通过修改字体，以至于页面数减少导致崩溃的问题
            if (mCurPage.position >= mCurPageList.size()) {
                mCurPage.position = mCurPageList.size() - 1;
            }
            // 重新获取指定页面
            mCurPage = mCurPageList.get(mCurPage.position);
        }

        mPageView.drawCurPage(false);

    }

    /**
     * 设置页面切换监听
     *
     * @param listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mPageChangeListener = listener;

        // 如果目录加载完之后才设置监听器，那么会默认回调
        if (isChapterListPrepare) {
            mPageChangeListener.onCategoryFinish(mChapterList);
        }
    }

    /**
     * 获取当前页的状态
     *
     * @return
     */
    public int getPageStatus() {
        return mStatus;
    }


    /**
     * 获取章节目录。
     *
     * @return
     */
    public List<TxtChapter> getChapterCategory() {
        return mChapterList;
    }

    /**
     * 获取当前页的页码
     *
     * @return
     */
    public int getPagePos() {
        return mCurPage.position;
    }

    /**
     * 获取当前页对象
     *
     * @return
     */
    public TxtPage getCurPage() {
        return mCurPage;
    }

    /**
     * 当前章所有页面
     *
     * @return
     */
    public List<TxtPage> getCurPageList() {
        return mCurPageList;
    }

    /**
     * 获取当前章节的章节位置
     *
     * @return
     */
    public int getChapterPos() {
        return mCurChapterPos;
    }

    /**
     * 获取当前章节
     *
     * @return
     */
    public TxtChapter getCurrentTxtChapter() {
        try{
            return getChapterCategory().get(mCurChapterPos >= getChapterCategory().size() ? getChapterCategory().size() - 1 : mCurChapterPos);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 主要是修改章节对应的源
     */
    public void setCurrentTxtChapter(TxtChapter txtChapter) {
        getChapterCategory().set(mCurChapterPos, txtChapter);
    }


    /**
     * 获取距离屏幕的高度
     *
     * @return
     */
    public int getMarginHeight() {
        return mMarginHeight;
    }

    /**
     * 保存阅读记录
     */
    public void saveRecord() {
        //本地阅读记录流程
        if (null == mChapterList || mChapterList.isEmpty()) {
            return;
        }
        //只有开始保存的时候才会删除之前的记录
        //删除之前的阅读记录
        if(null == mCurPage) return;
        cloudRecordBean.setBookId(Long.parseLong(mBookId));

        int totalChapterCount = mChapterList.size();
        if (mCurChapterPos < totalChapterCount) {
            cloudRecordBean.setChapter(mCurChapterPos);
        } else {
            cloudRecordBean.setChapter(totalChapterCount - 1);
        }

        try{
            //设置页码
            if (mCurPage != null) {
                cloudRecordBean.setChapterTitle(mCurPage.title);
                cloudRecordBean.setPos(mCurPage.position);
            } else {
                cloudRecordBean.setPos(0);
            }
        }catch (Exception e){

        }

        //优先使用数据中的标题和章节id
        try{
        if(null != mChapterList && !mChapterList.isEmpty()){
            TxtChapter tempC = mChapterList.get(mCurChapterPos);
            cloudRecordBean.setChapterTitle(tempC.getTitle());
            cloudRecordBean.setChapterId(Long.parseLong(tempC.getChapterId()));
            cloudRecordBean.setSource(tempC.getSourceKey());
        }
        }catch (Exception e){
        }

        //存储到数据库
        CloudRecordRepository.getInstance().addCloudRecord(cloudRecordBean);
    }


    /**
     * 初始化书籍
     */
    private void prepareBook() {
        cloudRecordBean = CloudRecordRepository.getInstance()
                .getCloudRecord(Long.parseLong(mBookId));

        if (cloudRecordBean == null) {
            cloudRecordBean = new CloudRecordBean();
        }

        //此处可修改得逻辑更完善todo
        mCurChapterPos = cloudRecordBean.getChapter();

        if (null != mChapterList && mCurChapterPos > mChapterList.size()) {
            mCurChapterPos = mChapterList.size() - 1;//防止mCurChapterPos索引大于章节目录
        }

        mLastChapterPos = mCurChapterPos;
    }


    /**
     * 打开当前章节
     */
    public void openChapter() {
        //mPageView未加载完成时，不打开
        if (!mPageView.isPrepare()) {
            return;
        }

        // 如果章节目录没有准备好
        if (!isChapterListPrepare) {
            mStatus = STATUS_LOADING;
            mPageView.drawCurPage(false);
            return;
        }

        // 如果获取到的章节目录为空
        if (mChapterList.isEmpty()) {
            mStatus = STATUS_CATEGORY_EMPTY;
            mPageView.drawCurPage(false);
            return;
        }

        //已经打开过页面置为false
        isFirstOpen = false;


        //取消本次预加载接下来的章节
        preLoadingNextChapter();

        //能获取到当前章节页面
        if (parseCurChapter()) {
            if (!isChapterOpen) {
                // 如果章节从未打开
                int position = cloudRecordBean.getPos();
                // 防止记录页的页号，大于当前最大页号
                if (position >= mCurPageList.size()) {
                    position = mCurPageList.size() - 1;
                }
                mCurPage = getCurPage(position);
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageChange(position);
                }
                mCancelPage = mCurPage;

            } else {
                // 如果章节打开过
                int position = mCurPagePos;
                mCurPagePos = 0;
                // 防止记录页的页号，大于当前最大页号
                if (position >= mCurPageList.size()) {
                    position = mCurPageList.size() - 1;
                }
                mCurPage = getCurPage(position);
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageChange(position);
                }
            }
        } else {
            mCurPage = new TxtPage();
        }

        // 切换状态
        isChapterOpen = true;

        mPageView.drawCurPage(false);
    }


    public void chapterError() {
        //加载错误
        mStatus = STATUS_ERROR;
        mPageView.drawCurPage(false);
    }

    public void chapterNetworkDisconnect() {
        //网络不可用
        mStatus = STATUS_NETWORD_DISCONNECT;
        mPageView.drawCurPage(false);
    }

    public void chapterVip() {
        //VIP章节
        mStatus = STATUS_VIP_CHAPTER;
        mPageView.drawCurPage(false);
    }

    /**
     * 关闭书本
     */
    public void closeBook() {
        isChapterListPrepare = false;
        isClose = true;

        mCurChapterPos = 0;
        // 如果当前缓存正在执行，则取消
        if (mPreLoadDisp != null) {
            mPreLoadDisp.dispose();
        }

        clearList(mChapterList);
        clearList(mCurPageList);
        clearList(mNextPageList);

        mChapterList = null;
        mCurPageList = null;
        mNextPageList = null;
        mPageView = null;
        mCurPage = null;

        recyclerBgBitmap();
    }

    private void clearList(List list) {
        if (list != null) {
            list.clear();
        }
    }

    public boolean isClose() {
        return isClose;
    }

    public boolean isChapterOpen() {
        return isChapterOpen;
    }

    /**
     * 加载页面列表
     *
     * @param chapterPos:章节序号
     * @return
     */
    private List<TxtPage> loadPageList(int chapterPos) throws Exception {

        if(null == mChapterList || mChapterList.isEmpty()){
            return null;
        }
        if(chapterPos >= mChapterList.size()){
            chapterPos = mChapterList.size() - 1;
        }
        // 获取章节
        TxtChapter chapter = mChapterList.get(chapterPos);
        // 判断章节是否存在
        if (!hasChapterData(chapter)) {
            return null;
        }
        // 获取章节的文本流
        BufferedReader reader = getChapterReader(chapter);
        List<TxtPage> chapters = loadPages(chapter, reader);

        return chapters;
    }

    /*******************************abstract method***************************************/

    /**
     * 刷新章节列表
     */
    public abstract void refreshChapterList(boolean needReopen,List<BookChapterBean> bookChapters);

    /**
     * 获取章节的文本流
     *
     * @param chapter
     * @return
     */
    protected abstract BufferedReader getChapterReader(TxtChapter chapter) throws Exception;

    /**
     * 章节数据是否存在
     *
     * @return
     */
    protected abstract boolean hasChapterData(TxtChapter chapter);

    /***********************************default method***********************************************/

    void drawPage(Bitmap bitmap, boolean isUpdate) {
        try {
            drawBackground(mPageView.getBgBitmap(), isUpdate);
            if (!isUpdate) {
                drawContent(bitmap);
            }
            //更新绘制
            mPageView.invalidate();
        } catch (Exception e) {

        }
    }

    /**
     * 听书模式
     *
     * @param bitmap
     * @param isUpdate
     * @param currentPagraphPosition
     */
    void drawVoicePage(Bitmap bitmap, boolean isUpdate, int currentPagraphPosition) {

        drawBackground(mPageView.getBgBitmap(), isUpdate);
        if (!isUpdate) {
            drawVoiceContent(bitmap, currentPagraphPosition);
        }
        //更新绘制
        mPageView.invalidate();
    }

    private void drawBackground(Bitmap bitmap, boolean isUpdate) {

        Canvas canvas = new Canvas(bitmap);
        int tipMarginHeight = mMarginWidth;//书籍标题的边距

        if (!isUpdate) {
            /****绘制背景****/
            canvas.drawColor(mBgColor);
            drawBackgroundImage(canvas);

            if (!mChapterList.isEmpty()) {
                /*****初始化标题的参数********/
                //需要注意的是:绘制text的y的起始点是text的基准线的位置，而不是从text的头部的位置
                float tipTop = 0;
                //要加上top距离绘制基线的高度，才能正确让顶部留出状态栏高度来。
                if (hasNotch) {
                    tipTop = ScreenUtils.getStatusBarHeight() + ScreenUtils.dpToPx(4) - mTipPaint.getFontMetrics().top;//齐刘海屏幕章节标题上边距额外增加一点距离
                } else {
                    tipTop = tipMarginHeight - mTipPaint.getFontMetrics().top;
                }
                //根据状态不一样，数据不一样
                if (mStatus != STATUS_FINISH) {
                    if (isChapterListPrepare) {
                        RxLogTool.e("drawBackground:" + mCurChapterPos + "---mChapterList.size:" + mChapterList.size());
                        if (mCurChapterPos < mChapterList.size()) {
                            String chapterTitle = mChapterList.get(mCurChapterPos).getTitle();
                            int wordCount = mTipPaint.breakText(chapterTitle,
                                    true, mVisibleWidth, null);
                            RxLogTool.e("wordCount:" + wordCount);
                            if (wordCount < chapterTitle.length()) {//截断
                                //第一行
                                String line1 = chapterTitle.substring(0, wordCount);

                                RxLogTool.e("line1:" + line1);
                                canvas.drawText(line1
                                        , mMarginWidth, tipTop, mTipPaint);

                                String line2 = chapterTitle.substring(wordCount, chapterTitle.length());

                                RxLogTool.e("line2:" + line2);

                                canvas.drawText(line2
                                        , mMarginWidth, tipTop + mTipPaint.getTextSize(), mTipPaint);
                            } else {
                                canvas.drawText(chapterTitle, mMarginWidth, tipTop, mTipPaint);
                            }

                        }

                    }
                } else {

                    String chapterTitle = mCurPage.title;
                    int wordCount = mTipPaint.breakText(chapterTitle,
                            true, mVisibleWidth, null);
                    RxLogTool.e("wordCount:" + wordCount);
                    if (wordCount < chapterTitle.length()) {//截断
                        //第一行
                        String line1 = chapterTitle.substring(0, wordCount);

                        RxLogTool.e("line1:" + line1);
                        canvas.drawText(line1
                                , mMarginWidth, tipTop, mTipPaint);

                        String line2 = chapterTitle.substring(wordCount, chapterTitle.length());

                        RxLogTool.e("line2:" + line2);

                        canvas.drawText(line2
                                , mMarginWidth, tipTop + mTipPaint.getTextSize(), mTipPaint);
                    } else {
                        canvas.drawText(chapterTitle, mMarginWidth, tipTop, mTipPaint);
                    }
                }


                // 只有finish的时候采用页码
                if (mStatus == STATUS_FINISH) {

                    /******绘制页码********/
                    // 底部的字显示的位置Y
                    float y = mDisplayHeight - mTipPaint.getFontMetrics().bottom - tipMarginHeight / 2;
                    String percent = (mCurPage.position + 1) + "/" + mCurPageList.size();
                    canvas.drawText(percent, mDisplayWidth - mTipPaint.measureText(percent) - mMarginWidth, y, mTipPaint);

                    if (mCurPage.position + 1 == mCurPageList.size() &&null != getCurrentTxtChapter() && !getCurrentTxtChapter().getSourceKey().equals("lianzai.com")) {//到了最后一页才显示
                        String originWeb = "原网页";
                        String originTipWeb = "已被实时转码以便在移动设备上查看";
                        RectF rect = new RectF();
                        rect.left = mMarginWidth - 5;
                        rect.top = y - mWebTipOtherPaint.getTextSize() - 2;
                        rect.bottom = y + 8;
                        rect.right = mMarginWidth + mWebTipPaint.measureText(originWeb) + 10;
                        RectF rectClick = new RectF();//点击原网页的区域
                        rectClick.left = rect.left - 20;
                        rectClick.top = rect.top - 20;
                        rectClick.bottom = rect.bottom + 20;
                        rectClick.right = rect.right + 20;
                        canvas.drawRoundRect(rectClick, 5f, 5f, mTransparentPaint);
                        //外站点击区域
                        mPageView.setmOutWebRect(rectClick);
                        canvas.drawRoundRect(new RectF(rect), 5f, 5f, mWebRectPaint);
                        canvas.drawText(originWeb, rect.right - mWebTipPaint.measureText(originWeb) - 5, rect.bottom - mWebTipPaint.getTextSize()/2 + 1, mWebTipPaint);
                        canvas.drawText(originTipWeb, rect.right + 10, y , mWebTipOtherPaint);
                    } else {
                        //外站点击区域
                        mPageView.setmOutWebRect(null);
                        //不和实时转码视图冲突
                        /******绘制电池********/
                        int visibleLeft = mMarginWidth;
                        int visibleBottom = mDisplayHeight - tipMarginHeight*2/3 + 2;
                        int outFrameWidth = (int) mTipPaint.measureText("xxx");
                        int outFrameHeight = (int) mTipPaint.getTextSize()*2/3;
                        int polarHeight = ScreenUtils.dpToPx(2);
                        int polarWidth = ScreenUtils.dpToPx(2);
                        int border = 2;
                        int innerMargin = 1;
                        //外框的制作
                        int outFrameLeft = visibleLeft;
                        int outFrameRight = outFrameLeft + outFrameWidth;
                        int outFrameTop = visibleBottom - outFrameHeight;
                        int outFrameBottom = visibleBottom;
                        Rect outFrame = new Rect(outFrameLeft, outFrameTop, outFrameRight, outFrameBottom);
                        mBatteryPaint.setStyle(Paint.Style.STROKE);
                        mBatteryPaint.setStrokeWidth(border);
                        canvas.drawRect(outFrame, mBatteryPaint);
                        //内框的制作
                        float innerWidth = (outFrame.width() - innerMargin * 2 - border) * (mBatteryLevel / 100.0f);
                        RectF innerFrame = new RectF(outFrameLeft + border + innerMargin, outFrameTop + border + innerMargin,
                                outFrameLeft + border + innerMargin + innerWidth, outFrameBottom - border - innerMargin);
                        mBatteryPaint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(innerFrame, mBatteryPaint);
                        //电极的制作
                        int polarLeft = outFrameRight;
                        int polarTop = visibleBottom - (outFrameHeight + polarHeight) / 2;
                        Rect polar = new Rect(polarLeft, polarTop, polarLeft+polarWidth,
                                polarTop + polarHeight);
                        mBatteryPaint.setStyle(Paint.Style.FILL);
                        canvas.drawRect(polar, mBatteryPaint);

                        /******绘制当前时间********/
                        //底部的字显示的位置Y
                        String time = StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_TIME);
                        float x1 = polarLeft + polarWidth + ScreenUtils.dpToPx(4);
                        canvas.drawText(time, x1, y, mTipPaint);
                    }
                }
            }
        } else {
            //擦除区域
//            mBgPaint.setColor(mBgColor);
//            canvas.drawRect(mDisplayWidth / 2, mDisplayHeight - mMarginHeight + ScreenUtils.dpToPx(2), mDisplayWidth, mDisplayHeight, mBgPaint);
            canvas.drawBitmap(bgBitmap, new Rect(mDisplayWidth / 2, mDisplayHeight - mMarginHeight + ScreenUtils.dpToPx(2), mDisplayWidth, mDisplayHeight), new RectF(), mBgPaint);
        }
    }

    /**
     * 释放图片
     */
    public void recyclerBgBitmap() {
        if (null != bgBitmap) {
            bgBitmap.recycle();
            bgBitmap = null;
        }
        if (null != loadFailureBg) {
            loadFailureBg.recycle();
            loadFailureBg = null;
        }
        if (null != loadFailureBg2) {
            loadFailureBg2.recycle();
            loadFailureBg2 = null;
        }
        if (null != adBitmap) {
            adBitmap.recycle();
            adBitmap = null;
        }


    }

    private void drawBackgroundImage(Canvas canvas) {
        if (!isNightMode) {
            //加载资源

            if (mPageStyle.getBgColor() == R.color.nb_read_bg_8) {
                canvas.drawBitmap(bgBitmap, null, new RectF(0, 0, mDisplayWidth, mDisplayHeight), null);
            }
//            else if (mPageStyle.getBgColor() == R.color.nb_read_bg_9) {
//                canvas.drawBitmap(bgBitmap, null, new RectF(0, 0, mDisplayWidth, mDisplayHeight), null);
//            } else if (mPageStyle.getBgColor() == R.color.nb_read_bg_10) {
//                canvas.drawBitmap(bgBitmap, null, new RectF(0, 0, mDisplayWidth, mDisplayHeight), null);
//            } 
            else {
                canvas.drawColor(mBgColor);
            }
        }
    }

    private void drawContent(Bitmap bitmap) {
        RxLogTool.e("PageView drawContent.....");
        Canvas canvas = new Canvas(bitmap);

        //每次翻页都先把广告置为false
        isAdShow = false;

        if (mPageMode == PageMode.SCROLL) {
            if (!isNightMode) {
                drawBackgroundImage(canvas);
            } else {
                canvas.drawColor(mBgColor);
            }
        }
        /******绘制内容****/

        if (mStatus != STATUS_FINISH) {
            //绘制字体
            String tip = "";

            RxLogTool.e("drawContent mStatus:" + mStatus + "");
            switch (mStatus) {
                case STATUS_LOADING:
                    tip = "";
                    RxLogTool.e("mStatus:" + tip);
                    break;
                case STATUS_ERROR:
//                    tip = "抱歉，当前章节无法阅读";
                    drawLoadChapterFailure(canvas, "抱歉，当前章节无法阅读");
                    break;
                case STATUS_VIP_CHAPTER:
//                    tip = "当前章节暂不提供阅读";
//                    drawLoadChapterFailure(canvas);
                    drawLoadChapterFailure(canvas, "抱歉，当前章节无法阅读");
                    break;
                case STATUS_NETWORD_DISCONNECT:
//                    tip = "本地未缓存该章节,无法打开";
                    drawLoadChapterFailure(canvas, "离线模式下该章节无法阅读");
                    break;
                case STATUS_EMPTY:
                    tip = "文章内容为空";
                    break;
                case STATUS_PARING:
                    tip = "正在排版请等待...";
                    break;
                case STATUS_PARSE_ERROR:
                    tip = "文件解析错误";
                    break;
                case STATUS_CATEGORY_EMPTY:
                    tip = "目录列表为空";
                    break;
            }

            //将提示语句放到正中间
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float textHeight = fontMetrics.top - fontMetrics.bottom;
            float textWidth = mTextPaint.measureText(tip);
            float pivotX = (mDisplayWidth - textWidth) / 2;
            float pivotY = (mDisplayHeight - textHeight) / 2;
            canvas.drawText(tip, pivotX, pivotY, mTextPaint);
        } else {
            float top;

            if (mPageMode == PageMode.SCROLL) {
                top = -mTextPaint.getFontMetrics().top;
            } else {
                top = mContentMarginHeight + mMarginHeight - mTextPaint.getFontMetrics().top;
            }

            //设置总距离
            int interval = mTextInterval + (int) mTextPaint.getTextSize();
            int para = mTextPara + (int) mTextPaint.getTextSize();
            int titleInterval = mTitleInterval + (int) mTitlePaint.getTextSize();
            int titlePara = mTitlePara + (int) mTextPaint.getTextSize();
            PageRow pageRow = null;

            //对标题进行绘制
            for (int i = 0; i < mCurPage.titleLines; ++i) {
                pageRow = mCurPage.pageRows.get(i);
                //设置顶部间距
                if (i == 0) {
                    top += mTitlePara;
                }
                //计算文字显示的起始点
//                int start = (int) (mDisplayWidth - mTitlePaint.measureText(str)) / 2;
                //进行绘制
                canvas.drawText(pageRow.line, mMarginWidth, top, mTitlePaint);

                //设置尾部间距
                if (i == mCurPage.titleLines - 1) {
                    top += titlePara;
                } else {
                    //行间距
                    top += titleInterval;
                }
            }

            //对内容进行绘制
            for (int i = mCurPage.titleLines; i < mCurPage.pageRows.size(); ++i) {
                pageRow = mCurPage.pageRows.get(i);

                String line = pageRow.line;

                canvas.drawText(line, mMarginWidth, top, mTextPaint);

                if (line.endsWith("\n")) {
                    top += para;//段落间距
                } else {
                    top += interval;
                }
            }
            //此处判断当前绘图高度和是否章节最后一页，满足条件则加上广告。adadad
            if(null != adBitmap){
                int bottom = mDisplayHeight - RxImageTool.dip2px(53);
                if(bottom - adBitmap.getHeight() - RxImageTool.dip2px(53) > top){
                    float x = (mDisplayWidth - adBitmap.getWidth()) / 2;
                    RectF rectF = new RectF();
                    rectF.left = x;
                    rectF.top =  bottom - adBitmap.getHeight();
                    rectF.right = x + adBitmap.getWidth();
                    rectF.bottom = bottom;
                    canvas.drawBitmap(adBitmap, null, rectF, null);
                    //设置广告点击区域
                    mPageView.setAdBitmap(rectF);
                    isAdShow = true;
                }
            }
        }
    }

    public Bitmap getAdBitmap() {
        return adBitmap;
    }

    public void setAdBitmap(Bitmap adBitmap) {
        this.adBitmap = adBitmap;
    }

    /**
     * 加载中提示
     */
    private void drawLoadingChapter(Canvas canvas) {
        float x = (mDisplayWidth - loadFailureBg.getWidth()) / 2;
        float y = (mDisplayHeight - loadFailureBg.getHeight()) / 2;

        RectF rectF = new RectF();
        rectF.left = x;
        rectF.top = y;
        rectF.right = x + loadFailureBg.getWidth();
        rectF.bottom = y + loadFailureBg.getHeight();

        canvas.drawRect(rectF, mTransparentPaint);
    }


    /**
     * 加载失败提示
     */
    private void drawLoadChapterFailure(Canvas canvas, String tip) {
        RxLogTool.e("drawLoadChapterFailure:" + tip);

        float x1 = (mDisplayWidth - loadFailureBg.getWidth()) / 2;
        float y1 = (mDisplayHeight - loadFailureBg.getHeight()) / 5;

        RectF rectF = new RectF();
        rectF.left = x1;
        rectF.top = y1;
        rectF.right = x1 + loadFailureBg.getWidth();
        rectF.bottom = y1 + loadFailureBg.getHeight();

        canvas.drawBitmap(loadFailureBg, null, rectF, null);

//        canvas.drawBitmap(loadFailureBg,x,y,null);

        //将提示语句放到正中间
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.top - fontMetrics.bottom;
        float textWidth = mTextPaint.measureText(tip);
        float pivotX = (mDisplayWidth - textWidth) / 2;
        float pivotY = rectF.bottom - 2 * textHeight;
        canvas.drawText(tip, pivotX, pivotY, mTextPaint);


        float x2 = (mDisplayWidth - loadFailureBg2.getWidth()) / 2;
        float y2 =  y1 + loadFailureBg.getHeight() - 3 * textHeight;

        RectF rectF2 = new RectF();
        rectF2.left = x2;
        rectF2.top = y2;
        rectF2.right = x2 + loadFailureBg2.getWidth();
        rectF2.bottom = y2 + loadFailureBg2.getHeight();

        canvas.drawBitmap(loadFailureBg2, null, rectF2, null);
        //设置重试点击区域
        mPageView.setLoadFailureBg2(rectF2);


        if(null != adBitmap){
            float x3 = (mDisplayWidth - adBitmap.getWidth()) / 2;
            int bottom = mDisplayHeight - RxImageTool.dip2px(53);;
            RectF rectF3 = new RectF();
            rectF3.left = x3;
            rectF3.top = bottom - adBitmap.getHeight();
            rectF3.right = x3 + adBitmap.getWidth();
            rectF3.bottom = bottom ;
            canvas.drawBitmap(adBitmap, null, rectF3, null);
            //设置广告点击区域
            mPageView.setAdBitmap(rectF3);
            isAdShow = true;
        }

    }


    /**
     * 听书内容刷新
     *
     * @param bitmap
     */
    private void drawVoiceContent(Bitmap bitmap, int currentParagraphPosition) {
        Canvas canvas = new Canvas(bitmap);

        if (mPageMode == PageMode.SCROLL) {
            if (!isNightMode) {
                drawBackgroundImage(canvas);
            } else {
                canvas.drawColor(mBgColor);
            }
        }
        /******刷新内容****/

        if (mStatus == STATUS_FINISH) {
            float top;

            if (mPageMode == PageMode.SCROLL) {
                top = -mTextPaint.getFontMetrics().top;
            } else {
                top = mContentMarginHeight + mMarginHeight - mTextPaint.getFontMetrics().top;
            }

            //设置总距离
            int interval = mTextInterval + (int) mTextPaint.getTextSize();
            int para = mTextPara + (int) mTextPaint.getTextSize();
            int titleInterval = mTitleInterval + (int) mTitlePaint.getTextSize();
            int titlePara = mTitlePara + (int) mTextPaint.getTextSize();
            PageRow pageRow = null;

            //对标题进行绘制
            for (int i = 0; i < mCurPage.titleLines; ++i) {
                pageRow = mCurPage.pageRows.get(i);
                //设置顶部间距
                if (i == 0) {
                    top += mTitlePara;
                }
                Rect rect = new Rect();
                rect.left = mMarginWidth;
                rect.top = (int) (top - mTitlePaint.getTextSize());
                rect.bottom = (int) top + mLineMarginHeight;
                rect.right = (int) (mMarginWidth + mTitlePaint.measureText(pageRow.line));

                if (pageRow.pagePosition == 0 && currentParagraphPosition == 1) {//标题着色
                    canvas.drawRect(rect, mVoicePaint);
                }

                //进行绘制
                canvas.drawText(pageRow.line, mMarginWidth, top, mTitlePaint);

                //设置尾部间距
                if (i == mCurPage.titleLines - 1) {
                    top += titlePara;
                } else {
                    //行间距
                    top += titleInterval;
                }
            }

            //对内容进行绘制
            for (int i = mCurPage.titleLines; i < mCurPage.pageRows.size(); ++i) {
                pageRow = mCurPage.pageRows.get(i);

                String line = pageRow.line;

                if (line.startsWith("　　")) {//
                    Rect rect = new Rect();
                    rect.left = (int) (mMarginWidth + mTextPaint.measureText("　　"));
                    rect.top = (int) (top - mTextPaint.getTextSize());
                    rect.bottom = (int) top + mLineMarginHeight;
                    rect.right = (int) (mMarginWidth + mTextPaint.measureText(line));

                    if (pageRow.currentParagraphPos == currentParagraphPosition) {//当前段落的所有行都要着色
                        canvas.drawRect(rect, mVoicePaint);
                    }
                } else {
                    Rect rect = new Rect();
                    rect.left = mMarginWidth;
                    rect.top = (int) (top - mTextPaint.getTextSize());
                    rect.bottom = (int) top + mLineMarginHeight;
                    rect.right = (int) (mMarginWidth + mTextPaint.measureText(line));

                    if (pageRow.currentParagraphPos == currentParagraphPosition) {//当前段落的所有行都要着色
                        canvas.drawRect(rect, mVoicePaint);
                    }
                }

                canvas.drawText(line, mMarginWidth, top, mTextPaint);

                if (line.endsWith("\n")) {
                    top += para;//段落间距
                } else {
                    top += interval;
                }
            }
        }
    }

    void prepareDisplay(int w, int h) {
        // 获取PageView的宽高
        mDisplayWidth = w;
        mDisplayHeight = h;

        // 获取内容显示位置的大小
        mVisibleWidth = mDisplayWidth - mMarginWidth * 2;
        mVisibleHeight = mDisplayHeight - mMarginHeight * 2 - mContentMarginHeight;

        // 重置 PageMode
       mPageView.setPageMode(mPageMode);

        //没有打开页面的情况
        if (!isChapterOpen || null == mCurPage) {
            // 展示加载界面
            mPageView.drawCurPage(false);
            // 如果在 display 之前调用过 openChapter 肯定是无法打开的。
            // 所以需要通过 display 再重新调用一次。
            if (!isFirstOpen) {
                // 打开书籍
                openChapter();
            }
        } else {
            // 如果章节已显示，那么就重新计算页面
            if (mStatus == STATUS_FINISH) {
                setChapterPos();
                dealLoadPageList(mCurChapterPos);
                // 重新设置文章指针的位置,判空
                TxtPage temp = getCurPage(mCurPage.position);
                if(null != temp)
                mCurPage = temp;

            }
            mPageView.drawCurPage(false);
        }
    }

    /**
     * 翻阅上一页
     *
     * @return
     */
    boolean prev() {
        // 以下情况禁止翻页
        if (!canTurnPage()) {
            return false;
        }

        if (mStatus == STATUS_FINISH) {
            // 先查看是否存在上一页
            TxtPage prevPage = getPrevPage();
            if (prevPage != null) {
                mCancelPage = mCurPage;
                mCurPage = prevPage;
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageChange(mCurPage.position);
                }
                mPageView.drawNextPage();
                return true;
            }
        }

        if (!hasPrevChapter()) {
            return false;
        }

        mCancelPage = mCurPage;
        if (parsePrevChapter()) {
            mCurPage = getPrevLastPage();
            if (mPageChangeListener != null) {
                mPageChangeListener.onPageChange(mCurPage.position);
            }
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawNextPage();
        return true;
    }

    /**
     * 解析上一章数据
     *
     * @return:数据是否解析成功
     */
    boolean parsePrevChapter() {
        // 加载上一章数据
        int prevChapter = mCurChapterPos - 1;

        mLastChapterPos = mCurChapterPos;
        mCurChapterPos = prevChapter;

        // 当前章缓存为下一章
        mNextPageList = mCurPageList;

        // 判断是否具有上一章缓存
        if (mPrePageList != null) {
            mCurPageList = mPrePageList;
            mPrePageList = null;
            mStatus = STATUS_FINISH;
            // 回调
            chapterChangeCallback();
        } else {
            setChapterPos();
            dealLoadPageList(mCurChapterPos);
        }
        return mCurPageList != null ? true : false;
    }

    private boolean hasPrevChapter() {
        //判断是否上一章节为空
        if (mCurChapterPos - 1 < 0) {
            return false;
        }
        return true;
    }

    /**
     * 翻到下一页
     *
     * @return:是否允许翻页
     */
    boolean next() {
        // 以下情况禁止翻页
        if (!canTurnPage()) {
            return false;
        }

        if (mStatus == STATUS_FINISH) {
            // 先查看是否存在下一页
            TxtPage nextPage = getNextPage();
            if (nextPage != null) {
                mCancelPage = mCurPage;
                mCurPage = nextPage;
                if (mPageChangeListener != null) {
                    mPageChangeListener.onPageChange(mCurPage.position);
                }
                mPageView.drawNextPage();
                return true;
            }
        }

        if (!hasNextChapter()) {
            return false;
        }

        mCancelPage = mCurPage;
        // 解析下一章数据
        if (parseNextChapter()) {
            mCurPage = getCurPage(0);
            if (mPageChangeListener != null) {
                mPageChangeListener.onPageChange(0);
            }
        } else {
            mCurPage = new TxtPage();
        }
        mPageView.drawNextPage();
        return true;
    }

    private boolean hasNextChapter() {
        // 判断是否到达目录最后一章
        if (mCurChapterPos + 1 >= mChapterList.size()) {
            return false;
        }
        return true;
    }

    boolean parseCurChapter() {
        setChapterPos();
        // 解析数据
        dealLoadPageList(mCurChapterPos);
        // 预加载下一页面
        preLoadNextChapter();
        return mCurPageList != null ? true : false;
    }

    /**
     * 解析下一章数据
     *
     * @return:返回解析成功还是失败
     */
    boolean parseNextChapter() {
        int nextChapter = mCurChapterPos + 1;

        mLastChapterPos = mCurChapterPos;
        mCurChapterPos = nextChapter;

        // 将当前章的页面列表，作为上一章缓存
        mPrePageList = mCurPageList;

        // 是否下一章数据已经预加载了
        if (mNextPageList != null) {
            mCurPageList = mNextPageList;
            mNextPageList = null;
            mStatus = STATUS_FINISH;
            // 回调
            chapterChangeCallback();
        } else {
            setChapterPos();
            // 处理页面解析
            dealLoadPageList(mCurChapterPos);
        }
        // 预加载下一页面
//        preLoadNextChapter();
        return mCurPageList != null ? true : false;
    }

    private void dealLoadPageList(int chapterPos) {
        try {
            mCurPageList = loadPageList(chapterPos);
            if (mCurPageList != null) {
                if (mCurPageList.isEmpty()) {
                    mStatus = STATUS_EMPTY;
                    // 添加一个空数据
                    TxtPage page = new TxtPage();
                    page.pageRows = new ArrayList<>(1);
                    mCurPageList.add(page);
                } else {
                    mStatus = STATUS_FINISH;
                }
            } else {
                mStatus = STATUS_LOADING;
            }
        } catch (Exception e) {
//            e.printStackTrace();

            mCurPageList = null;
            mStatus = STATUS_ERROR;
        }
        // 回调
        chapterChangeCallback();
    }

    private void chapterChangeCallback() {
        if (mPageChangeListener != null) {
            mPageChangeListener.onChapterChange(mCurChapterPos);
            mPageChangeListener.onPageCountChange(mCurPageList != null ? mCurPageList.size() : 0);
        }
    }

    // 预加载下一章
    private void preLoadNextChapter() {
        int nextChapter = mCurChapterPos + 1;

        // 如果不存在下一章，且下一章没有数据，则不进行加载。
        if (!hasNextChapter()
                || !hasChapterData(mChapterList.get(nextChapter))) {
            return;
        }

        //如果之前正在加载则取消
        if (mPreLoadDisp != null) {
            mPreLoadDisp.dispose();
        }

        //调用异步进行预加载加载
        Single.create(new SingleOnSubscribe<List<TxtPage>>() {
            @Override
            public void subscribe(SingleEmitter<List<TxtPage>> e) throws Exception {
                e.onSuccess(loadPageList(nextChapter));
            }
        }).compose(RxUtils::toSimpleSingle)
                .subscribe(new SingleObserver<List<TxtPage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mPreLoadDisp = d;
                    }

                    @Override
                    public void onSuccess(List<TxtPage> pages) {
                        mNextPageList = pages;
                    }

                    @Override
                    public void onError(Throwable e) {
                        //无视错误
                    }
                });
    }

    // 取消翻页
    void pageCancel() {
        if (mCurPage.position == 0 && mCurChapterPos > mLastChapterPos) { // 加载到下一章取消了
            if (mPrePageList != null) {
                cancelNextChapter();
            } else {
                if (parsePrevChapter()) {
                    mCurPage = getPrevLastPage();
                    if (mPageChangeListener != null) {
                        mPageChangeListener.onPageChange(mCurPage.position);
                    }
                } else {
                    mCurPage = new TxtPage();
                }
            }
        } else if (mCurPageList == null
                || (mCurPage.position == mCurPageList.size() - 1
                && mCurChapterPos < mLastChapterPos)) {  // 加载上一章取消了

            if (mNextPageList != null) {
                cancelPreChapter();
            } else {
                if (parseNextChapter()) {
                    mCurPage = getCurPage(0);
                    if (mPageChangeListener != null) {
                        mPageChangeListener.onPageChange(0);
                    }
                } else {
                    mCurPage = new TxtPage();
                }
            }
        } else {
            // 假设加载到下一页，又取消了。那么需要重新装载。
            mCurPage = mCancelPage;
        }
    }

    private void cancelNextChapter() {
        int temp = mLastChapterPos;
        mLastChapterPos = mCurChapterPos;
        mCurChapterPos = temp;

        mNextPageList = mCurPageList;
        mCurPageList = mPrePageList;
        mPrePageList = null;

        chapterChangeCallback();

        mCurPage = getPrevLastPage();
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(mCurPage.position);
        }
        mCancelPage = null;
    }

    private void cancelPreChapter() {
        // 重置位置点
        int temp = mLastChapterPos;
        mLastChapterPos = mCurChapterPos;
        mCurChapterPos = temp;
        // 重置页面列表
        mPrePageList = mCurPageList;
        mCurPageList = mNextPageList;
        mNextPageList = null;

        chapterChangeCallback();

        mCurPage = getCurPage(0);
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(0);
        }
        mCancelPage = null;
    }

    /**************************************private method********************************************/
    /**
     * 将章节数据，解析成页面列表
     *
     * @param chapter：章节信息
     * @param br：章节的文本流
     * @return
     */
    private List<TxtPage> loadPages(TxtChapter chapter, BufferedReader br) {
        //生成的页面
        List<TxtPage> pages = new ArrayList<>();
        //使用流的方式加载
        List<PageRow> linePageRows = new ArrayList<>();

        int rHeight = mVisibleHeight;
        int titleLinesCount = 0;
        int paraCount = 0;//当前页面段落数
        boolean showTitle = true; // 是否展示标题
        String paragraph = chapter.getTitle();//默认展示标题
        int totalPageWordCount = 0;//当前页面所占的字数
        try {
            while (showTitle || (paragraph = (br==null?null:br.readLine()) ) != null) {
                paragraph = StringUtils.convertCC(paragraph, mContext);
                // 重置段落
                if (!showTitle) {
                    paragraph = paragraph.replaceAll("\\s", "");
                    // 如果只有换行符，那么就不执行
                    if (paragraph.equals("")) continue;
                    paragraph = StringUtils.halfToFull("  " + paragraph + "\n");

                } else {
                    //设置 title 的顶部间距
                    rHeight -= mTitlePara;
                }
                int wordCount = 0;
                String subStr = null;
                while (paragraph.length() > 0) {
                    //当前空间，是否容得下一行文字
                    if (showTitle) {
                        rHeight -= mTitlePaint.getTextSize();
                    } else {
                        rHeight -= mTextPaint.getTextSize();
                    }
                    // 一页已经填充满了，创建 TextPage
                    if (rHeight <= 0) {
                        // 创建Page
                        TxtPage page = new TxtPage();
                        page.position = pages.size();
                        page.title = StringUtils.convertCC(chapter.getTitle(), mContext);
                        page.pageRows = new ArrayList<>(linePageRows);

                        page.totalWordCount = totalPageWordCount;
                        page.titleLines = titleLinesCount;
                        page.paraCount = paraCount;
                        pages.add(page);
                        // 重置Lines
                        linePageRows.clear();
                        totalPageWordCount = 0;

                        rHeight = mVisibleHeight;
                        titleLinesCount = 0;
                        paraCount = 0;

                        continue;
                    }

                    //测量一行占用的字节数
                    if (showTitle) {
                        wordCount = mTitlePaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    } else {
                        wordCount = mTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    }

                    subStr = paragraph.substring(0, wordCount);
                    if (!subStr.equals("\n")) {
                        if (!showTitle && linePageRows.size() == 0 && !subStr.startsWith("　　")) {//第一行
                            paraCount++;
                        }
                        if (subStr.startsWith("　　")) {//段落
                            paraCount++;
                        }


                        //将一行字节，存储到lines中
                        PageRow pageRow = new PageRow(subStr, paraCount);
                        linePageRows.add(pageRow);

                        totalPageWordCount += StringUtils.fullToHalf(subStr).length();

                        //设置段落间距
                        if (showTitle) {
                            titleLinesCount += 1;
                            rHeight -= mTitleInterval;
                        } else {
                            rHeight -= mTextInterval;
                        }
                    }
                    //裁剪
                    paragraph = paragraph.substring(wordCount);
                }

                //增加段落的间距
                if (!showTitle && linePageRows.size() != 0) {
                    rHeight = rHeight - mTextPara + mTextInterval;
                }

                if (showTitle) {
                    rHeight = rHeight - mTitlePara + mTitleInterval;
                    showTitle = false;
                    //标题也算作一个段落
                    paraCount++;
                }
            }

            if (linePageRows.size() != 0) {
                //创建Page
                TxtPage page = new TxtPage();
                page.position = pages.size();
                page.title = StringUtils.convertCC(chapter.getTitle(), mContext);
                page.pageRows = new ArrayList<>(linePageRows);
                page.totalWordCount = totalPageWordCount;

                page.titleLines = titleLinesCount;
                page.paraCount = paraCount;
                pages.add(page);

                //重置Lines
                linePageRows.clear();
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            IOUtils.close(br);
        }
        return pages;
    }


    /**
     * @return:获取初始显示的页面
     */
    private TxtPage getCurPage(int pos) {

        //判空
        if(null == mCurPageList ){
            return null;
        }
        if (mCurPageList.size() > pos) {
            return mCurPageList.get(pos);
        } else {
            return mCurPageList.get(mCurPageList.size() - 1);
        }

    }

    /**
     * @return:获取上一个页面
     */
    private TxtPage getPrevPage() {
        if(null == mCurPage){
            return null;
        }
        int pos = mCurPage.position - 1;
        if (pos < 0) {
            return null;
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取下一的页面
     */
    private TxtPage getNextPage() {
        int pos = mCurPage.position + 1;
        if (pos >= mCurPageList.size()) {
            return null;
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取上一个章节的最后一页
     */
    private TxtPage getPrevLastPage() {
        int pos = mCurPageList.size() - 1;
        return mCurPageList.get(pos);
    }

    /**
     * 根据当前状态，决定是否能够翻页
     *
     * @return
     */
    private boolean canTurnPage() {

        if (!isChapterListPrepare) {
            return false;
        }

        if (mStatus == STATUS_PARSE_ERROR
                || mStatus == STATUS_PARING ) {
            return false;
        }
//        else if (mStatus == STATUS_ERROR) {
//            mStatus = STATUS_LOADING;
//        }
        return true;
    }

    public void preLoadingNextChapter() {
    }

    /*****************************************interface*****************************************/

    public interface OnPageChangeListener {
        /**
         * 作用：章节切换的时候进行回调
         *
         * @param pos:切换章节的序号
         */
        void onChapterChange(int pos);

        /**
         * 作用：预加载章节内容
         *
         * @param requestChapters:需要下载的章节列表
         */
        void preLoadingChapters(List<TxtChapter> requestChapters);

        /**
         * 加载当前章节
         *
         * @param txtChapter
         */
        void requestCurrentChapter(TxtChapter txtChapter);

        /**
         * 当前章节已缓存过
         *
         * @param txtChapter
         */
        void currentChapterHasCached(TxtChapter txtChapter);

        /**
         * 作用：章节目录加载完成时候回调
         *
         * @param chapters：返回章节目录
         */
        void onCategoryFinish(List<TxtChapter> chapters);

        /**
         * 作用：章节页码数量改变之后的回调。==> 字体大小的调整，或者是否关闭虚拟按钮功能都会改变页面的数量。
         *
         * @param count:页面的数量
         */
        void onPageCountChange(int count);

        /**
         * 作用：当页面改变的时候回调
         *
         * @param pos:当前的页面的序号
         */
        void onPageChange(int pos);

    }


    public PageView getmPageView() {
        return mPageView;
    }

    public void setmPageView(PageView mPageView) {
        this.mPageView = mPageView;
    }

}
