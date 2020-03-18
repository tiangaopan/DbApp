package com.tgp.dbapp.db;

import java.util.List;

/**
 * 对BaseDao进行扩展
 * @author 田高攀
 * @since 2020/3/17 4:30 PM
 */
public class BaseDaoNewImpl<T> extends BaseDao<T> {

    public List<T> query(String sql) {
        return null;
    }
}
