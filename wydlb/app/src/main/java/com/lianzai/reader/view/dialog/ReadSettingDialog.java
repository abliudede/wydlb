package com.lianzai.reader.view.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.EasyPermissions;
import com.lianzai.reader.interfaces.OnRepeatClickListener;
import com.lianzai.reader.model.local.ReadSettingManager;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.read.ReadMoreSettingsActivity;
import com.lianzai.reader.ui.adapter.PageStyleAdapter;
import com.lianzai.reader.utils.BrightnessUtils;
import com.lianzai.reader.utils.ProtectedEyeUtils;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxPermissionsTool;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.page.PageLoader;
import com.lianzai.reader.view.page.PageMode;
import com.lianzai.reader.view.page.PageStyle;

import java.lang.reflect.Method;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class ReadSettingDialog extends Dialog {
    private static final String TAG = "ReadSettingDialog";
    private static final int DEFAULT_TEXT_SIZE = 18;

    @Bind(R.id.read_setting_sb_brightness)
    SeekBar mSbBrightness;
    @Bind(R.id.read_setting_cb_brightness_auto)
    CheckBox mCbBrightnessAuto;
    @Bind(R.id.read_setting_tv_font_minus)
    TextView mTvFontMinus;
    @Bind(R.id.read_setting_tv_font)
    TextView mTvFont;
    @Bind(R.id.read_setting_tv_font_plus)
    TextView mTvFontPlus;
    @Bind(R.id.read_setting_rg_page_mode)
    RadioGroup mRgPageMode;

    @Bind(R.id.read_setting_rb_simulation)
    RadioButton mRbSimulation;
    @Bind(R.id.read_setting_rb_cover)
    RadioButton mRbCover;
    @Bind(R.id.read_setting_rb_slide)
    RadioButton mRbSlide;
    @Bind(R.id.read_setting_rb_scroll)
    RadioButton mRbScroll;
    @Bind(R.id.read_setting_rb_none)
    RadioButton mRbNone;
    @Bind(R.id.read_setting_rv_bg)
    RecyclerView mRvBg;
    @Bind(R.id.read_setting_tv_auto)
    TextView read_setting_tv_auto;
    @Bind(R.id.read_setting_tv_more)
    TextView mTvMore;

    @Bind(R.id.read_setting_rg_font_style)//字体
    RadioGroup read_setting_rg_font_style;

    int mFontStyle;//字体样式

    @Bind(R.id.rl_show_bg)
    RelativeLayout rl_show_bg;//选中背景时候顶部需显示下


    @Bind(R.id.iv_bg_show)
    ImageView iv_bg_show;//预览背景图

    @Bind(R.id.iv_bg_name)//背景图名字
    ImageView iv_bg_name;

    @Bind(R.id.read_setting_cb_font_default)
    CheckBox mCbFontDefault;

    @Bind(R.id.cb_read_setting_protected_eye)//护眼模式
    CheckBox cb_read_setting_protected_eye;

    int mTextPaddingType;//当前的间距模式

    //边距
    @Bind(R.id.rg_padding)
    RadioGroup rg_padding;


    @Bind(R.id.read_setting_ll_menu)
    LinearLayout read_setting_ll_menu;

    @Bind(R.id.read_setting_rb_font_normal)
    RadioButton read_setting_rb_font_normal;

    @Bind(R.id.read_setting_rb_font_kaiti)
    RadioButton read_setting_rb_font_kaiti;

    @Bind(R.id.read_setting_rb_font_songti)
    RadioButton read_setting_rb_font_songti;


    @Bind(R.id.read_setting_rb_font_heiti)
    RadioButton read_setting_rb_font_heiti;

    @Bind(R.id.read_setting_rb_font_padding_small)
    RadioButton read_setting_rb_font_padding_small;

    @Bind(R.id.read_setting_rb_font_padding_default)
    RadioButton read_setting_rb_font_padding_default;

    @Bind(R.id.read_setting_rb_font_padding_medium)
    RadioButton read_setting_rb_font_padding_medium;

    @Bind(R.id.read_setting_rb_font_padding_big)
    RadioButton read_setting_rb_font_padding_big;



    /************************************/
    private PageStyleAdapter mPageStyleAdapter;
    private ReadSettingManager mSettingManager;
    private Activity mActivity;

    private PageMode mPageMode;
    private PageStyle mPageStyle;

    private int mBrightness;
    private int mTextSize;

    private boolean isBrightnessAuto;
    private boolean isOpenProtectedEye;
    private boolean isTextDefault;

    boolean isNightMode=false;

    LinearLayoutManager linearLayoutManager;

    RxDialogSureCancelNew rxDialogSureCancelNew;//护眼模式权限未打开的时候提示框

    ReadSettingCallback readSettingCallback;


    public ReadSettingDialog(@NonNull Activity activity) {
        super(activity, R.style.ReadSettingDialog);
        mActivity = activity;
    }

    public ReadSettingCallback getReadSettingCallback() {
        return readSettingCallback;
    }

    public void setReadSettingCallback(ReadSettingCallback readSettingCallback) {
        this.readSettingCallback = readSettingCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_setting);
        ButterKnife.bind(this);
        setUpWindow();
        initData();
        initWidget();
        initClick();
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }

    private void initData() {
        mSettingManager = ReadSettingManager.getInstance();


        isBrightnessAuto = mSettingManager.isBrightnessAuto();
        mBrightness = mSettingManager.getBrightness();
        isOpenProtectedEye=mSettingManager.isOpenProtectedEye();//护眼模式
        mTextSize = mSettingManager.getTextSize();
        isTextDefault = mSettingManager.isDefaultTextSize();
        mPageMode = mSettingManager.getPageMode();
        mPageStyle = mSettingManager.getPageStyle();
        mTextPaddingType=mSettingManager.getTextPaddingStyle();

        mFontStyle=mSettingManager.getFontStyle();
    }

    private void initWidget() {
        mSbBrightness.setProgress(mBrightness);
        mTvFont.setText(mTextSize + "");
        mCbFontDefault.setChecked(isTextDefault);

        mCbBrightnessAuto.setChecked(isBrightnessAuto);
        mCbBrightnessAuto.setCompoundDrawables(isBrightnessAuto?RxImageTool.getDrawable(15,R.mipmap.icon_selected):RxImageTool.getDrawable(15,R.mipmap.icon_not_selected),null,null,null);

        mCbFontDefault.setCompoundDrawables(isTextDefault?RxImageTool.getDrawable(15,R.mipmap.icon_selected):RxImageTool.getDrawable(15,R.mipmap.icon_not_selected),null,null,null);

        mTextPaddingType=ReadSettingManager.getInstance().getTextPaddingStyle();
        getTextPaddingStyle(mTextPaddingType);

        initPageMode();
        //RecyclerView
        setUpAdapter();

        changeNightMode();


        //字体设置获取
        if (mFontStyle==Constant.ReadFontStyle.KAITI_FONT){
            read_setting_rg_font_style.check(R.id.read_setting_rb_font_kaiti);
        }else if (mFontStyle==Constant.ReadFontStyle.SONGTI_FONT){
            read_setting_rg_font_style.check(R.id.read_setting_rb_font_songti);
        }else if (mFontStyle==Constant.ReadFontStyle.HEITI_FONT){
            read_setting_rg_font_style.check(R.id.read_setting_rb_font_heiti);
        }else if (mFontStyle==Constant.ReadFontStyle.SYSTEM_FONT){
            read_setting_rg_font_style.check(R.id.read_setting_rb_font_normal);
        }

        cb_read_setting_protected_eye.setChecked(isOpenProtectedEye);
    }

    public void changeNightMode(){
        float alpha;
        if (null!=cb_read_setting_protected_eye){
            iv_bg_show.setVisibility(View.INVISIBLE);
            iv_bg_name.setVisibility(View.INVISIBLE);
            isNightMode= ReadSettingManager.getInstance().isNightMode();
            int textColor=mActivity.getResources().getColor(R.color.read_night_text_color);
            int bgColor=mActivity.getResources().getColor(R.color.read_night_bg_color);
            if (isNightMode){
                read_setting_ll_menu.setBackgroundColor(bgColor);
                cb_read_setting_protected_eye.setTextColor(textColor);
                mTvMore.setTextColor(textColor);
                read_setting_tv_auto.setTextColor(textColor);
                cb_read_setting_protected_eye.setCompoundDrawables(null, RxImageTool.getDrawable(23,R.drawable.cb_open_protected_eye_drawable_white),null,null);
                mTvMore.setCompoundDrawables(null, RxImageTool.getDrawable(23,R.mipmap.icon_read_setting_more_white),null,null);
                read_setting_tv_auto.setCompoundDrawables(null, RxImageTool.getDrawable(23,R.mipmap.white_auto_page),null,null);
                //透明度设置
                alpha=0.5f;

            }else{
                bgColor=mActivity.getResources().getColor(R.color.white);
                textColor=mActivity.getResources().getColor(R.color.color_black_ff666666);
                cb_read_setting_protected_eye.setTextColor(textColor);
                mTvMore.setTextColor(textColor);
                read_setting_tv_auto.setTextColor(textColor);
                cb_read_setting_protected_eye.setCompoundDrawables(null, RxImageTool.getDrawable(23,R.drawable.cb_open_protected_eye_drawable),null,null);
                mTvMore.setCompoundDrawables(null, RxImageTool.getDrawable(23,R.mipmap.icon_read_setting_more_black),null,null);
                read_setting_tv_auto.setCompoundDrawables(null, RxImageTool.getDrawable(23,R.mipmap.black_auto_page),null,null);
                read_setting_ll_menu.setBackgroundColor(bgColor);
                alpha=1f;
            }

            //透明度设置
            mSbBrightness.setAlpha(alpha);
            mCbBrightnessAuto.setAlpha(alpha);
            mCbFontDefault.setAlpha(alpha);

            read_setting_rb_font_songti.setAlpha(alpha);
            read_setting_rb_font_normal.setAlpha(alpha);
            read_setting_rb_font_kaiti.setAlpha(alpha);
            read_setting_rb_font_heiti.setAlpha(alpha);

            read_setting_rb_font_songti.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_font_normal.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_font_kaiti.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_font_heiti.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));


            read_setting_rb_font_padding_small.setAlpha(alpha);
            read_setting_rb_font_padding_big.setAlpha(alpha);
            read_setting_rb_font_padding_default.setAlpha(alpha);
            read_setting_rb_font_padding_medium.setAlpha(alpha);


            read_setting_rb_font_padding_small.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_font_padding_big.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_font_padding_default.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            read_setting_rb_font_padding_medium.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));

            mRbSimulation.setAlpha(alpha);
            mRbCover.setAlpha(alpha);
            mRbSlide.setAlpha(alpha);
            mRbNone.setAlpha(alpha);

            mRbSimulation.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            mRbCover.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            mRbSlide.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            mRbNone.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));

            mRvBg.setAlpha(alpha);
            mTvFontMinus.setAlpha(alpha);
            mTvFontPlus.setAlpha(alpha);

            mTvFontPlus.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
            mTvFontMinus.setBackground(isNightMode?mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting_night):mActivity.getResources().getDrawable(R.drawable.selector_btn_read_setting));
        }
    }

    private void setUpAdapter() {
        Drawable[] drawables = {getDrawable(R.drawable.read_bg1),getDrawable(R.drawable.read_bg2),getDrawable(R.drawable.read_bg_paper0),
                getDrawable(R.drawable.read_bg7)
                , getDrawable(R.drawable.read_bg4)
                , getDrawable(R.drawable.read_bg6)
                , getDrawable(R.drawable.read_bg5)
                , getDrawable(R.drawable.read_bg3)};//,getDrawable(R.drawable.read_bg_paper2),getDrawable(R.drawable.read_bg_paper1)

        mPageStyleAdapter = new PageStyleAdapter();
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRvBg.setLayoutManager(linearLayoutManager);
        mRvBg.setAdapter(mPageStyleAdapter);
        mPageStyleAdapter.refreshItems(Arrays.asList(drawables));

        mPageStyleAdapter.setPageStyleChecked(mPageStyle);

    }

    private void initPageMode() {
        switch (mPageMode) {
            case SIMULATION:
                mRbSimulation.setChecked(true);
                break;
            case COVER:
                mRbCover.setChecked(true);
                break;
            case SLIDE:
                mRbSlide.setChecked(true);
                break;
            case NONE:
                mRbNone.setChecked(true);
                break;
            case SCROLL:
                mRbScroll.setChecked(true);
                break;
        }
    }

    private Drawable getDrawable(int drawRes) {
        return ContextCompat.getDrawable(getContext(), drawRes);
    }

    private void initClick() {
        //亮度调节

        mSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mCbBrightnessAuto.isChecked()) {
                    mCbBrightnessAuto.setChecked(false);
                }
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(mActivity, progress);
                //存储亮度的进度条
                ReadSettingManager.getInstance().setBrightness(progress);
            }
        });

        mCbBrightnessAuto.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        //获取屏幕的亮度
                        BrightnessUtils.setBrightness(mActivity, BrightnessUtils.getScreenBrightness(mActivity));

                    } else {
                        //获取进度条的亮度
                        BrightnessUtils.setBrightness(mActivity, mSbBrightness.getProgress());
                    }
                    ReadSettingManager.getInstance().setAutoBrightness(isChecked);
                    mCbBrightnessAuto.setCompoundDrawables(isChecked?RxImageTool.getDrawable(15,R.mipmap.icon_selected):RxImageTool.getDrawable(15,R.mipmap.icon_not_selected),null,null,null);
                }
        );

        //字体大小调节
        mTvFontMinus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) - 1;
                    if (fontSize < 25) return;
                    mTvFont.setText(fontSize + "");
                    if(mActivity instanceof UrlReadActivity){
                        PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                        if(null != mPageLoader)
                        mPageLoader.setTextSize(fontSize);
                    }

                }
        );

        mTvFontPlus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) + 1;
                    if (fontSize>100)return;
                    mTvFont.setText(fontSize + "");
                    PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                    if(null != mPageLoader)
                    mPageLoader.setTextSize(fontSize);
                }
        );

        mCbFontDefault.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        int fontSize = ScreenUtils.dpToPx(DEFAULT_TEXT_SIZE);
                        mTvFont.setText(fontSize + "");
                        PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                        if(null != mPageLoader)
                        mPageLoader.setTextSize(fontSize);
                    }

                    ReadSettingManager.getInstance().setDefaultTextSize(isChecked);

                    mCbFontDefault.setCompoundDrawables(isChecked?RxImageTool.getDrawable(15,R.mipmap.icon_selected):RxImageTool.getDrawable(15,R.mipmap.icon_not_selected),null,null,null);
                }
        );


        //Page Mode 切换
        mRgPageMode.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    PageMode pageMode;
                    switch (checkedId) {
                        case R.id.read_setting_rb_simulation:
                            pageMode = PageMode.SIMULATION;
                            break;
                        case R.id.read_setting_rb_cover:
                            pageMode = PageMode.COVER;
                            break;
                        case R.id.read_setting_rb_slide:
                            pageMode = PageMode.SLIDE;
                            break;
                        case R.id.read_setting_rb_scroll:
                            pageMode = PageMode.SCROLL;
                            break;
                        case R.id.read_setting_rb_none:
                            pageMode = PageMode.NONE;
                            break;
                        default:
                            pageMode = PageMode.SIMULATION;
                            break;
                    }
                    PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                    if(null != mPageLoader)
                        mPageLoader.setPageMode(pageMode);
                }
        );

        //边距设置
        rg_padding.setOnCheckedChangeListener(
                (group, i) -> {
                    if (i==R.id.read_setting_rb_font_padding_small){
                        mTextPaddingType=Constant.ReadPaddingStyle.PADDING_SMALL;
                    } else if (i == R.id.read_setting_rb_font_padding_default) {
                        mTextPaddingType=Constant.ReadPaddingStyle.PADDING_DEFAULT;
                    }else if(i==R.id.read_setting_rb_font_padding_medium){
                        mTextPaddingType=Constant.ReadPaddingStyle.PADDING_MEDIUM;
                    }else if(i==R.id.read_setting_rb_font_padding_big){
                        mTextPaddingType=Constant.ReadPaddingStyle.PADDING_BIG;
                    }

                    ReadSettingManager.getInstance().setTextPaddingStyle(mTextPaddingType);
                    //边距设置
                    PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                    if(null != mPageLoader)
                    mPageLoader.setPadding(mTextPaddingType);
                }
        );

        //背景的点击事件
        mPageStyleAdapter.setOnItemClickListener(
                (view, pos) ->{
                    PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                    if(null != mPageLoader)
                        mPageLoader.setPageStyle(PageStyle.values()[pos]);

                    if (isNightMode){
                        isNightMode=false;
                        ReadSettingManager.getInstance().setNightMode(isNightMode);
                        changeNightMode();
                    }

                    if (null!=getReadSettingCallback()){
                        getReadSettingCallback().changeReadBackgroundClick();

                    }

                    SystemBarUtils.transparentStatusBar(mActivity);

                    //预览图显示
                    rl_show_bg.setVisibility(View.VISIBLE);
                    iv_bg_show.setVisibility(View.VISIBLE);
                    iv_bg_name.setVisibility(View.VISIBLE);


                    switch (pos){
                        case 0:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_yuebai);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_1));
                            break;
                        case 1:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_xiangyabai);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_2));
                            break;
                        case 2:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name1);
                            iv_bg_show.setBackgroundResource(R.drawable.bg1);
                            break;
                        case 3:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_anhei);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_7));
                            break;
                        case 4:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_bolv);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_4));
                            break;
                        case 5:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_mhs);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_6));
                            break;

                        case 6:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_niaozise);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_5));
                            break;

                        case 7:
                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name_bozi);
                            iv_bg_show.setBackgroundColor(mActivity.getResources().getColor(R.color.nb_read_bg_3));
                            break;

