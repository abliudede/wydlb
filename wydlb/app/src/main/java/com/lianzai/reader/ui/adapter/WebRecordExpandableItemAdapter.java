package com.lianzai.reader.ui.adapter;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookRecordHistoryResponse;
import com.lianzai.reader.bean.WebHistoryListBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTimeTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

public class WebRecordExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int DATE = 0;
    public static final int CONTENT = 1;

    Callback callback;
    public WebRecordExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(DATE, R.layout.item_read_record_time);
        addItemType(CONTENT, R.layout.item_web_record);

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
                WebHistoryListBean bean=(WebHistoryListBean)item;
                TextView tv_date=holder.getView(R.id.tv_date);
                View view_line=holder.getView(R.id.view_line);
                TextView tv_read_minute=holder.getView(R.id.tv_read_minute);
                if (holder.getAdapterPosition() - getHeaderLayoutCount()==0){
                    view_line.setVisibility(View.GONE);
                }else{
                    view_line.setVisibility(View.VISIBLE);
                }
                tv_date.setText(bean.getDay());
                tv_read_minute.setVisibility(View.INVISIBLE);
                break;
            case CONTENT:
                WebHistoryListBean.ListBean bean1=(WebHistoryListBean.ListBean)item;
                TextView tv_web_name=holder.getView(R.id.tv_web_name);
                TextView tv_web_url=holder.getView(R.id.tv_web_url);
                tv_web_name.setText(bean1.getName());
                tv_web_url.setText(bean1.getUrl());
                break;
        }
    }

    public interface Callback{
        void optionsClick(String bookId, boolean isConcern, BookRecordHistoryResponse.DataBean.ListBeanX.ListBean bean);
    }
}
