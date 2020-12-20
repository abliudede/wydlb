package com.wydlb.first.ui.adapter;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wydlb.first.R;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.BookRecordHistoryResponse;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxTimeTool;
import com.wydlb.first.utils.RxTool;
import com.wydlb.first.utils.TimeFormatUtil;
import com.wydlb.first.view.SelectableRoundedImageView;

import java.util.List;

public class BookRecordExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = BookRecordExpandableItemAdapter.class.getSimpleName();

    public static final int DATE = 0;
    public static final int CONTENT = 1;

    Drawable drawable1;
    Drawable drawable2;

    Callback callback;
    public BookRecordExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(DATE, R.layout.item_read_record_time);
        addItemType(CONTENT, R.layout.item_book_record);
        int size= RxImageTool.dp2px(22);
        drawable1= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_v2_book_store_add);
        drawable2= RxTool.getContext().getResources().getDrawable(R.mipmap.icon_v2_has_added);
        drawable1.setBounds(0,0, size,size);
        drawable2.setBounds(0,0, size,size);
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case DATE:
                BookRecordHistoryResponse.DataBean.ListBeanX bean=(BookRecordHistoryResponse.DataBean.ListBeanX)item;
                TextView tv_date=holder.getView(R.id.tv_date);
                View view_line=holder.getView(R.id.view_line);
                TextView tv_read_minute=holder.getView(R.id.tv_read_minute);
                if (holder.getAdapterPosition()  - getHeaderLayoutCount() ==0){
                    view_line.setVisibility(View.GONE);
                }else{
                    view_line.setVisibility(View.VISIBLE);
                }
                tv_date.setText(TimeFormatUtil.getIntervalOther(RxTimeTool.stringYY_MM_DD_SDF2Milliseconds(bean.getDay())));
                tv_read_minute.setText(Html.fromHtml("<font color='#333333'>"+(bean.getTotal()<1?"":"约 ")+"</font><font color='#485CF8'>"+(bean.getTotal()<1?"小于1":bean.getTotal())+"</font><font color='#333333'> 分钟</font>"));
                break;
            case CONTENT:
                BookRecordHistoryResponse.DataBean.ListBeanX.ListBean bean1=(BookRecordHistoryResponse.DataBean.ListBeanX.ListBean)item;
                SelectableRoundedImageView iv_book_cover=holder.getView(R.id.iv_book_cover);
                ImageView iv_url_book=holder.getView(R.id.iv_url_book);
                TextView tv_book_read_time=holder.getView(R.id.tv_book_read_time);
                TextView tv_add_book_store=holder.getView(R.id.tv_add_book_store);
                TextView tv_book_name=holder.getView(R.id.tv_book_name);
                tv_book_name.setText(bean1.getName());
                if(bean1.getBookId() > Constant.bookIdLine){
                    iv_url_book.setVisibility(View.VISIBLE);
                }else {
                    iv_url_book.setVisibility(View.GONE);
                }

                tv_add_book_store.setVisibility(View.GONE);

//                if (bean1.isConcern()){
//                    tv_add_book_store.setTextColor(mContext.getResources().getColor(R.color.normal_text_color));
//                    tv_add_book_store.setBackgroundResource(R.drawable.v2_gray_5corner_bg);
//                    tv_add_book_store.setCompoundDrawables(drawable2,null,null,null);
//                }else{
//                    tv_add_book_store.setCompoundDrawables(drawable1,null,null,null);
//                    tv_add_book_store.setTextColor(mContext.getResources().getColor(R.color.white));
//                    tv_add_book_store.setBackgroundResource(R.drawable.v2_blue_5corner_bg);
//                }
//                tv_add_book_store.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(bean1.getBookId() > Constant.bookIdLine){
//                            callback.optionsClick(String.valueOf(bean1.getBookId()),bean1.isConcern(),bean1);
//                        }else {
//                            callback.optionsClick(bean1.getPlatformId(),bean1.isConcern(),bean1);
//                        }
//                    }
//                });
                RxImageTool.loadImage(mContext,bean1.getCover(),iv_book_cover);
                tv_book_read_time.setText(Html.fromHtml("<font color='#999999'>阅读 </font><font color='#485CF8'>"+(bean1.getReadMinute()<1?"&lt; 1":bean1.getReadMinute())+"</font><font color='#999999'> 分钟</font>"));
                break;
        }
    }

    public interface Callback{
        void optionsClick(String bookId,boolean isConcern,BookRecordHistoryResponse.DataBean.ListBeanX.ListBean bean);
    }
}
