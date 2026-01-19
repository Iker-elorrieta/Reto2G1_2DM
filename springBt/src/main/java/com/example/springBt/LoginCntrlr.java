package com.example.springBt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import modelo.Users;

@RestController
@RequestMapping("/api")
public class LoginCntrlr {

    private LoginService loginService = new LoginService();

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> datos) {

        String username = datos.get("username");
        String password = datos.get("password");

        Users user = loginService.login(username, password);

        Map<String, Object> respuesta = new HashMap<>();

        if (user == null) {
            respuesta.put("codigo", 0);
            respuesta.put("id", -1);
            return respuesta;
        }

        int codigo;
        if (user.getTipos().getId() == 4) {
            codigo = 2;
        } else {
            codigo = 1;
        }


        respuesta.put("codigo", codigo);
        respuesta.put("id", user.getId());

        return respuesta;
    }
}
