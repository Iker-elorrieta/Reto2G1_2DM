package com.example.springBt;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
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

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<modelo.Reuniones> lista = session.createQuery(
                "SELECT r FROM Reuniones r WHERE r.profesor.id = :idProfesor",
                modelo.Reuniones.class
            )
            .setParameter("idProfesor", idProfesor)
            .getResultList();

            List<Reuniones> resultado = new ArrayList<>();

            for (modelo.Reuniones r : lista) {

                Reuniones plano = new Reuniones();
                plano.setIdReunion(r.getIdReunion());
                plano.setIdAlumno(r.getAlumno().getId());
                plano.setIdProfesor(r.getProfesor().getId());
                plano.setEstado(r.getEstado());
                plano.setEstadoEus(r.getEstadoEus());
                plano.setIdCentro(r.getIdCentro());
                plano.setTitulo(r.getTitulo());
                plano.setAsunto(r.getAsunto());
                plano.setAula(r.getAula());
                plano.setFecha(r.getFecha());

                resultado.add(plano);
            }

            return resultado;
        }
    }
}
