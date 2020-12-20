package com.wydlb.first.view.miner.entity;

/**
 * Created by lrz on 2018/2/2.
 * 矿车实体类
 */

public class MinerCarEntity {
    public float x,y;
    public String carName;

    public int position;

    public boolean isMining=false;//是否采矿

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isMining() {
        return isMining;
    }

    public void setMining(boolean mining) {
        isMining = mining;
    }

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

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public MinerCarEntity() {
    }

    public MinerCarEntity(float x, float y, String carName, int position, boolean isMining) {
        this.x = x;
        this.y = y;
        this.carName = carName;
        this.position = position;
        this.isMining = isMining;
    }

    @Override
    public String toString() {
        return "MinerCarEntity{" +
                "x=" + x +
                ", y=" + y +
                ", carName='" + carName + '\'' +
                ", position=" + position +
                ", isMining=" + isMining +
                '}';
    }
}
