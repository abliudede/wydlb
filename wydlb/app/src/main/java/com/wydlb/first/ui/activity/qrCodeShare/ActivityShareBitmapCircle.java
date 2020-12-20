package com.wydlb.first.ui.activity.qrCodeShare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.wydlb.first.R;
import com.wydlb.first.base.BaseActivity;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.ShareBitmapBean;
import com.wydlb.first.component.AppComponent;
import com.wydlb.first.utils.CallBackUtil;
import com.wydlb.first.utils.FastBlur;
import com.wydlb.first.utils.GsonUtil;
import com.wydlb.first.utils.OKHttpUtil;
import com.wydlb.first.utils.RxActivityTool;
import com.wydlb.first.utils.RxClipboardTool;
import com.wydlb.first.utils.RxFileTool;
import com.wydlb.first.utils.RxImageTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.SystemBarUtils;
import com.wydlb.first.view.OverlapLogoView;
import com.wydlb.first.view.RxToast;
import com.wydlb.first.view.SelectableRoundedImageView;
import com.wydlb.first.view.dialog.RxDialogBitmapShare;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2019/03/04.
 * 圈子的二维码页面
 */

public class ActivityShareBitmapCircle extends BaseActivity  {


    private String platformId;

    /**
     * 分享弹框
     */
    RxDialogBitmapShare rxDialogShare;

    //中間視圖
    @Bind(R.id.share_rv)
    RelativeLayout share_rv;

    @Bind(R.id.iv_blur_img)
    ImageView iv_blur_img;

