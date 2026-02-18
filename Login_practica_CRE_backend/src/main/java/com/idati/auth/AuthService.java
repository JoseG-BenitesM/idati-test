package com.idati.auth;

import com.idati.usuario.UsuarioEntity;
import com.idati.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; // Este lee el BCrypt

    public String login(LoginRequest request) {
        // Busca en la DB
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // La "magia" donde Spring lee y compara:
        if (passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return "Bienvenido " + usuario.getUsuarioNombre();
        } else {
            throw new RuntimeException("Contraseña incorrecta");
        }
    }
}