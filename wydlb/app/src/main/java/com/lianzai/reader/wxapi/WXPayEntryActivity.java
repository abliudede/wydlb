package com.lianzai.reader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lianzai.reader.base.Constant;
import com.lianzai.reader.utils.RxEventBusTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.view.RxToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.handleIntent(getIntent(), this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		RxLogTool.e("WXPayEntryActivity:code"+resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			RxLogTool.e("WXPayEntryActivity:type true");
			if (resp.errCode == 0) {
				RxEventBusTool.sendEvents(Constant.EventTag.WX_PAY);
//				RxToast.custom("支付成功").show();
			}else if(resp.errCode == -2){
				RxToast.custom("取消支付").show();
			}else{
				RxToast.custom("支付失败").show();
			}
			finish();
		}
	}
}