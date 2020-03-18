package com.tgp.dbapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.tgp.dbapp.annotation.DbField;
import com.tgp.dbapp.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 建表
 * @author 田高攀
 * @since 2020/3/16 5:17 PM
 */
public class BaseDao<T> implements IBaseDao<T> {

    private static final String TAG = "BaseDao";

    /**
     * 持有数据库操作的引用
     */
    private SQLiteDatabase sqLiteDatabase;
    /**
     * 表名
     */
    private String tableName;

    /**
     * 持有操作数据库所对应的java类型
     */
    private Class<T> entityClass;

    /**
     * 标记用来表示是否做过初始化操作
     */
    private boolean isInit = false;
    /**
     * 定义一个缓存空间（key 字段名 value 成员变量）
     */
    private HashMap<String, Field> cacheMap;

    public void init(SQLiteDatabase sqLiteDatabase, Class<T> entityClass) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entityClass = entityClass;

        //*************自动建表*************
        //根据传入的entityClass来建表，只需要建一次
        if (!isInit) {
            //取得表名

            if (entityClass.getAnnotation(DbTable.class) == null) {
                //用类名做表名
                this.tableName = entityClass.getSimpleName();
            } else {
                //取得注解上的名字
                this.tableName = entityClass.getAnnotation(DbTable.class).value();
            }
            //执行建表操作
            String createSql = getCreateTableSql();
            Log.i(TAG, createSql);
            this.sqLiteDatabase.execSQL(createSql);
            cacheMap = new HashMap<>();
            initCacheMap();
            isInit = true;
        }

    }

    private void initCacheMap() {
        //取得所有字段名
        // 查询空表
        String sql = "select * from " + tableName + " limit 1, 0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        //获取列名
        String[] columnNames = cursor.getColumnNames();
        //取所有的成员变量
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
        }
        //字段跟对象的成员变量一一对应
        for (String columName : columnNames) {
            Field columnField = null;
            for (Field field : declaredFields) {
                String fieldName = "";
                if (field.getAnnotation(DbField.class) != null) {
                    fieldName = field.getAnnotation(DbField.class).value();
                } else {
                    fieldName = field.getName();
                }

                if (columName.equals(fieldName)) {
                    columnField = field;
                    break;
                }
            }
            if (columnField != null) {
                cacheMap.put(columName, columnField);
            }
        }
    }

    /**
     * 拼接SQL
     */
    private String getCreateTableSql() {
        //create table if not exists tb_user(id integer, name varchar(20))
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(tableName).append("(");
        //反射得到所有的成员变量
        Field[] declaredFields = entityClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //拿到成员类型
            Class<?> type = field.getType();
            if (field.getAnnotation(DbField.class) != null) {
                if (type == String.class) {
                    sb.append(field.getAnnotation(DbField.class).value()).append(" TEXT,");
                } else if (type == Integer.class) {
                    sb.append(field.getAnnotation(DbField.class).value()).append(" Integer,");
                } else if (type == Long.class) {
                    sb.append(field.getAnnotation(DbField.class).value()).append(" BIGINT,");
                } else if (type == Double.class) {
                    sb.append(field.getAnnotation(DbField.class).value()).append(" DOUBLE,");
                } else if (type == byte[].class) {
                    sb.append(field.getAnnotation(DbField.class).value()).append(" BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            } else {
                if (type == String.class) {
                    sb.append(field.getName()).append(" TEXT,");
                } else if (type == Integer.class) {
                    sb.append(field.getName()).append(" Integer,");
                } else if (type == Long.class) {
                    sb.append(field.getName()).append(" BIGINT,");
                } else if (type == Double.class) {
                    sb.append(field.getName()).append(" DOUBLE,");
                } else if (type == byte[].class) {
                    sb.append(field.getName()).append(" BLOB,");
                } else {
                    //不支持的类型
                    continue;
                }
            }
        }
        //去掉最后的逗号
        if (',' == (sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        return sb.toString();
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        for (String key : map.keySet()) {
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    /**
     * key(字段) value(成员变量)
     */

    private Map<String, String> getValueMap(T entity) {
        HashMap<String, String> map = new HashMap<>();
        //返回的是所有的成员变量
        for (Field next : cacheMap.values()) {
            next.setAccessible(true);
            try {
                //获取对象的属性值
                Object o = next.get(entity);
                if (o == null) {
                    continue;
                }
                //张三， 3
                String value = o.toString();
                String key = "";
                if (next.getAnnotation(DbField.class) != null) {
                    key = next.getAnnotation(DbField.class).value();
                } else {
                    key = next.getName();
                }
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public long insert(T entity) {
        //        ContentValues contentValues = new ContentValues();
        //        contentValues.put
        //        sqLiteDatabase.insert()
        //准备好ContentValues中需要的数据
        Map<String, String> map = getValueMap(entity);
        //把数据转移到ContentValues中
        ContentValues contentValues = getContentValues(map);
        //数据库操作
        return sqLiteDatabase.insert(tableName, null, contentValues);
    }

    @Override
    public long update(T entity, T where) {
        //        sqLiteDatabase.update(tableName, contentvalue, "name = ?", new String[]{"tgp"});
        int result = -1;
        Map<String, String> values = getValueMap(entity);
        ContentValues contentValues = getContentValues(values);
        //条件map
        Map<String, String> values1 = getValueMap(where);
        Condition condition = new Condition(values1);
        result = sqLiteDatabase.update(tableName, contentValues, condition.whereClause, condition.whereArgs);
        return result;
    }

    private class Condition {
        String whereClause;
        String[] whereArgs;

        Condition(Map<String, String> values) {
            ArrayList<String> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("1=1");
            //取得所有成员名
            Set<String> keys = values.keySet();
            for (String key : keys) {
                String value = values.get(key);
                if (value != null) {
                    sb.append(" and ").append(key).append("=?");
                    list.add(value);
                }
            }
            this.whereClause = sb.toString();
            whereArgs = list.toArray(new String[list.size()]);
        }
    }

    @Override
    public int delete(T where) {
//        sqLiteDatabase.delete(tableName, "name = ?", new String[]{"tgp"});
        Map<String, String> values = getValueMap(where);
        Condition condition = new Condition(values);
        int delete = sqLiteDatabase.delete(tableName, condition.whereClause, condition.whereArgs);
        return delete;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
//        sqLiteDatabase.query(tableName, null, "od = ?", new String[]{}, null, null, "1,5");
        Map<String, String> values = getValueMap(where);
        String limitString = "";
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }
        Condition condition = new Condition(values);
        Cursor query = sqLiteDatabase.query(tableName, null, condition.whereClause, condition.whereArgs, null, orderBy, limitString);
        List<T> objects = getResult(query, where);
        return objects;
    }

    private List<T> getResult(Cursor query, T where) {
        ArrayList objects = new ArrayList<>();
        Object item = null;
        while (query.moveToNext()) {
            try {
                item = where.getClass().newInstance();
                for (Map.Entry<String, Field> stringFieldEntry : cacheMap.entrySet()) {
                    //获取列名
                    String columnName = stringFieldEntry.getKey();
                    //用列名来拿到列名在游标中的位置
                    int columnIndex = query.getColumnIndex(columnName);
                    Field value = stringFieldEntry.getValue();
                    Class<?> type = value.getType();
                    if (columnIndex != -1) {
                        if (type == String.class) {
                            value.set(item, query.getString(columnIndex));
                        } else if (type == Double.class) {
                            value.set(item, query.getDouble(columnIndex));
                        } else if (type == Integer.class) {
                            value.set(item, query.getInt(columnIndex));
                        } else if (type == Long.class) {
                            value.set(item, query.getLong(columnIndex));
                        } else if (type == byte[].class) {
                            value.set(item, query.getBlob(columnIndex));
                        } else {
                            continue;
                        }
                    }
                }
                objects.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        query.close();
        return objects;
    }
}
