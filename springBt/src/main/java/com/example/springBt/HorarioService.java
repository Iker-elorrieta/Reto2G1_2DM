package com.example.springBt;

import modelo.Horarios;
import modelo.Users;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class HorarioService {

    public List<Horarios> obtenerHorarioProfesor(Integer idProfesor) {
        List<Horarios> resultado = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Users profesor = session.get(Users.class, idProfesor);

            List<modelo.Horarios> lista = session.createQuery(
                "SELECT h FROM Horarios h WHERE h.users = :profesor ORDER BY h.dia, h.hora",
                modelo.Horarios.class)
            .setParameter("profesor", profesor)
            .getResultList();

            for (modelo.Horarios h : lista) {

                // Inicializar solo lo necesario
                Hibernate.initialize(h.getModulos());

                // Convertir a objeto plano
                Horarios plano = new Horarios(h);

                resultado.add(plano);
            }
        }

        return resultado;
    }
}


