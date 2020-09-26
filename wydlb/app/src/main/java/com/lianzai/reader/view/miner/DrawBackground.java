package com.lianzai.reader.view.miner;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.ScreenUtils;

/**
 * Created by lrz on 2018/1/20.
 */

public class DrawBackground extends BaseDraw {
    private Rect rectBg = null;


    Rect rectName=null;
    Rect rectLevel=null;

    Rect rectPool=null;
    Rect rectMine=null;

    Rect doorRect=null;//矿门位置

    Bitmap background=null;
    Bitmap minerPoolBg=null;
    Bitmap mineBg=null;

    Bitmap musicOpenBg=null;
    Bitmap musicCloseBg=null;

    Bitmap btnTask=null;//任务列表
    Rect taskRect=null;

    Bitmap btnInvitation=null;//邀请
    Rect invitationRect=null;

    Rect musicRect=null;


    int screenWidth = 0;
    int screenHeight = 0;

    Paint minerNamePaint;
    Paint minerLevelPaint;

    int btnMargin=0;

    ValueAnimator anim;

    ValueAnimator musicAnim;

    String minerName="**的矿场";
    String mineLevel="Level 0";

    OptionClickListener optionClickListener;

    int downX = 0;
    int downY = 0;

    int  scaleSize=15;

    MinerMainView minerMainView;

    boolean musicIsOpen=false;//默认打开

    Matrix musicMatrix;
    public DrawBackground(Context context,MinerMainView minerMainView){
        if (screenWidth == 0 && screenHeight == 0) {
            this.minerMainView=minerMainView;
//            background = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_miner);
//            minerPoolBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_miner_pool);
//            mineBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_mine);
            btnTask = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_btn_task);
            btnInvitation = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_invitation);

            musicOpenBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_music_open);
            musicCloseBg = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_music_close);

            musicIsOpen= RxSharedPreferencesUtil.getInstance().getBoolean("musicOpen",false);

            btnMargin=RxImageTool.dp2px(2);
            minerNamePaint=new Paint();
            minerNamePaint.setTextSize(RxImageTool.sp2px(12));
            minerNamePaint.setColor(Color.parseColor("#cccccc"));

            minerLevelPaint=new Paint();
            minerLevelPaint.setTextSize(RxImageTool.sp2px(10));
            minerLevelPaint.setColor(Color.parseColor("#F8E71C"));

            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels+ ScreenUtils.getStatusBarHeight();

            rectBg=new Rect(0,0,screenWidth,screenHeight);

            int poolBottom=minerPoolBg.getHeight();
            RxLogTool.e("poolBottom:"+poolBottom);

            rectPool=new Rect(screenWidth-minerPoolBg.getWidth(),minerPoolBg.getHeight()/3,screenWidth,4*minerPoolBg.getHeight()/3);
            rectMine=new Rect(0,screenHeight-mineBg.getHeight(),mineBg.getWidth(),screenHeight);

            doorRect=new Rect(rectPool.right-2*minerPoolBg.getWidth()/3,rectPool.bottom-2*minerPoolBg.getHeight()/5+100,rectPool.right-2*minerPoolBg.getWidth()/3+70,rectPool.bottom-2*minerPoolBg.getHeight()/5+200);

            invitationRect=new Rect(screenWidth-btnInvitation.getWidth()-btnMargin,screenHeight-2*btnInvitation.getHeight()-btnMargin,screenWidth-btnMargin,screenHeight-btnInvitation.getHeight()-btnMargin);

            taskRect=new Rect(invitationRect.left-btnTask.getWidth()-RxImageTool.dp2px(10),screenHeight-2*btnTask.getHeight()-RxImageTool.dp2px(8),invitationRect.left-RxImageTool.dp2px(10),screenHeight-btnTask.getHeight()-RxImageTool.dp2px(8));

            musicRect=new Rect(screenWidth-3*musicOpenBg.getWidth()/2,musicOpenBg.getHeight()/2,screenWidth-musicOpenBg.getWidth()/2,3*musicOpenBg.getHeight()/2);
            musicMatrix=new Matrix();
            musicMatrix.postTranslate(musicRect.centerX()-musicOpenBg.getWidth()/2,musicRect.centerY()-musicOpenBg.getHeight()/2);

            rectName=new Rect();
            minerNamePaint.getTextBounds(minerName, 0, minerName.length(), rectName);


            rectLevel=new Rect();
            minerLevelPaint.getTextBounds(mineLevel, 0, mineLevel.length(), rectLevel);

            startAnimation();

            startMusicAnimation();
        }
    }

    public String getMinerName() {
        return minerName;
    }

    public void setMinerName(String minerName) {
        this.minerName = minerName;
    }

    public String getMineLevel() {
        return mineLevel;
    }

    public void setMineLevel(String mineLevel) {
        this.mineLevel = mineLevel;
    }

    public OptionClickListener getOptionClickListener() {
        return optionClickListener;
    }

    public void setOptionClickListener(OptionClickListener optionClickListener) {
        this.optionClickListener = optionClickListener;
    }

    /**
     * 矿池门的RECT
     * @return
     */
    public Rect getDoorRect() {
        return doorRect;
    }

    /**
     * 水晶矿场RECT
     * @return
     */
    public Rect getRectMine() {
        return rectMine;
    }

    @Override
    void onDraw(Canvas canvas) {

        canvas.drawBitmap(background,null,rectBg,null);
//        canvas.drawRect(rectPool,minerLevelPaint);
        //画矿场

        canvas.drawBitmap(minerPoolBg,null,rectPool,null);

//        canvas.drawRect(musicRect,minerNamePaint);

        //音乐开关
        canvas.drawBitmap(musicIsOpen?musicCloseBg:musicOpenBg,musicMatrix,null);

        //矿门位置
//        canvas.drawRect(doorRect,minerNamePaint);
//        canvas.drawRect(rectMine,minerLevelPaint);
        //画水晶矿
        canvas.drawBitmap(mineBg,null,rectMine,null);

        canvas.drawBitmap(btnInvitation,null,invitationRect,null);
        canvas.drawBitmap(btnTask,null,taskRect,null);


        if (rectName.width()>minerPoolBg.getWidth()/2){
            canvas.drawText(getMinerName(),screenWidth-rectName.width()-10,rectPool.top-rectName.height()-rectLevel.height()-13,minerNamePaint);
        }else{
            canvas.drawText(getMinerName(),screenWidth-minerPoolBg.getWidth()/4- rectName.width()/2,rectPool.top-rectName.height()-rectLevel.height()-13,minerNamePaint);
        }


        if (rectLevel.width()>minerPoolBg.getWidth()/2){
            canvas.drawText(getMineLevel(),screenWidth-rectLevel.width()-10,rectPool.top-rectLevel.height()-5,minerLevelPaint);
        }else{
            canvas.drawText(getMineLevel(),screenWidth-minerPoolBg.getWidth()/4- rectLevel.width()/2,rectPool.top-rectLevel.height()-5,minerLevelPaint);
        }


    }

    private void startAnimation(){
        anim=ValueAnimator.ofInt(scaleSize,0);

        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(-1);
        anim.setDuration(1000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int size=(int)animation.getAnimatedValue();
                invitationRect=new Rect(screenWidth-btnInvitation.getWidth()-btnMargin,screenHeight-2*btnInvitation.getHeight()-btnMargin,screenWidth-btnMargin+size,screenHeight-btnInvitation.getHeight()-btnMargin+size);
                minerMainView.postInvalidate();
            }
        });
        anim.start();
    }

    private void startMusicAnimation(){
        musicAnim=ValueAnimator.ofInt(0,360);
        musicAnim.setRepeatMode(ValueAnimator.RESTART);
        musicAnim.setRepeatCount(-1);
        musicAnim.setDuration(1000);
        musicAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int roate=(int)animation.getAnimatedValue();
                musicMatrix.setRotate(roate,musicOpenBg.getWidth()/2,musicOpenBg.getHeight()/2);
                musicMatrix.postTranslate(musicRect.centerX()-musicOpenBg.getWidth()/2,musicRect.centerY()-musicOpenBg.getHeight()/2);
                minerMainView.postInvalidate();
            }
        });
        musicAnim.start();
    }

    @Override
    void onDestroy() {
        background.recycle();
        minerPoolBg.recycle();
        mineBg.recycle();
        btnTask.recycle();
        btnInvitation.recycle();

        musicCloseBg.recycle();
        musicOpenBg.recycle();


        if (null!=anim){
            anim.cancel();
        }
        if (null!=musicAnim){
            musicAnim.cancel();
        }
    }

    public void onTouch(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = (int) event.getX();
                downY = (int) event.getY();
                if (taskRect.contains(downX,downY)){//任务列表
                    getOptionClickListener().optionClick(0);
                    RxLogTool.e("click 任务列表");
                }else if(invitationRect.contains(downX,downY)){//邀请
                    getOptionClickListener().optionClick(1);
                    RxLogTool.e("click 邀请");
                }else if(musicRect.contains(downX,downY)){//音乐开关

                    if (musicIsOpen)
                        musicIsOpen=false;
                    else
                        musicIsOpen=true;
                    RxSharedPreferencesUtil.getInstance().putBoolean("musicOpen",musicIsOpen);
                    minerMainView.postInvalidate();
                    getOptionClickListener().optionClick(2);
                    RxLogTool.e("click 音乐开关 :"+musicIsOpen);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
    }

    public interface OptionClickListener{
        void optionClick(int tag);
    }
}
