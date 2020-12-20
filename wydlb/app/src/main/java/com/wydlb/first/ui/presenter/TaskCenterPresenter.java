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
import com.wydlb.first.ui.contract.TaskCenterContract;
import com.wydlb.first.utils.RxLogTool;
import com.wydlb.first.utils.RxNetTool;
import com.wydlb.first.utils.RxUtils;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class TaskCenterPresenter extends RxPresenter<TaskCenterContract.View> implements TaskCenterContract.Presenter<TaskCenterContract.View> {

    private ReaderApi mReaderApi;

    @Inject
    public TaskCenterPresenter(ReaderApi readerApi) {
        this.mReaderApi = readerApi;
    }


    @Override
    public void getUserAwakenRelaList (ArrayMap<String, Object> params) {

        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.getUserAwakenRelaList(params).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {

                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.getUserAwakenRelaListSuccess(data);
                                    RxLogTool.e(data.toString());
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

    @Override
    public void getUserRelaList (ArrayMap<String, Object> params) {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.getUserRelaList(params).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {
//                            try{
//
//                            }catch (Exception e){
//
//                            }

                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.getUserRelaListSuccess(data);
                                    RxLogTool.e(data.toString());
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

    @Override
    public void receive (ArrayMap<String, Object> params) {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.receive(params).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {

                            try{
                                if (data != null && mView != null&& data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.receiveSuccess(data);
                                    RxLogTool.e(data.toString());
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


    @Override
    public void dailyCheck (ArrayMap<String, Object> params) {

        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.dailyCheck(params).compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {

                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.dailyCheckSuccess(data);
                                    RxLogTool.e(data.toString());
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

    @Override
    public void todayTaskList () {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.todayTaskList().compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {

                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.todayTaskListSuccess(data);
                                    RxLogTool.e(data.toString());
                                }else{
                                    mView.showError(data.getMsg());
                                }
                            }catch (Exception e){
                                RxLogTool.e(e.toString());
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


    @Override
    public void getTaskIsCompleteByUserId () {
        if (!RxNetTool.isAvailable()){
            mView.showError(Constant.NetWorkStatus.NETWORK_UNAVAILABLE);
            return;
        }
        Disposable disp =mReaderApi.getTaskIsCompleteByUserId().compose(RxUtils::toSimpleSingle)
                .subscribe(
                        data -> {

                            try{
                                if (data != null && mView != null && data.getCode()== Constant.ResponseCodeStatus.SUCCESS_CODE) {
                                    mView.getTaskIsCompleteByUserIdSuccess(data);
                                    RxLogTool.e(data.toString());
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
