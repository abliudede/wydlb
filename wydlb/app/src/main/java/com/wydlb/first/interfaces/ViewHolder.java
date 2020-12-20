package com.wydlb.first.interfaces;

import android.content.Context;
import android.view.View;

/**
 * Created by lrz on 2018/1/2.
 */

public interface ViewHolder<T> {
    View createView(Context context, int position);
    /**
     * @param context context
     * @param data 实体类对象
     * @param position 当前位置
     * @param size 页面个数
     */
    void onBind(Context context,T data,int position,int size);
}