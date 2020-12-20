package com.lianzai.reader.ui.activity.qrCodeShare;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.ShareBitmapBean;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.utils.CallBackUtil;
import com.lianzai.reader.utils.GsonUtil;
import com.lianzai.reader.utils.OKHttpUtil;
import com.lianzai.reader.utils.RxClipboardTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxLoginTool;
import com.lianzai.reader.utils.RxSharedPreferencesUtil;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.CircleImageView;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.lianzai.reader.view.dialog.RxDialogBitmapShare;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lrz on 2019/03/04.
 * 书籍的二维码页面
 */

public class ActivityShareBitmapBook extends BaseActivity  {



    private String bookId;

    private String source;

    /**
     * 分享弹框
     */
    RxDialogBitmapShare rxDialogShare;

    //中間視圖
    @Bind(R.id.share_rv)
    RelativeLayout share_rv;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.tv_des)
    TextView tv_des;

    @Bind(R.id.iv_logo)
    CircleImageView iv_logo;

    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;

    @Bind(R.id.tv_book_name)
    TextView tv_book_name;

    @Bind(R.id.tv_book_author)
    TextView tv_book_author;


    @Bind(R.id.iv_qrcode)
    ImageView iv_qrcode;


    @Bind(R.id.share_btn)
    TextView share_btn;

    @Bind(R.id.save_btn)
    TextView save_btn;


    private Bitmap bitmapshare;
    String shareTempPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER + "/shareTemp.jpg";
    private boolean hasSave = false;
    private ShareBitmapBean.DataBean.BookInfoBean mdata;


    public static void startActivity(Context context, String bookId,String source) {
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        bundle.putString("source", source);
        RxActivityTool.skipActivity(context, ActivityShareBitmapBook.class, bundle);
    }

    @Override
    public int getLayoutId() {
            return R.layout.activity_share_bitmap_book;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAccountComponent.builder().appComponent(appComponent).build().inject(this);
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
            bookId = getIntent().getExtras().getString("bookId");
            source = getIntent().getExtras().getString("source");
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
            map.put("type",String.valueOf(1));
            map.put("objId",bookId);
        //优先用书评传进来的source
        if(!TextUtils.isEmpty(source)){
            map.put("ext", source);
        }else {
            //获取本地存储的source
            String localSource = RxSharedPreferencesUtil.getInstance().getString(bookId + "_source");
            if(!TextUtils.isEmpty(localSource)){
                map.put("ext", localSource);
            }
        }
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
                            mdata = baseBean.getData().getBookInfo();
                            if(RxLoginTool.isLogin() && null != RxTool.getAccountBean()  && null != RxTool.getAccountBean().getData()) {
                                String nickName = RxTool.getAccountBean().getData().getNickName();
                                String userLogo = RxTool.getAccountBean().getData().getAvatar();
                                tv_name.setText(nickName);
                                RxImageTool.loadLogoImage(ActivityShareBitmapBook.this,userLogo,iv_logo);
                            }
                            RxClipboardTool.copyText(ActivityShareBitmapBook.this,baseBean.getData().getExtendField());
                            RxToast.custom("口令已复制到剪切板").show();
//                            tv_des.setText(mdata.get);
                            RxImageTool.loadBookStoreImage(ActivityShareBitmapBook.this,mdata.getCover(),iv_book_cover);
                            tv_book_name.setText("《" + mdata.getBookName() +"》");
                            tv_book_author.setText(mdata.getAuthorName() + "著");
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
                    RxImageTool.savePicToMedia(ActivityShareBitmapBook.this,new File(shareTempPath));
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
        Uri imageUri =  RxFileTool.getUriForFile(ActivityShareBitmapBook.this,file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "分享图片"));
        dismissDialog();
        share_btn.setText("分享");
    }

}
