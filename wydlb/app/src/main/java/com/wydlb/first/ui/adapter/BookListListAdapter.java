package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.BookListResponse;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2017/10/13.
 */

public class BookListListAdapter extends BaseQuickAdapter<BookListResponse.DataBean.ListBean,BaseViewHolder> {


    String mKeyword="";

    Context context;
    public BookListListAdapter(@Nullable List<BookListResponse.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_book_list_list, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookListResponse.DataBean.ListBean bean) {
        if (null==bean)return;
        SelectableRoundedImageView iv_book_list_cover=baseViewHolder.getView(R.id.iv_book_list_cover);
        TextView tv_book_list_name=baseViewHolder.getView(R.id.tv_book_list_name);
        TextView iv_book_list_description=baseViewHolder.getView(R.id.iv_book_list_description);
        TextView iv_book_list_more=baseViewHolder.getView(R.id.iv_book_list_more);
        TextView tv_update=baseViewHolder.getView(R.id.tv_update);

        tv_book_list_name.setText(bean.getBooklistName());
        RxImageTool.loadSquareImage(context,bean.getCover(),iv_book_list_cover);
        iv_book_list_description.setText(TextUtils.isEmpty(bean.getPlatformName())?bean.getBooklistIntro():String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName()));

        String moreStr=(TextUtils.isEmpty(bean.getBooklistAuthor())?"":bean.getBooklistAuthor())+(bean.isCollectionNumShow()?(" | "+bean.getCollectionNum()+"人收藏"):"");
        iv_book_list_more.setText(moreStr);

        if (bean.isUnread()){
            tv_update.setVisibility(View.VISIBLE);
        }else{
            tv_update.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mKeyword)){
            try{
                if (!TextUtils.isEmpty(bean.getBooklistName())) {
                    tv_book_list_name.setText("");
                    setTextColor(tv_book_list_name,mKeyword,new SpannableString(bean.getBooklistName().toUpperCase()),new SpannableString(bean.getBooklistName()));
                }

                if (!TextUtils.isEmpty(bean.getPlatformName())){
                    setTextColor(iv_book_list_description,mKeyword,new SpannableString(String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName())),new SpannableString(String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName())));
                }
//            setTextColor(iv_book_list_description,mKeyword,new SpannableString(bean.getBooklistIntro().toUpperCase()),new SpannableString(bean.getBooklistIntro()));
            }catch (Exception e){
                if (!TextUtils.isEmpty(bean.getPlatformName())){
                    iv_book_list_description.setText(String.format(mContext.getResources().getString(R.string.search_book_list),bean.getPlatformName()));
                }

                if (TextUtils.isEmpty(bean.getBooklistName())) {
                    tv_book_list_name.setText("");
                }
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
