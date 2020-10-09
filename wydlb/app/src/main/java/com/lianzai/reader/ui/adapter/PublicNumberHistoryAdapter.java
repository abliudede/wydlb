package com.lianzai.reader.ui.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.PublicNumberHistoryBean;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;


/**
 * 历史消息
 */
public class PublicNumberHistoryAdapter extends BaseQuickAdapter<PublicNumberHistoryBean.DataBeanX.ListBean,BaseViewHolder> {


    LinearLayout ll_notice;

    RelativeLayout rl_weekly;

    RelativeLayout rl_chapter_update;

    HeadImageView rl_chapter_update_iv_book_cover;
    TextView rl_chapter_update_tv_chapter_detail;
    TextView rl_chapter_update_tv_chapter_title;

    SelectableRoundedImageView rl_weekly_iv_img;
    TextView rl_weekly_tv_title;
    TextView rl_weekly_tv_content;
    TextView tv_open;

    TextView ll_notice_tv_title;
    TextView ll_notice_tv_content;

    TextView tv_timeline;

    RelativeLayout rl_open_chat_room;

    TextView tv_open_chat_room;

    TextView tv_open_chat_room_title;

    Context context;
    int width;
    public PublicNumberHistoryAdapter(@Nullable List<PublicNumberHistoryBean.DataBeanX.ListBean> data, Context mContext) {
        super(R.layout.item_public_number_history_chapter, data);
        this.context=mContext;
        width= RxDeviceTool.getScreenWidth(context)- RxImageTool.dip2px(20);// 9:5
    }



