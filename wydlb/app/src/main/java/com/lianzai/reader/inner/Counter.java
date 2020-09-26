package com.lianzai.reader.inner;

import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxTextTool;
import com.lianzai.reader.view.RxRunTextView;

import java.lang.ref.WeakReference;

/**
 * Created by lrz on 2017/11/8.
 */

public class Counter implements Runnable {
    WeakReference<RxRunTextView> view;
    int num;
    long pertime;

    public boolean flag=true;

    int i = 0;

    public Counter(RxRunTextView view,int num,long time) {
        this.view = new WeakReference<RxRunTextView>(view);
        this.num = num;
        this.pertime = time/num;
    }

    @Override
    public void run() {
        if (null!=view.get()){
            if (i> num - 1) {
                view.get().removeCallbacks(Counter.this);
                return;
            }
            RxTextTool.getBuilder("+" + i++).setProportion(1.0f).into(view.get());

            view.get().removeCallbacks(Counter.this);
            view.get().postDelayed(Counter.this, pertime);
        }
    }

    public WeakReference<RxRunTextView> getView() {
        return view;
    }

    public void setView(WeakReference<RxRunTextView> view) {
        this.view = view;
    }
}
