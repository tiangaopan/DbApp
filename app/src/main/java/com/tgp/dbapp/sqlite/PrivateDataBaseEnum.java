package com.tgp.dbapp.sqlite;

import com.tgp.dbapp.bean.User;
import com.tgp.dbapp.db.BaseDaoFactory;

import java.io.File;

/**
 * 类的大体描述放在这里
 * @author 田高攀
 * @since 2020/3/17 5:34 PM
 */
public enum PrivateDataBaseEnum {

    /**
     * 实例对象
     */
    DATEBASE("");

    private String value;

    /**
     * 获取路径,根据登陆的对象id去生成对应的路径
     */
    public String getValue() {
        UserDao baseDao = BaseDaoFactory.getInstance().getBaseDao(UserDao.class, User.class);
        if (baseDao != null) {
            User currentUser = baseDao.getCurrentUser();
            if (currentUser != null) {
                File file = new File("data/data/com.tgp.dbapp");
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file.getAbsolutePath() + "/" + currentUser.getId() + "_login.db";
            }

        }

        return value;
    }

    PrivateDataBaseEnum(String value) {
        this.value = value;
    }
}
