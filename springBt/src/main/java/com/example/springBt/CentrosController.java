package com.example.springBt;

import modelo.Centro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centros")
@CrossOrigin(origins = "*")
public class CentrosController {


    /**
     * Obtiene todos los centros
     * GET /api/centros
     */
    @GetMapping
    public ResponseEntity<List<Centro>> obtenerTodosCentros() {
        List<Centro> centros = Centro.obtenerTodosCentros();
        return ResponseEntity.ok(centros);
    }

    /**
     * Obtiene un centro por su código
     * GET /api/centros/{ccen}
     */
    @GetMapping("/{ccen}")
    public ResponseEntity<Centro> obtenerCentroPorCodigo(@PathVariable String ccen) {
        Centro centro = Centro.obtenerCentroPorCodigo(ccen);
        if (centro != null) {
            return ResponseEntity.ok(centro);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene centros por municipio
     * GET /api/centros/municipio/{municipio}
     */
    @GetMapping("/municipio/{municipio}")
    public ResponseEntity<List<Centro>> obtenerCentrosPorMunicipio(@PathVariable String municipio) {
        List<Centro> centros = Centro.obtenerCentrosPorMunicipio(municipio);
        return ResponseEntity.ok(centros);
    }

    /**
     * Obtiene el número total de centros
     * GET /api/centros/count
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> obtenerNumeroCentros() {
        int count = Centro.obtenerNumeroCentros();
        return ResponseEntity.ok(count);
    }
}