    @Bind(R.id.iv_circle_detail_cover)
    SelectableRoundedImageView iv_circle_detail_cover;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.olv_users_logo)
    OverlapLogoView olv_users_logo;

    @Bind(R.id.tv_des)
    TextView tv_des;

    @Bind(R.id.iv_qrcode)
    ImageView iv_qrcode;


    @Bind(R.id.share_btn)
    TextView share_btn;

    @Bind(R.id.save_btn)
    TextView save_btn;


    private Bitmap bitmapshare;
    String shareTempPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER + "/shareTemp.jpg";
    private boolean hasSave = false;
    private ShareBitmapBean.DataBean.CircleInfoBean mdata;

    Bitmap blurBitmap;


    public static void startActivity(Context context, String platformId) {
        Bundle bundle = new Bundle();
        bundle.putString("platformId", platformId);
        RxActivityTool.skipActivity(context, ActivityShareBitmapCircle.class, bundle);
    }

    @Override
    public int getLayoutId() {
            return R.layout.activity_share_bitmap_circle;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != bitmapshare) {
            bitmapshare.recycle();
            bitmapshare = null;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            // 自定义颜色
            tintManager.setTintColor(Color.parseColor("#000000"));
        }

        try{
            platformId = getIntent().getExtras().getString("platformId");
        }catch (Exception e){

        }

        //分享弹窗
        rxDialogShare = new RxDialogBitmapShare(this, R.style.BottomDialogStyle);

        requestPostDetail();

        share_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null == mdata) return;

                        share_btn.setText("正在处理...");
                        showDialog();


                        //没保存时先保存再分享
                        if (!hasSave) {
                            try {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        saveandShareBitmap();
                                    }
                                }, 500);
                            } catch (Exception e) {
                                RxToast.showToast("系统压力很大，请再试一次");
                            }

                        } else {
                            //已保存时直接分享
                            try {
                                share();
                            } catch (Exception e) {
                                RxToast.showToast("系统压力很大，请再试一次");
                            }

                        }
                    }
                }
        );

    }

    /**
     * 接口请求url
     * 类型 1书籍 2书单 3个人主页 4圈子主页 5活动主页 6首页 7登录页 8群聊 9书架
     */
    private void requestPostDetail() {
            Map<String, String> map=new HashMap<>();
            map.put("type",String.valueOf(4));
            map.put("objId",platformId);
            OKHttpUtil.okHttpGet(Constant.API_BASE_URL + "/qrCode/getQrCodeDetailInfo" ,map, new CallBackUtil.CallBackString() {
                @Override
                public void onFailure(Call call, Exception e) {
                    try{
//                        RxToast.custom("加载失败/qrCode/getQrCodeDetailInfo").show();
                        RxLogTool.e("requestHistory e:"+e.getMessage());
                    }catch (Exception ee){

                    }
                }

                @Override
                public void onResponse(String response) {
                    try{
                        ShareBitmapBean baseBean= GsonUtil.getBean(response,ShareBitmapBean.class);
                        if (baseBean.getCode()==Constant.ResponseCodeStatus.SUCCESS_CODE){
                            mdata = baseBean.getData().getCircleInfo();

                            RxClipboardTool.copyText(ActivityShareBitmapCircle.this,baseBean.getData().getExtendField());
                            RxToast.custom("口令已复制到剪切板").show();

                            String platformCover = mdata.getPlatformCover();
                            int errorImgId=RxImageTool.getNoCoverImgByRandom();
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                            requestOptions.placeholder(errorImgId);
                            requestOptions.error(errorImgId);
                            requestOptions.skipMemoryCache(false);
                            requestOptions.dontAnimate();
                            Glide.with(ActivityShareBitmapCircle.this).load(platformCover).apply(requestOptions).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    blurBitmap = RxImageTool.drawable2Bitmap(getResources().getDrawable(errorImgId), 5);
                                    blur(blurBitmap, iv_blur_img);
                                    return false;
                                }
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    RxLogTool.e("onResourceReady:");
                                    blurBitmap = RxImageTool.drawable2Bitmap(resource, 5);
                                    blur(blurBitmap, iv_blur_img);

                                    return false;
                                }
                            }).into(iv_circle_detail_cover);



//                            tv_des.setText(mdata.get);
                            tv_name.setText(mdata.getPlatformName());

                            if (mdata.getPlatformType() == Constant.UserIdentity.OFFICAL_PUBLIC_NUMBER || mdata.getPlatformType() > Constant.UserIdentity.CAN_ATTENTION_NUMBER) {
                                olv_users_logo.setVisibility(View.INVISIBLE);
                                tv_des.setVisibility(View.INVISIBLE);
                            }else {
                                olv_users_logo.setVisibility(View.VISIBLE);
                                tv_des.setVisibility(View.VISIBLE);
                                try {
                                    ArrayList<String> listTemp = new ArrayList<>();
                                    if (null != mdata.getHeadList() && !mdata.getHeadList().isEmpty()) {
                                        listTemp.addAll(mdata.getHeadList());
                                    }
                                    String[] arrString = (String[])listTemp.toArray(new String[listTemp.size()]);
                                    olv_users_logo.initViewData(arrString,"1","0");
                                } catch (Exception e) {
                                }
                                tv_des.setText(String.valueOf(mdata.getMemberCount()) + " 名书友已加入这个圈子");
                            }




                            Bitmap bmpresult = CodeUtils.createImage(baseBean.getData().getUrl(), 400, 400, null);
                            iv_qrcode.setImageBitmap(bmpresult);
                        }else{
                            RxToast.custom(baseBean.getMsg()).show();
                        }

                    }catch (Exception e){
//                        RxToast.custom("加载失败/qrCode/getQrCodeDetailInfo").show();
                        RxLogTool.e("requestHistory e:"+e.getMessage());
                    }
                }
            });
    }

