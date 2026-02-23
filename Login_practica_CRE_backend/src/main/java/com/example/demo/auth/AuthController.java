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
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, String> response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            String mensaje = e.getMessage();
            if(mensaje.equals("CUENTA_BLOQUEADA")) {
                return ResponseEntity.status(HttpStatus.LOCKED) // 423
                        .body(Map.of("error", "Tu cuenta ha sido bloqueada por demasiados intentos fallidos. Contacta al administrador."));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED) // 401
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }
    
    @PostMapping("/recuperar")
    public ResponseEntity<Map<String, String>> recuperar(@RequestBody Map<String, String> body) {
        return authService.solicitarRecuperacion(body.get("correo"));
    }
    
    @PostMapping("/restablecer")
    public ResponseEntity<Map<String, String>> restablecer(@RequestBody Map<String, String> body) {
        return authService.restablecerContrasena(body.get("token"), body.get("nuevaContrasena"));
    }
    
    @PostMapping("/register") // Ruta: http://localhost:8080/auth/register
    public String register() {
        return "Registro desde un endpoint público";
    }
}
