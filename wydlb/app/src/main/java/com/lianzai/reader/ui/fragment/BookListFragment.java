package com.lianzai.reader.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookListResponse;
import com.lianzai.reader.bean.BookShortAgeArtifact;
import com.lianzai.reader.bean.HomePageSettingBean;
import com.lianzai.reader.bean.HotRankingListBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.model.bean.BannerBean;
import com.lianzai.reader.ui.activity.ActivityBookList;
import com.lianzai.reader.ui.activity.ActivityBookListForRank;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.book.ActivityBookListDetail;
import com.lianzai.reader.ui.adapter.BookListGridAdapter;
import com.lianzai.reader.ui.adapter.HorizontalPagerAdapter;
import com.lianzai.reader.ui.adapter.HotRankGridAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.view.ItemHomePage;
import com.lianzai.reader.view.ItemRecommend;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogGoLogin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 首页-书单
 */
public class BookListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    //首页banner图
    View bannerView;
    ViewPager viewPager;
    HorizontalPagerAdapter horizontalPagerAdapter;
    List<BannerBean.DataBean> bannerBeanList = new ArrayList<>();
    LinearLayout ll_dots;//底部光标索引


    //排行榜部分
    View recommendView3;
    TextView tit_tv3;
    TextView tv_more_book_grid3;
    RecyclerView rv_grid_recommend3;
    HotRankGridAdapter bookListGridAdapter3;
    List<HotRankingListBean.DataBean> recommendList3 = new ArrayList<>();

    //精品书单部分
    View recommendView2;
    TextView tit_tv2;
    TextView tv_more_book_grid2;
    RecyclerView rv_grid_recommend2;
    BookListGridAdapter bookListGridAdapter2;
    List<BookListResponse.DataBean.ListBean> recommendList2 = new ArrayList<>();
    BookListResponse.DataBean recommendBean2;

    //为你推荐部分
    View recommendView1;
    TextView tit_tv;
    ImageView iv_exchange;
    LinearLayout recommend_ly;
    List<BookShortAgeArtifact.DataBean> recommendList1 = new ArrayList<>();

    //最新书单列表
    View updateTitleView;
    TextView tit_tv4;
    RecyclerView rv_grid_newest;
    TextView tv_more_book_list;
    BookListGridAdapter bookListListAdapter;
    List<BookListResponse.DataBean.ListBean> dataSource = new ArrayList<>();
    BookListResponse.DataBean updateLatestBean;


    //整体LinearLayout
    @Bind(R.id.ly_content)
    LinearLayout ly_content;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
