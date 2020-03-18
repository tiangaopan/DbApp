package com.tgp.dbapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tgp.dbapp.bean.Person;
import com.tgp.dbapp.bean.User;
import com.tgp.dbapp.bean.UserImg;
import com.tgp.dbapp.db.BaseDao;
import com.tgp.dbapp.db.BaseDaoFactory;
import com.tgp.dbapp.db.BaseDaoNewImpl;
import com.tgp.dbapp.db.PhotoDao;
import com.tgp.dbapp.sqlite.BaseDaoSubFactory;
import com.tgp.dbapp.sqlite.UserDao;
import com.tgp.dbapp.update.UpdataManager;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author gaopantian
 */
public class MainActivity extends AppCompatActivity {

    private int count = 0;

    UpdataManager mUpdataManger = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUpdataManger = new UpdataManager();
    }

    public void insert(View view) {
        BaseDao<User> baseDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        baseDao.insert(new User("1", "tgp", "123"));
        Toast.makeText(MainActivity.this, "执行成功", Toast.LENGTH_SHORT).show();
        BaseDao<Person> person = BaseDaoFactory.getInstance().getBaseDao(Person.class);
        person.insert(new Person(1, "tgpperson", "123person"));

    }

    public void update(View view) {
        BaseDaoNewImpl baseDao = BaseDaoFactory.getInstance().getBaseDao(BaseDaoNewImpl.class, Person.class);
        Person person = new Person();
        person.setName("tgp update");
        Person where = new Person();
        where.setId(1);
        long update = baseDao.update(person, where);
        Toast.makeText(MainActivity.this, "执行成功" + update, Toast.LENGTH_SHORT).show();

    }

    public void delete(View view) {
        BaseDao<Person> baseDao = BaseDaoFactory.getInstance().getBaseDao(Person.class);
        Person where = new Person();
        where.setId(1);
        baseDao.delete(where);
    }

    public void quary(View view) {
        BaseDao<Person> baseDao = BaseDaoFactory.getInstance().getBaseDao(Person.class);
        Person where = new Person();
        where.setId(1);
        List<Person> query = baseDao.query(where);
        Toast.makeText(MainActivity.this, "执行成功,个数" + query.size(), Toast.LENGTH_SHORT).show();

    }

    public void mutilLogin(View view) {
        User user = new User();
        user.setName("tgpmuilt" + (++count));
        user.setPassword("123iii" + count);
        user.setId("No" + count);
        UserDao baseDao = BaseDaoFactory.getInstance().getBaseDao(UserDao.class, User.class);
        baseDao.insert(user);
    }

    public void mutlInsert(View view) {
        UserImg img = new UserImg(new Date().toString(), "www.ali.com/" + (++count));
        PhotoDao baseDao = BaseDaoSubFactory.getInstance().getSubDao(PhotoDao.class, UserImg.class);
        baseDao.insert(img);
    }

    public void write(View view) {
        mUpdataManger.saveVersionInfo(this, "V003");
    }

    public void updatewrite(View view) {
        mUpdataManger.checkThisVersionTable(this);
        mUpdataManger.startUpdataDb(this);
    }
}
