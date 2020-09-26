package com.lianzai.reader.interfaces;

/**
 * Created by lrz on 2018/1/2.
 */

public interface HolderCreator<VH extends ViewHolder> {
    /**
     * 创建ViewHolder
     */
    VH createViewHolder();
}
