package com.example.springBt;

import modelo.Horarios;
import modelo.Users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horario")
public class HorarioController {


    @GetMapping("")
    public ResponseEntity<List<Horarios>> getAllHorarios() {
        List<Horarios> horarios = Horarios.obtenerTodosHorarios();
        return ResponseEntity.ok(horarios);
    }

    @GetMapping("{idUsuario}")
    public ResponseEntity<List<Horarios>> getHorarioByUsuario(@PathVariable("idUsuario") int idUsuario) {
        String tipo = Users.getTipoUser(idUsuario);
        List<Horarios> horarios;
        if ("Profesor".equalsIgnoreCase(tipo)) {
            horarios = Horarios.obtenerHorarioProfesor(idUsuario);
        } else {
            horarios = Horarios.obtenerHorarioAlumno(idUsuario);
        }
        return ResponseEntity.ok(horarios);
    }

    @PostMapping("/crear")
    public ResponseEntity<Boolean> crearHorario(@RequestBody Horarios horario) {
        boolean creado = Horarios.crearHorario(horario);
        return ResponseEntity.ok(creado);
    }

}
