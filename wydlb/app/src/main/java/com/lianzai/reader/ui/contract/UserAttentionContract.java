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
import com.lianzai.reader.bean.AttentionBean;
import com.lianzai.reader.bean.BaseBean;
import com.lianzai.reader.bean.CirclePersonBean;
import com.lianzai.reader.bean.LuckInfoBean;
import com.lianzai.reader.bean.LuckInfoFollowBean;
import com.lianzai.reader.bean.LuckLaunchBean;
import com.lianzai.reader.bean.UserAttentionBean;

public interface UserAttentionContract {

    interface View extends BaseContract.BaseView {
        void userAttentionSuccess(UserAttentionBean bean);
        void attentionOrUnattentionSuccess(AttentionBean bean, int pos,int type);
        void circlePersonSuccess(CirclePersonBean bean);
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void circlePerson(ArrayMap<String, Object> params);
        void userAttention(ArrayMap<String, Object> params);
        void attentionOrUnattention(ArrayMap<String, Object> params,int pos,int type);
        void gxbqPerson(ArrayMap<String, Object> params);
    }

}