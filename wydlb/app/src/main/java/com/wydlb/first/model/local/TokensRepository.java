package com.wydlb.first.model.local;


import com.wydlb.first.model.bean.TokenBean;
import com.wydlb.first.model.gen.DaoSession;
import com.wydlb.first.model.gen.TokenBeanDao;
import com.wydlb.first.utils.RxLogTool;

import java.util.List;


public class TokensRepository {
    private static final String TAG = "TokenBeanManager";
    private static volatile TokensRepository sInstance;
    private DaoSession mSession;
    private TokenBeanDao tokenBeanDao;
    private TokensRepository(){
        mSession = DaoDbHelper.getInstance()
                .getSession();
        tokenBeanDao = mSession.getTokenBeanDao();
    }

    public static TokensRepository getInstance(){
        if (sInstance == null){
            synchronized (TokensRepository.class){
                if (sInstance == null){
                    sInstance = new TokensRepository();
                }
            }
        }
        return sInstance;
    }



    public void saveTokens(List<TokenBean> beans){
        tokenBeanDao.insertOrReplaceInTx(beans);
    }

    public void saveTokens(TokenBean bean){
        mSession.getTokenBeanDao()
                .insertOrReplace(bean);
    }

    public TokenBean getToken(){
        long currentTime=System.currentTimeMillis();
        List<TokenBean> bean = tokenBeanDao.queryBuilder()
                .where(TokenBeanDao.Properties.ExpireTime.gt(currentTime)).list();
        if (null!=bean && !bean.isEmpty()){
            RxLogTool.e("TokensRepository currentTime:"+currentTime);
            RxLogTool.e("TokensRepository getToken:"+bean.toString());
            return bean.get(0);
        }
        return null;
    }


    public void deleteTokenBean(TokenBean tokenBean){
        tokenBeanDao.delete(tokenBean);
    }


    public DaoSession getSession(){
        return mSession;
    }
}
