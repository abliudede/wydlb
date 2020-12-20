package com.wydlb.first.interfaces;

import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by lrz on 2017/10/25.
 */

public class MyClickableSpan extends ClickableSpan {

    OnRepeatClickListener listener;

    public OnRepeatClickListener getListener() {
        return listener;
    }

    public void setListener(OnRepeatClickListener listener) {
        this.listener = listener;
    }

    /**
     * Performs the click action associated with this span.
     *
     * @param widget
     */
    @Override
    public void onClick(View widget) {
        listener.onRepeatClick(widget);
    }

    public void gc(){
        if (null!=listener){
            listener=null;
        }
    }
}
