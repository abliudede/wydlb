package com.lianzai.reader.ui.fragment;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.collection.ArrayMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseFragment;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.AccountTokenBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BookStoreResponse;
import com.lianzai.reader.bean.BookStoreResponseForVisitor;
import com.lianzai.reader.bean.CopyrightsBatchInfoBean;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.bean.GetCommonShareUrlBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRecyclerItemClickListener;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.bean.BookStoreBeanN;
import com.lianzai.reader.model.local.BookStoreRepository;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.ActivityTeamChoose;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBook;
import com.lianzai.reader.ui.activity.read.ActivityTeamChooseForLianzaihao;
import com.lianzai.reader.ui.adapter.BookStoreGridAdapter;
import com.lianzai.reader.ui.contract.MyBookStoreContract;
import com.lianzai.reader.ui.presenter.MyBookStorePresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxRecyclerViewDividerTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxVibrateTool;
import com.lianzai.reader.utils.SkipReadUtil;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogBookStoreOptions;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;
import com.lianzai.reader.view.dialog.RxDialogWebShare;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import okhttp3.Call;


/**
 * 新版书架
 */
public class MyBookFragment extends BaseFragment implements MyBookStoreContract.View {

    ItemTouchHelper mItemDragTouchHelper;

    ItemDragAndSwipeCallback itemSwipeCallback;


    @Bind(R.id.swipe_grid_refresh_layout) //九宫格容器
            SwipeRefreshLayout swipe_grid_refresh_layout;

    @Bind(R.id.rv_grid_book_store)//九宫格列表
            RecyclerView rv_grid_book_store;


    BookStoreGridAdapter bookStoreGridAdapter;

    List<BookStoreBeanN> bookStoreBeanList = new ArrayList<>();

    List<BookStoreBeanN> showBookStoreBeanList = new ArrayList<>();

    BookStoreResponse bookStoreResponse;
    private BookStoreResponseForVisitor bookStoreResponseforvisitor;

    int totalReadTime = 0;//阅读时长

    DividerItemDecoration dividerItemDecoration;

    AccountTokenBean accountTokenBean;

    RxDialogBookStoreOptions rxDialogBookStoreOptions;

    int uid;
    String bqtKey;

    boolean isFirstLoaded = true;

    int[] dragDeleteRect = new int[2];
    int[] leftBottomLocation = new int[2];
    int[] rightBottomLocation = new int[2];

    @Bind(R.id.rl_delete)
    RelativeLayout rl_delete;

    @Bind(R.id.rl_share)
    RelativeLayout rl_share;

//    @Bind(R.id.tv_delete_status)
//    TextView tv_delete_status;

//    @Bind(R.id.view_empty)
//    View view_empty;

    @Bind(R.id.view_loading)
    View view_loading;

    @Bind(R.id.view_not_login)
    View view_not_login;

    @Inject
    MyBookStorePresenter myBookStorePresenter;

    LinearLayout ll_go_to_read;

    MainActivity mainActivity;
    private int choose = 1;
    private RxDialogWebShare rxDialogWebShare;


    private WbShareHandler shareHandler;

