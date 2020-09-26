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
import com.lianzai.reader.ui.contract.BookCategoryContract;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class BookCategoryPresenter extends RxPresenter<BookCategoryContract.View> implements BookCategoryContract.Presenter<BookCategoryContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public BookCategoryPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void bookBookCategoryList() {
        if (!RxNetTool.isAvailable()){
            try {
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch (Exception e){

            }
            return;
        }
        Disposable disp =mReaderApi.bookBookCategoryList().compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            if (data != null && mView != null && data.getStatus_code()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                mView.bookBookCategoryList(data);
                            }else{
                                mView.showError("error");
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