//    CustomLoadMoreView customLoadMoreView;
    int pageNum = 1;
    int pageSize = 6;


    RxDialogGoLogin rxDialogGoLogin;//去登录弹窗

    private Handler mHandler = new Handler();

    @Override
    public int getLayoutResId() {
        return R.layout.book_list_fragment;
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


        rxDialogGoLogin = new RxDialogGoLogin(getActivity(), R.style.OptionDialogStyle);

        //banner部分
        bannerView = getLayoutInflater().inflate(R.layout.layout_banner_viewpager, null);
        viewPager = (ViewPager) bannerView.findViewById(R.id.viewPager);
        //动态计算高度
        ViewGroup.LayoutParams prama = viewPager.getLayoutParams();
        int height = (int) ((RxDeviceTool.getScreenWidth(getActivity()) - RxImageTool.dp2px(32)) / 2.35);
        prama.height = height;
        viewPager.setLayoutParams(prama);

        ll_dots = (LinearLayout) bannerView.findViewById(R.id.ll_dots);
        horizontalPagerAdapter = new HorizontalPagerAdapter(getContext());
        horizontalPagerAdapter.setBanner(bannerBeanList);
        viewPager.setAdapter(horizontalPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initDots((position + bannerBeanList.size() - 3) % (bannerBeanList.size() - 2), bannerBeanList.size() - 2);
                if (position == 0) {
                    //滑到列表第一位时,直接跳到数据末位
                    viewPager.setCurrentItem(bannerBeanList.size() - 2, false);
                } else if (position == bannerBeanList.size() - 1) {
                    //滑到列表末位时,直接跳到数据首位
                    viewPager.setCurrentItem(1, false);
                }

                //移除回调,并重新发起消息
                try {
                    mHandler.removeCallbacks(runnableForViewPager);
                    mHandler.postDelayed(runnableForViewPager, 2000);
                } catch (Exception ee) {
                    RxLogTool.e("removeCallbacks");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //排行榜部分
        recommendView3 = getLayoutInflater().inflate(R.layout.header_book_list_recommend, null);
        tit_tv3 = recommendView3.findViewById(R.id.tit_tv);
        tit_tv3.setText("排行榜");
        rv_grid_recommend3 = recommendView3.findViewById(R.id.rv_grid_recommend);
        tv_more_book_grid3 = recommendView3.findViewById(R.id.tv_more_book_grid);
        bookListGridAdapter3 = new HotRankGridAdapter(recommendList3, getContext());
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getContext(), 3);
        rv_grid_recommend3.addItemDecoration(new RxRecyclerViewDividerTool(0, RxImageTool.dip2px(8), RxImageTool.dip2px(10), 0));
        rv_grid_recommend3.setLayoutManager(gridLayoutManager3);
        rv_grid_recommend3.setAdapter(bookListGridAdapter3);
        bookListGridAdapter3.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityBookListDetail.startActivity(getContext(), String.valueOf(bookListGridAdapter3.getData().get(position).getBooklistId()));
            }
        });
        tv_more_book_grid3.setOnClickListener(
                v -> {
                    //切换到排行榜列表TODO
                    ActivityBookListForRank.startActivity(getActivity());
                }
        );


        //为你推荐部分
        recommendView1 = getLayoutInflater().inflate(R.layout.header_book_list_horizontalscrollview, null);
        tit_tv = recommendView1.findViewById(R.id.tit_tv);
        iv_exchange = recommendView1.findViewById(R.id.iv_exchange);
        recommend_ly = recommendView1.findViewById(R.id.recommend_ly);
        iv_exchange.setOnClickListener(
                v -> {
                    bookListRecommendRequest1();
                }
        );


        //精品书单部分
        recommendView2 = getLayoutInflater().inflate(R.layout.header_book_list_recommend, null);
        tit_tv2 = recommendView2.findViewById(R.id.tit_tv);
        tit_tv2.setText("精品书单");
        rv_grid_recommend2 = recommendView2.findViewById(R.id.rv_grid_recommend);
        tv_more_book_grid2 = recommendView2.findViewById(R.id.tv_more_book_grid);
        bookListGridAdapter2 = new BookListGridAdapter(recommendList2, getContext());
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 3);
        rv_grid_recommend2.addItemDecoration(new RxRecyclerViewDividerTool(0, RxImageTool.dip2px(8), RxImageTool.dip2px(10), 0));
        rv_grid_recommend2.setLayoutManager(gridLayoutManager2);
        rv_grid_recommend2.setAdapter(bookListGridAdapter2);
        bookListGridAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

