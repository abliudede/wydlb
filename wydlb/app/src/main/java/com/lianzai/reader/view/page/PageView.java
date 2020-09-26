package com.lianzai.reader.view.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.lianzai.reader.model.bean.CollBookBean;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.animation.CoverAutoPageAnim;
import com.lianzai.reader.view.animation.CoverPageAnim;
import com.lianzai.reader.view.animation.HorizonPageAnim;
import com.lianzai.reader.view.animation.NonePageAnim;
import com.lianzai.reader.view.animation.PageAnimation;
import com.lianzai.reader.view.animation.ScrollPageAnim;
import com.lianzai.reader.view.animation.SimulationPageAnim;
import com.lianzai.reader.view.animation.SlidePageAnim;


/**
 * Created by Administrator on 2016/8/29 0029.
 * 原作者的GitHub Project Path:(https://github.com/PeachBlossom/treader)
 * 绘制页面显示内容的类
 */
public class PageView extends View {

    private final static String TAG = "BookPageWidget";

    private int mViewWidth = 0; // 当前View的宽
    private int mViewHeight = 0; // 当前View的高

    private int mStartX = 0;
    private int mStartY = 0;
    private boolean isMove = false;
    // 初始化参数
    private int mBgColor = 0xFFCEC29C;
    private PageMode mPageMode = PageMode.SIMULATION;
    // 是否允许点击
    private boolean canTouch = true;
    // 唤醒菜单的区域
    private RectF mCenterRect = null;

    //外站跳转区域
    private RectF mOutWebRect = null;

    //重试点击区域
    private RectF loadFailureBg2 = null;

    private boolean isPrepare;
    // 动画类
    private PageAnimation mPageAnim;

    private boolean isEnable=true;//是否可用

    // 动画监听类
    private PageAnimation.OnPageChangeListener mPageAnimListener = new PageAnimation.OnPageChangeListener() {
        @Override
        public boolean hasPrev() {
            return PageView.this.hasPrevPage();
        }

        @Override
        public boolean hasNext() {
            return PageView.this.hasNextPage();
        }

        @Override
        public void pageCancel() {
            PageView.this.pageCancel();
        }
    };

    //点击监听
    private TouchListener mTouchListener;
    //内容加载器
    private PageLoader mPageLoader;
    private RectF adBitmaprectF3;

    public PageView(Context context) {
        this(context, null);

    }

    public PageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        isPrepare = true;

