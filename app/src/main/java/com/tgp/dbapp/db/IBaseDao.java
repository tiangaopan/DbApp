package com.tgp.dbapp.db;

import java.util.List;

/**
 * 定义增删改查接口
 * @author 田高攀
 * @since 2020/3/16 5:12 PM
 */
public interface IBaseDao<T> {

    /**
     *  插入
     */
    long insert(T entity);

    /**
     * 更新
     */
    long update(T entity, T where);

    /**
     * 删除
     */
    int delete(T where);

    /**
     * 查询
     */
    List<T> query(T where);

    /**
     * 限制查询
     */
    List<T> query(T where, String orderBy, Integer startIndex, Integer limit);
}