    @Override
    protected void convert(final BaseViewHolder baseViewHolder, PublicNumberHistoryBean.DataBeanX.ListBean bean) {
        tv_timeline=baseViewHolder.getView(R.id.tv_timeline);

        ll_notice=baseViewHolder.getView(R.id.ll_notice);
        rl_weekly=baseViewHolder.getView(R.id.rl_weekly);
        rl_chapter_update=baseViewHolder.getView(R.id.rl_chapter_update);

        rl_chapter_update_iv_book_cover=baseViewHolder.getView(R.id.rl_chapter_update_iv_book_cover);
        rl_chapter_update_tv_chapter_detail=baseViewHolder.getView(R.id.rl_chapter_update_tv_chapter_detail);
        rl_chapter_update_tv_chapter_title=baseViewHolder.getView(R.id.rl_chapter_update_tv_chapter_title);

        rl_weekly_iv_img=baseViewHolder.getView(R.id.rl_weekly_iv_img);
        rl_weekly_iv_img.setLayoutParams(new RelativeLayout.LayoutParams(width,width*5/9));

        rl_weekly_tv_content=baseViewHolder.getView(R.id.rl_weekly_tv_content);
        rl_weekly_tv_title=baseViewHolder.getView(R.id.rl_weekly_tv_title);
        tv_open=baseViewHolder.getView(R.id.tv_open);

        ll_notice_tv_title=baseViewHolder.getView(R.id.ll_notice_tv_title);
        ll_notice_tv_content=baseViewHolder.getView(R.id.ll_notice_tv_content);

        rl_open_chat_room=baseViewHolder.getView(R.id.rl_open_chat_room);


        tv_timeline.setText(TimeFormatUtil.getInterval(String.valueOf(bean.getData().getCreatedAt())));

        tv_open_chat_room=baseViewHolder.getView(R.id.tv_open_chat_room);
        tv_open_chat_room_title=baseViewHolder.getView(R.id.tv_open_chat_room_title);


        if (bean.getNewsType()== Constant.EXTENDFIELD.OFFICIAL_COMMUNICATION_TYPE){//官方连载号通知
            rl_weekly.setVisibility(View.GONE);
            rl_chapter_update.setVisibility(View.GONE);
            ll_notice.setVisibility(View.VISIBLE);
            rl_open_chat_room.setVisibility(View.GONE);

            ll_notice_tv_title.setText(bean.getData().getTitle());
            ll_notice_tv_content.setText(bean.getData().getContent());

        }else if (bean.getNewsType()== Constant.EXTENDFIELD.LOCAL_CHAPTER_UPADTE_TYPE||bean.getNewsType()== Constant.EXTENDFIELD.OUTSIDE_CHAPTER_UPADTE_TYPE){//章节更新
            rl_weekly.setVisibility(View.GONE);
            rl_chapter_update.setVisibility(View.VISIBLE);
            ll_notice.setVisibility(View.GONE);
            rl_open_chat_room.setVisibility(View.GONE);

            rl_chapter_update_iv_book_cover.loadAvatar(bean.getData().getImg());
            rl_chapter_update_tv_chapter_detail.setText(bean.getData().getTitle()+" "+bean.getData().getContent());
            rl_chapter_update_tv_chapter_title.setText("章节有更新");

        }else if (bean.getNewsType()== Constant.EXTENDFIELD.OFFICIAL_NOSHARE|bean.getNewsType()== Constant.EXTENDFIELD.OFFICIAL_NOTICE||bean.getNewsType()== Constant.EXTENDFIELD.OFFICIAL_WEEKLY){//官方公告或者周报
            rl_chapter_update.setVisibility(View.GONE);
            ll_notice.setVisibility(View.GONE);
            rl_weekly.setVisibility(View.VISIBLE);
            rl_open_chat_room.setVisibility(View.GONE);

            rl_weekly_tv_content.setText(bean.getData().getContent());
            rl_weekly_tv_title.setText(bean.getData().getTitle());

            if (!TextUtils.isEmpty(bean.getData().getUrl())){
                tv_open.setVisibility(View.VISIBLE);
            }else{
                tv_open.setVisibility(View.GONE);
            }

            if (bean.getNewsType()== Constant.EXTENDFIELD.OFFICIAL_WEEKLY){
                rl_weekly_iv_img.setVisibility(View.VISIBLE);
                RxImageTool.loadWeeklyImage(mContext,bean.getData().getImg(),rl_weekly_iv_img);

            }else{
                rl_weekly_iv_img.setVisibility(View.GONE);
            }

        }else if(bean.getNewsType()== Constant.EXTENDFIELD.OPEN_CHAT_ROOM_MESSAGE){//聊天室打开
            rl_weekly.setVisibility(View.GONE);
            rl_chapter_update.setVisibility(View.GONE);
            ll_notice.setVisibility(View.GONE);
            rl_open_chat_room.setVisibility(View.VISIBLE);

            tv_open_chat_room.setText(bean.getData().getContent());
            tv_open_chat_room_title.setText(bean.getData().getTitle());

            rl_open_chat_room.setOnClickListener(new OnRepeatClickListener() {
                @Override
                public void onRepeatClick(View v) {
//                    ChatRoomActivity.startActivity(mContext,String.valueOf(bean.getData().getBookId()));
                }
            });

        }else if(bean.getNewsType()== Constant.EXTENDFIELD.JOIN_TEAM_MESSAGE){//群聊打开
            rl_weekly.setVisibility(View.GONE);
            rl_chapter_update.setVisibility(View.GONE);
            ll_notice.setVisibility(View.GONE);
            rl_open_chat_room.setVisibility(View.VISIBLE);

            tv_open_chat_room.setText(bean.getData().getContent());
            tv_open_chat_room_title.setText(bean.getData().getTitle());
        }else if(bean.getNewsType()== Constant.EXTENDFIELD.TEAM_MANAGE){//群聊成为管理员消息
            rl_weekly.setVisibility(View.GONE);
            rl_chapter_update.setVisibility(View.GONE);
            ll_notice.setVisibility(View.VISIBLE);
            rl_open_chat_room.setVisibility(View.GONE);

            ll_notice_tv_title.setText(bean.getData().getTitle());
            ll_notice_tv_content.setText(bean.getData().getContent());
        }else{//未知消息
            rl_weekly.setVisibility(View.GONE);
            rl_chapter_update.setVisibility(View.GONE);
            ll_notice.setVisibility(View.VISIBLE);
            rl_open_chat_room.setVisibility(View.GONE);

            ll_notice_tv_title.setText("");
            ll_notice_tv_content.setText("当前版本无法显示该消息");
        }

    }

}
