package com.wydlb.first.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.AssociativeSearchResponse;
import com.wydlb.first.utils.RxImageTool;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2017/10/13.
 * 联想搜索
 */

public class AssociativeSearchAdapter extends BaseQuickAdapter<AssociativeSearchResponse.DataBean.PlatformBean, BaseViewHolder> {



    Context context;

    String mKeyword="";

    Drawable bookDrawable;
    Drawable bookListDrawable;

    public AssociativeSearchAdapter(@Nullable List<AssociativeSearchResponse.DataBean.PlatformBean> data, Context mContext) {
        super(R.layout.item_associative_search, data);
        this.context = mContext;

        bookDrawable =RxImageTool.getDrawable(14,R.mipmap.icon_book_small);
        bookListDrawable=RxImageTool.getDrawable(13,16,R.mipmap.icon_book_list_small);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,AssociativeSearchResponse.DataBean.PlatformBean bean) {
        TextView tvBookTitle = baseViewHolder.getView(R.id.tv_book_name);
        TextView tv_type = baseViewHolder.getView(R.id.tv_type);

        if (!bean.isBookList()){
            tvBookTitle.setCompoundDrawables(bookDrawable,null,null,null);
            tv_type.setText("");
        }else{
            tvBookTitle.setCompoundDrawables(bookListDrawable,null,null,null);
            tv_type.setText("书单");
        }

        try{
            setTextColor(tvBookTitle,mKeyword,new SpannableString(bean.getPlatformName().toUpperCase()),new SpannableString(bean.getPlatformName()));
        }catch (Exception e){
            tvBookTitle.setText(bean.getPlatformName());
        }
    }

    public void search(String keyword){
        mKeyword=keyword;
        notifyDataSetChanged();
    }

    private void setTextColor(TextView tv, String keyword, SpannableString ss,SpannableString showss) {
        if (TextUtils.isEmpty(keyword))return;
        Pattern p = Pattern.compile(keyword.toUpperCase());

        Matcher m = p.matcher(ss);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            showss.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(showss);
    }

}
