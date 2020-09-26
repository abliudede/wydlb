package com.lianzai.reader.utils;

import android.content.Context;

import com.lianzai.reader.base.Constant;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxApiTool {
	public static IWXAPI api;
	public static boolean isLogin = false;
	public static void init(Context context){
		api = WXAPIFactory.createWXAPI(context, Constant.APP_ID, true);
        api.registerApp(Constant.APP_ID);
	}
	
	private static String buildTransaction(final String type) {
	    return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	public static void wxLogin(){
		isLogin = true;
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "xuanchain_wallet_wx_login";
		api.sendReq(req);
	}

	public static void gc(){
		if (null!=api){
			api=null;
		}
	}

	
	public static void wxShare(String url,String title,String desc){
		try{
			isLogin = false;
			WXWebpageObject webpage = new WXWebpageObject();
			webpage.webpageUrl = url;
			WXMediaMessage msg = new WXMediaMessage(webpage);
			msg.title = title;
			msg.description = desc;
			//msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
			
			SendMessageToWX.Req req = new SendMessageToWX.Req();
			req.transaction = buildTransaction("webpage");
			req.message = msg;
			req.scene = /*isTimelineCb.isChecked() ? SendMessageToWX.Req.WXSceneTimeline : */SendMessageToWX.Req.WXSceneSession;
			api.sendReq(req);
			//instance.finish();
		}
		catch(Exception e){
//			e.printStackTrace();
		}
	}

	public static boolean isWXAppInstalled()
	{
		if (api == null)
			return false;
		return api.isWXAppInstalled();
	}
}
