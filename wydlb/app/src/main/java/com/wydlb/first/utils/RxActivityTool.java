package com.wydlb.first.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.wydlb.first.R;
import com.wydlb.first.ui.activity.ActivityWebView;
import com.wydlb.first.ui.activity.MainActivity;

import java.util.List;
import java.util.Stack;


/**
 * Created by vondear on 2016/1/24.
 * 封装Activity相关工具类
 */
public class RxActivityTool {

    private static Stack<Activity> activityStack;

    /**
     * 添加Activity 到栈
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activityStack.contains(activity)){
            RxLogTool.e("addActivity 已存在:"+activity.getClass().getName());
        }
        activityStack.add(activity);
        RxLogTool.e("addActivity size:"+activityStack.size());
    }

    /**
     * 返回到首页-当账号被挤下线或者token失效，要返回到首页
     */
    public static void returnMainActivity(){
        try{
            if (null!=activityStack&&activityStack.size()>1){
                for (int i=0;i<activityStack.size();i++){
                    RxLogTool.e("activityStack.get(i):"+activityStack.get(i).getClass().getName());
                    if (activityStack.get(i).getClass()!= MainActivity.class){
                        activityStack.get(i).finish();//关闭
                        activityStack.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 清除掉之前栈中的主界面
     */
    public static void removeMainActivity(){
        try{
            if (null!=activityStack&&activityStack.size()>1){
                for (int i=0;i<activityStack.size();i++){
                    RxLogTool.e("activityStack.get(i):"+activityStack.get(i).getClass().getName());
                    if (activityStack.get(i).getClass() == MainActivity.class){
                        activityStack.get(i).finish();//关闭
                        activityStack.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 清除掉之前栈中的网页界面
     */
    public static void removeWebActivity(){
        try{
            if (null!=activityStack&&activityStack.size()>1){
                for (int i=0;i<activityStack.size();i++){
                    RxLogTool.e("activityStack.get(i):"+activityStack.get(i).getClass().getName());
                    if (activityStack.get(i).getClass() == ActivityWebView.class){
                        activityStack.get(i).finish();//关闭
                        activityStack.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取当前的Activity（堆栈中最后一个压入的)
     */
    public static Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        Activity activity = activityStack.lastElement();

    }

    public static void removeLastActivity(Activity activity){
        try{
            if (null!=activityStack){
                activityStack.remove(activity);
                RxLogTool.e("removeLastActivity size:"+activityStack.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    public static void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public static void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {

        }
    }

    public static Stack<Activity> getActivityStack() {
        return activityStack;
    }

    /**
     * 判断是否存在指定Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isExistActivity(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    /**
     * 打开指定的Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     */
    public static void launchActivity(Context context, String packageName, String className) {
        launchActivity(context, packageName, className, null);
    }

    /**
     * 打开指定的Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     */
    public static void launchActivity(Context context, String packageName, String className, Bundle bundle) {
        context.startActivity(RxIntentTool.getComponentNameIntent(packageName, className, bundle));
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinishAll(Context context, Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * 要求最低API为11
     * Activity 跳转
     * 跳转后Finish之前所有的Activity
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinishAll(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
        ((Activity) context).finish();
    }


    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivityAndFinish(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    /**
     * Activity 跳转
     *
     * @param context
     */
    public static void skipActivity(Context context,Intent intent) {
        context.startActivity(intent);
    }

    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivity(Context context, Class<?> goal) {
        Intent intent = new Intent(context, goal);
        context.startActivity(intent);
    }

    /**
     * Activity 跳转
     *
     * @param context
     * @param goal
     */
    public static void skipActivity(Context context, Class<?> goal, Bundle bundle) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void skipActivityForResult(Activity context, Class<?> goal, int requestCode) {
        Intent intent = new Intent(context, goal);
        context.startActivityForResult(intent, requestCode);
    }

    public static void skipActivityForResult(Activity context, Class<?> goal, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, goal);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 获取launcher activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @return launcher activity
     */
    public static String getLauncherActivity(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo info : infos) {
            if (info.activityInfo.packageName.equals(packageName)) {
                return info.activityInfo.name;
            }
        }
        return "no " + packageName;
    }
}
