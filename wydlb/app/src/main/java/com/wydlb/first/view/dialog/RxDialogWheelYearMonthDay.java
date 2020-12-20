package com.wydlb.first.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wydlb.first.R;
import com.wydlb.first.utils.RxDeviceTool;
import com.wydlb.first.utils.RxTimeTool;
import com.wydlb.first.view.dialog.dialogWheel.DateArrayAdapter;
import com.wydlb.first.view.dialog.dialogWheel.NumericWheelAdapter;
import com.wydlb.first.view.dialog.dialogWheel.OnWheelChangedListener;
import com.wydlb.first.view.dialog.dialogWheel.WheelView;

import java.util.Calendar;

public class RxDialogWheelYearMonthDay extends RxDialog {
    private WheelView mYearView;
    private WheelView mMonthView;
    private WheelView mDayView;
    private int curYear;
    private int curMonth;
    private int curDay;
    private TextView mTvSure;
    private TextView mTvCancle;
    private Calendar mCalendar;
    private String mMonths[] = new String[]{"01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String mDays[] = new String[]{"01", "02", "03", "04", "05", "06", "07",
            "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    private int beginYear = 0;
    private int endYear = 0;
    private int divideYear = endYear - beginYear;

    public RxDialogWheelYearMonthDay(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        build(true);
    }

    public RxDialogWheelYearMonthDay(Context mContext, int beginYear,boolean showMonth) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.beginYear = beginYear;
        build(showMonth);
    }

    public RxDialogWheelYearMonthDay(Context mContext, int beginYear, int endYear,boolean showMonth) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.beginYear = beginYear;
        this.endYear = endYear;
        build(showMonth);
    }

    public RxDialogWheelYearMonthDay(Context mContext, TextView tv_time,boolean showMonth) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        build(showMonth);
        tv_time.setText(String.format(mContext.getResources().getString(R.string.year_month_title),String.valueOf(curYear), mMonths[curMonth]));

    }

    public int getBeginYear() {
        return beginYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getDivideYear() {
        return divideYear;
    }

    private void build(final boolean showMonth) {
        mCalendar = Calendar.getInstance();
        final View dialogView1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_year_month_day, null);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(mYearView, mMonthView, mDayView,showMonth);
            }
        };

        curYear = mCalendar.get(Calendar.YEAR);
        if (beginYear == 0) {
            beginYear = curYear - 5;
        }
        if (endYear == 0) {
            endYear = curYear;
        }
        if (beginYear > endYear) {
            endYear = beginYear;
        }

        //mYearView
        mYearView = (WheelView) dialogView1.findViewById(R.id.wheelView_year);
        mYearView.setBackgroundResource(R.drawable.transparent_bg);
        mYearView.setWheelBackground(R.drawable.transparent_bg);
        mYearView.setWheelForeground(R.drawable.wheel_val_holo);
        mYearView.setShadowColor(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
        mYearView.setViewAdapter(new DateNumericAdapter(mContext, beginYear, endYear, endYear - beginYear,showMonth?Gravity.RIGHT:Gravity.CENTER));
        mYearView.setCurrentItem(endYear - beginYear);
        mYearView.addChangingListener(listener);


        // mMonthView
        mMonthView = (WheelView) dialogView1
                .findViewById(R.id.wheelView_month);
        mMonthView.setBackgroundResource(R.drawable.transparent_bg);
        mMonthView.setWheelBackground(R.drawable.transparent_bg);
        mMonthView.setWheelForeground(R.drawable.wheel_val_holo);
        mMonthView.setShadowColor(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
        curMonth = mCalendar.get(Calendar.MONTH);
        mMonthView.setViewAdapter(new DateArrayAdapter(mContext, mMonths, curMonth));
        mMonthView.setCurrentItem(curMonth);
        mMonthView.addChangingListener(listener);

        if (!showMonth){
            mMonthView.setVisibility(View.GONE);
        }


        //mDayView
        mDayView = (WheelView) dialogView1.findViewById(R.id.wheelView_day);
        updateDays(mYearView, mMonthView, mDayView,showMonth);
        curDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayView.setCurrentItem(curDay - 1);
        mDayView.setBackgroundResource(R.drawable.transparent_bg);
        mDayView.setWheelBackground(R.drawable.transparent_bg);
        mDayView.setWheelForeground(R.drawable.wheel_val_holo);
        mDayView.setShadowColor(0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF);
        mDayView.setVisibility(View.GONE);

        mTvSure = (TextView) dialogView1.findViewById(R.id.tv_sure);
        mTvCancle = (TextView) dialogView1.findViewById(R.id.tv_cancel);

        setContentView(dialogView1);

        mLayoutParams.width= RxDeviceTool.getScreenWidth(mContext);
        mLayoutParams.y=-80;
        mLayoutParams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(mLayoutParams);
        getWindow().setWindowAnimations(R.style.Dialog_Bottom_To_Up_Anim);
    }



    public WheelView getDayView() {
        return mDayView;
    }

    public int getCurDay() {
        return curDay;
    }


    public WheelView getYearView() {
        return mYearView;
    }

    public WheelView getMonthView() {
        return mMonthView;
    }

    private int getCurYear() {
        return curYear;
    }

    private int getCurMonth() {
        return curMonth;
    }

    public TextView getSureView() {
        return mTvSure;
    }

    public TextView getCancleView() {
        return mTvCancle;
    }

    private String[] getMonths() {
        return mMonths;
    }

    private String[] getDays() {
        return mDays;
    }

    public String getSelectorYear() {
        return String.valueOf(beginYear + getYearView().getCurrentItem());
    }

    public String getSelectorMonth() {
        return getMonths()[getMonthView().getCurrentItem()];
    }

    public String getSelectorDay() {
        return getDays()[getDayView().getCurrentItem()];
    }

    /**
     * Updates mDayView wheel. Sets max mDays according to selected mMonthView and mYearView
     */
    private void updateDays(WheelView year, WheelView month, WheelView day,boolean showMonth) {
        mCalendar.set(Calendar.YEAR, beginYear + year.getCurrentItem());
        mCalendar.set(Calendar.MONTH, month.getCurrentItem());
        int maxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxDays = RxTimeTool.getDaysByYearMonth(beginYear + year.getCurrentItem(), month.getCurrentItem() + 1);
        day.setViewAdapter(new DateNumericAdapter(mContext, 1, maxDays, mCalendar.get(Calendar.DAY_OF_MONTH) - 1,showMonth?Gravity.RIGHT:Gravity.CENTER));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);


    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        int mGravity;
        /**
         * Constructor
         */
        public DateNumericAdapter(Context mContext, int minValue, int maxValue, int current,int gravity) {
            super(mContext, minValue, maxValue);
            this.currentValue = current;
            mGravity=gravity;
            setTextSize(16);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
//            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
//			}
            view.setGravity(mGravity);
//            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
}
