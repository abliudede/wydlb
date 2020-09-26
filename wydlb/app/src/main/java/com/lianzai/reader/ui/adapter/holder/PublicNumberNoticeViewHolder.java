package com.lianzai.reader.ui.adapter.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.adapter.PublicNumberAdapter;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.extension.PublicNumberAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class PublicNumberNoticeViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, IMMessage> {
    int width;
    public PublicNumberNoticeViewHolder(BaseQuickAdapter adapter) {
        super(adapter);

    }

    protected TextView tv_timeline;
    protected SelectableRoundedImageView iv_img;
    protected TextView tv_title;
    protected TextView tv_content;
    protected TextView tv_open;

    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        width= RxDeviceTool.getScreenWidth(holder.getContext())- RxImageTool.dip2px(20);// 9:5

        tv_timeline=holder.getView(R.id.tv_timeline);
        iv_img=holder.getView(R.id.iv_img);
        tv_title=holder.getView(R.id.tv_title);

        tv_content=holder.getView(R.id.tv_content);
        tv_open=holder.getView(R.id.tv_open);

        String timeString = TimeUtil.getTimeShowString(data.getTime(), true);
        tv_timeline.setText(timeString);

        iv_img.setLayoutParams(new RelativeLayout.LayoutParams(width,width*5/9));

        if (null!=data.getAttachment()&&data.getAttachment() instanceof PublicNumberAttachment){
            PublicNumberAttachment attachment=(PublicNumberAttachment)data.getAttachment();

            if (attachment.getNewsType()== Constant.EXTENDFIELD.OFFICIAL_COMMUNICATION_TYPE){//通知
                iv_img.setVisibility(View.GONE);
                tv_open.setVisibility(View.GONE);
            }else if(attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_NOTICE){//公告
                iv_img.setVisibility(View.GONE);
                tv_open.setVisibility(View.VISIBLE);

            }else if(attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_NOSHARE){//app评分等无分享的
                iv_img.setVisibility(View.GONE);
                tv_open.setVisibility(View.VISIBLE);

            } else if(attachment.getNewsType()==Constant.EXTENDFIELD.OFFICIAL_WEEKLY){//周报
                iv_img.setVisibility(View.VISIBLE);
                tv_open.setVisibility(View.VISIBLE);
                if(null != attachment.getPublicNumberBean()) {
                    RxImageTool.loadWeeklyImage(holder.getContext(), attachment.getPublicNumberBean().getImg(), iv_img);
                }
            }
            if(null != attachment.getPublicNumberBean()) {
                tv_title.setText(attachment.getPublicNumberBean().getTitle());
                tv_content.setText(attachment.getPublicNumberBean().getContent());

            holder.getConvertView().setOnClickListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
                    //访问链接
                    ((PublicNumberAdapter)getAdapter()).getPublicNumberOptionCallback().openWebUrl(attachment.getPublicNumberBean().getUrl(),attachment.getPublicNumberBean().getTitle(),attachment.getPublicNumberBean().getContent(),attachment.getPublicNumberBean().getImg(),attachment.getNewsType());
                }
            });
            }

        }
    }

}
