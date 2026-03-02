package com.example.demo.auth;

import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    // límite definido
    private static final int MAX_INTENTOS = 3;
    
    public Map<String, String> login(LoginRequestDTO request) {
        
        UsuarioEntity usuario = usuarioRepository.findByCorreoElectronico(request.getCorreoElectronico())
                .or(() -> usuarioRepository.findByUsuarioNombre(request.getCorreoElectronico()))
                .orElseThrow(() -> new RuntimeException("CREDENCIALES_INVALIDAS"));
        
        // Verificar expiración de intentos fallidos (24 horas)
        if(usuario.getIntentosFallidos() > 0 && usuario.getFechaUltimoIntento() != null) {
            LocalDateTime hace24horas = LocalDateTime.now().minusHours(24);
            if(usuario.getFechaUltimoIntento().isBefore(hace24horas)) {
                usuario.setIntentosFallidos((byte) 0);
                usuario.setFechaUltimoIntento(null);
                usuarioRepository.save(usuario);
            }
        }
        
        // Verificar si está bloqueado
        if(! usuario.isActivo()) {
            throw new RuntimeException("CUENTA_BLOQUEADA");
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
            usuario.setFechaUltimoIntento(LocalDateTime.now());
            
            if(intentos >= MAX_INTENTOS) {
                usuario.setEstadoUsuario((byte) 0);
                usuarioRepository.save(usuario);
                throw new RuntimeException("CUENTA_BLOQUEADA");
            }
            
            usuarioRepository.save(usuario);
            throw new RuntimeException("CREDENCIALES_INVALIDAS");
        }
    }
}
