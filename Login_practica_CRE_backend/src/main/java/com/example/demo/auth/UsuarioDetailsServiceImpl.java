package com.example.demo.auth;

import com.example.demo.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsServiceImpl implements UserDetailsService {
    
    private final UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByCorreoElectronico(username)
                .map(u -> {
                    String rol = u.getUsuarioRoles().stream()
                            .findFirst()
                            .map(ur -> ur.getRol().getRolNombre())
                            .orElse("ROLE_EMPLEADO");
                    
                    return org.springframework.security.core.userdetails.User.builder()
                            .username(u.getCorreoElectronico())
                            .password(u.getContrasena())
                            .authorities(rol)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
