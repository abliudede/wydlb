package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lianzai.reader.R;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.model.bean.ContactsBean;
import com.lianzai.reader.model.gen.ContactsBeanDao;
import com.lianzai.reader.model.local.DaoDbHelper;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.view.ItemRenWu;
import com.lianzai.reader.view.ItemRenWu2;

import java.util.List;

/**
 * Created by lrz on 2018/10/13.
 * 任务列表
 */

public class RenWuItemAdapterForLinearLayout {


    public static void reSetData(LinearLayout list,List<TaskCenterBean.DataBean.DailyTaskListBean> booksBeanList1,Context context){
        list.removeAllViews();
        if(null != booksBeanList1 && !booksBeanList1.isEmpty()){
            for(TaskCenterBean.DataBean.DailyTaskListBean item : booksBeanList1){
                ItemRenWu temp = new ItemRenWu(context);
                temp.bindData(item);
                list.addView(temp);
            }
        }
    }

    public static void reSetData2(LinearLayout list,List<TaskCenterBean.DataBean.DailyTaskListBean> booksBeanList1,Context context){
        list.removeAllViews();
        if(null != booksBeanList1 && !booksBeanList1.isEmpty()){
            for(TaskCenterBean.DataBean.DailyTaskListBean item : booksBeanList1){
                ItemRenWu2 temp = new ItemRenWu2(context);
                temp.bindData(item);
                list.addView(temp);
            }
        }
    }


}
