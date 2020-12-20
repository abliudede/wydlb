package com.wydlb.first.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wydlb.first.R;
import com.wydlb.first.bean.ChasingBookListBean;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.TimeFormatUtil;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 */

public class ChasingBookItemAdapter extends BaseQuickAdapter<ChasingBookListBean.DataBeanX.FollowListBean.DataBean,BaseViewHolder> {


    Context context;
    public ChasingBookItemAdapter(int layoutResId, @Nullable List<ChasingBookListBean.DataBeanX.FollowListBean.DataBean> data,Context mContext) {
        super(layoutResId, data);
        this.context=mContext;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, ChasingBookListBean.DataBeanX.FollowListBean.DataBean bean) {
        final ImageView roundedImageView=baseViewHolder.getView(R.id.iv_book_cover);
        RxImageTool.loadImage(context,bean.getCover(),roundedImageView);

        TextView tvBookTitle=baseViewHolder.getView(R.id.tv_book_title);
        TextView tvIsUpdate=baseViewHolder.getView(R.id.tv_is_update);
        TextView tvUpdateChapter=baseViewHolder.getView(R.id.tv_book_update_chapter);
        TextView tvAttentionCount=baseViewHolder.getView(R.id.tv_book_attention_count);

        tvBookTitle.setText(bean.getNovel_title());
//        if (!RxDataTool.isEmpty(bean.getRead_tag())){
//            tvIsUpdate.setVisibility(View.VISIBLE);
//        }else{
//            tvIsUpdate.setVisibility(View.GONE);
//        }
        tvAttentionCount.setText(bean.getMember_count()+"关注 | "+bean.getPost_count()+"帖子 | "+bean.getWord_num()+"字");
        tvUpdateChapter.setText(TimeFormatUtil.getInterval(bean.getLastest_chapter_shelf_time())+" "+bean.getLastest_chapter_title());
    }

}
