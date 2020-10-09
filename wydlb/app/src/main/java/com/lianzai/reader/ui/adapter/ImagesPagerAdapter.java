package com.lianzai.reader.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lianzai.reader.R;
import com.lianzai.reader.utils.RxImageTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.loadding.LoadingView;

import java.util.ArrayList;
import java.util.List;

import me.panpf.sketch.SketchImageView;
import me.panpf.sketch.decode.ImageAttrs;
import me.panpf.sketch.request.CancelCause;
import me.panpf.sketch.request.DisplayListener;
import me.panpf.sketch.request.ErrorCause;
import me.panpf.sketch.request.ImageFrom;
import me.panpf.sketch.zoom.ImageZoomer;

/**
 * Created by lrz on 2018/1/9.
 */

public class ImagesPagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private List<String>images=new ArrayList<>();

    ImageClickListener imageClickListener;

    public ImageClickListener getImageClickListener() {
        return imageClickListener;
    }

    public void setImageClickListener(ImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public ImagesPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return getImages().size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        FrameLayout frameLayout=(FrameLayout)mLayoutInflater.inflate(R.layout.item_image_detail, container, false);
        SketchImageView largeImageView =frameLayout.findViewById(R.id.large_imageview);
        LoadingView img_loading_view=frameLayout.findViewById(R.id.img_loading_view);

        RxLogTool.e("img url:"+images.get(position));
        largeImageView.setZoomEnabled(true);
//        largeImageView.getZoomer().setReadMode(true);
        largeImageView.getOptions().setDecodeGifImage(true);

        largeImageView.setDisplayListener(new DisplayListener() {
            @Override
            public void onStarted() {
                img_loading_view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCompleted(@NonNull Drawable drawable, @NonNull ImageFrom imageFrom, @NonNull ImageAttrs imageAttrs) {
                img_loading_view.setVisibility(View.GONE);
            }

            @Override
            public void onError(@NonNull ErrorCause cause) {
                img_loading_view.setVisibility(View.GONE);
            }

            @Override
            public void onCanceled(@NonNull CancelCause cause) {
                img_loading_view.setVisibility(View.GONE);
            }
        });

        RxImageTool.displayImageBySketch(mContext,images.get(position),largeImageView);

        largeImageView.setOnClickListener(
                v->getImageClickListener().clickClick(position,largeImageView)
        );


        largeImageView.getZoomer().setOnViewTapListener(new ImageZoomer.OnViewTapListener() {
            @Override
            public void onViewTap(@NonNull View view, float v, float v1) {
                getImageClickListener().clickClick(position,largeImageView);
            }
        });
        largeImageView.getZoomer().setOnViewLongPressListener(new ImageZoomer.OnViewLongPressListener() {
            @Override
            public void onViewLongPress(@NonNull View view, float v, float v1) {
                getImageClickListener().longPressClick(position,largeImageView);
            }
        });
        container.addView(frameLayout);
        return frameLayout;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

    public interface ImageClickListener{
        public void longPressClick(int pos,SketchImageView largeImageView);
        public void clickClick(int pos,SketchImageView largeImageView);
    }

}
