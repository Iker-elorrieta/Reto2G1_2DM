package com.example.springBt;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import modelo.Reuniones;

@RestController
@RequestMapping("/api/reuniones")
public class ReunionesController {

    @PostMapping("/crear")
    public ResponseEntity<Boolean> crearReunion(@RequestBody Reuniones reunion) {
        boolean creada = reunion.crearReunion();
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/profesor/{idProfesor}")
    public List<Reuniones> getReunionesProfesor(@PathVariable int idProfesor) {
        return Reuniones.obtenerReunionesProfesor(idProfesor);
    }
}
