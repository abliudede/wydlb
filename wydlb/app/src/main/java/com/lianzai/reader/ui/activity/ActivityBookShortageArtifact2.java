package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.BookShortageArtifactBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.BookShortageAdapter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 书荒神器,可刷新的页面
 */

public class ActivityBookShortageArtifact2 extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

//implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.rl_actionbar)
    RelativeLayout rl_actionbar;

    @Bind(R.id.tv_common_title)
    TextView tv_common_title;

    @Bind(R.id.img_back)
    ImageView img_back;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    RxLinearLayoutManager manager;
    BookShortageAdapter bookShortageAdapter;

    View headerView;
    TextView keywords_tv;//关键词
    TextView tv_msg;
    //头部视图
    SelectableRoundedImageView book_cover;

    private String keywords;
    private String img;
    private List<String> dataSource = new ArrayList<String>();


    public static void startActivity(Context context, String keywords,String img) {
        Bundle bundle = new Bundle();
        bundle.putString("keywords", keywords);
        bundle.putString("img", img);
        RxActivityTool.skipActivity(context, ActivityBookShortageArtifact2.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_shortage_artifact2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);

        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }

        //整体刷新控件
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        initView();

        try{
            keywords = getIntent().getExtras().getString("keywords");
            img = getIntent().getExtras().getString("img");
            keywords_tv.setText("关键词：" + keywords);
            tv_common_title.setText(keywords);
            RxImageTool.loadImage(ActivityBookShortageArtifact2.this,img,book_cover);
        }catch(Exception e){

        }

        onRefresh();
    }

    private void initView() {
        int statusBarHeight=ScreenUtil.getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)getResources().getDimension(R.dimen.action_bar_height));
        layoutParams.setMargins(0,statusBarHeight,0,0);
        rl_actionbar.setLayoutParams(layoutParams);

        headerView = getLayoutInflater().inflate(R.layout.header_book_shortage, null);
        book_cover = headerView.findViewById(R.id.book_cover);
        keywords_tv = headerView.findViewById(R.id.keywords_tv);
        tv_msg = headerView.findViewById(R.id.tv_msg);



        bookShortageAdapter = new BookShortageAdapter(dataSource, this);
        bookShortageAdapter.addHeaderView(headerView, 0);


        manager = new RxLinearLayoutManager(ActivityBookShortageArtifact2.this);
        recyclerView.setLayoutManager(manager);
        bookShortageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳搜索页面
                ActivitySearchFirst.skiptoSearch(dataSource.get(position),ActivityBookShortageArtifact2.this);
            }
        });

        recyclerView.setAdapter(bookShortageAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int distance = getScrolledDistance();
                if (distance <= 0) {
                    rl_actionbar.setBackgroundColor(Color.TRANSPARENT);
                    SystemBarUtils.transparentStatusBar(ActivityBookShortageArtifact2.this);
                    tv_common_title.setTextColor(Color.TRANSPARENT);

                    img_back.setImageResource(R.mipmap.icon_back_white);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookShortageArtifact2.this);
                        tintManager.setStatusBarTintEnabled(true);
                        tintManager.setNavigationBarTintEnabled(true);
                        // 自定义颜色
                        tintManager.setTintColor(getResources().getColor(R.color.black));
                    }

                } else {
                    if (distance < RxImageTool.dip2px(45)) {
                        int progress = (int) (new Float(distance) / new Float(RxImageTool.dip2px(45)) * 255);//255
                        rl_actionbar.setBackgroundColor(Color.argb(progress, 72, 92, 248));
                        tv_common_title.setTextColor(Color.argb(progress, 255, 255, 255));
                        SystemBarUtils.setStatusBarColor(ActivityBookShortageArtifact2.this, Color.argb(progress, 72, 92, 248));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookShortageArtifact2.this);
                            tintManager.setStatusBarTintEnabled(true);
                            tintManager.setNavigationBarTintEnabled(true);
                            // 自定义颜色
                            tintManager.setTintColor(Color.argb(progress, 72, 92, 248));
                        }
                    } else {
                        img_back.setImageResource(R.mipmap.icon_back_white);
                        rl_actionbar.setBackgroundColor(getResources().getColor(R.color.v2_blue_color));
                        SystemBarUtils.setStatusBarColor(ActivityBookShortageArtifact2.this, getResources().getColor(R.color.v2_blue_color));
                        tv_common_title.setTextColor(getResources().getColor(R.color.white));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookShortageArtifact2.this);
                            tintManager.setStatusBarTintEnabled(true);
                            tintManager.setNavigationBarTintEnabled(true);
                            // 自定义颜色
                            tintManager.setTintColor(getResources().getColor(R.color.v2_blue_color));
                        }
                    }
                }
            }
        });
    }

    /**
     * @description 获取垂直方向滑动的距离
     * @author qicheng.qing
     * @create 17/3/3 14:22
     */
    private int getScrolledDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View firstVisibleItem = recyclerView.getChildAt(0);
        int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
        int itemHeight = firstVisibleItem.getHeight();
        int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
        return (firstItemPosition + 1) * itemHeight - firstItemBottom;
    }


    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }




    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    private void bookShortageArtifactRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("bookName", keywords);
        map.put("num", "30");
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/bookShortageByBookNameNew", map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                showSwipeRefresh(false);
            }

            @Override
            public void onResponse(String response) {
                try {
                    showSwipeRefresh(false);
                    BookShortageArtifactBean bookShortageArtifactBean = GsonUtil.getBean(response, BookShortageArtifactBean.class);
                    if (bookShortageArtifactBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        dataSource.clear();

                        dataSource.addAll(bookShortageArtifactBean.getData().getBooknames());
                        bookShortageAdapter.notifyDataSetChanged();

                        if(!TextUtils.isEmpty(bookShortageArtifactBean.getData().getMsg())){
                            tv_msg.setVisibility(View.VISIBLE);
                            tv_msg.setText(bookShortageArtifactBean.getData().getMsg());
                        } else {
                            tv_msg.setVisibility(View.GONE);
                        }

                    } else {//加载失败
                        RxToast.custom(bookShortageArtifactBean.getMsg(), Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showSwipeRefresh(boolean isShow){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    if (null!=mSwipeRefreshLayout){
                        mSwipeRefreshLayout.setRefreshing(isShow);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, 50);
    }

    @Override
    public void onRefresh() {
        bookShortageArtifactRequest();
    }
}
