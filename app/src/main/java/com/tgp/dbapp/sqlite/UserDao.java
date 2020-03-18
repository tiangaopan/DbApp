package com.tgp.dbapp.sqlite;

import com.tgp.dbapp.bean.User;
import com.tgp.dbapp.db.BaseDao;

import java.util.List;

/**
 * 多库操作，将关联表中的已有数据的标记重置
 * @author 田高攀
 * @since 2020/3/17 5:14 PM
 */
public class UserDao extends BaseDao<User> {

    @Override
    public long insert(User entity) {
        List<User> userList = query(new User());
        User where = null;
        for (User user : userList) {
            where = new User();
            where.setId(user.getId());
            user.setStatus(0);
            update(user, where);
        }
        entity.setStatus(1);
        return super.insert(entity);
    }

    public User getCurrentUser() {
        User user = new User();
        user.setStatus(1);
        List<User> query = query(user);
        if (!query.isEmpty()) {
            return query.get(0);
        }
        return null;
    }

}
