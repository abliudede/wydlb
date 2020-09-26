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

import android.graphics.Bitmap;

import com.lianzai.reader.api.ReaderApi;
import com.lianzai.reader.base.Constant;
import com.lianzai.reader.base.RxPresenter;
import com.lianzai.reader.ui.contract.FileUploadContract;
import com.lianzai.reader.utils.RxFileTool;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxPhotoTool;
import com.lianzai.reader.utils.RxUtils;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class FileUploadPresenter extends RxPresenter<FileUploadContract.View> implements FileUploadContract.Presenter<FileUploadContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public FileUploadPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }


    @Override
    public void uploadFile(Bitmap bitmap,int pos) {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }

        String dirPath=RxFileTool.getRootPath()+Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
        RxFileTool.createOrExistsDir(dirPath);

        String filePath=dirPath+"/Auth_" +System.currentTimeMillis()+".jpg";

        File file=new File(filePath);
        RxFileTool.saveFile(RxPhotoTool.bitmap2InputStream(bitmap),filePath);

//        Map map=new HashMap();
//
//        map.put(Params.BUCKET,"lianzai-static");
//        map.put(Params.SAVE_KEY,"authentication/");
//        UploadManager.getInstance().formUpload(file, map, "", new UpCompleteListener() {
//            @Override
//            public void onComplete(boolean b, String s) {
//
//            }
//        }, new UpProgressListener() {
//            @Override
//            public void onRequestProgress(long l, long l1) {
//
//            }
//        });


        Disposable disp =mReaderApi.uploadFile(file).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            try {
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.uploadFileSuccess(data,pos);
                                    RxLogTool.e(data.toString());
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch ( Exception e){

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
