package com.example.springBt;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

import modelo.Tipos;
import modelo.Users;

@RestController
@RequestMapping("/api")
public class PerfilController {

	@GetMapping("/perfil/{id}")
	public Users getPerfil(@PathVariable(name = "id") int id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Users user = session.get(Users.class, id);

            if (user == null) {
                return null;
            }

            // Inicializar la relación de tipos
            if (user.getTipos() != null) {
                Hibernate.initialize(user.getTipos());
                // Limpiar colecciones del tipo para evitar proxies
                user.getTipos().setUserses(null);
            }

            // Evitar errores de serialización eliminando todas las colecciones lazy
            user.setMatriculacioneses(null);
            user.setReunionesesForAlumnoId(null);
            user.setReunionesesForProfesorId(null);
            user.setHorarioses(null);

            return user;
        }
    }

	@GetMapping("/profesor/{id}/alumnos")
	public List<Users> getAlumnosDelProfesor(@PathVariable(name = "id") int idProfesor) {

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

	        List<Users> alumnos = session.createQuery(
	            "SELECT DISTINCT m.users " +
	            "FROM Horarios h " +
	            "JOIN h.modulos mod " +
	            "JOIN mod.ciclos c " +
	            "JOIN Matriculaciones m ON m.ciclos.id = c.id " +
	            "WHERE h.users.id = :idProfesor",
	            Users.class
	        )
	        .setParameter("idProfesor", idProfesor)
	        .getResultList();

	        // Limpiar relaciones peligrosas
	        for (Users u : alumnos) {

	            if (u.getTipos() != null) {
	                Hibernate.initialize(u.getTipos());
	                u.getTipos().setUserses(null);
	            }

	            u.setMatriculacioneses(null);
	            u.setReunionesesForAlumnoId(null);
	            u.setReunionesesForProfesorId(null);
	            u.setHorarioses(null);
	        }

	        return alumnos;
	    }
	}


	@GetMapping("/profesores")
	public List<Users> getProfesores() {

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

	        Tipos tipoProfesor = session.get(Tipos.class, 3);

	        List<Users> profesores = session.createQuery(
	                "FROM Users u WHERE u.tipos = :tipoProfesor",
	                Users.class
	        )
	        .setParameter("tipoProfesor", tipoProfesor)
	        .list();

	        for (Users u : profesores) {

	            if (u.getTipos() != null) {
	                Hibernate.initialize(u.getTipos());
	                u.getTipos().setUserses(null);
	            }

	            u.setMatriculacioneses(null);
	            u.setReunionesesForAlumnoId(null);
	            u.setReunionesesForProfesorId(null);
	            u.setHorarioses(null);
	        }

	        return profesores;
	    }
	}
}
