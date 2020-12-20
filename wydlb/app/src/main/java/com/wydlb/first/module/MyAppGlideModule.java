package com.wydlb.first.module;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.wydlb.first.api.support.UnsafeOkHttpClient;

import java.io.InputStream;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);

        registry.replace(GlideUrl.class, InputStream.class,new OkHttpUrlLoader.Factory(UnsafeOkHttpClient.getUnsafeOkHttpClient()));
    }
}
