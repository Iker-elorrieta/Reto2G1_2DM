package com.example.springBt;

import org.hibernate.Session;
import modelo.Users;

public class LoginService {

    public Users login(String username, String password) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            Users user = session.createQuery(
                "FROM Users u JOIN FETCH u.tipos WHERE u.username = :username",
                Users.class
            )
            .setParameter("username", username)
            .uniqueResult();

            if (user == null) return null;
            if (!user.getPassword().equals(password)) return null;

            return user; 
        }
    }
}
