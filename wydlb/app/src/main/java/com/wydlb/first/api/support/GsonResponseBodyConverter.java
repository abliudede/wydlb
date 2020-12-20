package com.wydlb.first.api.support;

import com.google.gson.Gson;
import com.wydlb.first.R;
import com.wydlb.first.base.BuglyApplicationLike;
import com.wydlb.first.base.Constant;
import com.wydlb.first.bean.BaseBean;
import com.wydlb.first.utils.RxEventBusTool;
import com.wydlb.first.utils.RxLogTool;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author qicheng.qing
 * @description
 * @create 17/4/13,14:59
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            //ResultResponse 只解析result字段
            BaseBean resultResponse = gson.fromJson(response, BaseBean.class);
            if(resultResponse.getStatus() != 0 && resultResponse.getStatus() != 200){//解决返回404等异常状态时，code解析为成功的问题
                return gson.fromJson("",type);
            }else if (resultResponse.getCode() == Constant.ResponseCodeStatus.TOKEN_INVALID){//token 失效
                RxEventBusTool.sendEvents(Constant.EventTag.TOKEN_FAILURE);
                return gson.fromJson("",type);
            } else if (resultResponse.getCode()== Constant.ResponseCodeStatus.DISABLE_ACCOUNT){//账户被禁用
                RxEventBusTool.sendEvents(Constant.EventTag.DISABLE_ACCOUNT);
                return gson.fromJson("",type);
            }else {
                return gson.fromJson(response,type);
            }

        }catch (Exception e){
            RxLogTool.e("<<<Exception:" + e.toString());
            if (e.toString().contains("403")){
                throw new ResultException( BuglyApplicationLike.getContext().getResources().getString(R.string.error_403));
            }else if (e.toString().contains("404")){
                throw new ResultException( BuglyApplicationLike.getContext().getResources().getString(R.string.error_404));
            }else if (e.toString().contains("504")){
                throw new ResultException(BuglyApplicationLike.getContext().getResources().getString(R.string.error_504));
            }else{
                throw new ResultException(BuglyApplicationLike.getContext().getResources().getString(R.string.error_500));
            }
        }
    }
}