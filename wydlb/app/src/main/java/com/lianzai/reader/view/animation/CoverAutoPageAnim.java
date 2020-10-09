package com.lianzai.reader.view.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.view.View;

/**
 * Created by newbiechen on 17-7-24.
 */

public class CoverAutoPageAnim extends HorizonPageAnim {

    private Rect mSrcRect, mDestRect;
    private GradientDrawable mBackShadowDrawableBT;
    private long autoPageTime = 15000;

    public CoverAutoPageAnim(int w, int h, View view, OnPageChangeListener listener,int color) {
        super(w, h, view, listener);
        mSrcRect = new Rect(0, 0, mViewWidth, mViewHeight);
        mDestRect = new Rect(0, 0, mViewWidth, mViewHeight);

        int[] mBackShadowColors = new int[] { color,0x00000000};
        mBackShadowDrawableBT = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, mBackShadowColors);
        mBackShadowDrawableBT.setGradientType(GradientDrawable.LINEAR_GRADIENT);
    }

    @Override
    public void drawStatic(Canvas canvas) {
        if (isCancel){
            mNextBitmap = mCurBitmap.copy(Bitmap.Config.RGB_565, true);
            canvas.drawBitmap(mCurBitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(mNextBitmap, 0, 0, null);
        }
    }

    @Override
    public void drawMove(Canvas canvas) {

        switch (mDirection){
            case NEXT:
                int dis = (int) (mTouchY - mStartY);
                if (dis < 0){
                    dis = 0;
                }
                //计算bitmap截取的区域
                mSrcRect.top =  dis;
                //计算bitmap在canvas显示的区域
                mDestRect.top = dis;
                canvas.drawBitmap(mNextBitmap,0,0,null);
                canvas.drawBitmap(mCurBitmap,mSrcRect,mDestRect,null);
                addShadow(dis,canvas);
                break;
            default:
                mSrcRect.top = (int) (mTouchY);
                mDestRect.top = (int)(mTouchY);
                canvas.drawBitmap(mCurBitmap,0,0,null);
                canvas.drawBitmap(mNextBitmap,mSrcRect,mDestRect,null);
                addShadow((int) mTouchY,canvas);
                break;
        }
    }

    //添加阴影
    public void addShadow(int top,Canvas canvas) {
        mBackShadowDrawableBT.setBounds(0, top-30,mScreenWidth  ,top );
        mBackShadowDrawableBT.draw(canvas);
    }

    @Override
    public void startAnim() {
        super.startAnim();
        int dy = 0;
        switch (mDirection){
            case NEXT:
                if (isCancel){
                    dy = (int) -(mTouchY  - mStartY);
                }else{
                    int dis = (int) (mViewHeight - (mTouchY  - mStartY));
                    if (dis > mViewHeight){
                        dis = mViewHeight;
                    }
                    dy = dis;
                }
                break;
            default:
                if (isCancel){
                    dy = (int)(mViewHeight - mTouchY);
                }else{
                    dy = (int) - mTouchY;
                }
                break;
        }

        //滑动速度保持一致
        int duration = (int) (((autoPageTime - 300) * Math.abs(dy)) / mViewHeight);
        mScroller.startScroll( 0, (int)mTouchY, 0, dy, duration);
    }

    public long getAutoPageTime() {
        return autoPageTime;
    }

    public void setAutoPageTime(long autoPageTime) {
        this.autoPageTime = autoPageTime;
    }
}
