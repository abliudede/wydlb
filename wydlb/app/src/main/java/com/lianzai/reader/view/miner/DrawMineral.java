package com.lianzai.reader.view.miner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.miner.entity.MineralEntity;
import com.lianzai.reader.view.miner.evaluator.MineralEvaluator;
import com.lianzai.reader.view.miner.evaluator.MineralYEvaluator;
import com.lianzai.reader.view.miner.interpolator.BackInterpolator;
import com.lianzai.reader.view.miner.interpolator.EasingType;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lrz on 2018/2/2.
 * 矿石
 */

public class DrawMineral extends BaseDraw {
    private int screenWidth = 0;
    private int screenHeigth = 0;

    Rect rectValue=null;


    MinerMainView minerMainView;



    Paint paint;

    ValueAnimator anim;


    MineralClickListner mineralClickListner;


    int downX = 0;
    int downY = 0;

    String normalValue="矿晶";

    /**
     * 普通矿石
     */
    private Bitmap bitmapNormalMineral = null;

    /**
     * 光圈背景 动销
     */
    private Bitmap bitmapLight = null;

    /**
     * 金矿石
     */
    private Bitmap bitmapGlodMineral = null;

    CopyOnWriteArrayList<MineralEntity> mineralEntities=new CopyOnWriteArrayList<>();

    int margin=5;

    Canvas canvas;

    Random random;

    ValueAnimator animRepeat;

    public DrawMineral(Context context,MinerMainView minerMainView) {
        if (screenWidth == 0 && screenHeigth == 0) {
            this.minerMainView=minerMainView;
            random=new Random();
            bitmapNormalMineral = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_normal_mineral);
            bitmapGlodMineral = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_glod_mineral);
//            bitmapLight = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg_mineral_light);

            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeigth = dm.heightPixels;

            paint=new Paint();
            paint.setColor(Color.parseColor("#cccccc"));
            paint.setTextSize(RxImageTool.sp2px(12));

