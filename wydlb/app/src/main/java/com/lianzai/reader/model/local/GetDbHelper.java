package com.lianzai.reader.model.local;

import com.lianzai.reader.model.bean.DownloadTaskBean;

import java.util.List;


/**
 * Created by newbiechen on 17-4-28.
 */

public interface GetDbHelper {

    /******************************/
    List<DownloadTaskBean> getDownloadTaskList();
}
