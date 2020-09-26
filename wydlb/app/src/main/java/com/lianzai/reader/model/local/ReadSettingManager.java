package com.lianzai.reader.model.local;


import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.utils.SharedPreUtils;
import com.lianzai.reader.view.page.PageMode;
import com.lianzai.reader.view.page.PageStyle;

/**
 * Created by newbiechen on 17-5-17.
 * 阅读器的配置管理
 */

public class ReadSettingManager {
    /*************实在想不出什么好记的命名方式。。******************/
    public static final int READ_BG_DEFAULT = 0;
    public static final int READ_BG_1 = 1;
    public static final int READ_BG_2 = 2;
    public static final int READ_BG_3 = 3;
    public static final int READ_BG_4 = 4;
    public static final int READ_BG_5 = 5;
    public static final int READ_BG_6 = 6;
    public static final int READ_BG_7 = 7;
    public static final int READ_BG_8 = 8;
    public static final int READ_BG_9 = 9;
    public static final int NIGHT_MODE = 10;

    public static final String SHARED_READ_BG = "shared_read_bg";
    public static final String SHARED_READ_BRIGHTNESS = "shared_read_brightness";
    public static final String SHARED_READ_IS_BRIGHTNESS_AUTO = "shared_read_is_brightness_auto";
    public static final String SHARED_READ_TEXT_SIZE = "shared_read_font_text_size";
    public static final String SHARED_READ_IS_TEXT_DEFAULT = "shared_read_text_font_default";
    public static final String SHARED_READ_PAGE_MODE = "shared_read_mode";
    public static final String SHARED_READ_NIGHT_MODE = "shared_night_mode";
    public static final String SHARED_READ_VOLUME_TURN_PAGE = "shared_read_volume_turn_page";
    public static final String SHARED_READ_FULL_SCREEN = "shared_read_full_screen";
    public static final String SHARED_READ_CONVERT_TYPE = "shared_read_convert_type";

    public static final String SHARED_READ_SINGLE_HAND_TYPE = "shared_read_single_hand_type";

    public static final String SHARED_READ_TEXT_PADDING_TYPE = "shared_text_padding_type";

    public static final String SHARED_READ_SCREEN_KEEP_ON_TYPE = "shared_read_screen_keep_on_type";

    public static final String SHARED_READ_FONT_TYPE = "shared_font_type";

    public static final String SHARED_READ_LISTEN_BOOK_TYPE = "shared_listen_book_type";

    public static final String SHARED_READ_LISTEN_VOICE_SPPED = "shared_listen_book_voice_speed";
    public static final String SHARED_READ_LISTEN_VOICER = "shared_listen_book_voicer2";
    public static final String SHARED_LISTEN_BOOK_NEED_REFRESH = "shared_listen_book_need_refresh";

    public static final String SHARED_READ_PROTECTED_EYE_TYPE = "shared_read_protected_eye_type";

    private static volatile ReadSettingManager sInstance;

    private SharedPreUtils sharedPreUtils;

    public static ReadSettingManager getInstance() {
        if (sInstance == null) {
            synchronized (ReadSettingManager.class) {
                if (sInstance == null) {
                    sInstance = new ReadSettingManager();
                }
            }
        }
        return sInstance;
    }

    private ReadSettingManager() {
        sharedPreUtils = SharedPreUtils.getInstance();
    }

    public void setPageStyle(PageStyle pageStyle) {
        sharedPreUtils.putInt(SHARED_READ_BG, pageStyle.ordinal());
    }




    public void setTextPaddingStyle(int textPaddingStyle) {
        sharedPreUtils.putInt(SHARED_READ_TEXT_PADDING_TYPE,textPaddingStyle);
    }

    public void setFontStyle(int fontStyle) {
        sharedPreUtils.putInt(SHARED_READ_FONT_TYPE,fontStyle);
    }


    public void setBrightness(int progress) {
        sharedPreUtils.putInt(SHARED_READ_BRIGHTNESS, progress);
    }

    public void setVoiceSpeed(int speed) {
        sharedPreUtils.putInt(SHARED_READ_LISTEN_VOICE_SPPED, speed);
    }

    public int getVoiceSpeed() {
        return sharedPreUtils.getInt(SHARED_READ_LISTEN_VOICE_SPPED,50);
    }

//    public void setNeedRefresh(boolean need){
//        sharedPreUtils.putBoolean(SHARED_LISTEN_BOOK_NEED_REFRESH, need);
//    }
//
//    public boolean getNeedRefresh(){
//        return sharedPreUtils.getBoolean(SHARED_LISTEN_BOOK_NEED_REFRESH,false);
//    }

    public void setVoicer(String voicer) {
        sharedPreUtils.putString(SHARED_READ_LISTEN_VOICER, voicer);
    }

    public String getVoicer() {
        return sharedPreUtils.getString(SHARED_READ_LISTEN_VOICER,"0");
    }

