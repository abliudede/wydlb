package com.lianzai.reader.inner;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Created by lrz on 2017/12/5.
 */

public class AudioServiceActivityLeak extends ContextWrapper {
    AudioServiceActivityLeak(Context base) {
        super(base);
    }

    public static ContextWrapper preventLeakOf(Context base) {
        return new AudioServiceActivityLeak(base);
    }

    @Override public Object getSystemService(String name) {
        if (Context.AUDIO_SERVICE.equals(name)) {
            return getApplicationContext().getSystemService(name);
        }
        return super.getSystemService(name);
    }
}
