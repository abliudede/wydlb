package com.wydlb.first.utils;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtil {

    private static RequestOptions initRequestOptions(){
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//        requestOptions.ce();
        return  requestOptions;
    }
    public static void loadImage(Activity activity,String pic, ImageView imageView,RequestOptions requestOptions){
        Glide.with(activity).asBitmap().load(pic).apply(requestOptions).into(imageView);
    }

}
