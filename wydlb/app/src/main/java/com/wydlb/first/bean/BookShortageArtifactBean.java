package com.wydlb.first.bean;

import java.util.List;

public class BookShortageArtifactBean {


    /**
     * code : 0
     * msg : success
     * data : {"booknames":["剑来","斗破苍穹","将夜","庆余年","琥珀之剑","全球高武","诡秘之主","民国谍影","斗罗大陆","盘龙"],"msg":"🤖暂未搜到相关内容，为你推荐以下热门小说："}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * booknames : ["剑来","斗破苍穹","将夜","庆余年","琥珀之剑","全球高武","诡秘之主","民国谍影","斗罗大陆","盘龙"]
         * msg : 🤖暂未搜到相关内容，为你推荐以下热门小说：
         */

        private String msg;
        private List<String> booknames;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<String> getBooknames() {
            return booknames;
        }

        public void setBooknames(List<String> booknames) {
            this.booknames = booknames;
        }
    }
}
