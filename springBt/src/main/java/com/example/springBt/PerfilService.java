package com.example.springBt;

import org.hibernate.Session;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import modelo.Users;

@RestController 
@RequestMapping("/api")
public class PerfilService {

public Users getPerfil(@PathVariable int id) {
	
	
	try(Session session =HibernateUtil.getSessionFactory().openSession()){
		return session.get(Users.class,id);
	}

	
	
	
}
	
}