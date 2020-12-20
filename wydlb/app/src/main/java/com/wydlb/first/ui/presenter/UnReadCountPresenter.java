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


import com.wydlb.first.api.ReaderApi;
import com.wydlb.first.base.Constant;
import com.wydlb.first.base.RxPresenter;
import com.wydlb.first.ui.contract.UnReadCountContract;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;


public class UnReadCountPresenter extends RxPresenter<UnReadCountContract.View> implements UnReadCountContract.Presenter<UnReadCountContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public UnReadCountPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void getUnReadCount() {
        if (!RxNetTool.isAvailable()){

            try {
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch (Exception ex){

            }
            return;
        }
        Disposable disp =mReaderApi.getUnReadCount().compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.getUnReadCountSuccess(data);
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch (Exception e){

                            }
                        },
                        e -> {
                            try {
                                mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                            }catch (Exception ex){

                            }
                        }
                );
        addDisposable(disp);

    }


}
