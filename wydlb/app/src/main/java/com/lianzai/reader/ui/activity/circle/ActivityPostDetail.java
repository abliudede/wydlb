package com.lianzai.reader.ui.activity.circle;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonObject;
import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.JinZuanChargeBean;
import com.lianzai.reader.bean.AccountDetailBean;
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.BaseCountBean;
import com.lianzai.reader.bean.CircleBookBean;
import com.lianzai.reader.bean.CircleDynamicBean;
import com.lianzai.reader.bean.CirclePersonBean;
import com.lianzai.reader.bean.ConcernResponse;
import com.lianzai.reader.bean.FeedBackTypeResponse;
import com.lianzai.reader.bean.PayForBookBean;
import com.lianzai.reader.bean.PostDetailBean;
import com.lianzai.reader.bean.UrlKeyValueBean;
import com.lianzai.reader.bean.UserAttentionBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityWebView;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.adapter.BarPostItemAdapter;
import com.lianzai.reader.ui.contract.UserAttentionContract;
import com.lianzai.reader.ui.presenter.UserAttentionPresenter;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.DividerItemDecoration;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxDataTool;
import com.lianzai.reader.utils.RxDeviceTool;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxKeyboardTool;
import com.lianzai.reader.utils.RxLinearLayoutManager;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.utils.TimeFormatUtil;
import com.lianzai.reader.utils.URLUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.CommentEditText;
import com.lianzai.reader.view.CommentOptionPopup;
import com.lianzai.reader.view.CustomLoadMoreView;
import com.lianzai.reader.view.MoreOptionPopup;
import com.lianzai.reader.view.OverlapLogoView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogDelete;
import com.lianzai.reader.view.dialog.RxDialogSureCancelNew;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by lrz on 2017/10/13.
 * 动态详情
 */

public class ActivityPostDetail extends BaseActivity implements UserAttentionContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.tv_commom_title)
    TextView tv_commom_title;//标题

    @Bind(R.id.iv_user_logo)
    CircleImageView iv_user_logo;//头像

    @Bind(R.id.tv_nickname)
    TextView tv_nickname;//昵称

    @Bind(R.id.iv_follow)
    ImageView iv_follow;//关注按钮


    @Bind(R.id.tv_send)
    TextView tv_send;//发送按钮


    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.mask_view)
    View mask_view;//遮罩

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.ed_reply)
    CommentEditText ed_reply;//编辑输入框，一直浮现于视图之上

    @Bind(R.id.erro_view)
    RelativeLayout erro_view;

    @Bind(R.id.ll_bootom_view)
    LinearLayout ll_bootom_view;



    boolean isError = false;

    CustomLoadMoreView customLoadMoreView;

    BarPostItemAdapter barPostItemAdapter;

    int page = 1;

    String postId;//话题ID


    public AccountDetailBean accountDetailBean;
    private PostDetailBean baseBean;
    private CircleDynamicBean.DataBean.DynamicRespBean DynamicResp;
    private List<PostDetailBean.DataBean.CommentPageBean.ListBean> hotComments;

    @Inject
    UserAttentionPresenter userAttentionPresenter;

    //用于定位的评论id，直接在第一页进行查找
    private String focusId;
    private boolean isFollow = false;
    private int userType = Constant.Role.FANS_ROLE;

    public static void startActivity(Context context, String postId) {
//        RxActivityTool.removeActivityPostDetail();
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        RxActivityTool.skipActivity(context, ActivityPostDetail.class, bundle);
    }

    public static void startActivity(Context context, String postId, String focusId) {
//        RxActivityTool.removeActivityPostDetail();
        Bundle bundle = new Bundle();
        bundle.putString("postId", postId);
        bundle.putString("focusId", focusId);
        RxActivityTool.skipActivity(context, ActivityPostDetail.class, bundle);
    }

