package com.example.springBt;

import modelo.Horarios;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horario")
public class HorarioController {


    @GetMapping("/{idProfesor}")
    public ResponseEntity<List<Horarios>> obtenerHorario(
            @PathVariable("idProfesor") int idProfesor) {

        List<Horarios> horario = Horarios.obtenerHorarioProfesor(idProfesor);
        return ResponseEntity.ok(horario);
    }

}
