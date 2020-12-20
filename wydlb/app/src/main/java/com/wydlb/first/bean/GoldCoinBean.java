package com.wydlb.first.bean;

/**
 * Created by lrz on 2018/4/17.
 */

public class GoldCoinBean {
    private int amount;
    private int price;

    public GoldCoinBean(int amount, int price) {
        this.amount = amount;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
