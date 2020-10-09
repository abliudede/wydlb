package com.lianzai.reader.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
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
import com.lianzai.reader.R;
import com.lianzai.reader.bean.BookShortAgeArtifact;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.utils.FastBlur;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;

public class ItemRecommend extends RelativeLayout{

	private Context context;

	public ItemRecommend(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public ItemRecommend(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public ItemRecommend(Context context) {
		super(context);
		this.context = context;
		initView();
	}

    SelectableRoundedImageView iv_quanzi_bg;
	TextView tv_quanzi_title;
    SelectableRoundedImageView book_cover;
	TextView tv1;
	TextView tv2;
	TextView tv3;
//	TextView tv4;

	protected void initView() {
		inflate(getContext(), R.layout.item_recommend_home, this);
        iv_quanzi_bg = findViewById(R.id.iv_quanzi_bg);
        tv_quanzi_title = findViewById(R.id.tv_quanzi_title);
        book_cover = findViewById(R.id.book_cover);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
//        tv4 = findViewById(R.id.tv4);
	}

	public void bindData(BookShortAgeArtifact.DataBean bean) {
        //模糊背景以及加载
        int errorImgId=RxImageTool.getNoCoverImgByRandom();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.placeholder(errorImgId);
        requestOptions.error(errorImgId);
        requestOptions.skipMemoryCache(false);
        requestOptions.dontAnimate();
        Glide.with(getContext()).load(bean.getBookCover()).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Bitmap blurBitmap = RxImageTool.drawable2Bitmap(getResources().getDrawable(errorImgId), 5);
                blur(blurBitmap, iv_quanzi_bg);
                return false;
            }
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                RxLogTool.e("onResourceReady:");
                Bitmap blurBitmap = RxImageTool.drawable2Bitmap(resource, 5);
                blur(blurBitmap, iv_quanzi_bg);
                return false;
            }
        }).into(book_cover);

        tv_quanzi_title.setText("根据《" + bean.getBookName() + "》为你推荐以下书籍");
        if(null != bean.getRecommendBookName() && !bean.getRecommendBookName().isEmpty()){
           for(int i = 0 ; i < 3 && i < bean.getRecommendBookName().size(); i ++){
               String temp = bean.getRecommendBookName().get(i);
               switch (i){
                   case 0:
                       tv1.setText("《" + temp +"》");
                       break;
                   case 1:
                       tv2.setText("《" + temp +"》");
                       break;
                   case 2:
                       tv3.setText("《" + temp +"》");
                       break;
               }
           }
        }

        setOnClickListener(
                v -> {
                    //跳搜索页面的书荒神器tab
                    if(!TextUtils.isEmpty(bean.getBookName())){
                        ActivitySearchFirst.skiptoSearch2(bean.getBookName(),getContext());
                    }
                }
        );

        tv1.setOnClickListener(
                v -> {
                    if(null == bean){
                        return;
                    }
                    if(null == bean.getRecommendBookName()){
                        return;
                    }
                    if(bean.getRecommendBookName().size() < 1){
                        return;
                    }
                    //跳搜索页面
                    if(!TextUtils.isEmpty(bean.getRecommendBookName().get(0))){
                        ActivitySearchFirst.skiptoSearch(bean.getRecommendBookName().get(0),getContext());
                    }

                }
        );

        tv2.setOnClickListener(
                v -> {
                    if(null == bean){
                        return;
                    }
                    if(null == bean.getRecommendBookName()){
                        return;
                    }
                    if(bean.getRecommendBookName().size() < 2){
                        return;
                    }
                    //跳搜索页面
                    if(!TextUtils.isEmpty(bean.getRecommendBookName().get(1))){
                        ActivitySearchFirst.skiptoSearch(bean.getRecommendBookName().get(1),getContext());
                    }
                }
        );

        tv3.setOnClickListener(
                v -> {
                    if(null == bean){
                        return;
                    }
                    if(null == bean.getRecommendBookName()){
                        return;
                    }
                    if(bean.getRecommendBookName().size() < 3){
                        return;
                    }
                    //跳搜索页面
                    if(!TextUtils.isEmpty(bean.getRecommendBookName().get(2))){
                        ActivitySearchFirst.skiptoSearch(bean.getRecommendBookName().get(2),getContext());
                    }
                }
        );

	}

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, ImageView view) {
//        new Thread() {
//            @Override
//            public void run() {
//                //需要在子线程中处理的逻辑
//
//            }
//        }.start();

        new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                float radius = 40;//模糊程度
                Bitmap overlay = FastBlur.doBlur(bkg, (int) radius, true);
                view.setImageBitmap(overlay);
            }
        },getResources().getInteger(R.integer.refresh_delay));

    }


}

