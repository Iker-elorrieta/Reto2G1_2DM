package com.example.springBt;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import modelo.Tipos;
import modelo.Users;
@RestController
@RequestMapping("/api")
public class AlumnoService {

	@GetMapping("/profesor/{id}/alumnos")
	public List<Users> getAlumnosDelProfesor(@PathVariable(name = "id") int idProfesor) {

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {

	        // Cargar entidad completa del profesor
	        Users profesor = session.get(Users.class, idProfesor);

	        List<Users> alumnos = session.createQuery(
	            "SELECT DISTINCT r.usersByAlumnoId FROM Reuniones r WHERE r.usersByProfesorId = :profesor",
	            Users.class
	        )
	        .setParameter("profesor", profesor)
	        .getResultList();

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
