package com.example.springBt;

import modelo.Horarios;


import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioService {

	public List<Horarios> obtenerHorarioProfesor(Integer idProfesor) {
	    List<Horarios> resultado = new ArrayList<>();

	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        List<Horarios> lista = session.createQuery(
	            "SELECT h FROM Horarios h WHERE h.users.id = :idProfesor ORDER BY h.dia, h.hora",
	            Horarios.class)
	        .setParameter("idProfesor", idProfesor)
	        .getResultList();

	        for (Horarios h : lista) {
	            Hibernate.initialize(h.getModulos());
	            if (h.getModulos() != null) {
	                h.setNombreModulo(h.getModulos().getNombre());
	            }
	            resultado.add(h); 
	        }

	    }

	    return resultado;
	}

}
