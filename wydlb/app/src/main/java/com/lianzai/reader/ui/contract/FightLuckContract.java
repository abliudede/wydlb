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
import com.lianzai.reader.bean.GateWayResponse;
import com.lianzai.reader.bean.GetOrderByOrderNo;
import com.lianzai.reader.bean.GetRewaredPoolDetailBean;
import com.lianzai.reader.bean.LuckInfoBean;
import com.lianzai.reader.bean.LuckInfoFollowBean;
import com.lianzai.reader.bean.LuckLaunchBean;

import okhttp3.RequestBody;

public interface FightLuckContract {

    interface View extends BaseContract.BaseView {
        void luckLaunchSuccess (LuckLaunchBean bean);
        void luckPublishSuccess(BaseBean bean);
        void luckInfoSuccess(LuckInfoBean bean);
        void luckFollowSuccess(LuckInfoFollowBean bean);
        void luckJoinSuccess(BaseBean bean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {

        void luckLaunch ();
        void luckPublish(String body);
        void luckInfo(ArrayMap<String, Object> params);
        void luckFollow(ArrayMap<String, Object> params);
        void luckJoin(String body);
    }

}