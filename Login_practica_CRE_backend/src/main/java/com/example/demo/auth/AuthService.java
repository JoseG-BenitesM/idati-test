package com.example.demo.auth;

import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Este es el método que busca el AuthController
    public String login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return "Bienvenido " + usuario.getUsuarioNombre();
        } else {
            // Línea de depuración para obtener el hash correcto
            System.out.println(">>> HASH PARA LA DB: " + passwordEncoder.encode(request.getContrasena()));
            throw new RuntimeException("Credenciales incorrectas");
        }
    }

    // Método para registrar (Corregido con tus nombres de campos)
    public UsuarioEntity registrarUsuario(String nombre, String correo, String password) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuarioNombre(nombre);
        usuario.setCorreoElectronico(correo);
        usuario.setContrasena(passwordEncoder.encode(password));
        return usuarioRepository.save(usuario);
    }
}