package com.example.springBt;

import java.util.ArrayList;
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

	        // 1. Obtener los ciclos que imparte el profesor
	        List<Integer> ciclosIds = session.createQuery(
	            "SELECT DISTINCT h.modulos.ciclos.id FROM Horarios h WHERE h.users.id = :idProfesor",
	            Integer.class
	        )
	        .setParameter("idProfesor", idProfesor)
	        .getResultList();

	        if (ciclosIds.isEmpty()) return new ArrayList<>();

	        // 2. Buscar alumnos matriculados en esos ciclos
	        List<Users> alumnos = session.createQuery(
	            "SELECT DISTINCT m.users FROM Matriculaciones m WHERE m.ciclos.id IN (:ids)",
	            Users.class
	        )
	        .setParameter("ids", ciclosIds)
	        .getResultList();

	        // 3. Limpiar relaciones peligrosas
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
