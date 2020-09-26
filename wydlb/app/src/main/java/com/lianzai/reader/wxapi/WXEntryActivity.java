package com.lianzai.reader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.bean.DataSynEvent;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.RxToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private static final String TAG="WXEntryActivity";
	
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

	private static final int RETURN_MSG_TYPE_LOGIN = 1; //登录
	private static final int RETURN_MSG_TYPE_SHARE = 2; //分享
    private IWXAPI api;


    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxEventBusTool.registerEventBus(this);

    	api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);

        try {
        	api.handleIntent(getIntent(), this);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			RxLogTool.e(TAG,req.openId);
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			RxLogTool.e(TAG,req.openId);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		String result = "";
		String code ="";
		int type=0;
		if (resp instanceof  SendAuth.Resp){
			code=((SendAuth.Resp) resp).code;
			RxLogTool.e("wx code:"+code);
			RxLogTool.e("open id:"+((SendAuth.Resp) resp).openId);
			type=RETURN_MSG_TYPE_LOGIN;
			RxEventBusTool.sendEvents(new DataSynEvent(code,Constant.EventTag.WX_LOGIN));
		}else if (resp instanceof SendMessageToWX.Resp){
			type=RETURN_MSG_TYPE_SHARE;
		}
		RxLogTool.e(TAG,"code:"+code+"--type:"+type);

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if (type == RETURN_MSG_TYPE_SHARE) {
				result = "分享成功";
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "取消操作";
			break;
		default:
			result ="操作失败";
			break;
		}
		if (!TextUtils.isEmpty(result))
			RxToast.custom(result).show();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		RxEventBusTool.unRegisterEventBus(this);
	}
}