package com.lianzai.reader.ui.adapter;

import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.RelatedLinksBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.ItemChapters;

import java.util.List;

/**
 * Created by lrz on 2019/3/21.
 * 相关链接item视图
 */

public class RelatedLinksAdapter extends BaseQuickAdapter<RelatedLinksBean.DataBean.SourcesBean,BaseViewHolder> {

    private final Drawable star;
    ContentClickListener contentClickListener;

    private String author_name;
    private String book_name;
    private String bookDes;
    private String bookId;

    public RelatedLinksAdapter(@Nullable List<RelatedLinksBean.DataBean.SourcesBean> data) {
        super(R.layout.item_related_links, data);
        star = RxImageTool.getDrawable(R.mipmap.icon_star_light);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookDes() {
        return bookDes;
    }

    public void setBookDes(String bookDes) {
        this.bookDes = bookDes;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }


    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, RelatedLinksBean.DataBean.SourcesBean bean) {
        View view_content=baseViewHolder.getView(R.id.view_content);
        TextView tv_title= baseViewHolder.getView(R.id.tv_title);
        TextView tv_des= baseViewHolder.getView(R.id.tv_des);
        LinearLayout ly_chapters=baseViewHolder.getView(R.id.ly_chapters);
        LinearLayout ly_button=baseViewHolder.getView(R.id.ly_button);

        TextView tv_mulu=baseViewHolder.getView(R.id.tv_mulu);
        TextView tv_read=baseViewHolder.getView(R.id.tv_read);

        TextView tv_source=baseViewHolder.getView(R.id.tv_source);

        //标题部分
        String randomStr = "最新章节列表";
        switch(bean.getRandomInt()){
            case 0:
                randomStr = "最新章节列表";
                break;
            case 1:
                randomStr = "免费全文阅读";
                break;
            case 2:
                randomStr = "免费阅读";
                break;
            case 3:
                randomStr = "无广告全文阅读";
                break;
            case 4:
                randomStr = "无弹窗小说";
                break;
        }
        SpannableString mString = new SpannableString(book_name+ randomStr + "-" + author_name + "-" + bean.getSourceName());
        mString.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), 0, book_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_title.setText(mString);

        if(bean.isChoose()){
            tv_title.setCompoundDrawables(null,null,star,null);
        }else {
            tv_title.setCompoundDrawables(null,null,null,null);
        }

        //简介部分
        String randomDes;
        switch(bean.getRandomInt()){
            case 0:
                randomDes = book_name + "最新章节由网友提供，" + bookDes;
                SpannableString mDes1 = new SpannableString(randomDes);
                mDes1.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), 0, book_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_des.setText(mDes1);
                break;
            case 1:
                randomDes = book_name + "是"+ author_name +"精心创作的大作，" + bookDes;
                SpannableString mDes2 = new SpannableString(randomDes);
                mDes2.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), 0, book_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_des.setText(mDes2);
                break;
            case 2:
                randomDes = "小说" + book_name + "，"+ author_name +"著，" + book_name + "：" + bookDes;
                SpannableString mDes3 = new SpannableString(randomDes);
                mDes3.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), 2, 2+book_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_des.setText(mDes3);
                break;
            case 3:
                randomDes = bookDes;
                tv_des.setText(randomDes);
                break;

                default:
                    randomDes = book_name + "最新章节由网友提供，" + bookDes;
                    SpannableString mDes4 = new SpannableString(randomDes);
                    mDes4.setSpan(new TextAppearanceSpan(mContext, R.style.BlueNormalStyle), 0, book_name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv_des.setText(mDes4);
                    break;
        }


        if(null == bean.getChapters() || bean.getChapters().isEmpty()){
            ly_chapters.removeAllViews();
        }else {
            ly_chapters.removeAllViews();
            for(int i = 0 ; i < bean.getChapters().size() ;i++){
                ItemChapters item = new ItemChapters(mContext);
                item.bindData(bean.getChapters().get(i),bean.getSource(),bookId);
                ly_chapters.addView(item);
            }
        }

        tv_source.setText(bean.getSource());

        tv_mulu.setOnClickListener(
                v -> {
                    if(null != contentClickListener){
                        contentClickListener.muluClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
                }
        );

        tv_read.setOnClickListener(
                v -> {
                    if(null != contentClickListener){
                        contentClickListener.readClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
                }
        );

    }


    public interface ContentClickListener{
        void muluClick(View v, int pos);
        void readClick(View v, int pos);
    }
}
