package com.example.demo.auth;

import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    // límite definido
    private static final int MAX_INTENTOS = 3;
    
    public Map<String, String> login(LoginRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                .or(() -> usuarioRepository.findByUsuarioNombre(request.getCorreoElectronico()))
                .orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));
        
        if(! usuario.isActivo()) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        if(passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            // Login exitoso — resetear intentos
            usuario.setIntentosFallidos((byte) 0);
            usuario.setFechaUltimoIntento(null);
            usuarioRepository.save(usuario);
            
            String rol = usuario.getUsuarioRoles().stream()
                    .findFirst()
                    .map(ur -> ur.getRol().getRolNombre())
                    .orElse("ROLE_EMPLEADO");
            
            String token = jwtService.generarToken(usuario.getCorreoElectronico(), rol);
            
            return Map.of(
                    "token", token,
                    "usuario", usuario.getUsuarioNombre(),
                    "rol", rol
            );
            
        } else {
            // Login fallido — incrementar intentos
            byte intentos = (byte) (usuario.getIntentosFallidos() + 1);
            usuario.setIntentosFallidos(intentos);
            usuario.setFechaUltimoIntento(java.time.LocalDateTime.now());
            
            if(intentos >= MAX_INTENTOS) {
                usuario.setEstadoUsuario((byte) 0);
                usuarioRepository.save(usuario);
                throw new RuntimeException("CUENTA_BLOQUEADA"); // código interno
            }
            
            usuarioRepository.save(usuario);
            throw new RuntimeException("Credenciales inválidas");
        }
    }
    
    
    public ResponseEntity<Map<String, String>> solicitarRecuperacion(String correo) {
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(correo)
                .or(() -> usuarioRepository.findByUsuarioNombre(correo))
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        // Generar token único
        String token = java.util.UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setFechaTknExpiracion(java.time.LocalDateTime.now().plusMinutes(30));
        usuarioRepository.save(usuario);
        
        // Aquí iría el envío de correo — por ahora devolvemos el token directo para pruebas
        return ResponseEntity.ok(Map.of(
                "mensaje", "Token de recuperación generado",
                "token", token // en producción esto se envía por correo, no aquí
        ));
    }
    
    public ResponseEntity<Map<String, String>> restablecerContrasena(String token, String nuevaContrasena) {
        UsuarioEntity usuario = usuarioRepository.findByTokenRecuperacion(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));
        
        if(usuario.getFechaTknExpiracion().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }
        
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuario.setTokenRecuperacion(null);
        usuario.setFechaTknExpiracion(null);
        usuarioRepository.save(usuario);
        
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }
}
