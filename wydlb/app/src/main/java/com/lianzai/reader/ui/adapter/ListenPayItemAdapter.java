package com.lianzai.reader.ui.adapter;

import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.MyNoticeDataBean;
import com.lianzai.reader.ui.activity.circle.ActivityMyNotice;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.EllipsizeTextView;
import com.lianzai.reader.view.SelectableRoundedImageView;

import java.util.List;

/**
 * Created by lrz on 2019/3/21.
 * 听书设置
 */

public class ListenPayItemAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    ContentClickListener contentClickListener;

    public ListenPayItemAdapter(@Nullable List<String> data) {
        super(R.layout.item_listen_pay, data);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, String bean) {
        TextView tv_open= baseViewHolder.getView(R.id.tv_open);
        TextView tv_des= baseViewHolder.getView(R.id.tv_des);

        String[] list = bean.split("-");
        if(list.length > 1){
            String day = list[0];
            String readCount = list[1];
            StringBuilder sb = new StringBuilder();
            sb.append("开通/续费");
            sb.append(day);
            sb.append("日听书 锁定");
            sb.append(readCount);
            sb.append("阅点");
            //改变样式
            SpannableString mSpannableString = new SpannableString(sb.toString());
            mSpannableString.setSpan(new TextAppearanceSpan(mContext, R.style.ChengNormalStyle), 9+day.length(), sb.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_des.setText(mSpannableString);
        }
        tv_open.setOnClickListener(
                v -> {
                    getContentClickListener().payClick(v,baseViewHolder.getAdapterPosition() - getHeaderLayoutCount());
                }
        );

    }


    public interface ContentClickListener{
        void payClick(View v, int pos);
    }
}
