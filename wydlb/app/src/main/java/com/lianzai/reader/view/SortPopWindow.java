package com.lianzai.reader.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.ui.adapter.SortItemAdapter;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.RxLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lrz on 2018/1/8.
 */

public class SortPopWindow extends PopupWindow {
    private Context context;
    private View conentView;
    private RecyclerView recyclerView;
    private SortItemAdapter sortItemAdapter;

    SortClickListener sortClickListener;
    List<String> sortList = new ArrayList<>();
    int[] location = new int[2];
    public SortPopWindow(Context context) {
        this.context = context;
        initView();

    }

    public SortClickListener getSortClickListener() {
        return sortClickListener;
    }

    public void setSortClickListener(SortClickListener sortClickListener) {
        this.sortClickListener = sortClickListener;
    }

    public SortPopWindow(Context context, List<String> strings,SortClickListener sortClickListener) {
        this.context = context;
        this.sortList = strings;
        this.sortClickListener=sortClickListener;
        initView();
    }
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.popup_recyclerview_sort, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        this.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.recyclerView = (RecyclerView) conentView.findViewById(R.id.recyclerView);
        this.sortItemAdapter = new SortItemAdapter(sortList,context);

        sortItemAdapter.getHashMap().put(0,true);//默认选中第一个
        sortItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                sortClickListener.sortClick(i);
                sortItemAdapter.initMaps();
                sortItemAdapter.getHashMap().put(i,true);
                sortItemAdapter.notifyDataSetChanged();
                dismiss();

            }
        });
        RxLinearLayoutManager manager=new RxLinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(sortItemAdapter);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    public interface SortClickListener{
        void sortClick(int pos);
    }
}