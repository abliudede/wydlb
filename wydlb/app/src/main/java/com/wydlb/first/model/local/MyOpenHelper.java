package com.wydlb.first.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wydlb.first.model.gen.DaoMaster;
import com.wydlb.first.model.gen.TokenBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by newbiechen on 2017/10/9.
 */

public class MyOpenHelper extends DaoMaster.DevOpenHelper{
    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (oldVersion<newVersion && oldVersion >= 37){
            //更新数据
            MigrationHelper.getInstance().migrate(db,
                    TokenBeanDao.class
            );
        }else if(oldVersion<newVersion && oldVersion < 37){//再往前的数据库无法更新
            //更新数据
            MigrationHelper.getInstance().migrate(db,
                    TokenBeanDao.class
            );
        }

    }
}
