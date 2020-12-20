package com.wydlb.first.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wydlb.first.R;
import com.wydlb.first.base.BaseFragment;
import com.wydlb.first.bean.AccountTokenBean;
import com.wydlb.first.bean.WebHistoryListBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.model.bean.WebHistoryBean;
import com.wydlb.first.model.local.WebHistoryRepository;
import com.wydlb.first.ui.activity.ActivityWebView;
import com.wydlb.first.ui.adapter.BookRecordExpandableItemAdapter;
import com.wydlb.first.ui.adapter.WebRecordExpandableItemAdapter;
import com.wydlb.first.utils.RxLoginTool;
import com.wydlb.first.utils.TimeFormatUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;


/**
 * 我的阅读足迹网页tab
 */
public class WebRecordFragment extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.recyclerView)
    RecyclerView rv_data;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;


    WebRecordExpandableItemAdapter webRecordExpandableItemAdapter;

    ArrayList<MultiItemEntity> sectionBookDirectoryBeans = new ArrayList<>();//书籍目录


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recyclerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        initDynamicAdapter();
    }


    private void initDynamicAdapter(){

        webRecordExpandableItemAdapter= new WebRecordExpandableItemAdapter(sectionBookDirectoryBeans);

        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#333333"));


        LinearLayoutManager manager=new LinearLayoutManager(getActivity());

        rv_data.setAdapter(webRecordExpandableItemAdapter);
        rv_data.setLayoutManager(manager);

        webRecordExpandableItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (webRecordExpandableItemAdapter.getData().get(position).getItemType()==BookRecordExpandableItemAdapter.CONTENT){
                    openBookClick((WebHistoryListBean.ListBean)webRecordExpandableItemAdapter.getData().get(position));
                }
            }
        });

                    onRefresh();
    }


    @Override
    public void fetchData() {
    }

    @Override
    public void onRefresh() {
        requestReadHistory();
    }


    private void showSwipeRefresh(boolean isShow){
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if (null!=swipe_refresh_layout){
                        swipe_refresh_layout.setRefreshing(isShow);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },getResources().getInteger(R.integer.refresh_delay));
    }

    LinkedHashMap<String,WebHistoryListBean> tempMap = new LinkedHashMap<String,WebHistoryListBean>();


    private void requestReadHistory(){
        AccountTokenBean account = RxLoginTool.getLoginAccountToken();
        String uid = "0";
        if(null != account){
            uid = String.valueOf(account.getData().getId());
        }
        List<WebHistoryBean> list = WebHistoryRepository.getInstance().getWebHistoryList(uid);
        if (null != list && !list.isEmpty()){
            sectionBookDirectoryBeans.clear();
            tempMap.clear();
            for(int i=0; i<list.size(); i++) {
                WebHistoryBean oItem = list.get(i);
                //获取此项的日期key
                String date = TimeFormatUtil.getIntervalOther(oItem.getCreattime());
                //从map中操作，进行按日期分组
                if (tempMap.containsKey(date)) {
                    WebHistoryListBean data = tempMap.get(date);
                    WebHistoryListBean.ListBean item = new WebHistoryListBean.ListBean();
                    item.setName(oItem.getTitle());
                    item.setUrl(oItem.getUrl());
                    data.addSubItem(item);
                    tempMap.put(date, data);
                } else {
                    WebHistoryListBean data = new WebHistoryListBean();
                    data.setDay(date);
                    WebHistoryListBean.ListBean item = new WebHistoryListBean.ListBean();
                    item.setName(oItem.getTitle());
                    item.setUrl(oItem.getUrl());
                    data.addSubItem(item);
                    tempMap.put(date, data);
                }
            }

            //从map中按key取出值
            // 获取所有键值对对象的集合
//            Set<LinkedHashMap.Entry<String, WebHistoryListBean>> set = tempMap.entrySet();
            // 遍历键值对对象的集合，得到每一个键值对对象
            for (LinkedHashMap.Entry<String, WebHistoryListBean> me : tempMap.entrySet()) {
                // 根据键值对对象获取键和值
                String key = me.getKey();
                WebHistoryListBean value = me.getValue();
                sectionBookDirectoryBeans.add(value);
            }


            webRecordExpandableItemAdapter.setNewData(sectionBookDirectoryBeans);
            webRecordExpandableItemAdapter.expandAll();
        }else{
            webRecordExpandableItemAdapter.setEmptyView(R.layout.view_book_history_record_empty,rv_data);
        }
        showSwipeRefresh(false);
    }



    //打开阅读
    private void openBookClick(WebHistoryListBean.ListBean bean){
        ActivityWebView.startActivity(getActivity(),bean.getUrl());
    }


}
