package com.wydlb.first.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.wydlb.first.R;
import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.base.Constant;
import com.wydlb.first.view.RxToast;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by lrz on 2018/1/25.
 */

public class RxShareUtils {

    public static void sharePicByFile(File picFile, String tag, IWXAPI api) {

        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }

        if (!picFile.exists()) {
            return;
        }
        Bitmap pic = BitmapFactory.decodeFile(picFile.toString());

        WXImageObject imageObject = new WXImageObject(pic);
        //这个构造方法中自动把传入的bitmap转化为2进制数据,或者你直接传入byte[]也行
        //注意传入的数据不能大于10M,开发文档上写的

        WXMediaMessage msg = new WXMediaMessage();  //这个对象是用来包裹发送信息的对象
        msg.mediaObject = imageObject;
        //msg.mediaObject实际上是个IMediaObject对象,
        //它有很多实现类,每一种实现类对应一种发送的信息,
        //比如WXTextObject对应发送的信息是文字,想要发送文字直接传入WXTextObject对象就行

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(pic, 150, 150, true);

        msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        //在这设置缩略图
        //官方文档介绍这个bitmap不能超过32kb
        //如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,边长设置成150是比较保险的
        //或者使用msg.setThumbImage(thumbBitmap);省去自己转换二进制数据的过程
        //如果超过32kb则抛异常

        SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
        req.message = msg;  //把msg放入请求对象中
        req.scene = SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        //req.scene = SendMessageToWX.Req.WXSceneSession;   //设置发送给朋友
        req.transaction = tag;  //这个tag要唯一,用于在回调中分辨是哪个分享请求
        boolean b = api.sendReq(req);   //如果调用成功微信,会返回true

        RxLogTool.e("share flag:" + b);
    }

    public static void sharePicByBitmap(Bitmap bitmap, IWXAPI api, boolean isWx) {
        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (null == bitmap) {
            return;
        }

        WXImageObject imageObject = new WXImageObject(bitmap);
//        RxLogTool.e("source size : " + bitmap.getByteCount()/1024 + "k");
        //这个构造方法中自动把传入的bitmap转化为2进制数据,或者你直接传入byte[]也行
        //注意传入的数据不能大于10M,开发文档上写的

        WXMediaMessage msg = new WXMediaMessage();  //这个对象是用来包裹发送信息的对象
        msg.mediaObject = imageObject;
        //msg.mediaObject实际上是个IMediaObject对象,
        //它有很多实现类,每一种实现类对应一种发送的信息,
        //比如WXTextObject对应发送的信息是文字,想要发送文字直接传入WXTextObject对象就行

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, true);
//        RxLogTool.e("thumb size : " + thumbBitmap.getByteCount()/1024 + "k");

        msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        //在这设置缩略图
        //官方文档介绍这个bitmap不能超过32kb
        //如果一个像素是8bit的话换算成正方形的bitmap则边长不超过181像素,边长设置成150是比较保险的
        //或者使用msg.setThumbImage(thumbBitmap);省去自己转换二进制数据的过程
        //如果超过32kb则抛异常

        SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
        req.message = msg;  //把msg放入请求对象中
        req.scene = isWx ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
