package com.company.client;

import com.company.Budget;
import com.company.Exceptions.AutoException;
import com.company.Exceptions.ClientException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.*;

import static com.company.Main.FEE_INDEX;

public class ClientService {

    private SessionFactory sessionFactory;

    public ClientService() {

        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            throw e;
        }
    }

    public void addClient(Client client) throws AutoException {

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(client);
            tx.commit();
        } catch (PersistenceException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new AutoException("Sis automobilis jau remontuojamas");
        }
    }

    public List<Client> getAllAutos(boolean fixed) throws AutoException {

        Session session = sessionFactory.openSession();

        Query<Client> query = session.createQuery("Select c from Client c where fixed =:isFixed", Client.class);
        query.setParameter("isFixed", fixed);
        List<Client> allAutos = null;
        try {
            allAutos = query.getResultList();
        }catch (NoResultException e) {
            throw new AutoException("Sarasas tuscias");
        }
        if (allAutos.isEmpty()){
            throw new AutoException("Sarasas tuscias");
        }
        return allAutos;
    }

    public List<Client> getAllAutosForClient(String name, String surname) throws ClientException {

        Session session = sessionFactory.openSession();

        Query<Client> query = session.createQuery("Select c from Client c where name =:name and surname =:surname", Client.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        List<Client> autosByClient = query.getResultList();
        if (autosByClient.isEmpty()) {
            throw new ClientException(String.format("Klientas, %s %s, remontuojamu automobiliu neturi\n", name, surname));
        }
        return autosByClient;
    }

    public Client getAutoToReturn(String plateNr) throws AutoException {

        Session session = sessionFactory.openSession();

        Query<Client> q = session.createQuery("Select c from Client c where plate_nr =:plateNr", Client.class);
        q.setParameter("plateNr", plateNr);

        Client toReturn = null;
        try {
            toReturn = q.getSingleResult();
        }catch (NoResultException e) {
            throw new AutoException("Tokio automobilio nera arba jis dar nesuremontuotas");
        }

        Transaction tx = session.beginTransaction();
        try {
            session.delete(toReturn);
            updateBudget(toReturn);
            tx.commit();
        }catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }

        return toReturn;
    }


    public void markAutoAsFixed(String plateNr, double costs) throws ClientException {

        Session session = sessionFactory.openSession();

        Query<Client> q = session.createQuery("Select c from Client c where plate_nr =:plateNr", Client.class);
        q.setParameter("plateNr", plateNr);

        Client toUpdate = null;
        try {
            toUpdate = q.getSingleResult();
        }catch (NoResultException e) {
            throw new ClientException(String.format("Auto su valstybiniais numeriais: %s nerasta\n", plateNr));
        }
        if (!toUpdate.isFixed()){
            toUpdate.setFixed(true);
            toUpdate.setCosts(costs);

            Transaction tx = session.beginTransaction();
            session.update(toUpdate);
            tx.commit();
        }
        throw new ClientException(String.format("Auto su valstybiniais numeriais: %s jau yra uzregistruotas kaip sutvarkytas", plateNr));
    }

    public double getIncome() {

        Session session = sessionFactory.openSession();

        Query<Double> incomeQuery  = session.createQuery("SELECT SUM(b.income) FROM Budget b ", Double.class);

        return incomeQuery.getSingleResult();
    }

    public double getExpenses() {

        Session session = sessionFactory.openSession();

        Query<Double> incomeQuery  = session.createQuery("SELECT SUM(b.expenses) FROM Budget b ", Double.class);

        return incomeQuery.getSingleResult();
    }

    private void updateBudget(Client client) {

        Session session = sessionFactory.openSession();

        Budget budget = new Budget();
        budget.setExpenses(client.getCosts());
        budget.setIncome(client.getCosts() * FEE_INDEX);

        Transaction tx = session.beginTransaction();
        session.save(budget);
        tx.commit();
    }

}
