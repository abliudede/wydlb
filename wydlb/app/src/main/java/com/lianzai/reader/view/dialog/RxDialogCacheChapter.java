package com.lianzai.reader.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lianzai.reader.R;


public class RxDialogCacheChapter extends RxDialog {

    private TextView tv_cache_50;
    private TextView tv_cache_last_all;
    private TextView tv_cache_all;
    private TextView tv_cancel;

    private RelativeLayout rl_dialog_parent;

    CacheChapterCallback cacheChapterCallback;


    public RxDialogCacheChapter(Context context) {
        super(context,R.style.BottomDialogStyle);
        initView();
    }

    public CacheChapterCallback getCacheChapterCallback() {
        return cacheChapterCallback;
    }

    public void setCacheChapterCallback(CacheChapterCallback cacheChapterCallback) {
        this.cacheChapterCallback = cacheChapterCallback;
    }

    public RxDialogCacheChapter(Activity context) {
        super(context,R.style.ReadBottomDialogStyle);
        initView();
    }


    private void initView() {
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cache_chapter, null);
        tv_cache_50=dialogView.findViewById(R.id.tv_cache_50);
        tv_cache_last_all = dialogView.findViewById(R.id.tv_cache_last_all);
        tv_cache_all =  dialogView.findViewById(R.id.tv_cache_all);
        tv_cancel = dialogView.findViewById(R.id.tv_cancel);

        rl_dialog_parent=dialogView.findViewById(R.id.rl_dialog_parent);
        setCanceledOnTouchOutside(false);
        setContentView(dialogView);
        tv_cancel.setOnClickListener(
                v->dismiss()
        );

        tv_cache_50.setOnClickListener(
                v->{
                    getCacheChapterCallback().cacheChapter(0);
                    dismiss();
                }
        );
        tv_cache_last_all.setOnClickListener(
                v->{
                    getCacheChapterCallback().cacheChapter(1);
                    dismiss();
                }
        );
        tv_cache_all.setOnClickListener(
                v->{
                    getCacheChapterCallback().cacheChapter(2);
                    dismiss();
                }
        );

        rl_dialog_parent.setOnClickListener(
                v->dismiss()
        );


        setFullScreen();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//                ;
//        this.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//    }

    public interface CacheChapterCallback{
        public void cacheChapter(int type);
    }

}
