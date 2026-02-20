package com.example.demo.auth;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login") // Ruta: http://localhost:8080/auth/login
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    
    @PostMapping("/register") // Ruta: http://localhost:8080/auth/register
    public String register() {
        return "Registro desde un endpoint público";
    }
}