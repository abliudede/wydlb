package com.wydlb.first.bean;

import java.io.Serializable;
import java.util.List;

public class ReadSettingsResponse implements Serializable{

    private List<ReadSettingsBean> LianzaiReaderSettings;

    public List<ReadSettingsBean> getReadSettings() {
        return LianzaiReaderSettings;
    }

    public void setReadSettings(List<ReadSettingsBean> readSettings) {
        this.LianzaiReaderSettings = readSettings;
    }

    public static class ReadSettingsBean implements Serializable{
        /**
         * charset : GBK
         * source : biqiuge.com
         * regTitle : <h1>(.+?)</h1>
         * regContent : <div id="content" class="showtxt">(.+?)</div>
         */

        private String charset;
        private String source;
        private String regTitle;
        private String regContent;
        private String cName;
        private String[] illegalWords;

        private String error;

        public String getSourceName() {
            return cName;
        }

        public void setSourceName(String sourceName) {
            this.cName = sourceName;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String[] getIllegalWords() {
            return illegalWords;
        }

        public void setIllegalWords(String[] illegalWords) {
            this.illegalWords = illegalWords;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getRegTitle() {
            return regTitle;
        }

        public void setRegTitle(String regTitle) {
            this.regTitle = regTitle;
        }

        public String getRegContent() {
            return regContent;
        }

        public void setRegContent(String regContent) {
            this.regContent = regContent;
        }
    }
}
