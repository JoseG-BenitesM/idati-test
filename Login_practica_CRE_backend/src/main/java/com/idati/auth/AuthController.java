package com.idati.auth;

import org.springframework.web.bind.annotation.PostMapping; // Para mandar datos al server (POST requests)
import org.springframework.web.bind.annotation.RequestMapping;  // Usado para definir rutas HTTP en un controlador web como @RequestMapping("/ruta")
import org.springframework.web.bind.annotation.RestController;  // Permite usar @RestController en clases para manejar solicitudes HTTP y devolver JSONs
import lombok.RequiredArgsConstructor;  // Genera automáticamente un constructor, disminuyendo código repetitivo

// Anotaciones
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @PostMapping(value = "login")
    public String login() {return "Login desde un endpoint público";}
    
    @PostMapping(value = "register")
    public String register() {return "Registro desde un endpoint público";}
}
