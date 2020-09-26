package com.lianzai.reader.ui.adapter.holder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.adapter.ViewHolderImpl;
import com.lianzai.reader.model.bean.BookChapterBean;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.utils.BookManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.page.TxtChapter;


public class CategoryHolder extends ViewHolderImpl<BookChapterBean> {

    private TextView tv_chapter_name;
    ImageView iv_chapter_is_vip;



    @Override
    public void initView() {
        tv_chapter_name = findById(R.id.tv_chapter_name);
        iv_chapter_is_vip=findById(R.id.iv_chapter_is_vip);


    }

    @Override
    public void onBind(BookChapterBean bean, int pos){

        boolean mIsNightMode= ReadSettingManager.getInstance().isNightMode();

        if (!TextUtils.isEmpty(bean.getTitle())){
            tv_chapter_name.setText(bean.getTitle());
        }else{
            tv_chapter_name.setText("");
        }
        //如果此章节已经缓存 #FF333333 未缓存 #FF999999颜色
        if (!TextUtils.isEmpty(bean.getLink())&& BookManager
                .isChapterCached(bean.getBookId(),bean.getId())){
            if (mIsNightMode){
                tv_chapter_name.setTextColor(getContext().getResources().getColor(R.color.color_black_ff999999));
            }else{
                tv_chapter_name.setTextColor(getContext().getResources().getColor(R.color.color_black_ff333333));
            }
        }else{
            if (mIsNightMode){
                tv_chapter_name.setTextColor(getContext().getResources().getColor(R.color.color_black_ff333333));
            }else{
                tv_chapter_name.setTextColor(getContext().getResources().getColor(R.color.color_black_ff999999));
            }
        }
//        RxLogTool.e(pos+"-pos--CategoryHolder mIsNightMode:"+mIsNightMode);

        if (bean.getIsVip()){//是否是vip
            iv_chapter_is_vip.setVisibility(View.VISIBLE);
        }else {
            iv_chapter_is_vip.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_chapter;
    }

    public void setSelectedChapter(){
        boolean mIsNightMode= ReadSettingManager.getInstance().isNightMode();
        tv_chapter_name.setTextColor(mIsNightMode?Color.parseColor("#7f345fff"):getContext().getResources().getColor(R.color.v2_blue_color));
    }

}
