package com.wydlb.first.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by lrz on 2017/12/5.
 */

public class SectionBookDirectoryBean extends SectionEntity<BookDirectoryBean.VolumeChaptersBean.RecordBean> {


    BookDirectoryBean.VolumeChaptersBean.RecordBean bean;


    public SectionBookDirectoryBean(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionBookDirectoryBean(BookDirectoryBean.VolumeChaptersBean.RecordBean bean) {
        super(bean);
    }


}
