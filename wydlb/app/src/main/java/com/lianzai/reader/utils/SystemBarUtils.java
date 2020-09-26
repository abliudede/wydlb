package com.lianzai.reader.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 基于 Android 4.4
 *
 * 主要参数说明:
 *
 * SYSTEM_UI_FLAG_FULLSCREEN : 隐藏StatusBar
 * SYSTEM_UI_FLAG_HIDE_NAVIGATION : 隐藏NavigationBar
 * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN: 视图扩展到StatusBar的位置，并且StatusBar不消失。
 * 这里需要一些处理，一般是将StatusBar设置为全透明或者半透明。之后还需要使用fitSystemWindows=防止视图扩展到Status
 * Bar上面(会在StatusBar上加一层View，该View可被移动)
 * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION: 视图扩展到NavigationBar的位置
 * SYSTEM_UI_FLAG_LAYOUT_STABLE:稳定效果
 * SYSTEM_UI_FLAG_IMMERSIVE_STICKY:保证点击任意位置不会退出
 *
 * 可设置特效说明:
 * 1. 全屏特效
 * 2. 全屏点击不退出特效
 * 3. 注意在19 <=sdk <=21 时候，必须通过Window设置透明栏
 */

public class SystemBarUtils {

    private static final int UNSTABLE_STATUS = View.SYSTEM_UI_FLAG_FULLSCREEN;
    private static final int UNSTABLE_NAV = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    private static final int STABLE_STATUS =
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    private static final int STABLE_NAV = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    private static final int EXPAND_STATUS = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    private static final int EXPAND_NAV = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

    //阅读界面视图全屏无状态栏
    private static final int READ_STATUS1 = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;


    //阅读界面视图全屏有状态栏
    private static final int READ_STATUS2 = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;


    //阅读界面视图全屏无状态栏
    private static final int READ_STATUS3 = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;


    //设置隐藏StatusBar(点击任意地方会恢复)
    public static void hideUnStableStatusBar(Activity activity){
        //App全屏，隐藏StatusBar
        setFlag(activity,UNSTABLE_STATUS);
    }

    public static void showUnStableStatusBar(Activity activity){
        clearFlag(activity,UNSTABLE_STATUS);

    }

    //隐藏NavigationBar(点击任意地方会恢复)
    public static void hideUnStableNavBar(Activity activity){
        setFlag(activity,UNSTABLE_NAV);
    }

    public static void showUnStableNavBar(Activity activity){
        clearFlag(activity,UNSTABLE_NAV);
    }

    public static void hideStableStatusBar(Activity activity){
        //App全屏，隐藏StatusBar

        if (hasNotchInHuawei(activity)||hasNotchInOppo(activity)|hasNotchInVivo(activity)){
            setFlag(activity,EXPAND_STATUS);
        }else{
            setFlag(activity,STABLE_STATUS);
        }
    }

    public static void showStableStatusBar(Activity activity){
        clearFlag(activity,STABLE_STATUS);
    }

    public static void hideStableNavBar(Activity activity){
        //App全屏，隐藏StatusBar
        setFlag(activity,STABLE_NAV);
    }

    public static void showStableNavBar(Activity activity){
        clearFlag(activity,STABLE_NAV);
    }

    /**
     * 视图扩充到StatusBar
     */
    public static void expandStatusBar(Activity activity){
        setFlag(activity, EXPAND_STATUS);
    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置,全屏不隐藏导航栏
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    /**
     * 视图扩充到NavBar
     * @param activity
     */
    public static void expandNavBar(Activity activity){
        setFlag(activity, EXPAND_NAV);
    }

    public static void transparentStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 21){
            expandStatusBar(activity);
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
        else if (Build.VERSION.SDK_INT >= 19){
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
            activity.getWindow().setAttributes(attrs);
        }
    }