//    MoreOptionPopup moreOptionPopup;
    CommentOptionPopup commentOptionPopup;

    View headerView;
    RxLinearLayoutManager manager;

    List<PostDetailBean.DataBean.CommentPageBean.ListBean> replyListData = new ArrayList<>();

    private int replyId = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void initDatas() {
    }


    @Override
    public void configViews(Bundle savedInstanceState) {
        SystemBarUtils.setStatusBarColor(this, Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为暗色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        userAttentionPresenter.attachView(this);

        try {
            postId = getIntent().getExtras().getString("postId");
            focusId = getIntent().getExtras().getString("focusId");
        } catch (Exception e) {

        }


        ed_reply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int index = ed_reply.getSelectionStart();
                if (index >= 3 && s.toString().substring(index - 3, index).equals("\n\n\n")) {
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0, index - 1));
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index - 1);
                }
                if (index >= 3 && s.toString().substring(index - 3, index).equals("\n\n ")) {
                    //前面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    sb.append(s.toString().substring(0, index - 1));
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index - 1);
                }
                if (index >= 1 && s.length() >= index + 2 && s.toString().substring(index - 1, index + 2).equals("\n\n\n")) {
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index - 1);
                }
                if (index >= 1 && s.length() >= index + 2 && s.toString().substring(index - 1, index + 2).equals(" \n\n")) {
                    //后面两个已经是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index - 1);
                }
                if (index >= 2 && s.length() >= index + 1 && s.toString().substring(index - 2, index + 1).equals("\n\n\n")) {
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index - 1);
                }
                if (index >= 2 && s.length() >= index + 1 && s.toString().substring(index - 2, index + 1).equals("\n \n")) {
                    //前后各有一个是换行符，回车跟空格都不可用
                    StringBuilder sb = new StringBuilder();
                    if (index > 0) {
                        sb.append(s.toString().substring(0, index - 1));
                    }
                    if (s.length() > index) {
                        sb.append(s.toString().substring(index, s.length()));
                    }
                    ed_reply.setText(sb.toString());
                    ed_reply.setSelection(index - 1);
                }

                if(TextUtils.isEmpty(ed_reply.getText())){
                    tv_send.setTextColor(getResources().getColor(R.color.third_text_color));
                }else {
                    tv_send.setTextColor(getResources().getColor(R.color.bluetext_color));
                }

            }
        });

        tv_send.setOnClickListener(
                new OnRepeatClickListener() {
                    @Override
                    public void onRepeatClick(View v) {
                        RxKeyboardTool.hideKeyboard(ActivityPostDetail.this, ed_reply);
                        if(RxLoginTool.isLogin()) {
                            String content = ed_reply.getPublishText();
                            if (!TextUtils.isEmpty(content)) {
                                //回复的主函数
                                replyRequest(content);
                            }
                        }else {
                            ActivityLoginNew.startActivity(ActivityPostDetail.this);
                        }
                    }
                }
        );

        initHeaderView();

        barPostItemAdapter = new BarPostItemAdapter(replyListData);
        barPostItemAdapter.setOnLoadMoreListener(this, recyclerView);

        accountDetailBean = RxTool.getAccountBean();


        customLoadMoreView = new CustomLoadMoreView();
        barPostItemAdapter.setLoadMoreView(customLoadMoreView);
        barPostItemAdapter.addHeaderView(headerView, 0);

        barPostItemAdapter.setContentClickListener(new BarPostItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(ActivityPostDetail.this, String.valueOf(replyListData.get(pos).getComUserId()));
                } catch (Exception e) {
                }
            }

            @Override
            public void praiseClick(View v, int pos) {
                try {
                    PostDetailBean.DataBean.CommentPageBean.ListBean bean = replyListData.get(pos);
                    //点赞
                    if (!RxLoginTool.isLogin()) {
                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                        return;
                    }
                    if(!bean.isIsLike())
                    praiseRequest(String.valueOf(bean.getId()), 2, 2, pos);
                } catch (Exception e) {

                }
            }

            @Override
            public void replyClick(View v, int pos) {
                try {
                    PostDetailBean.DataBean.CommentPageBean.ListBean bean = replyListData.get(pos);
                    //评论区点击，打开评论详情弹窗
                    if (!RxLoginTool.isLogin()) {
                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                        return;
                    }
                    ActivityPostFloor.startActivity(ActivityPostDetail.this, String.valueOf(bean.getId()),"","",String.valueOf(DynamicResp.getPlatformId()));

                } catch (Exception e) {

                }
            }

            @Override
            public void contentClick(View v, int pos) {
                try {
                    PostDetailBean.DataBean.CommentPageBean.ListBean bean = replyListData.get(pos);
                    //弹出操作框
//                    if (!RxLoginTool.isLogin()) {
//                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
//                        return;
//                    }
                    if (null == commentOptionPopup) {
                        commentOptionPopup = new CommentOptionPopup(ActivityPostDetail.this);
                    }
                    commentOptionPopup.getTv_reply().setOnClickListener(
                            v1 -> {
                                if (!RxLoginTool.isLogin()) {
                                    ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                    return;
                                }
                                //回复点击
                                replyId = bean.getId();
                                ed_reply.setHint("回复" + bean.getComUserName() + "：");
                                ed_reply.setText("");
                                showInputMethod();
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_copy().setOnClickListener(
                            v1 -> {
                                //复制点击
                                RxClipboardTool.copyText(ActivityPostDetail.this, bean.getContentShow());
                                RxToast.custom("复制成功").show();
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_report().setOnClickListener(
                            v1 -> {
                                //举报点击
                                try {
                                    if (!RxLoginTool.isLogin()) {
                                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                        return;
                                    }
                                    //举报5：圈子动态 6：圈子评论 7：圈子回复
                                    againstCircleUser("6",String.valueOf(bean.getId()));
                                } catch (Exception e) {
                                }
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_delete().setOnClickListener(
                            v1 -> {
                                //删除点击
                                AccountDetailBean account = RxTool.getAccountBean();
                                if(null != account) {
                                    if (account.getData().getUid().equals(String.valueOf(bean.getComUserId()))) {
                                        //删除自己的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("contentType", 2);
                                        jsonObject.addProperty("objectId", bean.getId());
                                        deleteWay(jsonObject.toString(),1);
                                    } else {
                                        //删除别人的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("circleId", bean.getCircleId());
                                        jsonObject.addProperty("objectType", 2);
                                        jsonObject.addProperty("objectId", bean.getId());
                                        jsonObject.addProperty("objectUserId", bean.getComUserId());
                                        deleteWay(jsonObject.toString(),2);
                                    }
                                }else {
                                    ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                }

                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_mute().setOnClickListener(
                            v1 -> {
                                if (!RxLoginTool.isLogin()) {
                                    ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                    return;
                                }
                                //禁言
                                muteWay(bean.getComUserId(),bean.getCircleId());
                                commentOptionPopup.dismiss();
                            }
                    );
                    commentOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            v.setSelected(false);
                        }
                    });

                    try {
                        //区分是自己还是别人的动态
                        if (null != accountDetailBean) {
                            boolean equal = false;
                            if (accountDetailBean.getData().getUid().equals(String.valueOf(bean.getComUserId()))) {
                                equal = true;
                            }else {
                                equal = false;
                            }
                            if(userType < Constant.Role.MANAGE2_ROLE&& userType >= Constant.Role.ADMIN_ROLE && equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE&& !equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                            }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE&& equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE&& !equal){
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                            }else if(equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else {
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }
                        } else {
                            commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                            commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                            commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        RxLogTool.e(e.toString());
                    }
                    v.setSelected(true);
                    commentOptionPopup.showPopupWindow(ActivityPostDetail.this, v);

                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });


        manager = new RxLinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(barPostItemAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#333333"));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int distance = 0;
                try {
                    distance = getScrolledDistance();
                } catch (Exception e) {

                }
                if (distance <= 0) {
                    iv_user_logo.setVisibility(View.GONE);
                    tv_nickname.setVisibility(View.GONE);
                    iv_follow.setVisibility(View.GONE);
                    tv_commom_title.setVisibility(View.VISIBLE);
                } else {
                    iv_user_logo.setVisibility(View.VISIBLE);
                    tv_nickname.setVisibility(View.VISIBLE);
                    if (!isFollow) {
                        iv_follow.setVisibility(View.VISIBLE);
                    }
                    tv_commom_title.setVisibility(View.GONE);
                }
            }
        });


        showSwipeRefresh(true);
        onRefresh();
        //登录之后再获取角色信息
        if(RxLoginTool.isLogin()) {
            getCircleRole();
        }
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

    private void showSwipeRefresh(boolean isShow) {
        try {
            if (null != mSwipeRefreshLayout) {
                mSwipeRefreshLayout.setRefreshing(isShow);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void showInputMethod() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    RxKeyboardTool.showSoftInput(ActivityPostDetail.this, ed_reply);
                } catch (Exception e) {
                }
            }
        }, 200);
    }

    LinearLayout ll_dynamic_root;
    CircleImageView civ_dynamic_user_logo;
    TextView tv_dynamic_user_name;
    TextView tv_dynamic_create_date;
    TextView tv_dynamic_create_location;
    TextView tv_dynamic_content_text;
    LinearLayout ll_operation_praise;
    ImageView iv_operation_praise;
    TextView tv_operation_praise;
    LinearLayout ll_operation_comment;
    ImageView iv_operation_comment;
    TextView tv_operation_comment;
    ImageView iv_operation_share;
    ImageView iv_operation_more;

    RelativeLayout rl_payforbook;
    TextView tv_payforbook;
    ImageView iv_payforbook;

//    RelativeLayout rl_book;
//    SelectableRoundedImageView iv_book_cover;
//    TextView tv_book_chapter_name;
//    TextView tv_book_source;
//    TextView tv_book_name;
    LinearLayout ll_dynamic_images;
    View view_empty;
    TextView tv_hot_title;
    RecyclerView rv_hot_data;
    List<PostDetailBean.DataBean.CommentPageBean.ListBean> replyListData2 = new ArrayList<>();
    BarPostItemAdapter barPostItemAdapter2;

    TextView tv_sort;
    boolean sort = true;

    LinearLayout ly_week;
    SelectableRoundedImageView iv_week_cover;
    TextView tv_week_title;

    //圈子信息部分
    RelativeLayout circle_info;
    SelectableRoundedImageView iv_dynamic_book_cover;
    TextView tv_dynamic_book_title;
    TextView tv_dynamic_book_description;
    OverlapLogoView olv_users_logo;


    private void initHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.view_header_post_detail, null);
        ll_dynamic_root = headerView.findViewById(R.id.ll_dynamic_root);
        //用户信息
        civ_dynamic_user_logo = headerView.findViewById(R.id.civ_dynamic_user_logo);
        tv_dynamic_user_name = headerView.findViewById(R.id.tv_dynamic_user_name);
        tv_dynamic_create_date = headerView.findViewById(R.id.tv_dynamic_create_date);
        tv_dynamic_create_location = headerView.findViewById(R.id.tv_dynamic_create_location);
        //动态正文内容-纯文字，文字带图片,文字带书，文字带圈子,文字带网页
        tv_dynamic_content_text = headerView.findViewById(R.id.tv_dynamic_content_text);

        //显示打赏额外信息
        rl_payforbook= headerView.findViewById(R.id.rl_payforbook);
        tv_payforbook = headerView.findViewById(R.id.tv_payforbook);
        iv_payforbook = headerView.findViewById(R.id.iv_payforbook);

        //动态操作相关
        ll_operation_praise = headerView.findViewById(R.id.ll_operation_praise);
        iv_operation_praise = headerView.findViewById(R.id.iv_operation_praise);
        tv_operation_praise = headerView.findViewById(R.id.tv_operation_praise);
        ll_operation_comment = headerView.findViewById(R.id.ll_operation_comment);
        iv_operation_comment = headerView.findViewById(R.id.iv_operation_comment);
        tv_operation_comment = headerView.findViewById(R.id.tv_operation_comment);
        iv_operation_share = headerView.findViewById(R.id.iv_operation_share);
        iv_operation_more = headerView.findViewById(R.id.iv_operation_more);
        iv_operation_more.setVisibility(View.GONE);
        //书籍展示区域
//        rl_book = headerView.findViewById(R.id.rl_book);
//        iv_book_cover = headerView.findViewById(R.id.iv_book_cover);
//        tv_book_chapter_name = headerView.findViewById(R.id.tv_book_chapter_name);
//        tv_book_source = headerView.findViewById(R.id.tv_book_source);
//        tv_book_name = headerView.findViewById(R.id.tv_book_name);
        //周报展示区域
        ly_week = headerView.findViewById(R.id.ly_week);
        iv_week_cover = headerView.findViewById(R.id.iv_week_cover);
        tv_week_title = headerView.findViewById(R.id.tv_week_title);
        //图片展示区域
        ll_dynamic_images = headerView.findViewById(R.id.ll_dynamic_images);
        view_empty = headerView.findViewById(R.id.view_empty);
        //热门评论
        tv_hot_title = headerView.findViewById(R.id.tv_hot_title);
        rv_hot_data = headerView.findViewById(R.id.rv_hot_data);
        barPostItemAdapter2 = new BarPostItemAdapter(replyListData2);
        barPostItemAdapter2.setContentClickListener(new BarPostItemAdapter.ContentClickListener() {
            @Override
            public void headNickClick(View v, int pos) {
                try {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(ActivityPostDetail.this, String.valueOf(replyListData2.get(pos).getComUserId()));
                } catch (Exception e) {
                }
            }

            @Override
            public void praiseClick(View v, int pos) {
                try {
                    PostDetailBean.DataBean.CommentPageBean.ListBean bean = replyListData2.get(pos);
                    //点赞
                    if (!RxLoginTool.isLogin()) {
                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                        return;
                    }
                    if(!bean.isIsLike())
                    praiseRequest(String.valueOf(bean.getId()), 2, 1, pos);
                } catch (Exception e) {

                }
            }

            @Override
            public void replyClick(View v, int pos) {
                try {
                    PostDetailBean.DataBean.CommentPageBean.ListBean bean = replyListData2.get(pos);
                    //评论区点击，打开评论详情弹窗
                    if (!RxLoginTool.isLogin()) {
                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                        return;
                    }
                    ActivityPostFloor.startActivity(ActivityPostDetail.this, String.valueOf(bean.getId()),"","",String.valueOf(DynamicResp.getPlatformId()));

                } catch (Exception e) {

                }
            }

            @Override
            public void contentClick(View v, int pos) {
                try {
                    PostDetailBean.DataBean.CommentPageBean.ListBean bean = replyListData2.get(pos);
                    //弹出操作框
//                    if (!RxLoginTool.isLogin()) {
//                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
//                        return;
//                    }
                    if (null == commentOptionPopup) {
                        commentOptionPopup = new CommentOptionPopup(ActivityPostDetail.this);
                    }
                    commentOptionPopup.getTv_reply().setOnClickListener(
                            v1 -> {
                                //回复点击
                                if (!RxLoginTool.isLogin()) {
                                    ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                    return;
                                }
                                replyId = bean.getId();
                                ed_reply.setHint("回复" + bean.getComUserName() + "：");
                                ed_reply.setText("");
                                showInputMethod();
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_copy().setOnClickListener(
                            v1 -> {
                                //复制点击
                                RxClipboardTool.copyText(ActivityPostDetail.this, bean.getContentShow());
                                RxToast.custom("复制成功").show();
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_report().setOnClickListener(
                            v1 -> {
                                //举报点击
                                try {
                                    if (!RxLoginTool.isLogin()) {
                                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                        return;
                                    }
                                    //举报5：圈子动态 6：圈子评论 7：圈子回复
                                    againstCircleUser("6",String.valueOf(bean.getId()));
                                } catch (Exception e) {
                                }
                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_delete().setOnClickListener(
                            v1 -> {
                                //删除点击
                                AccountDetailBean account = RxTool.getAccountBean();
                                if(null != account) {
                                    if (account.getData().getUid().equals(String.valueOf(bean.getComUserId()))) {
                                        //删除自己的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("contentType", 2);
                                        jsonObject.addProperty("objectId", bean.getId());
                                        deleteWay(jsonObject.toString(),1);
                                    } else {
                                        //删除别人的
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("circleId", bean.getCircleId());
                                        jsonObject.addProperty("objectType", 2);
                                        jsonObject.addProperty("objectId", bean.getId());
                                        jsonObject.addProperty("objectUserId", bean.getComUserId());
                                        deleteWay(jsonObject.toString(),2);
                                    }
                                }else {
                                    ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                }

                                commentOptionPopup.dismiss();
                            }
                    );

                    commentOptionPopup.getTv_mute().setOnClickListener(
                            v1 -> {
                                if (!RxLoginTool.isLogin()) {
                                    ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                    return;
                                }
                                //禁言
                               muteWay(bean.getComUserId(),bean.getCircleId());
                                commentOptionPopup.dismiss();
                            }
                    );
                    commentOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            v.setSelected(false);
                        }
                    });

                    try {
                        //区分是自己还是别人的动态
                        if (null != accountDetailBean) {
                            boolean equal = false;
                            if (accountDetailBean.getData().getUid().equals(String.valueOf(bean.getComUserId()))) {
                                equal = true;
                            }else {
                                equal = false;
                            }
                            if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && !equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                            }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && !equal){
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                            }else if(equal){
                                commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }else {
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }
                        } else {
                            commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                            commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                            commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        RxLogTool.e(e.toString());
                    }
                    v.setSelected(true);
                    commentOptionPopup.showPopupWindow(ActivityPostDetail.this, v);

                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });


        rv_hot_data.setLayoutManager(new RxLinearLayoutManager(this));
        rv_hot_data.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        rv_hot_data.setAdapter(barPostItemAdapter2);

        tv_sort = headerView.findViewById(R.id.tv_sort);

        circle_info = headerView.findViewById(R.id.circle_info);
        iv_dynamic_book_cover = headerView.findViewById(R.id.iv_dynamic_book_cover);
        tv_dynamic_book_title = headerView.findViewById(R.id.tv_dynamic_book_title);
        tv_dynamic_book_description = headerView.findViewById(R.id.tv_dynamic_book_description);
        olv_users_logo = headerView.findViewById(R.id.olv_users_logo);

        circle_info.setOnClickListener(
                v -> {
                    if(null != DynamicResp){
                        ActivityCircleDetail.startActivity(ActivityPostDetail.this,String.valueOf(DynamicResp.getPlatformId()));
                    }else {
                        RxToast.custom("动态信息暂未获取到").show();
                    }

                }
        );


        ll_dynamic_root.setOnClickListener(
                v -> {
                    try {
                        //弹出操作框
//                        if (!RxLoginTool.isLogin()) {
//                            ActivityLoginNew.startActivity(ActivityPostDetail.this);
//                            return;
//                        }
                        if (null == commentOptionPopup) {
                            commentOptionPopup = new CommentOptionPopup(ActivityPostDetail.this);
                        }
                        commentOptionPopup.getTv_reply().setOnClickListener(
                                v1 -> {
                                    if (!RxLoginTool.isLogin()) {
                                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                        return;
                                    }
                                    //回复点击
                                    replyId = 0;
                                    ed_reply.setHint("回复" + DynamicResp.getUserName() + "：");
                                    ed_reply.setText("");
                                    showInputMethod();
                                    commentOptionPopup.dismiss();
                                }
                        );

                        commentOptionPopup.getTv_copy().setOnClickListener(
                                v1 -> {
                                    //复制点击
                                    RxClipboardTool.copyText(ActivityPostDetail.this, DynamicResp.getContentShow());
                                    RxToast.custom("复制成功").show();
                                    commentOptionPopup.dismiss();
                                }
                        );

                        commentOptionPopup.getTv_report().setOnClickListener(
                                v1 -> {
                                    //举报点击
                                    try {
                                        if (!RxLoginTool.isLogin()) {
                                            ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                            return;
                                        }
                                        //举报5：圈子动态 6：圈子评论 7：圈子回复
                                        againstCircleUser("5",String.valueOf(DynamicResp.getId()));
                                    } catch (Exception e) {
                                    }
                                    commentOptionPopup.dismiss();
                                }
                        );

                        commentOptionPopup.getTv_delete().setOnClickListener(
                                v1 -> {
                                    //删除点击
                                    AccountDetailBean account = RxTool.getAccountBean();
                                    if(null != account) {
                                        if (account.getData().getUid().equals(String.valueOf(DynamicResp.getUserId()))) {
                                            //删除自己的
                                            JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("contentType", 1);
                                            jsonObject.addProperty("objectId", DynamicResp.getId());
                                            deleteWay(jsonObject.toString(),1);
                                        } else {
                                            //删除别人的
                                            JsonObject jsonObject = new JsonObject();
                                            jsonObject.addProperty("circleId", DynamicResp.getPlatformId());
                                            jsonObject.addProperty("objectType", 1);
                                            jsonObject.addProperty("objectId", DynamicResp.getId());
                                            jsonObject.addProperty("objectUserId", DynamicResp.getUserId());
                                            deleteWay(jsonObject.toString(),2);
                                        }
                                    }else {
                                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                    }
                                    commentOptionPopup.dismiss();
                                }
                        );

                        commentOptionPopup.getTv_mute().setOnClickListener(
                                v1 -> {
                                    if (!RxLoginTool.isLogin()) {
                                        ActivityLoginNew.startActivity(ActivityPostDetail.this);
                                        return;
                                    }
                                    //禁言
                                    muteWay(DynamicResp.getUserId(),String.valueOf(DynamicResp.getPlatformId()));
                                    commentOptionPopup.dismiss();
                                }
                        );


                        try {
                            //区分是自己还是别人的动态
                            if (null != accountDetailBean) {
                                boolean equal = false;
                                if (accountDetailBean.getData().getUid().equals(String.valueOf(DynamicResp.getUserId()))) {
                                    equal = true;
                                }else {
                                    equal = false;
                                }
                                if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && equal){
                                    commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                    commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                                }else if(userType < Constant.Role.MANAGE2_ROLE && userType >= Constant.Role.ADMIN_ROLE && !equal){
                                    commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                    commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                                }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && equal){
                                    commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                    commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                                }else if(userType < Constant.Role.FANS_ROLE && userType >= Constant.Role.MANAGE2_ROLE && !equal){
                                    commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_mute().setVisibility(View.VISIBLE);
                                }else if(equal){
                                    commentOptionPopup.getTv_report().setVisibility(View.GONE);
                                    commentOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                                }else {
                                    commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                    commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                                    commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                                }
                            } else {
                                commentOptionPopup.getTv_report().setVisibility(View.VISIBLE);
                                commentOptionPopup.getTv_delete().setVisibility(View.GONE);
                                commentOptionPopup.getTv_mute().setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            RxLogTool.e(e.toString());
                        }

                        commentOptionPopup.showPopupWindow(ActivityPostDetail.this, tv_dynamic_content_text ,true);

                    } catch (Exception e) {
                        RxLogTool.e(e.toString());
                    }
                }
        );

        tv_sort.setOnClickListener(
                v -> {
                    if (sort) {
                        sort = false;
                        tv_sort.setText("楼层");
                        tv_sort.setCompoundDrawables(null, null, RxImageTool.getDrawable(R.mipmap.positive_sequence), null);
                    } else {
                        sort = true;
                        tv_sort.setText("楼层");
                        tv_sort.setCompoundDrawables(null, null, RxImageTool.getDrawable(R.mipmap.reverse_order), null);
                    }
                    requestPostDetail(true);
                }
        );

        civ_dynamic_user_logo.setOnClickListener(
                v -> {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(this, String.valueOf(DynamicResp.getUserId()));
                }
        );
        iv_user_logo.setOnClickListener(
                v -> {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(this, String.valueOf(DynamicResp.getUserId()));
                }
        );
        tv_dynamic_user_name.setOnClickListener(
                v -> {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(this, String.valueOf(DynamicResp.getUserId()));
                }
        );
        tv_nickname.setOnClickListener(
                v -> {
                    //点击进入个人主页
                    PerSonHomePageActivity.startActivity(this, String.valueOf(DynamicResp.getUserId()));
                }
        );
        iv_follow.setOnClickListener(
                v -> {
                    //关注此用户
                    try {
                        if (!RxLoginTool.isLogin()) {
                            ActivityLoginNew.startActivity(this);
                            return;
                        }
                        ArrayMap map = new ArrayMap();
                        map.put("attentionUserId", DynamicResp.getUserId());
                        map.put("attentionType", 1);
                        userAttentionPresenter.attentionOrUnattention(map, 0,0);
                    } catch (Exception e) {
                    }
                }
        );
        ll_operation_praise.setOnClickListener(
                v -> {
                    //动态点赞
                    try {
                        if (!RxLoginTool.isLogin()) {
                            ActivityLoginNew.startActivity(this);
                            return;
                        }
                        if(!DynamicResp.isIsLike())
                        praiseRequest(String.valueOf(DynamicResp.getId()), 1, 0, 0);
                    } catch (Exception e) {
                    }
                }
        );

        ll_operation_comment.setOnClickListener(
                v -> {
                    //评论点击
                    replyId = 0;
                    ed_reply.setHint("回复" + DynamicResp.getUserName() + "：");
                    ed_reply.setText("");
                    showInputMethod();
                }
        );

//        iv_operation_more.setOnClickListener(
//                v -> {
//                    //点击更多，举报或者删除
//                    if (!RxLoginTool.isLogin()) {
//                        ActivityLoginNew.startActivity(this);
//                        return;
//                    }
//                    if (null == moreOptionPopup) {
//                        moreOptionPopup = new MoreOptionPopup(this);
//                    }
//                    moreOptionPopup.getTv_report().setOnClickListener(
//                            v1 -> {
//                                try {
//                                    //举报5：圈子动态 6：圈子评论 7：圈子回复
//                                    againstCircleUser("5",String.valueOf(DynamicResp.getId()));
//                                } catch (Exception e) {
//
//                                }
//                                moreOptionPopup.dismiss();
//                            }
//                    );
//                    moreOptionPopup.getTv_delete().setOnClickListener(
//                            v1 -> {
//                                try {
//                                    //删除
//                                    JsonObject jsonObject = new JsonObject();
//                                    jsonObject.addProperty("contentType", 1);
//                                    jsonObject.addProperty("objectId", DynamicResp.getId());
//                                    requestDelete(jsonObject.toString());
//                                } catch (Exception e) {
//
//                                }
//                                moreOptionPopup.dismiss();
//                            }
//                    );
//                    moreOptionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            //隐藏蒙层
//                            //圈子详情页
//                            mask_view.setVisibility(View.GONE);
//                        }
//                    });
//                    //显示蒙层
//                    mask_view.setVisibility(View.VISIBLE);
//
//                    try {
//                        //区分是自己还是别人的动态
//                        if (null != accountDetailBean) {
//                            if (accountDetailBean.getData().getUid().equals(String.valueOf(DynamicResp.getUserId()))) {
//                                moreOptionPopup.getTv_delete().setVisibility(View.VISIBLE);
//                                moreOptionPopup.getTv_report().setVisibility(View.GONE);
//                            } else {
//                                moreOptionPopup.getTv_delete().setVisibility(View.GONE);
//                                moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            moreOptionPopup.getTv_delete().setVisibility(View.GONE);
//                            moreOptionPopup.getTv_report().setVisibility(View.VISIBLE);
//                        }
//                    } catch (Exception e) {
//
//                    }
//                    moreOptionPopup.showPopupWindow(this, v);
//                }
//        );

        ly_week.setOnClickListener(
                v -> {
                    //周报等页面，跳url
                    try {
                        CircleBookBean circleBookBean = GsonUtil.getBean(DynamicResp.getPicturesShow(), CircleBookBean.class);
                        ActivityWebView.startActivity(ActivityPostDetail.this, circleBookBean.getUrl(), DynamicResp.getTitleShow(), DynamicResp.getContentShow(), circleBookBean.getCover(), 2);
                    } catch (Exception e) {
                    }
                }
        );
//        rl_book.setOnClickListener(
//                v -> {
//                    if( DynamicResp.getPostType() == 3){
//                        //书籍页面，跳url
//                        try{
//                            CircleBookBean circleBookBean = GsonUtil.getBean(DynamicResp.getPicturesShow(),CircleBookBean.class);
//                            //是否开启了自动转码
//                            if(RxSharedPreferencesUtil.getInstance().getBoolean(Constant.AUTO_EXCHANGE,false)) {
//                                UrlReadActivity.startActivity(ActivityPostDetail.this,circleBookBean.getBookId(),circleBookBean.getUrl(),circleBookBean.getSource());
//                            }else {
//                                ActivityWebView.startActivityForRead(ActivityPostDetail.this,circleBookBean.getUrl(),circleBookBean.getBookId(),circleBookBean.getUrl(),circleBookBean.getSource());
//                            }
//                        }catch (Exception e){
//                        }
//                    }else if( DynamicResp.getPostType() == 2){
//                        //内站书籍页面，跳url
//                        try{
//                            CircleBookBean circleBookBean = GsonUtil.getBean(DynamicResp.getPicturesShow(),CircleBookBean.class);
//                            UrlReadActivity.startActivity(ActivityPostDetail.this, String.valueOf(circleBookBean.getBookId()),circleBookBean.getUrl(),circleBookBean.getSource());
//                        }catch (Exception e){
//                        }
//                    }
//                }
//        );

    }


    /* 获取圈子角色*/
    private void getCircleRole() {
        Map<String, String> map = new HashMap<>();
        map.put("objectType", "1");
        map.put("objectId",postId);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circleManage/getCircleRole", map, new CallBackUtil.CallBackString() {
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
                    BaseCountBean baseCountBean = GsonUtil.getBean(response, BaseCountBean.class);
                    if (baseCountBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                      userType = baseCountBean.getData();
                    }else{//加载失败
                        RxToast.custom(baseCountBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }

    /*查询用户专注状态*/
    private void getUserBothAttentionStatus(String uid) {
        Map<String, String> map = new HashMap<>();
        map.put("otherUserId", uid);
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/userAttention/getUserBothAttentionStatus", map, new CallBackUtil.CallBackString() {
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
                    BaseCountBean baseCountBean = GsonUtil.getBean(response, BaseCountBean.class);
                    if (baseCountBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {

                        if (null == accountDetailBean) {
                            iv_follow.setVisibility(View.GONE);
                            isFollow = true;
                        } else if (baseCountBean.getData() == 0 && !uid.equals(accountDetailBean.getData().getUid())) {
                            isFollow = false;
                        } else {
                            iv_follow.setVisibility(View.GONE);
                            isFollow = true;
                        }
                    }else{//加载失败
                        RxToast.custom(baseCountBean.getMsg(),Constant.ToastType.TOAST_ERROR).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 话题详情
     */
    private void requestPostDetail(boolean isRefresh) {

        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        Map<String, String> map = new HashMap<>();
        map.put("pageNumber", String.valueOf(page));
        map.put("pageSize", String.valueOf(100));
        map.put("orderBy", String.valueOf(sort));
        OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/circles/dynamicDetail/" + postId, map, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
//                    RxToast.custom("加载失败/circles/dynamicDetail/").show();
                    showSwipeRefresh(false);
                    barPostItemAdapter.loadMoreFail();
                } catch (Exception ee) {
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    RxLogTool.e("requestHistory:" + response);
                    showSwipeRefresh(false);
                    baseBean = GsonUtil.getBean(response, PostDetailBean.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        if (isRefresh) {
                            if(baseBean.getData().isIsDelete()){
                                erro_view.setVisibility(View.VISIBLE);
                                mSwipeRefreshLayout.setVisibility(View.GONE);
                                ll_bootom_view.setVisibility(View.GONE);
                                return;
                            }else {
                                erro_view.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                ll_bootom_view.setVisibility(View.VISIBLE);
                            }
                            //加载头部视图
                            DynamicResp = baseBean.getData().getDynamicResp();
                            hotComments = baseBean.getData().getHotComments();
                            loadHeadView();

                            replyListData.clear();
                            if (null != baseBean.getData().getCommentPage().getList()) {
                                replyListData.addAll(baseBean.getData().getCommentPage().getList());
                            }
                            if (replyListData.size() > 0) {
                                barPostItemAdapter.replaceData(replyListData);
                                barPostItemAdapter.setEnableLoadMore(true);
                                barPostItemAdapter.loadMoreComplete();
                                view_empty.setVisibility(View.GONE);
                                //进行匹配定位,定位评论或者定位到评论区域
                                if (null != focusId) {
                                    if (focusId.equals("comment")) {
                                        scrollTo(0);
                                    } else {
                                        for (int i = 0; i < replyListData.size(); i++) {
                                            String tempId = String.valueOf(replyListData.get(i).getId());
                                            if (tempId.equals(focusId)) {
                                                scrollTo(i);
                                                break;
                                            }
                                        }
                                    }
                                    focusId = null;
                                }
                            } else {
                                barPostItemAdapter.replaceData(replyListData);
                                view_empty.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (null != baseBean.getData().getCommentPage().getList() && baseBean.getData().getCommentPage().getList().size() > 0) {
                                replyListData.addAll(baseBean.getData().getCommentPage().getList());
                                barPostItemAdapter.setNewData(replyListData);
                            } else {
                                barPostItemAdapter.loadMoreEnd();
                            }
                        }
                    } else if(baseBean.getCode() == Constant.ResponseCodeStatus.DISABLE_CIRCLE) {
                        //跳到圈子已关闭的页面
                        ActivityCircleClose.startActivity(ActivityPostDetail.this);
                        finish();
                    }else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }

                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }
        });
    }

    //滚动到指定视图
    private void scrollTo(int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (position == 0) {
                        int top = ll_dynamic_root.getHeight();
                        recyclerView.smoothScrollBy(0, top);
                    } else {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position + barPostItemAdapter.getHeaderLayoutCount(), 0);
                    }

                } catch (Exception e) {
                }
            }
        }, 200);
    }

    private void loadHeadView() {
        RxImageTool.loadLogoImage(ActivityPostDetail.this, DynamicResp.getUserPic(), civ_dynamic_user_logo);
        RxImageTool.loadLogoImage(ActivityPostDetail.this, DynamicResp.getUserPic(), iv_user_logo);
        tv_dynamic_user_name.setText(DynamicResp.getUserName());
        tv_nickname.setText(DynamicResp.getUserName());
        if(RxLoginTool.isLogin()) {
            //关注状态,只在此时获取关注状态
            getUserBothAttentionStatus(String.valueOf(DynamicResp.getUserId()));
        }

        tv_dynamic_create_date.setText(TimeFormatUtil.getInterval(DynamicResp.getCreateTime()));
        if (!TextUtils.isEmpty(DynamicResp.getAddress())) {
            tv_dynamic_create_location.setVisibility(View.VISIBLE);
            tv_dynamic_create_location.setText(DynamicResp.getAddress());
        } else {
            tv_dynamic_create_location.setVisibility(View.GONE);
        }
        //识别书名号并设置点击事件。
        URLUtils.replaceBook(DynamicResp.getContentShow(),ActivityPostDetail.this,tv_dynamic_content_text,DynamicResp.getUrlTitle());


        if (DynamicResp.isIsLike()) {
            iv_operation_praise.setImageResource(R.mipmap.icon_dynamic_praised);
        } else {
            iv_operation_praise.setImageResource(R.mipmap.icon_dynamic_not_praise);
        }
        tv_operation_praise.setText(RxDataTool.getLikeCount(DynamicResp.getLikeCount()));
        tv_operation_comment.setText(RxDataTool.getLikeCount(DynamicResp.getCommentCount()));

        //圈子信息部分
        RxImageTool.loadLogoImage(ActivityPostDetail.this, baseBean.getData().getPlatformCover(), iv_dynamic_book_cover);
        tv_dynamic_book_title.setText(baseBean.getData().getPlatformName());
        tv_dynamic_book_description.setText("已有" + baseBean.getData().getPlatformPeopleNumber() + "名用户加入了这个圈子");
        try {
            ArrayList<String> listTemp = new ArrayList<>();
            int size = 0;
            if (null != baseBean.getData().getHeadList() && !baseBean.getData().getHeadList().isEmpty()) {
                size = baseBean.getData().getHeadList().size();
                if (size > 3) {
                    listTemp.addAll(baseBean.getData().getHeadList().subList(0, 3));
                } else {
                    listTemp.addAll(baseBean.getData().getHeadList());
                }
            }
            olv_users_logo.dynamicUpdate(listTemp, "");
        } catch (Exception e) {

        }


        //接下来判断是什么类型的动态
        /*2,3.书籍类型4,5.周报类型30.普通类型*/
        if(DynamicResp.getPostStatus() == 5){
            //举报类型
            tv_dynamic_content_text.setText("--该内容举报过多，连载客服正在审核中--");
            ly_week.setVisibility(View.GONE);
            ll_dynamic_images.setVisibility(View.GONE);
            rl_payforbook.setVisibility(View.GONE);
        }else if (DynamicResp.getPostType() == 2 || DynamicResp.getPostType() == 3) {
            ly_week.setVisibility(View.GONE);
            ll_dynamic_images.setVisibility(View.GONE);
            rl_payforbook.setVisibility(View.GONE);
//            try {
//                CircleBookBean circleBookBean = GsonUtil.getBean(DynamicResp.getPicturesShow(), CircleBookBean.class);
//                RxImageTool.loadImage(ActivityPostDetail.this, circleBookBean.getCover(), iv_book_cover);
//                String source = circleBookBean.getSource();
//                tv_book_chapter_name.setText(circleBookBean.getChapterName());
//                tv_book_source.setText("来源：" + source);
//                tv_book_name.setText("书名：" + circleBookBean.getBookName());
//                //不显示内站书来源
//                if(TextUtils.isEmpty(source) || source.contains("lianzai.com") || source.contains("bendixing.net") ){
//                    tv_book_source.setText("来源：连载阅读");
//                }
//            } catch (Exception e) {
//            }
        } else if (DynamicResp.getPostType() == 4 || DynamicResp.getPostType() == 5 || DynamicResp.getPostType() == 6 || DynamicResp.getPostType() == 7) {
            ly_week.setVisibility(View.VISIBLE);
            ll_dynamic_images.setVisibility(View.GONE);
            rl_payforbook.setVisibility(View.GONE);
            try {
                CircleBookBean circleBookBean = GsonUtil.getBean(DynamicResp.getPicturesShow(), CircleBookBean.class);
                RxImageTool.loadImage(ActivityPostDetail.this, circleBookBean.getCover(), iv_week_cover);
                tv_week_title.setText(DynamicResp.getTitleShow());
            } catch (Exception e) {
            }
        } else {

            String pictureShow = DynamicResp.getPicturesShow();
            if(!TextUtils.isEmpty(pictureShow) && pictureShow.startsWith("{")){//打赏发布的动态
                ly_week.setVisibility(View.GONE);
                ll_dynamic_images.setVisibility(View.GONE);
                rl_payforbook.setVisibility(View.VISIBLE);
                try {
                    PayForBookBean payForBookBean = GsonUtil.getBean(pictureShow, PayForBookBean.class);
                    RxImageTool.loadImage(ActivityPostDetail.this, payForBookBean.getRewardImg(), iv_payforbook);
                    tv_payforbook.setText(payForBookBean.getRewardContent());
                } catch (Exception e) {
                    RxLogTool.e(e.toString());
                }
            }else {
                ly_week.setVisibility(View.GONE);
                ll_dynamic_images.setVisibility(View.VISIBLE);
                rl_payforbook.setVisibility(View.GONE);
                if (null != DynamicResp.getThumbnailImages() && !DynamicResp.getThumbnailImages().isEmpty()) {
                    showDynamicImage(ll_dynamic_images, DynamicResp.getThumbnailImages());
                } else {
                    ll_dynamic_images.setVisibility(View.GONE);
                }
            }

        }

        //加载热门评论
        //判空
        replyListData2.clear();
        if (null != hotComments) {
            if (!hotComments.isEmpty()) {
                tv_hot_title.setVisibility(View.VISIBLE);
                rv_hot_data.setVisibility(View.VISIBLE);
                replyListData2.addAll(hotComments);
            }
        }
        barPostItemAdapter2.replaceData(replyListData2);
        //没有热门数据的时候，直接隐藏热门评论区域
        if (replyListData2.isEmpty()) {
            tv_hot_title.setVisibility(View.GONE);
            rv_hot_data.setVisibility(View.GONE);
        }
    }

    @Override
    public void gc() {
    }

    @Override
    public void initToolBar() {
    }

    @OnClick(R.id.img_back)
    void back() {
        backClick();
    }


    @Override
    public void onLoadMoreRequested() {
        requestPostDetail(false);
//        mSwipeRefreshLayout.setEnabled(false);
//        if (barPostItemAdapter.getData().size() < Constant.PageSize.MaxPageSize) {
//            RxLogTool.e("onLoadMoreRequested---不足一页");
//            barPostItemAdapter.loadMoreEnd();
//        } else {
//            if (!isError) {
//                requestPostDetail(false);
//            } else {
//                isError = true;
//                barPostItemAdapter.loadMoreFail();
//            }
//        }
//        mSwipeRefreshLayout.setEnabled(true);
    }


    //刷新数据
    @Override
    public void onRefresh() {
        requestPostDetail(true);
    }

    /*likeType:1、动态点赞2、评论点赞3、回复点赞*/
    /*isHot: 0、动态主题部分 1、热门评论部分 2、全部评论部分*/
    public void praiseRequest(String objectId, int likeType, int isHot, int position) {
        if(null == DynamicResp){
            RxToast.custom("动态信息暂未获取到").show();
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("likeType", likeType);
        jsonObject.addProperty("objectId", objectId);
        jsonObject.addProperty("platformId", String.valueOf(DynamicResp.getPlatformId()));
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/like", jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //此处展示金钻消耗toast
                        RxToast.showJinzuanCharge(circleDynamicBean);
                        //更新点赞状态
                        if (isHot == 0) {
                            //动态点赞
                            DynamicResp.setIsLike(true);
                            iv_operation_praise.setImageResource(R.mipmap.icon_dynamic_praised);
                            tv_operation_praise.setText(RxDataTool.getLikeCount(DynamicResp.getLikeCount() + 1));
                        } else if (isHot == 1) {
                            //热门评论点赞
                            PostDetailBean.DataBean.CommentPageBean.ListBean listBean = replyListData2.get(position);
                            int count = listBean.getLikeCount();
                            count++;
                            listBean.setIsLike(true);
                            listBean.setLikeCount(count);
                            replyListData2.set(position, listBean);
                            barPostItemAdapter2.replaceData(replyListData2);
                        } else if (isHot == 2) {
                            //全部评论点赞
                            PostDetailBean.DataBean.CommentPageBean.ListBean listBean = replyListData.get(position);
                            int count = listBean.getLikeCount();
                            count++;
                            listBean.setIsLike(true);
                            listBean.setLikeCount(count);
                            replyListData.set(position, listBean);
                            barPostItemAdapter.replaceData(replyListData);
                        }
                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 删除动态或者评论内容类型：1-帖子，2-评论，3-回复
     */
    private void requestDelete(String json) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/delete", json, new CallBackUtil.CallBackString() {
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
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("删除成功").show();
                        onRefresh();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 删除动态
     */
    private void requestDeleteOther(String json) {
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/deleteCircleContent",json, new CallBackUtil.CallBackString() {
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
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("删除成功").show();
                        onRefresh();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

    /**
     * 动态图片显示
     * 规则-1-3张图片，按显示区域等比例显示 4张图：每行显示两张 5张图：第一行显示两张 第二行显示三张 6张图：每行显示三张 7张图：第一行显示四张 第二行显示三张 8张图：每行显示四张 9张图：每行显示三张
     *
     * @param ll_dynamic_images
     */
    private void showDynamicImage(LinearLayout ll_dynamic_images, List<String> images) {
        if (null != ll_dynamic_images) {
            int contentWidth = RxDeviceTool.getScreenWidth(this) - RxImageTool.dip2px(32);
            int imageCount = images.size();
            int imageSize = 0;//图片尺寸
            int radius = 4;

            RxLogTool.e("imageCount:" + imageCount);

            if (imageCount == 0) {
                ll_dynamic_images.setVisibility(View.GONE);
            } else {
                ll_dynamic_images.setVisibility(View.VISIBLE);
                ll_dynamic_images.removeAllViews();

                int imageLine = 1;//默认一行

                if (imageCount == 4 || imageCount == 5 || imageCount == 6 || imageCount == 7 || imageCount == 8) {//两行 4张图：每行显示两张 6张图：每行显示三张 8张图：每行显示四张 9张图 每行显示三张
                    imageLine = 2;
                } else if (imageCount == 9) {//三行
                    imageLine = 3;
                }

                for (int i = 0; i < imageLine; i++) {
                    LinearLayout lineView = new LinearLayout(this);
                    LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lineView.setOrientation(LinearLayout.HORIZONTAL);
                    lineParams.setMargins(0, 6, 0, 0);
                    lineView.setLayoutParams(lineParams);

                    int lineImageCount = 0;

                    if (imageCount == 1) {
                        imageSize = contentWidth / 2 + 30;
                        SelectableRoundedImageView imgView = getRoundImageView(radius, radius, radius, radius, imageSize, false);
                        RxImageTool.loadImage(this, images.get(0), imgView);
                        lineView.addView(imgView, 0);

                        imgView.setOnClickListener(
                                v -> {
                                    ArrayList<String> listtemp = new ArrayList<String>();
                                    listtemp.addAll(DynamicResp.getImages());
                                    ActivityImagesPreview.startActivity(this, listtemp, 0);
                                }
                        );
                    } else if (imageCount == 2) {
                        lineImageCount = 2;
                        imageSize = contentWidth / 2;

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = i + j;
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, radius, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }

                    } else if (imageCount == 3) {
                        lineImageCount = 3;
                        imageSize = contentWidth / 3;

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*3)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, radius, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }

                    } else if (imageCount == 4) {
                        lineImageCount = 2;
                        imageSize = contentWidth / (lineImageCount + 1);

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }

                    } else if (imageCount == 5) {
                        if (i == 0) {//第一行
                            lineImageCount = 2;
                            imageSize = contentWidth / lineImageCount + 3;//第二行比第一行多一个间距 6，间距分配到图片宽度里面，故取3
                        } else if (i == 1) {
                            lineImageCount = 3;
                            imageSize = contentWidth / lineImageCount;
                        }

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition;
                            if (i == 0) {
                                imagePosition = j * (i + 1);
                            } else {
                                imagePosition = j + 2;
                            }
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxLogTool.e("imagePosition:" + imagePosition);
                            //加载图片
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);

                            //添加到父容器中
                            lineView.addView(imgView, j);
                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }
                    } else if (imageCount == 6) {
                        lineImageCount = 3;
                        imageSize = contentWidth / 3;

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*3)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 5) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);
                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }

                    } else if (imageCount == 7) {
                        if (i == 0) {//第一行
                            lineImageCount = 4;
                            imageSize = contentWidth / lineImageCount - 1;
                        } else if (i == 1) {
                            lineImageCount = 3;
                            imageSize = contentWidth / lineImageCount;//第二行比第一行多一个间距 6，间距分配到图片宽度里面，每个图片宽度增加2
                        }

                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition;
                            if (i == 0) {
                                imagePosition = j;
                            } else {
                                imagePosition = j + 4;
                            }
                            RxLogTool.e("-----imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 1 || imagePosition == 2 || imagePosition == 5) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 6) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, false);
                            }
                            //加载图片
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);

                            //添加到父容器中
                            lineView.addView(imgView, j);
                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }
                    } else if (imageCount == 8) {
                        lineImageCount = 4;
                        imageSize = contentWidth / 4 - 3;
                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*4)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 3) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 4) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1 || imagePosition == 2 || imagePosition == 5 || imagePosition == 6) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 7) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);

                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }
                    } else if (imageCount == 9) {
                        lineImageCount = 3;
                        imageSize = contentWidth / 3;
                        for (int j = 0; j < lineImageCount; j++) {
                            int imagePosition = (i * lineImageCount) + j;//（i*3)+j
                            RxLogTool.e("imagePosition:" + imagePosition);
                            SelectableRoundedImageView imgView = null;
                            if (imagePosition == 0) {
                                imgView = getRoundImageView(radius, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 2) {
                                imgView = getRoundImageView(0, radius, 0, 0, imageSize, true);
                            } else if (imagePosition == 6) {
                                imgView = getRoundImageView(0, 0, radius, 0, imageSize, false);
                            } else if (imagePosition == 1 || imagePosition == 3 || imagePosition == 4 || imagePosition == 5 || imagePosition == 7) {
                                imgView = getRoundImageView(0, 0, 0, 0, imageSize, false);
                            } else if (imagePosition == 8) {
                                imgView = getRoundImageView(0, 0, 0, radius, imageSize, true);
                            }
                            RxImageTool.loadImage(this, images.get(imagePosition), imgView);
                            lineView.addView(imgView, j);

                            imgView.setOnClickListener(
                                    v -> {
                                        ArrayList<String> listtemp = new ArrayList<String>();
                                        listtemp.addAll(DynamicResp.getImages());
                                        ActivityImagesPreview.startActivity(this, listtemp, imagePosition);
                                    }
                            );
                        }

                    }

                    ll_dynamic_images.addView(lineView);
                }
            }

        }

    }

    /**
     * @param leftTop
     * @param rightTop
     * @param leftBottom
     * @param rightBottom
     * @param imageSize
     * @param isLastItem  是否是当前行的最后一个元素
     * @return
     */
    private SelectableRoundedImageView getRoundImageView(float leftTop, float rightTop, float leftBottom, float rightBottom, int imageSize, boolean isLastItem) {
        SelectableRoundedImageView imgView = new SelectableRoundedImageView(this);
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgView.setBackgroundResource(R.drawable.v2_add_pic_bg_corner);
        imgView.setBorderColor(Color.TRANSPARENT);
        imgView.setCornerRadiiDP(leftTop, rightTop, leftBottom, rightBottom);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageSize, imageSize);
        if (!isLastItem) {
            params.setMargins(0, 0, 6, 0);
        } else {
            params.setMargins(0, 0, 0, 0);
        }

        imgView.setLayoutParams(params);
        return imgView;
    }

    /*1、动态回复2、评论回复*/
    public void replyRequest(String content) {
        if (replyId == 0) {
            getOneUrlResultForReplyDynamic(content);
        } else {
            getOneUrlResultForReplyComment(content);
        }
    }

    //实时解析网页链接
    public void getOneUrlResultForReplyDynamic(String content) {
        if (!TextUtils.isEmpty(content)) {
            //获取链接数组和新拼接的带空格字符串
            List<String> urlList = URLUtils.getUrlsInContent(content);
            //有链接时。
            if (urlList != null && !urlList.isEmpty()) {
                List<UrlKeyValueBean.DataBean> mList = new ArrayList<>();
                for (int i = 0 ; i <  urlList.size() ; i++) {
                    //从后往前的顺序显示。
                    String temp = urlList.get(i);
                    //去解析
                    //解析网址，解析成功则显示解析后的结果
                    Observable.create(new ObservableOnSubscribe<Map>() {
                        @Override
                        public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
                            Map map = null;
                            try {
                                //这里开始是做一个解析，需要在非UI线程进行
                                Document document = Jsoup.parse(new URL(temp), 5000);
                                String title = document.head().getElementsByTag("title").text();
                                map = new HashMap();
                                map.put("code", "1");
                                map.put("title", title);
                                emitter.onNext(map);
                            } catch (IOException e) {
                                map = new HashMap();
                                map.put("code", "0");
                                emitter.onNext(map);
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Map>() {
                                @Override
                                public void accept(Map map) throws Exception {

                                    if (map.get("code").equals("1") && !TextUtils.isEmpty(map.get("title").toString())) {
                                        String title = map.get("title").toString();
                                        if (title.length() > 20) title = title.substring(0, 19) + "...";
                                        //存入列表
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle(title);
                                        mList.add(info);
                                    } else {
//                                        String[] str = temp.split("\\.");
//                                        if (str.length > 1) {
//                                            //存入数据库
//                                            String str1 = str[str.length - 2];
//                                            String str2 = str[str.length - 1];
//                                            String[] strtemp = str2.split("\\/");
//                                            if(strtemp.length > 1){
//                                                str2 = strtemp[0];
//                                            }
//                                            StringBuilder sb = new StringBuilder();
//                                            sb.append(str1);
//                                            sb.append(".");
//                                            sb.append(str2);
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//
//                                            String title = sb.toString();
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        } else {
//                                            //存入数据库
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//
//                                            String title = temp;
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        }
                                        //存入数据库
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle("网页链接");
                                        mList.add(info);
                                    }

                                    //判断是不是所有的链接都有了结果
                                    if(mList.size() == urlList.size()){
                                        UrlKeyValueBean uploadBean = new UrlKeyValueBean();
                                        uploadBean.setList(mList);
                                        String uploadStr = GsonUtil.toJsonString(uploadBean);
                                        replyDynamic(content,uploadStr);
                                    }
                                }
                            });
                }
            }else {
                replyDynamic(content,"");
            }
        }
    }

    private void replyDynamic(String content , String uploadStr) {
        if(null == DynamicResp){
            RxToast.custom("动态信息暂未获取到").show();
            return;
        }
        if(null != tv_send){
            tv_send.setEnabled(false);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("urlTitle", uploadStr);
        jsonObject.addProperty("dynamicId", postId);
        jsonObject.addProperty("platformId", String.valueOf(DynamicResp.getPlatformId()));
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/dynamicComment", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try {
                    if(null != tv_send){
                        tv_send.setEnabled(true);
                    }
                } catch (Exception ex) {
//                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    if(null != tv_send){
                        tv_send.setEnabled(true);
                    }
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //此处展示金钻消耗toast
                        RxToast.showJinzuanCharge(circleDynamicBean);
                        ed_reply.setText("");
                        onRefresh();

                        if(circleDynamicBean.getData().getApproveType() != 1){
                            //不是先审圈子//刷新圈子列表
                            RxEventBusTool.sendEvents(Constant.EventTag.REFRESH_CIRCLE_LIST);
                        }

                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }

    //实时解析网页链接
    public void getOneUrlResultForReplyComment(String content) {
        if (!TextUtils.isEmpty(content)) {
            //获取链接数组和新拼接的带空格字符串
            List<String> urlList = URLUtils.getUrlsInContent(content);
            //有链接时。
            if (urlList != null && !urlList.isEmpty()) {
                List<UrlKeyValueBean.DataBean> mList = new ArrayList<>();
                for (int i = 0 ; i <  urlList.size() ; i++) {
                    //从后往前的顺序显示。
                    String temp = urlList.get(i);
                    //去解析
                    //解析网址，解析成功则显示解析后的结果
                    Observable.create(new ObservableOnSubscribe<Map>() {
                        @Override
                        public void subscribe(ObservableEmitter<Map> emitter) throws Exception {
                            Map map = null;
                            try {
                                //这里开始是做一个解析，需要在非UI线程进行
                                Document document = Jsoup.parse(new URL(temp), 5000);
                                String title = document.head().getElementsByTag("title").text();
                                map = new HashMap();
                                map.put("code", "1");
                                map.put("title", title);
                                emitter.onNext(map);
                            } catch (IOException e) {
                                map = new HashMap();
                                map.put("code", "0");
                                emitter.onNext(map);
                            }
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Map>() {
                                @Override
                                public void accept(Map map) throws Exception {

                                    if (map.get("code").equals("1") && !TextUtils.isEmpty(map.get("title").toString())) {
                                        String title = map.get("title").toString();
                                        if (title.length() > 20) title = title.substring(0, 19) + "...";
                                        //存入列表
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle(title);
                                        mList.add(info);
                                    } else {
//                                        String[] str = temp.split("\\.");
//                                        if (str.length > 1) {
//                                            //存入数据库
//                                            String str1 = str[str.length - 2];
//                                            String str2 = str[str.length - 1];
//                                            String[] strtemp = str2.split("\\/");
//                                            if(strtemp.length > 1){
//                                                str2 = strtemp[0];
//                                            }
//                                            StringBuilder sb = new StringBuilder();
//                                            sb.append(str1);
//                                            sb.append(".");
//                                            sb.append(str2);
//
//                                            String title = sb.toString();
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        } else {
//                                            //存入数据库
//                                            UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
//
//                                            String title = temp;
//                                            if (title.length() > 10) title = title.substring(0, 9) + "...";
//
//                                            info.setUrl(temp);
//                                            info.setTitle(title);
//                                            mList.add(info);
//                                        }
                                        //存入数据库
                                        UrlKeyValueBean.DataBean info = new UrlKeyValueBean.DataBean();
                                        info.setUrl(temp);
                                        info.setTitle("网页链接");
                                        mList.add(info);
                                    }

                                    //判断是不是所有的链接都有了结果
                                    if(mList.size() == urlList.size()){
                                        UrlKeyValueBean uploadBean = new UrlKeyValueBean();
                                        uploadBean.setList(mList);
                                        String uploadStr = GsonUtil.toJsonString(uploadBean);
                                        replyComment(content,uploadStr);
                                    }
                                }
                            });
                }
            }else {
                replyComment(content,"");
            }
        }
    }

    private void replyComment(String content,String uploadStr) {
        if(null == DynamicResp){
            RxToast.custom("动态信息暂未获取到").show();
            return;
        }
        if(null != tv_send){
            tv_send.setEnabled(false);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commentId", replyId);
        jsonObject.addProperty("content", content);
        jsonObject.addProperty("urlTitle", uploadStr);
        jsonObject.addProperty("platformId", String.valueOf(DynamicResp.getPlatformId()));
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circles/dynamicReply", jsonObject.toString(), new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                try{
                    if(null != tv_send){
                        tv_send.setEnabled(true);
                    }
                }catch (Exception ee){
                }
            }

            @Override
            public void onResponse(String response) {
                try {
                    if(null != tv_send){
                        tv_send.setEnabled(true);
                    }
                    JinZuanChargeBean circleDynamicBean = GsonUtil.getBean(response, JinZuanChargeBean.class);
                    if (circleDynamicBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        //此处展示金钻消耗toast
                        RxToast.showJinzuanCharge(circleDynamicBean);
                        ed_reply.setText("");
                        onRefresh();
                    } else {
                        RxToast.custom(circleDynamicBean.getMsg()).show();
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void userAttentionSuccess(UserAttentionBean bean) {

    }

    @Override
    public void attentionOrUnattentionSuccess(AttentionBean bean, int pos,int type) {
        RxToast.custom("关注成功").show();
        iv_follow.setVisibility(View.GONE);
        isFollow = true;
    }

    @Override
    public void showError(String message) {
        if (RxNetTool.showNetworkUnavailable(message)) {
            showNetworkDialog();
            return;
        }
        RxToast.custom(message).show();
    }

    @Override
    public void complete(String message) {
        showSeverErrorDialog(message);
    }

    /**
     * 举报圈子用户
     */
    private void againstCircleUser(String source,String userId){
        HashMap map=new HashMap();
        map.put("source",source);
        map.put("targetId",userId);
        OKHttpUtil.okHttpPost(Constant.API_BASE_URL + "/against/circleUser",map, new CallBackUtil.CallBackString() {
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
                    BaseBean baseBean=GsonUtil.getBean(response,BaseBean.class);
                    if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                        RxToast.custom("举报成功").show();
                    }else{
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                }catch (Exception e){
//                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void circlePersonSuccess(CirclePersonBean bean) {

    }

    RxDialogSureCancelNew rxDialogSureCancelNew;//二次确认提示
    private void muteWay(int bannedUserId,String circleId){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("禁言");
        rxDialogSureCancelNew.setContent("确定对Ta进行禁言吗？禁言后，Ta将不能再在圈子里发表任何言论。");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                requestMute(4,bannedUserId,circleId);
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }


    private void deleteWay(String json,int type){
        if (null == rxDialogSureCancelNew) {
            rxDialogSureCancelNew = new RxDialogSureCancelNew(this);
        }
        rxDialogSureCancelNew.setButtonText("确定", "取消");
        rxDialogSureCancelNew.setCanceledOnTouchOutside(true);
        rxDialogSureCancelNew.setTitle("删除");
        rxDialogSureCancelNew.setContent("确定删除该内容吗？");
        rxDialogSureCancelNew.setCancel("取消");
        rxDialogSureCancelNew.setSure("确定");
        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
                if(type == 2){
                    requestDeleteOther(json);
                }else if(type == 1){
                    requestDelete(json);
                }
            }
        });
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });
        rxDialogSureCancelNew.show();
    }

    /**
     * 禁言动态
     */
    private void requestMute(int bannedReplyType ,int bannedUserId,String circleId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("bannedReplyType", bannedReplyType);
        jsonObject.addProperty("bannedUserId", bannedUserId);
        jsonObject.addProperty("circleId",circleId );
        OKHttpUtil.okHttpPostJson(Constant.API_BASE_URL + "/circleManage/bannedCircleMember",jsonObject.toString(), new CallBackUtil.CallBackString() {
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
                    ConcernResponse baseBean = GsonUtil.getBean(response, ConcernResponse.class);
                    if (baseBean.getCode() == Constant.ResponseCodeStatus.SUCCESS_CODE) {
                        RxToast.custom("禁言成功").show();
                    } else {
                        RxToast.custom(baseBean.getMsg()).show();
                    }
                } catch (Exception e) {
                    RxLogTool.e(" e:" + e.getMessage());
                }
            }
        });
    }

}