//                RxActivityTool.skipActivity(getContext(), BookRecommendSettingActivity.class);
                ActivityBookListDetail.startActivity(getContext(), String.valueOf(bookListGridAdapter2.getData().get(position).getId()));
            }
        });
        tv_more_book_grid2.setOnClickListener(
                v -> ActivityBookList.startActivity(getActivity(), Constant.BookListType.RECOMMEND_TYPE)
        );


        //最新书单头部
        updateTitleView = getLayoutInflater().inflate(R.layout.header_book_list_recommend, null);
        tit_tv4 = updateTitleView.findViewById(R.id.tit_tv);
        tit_tv4.setText("最新书单");
        rv_grid_newest = updateTitleView.findViewById(R.id.rv_grid_recommend);
        tv_more_book_list = updateTitleView.findViewById(R.id.tv_more_book_grid);
        tv_more_book_list.setOnClickListener(
                v -> ActivityBookList.startActivity(getActivity(), Constant.BookListType.UPDATE_TYPE)
        );
        //最新书单列表部分
        bookListListAdapter = new BookListGridAdapter(dataSource, getContext());
        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(getContext(), 3);
        rv_grid_newest.addItemDecoration(new RxRecyclerViewDividerTool(0, RxImageTool.dip2px(8), RxImageTool.dip2px(10), 0));
        rv_grid_newest.setLayoutManager(gridLayoutManager4);
        rv_grid_newest.setAdapter(bookListListAdapter);
        bookListListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityBookListDetail.startActivity(getContext(), String.valueOf(bookListListAdapter.getData().get(position).getId()));
            }
        });

        //整体刷新控件
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));


        //请求数据
        getData();
    }

    private void initDots(int currentPosition, int size) {
        if (size <= 1) {
            return;
        }
        ll_dots.removeAllViews();
        int dotSize = RxImageTool.dip2px(5);
        int marginLeft = RxImageTool.dip2px(5);
        for (int i = 0; i < size; i++) {
            if (i == currentPosition) {
                SelectableRoundedImageView dotImgView = new SelectableRoundedImageView(getActivity());
//                dotImgView.setBackgroundColor(getResources().getColor(R.color.white));
                dotImgView.setOval(true);
                dotImgView.setBackgroundResource(R.drawable.circle_dot_focus);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize * 3, dotSize);
                layoutParams.setMargins(marginLeft, 0, 0, 0);
                dotImgView.setLayoutParams(layoutParams);
                ll_dots.addView(dotImgView);
            } else {
                SelectableRoundedImageView dotImgView = new SelectableRoundedImageView(getActivity());
//                dotImgView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                dotImgView.setOval(true);
                dotImgView.setBackgroundResource(R.drawable.circle_dot_normal);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dotSize, dotSize);
                layoutParams.setMargins(marginLeft, 0, 0, 0);
                dotImgView.setLayoutParams(layoutParams);
                ll_dots.addView(dotImgView);
            }
        }
        ll_dots.postInvalidate();
    }


    @Override
    public void onRefresh() {
        try{
            if(getActivity() instanceof MainActivity){
                ((MainActivity) getActivity()).getHomePageSwitchFragment().findIntervalRewardRequest();
            }
        }catch (Exception e){

        }
        getData();
    }

    public void getData(){
        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != listNeedRmove && !listNeedRmove.isEmpty()) {
                    for (ItemHomePage item : listNeedRmove) {
                        ly_content.removeView(item);
                    }
                    listNeedRmove.clear();
                }
                homePageSettingBookList();
            }
        }, getResources().getInteger(R.integer.refresh_delay));
    }

    @Override
    public void fetchData() {
    }

