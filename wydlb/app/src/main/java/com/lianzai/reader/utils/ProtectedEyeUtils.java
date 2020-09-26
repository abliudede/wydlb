package com.lianzai.reader.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.view.RxToast;

import java.lang.reflect.Method;

/**
 * Copyright (C), 2018
 * FileName: ProtectedEyeUtils
 * Author: lrz
 * Date: 2018/11/8 14:43
 * Description: ${DESCRIPTION} 护眼模式工具类
 */
public class ProtectedEyeUtils {
    static FrameLayout view;

    public static void openProtectedEyeMode(Context mContext){
        try{
            if (null==view){//防止打开app后手动修改权限导致闪退
                if (checkAlertWindowsPermission(mContext)){
                    WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//8.0+
                        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        params.type =  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

                    }else{
                        params.type =  WindowManager.LayoutParams.TYPE_TOAST;
                    }

                    int flag=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    params.format = PixelFormat.TRANSLUCENT;
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.height = WindowManager.LayoutParams.MATCH_PARENT;
                    params.flags=flag;


                    view=new FrameLayout(mContext);
                    view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));
                    RxLogTool.e("openProtectedEyeMode color:"+RxImageTool.getProtectedEyeColor(50));

                    view.setBackgroundColor(RxImageTool.getProtectedEyeColor(50));//设置过滤蓝光背景

                    windowManager.addView(view,params);

                    RxLogTool.e("ProtectedEyeUtils openProtectedEyeMode......");
                }else{
                    ReadSettingManager.getInstance().setOpenProtectedEye(false);//关闭护眼模式
                }
            }
        }catch (Exception e){
            RxToast.custom("您的设备不支持开启护眼模式",Constant.ToastType.TOAST_ERROR).show();
        }

    }

    /**
     * 判断 悬浮窗口权限是否打开
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean checkAlertWindowsPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            try {
                Object object = context.getSystemService(Context.APP_OPS_SERVICE);
                if (object == null) {
                    return false;
                }
                Class localClass = object.getClass();
                Class[] arrayOfClass = new Class[3];
                arrayOfClass[0] = Integer.TYPE;
                arrayOfClass[1] = Integer.TYPE;
                arrayOfClass[2] = String.class;
                Method method = localClass.getMethod("checkOp", arrayOfClass);
                if (method == null) {
                    return false;
                }
                Object[] arrayOfObject1 = new Object[3];
                arrayOfObject1[0] = 24;
                arrayOfObject1[1] = Binder.getCallingUid();
                arrayOfObject1[2] = context.getPackageName();
                int m = ((Integer) method.invoke(object, arrayOfObject1));
                return m == AppOpsManager.MODE_ALLOWED;
            } catch (Exception ex) {

            }
        }else{
            return true;
        }
        return  false;
    }


    public static void closeProtectedEyeMode(Context mContext){
        try {
            RxLogTool.e("ProtectedEyeUtils closeProtectedEyeMode......");
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            if (null!=view){
                windowManager.removeView(view);
                view=null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
