package com.example.springBt;

import modelo.Horarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horario")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/{idProfesor}")
    public ResponseEntity<List<Horarios>> obtenerHorario(
            @PathVariable("idProfesor") int idProfesor) {

        List<Horarios> horario = horarioService.obtenerHorarioProfesor(idProfesor);
        return ResponseEntity.ok(horario);
    }

}
