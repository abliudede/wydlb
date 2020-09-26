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
package com.lianzai.reader.ui.presenter;

import com.lianzai.reader.api.ReaderApi;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.RxPresenter;
import com.lianzai.reader.ui.contract.FollowContract;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class FollowPresenter extends RxPresenter<FollowContract.View> implements FollowContract.Presenter<FollowContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public FollowPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void barFollow(String id) {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.barFollow(id).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            try {
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.barFollow(data);
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch (Exception e){

                            }
                        },
                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
                                try {
                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                }catch (Exception ex){

                                }
//                            }
                        }
                );
        addDisposable(disp);

    }

    @Override
    public void barCancelFollow(String id) {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.barCancelFollow(id).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                mView.barCancelFollow(data);
                            }else{
                                mView.showError(data.getMsg());
                            }
                        },
                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
                                try {
                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                }catch (Exception ex){

                                }
//                            }
                        }
                );
        addDisposable(disp);

    }


}