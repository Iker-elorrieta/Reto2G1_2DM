package com.example.springBt;



import org.springframework.web.bind.annotation.*;

import modelo.Users;
@RestController
@RequestMapping("/api")
public class LoginController {


    @GetMapping("/login/{usuario}/{password}")
    public Users login(
            @PathVariable("usuario") String usuario,
            @PathVariable("password") String password) {

        return Users.login(usuario, password);
    }

}




