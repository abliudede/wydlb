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
package com.lianzai.reader.ui.contract;


import androidx.collection.ArrayMap;

import com.lianzai.reader.base.BaseContract;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.GetTaskIsCompleteByUserIdBean;
import com.lianzai.reader.bean.ReceiveBean;
import com.lianzai.reader.bean.TaskCenterBean;
import com.lianzai.reader.bean.UserRelaListBean;

public interface TaskCenterContract {

    interface View extends BaseContract.BaseView {
        void getUserAwakenRelaListSuccess(UserRelaListBean bean);
        void getUserRelaListSuccess(UserRelaListBean bean);
        void receiveSuccess(ReceiveBean bean);
        void dailyCheckSuccess(BaseBean bean);
        void todayTaskListSuccess(TaskCenterBean bean);
        void getTaskIsCompleteByUserIdSuccess(GetTaskIsCompleteByUserIdBean bean);

    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getUserAwakenRelaList (ArrayMap<String, Object> params);
        void getUserRelaList (ArrayMap<String, Object> params);
        void receive (ArrayMap<String, Object> params);
        void dailyCheck(ArrayMap<String, Object> params);
        void todayTaskList();
        void getTaskIsCompleteByUserId();

    }

}