            rectValue=new Rect();
            paint.getTextBounds(normalValue, 0, normalValue.length(), rectValue);


        }
    }

    public MineralClickListner getMineralClickListner() {
        return mineralClickListner;
    }

    public void setMineralClickListner(MineralClickListner mineralClickListner) {
        this.mineralClickListner = mineralClickListner;
    }

    public CopyOnWriteArrayList<MineralEntity> getMineralEntities() {
        return mineralEntities;
    }

    public void setMineralEntities(CopyOnWriteArrayList<MineralEntity> data) {

        int minX=bitmapNormalMineral.getWidth();
        int maxX=screenWidth-bitmapNormalMineral.getWidth();

        int minY=bitmapNormalMineral.getHeight();
        int maxY=screenHeigth-bitmapNormalMineral.getHeight()-rectValue.height()-margin;

        for(int i=0;i<data.size();i++){

            int x=random.nextInt(maxX-minX+1)+minX;
            int y=random.nextInt(maxY-minY+1)+minY;
            MineralEntity mineralEntity=data.get(i);
            mineralEntity.position=i;
            mineralEntity.rect=rectValue;
            mineralEntity.setRealValue(mineralEntity.getRealValue());
            if (mineralEntity.tag==2){
                mineralEntity.setX((screenWidth-bitmapGlodMineral.getWidth())/2);
                mineralEntity.setY((screenHeigth-bitmapGlodMineral.getHeight())/2);
            }else{
                mineralEntity.setX(x);
                mineralEntity.setY(y);
            }

            mineralEntities.add(mineralEntity);
            startRepeatAnimation(mineralEntity);
        }
        minerMainView.invalidate();

    }

    @Override
    void onDraw(Canvas canvas) {

    }


    public void drawMineral(Canvas canvas){
        this.canvas=canvas;
        if (mineralEntities.size()>0) {
            Iterator<MineralEntity> mineralEntityIterator = mineralEntities.listIterator();
            while (mineralEntityIterator.hasNext()) {
                MineralEntity mineralEntity = mineralEntityIterator.next();
                if (mineralEntity.isShow()){
                    if (mineralEntity.tag==1){//普通的矿晶
                        Rect rect = new Rect((int)mineralEntity.x, (int)mineralEntity.y,
                                (int)mineralEntity.x + bitmapNormalMineral.getWidth(),
                                (int)mineralEntity.y + bitmapNormalMineral.getHeight());
                        canvas.drawBitmap(bitmapNormalMineral, null, rect, null);
                        mineralEntity.rect=rect;

                        paint.getTextBounds(mineralEntity.value, 0, mineralEntity.value.length(), rectValue);
                        canvas.drawText(mineralEntity.getValue(),rect.centerX()-rectValue.width()/2,rect.centerY()+bitmapNormalMineral.getHeight()/2+rectValue.height(),paint);
                    }else if(mineralEntity.tag==2){//金矿晶

                        Rect rect = new Rect((int)mineralEntity.x, (int)mineralEntity.y,
                                (int)mineralEntity.x + bitmapGlodMineral.getWidth(),
                                (int)mineralEntity.y + bitmapGlodMineral.getHeight());

                        //动效背景
                        canvas.drawBitmap(bitmapLight,null,rect,null);

                        canvas.drawBitmap(bitmapGlodMineral, null, rect, null);
                        mineralEntity.rect=rect;

                        paint.getTextBounds(mineralEntity.value, 0, mineralEntity.value.length(), rectValue);
                        canvas.drawText(mineralEntity.getValue(),rect.centerX()-rectValue.width()/2,rect.centerY()+bitmapGlodMineral.getHeight()/2+rectValue.height(),paint);
                    }
                }

            }
        }
    }

    @Override
    void onDestroy() {
        bitmapNormalMineral.recycle();
        bitmapGlodMineral.recycle();

        if (null!=anim)
            anim.cancel();

        if (null!=animRepeat)
            animRepeat.cancel();
    }

    public void onTouch(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = (int) event.getX();
                downY = (int) event.getY();

                RxLogTool.e("onTouch downX:"+downX+"--downY:"+downY);

                for(int i=mineralEntities.size()-1;i>=0;i--){
                    MineralEntity mineralEntity=mineralEntities.get(i);
                    if (mineralEntity.isClick()&&mineralEntity.rect.contains(downX,downY)){
                        mineralEntity.setClick(false);
                        RxLogTool.e("click:"+mineralEntity.toString());
                        if (null!=getMineralClickListner()){
                            getMineralClickListner().receiveMineralClick(mineralEntity);
                        }
                        mineralEntities.set(mineralEntity.position,mineralEntity);
                        startAnimation(mineralEntity);
                        //一次只去掉一个

                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }

    }

    private void startAnimation(final MineralEntity currentMineralEntity){
        MineralEntity endMineralEntity=new MineralEntity(-bitmapNormalMineral.getWidth()/2,-bitmapGlodMineral.getHeight()/2,currentMineralEntity.tag,currentMineralEntity.value,currentMineralEntity.rect,currentMineralEntity.position);

        anim=ValueAnimator.ofObject(new MineralEvaluator(),currentMineralEntity,endMineralEntity);
        anim.setDuration(1000);
        anim.setInterpolator(new BackInterpolator(EasingType.IN, 0f));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                MineralEntity entity=(MineralEntity)animation.getAnimatedValue();
                if (mineralEntities.get(entity.position).isShow()){
                    mineralEntities.set(entity.position,entity);
                }
                minerMainView.invalidate();

            }
        });
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentMineralEntity.setShow(false);
                mineralEntities.set(currentMineralEntity.position,currentMineralEntity);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.start();
    }

    /**
     * 矿晶上下来回移动
     * @param mineralEntity
     */
    private void startRepeatAnimation(MineralEntity mineralEntity){
        MineralEntity endMineralEntity=new MineralEntity(mineralEntity.x,mineralEntity.y+random.nextInt(20)+10,mineralEntity.tag,mineralEntity.value,mineralEntity.rect,mineralEntity.position);

        animRepeat=ValueAnimator.ofObject(new MineralYEvaluator(),mineralEntity,endMineralEntity);

        animRepeat.setDuration(1500);
        animRepeat.setRepeatMode(ValueAnimator.REVERSE);
        animRepeat.setRepeatCount(-1);
        animRepeat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MineralEntity entity=(MineralEntity)animation.getAnimatedValue();
                if (mineralEntities.get(entity.position).isShow()){
                    mineralEntities.set(entity.position,entity);
                }else{
                    entity.setShow(false);
                    mineralEntities.set(entity.position,entity);
                    animation.cancel();
                }
                minerMainView.invalidate();
            }
        });


        animRepeat.start();
    }

    public  interface MineralClickListner{
        void receiveMineralClick(MineralEntity entity);
    }
}
