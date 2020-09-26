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
import com.lianzai.reader.ui.contract.DeleteReplyOrCommentContract;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class DeleteReplyOrCommentPresenter extends RxPresenter<DeleteReplyOrCommentContract.View> implements DeleteReplyOrCommentContract.Presenter<DeleteReplyOrCommentContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public DeleteReplyOrCommentPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void deleteReplyOrComment(boolean isPost,String id) {
        if (!RxNetTool.isAvailable()){

            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch ( Exception e){
                RxLogTool.e("DeleteReplyOrCommentPresenter:"+e.toString());
            }

            return;
        }
        Disposable disp =(isPost?mReaderApi.deletePost(id):mReaderApi.deleteReply(id)).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.deleteReplyOrComment(data);
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch (Exception e){

                            }
                        },
                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
                                try{
                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                }catch ( Exception ex){
                                    RxLogTool.e("DeleteReplyOrCommentPresenter:"+ex.toString());
                                }
//                            }
                        }
                );
        addDisposable(disp);

    }


}