//                        case 8:
//                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name3);
//                            iv_bg_show.setBackgroundResource(R.drawable.bg3);
//                            break;
//
//                        case 9:
//                            iv_bg_name.setImageResource(R.mipmap.icon_read_bg_name2);
//                            iv_bg_show.setBackgroundResource(R.drawable.bg2);
//                            break;

                    }
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_bg_name.setVisibility(View.INVISIBLE);
                            iv_bg_show.setVisibility(View.INVISIBLE);
                        }
                    },1000);
                }
        );

        //更多设置
        mTvMore.setOnClickListener(
                (v) -> {
                    ReadMoreSettingsActivity.startActivity(mActivity);
                    dismiss();
                }
        );

        //字体设置
        read_setting_rg_font_style.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.read_setting_rb_font_normal){
                    mFontStyle=Constant.ReadFontStyle.SYSTEM_FONT;
                }else if(i==R.id.read_setting_rb_font_kaiti){
                    mFontStyle=Constant.ReadFontStyle.KAITI_FONT;
                }else if(i==R.id.read_setting_rb_font_songti){
                    mFontStyle=Constant.ReadFontStyle.SONGTI_FONT;
                }else if(i==R.id.read_setting_rb_font_heiti){
                    mFontStyle=Constant.ReadFontStyle.HEITI_FONT;
                }
                ReadSettingManager.getInstance().setFontStyle(mFontStyle);

                PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
                if(null != mPageLoader)
                mPageLoader.setFontStyle(mFontStyle);
            }
        });

    }

    /**
     * 护眼模式设置
     * @param isCheck
     */
    @OnCheckedChanged(R.id.cb_read_setting_protected_eye)void isOpenProtectedEyeClick(boolean isCheck){

        if (ProtectedEyeUtils.checkAlertWindowsPermission(mActivity)){
            if (isCheck){
                mSettingManager.setOpenProtectedEye(true);
                ProtectedEyeUtils.openProtectedEyeMode(mActivity);
//                RxToast.custom("阅读护眼模式已开启",Constant.ToastType.TOAST_SUCCESS).show();
            }else{
//                RxToast.custom("阅读护眼模式已关闭",Constant.ToastType.TOAST_SUCCESS).show();
                ProtectedEyeUtils.closeProtectedEyeMode(mActivity);
                mSettingManager.setOpenProtectedEye(false);
            }
        }else{
            cb_read_setting_protected_eye.setChecked(!isCheck);
            showOpenPermissionAlertDialog();
        }

    }

    public boolean isBrightFollowSystem() {
        if (mCbBrightnessAuto == null) {
            return false;
        }
        return mCbBrightnessAuto.isChecked();
    }


    private void getTextPaddingStyle(int textPaddingType){

        if (textPaddingType==Constant.ReadPaddingStyle.PADDING_SMALL){
            rg_padding.check(R.id.read_setting_rb_font_padding_small);
        }else if (textPaddingType==Constant.ReadPaddingStyle.PADDING_MEDIUM){
            rg_padding.check(R.id.read_setting_rb_font_padding_medium);
        }else if (textPaddingType==Constant.ReadPaddingStyle.PADDING_BIG){
            rg_padding.check(R.id.read_setting_rb_font_padding_big);
        }else if (textPaddingType==Constant.ReadPaddingStyle.PADDING_DEFAULT){
            rg_padding.check(R.id.read_setting_rb_font_padding_default);
        }

        if (textPaddingType!=mTextPaddingType){
            ReadSettingManager.getInstance().setTextPaddingStyle(textPaddingType);

            //边距设置
            PageLoader mPageLoader = ((UrlReadActivity) mActivity).getmPageLoader();
            if(null != mPageLoader)
            mPageLoader.setPadding(textPaddingType);
        }

        mTextPaddingType=textPaddingType;

    }


    @OnClick(R.id.rl_show_bg)void exitDialogClick(){
        dismiss();
    }


    private void showOpenPermissionAlertDialog(){
        if (null==rxDialogSureCancelNew){
            rxDialogSureCancelNew=new RxDialogSureCancelNew(mActivity);
        }
        rxDialogSureCancelNew.setTitle("提示");
        rxDialogSureCancelNew.setContent("护眼模式需要您同意在其他应用上层显示,请开启该权限");
        rxDialogSureCancelNew.setButtonText("去开启","取消");
        rxDialogSureCancelNew.setCancelListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                rxDialogSureCancelNew.dismiss();
            }
        });

        rxDialogSureCancelNew.setSureListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                try{
                    rxDialogSureCancelNew.dismiss();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + mActivity.getPackageName()));
                    mActivity.startActivityForResult(intent, 1001);//这个CODE可以不接受，因为每次都会检查该权限
                }catch (Exception e){

                }
            }
        });
        rxDialogSureCancelNew.show();
    }


    public interface ReadSettingCallback{
        void changeReadBackgroundClick();
    }

    public TextView getRead_setting_tv_auto() {
        return read_setting_tv_auto;
    }

    public void setRead_setting_tv_auto(TextView read_setting_tv_auto) {
        this.read_setting_tv_auto = read_setting_tv_auto;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
//        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//    }

}
