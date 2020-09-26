package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookShortageSearchBean;
import com.lianzai.reader.bean.SearchNovelsBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2017/10/13.
 */

public class BooksShortageSearchAdapter extends BaseQuickAdapter<BookShortageSearchBean.DataBean,BaseViewHolder> {

    Context context;
    public BooksShortageSearchAdapter(@Nullable List<BookShortageSearchBean.DataBean> data, Context mContext) {
        super(R.layout.item_search_book_shortage, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookShortageSearchBean.DataBean bean) {

        RelativeLayout view1 = baseViewHolder.getView(R.id.view1);

        SelectableRoundedImageView iv_book_cover = baseViewHolder.getView(R.id.iv_book_cover);
        TextView tv_book_title=baseViewHolder.getView(R.id.tv_book_title);
        TextView tv_author=baseViewHolder.getView(R.id.tv_author);
        TextView tv_book_des=baseViewHolder.getView(R.id.tv_book_des);


        LinearLayout view2 = baseViewHolder.getView(R.id.view2);
        TextView tv_book_title2=baseViewHolder.getView(R.id.tv_book_title2);


        if(bean.getPlatformId() > 0){
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);

            RxImageTool.loadImage(context,bean.getCover(),iv_book_cover);
            tv_book_title.setText(bean.getBookName());
            tv_author.setText(bean.getAutherName() + "著");
            tv_book_des.setText(bean.getBookInfo());

        }else {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);

            tv_book_title2.setText("《" + bean.getBookName() + "》");
        }
    }


}