    RxDialogSureCancelNew rxDialogSureCancelTip;//提示

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity=(MainActivity)activity;
    }

    @Override
    public int getLayoutResId() {
        RxEventBusTool.registerEventBus(this);
        return R.layout.fragment_book_store;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxEventBusTool.unRegisterEventBus(this);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        myBookStorePresenter.attachView(this);

        //微博初始化
        shareHandler = new WbShareHandler(getActivity());
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        ll_go_to_read=view_not_login.findViewById(R.id.ll_go_to_read);

        rxDialogBookStoreOptions = new RxDialogBookStoreOptions(getActivity(), R.style.BottomDialogStyle);

        totalReadTime = RxSharedPreferencesUtil.getInstance().getInt("readTotalTime", 0);

        //公告部分
        dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);

        initGridAdapter();

        refreshLoginView();//刷新

        swipe_grid_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshRequest(true);
            }
        });
        swipe_grid_refresh_layout.setColorSchemeColors(Color.parseColor("#333333"));

        rl_delete.post(new Runnable() {
            @Override
            public void run() {
                if(null != rl_delete){
                    rl_delete.getLocationOnScreen(dragDeleteRect);
                    leftBottomLocation[0] = 0;
                    leftBottomLocation[1] = dragDeleteRect[1] + RxImageTool.dip2px(115);

                    rightBottomLocation[0] = dragDeleteRect[0] + RxImageTool.dip2px(115);
                    rightBottomLocation[1] = dragDeleteRect[1] + RxImageTool.dip2px(115);
                }
            }
        });


    }

    private void refreshLoginView(){
        accountTokenBean = RxLoginTool.getLoginAccountToken();
        if (null != accountTokenBean) {
            swipe_grid_refresh_layout.setEnabled(true);
            //非游客状态
            bqtKey = accountTokenBean.getData().getUid() + Constant.BQT_KEY;
            uid = accountTokenBean.getData().getUid();
//          view_not_login.setVisibility(View.GONE);

        } else {
            swipe_grid_refresh_layout.setEnabled(false);
            //游客状态
            view_not_login.setVisibility(View.VISIBLE);
        }

        ll_go_to_read.setOnClickListener(
                v->mainActivity.changeTab(0)
        );

        dragInit();
    }


    boolean isDelete = false;
    boolean isShare = false;

    boolean canDeleteBook = false;//能否移除该书-全局
    boolean canShareBook = false;//能否移除该书-全局

    private void initGridAdapter() {
        bookStoreGridAdapter = new BookStoreGridAdapter(showBookStoreBeanList, getActivity());

        rv_grid_book_store.removeItemDecoration(dividerItemDecoration);
        rv_grid_book_store.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dip2px(17),0 ,0,0));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_grid_book_store.setLayoutManager(gridLayoutManager);
        rv_grid_book_store.setAdapter(bookStoreGridAdapter);

        bookStoreGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(position >= 0 && position < bookStoreGridAdapter.getItemCount()){
                    openBookClick(bookStoreGridAdapter.getItem(position));
                }
            }
        });

        dragInit();
    }


    private void dragInit(){
        if (RxLoginTool.isLogin()) {
            //九宫格拖曳删除,只有登录状态才有此功能
            if (null==mItemDragTouchHelper){
                mItemDragTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
                    //
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
                        RxLogTool.e("onMoved..... y:");
                    }

                    @Override
                    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        RxLogTool.e("onChildDrawOver..... :");
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                        RxLogTool.e("onChildDraw..... :");

                        int[] location = new int[2];
                        viewHolder.itemView.getLocationOnScreen(location);
                        int width = viewHolder.itemView.getWidth();
                        int height = viewHolder.itemView.getHeight();
                        int xCenter = location[0] + width/2;
                        int yCenter = location[1] + height/2;
                        int xdelete = Math.abs(xCenter- rightBottomLocation[0]);
                        int ydelete = Math.abs(yCenter - rightBottomLocation[1]);
                        int xshare = Math.abs(xCenter- leftBottomLocation[0]);
                        int yshare = Math.abs(yCenter - leftBottomLocation[1]);
                        int circleValue = RxImageTool.dip2px(115) * RxImageTool.dip2px(115);

                        if (xdelete*xdelete + ydelete*ydelete < circleValue) {
                            isDelete = true;
                            rl_delete.setScaleX(1.2f);
                            rl_delete.setScaleY(1.2f);
                        } else {
                            isDelete = false;
                            rl_delete.setScaleX(1.0f);
                            rl_delete.setScaleY(1.0f);
                        }

                        if (xshare*xshare + yshare*yshare < circleValue) {
                            isShare = true;
                            rl_share.setScaleX(1.2f);
                            rl_share.setScaleY(1.2f);
                        } else {
                            isShare = false;
                            rl_share.setScaleX(1.0f);
                            rl_share.setScaleY(1.0f);
                        }

                        RxLogTool.e("onChildDraw..... y:" + dY + "-dragDeleteRect-y:" + dragDeleteRect[1]);
                    }

                    @Override
                    public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                        RxLogTool.e("getAnimationDuration up.....");
                        canDeleteBook = isDelete;
                        canShareBook = isShare;
                        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        if (null == viewHolder) return;
                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                            //滑动到顶部
                            viewHolder.itemView.setScaleX(1.05f);
                            viewHolder.itemView.setScaleY(1.05f);
                            viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, true);
                        }
                        super.onSelectedChanged(viewHolder, actionState);
                    }

                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);
                        if (viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support) != null
                                && (Boolean) viewHolder.itemView.getTag(R.id.BaseQuickAdapter_dragging_support)) {
                            //防止回收崩溃
                            try{
                                viewHolder.itemView.setTag(R.id.BaseQuickAdapter_dragging_support, false);
                                viewHolder.itemView.setScaleX(1f);
                                viewHolder.itemView.setScaleY(1f);
                                rl_delete.setVisibility(View.INVISIBLE);
                                rl_share.setVisibility(View.INVISIBLE);
                            }catch (Exception e){

                            }

                            int pos = viewHolder.getAdapterPosition() - bookStoreGridAdapter.getHeaderLayoutCount();
                            if (canDeleteBook) {//删除
                                try {
                                    if (pos >= 0 && pos < bookStoreGridAdapter.getData().size()) {
                                        showdeleteBookDialog(pos);
                                    }
                                } catch (Exception e) {
//                                    e.printStackTrace();
                                }
                            }


                            if (canShareBook) {//分享
                                try {
                                    if (pos >= 0 && pos < bookStoreGridAdapter.getData().size()) {
                                        isShare = false;
                                            //旧书籍分享模式
                                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //分享
                                                    showShareBarDialog(bookStoreGridAdapter.getItem(pos));
                                                }
                                            }, 300);

                                    }
                                } catch (Exception e) {
//                                    e.printStackTrace();
                                }
                            }

                        }
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                    }
                });

                ////允许拖动
                bookStoreGridAdapter.enableDragItem(mItemDragTouchHelper);
                bookStoreGridAdapter.disableSwipeItem();
                mItemDragTouchHelper.attachToRecyclerView(rv_grid_book_store);
            }else{
                //允许拖动
                bookStoreGridAdapter.enableDragItem(mItemDragTouchHelper);
            }

            rv_grid_book_store.addOnItemTouchListener(new OnRecyclerItemClickListener(rv_grid_book_store, new OnRecyclerItemClickListener.RecyclerCustomClick() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder vh) {

                }

                @Override
                public void onItemLongClick(RecyclerView.ViewHolder vh) {
                    if (RxLoginTool.isLogin()){
                        RxVibrateTool.vibrateOnce(getApplicationContext(), 30);
                        rl_delete.setVisibility(View.VISIBLE);
                        rl_share.setVisibility(View.VISIBLE);
                    }
                }
            }));

        }else{//禁止拖曳
            bookStoreGridAdapter.disableDragItem();
        }
    }

    private void showdeleteBookDialog(int pos) {
        if (null == rxDialogSureCancelTip) {
            rxDialogSureCancelTip = new RxDialogSureCancelNew(getActivity());
            rxDialogSureCancelTip.setButtonText("确定", "取消");
            rxDialogSureCancelTip.getCancelView().setVisibility(View.VISIBLE);
            rxDialogSureCancelTip.getSureView().setBackgroundResource(R.drawable.shape_blue_bottomyuanjiao);
            rxDialogSureCancelTip.setTitle("删除");
            rxDialogSureCancelTip.setContent("是否确定删除本书？");
        }
        rxDialogSureCancelTip.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                deleteBook(pos);
                rxDialogSureCancelTip.dismiss();
            }
        });
        rxDialogSureCancelTip.show();
    }

    private void deleteBook(int pos) {
        isDelete = false;
        //删除成功
        RxVibrateTool.vibrateOnce(getApplicationContext(), 400);
        int bookId = Integer.parseInt(bookStoreGridAdapter.getItem(pos).getBookId());
        if(bookId <= Constant.bookIdLine){
            //旧书籍删除模式
            String platformId = bookStoreGridAdapter.getItem(pos).getPlatformId();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //请求接口移除
                    deleteBookByPlatformId(platformId);
                }
            }, 300);
        }else {
            //url书籍删除模式
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //请求接口移除
                    deleteBookByBookId(String.valueOf(bookId));
                }
            }, 300);
        }
        bookStoreGridAdapter.remove(pos);
    }


    //打开阅读
    private void openBookClick(BookStoreBeanN bookStoreBean) {
        SkipReadUtil.normalRead(getActivity(),bookStoreBean.getBookId(),"",false);
    }

    /**
     * 分享bar详情框
     */
    private void showShareBarDialog(BookStoreBeanN beanN) {
        if(null == rxDialogWebShare){
            rxDialogWebShare = new RxDialogWebShare(getActivity(), R.style.BottomDialogStyle);
            rxDialogWebShare.getTv_share_ftf().setText("连载群");
            rxDialogWebShare.getTv_share_ftf().setCompoundDrawables(null,RxImageTool.getDrawable(R.mipmap.icon_to_team),null,null);
            rxDialogWebShare.getTv_share_refresh().setText("连载口令");
            rxDialogWebShare.getTv_share_refresh().setCompoundDrawables(null,RxImageTool.getDrawable(R.mipmap.icon_lianzaikouling),null,null);
            rxDialogWebShare.getTv_share_copy().setText("转码声明");
            rxDialogWebShare.getTv_share_copy().setCompoundDrawables(null,RxImageTool.getDrawable(R.mipmap.share_transcoding),null,null);

        }
        int bookId = Integer.parseInt(beanN.getBookId());

        rxDialogWebShare.getTv_share_sms().setVisibility(View.GONE);
        rxDialogWebShare.getTv_share_browseopen().setVisibility(View.GONE);
        if(bookId <= Constant.bookIdLine){
            //隐藏多余按钮
            rxDialogWebShare.getTv_share_wx().setVisibility(View.VISIBLE);
            rxDialogWebShare.getTv_share_weibo().setVisibility(View.VISIBLE);
            rxDialogWebShare.getTv_share_qq().setVisibility(View.VISIBLE);
            rxDialogWebShare.getTv_share_timeline().setVisibility(View.VISIBLE);
        }else {
            //隐藏短信按钮
            rxDialogWebShare.getTv_share_wx().setVisibility(View.GONE);
            rxDialogWebShare.getTv_share_weibo().setVisibility(View.GONE);
            rxDialogWebShare.getTv_share_qq().setVisibility(View.GONE);
            rxDialogWebShare.getTv_share_timeline().setVisibility(View.GONE);
        }

        rxDialogWebShare.getTv_share_ftf().setOnClickListener(
                v -> {
                    //分享到连载群
                    if(bookId <= Constant.bookIdLine){
                        if(RxLoginTool.isLogin()) {
                            ActivityTeamChooseForLianzaihao.startActivity(getActivity(), beanN.getPlatformName(), beanN.getPlatformIntroduce(), beanN.getPlatformCover(), beanN.getBookId(), beanN.getPlatformId());
                        }
                    }else {
                        if(RxLoginTool.isLogin()) {
                            ActivityTeamChoose.startActivity(getActivity(), beanN.getBookName(), "", beanN.getBookCover(), bookId,"");
                        }
                    }
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.getTv_share_wx().setOnClickListener(
                v -> {
                    //微信分享
                    if(bookId <= Constant.bookIdLine){
                        getCommonShareUrl1(1,beanN.getPlatformId());
                    }else {

                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_qq().setOnClickListener(
                v -> {
                    //QQ分享
                    if(bookId <= Constant.bookIdLine){
                        getCommonShareUrl1(3,beanN.getPlatformId());
                    }else {

                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_timeline().setOnClickListener(
                v -> {
                    //朋友圈分享
                    if(bookId <= Constant.bookIdLine){
                        getCommonShareUrl1(2,beanN.getPlatformId());
                    }else {

                    }
                    rxDialogWebShare.dismiss();
                }
        );
        rxDialogWebShare.getTv_share_weibo().setOnClickListener(
                v -> {
                    //微博分享
                    if(bookId <= Constant.bookIdLine){
                        getCommonShareUrl1(5,beanN.getPlatformId());
                    }else {

                    }
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.getTv_share_refresh().setOnClickListener(
                v -> {
                    //连载口令
                    if(bookId <= Constant.bookIdLine){
                        String nowSource = RxSharedPreferencesUtil.getInstance().getString(String.valueOf(bookId) + "_source");
                        ActivityShareBitmapBook.startActivity(getActivity(),String.valueOf(bookId),nowSource);
                    }else {
                        //连载口令
                        ActivityShareBitmapBook.startActivity(getActivity(),String.valueOf(bookId),"");
                    }
                    rxDialogWebShare.dismiss();
                }
        );


        rxDialogWebShare.getTv_share_copy().setOnClickListener(
                v -> {
                    //转码声明
                    if(bookId <= Constant.bookIdLine){
                        ActivityWebView.startActivity(getActivity(),Constant.H5_HELP_BASE_URL + "/helper/show/27");
                    }else {
                        ActivityWebView.startActivity(getActivity(),Constant.H5_HELP_BASE_URL + "/helper/show/27");
                    }
                    rxDialogWebShare.dismiss();
                }
        );

        rxDialogWebShare.show();
    }


    /**
     * 获取分享配置URL
     */
    private void getCommonShareUrl1(int shareMode,String lianzaihaoid){

        Map<String,String> map=new HashMap<>();
        map.put("code", "1002");
        map.put("shareMode", String.valueOf(shareMode));
        map.put("objId", lianzaihaoid);

        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/common/share/getCommonShareUrl" ,map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try{
                    GetCommonShareUrlBean baseBean= GsonUtil.getBean(response,GetCommonShareUrlBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){

                        switch (shareMode){
                            case 1:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                }else {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //需要在子线程中处理的逻辑
                                            File file1 = new File(RxImageTool.getImgPathFromCache(baseBean.getData().getHeadVal(), getApplicationContext()));
                                            RxShareUtils.shareWebUrl(file1, baseBean.getData().getOneUrlVal(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), BuglyApplicationLike.getsInstance().api, true);
                                        }
                                    }.start();
                                }
                                break;
                            case 2:
                                if (!BuglyApplicationLike.api.isWXAppInstalled()) {
                                    RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                } else if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
                                    RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
                                    return;
                                }else {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //需要在子线程中处理的逻辑
                                            File file2 = new File(RxImageTool.getImgPathFromCache(baseBean.getData().getHeadVal(), getApplicationContext()));
                                            RxShareUtils.shareWebUrl(file2, baseBean.getData().getOneUrlVal(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), BuglyApplicationLike.getsInstance().api, false);
                                        }
                                    }.start();
                                }
                                break;
                            case 3:
                                RxShareUtils.QQShareUrl(getActivity(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;
                            case 4:
                                RxShareUtils.QQShareUrl(getActivity(), baseBean.getData().getTitleVal(), baseBean.getData().getContentVal(), baseBean.getData().getOneUrlVal(), baseBean.getData().getHeadVal(), qqShareListener);
                                break;

                            case 5:
                                //微博分享
                                RxShareUtils.WBShare(shareHandler, getActivity(), true, baseBean.getData().getContentVal(), baseBean.getData().getTitleVal(), baseBean.getData().getOneUrlVal() , true, null);
                                break;

                            default:
                                break;
                        }

                    }else{
                        RxToast.custom(baseBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }

                }catch (Exception e){
                    RxLogTool.e("requestUnConcernPlatform e:"+e.getMessage());
                }
            }
        });
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
//            RxToast.custom("分享取消").show();
        }

        @Override
        public void onComplete(Object response) {
//            RxToast.custom("分享成功").show();
        }

        @Override
        public void onError(UiError e) {
//            RxToast.custom("分享失败").show();
        }
    };

    @Override
    public void fetchData() {
        RxLogTool.e("requestBookStore fetchData......");
        if (null != accountTokenBean) {
            fetchLocalDataAndShow();
            if (RxNetTool.isAvailable()) {//网络可用才能用
                onRefreshRequest(false);
            }
        }
    }

    /**
     * 请求刷新
     */
    private void onRefreshRequest(boolean needSendEvent) {
        if(needSendEvent) {
            RxEventBusTool.sendEvents(Constant.EventTag.READTIME_REFRESH_TAG);//阅读时长刷新
        }
        if(RxLoginTool.isLogin()) {
            requestBookStore();
        }
    }


    long timestamp;

    private void requestBookStore() {
        ArrayMap map = new ArrayMap();
        bqtKey = accountTokenBean.getData().getUid() + Constant.BQT_KEY;
        timestamp = RxSharedPreferencesUtil.getInstance().getLong(bqtKey, 0);
        if (timestamp > 0) {//
            map.put("timestamp", String.valueOf(timestamp));
        }
        myBookStorePresenter.requestBookStore(map);
    }

    /**
     * 渲染数据-缓存数据等操作
     */
    private void fetchLocalDataAndShow() {
        isFirstLoaded = false;//加载过了
        if (null != view_loading) {
            view_loading.setVisibility(View.GONE);
        }
        List<BookStoreBeanN> list = BookStoreRepository.getInstance().getBookStoreBooksByUserId(uid);
        List<BookStoreBeanN> listTemp = new ArrayList<BookStoreBeanN>();
        if(null != list && !list.isEmpty()) {
            switch (choose) {
                case 1:
                    listTemp = list;
                    break;
                case 2:
                    for(BookStoreBeanN item : list){
                        if(Integer.parseInt(item.getBookId()) <= Constant.bookIdLine){
                            listTemp.add(item);
                        }
                    }
                    break;
                case 3:
                    for(BookStoreBeanN item : list){
                        if(Integer.parseInt(item.getBookId()) > Constant.bookIdLine){
                            listTemp.add(item);
                        }
                    }
                    break;
            }
        }

//        if (null!=list){
//            for(int i=0;i<list.size();i++){
//                RxLogTool.e("Index:"+i+"--fetchLocalDataAndShow bookStoreBean:"+list.get(i).getPlatformName()+"----"+list.get(i).isUnread()+"--date:"+list.get(i).getUpdateDate());
//            }
//        }

        if (listTemp.size() > 0) {
            //排序
            //填充数据
            bookStoreGridAdapter.replaceData(listTemp);
            view_not_login.setVisibility(View.GONE);
        } else {
            //填充数据
            bookStoreGridAdapter.replaceData(listTemp);
            view_not_login.setVisibility(View.VISIBLE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusNotification(DataSynEvent event) {
        if (event.getTag() == Constant.EventTag.REFRESH_BOOK_STORE_TAG) {
                if(RxLoginTool.isLogin()) {
                    //同步之后，取出数据重新显示
                    fetchLocalDataAndShow();//刷新数据
                }
        }else  if (event.getTag() == Constant.EventTag.REFRESH_BOOK_STORE_REQUEST) {
            if(RxLoginTool.isLogin()) {
                //防止清除缓存之后，页面没有数据，所以还是每次切换过来都请求网络。
                fetchData();//刷新数据
            }
        }else if(event.getTag()==Constant.EventTag.LOGIN_REFRESH_TAG){//登录刷新
            refreshLoginView();//登录状态刷新
            fetchData();//刷新数据
        }else if(event.getTag()==Constant.EventTag.LOGIN_OUT_REFRESH_TAG){//退出登录成功
            refreshLoginView();
        }
    }

    private void deleteBookByPlatformId(String platFormId) {
        try {
            if (!RxNetTool.isAvailable()) {
                RxToast.custom(getResources().getString(R.string.network_is_not_available)).show();
            } else {
                requestUnConcernPlatform(platFormId);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void deleteBookByBookId(String bookId) {
        try {
            if (!RxNetTool.isAvailable()) {
                RxToast.custom(getResources().getString(R.string.network_is_not_available)).show();
            } else {
                requestUnConcernBookId(bookId);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }



    /**
     * 取消关注
     */
    private void requestUnConcernPlatform(String objectId) {
        OKHttpUtil.okHttpPut(Constant.API_BASE_URL + "/platforms/" + objectId + "/UnConcern", new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestUnConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 取消关注
     */
    private void requestUnConcernBookId(String bookId) {
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/book/shelf/bookCancelAttention/" + bookId , new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
//                    RxToast.custom("网络错误").show();
                }catch (Exception ee){
                }
            }
            @Override
            public void onResponse(String response) {
                try {
                    BaseBean baseBean = GsonUtil.getBean(response, BaseBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //同步数据库
                        requestBookStore();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e("requestUnConcernPlatform e:" + e.getMessage());
                }
            }
        });
    }

    private void deleteBookOptions(String objectId) {
        //移除
        BookStoreRepository.getInstance().deleteAllByPlatformIdAndUid(uid, objectId);
        RxLogTool.e("deleteBookByPlatformId platFormId:" + objectId);

        fetchLocalDataAndShow();
    }

    private void deleteBookOptionsForUrl(String objectId) {
        //移除
        BookStoreRepository.getInstance().deleteAllByBookIdAndUid(uid, objectId);
        RxLogTool.e("deleteBookByPlatformId platFormId:" + objectId);

        fetchLocalDataAndShow();
    }

    @Override
    public void initBookData(BookStoreResponse bean) {
        view_loading.setVisibility(View.VISIBLE);
        bookStoreResponse = bean;
        //清空之前记录
        bookStoreBeanList.clear();
        bookStoreGridAdapter.loadMoreEnd();
        //需要删除的书籍
        if (null != bookStoreResponse.getData().getDelete_List() && bookStoreResponse.getData().getDelete_List().size() > 0) {
            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getDelete_List()) {//本地移除该书
                if(!TextUtils.isEmpty(bookStoreBean.getBookId())){
                        //移除该书,旧书模式
                        deleteBookOptions(bookStoreBean.getPlatformId());
                }
            }
        }
        //需要删除的书籍
        if (null != bookStoreResponse.getData().getRecognitionDeleteList() && bookStoreResponse.getData().getRecognitionDeleteList().size() > 0) {
            for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionDeleteList()) {//本地移除该书
                if(!TextUtils.isEmpty(bookStoreBean.getBookId())){
                    //移除该书,新url模式
                    deleteBookOptionsForUrl(bookStoreBean.getBookId());
                }
            }
        }
        //给每本书指定用户ID
        int i = 0;
        long time = System.currentTimeMillis();
        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getRecognitionList()) {
            i++;
            bookStoreBean.setUid(uid);
            bookStoreBean.setUpdateDate(time - i);
            bookStoreBeanList.add(bookStoreBean);
        }
        //给每本书指定用户ID
        for (BookStoreBeanN bookStoreBean : bookStoreResponse.getData().getList()) {
            i++;
            bookStoreBean.setUid(uid);
            bookStoreBean.setUpdateDate(time - i);
            bookStoreBeanList.add(bookStoreBean);
        }
        //缓存这次的请求时间
        RxSharedPreferencesUtil.getInstance().putLong(bqtKey, bookStoreResponse.getData().getTimestamp());
        //显示数据-缓存数据
        BookStoreRepository.getInstance().saveBooks(bookStoreBeanList);

        //此处先请求同步共享版权状态的接口，然后再显示
        //加个时间戳，30s只请求一次？
        long requestTime = RxSharedPreferencesUtil.getInstance().getLong(Constant.LAST_REQUEST_TIME);
        if(time - requestTime > 30000) {
            RxSharedPreferencesUtil.getInstance().putLong(Constant.LAST_REQUEST_TIME,time);
            copyrightsBatchInfo();
        }else {
            //防刷机制，减轻系统负担，30s内，直接显示
            try {
                fetchLocalDataAndShow();
            } catch (Exception ee) {
            }
        }

        try {
            if (null != view_loading) {
                view_loading.setVisibility(View.GONE);
            }
            if (null != swipe_grid_refresh_layout) {
                swipe_grid_refresh_layout.setRefreshing(false);
            }

        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    @Override
    public void showError(String message) {
        try {
            if (null != view_loading) {
                view_loading.setVisibility(View.GONE);
            }
            if (null != swipe_grid_refresh_layout) {
                swipe_grid_refresh_layout.setRefreshing(false);
            }

        } catch (Exception ex) {
//            ex.printStackTrace();
        }
        RxToast.custom(message, Constant.ToastType.TOAST_ERROR).show();

    }

    @Override
    public void complete(String message) {
        try {
            if (null != view_loading) {
                view_loading.setVisibility(View.GONE);
            }
            if (null != swipe_grid_refresh_layout) {
                swipe_grid_refresh_layout.setRefreshing(false);
            }

        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    public void setChoose(int i) {
        this.choose = i;
        if(RxLoginTool.isLogin()) {
           //不管是否登录都能设置此页面，但只有登录时才会刷新数据。
            fetchData();//刷新数据
        }
    }


    //共享版权批量接口
    private void copyrightsBatchInfo() {
        List<BookStoreBeanN> list = BookStoreRepository.getInstance().getBookStoreInsideBooksByUserId(uid);
        StringBuilder sbUrl = new StringBuilder();
        if(null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size();i++) {
                BookStoreBeanN item  = list.get(i);
                if(i != 0){
                    sbUrl.append(",");
                }
                sbUrl.append(item.getBookId());
            }
        }

        if(TextUtils.isEmpty(sbUrl.toString())){
            //没有内站书的情况下直接显示
            fetchLocalDataAndShow();
            return;
        }

        Map<String, String> map=new HashMap<>();
        map.put("list",sbUrl.toString());
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/copyrights/batch/info",map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    //失败的情况下直接显示
                    fetchLocalDataAndShow();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                RxLogTool.e("getStartVersion-----" + response);
                try {
                    CopyrightsBatchInfoBean copyrightsBatchInfoBean = GsonUtil.getBean(response, CopyrightsBatchInfoBean.class);
                    if (copyrightsBatchInfoBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        List<Integer> tempList = copyrightsBatchInfoBean.getData();
                        //清空共享版权状态
                        BookStoreRepository.getInstance().clearIsCopyrightBooks(list);
                        //设置共享版权状态
                        if (null != tempList && !tempList.isEmpty()) {
                            for(Integer bid : tempList){
                                BookStoreRepository.getInstance().updateIsCopyrightBooks(uid, bid);
                            }
                        }
                    }
                    //数据完毕直接显示
                    fetchLocalDataAndShow();
                } catch (Exception e) {
                    try {
                        //失败的情况下直接显示
                        fetchLocalDataAndShow();
                    } catch (Exception ee) {
                    }
                }
            }
        });
    }


}
