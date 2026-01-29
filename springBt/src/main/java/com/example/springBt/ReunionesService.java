package com.example.springBt;


import modelo.Reuniones;
import modelo.Users;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReunionesService {

    // Crear reunión
    public boolean crearReunion(Reuniones reunion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Users alumno = session.get(Users.class, reunion.getIdAlumno());
            Users profesor = session.get(Users.class, reunion.getIdProfesor());
          

            reunion.setAlumno(alumno);
            reunion.setProfesor(profesor);
     


            if (alumno == null || profesor == null) {
                throw new RuntimeException("Alumno o profesor no existe");
            }

            reunion.setAlumno(alumno);
            reunion.setProfesor(profesor);

            // 🔹 Timestamps
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            reunion.setCreatedAt(ahora);
            reunion.setUpdatedAt(ahora);

            // 🔹 Persistir
            session.persist(reunion);
            tx.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Servicio para obtener reuniones de un profesor
    public List<Reuniones> obtenerReunionesProfesor(int idProfesor) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<modelo.Reuniones> lista = session.createQuery(
                    "SELECT r FROM Reuniones r WHERE r.profesor.id = :idProfesor",
                    modelo.Reuniones.class
            )
            .setParameter("idProfesor", idProfesor)
            .getResultList();

            List<Reuniones> resultado = new ArrayList<>();

            // Convertimos a DTO con solo IDs
            for (modelo.Reuniones r : lista) {
                Reuniones plano = new Reuniones();
                plano.setIdReunion(r.getIdReunion());
                plano.setIdAlumno(r.getAlumno().getId());
                plano.setIdProfesor(r.getProfesor().getId());
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
