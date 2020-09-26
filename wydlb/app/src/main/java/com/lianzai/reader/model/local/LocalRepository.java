package com.lianzai.reader.model.local;

import com.lianzai.reader.model.bean.DownloadTaskBean;
import com.lianzai.reader.model.gen.DaoSession;
import com.lianzai.reader.utils.RxLogTool;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;


/**
 * Created by newbiechen on 17-4-26.
 */

public class LocalRepository implements SaveDbHelper,GetDbHelper,DeleteDbHelper{
    private static final String TAG = "LocalRepository";
    private static final String DISTILLATE_ALL = "normal";
    private static final String DISTILLATE_BOUTIQUES = "distillate";

    private static volatile LocalRepository sInstance;
    private DaoSession mSession;
    private LocalRepository(){
        mSession = DaoDbHelper.getInstance().getSession();
    }

    public static LocalRepository getInstance(){
        if (sInstance == null){
            synchronized (LocalRepository.class){
                if (sInstance == null){
                    sInstance = new LocalRepository();
                }
            }
        }
        return sInstance;
    }

    /*************************************数据存储*******************************************/





    @Override
    public void saveDownloadTask(DownloadTaskBean bean) {
        //目录不需要缓存了-已经存过了 添加下载任务数据
//        BookRepository.getInstance()
//                .saveBookChaptersWithAsync(bean.getBookChapters());
        mSession.getDownloadTaskBeanDao()
                .insertOrReplace(bean);
    }

    /***************************************read data****************************************************/


    @Override
    public List<DownloadTaskBean> getDownloadTaskList() {
        return mSession.getDownloadTaskBeanDao()
                .loadAll();
    }

    private <T> void queryOrderBy(QueryBuilder queryBuilder, Class<T> daoCls,String orderBy){
        //获取Dao中的Properties
        Class<?>[] innerCls = daoCls.getClasses();
        Class<?> propertiesCls = null;
        for (Class<?> cls : innerCls){
            if (cls.getSimpleName().equals("Properties")){
                propertiesCls = cls;
                break;
            }
        }
        //如果不存在则返回
        if (propertiesCls == null) return;

        //这里没有进行异常处理有点小问题
        try {
            Field field = propertiesCls.getField(orderBy);
            Property property = (Property) field.get(propertiesCls);
            queryBuilder.orderDesc(property);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            RxLogTool.e(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            RxLogTool.e(e);
        }
    }

    private <T> Single<List<T>> queryToRx(QueryBuilder<T> builder){
        return Single.create(new SingleOnSubscribe<List<T>>() {
            @Override
            public void subscribe(SingleEmitter<List<T>> e) throws Exception {
                List<T> data = builder.list();
                if (data == null){
                    data = new ArrayList<T>(1);
                }
                e.onSuccess(data);
            }
        });
    }

    /*******************************************************************************************/
    /**
     * 处理多出来的数据,一般在退出程序的时候进行
     */
    public void disposeOverflowData(){
        //固定存储100条数据，剩下的数据都删除
        mSession.startAsyncSession()
                .runInTx(
                        ()->{
                        }
                );
    }


    @Override
    public void deleteAll() {
        //清空全部数据。
    }
}
