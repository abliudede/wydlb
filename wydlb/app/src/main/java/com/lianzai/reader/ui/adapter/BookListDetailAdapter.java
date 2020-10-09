package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
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
 * Created by lrz on 2017/10/13.
 */

public class BookListDetailAdapter extends BaseQuickAdapter<BookListDetailResponse.DataBean.ListBean,BaseViewHolder> {

    BookListDetailItemClickListener bookListDetailItemClickListener;

    Drawable hasAddDrawable;
    Drawable notAddDrawable;

    Context context;
    int contentWidth;
    public BookListDetailAdapter(@Nullable List<BookListDetailResponse.DataBean.ListBean> data, Context mContext) {
        super(R.layout.item_book_list_detail, data);
        this.context=mContext;

        contentWidth=ScreenUtil.getDisplayWidth()-RxImageTool.dip2px(40);//显示点评内容的宽度

        hasAddDrawable = RxTool.getContext().getResources().getDrawable(R.mipmap.icon_has_add_book);
        hasAddDrawable.setBounds(0, 0, RxImageTool.dp2px(10), RxImageTool.dp2px(6));

        notAddDrawable = RxTool.getContext().getResources().getDrawable(R.mipmap.icon_add_white);
        notAddDrawable.setBounds(0, 0,RxImageTool.dp2px(10),RxImageTool.dp2px(11));
    }

    public BookListDetailItemClickListener getBookListDetailItemClickListener() {
        return bookListDetailItemClickListener;
    }

    public void setBookListDetailItemClickListener(BookListDetailItemClickListener bookListDetailItemClickListener) {
        this.bookListDetailItemClickListener = bookListDetailItemClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookListDetailResponse.DataBean.ListBean bean) {
        SelectableRoundedImageView iv_book_cover=baseViewHolder.getView(R.id.iv_book_cover);
        TextView tv_book_name=baseViewHolder.getView(R.id.tv_book_name);
        TextView tv_more=baseViewHolder.getView(R.id.tv_more);
        TextView tv_description=baseViewHolder.getView(R.id.tv_description);

        RelativeLayout rl_book_content=baseViewHolder.getView(R.id.rl_book_content);

        TextView tv_score_number=baseViewHolder.getView(R.id.tv_score_number);
        //字体设置
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(),"DINMedium.ttf");
        tv_score_number.setTypeface(typeface);

        TextView tv_comment=baseViewHolder.getView(R.id.tv_comment);

        TextView tv_add_book=baseViewHolder.getView(R.id.tv_add_book);

        TextView tv_expand=baseViewHolder.getView(R.id.tv_expand);

        RatingBar rb_star=baseViewHolder.getView(R.id.rb_star);

        ImageView up_iv=baseViewHolder.getView(R.id.up_iv);

        LinearLayout score_ly=baseViewHolder.getView(R.id.score_ly);

        ImageView gongxiangbanquan_jiaobiao = baseViewHolder.getView(R.id.gongxiangbanquan_jiaobiao);

        if(bean.isCopyright()){
            gongxiangbanquan_jiaobiao.setVisibility(View.VISIBLE);
        }else {
            gongxiangbanquan_jiaobiao.setVisibility(View.GONE);
        }

        rb_star.setRating(bean.getScore()/2);

        tv_book_name.setText(bean.getBookTitle());

        String moreStr=(TextUtils.isEmpty(bean.getBookAuthor())?"":bean.getBookAuthor())+(TextUtils.isEmpty(bean.getBookCategory())?"":" | "+bean.getBookCategory())+" | "+(bean.getNovelType()==1?"连载中":"完结");
        tv_more.setText(moreStr);

