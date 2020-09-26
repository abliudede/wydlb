package com.lianzai.reader.view.miner;

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

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.miner.entity.MinerCarEntity;
import com.lianzai.reader.view.miner.evaluator.MinerCarEvaluator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lrz on 2018/2/2.
 */

public class DrawMinerCar extends BaseDraw {
    private int screenWidth = 0;
    private int screenHeigth = 0;

    Rect doorRect;
    Rect rectCarName=null;
    List<MinerCarEntity>minerCarEntities=new ArrayList<>();

    Paint paint;

    ValueAnimator anim;

    /**
     * 采矿回来的矿车
     */
    private Bitmap bitmapMinerCarBack = null;

    /**
     * 去采矿的矿车
     */
    private Bitmap bitmapMinerCarGo = null;

    MinerMainView minerMainView;

    float targetX=394;
    float targetY=785;

    public DrawMinerCar(Context context,MinerMainView minerMainView) {
        if (screenWidth == 0 && screenHeigth == 0) {
            this.minerMainView=minerMainView;

            bitmapMinerCarBack = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_miner_car_back);
            bitmapMinerCarGo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_miner_car_go);

            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            screenWidth = dm.widthPixels;
            screenHeigth = dm.heightPixels;

            paint=new Paint();
            paint.setColor(Color.parseColor("#cccccc"));
            paint.setTextSize(RxImageTool.sp2px(12));

            MinerCarEntity minerCarEntity=new MinerCarEntity();
            minerCarEntity.carName="宿主矿机";
            minerCarEntity.x=88;
            minerCarEntity.y=1383;


            rectCarName=new Rect();
            paint.getTextBounds(minerCarEntity.carName, 0, minerCarEntity.carName.length(), rectCarName);

            minerCarEntities.add(minerCarEntity);

            for(MinerCarEntity entity:minerCarEntities){
                startAnimation(entity);
            }
        }
    }

    public void setDoorRect(Rect doorRect){
        this.doorRect=doorRect;
        targetX=doorRect.left;
        targetY=doorRect.bottom;
    }

    @Override
    void onDraw(Canvas canvas) {

    }

    public void drawMinerCar(Canvas canvas){
        if (minerCarEntities.size()>0){
            Iterator<MinerCarEntity> minerCarEntityIterator=minerCarEntities.listIterator();
            while (minerCarEntityIterator.hasNext()){
                MinerCarEntity minerCarEntity=minerCarEntityIterator.next();
                if (minerCarEntity.isMining){
                    minerCarEntity.carName="去采矿的路上";
                    Rect rect = new Rect((int)minerCarEntity.x, (int)minerCarEntity.y,
                            (int)minerCarEntity.x + bitmapMinerCarGo.getWidth(),
                            (int)minerCarEntity.y + bitmapMinerCarGo.getHeight());

                    canvas.drawText(minerCarEntity.carName,rect.right-rectCarName.width()/2,rect.top+rectCarName.height()/2,paint);
                    canvas.drawBitmap(bitmapMinerCarGo, null, rect, null);
                }else{
                    minerCarEntity.carName="采矿回来的路上";

                    Rect rect = new Rect((int)minerCarEntity.x, (int)minerCarEntity.y,
                            (int)minerCarEntity.x + bitmapMinerCarBack.getWidth(),
                            (int)minerCarEntity.y + bitmapMinerCarBack.getHeight());

                    canvas.drawText(minerCarEntity.carName,rect.right-rectCarName.width()/2,rect.top+rectCarName.height()/2,paint);
                    canvas.drawBitmap(bitmapMinerCarBack, null, rect, null);
                }
                RxLogTool.e(minerCarEntity.carName);
            }
        }
    }

    private void startAnimation(final MinerCarEntity minerCarEntity){

        MinerCarEntity endMinerCarEntity=new MinerCarEntity(targetX,targetY,minerCarEntity.getCarName(),minerCarEntity.getPosition(),true);

        anim=ValueAnimator.ofObject(new MinerCarEvaluator(),minerCarEntity,endMinerCarEntity);

        anim.setDuration(1000);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(-1);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                MinerCarEntity entity=(MinerCarEntity)animation.getAnimatedValue();
                minerCarEntities.set(entity.getPosition(),entity);
                minerMainView.invalidate();
            }
        });

        anim.start();
    }

    @Override
    void onDestroy() {
        bitmapMinerCarBack.recycle();
        bitmapMinerCarGo.recycle();

        if (null!=anim)
            anim.cancel();
    }
}
