package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.IntegrateSearchBean;
import com.lianzai.reader.bean.IntegrateSearchResponse;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.activity.chat.PublicNumberDetailActivity;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetail;
import com.lianzai.reader.ui.adapter.BooksIntegrateSearchAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxAppTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 综合搜索
 */
public class SearchIntegrateFragment extends BaseFragment{

    @Bind(R.id.recyclerView)
    RecyclerView rv_integrate_search_list;

    ActivitySearch2 activitySearch2;

    @Bind(R.id.loading_view)
    View loading_view;

    BooksIntegrateSearchAdapter booksIntegrateSearchAdapter;//综合搜索

    List<IntegrateSearchBean> integrateSearchBeans=new ArrayList<>();

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
        initIntegrateAdapter();
    }


    //综合搜索数据适配器
    private void initIntegrateAdapter(){
        emptyView=getLayoutInflater().inflate(R.layout.view_search_book_list_record_empty,null);

        booksIntegrateSearchAdapter=new BooksIntegrateSearchAdapter(integrateSearchBeans,getContext());

        RxLinearLayoutManager rxLinearLayoutManager=new RxLinearLayoutManager(getContext());
        rv_integrate_search_list.setLayoutManager(rxLinearLayoutManager);
        rv_integrate_search_list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        booksIntegrateSearchAdapter.setIntegrateClickListener(new BooksIntegrateSearchAdapter.IntegrateClickListener() {
            @Override
            public void bookMoreClick() {
                activitySearch2.changeTab(1);
            }

            @Override
            public void bookItemClick(IntegrateSearchResponse.DataBean.PlatformBean bean) {
                //区分跳名片页还是圈子首页
                if(bean.getPlatformType() == 1 && RxAppTool.isCircle(bean.getPlatformId()) ){
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

            @Override
            public void bookListItemClick(String bookListId) {
                ActivityBookListDetail.startActivity(getActivity(),bookListId);
            }

            @Override
            public void bookListMoreClick() {
                activitySearch2.changeTab(2);
            }
        });

        rv_integrate_search_list.setAdapter(booksIntegrateSearchAdapter);


    }

    IntegrateSearchResponse integrateSearchResponse;

    //综合搜索
    public void searchIntegrateRequest(String keyword){
        if (null!=loading_view){
            loading_view.setVisibility(View.VISIBLE);
        }
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL+"/search/integrate/"+keyword, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                if (null!=loading_view){
                    loading_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onResponse(String response) {
                RxLogTool.e("searchIntegrateRequest response---:"+response);
                try{
                    if (null!=loading_view){
                        loading_view.setVisibility(View.GONE);
                    }
                    integrateSearchResponse= GsonUtil.getBean(response,IntegrateSearchResponse.class);
                    if (integrateSearchResponse.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE){

                        integrateSearchBeans.clear();

                        if (null!=integrateSearchResponse.getData().getPlatform()&&integrateSearchResponse.getData().getPlatform().size()>0){
                            IntegrateSearchBean integrateSearchBean=new IntegrateSearchBean(Constant.IIntegrateSearchType.BOOK_TITLE_TYPE,null,null);
                            integrateSearchBeans.add(integrateSearchBean);//书籍标题

                            for(int i=0;i<integrateSearchResponse.getData().getPlatform().size();i++){
                                IntegrateSearchBean bean=new IntegrateSearchBean(Constant.IIntegrateSearchType.BOOK_TYPE,null,integrateSearchResponse.getData().getPlatform().get(i));
                                integrateSearchBeans.add(bean);
                            }
                        }


                        if (null!=integrateSearchResponse.getData().getBooklist()&&integrateSearchResponse.getData().getBooklist().size()>0){

                            IntegrateSearchBean integrateSearchBeanLine=new IntegrateSearchBean(Constant.IIntegrateSearchType.LINE_TYPE,null,null);
                            integrateSearchBeans.add(integrateSearchBeanLine);//间距

                            IntegrateSearchBean integrateSearchBean1=new IntegrateSearchBean(Constant.IIntegrateSearchType.BOOK_LIST_TITLE_TYPE,null,null);
                            integrateSearchBeans.add(integrateSearchBean1);//书单标题

                            for(int i=0;i<integrateSearchResponse.getData().getBooklist().size();i++){
                                IntegrateSearchBean bean=new IntegrateSearchBean(Constant.IIntegrateSearchType.BOOK_LIST_TYPE,integrateSearchResponse.getData().getBooklist().get(i),null);
                                integrateSearchBeans.add(bean);
                            }
                        }


                        if (integrateSearchBeans.size()==0){
                            booksIntegrateSearchAdapter.setEmptyView(emptyView);
                        }

                        booksIntegrateSearchAdapter.replaceData(integrateSearchBeans);

                        booksIntegrateSearchAdapter.search(keyword);
                    }
                }catch (Exception e){
                    RxLogTool.e("searchIntegrateRequest:"+e.toString());

                }

            }
        });
    }



    @Override
    public void fetchData() {

//        if (!TextUtils.isEmpty(activitySearch2.ed_search_keyword.getText().toString().trim())){
//            searchIntegrateRequest(activitySearch2.ed_search_keyword.getText().toString());
//        }

    }

}
