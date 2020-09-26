package com.lianzai.reader.bean;

import java.util.List;

/**
 * Copyright (C), 2018
 * FileName: IsOpenChatRoomResponse
 * Author: lrz
 * Date: 2018/9/14 09:59
 * Description: ${DESCRIPTION}
 */
public class IsOpenChatRoomResponse extends BaseBean {


    /**
     * data : {"roomId":56583369,"chatroomRecentNewsList":[{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君","attach":"1288"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君","attach":"1589"},{"fromAvator":"https://static.lianzai.com/static/avatar.png","fromNick":"李荣浩","attach":"158"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君在[云巅仙缘]的昵称","attach":"hello"},{"fromAvator":"https://static.lianzai.com/iOS_avatar/153792351776751.jpeg","fromNick":"哇喔","attach":"默契吗OK名哦"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"1800"},{"fromAvator":"https://static.lianzai.com/iOS_avatar/153792351776751.jpeg","fromNick":"哇喔","attach":"名女 Mr 的"},{"fromAvator":"https://static.lianzai.com/static/avatar.png","fromNick":"李荣浩","attach":"3468"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君在[云巅仙缘]的昵称","attach":"2155"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI81hOdg7FFb52Xuv0JEentv3cACAibNTjpic5c6SiceRHZ2DG04iah2JhevENGY3EVf9deHfwqT3iabew/0","fromNick":"Mond","attach":"159 "},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI81hOdg7FFb52Xuv0JEentv3cACAibNTjpic5c6SiceRHZ2DG04iah2JhevENGY3EVf9deHfwqT3iabew/0","fromNick":"Mond","attach":"112233"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝","attach":"145"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"为啥看不到对方的头像"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝","attach":"15"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"我是作者"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「Mond:图片」\n- - - - - - - - - - - - - - - - - - - - - \n458885"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「Mond:语音内容」\n- - - - - - - - - - - - - - - - - - - - - \n219798"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「幻梦:「李荣浩:111」\n- - - - - - - - - - - - - - - - - - - - -\n 24589834」\n- - - - - - - - - - - - - - - - - - - - -\n 3498"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「李荣浩:111」\n- - - - - - - - - - - - - - - - - - - - -\n 24589834"},{"fromAvator":"https://static.lianzai.com/static/avatar.png","fromNick":"李荣浩","attach":"1589"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * roomId : 56583369
         * chatroomRecentNewsList : [{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君","attach":"1288"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君","attach":"1589"},{"fromAvator":"https://static.lianzai.com/static/avatar.png","fromNick":"李荣浩","attach":"158"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君在[云巅仙缘]的昵称","attach":"hello"},{"fromAvator":"https://static.lianzai.com/iOS_avatar/153792351776751.jpeg","fromNick":"哇喔","attach":"默契吗OK名哦"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"1800"},{"fromAvator":"https://static.lianzai.com/iOS_avatar/153792351776751.jpeg","fromNick":"哇喔","attach":"名女 Mr 的"},{"fromAvator":"https://static.lianzai.com/static/avatar.png","fromNick":"李荣浩","attach":"3468"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝君在[云巅仙缘]的昵称","attach":"2155"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI81hOdg7FFb52Xuv0JEentv3cACAibNTjpic5c6SiceRHZ2DG04iah2JhevENGY3EVf9deHfwqT3iabew/0","fromNick":"Mond","attach":"159 "},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI81hOdg7FFb52Xuv0JEentv3cACAibNTjpic5c6SiceRHZ2DG04iah2JhevENGY3EVf9deHfwqT3iabew/0","fromNick":"Mond","attach":"112233"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝","attach":"145"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"为啥看不到对方的头像"},{"fromAvator":"https://static.lianzai.com/avatar/1519377609669.png","fromNick":"哝哝","attach":"15"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"我是作者"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「Mond:图片」\n- - - - - - - - - - - - - - - - - - - - - \n458885"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「Mond:语音内容」\n- - - - - - - - - - - - - - - - - - - - - \n219798"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「幻梦:「李荣浩:111」\n- - - - - - - - - - - - - - - - - - - - -\n 24589834」\n- - - - - - - - - - - - - - - - - - - - -\n 3498"},{"fromAvator":"https://wx.qlogo.cn/mmopen/vi_32/sHPrgfrTWlXeSp0H9JstkGiacUdo2D3Ks5hjufA1s9rmXMAgj3FWCuuwSaicDMwWwxKfTzJ3JgjtR3wibcWuxibGHw/0","fromNick":"幻梦","attach":"「李荣浩:111」\n- - - - - - - - - - - - - - - - - - - - -\n 24589834"},{"fromAvator":"https://static.lianzai.com/static/avatar.png","fromNick":"李荣浩","attach":"1589"}]
         */

        private int roomId;
        private boolean hidden;
        private List<ChatroomRecentNewsListBean> chatroomRecentNewsList;

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public List<ChatroomRecentNewsListBean> getChatroomRecentNewsList() {
            return chatroomRecentNewsList;
        }

        public void setChatroomRecentNewsList(List<ChatroomRecentNewsListBean> chatroomRecentNewsList) {
            this.chatroomRecentNewsList = chatroomRecentNewsList;
        }

        public static class ChatroomRecentNewsListBean {
            /**
             * fromAvator : https://static.lianzai.com/avatar/1519377609669.png
             * fromNick : 哝哝君
             * attach : 1288
             */

            private String fromAvator;
            private String fromNick;
            private String attach;

            public String getFromAvator() {
                return fromAvator;
            }

            public void setFromAvator(String fromAvator) {
                this.fromAvator = fromAvator;
            }

            public String getFromNick() {
                return fromNick;
            }

            public void setFromNick(String fromNick) {
                this.fromNick = fromNick;
            }

            public String getAttach() {
                return attach;
            }

            public void setAttach(String attach) {
                this.attach = attach;
            }
        }
    }
}
