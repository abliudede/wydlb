package com.wydlb.first.ui.adapter;

import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.model.bean.BookStoreBeanN;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SendCardAdapter extends BaseQuickAdapter<BookStoreBeanN,BaseViewHolder> {

    String mKeyword="";
    public SendCardAdapter(@Nullable List<BookStoreBeanN> data) {
        super(R.layout.item_send_card, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, BookStoreBeanN item) {
        SelectableRoundedImageView bookImageView=helper.getView(R.id.iv_book_cover);
        TextView bookTitleTextView=helper.getView(R.id.tv_book_title);
        ImageView iv_url_book = helper.getView(R.id.iv_url_book);

        if(Integer.parseInt(item.getBookId()) > Constant.bookIdLine){
            RxImageTool.loadImage(mContext,item.getBookCover(),bookImageView);
            iv_url_book.setVisibility(View.VISIBLE);
            bookTitleTextView.setText(item.getBookName());
            try{
                setTextColor(bookTitleTextView,mKeyword,new SpannableString(item.getBookName().toUpperCase()),new SpannableString(item.getBookName()));
            }catch (Exception e){
                bookTitleTextView.setText(item.getBookName());
            }
        }else {
            RxImageTool.loadImage(mContext,item.getPlatformCover(),bookImageView);
            iv_url_book.setVisibility(View.GONE);
            bookTitleTextView.setText(item.getPlatformName());
            try{
                setTextColor(bookTitleTextView,mKeyword,new SpannableString(item.getPlatformName().toUpperCase()),new SpannableString(item.getPlatformName()));
            }catch (Exception e){
                bookTitleTextView.setText(item.getPlatformName());
            }
        }




    }

    public void search(String keyword){
        mKeyword=keyword;
        notifyDataSetChanged();
    }

    private void setTextColor(TextView tv, String keyword, SpannableString ss, SpannableString showss) {
        if (TextUtils.isEmpty(keyword))return;
        Pattern p = Pattern.compile(keyword.toUpperCase());
        RxLogTool.e("setTextColor keyword："+keyword);

        Matcher m = p.matcher(ss);

        RxLogTool.e("setTextColor ss："+ss);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            showss.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(showss);
    }
}
