package com.tgp.dbapp.bean;

import com.tgp.dbapp.annotation.DbField;
import com.tgp.dbapp.annotation.DbTable;

/**
 * 类的大体描述放在这里
 * @author 田高攀
 * @since 2020/3/16 4:10 PM
 */

@DbTable("tb_user")
public class User {

    @DbField("_id")
    private String id;
    private String name;
    private String password;
    private Integer status;

    public User() {

    }

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}
