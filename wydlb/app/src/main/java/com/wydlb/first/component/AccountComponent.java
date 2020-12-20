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
package com.wydlb.first.component;

import com.wydlb.first.ui.activity.ActivityBountyHunter;
import com.wydlb.first.ui.activity.ActivityChoosePayWay;
import com.wydlb.first.ui.activity.ActivityEnterAmount;
import com.wydlb.first.ui.activity.ActivityImagesPreview;
import com.wydlb.first.ui.activity.ActivityLoginNew;
import com.wydlb.first.ui.activity.ActivityOneKeyReward;
import com.wydlb.first.ui.activity.GameFightLuck.ActivityFightLuckDetail;
import com.wydlb.first.ui.activity.GameFightLuck.ActivityFightLuckEnter;
import com.wydlb.first.ui.activity.GameFightLuck.ActivityFightLuckPersonList;
import com.wydlb.first.ui.activity.MainActivity;
import com.wydlb.first.ui.activity.MinerGameActivity;
import com.wydlb.first.ui.activity.listenPay.ActivityListenPay;
import com.wydlb.first.ui.activity.profile.ActivityUserProfile;
import com.wydlb.first.ui.fragment.MyMessageFragment;
import com.wydlb.first.view.dialog.RxDialogBindReceiptAccount;
import com.wydlb.first.view.dialog.RxDialogDelete;
import com.wydlb.first.view.dialog.RxDialogWalletPicker;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface AccountComponent {

    MainActivity inject(MainActivity activity);

    MyMessageFragment inject(MyMessageFragment myMessageFragment);

    MinerGameActivity inject(MinerGameActivity minerGameActivity);

    RxDialogDelete inject(RxDialogDelete rxDialogDelete);

    ActivityImagesPreview inject(ActivityImagesPreview activityImagesPreview);

    RxDialogWalletPicker inject(RxDialogWalletPicker rxDialogWalletPicker);

    RxDialogBindReceiptAccount inject(RxDialogBindReceiptAccount rxDialogBindReceiptAccount);

    ActivityBountyHunter inject(ActivityBountyHunter activitySettings);

    ActivityOneKeyReward inject(ActivityOneKeyReward activitySettings);

    ActivityEnterAmount inject(ActivityEnterAmount activitySettings);

    ActivityChoosePayWay inject(ActivityChoosePayWay activitySettings);

    ActivityUserProfile inject(ActivityUserProfile activityUserProfile);

    ActivityFightLuckEnter inject(ActivityFightLuckEnter myBookFragment);

    ActivityFightLuckDetail inject(ActivityFightLuckDetail myBookFragment);

    ActivityFightLuckPersonList inject(ActivityFightLuckPersonList myBookFragment);

    ActivityLoginNew inject(ActivityLoginNew newReadActivity);

    ActivityListenPay  inject(ActivityListenPay activitySettings);

}