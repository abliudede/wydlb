package com.wydlb.first.view.hview;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Base TextView
 * Created by hanks on 2017/3/13.
 */

public abstract class HTextView extends AppCompatTextView {
    public HTextView(Context context) {
        this(context, null);
    }

    public HTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setProgress(float progress);

    public abstract void animateText(CharSequence text);
}