//        req.scene = SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        //req.scene = SendMessageToWX.Req.WXSceneSession;   //设置发送给朋友
        req.transaction = buildTransaction("pic");  //这个tag要唯一,用于在回调中分辨是哪个分享请求
        boolean b = api.sendReq(req);   //如果调用成功微信,会返回true

        RxLogTool.e("share flag:" + b);
    }

    public static void shareWebUrl(File picFile, String shareUrl, String title, String description, IWXAPI api, boolean isWx) {
//        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
//            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
//            return;
//        }
//        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
//            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
//            return;
//        }
        if (!TextUtils.isEmpty(description)&&description.length()>100){//不为空
            description=description.substring(0,100);
            RxLogTool.e("shareWebUrl--substring description:"+description);
        }
        RxLogTool.e("shareWebUrl--shareUrl:" + shareUrl + "--title:" + title + "--description:" + description + "--isWx:" + isWx);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);


        if (isWx) {//分享给好友-显示标题跟建议
            if (!TextUtils.isEmpty(title)) {
                msg.title = title;
                msg.description = RxTool.deleteHtmlChar(description);
            } else {
//                msg.title=RxTool.deleteHtmlChar(description);
                msg.description = RxTool.deleteHtmlChar(description);
            }
        } else {//分享到朋友圈-显示内容
            if (!TextUtils.isEmpty(title)) {
                msg.title = title + "    " + RxTool.deleteHtmlChar(description);
            } else {
                msg.title = RxTool.deleteHtmlChar(description);
            }
        }


        if (null!=picFile&&picFile.exists()){
            Bitmap pic = BitmapFactory.decodeFile(picFile.toString());
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(pic, 180, 180, true);

            msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        }else{
            Bitmap pic = BitmapFactory.decodeResource(BuglyApplicationLike.getContext().getResources(), R.mipmap.ic_share);
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(pic, 120, 120, true);
            msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
        req.message = msg;  //把msg放入请求对象中
        req.scene = isWx ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        req.transaction = buildTransaction("webpage");  //这个tag要唯一,用于在回调中分辨是哪个分享请求
        boolean b = api.sendReq(req);   //如果调用成功微信,会返回true

        RxLogTool.e("share flag:" + b);
    }

    public static void shareWebUrlForBitmap(Bitmap bitmap, String shareUrl, String title, String description, IWXAPI api, boolean isWx) {
        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (!TextUtils.isEmpty(description)&&description.length()>100){//不为空
            description=description.substring(0,100);
            RxLogTool.e("shareWebUrl--substring description:"+description);
        }
        RxLogTool.e("shareWebUrl--shareUrl:" + shareUrl + "--title:" + title + "--description:" + description + "--isWx:" + isWx);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = shareUrl;

        WXMediaMessage msg = new WXMediaMessage(webpage);


        if (isWx) {//分享给好友-显示标题跟建议
            if (!TextUtils.isEmpty(title)) {
                msg.title = title;
                msg.description = RxTool.deleteHtmlChar(description);
            } else {
//                msg.title=RxTool.deleteHtmlChar(description);
                msg.description = RxTool.deleteHtmlChar(description);
            }
        } else {//分享到朋友圈-显示内容
            if (!TextUtils.isEmpty(title)) {
                msg.title = title + "    " + RxTool.deleteHtmlChar(description);
            } else {
                msg.title = RxTool.deleteHtmlChar(description);
            }
        }


        if (null!=bitmap){
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);

            msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        }else{
            Bitmap pic = BitmapFactory.decodeResource(BuglyApplicationLike.getContext().getResources(), R.mipmap.ic_share);
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(pic, 120, 120, true);
            msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        }


        SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
        req.message = msg;  //把msg放入请求对象中
        req.scene = isWx ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        req.transaction = buildTransaction("webpage");  //这个tag要唯一,用于在回调中分辨是哪个分享请求
        boolean b = api.sendReq(req);   //如果调用成功微信,会返回true

        RxLogTool.e("share flag:" + b);
    }


    public static void shareImage(Bitmap bitmap, IWXAPI api, boolean isWx) {
        if (!BuglyApplicationLike.api.isWXAppInstalled()) {
            RxToast.custom("请先安装微信客户端", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (BuglyApplicationLike.api.getWXAppSupportAPI() < 0x21020001) {
            RxToast.custom("请先更新微信客户端",Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        if (null==bitmap)return;
        WXImageObject imageObject=new WXImageObject(bitmap);
        WXMediaMessage msg=new WXMediaMessage();
        msg.mediaObject=imageObject;

        if (null!=bitmap){
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);

            msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        }else{
            Bitmap pic = BitmapFactory.decodeResource(BuglyApplicationLike.getContext().getResources(), R.mipmap.ic_share);
            Bitmap thumbBitmap = Bitmap.createScaledBitmap(pic, 120, 120, true);
            msg.thumbData = RxImageTool.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
        req.message = msg;  //把msg放入请求对象中
        req.scene = isWx ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;    //设置发送到朋友圈
        req.transaction = buildTransaction("img");  //这个tag要唯一,用于在回调中分辨是哪个分享请求
        boolean b = api.sendReq(req);   //如果调用成功微信,会返回true

        RxLogTool.e("share flag:" + b);
    }



    private static String buildTransaction(String type) {
        return type + String.valueOf(System.currentTimeMillis());
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
                    PackageManager pm;
                    if ((pm = context.getApplicationContext().getPackageManager()) == null) {
                        return false;
                    }
                    List<PackageInfo> packages = pm.getInstalledPackages(0);
                    for (PackageInfo info : packages) {
                        String name = info.packageName.toLowerCase(Locale.ENGLISH);
                        if ("com.tencent.mobileqq".equals(name)) {
                            return true;
                        }
                    }
                    return false;
    }
    /**
     * 判断微博是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeiboInstalled(Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void QQShareUrl(Context context, String title, String des, String url,String uri, IUiListener listener)
    {
        if(!isQQClientAvailable(context)){
            RxToast.custom("请先安装QQ客户端", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        //QQ分享初始化
        Tencent mTencent = Tencent.createInstance("101502491", context);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  des);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  url);
        if(!TextUtils.isEmpty(uri)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, uri);
        }
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "连载神器");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ((Activity) context, params, listener);
    }
    public static void QQShareImg(Context context, String uri, IUiListener listener)
    {
        if(!isQQClientAvailable(context)){
            RxToast.custom("请先安装QQ客户端", Constant.ToastType.TOAST_ERROR).show();
            return;
        }
        Tencent mTencent = Tencent.createInstance("101502491", context);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,uri);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "连载神器");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ((Activity) context, params, listener);

    }

}
