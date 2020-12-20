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

import android.content.Intent;
import android.net.Uri;

import com.wydlb.first.api.ReaderApi;
import com.wydlb.first.base.Constant;
import com.wydlb.first.base.RxPresenter;
import com.wydlb.first.ui.activity.ActivityImagesPreview;
import com.wydlb.first.ui.contract.DownloadFileContract;
import com.wydlb.first.utils.RxFileTool;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class DownloadFilePresenter extends RxPresenter<DownloadFileContract.View> implements DownloadFileContract.Presenter<DownloadFileContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public DownloadFilePresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }

    @Override
    public void downloadFile(String path) {
        if (!RxNetTool.isAvailable()){

            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch ( Exception e){
                RxLogTool.e("DownloadFilePresenter:"+e.toString());
            }

            return;
        }
        Disposable disp =mReaderApi.downloadFile(path).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
                            try{
                                String dirPath= RxFileTool.getRootPath()+Constant.APP_FOLDER+Constant.IMAGES_FOLDER;
                                RxFileTool.createOrExistsDir(dirPath);

                                File newFile=new File(dirPath+"/"+System.currentTimeMillis()+"."+data.contentType().subtype());
                                if (writeResponseBodyToDisk(data,newFile)){

                                    mView.downloadFileSuccess(newFile.getPath());
                                }else{
                                    mView.downloadFileFailed();
                                }
                            }catch (Exception e){

                            }
                        },
                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
                                try{
                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
                                }catch ( Exception ex){
                                    RxLogTool.e("DownloadFilePresenter:"+ex.toString());
                                }
//                            }
                        }
                );
        addDisposable(disp);

    }


    private boolean writeResponseBodyToDisk(ResponseBody body,File newFile) {
        try {


            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(newFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    RxLogTool.e("file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}
