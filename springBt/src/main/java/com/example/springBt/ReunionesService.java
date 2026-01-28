package com.example.springBt;

import modelo.Reuniones;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;



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
    @GetMapping("/reuniones/profesor/{idProfesor}")
    public List<Reuniones> getReunionesProfesor(@PathVariable int idProfesor) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<modelo.Reuniones> lista = session.createQuery(
                "SELECT r FROM Reuniones r WHERE r.usersByProfesorId.id = :idProfesor",
                modelo.Reuniones.class
            )
            .setParameter("idProfesor", idProfesor)
            .getResultList();

            List<Reuniones> resultado = new ArrayList<>();

            for (modelo.Reuniones r : lista) {
                Reuniones plano = new Reuniones();
                plano.setIdReunion(r.getIdReunion());
                plano.setIdAlumno(r.getUsersByAlumnoId().getId());
                plano.setIdProfesor(r.getUsersByProfesorId().getId());
                plano.setEstado(r.getEstado());
                plano.setEstadoEus(r.getEstadoEus());
                plano.setIdCentro(r.getIdCentro());
                plano.setTitulo(r.getTitulo());
                plano.setAsunto(r.getAsunto());
                plano.setAula(r.getAula());
                plano.setFecha(r.getFecha());
                resultado.add(plano);
            }

            return resultado;
        }
    }
    

  

    }



    
    

