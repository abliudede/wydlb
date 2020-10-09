package com.lianzai.reader.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lianzai.reader.R;
import com.lianzai.reader.base.BaseActivity;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.component.AppComponent;
import com.lianzai.reader.component.DaggerAccountComponent;
import com.lianzai.reader.ui.adapter.ImagesPagerAdapter;
import com.lianzai.reader.ui.contract.DownloadFileContract;
import com.lianzai.reader.ui.presenter.DownloadFilePresenter;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.SystemBarUtils;
import com.lianzai.reader.view.RxToast;
import com.lianzai.reader.view.dialog.RxDialogImageOption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import me.panpf.sketch.SketchImageView;

/**
 * Created by lrz on 2017/10/14.
 * 图片预览
 */

public class ActivityImagesPreview extends BaseActivity implements DownloadFileContract.View{


    @Bind(R.id.viewpager_images)
    ViewPager viewpager_images;

    ImagesPagerAdapter imagesPagerAdapter;

    List<String>imgs=new ArrayList<>();

    int showPosition=0;

    RxDialogImageOption rxDialogImageOption;

    @Inject
    DownloadFilePresenter downloadFilePresenter;

//    @Bind(R.id.ll_dots)
//    LinearLayout ll_dots;//底部光标索引

    @Bind(R.id.tv_page)
    TextView tv_page;

    public static void startActivity(Activity activity, ArrayList<String> imgs,int mShowPosition){

        Intent intent=new Intent(activity,ActivityImagesPreview.class);
        intent.putStringArrayListExtra("imgs",imgs);
        intent.putExtra("position",mShowPosition);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.image_scale_in,0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_preview;
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
        downloadFilePresenter.attachView(this);

        SystemBarUtils.expandStatusBar(this);
        SystemBarUtils.transparentStatusBar(this);

        imgs=getIntent().getStringArrayListExtra("imgs");
        showPosition=getIntent().getIntExtra("position",0);

        if (null==imgs)return;

        if (imgs.size()>1){
            tv_page.setVisibility(View.VISIBLE);
        }else {
            tv_page.setVisibility(View.GONE);
        }
        tv_page.setText((showPosition+1)+"/"+imgs.size());

        rxDialogImageOption=new RxDialogImageOption(this,R.style.BottomDialogStyle);
        rxDialogImageOption.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

//        initDots(showPosition);

        imagesPagerAdapter=new ImagesPagerAdapter(this);
        imagesPagerAdapter.setImages(imgs);
        viewpager_images.setAdapter(imagesPagerAdapter);
        viewpager_images.setCurrentItem(showPosition);
        imagesPagerAdapter.setImageClickListener(new ImagesPagerAdapter.ImageClickListener() {
            @Override
            public void longPressClick(final int pos, SketchImageView largeImageView) {
                rxDialogImageOption.show();
                SystemBarUtils.setStatusBarColor(ActivityImagesPreview.this,R.color.white);
                rxDialogImageOption.getTv_save_pic().setOnClickListener(
                        v->{
                            downloadFilePresenter.downloadFile(imgs.get(pos));
                            showDialog();
                            rxDialogImageOption.dismiss();
                        }
                );
            }

            @Override
            public void clickClick(int pos, SketchImageView largeImageView) {
                finish();
//                overridePendingTransition(0,R.anim.image_scale_out);
            }
        });
        viewpager_images.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public void gc() {

    }
    @Override
    public void initToolBar() {

    }


    private void initDots(int currentPosition){
        if (imgs.size()<=1){
            return;
        }
        if (imgs.size()>1){
            tv_page.setVisibility(View.VISIBLE);
        }else {
            tv_page.setVisibility(View.GONE);
        }
        tv_page.setText((currentPosition+1)+"/"+imgs.size());
//        ll_dots.removeAllViews();
//        int dotSize=RxImageTool.dip2px(7);
//        int marginLeft=RxImageTool.dip2px(5);
//        for(int i=0;i<imgs.size();i++){
//            SelectableRoundedImageView dotImgView=new SelectableRoundedImageView(this);
//            dotImgView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//            dotImgView.setOval(true);
//            if (i==currentPosition){
//                dotImgView.setBackgroundResource(R.drawable.circle_dot_focus);
//            }else{
//                dotImgView.setBackgroundResource(R.drawable.circle_dot_normal);
//            }
//            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(dotSize,dotSize);
//            layoutParams.setMargins(marginLeft,0,0,0);
//            dotImgView.setLayoutParams(layoutParams);
//            ll_dots.addView(dotImgView);
//        }
//        ll_dots.postInvalidate();
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = true;
        try {
            result = super.dispatchTouchEvent(ev);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void downloadFileSuccess(String path) {
            RxImageTool.savePicToMedia(ActivityImagesPreview.this,new File(path));
            RxToast.custom("图片已保存至"+path, Constant.ToastType.TOAST_SUCCESS).show();
            dismissDialog();
    }

    @Override
    public void downloadFileFailed() {
        RxToast.custom("图片保存失败",Constant.ToastType.TOAST_ERROR).show();
        dismissDialog();
    }

    @Override
    public void showError(String message) {
        dismissDialog();
        RxToast.custom("图片保存失败",Constant.ToastType.TOAST_ERROR).show();
    }

    @Override
    public void complete(String message) {
        dismissDialog();
        RxToast.custom("图片保存失败",Constant.ToastType.TOAST_ERROR).show();
    }
}
