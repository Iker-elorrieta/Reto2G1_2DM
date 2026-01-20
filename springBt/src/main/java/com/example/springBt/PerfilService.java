package com.example.springBt;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

import modelo.Users;
@RestController
@RequestMapping("/api")
public class PerfilService {

	@GetMapping("/perfil/{id}")
	public Users getPerfil(@PathVariable(name = "id") int id) {


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Users user = session.get(Users.class, id);

            if (user == null) {
                return null;
            }

            Hibernate.initialize(user.getTipos());

            // Evitar errores de serialización
            user.setMatriculacioneses(null);
            user.setReunionesesForAlumnoId(null);
            user.setReunionesesForProfesorId(null);
            user.setHorarioses(null);

            return user;
        }
    }
}