    public static void readStatusBar1(Activity activity){
        //要先清除视图标记
        if (Build.VERSION.SDK_INT >= 21){
//            clearFlag(activity,View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setFlag(activity, READ_STATUS1);
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
        else if (Build.VERSION.SDK_INT >= 19){
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
            activity.getWindow().setAttributes(attrs);
        }
    }

    public static void readStatusBar2(Activity activity){
        //要先清除视图标记
        if (Build.VERSION.SDK_INT >= 21){
//            clearFlag(activity,View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setFlag(activity, READ_STATUS2);
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
        else if (Build.VERSION.SDK_INT >= 19){
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
            activity.getWindow().setAttributes(attrs);
        }
    }

    public static void readStatusBar3(Activity activity){
        //要先清除视图标记
        if (Build.VERSION.SDK_INT >= 21){
            clearFlag(activity,View.SYSTEM_UI_FLAG_FULLSCREEN );
//            setFlag(activity, READ_STATUS2);
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
        else if (Build.VERSION.SDK_INT >= 19){
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
            activity.getWindow().setAttributes(attrs);
        }
    }




    public static void readStatusBar4(Activity activity){
        //要先清除视图标记
        if (Build.VERSION.SDK_INT >= 21){
//            clearFlag(activity,View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            setFlag(activity, READ_STATUS3);
            activity.getWindow()
                    .setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
        else if (Build.VERSION.SDK_INT >= 19){
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
            activity.getWindow().setAttributes(attrs);
        }
    }


    public static void setStatusBarColor(Activity activity,int color){
        if (Build.VERSION.SDK_INT >= 21){
            expandStatusBar(activity);
            activity.getWindow()
                    .setStatusBarColor(color);
        }
//        else if (Build.VERSION.SDK_INT >= 19){
//            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
//            attrs.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | attrs.flags);
//            activity.getWindow().setAttributes(attrs);
//        }
    }

    public static void transparentNavBar(Activity activity){
        if (Build.VERSION.SDK_INT >= 21){
            expandNavBar(activity);
            //下面这个方法在sdk:21以上才有
            activity.getWindow()
                    .setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
        }
    }

    public static void setNavBarColor(Activity activity,int color){
        if (Build.VERSION.SDK_INT >= 21){
            expandNavBar(activity);
            //下面这个方法在sdk:21以上才有
            activity.getWindow()
                    .setNavigationBarColor(color);
        }
    }

    public static void setFlag(Activity activity, int flag){
        if (Build.VERSION.SDK_INT >= 19){
            View decorView = activity.getWindow().getDecorView();
            int option = decorView.getSystemUiVisibility() | flag;
            decorView.setSystemUiVisibility(option);
        }
    }

    //取消flag
    public static void clearFlag(Activity activity, int flag){
        if (Build.VERSION.SDK_INT >= 19){
            View decorView = activity.getWindow().getDecorView();
            int option = decorView.getSystemUiVisibility() & (~flag);
            decorView.setSystemUiVisibility(option);
        }
    }

    public static void setToggleFlag(Activity activity, int option){
        if (Build.VERSION.SDK_INT >= 19){
            if (isFlagUsed(activity,option)){
                clearFlag(activity,option);
            }
            else {
                setFlag(activity,option);
            }
        }
    }

    /**
     * @param activity
     * @return flag是否已被使用
     */
    public static boolean isFlagUsed(Activity activity, int flag) {
        int currentFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
        if((currentFlag & flag)
                == flag) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * 所有刘海屏判断
     * @param context
     * @return
     */
    public static boolean hasNotch(Context context) {
        //先判断Android P
//        if(android.os.Build.VERSION.SDK_INT >= 28){
//            if(isAndroidP((Activity) context) != null){
//                return true;
//            }else {
//                return false;
//            }
//        }else{

        try{
            if(hasNotchInOppo(context)){
                return true;
            }else  if(hasNotchInVivo(context)){
                return true;
            }else if(hasNotchInHuawei(context)){
                return true;
            }else if(getInt("ro.miui.notch",context) == 1){
                return true;
            }else if(hasNotchInChuiZi(context)){
                return true;
            }else if(isOnePlus()){
                return true;
            }
        }catch (Exception e){

        }

//        }
        return false;
    }

//    /**
//     * Android P 刘海屏判断
//     * @param activity
//     * @return
//     */
//    public static DisplayCutout isAndroidP(Activity activity){
//        View decorView = activity.getWindow().getDecorView();
//        if (decorView != null && android.os.Build.VERSION.SDK_INT >= 28){
//            WindowInsets windowInsets = decorView.getRootWindowInsets();
//            if (windowInsets != null)
//                return windowInsets.getDisplayCutout();
//        }
//        return null;
//    }


    // 是否是一加手机,OnePlus 7 的型号为GM1900，收集信息之后可以用做判断
    public static boolean isOnePlus() {//Build.BRAND
        return "OnePlus".equals(Build.MANUFACTURER);
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    /**
     * 小米刘海屏判断.
     * @return 0 if it is not notch ; return 1 means notch
     * @throws IllegalArgumentException if the key exceeds 32 characters
     */
    public static int getInt(String key,Context activity) {
        int result = 0;
        if (isXiaomi()){
            try {
                ClassLoader classLoader = activity.getClassLoader();
                @SuppressWarnings("rawtypes")
                Class SystemProperties = classLoader.loadClass("android.os.SystemProperties");
                //参数类型
                @SuppressWarnings("rawtypes")
                Class[] paramTypes = new Class[2];
                paramTypes[0] = String.class;
                paramTypes[1] = int.class;
                Method getInt = SystemProperties.getMethod("getInt", paramTypes);
                //参数
                Object[] params = new Object[2];
                params[0] = new String(key);
                params[1] = new Integer(0);
                result = (Integer) getInt.invoke(SystemProperties, params);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * oppo刘海屏判断
     * @param context
     * @return
     */
    public static boolean hasNotchInOppo(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }
    /**
     * VIVO刘海屏判断
     * @param context
     * @return
     */
    public static boolean hasNotchInVivo(Context context){
        boolean hasNotch=false;
        try{
            ClassLoader cl = context.getClassLoader();
            Class ftFeature = cl.loadClass("android.util.FtFeature");
            Method[] methods = ftFeature.getDeclaredMethods();
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) {
                    Method method = methods[i];
                    if(method != null&&method.getName().equalsIgnoreCase("isFeatureSupport")) {
                        hasNotch = (boolean) method.invoke(ftFeature, 0x00000020);
                        break;
                    }
                }
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
        return  hasNotch;
    }

    /**
     * 华为刘海屏判断
     * @param context
     * @return
     */
    public static boolean hasNotchInHuawei(Context context){
        boolean hasNotch=false;
        try{
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method hasNotchInScreen = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            if(hasNotchInScreen != null) {
                hasNotch = (boolean) hasNotchInScreen.invoke(HwNotchSizeUtil);
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
        return  hasNotch;
    }

    /**
     * 锤子刘海屏判断
     * @param context
     * @return
     */
    public static boolean hasNotchInChuiZi(Context context){
        boolean hasNotch=false;
        try{
            ClassLoader cl = context.getClassLoader();
            Class DisplayUtilsSmt = cl.loadClass("smartisanos.api.DisplayUtilsSmt");
            Method hasNotchInScreen = DisplayUtilsSmt.getMethod("isFeatureSupport");
            if(hasNotchInScreen != null) {
                hasNotch = (boolean) hasNotchInScreen.invoke(0x00000001);
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
        return  hasNotch;
    }



    /**
     *  修改状态栏文字颜色，这里小米，魅族区别对待。
     */
    public static void setLightStatusBar(final Activity activity, final boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            switch (getLightStatusBarAvailableRomType()) {
                case AvailableRomType.MIUI:
                    MIUISetStatusBarLightMode(activity, dark);
                    break;

                case AvailableRomType.FLYME:
                    setFlymeLightStatusBar(activity, dark);

                    break;

                case AvailableRomType.ANDROID_NATIVE:
                    setAndroidNativeLightStatusBar(activity, dark);
                    break;

            }
        }
    }

    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isMiUIV7OrAbove()) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    class AvailableRomType {
        public static final int MIUI = 1;
        public static final int FLYME = 2;
        public static final int ANDROID_NATIVE = 3;
        public static final int NA = 4;
    }

    public static int getLightStatusBarAvailableRomType() {
        //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错
        if (isMiUIV7OrAbove()) {
            return AvailableRomType.ANDROID_NATIVE;
        }

        if (isMiUIV6OrAbove()) {
            return AvailableRomType.MIUI;
        }

        if (isFlymeV4OrAbove()) {
            return AvailableRomType.FLYME;
        }

        if (isAndroidMOrAbove()) {
            return AvailableRomType.ANDROID_NATIVE;
        }

        return AvailableRomType.NA;
    }

    //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
    //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
    private static boolean isFlymeV4OrAbove() {
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]\\.(\\d+\\.)+\\S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    //Android Api 23以上
    private static boolean isAndroidMOrAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";

    private static boolean isMiUIV6OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 4;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }

    }

    static boolean isMiUIV7OrAbove() {
        try {
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            String uiCode = properties.getProperty(KEY_MIUI_VERSION_CODE, null);
            if (uiCode != null) {
                int code = Integer.parseInt(uiCode);
                return code >= 5;
            } else {
                return false;
            }

        } catch (final Exception e) {
            return false;
        }

    }


}
