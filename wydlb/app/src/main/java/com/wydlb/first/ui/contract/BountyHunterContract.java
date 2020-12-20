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
package com.wydlb.first.ui.contract;


import androidx.collection.ArrayMap;

import com.wydlb.first.base.BaseContract;
import com.wydlb.first.bean.GateWayResponse;
import com.wydlb.first.bean.GetOrderByOrderNo;
import com.wydlb.first.bean.GetRewaredPoolDetailBean;

public interface BountyHunterContract {

    interface View extends BaseContract.BaseView {
        void getRewaredPoolDetailSuccess(GetRewaredPoolDetailBean bean);
        void getOrderByOrderNoSuccess(GetOrderByOrderNo bean);
        void placeRewardOrderSuccess(GateWayResponse bean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void getRewaredPoolDetail(ArrayMap<String, Object> params);
        void getOrderByOrderNo(ArrayMap<String, Object> params);
        void placeRewardOrder(ArrayMap<String, Object> params);


    }

}
