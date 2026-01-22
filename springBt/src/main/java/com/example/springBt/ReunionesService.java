package com.example.springBt;

import modelo.Reuniones;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ReunionesService {

    public boolean crearReunion(Reuniones reunion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

          
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            reunion.setCreatedAt(ahora);
            reunion.setUpdatedAt(ahora);

            session.persist(reunion);
            tx.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
