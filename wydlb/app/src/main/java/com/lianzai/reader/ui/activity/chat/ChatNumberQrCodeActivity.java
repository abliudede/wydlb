package com.lianzai.reader.ui.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.FileProvider;
import android.text.Html;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.BuglyApplicationLike;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxShareUtils;
import com.lianzai.reader.utils.ScreenUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.SelectableRoundedImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 连载号二维码
 */
public class ChatNumberQrCodeActivity extends BaseActivity {

    @Bind(R.id.iv_qr_code)
    ImageView iv_qr_code;

    Bitmap logoBitmap;

    Bitmap qrBitmap;

    Bitmap shareBitmap;

    @Bind(R.id.tv_tip)
    TextView tv_tip;

    @Bind(R.id.tv_book_other)
    TextView tv_book_other;

    @Bind(R.id.tv_book_title)
    TextView tv_book_title;

    @Bind(R.id.iv_book_cover)
    SelectableRoundedImageView iv_book_cover;

    @Bind(R.id.rl_content_view)
    RelativeLayout rl_content_view;



    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChatNumberQrCodeActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode;
    }


    @Override
    public void initToolBar() {
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void configViews(Bundle savedInstanceState) {
        logoBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        qrBitmap=CodeUtils.createImage("连载号地址", ScreenUtils.dpToPx(190),ScreenUtils.dpToPx(190),logoBitmap);
        iv_qr_code.setImageBitmap(qrBitmap);

        tv_tip.setText(Html.fromHtml("用<font color='#3865FE'>「连载神器」</font>扫码开始阅读"));

        tv_book_other.setText("连载熊 | 共100个站点来源");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shareBitmap=RxImageTool.loadBitmapFromView(rl_content_view);
            }
        },500);//延迟1秒后生成图片
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }


    @OnClick(R.id.img_back)
    void finishClick() {
        backClick();
    }


    @Override
    public void gc() {

        if (null!=logoBitmap){
            logoBitmap.recycle();
            logoBitmap=null;
        }
        if (null!=qrBitmap){
            qrBitmap.recycle();
            qrBitmap=null;
        }
        if (null!=shareBitmap){
            shareBitmap.recycle();
            shareBitmap=null;
        }
    }


    /**
     * 微信
     */
    @OnClick(R.id.iv_wx)void wxClick(){
        if (null==shareBitmap)return;
                RxShareUtils.shareImage(shareBitmap, BuglyApplicationLike.getsInstance().api,true);
    }


    /**
     * 微信朋友
     */
    @OnClick(R.id.iv_wx_friend)void wxFriendsClick(){
        if (null==shareBitmap)return;
                RxShareUtils.shareImage(shareBitmap, BuglyApplicationLike.getsInstance().api,false);
    }


    /**
     * 保存图片
     */
    @OnClick(R.id.iv_save)void savePicClick(){
        if (null==shareBitmap)return;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try{
                    String dirPath= RxFileTool.getRootPath()+ Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
                    RxFileTool.createOrExistsDir(dirPath);
                    String filePath=dirPath+"/"+System.currentTimeMillis()+".png";
                    String path=RxImageTool.savePhotoToSD(filePath,shareBitmap, BuglyApplicationLike.getContext());
                    RxImageTool.savePicToMedia(ChatNumberQrCodeActivity.this,new File(path));
                    RxToast.custom("保存成功").show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}
