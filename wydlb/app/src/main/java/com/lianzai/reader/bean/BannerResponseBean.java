package com.lianzai.reader.bean;

import com.lianzai.reader.model.bean.BannerBean;

import java.io.Serializable;
import java.util.List;


public class BannerResponseBean implements Serializable {

    private static final long serialVersionUID = 3869981168898331823L;
    /**
     * status_code : 200
     * message : 获取数据成功
     * data : [{"id":206,"cat_id":7,"title":"赏金猎人","url_link":"https://m.lianzai.com/bonusPool","img_url":"https://static.lianzai.com/cover/20171224095516.png","desc":"","sort_order":4,"status":1,"is_mining":0,"create_time":"2017-12-24 09:32:39","update_time":"2017-12-24 09:55:19","name":"","category_name":""},{"id":200,"cat_id":7,"title":"\u201c一键赏\u201d功能权重增加","url_link":"https://m.lianzai.com/p/3676819252","img_url":"https://static.lianzai.com/cover/20171223100209.png","desc":"","sort_order":2,"status":1,"is_mining":0,"create_time":"2017-12-23 10:02:13","update_time":"2017-12-23 10:07:01","name":"","category_name":""},{"id":164,"cat_id":7,"title":"连载网挖矿作品列表","url_link":"https://m.lianzai.com/miningList","img_url":"https://static.lianzai.com/cover/20171213070329.png","desc":"","sort_order":0,"status":1,"is_mining":0,"create_time":"2017-12-13 19:03:33","update_time":"2017-12-23 10:06:54","name":"","category_name":""}]
     */

    private int status_code;
    private String message;
    private List<BannerBean> data;

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BannerBean> getData() {
        return data;
    }

    public void setData(List<BannerBean> data) {
        this.data = data;
    }

}
