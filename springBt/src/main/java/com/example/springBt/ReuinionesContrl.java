package com.example.springBt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import modelo.Reuniones;

@RestController 
@RequestMapping("/api/reuniones")
public class ReuinionesContrl {
	    @Autowired
	    private ReunionesService reunionService;

	    @PostMapping("/crear")
	    public ResponseEntity<Boolean> crearReunion(@RequestBody Reuniones reunion) {
	        boolean creada = reunionService.crearReunion(reunion);
	        return ResponseEntity.ok(creada);
	    }
	

	
}