        if (mPageLoader != null) {
            if(null == mPageLoader.getmPageView()){
                mPageLoader.setmPageView(this);
            }
            mPageLoader.prepareDisplay(w, h);
        }
    }

    //设置翻页的模式
    void setPageMode(PageMode pageMode) {
        mPageMode = pageMode;
        //视图未初始化的时候，禁止调用
        if (mViewWidth == 0 || mViewHeight == 0) return;

        switch (mPageMode) {
            case SIMULATION:
                mPageAnim = new SimulationPageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case COVER:
                mPageAnim = new CoverPageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case COVER_AUTO:
                ReadSettingManager mSettingManager = ReadSettingManager.getInstance();
                PageStyle mPageStyle;
                if (mSettingManager.isNightMode()) {
                    mPageStyle = PageStyle.NIGHT;
                }else {
                    mPageStyle = mSettingManager.getPageStyle();
                }
                int mColor = ContextCompat.getColor(getContext(), mPageStyle.getFontColor());
                mPageAnim = new CoverAutoPageAnim(mViewWidth, mViewHeight, this, mPageAnimListener,mColor);
                break;
            case SLIDE:
                mPageAnim = new SlidePageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case NONE:
                mPageAnim = new NonePageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
                break;
            case SCROLL:
                mPageAnim = new ScrollPageAnim(mViewWidth, mViewHeight, 0,
                        mPageLoader.getMarginHeight(), this, mPageAnimListener);
                break;
            default:
                mPageAnim = new SimulationPageAnim(mViewWidth, mViewHeight, this, mPageAnimListener);
        }
    }

    public Bitmap getNextBitmap() {
        if (mPageAnim == null) return null;
        return mPageAnim.getNextBitmap();
    }

    public Bitmap getBgBitmap() {
        if (mPageAnim == null) return null;
        return mPageAnim.getBgBitmap();
    }

    public boolean autoPrevPage() {
        //滚动暂时不支持自动翻页
        if (mPageAnim instanceof ScrollPageAnim) {
            return false;
        } else {
            startPageAnim(PageAnimation.Direction.PRE);
            return true;
        }
    }

    public boolean autoNextPage() {
        if (mPageAnim instanceof ScrollPageAnim) {
            return false;
        } else {
            startPageAnim(PageAnimation.Direction.NEXT);
            return true;
        }
    }

    private void startPageAnim(PageAnimation.Direction direction) {
        if (mTouchListener == null) return;
        //是否正在执行动画
        abortAnimation();
        if (direction == PageAnimation.Direction.NEXT) {
            int x = mViewWidth;
            int y = mViewHeight;
            //初始化动画
            mPageAnim.setStartPoint(x, y);
            //设置点击点
            mPageAnim.setTouchPoint(x, y);
            //设置方向
            Boolean hasNext = hasNextPage();

            mPageAnim.setDirection(direction);
            if (!hasNext) {
                return;
            }
        } else {
            int x = 0;
            int y = mViewHeight;
            //初始化动画
            mPageAnim.setStartPoint(x, y);
            //设置点击点
            mPageAnim.setTouchPoint(x, y);
            mPageAnim.setDirection(direction);
            //设置方向方向
            Boolean hashPrev = hasPrevPage();
            if (!hashPrev) {
                return;
            }
        }
        mPageAnim.startAnim();
        this.postInvalidate();
    }

    public void setmOutWebRect(RectF mOutWebRect) {
        this.mOutWebRect = mOutWebRect;
    }

    public void setLoadFailureBg2(RectF loadFailureBg2) {
        this.loadFailureBg2 = loadFailureBg2;
    }

    public void setAdBitmap(RectF rectF3) {
        this.adBitmaprectF3 = rectF3;
    }

    public void setBgColor(int color) {
        mBgColor = color;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制背景
        canvas.drawColor(mBgColor);

        //绘制动画
        mPageAnim.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (!canTouch && event.getAction() != MotionEvent.ACTION_DOWN) return true;

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                isMove = false;
                canTouch = mTouchListener.onTouch();
                mPageAnim.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                // 判断是否大于最小滑动值。
                int slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (!isMove) {
                    isMove = Math.abs(mStartX - event.getX()) > slop || Math.abs(mStartY - event.getY()) > slop;
                }

                // 如果滑动了，则进行翻页。
                if (isMove) {
                    mPageAnim.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove) {

                    //重试区域点击
                    if (null!= loadFailureBg2&& loadFailureBg2.contains(x,y)){
                        if (mTouchListener != null) {
                            mTouchListener.retry();
                        }
                        return true;
                    }

                    //广告区域点击
                    if (null!= adBitmaprectF3&& adBitmaprectF3.contains(x,y)){
                        if (mTouchListener != null && mPageLoader.isAdShow) {
                            mTouchListener.adClick();
                            return true;
                        }
                    }


                    //原网页点击
                    if (null!=mOutWebRect&&mOutWebRect.contains(x,y)){
                        if (mTouchListener != null) {
                            mTouchListener.goOriginWeb();
                        }
                        return true;
                    }

                    //设置中间区域范围
                    if (mCenterRect == null) {
                        mCenterRect = new RectF(mViewWidth / 5, mViewHeight / 3,
                                mViewWidth * 4 / 5, mViewHeight * 2 / 3);
                    }

                    //是否点击了中间
                    if (mCenterRect.contains(x, y)) {
                        if (mTouchListener != null) {
                            mTouchListener.center();
                        }
                        return true;
                    }
                }
                mPageAnim.onTouchEvent(event);
                break;
        }
        return true;
    }

    /**
     * 判断是否存在上一页
     *
     * @return
     */
    private boolean hasPrevPage() {
        mTouchListener.prePage();
        return mPageLoader.prev();
    }

    /**
     * 判断是否下一页存在
     *
     * @return
     */
    private boolean hasNextPage() {
        mTouchListener.nextPage();
        return mPageLoader.next();
    }

    private void pageCancel() {
        mTouchListener.cancel();
        mPageLoader.pageCancel();
    }

    @Override
    public void computeScroll() {
        //进行滑动
        mPageAnim.scrollAnim();
        super.computeScroll();
    }

    //如果滑动状态没有停止就取消状态，重新设置Anim的触碰点
    public void abortAnimation() {
        if(null != mPageAnim) {
            mPageAnim.abortAnim();
        }
    }

    public boolean isRunning() {
        if (mPageAnim == null) {
            return false;
        }
        return mPageAnim.isRunning();
    }

    public boolean isPrepare() {
        return isPrepare;
    }

    public void setTouchListener(TouchListener mTouchListener) {
        this.mTouchListener = mTouchListener;
    }

    public void drawNextPage() {
        if (!isPrepare) return;

        if (mPageAnim instanceof HorizonPageAnim) {
            ((HorizonPageAnim) mPageAnim).changePage();
        }
        mPageLoader.drawPage(getNextBitmap(), false);
    }

    /**
     * 绘制当前页。
     *
     * @param isUpdate
     */
    public void drawCurPage(boolean isUpdate) {
        if (!isPrepare) return;

        if (!isUpdate){
            if (mPageAnim instanceof ScrollPageAnim) {
                ((ScrollPageAnim) mPageAnim).resetBitmap();
            }
        }

        mPageLoader.drawPage(getNextBitmap(), isUpdate);
    }

    /**
     * 绘制当前页。-听书模式
     *
     * @param isUpdate
     */
    public void drawVoiceCurPage(boolean isUpdate,int currentParagraphPosition) {
        if (!isPrepare) return;

        if (!isUpdate){
            if (mPageAnim instanceof ScrollPageAnim) {
                ((ScrollPageAnim) mPageAnim).resetBitmap();
            }
        }

        mPageLoader.drawVoicePage(getNextBitmap(), isUpdate,currentParagraphPosition);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(null != mPageAnim) {
            mPageAnim.abortAnim();
            mPageAnim.clear();
            mPageAnim = null;
        }
        mPageLoader = null;
    }

    /**
     * 获取 PageLoader
     *
     * @return
     */
    public PageLoader getPageLoader(String mBookId) {
        // 判是否已经存在
        if (mPageLoader != null) {
            return mPageLoader;
        }
        // 根据书籍类型，获取具体的加载器
//        if (collBook.isLocal()) {
//            mPageLoader = new LocalPageLoader(this, collBook);
//        } else {
//            mPageLoader = new NetPageLoader(this, collBook);
//        }
        mPageLoader = new NetPageLoader(this,mBookId );

        // 判断是否 PageView 已经初始化完成
        if (mViewWidth != 0 || mViewHeight != 0) {
            if(null == mPageLoader.getmPageView()){
                mPageLoader.setmPageView(this);
            }
            // 初始化 PageLoader 的屏幕大小
            mPageLoader.prepareDisplay(mViewWidth, mViewHeight);
        }
        return mPageLoader;
    }

    public String getXY(){
        return mStartX+","+mStartY;
    }

    public interface TouchListener {
        boolean onTouch();
        void center();
        void prePage();
        void nextPage();
        void cancel();
        void goOriginWeb();
        void retry();
        void adClick();
    }

    public void setAutoPageTime(long autoTime){
        if(null == mPageAnim) return;
        if (mPageAnim instanceof CoverAutoPageAnim) {
            ((CoverAutoPageAnim) mPageAnim).setAutoPageTime(autoTime);
        }
    }

}
