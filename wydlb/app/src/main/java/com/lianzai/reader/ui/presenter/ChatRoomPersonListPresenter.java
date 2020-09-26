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
import com.lianzai.reader.bean.ChatRoomPersonBaseInfo;
import com.lianzai.reader.ui.contract.ChatRoomPersonListContract;
import com.lianzai.reader.utils.RxLogTool;
import com.lianzai.reader.utils.RxNetTool;
import com.lianzai.reader.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class ChatRoomPersonListPresenter extends RxPresenter<ChatRoomPersonListContract.View> implements ChatRoomPersonListContract.Presenter<ChatRoomPersonListContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public ChatRoomPersonListPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }


    @Override
    public void quanziPersonBaseInfo(ArrayMap<String, String> maps) {

        if (!RxNetTool.isAvailable()){
            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch ( Exception e){

            }
            return;
        }

//        Single<ChatRoomPersonBaseInfo> single=mReaderApi.getBarMemberBaseInfo(maps);
//        Disposable disp =single.compose(RxUtils::toSimpleSingle)
//                .subscribe(
//                        data -> {
//                            try{
//                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                                    mView.QuanziPersonBaseInfoSuccess(data);
//                                }else{
//                                    if(data.getCode() != Constant.ResponseCodeStatus.TOKEN_INVALID)
//                                    mView.showError("error");
//                                }
//                            }catch (Exception e){
//
//                            }
//                        },
//                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
//                                try {
//                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
//                                }catch ( Exception ex){
//
//                                }
//                            }
//                        }
//                );
//        addDisposable(disp);

    }

    @Override
    public void quanziPersonOwnerInfo(ArrayMap<String, String> maps) {

        if (!RxNetTool.isAvailable()){
            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch ( Exception e){

            }
            return;
        }
//        Single<ChatRoomPersonBaseInfo> single=mReaderApi.getBarMemberOwnerInfo(maps);
//        Disposable disp =single.compose(RxUtils::toSimpleSingle)
//                .subscribe(
//                        data -> {
//                            try{
//                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                                    mView.QuanziPersonBaseInfoSuccess(data);
//                                }else{
//                                    if(data.getCode() != Constant.ResponseCodeStatus.TOKEN_INVALID)
//                                        mView.showError("error");
//                                }
//                            }catch (Exception e){
//
//                            }
//                        },
//                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
//                                try {
//                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
//                                }catch ( Exception ex){
//
//                                }
//                            }
//                        }
//                );
//        addDisposable(disp);

    }

    @Override
    public void bansetRequest(ArrayMap<String, Object> maps, int position) {
        if (!RxNetTool.isAvailable()){
            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch (Exception e){

            }
            return;
        }

//        Disposable disp = mReaderApi.banset(maps).compose(RxUtils::toSimpleSingle)
//                .subscribe(
//                        data -> {
//                            try {
//                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                                    mView.bansetRequestSuccess(data,position);
//                                    RxLogTool.e(data.toString());
//                                }else{
//                                    if(data.getCode() != Constant.ResponseCodeStatus.TOKEN_INVALID)
//                                    mView.showError(data.getMsg());
//                                }
//                            }catch (Exception e){
//
//                            }
//                        },
//                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
//                                try {
//                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
//                                }catch (Exception ex){
//
//                                }
//                            }
//                        }
//                );
//        addDisposable(disp);
    }

    @Override
    public void rolesetRequest(ArrayMap<String, Object> maps, int position , int role) {
        if (!RxNetTool.isAvailable()){
            try{
                mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            }catch (Exception e){

            }
            return;
        }

//        Disposable disp = mReaderApi.roleset(maps).compose(RxUtils::toSimpleSingle)
//                .subscribe(
//                        data -> {
//                            try {
//                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
//                                    mView.rolesetRequestSuccess(data,position,role);
//                                    RxLogTool.e(data.toString());
//                                }else{
//                                    if(data.getCode() != Constant.ResponseCodeStatus.TOKEN_EXPRIED)
//                                    mView.showError(data.getMsg());
//                                }
//                            }catch (Exception e){
//
//                            }
//                        },
//                        e -> {
//                            if (!e.toString().contains("NullPointerException")){
//                                try {
//                                    mView.complete(Constant.NetWorkStatus.SERVER_ERROR);
//                                }catch (Exception ex){
//
//                                }
//                            }
//                        }
//                );
//        addDisposable(disp);
    }
}
