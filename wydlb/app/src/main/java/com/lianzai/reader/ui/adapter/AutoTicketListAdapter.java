package com.lianzai.reader.ui.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.UserAutoSettingDailyTicketListBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * 自动投票列表
 */
public class AutoTicketListAdapter extends BaseQuickAdapter<UserAutoSettingDailyTicketListBean.DataBean,BaseViewHolder> {


    private Context context;

    public AutoTicketListAdapter(@Nullable List<UserAutoSettingDailyTicketListBean.DataBean> data, Context mContext) {
        super(R.layout.item_auto_ticket_list, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,UserAutoSettingDailyTicketListBean.DataBean bean) {
        SelectableRoundedImageView iv_book_cover=baseViewHolder.getView(R.id.iv_book_cover);
        TextView tv_book_name=baseViewHolder.getView(R.id.tv_book_name);
        TextView tv_book_author=baseViewHolder.getView(R.id.tv_book_author);
        TextView tv_book_last_chapter=baseViewHolder.getView(R.id.tv_book_last_chapter);
        TextView tv_modify=baseViewHolder.getView(R.id.tv_modify);

        RxImageTool.loadImage(context,bean.getBookCover(),iv_book_cover);
        tv_book_name.setText(bean.getBookName());
        tv_book_author.setText(bean.getBookAuthor() + " 著");
        tv_book_last_chapter.setText("自动投票数 " + String.valueOf(bean.getAmt()));

        iv_book_cover.setOnClickListener(
                v -> {
                    if(null != dynamicClickListener) {
                        dynamicClickListener.picClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
                }
        );

        tv_modify.setOnClickListener(
                v -> {
                    if(null != dynamicClickListener) {
                        dynamicClickListener.modifyClick(baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                    }
                }
        );

    }

    DynamicClickListener dynamicClickListener;

    public void setDynamicClickListener(DynamicClickListener dynamicClickListener) {
        this.dynamicClickListener = dynamicClickListener;
    }

    public interface DynamicClickListener {
        void modifyClick(int position);
        void picClick(int position);
    }


}
