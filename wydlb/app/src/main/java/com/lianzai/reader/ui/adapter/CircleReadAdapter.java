package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.CircleReadBean;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * 书圈功能
 */
public class CircleReadAdapter extends BaseQuickAdapter<CircleReadBean.DataBean.BookCirclePageBean.ListBean, BaseViewHolder> {


    public CircleReadAdapter(@Nullable List<CircleReadBean.DataBean.BookCirclePageBean.ListBean> data, Context mContext) {
        super(R.layout.item_circle_read, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, CircleReadBean.DataBean.BookCirclePageBean.ListBean obj) {
        //用户信息
        SelectableRoundedImageView iv_dynamic_book_cover = holder.getView(R.id.iv_dynamic_book_cover);
        ImageView iv_tag = holder.getView(R.id.iv_tag);
        TextView tv_person_count = holder.getView(R.id.tv_person_count);
        TextView tv_dynamic_book_title = holder.getView(R.id.tv_dynamic_book_title);
        TextView tv_dynamic_book_description = holder.getView(R.id.tv_dynamic_book_description);
        TextView tv_dynamic_book_descontent = holder.getView(R.id.tv_dynamic_book_descontent);
//        View bootom_view = holder.getView(R.id.bootom_view);

//        //最后一个视图增加一个底部间隔
//        try{
//            if(holder.getAdapterPosition() - getHeaderLayoutCount() >= mData.size() - 1){
//                bootom_view.setVisibility(View.VISIBLE);
//            }else {
//                bootom_view.setVisibility(View.GONE);
//            }
//        }catch (Exception e){
//        }
        ImageView gongxiangbanquan_jiaobiao = holder.getView(R.id.gongxiangbanquan_jiaobiao);

        if(obj.isCopyright()){
            gongxiangbanquan_jiaobiao.setVisibility(View.VISIBLE);
        }else {
            gongxiangbanquan_jiaobiao.setVisibility(View.GONE);
        }

        /*开始数据加载*/
        /*通用部分*/
        RxImageTool.loadImage(mContext, obj.getBookCover(), iv_dynamic_book_cover);
        int position = holder.getAdapterPosition() - getHeaderLayoutCount();
        if(position == 0){
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setImageResource(R.mipmap.circle_read_one);
        }else if(position == 1){
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setImageResource(R.mipmap.circle_read_two);
        }else if(position == 2){
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setImageResource(R.mipmap.circle_read_three);
        }else {
            iv_tag.setVisibility(View.GONE);
        }

        if(obj.getAttentionNumber() > 0 ){
            tv_person_count.setVisibility(View.VISIBLE);
            tv_person_count.setText(String.valueOf(obj.getAttentionNumber()) + "\n" + "圈友在读");
        }else {
            tv_person_count.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(obj.getBookName())) {
            tv_dynamic_book_title.setText(obj.getBookName());
        }else {
            tv_dynamic_book_title.setText("");
        }

        StringBuilder des = new StringBuilder();
        if(!TextUtils.isEmpty(obj.getBookAuthor())) {
            des.append(obj.getBookAuthor());
        }
        if(!TextUtils.isEmpty(obj.getCategoryName())) {
            des.append("|");
            des.append(obj.getCategoryName());
        }
        if(!TextUtils.isEmpty(obj.getBookType())) {
            des.append("·");
            des.append(obj.getBookType());
        }

        if(!TextUtils.isEmpty(des)){
            tv_dynamic_book_description.setText(des.toString());
        } else {
            tv_dynamic_book_description.setText("");
        }

        if(!TextUtils.isEmpty(obj.getBookInfo())) {
            tv_dynamic_book_descontent.setText(obj.getBookInfo());
        }else {
            tv_dynamic_book_descontent.setText("");
        }


    }

}
