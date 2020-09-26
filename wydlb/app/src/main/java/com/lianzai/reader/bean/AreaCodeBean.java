package com.lianzai.reader.bean;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by lrz on 2018/2/7.
 */

public class AreaCodeBean extends BaseIndexPinyinBean {

    /**
     * ISNI : Afghanistan
     * ChinaName : 阿富汗
     * QTQ : AF
     * InternaCode : 0093
     * REGENT : 亚洲
     * field6 : Босна и Херцеговина
     */

    private String ISNI;
    private String ChinaName;
    private String QTQ;
    private String InternaCode;
    private String REGENT;

    public String getISNI() {
        return ISNI;
    }

    public void setISNI(String ISNI) {
        this.ISNI = ISNI;
    }

    public String getChinaName() {
        return ChinaName;
    }

    public void setChinaName(String ChinaName) {
        this.ChinaName = ChinaName;
    }

    public String getQTQ() {
        return QTQ;
    }

    public void setQTQ(String QTQ) {
        this.QTQ = QTQ;
    }

    public String getInternaCode() {
        return InternaCode;
    }

    public void setInternaCode(String InternaCode) {
        this.InternaCode = InternaCode;
    }

    public String getREGENT() {
        return REGENT;
    }

    public void setREGENT(String REGENT) {
        this.REGENT = REGENT;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getTarget() {
        return getChinaName();
    }

    @Override
    public String toString() {
        return "AreaCodeBean{" +
                "ISNI='" + ISNI + '\'' +
                ", ChinaName='" + ChinaName + '\'' +
                ", QTQ='" + QTQ + '\'' +
                ", InternaCode='" + InternaCode + '\'' +
                ", REGENT='" + REGENT + '\'' +
                '}';
    }
}
