package com.tgp.dbapp.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tgp.dbapp.db.BaseDao;
import com.tgp.dbapp.db.BaseDaoFactory;

/**
 * 类的大体描述放在这里
 * @author 田高攀
 * @since 2020/3/17 5:29 PM
 */
public class BaseDaoSubFactory extends BaseDaoFactory {

    private static final String TAG = "BaseDaoSubFactory";

    private static BaseDaoSubFactory sInstance;

    public static BaseDaoSubFactory getInstance() {
        if (sInstance == null) {
            synchronized (BaseDaoFactory.class) {
                if (sInstance == null) {
                    sInstance = new BaseDaoSubFactory();
                }
            }
        }
        return sInstance;
    }

    /**
     * 定义一个用于实现数据库分库的操作对象
     */

    private SQLiteDatabase sqLiteDatabase;

    public <T extends BaseDao<M>, M> T getSubDao(Class<T> daoClass, Class<M> entity){
        BaseDao baseDao = null;
        Log.e(TAG, PrivateDataBaseEnum.DATEBASE.getValue());
        if (map.get(PrivateDataBaseEnum.DATEBASE.getValue()) != null) {
            return (T) map.get(PrivateDataBaseEnum.DATEBASE.getValue());
        }

        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnum.DATEBASE.getValue(), null);
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entity);
            map.put(PrivateDataBaseEnum.DATEBASE.getValue(), baseDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }

}
