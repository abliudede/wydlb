package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.SearchNovelsBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.activity.chat.PublicNumberDetailActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.BooksSearchAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 搜索--书籍
 */
public class SearchBookFragment extends BaseFragment  {

    @Bind(R.id.recyclerView)
    RecyclerView rv_search_book;

    CustomLoadMoreView customLoadMoreView;

    @Bind(R.id.loading_view)
    View loading_view;


    BooksSearchAdapter booksSearchAdapter;

    int pageNum=1;
    int pageSize=10;

    String mKeyWord;

    List<SearchNovelsBean.DataBean.ListBean> booksBeanList=new ArrayList<>();


    ActivitySearch2 activitySearch2;

    View emptyView;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitySearch2 =(ActivitySearch2)activity;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recyclerview_not_refresh;
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
        initBookAdapter();
    }


    private void initBookAdapter(){
        booksSearchAdapter=new BooksSearchAdapter(booksBeanList,getContext());
        customLoadMoreView = new CustomLoadMoreView();

        emptyView=getLayoutInflater().inflate(R.layout.view_search_book_list_record_empty,null);


        booksSearchAdapter.setLoadMoreView(customLoadMoreView);
        booksSearchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNum++;
                bookListRequest(mKeyWord,false);

            }
        }, rv_search_book);

        booksSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SearchNovelsBean.DataBean.ListBean bean=booksSearchAdapter.getData().get(i);
                //区分跳名片页还是圈子首页
                if(bean.getPlatformType() == 1 && RxAppTool.isCircle(bean.getPlatformId())){
                    ActivityCircleDetail.startActivity(getActivity(),String.valueOf(bean.getPlatformId()));
                }else if(bean.getPlatformType() == 2){
                    ActivityCircleDetail.startActivity(getActivity(),String.valueOf(bean.getPlatformId()));
                }else if(bean.getPlatformType() == 3){
                    ActivityCircleDetail.startActivity(getActivity(),String.valueOf(bean.getPlatformId()));
                }else if(bean.getPlatformType() == 4){
                    ActivityCircleDetail.startActivity(getActivity(),String.valueOf(bean.getPlatformId()));
                }else {
                    PublicNumberDetailActivity.startActivity(getActivity(),bean.getPlatformId());
                }
            }
        });

        RxLinearLayoutManager manager = new RxLinearLayoutManager(getContext());
        rv_search_book.setLayoutManager(manager);
        rv_search_book.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

        rv_search_book.setAdapter(booksSearchAdapter);

    }


    @Override
    public void fetchData() {


    }

    SearchNovelsBean.DataBean dataBean;


    public void bookListRequest(String keyword,boolean isRefresh){

        mKeyWord=keyword;
        if (isRefresh){
            pageNum=1;

            if (null!=loading_view){
                loading_view.setVisibility(View.VISIBLE);
            }
        }
        Map<String,String> map=new HashMap<>();
        map.put("pageNum",String.valueOf(pageNum));
        map.put("pageSize",String.valueOf(pageSize));

        if (pageNum>3){
            activitySearch2.showGuideView();
        }

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/search/"+keyword, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    if (null!=loading_view){
                        loading_view.setVisibility(View.GONE);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) {
                try{
                    if (null!=loading_view){
                        loading_view.setVisibility(View.GONE);
                    }
                    SearchNovelsBean bookListResponse= GsonUtil.getBean(response,SearchNovelsBean.class);
                    if (bookListResponse.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        dataBean=bookListResponse.getData();
                        if (pageNum==1){
                            booksBeanList.clear();
                        }
                        if (null!=dataBean.getList()){
                            booksBeanList.addAll(dataBean.getList());
                        }
                        booksSearchAdapter.search(keyword);
                        booksSearchAdapter.replaceData(booksBeanList);

                        //是否允许加载更多
                        if (null == dataBean.getList() || dataBean.getList().isEmpty()){//最后一页
                            booksSearchAdapter.loadMoreEnd();
                        }else{
                            booksSearchAdapter.loadMoreComplete();
                        }

                    }else{//加载失败
                        booksSearchAdapter.loadMoreFail();
                    }

                    if(booksBeanList.isEmpty()) {
                        booksSearchAdapter.setEmptyView(emptyView);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
