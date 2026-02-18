package com.idati.auth;

import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody; // NUEVO: Para leer el JSON del body
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RestController;  
import org.springframework.web.bind.annotation.CrossOrigin; // NUEVO: Para que Angular pueda conectarse
import lombok.RequiredArgsConstructor;  

// Anotaciones
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Permite la comunicación con tu frontend
public class AuthController {

    // Inyectamos el servicio que creamos anteriormente
    private final AuthService authService;

    @PostMapping(value = "login")
    public String login(@RequestBody LoginRequest request) {
        // Llamamos a la lógica que valida contra la base de datos
        return authService.login(request);
    }
    
    @PostMapping(value = "register")
    public String register() {
        return "Registro desde un endpoint público";
    }
}