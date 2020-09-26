package com.lianzai.reader.ui.adapter.holder;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.URLUtils;
import com.netease.nim.uikit.business.chatroom.viewholder.ChatRoomViewHolderHelper;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

public class IMTextInViewHolder extends IMBaseViewHolder{

    TextView im_tv_text;
    TextView im_tv_nickname;

    public IMTextInViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    int x=0;
    int y=0;

    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);

        im_tv_nickname=holder.getView(R.id.im_tv_nickname);
        im_tv_text=holder.getView(R.id.im_tv_text);

        //显示文本内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), im_tv_text,data.getContent() , -1, 0.65f,1);

        if (!TextUtils.isEmpty(ChatRoomViewHolderHelper.getNameText(data))) {
            ChatRoomViewHolderHelper.setNameText(data,im_tv_nickname,roleType,holder.getContext());
        }

        im_tv_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                x=(int)motionEvent.getX();
                y=(int)motionEvent.getY();
                return false;
            }
        });
        im_tv_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                chatRoomAdapter.getChatItemClickListener().contentLongClick(im_tv_text,position,data,x,y);
                return false;
            }
        });


    }

    private CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return spannedHtml;
    }

    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                  final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {      //在这里添加点击事件

            }
        };
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

}
