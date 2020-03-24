package com.tgp.dbapp.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 工厂
 * @author 田高攀
 * @since 2020/3/16 5:46 PM
 */
public class BaseDaoFactory {

    private static BaseDaoFactory sInstance;
    private SQLiteDatabase sqLiteDatabase;
    /**
     * 设计一个数据库的连接池
     */
    protected Map<String, BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());
    /**
     * 定义建数据库的路径，建议写到sd卡中，这样下次安装数据还在
     */
    private String sqliteDatabasePath;

    public static BaseDaoFactory getInstance() {
        if (sInstance == null) {
            synchronized (BaseDaoFactory.class) {
                if (sInstance == null) {
                    sInstance = new BaseDaoFactory();
                }
            }
        }
        return sInstance;
    }

    private BaseDaoFactory() {
//        Environment.getExternalStorageDirectory() + File.separator;
        sqliteDatabasePath = "data/data/com.tgp.dbapp/test.db";
        //新建数据库
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDatabasePath, null);
    }

    public <T> BaseDao<T> getBaseDao(Class<T> entity) {
        BaseDao baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    public <T extends BaseDao<M>, M> T getBaseDao(Class<T> daoClass, Class<M> entity){
        BaseDao baseDao = null;
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase, entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T)baseDao;
    }
}
