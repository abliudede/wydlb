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
package com.lianzai.reader.component;

import com.lianzai.reader.ui.activity.ActivityAreaCode;
import com.lianzai.reader.ui.activity.ActivityBountyHunter;
import com.lianzai.reader.ui.activity.ActivityChoosePayWay;
import com.lianzai.reader.ui.activity.ActivityEnterAmount;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityOneKeyReward;
import com.lianzai.reader.ui.activity.ActivityRealNameAuthFirst;
import com.lianzai.reader.ui.activity.ActivityRealNameAuthSecond;
import com.lianzai.reader.ui.activity.ActivitySafety;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckDetail;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckEnter;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckPersonList;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.MinerGameActivity;
import com.lianzai.reader.ui.activity.listenPay.ActivityListenPay;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBook;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketManage;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketRecord;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightAssets;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightHistoryList;
import com.lianzai.reader.ui.activity.wallet.ActivityGoldDrillDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletReadLockTicketList;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletRechargeGoldCoin;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletRechargeReadCoin;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletRechargeReadTicket;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletRecordList;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletWithdrawGoldCoin;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletWithdrawReadCoin;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletWithdrawReadTicket;
import com.lianzai.reader.ui.activity.wallet.ActivityWithdrawReadCoinBindWallet;
import com.lianzai.reader.ui.activity.wallet.ActivityWithdrawReadCoinConfirmOrder;
import com.lianzai.reader.ui.fragment.MyMessageFragment;
import com.lianzai.reader.view.dialog.RxDialogBindReceiptAccount;
import com.lianzai.reader.view.dialog.RxDialogDelete;
import com.lianzai.reader.view.dialog.RxDialogWalletPicker;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface AccountComponent {

    MainActivity inject(MainActivity activity);

    MyMessageFragment inject(MyMessageFragment myMessageFragment);

    MinerGameActivity inject(MinerGameActivity minerGameActivity);

    ActivityAreaCode inject(ActivityAreaCode activityAreaCode);

    RxDialogDelete inject(RxDialogDelete rxDialogDelete);

    ActivityRealNameAuthFirst inject(ActivityRealNameAuthFirst activityRealNameAuthFirst);
    ActivityRealNameAuthSecond inject(ActivityRealNameAuthSecond activityRealNameAuthSecond);

    ActivityImagesPreview inject(ActivityImagesPreview activityImagesPreview);

    ActivitySafety inject(ActivitySafety activityImagesPreview);

    ActivityWalletDetail inject(ActivityWalletDetail activityWalletDetail);

    RxDialogWalletPicker inject(RxDialogWalletPicker rxDialogWalletPicker);


    RxDialogBindReceiptAccount inject(RxDialogBindReceiptAccount rxDialogBindReceiptAccount);


    ActivityWalletRechargeGoldCoin inject(ActivityWalletRechargeGoldCoin walletRechargeGoldCoin);

    ActivityWalletRechargeReadCoin inject(ActivityWalletRechargeReadCoin walletRechargeReadCoin);


    ActivityWalletRechargeReadTicket inject(ActivityWalletRechargeReadTicket walletRechargeReadTicket);

    ActivityWalletWithdrawReadTicket inject(ActivityWalletWithdrawReadTicket walletWithdrawReadTicket);

    ActivityWalletWithdrawGoldCoin inject(ActivityWalletWithdrawGoldCoin walletWithdrawGoldCoin);
    ActivityWalletWithdrawReadCoin inject(ActivityWalletWithdrawReadCoin walletWithdrawReadCoin);

    ActivityWalletReadLockTicketList inject(ActivityWalletReadLockTicketList activityWalletReadLockTicketList);
    ActivityWalletRecordList inject(ActivityWalletRecordList activityWalletRecordList);

    ActivityBountyHunter inject(ActivityBountyHunter activitySettings);

    ActivityOneKeyReward inject(ActivityOneKeyReward activitySettings);

    ActivityEnterAmount inject(ActivityEnterAmount activitySettings);

    ActivityChoosePayWay inject(ActivityChoosePayWay activitySettings);

    ActivityWithdrawReadCoinConfirmOrder inject(ActivityWithdrawReadCoinConfirmOrder activitySettings);

    ActivityUserProfile inject(ActivityUserProfile activityUserProfile);

    ActivityGoldDrillDetail inject(ActivityGoldDrillDetail activityBanPerson);

    ActivityFightLuckEnter inject(ActivityFightLuckEnter myBookFragment);

    ActivityFightLuckDetail inject(ActivityFightLuckDetail myBookFragment);

    ActivityFightLuckPersonList inject(ActivityFightLuckPersonList myBookFragment);

    ActivityLoginNew inject(ActivityLoginNew newReadActivity);

    ActivityShareBitmapBook inject(ActivityShareBitmapBook perSonHomePageActivity);

    ActivityAutoTicketManage inject(ActivityAutoTicketManage activitySettings);

    ActivityAutoTicketRecord inject(ActivityAutoTicketRecord activitySettings);

    ActivityCopyrightAssets inject(ActivityCopyrightAssets activitySettings);

    ActivityCopyrightDetail inject(ActivityCopyrightDetail activitySettings);

    ActivityWithdrawReadCoinBindWallet inject(ActivityWithdrawReadCoinBindWallet activitySettings);

    ActivityListenPay  inject(ActivityListenPay activitySettings);

    ActivityCopyrightHistoryList inject(ActivityCopyrightHistoryList activitySettings);
}