package com.wydlb.first.model.local;

import com.wydlb.first.model.bean.DownloadTaskBean;

import java.util.List;


/**
 * Created by newbiechen on 17-4-28.
 */

public interface GetDbHelper {

    /******************************/
    List<DownloadTaskBean> getDownloadTaskList();
}
