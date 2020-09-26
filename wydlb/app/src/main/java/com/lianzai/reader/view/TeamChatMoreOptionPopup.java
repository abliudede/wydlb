package com.lianzai.reader.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.extension.FightLuckBean;
import com.netease.nim.uikit.extension.ImLianzaihaoAttachment;
import com.netease.nim.uikit.extension.StickerAttachment;
import com.netease.nim.uikit.extension.UrlBookBean;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by lrz on 2018/3/19.
 */

public class TeamChatMoreOptionPopup extends PopupWindow implements View.OnClickListener {

    TextView tv_copy_text;
    TextView tv_delete_msg;
    TextView tv_praise_msg;
    TextView tv_tread_msg;
    TextView tv_quote_msg;

    Activity mContext;

    View contentView;

    TeamChatOptionItemListener teamChatOptionItemListener;

    public TeamChatMoreOptionPopup(Activity context, TeamChatOptionItemListener optionItemListener) {
        super(context);
        mContext=context;
        this.teamChatOptionItemListener=optionItemListener;
        initView();
    }

    private void initView(){
        contentView=mContext.getLayoutInflater().inflate(R.layout.chatroom_option_popup_window,null);
        tv_copy_text=(TextView)contentView.findViewById(R.id.tv_copy_text);
        tv_delete_msg=(TextView)contentView.findViewById(R.id.tv_delete_msg);
        tv_praise_msg=(TextView)contentView.findViewById(R.id.tv_praise_msg);

        tv_tread_msg=(TextView)contentView.findViewById(R.id.tv_tread_msg);
        tv_quote_msg=(TextView)contentView.findViewById(R.id.tv_quote_msg);


        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));
        this.setAnimationStyle(R.style.More_Options_Anim_Direction_Bottom);
        this.setFocusable(true);
        setContentView(contentView);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }


    public void showPopupWindow(View view, int bottomMargin, IMMessage iMMessage, int x, int y){
        showItemView(iMMessage);

        int[] location=new int[2];
        view.getLocationOnScreen(location);
        int showHeight = ScreenUtil.getDisplayHeight()-ScreenUtil.getStatusBarHeight(mContext)-location[1]-bottomMargin-view.getMeasuredHeight();

        //重新测量
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int windowHeight=contentView.getMeasuredHeight();
        int windowWidth=contentView.getMeasuredWidth();


        if (view.getMeasuredHeight()>ScreenUtil.getDisplayHeight()){
            this.setAnimationStyle(R.style.Chatroom_More_Options_In_Top_Anim);
            showAtLocation(view, Gravity.NO_GRAVITY,x+windowWidth/2,y+location[1]-windowHeight);

        }else{
            if (showHeight<windowHeight){//点击区域显示的区域大于弹框内容高度 ，默认在下面弹出，否则在上面弹出

                if (iMMessage.getDirect()== MsgDirectionEnum.Out){
                    this.setAnimationStyle(R.style.Chatroom_More_Options_Out_Top_Anim);
                    this.showAsDropDown(view,-RxImageTool.dip2px(30),-windowHeight-view.getMeasuredHeight()-RxImageTool.dip2px(5));
                }else{
                    this.setAnimationStyle(R.style.Chatroom_More_Options_In_Top_Anim);
                    this.showAsDropDown(view,0,-windowHeight-view.getMeasuredHeight()-RxImageTool.dip2px(5));
                }


            }else{
                if (iMMessage.getDirect()==MsgDirectionEnum.Out){
                    this.setAnimationStyle(R.style.Chatroom_More_Options_Out_Bottom_Anim);

                    this.showAsDropDown(view,-RxImageTool.dip2px(30),RxImageTool.dip2px(5));
                }else{
                    this.setAnimationStyle(R.style.Chatroom_More_Options_In_Bottom_Anim);
                    this.showAsDropDown(view,0,RxImageTool.dip2px(5));
                }

            }

        }

    }


    private void showItemView(IMMessage iMMessage){
        if (null==iMMessage)return;

        tv_copy_text.setOnClickListener(
                v->teamChatOptionItemListener.copyTextClick(iMMessage)
        );

        tv_delete_msg.setOnClickListener(
                v->teamChatOptionItemListener.deleteClick(iMMessage)
        );

        tv_tread_msg.setOnClickListener(
                v->teamChatOptionItemListener.treadClick(iMMessage)
        );

        tv_quote_msg.setOnClickListener(
                v->teamChatOptionItemListener.quoteClick(iMMessage)
        );

        tv_delete_msg.setVisibility(View.VISIBLE);
        if (iMMessage.getMsgType()== MsgTypeEnum.text){//文本
            tv_copy_text.setVisibility(View.VISIBLE);
            tv_quote_msg.setVisibility(View.VISIBLE);
            tv_tread_msg.setVisibility(View.VISIBLE);
        }
        else if (iMMessage.getMsgType()== MsgTypeEnum.audio){//音频
            tv_copy_text.setVisibility(View.GONE);
            tv_quote_msg.setVisibility(View.GONE);
            tv_tread_msg.setVisibility(View.VISIBLE);
        }else if(iMMessage.getMsgType()== MsgTypeEnum.image){
            tv_copy_text.setVisibility(View.GONE);
            tv_quote_msg.setVisibility(View.GONE);
            tv_tread_msg.setVisibility(View.VISIBLE);
        }else if(iMMessage.getMsgType()== MsgTypeEnum.custom){
            tv_copy_text.setVisibility(View.GONE);
            tv_tread_msg.setVisibility(View.VISIBLE);

            if (null!=iMMessage.getAttachment()&&iMMessage.getAttachment() instanceof ImLianzaihaoAttachment ){
                tv_quote_msg.setVisibility(View.GONE);
            }else if(null!=iMMessage.getAttachment()&&iMMessage.getAttachment() instanceof StickerAttachment){
                tv_quote_msg.setVisibility(View.GONE);
            }else if(null!=iMMessage.getAttachment()&&iMMessage.getAttachment() instanceof FightLuckBean){
                tv_quote_msg.setVisibility(View.GONE);
            }else if(null!=iMMessage.getAttachment()&&iMMessage.getAttachment() instanceof UrlBookBean){
                tv_quote_msg.setVisibility(View.GONE);
            }else{//其他类型，引用。踩都不显示
                tv_quote_msg.setVisibility(View.GONE);
                tv_tread_msg.setVisibility(View.GONE);
            }
        }
    }


    public interface TeamChatOptionItemListener{
        void treadClick(IMMessage iMMessage);
        void copyTextClick(IMMessage iMMessage);
        void deleteClick(IMMessage iMMessage);
        void quoteClick(IMMessage iMMessage);
    }


}
