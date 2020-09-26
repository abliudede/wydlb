package com.lianzai.reader.ui.adapter;

import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.CommentDetailBean;
import com.lianzai.reader.bean.ReadabilityBean;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.page.PageStyle;

import java.util.List;

/**
 * Created by lrz on 2017/10/13.
 * 纯净阅读模式的适配器
 */

public class PureReadItemAdapter extends BaseQuickAdapter<ReadabilityBean,BaseViewHolder> {

    int textSize = 46;
    private PageStyle pageStyle;
    private int x;
    private int y;

    ContentClickListener contentClickListener;

    public PureReadItemAdapter(@Nullable List<ReadabilityBean> data) {
        super(R.layout.item_pure_read, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, ReadabilityBean bean) {
        TextView tv_content=baseViewHolder.getView(R.id.tv_content);
        TextView tv_title=baseViewHolder.getView(R.id.tv_title);

        try{
            int mTextColor = ContextCompat.getColor(mContext, pageStyle.getFontColor());
//            int mBgColor = ContextCompat.getColor(mContext, pageStyle.getBgColor());

            tv_title.setTextColor(mTextColor);
//            tv_title.setBackgroundColor(mBgColor);
            tv_content.setTextColor(mTextColor);
//            tv_content.setBackgroundColor(mBgColor);
//            if (pageStyle.getBgColor() == R.color.nb_read_bg_8) {
//                tv_title.setBackgroundResource(R.drawable.bg1);
//                tv_content.setBackgroundResource(R.drawable.bg1);
//            }

        }catch (Exception e){

        }

        int normal = RxImageTool.dip2px(24);
        int high = RxImageTool.dip2px(92);

        if(baseViewHolder.getAdapterPosition() == 0){
            tv_title.setPadding(normal,high,normal,normal);
        }else {
            tv_title.setPadding(normal,normal,normal,normal);
        }

        tv_title.setTextSize(textSize + 6);
        tv_content.setTextSize(textSize);

        if(null != bean.getData()){
            tv_title.setText("-------" + bean.getData().getTitle() + "-------" );
            String content = bean.getData().getContent().replace("\n","\n\u3000\u3000");
            content = content.replace("      ","\n\u3000\u3000");
            tv_content.setText("\u3000\u3000" + content);
        }

        tv_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() != MotionEvent.ACTION_CANCEL) {
                    x = (int) motionEvent.getRawX();
                    y = (int) motionEvent.getRawY();
//                }
                return false;
            }
        });
        tv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() != MotionEvent.ACTION_CANCEL) {
                    x = (int) motionEvent.getRawX();
                    y = (int) motionEvent.getRawY();
//                }
                return false;
            }
        });


        tv_title.setOnClickListener(
                v -> {
                    if(null != contentClickListener){
                        contentClickListener.titleClick(baseViewHolder.getAdapterPosition()- getHeaderLayoutCount(),x,y);
                    }
                }
        );

        tv_content.setOnClickListener(
                v -> {
                    if(null != contentClickListener){
                        contentClickListener.contentClick(baseViewHolder.getAdapterPosition()- getHeaderLayoutCount(),x,y);
                    }
                }
        );


    }

    public void setTextSize(int textSize) {
        this.textSize  = textSize;
    }

    public void setPageStyleChecked(PageStyle pageStyle){
        this.pageStyle = pageStyle;
    }

    public interface ContentClickListener {
        void titleClick(int position,int x,int y);

        void contentClick(int position,int x,int y);
    }

    public ContentClickListener getContentClickListener() {
        return contentClickListener;
    }

    public void setContentClickListener(ContentClickListener contentClickListener) {
        this.contentClickListener = contentClickListener;
    }
}
