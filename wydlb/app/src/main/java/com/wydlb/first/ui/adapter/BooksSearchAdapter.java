package com.wydlb.first.ui.adapter;

import android.content.Context;
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
import com.wydlb.first.bean.SearchNovelsBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lrz on 2017/10/13.
 */

public class BooksSearchAdapter extends BaseQuickAdapter<SearchNovelsBean.DataBean.ListBean,BaseViewHolder> {


    String mKeyword="";
    Context context;
    public BooksSearchAdapter( @Nullable List<SearchNovelsBean.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_search_book, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,SearchNovelsBean.DataBean.ListBean bean) {
        final SelectableRoundedImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(context,bean.getPlatformCover(),roundedImageView);

//        TextView tv_is_add=baseViewHolder.getView(R.id.tv_is_add);

        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        TextView tvUpdateChapter=baseViewHolder.getView(R.id.tv_book_update_chapter);
        TextView tv_author=baseViewHolder.getView(R.id.tv_author);
        ImageView gongxiangbanquan_jiaobiao = baseViewHolder.getView(R.id.gongxiangbanquan_jiaobiao);

        if(bean.isCopyright()){
            gongxiangbanquan_jiaobiao.setVisibility(View.VISIBLE);
        }else {
            gongxiangbanquan_jiaobiao.setVisibility(View.GONE);
        }


        if (TextUtils.isEmpty(bean.getPenName())) {
            tv_author.setText("");
        }else{
            tv_author.setText(bean.getPenName());
        }

        if (!TextUtils.isEmpty(bean.getPlatformName())){
            tvBookTitle.setText(bean.getPlatformName());
        }


//        if (bean.isConcern()){//已关注
//            tv_is_add.setVisibility(View.VISIBLE);
//        }else {
//            tv_is_add.setVisibility(View.GONE);
//        }

        tvUpdateChapter.setText(bean.getPlatformIntroduce());

        try{
            if (!TextUtils.isEmpty(bean.getPenName())) {
                tv_author.setText("");
                setTextColor(tv_author,mKeyword,new SpannableString(bean.getPenName().toUpperCase()),new SpannableString(bean.getPenName()));
            }

            setTextColor(tvBookTitle,mKeyword,new SpannableString(bean.getPlatformName().toUpperCase()),new SpannableString(bean.getPlatformName()));
        }catch (Exception e){
            tvBookTitle.setText(bean.getPlatformName());

            if (TextUtils.isEmpty(bean.getPenName())) {
                tv_author.setText("");
            }
        }

    }


    public void search(String keyword){
        mKeyword=keyword;
        notifyDataSetChanged();
    }

    private void setTextColor(TextView tv, String keyword, SpannableString ss,SpannableString showss) {
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
