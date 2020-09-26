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

import android.support.v4.util.ArrayMap;

import com.lianzai.reader.api.ReaderApi;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.RxPresenter;
import com.lianzai.reader.ui.contract.SearchNovelsContract;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class SearchNovelsPresenter extends RxPresenter<SearchNovelsContract.View> implements SearchNovelsContract.Presenter<SearchNovelsContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public SearchNovelsPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void searchNovelsByKeyWord(ArrayMap<String,Object> maps,String keyword) {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        RxLogTool.e("keyword:"+keyword);
        Disposable disp =mReaderApi.searchNovelsByKeyWord(maps,keyword).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.searchNovelsByKeyWord(data);
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch (Exception e){

                            }
                        },
                        e -> {
                            RxLogTool.e("searchNovelsByKeyWord:"+e.getMessage());
                            mView.complete(e.getMessage());
                        }
                );
        addDisposable(disp);

    }


}