package com.example.springBt;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import modelo.Users;
@RestController
@RequestMapping("/api")
public class AlumnoService {

    @GetMapping("/profesor/{id}/alumnos")
    public List<Users> getAlumnosDelProfesor(@PathVariable(name = "id") int idProfesor) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Users> alumnos = session.createQuery(
                "SELECT DISTINCT r.usersByAlumnoId FROM Reuniones r WHERE r.usersByProfesorId.id = :idProfesor",
                Users.class
            )
            .setParameter("idProfesor", idProfesor)
            .getResultList();

            for (Users u : alumnos) {
                // Inicializar tipos y limpiar sus colecciones
                if (u.getTipos() != null) {
                    Hibernate.initialize(u.getTipos());
                    u.getTipos().setUserses(null);
                }
                
                // Limpiar colecciones del usuario
                u.setMatriculacioneses(null);
                u.setReunionesesForAlumnoId(null);
                u.setReunionesesForProfesorId(null);
                u.setHorarioses(null);
            }

            return alumnos;
        }
    }

    public List<Users> getProfesores() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Users> profesores = session.createQuery(
                    "FROM Users WHERE tipos.name = 'profesor'", Users.class
            ).getResultList();

            for (Users u : profesores) {

                // Inicializar tipo
                if (u.getTipos() != null) {
                    Hibernate.initialize(u.getTipos());
                    u.getTipos().setUserses(null);
                }

                // Limpiar colecciones LAZY
                u.setMatriculacioneses(null);
                u.setReunionesesForAlumnoId(null);
                u.setReunionesesForProfesorId(null);
                u.setHorarioses(null);
            }

            return profesores;
        }
    }

}
