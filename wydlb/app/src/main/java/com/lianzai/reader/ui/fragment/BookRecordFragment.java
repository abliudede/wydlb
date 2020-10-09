package com.lianzai.reader.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookRecordHistoryResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.BookRecordExpandableItemAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 我的阅读足迹书籍tab
 */
public class BookRecordFragment extends BaseFragment  implements  SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.recyclerView)
    RecyclerView rv_data;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipe_refresh_layout;


    BookRecordExpandableItemAdapter bookRecordExpandableItemAdapter;

    ArrayList<MultiItemEntity> sectionBookDirectoryBeans = new ArrayList<>();//书籍目录


    boolean isLoadMore = false;
    boolean isError = false;
    int page = 1;

    CustomLoadMoreView customLoadMoreView;



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

        bookRecordExpandableItemAdapter= new BookRecordExpandableItemAdapter(sectionBookDirectoryBeans);

        customLoadMoreView = new CustomLoadMoreView();
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeColors(Color.parseColor("#333333"));

        bookRecordExpandableItemAdapter.setLoadMoreView(customLoadMoreView);
        bookRecordExpandableItemAdapter.setOnLoadMoreListener(this,rv_data);

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());

        rv_data.setAdapter(bookRecordExpandableItemAdapter);
//        rv_data.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        rv_data.setLayoutManager(manager);

        bookRecordExpandableItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (bookRecordExpandableItemAdapter.getData().get(position).getItemType()==BookRecordExpandableItemAdapter.CONTENT){
                    openBookClick((BookRecordHistoryResponse.DataBean.ListBeanX.ListBean)bookRecordExpandableItemAdapter.getData().get(position));
                }
            }
        });

        if (RxNetTool.isAvailable()){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null!=swipe_refresh_layout)
                        swipe_refresh_layout.setRefreshing(true);
                    onRefresh();
                }
            },getResources().getInteger(R.integer.refresh_delay));
        }else{
            bookRecordExpandableItemAdapter.setEmptyView(R.layout.view_disconnect_network);
        }

    }


    @Override
    public void fetchData() {
    }

    @Override
    public void onRefresh() {
        requestReadHistory(true);
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


    private void requestReadHistory(boolean isRefresh){
        if (isRefresh){
            page=1;
            isLoadMore = false;
        }else{
            isLoadMore = true;
            page++;
        }
        Map map=new HashMap();
        map.put("pageNum",String.valueOf(page));
        map.put("pageSize","10");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/user/read/history" ,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    bookRecordExpandableItemAdapter.loadMoreFail();
                    if (null!=swipe_refresh_layout)
                        swipe_refresh_layout.setRefreshing(false);
                }catch (Exception ex){

                }
            }


            @Override
            public void onResponse(String response) {
                try{
                    BookRecordHistoryResponse bookRecordHistoryResponse=GsonUtil.getBean(response,BookRecordHistoryResponse.class);
                    if (bookRecordHistoryResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        if (isRefresh){
                            sectionBookDirectoryBeans.clear();
                        }
                        if(null != bookRecordHistoryResponse.getData().getList()){
                            if (bookRecordHistoryResponse.getData().getList().size()>0){
                                for(int i=0;i<bookRecordHistoryResponse.getData().getList().size();i++){
                                    BookRecordHistoryResponse.DataBean.ListBeanX volumeBean=bookRecordHistoryResponse.getData().getList().get(i);
                                    for(int j=0;j<volumeBean.getList().size();j++){
                                        BookRecordHistoryResponse.DataBean.ListBeanX.ListBean chaptersBean=volumeBean.getList().get(j);
                                        volumeBean.addSubItem(chaptersBean);
                                    }
                                    sectionBookDirectoryBeans.add(volumeBean);
                                }
                                RxLogTool.e("sectionBookDirectoryBeans size:"+sectionBookDirectoryBeans.size());
                                bookRecordExpandableItemAdapter.setNewData(sectionBookDirectoryBeans);
                                bookRecordExpandableItemAdapter.expandAll();
                            }else{
                                if (page==1){
                                    bookRecordExpandableItemAdapter.setEmptyView(R.layout.view_book_history_record_empty,rv_data);
                                }else{
                                    bookRecordExpandableItemAdapter.loadMoreEnd();
                                }
                            }
                        }else {
                            if (page==1){
                                bookRecordExpandableItemAdapter.setEmptyView(R.layout.view_book_history_record_empty,rv_data);
                            }else{
                                bookRecordExpandableItemAdapter.loadMoreEnd();
                            }
                        }
                    }else{
                        RxToast.custom(bookRecordHistoryResponse.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                    if (null!=swipe_refresh_layout)
                        swipe_refresh_layout.setRefreshing(false);
                }catch (Exception e){

                }


            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        if (bookRecordExpandableItemAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
            RxLogTool.e("onLoadMoreRequested---不足一页");
            if (rv_data.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (rv_data.isComputingLayout() == false)) {
                bookRecordExpandableItemAdapter.loadMoreEnd();
            }
        } else {
            if (!isError) {
                requestReadHistory(false);
            } else {
                isError = true;
                bookRecordExpandableItemAdapter.loadMoreFail();
            }
        }
    }

    //打开阅读
    private void openBookClick(BookRecordHistoryResponse.DataBean.ListBeanX.ListBean bean){
        SkipReadUtil.normalRead(getActivity(),String.valueOf(bean.getBookId()),"",false);
    }


}