//    @Override
//    public void onLoadMoreRequested() {
//        pageNum++;
//        bookListRequest();
//    }

    @OnClick(R.id.search_tv)
    void searchClick() {
        ActivitySearchFirst.startActivity(getActivity());
    }

    private void showSwipeRefresh(boolean isShow) {
        try {
            if (null != mSwipeRefreshLayout) {
                mSwipeRefreshLayout.setRefreshing(isShow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*banner图请求*/
    private void bannerRequest() {
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/banner/getBanner", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                //移除回调
                try {
                    ll_dots.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    mHandler.removeCallbacks(runnableForViewPager);
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                    RxLogTool.e("removeCallbacks");
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BannerBean bannerBean = GsonUtil.getBean(response, BannerBean.class);
                    if (bannerBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //banner刷新逻辑

                        bannerBeanList.clear();
                        bannerBeanList.addAll(bannerBean.getData());

                        if (null == bannerBeanList || bannerBeanList.isEmpty()) {
                            //列表为空的时候直接放进去
                            horizontalPagerAdapter.setBanner(bannerBeanList);
                            horizontalPagerAdapter.notifyDataSetChanged();
                            viewPager.setVisibility(View.GONE);
                            ll_dots.setVisibility(View.GONE);
                            //移除回调
                            try {
                                mHandler.removeCallbacks(runnableForViewPager);
                            } catch (Exception e) {
                                RxLogTool.e("removeCallbacks");
                            }
                        } else {
                            //列表不为空的时候，首尾各加一个,用来做无限循环
                            BannerBean.DataBean tempStart = bannerBeanList.get(bannerBeanList.size() - 1);
                            BannerBean.DataBean tempEnd = bannerBeanList.get(0);
                            bannerBeanList.add(0, tempStart);
                            bannerBeanList.add(tempEnd);

                            horizontalPagerAdapter.setBanner(bannerBeanList);
                            horizontalPagerAdapter.notifyDataSetChanged();

                            viewPager.setVisibility(View.VISIBLE);
                            ll_dots.setVisibility(View.VISIBLE);
                            //初次获得数据
                            viewPager.setCurrentItem(1, false);
                            initDots((1 + bannerBeanList.size() - 3) % (bannerBeanList.size() - 2), bannerBeanList.size() - 2);
                            //移除回调,并重新发起消息
                            try {
                                mHandler.removeCallbacks(runnableForViewPager);
                                mHandler.postDelayed(runnableForViewPager, 3000);
                            } catch (Exception e) {
                                RxLogTool.e("removeCallbacks");
                            }
                        }
                    } else {//加载失败
                        viewPager.setVisibility(View.GONE);
                        ll_dots.setVisibility(View.GONE);
                        RxToast.custom(bannerBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                        //移除回调
                        try {
                            mHandler.removeCallbacks(runnableForViewPager);
                        } catch (Exception e) {
                            RxLogTool.e("removeCallbacks");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * ViewPager的定时器
     */
    Runnable runnableForViewPager = new Runnable() {
        @Override
        public void run() {
            try {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /*最新书单的列表*/
    private void bookListRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/list", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败").show();
                    showSwipeRefresh(false);
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BookListResponse bookListResponse = GsonUtil.getBean(response, BookListResponse.class);
                    if (bookListResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        updateLatestBean = bookListResponse.getData();
                        //填充数据
                        if (pageNum > 1) {
                            bookListListAdapter.addData(updateLatestBean.getList());
                        } else {
                            dataSource.clear();
                            dataSource.addAll(updateLatestBean.getList());
                            bookListListAdapter.replaceData(dataSource);
                        }
                    } else {//加载失败
                        RxToast.custom(bookListResponse.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 为你推荐
     */
    private void bookListRecommendRequest1() {
//        tit_tv.setVisibility(View.GONE);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/bookShortageArtifact", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    tit_tv.setVisibility(View.GONE);
                    iv_exchange.setVisibility(View.GONE);
                    recommend_ly.setVisibility(View.GONE);
//                    RxToast.custom("网络错误").show();
                    RxLogTool.e(e.toString());
                } catch (Exception ee) {

                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BookShortAgeArtifact bookListResponse = GsonUtil.getBean(response, BookShortAgeArtifact.class);
                    if (bookListResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        recommendList1.clear();
                        recommendList1.addAll(bookListResponse.getData());
                        recommend_ly.removeAllViews();
                        //填充数据
                        for (BookShortAgeArtifact.DataBean item : recommendList1) {
                            ItemRecommend temp = new ItemRecommend(getActivity());
                            temp.bindData(item);
                            recommend_ly.addView(temp);
                        }
                        if (!recommendList1.isEmpty()) {
                            tit_tv.setVisibility(View.VISIBLE);
                            iv_exchange.setVisibility(View.VISIBLE);
                            recommend_ly.setVisibility(View.VISIBLE);
                        } else {
                            tit_tv.setVisibility(View.GONE);
                            iv_exchange.setVisibility(View.GONE);
                            recommend_ly.setVisibility(View.GONE);
                        }
                    } else {//加载失败
                        tit_tv.setVisibility(View.GONE);
                        iv_exchange.setVisibility(View.GONE);
                        recommend_ly.setVisibility(View.GONE);
                        RxToast.custom(bookListResponse.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 排行榜
     */
    private void bookListRecommendRequest3() {
        Map<String, String> map = new HashMap<>();
        map.put("topN", "3");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/hotRankingList", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    HotRankingListBean hotRankingListBean = GsonUtil.getBean(response, HotRankingListBean.class);
                    if (hotRankingListBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        recommendList3.clear();
                        recommendList3.addAll(hotRankingListBean.getData());
                        //填充数据
                        bookListGridAdapter3.replaceData(recommendList3);

                    } else {//加载失败
                        bookListGridAdapter3.loadMoreFail();
                        RxToast.custom(hotRankingListBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 精品书单
     */
    private void bookListRecommendRequest2() {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", "1");
        map.put("pageSize", "6");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/list/recommend", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    BookListResponse bookListResponse = GsonUtil.getBean(response, BookListResponse.class);
                    if (bookListResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        recommendBean2 = bookListResponse.getData();
                        //填充数据
                        bookListGridAdapter2.replaceData(recommendBean2.getList());
                    } else {//加载失败
                        bookListGridAdapter2.loadMoreFail();
                        RxToast.custom(bookListResponse.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initHeadView() {
        //添加所有视图
        ly_content.addView(bannerView, 0);
        ly_content.addView(recommendView3, 1);
        ly_content.addView(recommendView2, 2);
        ly_content.addView(recommendView1, 3);
        ly_content.addView(updateTitleView, 4);


        bannerRequest();
        bookListRecommendRequest3();
        bookListRecommendRequest2();
        bookListRecommendRequest1();
        bookListRequest();
    }

    List<ItemHomePage> listNeedRmove = new ArrayList<ItemHomePage>();

    private void homePageSettingBookList() {
//        Map<String,String>map=new HashMap<>();
//        map.put("pageNum",String.valueOf(pageNum));
//        map.put("pageSize",String.valueOf(pageSize));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/homePageSetting/homePageSettingBookList", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("网络错误").show();
                    showSwipeRefresh(false);
                    initHeadView();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    showSwipeRefresh(false);
                    HomePageSettingBean homePageSettingBean = GsonUtil.getBean(response, HomePageSettingBean.class);
                    if (homePageSettingBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<HomePageSettingBean.DataBean.HeadBookListBean> headData = homePageSettingBean.getData().getHeadBookList();
                        List<HomePageSettingBean.DataBean.HeadBookListBean> selfBuildData = homePageSettingBean.getData().getSelfBuiltBookList();

                        int index = 0;
                        if (null == bannerView.getParent()) {
                            //添加所有视图
                            ly_content.addView(bannerView, index);
                        }


                        if (null != headData && !headData.isEmpty()) {
                            for (HomePageSettingBean.DataBean.HeadBookListBean item : headData) {
                                ItemHomePage itemHomePage = new ItemHomePage(getActivity());
                                itemHomePage.bindData(item);
                                listNeedRmove.add(itemHomePage);
                                index++;
                                ly_content.addView(itemHomePage,index);
                            }
                        }
                        index++;
                        if (null == recommendView3.getParent())
                            ly_content.addView(recommendView3,index);
                        index++;
                        if (null == recommendView2.getParent())
                            ly_content.addView(recommendView2,index);

                        if (null != selfBuildData && !selfBuildData.isEmpty()) {
                            for (HomePageSettingBean.DataBean.HeadBookListBean item : selfBuildData) {
                                ItemHomePage itemHomePage = new ItemHomePage(getActivity());
                                itemHomePage.bindData(item);
                                listNeedRmove.add(itemHomePage);
                                index++;
                                ly_content.addView(itemHomePage,index);
                            }
                        }
                        index++;
                        if (null == recommendView1.getParent())
                            ly_content.addView(recommendView1,index);
                        index++;
                        if (null == updateTitleView.getParent())
                            ly_content.addView(updateTitleView,index);

                        bannerRequest();
                        bookListRecommendRequest3();
                        bookListRecommendRequest2();
                        bookListRecommendRequest1();
                        bookListRequest();

                    } else {
                        RxToast.custom(homePageSettingBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                        try {
                            initHeadView();
                        } catch (Exception ee) {
                        }
                    }
                } catch (Exception e) {
                    try {
                        initHeadView();
                    } catch (Exception ee) {
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
