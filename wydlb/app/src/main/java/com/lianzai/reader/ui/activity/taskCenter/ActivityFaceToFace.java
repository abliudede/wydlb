package com.lianzai.reader.ui.activity.taskCenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.ui.activity.PermissionActivity;
import com.lianzai.reader.utils.RxActivityTool;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.RxTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lrz on 2018/10/14.
 * 白色顶部以及状态栏页面
 */

public class ActivityFaceToFace extends PermissionActivity implements WbShareCallback{


    private WbShareHandler shareHandler;
    private Bitmap bitmapshare;
    @Bind(R.id.erweima_iv)
    ImageView erweima_iv;
    @Bind(R.id.share_rv)
    RelativeLayout share_rv;


    private boolean hasSave = false;
    private String shareTempPath = RxFileTool.getRootPath() + Constant.APP_FOLDER + Constant.IMAGES_FOLDER + "/shareTemp.jpg";
    private String url = "";


    public static void startActivity(Context context, String url){
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        RxActivityTool.skipActivity(context,ActivityFaceToFace.class,bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facetoface);
        ButterKnife.bind(this);

        try {
            url=getIntent().getExtras().getString("url");
        }catch (Exception e){

        }

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

        //微博初始化
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);

        Bitmap bmpresult = CodeUtils.createImage(url, 400, 400, null);
        erweima_iv.setImageBitmap(bmpresult);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick(R.id.img_back)void backClick(){
        finish();
    }
    @OnClick(R.id.tv_close)void tv_closeClick(){
        finish();
    }

    @OnClick(R.id.bottom_view_tab1)void bottom_view_tab1Click(){
        //bitmapshare为空时就画图
        if (null == bitmapshare) {
            // 创建相应大小的bitmap
            bitmapshare = Bitmap.createBitmap(share_rv.getMeasuredWidth(), share_rv.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
            final Canvas canvas = new Canvas(bitmapshare);
            //绘制viewGroup内容
            share_rv.draw(canvas);
        }
        RxShareUtils.sharePicByBitmap(bitmapshare, BuglyApplicationLike.getsInstance().api,true);
    }

    @OnClick(R.id.bottom_view_tab4)void bottom_view_tab4Click(){
        //bitmapshare为空时就画图
        if (null == bitmapshare) {
            // 创建相应大小的bitmap
            bitmapshare = Bitmap.createBitmap(share_rv.getMeasuredWidth(), share_rv.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
            final Canvas canvas = new Canvas(bitmapshare);
            //绘制viewGroup内容
            share_rv.draw(canvas);
        }
        RxShareUtils.sharePicByBitmap(bitmapshare,BuglyApplicationLike.getsInstance().api,false);
    }

    @OnClick(R.id.bottom_view_tab2)void bottom_view_tab2Click(){
        //微博按钮点击事件
        //bitmapshare为空时就画图
        if (null == bitmapshare) {
            // 创建相应大小的bitmap
            bitmapshare = Bitmap.createBitmap(share_rv.getMeasuredWidth(), share_rv.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
            final Canvas canvas = new Canvas(bitmapshare);
            //绘制viewGroup内容
            share_rv.draw(canvas);
        }
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
            RxShareUtils.QQShareImg(this,shareTempPath,qqShareListener);

        }

    }

    private void saveandShareBitmap() {
        if (!hasSave) {
            //请求权限后再保存图片
            checkPermission(new CheckPermListener() {
                @Override
                public void superPermission() {
                    //保存成功后调用分享
                    if (RxImageTool.save(bitmapshare, new File(shareTempPath), Bitmap.CompressFormat.PNG)) {
                        hasSave = true;
                        RxShareUtils.QQShareImg(ActivityFaceToFace.this,shareTempPath,qqShareListener);
                    }
                }
                @Override
                public void noPermission() {

                }
            }, R.string.ask_again, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        }
    }


    @OnClick(R.id.bottom_view_tab5)void bottom_view_tab5Click(){
        //微博按钮点击事件
        if (null == bitmapshare) {
            // 创建相应大小的bitmap
            bitmapshare = Bitmap.createBitmap(share_rv.getMeasuredWidth(), share_rv.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
            final Canvas canvas = new Canvas(bitmapshare);
            //绘制viewGroup内容
            share_rv.draw(canvas);
        }
        RxShareUtils.WBShare(shareHandler,this,false,"","","",true,bitmapshare);
    }

    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            RxToast.custom("分享取消").show();
        }
        @Override
        public void onComplete(Object response) {
            RxToast.custom("分享成功").show();
        }
        @Override
        public void onError(UiError e) {
            RxToast.custom("分享失败").show();
        }
    };

    @Override
    public void onWbShareSuccess() {
        RxToast.custom("分享成功").show();
    }

    @Override
    public void onWbShareCancel() {
        RxToast.custom("分享取消").show();
    }

    @Override
    public void onWbShareFail() {
        RxToast.custom("分享失败").show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(null != intent)
        shareHandler.doResultIntent(intent,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != data && null != shareHandler)
        shareHandler.doResultIntent(data,this);
    }

}
