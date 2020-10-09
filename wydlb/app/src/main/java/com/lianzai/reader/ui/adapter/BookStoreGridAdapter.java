package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * 书架-九宫格
 */
public class BookStoreGridAdapter extends BaseItemDraggableAdapter<BookStoreBeanN,BaseViewHolder> {

    Drawable unReadDrawable;

    int imageWidth=0;
    Context context;


    public BookStoreGridAdapter(@Nullable List<BookStoreBeanN> data, Context mContext) {
        super(R.layout.item_book_store_grid, data);
        this.context=mContext;
        imageWidth= (RxDeviceTool.getScreenWidth(context)-RxImageTool.dp2px(62))/3;
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder,BookStoreBeanN bean) {
        RelativeLayout content = baseViewHolder.getView(R.id.content);

        SelectableRoundedImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        ImageView iv_url_book=baseViewHolder.getView(R.id.iv_url_book);
        TextView tv_huancun_count = baseViewHolder.getView(R.id.tv_huancun_count);
        ImageView view_red = baseViewHolder.getView(R.id.view_red);

        ImageView gongxiangbanquan_jiaobiao = baseViewHolder.getView(R.id.gongxiangbanquan_jiaobiao);

        //根据屏幕宽度设置视图宽度
        ViewGroup.LayoutParams layoutParams = content.getLayoutParams();
        layoutParams.width = imageWidth;
        content.setLayoutParams(layoutParams);
        //再根据此宽度，设置图片大小
        ViewGroup.LayoutParams layoutParams2 = roundedImageView.getLayoutParams();
        layoutParams2.height = imageWidth/3*4;
        roundedImageView.setLayoutParams(layoutParams2);

        //获取源字符串
        String bidStr = SkipReadUtil.getSource(bean.getBookId());





        if(Integer.parseInt(bean.getBookId()) > Constant.bookIdLine){
            iv_url_book.setVisibility(View.VISIBLE);
            iv_url_book.setImageResource(R.mipmap.url_book_icon2);
            baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getBookName())?"":bean.getBookName());
            RxImageTool.loadBookStoreImage(context,bean.getBookCover(),roundedImageView);
            view_red.setVisibility(View.GONE);
            tv_huancun_count.setVisibility(View.GONE);
        }else {

            if(bean.getPlatformType() == Constant.UserIdentity.BOOK_PUBLIC_NUMBER){
                iv_url_book.setVisibility(View.GONE);
                //只有内站书显示缓存章节数
                int cacheChapterCont = BookManager.getCacheChapterCount(bean.getBookId() + bidStr);
                if(cacheChapterCont > 0) {
                    tv_huancun_count.setVisibility(View.VISIBLE);
                    tv_huancun_count.setText("已缓存" + cacheChapterCont + "章");
                }else {
                    tv_huancun_count.setVisibility(View.GONE);
                }
            }else {
                iv_url_book.setVisibility(View.VISIBLE);
                iv_url_book.setImageResource(R.mipmap.url_book_icon1);
                tv_huancun_count.setVisibility(View.GONE);
            }
//            iv_url_book.setImageResource(R.mipmap.url_book_icon1);
            baseViewHolder.setText(R.id.tv_book_name,TextUtils.isEmpty(bean.getPlatformName())?"":bean.getPlatformName());
            RxImageTool.loadBookStoreImage(context,bean.getPlatformCover(),roundedImageView);
            if (bean.getIsUnread()){
                view_red.setVisibility(View.VISIBLE);
            }else{
                view_red.setVisibility(View.GONE);
            }
        }

        //共享版权角标
        if(bean.getIsCopyright()){
            gongxiangbanquan_jiaobiao.setVisibility(View.VISIBLE);
        }else {
            gongxiangbanquan_jiaobiao.setVisibility(View.GONE);
        }


    }



}
