package Dao.DaoImpl;

import Bean.Relation;
import Bean.User;
import Dao.UserDao;
import org.apache.struts2.ServletActionContext;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import javax.servlet.Servlet;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {
    @Override
    public User findByPassword(String username, String password) {
        return getHibernateTemplate().execute(new HibernateCallback<User>() {
            @Override
            public User doInHibernate(Session session) throws HibernateException {
                String sql = "from User where username = ? and password = ?";
                User user_sql = (User) session.createQuery(sql)
                        .setParameter(0, username)
                        .setParameter(1, password)
                        .uniqueResult();
                return user_sql;
            }
        });
    }

    @Override
    public void addUser(User user) {
        getHibernateTemplate().save(user);
    }

    @Override
    public void follow(String uid, String follow_id) {
        //增加一个关注用户
        Relation follow = new Relation();
        follow.setUid(uid);
        follow.setFollow_uid(follow_id);
        follow.setType(1);

        getHibernateTemplate().save(follow);

        //增加一个粉丝用户
        Relation fans = new Relation();
        fans.setUid(follow_id);
        fans.setFollow_uid(uid);
        fans.setType(2);

        getHibernateTemplate().save(fans);
    }

    @Override
    public User findUserById(String showUserId) {
        return getHibernateTemplate().execute(new HibernateCallback<User>() {
            @Override
            public User doInHibernate(Session session) throws HibernateException {
                return session.get(User.class, showUserId);
            }
        });
    }

    @Override
    public User findUserByUsername(String username) {
        return getHibernateTemplate().execute(new HibernateCallback<User>() {
            @Override
            public User doInHibernate(Session session) throws HibernateException {
                return (User) session.createQuery("from User where username = ?")
                        .setParameter(0, username)
                        .uniqueResult();
            }
        });
    }
}