//    /**
//     * 分享图片详情框
//     */
//    private void showShareBarDialog() {
//
//        rxDialogShare.getTv_share_creat().setVisibility(View.GONE);
//        rxDialogShare.getTv_share_copy().setVisibility(View.GONE);
//
//
//        rxDialogShare.getTv_share_wx_circle().setOnClickListener(
//                v -> {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            RxShareUtils.sharePicByBitmap(bitmapshare, BuglyApplicationLike.getsInstance().api, false);
//                        }
//                    }).start();
//                    rxDialogShare.dismiss();
//                }
//        );
//        rxDialogShare.getTv_share_wx().setOnClickListener(
//                v -> {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            RxShareUtils.sharePicByBitmap(bitmapshare, BuglyApplicationLike.getsInstance().api, true);
//                        }
//                    }).start();
//                    rxDialogShare.dismiss();
//                }
//        );
//
//
//        rxDialogShare.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
////                SystemBarUtils.expandNavBar(ActivityBarDetail.this);
////                SystemBarUtils.hideStableNavBar(ActivityBarDetail.this);
//            }
//        });
//
//        rxDialogShare.show();
//    }


    @Override
    public void gc() {
        if (null != bitmapshare) {
            bitmapshare.recycle();
            bitmapshare = null;
        }
        RxToast.gc();
    }


    @Override
    public void initToolBar() {
    }


    @OnClick(R.id.img_back)
    void closeClick() {
        finish();
    }

    @OnClick(R.id.tv_close)
    void tv_closeClick() {
        finish();
    }

    @OnClick(R.id.save_btn)
    void saveClick() {
        if (null == mdata) return;

        try {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                  saveBitmap();
                }
            }, 50);
        } catch (Exception e) {
            RxToast.showToast("系统压力很大，请再试一次");
        }

    }


    private void saveBitmap() {
        if (null == bitmapshare) {
            // 创建相应大小的bitmap
            bitmapshare = Bitmap.createBitmap(share_rv.getMeasuredWidth(), share_rv.getMeasuredHeight(), Bitmap.Config.RGB_565);
            final Canvas canvas = new Canvas(bitmapshare);
            //绘制viewGroup内容
            share_rv.draw(canvas);
        }
        shareTempPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER + "/" + System.currentTimeMillis() + ".jpg";
        //请求权限后再保存图片
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {
                if (RxImageTool.save(bitmapshare, new File(shareTempPath), Bitmap.CompressFormat.PNG)) {
                    hasSave = true;
                    RxImageTool.savePicToMedia(ActivityShareBitmapCircle.this,new File(shareTempPath));
                    RxToast.custom("保存成功").show();
                }
            }
            @Override
            public void noPermission() {

            }
        }, R.string.ask_again, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    private void saveandShareBitmap() {

        if (!hasSave) {
            //bitmapshare为空时就画图
            if (null == bitmapshare) {
                // 创建相应大小的bitmap
                bitmapshare = Bitmap.createBitmap(share_rv.getMeasuredWidth(), share_rv.getMeasuredHeight(), Bitmap.Config.RGB_565);
                final Canvas canvas = new Canvas(bitmapshare);
                //绘制viewGroup内容
                share_rv.draw(canvas);
            }
            shareTempPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER + "/" + System.currentTimeMillis() + ".jpg";
            //请求权限后再保存图片
            checkPermission(new CheckPermListener() {
                @Override
                public void superPermission() {
                    //保存成功后调用分享
                    if (RxImageTool.save(bitmapshare, new File(shareTempPath), Bitmap.CompressFormat.PNG)) {
                        hasSave = true;
                        share();
                    }
                }
                @Override
                public void noPermission() {

                }
            }, R.string.ask_again, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        }
    }

    private void share(){
        //调用系统分享
        File file = new File(shareTempPath);
        Uri imageUri =  RxFileTool.getUriForFile(ActivityShareBitmapCircle.this,file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
        dismissDialog();
        share_btn.setText("分享");
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {
        long startMs = System.currentTimeMillis();
        float radius = 100;//模糊程度
        Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
        view.setImageBitmap(overlay);
        /**
         * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。
         */
        RxLogTool.e("blur time:" + (System.currentTimeMillis() - startMs));
    }

}
