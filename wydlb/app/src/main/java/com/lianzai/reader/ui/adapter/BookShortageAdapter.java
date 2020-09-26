package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookListDetailResponse;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

/**
 * Created by lrz on 2019/3/11.
 */

public class BookShortageAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    Context context;
    public BookShortageAdapter(@Nullable List<String> data, Context mContext) {
        super(R.layout.item_book_shortage, data);
        this.context=mContext;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,String bean) {
        TextView tv_bookname=baseViewHolder.getView(R.id.tv_bookname);
        tv_bookname.setText("《"+bean+"》");
    }

}
