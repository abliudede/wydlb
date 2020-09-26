package com.lianzai.reader.ui.adapter.holder;

import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.utils.URLUtils;
import com.netease.nim.uikit.business.session.emoji.MoonUtil;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

public class IMTextOutViewHolder extends IMBaseViewHolder {

    TextView im_tv_text;


    int x=0;
    int y=0;
    public IMTextOutViewHolder(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }


    @Override
    public void convert(BaseViewHolder holder, ChatRoomMessage data, int position, boolean isScrolling) {
        super.convert(holder,data,position,isScrolling);
        im_tv_text=holder.getView(R.id.im_tv_text);

        //显示文本内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), im_tv_text, data.getContent(), -1, 0.65f,2);

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

}
