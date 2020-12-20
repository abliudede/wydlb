package com.wydlb.first.inner;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.utils.RxSharedPreferencesUtil;

/**
 * Created by lrz on 2017/10/20.
 */

public class MyCountDownTimer extends CountDownTimer {

    Context mContext;
    TextView mTextView;
    String mHint;
    long currentMillisUntilFinished=0;
    public MyCountDownTimer(long millisInFuture, long countDownInterval, Context context,TextView textview,String hint) {
        super(millisInFuture, countDownInterval);
        this.mContext=context;
        this.mTextView=textview;
        mHint=hint;
    }

    public long getCurrentMillisUntilFinished() {
        return currentMillisUntilFinished;
    }

    public void setCurrentMillisUntilFinished(long currentMillisUntilFinished) {
        this.currentMillisUntilFinished = currentMillisUntilFinished;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        setCurrentMillisUntilFinished(millisUntilFinished);
        mTextView.setText(String.format(mContext.getResources().getString(R.string.get_sms_again_hint),String.valueOf(millisUntilFinished / 1000) ));
//        mTextView.setBackgroundResource(R.drawable.gray_bg);
        mTextView.setEnabled(false);
        mTextView.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
    }

    @Override
    public void onFinish() {
        mTextView.setEnabled(true);
        mTextView.setText(mHint);
//        mTextView.setBackgroundResource(R.drawable.gradient_bg);
        mTextView.setTextColor(mContext.getResources().getColor(R.color.blue_color));

        RxSharedPreferencesUtil.getInstance().remove(Constant.SMS_TIME);
    }
}
