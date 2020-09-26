package com.lianzai.reader.inner;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxTimeTool;

/**
 * Created by lrz on 2017/10/20.
 */

public class MyCountDownTimerForMMSS extends CountDownTimer {

    Context mContext;
    TextView mTextView;
    String mHint;
    long currentMillisUntilFinished=0;
    private finishCall call;

    public finishCall getCall() {
        return call;
    }

    public void setCall(finishCall call) {
        this.call = call;
    }

    public MyCountDownTimerForMMSS(long millisInFuture, long countDownInterval, Context context, TextView textview, String hint) {
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
        mTextView.setText(RxTimeTool.formatTime(millisUntilFinished));
//        mTextView.setBackgroundResource(R.drawable.gray_bg);
//        mTextView.setEnabled(false);
//        mTextView.setTextColor(mContext.getResources().getColor(R.color.third_text_color));
    }

    @Override
    public void onFinish() {
        mTextView.setEnabled(true);
        mTextView.setText(mHint);
//        mTextView.setBackgroundResource(R.drawable.gradient_bg);
//        mTextView.setTextColor(mContext.getResources().getColor(R.color.blue_color));
        call.onFinish();
//        RxSharedPreferencesUtil.getInstance().remove(Constant.SMS_TIME);
    }

    public interface finishCall{
        void onFinish();
    }
}
