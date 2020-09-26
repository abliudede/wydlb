package com.lianzai.reader.view.miner.evaluator;

import android.animation.TypeEvaluator;

import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.miner.entity.MinerCarEntity;

/**
 * Created by lrz on 2018/2/5.
 */

public class MinerCarEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        MinerCarEntity startMinerCarEntity=(MinerCarEntity)startValue;
        MinerCarEntity endMinerCarEntity=(MinerCarEntity)endValue;
        // 根据fraction来计算当前动画的x和y的值
        float x = startMinerCarEntity.getX() + fraction * (endMinerCarEntity.getX() - startMinerCarEntity.getX());
        float y = startMinerCarEntity.getY() + fraction * (endMinerCarEntity.getY() - startMinerCarEntity.getY());
        String carName=startMinerCarEntity.getCarName();
        int position=startMinerCarEntity.getPosition();

        boolean isMining;
//
        if (y>endMinerCarEntity.getY()){
            isMining=true;
            RxLogTool.e("isMining:"+isMining+"---y:"+y);
        }else{
            isMining=false;
            RxLogTool.e("isMining:"+isMining+"---y:"+y);
        }

        MinerCarEntity minerCarEntity=new MinerCarEntity(x,y,carName,position,isMining);

        return minerCarEntity;
    }
}
