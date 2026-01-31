package com.example.springBt;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import modelo.Users;

@RestController
@RequestMapping("/api")
public class PerfilController {

    @GetMapping("/login/{usuario}/{password}")
    public Users login(
            @PathVariable("usuario") String usuario,
            @PathVariable("password") String password) {

        return Users.login(usuario, password);
    }

	@GetMapping("/perfil/{id}")
	public Users getPerfil(@PathVariable(name = "id") int id) {
        return Users.getPerfil(id);
    }

	@GetMapping("/profesor/{id}/alumnos")
	public List<Users> getAlumnosDelProfesor(@PathVariable(name = "id") int idProfesor) {
        return Users.getAlumnosDelProfesor(idProfesor);
    }

	@GetMapping("/profesores")
	public List<Users> getProfesores() {
        return Users.getProfesores();
    }
}
