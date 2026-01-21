package com.example.springBt;

import modelo.Horarios;
import modelo.Modulos;

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

            resultado = session.createQuery(
                    "SELECT h FROM Horarios h WHERE h.users.id = :idProfesor ORDER BY h.dia, h.hora",
                    Horarios.class)
            .setParameter("idProfesor", idProfesor)
            .getResultList();

            for (Horarios h : resultado) {

                // Inicializar SIEMPRE el módulo
                Hibernate.initialize(h.getModulos());
                Modulos m = h.getModulos();

                if (m != null) {
                    Hibernate.initialize(m.getId());
                    Hibernate.initialize(m.getNombre());
                    Hibernate.initialize(m.getNombreEus());
                    Hibernate.initialize(m.getHoras());
                    Hibernate.initialize(m.getCurso());
                    Hibernate.initialize(m.getCiclos());

                    m.setHorarioses(null);
                    m.setCiclos(null);
                }

                h.setUsers(null);
            }

        }

        return resultado;
    }
}
