package com.netease.nim.uikit.business.recent.adapter;

import android.support.v7.widget.RecyclerView;

import com.lianzai.reader.R;
import com.netease.nim.uikit.business.recent.RecentContactsCallback;
import com.netease.nim.uikit.business.recent.holder.CommonRecentViewHolder;
import com.netease.nim.uikit.business.recent.holder.TeamRecentViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class RecentContactAdapter extends BaseMultiItemQuickAdapter<RecentContact, BaseViewHolder> {

    interface ViewType {
        int VIEW_TYPE_COMMON = 1;
        int VIEW_TYPE_TEAM = 2;
    }

    private RecentContactsCallback callback;

    public RecentContactAdapter(RecyclerView recyclerView, List<RecentContact> data) {
        super(recyclerView, data);
        addItemType(ViewType.VIEW_TYPE_COMMON, R.layout.nim_recent_contact_list_item, CommonRecentViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_TEAM, R.layout.nim_recent_contact_list_item, TeamRecentViewHolder.class);
    }

    @Override
    protected int getViewType(RecentContact item) {
//        int type = 0;
//        int objectId = 0;
//        try {
//            type = (int) item.getExtension().get("type");
//            objectId = (int) item.getExtension().get("objectId");
//        }catch (Exception e){
//        }
//
//        //这里可以方便切分成两个类别
//        if(type == 1 && objectId == 1){
//            return ViewType.VIEW_TYPE_COMMON;
//        }else if(type == 2){
//            return ViewType.VIEW_TYPE_COMMON;
//        }else if(type == 3){
//            return ViewType.VIEW_TYPE_COMMON;
//        }else {
//            return ViewType.VIEW_TYPE_TEAM;
//        }
        return item.getSessionType() == SessionTypeEnum.Team ? ViewType.VIEW_TYPE_TEAM : ViewType.VIEW_TYPE_COMMON;
    }

    @Override
    protected String getItemKey(RecentContact item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getSessionType().getValue()).append("_").append(item.getContactId());

        return sb.toString();
    }

    public RecentContactsCallback getCallback() {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }
}
