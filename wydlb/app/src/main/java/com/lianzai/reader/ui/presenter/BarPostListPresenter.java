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
import com.lianzai.reader.bean.BarDetailListResponse;
import com.lianzai.reader.ui.contract.BarPostListContract;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class BarPostListPresenter extends RxPresenter<BarPostListContract.View> implements BarPostListContract.Presenter<BarPostListContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public BarPostListPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void barPostList(ArrayMap<String,Object> maps,int type) {
        if (!RxNetTool.isAvailable()){
            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch ( Exception e){

            }
            return;
        }

        Single<BarDetailListResponse> single=null;
        if (type==Constant.BarPostType.MINE_RECOMMEND){
            single=mReaderApi.homeRecommendList(maps);
        }else if (type==Constant.BarPostType.MINE_ATTENTION){
            single=mReaderApi.attentionMineList(maps);
        }
        if (null==single)
            return;
        Disposable disp =single.compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                           try{
                               if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                   mView.barPostList(data);
                               }else{
                                   mView.showError("error");
                               }
                           }catch (Exception e){
                                e.printStackTrace();
                           }
                        },
                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
                                try {
                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                }catch ( Exception ex){

                                }
//                            }
                        }
                );
        addDisposable(disp);


    }


}
