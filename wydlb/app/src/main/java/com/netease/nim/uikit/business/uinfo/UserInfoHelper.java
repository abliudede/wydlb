package com.netease.nim.uikit.business.uinfo;

import android.text.TextUtils;

import com.lianzai.reader.utils.RxLogTool;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

public class UserInfoHelper {

    // 获取用户显示在标题栏和最近联系人中的名字
    public static String getUserTitleName(String id, SessionTypeEnum sessionType) {
        if (SessionTypeEnum.P2P == sessionType) {
            try{
                return getUserDisplayName(id);
            }catch (Exception e){
                return "";
            }
        } else if (SessionTypeEnum.Team == sessionType) {
            try{
                return TeamHelper.getTeamName(id);
            }catch (Exception e){
                return "";
            }
        }
        return id;
    }

    /**
     * @param account 用户帐号
     * @return
     */
    public static String getUserDisplayName(String account) {
        try {
            RxLogTool.e("getUserDisplayName:"+account);
            String alias = NimUIKit.getContactProvider().getAlias(account);
            if (!TextUtils.isEmpty(alias)) {
                return alias;
            } else {
                NimUserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
                    return userInfo.getName();
                } else {
                    return account;
                }
            }
        }catch (Exception e){
            RxLogTool.e("getUserDisplayName:"+e.getMessage());
        }
        return "---";
    }

    // 获取用户原本的昵称
    public static String getUserName(String account) {
        NimUserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            return "";
        }
    }

    public static String getAvatar(String account) {
        NimUserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getAvatar())) {
            return userInfo.getAvatar();
        } else {
            return "";
        }
    }

    public static String getExtension(String account){

        NimUserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);

        if (userInfo != null && !TextUtils.isEmpty(userInfo.getExtension())) {
            return userInfo.getExtension();
        } else {
            return null;
        }
    }

    /**
     * @param account         账号
     * @param selfNameDisplay 如果是自己，则显示内容
     * @return
     */
    public static String getUserDisplayNameEx(String account, String selfNameDisplay) {
        if (account.equals(NimUIKit.getAccount())) {
            return selfNameDisplay;
        }

        return getUserDisplayName(account);
    }
}
