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
import com.lianzai.reader.ui.activity.ActivityBindAccount;
import com.lianzai.reader.ui.activity.ActivityBindPhone;
import com.lianzai.reader.ui.activity.ActivityBookList;
import com.lianzai.reader.ui.activity.ActivityBookListForRank;
import com.lianzai.reader.ui.activity.ActivityBountyHunter;
import com.lianzai.reader.ui.activity.ActivityChoosePayWay;
import com.lianzai.reader.ui.activity.ActivityEditPassword;
import com.lianzai.reader.ui.activity.ActivityEditPasswordShowPhone;
import com.lianzai.reader.ui.activity.ActivityEnterAmount;
import com.lianzai.reader.ui.activity.ActivityImagesPreview;
import com.lianzai.reader.ui.activity.ActivityLogin;
import com.lianzai.reader.ui.activity.ActivityLoginNew;
import com.lianzai.reader.ui.activity.ActivityOneKeyReward;
import com.lianzai.reader.ui.activity.ActivityPushSettings;
import com.lianzai.reader.ui.activity.ActivityRealNameAuthFirst;
import com.lianzai.reader.ui.activity.ActivityRealNameAuthSecond;
import com.lianzai.reader.ui.activity.ActivityRegisterAndPassword;
import com.lianzai.reader.ui.activity.ActivitySafety;
import com.lianzai.reader.ui.activity.ActivitySearch2;
import com.lianzai.reader.ui.activity.ActivitySearchFirst;
import com.lianzai.reader.ui.activity.ActivitySetPassword;
import com.lianzai.reader.ui.activity.ActivitySettings;
import com.lianzai.reader.ui.activity.ActivityVerificationCode;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckDetail;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckEnter;
import com.lianzai.reader.ui.activity.GameFightLuck.ActivityFightLuckPersonList;
import com.lianzai.reader.ui.activity.MainActivity;
import com.lianzai.reader.ui.activity.MinerGameActivity;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityAttentionPersonList;
import com.lianzai.reader.ui.activity.PersonHomePage.ActivityUserShieldingList;
import com.lianzai.reader.ui.activity.PersonHomePage.PerSonHomePageActivity;
import com.lianzai.reader.ui.activity.TeamChat.ActivityTeamBanPerson;
import com.lianzai.reader.ui.activity.TeamChat.ActivityTeamChatInfo;
import com.lianzai.reader.ui.activity.TeamChat.ActivityTeamChatPersonList;
import com.lianzai.reader.ui.activity.TeamChat.ActivityTeamChatSearchPersonList;
import com.lianzai.reader.ui.activity.TeamChat.ActivityTeamChatShowPerson;
import com.lianzai.reader.ui.activity.UrlIdentification.ActivityReadTimeShow;
import com.lianzai.reader.ui.activity.UrlIdentification.ActivityTeamChoose;
import com.lianzai.reader.ui.activity.UrlIdentification.UrlReadActivity;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityBanPerson;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityChatRoomInfo;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityChatRoomPersonList;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityChatRoomShowPerson;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityMilepost;
import com.lianzai.reader.ui.activity.chatRoomSetting.ActivityRank;
import com.lianzai.reader.ui.activity.circle.ActivityCircleClose;
import com.lianzai.reader.ui.activity.circle.ActivityCircleDetailShow;
import com.lianzai.reader.ui.activity.circle.ActivityCircleGXBQPersonList;
import com.lianzai.reader.ui.activity.circle.ActivityCircleManagerAuditList;
import com.lianzai.reader.ui.activity.circle.ActivityCircleManagerPersonList;
import com.lianzai.reader.ui.activity.circle.ActivityCircleMutePersonList;
import com.lianzai.reader.ui.activity.circle.ActivityCirclePersonList;
import com.lianzai.reader.ui.activity.circle.ActivityCircleReportAuditList;
import com.lianzai.reader.ui.activity.circle.ActivityInsideBookMulu;
import com.lianzai.reader.ui.activity.circle.ActivityMyNotice;
import com.lianzai.reader.ui.activity.circle.ActivityMyPraised;
import com.lianzai.reader.ui.activity.circle.ActivityPostCircle;
import com.lianzai.reader.ui.activity.circle.ActivityPostDetail;
import com.lianzai.reader.ui.activity.circle.ActivityPostFloor;
import com.lianzai.reader.ui.activity.circle.ActivityRelatedLinks;
import com.lianzai.reader.ui.activity.listenPay.ActivityListenPay;
import com.lianzai.reader.ui.activity.profile.ActivityUserProfile;
import com.lianzai.reader.ui.activity.qrCodeShare.ActivityShareBitmapBook;
import com.lianzai.reader.ui.activity.read.ActivityTeamChooseForLianzaihao;
import com.lianzai.reader.ui.activity.read.NewReadActivity;
import com.lianzai.reader.ui.activity.taskCenter.ActivityGetRedEnvelope;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketManage;
import com.lianzai.reader.ui.activity.wallet.ActivityAutoTicketRecord;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightAssets;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityCopyrightHistoryList;
import com.lianzai.reader.ui.activity.wallet.ActivityGoldDrillDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletDetail;
import com.lianzai.reader.ui.activity.wallet.ActivityWalletMain;
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
import com.lianzai.reader.ui.fragment.BookStoreFragment;
import com.lianzai.reader.ui.fragment.MyBookFragment;
import com.lianzai.reader.ui.fragment.MyMessageFragment;
import com.lianzai.reader.ui.fragment.NewMineFragment;
import com.lianzai.reader.ui.fragment.TaskCenterFragment;
import com.lianzai.reader.view.dialog.RxDialogBindReceiptAccount;
import com.lianzai.reader.view.dialog.RxDialogDelete;
import com.lianzai.reader.view.dialog.RxDialogWalletPicker;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface AccountComponent {
    ActivityLogin inject(ActivityLogin activity);

    ActivityRegisterAndPassword inject(ActivityRegisterAndPassword activity);

    ActivityEditPassword inject(ActivityEditPassword activity);

    MainActivity inject(MainActivity activity);

    BookStoreFragment inject(BookStoreFragment bookStoreFragment);

    ActivityBookList inject(ActivityBookList activityBookList);

    MyMessageFragment inject(MyMessageFragment myMessageFragment);

    MinerGameActivity inject(MinerGameActivity minerGameActivity);

    ActivityAreaCode inject(ActivityAreaCode activityAreaCode);

    ActivityBindAccount inject(ActivityBindAccount activityBindAccount);

    RxDialogDelete inject(RxDialogDelete rxDialogDelete);

    ActivityRealNameAuthFirst inject(ActivityRealNameAuthFirst activityRealNameAuthFirst);
    ActivityRealNameAuthSecond inject(ActivityRealNameAuthSecond activityRealNameAuthSecond);

    ActivityImagesPreview inject(ActivityImagesPreview activityImagesPreview);

    ActivitySafety inject(ActivitySafety activityImagesPreview);

    ActivityWalletMain inject(ActivityWalletMain activityWalletMain);

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

    ActivitySettings inject(ActivitySettings activitySettings);

    NewMineFragment inject(NewMineFragment newMineFragment);


    ActivityBountyHunter inject(ActivityBountyHunter activitySettings);

    ActivityOneKeyReward inject(ActivityOneKeyReward activitySettings);

    ActivityEnterAmount inject(ActivityEnterAmount activitySettings);

    ActivityChoosePayWay inject(ActivityChoosePayWay activitySettings);

    ActivityWithdrawReadCoinConfirmOrder inject(ActivityWithdrawReadCoinConfirmOrder activitySettings);

    ActivityUserProfile inject(ActivityUserProfile activityUserProfile);

    ActivityChatRoomInfo inject(ActivityChatRoomInfo activityUserProfile);

    ActivityRank inject(ActivityRank activityUserProfile);

    ActivityChatRoomPersonList inject(ActivityChatRoomPersonList activityUserProfile);

    ActivityMilepost inject(ActivityMilepost activityMilepost);

    ActivityBanPerson inject(ActivityBanPerson activityBanPerson);

    ActivityVerificationCode inject(ActivityVerificationCode activityBanPerson);

    ActivitySetPassword inject(ActivitySetPassword activityBanPerson);

    ActivityBindPhone inject(ActivityBindPhone activityBanPerson);

    ActivityEditPasswordShowPhone inject(ActivityEditPasswordShowPhone activityBanPerson);

    ActivityChatRoomShowPerson inject(ActivityChatRoomShowPerson activityBanPerson);

    ActivityGetRedEnvelope inject(ActivityGetRedEnvelope activityBanPerson);

    ActivityGoldDrillDetail inject(ActivityGoldDrillDetail activityBanPerson);

    MyBookFragment inject(MyBookFragment myBookFragment);

    TaskCenterFragment inject(TaskCenterFragment myBookFragment);

    ActivityFightLuckEnter inject(ActivityFightLuckEnter myBookFragment);

    ActivityFightLuckDetail inject(ActivityFightLuckDetail myBookFragment);

    ActivityFightLuckPersonList inject(ActivityFightLuckPersonList myBookFragment);

    NewReadActivity inject(NewReadActivity newReadActivity);

    ActivityLoginNew inject(ActivityLoginNew newReadActivity);

    ActivityTeamChatInfo inject(ActivityTeamChatInfo newReadActivity);

    ActivityTeamChatShowPerson inject(ActivityTeamChatShowPerson newReadActivity);

    ActivityTeamChatPersonList inject(ActivityTeamChatPersonList newReadActivity);

    ActivityTeamBanPerson inject(ActivityTeamBanPerson newReadActivity);

    ActivityTeamChatSearchPersonList inject(ActivityTeamChatSearchPersonList newReadActivity);

    ActivityAttentionPersonList inject(ActivityAttentionPersonList newReadActivity);

    UrlReadActivity inject(UrlReadActivity newReadActivity);

    ActivityTeamChoose inject(ActivityTeamChoose newReadActivity);

    PerSonHomePageActivity inject(PerSonHomePageActivity perSonHomePageActivity);

    ActivityBookListForRank inject(ActivityBookListForRank perSonHomePageActivity);

    ActivityTeamChooseForLianzaihao inject(ActivityTeamChooseForLianzaihao perSonHomePageActivity);

    ActivityShareBitmapBook inject(ActivityShareBitmapBook perSonHomePageActivity);

    ActivityPostCircle inject(ActivityPostCircle activityPostCircle);

    ActivityCirclePersonList inject(ActivityCirclePersonList activityPostCircle);

    ActivityPostDetail inject(ActivityPostDetail activityPostCircle);

    ActivityPostFloor inject(ActivityPostFloor activitySettings);

    ActivityMyNotice  inject(ActivityMyNotice activitySettings);

    ActivityMyPraised inject(ActivityMyPraised activitySettings);

    ActivityRelatedLinks inject(ActivityRelatedLinks activitySettings);

    ActivityInsideBookMulu inject(ActivityInsideBookMulu activitySettings);

    ActivityUserShieldingList inject(ActivityUserShieldingList activitySettings);

    ActivityPushSettings inject(ActivityPushSettings activitySettings);

    ActivityCircleClose inject(ActivityCircleClose activitySettings);

    ActivityCircleGXBQPersonList inject(ActivityCircleGXBQPersonList activitySettings);

    ActivityCircleDetailShow inject(ActivityCircleDetailShow activitySettings);

    ActivityCircleManagerPersonList inject(ActivityCircleManagerPersonList activitySettings);

    ActivityCircleMutePersonList inject(ActivityCircleMutePersonList activitySettings);

    ActivityCircleReportAuditList inject(ActivityCircleReportAuditList activitySettings);

    ActivityCircleManagerAuditList inject(ActivityCircleManagerAuditList activitySettings);

    ActivityAutoTicketManage inject(ActivityAutoTicketManage activitySettings);

    ActivityAutoTicketRecord inject(ActivityAutoTicketRecord activitySettings);

    ActivityCopyrightAssets inject(ActivityCopyrightAssets activitySettings);

    ActivityCopyrightDetail inject(ActivityCopyrightDetail activitySettings);

    ActivityReadTimeShow inject(ActivityReadTimeShow activitySettings);

    ActivityWithdrawReadCoinBindWallet inject(ActivityWithdrawReadCoinBindWallet activitySettings);

    ActivitySearchFirst inject(ActivitySearchFirst activitySettings);

    ActivitySearch2 inject(ActivitySearch2 activitySettings);

    ActivityListenPay  inject(ActivityListenPay activitySettings);

    ActivityCopyrightHistoryList inject(ActivityCopyrightHistoryList activitySettings);
}