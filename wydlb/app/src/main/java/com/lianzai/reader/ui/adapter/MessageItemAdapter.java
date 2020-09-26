package com.lianzai.reader.ui.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.MessageBean;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.CircleImageView;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 通知消息
 */

public class MessageItemAdapter extends BaseQuickAdapter<MessageBean.DataBeanX.DataBean,BaseViewHolder> {

//

    public MessageItemAdapter(@Nullable List<MessageBean.DataBeanX.DataBean> data) {
        super(R.layout.item_my_message_detail, data);
    }


    @Override
    protected void convert(final BaseViewHolder baseViewHolder, MessageBean.DataBeanX.DataBean bean) {

        CircleImageView iv_logo=baseViewHolder.getView(R.id.iv_logo);
        TextView tv_name=baseViewHolder.getView(R.id.tv_name);

        TextView tv_content=baseViewHolder.getView(R.id.tv_content);
        TextView tv_source=baseViewHolder.getView(R.id.tv_source);

        TextView tv_date=baseViewHolder.getView(R.id.tv_date);
        ImageView iv_read_status=baseViewHolder.getView(R.id.iv_read_status);

        iv_logo.setImageResource(R.mipmap.ic_launcher);
        tv_name.setText(bean.getContent().getTitle());

        if (!TextUtils.isEmpty(bean.getContent().getContent())){

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_content.setText(Html.fromHtml(bean.getContent().getContent(),Html.FROM_HTML_MODE_LEGACY).toString());
            } else {
                tv_content.setText(Html.fromHtml(bean.getContent().getContent()).toString());
            }
        }

        tv_source.setVisibility(View.GONE);
        try{

            tv_date.setText(TimeFormatUtil.getInterval(bean.getCreate_time()));
        }catch (Exception e){

        }
        if (bean.isRead()){
            iv_read_status.setVisibility(View.GONE);
        }else{
            iv_read_status.setVisibility(View.VISIBLE);
        }

    }

}
