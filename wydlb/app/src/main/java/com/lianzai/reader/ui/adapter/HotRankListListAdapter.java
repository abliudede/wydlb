package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.HotRankingListBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2019/01/11.
 */

public class HotRankListListAdapter extends BaseQuickAdapter<HotRankingListBean.DataBean,BaseViewHolder> {


    Context context;
    public HotRankListListAdapter(@Nullable List<HotRankingListBean.DataBean> data, Context mContext) {
        super(R.layout.item_hotranking_list, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder,HotRankingListBean.DataBean bean) {
        if (null==bean)return;
        SelectableRoundedImageView iv_book_list_cover=baseViewHolder.getView(R.id.iv_book_list_cover);
        TextView iv_book_list_description1=baseViewHolder.getView(R.id.iv_book_list_description1);
        TextView iv_book_list_description2=baseViewHolder.getView(R.id.iv_book_list_description2);
        TextView iv_book_list_description3=baseViewHolder.getView(R.id.iv_book_list_description3);

        RxImageTool.loadSquareImage(context,bean.getCover(),iv_book_list_cover);
        iv_book_list_description1.setVisibility(View.GONE);
        iv_book_list_description2.setVisibility(View.GONE);
        iv_book_list_description3.setVisibility(View.GONE);
        if(null != bean.getDetails()){
            if(!bean.getDetails().isEmpty()){
                int i = 0;
                for(HotRankingListBean.DataBean.DetailsBean item:bean.getDetails()){
                    StringBuilder sb = new StringBuilder();
                    sb.append(i+1);
                    sb.append(".");
                    sb.append(item.getBookTitle());
                    sb.append(" - ");
                    sb.append(item.getBookAuthor());
                    switch (i){
                        case 0:
                            iv_book_list_description1.setVisibility(View.VISIBLE);
                            iv_book_list_description1.setText(sb.toString());
                            break;
                        case 1:
                            iv_book_list_description2.setVisibility(View.VISIBLE);
                            iv_book_list_description2.setText(sb.toString());
                            break;
                        case 2:
                            iv_book_list_description3.setVisibility(View.VISIBLE);
                            iv_book_list_description3.setText(sb.toString());
                            break;
                    }
                        i++;
                }
            }
        }



    }

}
