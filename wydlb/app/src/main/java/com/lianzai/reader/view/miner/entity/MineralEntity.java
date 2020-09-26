package com.lianzai.reader.view.miner.entity;

import android.graphics.Rect;

/**
 * Created by lrz on 2018/2/3.
 * 矿石实体
 */

public class MineralEntity {

    public float x,y;

    public int tag=1;//1，普通矿石 2，金矿石

    public Rect rect;

    public int position;

    boolean isClick=true;//是否可点击、默认都可点击

    private boolean isShow=true;

    float realValue;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public MineralEntity(float x, float y, int tag, String value, Rect rect, int position) {
        this.x = x;
        this.y = y;
        this.tag = tag;
        this.value = value;
        this.rect=rect;
        this.position=position;

    }


    public MineralEntity(int tag, String value,float realValue) {
        this.tag = tag;
        this.value = value;
        this.realValue=realValue;
    }

    public float getRealValue() {
        return realValue;
    }

    public void setRealValue(float realValue) {
        this.realValue = realValue;
    }

    public String value;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MineralEntity{" +
                "x=" + x +
                ", y=" + y +
                ", tag=" + tag +
                ", rect=" + rect +
                ", position=" + position +
                ", isClick=" + isClick +
                ", isShow=" + isShow +
                ", realValue=" + realValue +
                ", value='" + value + '\'' +
                '}';
    }
}
