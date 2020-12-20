package com.wydlb.first.view.miner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.view.miner.entity.MineralEntity;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lrz on 2018/1/20.
 */

public class MinerMainView extends View implements  View.OnTouchListener ,DrawMineral.MineralClickListner,DrawBackground.OptionClickListener{


    DrawMineral.MineralClickListner mineralClickListner;

    DrawBackground.OptionClickListener optionClickListener;

    Paint paint;

    /**
     * 画笔
     */
    private Canvas canvas = null;

    /**
     * 游戏是否暂停
     */
    private boolean isPause = false;

    DrawBackground drawBackgound;

    DrawMinerCar drawMinerCar;


    DrawMineral drawMineral;

    CopyOnWriteArrayList<MineralEntity> mineralEntities=new CopyOnWriteArrayList<>();

    public MinerMainView(Context context) {
        super(context);
        init(context);
    }

    public MinerMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MinerMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawBackground.OptionClickListener getOptionClickListener() {
        return optionClickListener;
    }

    public void setOptionClickListener(DrawBackground.OptionClickListener optionClickListener) {
        this.optionClickListener = optionClickListener;
    }

    public DrawMineral.MineralClickListner getMineralClickListner() {
        return mineralClickListner;
    }

    public void setMineralClickListner(DrawMineral.MineralClickListner mineralClickListner) {
        this.mineralClickListner = mineralClickListner;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public DrawBackground getDrawBackgound() {
        return drawBackgound;
    }

    private  void init(Context context){
        RxTool.init(context);

        drawBackgound = new DrawBackground(context,this);
        drawBackgound.setOptionClickListener(this);
        drawMinerCar=new DrawMinerCar(context,this);

        drawMineral=new DrawMineral(context,this);
        drawMineral.setMineralClickListner(this);

        this.setOnTouchListener(this);
    }

    public CopyOnWriteArrayList<MineralEntity> getMineralEntities() {
        return mineralEntities;
    }

    public void setMineralEntities(CopyOnWriteArrayList<MineralEntity> mineralEntities) {
        this.mineralEntities = mineralEntities;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isPause){
            drawBackgound.onDraw(canvas);

//        drawMinerCar.setDoorRect(drawBackgound.getDoorRect());
//
//        drawMinerCar.drawMinerCar(canvas);

            if (drawMineral.getMineralEntities().size()==0){
                drawMineral.setMineralEntities(getMineralEntities());
            }
            drawMineral.drawMineral(canvas);
        }

    }

    /**
     * 释放资源
     */
    public void onDestroy(){
        drawBackgound.onDestroy();
        drawMineral.onDestroy();
        drawMinerCar.onDestroy();

        RxLogTool.e("释放资源..."+System.currentTimeMillis());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        drawMineral.onTouch(event);
        drawBackgound.onTouch(event);
        return true;
    }

    @Override
    public void receiveMineralClick(MineralEntity entity) {
        mineralClickListner.receiveMineralClick(entity);
    }

    @Override
    public void optionClick(int tag) {
        getOptionClickListener().optionClick(tag);
    }
}
