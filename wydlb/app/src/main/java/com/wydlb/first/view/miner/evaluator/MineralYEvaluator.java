package com.wydlb.first.view.miner.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.Rect;

import com.wydlb.first.view.miner.entity.MineralEntity;

/**
 * Created by lrz on 2018/2/5.
 */

public class MineralYEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        MineralEntity startMineralEntity=(MineralEntity)startValue;
        MineralEntity endMineralEntity=(MineralEntity)endValue;
        // 根据fraction来计算当前动画的x和y的值
        float x = startMineralEntity.getX();
        float y = startMineralEntity.getY() + fraction * (endMineralEntity.getY() - startMineralEntity.getY());
        int tag=startMineralEntity.getTag();
        String value=startMineralEntity.getValue();
        Rect rect=startMineralEntity.rect;

        float realValue=startMineralEntity.getRealValue();
        int position=startMineralEntity.position;
        MineralEntity mineralEntity=new MineralEntity(x,y,tag,value,rect,position);
        mineralEntity.setRealValue(realValue);
        return mineralEntity;
    }
}
