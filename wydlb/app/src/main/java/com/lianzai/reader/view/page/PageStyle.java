package com.lianzai.reader.view.page;

import androidx.annotation.ColorRes;

import com.lianzai.reader.R;


/**
 * Created by newbiechen on 2018/2/5.
 * 作用：页面的展示风格。
 */

public enum PageStyle {
    BG_0(R.color.nb_read_font_1, R.color.nb_read_bg_1),//背景选择1-月白
    BG_1(R.color.nb_read_font_2, R.color.nb_read_bg_2),//背景选择2-象牙白
    BG_7(R.color.nb_read_font_8, R.color.nb_read_bg_8),//背景选择3-羊皮纸

    BG_6(R.color.nb_read_font_7, R.color.nb_read_bg_7),//背景选择4-暗黑色
    BG_3(R.color.nb_read_font_4, R.color.nb_read_bg_4),//背景选择5-薄绿
    BG_5(R.color.nb_read_font_6, R.color.nb_read_bg_6),//背景选择6-卯の花色

    BG_4(R.color.nb_read_font_5, R.color.nb_read_bg_5),//背景选择7-鸟の子色
    BG_2(R.color.nb_read_font_3, R.color.nb_read_bg_3),//背景选择8-薄桜
//    BG_9(R.color.nb_read_font_10, R.color.nb_read_bg_10),//背景选择9-素描纸
//
//    BG_8(R.color.nb_read_font_9, R.color.nb_read_bg_9),//背景选择10-竖纹纸


    NIGHT(R.color.nb_read_font_night, R.color.nb_read_bg_night);

    private int fontColor;
    private int bgColor;

    PageStyle(@ColorRes int fontColor, @ColorRes int bgColor) {
        this.fontColor = fontColor;
        this.bgColor = bgColor;
    }

    public int getFontColor() {
        return fontColor;
    }


    public int getBgColor() {
        return bgColor;
    }
}
