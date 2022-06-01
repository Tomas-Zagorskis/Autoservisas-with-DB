package com.company.users;

import com.company.Exceptions.UserException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

public class UserService {

    private SessionFactory sessionFactory;

    public UserService() {

        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            throw e;
        }
    }

    public User getUser(String username, String password) {

       Session session = sessionFactory.openSession();

        Query<User> query = session.createQuery("Select u from User u where u.username =:username and u.password=:password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        if (query.getSingleResult() != null) {
            return query.getSingleResult();
        }
        return null;
    }

    public List<User> getAllUsers() {

        Session session = sessionFactory.openSession();

        Query<User> query = session.createQuery("from User", User.class);
        return query.getResultList();
    }

    public void addUser(User user) throws UserException {

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new UserException(String.format("Prisijungimo vardas '%s' yra uzimtas", user.getUsername()));
        }
    }


    public void deleteUserByUsername(String username) throws UserException {

        Session session = sessionFactory.openSession();

        Query<User> q = session.createQuery("Select u from User u where u.username =:username", User.class);
        q.setParameter("username", username);

        User toDelete = null;
        try {
            toDelete = q.getSingleResult();
        }catch (NoResultException e) {
            throw new UserException("Toks vartotojas neegzistuoja");
        }
        Transaction tx = session.beginTransaction();
        try {
            session.delete(toDelete);
            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            session.close();
        }
    }

    public void changeUsersPassword(User user, String password) {

        Session session = sessionFactory.openSession();

        Query<User> q = session.createQuery("Select u from User u where u.username =:username", User.class);
        q.setParameter("username", user.getUsername());

        User toChangePassw = q.getSingleResult();
        toChangePassw.setPassword(password);

        Transaction tx = session.beginTransaction();
        try {
            session.update(toChangePassw);
            tx.commit();
        }catch (Exception e) {
            tx.rollback();
        }finally {
            session.close();
        }
    }
}
