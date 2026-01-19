package com.example.springBt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


import modelo.Users;

public class LoginService {

    private Map<String, Users> mapaUsuarios = new HashMap<>();

    public LoginService() {
    	
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            List<Users> lista = session.createQuery("FROM Users", Users.class).list();

            for (Users u : lista) {
                mapaUsuarios.put(u.getUsername(), u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Users login(String username, String password) {

        Users user = mapaUsuarios.get(username);

        if (user == null) {
            return null;
        }

        if (!user.getPassword().equals(password)) {
            return null;
        }

        return user;
    }
}