        tv_description.setText(bean.getPlatformIntroduce());

//        String score=bean.getScore()+" 分";
//
//        SpannableStringBuilder builder = new SpannableStringBuilder(score);
//
//        builder.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.text_size_17)),0,score.length()-2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new StyleSpan(Typeface.BOLD),0,score.length()-2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.text_size_12)),score.length()-2,score.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//同一个textview中两种字体无法对齐
        if(bean.getScore() <= 0 && TextUtils.isEmpty(bean.getEditorComment())){
            up_iv.setVisibility(View.GONE);
            score_ly.setVisibility(View.GONE);
        }else {
            up_iv.setVisibility(View.VISIBLE);
            score_ly.setVisibility(View.VISIBLE);
            tv_score_number.setText(String.valueOf(bean.getScore()));
            String comment=TextUtils.isEmpty(bean.getEditorComment())?"暂无推荐评语":"推荐评语："+bean.getEditorComment();
            int maxWordCount=getThreeLineStringCount(contentWidth,comment,tv_comment.getPaint());

            if (maxWordCount>0&&!bean.isExpand()){//超过三行-并且未展开过，已经展开过的就不处理
                tv_expand.setVisibility(View.VISIBLE);
                tv_comment.setText(comment.substring(0,maxWordCount-2)+"...");
            }else{
                tv_expand.setVisibility(View.GONE);
                tv_comment.setText(comment);
            }

            tv_expand.setOnClickListener(v->{
                bean.setExpand(true);
                tv_expand.setVisibility(View.GONE);
//            tv_comment.setMaxLines(0);
                tv_comment.setText(comment);

            });
        }

//        if (baseViewHolder.getAdapterPosition()%2==0){
//            comment="评语：雪中悍刀行此名之前，小二上酒风靡一时，烽火的文笔从陈二狗的妖孽人生开始有了重大转变，朴实无华之中带着一股装逼味儿，这应该就是所谓的返璞归真了。 这本书打着反武侠反历史的名头，其实更觉得，烽火的文笔从陈二狗的妖孽人生开始有了重大转变，朴实无华之中带着一股装逼味儿，这应该就是所谓的返璞归真了。 这本书打着反武侠反历史的名头，烽火的文笔从陈二狗的妖孽人生开始有了重大转变，朴实无华之中带着一股装逼味儿，这应该就是所谓的返璞归真了。 这本书打着反武侠反历史的名头";
//        }else{
//            comment="评语：雪中悍刀行此名之前，小二上酒风靡一时.烽火的文笔从陈二狗的妖孽人生开始有了重大转变";
//        }


        rl_book_content.setOnClickListener(
                v->getBookListDetailItemClickListener().openBookClick(baseViewHolder.getAdapterPosition()-getHeaderLayoutCount())
        );

//        if (bean.isExpand()){
////            tv_expand.setMaxLines(0);
//            tv_comment.setText(comment);
//        }else{
////            tv_expand.setMaxLines(3);
//            tv_comment.setText(comment);
//        }

        if (bean.isConcern()){//已添加至书架

            tv_add_book.setTextColor(context.getResources().getColor(R.color.color_black_ff666666));
            tv_add_book.setCompoundDrawables(hasAddDrawable, null, null, null);
            tv_add_book.setBackgroundResource(R.drawable.v2_gray_2corner_bg);
        }else{
            tv_add_book.setTextColor(context.getResources().getColor(R.color.white));
            tv_add_book.setBackgroundResource(R.drawable.v2_blue_2corner_bg);
            tv_add_book.setCompoundDrawables(notAddDrawable, null, null, null);
        }


        tv_add_book.setOnClickListener(
                view -> {
                    getBookListDetailItemClickListener().addBookClick(baseViewHolder.getAdapterPosition()-getHeaderLayoutCount());
                }
        );

        RxImageTool.loadImage(context,bean.getPlatformCover(),iv_book_cover);
    }

    //获取显示三行需要多少个字
    private int getThreeLineStringCount(int maxLineWidth,String str, Paint paint){
        long startTime=System.currentTimeMillis();

        int width=0;
        int maxCount=0;//三行最多显示的字符数
        RxLogTool.e("maxLineWidth:"+maxLineWidth);
        RxLogTool.e("maxLength:"+str.length());
        int lineCount=0;//换行符个数
        for(int i=0;i<str.length();i++){
            if (String.valueOf(str.charAt(i)).equals("\n")){//换行符
                lineCount++;
                RxLogTool.e("有换行符:"+lineCount);
            }
            width+=paint.measureText(String.valueOf(str.charAt(i)));

            if (3*maxLineWidth<=width){//超过三行最多显示了多少个字
                RxLogTool.e("word count:"+i);
                maxCount=i;
                break;
            }
            if (lineCount>3){//有三个换行符
                RxLogTool.e("word count:"+i);
                maxCount=i;
                break;
            }
            RxLogTool.e(i+"--width:"+width);
        }
        long endTime=System.currentTimeMillis();
        RxLogTool.e("spend time:"+(endTime-startTime));
        return maxCount;
    }


    public interface BookListDetailItemClickListener{
        void addBookClick(int position);
        void openBookClick(int position);
    }
}
