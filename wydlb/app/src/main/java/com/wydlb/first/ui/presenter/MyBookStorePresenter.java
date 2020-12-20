/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wydlb.first.ui.presenter;

import androidx.collection.ArrayMap;

import com.wydlb.first.api.ReaderApi;
import com.wydlb.first.base.Constant;
import com.wydlb.first.base.RxPresenter;
import com.wydlb.first.ui.contract.MyBookStoreContract;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class MyBookStorePresenter extends RxPresenter<MyBookStoreContract.View> implements MyBookStoreContract.Presenter<MyBookStoreContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public MyBookStorePresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void requestBookStore(ArrayMap<String, Object> maps) {
        if (!RxNetTool.isAvailable()){
            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch ( Exception e){
                RxLogTool.e("requestBookStore:"+e.toString());
            }

            return;
        }

        RxLogTool.e("requestBookStore request...");
        Disposable disp =mReaderApi.requestBookStore(maps).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {

                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.initBookData(data);
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch (Exception e){
                                mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                RxLogTool.e("requestBookStore e:"+e.getMessage());
                            }
                        },
                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
                                try{
                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                }catch ( Exception ex){
                                    RxLogTool.e("AccountPresenter:"+ex.toString());
                                }
//                            }
                        }
                );
        addDisposable(disp);

    }

}
