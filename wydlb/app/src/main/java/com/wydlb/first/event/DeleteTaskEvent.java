package com.wydlb.first.event;

import com.wydlb.first.model.bean.CollBookBean;

/**
 * Created by newbiechen on 17-5-27.
 */

public class DeleteTaskEvent {
    public CollBookBean collBook;

    public DeleteTaskEvent(CollBookBean collBook){
        this.collBook = collBook;
    }
}
