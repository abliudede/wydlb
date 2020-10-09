package com.lianzai.reader.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.lianzai.reader.bean.BookShortAgeArtifact;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.ui.adapter.BookShortageAdapter;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 书荒神器
 */

public class ActivityBookShortageArtifact extends BaseActivity {

//implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener

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

    List<String> dataSource = new ArrayList<>();


    View headerView;
    TextView keywords_tv;//关键词
    TextView tv_msg;
    //头部视图
    SelectableRoundedImageView book_cover;

    private String keywords;
    private String img;
    private String msg;
    private BookShortAgeArtifact.DataBean bean;


    public static void startActivity(Context context, String json) {
        Bundle bundle = new Bundle();
        bundle.putString("json", json);
        RxActivityTool.skipActivity(context, ActivityBookShortageArtifact.class, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_shortage_artifact;
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


        initView();

        try{
            String json = getIntent().getExtras().getString("json");
            bean = GsonUtil.getBean(json, BookShortAgeArtifact.DataBean.class);
            keywords = bean.getBookName();
            msg = bean.getMsg();
            img = bean.getBookCover();

            keywords_tv.setText("关键词：" + keywords);
            tv_common_title.setText(keywords);
            if(!TextUtils.isEmpty(msg)){
                tv_msg.setVisibility(View.VISIBLE);
                tv_msg.setText(msg);
            }
            else {
                tv_msg.setVisibility(View.GONE);
            }
            RxImageTool.loadImage(ActivityBookShortageArtifact.this,img,book_cover);
        }catch(Exception e){

        }

        //添加数据
        if(null != bean && null != bean.getRecommendBookName()) {
            dataSource.addAll(bean.getRecommendBookName());
        }
        bookShortageAdapter.notifyDataSetChanged();
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


        manager = new RxLinearLayoutManager(ActivityBookShortageArtifact.this);
        recyclerView.setLayoutManager(manager);
        bookShortageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳搜索页面
                ActivitySearchFirst.skiptoSearch(dataSource.get(position),ActivityBookShortageArtifact.this);
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
                    SystemBarUtils.transparentStatusBar(ActivityBookShortageArtifact.this);
                    tv_common_title.setTextColor(Color.TRANSPARENT);

                    img_back.setImageResource(R.mipmap.icon_back_white);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookShortageArtifact.this);
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
                        SystemBarUtils.setStatusBarColor(ActivityBookShortageArtifact.this, Color.argb(progress, 72, 92, 248));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookShortageArtifact.this);
                            tintManager.setStatusBarTintEnabled(true);
                            tintManager.setNavigationBarTintEnabled(true);
                            // 自定义颜色
                            tintManager.setTintColor(Color.argb(progress, 72, 92, 248));
                        }
                    } else {
                        img_back.setImageResource(R.mipmap.icon_back_white);
                        rl_actionbar.setBackgroundColor(getResources().getColor(R.color.v2_blue_color));
                        SystemBarUtils.setStatusBarColor(ActivityBookShortageArtifact.this, getResources().getColor(R.color.v2_blue_color));
                        tv_common_title.setTextColor(getResources().getColor(R.color.white));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            SystemBarTintManager tintManager = new SystemBarTintManager(ActivityBookShortageArtifact.this);
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


//    private void bookListDetailRequest() {
//        Map<String, String> map = new HashMap<>();
//        map.put("pageNum", String.valueOf(pageNum));
//        map.put("pageSize", String.valueOf(pageSize));
//        map.put("booklistId", bookListId);
//        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/booklist/detail/list", map, new CallBackUtil.CallBackString() {
//            @Override
//            public void onFailure(Call call, Exception e) {
//                showSwipeRefresh(false);
//            }
//
//            @Override
//            public void onResponse(String response) {
//                try {
//                    showSwipeRefresh(false);
//                    BookListDetailResponse bookListDetailResponse = GsonUtil.getBean(response, BookListDetailResponse.class);
//                    if (bookListDetailResponse.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
//
//                    } else {//加载失败
//                        RxToast.custom(bookListDetailResponse.getMsg(), Constant.ToastType.TOAST_ERROR).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

}
