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


import android.support.v4.util.ArrayMap;

import com.lianzai.reader.base.BaseContract;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.ChatRoomPersonBaseInfo;

public interface ChatRoomPersonListContract {

    interface View extends BaseContract.BaseView {
        void QuanziPersonBaseInfoSuccess(ChatRoomPersonBaseInfo bean);
        void bansetRequestSuccess(BaseBean bean, int position);
        void rolesetRequestSuccess(BaseBean bean, int position, int role);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void quanziPersonBaseInfo(ArrayMap<String, String> maps);
        void quanziPersonOwnerInfo(ArrayMap<String, String> maps);
        void bansetRequest(ArrayMap<String, Object> maps, int position);
        void rolesetRequest(ArrayMap<String, Object> maps, int position, int role);
    }

}
