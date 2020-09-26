package com.lianzai.reader.bean;

public class WSRequestBean {
//    {process: "latestTrade", pair: "read-cnt"}

    private String process;
    private String pair;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }
}
