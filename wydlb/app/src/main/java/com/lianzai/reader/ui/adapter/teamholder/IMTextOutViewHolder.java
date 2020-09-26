package com.lianzai.reader.ui.adapter.teamholder;

import android.text.Layout;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.URLUtils;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.model.IMMessage;

public class IMTextOutViewHolder extends IMBaseViewHolder {

    TextView im_tv_text;

    TextView im_tv_text_reply_name;
    TextView im_tv_text_reply;
    View im_tv_text_reply_view;
    LinearLayout im_tv_text_ly;

    int x=0;
    int y=0;
    public IMTextOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_text=holder.getView(R.id.im_tv_text);

        im_tv_text_reply_name=holder.getView(R.id.im_tv_text_reply_name);
        im_tv_text_reply=holder.getView(R.id.im_tv_text_reply);
        im_tv_text_reply_view=holder.getView(R.id.im_tv_text_reply_view);
        im_tv_text_ly=holder.getView(R.id.im_tv_text_ly);
        //显示文本内容
        String reply = null;
        String type = null;
        String name = null;
        try {
            type = (String) data.getRemoteExtension().get("remoteType");
            reply = (String) data.getRemoteExtension().get("content");
            name = (String) data.getRemoteExtension().get("name");
        }catch (Exception e){

        }
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), im_tv_text, data.getContent(), -1, 0.65f,2);
        if (null != reply && null != type && "quote".equals(type)) {
            im_tv_text_reply.setVisibility(View.VISIBLE);
            im_tv_text_reply_view.setVisibility(View.VISIBLE);
            im_tv_text_reply_name.setVisibility(View.VISIBLE);
            im_tv_text_reply_name.setText("回复：" + name);
            im_tv_text_reply.setText(reply);
        }else {
            im_tv_text_reply.setVisibility(View.GONE);
            im_tv_text_reply_view.setVisibility(View.GONE);
            im_tv_text_reply_name.setVisibility(View.GONE);
        }



//        im_tv_text.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                teamChatAdapter.getChatItemClickListener().contentLongClick(im_tv_text,position,data,x,y);
//                return true;
//            }
//        });
        im_tv_text_ly.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                teamChatAdapter.getChatItemClickListener().contentLongClick(im_tv_text,position,data,x,y);
                return true;
            }
        });

        im_tv_text.setOnTouchListener(new CustomTextViewTouchListener(null,new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                teamChatAdapter.getChatItemClickListener().contentLongClick(im_tv_text,position,data,x,y);
                return true;
            }
        }));

    }

    public class CustomTextViewTouchListener implements View.OnTouchListener {

        private View.OnClickListener mOnClickListener;
        private View.OnLongClickListener mOnLongClickListener;

        private long mLastActionDownTime = -1;

        //点击、长按、clickablespan示范代码，可参考使用
        public CustomTextViewTouchListener(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
            mOnClickListener = onClickListener;
            mOnLongClickListener = onLongClickListener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x=(int)event.getX();
            y=(int)event.getY();
            int action = event.getAction();
            TextView tv = (TextView) v;
            CharSequence text = tv.getText();
            if (action == MotionEvent.ACTION_DOWN) {
                mLastActionDownTime = System.currentTimeMillis();
            } else {
                long actionUpTime = System.currentTimeMillis();
                if (actionUpTime - mLastActionDownTime >= ViewConfiguration.getLongPressTimeout() && null != mOnLongClickListener) {
                    mOnLongClickListener.onLongClick(v);
                    return true;
                } else if (action == MotionEvent.ACTION_UP) {
                    if (text instanceof Spanned) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        x -= tv.getTotalPaddingLeft();
                        y -= tv.getTotalPaddingTop();
                        x += tv.getScrollX();
                        y += tv.getScrollY();
                        Layout layout = tv.getLayout();
                        int line = layout.getLineForVertical(y);
                        int off = layout.getOffsetForHorizontal(line, x);
                        ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);
                        if (link.length != 0) {
                            link[0].onClick(tv);
                        } else {
                            //do textview click event
                            if (null != mOnClickListener) {
                                mOnClickListener.onClick(v);
                            }
                        }
                    } else {
                        if (null != mOnClickListener) {
                            mOnClickListener.onClick(v);
                        }
                    }
                }
            }
            return true;
        }
    }


}
