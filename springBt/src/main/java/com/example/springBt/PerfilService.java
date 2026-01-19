package com.example.springBt;



import org.hibernate.Hibernate;

import org.hibernate.Session;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;



import modelo.Users;



@RestController 

@RequestMapping("/api")

public class PerfilService {



@RequestMapping("/perfil/{id}")

public Users getPerfil(@PathVariable("id") int id) {

	

	

	try(Session session =HibernateUtil.getSessionFactory().openSession()){

		Users user = session.get(Users.class, id);

        if (user != null) {

            Hibernate.initialize(user.getTipos());

        }

        return user;

	}



	

	

	

}

	

}