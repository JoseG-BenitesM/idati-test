package com.example.demo.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "https://login-idate.netlify.app"})
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            Map<String, String> response = authService.login(request);
            return ResponseEntity.ok(response); // HTTP 200 con el body
        } catch (RuntimeException e) {
            String mensaje = e.getMessage(); if(mensaje.equals("CUENTA_BLOQUEADA")) {
                return ResponseEntity.status(HttpStatus.LOCKED)
                        // HTTP 423 Locked — código específico para cuenta bloqueada
                        .body(Map.of("error",
                                "Tu cuenta ha sido bloqueada por demasiados intentos fallidos. Contacta al administrador."));
            } return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    // HTTP 401 Unauthorized — credenciales incorrectas
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }
    
    
    @PostMapping("/register") // Ruta: http://localhost:8080/auth/register
    public String register() {
        return "Registro desde un endpoint público";
    }
}