    public void setAutoBrightness(boolean isAuto) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, isAuto);
    }

//    public boolean isListenBookStyle() {
//        return sharedPreUtils.getBoolean(SHARED_READ_LISTEN_BOOK_TYPE, false);
//    }
//
//    public void setListenBookStyle(boolean isListenBook) {
//        sharedPreUtils.putBoolean(SHARED_READ_LISTEN_BOOK_TYPE, isListenBook);
//    }

    public void setOpenProtectedEye(boolean isOpenProtectedEye) {
        sharedPreUtils.putBoolean(SHARED_READ_PROTECTED_EYE_TYPE, isOpenProtectedEye);
    }

    public boolean isOpenProtectedEye() {
        return sharedPreUtils.getBoolean(SHARED_READ_PROTECTED_EYE_TYPE, false);
    }

    public void setSingleHand(boolean isSingleHand) {
        sharedPreUtils.putBoolean(SHARED_READ_SINGLE_HAND_TYPE, isSingleHand);
    }

    public void setScreenKeepOn(boolean isKeepOn) {
        sharedPreUtils.putBoolean(SHARED_READ_SCREEN_KEEP_ON_TYPE, isKeepOn);
    }

    public boolean isScreenKeepOn() {
        return sharedPreUtils.getBoolean(SHARED_READ_SCREEN_KEEP_ON_TYPE, true);
    }

    public void setDefaultTextSize(boolean isDefault) {
        sharedPreUtils.putBoolean(SHARED_READ_IS_TEXT_DEFAULT, isDefault);
    }

    public void setTextSize(int textSize) {
        sharedPreUtils.putInt(SHARED_READ_TEXT_SIZE, textSize);
    }

    public void setPageMode(PageMode mode) {
        sharedPreUtils.putInt(SHARED_READ_PAGE_MODE, mode.ordinal());
    }

    public void setNightMode(boolean isNight) {
        sharedPreUtils.putBoolean(SHARED_READ_NIGHT_MODE, isNight);
    }

    public int getBrightness() {
        return sharedPreUtils.getInt(SHARED_READ_BRIGHTNESS, 40);
    }

    public boolean isBrightnessAuto() {//默认跟随系统亮度
        return sharedPreUtils.getBoolean(SHARED_READ_IS_BRIGHTNESS_AUTO, true);
    }

    /**
     * 是否是单手模式
     * @return
     */
    public boolean isSingleHand() {
        return sharedPreUtils.getBoolean(SHARED_READ_SINGLE_HAND_TYPE, false);
    }

    public int getTextSize() {
        return sharedPreUtils.getInt(SHARED_READ_TEXT_SIZE, ScreenUtils.spToPx(18));
    }

    public int getFontStyle() {
        return sharedPreUtils.getInt(SHARED_READ_FONT_TYPE, Constant.ReadFontStyle.SYSTEM_FONT);
    }

    public int getTextPaddingStyle() {//默认中等边距
        return sharedPreUtils.getInt(SHARED_READ_TEXT_PADDING_TYPE, Constant.ReadPaddingStyle.PADDING_DEFAULT);
    }

    public boolean isDefaultTextSize() {
        return sharedPreUtils.getBoolean(SHARED_READ_IS_TEXT_DEFAULT, true);
    }


    public PageMode getPageMode() {
        int mode = sharedPreUtils.getInt(SHARED_READ_PAGE_MODE, PageMode.SIMULATION.ordinal());
        return PageMode.values()[mode];
    }

    public PageStyle getPageStyle() {
        int style = sharedPreUtils.getInt(SHARED_READ_BG, PageStyle.BG_0.ordinal());//默认是第一个背景
        if(style >= 9){
            style = 8;
        }
        return PageStyle.values()[style];
    }

    public boolean isNightMode() {
        return sharedPreUtils.getBoolean(SHARED_READ_NIGHT_MODE, false);
    }

    public void setVolumeTurnPage(boolean isTurn) {
        sharedPreUtils.putBoolean(SHARED_READ_VOLUME_TURN_PAGE, isTurn);
    }

    public boolean isVolumeTurnPage() {//默认音量键翻页
        return sharedPreUtils.getBoolean(SHARED_READ_VOLUME_TURN_PAGE, true);
    }

    public void setFullScreen(boolean isFullScreen) {
        sharedPreUtils.putBoolean(SHARED_READ_FULL_SCREEN, isFullScreen);
    }

    public boolean isFullScreen() {
        return sharedPreUtils.getBoolean(SHARED_READ_FULL_SCREEN, false);
    }

    public void setConvertType(int convertType) {
        sharedPreUtils.putInt(SHARED_READ_CONVERT_TYPE, convertType);
    }

    public int getConvertType() {
        return sharedPreUtils.getInt(SHARED_READ_CONVERT_TYPE, 0);
    }
}
