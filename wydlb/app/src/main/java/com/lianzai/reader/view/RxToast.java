package com.lianzai.reader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.inner.Counter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import static com.lianzai.reader.utils.RxTool.getContext;

/**
 * @author vondear 在系统的Toast基础上封装
 */

@SuppressLint("InflateParams")
public class RxToast {

    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";


    //*******************************************普通 使用ApplicationContext 方法*********************
    /**
     * Toast 替代方法 ：立即显示无需等待
     */
    private static Toast mToast;
    private static long mExitTime;


    public static Toast custom(@NonNull String message) {
        Toast currentToast = new Toast(BuglyApplicationLike.getContext());

        View toastView = LayoutInflater.from(BuglyApplicationLike.getContext()).inflate(R.layout.toast_layout, null);
        TextView toastTextView=toastView.findViewById(R.id.toast_text);
        ImageView iv_status=toastView.findViewById(R.id.iv_status);
        iv_status.setVisibility(View.GONE);

        toastTextView.setText(message);
        currentToast.setGravity(Gravity.CENTER,0, 0);
        currentToast.setView(toastView);
        currentToast.setDuration(Toast.LENGTH_SHORT);

        return currentToast;
    }

    public static Toast custom(@NonNull String message,int type) {
        Toast currentToast = new Toast(BuglyApplicationLike.getContext());

        View toastView = LayoutInflater.from(BuglyApplicationLike.getContext()).inflate(R.layout.toast_layout, null);
        TextView toastTextView=toastView.findViewById(R.id.toast_text);
        ImageView iv_status=toastView.findViewById(R.id.iv_status);

        if (type== Constant.ToastType.TOAST_NORMAL){
            iv_status.setImageResource(R.mipmap.icon_toast_information);
        }else if(type== Constant.ToastType.TOAST_SUCCESS){
            iv_status.setImageResource(R.mipmap.icon_toast_success);
        }else if(type== Constant.ToastType.TOAST_ERROR){
            iv_status.setImageResource(R.mipmap.icon_toast_fail);
        }else if(type== Constant.ToastType.TOAST_NETWORK_DISCONNECT){
            iv_status.setImageResource(R.mipmap.icon_toast_network_disconnect);
        }

        toastTextView.setText(message);
        currentToast.setGravity(Gravity.CENTER,0, 0);
        currentToast.setView(toastView);
        currentToast.setDuration(Toast.LENGTH_SHORT);
        return currentToast;
    }


    public static void customAward(@NonNull String message,int awardAmount) {
        Toast currentToast = new Toast(BuglyApplicationLike.getContext());

        View toastView = LayoutInflater.from(BuglyApplicationLike.getContext()).inflate(R.layout.toast_award_layout, null);
        TextView toast_title_text=toastView.findViewById(R.id.toast_title_text);
        TextView tv_jinzuan_tag=toastView.findViewById(R.id.tv_jinzuan_tag);

        tv_jinzuan_tag.setCompoundDrawables(RxImageTool.getDrawable(R.mipmap.jinzuan_mid),null,null,null);

        TextView toast_amount_text=toastView.findViewById(R.id.toast_amount_text);

//        SpannableStringBuilder builder = new SpannableStringBuilder(awardAmount);
//        builder.setSpan(new AbsoluteSizeSpan(BuglyApplicationLike.getContext().getResources().getDimensionPixelSize(R.dimen.text_size_13)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new AbsoluteSizeSpan(BuglyApplicationLike.getContext().getResources().getDimensionPixelSize(R.dimen.text_size_16)),1,awardAmount.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        toast_amount_text.setText("+" + awardAmount);
//        Counter counter1 = new Counter(toast_amount_text, awardAmount, 600);
//        toast_amount_text.removeCallbacks(counter1);
//        toast_amount_text.post(counter1);

        toast_title_text.setText(message);
        currentToast.setGravity(Gravity.CENTER,0, 0);
        currentToast.setView(toastView);
        currentToast.setDuration(Toast.LENGTH_LONG);
        currentToast.show();
        //延长土司时间
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                currentToast.cancel();
//            }
//        },12000);
    }

    //===========================================内需方法============================================


    //******************************************系统 Toast 替代方法***************************************

    public static final void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackground(drawable);
        else
            view.setBackgroundDrawable(drawable);
    }

    public static final Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(id);
        else
            return context.getResources().getDrawable(id);
    }

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context Context
     * @param str     要显示的字符串
     * @param isLong  Toast.LENGTH_LONG / Toast.LENGTH_SHORT
     */
    public static void showToast(Context context, String str, boolean isLong) {
        if (isLong) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    public static void showToastShort(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    public static void showToastShort(int resId) {
        Toast.makeText(getContext(), getContext().getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    public static void showToastLong(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    public static void showToastLong(int resId) {
        Toast.makeText(getContext(), getContext().getString(resId), Toast.LENGTH_LONG).show();
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param msg 显示内容
     */
    public static void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param resId String资源ID
     */
    public static void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), getContext().getString(resId), Toast.LENGTH_LONG);
        } else {
            mToast.setText(getContext().getString(resId));
        }
        mToast.show();
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    String资源ID
     * @param duration 显示时长
     */
    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }
    //===========================================Toast 替代方法======================================

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static boolean doubleClickExit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            RxToast.custom("再按一次退出").show();
            mExitTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    public static void gc(){
    }

    public static void showJinzuanCharge(JinZuanChargeBean jinzuan){
        if(null != jinzuan){
            String temp = "";
            if(jinzuan.getData().getApproveType() == 1){
                temp = "，审核通过后展示";
            }

            if(jinzuan.getData().getKdiamCount() > 0){
                //金钻消耗的情况
                String charge = String.valueOf(jinzuan.getData().getKdiamCount());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_for_change, null);
                TextView suanlicount = (TextView) view.findViewById(R.id.toast_text_count);
                TextView toast_text_des = (TextView) view.findViewById(R.id.toast_text_des);
                suanlicount.setText("金钻-" + charge);
                switch (jinzuan.getData().getType()){
                    case 1:
                        toast_text_des.setText("发表成功" + temp);
                        break;
                    case 2:
                        toast_text_des.setText("发表成功" + temp);//评论成功
                        break;
                    case 3:
                        toast_text_des.setText("发表成功" + temp);//回复成功
                        break;
                    case 4:
                        toast_text_des.setText("点赞成功");
                        break;
                }
                ToastUtil toast = new ToastUtil(getContext(), view, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }else {
                //没有金钻消耗的情况
                switch (jinzuan.getData().getType()){
                    case 1:
                        custom("发表成功" + temp).show();
                        break;
                    case 2:
                        custom("发表成功" + temp).show();
                        break;
                    case 3:
                        custom("发表成功" + temp).show();
                        break;
                    case 4:
                        custom("点赞成功").show();
                        break;
                }
            }
        }
    }
}
